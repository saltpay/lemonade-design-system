import SwiftUI

// MARK: - Icon Button Variant

/// Icon button variants following the Lemonade Design System.
public enum LemonadeIconButtonVariant {
    case ghost
    case subtle
}

// MARK: - Icon Button Size

/// Icon button sizes following the Lemonade Design System.
public enum LemonadeIconButtonSize {
    case small
    case medium
    case large
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
    ///   - variant: LemonadeIconButtonVariant to style the Button accordingly
    ///   - size: LemonadeIconButtonSize to size the Button accordingly
    /// - Returns: A styled IconButton view
    @ViewBuilder
    static func IconButton(
        icon: LemonadeIcon,
        contentDescription: String?,
        onClick: @escaping () -> Void,
        enabled: Bool = true,
        variant: LemonadeIconButtonVariant = .subtle,
        size: LemonadeIconButtonSize = .medium
    ) -> some View {
        LemonadeIconButtonView(
            icon: icon,
            contentDescription: contentDescription,
            onClick: onClick,
            enabled: enabled,
            variant: variant,
            size: size
        )
    }
}

// MARK: - Internal Icon Button Colors

private struct LemonadeIconButtonColors {
    let backgroundColor: Color
    let backgroundHoverColor: Color
    let backgroundPressedColor: Color
}

// MARK: - Internal Icon Button Size Data

private struct LemonadeIconButtonSizeData {
    let iconSize: LemonadeUiIconSize
    let innerPadding: CGFloat
    let cornerRadius: CGFloat
}

// MARK: - Icon Button Size Extension

private extension LemonadeIconButtonSize {
    var sizeData: LemonadeIconButtonSizeData {
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
        case .small:
            return LemonadeIconButtonSizeData(
                iconSize: .small,
                innerPadding: LemonadeTheme.spaces.spacing200,
                cornerRadius: LemonadeTheme.radius.radius300
            )
        }
    }
}

// MARK: - Icon Button Variant Extension

private extension LemonadeIconButtonVariant {
    var colorData: LemonadeIconButtonColors {
        switch self {
        case .ghost:
            return LemonadeIconButtonColors(
                backgroundColor: Color.clear,
                backgroundHoverColor: LemonadeTheme.colors.interaction.bgSubtleInteractive,
                backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralSubtlePressed
            )
        case .subtle:
            return LemonadeIconButtonColors(
                backgroundColor: LemonadeTheme.colors.background.bgNeutralSubtle,
                backgroundHoverColor: LemonadeTheme.colors.interaction.bgNeutralSubtleInteractive,
                backgroundPressedColor: LemonadeTheme.colors.interaction.bgNeutralSubtlePressed
            )
        }
    }
}

// MARK: - Internal Icon Button View

private struct LemonadeIconButtonView: View {
    let icon: LemonadeIcon
    let contentDescription: String?
    let onClick: () -> Void
    let enabled: Bool
    let variant: LemonadeIconButtonVariant
    let size: LemonadeIconButtonSize

    @State private var isPressed = false
    @State private var isHovering = false

    private var backgroundColor: Color {
        if isPressed {
            return variant.colorData.backgroundPressedColor
        } else if isHovering {
            return variant.colorData.backgroundHoverColor
        } else {
            return variant.colorData.backgroundColor
        }
    }

    var body: some View {
        SwiftUI.Button(action: onClick) {
            LemonadeUi.Icon(
                icon: icon,
                contentDescription: contentDescription,
                size: size.sizeData.iconSize
            )
            .padding(size.sizeData.innerPadding)
            .background(
                RoundedRectangle(cornerRadius: size.sizeData.cornerRadius)
                    .fill(backgroundColor)
                    .animation(.easeInOut(duration: 0.1), value: backgroundColor)
            )
            .clipShape(RoundedRectangle(cornerRadius: size.sizeData.cornerRadius))
        }
        .buttonStyle(LemonadeIconButtonStyle(isPressed: $isPressed))
        .onHover { hovering in
            isHovering = hovering
        }
        .disabled(!enabled)
        .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
    }
}

// MARK: - Icon Button Style

private struct LemonadeIconButtonStyle: ButtonStyle {
    @Binding var isPressed: Bool

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .onChange(of: configuration.isPressed) { newValue in
                withAnimation(.easeInOut(duration: 0.1)) {
                    isPressed = newValue
                }
            }
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeIconButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Subtle variant
            HStack(spacing: 16) {
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    variant: .subtle,
                    size: .small
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    variant: .subtle,
                    size: .medium
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    variant: .subtle,
                    size: .large
                )
            }

            // Ghost variant
            HStack(spacing: 16) {
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    variant: .ghost,
                    size: .small
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    variant: .ghost,
                    size: .medium
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    variant: .ghost,
                    size: .large
                )
            }

            // Disabled
            HStack(spacing: 16) {
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    enabled: false,
                    variant: .subtle
                )
                LemonadeUi.IconButton(
                    icon: .heart,
                    contentDescription: "Favorite",
                    onClick: {},
                    enabled: false,
                    variant: .ghost
                )
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
