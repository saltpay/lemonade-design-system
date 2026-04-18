import SwiftUI

// MARK: - SelectListItem Component

public extension LemonadeUi {
    /// A list item with the sole purpose of selection of a single or multiple items.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SelectListItem(
    ///     label: "Label",
    ///     supportText: "Description",
    ///     type: .single,
    ///     checked: true,
    ///     onItemClicked: { /* action */ },
    ///     showDivider: true
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: Label to be displayed in the selection item
    ///   - type: SelectListItemType, defines selection behavior and control
    ///   - checked: Flag defining if item is selected or not
    ///   - onItemClicked: Callback triggered on click interaction
    ///   - variant: Visual variant. `.plain` (default) is a bare row meant to sit inside a
    ///     surrounding surface. `.outlined` presents its own rounded container with a border
    ///     and a brand-tinted background when selected.
    ///   - isLoading: Shows a skeleton loading placeholder instead of content. Only honored
    ///     by `.plain`.
    ///   - enabled: Flag that defines if component is enabled. Defaults to true
    ///   - showDivider: Flag to show a divider below the list item. Only honored by `.plain`.
    ///   - supportText: Text to be displayed below the label
    ///   - leadingSlot: Content to be placed in leading position
    ///   - trailingSlot: Content to be placed before the selection control
    /// - Returns: A styled SelectListItem view
    @ViewBuilder
    static func SelectListItem<LeadingContent: View, TrailingContent: View>(
        label: String,
        type: SelectListItemType,
        checked: Bool,
        onItemClicked: @escaping () -> Void,
        variant: SelectListItemVariant = .plain,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        supportText: String? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        switch variant {
        case .plain:
            PlainSelectListItem(
                label: label,
                type: type,
                checked: checked,
                onItemClicked: onItemClicked,
                isLoading: isLoading,
                enabled: enabled,
                showDivider: showDivider,
                supportText: supportText,
                leadingSlot: leadingSlot,
                trailingSlot: trailingSlot
            )
        case .outlined:
            OutlinedSelectListItem(
                label: label,
                type: type,
                checked: checked,
                onItemClicked: onItemClicked,
                enabled: enabled,
                supportText: supportText,
                leadingSlot: leadingSlot,
                trailingSlot: trailingSlot
            )
        }
    }

    /// A list item with the sole purpose of selection without leading slot.
    @ViewBuilder
    static func SelectListItem<TrailingContent: View>(
        label: String,
        type: SelectListItemType,
        checked: Bool,
        onItemClicked: @escaping () -> Void,
        variant: SelectListItemVariant = .plain,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        supportText: String? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            variant: variant,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            supportText: supportText,
            leadingSlot: { EmptyView() },
            trailingSlot: trailingSlot
        )
    }

    /// A list item with the sole purpose of selection without trailing slot.
    @ViewBuilder
    static func SelectListItem<LeadingContent: View>(
        label: String,
        type: SelectListItemType,
        checked: Bool,
        onItemClicked: @escaping () -> Void,
        variant: SelectListItemVariant = .plain,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        supportText: String? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            variant: variant,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            supportText: supportText,
            leadingSlot: leadingSlot,
            trailingSlot: { EmptyView() }
        )
    }

    /// A list item with the sole purpose of selection without slots.
    @ViewBuilder
    static func SelectListItem(
        label: String,
        type: SelectListItemType,
        checked: Bool,
        onItemClicked: @escaping () -> Void,
        variant: SelectListItemVariant = .plain,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        supportText: String? = nil
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            variant: variant,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            supportText: supportText,
            leadingSlot: { EmptyView() },
            trailingSlot: { EmptyView() }
        )
    }
}

// MARK: - Shared helpers

private func handleSelectTap(
    type: SelectListItemType,
    checked: Bool,
    onItemClicked: () -> Void
) {
    switch type {
    case .single:
        if !checked {
            onItemClicked()
        }
    case .multiple, .toggle:
        onItemClicked()
    }
}

private struct SelectionControlView: View {
    let type: SelectListItemType
    let checked: Bool
    let onItemClicked: () -> Void
    let enabled: Bool

    var body: some View {
        switch type {
        case .single:
            LemonadeUi.RadioButton(
                checked: checked,
                onRadioButtonClicked: onItemClicked,
                enabled: enabled
            )
        case .multiple:
            LemonadeUi.Checkbox(
                status: checked ? .checked : .unchecked,
                onCheckboxClicked: onItemClicked,
                enabled: enabled
            )
        case .toggle:
            LemonadeUi.Switch(
                checked: checked,
                onCheckedChange: { _ in onItemClicked() },
                enabled: enabled
            )
        }
    }
}

// MARK: - Plain variant (delegates to ListItem)

private struct PlainSelectListItem<LeadingContent: View, TrailingContent: View>: View {
    let label: String
    let type: SelectListItemType
    let checked: Bool
    let onItemClicked: () -> Void
    let isLoading: Bool
    let enabled: Bool
    let showDivider: Bool
    let supportText: String?
    let leadingSlot: () -> LeadingContent
    let trailingSlot: () -> TrailingContent

    var body: some View {
        LemonadeUi.ListItem(
            label: label,
            supportText: supportText,
            voice: .neutral,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            onListItemClick: {
                handleSelectTap(type: type, checked: checked, onItemClicked: onItemClicked)
            },
            leadingSlot: { leadingSlot() },
            trailingSlot: {
                HStack(spacing: LemonadeTheme.spaces.spacing200) {
                    trailingSlot()

                    SelectionControlView(
                        type: type,
                        checked: checked,
                        onItemClicked: onItemClicked,
                        enabled: enabled
                    )
                }
            }
        )
    }
}

// MARK: - Outlined variant

private struct OutlinedSelectListItem<LeadingContent: View, TrailingContent: View>: View {
    let label: String
    let type: SelectListItemType
    let checked: Bool
    let onItemClicked: () -> Void
    let enabled: Bool
    let supportText: String?
    let leadingSlot: () -> LeadingContent
    let trailingSlot: () -> TrailingContent

    private var backgroundColor: Color {
        checked
            ? LemonadeTheme.colors.background.bgBrandSubtle
            : LemonadeTheme.colors.background.bgDefault
    }

    private var borderColor: Color {
        checked
            ? LemonadeTheme.colors.border.borderSelected
            : LemonadeTheme.colors.border.borderNeutralLow
    }

    private var borderWidth: CGFloat {
        checked
            ? LemonadeTheme.borderWidth.base.border50
            : LemonadeTheme.borderWidth.base.border40
    }

    private var contentOpacity: Double {
        enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled
    }

    var body: some View {
        HStack(spacing: 0) {
            if LeadingContent.self != EmptyView.self {
                leadingSlot()
                    .opacity(contentOpacity)
                    .padding(.trailing, LemonadeTheme.spaces.spacing300)
            }

            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing50) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodyMediumMedium,
                    color: LemonadeTheme.colors.content.contentPrimary
                )

                if let supportText = supportText {
                    LemonadeUi.Text(
                        supportText,
                        textStyle: LemonadeTypography.shared.bodySmallRegular,
                        color: LemonadeTheme.colors.content.contentSecondary
                    )
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.trailing, LemonadeTheme.spaces.spacing300)
            .opacity(contentOpacity)

            if TrailingContent.self != EmptyView.self {
                trailingSlot()
                    .opacity(contentOpacity)
            }

            SelectionControlView(
                type: type,
                checked: checked,
                onItemClicked: onItemClicked,
                enabled: enabled
            )
            .allowsHitTesting(false)
            .padding(.leading, LemonadeTheme.spaces.spacing200)
        }
        .padding(
            EdgeInsets(
                top: LemonadeTheme.spaces.spacing300,
                leading: LemonadeTheme.spaces.spacing300,
                bottom: LemonadeTheme.spaces.spacing300,
                trailing: LemonadeTheme.spaces.spacing400
            )
        )
        .background(
            RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                .fill(backgroundColor)
        )
        .overlay(
            RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500)
                .strokeBorder(borderColor, lineWidth: borderWidth)
        )
        .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
        .onTapGesture {
            guard enabled else { return }
            handleSelectTap(type: type, checked: checked, onItemClicked: onItemClicked)
        }
        .accessibilityElement(children: .combine)
        .accessibilityLabel(label)
        .accessibilityValue(supportText ?? "")
        .accessibilityAddTraits(checked ? .isSelected : [])
        .animation(.easeInOut(duration: 0.15), value: checked)
    }
}
