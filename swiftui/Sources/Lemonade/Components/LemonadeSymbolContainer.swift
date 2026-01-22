import SwiftUI

// MARK: - SymbolContainer Voice

/// Defines the tone of voice for a SymbolContainer.
public enum SymbolContainerVoice {
    case neutral
    case critical
    case warning
    case info
    case positive
    case brand
    case brandSubtle

    var tintColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.content.contentPrimary
        case .critical: return LemonadeTheme.colors.content.contentCritical
        case .warning: return LemonadeTheme.colors.content.contentCaution
        case .info: return LemonadeTheme.colors.content.contentInfo
        case .positive: return LemonadeTheme.colors.content.contentPositive
        case .brand: return LemonadeTheme.colors.content.contentOnBrandHigh
        case .brandSubtle: return LemonadeTheme.colors.content.contentOnBrandHigh
        }
    }

    var containerColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.background.bgNeutralSubtle
        case .critical: return LemonadeTheme.colors.background.bgCriticalSubtle
        case .warning: return LemonadeTheme.colors.background.bgCautionSubtle
        case .info: return LemonadeTheme.colors.background.bgInfoSubtle
        case .positive: return LemonadeTheme.colors.background.bgPositiveSubtle
        case .brand: return LemonadeTheme.colors.background.bgBrand
        case .brandSubtle: return LemonadeTheme.colors.background.bgBrandSubtle
        }
    }
}

// MARK: - SymbolContainer Size

/// Size options for the SymbolContainer.
public enum SymbolContainerSize {
    case xSmall
    case small
    case medium
    case large
    case xLarge

    var containerSize: CGFloat {
        switch self {
        case .xSmall: return 24
        case .small: return 32
        case .medium: return 40
        case .large: return 48
        case .xLarge: return 64
        }
    }

    var contentSize: CGFloat {
        switch self {
        case .xSmall: return 12
        case .small: return 16
        case .medium: return 20
        case .large: return 24
        case .xLarge: return 32
        }
    }

    var iconSize: LemonadeUiIconSize {
        switch self {
        case .xSmall: return .xSmall
        case .small: return .small
        case .medium: return .medium
        case .large: return .large
        case .xLarge: return .xLarge
        }
    }

    var textStyle: LemonadeTextStyle {
        switch self {
        case .xSmall: return LemonadeTypography().bodyXSmallSemiBold
        case .small: return LemonadeTypography().bodySmallSemiBold
        case .medium: return LemonadeTypography().bodySmallSemiBold
        case .large: return LemonadeTypography().bodyLargeSemiBold
        case .xLarge: return LemonadeTypography().bodyXLargeSemiBold
        }
    }
}

// MARK: - SymbolContainer Component

public extension LemonadeUi {
    /// A versatile container used to display an icon with consistent sizing and tone.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SymbolContainer(
    ///     icon: .heart,
    ///     contentDescription: "Favorite",
    ///     voice: .info,
    ///     size: .medium
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - icon: LemonadeIcon to be displayed inside the container
    ///   - contentDescription: Localized content description for the icon
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    /// - Returns: A styled SymbolContainer view with icon
    @ViewBuilder
    static func SymbolContainer(
        icon: LemonadeIcon,
        contentDescription: String?,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size) {
            LemonadeUi.Icon(
                icon: icon,
                contentDescription: contentDescription,
                size: size.iconSize,
                tint: voice.tintColor
            )
        }
    }

    /// A versatile container used to display text with consistent sizing and tone.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SymbolContainer(
    ///     text: "W",
    ///     voice: .info,
    ///     size: .medium
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - text: String to be displayed inside the container
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    /// - Returns: A styled SymbolContainer view with text
    @ViewBuilder
    static func SymbolContainer(
        text: String,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size) {
            LemonadeUi.Text(
                text,
                textStyle: size.textStyle,
                color: voice.tintColor
            )
        }
    }

    /// A versatile container used to display custom content with consistent sizing and tone.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SymbolContainer(
    ///     voice: .info,
    ///     size: .medium
    /// ) {
    ///     Image("custom-image")
    /// }
    /// ```
    ///
    /// - Parameters:
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    ///   - content: Custom content to display inside the container
    /// - Returns: A styled SymbolContainer view with custom content
    @ViewBuilder
    static func SymbolContainer<Content: View>(
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        @ViewBuilder content: @escaping () -> Content
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size) {
            content()
                .frame(width: size.contentSize, height: size.contentSize)
        }
    }
}

// MARK: - Internal SymbolContainer View

private struct LemonadeSymbolContainerView<Content: View>: View {
    let voice: SymbolContainerVoice
    let size: SymbolContainerSize
    let content: () -> Content

    var body: some View {
        content()
            .frame(width: size.containerSize, height: size.containerSize)
            .background(voice.containerColor)
            .clipShape(Circle())
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeSymbolContainer_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Icon variant - all sizes
            HStack(spacing: 8) {
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Heart", size: .xSmall)
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Heart", size: .small)
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Heart", size: .medium)
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Heart", size: .large)
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Heart", size: .xLarge)
            }

            // Text variant - all voices
            HStack(spacing: 8) {
                LemonadeUi.SymbolContainer(text: "A", voice: .neutral)
                LemonadeUi.SymbolContainer(text: "B", voice: .critical)
                LemonadeUi.SymbolContainer(text: "C", voice: .warning)
                LemonadeUi.SymbolContainer(text: "D", voice: .info)
                LemonadeUi.SymbolContainer(text: "E", voice: .positive)
            }

            // Brand voices
            HStack(spacing: 8) {
                LemonadeUi.SymbolContainer(icon: .star, contentDescription: "Star", voice: .brand)
                LemonadeUi.SymbolContainer(icon: .star, contentDescription: "Star", voice: .brandSubtle)
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
