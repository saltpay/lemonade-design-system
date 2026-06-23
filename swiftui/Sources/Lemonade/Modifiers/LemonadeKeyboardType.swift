import SwiftUI

#if canImport(UIKit)
import UIKit

// MARK: - Environment

private struct LemonadeKeyboardTypeKey: EnvironmentKey {
    static let defaultValue: UIKeyboardType = .default
}

extension EnvironmentValues {
    var lemonadeKeyboardType: UIKeyboardType {
        get { self[LemonadeKeyboardTypeKey.self] }
        set { self[LemonadeKeyboardTypeKey.self] = newValue }
    }
}

// MARK: - View Modifier

public extension View {
    /// Sets the keyboard type for the Lemonade text fields in this hierarchy.
    /// Applies to every variant — `LemonadeUi.TextField`, `TextFieldWithSelector`,
    /// and their `LemonadeTextFieldValue`-based forms — including the plain
    /// `String`-binding overloads, which take no `keyboardType` parameter.
    ///
    /// Backed by the platform: on iOS it maps to `UITextField.keyboardType`. The
    /// field reloads its input view if the keyboard type changes while the field
    /// is focused, so a dynamic switch takes effect without dropping focus. Has no
    /// effect on views other than Lemonade text fields.
    ///
    /// Availability: this modifier (and `UIKeyboardType`) only exists where UIKit
    /// does — iOS, iPadOS, and Mac Catalyst. It is not compiled for native macOS
    /// (AppKit), where the Lemonade text fields fall back to a plain field with no
    /// keyboard-type concept, so guard cross-platform call sites with
    /// `#if canImport(UIKit)`.
    ///
    /// - Note: This is deliberately *not* named `keyboardType(_:)`. SwiftUI already
    ///   ships a `View.keyboardType(_:)` with the same signature; declaring another
    ///   would make every call site ambiguous. Use this Lemonade-namespaced
    ///   modifier to drive the Lemonade fields' UIKit-backed keyboard.
    ///
    /// Precedence: an explicit `keyboardType:` argument on a
    /// `LemonadeTextFieldValue`-based overload wins over this modifier; this
    /// modifier in turn wins over the `.default` fallback. For the `String`-binding
    /// overloads (no parameter), this modifier is the only way to set the keyboard.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.TextField(
    ///     input: $email,
    ///     label: "Email",
    ///     placeholderText: "you@example.com"
    /// )
    /// .lemonadeKeyboardType(.emailAddress)
    /// ```
    ///
    /// - Parameter type: The `UIKeyboardType` the field should present.
    /// - Returns: A view whose Lemonade text fields use the given keyboard type.
    func lemonadeKeyboardType(_ type: UIKeyboardType) -> some View {
        environment(\.lemonadeKeyboardType, type)
    }
}
#endif
