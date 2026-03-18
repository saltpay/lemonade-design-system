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
