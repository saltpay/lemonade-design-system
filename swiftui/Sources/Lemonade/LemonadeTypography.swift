import SwiftUI

/// Represents a text style with typographic properties.
public struct LemonadeTextStyle {
    /// The font size in points
    public let fontSize: CGFloat
    /// The line height in points
    public let lineHeight: CGFloat
    /// The font weight
    public let fontWeight: Font.Weight
    /// The letter spacing in points, nil if default
    public let letterSpacing: CGFloat?

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

    /// Returns a SwiftUI Font based on this text style
    public var font: Font {
        // Use specific font file names to avoid SwiftUI weight mapping issues
        let fontName: String
        switch fontWeight {
        case .regular:
            fontName = "Figtree-Regular"
        case .medium:
            fontName = "Figtree-Medium"
        case .semibold, .bold:
            fontName = "Figtree-SemiBold"
        default:
            fontName = "Figtree-Regular"
        }
        return .custom(fontName, size: fontSize)
    }

    /// Returns the line spacing (lineHeight - fontSize)
    public var lineSpacing: CGFloat {
        max(0, lineHeight - fontSize)
    }
}

/// Protocol defining all available text styles in the Lemonade Design System.
public protocol LemonadeTypographyProtocol {
    // Display styles
    var displayXSmall: LemonadeTextStyle { get }
    var displaySmall: LemonadeTextStyle { get }
    var displayMedium: LemonadeTextStyle { get }
    var displayLarge: LemonadeTextStyle { get }

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

    public init() {}

    // Display styles
    public let displayXSmall = LemonadeTextStyle(fontSize: 24, lineHeight: 32, fontWeight: .semibold)
    public let displaySmall = LemonadeTextStyle(fontSize: 28, lineHeight: 36, fontWeight: .semibold)
    public let displayMedium = LemonadeTextStyle(fontSize: 36, lineHeight: 44, fontWeight: .semibold)
    public let displayLarge = LemonadeTextStyle(fontSize: 48, lineHeight: 56, fontWeight: .semibold)

    // Heading styles
    public let headingXLarge = LemonadeTextStyle(fontSize: 40, lineHeight: 48, fontWeight: .semibold)
    public let headingLarge = LemonadeTextStyle(fontSize: 32, lineHeight: 40, fontWeight: .semibold)
    public let headingMedium = LemonadeTextStyle(fontSize: 28, lineHeight: 36, fontWeight: .semibold)
    public let headingSmall = LemonadeTextStyle(fontSize: 24, lineHeight: 32, fontWeight: .semibold)
    public let headingXSmall = LemonadeTextStyle(fontSize: 18, lineHeight: 26, fontWeight: .semibold)
    public let headingXXSmall = LemonadeTextStyle(fontSize: 16, lineHeight: 24, fontWeight: .semibold)

    // Body XLarge styles
    public let bodyXLargeRegular = LemonadeTextStyle(fontSize: 20, lineHeight: 28, fontWeight: .regular)
    public let bodyXLargeMedium = LemonadeTextStyle(fontSize: 20, lineHeight: 28, fontWeight: .medium)
    public let bodyXLargeSemiBold = LemonadeTextStyle(fontSize: 20, lineHeight: 28, fontWeight: .semibold)

    // Body Large styles
    public let bodyLargeRegular = LemonadeTextStyle(fontSize: 18, lineHeight: 28, fontWeight: .regular)
    public let bodyLargeMedium = LemonadeTextStyle(fontSize: 18, lineHeight: 28, fontWeight: .medium)
    public let bodyLargeSemiBold = LemonadeTextStyle(fontSize: 18, lineHeight: 28, fontWeight: .semibold)

    // Body Medium styles
    public let bodyMediumRegular = LemonadeTextStyle(fontSize: 16, lineHeight: 24, fontWeight: .regular)
    public let bodyMediumMedium = LemonadeTextStyle(fontSize: 16, lineHeight: 24, fontWeight: .medium)
    public let bodyMediumSemiBold = LemonadeTextStyle(fontSize: 16, lineHeight: 24, fontWeight: .semibold)
    public let bodyMediumBold = LemonadeTextStyle(fontSize: 16, lineHeight: 24, fontWeight: .semibold)

    // Body Small styles
    public let bodySmallRegular = LemonadeTextStyle(fontSize: 14, lineHeight: 20, fontWeight: .regular)
    public let bodySmallMedium = LemonadeTextStyle(fontSize: 14, lineHeight: 20, fontWeight: .medium)
    public let bodySmallSemiBold = LemonadeTextStyle(fontSize: 14, lineHeight: 20, fontWeight: .semibold)

    // Body XSmall styles
    public let bodyXSmallRegular = LemonadeTextStyle(fontSize: 12, lineHeight: 16, fontWeight: .regular)
    public let bodyXSmallMedium = LemonadeTextStyle(fontSize: 12, lineHeight: 16, fontWeight: .medium)
    public let bodyXSmallSemiBold = LemonadeTextStyle(fontSize: 12, lineHeight: 16, fontWeight: .semibold)
    public let bodyXSmallOverline = LemonadeTextStyle(fontSize: 12, lineHeight: 16, fontWeight: .semibold, letterSpacing: 1.5)
}

// MARK: - Environment Key

private struct LemonadeTypographyKey: EnvironmentKey {
    static let defaultValue: LemonadeTypographyProtocol = LemonadeTypography()
}

public extension EnvironmentValues {
    var lemonadeTypography: LemonadeTypographyProtocol {
        get { self[LemonadeTypographyKey.self] }
        set { self[LemonadeTypographyKey.self] = newValue }
    }
}
