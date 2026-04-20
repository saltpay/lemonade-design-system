import Foundation

/// Protocol for transforming text visually while maintaining cursor position mapping.
/// This mirrors Compose's VisualTransformation API for feature parity with KMP.
///
/// ## Usage
/// ```swift
/// struct PhoneNumberTransformation: LemonadeVisualTransformation {
///     func transform(_ text: String) -> LemonadeTransformedText {
///         let formatted = formatPhoneNumber(text)
///         return LemonadeTransformedText(
///             text: formatted,
///             offsetMapping: PhoneNumberOffsetMapping(original: text, transformed: formatted)
///         )
///     }
/// }
/// ```
///
/// Use with `LemonadeUi.TextField(value:visualTransformation:...)` to display
/// formatted text while keeping raw data as the underlying state.
public protocol LemonadeVisualTransformation {
    /// Transforms the input text and provides cursor position mapping.
    /// - Parameter text: The original (raw) text
    /// - Returns: Transformed text with offset mapping for cursor positions
    func transform(_ text: String) -> LemonadeTransformedText
}

/// The result of a visual transformation, containing the formatted text
/// and mapping for cursor positions.
public struct LemonadeTransformedText {
    /// The visually transformed (formatted) text to display
    public let text: String

    /// Mapping between original and transformed cursor positions
    public let offsetMapping: LemonadeOffsetMapping

    public init(text: String, offsetMapping: LemonadeOffsetMapping) {
        self.text = text
        self.offsetMapping = offsetMapping
    }
}

/// Protocol for mapping cursor positions between original and transformed text.
///
/// When the user's cursor is at position X in the transformed (displayed) text,
/// we need to know where that corresponds to in the original (raw) text, and vice versa.
///
/// ## Example
/// Original: "1234567890" (10 chars)
/// Transformed: "123-456-7890" (12 chars)
///
/// - Cursor after "123" in original (offset 3) → after "123" in transformed (offset 3)
/// - Cursor after "1234" in original (offset 4) → after "123-4" in transformed (offset 5)
/// - Cursor after "123-" in transformed (offset 4) → after "123" in original (offset 3)
public protocol LemonadeOffsetMapping {
    /// Maps a cursor position from the original text to the transformed text.
    /// - Parameter offset: Position in the original (raw) text
    /// - Returns: Corresponding position in the transformed (displayed) text
    func originalToTransformed(_ offset: Int) -> Int

    /// Maps a cursor position from the transformed text to the original text.
    /// - Parameter offset: Position in the transformed (displayed) text
    /// - Returns: Corresponding position in the original (raw) text
    func transformedToOriginal(_ offset: Int) -> Int
}

// MARK: - Built-in Offset Mappings

/// Identity mapping - no transformation, cursor positions are unchanged.
public struct LemonadeIdentityOffsetMapping: LemonadeOffsetMapping {
    public init() {}

    public func originalToTransformed(_ offset: Int) -> Int { offset }
    public func transformedToOriginal(_ offset: Int) -> Int { offset }
}

/// Offset mapping that counts digits/significant characters to handle separators.
///
/// This is useful for phone numbers, credit cards, etc. where separators (spaces, dashes)
/// are added but the underlying data is just digits.
///
/// ## Example
/// Original: "+351912345678"
/// Transformed: "(+351) 912 345 678"
///
/// The mapping counts digits and '+' signs to find corresponding positions.
public struct LemonadeSeparatorOffsetMapping: LemonadeOffsetMapping {
    private let original: String
    private let transformed: String
    private let isSignificant: (Character) -> Bool

    /// Creates a separator offset mapping.
    /// - Parameters:
    ///   - original: The original (raw) text
    ///   - transformed: The transformed (formatted) text
    ///   - isSignificant: Function to determine if a character is significant (default: digits and '+')
    public init(
        original: String,
        transformed: String,
        isSignificant: @escaping (Character) -> Bool = { $0.isNumber || $0 == "+" }
    ) {
        self.original = original
        self.transformed = transformed
        self.isSignificant = isSignificant
    }

    public func originalToTransformed(_ offset: Int) -> Int {
        guard !original.isEmpty, !transformed.isEmpty else { return 0 }
        guard offset < original.count else { return transformed.count }

        // Count significant characters in original up to offset
        let originalArray = Array(original)
        var significantCount = 0
        for i in 0..<offset {
            if isSignificant(originalArray[i]) {
                significantCount += 1
            }
        }

        // Find position in transformed where we've seen the same number of significant chars
        let transformedArray = Array(transformed)
        var seen = 0
        for i in 0..<transformedArray.count {
            if seen >= significantCount { return i }
            if isSignificant(transformedArray[i]) {
                seen += 1
            }
        }

        return transformed.count
    }

    public func transformedToOriginal(_ offset: Int) -> Int {
        guard !original.isEmpty, !transformed.isEmpty else { return 0 }
        guard offset < transformed.count else { return original.count }

        // Count significant characters in transformed up to offset
        let transformedArray = Array(transformed)
        var significantCount = 0
        for i in 0..<offset {
            if isSignificant(transformedArray[i]) {
                significantCount += 1
            }
        }

        // Find position in original where we've seen the same number of significant chars
        let originalArray = Array(original)
        var seen = 0
        for i in 0..<originalArray.count {
            if seen >= significantCount { return i }
            if isSignificant(originalArray[i]) {
                seen += 1
            }
        }

        return original.count
    }
}

/// Offset mapping that prevents cursor from going before a prefix.
/// Useful for fields where a prefix (like '+' in phone numbers) should not be editable.
public struct LemonadePrefixProtectedOffsetMapping: LemonadeOffsetMapping {
    private let prefixLength: Int

    /// Creates a prefix-protected offset mapping.
    /// - Parameter prefixLength: The length of the prefix to protect
    public init(prefixLength: Int) {
        self.prefixLength = max(0, prefixLength)
    }

    public func originalToTransformed(_ offset: Int) -> Int {
        return offset < prefixLength ? prefixLength : offset
    }

    public func transformedToOriginal(_ offset: Int) -> Int {
        return offset < prefixLength ? prefixLength : offset
    }
}

// MARK: - Identity Transformation

/// A no-op visual transformation that passes text through unchanged.
public struct LemonadeIdentityTransformation: LemonadeVisualTransformation {
    public init() {}

    public func transform(_ text: String) -> LemonadeTransformedText {
        LemonadeTransformedText(text: text, offsetMapping: LemonadeIdentityOffsetMapping())
    }
}
