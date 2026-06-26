import SwiftUI

#if canImport(UIKit)
import UIKit
#endif

/// Input modes for ``LemonadeUi/PinCode(value:variant:length:error:submitting:onComplete:)``.
/// Selects which system keyboard is requested.
public enum LemonadePinCodeVariant {
    /// Requests a numeric keyboard.
    case numeric
    /// Requests the full keyboard.
    case alphanumeric
}

// MARK: - PinCode Component

public extension LemonadeUi {
    /// A PIN code entry component rendering the entered characters as a row of boxes.
    ///
    /// Each box mirrors the styling of ``LemonadeUi/TextField(input:onInputChanged:label:optionalIndicator:supportText:placeholderText:errorMessage:error:enabled:)``
    /// — same border, focus ring, error and disabled states, font, and height.
    ///
    /// Input always comes from the device's system keyboard, surfaced through a hidden field
    /// overlaid on the boxes. The `variant` only selects which keyboard appears:
    /// - `.numeric` requests a numeric keyboard.
    /// - `.alphanumeric` requests the full keyboard.
    ///
    /// The indicator renders `length` boxes; each typed character is shown in its box. The
    /// component appends to and removes from `value` internally, never letting it grow past
    /// `length`.
    ///
    /// The hidden field uses the accessibility identifier `pin_code_field`, a stable contract for
    /// E2E tests.
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
    ///   - variant: Which system keyboard to request for input.
    ///   - length: The number of characters to enter. Defaults to 6.
    ///   - error: When true the boxes turn critical and shake. Re-triggers on each rising edge.
    ///   - submitting: When true the boxes show the disabled style and input is disabled.
    ///   - onComplete: Called once when `value` reaches `length`.
    /// - Returns: A styled PinCode view.
    static func PinCode(
        value: Binding<String>,
        variant: LemonadePinCodeVariant = .numeric,
        length: Int = 6,
        error: Bool = false,
        submitting: Bool = false,
        onComplete: ((String) -> Void)? = nil
    ) -> some View {
        precondition(length > 0, "PinCode length must be greater than zero.")
        return LemonadePinCodeView(
            value: value,
            variant: variant,
            length: length,
            error: error,
            submitting: submitting,
            onComplete: onComplete
        )
    }
}

// MARK: - Internal PinCode View

private struct LemonadePinCodeView: View {
    @Binding var value: String
    let variant: LemonadePinCodeVariant
    let length: Int
    let error: Bool
    let submitting: Bool
    let onComplete: ((String) -> Void)?

    @State private var shakeTrigger: CGFloat = 0
    // Focus is owned here and read directly by the indicator (for the active ring) and bound
    // into the hidden field — no @State/@FocusState sync loop, so a tap focuses immediately.
    @FocusState private var focused: Bool

    var body: some View {
        ZStack {
            PinCodeIndicator(
                value: value,
                length: length,
                error: error,
                enabled: !submitting,
                focused: focused
            )
            .equatable()
            .modifier(ShakeEffect(animatableData: shakeTrigger))

            PinCodeHiddenField(
                value: $value,
                variant: variant,
                length: length,
                enabled: !submitting,
                focused: $focused
            )
        }
        .animation(.easeInOut(duration: 0.15), value: focused)
        .animation(.easeInOut(duration: 0.15), value: error)
        .onChange(of: error) { isError in
            guard isError else { return }
            #if canImport(UIKit)
            UINotificationFeedbackGenerator().notificationOccurred(.error)
            #endif
            withAnimation(.linear(duration: 0.3)) { shakeTrigger += 1 }
        }
        .onAppear { clampAndReport(value) }
        .onChange(of: value) { clampAndReport($0) }
    }

    /// Enforces the "`value` stays clamped to `length`" contract for any value — including one set
    /// externally (e.g. restoring state) — then reports completion off the clamped result.
    private func clampAndReport(_ newValue: String) {
        let clamped = String(newValue.prefix(length))
        if clamped != newValue { value = clamped }
        if clamped.count == length { onComplete?(clamped) }
    }
}

// MARK: - Indicator

// `Equatable` lets SwiftUI skip the whole indicator (and its `length` boxes) when none of
// its inputs changed — e.g. while another PinCode on the same screen is being edited.
private struct PinCodeIndicator: View, Equatable {
    let value: String
    let length: Int
    let error: Bool
    let enabled: Bool
    let focused: Bool

    private var filledCount: Int { min(value.count, length) }

    var body: some View {
        // Index the string once per render rather than re-walking it for every box.
        let characters = Array(value)
        // Each box is `maxWidth: .infinity`, so the row stretches to fill the width it's given
        // and the boxes split it evenly.
        HStack(spacing: LemonadeTheme.spaces.spacing200) {
            ForEach(0 ..< length, id: \.self) { index in
                boxCell(character: index < characters.count ? characters[index] : nil, index: index)
            }
        }
        .frame(maxWidth: .infinity)
    }

    private func boxCell(character: Character?, index: Int) -> some View {
        // The "active" cell — the next one to fill — shows the focus ring while the
        // keyboard is up, matching a focused text field.
        let isActive = enabled && focused && index == filledCount
        return Color.clear
            .frame(maxWidth: .infinity)
            .overlay {
                if let character {
                    LemonadeUi.Text(
                        String(character),
                        textStyle: LemonadeTypography.shared.bodyMediumMedium,
                        color: LemonadeTheme.colors.content.contentPrimary
                    )
                }
            }
            .modifier(TextFieldContainerModifier(
                backgroundColor: textFieldBackgroundColor(enabled: enabled, error: error, isFocused: isActive, isHovered: false),
                borderColor: textFieldBorderColor(enabled: enabled, isFocused: isActive, error: error),
                enabled: enabled,
                isFocused: isActive,
                cornerRadius: TextFieldConstants.cornerRadius,
                isHovered: .constant(false)
            ))
    }
}

// MARK: - Hidden field

private struct PinCodeHiddenField: View {
    @Binding var value: String
    let variant: LemonadePinCodeVariant
    let length: Int
    let enabled: Bool
    @FocusState.Binding var focused: Bool

    var body: some View {
        // Full-bleed transparent field: a tap anywhere on the boxes lands here and focuses it
        // natively, so the keyboard opens on the first tap with no sync round-trip.
        TextField("", text: clampedBinding)
            .focused($focused)
            .autocorrectionDisabled(true)
            .foregroundColor(.clear)
            .tint(.clear)
            .disabled(!enabled)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .contentShape(Rectangle())
            .accessibilityIdentifier("pin_code_field")
            .modifier(SystemKeyboardTraits(variant: variant))
    }

    private var clampedBinding: Binding<String> {
        Binding(
            get: { value },
            set: { value = String($0.prefix(length)) }
        )
    }
}

private struct SystemKeyboardTraits: ViewModifier {
    let variant: LemonadePinCodeVariant

    func body(content: Content) -> some View {
        #if os(iOS)
        switch variant {
        case .numeric:
            content.keyboardType(.numberPad)
        case .alphanumeric:
            content
                .keyboardType(.asciiCapable)
                .textInputAutocapitalization(.never)
        }
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


// MARK: - Previews

#if DEBUG
struct LemonadePinCode_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 32) {
            LemonadeUi.PinCode(value: .constant("123"))

            LemonadeUi.PinCode(
                value: .constant("123"),
                error: true
            )

            LemonadeUi.PinCode(
                value: .constant("12"),
                submitting: true
            )

            LemonadeUi.PinCode(
                value: .constant("aB3"),
                variant: .alphanumeric
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
