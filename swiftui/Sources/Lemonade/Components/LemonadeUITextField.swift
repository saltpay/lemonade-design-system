import SwiftUI

#if canImport(UIKit)
import UIKit

/// Internal UIViewRepresentable wrapper around UITextField for cursor position control.
/// This enables LemonadeTextFieldValue support on iOS.
///
/// ## Clipboard Detection Avoidance
/// This implementation uses `textField(_:shouldChangeCharactersIn:replacementString:)` to handle
/// text changes **synchronously** before iOS applies them. This prevents iOS from detecting
/// programmatic clipboard access when the parent view formats/transforms the text.
///
/// The key insight: iOS monitors for clipboard access when text is modified programmatically
/// after user input. By intercepting changes in `shouldChangeCharactersIn` and applying the
/// formatted result directly, we avoid this detection entirely.
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
        textField.autocorrectionType = .no
        textField.text = value.text

        // Prevent vertical expansion - hug content tightly
        textField.setContentHuggingPriority(.required, for: .vertical)
        textField.setContentCompressionResistancePriority(.required, for: .vertical)

        // Set initial cursor position (clamped to valid range)
        let clampedPosition = clampedCursorPosition(value.cursorPosition, for: value.text)
        if let position = textField.position(
            from: textField.beginningOfDocument,
            offset: clampedPosition
        ) {
            textField.selectedTextRange = textField.textRange(from: position, to: position)
        }

        context.coordinator.textField = textField

        return textField
    }

    func updateUIView(_ textField: UITextField, context: Context) {
        context.coordinator.parent = self  // Keep coordinator in sync

        // Guard against re-entrant updates from our own callbacks to avoid infinite loops
        guard !context.coordinator.isUpdating else { return }
        context.coordinator.isUpdating = true
        defer { context.coordinator.isUpdating = false }

        let currentText = textField.text ?? ""
        let isEditing = context.coordinator.isUserEditing

        // Sync text if it differs (for external state updates like selector changes)
        // Only sync when not actively editing to avoid interfering with cursor position
        if !isEditing && currentText != value.text {
            textField.text = value.text
        }

        // Update cursor position when not editing (clamped to valid range for current text)
        if !isEditing {
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
        var isUserEditing = false
        weak var textField: UITextField?

        init(_ parent: LemonadeUITextField) {
            self.parent = parent
        }

        // MARK: - UITextFieldDelegate

        func textFieldDidBeginEditing(_ textField: UITextField) {
            isUserEditing = true
            parent.isFocused = true
            parent.onEditingChanged?(true)
        }

        func textFieldDidEndEditing(_ textField: UITextField) {
            isUserEditing = false
            parent.isFocused = false
            parent.onEditingChanged?(false)
        }

        /// Intercepts text changes BEFORE they're applied to avoid iOS clipboard detection.
        ///
        /// This is the key method for avoiding the "Paste from..." system prompt. By handling
        /// the text change synchronously here (computing new text, notifying parent, updating
        /// the text field ourselves), we prevent iOS from detecting programmatic clipboard access.
        func textField(
            _ textField: UITextField,
            shouldChangeCharactersIn range: NSRange,
            replacementString string: String
        ) -> Bool {
            // Get current text
            let currentText = textField.text ?? ""
            guard let textRange = Range(range, in: currentText) else {
                return true
            }

            // Calculate new text after this change
            let newText = currentText.replacingCharacters(in: textRange, with: string)

            // Calculate expected cursor position after the edit
            // For typing: cursor moves to end of inserted text
            // For deleting: cursor stays at the start of the deleted range
            let expectedCursorPosition = range.location + string.utf16.count

            // Create new value and notify parent synchronously
            let newValue = LemonadeTextFieldValue(
                text: newText,
                cursorPosition: expectedCursorPosition
            )

            // Update parent binding synchronously
            isUpdating = true
            parent.value = newValue
            parent.onValueChange?(newValue)
            isUpdating = false

            // After parent has processed, apply the (possibly transformed) result
            let resultText = parent.value.text
            let resultCursorPosition = parent.value.cursorPosition

            // Update text field directly
            textField.text = resultText

            // Position cursor appropriately
            let clampedPosition = min(max(resultCursorPosition, 0), resultText.utf16.count)
            if let newPosition = textField.position(
                from: textField.beginningOfDocument,
                offset: clampedPosition
            ) {
                textField.selectedTextRange = textField.textRange(from: newPosition, to: newPosition)
            }

            // Return false - we've handled the text change ourselves
            return false
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

// MARK: - UITextField with Visual Transformation

/// Internal UIViewRepresentable wrapper around UITextField with visual transformation support.
/// This mirrors Compose's VisualTransformation API for feature parity with KMP.
///
/// ## How it works
/// - The binding holds **raw/original** text (e.g., "+351912345678")
/// - The text field displays **transformed** text (e.g., "(+351) 912 345 678")
/// - Cursor positions are mapped between raw and transformed coordinates using `OffsetMapping`
///
/// ## Clipboard Detection Avoidance
/// Like `LemonadeUITextField`, this uses `textField(_:shouldChangeCharactersIn:replacementString:)`
/// to handle text changes synchronously, preventing iOS clipboard detection prompts.
internal struct LemonadeUITextFieldWithTransformation: UIViewRepresentable {
    /// Binding to the raw (original) text - this is what your state holds
    @Binding var rawText: String
    @Binding var isFocused: Bool
    var isEnabled: Bool
    var textStyle: LemonadeTextStyle
    var textColor: Color
    var keyboardType: UIKeyboardType = .default
    var visualTransformation: LemonadeVisualTransformation
    var onRawTextChange: ((String) -> Void)?
    var onEditingChanged: ((Bool) -> Void)?

    func makeUIView(context: Context) -> UITextField {
        let textField = UITextField()
        textField.delegate = context.coordinator
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)
        textField.borderStyle = .none
        textField.backgroundColor = .clear
        textField.isEnabled = isEnabled
        textField.keyboardType = keyboardType
        textField.autocorrectionType = .no

        // Prevent vertical expansion
        textField.setContentHuggingPriority(.required, for: .vertical)
        textField.setContentCompressionResistancePriority(.required, for: .vertical)

        // Display transformed text
        let transformed = visualTransformation.transform(rawText)
        textField.text = transformed.text

        // Set cursor at end of transformed text
        if let endPosition = textField.position(from: textField.endOfDocument, offset: 0) {
            textField.selectedTextRange = textField.textRange(from: endPosition, to: endPosition)
        }

        context.coordinator.textField = textField
        context.coordinator.currentTransformation = transformed

        return textField
    }

    func updateUIView(_ textField: UITextField, context: Context) {
        guard !context.coordinator.isUpdating else { return }
        context.coordinator.isUpdating = true
        defer { context.coordinator.isUpdating = false }

        let isEditing = context.coordinator.isUserEditing

        // Always update transformation when raw text changes
        let transformed = visualTransformation.transform(rawText)
        context.coordinator.currentTransformation = transformed

        // Sync displayed text if it differs (for external state updates)
        // Only sync when not actively editing to avoid interfering with cursor position
        let currentDisplayedText = textField.text ?? ""
        if !isEditing && currentDisplayedText != transformed.text {
            textField.text = transformed.text

            // Move cursor to end when text changes externally
            if let endPosition = textField.position(from: textField.endOfDocument, offset: 0) {
                textField.selectedTextRange = textField.textRange(from: endPosition, to: endPosition)
            }
        }

        textField.isEnabled = isEnabled
        textField.font = textStyle.uiFont
        textField.textColor = UIColor(textColor)

        if textField.keyboardType != keyboardType {
            textField.keyboardType = keyboardType
            if textField.isFirstResponder {
                textField.reloadInputViews()
            }
        }

        // Handle focus state
        if isFocused && !textField.isFirstResponder {
            textField.becomeFirstResponder()
        } else if !isFocused && textField.isFirstResponder {
            textField.resignFirstResponder()
        }
    }

    func makeCoordinator() -> TransformationCoordinator {
        TransformationCoordinator(self)
    }

    class TransformationCoordinator: NSObject, UITextFieldDelegate {
        var parent: LemonadeUITextFieldWithTransformation
        var isUpdating = false
        var isUserEditing = false
        weak var textField: UITextField?
        var currentTransformation: LemonadeTransformedText?

        init(_ parent: LemonadeUITextFieldWithTransformation) {
            self.parent = parent
        }

        func textFieldDidBeginEditing(_ textField: UITextField) {
            isUserEditing = true
            parent.isFocused = true
            parent.onEditingChanged?(true)
        }

        func textFieldDidEndEditing(_ textField: UITextField) {
            isUserEditing = false
            parent.isFocused = false
            parent.onEditingChanged?(false)
        }

        func textField(
            _ textField: UITextField,
            shouldChangeCharactersIn range: NSRange,
            replacementString string: String
        ) -> Bool {
            guard let transformation = currentTransformation else { return true }

            let displayedText = textField.text ?? ""
            guard Range(range, in: displayedText) != nil else { return true }

            // 1. Map the change from transformed coordinates to original coordinates
            let transformedStartOffset = range.location
            let transformedEndOffset = range.location + range.length

            let originalStartOffset = transformation.offsetMapping.transformedToOriginal(transformedStartOffset)
            let originalEndOffset = transformation.offsetMapping.transformedToOriginal(transformedEndOffset)

            // 2. Apply the change to the raw text
            let rawText = parent.rawText
            let rawStartIndex = rawText.index(rawText.startIndex, offsetBy: min(originalStartOffset, rawText.count))
            let rawEndIndex = rawText.index(rawText.startIndex, offsetBy: min(originalEndOffset, rawText.count))

            var newRawText = rawText
            newRawText.replaceSubrange(rawStartIndex..<rawEndIndex, with: string)

            // 3. Calculate new cursor position in raw text
            let newRawCursorPosition = originalStartOffset + string.count

            // 4. Update parent binding synchronously
            isUpdating = true
            parent.rawText = newRawText
            parent.onRawTextChange?(newRawText)
            isUpdating = false

            // 5. Re-transform with the new raw text
            let newTransformation = parent.visualTransformation.transform(parent.rawText)
            currentTransformation = newTransformation

            // 6. Update the text field with the new transformed text
            textField.text = newTransformation.text

            // 7. Map cursor position from raw to transformed and set it
            let newTransformedCursorPosition = newTransformation.offsetMapping.originalToTransformed(newRawCursorPosition)
            let clampedPosition = min(max(newTransformedCursorPosition, 0), newTransformation.text.count)

            if let newPosition = textField.position(
                from: textField.beginningOfDocument,
                offset: clampedPosition
            ) {
                textField.selectedTextRange = textField.textRange(from: newPosition, to: newPosition)
            }

            return false
        }

        func textFieldDidChangeSelection(_ textField: UITextField) {
            // Selection changes in transformed text don't need to update raw text state
            // They're handled when the user actually edits
        }

        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder()
            return true
        }
    }
}
#endif
