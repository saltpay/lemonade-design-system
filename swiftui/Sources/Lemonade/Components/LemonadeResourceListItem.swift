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
    ///     supportText: "Support Text",
    ///     showDivider: true
    /// ) {
    ///     LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil)
    /// }
    /// ```
    ///
    /// - Parameters:
    ///   - label: Main String to be displayed
    ///   - value: Value String to be displayed in trailing position
    ///   - supportText: String to be displayed as support text
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
        supportText: String? = nil,
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder addonSlot: @escaping () -> AddonContent,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        ListItem(
            label: label,
            supportText: supportText,
            voice: .neutral,
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
        supportText: String? = nil,
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        ResourceListItem(
            label: label,
            value: value,
            supportText: supportText,
            enabled: enabled,
            showDivider: showDivider,
            onItemClicked: onItemClicked,
            addonSlot: { EmptyView() },
            leadingSlot: leadingSlot
        )
    }
}
