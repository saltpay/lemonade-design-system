import SwiftUI

// MARK: - Tile Variant

/// Defines the visual variant for Tile.
public enum LemonadeTileVariant {
    case filled
    case outlined

    @available(*, deprecated, renamed: "filled", message: "Use .filled instead")
    case neutral
    @available(*, deprecated, renamed: "outlined", message: "Use .outlined instead")
    case muted
    @available(*, deprecated, message: "OnColor variant has been removed. Use .filled instead")
    case onColor
    @available(*, deprecated, message: "Use .filled with isSelected = true instead")
    case selected

    var backgroundColor: Color {
        switch self {
        case .filled, .neutral: return LemonadeTheme.colors.background.bgElevated
        case .outlined, .muted: return LemonadeTheme.colors.background.bgDefault
        case .onColor: return LemonadeTheme.colors.background.bgBrandElevated
        case .selected: return LemonadeTheme.colors.background.bgBrandSubtle
        }
    }

    var backgroundPressedColor: Color {
        switch self {
        case .filled, .neutral: return LemonadeTheme.colors.interaction.bgElevatedPressed
        case .outlined, .muted: return LemonadeTheme.colors.interaction.bgDefaultPressed
        case .onColor, .selected: return LemonadeTheme.colors.interaction.bgBrandElevatedPressed
        }
    }

    var borderColor: Color {
        switch self {
        case .filled, .neutral, .outlined, .muted: return LemonadeTheme.colors.border.borderNeutralMedium
        case .onColor: return LemonadeTheme.colors.border.borderNeutralMediumInverse
        case .selected: return LemonadeTheme.colors.border.borderSelected
        }
    }

    var borderWidth: CGFloat {
        switch self {
        case .filled, .neutral, .outlined, .muted, .onColor: return LemonadeTheme.borderWidth.base.border25
        case .selected: return LemonadeTheme.borderWidth.base.border50
        }
    }

    var shadow: LemonadeShadow? {
        switch self {
        case .filled, .neutral: return nil
        case .outlined, .muted: return .xsmall
        case .onColor, .selected: return nil
        }
    }
}

// MARK: - Tile Component

public extension LemonadeUi {
    /// A tile component with icon and label.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tile(
    ///     label: "Label",
    ///     icon: .heart,
    ///     variant: .filled,
    ///     onClick: { /* action */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the tile
    ///   - icon: LemonadeIcon to be displayed above the label
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - isSelected: Whether the tile is in a selected state. Defaults to false
    ///   - supportText: Optional secondary text displayed below the label
    ///   - onClick: Callback called when component is tapped
    ///   - variant: LemonadeTileVariant to define visual style. Defaults to .filled
    ///   - stretched: Whether the tile should stretch to fill available width. Defaults to false
    ///   - alignment: Horizontal alignment of the tile content. Defaults to .center
    /// - Returns: A styled Tile view
    @ViewBuilder
    static func Tile(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        isSelected: Bool = false,
        supportText: String? = nil,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .filled,
        stretched: Bool = false,
        alignment: HorizontalAlignment = .center
    ) -> some View {
        LemonadeTileView(
            label: label,
            icon: icon,
            enabled: enabled,
            isSelected: isSelected,
            supportText: supportText,
            onClick: onClick,
            variant: variant,
            stretched: stretched,
            alignment: alignment
        )
    }
}

// MARK: - Internal Tile View

private struct LemonadeTileView: View {
    let label: String
    let icon: LemonadeIcon
    let enabled: Bool
    let isSelected: Bool
    let supportText: String?
    let onClick: (() -> Void)?
    let variant: LemonadeTileVariant
    let stretched: Bool
    let alignment: HorizontalAlignment

    private let minWidth: CGFloat = 120
    private let minHeight: CGFloat = 88

    private var effectiveBackgroundColor: Color {
        isSelected ? LemonadeTheme.colors.background.bgBrandSubtle : variant.backgroundColor
    }

    private var effectiveBorderColor: Color {
        isSelected ? LemonadeTheme.colors.border.borderSelected : variant.borderColor
    }

    private var effectiveBorderWidth: CGFloat {
        isSelected ? LemonadeTheme.borderWidth.base.border50 : variant.borderWidth
    }

    private var effectiveShadow: LemonadeShadow? {
        isSelected ? nil : variant.shadow
    }

    private var tileContent: some View {
        VStack(alignment: alignment, spacing: LemonadeTheme.spaces.spacing300) {
            LemonadeUi.Icon(
                icon: icon,
                contentDescription: nil,
                size: .medium
            )

            Spacer()

            VStack(alignment: alignment, spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodySmallMedium,
                    color: LemonadeTheme.colors.content.contentPrimary,
                    overflow: .tail,
                    maxLines: 1
                )

                if let supportText {
                    LemonadeUi.Text(
                        supportText,
                        textStyle: LemonadeTypography.shared.bodySmallRegular,
                        color: LemonadeTheme.colors.content.contentSecondary,
                        overflow: .tail,
                        maxLines: 1
                    )
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: Alignment(horizontal: alignment, vertical: .center))
        .padding(LemonadeTheme.spaces.spacing300)
    }

    var body: some View {
        Group {
            if #available(iOS 16, macOS 13, *) {
                DefaultMinSize(minWidth: minWidth) {
                    tileContent
                }
            } else {
                tileContent
                    .frame(minWidth: minWidth)
            }
        }
        .frame(minHeight: minHeight)
        .applyIf(stretched) { $0.frame(maxWidth: .infinity) }
        .background(effectiveBackgroundColor)
        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .overlay(
            RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                .stroke(effectiveBorderColor, lineWidth: effectiveBorderWidth)
        )
        .applyIf(effectiveShadow != nil) { view in
            view.lemonadeShadow(self.effectiveShadow!)
        }
        .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
        .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .onTapGesture {
            if let onClick = onClick, enabled {
                onClick()
            }
        }
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeTile_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // New variants
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Filled",
                    icon: .heart,
                    variant: .filled
                )

                LemonadeUi.Tile(
                    label: "Outlined",
                    icon: .star,
                    variant: .outlined
                )

                LemonadeUi.Tile(
                    label: "Selected",
                    icon: .circleCheck,
                    isSelected: true,
                    variant: .filled
                )
            }

            // With support text
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Filled",
                    icon: .heart,
                    supportText: "Support",
                    variant: .filled
                )

                LemonadeUi.Tile(
                    label: "Outlined",
                    icon: .star,
                    supportText: "Support",
                    variant: .outlined
                )
            }

            // Alignment
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Leading",
                    icon: .arrowLeft,
                    variant: .filled,
                    alignment: .leading
                )

                LemonadeUi.Tile(
                    label: "Center",
                    icon: .arrowLeftRight,
                    variant: .filled,
                    alignment: .center
                )

                LemonadeUi.Tile(
                    label: "Trailing",
                    icon: .arrowRight,
                    variant: .filled,
                    alignment: .trailing
                )
            }

            // Disabled
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Disabled",
                    icon: .heart,
                    enabled: false,
                    variant: .filled
                )
            }

            // Tight container -- tiles shrink below 120pt instead of overflowing
            // Only demonstrates correctly on iOS 16+ where DefaultMinSize is used
            if #available(iOS 16, macOS 13, *) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Tight container (200pt for 3 tiles)")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    HStack(spacing: 8) {
                        LemonadeUi.Tile(label: "One", icon: .heart, variant: .filled)
                        LemonadeUi.Tile(label: "Two", icon: .star, variant: .filled)
                        LemonadeUi.Tile(label: "Three", icon: .check, variant: .filled)
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
