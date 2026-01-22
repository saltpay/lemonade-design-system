import SwiftUI

// MARK: - Button Variant

/// Button variants following the Lemonade Design System.
public enum LemonadeButtonVariant {
    case primary
    case secondary
    case neutral
    case critical
    case special
}

// MARK: - Button Size

/// Button sizes following the Lemonade Design System.
public enum LemonadeButtonSize {
    case small
    case medium
    case large
}

// MARK: - Button Component

public extension LemonadeUi {
    /// Lemonade labeled button component. Used for simple click actions with a text and optional icons.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Button(
    ///     label: "Click me!",
    ///     onClick: { print("Button clicked!") }
    /// )
    ///
    /// // Full width button using standard SwiftUI modifier
    /// LemonadeUi.Button(
    ///     label: "Full Width",
    ///     onClick: { }
    /// )
    /// .frame(maxWidth: .infinity)
    /// ```
    ///
    /// - Parameters:
    ///   - label: String to be displayed as the Button's label
    ///   - onClick: Callback to be invoked when the Button is clicked
    ///   - leadingIcon: LemonadeIcon shown before the label
    ///   - trailingIcon: LemonadeIcon shown after the label
    ///   - variant: LemonadeButtonVariant to style the Button accordingly
    ///   - size: LemonadeButtonSize to size the Button accordingly
    ///   - enabled: Boolean flag to enable or disable the Button
    /// - Returns: A styled Button view
    @ViewBuilder
    static func Button(
        label: String,
        onClick: @escaping () -> Void,
        leadingIcon: LemonadeIcon? = nil,
        trailingIcon: LemonadeIcon? = nil,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        enabled: Bool = true
    ) -> some View {
        LemonadeButtonView(
            label: label,
            onClick: onClick,
            leadingIcon: leadingIcon,
            trailingIcon: trailingIcon,
            variant: variant,
            size: size,
            enabled: enabled
        )
    }
}

// MARK: - Internal Button Colors

private struct LemonadeButtonColors {
    let contentColor: Color
    let solidBackgroundColor: Color
    let pressedBackgroundColor: Color
    let brushBackgroundColors: [Color]?

    init(
        contentColor: Color,
        solidBackgroundColor: Color,
        pressedBackgroundColor: Color,
        brushBackgroundColors: [Color]? = nil
    ) {
        self.contentColor = contentColor
        self.solidBackgroundColor = solidBackgroundColor
        self.pressedBackgroundColor = pressedBackgroundColor
        self.brushBackgroundColors = brushBackgroundColors
    }
}

// MARK: - Internal Button Content Data

private struct LemonadeButtonContentData {
    let verticalPadding: CGFloat
    let horizontalPadding: CGFloat
    let minHeight: CGFloat
    let minWidth: CGFloat
    let cornerRadius: CGFloat
    let textStyle: LemonadeTextStyle
}

// MARK: - Button Size Extension

private extension LemonadeButtonSize {
    var contentData: LemonadeButtonContentData {
        switch self {
        case .small:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing200,
                horizontalPadding: LemonadeTheme.spaces.spacing300,
                minHeight: LemonadeTheme.sizes.size1000,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius300,
                textStyle: LemonadeTypography().bodySmallSemiBold
            )
        case .medium:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing300,
                horizontalPadding: LemonadeTheme.spaces.spacing400,
                minHeight: LemonadeTheme.sizes.size1200,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius300,
                textStyle: LemonadeTypography().bodyMediumSemiBold
            )
        case .large:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing300,
                horizontalPadding: LemonadeTheme.spaces.spacing400,
                minHeight: LemonadeTheme.sizes.size1400,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius400,
                textStyle: LemonadeTypography().bodyMediumSemiBold
            )
        }
    }
}

// MARK: - Button Variant Extension

private extension LemonadeButtonVariant {
    var variantData: LemonadeButtonColors {
        switch self {
        case .primary:
            return LemonadeButtonColors(
                contentColor: LemonadeTheme.colors.content.contentOnBrandHigh,
                solidBackgroundColor: LemonadeTheme.colors.background.bgBrand,
                pressedBackgroundColor: LemonadeTheme.colors.interaction.bgBrandInteractive
            )
        case .secondary:
            return LemonadeButtonColors(
                contentColor: LemonadeTheme.colors.content.contentPrimaryInverse,
                solidBackgroundColor: LemonadeTheme.colors.background.bgSubtleInverse,
                pressedBackgroundColor: LemonadeTheme.colors.interaction.bgNeutralPressed
            )
        case .neutral:
            return LemonadeButtonColors(
                contentColor: LemonadeTheme.colors.content.contentPrimary,
                solidBackgroundColor: LemonadeTheme.colors.background.bgElevated,
                pressedBackgroundColor: LemonadeTheme.colors.interaction.bgElevatedPressed
            )
        case .critical:
            return LemonadeButtonColors(
                contentColor: LemonadeTheme.colors.content.contentCritical,
                solidBackgroundColor: LemonadeTheme.colors.background.bgCriticalSubtle,
                pressedBackgroundColor: LemonadeTheme.colors.interaction.bgCriticalSubtleInteractive
            )
        case .special:
            return LemonadeButtonColors(
                contentColor: LemonadeTheme.colors.content.contentOnBrandHigh,
                solidBackgroundColor: LemonadeTheme.colors.background.bgBrand,
                pressedBackgroundColor: LemonadeTheme.colors.interaction.bgBrandInteractive,
                brushBackgroundColors: [
                    LemonadeTheme.colors.background.bgBrandElevated,
                    LemonadeTheme.colors.background.bgBrandElevated.opacity(0)
                ]
            )
        }
    }
}

// MARK: - Internal Button View

private struct LemonadeButtonView: View {
    let label: String
    let onClick: () -> Void
    let leadingIcon: LemonadeIcon?
    let trailingIcon: LemonadeIcon?
    let variant: LemonadeButtonVariant
    let size: LemonadeButtonSize
    let enabled: Bool

    @State private var isPressed = false

    private var backgroundColor: Color {
        isPressed ? variant.variantData.pressedBackgroundColor : variant.variantData.solidBackgroundColor
    }

    var body: some View {
        SwiftUI.Button(action: onClick) {
            HStack(spacing: 0) {
                Spacer(minLength: 0)

                if let leadingIcon = leadingIcon {
                    LemonadeUi.Icon(
                        icon: leadingIcon,
                        contentDescription: nil,
                        size: .medium,
                        tint: variant.variantData.contentColor
                    )
                }

                LemonadeUi.Text(
                    label,
                    textStyle: size.contentData.textStyle,
                    color: variant.variantData.contentColor
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing200)

                if let trailingIcon = trailingIcon {
                    LemonadeUi.Icon(
                        icon: trailingIcon,
                        contentDescription: nil,
                        size: .medium,
                        tint: variant.variantData.contentColor
                    )
                }

                Spacer(minLength: 0)
            }
            .padding(.horizontal, size.contentData.horizontalPadding)
            .frame(height: size.contentData.minHeight)
            .frame(minWidth: size.contentData.minWidth)
            .background(
                ZStack {
                    RoundedRectangle(cornerRadius: size.contentData.cornerRadius)
                        .fill(backgroundColor)

                    if let gradientColors = variant.variantData.brushBackgroundColors {
                        RoundedRectangle(cornerRadius: size.contentData.cornerRadius)
                            .fill(
                                LinearGradient(
                                    colors: gradientColors,
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            )
                    }
                }
            )
            .clipShape(RoundedRectangle(cornerRadius: size.contentData.cornerRadius))
        }
        .buttonStyle(LemonadeButtonStyle(isPressed: $isPressed))
        .disabled(!enabled)
        .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
    }
}

// MARK: - Button Style

private struct LemonadeButtonStyle: ButtonStyle {
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
struct LemonadeButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 16) {
            // Primary variants
            LemonadeUi.Button(
                label: "Primary",
                onClick: {}
            )

            LemonadeUi.Button(
                label: "Secondary",
                onClick: {},
                variant: .secondary
            )

            LemonadeUi.Button(
                label: "Neutral",
                onClick: {},
                variant: .neutral
            )

            LemonadeUi.Button(
                label: "Critical",
                onClick: {},
                variant: .critical
            )

            LemonadeUi.Button(
                label: "Special",
                onClick: {},
                variant: .special
            )

            // With icons
            LemonadeUi.Button(
                label: "With Icons",
                onClick: {},
                leadingIcon: .airplane,
                trailingIcon: .chevronRight
            )

            // Sizes
            HStack(spacing: 8) {
                LemonadeUi.Button(
                    label: "Small",
                    onClick: {},
                    size: .small
                )

                LemonadeUi.Button(
                    label: "Medium",
                    onClick: {},
                    size: .medium
                )

                LemonadeUi.Button(
                    label: "Large",
                    onClick: {},
                    size: .large
                )
            }

            // Disabled
            LemonadeUi.Button(
                label: "Disabled",
                onClick: {},
                enabled: false
            )

            // Full width
            LemonadeUi.Button(
                label: "Full Width",
                onClick: {}
            )
            .frame(maxWidth: .infinity)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
