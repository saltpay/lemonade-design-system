import SwiftUI

#if canImport(UIKit)
import UIKit
#endif

/// Input modes for ``LemonadeUi/PinCode(value:variant:length:masked:error:submitting:onBiometryClick:onComplete:)``.
public enum LemonadePinCodeVariant {
    /// Digits only, entered through the component's built-in on-screen numpad.
    case numeric
    /// Any character, entered through the device's system keyboard.
    case alphanumeric
}

// MARK: - PinCode Component

public extension LemonadeUi {
    /// A PIN code entry component combining a progress indicator with an input mechanism.
    ///
    /// The `variant` decides how characters are entered:
    /// - `.numeric` renders a built-in on-screen numpad with an optional biometry key.
    ///   `onBiometryClick` is only honored in this variant.
    /// - `.alphanumeric` uses the device's system keyboard; no numpad is shown.
    ///
    /// The indicator renders `length` cells. When `masked` is true (the default) each entered
    /// character shows as a filled dot; when false the typed characters are shown in boxes. The
    /// component appends to and removes from `value` internally, never letting it grow past
    /// `length`.
    ///
    /// The keypad exposes the accessibility identifiers `digit_0`...`digit_9`, `btn_delete` and
    /// `btn_biometry`; the alphanumeric hidden field uses `pin_code_field`. These are a stable
    /// contract for E2E tests.
    ///
    /// ## Usage
    /// ```swift
    /// @State private var pin = ""
    ///
    /// LemonadeUi.PinCode(
    ///     value: $pin,
    ///     onComplete: { code in /* validate */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - value: The current entry. The component keeps it clamped to `length`.
    ///   - variant: Whether input comes from the built-in numpad or the system keyboard.
    ///   - length: The number of characters to enter. Defaults to 6.
    ///   - masked: When true the indicator shows dots; when false it shows the typed characters.
    ///   - error: When true the indicator turns critical and shakes. Re-triggers on each rising edge.
    ///   - submitting: When true the indicator dims to a filled loading state and input is disabled.
    ///   - onBiometryClick: Optional biometry key on the numpad. Ignored for `.alphanumeric`.
    ///   - onComplete: Called once when `value` reaches `length`.
    /// - Returns: A styled PinCode view.
    @ViewBuilder
    static func PinCode(
        value: Binding<String>,
        variant: LemonadePinCodeVariant = .numeric,
        length: Int = 6,
        masked: Bool = true,
        error: Bool = false,
        submitting: Bool = false,
        onBiometryClick: (() -> Void)? = nil,
        onComplete: ((String) -> Void)? = nil
    ) -> some View {
        LemonadePinCodeView(
            value: value,
            variant: variant,
            length: length,
            masked: masked,
            error: error,
            submitting: submitting,
            onBiometryClick: onBiometryClick,
            onComplete: onComplete
        )
    }
}

// MARK: - Internal PinCode View

private struct LemonadePinCodeView: View {
    @Binding var value: String
    let variant: LemonadePinCodeVariant
    let length: Int
    let masked: Bool
    let error: Bool
    let submitting: Bool
    let onBiometryClick: (() -> Void)?
    let onComplete: ((String) -> Void)?

    @State private var shakeTrigger: CGFloat = 0

    var body: some View {
        VStack(spacing: LemonadeTheme.spaces.spacing800) {
            switch variant {
            case .numeric:
                indicator
                PinCodeNumpad(
                    onDigit: append,
                    onDelete: deleteLast,
                    onBiometryClick: onBiometryClick,
                    enabled: !submitting,
                    hasValue: !value.isEmpty
                )
            case .alphanumeric:
                ZStack {
                    indicator
                    PinCodeHiddenField(value: $value, length: length, enabled: !submitting)
                }
            }
        }
        .onChange(of: error) { isError in
            guard isError else { return }
            #if canImport(UIKit)
            UINotificationFeedbackGenerator().notificationOccurred(.error)
            #endif
            withAnimation(.linear(duration: 0.4)) { shakeTrigger += 1 }
        }
        .onChange(of: value) { newValue in
            if newValue.count == length { onComplete?(newValue) }
        }
    }

    private var indicator: some View {
        PinCodeIndicator(
            value: value,
            length: length,
            masked: masked,
            error: error,
            submitting: submitting
        )
        .modifier(ShakeEffect(animatableData: shakeTrigger))
    }

    private func append(_ digit: String) {
        guard value.count < length else { return }
        value += digit
    }

    private func deleteLast() {
        guard !value.isEmpty else { return }
        value.removeLast()
    }
}

// MARK: - Indicator

private struct PinCodeIndicator: View {
    let value: String
    let length: Int
    let masked: Bool
    let error: Bool
    let submitting: Bool

    private var filledCount: Int { min(value.count, length) }

    var body: some View {
        if masked {
            HStack(spacing: LemonadeTheme.spaces.spacing600) {
                ForEach(0 ..< length, id: \.self) { index in
                    let filled = submitting || index < filledCount
                    Circle()
                        .fill(dotColor(filled: filled))
                        .frame(width: dotSize, height: dotSize)
                }
            }
            .opacity(submitting ? submittingAlpha : 1)
            .animation(.easeInOut(duration: 0.15), value: filledCount)
            .animation(.easeInOut(duration: 0.2), value: submitting)
        } else {
            HStack(spacing: LemonadeTheme.spaces.spacing300) {
                ForEach(0 ..< length, id: \.self) { index in
                    boxCell(index: index)
                }
            }
            .frame(maxWidth: indicatorMaxWidth)
            .padding(.horizontal, LemonadeTheme.spaces.spacing400)
        }
    }

    private func dotColor(filled: Bool) -> Color {
        if filled && error { return LemonadeTheme.colors.content.contentCritical }
        if filled { return LemonadeTheme.colors.content.contentPrimary }
        return LemonadeTheme.colors.border.borderNeutralLow
    }

    private func boxCell(index: Int) -> some View {
        let character = characterAt(index)
        let isActive = !submitting && index == filledCount
        return RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300)
            .strokeBorder(boxBorderColor(isActive: isActive), lineWidth: LemonadeTheme.borderWidth.base.border25)
            .frame(maxWidth: .infinity)
            .frame(height: LemonadeTheme.sizes.size1600)
            .overlay {
                if let character {
                    LemonadeUi.Text(
                        String(character),
                        textStyle: LemonadeTypography.shared.headingMedium,
                        color: error
                            ? LemonadeTheme.colors.content.contentCritical
                            : LemonadeTheme.colors.content.contentPrimary
                    )
                }
            }
    }

    private func characterAt(_ index: Int) -> Character? {
        let characters = Array(value)
        guard index < characters.count else { return nil }
        return characters[index]
    }

    private func boxBorderColor(isActive: Bool) -> Color {
        if error { return LemonadeTheme.colors.border.borderCritical }
        if isActive { return LemonadeTheme.colors.border.borderSelected }
        return LemonadeTheme.colors.border.borderNeutralLow
    }
}

// MARK: - Numpad

private struct PinCodeNumpad: View {
    let onDigit: (String) -> Void
    let onDelete: () -> Void
    let onBiometryClick: (() -> Void)?
    let enabled: Bool
    let hasValue: Bool

    private let rows = [["1", "2", "3"], ["4", "5", "6"], ["7", "8", "9"]]

    var body: some View {
        VStack(spacing: LemonadeTheme.spaces.spacing600) {
            ForEach(rows, id: \.self) { row in
                HStack(spacing: LemonadeTheme.spaces.spacing500) {
                    ForEach(row, id: \.self) { digit in
                        cell {
                            PinCodeKey(label: digit, enabled: enabled, action: { onDigit(digit) })
                                .accessibilityIdentifier("digit_\(digit)")
                        }
                    }
                }
            }

            HStack(spacing: LemonadeTheme.spaces.spacing500) {
                cell {
                    if let onBiometryClick {
                        PinCodeIconKey(
                            icon: .fingerPrint,
                            contentDescription: "Use biometrics",
                            enabled: enabled,
                            action: onBiometryClick
                        )
                        .accessibilityIdentifier("btn_biometry")
                    } else {
                        Color.clear.frame(width: LemonadeTheme.sizes.size2000, height: LemonadeTheme.sizes.size2000)
                    }
                }
                cell {
                    PinCodeKey(label: "0", enabled: enabled, action: { onDigit("0") })
                        .accessibilityIdentifier("digit_0")
                }
                cell {
                    PinCodeIconKey(
                        icon: .backspace,
                        contentDescription: "Delete",
                        enabled: enabled && hasValue,
                        action: onDelete
                    )
                    .opacity(hasValue ? 1 : 0)
                    .animation(.easeInOut(duration: 0.2), value: hasValue)
                    .accessibilityIdentifier("btn_delete")
                }
            }
        }
        .frame(maxWidth: numpadMaxWidth)
        .padding(.horizontal, LemonadeTheme.spaces.spacing400)
        .padding(.bottom, LemonadeTheme.spaces.spacing800)
    }

    @ViewBuilder
    private func cell<Content: View>(@ViewBuilder content: () -> Content) -> some View {
        content().frame(maxWidth: .infinity)
    }
}

// MARK: - Numpad keys

private struct PinCodeKey: View {
    let label: String
    let enabled: Bool
    let action: () -> Void

    var body: some View {
        Button {
            performHaptic()
            action()
        } label: {
            LemonadeUi.Text(
                label,
                textStyle: LemonadeTypography.shared.displaySmall,
                color: LemonadeTheme.colors.content.contentPrimary
            )
            .frame(width: LemonadeTheme.sizes.size2000, height: LemonadeTheme.sizes.size2000)
            .modifier(PinCodeButtonChrome())
            .contentShape(Circle())
        }
        .buttonStyle(PinCodeButtonStyle())
        .disabled(!enabled)
    }
}

private struct PinCodeIconKey: View {
    let icon: LemonadeIcon
    let contentDescription: String
    let enabled: Bool
    let action: () -> Void

    var body: some View {
        Button {
            performHaptic()
            action()
        } label: {
            LemonadeUi.Icon(
                icon: icon,
                contentDescription: contentDescription,
                size: .xLarge,
                tint: LemonadeTheme.colors.content.contentTertiary
            )
            .frame(width: LemonadeTheme.sizes.size2000, height: LemonadeTheme.sizes.size2000)
            .contentShape(Circle())
        }
        .buttonStyle(PinCodeButtonStyle())
        .disabled(!enabled)
    }
}

private struct PinCodeButtonChrome: ViewModifier {
    func body(content: Content) -> some View {
        #if os(iOS)
        if #available(iOS 26.0, *) {
            content.glassEffect(.regular.interactive(), in: Circle())
        } else {
            content.background(borderCircle)
        }
        #else
        content.background(borderCircle)
        #endif
    }

    private var borderCircle: some View {
        Circle().stroke(
            LemonadeTheme.colors.border.borderNeutralLow,
            lineWidth: LemonadeTheme.borderWidth.base.border25
        )
    }
}

private struct PinCodeButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .background(
                Circle().fill(LemonadeTheme.colors.background.bgElevated.opacity(configuration.isPressed ? 1 : 0))
            )
            .scaleEffect(configuration.isPressed ? 0.92 : 1.0)
            .animation(.easeOut(duration: 0.12), value: configuration.isPressed)
    }
}

// MARK: - Alphanumeric hidden field

private struct PinCodeHiddenField: View {
    @Binding var value: String
    let length: Int
    let enabled: Bool

    @FocusState private var focused: Bool

    var body: some View {
        TextField("", text: clampedBinding)
            .focused($focused)
            .autocorrectionDisabled(true)
            .foregroundColor(.clear)
            .tint(.clear)
            .disabled(!enabled)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .contentShape(Rectangle())
            .accessibilityIdentifier("pin_code_field")
            .modifier(SystemKeyboardTraits())
            .onAppear { focused = true }
    }

    private var clampedBinding: Binding<String> {
        Binding(
            get: { value },
            set: { value = String($0.prefix(length)) }
        )
    }
}

private struct SystemKeyboardTraits: ViewModifier {
    func body(content: Content) -> some View {
        #if os(iOS)
        content
            .keyboardType(.asciiCapable)
            .textInputAutocapitalization(.never)
        #else
        content
        #endif
    }
}

// MARK: - Shake

private struct ShakeEffect: GeometryEffect {
    var amount: CGFloat = 10
    var shakesPerUnit: CGFloat = 3
    var animatableData: CGFloat

    func effectValue(size: CGSize) -> ProjectionTransform {
        ProjectionTransform(
            CGAffineTransform(translationX: amount * sin(animatableData * .pi * shakesPerUnit), y: 0)
        )
    }
}

// MARK: - Constants

private let dotSize: CGFloat = 16
private let submittingAlpha: Double = 0.2
private let numpadMaxWidth: CGFloat = 360
private let indicatorMaxWidth: CGFloat = 360

private func performHaptic() {
    #if canImport(UIKit)
    UIImpactFeedbackGenerator(style: .light).impactOccurred()
    #endif
}

// MARK: - Previews

#if DEBUG
struct LemonadePinCode_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 32) {
            LemonadeUi.PinCode(
                value: .constant("123"),
                onBiometryClick: {}
            )

            LemonadeUi.PinCode(
                value: .constant("123"),
                error: true
            )

            LemonadeUi.PinCode(
                value: .constant("aB3"),
                variant: .alphanumeric,
                masked: false
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
