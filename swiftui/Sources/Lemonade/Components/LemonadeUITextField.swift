import SwiftUI

#if canImport(UIKit)
import UIKit

/// Internal UIViewRepresentable wrapper around UITextField for cursor position control.
/// This enables LemonadeTextFieldValue support on iOS.
internal struct LemonadeUITextField: UIViewRepresentable {
    @Binding var value: LemonadeTextFieldValue
    var isEnabled: Bool
    var textStyle: LemonadeTextStyle
    var textColor: Color
    var onEditingChanged: ((Bool) -> Void)?

    func makeUIView(context: Context) -> UITextField {
        let textField = UITextField()
        textField.delegate = context.coordinator
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)
        textField.borderStyle = .none
        textField.backgroundColor = .clear
        textField.isEnabled = isEnabled
        textField.text = value.text

        // Set initial cursor position
        if let position = textField.position(
            from: textField.beginningOfDocument,
            offset: value.cursorPosition
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
        // Prevent updates while user is actively editing to avoid cursor jumping
        guard !context.coordinator.isUpdating else { return }
        context.coordinator.isUpdating = true
        defer { context.coordinator.isUpdating = false }

        // Update text if changed externally
        if textField.text != value.text {
            textField.text = value.text
        }

        // Update cursor position
        if let newPosition = textField.position(
            from: textField.beginningOfDocument,
            offset: value.cursorPosition
        ) {
            let currentOffset = textField.selectedTextRange.map {
                textField.offset(from: textField.beginningOfDocument, to: $0.start)
            } ?? -1

            // Only update if different to avoid cursor jumping
            if currentOffset != value.cursorPosition {
                textField.selectedTextRange = textField.textRange(from: newPosition, to: newPosition)
            }
        }

        textField.isEnabled = isEnabled
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)
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

            parent.value = LemonadeTextFieldValue(
                text: text,
                cursorPosition: cursorPosition
            )
        }

        func textFieldDidBeginEditing(_ textField: UITextField) {
            parent.onEditingChanged?(true)
        }

        func textFieldDidEndEditing(_ textField: UITextField) {
            parent.onEditingChanged?(false)
        }

        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder()
            return true
        }

        private func getCursorPosition(_ textField: UITextField) -> Int {
            guard let selectedRange = textField.selectedTextRange else { return 0 }
            return textField.offset(from: textField.beginningOfDocument, to: selectedRange.start)
        }
    }
}

// MARK: - Focus Support

extension LemonadeUITextField {
    /// Makes the text field become first responder when focused
    func focused(_ isFocused: Bool, textField: UITextField) {
        if isFocused && !textField.isFirstResponder {
            textField.becomeFirstResponder()
        } else if !isFocused && textField.isFirstResponder {
            textField.resignFirstResponder()
        }
    }
}
#endif
