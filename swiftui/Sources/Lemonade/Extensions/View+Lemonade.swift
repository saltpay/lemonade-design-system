import SwiftUI

// MARK: - Localization Helper

/// Internal helper for localized strings within the Lemonade library bundle.
func lemonadeLocalizedString(_ key: String) -> String {
    NSLocalizedString(key, bundle: .lemonade, comment: "")
}

/// Internal helper for localized strings with format arguments within the Lemonade library bundle.
func lemonadeLocalizedString(_ key: String, _ arguments: CVarArg...) -> String {
    let format = NSLocalizedString(key, bundle: .lemonade, comment: "")
    return String(format: format, arguments: arguments)
}

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
