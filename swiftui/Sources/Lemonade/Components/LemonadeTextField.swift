import SwiftUI

// MARK: - TextField Component

public extension LemonadeUi {
    /// Text Field component allows users to enter or edit text.
    /// It supports multiple interaction states, label, support text, error states,
    /// and optional leading/trailing content.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.TextField(
    ///     input: $text,
    ///     onInputChanged: { newValue in text = newValue },
    ///     label: "Label",
    ///     supportText: "Support text",
    ///     placeholderText: "Enter text..."
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - input: The inputted text (Binding)
    ///   - onInputChanged: Callback when the user inputs content
    ///   - label: Label displayed above the text field
    ///   - optionalIndicator: Optional text displayed on the right of the label
    ///   - supportText: Support text displayed below the text field
    ///   - placeholderText: Placeholder text when the field is empty
    ///   - errorMessage: Error message displayed when error is true
    ///   - error: Whether the text field has an error
    ///   - enabled: Whether the text field is enabled
    ///   - leadingContent: Content displayed on the left inside the text field
    ///   - trailingContent: Content displayed on the right inside the text field
    /// - Returns: A styled TextField view
    @ViewBuilder
    static func TextField(
        input: Binding<String>,
        onInputChanged: ((String) -> Void)? = nil,
        label: String? = nil,
        optionalIndicator: String? = nil,
        supportText: String? = nil,
        placeholderText: String? = nil,
        errorMessage: String? = nil,
        error: Bool = false,
        enabled: Bool = true,
        leadingContent: (() -> AnyView)? = nil,
        trailingContent: (() -> AnyView)? = nil
    ) -> some View {
        LemonadeTextFieldView(
            input: input,
            onInputChanged: onInputChanged,
            label: label,
            optionalIndicator: optionalIndicator,
            supportText: supportText,
            placeholderText: placeholderText,
            errorMessage: errorMessage,
            error: error,
            enabled: enabled,
            leadingContent: leadingContent,
            trailingContent: trailingContent
        )
    }
}

// MARK: - TextFieldWithSelector Component

public extension LemonadeUi {
    /// A text input combined with a selectable element, allowing users to choose a prefix
    /// before entering text. Ideal for structured inputs like phone numbers.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.TextFieldWithSelector(
    ///     input: $text,
    ///     onInputChanged: { newValue in text = newValue },
    ///     leadingAction: { /* show selector */ },
    ///     leadingContent: {
    ///         HStack {
    ///             Text("+1")
    ///             Image(systemName: "chevron.down")
    ///         }
    ///     }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - input: The inputted text (Binding)
    ///   - onInputChanged: Callback when the user inputs content
    ///   - leadingAction: Action triggered when the leading content is clicked
    ///   - leadingContent: Content displayed on the left as selector
    ///   - label: Label displayed above the text field
    ///   - optionalIndicator: Optional text displayed on the right of the label
    ///   - supportText: Support text displayed below the text field
    ///   - placeholderText: Placeholder text when the field is empty
    ///   - errorMessage: Error message displayed when error is true
    ///   - error: Whether the text field has an error
    ///   - enabled: Whether the text field is enabled
    ///   - trailingContent: Content displayed on the right inside the text field
    /// - Returns: A styled TextFieldWithSelector view
    @ViewBuilder
    static func TextFieldWithSelector<LeadingContent: View>(
        input: Binding<String>,
        onInputChanged: ((String) -> Void)? = nil,
        leadingAction: @escaping () -> Void,
        leadingContent: @escaping () -> LeadingContent,
        label: String? = nil,
        optionalIndicator: String? = nil,
        supportText: String? = nil,
        placeholderText: String? = nil,
        errorMessage: String? = nil,
        error: Bool = false,
        enabled: Bool = true,
        trailingContent: (() -> AnyView)? = nil
    ) -> some View {
        LemonadeTextFieldWithSelectorView(
            input: input,
            onInputChanged: onInputChanged,
            leadingAction: leadingAction,
            leadingContent: leadingContent,
            label: label,
            optionalIndicator: optionalIndicator,
            supportText: supportText,
            placeholderText: placeholderText,
            errorMessage: errorMessage,
            error: error,
            enabled: enabled,
            trailingContent: trailingContent
        )
    }
}

// MARK: - Internal TextField View

private struct LemonadeTextFieldView: View {
    @Binding var input: String
    let onInputChanged: ((String) -> Void)?
    let label: String?
    let optionalIndicator: String?
    let supportText: String?
    let placeholderText: String?
    let errorMessage: String?
    let error: Bool
    let enabled: Bool
    let leadingContent: (() -> AnyView)?
    let trailingContent: (() -> AnyView)?

    @FocusState private var isFocused: Bool
    @State private var isHovered = false

    private let minHeight: CGFloat = 56 // size1400
    private let cornerRadius: CGFloat = 12 // radius300
    private let horizontalPadding: CGFloat = 12 // spacing300
    private let verticalPadding: CGFloat = 12 // spacing300

    private var backgroundColor: Color {
        switch (enabled, error, isFocused, isHovered) {
        case (false, _, _, _):
            return LemonadeTheme.colors.background.bgElevated
        case (true, true, false, _):
            return LemonadeTheme.colors.background.bgCriticalSubtle
        case (true, _, _, true):
            return LemonadeTheme.colors.interaction.bgSubtleInteractive
        default:
            return .clear
        }
    }

    private var borderColor: Color {
        switch (enabled, isFocused, error) {
        case (false, _, _):
            return .clear
        case (true, true, _):
            return LemonadeTheme.colors.border.borderSelected
        case (true, false, true):
            return LemonadeTheme.colors.border.borderCritical
        default:
            return LemonadeTheme.colors.border.borderNeutralMedium
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing50) {
            // Label row
            if label != nil || optionalIndicator != nil {
                HStack {
                    if let label = label {
                        LemonadeUi.Text(
                            label,
                            textStyle: LemonadeTypography.shared.bodySmallMedium,
                            color: enabled
                                ? LemonadeTheme.colors.content.contentPrimary
                                : LemonadeTheme.colors.content.contentSecondary
                        )
                    }

                    Spacer()

                    if let optionalIndicator = optionalIndicator {
                        LemonadeUi.Text(
                            optionalIndicator,
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }
                .padding(.horizontal, LemonadeTheme.spaces.spacing50)
            }

            // Text field container
            HStack(spacing: LemonadeTheme.spaces.spacing300) {
                if let leadingContent = leadingContent {
                    leadingContent()
                }

                ZStack(alignment: .leading) {
                    if input.isEmpty, let placeholderText = placeholderText {
                        LemonadeUi.Text(
                            placeholderText,
                            textStyle: LemonadeTypography.shared.bodyMediumRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }

                    SwiftUI.TextField("", text: $input)
                        .font(LemonadeTypography.shared.bodyMediumRegular.font)
                        .foregroundColor(LemonadeTheme.colors.content.contentPrimary)
                        .focused($isFocused)
                        .disabled(!enabled)
                        .onChange(of: input) { newValue in
                            onInputChanged?(newValue)
                        }
                }

                if let trailingContent = trailingContent {
                    trailingContent()
                }
            }
            .padding(.horizontal, horizontalPadding)
            .padding(.vertical, verticalPadding)
            .frame(minHeight: minHeight)
            .background(LemonadeTheme.colors.background.bgDefault)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius)
                    .stroke(borderColor, lineWidth: LemonadeTheme.borderWidth.base.border25)
            )
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius + 2)
                    .stroke(
                        isFocused ? LemonadeTheme.colors.background.bgElevated : .clear,
                        lineWidth: LemonadeTheme.borderWidth.base.border50
                    )
                    .padding(-2)
            )
            .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            .onHover { hovering in
                isHovered = hovering
            }

            // Support/Error text
            if enabled && error, let errorMessage = errorMessage {
                LemonadeUi.Text(
                    errorMessage,
                    textStyle: LemonadeTypography.shared.bodyXSmallRegular,
                    color: LemonadeTheme.colors.content.contentCritical
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing50)
            } else if let supportText = supportText {
                LemonadeUi.Text(
                    supportText,
                    textStyle: LemonadeTypography.shared.bodyXSmallRegular,
                    color: LemonadeTheme.colors.content.contentSecondary
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing50)
            }
        }
        .animation(.easeInOut(duration: 0.15), value: isFocused)
        .animation(.easeInOut(duration: 0.15), value: error)
    }
}

// MARK: - Internal TextField With Selector View

private struct LemonadeTextFieldWithSelectorView<LeadingContent: View>: View {
    @Binding var input: String
    let onInputChanged: ((String) -> Void)?
    let leadingAction: () -> Void
    let leadingContent: () -> LeadingContent
    let label: String?
    let optionalIndicator: String?
    let supportText: String?
    let placeholderText: String?
    let errorMessage: String?
    let error: Bool
    let enabled: Bool
    let trailingContent: (() -> AnyView)?

    @FocusState private var isFocused: Bool
    @State private var isHovered = false

    private let minHeight: CGFloat = 56 // size1400
    private let cornerRadius: CGFloat = 12 // radius300
    private let horizontalPadding: CGFloat = 12 // spacing300
    private let verticalPadding: CGFloat = 12 // spacing300

    private var backgroundColor: Color {
        switch (enabled, error, isFocused, isHovered) {
        case (false, _, _, _):
            return LemonadeTheme.colors.background.bgElevated
        case (true, true, false, _):
            return LemonadeTheme.colors.background.bgCriticalSubtle
        case (true, _, _, true):
            return LemonadeTheme.colors.interaction.bgSubtleInteractive
        default:
            return .clear
        }
    }

    private var borderColor: Color {
        switch (enabled, isFocused, error) {
        case (false, _, _):
            return .clear
        case (true, true, _):
            return LemonadeTheme.colors.border.borderSelected
        case (true, false, true):
            return LemonadeTheme.colors.border.borderCritical
        default:
            return LemonadeTheme.colors.border.borderNeutralMedium
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing50) {
            // Label row
            if label != nil || optionalIndicator != nil {
                HStack {
                    if let label = label {
                        LemonadeUi.Text(
                            label,
                            textStyle: LemonadeTypography.shared.bodySmallMedium,
                            color: enabled
                                ? LemonadeTheme.colors.content.contentPrimary
                                : LemonadeTheme.colors.content.contentSecondary
                        )
                    }

                    Spacer()

                    if let optionalIndicator = optionalIndicator {
                        LemonadeUi.Text(
                            optionalIndicator,
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }
                .padding(.horizontal, LemonadeTheme.spaces.spacing50)
            }

            // Text field container with selector
            HStack(spacing: 0) {
                // Selector button
                SwiftUI.Button(action: leadingAction) {
                    leadingContent()
                        .padding(LemonadeTheme.spaces.spacing400)
                }
                .buttonStyle(PlainButtonStyle())
                .disabled(!enabled)
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)

                // Divider
                Rectangle()
                    .fill(LemonadeTheme.colors.border.borderNeutralMedium)
                    .frame(width: LemonadeTheme.borderWidth.base.border25)
                    .frame(minHeight: minHeight)

                // Text input area
                HStack(spacing: LemonadeTheme.spaces.spacing300) {
                    ZStack(alignment: .leading) {
                        if input.isEmpty, let placeholderText = placeholderText {
                            LemonadeUi.Text(
                                placeholderText,
                                textStyle: LemonadeTypography.shared.bodyMediumRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                        }

                        SwiftUI.TextField("", text: $input)
                            .font(LemonadeTypography.shared.bodyMediumRegular.font)
                            .foregroundColor(LemonadeTheme.colors.content.contentPrimary)
                            .focused($isFocused)
                            .disabled(!enabled)
                            .onChange(of: input) { newValue in
                                onInputChanged?(newValue)
                            }
                    }

                    if let trailingContent = trailingContent {
                        trailingContent()
                    }
                }
                .padding(.horizontal, horizontalPadding)
                .padding(.vertical, verticalPadding)
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            }
            .frame(minHeight: minHeight)
            .background(LemonadeTheme.colors.background.bgDefault)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius)
                    .stroke(borderColor, lineWidth: LemonadeTheme.borderWidth.base.border25)
            )
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius + 2)
                    .stroke(
                        isFocused ? LemonadeTheme.colors.background.bgElevated : .clear,
                        lineWidth: LemonadeTheme.borderWidth.base.border50
                    )
                    .padding(-2)
            )
            .onHover { hovering in
                isHovered = hovering
            }

            // Support/Error text
            if enabled && error, let errorMessage = errorMessage {
                LemonadeUi.Text(
                    errorMessage,
                    textStyle: LemonadeTypography.shared.bodyXSmallRegular,
                    color: LemonadeTheme.colors.content.contentCritical
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing50)
            } else if let supportText = supportText {
                LemonadeUi.Text(
                    supportText,
                    textStyle: LemonadeTypography.shared.bodyXSmallRegular,
                    color: LemonadeTheme.colors.content.contentSecondary
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing50)
            }
        }
        .animation(.easeInOut(duration: 0.15), value: isFocused)
        .animation(.easeInOut(duration: 0.15), value: error)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeTextField_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Basic text field
            StatefulPreviewWrapper("") { input in
                LemonadeUi.TextField(
                    input: input,
                    label: "Label",
                    optionalIndicator: "Optional",
                    supportText: "Support text",
                    placeholderText: "Enter text..."
                )
            }

            // With error
            StatefulPreviewWrapper("Invalid input") { input in
                LemonadeUi.TextField(
                    input: input,
                    label: "Email",
                    placeholderText: "Enter email",
                    errorMessage: "Please enter a valid email",
                    error: true
                )
            }

            // Disabled
            StatefulPreviewWrapper("Disabled text") { input in
                LemonadeUi.TextField(
                    input: input,
                    label: "Disabled",
                    enabled: false
                )
            }

            // With leading/trailing content
            StatefulPreviewWrapper("") { input in
                LemonadeUi.TextField(
                    input: input,
                    label: "Password",
                    placeholderText: "Enter password",
                    leadingContent: {
                        AnyView(
                            LemonadeUi.Icon(
                                icon: .padlock,
                                contentDescription: nil,
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentSecondary
                            )
                        )
                    },
                    trailingContent: {
                        AnyView(
                            LemonadeUi.Icon(
                                icon: .eyeClosed,
                                contentDescription: nil,
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentSecondary
                            )
                        )
                    }
                )
            }

            // TextField with selector
            StatefulPreviewWrapper("") { input in
                LemonadeUi.TextFieldWithSelector(
                    input: input,
                    leadingAction: { print("Selector tapped") },
                    leadingContent: {
                        HStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "+1",
                                textStyle: LemonadeTypography().bodyMediumMedium
                            )
                            LemonadeUi.Icon(
                                icon: .chevronDown,
                                contentDescription: nil,
                                size: .small,
                                tint: LemonadeTheme.colors.content.contentPrimary
                            )
                        }
                    },
                    label: "Phone Number",
                    placeholderText: "Enter phone number"
                )
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}

// Helper for stateful previews
private struct StatefulPreviewWrapper<Value, Content: View>: View {
    @State private var value: Value
    let content: (Binding<Value>) -> Content

    init(_ value: Value, @ViewBuilder content: @escaping (Binding<Value>) -> Content) {
        _value = State(initialValue: value)
        self.content = content
    }

    var body: some View {
        content($value)
    }
}
#endif
