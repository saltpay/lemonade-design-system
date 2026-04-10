import SwiftUI

// MARK: - ResourceListItem Component

public extension LemonadeUi {
    /// A list item for resource info display.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.ResourceListItem(
    ///     label: "Label",
    ///     value: "Value",
    ///     description: "Description",
    ///     showDivider: true
    /// ) {
    ///     LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil)
    /// }
    /// ```
    ///
    /// - Parameters:
    ///   - label: Main String to be displayed
    ///   - value: Value String to be displayed in trailing position
    ///   - description: String to be displayed as description
    ///   - isLoading: Shows a skeleton loading placeholder instead of content
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - showDivider: Flag to show a divider below the list item. Defaults to false
    ///   - onItemClicked: Callback called when component is tapped
    ///   - addonSlot: Slot to be displayed below the value
    ///   - leadingSlot: Slot component to be placed in leading position
    /// - Returns: A styled ResourceListItem view
    @ViewBuilder
    static func ResourceListItem<LeadingContent: View, AddonContent: View>(
        label: String,
        value: String,
        description: String? = nil,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder addonSlot: @escaping () -> AddonContent,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        ListItem(
            label: label,
            description: description,
            voice: .neutral,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            onListItemClick: onItemClicked,
            leadingSlot: {
                leadingSlot()
                    .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            },
            trailingSlot: {
                VStack(alignment: .trailing, spacing: LemonadeTheme.spaces.spacing50) {
                    LemonadeUi.Text(
                        value,
                        textStyle: LemonadeTypography.shared.bodyMediumMedium
                    )

                    addonSlot()
                }
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            }
        )
    }

    /// A list item for resource info display without addon slot.
    @ViewBuilder
    static func ResourceListItem<LeadingContent: View>(
        label: String,
        value: String,
        description: String? = nil,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        ResourceListItem(
            label: label,
            value: value,
            description: description,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            onItemClicked: onItemClicked,
            addonSlot: { EmptyView() },
            leadingSlot: leadingSlot
        )
    }
}
