import SwiftUI

// MARK: - Environment

private struct LemonadeSecureTextEntryKey: EnvironmentKey {
    static let defaultValue: Bool = false
}

extension EnvironmentValues {
    var lemonadeSecureTextEntry: Bool {
        get { self[LemonadeSecureTextEntryKey.self] }
        set { self[LemonadeSecureTextEntryKey.self] = newValue }
    }
}

// MARK: - View Modifier

public extension View {
    /// Marks `LemonadeUi.TextField` views in this hierarchy as secure entry
    /// fields, suitable for passwords and other sensitive input.
    ///
    /// Secure entry is backed by the platform — on iOS it uses native secure
    /// text entry (`SecureField` / `UITextField.isSecureTextEntry`), so it
    /// provides character masking, exclusion from screenshots and screen
    /// recordings, and disables keyboard caching/autofill leakage. Has no
    /// effect on views other than Lemonade text fields.
    ///
    /// Because the parameter is dynamic, a show/hide toggle is a one-liner —
    /// flip it from your own state.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.TextField(
    ///     input: $password,
    ///     label: "Password",
    ///     placeholderText: "Enter password"
    /// )
    /// .secureTextEntry()
    ///
    /// // With a show/hide toggle:
    /// LemonadeUi.TextField(input: $password) {
    ///     LemonadeUi.Icon(icon: .padlock, contentDescription: nil)
    /// } trailingContent: {
    ///     LemonadeUi.Icon(
    ///         icon: isVisible ? .eyeOpen : .eyeClosed,
    ///         contentDescription: isVisible ? "Hide password" : "Show password"
    ///     )
    ///     .onTapGesture { isVisible.toggle() }
    /// }
    /// .secureTextEntry(!isVisible)
    /// ```
    ///
    /// - Parameter isSecure: Whether the field should mask its contents. Pass
    ///   `false` to opt back into plain text after the modifier has been
    ///   applied higher up the hierarchy.
    /// - Returns: A view whose Lemonade text fields use secure entry.
    func secureTextEntry(_ isSecure: Bool = true) -> some View {
        environment(\.lemonadeSecureTextEntry, isSecure)
    }
}
