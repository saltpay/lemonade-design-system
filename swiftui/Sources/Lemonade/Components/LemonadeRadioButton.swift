import SwiftUI

// MARK: - RadioButton Component

public extension LemonadeUi {
    /// A form control that allows users to select a single option from a group.
    /// Selecting one option automatically deselects any previously selected option.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.RadioButton(
    ///     checked: true,
    ///     onRadioButtonClicked: { /* some action to be triggered if not already checked */ },
    ///     label: "Label",
    ///     supportText: "Support text",
    ///     enabled: true
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - checked: The selected state of the radio button. `true` if selected, `false` otherwise.
    ///   - onRadioButtonClicked: A callback invoked when the user clicks the radio button.
    ///   - label: The primary text label displayed next to the radio button.
    ///   - supportText: Optional secondary text displayed below the label.
    ///   - enabled: Controls the enabled state of the radio button.
    /// - Returns: A styled RadioButton view with label
    @ViewBuilder
    static func RadioButton(
        checked: Bool,
        onRadioButtonClicked: @escaping () -> Void,
        label: String,
        supportText: String? = nil,
        enabled: Bool = true
    ) -> some View {
        LemonadeRadioButtonWithLabel(
            checked: checked,
            onRadioButtonClicked: onRadioButtonClicked,
            label: label,
            supportText: supportText,
            enabled: enabled
        )
    }

    /// A form control that allows users to select a single option from a group.
    /// This variant displays only the visual radio button element.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.RadioButton(
    ///     checked: true,
    ///     onRadioButtonClicked: { /* some action */ },
    ///     enabled: true
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - checked: The selected state of the radio button.
    ///   - onRadioButtonClicked: A callback invoked when the user clicks the radio button.
    ///   - enabled: Controls the enabled state of the radio button.
    /// - Returns: A styled RadioButton view without label
    @ViewBuilder
    static func RadioButton(
        checked: Bool,
        onRadioButtonClicked: @escaping () -> Void,
        enabled: Bool = true
    ) -> some View {
        LemonadeCoreRadioButton(
            checked: checked,
            onRadioButtonClicked: onRadioButtonClicked,
            enabled: enabled
        )
    }
}

// MARK: - RadioButton With Label

private struct LemonadeRadioButtonWithLabel: View {
    let checked: Bool
    let onRadioButtonClicked: () -> Void
    let label: String
    let supportText: String?
    let enabled: Bool

    var body: some View {
        SwiftUI.Button(action: {
            if !checked {
                onRadioButtonClicked()
            }
        }) {
            HStack(alignment: .top, spacing: LemonadeTheme.spaces.spacing200) {
                LemonadeCoreRadioButton(
                    checked: checked,
                    onRadioButtonClicked: onRadioButtonClicked,
                    enabled: enabled
                )

                VStack(alignment: .leading, spacing: 0) {
                    LemonadeUi.Text(
                        label,
                        textStyle: LemonadeTypography.shared.bodyMediumMedium
                    )

                    if let supportText = supportText {
                        LemonadeUi.Text(
                            supportText,
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            }
        }
        .buttonStyle(PlainButtonStyle())
        .disabled(!enabled)
    }
}

// MARK: - Core RadioButton

private struct LemonadeCoreRadioButton: View {
    let checked: Bool
    let onRadioButtonClicked: () -> Void
    let enabled: Bool

    @State private var isHovered = false

    private let componentSize: CGFloat = 20
    private let checkedCircleSize: CGFloat = 10

    private var backgroundColor: Color {
        switch (enabled, checked, isHovered) {
        case (false, _, _):
            return LemonadeTheme.colors.background.bgElevatedHigh
        case (true, true, _):
            return LemonadeTheme.colors.background.bgBrandHigh
        case (true, false, true):
            return LemonadeTheme.colors.interaction.bgSubtleInteractive
        case (true, false, false):
            return LemonadeTheme.colors.background.bgDefault
        }
    }

    private var borderColor: Color {
        switch (checked, enabled) {
        case (true, true):
            return LemonadeTheme.colors.background.bgBrandHigh
        default:
            return LemonadeTheme.colors.border.borderNeutralMedium
        }
    }

    private var centerCircleColor: Color {
        switch (checked, enabled) {
        case (true, false):
            return LemonadeTheme.colors.background.bgDefaultInverse
                .opacity(LemonadeTheme.opacity.state.opacityDisabled)
        case (true, true):
            return LemonadeTheme.colors.background.bgDefault
        default:
            return .clear
        }
    }

    var body: some View {
        SwiftUI.Button(action: {
            if !checked {
                onRadioButtonClicked()
            }
        }) {
            ZStack {
                Circle()
                    .fill(backgroundColor)
                    .overlay(
                        Circle()
                            .stroke(
                                borderColor,
                                lineWidth: (!enabled && checked) ? 0 : LemonadeTheme.borderWidth.base.border50
                            )
                    )
                    .frame(width: componentSize, height: componentSize)

                if checked {
                    Circle()
                        .fill(centerCircleColor)
                        .frame(width: checkedCircleSize, height: checkedCircleSize)
                        .transition(.scale)
                }
            }
            .animation(.easeInOut(duration: 0.15), value: checked)
            .animation(.easeInOut(duration: 0.15), value: isHovered)
        }
        .buttonStyle(PlainButtonStyle())
        .disabled(!enabled)
        .onHover { hovering in
            isHovered = hovering
        }
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeRadioButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Unlabeled radio buttons
            HStack(spacing: 16) {
                LemonadeUi.RadioButton(
                    checked: true,
                    onRadioButtonClicked: {}
                )
                LemonadeUi.RadioButton(
                    checked: false,
                    onRadioButtonClicked: {}
                )
            }

            // Disabled
            HStack(spacing: 16) {
                LemonadeUi.RadioButton(
                    checked: true,
                    onRadioButtonClicked: {},
                    enabled: false
                )
                LemonadeUi.RadioButton(
                    checked: false,
                    onRadioButtonClicked: {},
                    enabled: false
                )
            }

            // Labeled radio buttons
            VStack(alignment: .leading, spacing: 16) {
                LemonadeUi.RadioButton(
                    checked: true,
                    onRadioButtonClicked: {},
                    label: "Option 1",
                    supportText: "This is the first option"
                )

                LemonadeUi.RadioButton(
                    checked: false,
                    onRadioButtonClicked: {},
                    label: "Option 2",
                    supportText: "This is the second option"
                )

                LemonadeUi.RadioButton(
                    checked: false,
                    onRadioButtonClicked: {},
                    label: "Option 3 (Disabled)",
                    supportText: "This option is disabled",
                    enabled: false
                )
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
