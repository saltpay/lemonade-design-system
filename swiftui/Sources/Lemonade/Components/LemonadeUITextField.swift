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
    var keyboardType: UIKeyboardType
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
        textField.isEnabled = isEnabled
        textField.keyboardType = keyboardType
        textField.text = value.text

        // Set initial cursor position (clamped to valid range)
        let clampedPosition = clampedCursorPosition(value.cursorPosition, for: value.text)
        if let position = textField.position(
            from: textField.beginningOfDocument,
            offset: clampedPosition
        ) {
            textField.selectedTextRange = textField.textRange(from: position, to: position)
        }

        // Add target for text changes
        textField.addTarget(
            context.coordinator,
            action: #selector(Coordinator.textFieldDidChange(_:)),
            for: .editingChanged
        )

        return textField
    }

    func updateUIView(_ textField: UITextField, context: Context) {
        // Guard against re-entrant updates from our own callbacks to avoid infinite loops
        guard !context.coordinator.isUpdating else { return }
        context.coordinator.isUpdating = true
        defer { context.coordinator.isUpdating = false }

        let currentText = textField.text ?? ""

        // Update text if changed externally
        if currentText != value.text {
            textField.text = value.text
        }

        // Update cursor position (clamped to valid range for current text)
        let textForCursor = textField.text ?? ""
        let clampedPosition = clampedCursorPosition(value.cursorPosition, for: textForCursor)
        if let newPosition = textField.position(
            from: textField.beginningOfDocument,
            offset: clampedPosition
        ) {
            let currentOffset = textField.selectedTextRange.map {
                textField.offset(from: textField.beginningOfDocument, to: $0.start)
            } ?? -1

            // Only update if different to avoid cursor jumping
            if currentOffset != clampedPosition {
                textField.selectedTextRange = textField.textRange(from: newPosition, to: newPosition)
            }
        }

        textField.isEnabled = isEnabled
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)

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
                let newValue = LemonadeTextFieldValue(
                    text: parent.value.text,
                    cursorPosition: cursorPosition
                )
                parent.value = newValue
                parent.onValueChange?(newValue)
                isUpdating = false
            }
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
#endif
