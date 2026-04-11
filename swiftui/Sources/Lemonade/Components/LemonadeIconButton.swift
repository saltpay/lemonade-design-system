import SwiftUI

// MARK: - Button Variant (shared)

/// Color variants for buttons, determining the color palette.
public enum LemonadeButtonVariant {
    case primary
    case secondary
    case neutral
    case critical
}

// MARK: - Button Type (shared)

/// Fill treatment for buttons.
public enum LemonadeButtonType {
    case solid
    case subtle
    case ghost
}

// MARK: - Icon Button Shape

/// Shape options for the icon button.
public enum LemonadeIconButtonShape {
    case rounded
    case circular
}

// MARK: - Icon Button Component

public extension LemonadeUi {
    /// Lemonade icon button component. Used for simple click actions with only an icon.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.IconButton(
    ///     icon: .heart,
    ///     contentDescription: "Favorite",
    ///     onClick: { print("icon button clicked!") }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - icon: LemonadeIcon to be displayed as the Button's icon
    ///   - contentDescription: String content description for accessibility
    ///   - onClick: Callback to be invoked when the Button is clicked
    ///   - enabled: Boolean flag to enable or disable the Button
    ///   - variant: LemonadeButtonVariant for the color palette (primary, secondary, neutral, critical)
    ///   - type: LemonadeButtonType for the fill treatment (solid, subtle, ghost)
    ///   - size: LemonadeButtonSize to size the Button accordingly
    ///   - loading: Boolean flag to show a loading spinner
    ///   - shape: LemonadeIconButtonShape for the button shape (rounded, circular)
    /// - Returns: A styled IconButton view
    @ViewBuilder
    static func IconButton(
        icon: LemonadeIcon,
        contentDescription: String?,
        onClick: @escaping () -> Void,
        enabled: Bool = true,
        variant: LemonadeButtonVariant = .neutral,
        type: LemonadeButtonType = .subtle,
        size: LemonadeButtonSize = .medium,
        loading: Bool = false,
        shape: LemonadeIconButtonShape = .rounded
    ) -> some View {
        LemonadeIconButtonView(
            icon: icon,
            contentDescription: contentDescription,
            onClick: onClick,
            enabled: enabled,
            variant: variant,
            type: type,
            size: size,
            loading: loading,
            shape: shape
        )
    }
}

// MARK: - Internal Icon Button Colors

private struct LemonadeIconButtonColors {
    let backgroundColor: Color
    let backgroundHoverColor: Color
    let backgroundPressedColor: Color
    let contentColor: Color
}

// MARK: - Internal Icon Button Size Data

private struct LemonadeIconButtonSizeData {
    let iconSize: LemonadeUiIconSize
    let innerPadding: CGFloat
    let cornerRadius: CGFloat
}

// MARK: - Icon Button Size Extension

private extension LemonadeButtonSize {
    var iconButtonSizeData: LemonadeIconButtonSizeData {
        switch self {
        case .large:
            return LemonadeIconButtonSizeData(
                iconSize: .large,
                innerPadding: LemonadeTheme.spaces.spacing400,
                cornerRadius: LemonadeTheme.radius.radius400
            )
        case .medium:
            return LemonadeIconButtonSizeData(
                iconSize: .large,
                innerPadding: LemonadeTheme.spaces.spacing200,
                cornerRadius: LemonadeTheme.radius.radius300
            )
        case .small, .xSmall:
            return LemonadeIconButtonSizeData(
                iconSize: .small,
                innerPadding: LemonadeTheme.spaces.spacing200,
                cornerRadius: LemonadeTheme.radius.radius300
            )
        }
    }
}

// MARK: - Color Resolution (Variant x Type)

private func resolveColors(
    variant: LemonadeButtonVariant,
    type: LemonadeButtonType
) -> LemonadeIconButtonColors {
    switch (variant, type) {
    // MARK: Primary
    case (.primary, .solid):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgBrand,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgBrandInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgBrandPressed,
            contentColor: LemonadeTheme.colors.content.contentOnBrandHigh
        )
    case (.primary, .subtle):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgBrandSubtle,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentBrandHigh
        )
    case (.primary, .ghost):
        return LemonadeIconButtonColors(
            backgroundColor: Color.clear,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentBrandHigh
        )

    // MARK: Secondary
    case (.secondary, .solid):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgSubtleInverse,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgNeutralInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralPressed,
            contentColor: LemonadeTheme.colors.content.contentPrimaryInverse
        )
    case (.secondary, .subtle):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgNeutralSubtle,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgNeutralSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentPrimary
        )
    case (.secondary, .ghost):
        return LemonadeIconButtonColors(
            backgroundColor: Color.clear,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentPrimary
        )

    // MARK: Neutral
    case (.neutral, .solid):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgElevated,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgElevatedInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgElevatedPressed,
            contentColor: LemonadeTheme.colors.content.contentPrimary
        )
    case (.neutral, .subtle):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgNeutralSubtle,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgNeutralSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentPrimary
        )
    case (.neutral, .ghost):
        return LemonadeIconButtonColors(
            backgroundColor: Color.clear,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentPrimary
        )

    // MARK: Critical
    case (.critical, .solid):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgCritical,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgCriticalInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgCriticalPressed,
            contentColor: LemonadeTheme.colors.content.contentAlwaysLight
        )
    case (.critical, .subtle):
        return LemonadeIconButtonColors(
            backgroundColor: LemonadeTheme.colors.background.bgCriticalSubtle,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgCriticalSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgCriticalSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentCritical
        )
    case (.critical, .ghost):
        return LemonadeIconButtonColors(
            backgroundColor: Color.clear,
            backgroundHoverColor: LemonadeTheme.colors.interaction.bgSubtleInteractive,
            backgroundPressedColor: LemonadeTheme.colors.interaction.bgCriticalSubtlePressed,
            contentColor: LemonadeTheme.colors.content.contentCritical
        )
    }
}

// MARK: - Internal Icon Button View

private struct LemonadeIconButtonView: View {
    let icon: LemonadeIcon
    let contentDescription: String?
    let onClick: () -> Void
    let enabled: Bool
    let variant: LemonadeButtonVariant
    let type: LemonadeButtonType
    let size: LemonadeButtonSize
    let loading: Bool
    let shape: LemonadeIconButtonShape

    @State private var isPressed = false
    @State private var isHovering = false

    private var cornerRadius: CGFloat {
        shape == .circular
            ? .infinity
            : size.iconButtonSizeData.cornerRadius
    }

    var body: some View {
        let colors = resolveColors(variant: variant, type: type)
        let bgColor: Color = isPressed
            ? colors.backgroundPressedColor
            : isHovering ? colors.backgroundHoverColor : colors.backgroundColor
        let buttonShape = RoundedRectangle(cornerRadius: cornerRadius)

        SwiftUI.Button(action: onClick) {
            Group {
                if loading {
                    LemonadeUi.Spinner(tint: colors.contentColor)
                } else {
                    LemonadeUi.Icon(
                        icon: icon,
                        contentDescription: contentDescription,
                        size: size.iconButtonSizeData.iconSize,
                        tint: colors.contentColor
                    )
                }
            }
            .padding(size.iconButtonSizeData.innerPadding)
            .background(
                buttonShape
                    .fill(bgColor)
                    .animation(.easeInOut(duration: 0.1), value: bgColor)
            )
            .clipShape(buttonShape)
        }
        .buttonStyle(LemonadePressTrackingButtonStyle(isPressed: $isPressed))
        .onHover { hovering in
            isHovering = hovering
        }
        .disabled(!enabled || loading)
        .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeIconButton_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Primary Solid
                previewSection(title: "Primary Solid", variant: .primary, type: .solid)

                // Secondary Solid
                previewSection(title: "Secondary Solid", variant: .secondary, type: .solid)

                // Neutral Subtle (default)
                previewSection(title: "Neutral Subtle", variant: .neutral, type: .subtle)

                // Neutral Ghost
                previewSection(title: "Neutral Ghost", variant: .neutral, type: .ghost)

                // Critical Subtle
                previewSection(title: "Critical Subtle", variant: .critical, type: .subtle)

                // Critical Solid
                previewSection(title: "Critical Solid", variant: .critical, type: .solid)

                // Loading states
                HStack(spacing: 16) {
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Loading",
                        onClick: {},
                        variant: .primary,
                        type: .solid,
                        loading: true
                    )
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Loading",
                        onClick: {},
                        variant: .neutral,
                        type: .subtle,
                        loading: true
                    )
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Loading",
                        onClick: {},
                        variant: .critical,
                        type: .solid,
                        loading: true
                    )
                }

                // Circular shape
                HStack(spacing: 16) {
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Circular",
                        onClick: {},
                        variant: .primary,
                        type: .solid,
                        shape: .circular
                    )
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Circular",
                        onClick: {},
                        variant: .neutral,
                        type: .subtle,
                        shape: .circular
                    )
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Circular",
                        onClick: {},
                        variant: .critical,
                        type: .solid,
                        shape: .circular
                    )
                }

                // Disabled
                HStack(spacing: 16) {
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Disabled",
                        onClick: {},
                        enabled: false,
                        variant: .primary,
                        type: .solid
                    )
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Disabled",
                        onClick: {},
                        enabled: false,
                        variant: .neutral,
                        type: .subtle
                    )
                    LemonadeUi.IconButton(
                        icon: .heart,
                        contentDescription: "Disabled",
                        onClick: {},
                        enabled: false,
                        variant: .neutral,
                        type: .ghost
                    )
                }
            }
            .padding()
        }
        .previewLayout(.sizeThatFits)
    }

    private static func previewSection(
        title: String,
        variant: LemonadeButtonVariant,
        type: LemonadeButtonType
    ) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            SwiftUI.Text(title).font(.caption).foregroundColor(.secondary)
            HStack(spacing: 16) {
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: title,
                    onClick: {},
                    variant: variant,
                    type: type,
                    size: .small
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: title,
                    onClick: {},
                    variant: variant,
                    type: type,
                    size: .medium
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: title,
                    onClick: {},
                    variant: variant,
                    type: type,
                    size: .large
                )
            }
        }
    }
}
#endif
