import SwiftUI
import Combine

/// Animation duration for toast transitions.
private let toastAnimationDuration: TimeInterval = 0.4

// MARK: - Toast Duration

/// Predefined duration values for toast notifications.
public enum LemonadeToastDuration {
    /// Short duration: 3 seconds
    case short
    /// Medium duration: 6 seconds
    case medium
    /// Long duration: 9 seconds
    case long
    /// Custom duration
    case custom(TimeInterval)

    public var timeInterval: TimeInterval {
        switch self {
        case .short: return 3
        case .medium: return 6
        case .long: return 9
        case .custom(let duration): return duration
        }
    }
}

// MARK: - Toast Item

/// Represents a toast notification to be displayed.
public struct LemonadeToastItem: Identifiable, Equatable {
    public let id: UUID
    public let label: String
    public let voice: LemonadeToastVoice
    public let icon: LemonadeIcon?
    public let duration: LemonadeToastDuration
    public let isDismissible: Bool
    
    public init(
        id: UUID = UUID(),
        label: String,
        voice: LemonadeToastVoice = .neutral,
        icon: LemonadeIcon? = nil,
        duration: LemonadeToastDuration = .short,
        isDismissible: Bool = true
    ) {
        self.id = id
        self.label = label
        self.voice = voice
        self.icon = icon
        self.duration = duration
        self.isDismissible = isDismissible
    }
    
    public static func == (lhs: LemonadeToastItem, rhs: LemonadeToastItem) -> Bool {
        lhs.id == rhs.id
    }
}

// MARK: - Toast Manager

/// A manager for displaying toast notifications.
///
/// Use the environment object to show toasts from any view in your hierarchy.
///
/// ## Setup
/// Add the toast container modifier to your root view:
/// ```swift
/// ContentView()
///     .lemonadeToastContainer()
/// ```
///
/// ## Usage
/// ```swift
/// struct MyView: View {
///     @EnvironmentObject private var toastManager: LemonadeToastManager
///
///     var body: some View {
///         Button("Show Toast") {
///             toastManager.show(
///                 label: "Changes saved",
///                 voice: .success
///             )
///         }
///     }
/// }
/// ```
@MainActor
public final class LemonadeToastManager: ObservableObject {
    /// The currently displayed toast, if any.
    @Published public private(set) var currentToast: LemonadeToastItem?

    /// Whether there's a pending toast (used for exit animation).
    @Published public private(set) var hasPendingToast: Bool = false

    /// Whether the current toast should fade out when exiting.
    /// True when being replaced by another toast, false for natural exit.
    @Published public private(set) var shouldFadeOut: Bool = false

    /// Queue of pending toasts.
    private var pendingToasts: [LemonadeToastItem] = []

    /// Timer for auto-dismissal.
    private var dismissTask: Task<Void, Never>?

    /// Delay to display a new Toast if there is a Toast visible
    private let newToastDelay: UInt64 = 100_000_000

    public init() {}
    
    /// Shows a toast notification.
    ///
    /// - Parameters:
    ///   - label: The message to display.
    ///   - voice: The toast variant (success, error, neutral).
    ///   - icon: Custom icon for neutral toasts only.
    ///   - duration: How long the toast should be visible.
    ///   - dismissible: Whether the toast can be dismissed by swiping.
    public func show(
        label: String,
        voice: LemonadeToastVoice = .neutral,
        icon: LemonadeIcon? = nil,
        duration: LemonadeToastDuration = .short,
        dismissible: Bool = true
    ) {
        let toast = LemonadeToastItem(
            label: label,
            voice: voice,
            icon: icon,
            duration: duration,
            isDismissible: dismissible
        )
        
        if currentToast != nil {
            // Queue the new toast and dismiss current after delay
            pendingToasts.append(toast)
            hasPendingToast = true
            shouldFadeOut = true  // Current toast will fade out when replaced
            scheduleTransition()
        } else {
            displayToast(toast)
        }
    }
    
    /// Dismisses the current toast.
    public func dismiss() {
        dismissTask?.cancel()
        dismissTask = nil

        let hadPending = !pendingToasts.isEmpty
        hasPendingToast = hadPending

        if hadPending {
            // Show next toast immediately (overlapping animations)
            showNextToastIfAvailable()
        } else {
            // No pending toast - natural exit with slide
            shouldFadeOut = false
            withAnimation(.easeInOut(duration: toastAnimationDuration)) {
                currentToast = nil
            }

            Task { @MainActor in
                try? await Task.sleep(nanoseconds: UInt64(toastAnimationDuration * 1_000_000_000))
                hasPendingToast = false
            }
        }
    }
    
    /// Displays a toast immediately.
    private func displayToast(_ toast: LemonadeToastItem) {
        dismissTask?.cancel()

        withAnimation(.easeInOut(duration: toastAnimationDuration)) {
            currentToast = toast
        }

        // Wait for entry animation to complete, then start visibility timer
        let totalDelay = toastAnimationDuration + toast.duration.timeInterval
        scheduleAutoDismiss(after: totalDelay)
    }

    /// Schedules auto-dismissal of the current toast.
    /// - Parameter delay: Total delay including entry animation time.
    private func scheduleAutoDismiss(after delay: TimeInterval) {
        dismissTask = Task { @MainActor in
            try? await Task.sleep(nanoseconds: UInt64(delay * 1_000_000_000))
            guard !Task.isCancelled else { return }
            dismiss()
        }
    }
    
    /// Schedules transition to pending toast.
    private func scheduleTransition() {
        dismissTask?.cancel()
        dismissTask = Task { @MainActor in
            try? await Task.sleep(nanoseconds: newToastDelay)
            guard !Task.isCancelled else { return }
            dismiss()
        }
    }
    
    /// Shows the next pending toast if available.
    private func showNextToastIfAvailable() {
        guard !pendingToasts.isEmpty else {
            hasPendingToast = false
            return
        }
        let nextToast = pendingToasts.removeFirst()
        hasPendingToast = false
        displayToast(nextToast)
    }
}

// MARK: - Toast Animation State

/// Represents the animation state of a toast.
private enum ToastAnimationPhase {
    case entering
    case visible
    case exitingWithSlide
    case exitingWithFade
    case hidden
}

// MARK: - Toast Container View

/// Internal view that displays the toast overlay.
private struct LemonadeToastContainerView<Content: View>: View {
    @StateObject private var toastManager = LemonadeToastManager()
    @StateObject private var keyboardObserver = KeyboardObserver()
    @State private var dragOffset: CGFloat = 0
    @State private var displayedToast: LemonadeToastItem?
    @State private var animationPhase: ToastAnimationPhase = .hidden

    let content: Content

    var body: some View {
        content
            .environmentObject(toastManager)
            .overlay(alignment: .bottom) {
                toastOverlay
            }
            .onChange(of: toastManager.currentToast?.id) { newToastId in
                handleToastChange(newToastId: newToastId)
            }
    }

    private func handleToastChange(newToastId: UUID?) {
        if newToastId != nil {
            // A new toast is being shown
            if displayedToast != nil {
                // There's a current toast - exit with fade, then show new
                exitCurrentToast(withFade: true) {
                    showNewToast()
                }
            } else {
                // No current toast - just show the new one
                showNewToast()
            }
        } else {
            // Toast is being dismissed (no replacement)
            if displayedToast != nil {
                exitCurrentToast(withFade: false, completion: nil)
            }
        }
    }

    private func showNewToast() {
        displayedToast = toastManager.currentToast
        animationPhase = .entering

        withAnimation(.easeOut(duration: toastAnimationDuration)) {
            animationPhase = .visible
        }
    }

    private func exitCurrentToast(withFade: Bool, completion: (() -> Void)?) {
        let exitPhase: ToastAnimationPhase = withFade ? .exitingWithFade : .exitingWithSlide

        withAnimation(.easeIn(duration: toastAnimationDuration)) {
            animationPhase = exitPhase
        }

        // After animation completes, clean up and run completion
        Task { @MainActor in
            try? await Task.sleep(nanoseconds: UInt64(toastAnimationDuration * 1_000_000_000))
            displayedToast = nil
            animationPhase = .hidden
            completion?()
        }
    }

    @ViewBuilder
    private var toastOverlay: some View {
        if let toast = displayedToast {
            ToastItemView(
                toast: toast,
                dragOffset: $dragOffset,
                keyboardHeight: keyboardObserver.keyboardHeight,
                hasPendingToast: toastManager.hasPendingToast,
                onDismiss: { toastManager.dismiss() }
            )
            .scaleEffect(scaleForPhase, anchor: .bottom)
            .opacity(opacityForPhase)
            .blur(radius: blurForPhase)
            .offset(y: offsetForPhase)
        }
    }

    private var scaleForPhase: CGFloat {
        switch animationPhase {
        case .exitingWithFade:
            return 0.88
        default:
            return 1.0
        }
    }

    private var opacityForPhase: Double {
        switch animationPhase {
        case .entering, .exitingWithSlide:
            return 1.0
        case .visible:
            return 1.0
        case .exitingWithFade, .hidden:
            return animationPhase == .hidden ? 1.0 : 0.0
        }
    }

    private var blurForPhase: CGFloat {
        switch animationPhase {
        case .exitingWithFade:
            return 10.0
        default:
            return 0.0
        }
    }

    private var offsetForPhase: CGFloat {
        switch animationPhase {
        case .entering:
            return 200  // Start off-screen
        case .visible, .exitingWithFade:
            return 0
        case .exitingWithSlide:
            return 200  // Slide off-screen
        case .hidden:
            return 200
        }
    }
}

// MARK: - Toast Item View

/// Individual toast view with gesture handling.
private struct ToastItemView: View {
    let toast: LemonadeToastItem
    @Binding var dragOffset: CGFloat
    let keyboardHeight: CGFloat
    let hasPendingToast: Bool
    let onDismiss: () -> Void
    
    var body: some View {
        GeometryReader { _ in
            VStack {
                Spacer()
                LemonadeUi.Toast(
                    label: toast.label,
                    voice: toast.voice,
                    icon: toast.icon
                )
                .frame(maxWidth: .infinity)
                .padding(
                    EdgeInsets(
                        top: .space.spacing2000,
                        leading: .space.spacing200,
                        bottom: keyboardHeight,
                        trailing: .space.spacing200
                    )
                )
                .contentShape(Rectangle())
            }
            .frame(maxWidth: .infinity)
            .offset(y: dragOffset)

            .gesture(dismissGesture)
        }
        .ignoresSafeArea(.keyboard)
        .onAppear {
            dragOffset = 0
        }
        .id(toast.id)
    }
    
    private var dismissGesture: some Gesture {
        DragGesture()
            .onChanged { value in
                guard toast.isDismissible else { return }
                // Only allow downward drag
                if value.translation.height > 0 {
                    dragOffset = value.translation.height
                }
            }
            .onEnded { value in
                guard toast.isDismissible else { return }
                // Dismiss if dragged down enough
                if value.translation.height > 25 {
                    onDismiss()
                }
                withAnimation(.easeInOut(duration: toastAnimationDuration)) {
                    
                }
            }
    }
}

// MARK: - Keyboard Observer

/// Observes keyboard visibility and height changes.
private final class KeyboardObserver: ObservableObject {
    @Published var keyboardHeight: CGFloat = 0
    
    private var cancellables = Set<AnyCancellable>()
    
    init() {
        #if os(iOS)
        NotificationCenter.default
            .publisher(for: UIResponder.keyboardWillShowNotification)
            .compactMap { notification -> CGFloat? in
                (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect)?.height
            }
            .receive(on: DispatchQueue.main)
            .sink { [weak self] height in
                withAnimation(.easeInOut(duration: 0.25)) {
                    self?.keyboardHeight = height
                }
            }
            .store(in: &cancellables)
        
        NotificationCenter.default
            .publisher(for: UIResponder.keyboardWillHideNotification)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] _ in
                withAnimation(.easeInOut(duration: 0.25)) {
                    self?.keyboardHeight = 0
                }
            }
            .store(in: &cancellables)
        #endif
    }
}

// MARK: - View Extension

public extension View {
    /// Adds a toast container to this view hierarchy.
    ///
    /// Apply this modifier to your root view to enable toast notifications.
    ///
    /// ## Usage
    /// ```swift
    /// @main
    /// struct MyApp: App {
    ///     var body: some Scene {
    ///         WindowGroup {
    ///             ContentView()
    ///                 .lemonadeToastContainer()
    ///         }
    ///     }
    /// }
    /// ```
    func lemonadeToastContainer() -> some View {
        LemonadeToastContainerView(content: self)
    }
}
