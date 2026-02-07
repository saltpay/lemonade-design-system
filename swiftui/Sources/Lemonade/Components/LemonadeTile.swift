import SwiftUI

// MARK: - Tile Type

/// Defines the type for Tile.
private enum LemonadeTileType {
    case action(LemonadeTileVariant)
    case selection

    @available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *)
    var sensoryFeedback: SensoryFeedback {
        switch self {
        case .action: return .impact
        case .selection: return .selection
        }
    }
}
// MARK: - Tile Variant

/// Defines the visual variant for Tile.
public enum LemonadeTileVariant {
    case neutral
    case muted
    case onColor
    
    var backgroundColor: Color {
        switch self {
        case .neutral: return .bg.bgElevated
        case .muted: return .bg.bgDefault
        case .onColor: return .bg.bgBrandElevated
        }
    }
    
    var backgroundPressedColor: Color {
        switch self {
        case .neutral: return .interaction.bgElevatedInteractive
        case .muted: return .interaction.bgDefaultPressed
        case .onColor: return .interaction.bgBrandElevatedInteractive
        }
    }
    
    var borderColor: Color {
        switch self {
        case .neutral: return Color.clear
        case .muted: return .border.borderNeutralMedium
        case .onColor: return .border.borderNeutralMediumInverse
        }
    }
    
    @available(*, deprecated, message: "Used only on the deprecated LemonadeUi.Tile.")
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
    @available(*, deprecated, message: "Use LemonadeUi.ActionTile instead.")
    static func Tile<AddonContent: View>(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: (() -> Void)? = nil,
        variant: LemonadeTileVariant = .neutral,
        @ViewBuilder addon: @escaping () -> AddonContent
    ) -> some View {
        LemonadeOldTileView(
            label: label,
            icon: icon,
            enabled: enabled,
            onClick: onClick,
            variant: variant,
            addon: addon
        )
    }
    
    /// An Action Tile component with leading slot, label, and optional addon.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.ActionTile(
    ///     label: "Label",
    ///     leadingSlot: { LemonadeUi.Icon(icon: LemonadeIcons.heart) },
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
    /// - Returns: A styled ActionTile view
    @ViewBuilder
    static func ActionTile(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: @escaping () -> Void,
        variant: LemonadeTileVariant = .neutral
    ) -> some View {
        LemonadeTileView(
            label: label,
            leadingSlot: {
                LemonadeUi.Icon(icon: icon, contentDescription: nil)
            },
            type: .action(variant),
            isSelected: false,
            onClick: onClick,
            addon: nil as (() -> EmptyView)?,
            enabled: enabled
        )
    }
    
    @ViewBuilder
    static func ActionTile<AddonContent: View>(
        label: String,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: @escaping () -> Void,
        variant: LemonadeTileVariant = .neutral,
        @ViewBuilder addon: @escaping () -> AddonContent
    ) -> some View {
        LemonadeTileView(
            label: label,
            leadingSlot: {
                LemonadeUi.Icon(icon: icon, contentDescription: nil)
            },
            type: .action(variant),
            isSelected: false,
            onClick: onClick,
            addon: addon,
            enabled: enabled
        )
    }
    
    @ViewBuilder
    static func ActionTile<LeadingSlotContent: View>(
        label: String,
        @ViewBuilder leadingSlot: @escaping () -> LeadingSlotContent,
        enabled: Bool = true,
        onClick: @escaping () -> Void,
        variant: LemonadeTileVariant = .neutral
    ) -> some View {
        LemonadeTileView(
            label: label,
            leadingSlot: leadingSlot,
            type: .action(variant),
            isSelected: false,
            onClick: onClick,
            addon: nil as (() -> EmptyView)?,
            enabled: enabled
        )
    }
    
    @ViewBuilder
    static func ActionTile<LeadingSlotContent: View, AddonContent: View>(
        label: String,
        @ViewBuilder leadingSlot: @escaping () -> LeadingSlotContent,
        enabled: Bool = true,
        onClick: @escaping () -> Void,
        variant: LemonadeTileVariant = .neutral,
        @ViewBuilder addon: @escaping () -> AddonContent
    ) -> some View {
        LemonadeTileView(
            label: label,
            leadingSlot: leadingSlot,
            type: .action(variant),
            isSelected: false,
            onClick: onClick,
            addon: addon,
            enabled: enabled
        )
    }
    
    
    /// A Selection Tile component with leading slot and label.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SelectionTile(
    ///     label: "Label",
    ///     isSelected: true,
    ///     leadingSlot: { LemonadeUi.Icon(icon: LemonadeIcons.heart) },
    ///     onClick: { /* action */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the tile
    ///   - leadingSlot: Content to be displayed above the label
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - onClick: Callback called when component is tapped
    ///   - addon: Optional content to be displayed as a badge overlay
    /// - Returns: A styled SelectionTile view
    @ViewBuilder
    static func SelectionTile(
        label: String,
        isSelected: Bool,
        icon: LemonadeIcon,
        enabled: Bool = true,
        onClick: @escaping () -> Void
    ) -> some View {
        LemonadeTileView(
            label: label,
            leadingSlot: {
                LemonadeUi.Icon(icon: icon, contentDescription: nil)
            },
            type: .selection,
            isSelected: isSelected,
            onClick: onClick,
            addon: nil as (() -> EmptyView)?,
            enabled: enabled
        )
    }
    
    @ViewBuilder
    static func SelectionTile<LeadingSlotContent: View>(
        label: String,
        isSelected: Bool,
        leadingSlot: @escaping () -> LeadingSlotContent,
        enabled: Bool = true,
        onClick: @escaping () -> Void
    ) -> some View {
        LemonadeTileView(
            label: label,
            leadingSlot: leadingSlot,
            type: .selection,
            isSelected: isSelected,
            onClick: onClick,
            addon: nil as (() -> EmptyView)?,
            enabled: enabled
        )
    }
}

// MARK: - Internal Tile View

private struct LemonadeTileView<LeadingSlotContent: View, AddonContent: View>: View {
    let label: String
    @ViewBuilder let leadingSlot: () -> LeadingSlotContent
    let type: LemonadeTileType
    let isSelected: Bool
    let onClick: () -> Void
    let addon: (() -> AddonContent)?
    let enabled: Bool
    
    /// Trigger counter for sensory feedback (iOS 17+)
    @State private var feedbackTrigger: Int = 0
    @State private var isPressed = false
    
    private let minWidth: CGFloat = 80
    
    private var backgroundColor: Color {
        switch type {
        case .action(let variant):
            isPressed ? variant.backgroundPressedColor : variant.backgroundColor
        case .selection:
            isSelected ? .bg.bgBrandSubtle : .bg.bgElevated
        }
    }

    private var borderColor: Color {
        switch type {
        case .action(let variant):
            variant.borderColor
        case .selection:
            isSelected ? LemonadeTheme.colors.border.borderSelected : Color.clear
        }
    }

    private var borderWidth: CGFloat {
        switch type {
        case .action:
                .borderWidth.border25
        case .selection:
            isSelected ? .borderWidth.borderSelected : .borderWidth.border0
        }
    }
    
    
    private var currentOpacity: Double {
        if !enabled {
            return .opacity.opacityDisabled
        }
        if isPressed {
            return .opacity.opacityPressed
        }
        return .opacity.opacity100
    }
    
    private var scale: CGFloat {
        if isPressed {
            return 0.98
        }
        return 1
    }
    
    var body: some View {
        Button(action: {
            onClick()
            feedbackTrigger += 1
        }) {
            tileContent
        }
        .buttonStyle(TileButtonStyle(isPressed: $isPressed))
        .modifier(TileSensoryFeedbackModifier(
            trigger: feedbackTrigger,
            type: type
        ))
        .disabled(!enabled)
    }
    
    private var tileContent: some View {
        ZStack(alignment: .topTrailing) {
            VStack(alignment: .leading, spacing: .space.spacing400) {
                
                leadingSlot()
                
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                    color: .content.contentPrimary,
                    overflow: .tail,
                    maxLines: 1
                )
            }
            .frame(minWidth: minWidth, maxWidth: .infinity, alignment: .leading)
            .padding(.space.spacing400)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: .radius.radius500))
            .overlay(
                RoundedRectangle(cornerRadius: .radius.radius500)
                    .stroke(
                        borderColor,
                        lineWidth: borderWidth
                    )
            )
            
            // Addon badge
            if let addon = addon {
                addon()
                    .offset(x: .space.spacing200, y: -.space.spacing100)
            }
        }
        .opacity(currentOpacity)
        .scaleEffect(scale)
        .contentShape(RoundedRectangle(cornerRadius: .radius.radius500))
        .animation(.easeInOut(duration: 0.15), value: isPressed)
        .animation(.easeInOut(duration: 0.15), value: isSelected)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
        .accessibilityLabel(label)
        
        
    }
    
    // MARK: - Button Style
    
    private struct TileButtonStyle: ButtonStyle {
        @Binding var isPressed: Bool
        
        func makeBody(configuration: Configuration) -> some View {
            configuration.label
                .onChange(of: configuration.isPressed) { newValue in
                    withAnimation(.easeInOut(duration: 0.15)) {
                        isPressed = newValue
                    }
                }
        }
    }
    
    // MARK: - Sensory Feedback Modifier
    
    /// A modifier that provides sensory feedback on button tap or selection
    /// Uses the iOS 17+ SensoryFeedback API with graceful fallback for older versions.
    private struct TileSensoryFeedbackModifier: ViewModifier {
        let trigger: Int
        let type: LemonadeTileType?
        
        func body(content: Content) -> some View {
            if #available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *) {
                content
                    .sensoryFeedback(trigger: trigger) { _, _ in
                        type?.sensoryFeedback
                    }
            } else {
                content
            }
        }
    }
}

@available(*, deprecated, message: "Use LemonadeTileView instead.")
private struct LemonadeOldTileView<AddonContent: View>: View {
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
        ScrollView {
            VStack(alignment: .leading, spacing: .space.spacing600) {
                // ActionTile
                VStack(alignment: .leading, spacing: .space.spacing300) {
                    LemonadeUi.Text("Action", textStyle: LemonadeTypography.shared.headingXXSmall)
                    // All variants
                    HStack(spacing: .space.spacing200) {
                        LemonadeUi.ActionTile(
                            label: "Neutral",
                            icon: .heart,
                            onClick: {},
                            variant: .neutral,
                        )
                        
                        LemonadeUi.ActionTile(
                            label: "Muted",
                            icon: .star,
                            onClick: {},
                            variant: .muted
                        )
                        
                    }
                    
                    // OnColor variant (needs brand background)
                    LemonadeUi.ActionTile(
                        label: "OnColor",
                        icon: .heart,
                        onClick: {},
                        variant: .onColor
                    )
                    .padding(.space.spacing300)
                    .background(.bg.bgBrand)
                    
                    HStack(spacing: .space.spacing200) {
                        // With addon
                        LemonadeUi.ActionTile(
                            label: "With Addon",
                            icon: .heart,
                            onClick: {},
                            variant: .neutral,
                            addon: {
                                LemonadeUi.Badge(text: "New", size: .xSmall)
                            }
                        )
                        
                        // Custom Slot
                        LemonadeUi.ActionTile(
                            label: "Custom leading",
                            leadingSlot: {
                                LemonadeUi.CountryFlag(flag: .gBUnitedKingdom)
                            },
                            onClick: {},
                            variant: .neutral
                        )
                    }
                    
                    LemonadeUi.ActionTile(
                        label: "Disabled",
                        icon: .heart,
                        enabled: false,
                        onClick: {},
                        variant: .neutral
                    )
                }
                
                // SelectionTile
                VStack(alignment: .leading, spacing: .space.spacing300) {
                    LemonadeUi.Text("Selection", textStyle: LemonadeTypography.shared.headingXXSmall)
                    // All variants
                    HStack(spacing: .space.spacing200) {
                        LemonadeUi.SelectionTile(
                            label: "Unselected",
                            isSelected: false,
                            icon: .heart,
                            onClick: {},
                        )
                        
                        LemonadeUi.SelectionTile(
                            label: "Selected",
                            isSelected: true,
                            icon: .heart,
                            onClick: {},
                        )
                        
                        // Disabled
                        LemonadeUi.SelectionTile(
                            label: "Disabled",
                            isSelected: false,
                            icon: .heart,
                            enabled: false,
                            onClick: {},
                        )
                    }
                }
                
                VStack(alignment: .leading, spacing: .space.spacing300) {
                    LemonadeUi.Text("Selection with custom leading", textStyle: LemonadeTypography.shared.headingXXSmall)
                    // All variants
                    HStack(spacing: .space.spacing200) {
                        LemonadeUi.SelectionTile(
                            label: "Unselected",
                            isSelected: false,
                            leadingSlot: {
                                LemonadeUi.CountryFlag(flag: .gBUnitedKingdom)
                            },
                            onClick: {},
                        )
                        
                        LemonadeUi.SelectionTile(
                            label: "Selected",
                            isSelected: true,
                            leadingSlot: {
                                LemonadeUi.CountryFlag(flag: .gBUnitedKingdom)
                            },
                            onClick: {},
                        )
                        
                        // Disabled
                        LemonadeUi.SelectionTile(
                            label: "Disabled",
                            isSelected: false,
                            leadingSlot: {
                                LemonadeUi.CountryFlag(flag: .gBUnitedKingdom)
                            },
                            enabled: false,
                            onClick: {},
                        )
                    }
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
            .padding()
            .previewLayout(.sizeThatFits)
        }
    }
}
#endif
