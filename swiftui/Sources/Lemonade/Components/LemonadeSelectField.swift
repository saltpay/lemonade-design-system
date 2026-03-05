import SwiftUI

// MARK: - SelectField Component

public extension LemonadeUi {
    /// Select Field allows users to trigger an options list or picker without typing.
    /// Visually identical to TextField but the entire field is clickable (no text input).
    /// Ideal for country selection, category pickers, or any dropdown trigger.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SelectField(
    ///     onClick: { showPicker = true },
    ///     selectedValue: selectedOption,
    ///     label: "Category",
    ///     placeholderText: "Select a category"
    /// )
    /// ```
    @ViewBuilder
    static func SelectField(
        onClick: @escaping () -> Void,
        selectedValue: String?,
        placeholderText: String? = nil,
        label: String? = nil,
        optionalIndicator: String? = nil,
        supportText: String? = nil,
        errorMessage: String? = nil,
        error: Bool = false,
        enabled: Bool = true
    ) -> some View {
        LemonadeSelectFieldView<EmptyView>(
            onClick: onClick,
            selectedValue: selectedValue,
            placeholderText: placeholderText,
            label: label,
            optionalIndicator: optionalIndicator,
            supportText: supportText,
            errorMessage: errorMessage,
            error: error,
            enabled: enabled,
            leadingContent: nil
        )
    }

    /// Select Field with optional leading content (e.g., an icon).
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SelectField(
    ///     onClick: { showPicker = true },
    ///     selectedValue: "Favourites",
    ///     label: "Collection"
    /// ) {
    ///     LemonadeUi.Icon(icon: .heart, contentDescription: nil)
    /// }
    /// ```
    @ViewBuilder
    static func SelectField<LeadingContent: View>(
        onClick: @escaping () -> Void,
        selectedValue: String?,
        placeholderText: String? = nil,
        label: String? = nil,
        optionalIndicator: String? = nil,
        supportText: String? = nil,
        errorMessage: String? = nil,
        error: Bool = false,
        enabled: Bool = true,
        @ViewBuilder leadingContent: @escaping () -> LeadingContent
    ) -> some View {
        LemonadeSelectFieldView(
            onClick: onClick,
            selectedValue: selectedValue,
            placeholderText: placeholderText,
            label: label,
            optionalIndicator: optionalIndicator,
            supportText: supportText,
            errorMessage: errorMessage,
            error: error,
            enabled: enabled,
            leadingContent: leadingContent
        )
    }
}

// MARK: - Internal SelectField View

private struct LemonadeSelectFieldView<LeadingContent: View>: View {
    let onClick: () -> Void
    let selectedValue: String?
    let placeholderText: String?
    let label: String?
    let optionalIndicator: String?
    let supportText: String?
    let errorMessage: String?
    let error: Bool
    let enabled: Bool
    let leadingContent: (() -> LeadingContent)?

    @State private var isHovered = false

    private var backgroundColor: Color {
        switch (enabled, error, isHovered) {
        case (false, _, _):
            return LemonadeTheme.colors.background.bgElevated
        case (true, true, _):
            return LemonadeTheme.colors.background.bgCriticalSubtle
        case (true, _, true):
            return LemonadeTheme.colors.interaction.bgSubtleInteractive
        default:
            return .clear
        }
    }

    private var borderColor: Color {
        switch (enabled, error) {
        case (false, _):
            return .clear
        case (true, true):
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

            // Select field container
            SwiftUI.Button(action: onClick) {
                HStack(spacing: LemonadeTheme.spaces.spacing300) {
                    if let leadingContent = leadingContent {
                        leadingContent()
                    }

                    if let text = selectedValue ?? placeholderText {
                        LemonadeUi.Text(
                            text,
                            textStyle: LemonadeTypography.shared.bodyMediumRegular,
                            color: selectedValue != nil
                                ? LemonadeTheme.colors.content.contentPrimary
                                : LemonadeTheme.colors.content.contentSecondary
                        )
                        .lineLimit(1)
                    }

                    Spacer(minLength: 0)

                    LemonadeUi.Icon(
                        icon: .chevronDown,
                        contentDescription: nil,
                        tint: LemonadeTheme.colors.content.contentSecondary
                    )
                }
                .padding(.horizontal, LemonadeTheme.spaces.spacing400)
                .padding(.vertical, LemonadeTheme.spaces.spacing400)
            }
            .buttonStyle(PlainButtonStyle())
            .disabled(!enabled)
            .frame(minHeight: LemonadeTheme.sizes.size1400)
            .background(LemonadeTheme.colors.background.bgDefault)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))
            .overlay(
                RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300)
                    .stroke(borderColor, lineWidth: LemonadeTheme.borderWidth.base.border25)
            )
            .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            .onHover { hovering in
                isHovered = hovering
            }
            .accessibilityAddTraits(.isButton)
            .accessibilityRemoveTraits(.isStaticText)

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
        .animation(.easeInOut(duration: 0.15), value: error)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeSelectField_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LemonadeTheme.spaces.spacing600) {
            LemonadeUi.SelectField(
                onClick: {},
                selectedValue: nil,
                placeholderText: "Select an option",
                label: "Category"
            )

            LemonadeUi.SelectField(
                onClick: {},
                selectedValue: "English",
                label: "Language"
            )

            LemonadeUi.SelectField(
                onClick: {},
                selectedValue: nil,
                placeholderText: "Select an option",
                label: "Required",
                errorMessage: "Please select an option",
                error: true
            )

            LemonadeUi.SelectField(
                onClick: {},
                selectedValue: "Locked value",
                label: "Disabled",
                enabled: false
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
