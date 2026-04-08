import SwiftUI

// MARK: - Card Padding

/// Padding options for Card content.
public enum LemonadeCardPadding {
    case none
    case xSmall
    case small
    case medium

    var spacing: CGFloat {
        switch self {
        case .none: return LemonadeTheme.spaces.spacing0
        case .xSmall: return LemonadeTheme.spaces.spacing100
        case .small: return LemonadeTheme.spaces.spacing200
        case .medium: return LemonadeTheme.spaces.spacing400
        }
    }
}

// MARK: - Card Background

/// Background style options for Card.
public enum LemonadeCardBackground {
    case `default`
    case subtle
    case subtleHigh

    var color: Color {
        switch self {
        case .default: return LemonadeTheme.colors.background.bgDefault
        case .subtle: return LemonadeTheme.colors.background.bgSubtle
        case .subtleHigh: return LemonadeTheme.colors.background.bgElevated
        }
    }
}

// MARK: - Card Heading Style

/// Heading style options for Card header.
public enum LemonadeCardHeadingStyle {
    case `default`
    case overline

    var textStyle: LemonadeTextStyle {
        switch self {
        case .default: return LemonadeTypography.shared.headingXXSmall
        case .overline: return LemonadeTypography.shared.bodyXSmallOverline
        }
    }

    var textColor: Color {
        switch self {
        case .default: return LemonadeTheme.colors.content.contentPrimary
        case .overline: return LemonadeTheme.colors.content.contentSecondary
        }
    }
}

// MARK: - Card Header Config

/// Configuration for the Card header.
public struct CardHeaderConfig<LeadingContent: View, TrailingContent: View> {
    let title: String
    let headingStyle: LemonadeCardHeadingStyle
    let leadingSlot: (() -> LeadingContent)?
    let trailingSlot: (() -> TrailingContent)?
    let showNavigationIndicator: Bool

    public init(
        title: String,
        headingStyle: LemonadeCardHeadingStyle = .default,
        leadingSlot: (() -> LeadingContent)? = nil,
        trailingSlot: (() -> TrailingContent)? = nil,
        showNavigationIndicator: Bool = false
    ) {
        self.title = title
        self.headingStyle = headingStyle
        self.leadingSlot = leadingSlot
        self.trailingSlot = trailingSlot
        self.showNavigationIndicator = showNavigationIndicator
    }
}

// Convenience initializer without leading or trailing content
extension CardHeaderConfig where LeadingContent == EmptyView, TrailingContent == EmptyView {
    public init(
        title: String,
        headingStyle: LemonadeCardHeadingStyle = .default,
        showNavigationIndicator: Bool = false
    ) {
        self.title = title
        self.headingStyle = headingStyle
        self.leadingSlot = nil
        self.trailingSlot = nil
        self.showNavigationIndicator = showNavigationIndicator
    }
}

// Convenience initializer with only trailing content
extension CardHeaderConfig where LeadingContent == EmptyView {
    public init(
        title: String,
        headingStyle: LemonadeCardHeadingStyle = .default,
        trailingSlot: (() -> TrailingContent)? = nil,
        showNavigationIndicator: Bool = false
    ) {
        self.title = title
        self.headingStyle = headingStyle
        self.leadingSlot = nil
        self.trailingSlot = trailingSlot
        self.showNavigationIndicator = showNavigationIndicator
    }
}

// Convenience initializer with only leading content
extension CardHeaderConfig where TrailingContent == EmptyView {
    public init(
        title: String,
        headingStyle: LemonadeCardHeadingStyle = .default,
        leadingSlot: (() -> LeadingContent)? = nil,
        showNavigationIndicator: Bool = false
    ) {
        self.title = title
        self.headingStyle = headingStyle
        self.leadingSlot = leadingSlot
        self.trailingSlot = nil
        self.showNavigationIndicator = showNavigationIndicator
    }
}

// MARK: - Card Component

public extension LemonadeUi {
    /// A card container component with optional header and configurable padding and background.
    ///
    /// - Parameters:
    ///   - contentPadding: LemonadeCardPadding for the content area. Defaults to .none
    ///   - background: LemonadeCardBackground style. Defaults to .default
    ///   - header: Optional CardHeaderConfig for the header
    ///   - content: Content to display inside the card
    /// - Returns: A styled Card view
    @ViewBuilder
    static func Card<Content: View, LeadingContent: View, TrailingContent: View>(
        contentPadding: LemonadeCardPadding = .none,
        background: LemonadeCardBackground = .default,
        header: CardHeaderConfig<LeadingContent, TrailingContent>? = nil,
        @ViewBuilder content: @escaping () -> Content
    ) -> some View {
        LemonadeCardView(
            contentPadding: contentPadding,
            background: background,
            header: header,
            content: content
        )
    }

    /// A card container component without header.
    ///
    /// - Parameters:
    ///   - contentPadding: LemonadeCardPadding for the content area. Defaults to .none
    ///   - background: LemonadeCardBackground style. Defaults to .default
    ///   - content: Content to display inside the card
    /// - Returns: A styled Card view
    @ViewBuilder
    static func Card<Content: View>(
        contentPadding: LemonadeCardPadding = .none,
        background: LemonadeCardBackground = .default,
        @ViewBuilder content: @escaping () -> Content
    ) -> some View {
        LemonadeCardView<Content, EmptyView, EmptyView>(
            contentPadding: contentPadding,
            background: background,
            header: nil,
            content: content
        )
    }
}

// MARK: - Internal Card View

private struct LemonadeCardView<Content: View, LeadingContent: View, TrailingContent: View>: View {
    let contentPadding: LemonadeCardPadding
    let background: LemonadeCardBackground
    let header: CardHeaderConfig<LeadingContent, TrailingContent>?
    let content: () -> Content

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if let header = header {
                LemonadeCardHeader(config: header)
            }

            VStack(alignment: .leading, spacing: 0) {
                content()
            }
            .padding(contentPadding.spacing)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(background.color)
        .clipShape(LemonadeTheme.shapes.semantic.radiusContainerDefault)
    }
}

private struct LemonadeCardHeader<LeadingContent: View, TrailingContent: View>: View {
    let config: CardHeaderConfig<LeadingContent, TrailingContent>

    var body: some View {
        HStack(spacing: LemonadeTheme.spaces.spacing200) {
            if let leadingSlot = config.leadingSlot {
                leadingSlot()
            }

            LemonadeUi.Text(
                config.title,
                textStyle: config.headingStyle.textStyle,
                color: config.headingStyle.textColor,
                overflow: .tail,
                maxLines: 1
            )
            .frame(maxWidth: .infinity, alignment: .leading)

            if let trailingSlot = config.trailingSlot {
                trailingSlot()
            }

            if config.showNavigationIndicator {
                LemonadeUi.Icon(
                    icon: .chevronRight,
                    contentDescription: nil,
                    size: .medium,
                    tint: LemonadeTheme.colors.content.contentSecondary
                )
            }
        }
        .padding(.horizontal, LemonadeTheme.spaces.spacing400)
        .padding(.top, LemonadeTheme.spaces.spacing400)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeCard_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Basic card
            LemonadeUi.Card(contentPadding: .medium) {
                LemonadeUi.Text("This is card content")
            }

            // Card with header
            LemonadeUi.Card(
                contentPadding: .medium,
                header: CardHeaderConfig(title: "Card Title")
            ) {
                LemonadeUi.Text("Content with header")
            }

            // Card with header and trailing slot
            LemonadeUi.Card(
                contentPadding: .medium,
                header: CardHeaderConfig(
                    title: "Card with Tag",
                    trailingSlot: {
                        LemonadeUi.Tag(label: "New", voice: .positive)
                    }
                )
            ) {
                LemonadeUi.Text("Content with header and trailing tag")
            }

            // Card with overline heading
            LemonadeUi.Card(
                contentPadding: .medium,
                header: CardHeaderConfig(
                    title: "Overline Title",
                    headingStyle: .overline
                )
            ) {
                LemonadeUi.Text("Content with overline heading")
            }

            // Card with navigation indicator
            LemonadeUi.Card(
                contentPadding: .medium,
                header: CardHeaderConfig(
                    title: "Navigable Card",
                    showNavigationIndicator: true
                )
            ) {
                LemonadeUi.Text("Card with navigation indicator")
            }

            // Subtle High background
            LemonadeUi.Card(
                contentPadding: .medium,
                background: .subtleHigh
            ) {
                LemonadeUi.Text("Subtle High background card")
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .previewLayout(.sizeThatFits)
    }
}
#endif
