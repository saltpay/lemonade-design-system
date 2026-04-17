import SwiftUI

// MARK: - Badge Size

/// Size options for the Badge component.
public enum LemonadeBadgeSize {
    case xSmall
    case small
    
    var height: CGFloat {
        switch self {
        case .xSmall: return LemonadeTheme.sizes.size400
        case .small: return LemonadeTheme.sizes.size500
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
        case .xSmall: return LemonadeTheme.sizes.size250
        case .small: return LemonadeTheme.sizes.size300
        }
    }
    
    var lineHeight: CGFloat {
        switch self {
        case .xSmall: return LemonadeTheme.sizes.size350
        case .small: return LemonadeTheme.sizes.size400
        }
    }
}

// MARK: - Badge Component

private struct LemonadeBadgeSizeKey: EnvironmentKey {
    static let defaultValue: LemonadeBadgeSize = .small
}

extension EnvironmentValues {
    var lemonadeBadgeSize: LemonadeBadgeSize {
        get { self[LemonadeBadgeSizeKey.self] }
        set { self[LemonadeBadgeSizeKey.self] = newValue }
    }
}

// MARK: - Modifier

private struct LemonadeBadgeSizeModifier: ViewModifier {
    let size: LemonadeBadgeSize
    
    func body(content: Content) -> some View {
        content.environment(\.lemonadeBadgeSize, size)
    }
}

public extension View {

    /// Sets the size of a `LemonadeBadge`.
    ///
    /// This modifier should only be used with `LemonadeUi.Badge(...)`.
    ///
    /// - Parameter size: The badge size configuration
    /// - Returns: A view with the updated badge size environment value
    func badgeSize(_ size: LemonadeBadgeSize) -> some View {
        modifier(LemonadeBadgeSizeModifier(size: size))
    }
}

// MARK: - Public API

public extension LemonadeUi {

    /// Badge component to highlight new or unread items, or to indicate status.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Badge(text: "New")
    ///     .badgeSize(.small)
    /// ```
    ///
    ///  Parameters:
    ///   - text: The text to be displayed inside the badge
    /// - Returns: A styled Badge view
    @ViewBuilder
    static func Badge(
        text: String
    ) -> some View {
        LemonadeBadgeView(text: text)
    }
    
    @available(*, deprecated, message: "Use LemonadeUi.Badge(text:).badgeSize(_:) instead.")
    @ViewBuilder
    static func Badge(
        text: String,
        size: LemonadeBadgeSize
    ) -> some View {
        LemonadeBadgeView(text: text)
            .badgeSize(size)
    }
}

// MARK: - Internal Badge View

private struct LemonadeBadgeView: View {
    let text: String
    
    @Environment(\.lemonadeBadgeSize)
    private var size
    
    private var gradientColors: [Color] {
        let brandHighlight = LemonadeTheme.colors.background.bgBrandHigh
        return [
            brandHighlight,
            brandHighlight.opacity(0)
        ]
    }
    
    var body: some View {
        Text(text)
            .font(.custom(LemonadeTypography.fontFamily, size: size.fontSize).weight(.semibold))
            .foregroundStyle(LemonadeTheme.colors.content.contentOnBrandHigh)
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
                LemonadeUi.Badge(text: "New")
                    .badgeSize(.xSmall)
                
                LemonadeUi.Badge(text: "New")
                    .badgeSize(.small)
            }
            
            HStack(spacing: 16) {
                LemonadeUi.Badge(text: "5")
                    .badgeSize(.xSmall)
                
                LemonadeUi.Badge(text: "99+")
                    .badgeSize(.small)
            }
            
            LemonadeUi.Badge(text: "Label")
                .badgeSize(.small)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
