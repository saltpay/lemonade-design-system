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
        spacedContents: Bool = false,
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
            spacedContents: spacedContents,
            enabled: enabled,
            loading: loading
        )
    }

    // MARK: - Slot-based Button overloads

    /// Creates a button with custom leading and trailing slot content.
    ///
    /// The slot closures receive ``LemonadeButtonColors`` so custom content can
    /// use variant-aware colors.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Button(
    ///     label: "Custom",
    ///     onClick: { },
    ///     leadingSlot: { colors in
    ///         Image(systemName: "star.fill")
    ///             .foregroundColor(colors.contentColor)
    ///     },
    ///     trailingSlot: { colors in
    ///         Image(systemName: "chevron.right")
    ///             .foregroundColor(colors.contentColor)
    ///     }
    /// )
    /// ```
    @ViewBuilder
    static func Button<LeadingContent: View, TrailingContent: View>(
        label: String,
        onClick: @escaping () -> Void,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        spacedContents: Bool = false,
        enabled: Bool = true,
        loading: Bool = false,
        @ViewBuilder leadingSlot: @escaping (LemonadeButtonColors) -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping (LemonadeButtonColors) -> TrailingContent
    ) -> some View {
        LemonadeSlotButtonView(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            spacedContents: spacedContents,
            enabled: enabled,
            loading: loading,
            leadingSlot: leadingSlot,
            trailingSlot: trailingSlot
        )
    }

    /// Creates a button with only a custom leading slot.
    @ViewBuilder
    static func Button<LeadingContent: View>(
        label: String,
        onClick: @escaping () -> Void,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        spacedContents: Bool = false,
        enabled: Bool = true,
        loading: Bool = false,
        @ViewBuilder leadingSlot: @escaping (LemonadeButtonColors) -> LeadingContent
    ) -> some View {
        Button(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            spacedContents: spacedContents,
            enabled: enabled,
            loading: loading,
            leadingSlot: leadingSlot,
            trailingSlot: { _ in EmptyView() }
        )
    }

    /// Creates a button with only a custom trailing slot.
    @ViewBuilder
    static func Button<TrailingContent: View>(
        label: String,
        onClick: @escaping () -> Void,
        variant: LemonadeButtonVariant = .primary,
        size: LemonadeButtonSize = .large,
        spacedContents: Bool = false,
        enabled: Bool = true,
        loading: Bool = false,
        @ViewBuilder trailingSlot: @escaping (LemonadeButtonColors) -> TrailingContent
    ) -> some View {
        Button(
            label: label,
            onClick: onClick,
            variant: variant,
            size: size,
            spacedContents: spacedContents,
            enabled: enabled,
            loading: loading,
            leadingSlot: { _ in EmptyView() },
            trailingSlot: trailingSlot
        )
    }
}

// MARK: - Button Colors

/// Colors resolved for a specific ``LemonadeButtonVariant``.
///
/// Exposed so that custom slot content can use variant-aware colors.
public struct LemonadeButtonColors {
    public let contentColor: Color
    public let solidBackgroundColor: Color
    public let pressedBackgroundColor: Color
    public let brushBackgroundColors: [Color]?

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
                textStyle: LemonadeTypography.shared.bodySmallSemiBold
            )
        case .medium:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing300,
                horizontalPadding: LemonadeTheme.spaces.spacing400,
                minHeight: LemonadeTheme.sizes.size1200,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius300,
                textStyle: LemonadeTypography.shared.bodyMediumSemiBold
            )
        case .large:
            return LemonadeButtonContentData(
                verticalPadding: LemonadeTheme.spaces.spacing300,
                horizontalPadding: LemonadeTheme.spaces.spacing400,
                minHeight: LemonadeTheme.sizes.size1400,
                minWidth: LemonadeTheme.sizes.size1600,
                cornerRadius: LemonadeTheme.radius.radius400,
                textStyle: LemonadeTypography.shared.bodyMediumSemiBold
            )
        }
    }
}

// MARK: - Button Variant Extension

extension LemonadeButtonVariant {
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

// MARK: - Internal Button View

private struct LemonadeButtonView: View {
    let label: String
    let onClick: () -> Void
    let leadingIcon: LemonadeIcon?
    let trailingIcon: LemonadeIcon?
    let variant: LemonadeButtonVariant
    let size: LemonadeButtonSize
    let spacedContents: Bool
    let enabled: Bool
    let loading: Bool

    @State private var isPressed = false

    private var backgroundColor: Color {
        isPressed ? variant.variantData.pressedBackgroundColor : variant.variantData.solidBackgroundColor
    }

    var body: some View {
        SwiftUI.Button(action: onClick) {
            HStack(spacing: 0) {
                if !spacedContents {
                    Spacer(minLength: 0)
                }

                if loading {
                    ProgressView()
                        .tint(variant.variantData.contentColor)
                } else {
                    if let leadingIcon = leadingIcon {
                        LemonadeUi.Icon(
                            icon: leadingIcon,
                            contentDescription: nil,
                            size: .medium,
                            tint: variant.variantData.contentColor
                        )
                    }

                    if spacedContents {
                        Spacer(minLength: 0)
                    }

                    LemonadeUi.Text(
                        label,
                        textStyle: size.contentData.textStyle,
                        color: variant.variantData.contentColor
                    )
                    .padding(.horizontal, LemonadeTheme.spaces.spacing200)

                    if spacedContents {
                        Spacer(minLength: 0)
                    }

                    if let trailingIcon = trailingIcon {
                        LemonadeUi.Icon(
                            icon: trailingIcon,
                            contentDescription: nil,
                            size: .medium,
                            tint: variant.variantData.contentColor
                        )
                    }
                }

                if !spacedContents {
                    Spacer(minLength: 0)
                }
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
        .buttonStyle(LemonadePressTrackingButtonStyle(isPressed: $isPressed))
        .disabled(!enabled || loading)
        .opacity((enabled || loading) ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
    }
}

// MARK: - Internal Slot Button View

private struct LemonadeSlotButtonView<LeadingContent: View, TrailingContent: View>: View {
    let label: String
    let onClick: () -> Void
    let variant: LemonadeButtonVariant
    let size: LemonadeButtonSize
    let spacedContents: Bool
    let enabled: Bool
    let loading: Bool
    @ViewBuilder let leadingSlot: (LemonadeButtonColors) -> LeadingContent
    @ViewBuilder let trailingSlot: (LemonadeButtonColors) -> TrailingContent

    @State private var isPressed = false

    private var colors: LemonadeButtonColors { variant.variantData }

    private var backgroundColor: Color {
        isPressed ? colors.pressedBackgroundColor : colors.solidBackgroundColor
    }

    var body: some View {
        SwiftUI.Button(action: onClick) {
            HStack(spacing: 0) {
                if !spacedContents {
                    Spacer(minLength: 0)
                }

                if loading {
                    ProgressView()
                        .tint(colors.contentColor)
                } else {
                    leadingSlot(colors)

                    if spacedContents {
                        Spacer(minLength: 0)
                    }

                    LemonadeUi.Text(
                        label,
                        textStyle: size.contentData.textStyle,
                        color: colors.contentColor
                    )
                    .padding(.horizontal, LemonadeTheme.spaces.spacing200)

                    if spacedContents {
                        Spacer(minLength: 0)
                    }

                    trailingSlot(colors)
                }

                if !spacedContents {
                    Spacer(minLength: 0)
                }
            }
            .padding(.horizontal, size.contentData.horizontalPadding)
            .frame(height: size.contentData.minHeight)
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
