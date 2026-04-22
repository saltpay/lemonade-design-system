import SwiftUI

// MARK: - ActionListItem Component

public extension LemonadeUi {
    /// Basic building block for list items.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.ActionListItem(
    ///     label: "Label",
    ///     supportText: "Description",
    ///     showDivider: true,
    ///     onItemClicked: { /* action */ },
    ///     leadingSlot: { LemonadeUi.Icon(icon: .heart, contentDescription: nil) },
    ///     trailingSlot: { LemonadeUi.Tag(label: "New", voice: .warning) }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: Label String to be displayed
    ///   - supportText: Text to be displayed as supportText below the label
    ///   - voice: LemonadeListItemVoice to define tone of voice. Defaults to .neutral
    ///   - showNavigationIndicator: Indicates navigation visually
    ///   - isLoading: Shows a skeleton loading placeholder instead of content
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - showDivider: Flag to show a divider below the list item. Defaults to false
    ///   - trailingAlignment: Vertical alignment of the trailing slot and navigation indicator. Defaults to `.center`.
    ///   - onItemClicked: Callback called when component is tapped
    ///   - leadingSlot: Slot content to be placed in leading position
    ///   - trailingSlot: Slot content to be placed in trailing position
    /// - Returns: A styled ActionListItem view
    @ViewBuilder
    static func ActionListItem<LeadingContent: View, TrailingContent: View>(
        label: String,
        supportText: String? = nil,
        voice: LemonadeListItemVoice = .neutral,
        showNavigationIndicator: Bool = false,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        trailingAlignment: VerticalAlignment = .center,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        ListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            navigationIndicator: showNavigationIndicator,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            trailingAlignment: trailingAlignment,
            onListItemClick: onItemClicked,
            leadingSlot: leadingSlot,
            trailingSlot: {
                trailingSlot()
                    .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            }
        )
    }

    /// ActionListItem without trailing slot.
    @ViewBuilder
    static func ActionListItem<LeadingContent: View>(
        label: String,
        supportText: String? = nil,
        voice: LemonadeListItemVoice = .neutral,
        showNavigationIndicator: Bool = false,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        trailingAlignment: VerticalAlignment = .center,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        ActionListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            showNavigationIndicator: showNavigationIndicator,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            trailingAlignment: trailingAlignment,
            onItemClicked: onItemClicked,
            leadingSlot: leadingSlot,
            trailingSlot: { EmptyView() }
        )
    }

    /// ActionListItem without leading slot.
    @ViewBuilder
    static func ActionListItem<TrailingContent: View>(
        label: String,
        supportText: String? = nil,
        voice: LemonadeListItemVoice = .neutral,
        showNavigationIndicator: Bool = false,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        trailingAlignment: VerticalAlignment = .center,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        ActionListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            showNavigationIndicator: showNavigationIndicator,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            trailingAlignment: trailingAlignment,
            onItemClicked: onItemClicked,
            leadingSlot: { EmptyView() },
            trailingSlot: trailingSlot
        )
    }

    /// ActionListItem without slots.
    @ViewBuilder
    static func ActionListItem(
        label: String,
        supportText: String? = nil,
        voice: LemonadeListItemVoice = .neutral,
        showNavigationIndicator: Bool = false,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        trailingAlignment: VerticalAlignment = .center,
        onItemClicked: (() -> Void)? = nil
    ) -> some View {
        ActionListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            showNavigationIndicator: showNavigationIndicator,
            isLoading: isLoading,
            enabled: enabled,
            showDivider: showDivider,
            trailingAlignment: trailingAlignment,
            onItemClicked: onItemClicked,
            leadingSlot: { EmptyView() },
            trailingSlot: { EmptyView() }
        )
    }
}

#if DEBUG
struct LemonadeActionListItem_Previews: PreviewProvider {
    static var previews: some View {
        VStack(alignment: .leading, spacing: 0) {
            // SelectListItem - Single with divider
            LemonadeUi.ActionListItem(
                label: "Action",
                supportText: "Support text",
                showNavigationIndicator: true,
                showDivider: true,
                onItemClicked: {},
                leadingSlot: {
                    LemonadeUi.SymbolContainer(
                        icon: .star,
                        contentDescription: nil,
                        size: .medium,
                        shape: .rounded
                    )
                }
            )
            LemonadeUi.ActionListItem(
                label: "Action",
                supportText: "Support text",
                showNavigationIndicator: true,
                onItemClicked: {},
                leadingSlot: {
                    LemonadeUi.SymbolContainer(
                        icon: .star,
                        contentDescription: nil,
                        size: .medium,
                        shape: .rounded
                    )
                }
            )
        }
        .frame(maxHeight: .infinity, alignment: .top)
    }
}
#endif
