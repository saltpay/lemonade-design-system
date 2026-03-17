import SwiftUI
import Combine

// MARK: - Toast Animation Constants

/// Animation configuration for toast transitions.
enum ToastAnimationConfig: Sendable {
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
public enum LemonadeToastDuration: Sendable, Equatable {
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
public struct LemonadeToastItem: Identifiable, Equatable, Sendable {
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
