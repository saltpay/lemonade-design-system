import SwiftUI

// MARK: - Button Corner Radius

/// Corner radius style for `LemonadeUi.Button`.
///
/// Use with the `roundedCorner(_:)` modifier to override the per-size corner
/// radius applied by the button. This mirrors the Compose API
/// `Modifier.clip(Shape.Full)` available on Android.
public enum LemonadeButtonCornerRadius: Sendable, Hashable {
    /// Use the per-size default corner radius.
    /// Useful for resetting an override applied higher up in the view hierarchy.
    case `default`
    /// Pill shape — corners are clipped to a full half-height radius.
    case full
}

// MARK: - Environment

private struct LemonadeButtonCornerRadiusKey: EnvironmentKey {
    static let defaultValue: LemonadeButtonCornerRadius? = nil
}

extension EnvironmentValues {
    var lemonadeButtonCornerRadius: LemonadeButtonCornerRadius? {
        get { self[LemonadeButtonCornerRadiusKey.self] }
        set { self[LemonadeButtonCornerRadiusKey.self] = newValue }
    }
}

// MARK: - View Modifier

public extension View {
    /// Overrides the corner radius applied by `LemonadeUi.Button` views in this
    /// hierarchy. No effect on other views.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Button(label: "Pill", onClick: { })
    ///     .roundedCorner(.full)
    /// ```
    func roundedCorner(_ radius: LemonadeButtonCornerRadius) -> some View {
        environment(\.lemonadeButtonCornerRadius, radius)
    }
}
