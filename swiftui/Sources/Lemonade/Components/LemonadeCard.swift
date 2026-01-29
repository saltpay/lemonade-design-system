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
    
    var color: Color {
        switch self {
        case .default: return LemonadeTheme.colors.background.bgDefault
        case .subtle: return LemonadeTheme.colors.background.bgSubtle
        }
    }
}

// MARK: - Card Header Config

/// Configuration for the Card header.
public struct CardHeaderConfig<TrailingContent: View> {
    let title: String
    let trailingSlot: (() -> TrailingContent)?
    
    public init(title: String, trailingSlot: (() -> TrailingContent)? = nil) {
        self.title = title
        self.trailingSlot = trailingSlot
    }
}

// Convenience initializer without trailing content
extension CardHeaderConfig where TrailingContent == EmptyView {
    public init(title: String) {
        self.title = title
        self.trailingSlot = nil
    }
}

// MARK: - Card Component

public extension LemonadeUi {
    /// A card container component with optional header and configurable padding and background.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Card(
    ///     contentPadding: .medium,
    ///     background: .default,
    ///     header: CardHeaderConfig(title: "Card Title")
    /// ) {
    ///     Text("Card content goes here")
    /// }
    /// ```
    ///
    /// - Parameters:
    ///   - contentPadding: LemonadeCardPadding for the content area. Defaults to .none
    ///   - background: LemonadeCardBackground style. Defaults to .default
    ///   - header: Optional CardHeaderConfig for the header
    ///   - content: Content to display inside the card
    /// - Returns: A styled Card view
    @ViewBuilder
    static func Card<Content: View, TrailingContent: View>(
        contentPadding: LemonadeCardPadding = .none,
        background: LemonadeCardBackground = .default,
        header: CardHeaderConfig<TrailingContent>? = nil,
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
        LemonadeCardView<Content, EmptyView>(
            contentPadding: contentPadding,
            background: background,
            header: nil,
            content: content
        )
    }
}

// MARK: - Internal Card View

private struct LemonadeCardView<Content: View, TrailingContent: View>: View {
    let contentPadding: LemonadeCardPadding
    let background: LemonadeCardBackground
    let header: CardHeaderConfig<TrailingContent>?
    let content: () -> Content
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Header
            if let header = header {
                HStack(spacing: LemonadeTheme.spaces.spacing200) {
                    LemonadeUi.Text(
                        header.title,
                        textStyle: LemonadeTypography().headingXXSmall,
                        overflow: .tail,
                        maxLines: 1
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                    if let trailingSlot = header.trailingSlot {
                        trailingSlot()
                    }
                }
                .padding(.horizontal, LemonadeTheme.spaces.spacing400)
                .padding(.top, LemonadeTheme.spaces.spacing400)
            }
            
            // Content
            VStack(alignment: .leading, spacing: 0) {
                content()
            }
            .padding(contentPadding.spacing)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(background.color)
        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
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
            
            // Subtle background
            LemonadeUi.Card(
                contentPadding: .medium,
                background: .subtle
            ) {
                LemonadeUi.Text("Subtle background card")
            }
        }
        .padding()
        .background(Color.gray.opacity(0.1))
        .previewLayout(.sizeThatFits)
    }
}
#endif
