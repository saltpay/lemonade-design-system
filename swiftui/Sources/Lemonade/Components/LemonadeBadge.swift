import SwiftUI

// MARK: - Badge Size

/// Size options for the Badge component.
public enum LemonadeBadgeSize {
    case xSmall
    case small

    var height: CGFloat {
        switch self {
        case .xSmall: return 16
        case .small: return 20
        }
    }

    var horizontalPadding: CGFloat {
        switch self {
        case .xSmall: return LemonadeTheme.spaces.spacing50
        case .small: return LemonadeTheme.spaces.spacing100
        }
    }

    var textHorizontalPadding: CGFloat {
        LemonadeTheme.spaces.spacing50
    }

    var textVerticalPadding: CGFloat {
        switch self {
        case .xSmall: return 1
        case .small: return 2
        }
    }

    var fontSize: CGFloat {
        switch self {
        case .xSmall: return 10
        case .small: return 12
        }
    }

    var lineHeight: CGFloat {
        switch self {
        case .xSmall: return 14
        case .small: return 16
        }
    }
}

// MARK: - Badge Component

public extension LemonadeUi {
    /// Badge component to highlight new or unread items, or to indicate status.
    ///
    /// Badges are small, rounded indicators that can be used to draw attention
    /// to specific elements. They typically display counts, statuses, or categories.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Badge(
    ///     text: "New",
    ///     size: .small
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - text: The text to be displayed inside the badge
    ///   - size: The size of the badge. Defaults to .small
    /// - Returns: A styled Badge view
    @ViewBuilder
    static func Badge(
        text: String,
        size: LemonadeBadgeSize = .small
    ) -> some View {
        LemonadeBadgeView(
            text: text,
            size: size
        )
    }
}

// MARK: - Internal Badge View

private struct LemonadeBadgeView: View {
    let text: String
    let size: LemonadeBadgeSize

    // Gradient colors matching KMP implementation
    private let gradientColors = [
        Color(red: 214/255, green: 242/255, blue: 90/255), // #D6F25A
        Color(red: 214/255, green: 242/255, blue: 90/255).opacity(0)
    ]

    var body: some View {
        SwiftUI.Text(text)
            .font(.custom("Figtree", size: size.fontSize).weight(.semibold))
            .foregroundColor(LemonadeTheme.colors.content.contentOnBrandHigh)
            .lineLimit(1)
            .padding(.horizontal, size.textHorizontalPadding)
            .padding(.vertical, size.textVerticalPadding)
            .padding(.horizontal, size.horizontalPadding)
            .frame(height: size.height)
            .background(
                ZStack {
                    Capsule()
                        .fill(LemonadeTheme.colors.background.bgBrand)

                    Capsule()
                        .fill(
                            LinearGradient(
                                colors: gradientColors,
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                }
            )
            .clipShape(Capsule())
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeBadge_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            HStack(spacing: 16) {
                LemonadeUi.Badge(text: "New", size: .xSmall)
                LemonadeUi.Badge(text: "New", size: .small)
            }

            HStack(spacing: 16) {
                LemonadeUi.Badge(text: "5", size: .xSmall)
                LemonadeUi.Badge(text: "99+", size: .small)
            }

            LemonadeUi.Badge(text: "Label", size: .small)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
