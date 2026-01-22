import SwiftUI

// MARK: - Switch Component

public extension LemonadeUi {
    /// A toggle switch component that provides visual feedback for on/off states.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Switch(
    ///     checked: true,
    ///     onCheckedChange: { newValue in /* handle change */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - checked: `true` if the switch is in the "on" state, `false` otherwise.
    ///   - onCheckedChange: A callback invoked when the user interacts with the switch.
    ///   - enabled: Controls the enabled state of the switch. Defaults to true.
    /// - Returns: A styled Switch view
    @ViewBuilder
    static func Switch(
        checked: Bool,
        onCheckedChange: @escaping (Bool) -> Void,
        enabled: Bool = true
    ) -> some View {
        LemonadeCoreSwitch(
            checked: checked,
            onCheckedChange: onCheckedChange,
            enabled: enabled
        )
    }

    /// A toggle switch component with label and optional support text.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Switch(
    ///     checked: true,
    ///     onCheckedChange: { newValue in /* handle change */ },
    ///     label: "Instant Settlements",
    ///     supportText: "Enable instant settlement processing"
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - checked: `true` if the switch is in the "on" state, `false` otherwise.
    ///   - onCheckedChange: A callback invoked when the user interacts with the switch.
    ///   - label: A String to be shown as the label for the component.
    ///   - supportText: Optional String shown as support text below the label.
    ///   - enabled: Controls the enabled state of the switch. Defaults to true.
    /// - Returns: A styled Switch view with label
    @ViewBuilder
    static func Switch(
        checked: Bool,
        onCheckedChange: @escaping (Bool) -> Void,
        label: String,
        supportText: String? = nil,
        enabled: Bool = true
    ) -> some View {
        LemonadeSwitchWithLabel(
            checked: checked,
            onCheckedChange: onCheckedChange,
            label: label,
            supportText: supportText,
            enabled: enabled
        )
    }
}

// MARK: - Switch With Label

private struct LemonadeSwitchWithLabel: View {
    let checked: Bool
    let onCheckedChange: (Bool) -> Void
    let label: String
    let supportText: String?
    let enabled: Bool

    var body: some View {
        SwiftUI.Button(action: { onCheckedChange(!checked) }) {
            HStack(alignment: .top, spacing: LemonadeTheme.spaces.spacing200) {
                LemonadeCoreSwitch(
                    checked: checked,
                    onCheckedChange: onCheckedChange,
                    enabled: enabled
                )

                VStack(alignment: .leading, spacing: 0) {
                    LemonadeUi.Text(
                        label,
                        textStyle: LemonadeTypography().bodyMediumMedium
                    )

                    if let supportText = supportText {
                        LemonadeUi.Text(
                            supportText,
                            textStyle: LemonadeTypography().bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)

                Spacer()
            }
        }
        .buttonStyle(PlainButtonStyle())
        .disabled(!enabled)
    }
}

// MARK: - Core Switch

private struct LemonadeCoreSwitch: View {
    let checked: Bool
    let onCheckedChange: (Bool) -> Void
    let enabled: Bool

    @State private var isPressed = false

    private let minHeight: CGFloat = 28
    private let minWidth: CGFloat = 48
    private let indicatorHeight: CGFloat = 22
    private let indicatorDefaultWidth: CGFloat = 22
    private let indicatorPressedWidth: CGFloat = 26
    private let padding: CGFloat = 2 // spacing50

    private var indicatorWidth: CGFloat {
        isPressed ? indicatorPressedWidth : indicatorDefaultWidth
    }

    private var trackColor: Color {
        switch (enabled, checked, isPressed) {
        case (false, _, _):
            return LemonadeTheme.colors.background.bgElevatedHigh
        case (true, true, true):
            return LemonadeTheme.colors.interaction.bgBrandHighPressed
        case (true, true, false):
            return LemonadeTheme.colors.background.bgBrandHigh
        case (true, false, true):
            return LemonadeTheme.colors.interaction.bgElevatedPressed
        case (true, false, false):
            return LemonadeTheme.colors.background.bgElevatedHigh
        }
    }

    private var knobColor: Color {
        enabled
            ? LemonadeTheme.colors.background.bgDefault
            : LemonadeTheme.colors.background.bgElevatedHigh
    }

    var body: some View {
        ZStack(alignment: checked ? .trailing : .leading) {
            // Track
            Capsule()
                .fill(trackColor)

            // Knob
            Capsule()
                .fill(knobColor)
                .overlay(
                    Capsule()
                        .stroke(
                            LemonadeTheme.colors.background.bgElevated,
                            lineWidth: LemonadeTheme.borderWidth.base.border25
                        )
                )
                .applyIf(enabled) { view in
                    view.lemonadeShadow(.small)
                }
                .frame(width: indicatorWidth, height: indicatorHeight)
                .padding(padding)
        }
        .frame(width: minWidth, height: minHeight)
        .animation(.easeInOut(duration: 0.2), value: checked)
        .animation(.easeInOut(duration: 0.1), value: isPressed)
        .contentShape(Capsule())
        .onTapGesture {
            if enabled {
                onCheckedChange(!checked)
            }
        }
        .simultaneousGesture(
            DragGesture(minimumDistance: 0)
                .onChanged { _ in
                    if enabled && !isPressed {
                        isPressed = true
                    }
                }
                .onEnded { _ in
                    isPressed = false
                }
        )
        .disabled(!enabled)
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
struct LemonadeSwitch_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Unlabeled switches
            HStack(spacing: 16) {
                LemonadeUi.Switch(
                    checked: true,
                    onCheckedChange: { _ in }
                )
                LemonadeUi.Switch(
                    checked: false,
                    onCheckedChange: { _ in }
                )
            }

            // Disabled
            HStack(spacing: 16) {
                LemonadeUi.Switch(
                    checked: true,
                    onCheckedChange: { _ in },
                    enabled: false
                )
                LemonadeUi.Switch(
                    checked: false,
                    onCheckedChange: { _ in },
                    enabled: false
                )
            }

            // Labeled switches
            VStack(alignment: .leading, spacing: 16) {
                LemonadeUi.Switch(
                    checked: true,
                    onCheckedChange: { _ in },
                    label: "Instant Settlements",
                    supportText: "Enable instant settlement processing"
                )

                LemonadeUi.Switch(
                    checked: false,
                    onCheckedChange: { _ in },
                    label: "Notifications",
                    supportText: "Receive push notifications"
                )

                LemonadeUi.Switch(
                    checked: true,
                    onCheckedChange: { _ in },
                    label: "Disabled Option",
                    supportText: "This option cannot be changed",
                    enabled: false
                )
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
