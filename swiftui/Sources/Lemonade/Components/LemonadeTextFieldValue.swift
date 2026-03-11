import Foundation

/// Represents a text field value with cursor position control.
/// This mirrors Compose's TextFieldValue for feature parity with KMP.
///
/// ## Usage
/// ```swift
/// @State var textFieldValue = LemonadeTextFieldValue(text: "Hello", cursorPosition: 5)
///
/// LemonadeUi.TextField(
///     value: $textFieldValue,
///     onValueChange: { newValue in
///         // Format text and adjust cursor
///         textFieldValue = LemonadeTextFieldValue(
///             text: formatPhoneNumber(newValue.text),
///             cursorPosition: calculateNewCursorPosition(newValue)
///         )
///     }
/// )
/// ```
public struct LemonadeTextFieldValue: Equatable {
    /// The text content of the text field
    public var text: String

    /// The cursor position as an integer offset from the start of the text.
    /// Valid range is 0 to text.count (inclusive).
    public var cursorPosition: Int

    /// Creates a TextFieldValue with text and optional cursor position.
    /// - Parameters:
    ///   - text: The text content
    ///   - cursorPosition: The cursor position. If nil, defaults to end of text.
    public init(text: String = "", cursorPosition: Int? = nil) {
        self.text = text
        let position = cursorPosition ?? text.count
        self.cursorPosition = min(max(position, 0), text.count)
    }

    /// Creates a TextFieldValue with cursor at the end of the text.
    /// - Parameter text: The text content
    public static func atEnd(_ text: String) -> LemonadeTextFieldValue {
        LemonadeTextFieldValue(text: text, cursorPosition: text.count)
    }

    /// Creates a TextFieldValue with cursor at the start of the text.
    /// - Parameter text: The text content
    public static func atStart(_ text: String) -> LemonadeTextFieldValue {
        LemonadeTextFieldValue(text: text, cursorPosition: 0)
    }
}
