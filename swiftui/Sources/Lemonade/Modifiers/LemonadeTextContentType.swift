import SwiftUI

#if canImport(UIKit)
import UIKit

// MARK: - Environment

private struct LemonadeTextContentTypeKey: EnvironmentKey {
    static let defaultValue: UITextContentType? = nil
}

extension EnvironmentValues {
    var lemonadeTextContentType: UITextContentType? {
        get { self[LemonadeTextContentTypeKey.self] }
        set { self[LemonadeTextContentTypeKey.self] = newValue }
    }
}

// MARK: - View Modifier

public extension View {
    /// Sets the text content type for the Lemonade text fields in this hierarchy,
    /// driving iOS AutoFill (e.g. `.username`, `.password`, `.newPassword`,
    /// `.oneTimeCode`, `.emailAddress`) and the QuickType bar's contextual
    /// suggestions. Applies to every variant — `LemonadeUi.TextField`,
    /// `TextFieldWithSelector`, and their `LemonadeTextFieldValue`-based forms —
    /// including the plain `String`-binding overloads.
    ///
    /// Backed by the platform: on iOS it maps to `UITextField.textContentType`.
    /// Has no effect on views other than Lemonade text fields.
    ///
    /// Availability: this modifier (and `UITextContentType`) only exists where
    /// UIKit does — iOS, iPadOS, and Mac Catalyst. It is not compiled for native
    /// macOS (AppKit), where the Lemonade text fields fall back to a plain field
    /// with no content-type concept, so guard cross-platform call sites with
    /// `#if canImport(UIKit)`.
    ///
    /// - Note: This is deliberately *not* named `textContentType(_:)`. SwiftUI
    ///   already ships a `View.textContentType(_:)`, but it configures only
    ///   SwiftUI's own native fields and has no effect on the UIKit-backed
    ///   Lemonade field. Use this Lemonade-namespaced modifier to drive AutoFill
    ///   on the Lemonade fields.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.TextField(
    ///     input: $email,
    ///     label: "Email",
    ///     placeholderText: "you@example.com"
    /// )
    /// .lemonadeTextContentType(.username)
    /// ```
    ///
    /// - Parameter type: The `UITextContentType` the field should advertise, or
    ///   `nil` to clear it.
    /// - Returns: A view whose Lemonade text fields use the given content type.
    func lemonadeTextContentType(_ type: UITextContentType?) -> some View {
        environment(\.lemonadeTextContentType, type)
    }
}
#endif
