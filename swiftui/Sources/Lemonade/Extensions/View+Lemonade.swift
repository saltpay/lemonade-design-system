import SwiftUI

// MARK: - Conditional View Modifier

extension View {
    /// Applies the given transform only when the condition is `true`.
    ///
    /// ```swift
    /// myView
    ///     .applyIf(stretched) { $0.frame(maxWidth: .infinity) }
    /// ```
    @ViewBuilder
    func applyIf<T: View>(_ condition: Bool, transform: (Self) -> T) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}

// MARK: - Selection Haptic

extension View {
    /// Plays the selection haptic whenever `trigger` changes.
    ///
    /// Uses a heavy `impact` rather than `.selection`: the plain selection tick reads too
    /// faintly for a selection commit. Firing on the value change rather than on tap means a
    /// programmatic selection change buzzes too, and a tap that does not change the selection
    /// stays silent.
    ///
    /// Shared by every component that presents a selectable surface, so the feel of "this was
    /// selected" is defined in one place. The Compose counterpart is `SelectionHapticEffect`.
    ///
    /// Relies on the iOS 17+ `sensoryFeedback` API with a graceful no-op fallback on older
    /// versions, matching `ToastSensoryFeedbackModifier`.
    @ViewBuilder
    func selectionImpactFeedback(trigger: Bool) -> some View {
        if #available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *) {
            sensoryFeedback(.impact(weight: .heavy), trigger: trigger)
        } else {
            self
        }
    }
}
