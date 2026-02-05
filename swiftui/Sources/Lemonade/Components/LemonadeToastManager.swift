import SwiftUI
import Combine

// MARK: - Toast Animation Constants

/// Animation configuration for toast transitions.
private enum ToastAnimationConfig {
    /// Duration for entry/exit animations
    static let duration: TimeInterval = 0.35

    /// Spring animation for natural feel
    static var spring: Animation {
        .spring(response: duration, dampingFraction: 0.8)
    }

    /// Smooth animation for fading effects
    static var smooth: Animation {
        .easeInOut(duration: duration)
    }

    /// Interactive spring for drag gestures
    static var interactiveSpring: Animation {
        .spring(response: 0.3, dampingFraction: 0.7)
    }

    /// Fallback slide offset if toast height not yet measured
    static let fallbackSlideOffset: CGFloat = 200

    /// Fade exit scale
    static let fadeScale: CGFloat = 0.88

    /// Fade exit blur radius
    static let fadeBlur: CGFloat = 10

    /// Drag threshold to trigger dismiss
    static let dragDismissThreshold: CGFloat = 25

    /// Convert seconds to nanoseconds for Task.sleep
    static func nanoseconds(from seconds: TimeInterval) -> UInt64 {
        UInt64(seconds * 1_000_000_000)
    }
}

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

    /// Queue of pending toasts.
    private var pendingToasts: [LemonadeToastItem] = []

    /// Timer for auto-dismissal.
    private var dismissTask: Task<Void, Never>?

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

        let hadPending = !pendingToasts.isEmpty

        if hadPending {
            // Show next toast immediately (overlapping animations)
            showNextToastIfAvailable()
        } else {
            currentToast = nil
        }
    }

    /// Displays a toast immediately.
    private func displayToast(_ toast: LemonadeToastItem) {
        dismissTask?.cancel()
        currentToast = toast

        // Wait for entry animation to complete, then start visibility timer
        let totalDelay = ToastAnimationConfig.duration + toast.duration.timeInterval
        scheduleAutoDismiss(after: totalDelay)
    }

    /// Schedules auto-dismissal of the current toast.
    /// - Parameter delay: Total delay including entry animation time.
    private func scheduleAutoDismiss(after delay: TimeInterval) {
        dismissTask = Task { @MainActor in
            try? await Task.sleep(nanoseconds: ToastAnimationConfig.nanoseconds(from: delay))
            guard !Task.isCancelled else { return }
            dismiss()
        }
    }

    /// Schedules transition to pending toast.
    private func scheduleTransition() {
        dismissTask?.cancel()
        dismissTask = Task { @MainActor in
            try? await Task.sleep(nanoseconds: ToastAnimationConfig.nanoseconds(from: 0.1)) // 100ms
            guard !Task.isCancelled else { return }
            dismiss()
        }
    }

    /// Shows the next pending toast if available.
    private func showNextToastIfAvailable() {
        guard !pendingToasts.isEmpty else {
            return
        }
        let nextToast = pendingToasts.removeFirst()
        displayToast(nextToast)
    }
}

// MARK: - Toast Animation Phase

/// Represents the animation state of a toast.
/// Using an enum ensures clear state transitions and predictable animations.
private enum ToastAnimationPhase: Equatable {
    /// Toast is off-screen, waiting to enter
    case hidden
    /// Toast is animating in from the bottom
    case entering
    /// Toast is fully visible on screen
    case visible
    /// Toast is sliding out to the bottom (natural dismissal)
    case exitingSlide
    /// Toast is fading out with scale/blur (being replaced)
    case exitingFade

    /// The Y offset for this phase
    /// - Parameter toastHeight: The measured height of the toast (used for slide distance)
    func offset(for toastHeight: CGFloat) -> CGFloat {
        let slideDistance = toastHeight > 0 ? toastHeight : ToastAnimationConfig.fallbackSlideOffset
        switch self {
        case .hidden, .entering, .exitingSlide:
            return slideDistance
        case .visible, .exitingFade:
            return 0
        }
    }

    /// The scale for this phase
    var scale: CGFloat {
        switch self {
        case .exitingFade:
            return ToastAnimationConfig.fadeScale
        default:
            return 1.0
        }
    }

    /// The opacity for this phase
    var opacity: Double {
        switch self {
        case .exitingFade:
            return 0
        default:
            return 1.0
        }
    }

    /// The blur radius for this phase
    var blur: CGFloat {
        switch self {
        case .exitingFade:
            return ToastAnimationConfig.fadeBlur
        default:
            return 0
        }
    }

    /// Whether the toast should be visible in the view hierarchy
    var isPresented: Bool {
        switch self {
        case .hidden:
            return false
        default:
            return true
        }
    }
}

// MARK: - Toast Container View

/// Internal view that displays the toast overlay.
/// Uses explicit animation state management for predictable transitions.
private struct LemonadeToastContainerView<Content: View>: View {
    @StateObject private var toastManager = LemonadeToastManager()

    /// The toast currently being displayed (may differ from manager during transitions)
    @State private var displayedToast: LemonadeToastItem?
    /// Current animation phase
    @State private var animationPhase: ToastAnimationPhase = .hidden
    /// Drag offset for swipe-to-dismiss gesture
    @State private var dragOffset: CGFloat = 0
    /// Trigger counter for sensory feedback (iOS 17+)
    @State private var feedbackTrigger: Int = 0
    /// Measured toast height for accurate slide animation
    @State private var toastHeight: CGFloat = 0

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
            .modifier(ToastSensoryFeedbackModifier(
                trigger: feedbackTrigger,
                voice: displayedToast?.voice
            ))
    }

    // MARK: - Toast Change Handling

    private func handleToastChange(newToastId: UUID?) {
        if newToastId != nil {
            // New toast requested
            if displayedToast != nil {
                // Replace current toast: fade out, then show new
                exitCurrentToast(withFade: true) {
                    enterNewToast()
                }
            } else {
                // No current toast: show immediately
                enterNewToast()
            }
        } else {
            // Dismissal requested (no replacement)
            if displayedToast != nil {
                exitCurrentToast(withFade: false, completion: nil)
            }
        }
    }

    private func enterNewToast() {
        // Set up the new toast in entering state
        displayedToast = toastManager.currentToast
        animationPhase = .entering

        // Trigger sensory feedback (iOS 17+)
        feedbackTrigger += 1

        // Animate to visible - use spring for natural feel
        withAnimation(ToastAnimationConfig.spring) {
            animationPhase = .visible
        }
    }

    private func exitCurrentToast(withFade: Bool, completion: (() -> Void)?) {
        let exitPhase: ToastAnimationPhase = withFade ? .exitingFade : .exitingSlide
        let animation: Animation = withFade ? ToastAnimationConfig.smooth : ToastAnimationConfig.spring

        withAnimation(animation) {
            animationPhase = exitPhase
        }

        // Clean up after animation completes
        Task { @MainActor in
            try? await Task.sleep(nanoseconds: ToastAnimationConfig.nanoseconds(from: ToastAnimationConfig.duration))
            displayedToast = nil
            animationPhase = .hidden
            completion?()
        }
    }

    // MARK: - Toast Overlay

    @ViewBuilder
    private var toastOverlay: some View {
        if let toast = displayedToast, animationPhase.isPresented {
            ToastItemView(
                toast: toast,
                onDismiss: { toastManager.dismiss() },
                onDragChanged: handleDragChanged,
                onDragEnded: handleDragEnded,
                onHeightChanged: { height in
                    toastHeight = height
                }
            )
            // GPU-accelerated transforms for animation
            .scaleEffect(animationPhase.scale, anchor: .bottom)
            // Animation offset (based on measured height) + drag offset
            .offset(y: animationPhase.offset(for: toastHeight) + dragOffset)
            .animatableBlur(radius: animationPhase.blur)
            .opacity(animationPhase.opacity)
            // Animate phase changes
            .animation(ToastAnimationConfig.spring, value: animationPhase)
            // Separate animation for drag (snappier)
            .animation(ToastAnimationConfig.interactiveSpring, value: dragOffset)
        }
    }

    // MARK: - Drag Gesture Handling

    private func handleDragChanged(_ translation: CGFloat) {
        // Only allow downward drag (positive translation)
        dragOffset = max(0, translation)
    }

    private func handleDragEnded(_ translation: CGFloat) {
        if translation > ToastAnimationConfig.dragDismissThreshold {
            // Dismiss the toast
            toastManager.dismiss()
        }
        // Reset drag offset with spring animation
        dragOffset = 0
    }
}

// MARK: - Toast Item View

/// Individual toast view with gesture handling.
/// Only the toast itself is interactive - the rest passes through touches.
private struct ToastItemView: View {
    let toast: LemonadeToastItem
    let onDismiss: () -> Void
    let onDragChanged: (CGFloat) -> Void
    let onDragEnded: (CGFloat) -> Void
    let onHeightChanged: (CGFloat) -> Void

    var body: some View {
        VStack {
            Spacer()
                .allowsHitTesting(false) // Allow touches to pass through

            toastContent
        }
        .frame(maxWidth: .infinity, alignment: .bottom)
        .allowsHitTesting(true)
        .id(toast.id)
    }

    private var toastContent: some View {
        LemonadeUi.Toast(
            label: toast.label,
            voice: toast.voice,
            icon: toast.icon
        )
        .frame(maxWidth: .infinity)
        .padding(.top, .space.spacing1800) // Extra space for dismiss gesture
        .padding(.horizontal, .space.spacing200)
        .padding(.bottom, .space.spacing400)
        .background(
            GeometryReader { geometry in
                Color.clear
                    .onAppear {
                        onHeightChanged(geometry.size.height)
                    }
                    .onChange(of: geometry.size.height) { newHeight in
                        onHeightChanged(newHeight)
                    }
            }
        )
        .contentShape(Rectangle())
        .gesture(dismissGesture)
    }

    private var dismissGesture: some Gesture {
        DragGesture()
            .onChanged { value in
                guard toast.isDismissible else { return }
                onDragChanged(value.translation.height)
            }
            .onEnded { value in
                guard toast.isDismissible else { return }
                onDragEnded(value.translation.height)
            }
    }
}

// MARK: - Sensory Feedback Modifier

/// A modifier that provides sensory feedback when a toast appears.
/// Uses iOS 17+ SensoryFeedback API with graceful fallback for older versions.
private struct ToastSensoryFeedbackModifier: ViewModifier {
    let trigger: Int
    let voice: LemonadeToastVoice?

    func body(content: Content) -> some View {
        if #available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *) {
            content
                .sensoryFeedback(trigger: trigger) { _, _ in
                    voice?.sensoryFeedback
                }
        } else {
            content
        }
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
