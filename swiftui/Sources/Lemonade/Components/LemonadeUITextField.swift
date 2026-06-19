import SwiftUI

#if canImport(UIKit)
import UIKit

/// Internal UIViewRepresentable wrapper around UITextField for cursor position control.
/// This enables LemonadeTextFieldValue support on iOS.
///
/// - Note: Cursor positions are measured in UTF-16 code units to match UIKit's internal indexing
///   and maintain compatibility with Kotlin/Compose on Android.
internal struct LemonadeUITextField: UIViewRepresentable {
    @Binding var value: LemonadeTextFieldValue
    @Binding var isFocused: Bool
    var isEnabled: Bool
    var textStyle: LemonadeTextStyle
    var textColor: Color
    var keyboardType: UIKeyboardType = .default
    /// Enables native secure text entry (character masking). Note: UIKit clears
    /// the field's text and selection when this flips, so `updateUIView` applies
    /// it before re-synchronizing text and cursor to keep them stable on toggle.
    var isSecure: Bool = false
    var onValueChange: ((LemonadeTextFieldValue) -> Void)?
    var onEditingChanged: ((Bool) -> Void)?

    /// Clamps cursor position to valid UTF-16 range for the given text
    private func clampedCursorPosition(_ position: Int, for text: String) -> Int {
        let utf16Length = text.utf16.count
        return min(max(position, 0), utf16Length)
    }

    func makeUIView(context: Context) -> UITextField {
        let textField = UITextField()
        textField.delegate = context.coordinator
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)
        textField.borderStyle = .none
        textField.backgroundColor = .clear
        textField.tintColor = UIColor(textColor)
        textField.isEnabled = isEnabled
        textField.keyboardType = keyboardType
        textField.isSecureTextEntry = isSecure
        textField.text = value.text

        // Let SwiftUI compress the field to the available width instead of growing it to the
        // text's intrinsic width. Without this, long input pushes the layout past the screen edge.
        textField.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        textField.setContentHuggingPriority(.defaultLow, for: .horizontal)

        // Set initial cursor position (clamped to valid range)
        textField.setCaret(toUTF16Offset: clampedCursorPosition(value.cursorPosition, for: value.text))

        // Add target for text changes
        textField.addTarget(
            context.coordinator,
            action: #selector(Coordinator.textFieldDidChange(_:)),
            for: .editingChanged
        )

        return textField
    }

    func updateUIView(_ textField: UITextField, context: Context) {
        context.coordinator.parent = self  // Keep coordinator in sync

        // Guard against re-entrant updates from our own callbacks to avoid infinite loops
        guard !context.coordinator.isUpdating else { return }
        context.coordinator.isUpdating = true
        defer { context.coordinator.isUpdating = false }

        // Toggle secure entry *before* synchronizing text/cursor: UIKit clears a
        // field's contents and selection when isSecureTextEntry flips, so the text +
        // cursor sync below must run afterwards to restore them.
        if textField.isSecureTextEntry != isSecure {
            textField.isSecureTextEntry = isSecure
        }

        let currentText = textField.text ?? ""

        // Update text if changed externally
        if currentText != value.text {
            textField.text = value.text
        }

        // Update cursor position (clamped to valid range for current text)
        let textForCursor = textField.text ?? ""
        let clampedPosition = clampedCursorPosition(value.cursorPosition, for: textForCursor)
        let currentOffset = textField.selectedTextRange.map {
            textField.offset(from: textField.beginningOfDocument, to: $0.start)
        } ?? -1

        // Only update if different to avoid cursor jumping
        if currentOffset != clampedPosition {
            textField.setCaret(toUTF16Offset: clampedPosition)
        }

        textField.isEnabled = isEnabled
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)
        textField.tintColor = UIColor(textColor)

        // Update keyboard type and reload if changed while focused
        if textField.keyboardType != keyboardType {
            textField.keyboardType = keyboardType
            if textField.isFirstResponder {
                textField.reloadInputViews()
            }
        }

        // Handle focus state
        updateFocus(textField)
    }

    private func updateFocus(_ textField: UITextField) {
        if isFocused && !textField.isFirstResponder {
            textField.becomeFirstResponder()
        } else if !isFocused && textField.isFirstResponder {
            textField.resignFirstResponder()
        }
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, UITextFieldDelegate {
        var parent: LemonadeUITextField
        var isUpdating = false

        init(_ parent: LemonadeUITextField) {
            self.parent = parent
        }

        @objc func textFieldDidChange(_ textField: UITextField) {
            guard !isUpdating else { return }

            let text = textField.text ?? ""
            let cursorPosition = getCursorPosition(textField)

            let newValue = LemonadeTextFieldValue(
                text: text,
                cursorPosition: cursorPosition
            )
            parent.value = newValue
            parent.onValueChange?(newValue)
        }

        func textFieldDidBeginEditing(_ textField: UITextField) {
            parent.isFocused = true
            parent.onEditingChanged?(true)
        }

        func textFieldDidEndEditing(_ textField: UITextField) {
            parent.isFocused = false
            parent.onEditingChanged?(false)
        }

        func textFieldDidChangeSelection(_ textField: UITextField) {
            guard !isUpdating else { return }

            let cursorPosition = getCursorPosition(textField)
            if parent.value.cursorPosition != cursorPosition {
                isUpdating = true
                defer { isUpdating = false }
                let newValue = LemonadeTextFieldValue(
                    text: parent.value.text,
                    cursorPosition: cursorPosition
                )
                parent.value = newValue
                parent.onValueChange?(newValue)
            }
        }

        func textField(
            _ textField: UITextField,
            shouldChangeCharactersIn range: NSRange,
            replacementString string: String
        ) -> Bool {
            // A secure UITextField wipes all of its text on the first edit after the
            // text was assigned programmatically — which we do when restoring content
            // across a secure-entry toggle. Apply the edit ourselves and return false
            // so UIKit never runs that auto-clear, keeping the field stable when the
            // user keeps typing after a show/hide toggle.
            guard textField.isSecureTextEntry else { return true }
            guard let oldText = textField.text,
                  let replaceRange = Range(range, in: oldText) else { return true }

            textField.text = oldText.replacingCharacters(in: replaceRange, with: string)

            // Place the cursor right after the inserted text (UTF-16 offset).
            textField.setCaret(toUTF16Offset: range.location + (string as NSString).length)

            // Programmatic text changes don't fire `.editingChanged`, so propagate manually.
            textFieldDidChange(textField)
            return false
        }

        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder()
            return true
        }

        /// Returns cursor position as UTF-16 code unit offset (matches UIKit's internal indexing)
        private func getCursorPosition(_ textField: UITextField) -> Int {
            guard let selectedRange = textField.selectedTextRange else { return 0 }
            return textField.offset(from: textField.beginningOfDocument, to: selectedRange.start)
        }
    }
}

private extension UITextField {
    /// Places the caret (a collapsed selection) at the given UTF-16 code unit offset.
    func setCaret(toUTF16Offset offset: Int) {
        guard let position = position(from: beginningOfDocument, offset: offset) else { return }
        selectedTextRange = textRange(from: position, to: position)
    }
}
#endif
