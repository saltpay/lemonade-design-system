import SwiftUI

// MARK: - Spinner Component

public extension LemonadeUi {
    /// Spinner component for indicating loading state.
    /// Wraps the native iOS ProgressView with a customizable tint color.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Spinner()
    /// LemonadeUi.Spinner(tint: LemonadeTheme.colors.content.contentBrand)
    /// ```
    ///
    /// - Parameter tint: The spinner color. Defaults to `contentSecondary`
    /// - Returns: A styled spinner view
    @ViewBuilder
    static func Spinner(
        tint: Color = LemonadeTheme.colors.content.contentSecondary
    ) -> some View {
        ProgressView()
            .tint(tint)
            .accessibilityLabel("Loading")
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeSpinner_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            LemonadeUi.Spinner()

            LemonadeUi.Spinner(
                tint: LemonadeTheme.colors.content.contentBrand
            )

            LemonadeUi.Spinner(
                tint: LemonadeTheme.colors.content.contentCritical
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
