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
        case .xSmall: return LemonadeTheme.sizes.size600
        case .small: return LemonadeTheme.sizes.size800
        case .medium: return LemonadeTheme.sizes.size1000
        case .large: return LemonadeTheme.sizes.size1200
        case .xLarge: return LemonadeTheme.sizes.size1600
        }
    }

    var contentSize: CGFloat {
        switch self {
        case .xSmall: return LemonadeTheme.sizes.size300
        case .small: return LemonadeTheme.sizes.size400
        case .medium: return LemonadeTheme.sizes.size500
        case .large: return LemonadeTheme.sizes.size600
        case .xLarge: return LemonadeTheme.sizes.size800
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
        case .xSmall: return LemonadeTypography.shared.bodyXSmallSemiBold
        case .small: return LemonadeTypography.shared.bodySmallSemiBold
        case .medium: return LemonadeTypography.shared.bodySmallSemiBold
        case .large: return LemonadeTypography.shared.bodyLargeSemiBold
        case .xLarge: return LemonadeTypography.shared.bodyXLargeSemiBold
        }
    }
}

// MARK: - SymbolContainer Shape

/// Shape options for the SymbolContainer.
public enum SymbolContainerShape {
    case circle
    case rounded
}

// MARK: - SymbolContainer Component

public extension LemonadeUi {
    /// A versatile container used to display an icon with consistent sizing and tone.
    ///
    /// - Parameters:
    ///   - icon: LemonadeIcon to be displayed inside the container
    ///   - contentDescription: Localized content description for the icon
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    ///   - shape: SymbolContainerShape to define the container's shape. Defaults to .circle
    ///   - badgeSlot: Optional content to be displayed as a badge overlay at the bottom-right corner
    /// - Returns: A styled SymbolContainer view with icon
    @ViewBuilder
    static func SymbolContainer<Badge: View>(
        icon: LemonadeIcon,
        contentDescription: String?,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle,
        @ViewBuilder badgeSlot: @escaping () -> Badge
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size, shape: shape, badgeSlot: badgeSlot) {
            LemonadeUi.Icon(
                icon: icon,
                contentDescription: contentDescription,
                size: size.iconSize,
                tint: voice.tintColor
            )
        }
    }

    /// A versatile container used to display an icon with consistent sizing and tone (no badge).
    @ViewBuilder
    static func SymbolContainer(
        icon: LemonadeIcon,
        contentDescription: String?,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle
    ) -> some View {
        LemonadeSymbolContainerView<EmptyView>(voice: voice, size: size, shape: shape, badgeSlot: nil) {
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
    /// - Parameters:
    ///   - text: String to be displayed inside the container
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    ///   - shape: SymbolContainerShape to define the container's shape. Defaults to .circle
    ///   - badgeSlot: Optional content to be displayed as a badge overlay at the bottom-right corner
    /// - Returns: A styled SymbolContainer view with text
    @ViewBuilder
    static func SymbolContainer<Badge: View>(
        text: String,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle,
        @ViewBuilder badgeSlot: @escaping () -> Badge
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size, shape: shape, badgeSlot: badgeSlot) {
            LemonadeUi.Text(
                text,
                textStyle: size.textStyle,
                color: voice.tintColor
            )
        }
    }

    /// A versatile container used to display text with consistent sizing and tone (no badge).
    @ViewBuilder
    static func SymbolContainer(
        text: String,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle
    ) -> some View {
        LemonadeSymbolContainerView<EmptyView>(voice: voice, size: size, shape: shape, badgeSlot: nil) {
            LemonadeUi.Text(
                text,
                textStyle: size.textStyle,
                color: voice.tintColor
            )
        }
    }

    /// A versatile container used to display an image with consistent sizing and tone.
    ///
    /// - Parameters:
    ///   - image: SwiftUI Image to be displayed inside the container. Rendered with its original colors (no tint).
    ///   - contentDescription: Localized content description for the image
    ///   - fill: When true, the image fills the entire container and is clipped by the shape. When false, the image is sized to the content area and centered.
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    ///   - shape: SymbolContainerShape to define the container's shape. Defaults to .circle
    ///   - badgeSlot: Optional content to be displayed as a badge overlay at the bottom-right corner
    /// - Returns: A styled SymbolContainer view with image
    @ViewBuilder
    static func SymbolContainer<Badge: View>(
        image: Image,
        contentDescription: String?,
        fill: Bool = true,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle,
        @ViewBuilder badgeSlot: @escaping () -> Badge
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size, shape: shape, badgeSlot: badgeSlot) {
            image
                .resizable()
                .aspectRatio(contentMode: fill ? .fill : .fit)
                .frame(
                    width: fill ? size.containerSize : size.contentSize,
                    height: fill ? size.containerSize : size.contentSize
                )
                .clipped()
                .accessibilityLabel(contentDescription ?? "")
                .accessibilityHidden(contentDescription == nil)
        }
    }

    /// A versatile container used to display an image with consistent sizing and tone (no badge).
    @ViewBuilder
    static func SymbolContainer(
        image: Image,
        contentDescription: String?,
        fill: Bool = true,
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle
    ) -> some View {
        LemonadeSymbolContainerView<EmptyView>(voice: voice, size: size, shape: shape, badgeSlot: nil) {
            image
                .resizable()
                .aspectRatio(contentMode: fill ? .fill : .fit)
                .frame(
                    width: fill ? size.containerSize : size.contentSize,
                    height: fill ? size.containerSize : size.contentSize
                )
                .clipped()
                .accessibilityLabel(contentDescription ?? "")
                .accessibilityHidden(contentDescription == nil)
        }
    }

    /// A versatile container used to display custom content with consistent sizing and tone.
    ///
    /// - Parameters:
    ///   - voice: SymbolContainerVoice to define the tone. Defaults to .neutral
    ///   - size: SymbolContainerSize to define the container's size. Defaults to .medium
    ///   - shape: SymbolContainerShape to define the container's shape. Defaults to .circle
    ///   - badgeSlot: Optional content to be displayed as a badge overlay at the bottom-right corner
    ///   - content: Custom content to display inside the container
    /// - Returns: A styled SymbolContainer view with custom content
    @ViewBuilder
    static func SymbolContainer<Content: View, Badge: View>(
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle,
        @ViewBuilder badgeSlot: @escaping () -> Badge,
        @ViewBuilder content: @escaping () -> Content
    ) -> some View {
        LemonadeSymbolContainerView(voice: voice, size: size, shape: shape, badgeSlot: badgeSlot) {
            content()
                .frame(width: size.contentSize, height: size.contentSize)
        }
    }

    /// A versatile container used to display custom content with consistent sizing and tone (no badge).
    @ViewBuilder
    static func SymbolContainer<Content: View>(
        voice: SymbolContainerVoice = .neutral,
        size: SymbolContainerSize = .medium,
        shape: SymbolContainerShape = .circle,
        @ViewBuilder content: @escaping () -> Content
    ) -> some View {
        LemonadeSymbolContainerView<EmptyView>(voice: voice, size: size, shape: shape, badgeSlot: nil) {
            content()
                .frame(width: size.contentSize, height: size.contentSize)
        }
    }
}

// MARK: - Internal SymbolContainer View

private struct LemonadeSymbolContainerView<Badge: View>: View {
    let voice: SymbolContainerVoice
    let size: SymbolContainerSize
    let shape: SymbolContainerShape
    let badgeSlot: (() -> Badge)?
    let content: () -> AnyView

    init<Content: View>(
        voice: SymbolContainerVoice,
        size: SymbolContainerSize,
        shape: SymbolContainerShape,
        badgeSlot: (() -> Badge)?,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.voice = voice
        self.size = size
        self.shape = shape
        self.badgeSlot = badgeSlot
        self.content = { AnyView(content()) }
    }

    @ViewBuilder
    private var containerView: some View {
        let base = content()
            .frame(width: size.containerSize, height: size.containerSize)
            .background(voice.containerColor)

        switch shape {
        case .circle:
            base.clipShape(Circle())
        case .rounded:
            base.clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))
        }
    }

    var body: some View {
        if let badgeSlot = badgeSlot {
            ZStack(alignment: .topTrailing) {
                containerView

                badgeSlot()
                    .offset(
                        x: LemonadeTheme.spaces.spacing100,
                        y: -LemonadeTheme.spaces.spacing100
                    )
            }
        } else {
            containerView
        }
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

            // Shapes
            HStack(spacing: 8) {
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Circle", shape: .circle)
                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: "Rounded", shape: .rounded)
            }

            // Brand voices
            HStack(spacing: 8) {
                LemonadeUi.SymbolContainer(icon: .star, contentDescription: "Star", voice: .brand)
                LemonadeUi.SymbolContainer(icon: .star, contentDescription: "Star", voice: .brandSubtle)
            }

            // With badge
            HStack(spacing: 16) {
                LemonadeUi.SymbolContainer(
                    icon: .heart,
                    contentDescription: "Heart",
                    size: .medium
                ) {
                    LemonadeUi.Badge(text: "3", size: .xSmall)
                }
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
