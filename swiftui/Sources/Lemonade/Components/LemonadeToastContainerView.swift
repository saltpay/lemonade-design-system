import SwiftUI

// MARK: - Toast Animation Phase

/// Represents the animation state of a toast.
/// Using an enum ensures clear state transitions and predictable animations.
enum ToastAnimationPhase: Equatable {
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
struct LemonadeToastContainerView<Content: View>: View {
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
    /// Tracked exit task to prevent race conditions from fire-and-forget Tasks
    @State private var exitTask: Task<Void, Never>?

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

        // Cancel any previously scheduled exit task to prevent races
        exitTask?.cancel()

        // Clean up after animation completes
        exitTask = Task { @MainActor in
            try? await Task.sleep(nanoseconds: ToastAnimationConfig.nanoseconds(from: ToastAnimationConfig.duration))
            guard !Task.isCancelled else { return }
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
struct ToastItemView: View {
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
