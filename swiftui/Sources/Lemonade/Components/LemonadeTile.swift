import SwiftUI

// MARK: - Tile Variant

/// Defines the visual variant for Tile.
public enum LemonadeTileVariant {
    case filled
    case outlined
    
    var backgroundColor: Color {
        switch self {
        case .filled: return LemonadeTheme.colors.background.bgElevated
        case .outlined: return LemonadeTheme.colors.background.bgDefault
        }
    }
    
    var backgroundPressedColor: Color {
        switch self {
        case .filled: return LemonadeTheme.colors.interaction.bgElevatedPressed
        case .outlined: return LemonadeTheme.colors.interaction.bgDefaultPressed
        }
    }
    
    var borderColor: Color {
        switch self {
        case .filled: return LemonadeTheme.colors.border.borderNeutralMedium
        case .outlined: return LemonadeTheme.colors.border.borderNeutralMedium
        }
    }
    
    var borderWidth: CGFloat {
        switch self {
        case .filled: return 0
        case .outlined: return LemonadeTheme.borderWidth.base.border40
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
        stretched: Bool = false
    ) -> some View {
        LemonadeTileView<EmptyView>(
            label: label,
            icon: icon,
            enabled: enabled,
            isSelected: isSelected,
            supportText: supportText,
            onClick: onClick,
            variant: variant,
            stretched: stretched,
            topAccessory: nil
        )
    }
    
    /// A tile component with icon, label, and a top-right accessory view.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tile(
    ///     label: "Label",
    ///     icon: .heart,
    ///     variant: .filled,
    ///     onClick: { /* action */ }
    /// ) {
    ///     // top-right accessory content
    ///     Image(systemName: "info.circle")
    /// }
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
    ///   - topAccessory: A view rendered at the top-right of the tile
    /// - Returns: A styled Tile view
    @ViewBuilder
    static func Tile<TopAccessory: View>(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        isSelected: Bool = false,
        supportText: String? = nil,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .filled,
        stretched: Bool = false,
        @ViewBuilder topAccessory: @escaping () -> TopAccessory
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
            topAccessory: topAccessory
        )
    }
    
    /// A tile component with a custom leading slot view instead of an icon.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tile(
    ///     label: "Custom",
    ///     variant: .filled,
    ///     leadingSlot: {
    ///         LemonadeUi.Icon(icon: .shoppingBag, contentDescription: nil, size: .medium)
    ///     }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the tile
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - isSelected: Whether the tile is in a selected state. Defaults to false
    ///   - supportText: Optional secondary text displayed below the label
    ///   - onClick: Callback called when component is tapped
    ///   - variant: LemonadeTileVariant to define visual style. Defaults to .filled
    ///   - stretched: Whether the tile should stretch to fill available width. Defaults to false
    ///   - leadingSlot: A custom view rendered where the icon would normally appear
    /// - Returns: A styled Tile view
    @ViewBuilder
    static func Tile<LeadingContent: View>(
        label: String,
        enabled: Bool = true,
        isSelected: Bool = false,
        supportText: String? = nil,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .filled,
        stretched: Bool = false,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        LemonadeTileSlotView<LeadingContent, EmptyView>(
            label: label,
            enabled: enabled,
            isSelected: isSelected,
            supportText: supportText,
            onClick: onClick,
            variant: variant,
            stretched: stretched,
            leadingSlot: leadingSlot,
            topAccessory: nil
        )
    }
    
    /// A tile component with a custom leading slot view and a top-right accessory.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tile(
    ///     label: "Custom",
    ///     variant: .filled,
    ///     leadingSlot: {
    ///         LemonadeUi.Icon(icon: .shoppingBag, contentDescription: nil, size: .medium)
    ///     },
    ///     topAccessory: {
    ///         LemonadeUi.Icon(icon: .circleInfo, contentDescription: nil, size: .small)
    ///     }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the tile
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - isSelected: Whether the tile is in a selected state. Defaults to false
    ///   - supportText: Optional secondary text displayed below the label
    ///   - onClick: Callback called when component is tapped
    ///   - variant: LemonadeTileVariant to define visual style. Defaults to .filled
    ///   - stretched: Whether the tile should stretch to fill available width. Defaults to false
    ///   - leadingSlot: A custom view rendered where the icon would normally appear
    ///   - topAccessory: A view rendered at the top-right of the tile
    /// - Returns: A styled Tile view
    @ViewBuilder
    static func Tile<LeadingContent: View, TopAccessory: View>(
        label: String,
        enabled: Bool = true,
        isSelected: Bool = false,
        supportText: String? = nil,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .filled,
        stretched: Bool = false,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder topAccessory: @escaping () -> TopAccessory
    ) -> some View {
        LemonadeTileSlotView(
            label: label,
            enabled: enabled,
            isSelected: isSelected,
            supportText: supportText,
            onClick: onClick,
            variant: variant,
            stretched: stretched,
            leadingSlot: leadingSlot,
            topAccessory: topAccessory
        )
    }
    
}

// MARK: - Haptic Helpers

#if os(iOS)
fileprivate func triggerTouchHaptic() {
    UIImpactFeedbackGenerator(style: .light).impactOccurred()
}

fileprivate func triggerSelectionHaptic() {
    UISelectionFeedbackGenerator().selectionChanged()
}
#else
fileprivate func triggerTouchHaptic() {}
fileprivate func triggerSelectionHaptic() {}
#endif

// MARK: - Tile Button Style

private struct LemonadeTileButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .opacity(configuration.isPressed ? .opacity.opacityPressed : .opacity.opacity100)
            .scaleEffect(configuration.isPressed ? 0.96 : 1.0)
            .animation(.easeInOut(duration: 0.1), value: configuration.isPressed)
    }
}

// MARK: - Internal Tile View

private struct LemonadeTileView<TopAccessory: View>: View {
    let label: String
    let icon: LemonadeIcon
    let enabled: Bool
    let isSelected: Bool
    let supportText: String?
    let onClick: (() -> Void)?
    let variant: LemonadeTileVariant
    let stretched: Bool
    let topAccessory: (() -> TopAccessory)?
    
    private let minWidth: CGFloat = 120
    
    private var effectiveBackgroundColor: Color {
        isSelected ? LemonadeTheme.colors.background.bgBrandSubtle : variant.backgroundColor
    }
    
    private var effectiveBorderColor: Color {
        isSelected ? LemonadeTheme.colors.border.borderSelected : variant.borderColor
    }
    
    private var effectiveBorderWidth: CGFloat {
        isSelected ? LemonadeTheme.borderWidth.base.border50 : variant.borderWidth
    }
    
    private var effectiveContentColor: Color {
        isSelected ? LemonadeTheme.colors.content.contentOnBrandHigh : LemonadeTheme.colors.content.contentPrimary
    }
    
    private var tileContent: some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            // Top row: icon + topAccessory
            HStack {
                LemonadeUi.Icon(
                    icon: icon,
                    contentDescription: nil,
                    size: .medium,
                    tint: effectiveContentColor
                )
                
                Spacer(minLength: 0)
                
                if let topAccessory {
                    topAccessory()
                }
            }
            
            Spacer(minLength: 0)
            
            VStack(alignment: .leading, spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodySmallMedium,
                    color: effectiveContentColor,
                    overflow: .tail,
                    maxLines: 1
                )
                
                if let supportText {
                    LemonadeUi.Text(
                        supportText,
                        textStyle: LemonadeTypography.shared.bodySmallRegular,
                        color: LemonadeTheme.colors.content.contentSecondary,
                        overflow: .tail,
                        maxLines: 1,
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(LemonadeTheme.spaces.spacing300)
    }
    
    private var tileShape: some View {
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
        .applyIf(stretched) {
            $0.frame(maxWidth: .infinity, alignment: .leading)
        }
        .background(effectiveBackgroundColor)
        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .overlay(
            RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                .stroke(effectiveBorderColor, lineWidth: effectiveBorderWidth)
        )
        .opacity(enabled ? .opacity.opacity100 : LemonadeTheme.opacity.state.opacityDisabled)
        .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .onChange(of: isSelected) { newValue in
            if newValue { triggerSelectionHaptic() }
        }
    }
    
    var body: some View {
        if let onClick {
            Button(action: { triggerTouchHaptic(); onClick() }) { tileShape }
                .buttonStyle(LemonadeTileButtonStyle())
                .disabled(!enabled)
        } else {
            tileShape
        }
    }
}

// MARK: - Internal Tile Slot View

private struct LemonadeTileSlotView<LeadingContent: View, TopAccessory: View>: View {
    let label: String
    let enabled: Bool
    let isSelected: Bool
    let supportText: String?
    let onClick: (() -> Void)?
    let variant: LemonadeTileVariant
    let stretched: Bool
    let leadingSlot: () -> LeadingContent
    let topAccessory: (() -> TopAccessory)?
    
    private let minWidth: CGFloat = 120
    
    private var effectiveBackgroundColor: Color {
        isSelected ? LemonadeTheme.colors.background.bgBrandSubtle : variant.backgroundColor
    }
    
    private var effectiveBorderColor: Color {
        isSelected ? LemonadeTheme.colors.border.borderSelected : variant.borderColor
    }
    
    private var effectiveBorderWidth: CGFloat {
        isSelected ? LemonadeTheme.borderWidth.base.border50 : variant.borderWidth
    }
    
    private var effectiveContentColor: Color {
        isSelected ? LemonadeTheme.colors.content.contentOnBrandHigh : LemonadeTheme.colors.content.contentPrimary
    }
    
    private var tileContent: some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            // Top row: leadingSlot + topAccessory
            HStack {
                leadingSlot()
                
                Spacer(minLength: 0)
                
                if let topAccessory {
                    topAccessory()
                }
            }
            
            Spacer(minLength: 0)
            
            VStack(alignment: .leading, spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodySmallMedium,
                    color: effectiveContentColor,
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
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(LemonadeTheme.spaces.spacing300)
    }

    private var tileShape: some View {
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
        .applyIf(stretched) {
            $0.frame(maxWidth: .infinity, alignment: .leading)
        }
        .background(effectiveBackgroundColor)
        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .overlay(
            RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                .stroke(effectiveBorderColor, lineWidth: effectiveBorderWidth)
        )
        .opacity(enabled ? .opacity.opacity100 : LemonadeTheme.opacity.state.opacityDisabled)
        .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .onChange(of: isSelected) { newValue in
            if newValue { triggerSelectionHaptic() }
        }
    }
    
    var body: some View {
        if let onClick {
            Button(action: { triggerTouchHaptic(); onClick() }) { tileShape }
                .buttonStyle(LemonadeTileButtonStyle())
                .disabled(!enabled)
        } else {
            tileShape
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
                    onClick: {},
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
                    variant: .filled,
                    stretched: true
                )
                
                LemonadeUi.Tile(
                    label: "Outlined",
                    icon: .star,
                    supportText: "Long Support Text to Check Layout",
                    variant: .outlined,
                    stretched: true
                )
            }
            
            // With top accessory
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "With Accessory",
                    icon: .heart,
                    variant: .filled,
                    topAccessory: {
                        LemonadeUi.Icon(
                            icon: .circleInfo,
                            contentDescription: nil,
                            size: .small
                        )
                    }
                )
                
                LemonadeUi.Tile(
                    label: "Selected",
                    icon: .star,
                    isSelected: true,
                    variant: .filled,
                    topAccessory: {
                        LemonadeUi.Icon(
                            icon: .circleInfo,
                            contentDescription: nil,
                            size: .small
                        )
                    }
                )
            }
            
            // Disabled
            HStack(spacing: 16) {
                LemonadeUi.Tile(
                    label: "Disabled",
                    icon: .heart,
                    enabled: false,
                    variant: .filled,
                    stretched: true
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
