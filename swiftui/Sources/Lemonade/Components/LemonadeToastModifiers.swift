import SwiftUI

// MARK: - Sensory Feedback Modifier

/// A modifier that provides sensory feedback when a toast appears.
/// Uses iOS 17+ SensoryFeedback API with graceful fallback for older versions.
struct ToastSensoryFeedbackModifier: ViewModifier {
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
