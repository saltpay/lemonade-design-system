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

// MARK: - Minimum Tappable Frame

extension View {
    /// Grows the view's layout frame to at least `minSize` on each side, centered on the
    /// content. Use on icon-only button labels whose rendered size is smaller than the
    /// platform's minimum touch target, so the enclosing `Button`'s tappable area grows
    /// with it instead of matching the icon's small visual bounds.
    ///
    /// ```swift
    /// Button(action: onClick) {
    ///     LemonadeUi.Icon(icon: .times, contentDescription: "Close")
    /// }
    /// .clickableFrame(minSize: .size800)
    /// ```
    func clickableFrame(minSize: LemonadeSizes) -> some View {
        frame(minWidth: minSize.value, minHeight: minSize.value)
    }
}
