import SwiftUI

// MARK: - Chip Component

public extension LemonadeUi {
    /// A compact element used to display information, trigger actions, or represent selections.
    /// Commonly used for tags, filters, or interactive choices in dense interfaces.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Chip(
    ///     label: "Label",
    ///     selected: true,
    ///     leadingIcon: .airplane
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the chip
    ///   - selected: Set to true if the chip is in the selected state
    ///   - leadingIcon: Optional LemonadeIcon to be displayed in the leading position
    ///   - trailingIcon: Optional LemonadeIcon to be displayed in the trailing position
    ///   - counter: Optional Int number to be displayed in the chip
    ///   - enabled: Controls the enabled state of the chip. Defaults to true
    ///   - onChipClicked: Optional callback for when the chip is clicked
    ///   - onTrailingIconClick: Optional callback when the trailing icon is clicked
    /// - Returns: A styled Chip view
    @ViewBuilder
    static func Chip(
        label: String,
        selected: Bool,
        leadingIcon: LemonadeIcon? = nil,
        trailingIcon: LemonadeIcon? = nil,
        counter: Int? = nil,
        enabled: Bool = true,
        onChipClicked: (() -> Void)? = nil,
        onTrailingIconClick: (() -> Void)? = nil
    ) -> some View {
        LemonadeChipView(
            label: label,
            selected: selected,
            leadingSlot: leadingIcon.map { icon in
                AnyView(
                    LemonadeUi.Icon(
                        icon: icon,
                        contentDescription: nil,
                        size: .small,
                        tint: selected
                            ? LemonadeTheme.colors.content.contentBrandInverse
                            : LemonadeTheme.colors.content.contentPrimary
                    )
                )
            },
            trailingSlot: trailingIcon.map { icon in
                AnyView(
                    LemonadeUi.Icon(
                        icon: icon,
                        contentDescription: nil,
                        size: .small,
                        tint: selected
                            ? LemonadeTheme.colors.content.contentBrandInverse
                            : LemonadeTheme.colors.content.contentPrimary
                    )
                )
            },
            counter: counter,
            enabled: enabled,
            onChipClicked: onChipClicked,
            onTrailingIconClick: onTrailingIconClick
        )
    }

    /// A compact element used to display information with a custom leading image.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Chip(
    ///     label: "Label",
    ///     selected: true,
    ///     leadingImage: Image("profile")
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: The text to be displayed in the chip
    ///   - selected: Set to true if the chip is in the selected state
    ///   - leadingImage: Image to be displayed in the leading position
    ///   - trailingIcon: Optional LemonadeIcon to be displayed in the trailing position
    ///   - counter: Optional Int number to be displayed in the chip
    ///   - enabled: Controls the enabled state of the chip. Defaults to true
    ///   - onChipClicked: Optional callback for when the chip is clicked
    ///   - onTrailingIconClick: Optional callback when the trailing icon is clicked
    /// - Returns: A styled Chip view
    @ViewBuilder
    static func Chip(
        label: String,
        selected: Bool,
        leadingImage: Image,
        trailingIcon: LemonadeIcon? = nil,
        counter: Int? = nil,
        enabled: Bool = true,
        onChipClicked: (() -> Void)? = nil,
        onTrailingIconClick: (() -> Void)? = nil
    ) -> some View {
        LemonadeChipView(
            label: label,
            selected: selected,
            leadingSlot: AnyView(
                leadingImage
                    .resizable()
                    .scaledToFill()
                    .frame(width: 20, height: 20)
                    .clipShape(Circle())
                    .overlay(
                        Circle()
                            .stroke(
                                LemonadeTheme.colors.border.borderNeutralMedium,
                                lineWidth: LemonadeTheme.borderWidth.base.border25
                            )
                    )
            ),
            trailingSlot: trailingIcon.map { icon in
                AnyView(
                    LemonadeUi.Icon(
                        icon: icon,
                        contentDescription: nil,
                        size: .small,
                        tint: selected
                            ? LemonadeTheme.colors.content.contentBrandInverse
                            : LemonadeTheme.colors.content.contentPrimary
                    )
                )
            },
            counter: counter,
            enabled: enabled,
            onChipClicked: onChipClicked,
            onTrailingIconClick: onTrailingIconClick
        )
    }
}

// MARK: - Internal Chip View

private struct LemonadeChipView: View {
    let label: String
    let selected: Bool
    let leadingSlot: AnyView?
    let trailingSlot: AnyView?
    let counter: Int?
    let enabled: Bool
    let onChipClicked: (() -> Void)?
    let onTrailingIconClick: (() -> Void)?

    @State private var isPressed = false

    private let minWidth: CGFloat = 64
    private let minHeight: CGFloat = 32
    private let actionsSize: CGFloat = 16

    private var backgroundColor: Color {
        if selected {
            return isPressed
                ? LemonadeTheme.colors.interaction.bgBrandHighInteractive
                : LemonadeTheme.colors.background.bgBrandHigh
        } else {
            return isPressed
                ? LemonadeTheme.colors.interaction.bgSubtleInteractive
                : LemonadeTheme.colors.background.bgDefault
        }
    }

    private var contentColor: Color {
        selected
            ? LemonadeTheme.colors.content.contentBrandInverse
            : LemonadeTheme.colors.content.contentPrimary
    }

    private var borderColor: Color {
        LemonadeTheme.colors.border.borderNeutralMedium
    }

    var body: some View {
        HStack(spacing: 0) {
            // Leading slot
            if let leadingSlot = leadingSlot {
                leadingSlot
                    .frame(width: actionsSize, height: actionsSize)
            }

            // Label
            LemonadeUi.Text(
                label,
                textStyle: LemonadeTypography.shared.bodySmallMedium,
                color: contentColor
            )
            .padding(.horizontal, LemonadeTheme.spaces.spacing100)

            // Counter
            if let counter = counter {
                SwiftUI.Text("\(counter)")
                    .font(.custom("Figtree", size: 10).weight(.semibold))
                    .foregroundStyle(LemonadeTheme.colors.content.contentOnBrandHigh)
                    .lineLimit(1)
                    .padding(.horizontal, LemonadeTheme.spaces.spacing100)
                    .frame(minWidth: 18, minHeight: 16)
                    .background(LemonadeTheme.colors.background.bgBrand)
                    .clipShape(Capsule())
                    .padding(.horizontal, LemonadeTheme.spaces.spacing100)
            }

            // Trailing slot
            if let trailingSlot = trailingSlot {
                if let onTrailingIconClick = onTrailingIconClick {
                    SwiftUI.Button(action: onTrailingIconClick) {
                        trailingSlot
                            .frame(width: actionsSize, height: actionsSize)
                    }
                    .buttonStyle(PlainButtonStyle())
                    .disabled(!enabled)
                    .padding(.leading, LemonadeTheme.spaces.spacing50)
                } else {
                    trailingSlot
                        .frame(width: actionsSize, height: actionsSize)
                        .padding(.leading, LemonadeTheme.spaces.spacing50)
                }
            }
        }
        .padding(.horizontal, LemonadeTheme.spaces.spacing200)
        .padding(.vertical, LemonadeTheme.spaces.spacing100)
        .frame(minWidth: minWidth, minHeight: minHeight)
        .background(backgroundColor)
        .clipShape(Capsule())
        .overlay(
            Capsule()
                .stroke(borderColor, lineWidth: 1)
        )
        .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
        .contentShape(Capsule())
        .simultaneousGesture(
            onChipClicked != nil && enabled
                ? DragGesture(minimumDistance: 0)
                    .onChanged { _ in isPressed = true }
                    .onEnded { _ in
                        isPressed = false
                        onChipClicked?()
                    }
                : nil
        )
        .animation(.easeInOut(duration: 0.15), value: isPressed)
        .animation(.easeInOut(duration: 0.15), value: selected)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeChip_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 16) {
            // Basic chips
            HStack(spacing: 8) {
                LemonadeUi.Chip(label: "Unselected", selected: false)
                LemonadeUi.Chip(label: "Selected", selected: true)
            }

            // With counter
            HStack(spacing: 8) {
                LemonadeUi.Chip(label: "Label", selected: false, counter: 5)
                LemonadeUi.Chip(label: "Label", selected: true, counter: 12)
            }

            // With icons
            HStack(spacing: 8) {
                LemonadeUi.Chip(
                    label: "Leading",
                    selected: false,
                    leadingIcon: .heart
                )
                LemonadeUi.Chip(
                    label: "Trailing",
                    selected: true,
                    trailingIcon: .circleX
                )
            }

            // With both icons
            LemonadeUi.Chip(
                label: "Both Icons",
                selected: false,
                leadingIcon: .star,
                trailingIcon: .circleX
            )

            // Disabled
            HStack(spacing: 8) {
                LemonadeUi.Chip(label: "Disabled", selected: false, enabled: false)
                LemonadeUi.Chip(label: "Disabled", selected: true, enabled: false)
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
