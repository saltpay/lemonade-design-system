import SwiftUI

// MARK: - Tile Variant

/// Defines the visual variant for Tile.
public enum LemonadeTileVariant {
    case neutral
    case muted
    case onColor
    case selected

    var backgroundColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.background.bgElevated
        case .muted: return LemonadeTheme.colors.background.bgDefault
        case .onColor, .selected: return LemonadeTheme.colors.background.bgBrandElevated
        }
    }

    var backgroundPressedColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.interaction.bgElevatedPressed
        case .muted: return LemonadeTheme.colors.interaction.bgDefaultPressed
        case .onColor, .selected: return LemonadeTheme.colors.interaction.bgBrandElevatedPressed
        }
    }

    var borderColor: Color {
        switch self {
        case .neutral, .muted: return LemonadeTheme.colors.border.borderNeutralMedium
        case .onColor: return LemonadeTheme.colors.border.borderNeutralMediumInverse
        case .selected: return LemonadeTheme.colors.border.borderNeutralHigh
        }
    }

    var borderWidth: CGFloat {
        switch self {
        case .neutral, .muted, .onColor: return LemonadeTheme.borderWidth.base.border25
        case .selected: return LemonadeTheme.borderWidth.base.border50
        }
    }

    var shadow: LemonadeShadow? {
        switch self {
        case .neutral: return nil
        case .muted: return .xsmall
        case .onColor, .selected: return nil
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
    ///   - stretched: Whether the tile should stretch to fill available width. Defaults to false
    ///   - alignment: Horizontal alignment of the tile content. Defaults to .center
    ///   - addon: Optional content to be displayed as a badge overlay
    /// - Returns: A styled Tile view
    @ViewBuilder
    static func Tile<AddonContent: View>(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .neutral,
        stretched: Bool = false,
        alignment: HorizontalAlignment = .center,
        @ViewBuilder addon: @escaping () -> AddonContent
    ) -> some View {
        LemonadeTileView(
            label: label,
            icon: icon,
            enabled: enabled,
            onClick: onClick,
            variant: variant,
            stretched: stretched,
            alignment: alignment,
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
        variant: LemonadeTileVariant = .neutral,
        stretched: Bool = false,
        alignment: HorizontalAlignment = .center
    ) -> some View {
        LemonadeTileView<EmptyView>(
            label: label,
            icon: icon,
            enabled: enabled,
            onClick: onClick,
            variant: variant,
            stretched: stretched,
            alignment: alignment,
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
    let stretched: Bool
    let alignment: HorizontalAlignment
    let addon: (() -> AddonContent)?

    @State private var isPressed = false

    private let minWidth: CGFloat = 120
    private let minHeight: CGFloat = 80

    private var backgroundColor: Color {
        isPressed ? variant.backgroundPressedColor : variant.backgroundColor
    }

    private var tileContent: some View {
        VStack(alignment: alignment, spacing: LemonadeTheme.spaces.spacing200) {
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
    }

    var body: some View {
        ZStack(alignment: .topTrailing) {
            // Main tile content
            Group {
                if #available(iOS 16, macOS 13, *) {
                    DefaultMinSize(minWidth: minWidth, minHeight: minHeight) {
                        tileContent
                    }
                } else {
                    tileContent
                        .frame(minWidth: minWidth, minHeight: minHeight)
                }
            }
            .padding(.horizontal, LemonadeTheme.spaces.spacing100)
            .padding(.vertical, LemonadeTheme.spaces.spacing400)
            .applyIf(stretched) { $0.frame(maxWidth: .infinity) }
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
            .overlay(
                RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                    .stroke(variant.borderColor, lineWidth: variant.borderWidth)
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

                LemonadeUi.Tile(
                    label: "Selected",
                    icon: .circleCheck,
                    variant: .selected
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

            // Alignment
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Leading",
                    icon: .arrowLeft,
                    variant: .neutral,
                    alignment: .leading
                )

                LemonadeUi.Tile(
                    label: "Center",
                    icon: .arrowLeftRight,
                    variant: .neutral,
                    alignment: .center
                )

                LemonadeUi.Tile(
                    label: "Trailing",
                    icon: .arrowRight,
                    variant: .neutral,
                    alignment: .trailing
                )
            }

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

            // Tight container — tiles shrink below 120pt instead of overflowing
            // Only demonstrates correctly on iOS 16+ where DefaultMinSize is used
            if #available(iOS 16, macOS 13, *) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Tight container (200pt for 3 tiles)")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    HStack(spacing: 8) {
                        LemonadeUi.Tile(label: "One", icon: .heart, variant: .neutral)
                        LemonadeUi.Tile(label: "Two", icon: .star, variant: .neutral)
                        LemonadeUi.Tile(label: "Three", icon: .check, variant: .neutral)
                    }
                    .frame(width: 200)
                }
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
