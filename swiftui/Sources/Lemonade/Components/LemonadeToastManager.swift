import SwiftUI
import Combine

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

    /// Queue of pending toasts.
    private var pendingToasts: [LemonadeToastItem] = []

    /// Timer for auto-dismissal.
    private var dismissTask: Task<Void, Never>?

    /// Animation duration for toast transitions.
    private let animationDuration: TimeInterval = 0.3
    
    private let newToastDelay: UInt64 = 300_000_000

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
            scheduleTransition()
        } else {
            displayToast(toast)
        }
    }

    /// Dismisses the current toast.
    public func dismiss() {
        dismissTask?.cancel()
        dismissTask = nil

        withAnimation(.easeInOut(duration: animationDuration)) {
            currentToast = nil
        }

        // Show next toast after animation completes
        Task { @MainActor in
            try? await Task.sleep(nanoseconds: UInt64(animationDuration * 1_000_000_000))
            showNextToastIfAvailable()
        }
    }

    /// Displays a toast immediately.
    private func displayToast(_ toast: LemonadeToastItem) {
        dismissTask?.cancel()

        withAnimation(.easeInOut(duration: animationDuration)) {
            currentToast = toast
        }

        scheduleAutoDismiss(after: toast.duration.timeInterval)
    }

    /// Schedules auto-dismissal of the current toast.
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
            try? await Task.sleep(nanoseconds: newToastDelay) // 300ms
            guard !Task.isCancelled else { return }
            dismiss()
        }
    }

    /// Shows the next pending toast if available.
    private func showNextToastIfAvailable() {
        guard !pendingToasts.isEmpty else { return }
        let nextToast = pendingToasts.removeFirst()
        displayToast(nextToast)
    }
}

// MARK: - Toast Container View

/// Internal view that displays the toast overlay.
private struct LemonadeToastContainerView<Content: View>: View {
    @StateObject private var toastManager = LemonadeToastManager()
    @StateObject private var keyboardObserver = KeyboardObserver()
    @State private var dragOffset: CGFloat = 0

    let content: Content

    var body: some View {
        content
            .environmentObject(toastManager)
            .overlay(alignment: .bottom) {
                toastOverlay
            }
    }

    @ViewBuilder
    private var toastOverlay: some View {
        if let toast = toastManager.currentToast {
            GeometryReader { geometry in
                let keyboardHeight = keyboardObserver.keyboardHeight

                VStack {
                    Spacer()
                    LemonadeUi.Toast(
                        label: toast.label,
                        voice: toast.voice,
                        icon: toast.icon
                    )
                    .id(toast.id)
                    .offset(y: dragOffset)
                    .gesture(dismissGesture(for: toast))
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                    .padding(.horizontal, LemonadeTheme.spaces.spacing200)
                    .padding(.bottom, keyboardHeight)
                    .onAppear {
                        dragOffset = 0
                    }
                }
                .frame(maxWidth: .infinity)
            }
            .ignoresSafeArea(.keyboard)
        }
    }

    private func dismissGesture(for toast: LemonadeToastItem) -> some Gesture {
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
                if value.translation.height > 50 {
                    toastManager.dismiss()
                }
                withAnimation(.easeInOut(duration: 0.2)) {
                    dragOffset = 0
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
