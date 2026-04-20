import SwiftUI

#if canImport(UIKit)
import UIKit
#endif

/// Represents a text style with typographic properties.
public struct LemonadeTextStyle: Sendable {
    /// The font size in points
    public let fontSize: CGFloat
    /// The line height in points
    public let lineHeight: CGFloat
    /// The font weight
    public let fontWeight: Font.Weight
    /// The letter spacing in points, nil if default
    public let letterSpacing: CGFloat?

    /// Fallback line height ratio for Figtree font (used on platforms without UIKit).
    /// Calculated from font metrics: (ascender - descender + lineGap) / unitsPerEm
    /// Figtree: (950 - (-250) + 0) / 1000 = 1.20
    private static let fallbackLineHeightRatio: CGFloat = 1.20

    public init(
        fontSize: CGFloat,
        lineHeight: CGFloat,
        fontWeight: Font.Weight,
        letterSpacing: CGFloat? = nil
    ) {
        self.fontSize = fontSize
        self.lineHeight = lineHeight
        self.fontWeight = fontWeight
        self.letterSpacing = letterSpacing
    }

    /// The font name based on the weight
    public var fontName: String {
        switch fontWeight {
        case .regular:
            return "Figtree-Regular"
        case .medium:
            return "Figtree-Medium"
        case .semibold, .bold:
            return "Figtree-SemiBold"
        default:
            return "Figtree-Regular"
        }
    }

    /// Returns a SwiftUI Font based on this text style
    public var font: Font {
        .custom(fontName, size: fontSize, relativeTo: .body)
    }

#if canImport(UIKit)
    /// Returns a UIFont based on this text style
    public var uiFont: UIFont {
        UIFont(name: fontName, size: fontSize)
            ?? .systemFont(ofSize: fontSize)
    }
#endif

    /// Returns the line spacing needed to achieve the desired line height.
    /// Uses UIFont.lineHeight for precise calculation on iOS, fallback ratio on other platforms.
    public var lineSpacing: CGFloat {
#if canImport(UIKit)
        let naturalLineHeight = uiFont.lineHeight
#else
        let naturalLineHeight = fontSize * Self.fallbackLineHeightRatio
#endif
        return max(0, lineHeight - naturalLineHeight)
    }
}

/// Protocol defining all available text styles in the Lemonade Design System.
public protocol LemonadeTypographyProtocol {
    // Display styles
    var displayXSmall: LemonadeTextStyle { get }
    var displaySmall: LemonadeTextStyle { get }
    var displayMedium: LemonadeTextStyle { get }
    var displayLarge: LemonadeTextStyle { get }
    var displayXLarge: LemonadeTextStyle { get }
    var display2XLarge: LemonadeTextStyle { get }
    var display3XLarge: LemonadeTextStyle { get }

    // Heading styles
    var headingXLarge: LemonadeTextStyle { get }
    var headingLarge: LemonadeTextStyle { get }
    var headingMedium: LemonadeTextStyle { get }
    var headingSmall: LemonadeTextStyle { get }
    var headingXSmall: LemonadeTextStyle { get }
    var headingXXSmall: LemonadeTextStyle { get }

    // Body XLarge styles
    var bodyXLargeRegular: LemonadeTextStyle { get }
    var bodyXLargeMedium: LemonadeTextStyle { get }
    var bodyXLargeSemiBold: LemonadeTextStyle { get }

    // Body Large styles
    var bodyLargeRegular: LemonadeTextStyle { get }
    var bodyLargeMedium: LemonadeTextStyle { get }
    var bodyLargeSemiBold: LemonadeTextStyle { get }

    // Body Medium styles
    var bodyMediumRegular: LemonadeTextStyle { get }
    var bodyMediumMedium: LemonadeTextStyle { get }
    var bodyMediumSemiBold: LemonadeTextStyle { get }
    var bodyMediumBold: LemonadeTextStyle { get }

    // Body Small styles
    var bodySmallRegular: LemonadeTextStyle { get }
    var bodySmallMedium: LemonadeTextStyle { get }
    var bodySmallSemiBold: LemonadeTextStyle { get }

    // Body XSmall styles
    var bodyXSmallRegular: LemonadeTextStyle { get }
    var bodyXSmallMedium: LemonadeTextStyle { get }
    var bodyXSmallSemiBold: LemonadeTextStyle { get }
    var bodyXSmallOverline: LemonadeTextStyle { get }
}

/// Default implementation of LemonadeTypography following the Lemonade Design System specifications.
public struct LemonadeTypography: LemonadeTypographyProtocol {
    /// Shared instance to avoid repeated allocations
    public static let shared = LemonadeTypography()

    /// The font family used across the design system.
    public static let fontFamily = "Figtree"

    public init() {}

    // Display styles
    public let displayXSmall = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize600.value,
        lineHeight: LemonadeLineHeights.lineHeight800.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )
    public let displaySmall = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize700.value,
        lineHeight: LemonadeLineHeights.lineHeight900.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )
    public let displayMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize900.value,
        lineHeight: LemonadeLineHeights.lineHeight1100.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )
    public let displayLarge = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize1200.value,
        lineHeight: LemonadeLineHeights.lineHeight1400.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )
    public let displayXLarge = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize1400.value,
        lineHeight: LemonadeLineHeights.lineHeight1600.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )
    public let display2XLarge = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize1600.value,
        lineHeight: LemonadeLineHeights.lineHeight1800.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )
    public let display3XLarge = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize1800.value,
        lineHeight: LemonadeLineHeights.lineHeight2000.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: -0.25
    )

    // Heading styles
    public let headingXLarge = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize1000.value,
        lineHeight: LemonadeLineHeights.lineHeight1200.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    public let headingLarge = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize800.value,
        lineHeight: LemonadeLineHeights.lineHeight1000.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    public let headingMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize700.value,
        lineHeight: LemonadeLineHeights.lineHeight900.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    public let headingSmall = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize600.value,
        lineHeight: LemonadeLineHeights.lineHeight800.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    public let headingXSmall = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize450.value,
        lineHeight: LemonadeLineHeights.lineHeight650.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    public let headingXXSmall = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize400.value,
        lineHeight: LemonadeLineHeights.lineHeight600.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )

    // Body XLarge styles
    public let bodyXLargeRegular = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize500.value,
        lineHeight: LemonadeLineHeights.lineHeight700.value,
        fontWeight: LemonadeFontWeights.regular.value
    )
    public let bodyXLargeMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize500.value,
        lineHeight: LemonadeLineHeights.lineHeight700.value,
        fontWeight: LemonadeFontWeights.medium.value
    )
    public let bodyXLargeSemiBold = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize500.value,
        lineHeight: LemonadeLineHeights.lineHeight700.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )

    // Body Large styles
    public let bodyLargeRegular = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize450.value,
        lineHeight: LemonadeLineHeights.lineHeight700.value,
        fontWeight: LemonadeFontWeights.regular.value
    )
    public let bodyLargeMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize450.value,
        lineHeight: LemonadeLineHeights.lineHeight700.value,
        fontWeight: LemonadeFontWeights.medium.value
    )
    public let bodyLargeSemiBold = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize450.value,
        lineHeight: LemonadeLineHeights.lineHeight700.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )

    // Body Medium styles
    public let bodyMediumRegular = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize400.value,
        lineHeight: LemonadeLineHeights.lineHeight600.value,
        fontWeight: LemonadeFontWeights.regular.value
    )
    public let bodyMediumMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize400.value,
        lineHeight: LemonadeLineHeights.lineHeight600.value,
        fontWeight: LemonadeFontWeights.medium.value
    )
    public let bodyMediumSemiBold = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize400.value,
        lineHeight: LemonadeLineHeights.lineHeight600.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    /// Maps to `.semibold` weight because the Figtree font family does not include a true bold weight.
    public let bodyMediumBold = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize400.value,
        lineHeight: LemonadeLineHeights.lineHeight600.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )

    // Body Small styles
    public let bodySmallRegular = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize350.value,
        lineHeight: LemonadeLineHeights.lineHeight500.value,
        fontWeight: LemonadeFontWeights.regular.value
    )
    public let bodySmallMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize350.value,
        lineHeight: LemonadeLineHeights.lineHeight500.value,
        fontWeight: LemonadeFontWeights.medium.value
    )
    public let bodySmallSemiBold = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize350.value,
        lineHeight: LemonadeLineHeights.lineHeight500.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )

    // Body XSmall styles
    public let bodyXSmallRegular = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize300.value,
        lineHeight: LemonadeLineHeights.lineHeight400.value,
        fontWeight: LemonadeFontWeights.regular.value
    )
    public let bodyXSmallMedium = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize300.value,
        lineHeight: LemonadeLineHeights.lineHeight400.value,
        fontWeight: LemonadeFontWeights.medium.value
    )
    public let bodyXSmallSemiBold = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize300.value,
        lineHeight: LemonadeLineHeights.lineHeight400.value,
        fontWeight: LemonadeFontWeights.semibold.value
    )
    public let bodyXSmallOverline = LemonadeTextStyle(
        fontSize: LemonadeFontSizes.fontSize300.value,
        lineHeight: LemonadeLineHeights.lineHeight400.value,
        fontWeight: LemonadeFontWeights.semibold.value,
        letterSpacing: 1.5
    )
}

// MARK: - Environment Key

private struct LemonadeTypographyKey: EnvironmentKey {
    static let defaultValue: LemonadeTypographyProtocol = LemonadeTypography()
}

extension EnvironmentValues {
    public var lemonadeTypography: LemonadeTypographyProtocol {
        get { self[LemonadeTypographyKey.self] }
        set { self[LemonadeTypographyKey.self] = newValue }
    }
}
