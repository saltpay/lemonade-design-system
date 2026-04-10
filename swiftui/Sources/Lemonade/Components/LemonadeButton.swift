import SwiftUI

// MARK: - Button Variant

/// Button variants following the Lemonade Design System.
public enum LemonadeButtonVariant {
    case primary
    case secondary
    case neutral
    case critical
    case criticalSolid
    case special
}

// MARK: - Button Size

/// Button sizes following the Lemonade Design System.
public enum LemonadeButtonSize {
    case xSmall
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
    ///   - loading: Boolean flag to show a loading spinner and disable interaction. Unlike `enabled`, loading keeps full opacity.
    /// - Returns: A styled Button view
    @ViewBuilder
    static func Button(
        label: String,
        onClick: @escaping () -> Void,
        leadingIcon: LemonadeIcon? = nil,
        trailingIcon: LemonadeIcon? = nil,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        enabled: Bool = true,
        loading: Bool = false
    ) -> some View {
        LemonadeButtonView(
            label: label,
            onClick: onClick,
            leadingIcon: leadingIcon,
            trailingIcon: trailingIcon,
            variant: variant,
            size: size,
            enabled: enabled,
            loading: loading
        )
    }
}

// MARK: - Slot-based Button Component

public extension LemonadeUi {
    /// Lemonade labeled button component with custom leading and trailing slots.
    /// Used for advanced button layouts such as "Dual Action" buttons.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Button(
    ///     label: "Dual Action",
    ///     onClick: { },
    ///     trailingSlot: { colors in
    ///         LemonadeUi.VerticalDivider()
    ///         Image(systemName: "ellipsis")
    ///             .foregroundColor(colors.contentColor)
    ///     },
    ///     expandContents: true,
    ///     size: .medium
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: String to be displayed as the Button's label
    ///   - onClick: Callback to be invoked when the Button is clicked
    ///   - leadingSlot: Custom view builder for the leading area, receives LemonadeButtonColors
    ///   - trailingSlot: Custom view builder for the trailing area, receives LemonadeButtonColors
    ///   - expandContents: When true, the content area expands to fill available space between slots
    ///   - variant: LemonadeButtonVariant to style the Button accordingly
    ///   - size: LemonadeButtonSize to size the Button accordingly
    ///   - enabled: Boolean flag to enable or disable the Button
    ///   - loading: Boolean flag to show a loading spinner and disable interaction
    /// - Returns: A styled Button view
    @ViewBuilder
    static func Button<LeadingSlot: View, TrailingSlot: View>(
        label: String,
        onClick: @escaping () -> Void,
        @ViewBuilder leadingSlot: @escaping (LemonadeButtonColors) -> LeadingSlot,
        @ViewBuilder trailingSlot: @escaping (LemonadeButtonColors) -> TrailingSlot,
        expandContents: Bool = false,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        enabled: Bool = true,
        loading: Bool = false
    ) -> some View {
        LemonadeSlotButtonView(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            enabled: enabled,
            loading: loading,
            expandContents: expandContents,
            leadingSlot: leadingSlot,
            trailingSlot: trailingSlot
        )
    }

    /// Lemonade labeled button component with a custom trailing slot.
    @ViewBuilder
    static func Button<TrailingSlot: View>(
        label: String,
        onClick: @escaping () -> Void,
        @ViewBuilder trailingSlot: @escaping (LemonadeButtonColors) -> TrailingSlot,
        expandContents: Bool = false,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        enabled: Bool = true,
        loading: Bool = false
    ) -> some View {
        LemonadeSlotButtonView(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            enabled: enabled,
            loading: loading,
            expandContents: expandContents,
            leadingSlot: nil as ((LemonadeButtonColors) -> EmptyView)?,
            trailingSlot: trailingSlot
        )
    }

    /// Lemonade labeled button component with a custom leading slot.
    @ViewBuilder
    static func Button<LeadingSlot: View>(
        label: String,
        onClick: @escaping () -> Void,
        @ViewBuilder leadingSlot: @escaping (LemonadeButtonColors) -> LeadingSlot,
        expandContents: Bool = false,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        enabled: Bool = true,
        loading: Bool = false
    ) -> some View {
        LemonadeSlotButtonView(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            enabled: enabled,
            loading: loading,
            expandContents: expandContents,
            leadingSlot: leadingSlot,
            trailingSlot: nil as ((LemonadeButtonColors) -> EmptyView)?
        )
    }
}

// MARK: - Button Colors

public struct LemonadeButtonColors {
    public let contentColor: Color
    public let solidBackgroundColor: Color
    public let pressedBackgroundColor: Color
    public let brushBackgroundColors: [Color]?

    internal init(
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
    let requiredHeight: CGFloat
    let minWidth: CGFloat
    let cornerRadius: CGFloat
    let textStyle: LemonadeTextStyle
}

// MARK: - Button Size Extension

private extension LemonadeButtonSize {
    var contentData: LemonadeButtonContentData {
        switch self {
        case .xSmall:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing100,
                horizontalPadding: LemonadeTheme.spaces.spacing200,
                requiredHeight: LemonadeTheme.sizes.size1000,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius200,
                textStyle: LemonadeTypography.shared.bodySmallSemiBold
            )
        case .small:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing200,
                horizontalPadding: LemonadeTheme.spaces.spacing300,
                requiredHeight: LemonadeTheme.sizes.size1000,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius300,
                textStyle: LemonadeTypography.shared.bodySmallSemiBold
            )
        case .medium:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing300,
                horizontalPadding: LemonadeTheme.spaces.spacing400,
                requiredHeight: LemonadeTheme.sizes.size1200,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius300,
                textStyle: LemonadeTypography.shared.bodyMediumSemiBold
            )
        case .large:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing300,
                horizontalPadding: LemonadeTheme.spaces.spacing400,
                requiredHeight: LemonadeTheme.sizes.size1400,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius400,
                textStyle: LemonadeTypography.shared.bodyMediumSemiBold
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
        case .criticalSolid:
            return LemonadeButtonColors(
                contentColor: LemonadeTheme.colors.content.contentAlwaysLight,
                solidBackgroundColor: LemonadeTheme.colors.background.bgCritical,
                pressedBackgroundColor: LemonadeTheme.colors.interaction.bgCriticalInteractive
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

// MARK: - Internal Core Button View

private struct LemonadeCoreButtonView<LeadingSlot: View, TrailingSlot: View>: View {
    let label: String
    let onClick: () -> Void
    let variant: LemonadeButtonVariant
    let size: LemonadeButtonSize
    let enabled: Bool
    let loading: Bool
    let expandContents: Bool
    let contentSlot: (LemonadeButtonColors) -> AnyView
    let leadingSlot: ((LemonadeButtonColors) -> LeadingSlot)?
    let trailingSlot: ((LemonadeButtonColors) -> TrailingSlot)?

    @State private var isPressed = false

    private var colors: LemonadeButtonColors {
        variant.variantData
    }

    private var backgroundColor: Color {
        isPressed ? colors.pressedBackgroundColor : colors.solidBackgroundColor
    }

    var body: some View {
        SwiftUI.Button(action: onClick) {
            HStack(spacing: 0) {
                if !loading, let leadingSlot = leadingSlot {
                    leadingSlot(colors)
                }

                HStack(spacing: 0) {
                    Spacer(minLength: 0)
                    contentSlot(colors)
                    Spacer(minLength: 0)
                }
                .padding(.vertical, size.contentData.verticalPadding)
                .padding(.horizontal, size.contentData.horizontalPadding)
                .if(expandContents) { view in
                    view.frame(maxWidth: .infinity)
                }

                if !loading, let trailingSlot = trailingSlot {
                    trailingSlot(colors)
                }
            }
            .frame(height: size.contentData.requiredHeight)
            .frame(minWidth: size.contentData.minWidth)
            .background(
                ZStack {
                    RoundedRectangle(cornerRadius: size.contentData.cornerRadius)
                        .fill(backgroundColor)

                    if let gradientColors = colors.brushBackgroundColors {
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
        .buttonStyle(LemonadePressTrackingButtonStyle(isPressed: $isPressed))
        .disabled(!enabled || loading)
        .opacity((enabled || loading) ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
    }
}

// MARK: - Conditional Modifier

private extension View {
    @ViewBuilder
    func `if`<Content: View>(_ condition: Bool, transform: (Self) -> Content) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}

// MARK: - Internal Icon Button View

private struct LemonadeButtonView: View {
    let label: String
    let onClick: () -> Void
    let leadingIcon: LemonadeIcon?
    let trailingIcon: LemonadeIcon?
    let variant: LemonadeButtonVariant
    let size: LemonadeButtonSize
    let enabled: Bool
    let loading: Bool

    var body: some View {
        LemonadeCoreButtonView(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            enabled: enabled,
            loading: loading,
            expandContents: false,
            contentSlot: { colors in
                AnyView(Group {
                    if loading {
                        ProgressView()
                            .tint(colors.contentColor)
                    } else {
                        if let leadingIcon = leadingIcon {
                            LemonadeUi.Icon(
                                icon: leadingIcon,
                                contentDescription: nil,
                                size: .medium,
                                tint: colors.contentColor
                            )
                        }

                        LemonadeUi.Text(
                            label,
                            textStyle: size.contentData.textStyle,
                            color: colors.contentColor
                        )
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, LemonadeTheme.spaces.spacing200)

                        if let trailingIcon = trailingIcon {
                            LemonadeUi.Icon(
                                icon: trailingIcon,
                                contentDescription: nil,
                                size: .medium,
                                tint: colors.contentColor
                            )
                        }
                    }
                })
            },
            leadingSlot: nil as ((LemonadeButtonColors) -> EmptyView)?,
            trailingSlot: nil as ((LemonadeButtonColors) -> EmptyView)?
        )
    }
}

// MARK: - Internal Slot Button View

private struct LemonadeSlotButtonView<LeadingSlot: View, TrailingSlot: View>: View {
    let label: String
    let onClick: () -> Void
    let variant: LemonadeButtonVariant
    let size: LemonadeButtonSize
    let enabled: Bool
    let loading: Bool
    let expandContents: Bool
    let leadingSlot: ((LemonadeButtonColors) -> LeadingSlot)?
    let trailingSlot: ((LemonadeButtonColors) -> TrailingSlot)?

    var body: some View {
        LemonadeCoreButtonView(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            enabled: enabled,
            loading: loading,
            expandContents: expandContents,
            contentSlot: { colors in
                AnyView(Group {
                    if loading {
                        ProgressView()
                            .tint(colors.contentColor)
                    } else {
                        LemonadeUi.Text(
                            label,
                            textStyle: size.contentData.textStyle,
                            color: colors.contentColor
                        )
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, LemonadeTheme.spaces.spacing200)
                    }
                })
            },
            leadingSlot: leadingSlot,
            trailingSlot: trailingSlot
        )
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
                    label: "XSmall",
                    onClick: {},
                    size: .xSmall
                )

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
