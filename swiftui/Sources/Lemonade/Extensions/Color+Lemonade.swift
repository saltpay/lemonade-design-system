import SwiftUI

// MARK: - Color Namespace Extensions

/// Enables shorthand color access like `.content.contentPrimary`
/// Usage:
/// ```swift
/// Text("Hello")
///     .foregroundStyle(.content.contentPrimary)
///     .background(.bg.bgDefault)
/// ```

// MARK: - Content Colors Namespace

public struct LemonadeContentColorsShorthand {
    public var contentPrimary: Color { LemonadeTheme.colors.content.contentPrimary }
    public var contentSecondary: Color { LemonadeTheme.colors.content.contentSecondary }
    public var contentTertiary: Color { LemonadeTheme.colors.content.contentTertiary }
    public var contentPrimaryInverse: Color { LemonadeTheme.colors.content.contentPrimaryInverse }
    public var contentSecondaryInverse: Color { LemonadeTheme.colors.content.contentSecondaryInverse }
    public var contentTertiaryInverse: Color { LemonadeTheme.colors.content.contentTertiaryInverse }
    public var contentBrand: Color { LemonadeTheme.colors.content.contentBrand }
    public var contentBrandInverse: Color { LemonadeTheme.colors.content.contentBrandInverse }
    public var contentBrandHigh: Color { LemonadeTheme.colors.content.contentBrandHigh }
    public var contentOnBrandLow: Color { LemonadeTheme.colors.content.contentOnBrandLow }
    public var contentOnBrandHigh: Color { LemonadeTheme.colors.content.contentOnBrandHigh }
    public var contentCritical: Color { LemonadeTheme.colors.content.contentCritical }
    public var contentCaution: Color { LemonadeTheme.colors.content.contentCaution }
    public var contentPositive: Color { LemonadeTheme.colors.content.contentPositive }
    public var contentInfo: Color { LemonadeTheme.colors.content.contentInfo }
    public var contentNeutral: Color { LemonadeTheme.colors.content.contentNeutral }
    public var contentAlwaysLight: Color { LemonadeTheme.colors.content.contentAlwaysLight }
    public var contentAlwaysDark: Color { LemonadeTheme.colors.content.contentAlwaysDark }
    public var contentCautionOnColor: Color { LemonadeTheme.colors.content.contentCautionOnColor }
    public var contentInfoOnColor: Color { LemonadeTheme.colors.content.contentInfoOnColor }
    public var contentPositiveOnColor: Color { LemonadeTheme.colors.content.contentPositiveOnColor }
    public var contentNeutralOnColor: Color { LemonadeTheme.colors.content.contentNeutralOnColor }
}

// MARK: - Background Colors Namespace

public struct LemonadeBackgroundColorsShorthand {
    public var bgDefault: Color { LemonadeTheme.colors.background.bgDefault }
    public var bgDefaultInverse: Color { LemonadeTheme.colors.background.bgDefaultInverse }
    public var bgSubtle: Color { LemonadeTheme.colors.background.bgSubtle }
    public var bgSubtleInverse: Color { LemonadeTheme.colors.background.bgSubtleInverse }
    public var bgElevated: Color { LemonadeTheme.colors.background.bgElevated }
    public var bgElevatedInverse: Color { LemonadeTheme.colors.background.bgElevatedInverse }
    public var bgElevatedHigh: Color { LemonadeTheme.colors.background.bgElevatedHigh }
    public var bgBrand: Color { LemonadeTheme.colors.background.bgBrand }
    public var bgBrandHigh: Color { LemonadeTheme.colors.background.bgBrandHigh }
    public var bgBrandSubtle: Color { LemonadeTheme.colors.background.bgBrandSubtle }
    public var bgBrandElevated: Color { LemonadeTheme.colors.background.bgBrandElevated }
    public var bgCritical: Color { LemonadeTheme.colors.background.bgCritical }
    public var bgCriticalSubtle: Color { LemonadeTheme.colors.background.bgCriticalSubtle }
    public var bgCaution: Color { LemonadeTheme.colors.background.bgCaution }
    public var bgCautionSubtle: Color { LemonadeTheme.colors.background.bgCautionSubtle }
    public var bgPositive: Color { LemonadeTheme.colors.background.bgPositive }
    public var bgPositiveSubtle: Color { LemonadeTheme.colors.background.bgPositiveSubtle }
    public var bgInfo: Color { LemonadeTheme.colors.background.bgInfo }
    public var bgInfoSubtle: Color { LemonadeTheme.colors.background.bgInfoSubtle }
    public var bgNeutral: Color { LemonadeTheme.colors.background.bgNeutral }
    public var bgNeutralSubtle: Color { LemonadeTheme.colors.background.bgNeutralSubtle }
    public var bgAlwaysLight: Color { LemonadeTheme.colors.background.bgAlwaysLight }
    public var bgAlwaysDark: Color { LemonadeTheme.colors.background.bgAlwaysDark }
}

// MARK: - Border Colors Namespace

public struct LemonadeBorderColorsShorthand {
    public var borderNeutralLow: Color { LemonadeTheme.colors.border.borderNeutralLow }
    public var borderNeutralMedium: Color { LemonadeTheme.colors.border.borderNeutralMedium }
    public var borderNeutralHigh: Color { LemonadeTheme.colors.border.borderNeutralHigh }
    public var borderNeutralLowInverse: Color { LemonadeTheme.colors.border.borderNeutralLowInverse }
    public var borderNeutralMediumInverse: Color { LemonadeTheme.colors.border.borderNeutralMediumInverse }
    public var borderNeutralHighInverse: Color { LemonadeTheme.colors.border.borderNeutralHighInverse }
    public var borderSelected: Color { LemonadeTheme.colors.border.borderSelected }
    public var borderSelectedInverse: Color { LemonadeTheme.colors.border.borderSelectedInverse }
    public var borderBrand: Color { LemonadeTheme.colors.border.borderBrand }
    public var borderBrandInverse: Color { LemonadeTheme.colors.border.borderBrandInverse }
    public var borderCritical: Color { LemonadeTheme.colors.border.borderCritical }
    public var borderCriticalSubtle: Color { LemonadeTheme.colors.border.borderCriticalSubtle }
    public var borderCaution: Color { LemonadeTheme.colors.border.borderCaution }
    public var borderCautionSubtle: Color { LemonadeTheme.colors.border.borderCautionSubtle }
    public var borderPositive: Color { LemonadeTheme.colors.border.borderPositive }
    public var borderPositiveSubtle: Color { LemonadeTheme.colors.border.borderPositiveSubtle }
    public var borderInfo: Color { LemonadeTheme.colors.border.borderInfo }
    public var borderInfoSubtle: Color { LemonadeTheme.colors.border.borderInfoSubtle }
    public var borderOnBrandLow: Color { LemonadeTheme.colors.border.borderOnBrandLow }
    public var borderOnBrandMedium: Color { LemonadeTheme.colors.border.borderOnBrandMedium }
    public var borderOnBrandHigh: Color { LemonadeTheme.colors.border.borderOnBrandHigh }
    public var borderAlwaysLight: Color { LemonadeTheme.colors.border.borderAlwaysLight }
    public var borderAlwaysDark: Color { LemonadeTheme.colors.border.borderAlwaysDark }
}

// MARK: - Interaction Colors Namespace

public struct LemonadeInteractionColorsShorthand {
    public var bgDefaultInteractive: Color { LemonadeTheme.colors.interaction.bgDefaultInteractive }
    public var bgDefaultPressed: Color { LemonadeTheme.colors.interaction.bgDefaultPressed }
    public var bgSubtleInteractive: Color { LemonadeTheme.colors.interaction.bgSubtleInteractive }
    public var bgElevatedInteractive: Color { LemonadeTheme.colors.interaction.bgElevatedInteractive }
    public var bgElevatedPressed: Color { LemonadeTheme.colors.interaction.bgElevatedPressed }
    public var bgElevatedHighInteractive: Color { LemonadeTheme.colors.interaction.bgElevatedHighInteractive }
    public var bgBrandInteractive: Color { LemonadeTheme.colors.interaction.bgBrandInteractive }
    public var bgBrandPressed: Color { LemonadeTheme.colors.interaction.bgBrandPressed }
    public var bgBrandHighInteractive: Color { LemonadeTheme.colors.interaction.bgBrandHighInteractive }
    public var bgBrandHighPressed: Color { LemonadeTheme.colors.interaction.bgBrandHighPressed }
    public var bgBrandElevatedInteractive: Color { LemonadeTheme.colors.interaction.bgBrandElevatedInteractive }
    public var bgBrandElevatedPressed: Color { LemonadeTheme.colors.interaction.bgBrandElevatedPressed }
    public var bgCriticalInteractive: Color { LemonadeTheme.colors.interaction.bgCriticalInteractive }
    public var bgCriticalPressed: Color { LemonadeTheme.colors.interaction.bgCriticalPressed }
    public var bgCriticalSubtleInteractive: Color { LemonadeTheme.colors.interaction.bgCriticalSubtleInteractive }
    public var bgCautionInteractive: Color { LemonadeTheme.colors.interaction.bgCautionInteractive }
    public var bgCautionPressed: Color { LemonadeTheme.colors.interaction.bgCautionPressed }
    public var bgCautionSubtleInteractive: Color { LemonadeTheme.colors.interaction.bgCautionSubtleInteractive }
    public var bgCautionSubtlePressed: Color { LemonadeTheme.colors.interaction.bgCautionSubtlePressed }
    public var bgPositiveInteractive: Color { LemonadeTheme.colors.interaction.bgPositiveInteractive }
    public var bgPositivePressed: Color { LemonadeTheme.colors.interaction.bgPositivePressed }
    public var bgPositiveSubtleInteractive: Color { LemonadeTheme.colors.interaction.bgPositiveSubtleInteractive }
    public var bgPositiveSubtlePressed: Color { LemonadeTheme.colors.interaction.bgPositiveSubtlePressed }
    public var bgInfoInteractive: Color { LemonadeTheme.colors.interaction.bgInfoInteractive }
    public var bgInfoPressed: Color { LemonadeTheme.colors.interaction.bgInfoPressed }
    public var bgInfoSubtleInteractive: Color { LemonadeTheme.colors.interaction.bgInfoSubtleInteractive }
    public var bgInfoSubtlePressed: Color { LemonadeTheme.colors.interaction.bgInfoSubtlePressed }
    public var bgNeutralInteractive: Color { LemonadeTheme.colors.interaction.bgNeutralInteractive }
    public var bgNeutralPressed: Color { LemonadeTheme.colors.interaction.bgNeutralPressed }
    public var bgNeutralSubtleInteractive: Color { LemonadeTheme.colors.interaction.bgNeutralSubtleInteractive }
    public var bgNeutralSubtlePressed: Color { LemonadeTheme.colors.interaction.bgNeutralSubtlePressed }
}

// MARK: - ShapeStyle Extensions

public extension ShapeStyle where Self == Color {
    /// Content color tokens
    /// Usage: `.foregroundStyle(.content.contentPrimary)`
    static var content: LemonadeContentColorsShorthand { LemonadeContentColorsShorthand() }

    /// Background color tokens
    /// Usage: `.background(.bg.bgDefault)`
    static var bg: LemonadeBackgroundColorsShorthand { LemonadeBackgroundColorsShorthand() }

    /// Border color tokens
    /// Usage: `.border(.border.borderNeutralMedium)`
    static var border: LemonadeBorderColorsShorthand { LemonadeBorderColorsShorthand() }

    /// Interaction color tokens
    /// Usage: `.background(.interaction.bgBrandInteractive)`
    static var interaction: LemonadeInteractionColorsShorthand { LemonadeInteractionColorsShorthand() }
}
