import SwiftUI

// MARK: - Tile Variant

/// Defines the visual variant for Tile.
public enum LemonadeTileVariant {
    case neutral
    case muted
    case onColor

    var backgroundColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.background.bgElevated
        case .muted: return LemonadeTheme.colors.background.bgDefault
        case .onColor: return LemonadeTheme.colors.background.bgBrandElevated
        }
    }

    var backgroundPressedColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.interaction.bgElevatedPressed
        case .muted: return LemonadeTheme.colors.interaction.bgDefaultPressed
        case .onColor: return LemonadeTheme.colors.interaction.bgBrandElevatedPressed
        }
    }

    var borderColor: Color {
        switch self {
        case .neutral, .muted: return LemonadeTheme.colors.border.borderNeutralMedium
        case .onColor: return LemonadeTheme.colors.border.borderNeutralMediumInverse
        }
    }

    var shadow: LemonadeShadow? {
        switch self {
        case .neutral: return nil
        case .muted: return .xsmall
        case .onColor: return nil
        }
    }
}

// MARK: - Tile Component

public extension LemonadeUi {
    /// A tile component with icon, label, and optional addon.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tile(
    ///     label: "Label",
    ///     icon: .heart,
    ///     variant: .neutral,
    ///     onClick: { /* action */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the tile
    ///   - icon: LemonadeIcon to be displayed above the label
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - onClick: Callback called when component is tapped
    ///   - variant: LemonadeTileVariant to define visual style. Defaults to .neutral
    ///   - addon: Optional content to be displayed as a badge overlay
    /// - Returns: A styled Tile view
    @ViewBuilder
    static func Tile<AddonContent: View>(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .neutral,
        @ViewBuilder addon: @escaping () -> AddonContent
    ) -> some View {
        LemonadeTileView(
            label: label,
            icon: icon,
            enabled: enabled,
            onClick: onClick,
            variant: variant,
            addon: addon
        )
    }

    /// A tile component without addon.
    @ViewBuilder
    static func Tile(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .neutral
    ) -> some View {
        LemonadeTileView<EmptyView>(
            label: label,
            icon: icon,
            enabled: enabled,
            onClick: onClick,
            variant: variant,
            addon: nil
        )
    }
}

// MARK: - Internal Tile View

private struct LemonadeTileView<AddonContent: View>: View {
    let label: String
    let icon: LemonadeIcon
    let enabled: Bool
    let onClick: (() -> Void)?
    let variant: LemonadeTileVariant
    let addon: (() -> AddonContent)?

    @State private var isPressed = false

    private let minWidth: CGFloat = 120

    private var backgroundColor: Color {
        isPressed ? variant.backgroundPressedColor : variant.backgroundColor
    }

    var body: some View {
        ZStack(alignment: .topTrailing) {
            // Main tile content
            VStack(spacing: LemonadeTheme.spaces.spacing200) {
                LemonadeUi.Icon(
                    icon: icon,
                    contentDescription: nil,
                    size: .medium
                )

                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodyMediumMedium,
                    color: LemonadeTheme.colors.content.contentPrimary,
                    overflow: .tail,
                    maxLines: 1
                )
            }
            .frame(minWidth: minWidth)
            .padding(.horizontal, LemonadeTheme.spaces.spacing100)
            .padding(.vertical, LemonadeTheme.spaces.spacing400)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
            .overlay(
                RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                    .stroke(variant.borderColor, lineWidth: LemonadeTheme.borderWidth.base.border25)
            )
            .applyIf(variant.shadow != nil) { view in
                view.lemonadeShadow(variant.shadow!)
            }
            .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
            .simultaneousGesture(
                onClick != nil && enabled
                    ? DragGesture(minimumDistance: 0)
                        .onChanged { _ in isPressed = true }
                        .onEnded { _ in
                            isPressed = false
                            onClick?()
                        }
                    : nil
            )
            .animation(.easeInOut(duration: 0.15), value: isPressed)

            // Addon badge
            if let addon = addon {
                addon()
                    .offset(
                        x: LemonadeTheme.spaces.spacing200,
                        y: -LemonadeTheme.spaces.spacing200
                    )
            }
        }
    }
}

// MARK: - View Extension Helper

private extension View {
    @ViewBuilder
    func applyIf<T: View>(_ condition: Bool, transform: (Self) -> T) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeTile_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // All variants
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Neutral",
                    icon: .heart,
                    variant: .neutral
                )

                LemonadeUi.Tile(
                    label: "Muted",
                    icon: .star,
                    variant: .muted
                )
            }

            // OnColor variant (needs brand background)
            LemonadeUi.Tile(
                label: "OnColor",
                icon: .check,
                variant: .onColor
            )
            .padding()
            .background(LemonadeTheme.colors.background.bgBrand)

            // With addon
            LemonadeUi.Tile(
                label: "With Addon",
                icon: .heart,
                variant: .neutral
            ) {
                LemonadeUi.Badge(text: "New", size: .xSmall)
            }

            // Disabled
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Disabled",
                    icon: .heart,
                    enabled: false,
                    variant: .neutral
                )
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
