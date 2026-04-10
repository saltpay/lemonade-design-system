import SwiftUI

// MARK: - SelectListItem Component

public extension LemonadeUi {
    /// A list item with the sole purpose of selection of a single or multiple items.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SelectListItem(
    ///     label: "Label",
    ///     description: "Description",
    ///     type: .single,
    ///     checked: true,
    ///     onItemClicked: { /* action */ },
    ///     showDivider: true
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: Label to be displayed in the selection item
    ///   - type: SelectListItemType, defines selection behavior and component
    ///   - checked: Flag defining if item is selected or not
    ///   - onItemClicked: Callback triggered on click interaction
    ///   - isLoading: Shows a skeleton loading placeholder instead of content
    ///   - enabled: Flag that defines if component is enabled. Defaults to true
    ///   - showDivider: Flag to show a divider below the list item. Defaults to false
    ///   - description: Text to be displayed below the label
    ///   - leadingSlot: Content to be placed in leading position
    ///   - trailingSlot: Content to be placed before the selection control
    /// - Returns: A styled SelectListItem view
    @ViewBuilder
    static func SelectListItem<LeadingContent: View, TrailingContent: View>(
        label: String,
        type: SelectListItemType,
        checked: Bool,
        onItemClicked: @escaping () -> Void,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        description: String? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        ListItem(
            label: label,
            description: description,
            voice: .neutral,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            onListItemClick: {
                switch type {
                case .single:
                    if !checked {
                        onItemClicked()
                    }
                case .multiple, .toggle:
                    onItemClicked()
                }
            },
            leadingSlot: { leadingSlot() },
            trailingSlot: {
                HStack(spacing: LemonadeTheme.spaces.spacing200) {
                    trailingSlot()

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
        )
    }

    /// A list item with the sole purpose of selection without leading slot.
    @ViewBuilder
    static func SelectListItem<TrailingContent: View>(
        label: String,
        type: SelectListItemType,
        checked: Bool,
        onItemClicked: @escaping () -> Void,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        description: String? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            description: description,
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
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        description: String? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            description: description,
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
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        description: String? = nil
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            description: description,
            leadingSlot: { EmptyView() },
            trailingSlot: { EmptyView() }
        )
    }
}
