import SwiftUI

// MARK: - SelectListItemType

/// Defines the selection behavior type for SelectListItem.
public enum SelectListItemType {
    case single
    case multiple
}

// MARK: - LemonadeListItemVoice

/// Defines the tone of voice for ActionListItem.
public enum LemonadeListItemVoice {
    case neutral
    case critical

    var interactionBackground: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.interaction.bgSubtleInteractive
        case .critical: return LemonadeTheme.colors.interaction.bgCriticalSubtleInteractive
        }
    }

    var contentColor: Color {
        switch self {
        case .neutral: return LemonadeTheme.colors.content.contentPrimary
        case .critical: return LemonadeTheme.colors.content.contentCritical
        }
    }
}

// MARK: - SelectListItem Component

public extension LemonadeUi {
    /// A list item with the sole purpose of selection of a single or multiple items.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SelectListItem(
    ///     label: "Label",
    ///     supportText: "Support Text",
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
    ///   - enabled: Flag that defines if component is enabled. Defaults to true
    ///   - showDivider: Flag to show a divider below the list item. Defaults to false
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
        enabled: Bool = true,
        showDivider: Bool = false,
        supportText: String? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        LemonadeCoreListItemView(
            label: label,
            supportText: supportText,
            voice: .neutral,
            enabled: enabled,
            showDivider: showDivider,
            onListItemClick: {
                switch type {
                case .single:
                    if !checked {
                        onItemClicked()
                    }
                case .multiple:
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
        enabled: Bool = true,
        showDivider: Bool = false,
        supportText: String? = nil
    ) -> some View {
        SelectListItem(
            label: label,
            type: type,
            checked: checked,
            onItemClicked: onItemClicked,
            enabled: enabled,
            showDivider: showDivider,
            supportText: supportText,
            leadingSlot: { EmptyView() },
            trailingSlot: { EmptyView() }
        )
    }
}

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
        LemonadeCoreListItemView(
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

// MARK: - ActionListItem Component

public extension LemonadeUi {
    /// Basic building block for list items.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.ActionListItem(
    ///     label: "Label",
    ///     supportText: "Support Text",
    ///     showDivider: true,
    ///     onItemClicked: { /* action */ },
    ///     leadingSlot: { LemonadeUi.Icon(icon: .heart, contentDescription: nil) },
    ///     trailingSlot: { LemonadeUi.Tag(label: "New", voice: .warning) }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - label: Label String to be displayed
    ///   - supportText: Text to be displayed as Support Text
    ///   - voice: LemonadeListItemVoice to define tone of voice. Defaults to .neutral
    ///   - showNavigationIndicator: Indicates navigation visually
    ///   - enabled: Flag to define if component is enabled. Defaults to true
    ///   - showDivider: Flag to show a divider below the list item. Defaults to false
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
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        LemonadeCoreListItemView(
            label: label,
            supportText: supportText,
            voice: voice,
            enabled: enabled,
            showDivider: showDivider,
            onListItemClick: onItemClicked,
            leadingSlot: leadingSlot,
            trailingSlot: {
                HStack {
                    trailingSlot()

                    if showNavigationIndicator {
                        LemonadeUi.Icon(
                            icon: .chevronRight,
                            contentDescription: "Navigation indicator",
                            size: .medium,
                            tint: LemonadeTheme.colors.content.contentTertiary
                        )
                    }
                }
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
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent
    ) -> some View {
        ActionListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            showNavigationIndicator: showNavigationIndicator,
            enabled: enabled,
            showDivider: showDivider,
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
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        ActionListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            showNavigationIndicator: showNavigationIndicator,
            enabled: enabled,
            showDivider: showDivider,
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
        enabled: Bool = true,
        showDivider: Bool = false,
        onItemClicked: (() -> Void)? = nil
    ) -> some View {
        ActionListItem(
            label: label,
            supportText: supportText,
            voice: voice,
            showNavigationIndicator: showNavigationIndicator,
            enabled: enabled,
            showDivider: showDivider,
            onItemClicked: onItemClicked,
            leadingSlot: { EmptyView() },
            trailingSlot: { EmptyView() }
        )
    }
}

// MARK: - Core ListItem View

private struct LemonadeCoreListItemView<LeadingContent: View, TrailingContent: View>: View {
    let label: String
    let supportText: String?
    let voice: LemonadeListItemVoice
    let enabled: Bool
    let showDivider: Bool
    let onListItemClick: (() -> Void)?
    let leadingSlot: () -> LeadingContent
    let trailingSlot: () -> TrailingContent

    var body: some View {
        ListItemSafeArea(showDivider: showDivider) {
            if let onClick = onListItemClick, enabled {
                Button(action: onClick) {
                    listItemContent
                }
                .buttonStyle(ListItemButtonStyle(voice: voice))
                .disabled(!enabled)
            } else {
                listItemContent
            }
        }
    }

    private var listItemContent: some View {
        HStack(spacing: LemonadeTheme.spaces.spacing300) {
            // Leading slot
            leadingSlot()
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)

            // Content column
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing50) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodyMediumMedium,
                    color: voice.contentColor
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
            .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)

            // Trailing slot
            trailingSlot()
        }
        .padding(.horizontal, LemonadeTheme.spaces.spacing300)
        .padding(.vertical, LemonadeTheme.spaces.spacing200)
        .frame(minHeight: LemonadeTheme.sizes.size1200)
    }
}

// MARK: - SafeArea Wrapper

private struct ListItemSafeArea<Content: View>: View {
    let showDivider: Bool
    @ViewBuilder let content: () -> Content

    var body: some View {
        VStack(spacing: 0) {
            content()
                .padding(LemonadeTheme.spaces.spacing100)

            if showDivider {
                LemonadeUi.HorizontalDivider()
                .padding(.horizontal, LemonadeTheme.spaces.spacing400)
            }
        }
        .background(Color.clear)
    }
}

// MARK: - ListItem Button Style

private struct ListItemButtonStyle: ButtonStyle {
    let voice: LemonadeListItemVoice

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .background(
                configuration.isPressed
                    ? voice.interactionBackground
                    : Color.clear
            )
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))
            .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))
            .animation(.easeInOut(duration: 0.15), value: configuration.isPressed)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeListItem_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 0) {
            // SelectListItem - Single with divider
            LemonadeUi.SelectListItem(
                label: "Single Selection",
                type: .single,
                checked: true,
                onItemClicked: {},
                showDivider: true,
                supportText: "Support text"
            )

            // SelectListItem - Multiple with divider
            LemonadeUi.SelectListItem(
                label: "Multiple Selection",
                type: .multiple,
                checked: false,
                onItemClicked: {},
                showDivider: true,
                supportText: "Support text"
            )

            LemonadeUi.HorizontalDivider()
                .padding(.vertical, .space.spacing200)

            // ResourceListItem with divider
            LemonadeUi.ResourceListItem(
                label: "Resource Label",
                value: "$100.00",
                supportText: "Metadata",
                showDivider: true
            ) {
                LemonadeUi.SymbolContainer(
                    icon: .heart,
                    contentDescription: nil,
                    size: .large
                )
            }

            // ResourceListItem with addon and divider
            LemonadeUi.ResourceListItem(
                label: "With Addon",
                value: "$50.00",
                showDivider: true,
                addonSlot: {
                    LemonadeUi.Tag(label: "Approved", voice: .positive)
                },
                leadingSlot: {
                    LemonadeUi.SymbolContainer(
                        icon: .star,
                        contentDescription: nil,
                        size: .large
                    )
                }
            )

            LemonadeUi.HorizontalDivider()
                .padding(.vertical, .space.spacing200)

            // ActionListItem with divider
            LemonadeUi.ActionListItem(
                label: "Action Item",
                supportText: "Support text",
                showNavigationIndicator: true,
                showDivider: true,
                onItemClicked: {},
                leadingSlot: {
                    LemonadeUi.Icon(
                        icon: .heart,
                        contentDescription: nil,
                        size: .medium
                    )
                }
            )

            // ActionListItem - Critical with divider
            LemonadeUi.ActionListItem(
                label: "Delete Account",
                voice: .critical,
                showDivider: false,
                onItemClicked: {},
                leadingSlot: {
                    LemonadeUi.Icon(
                        icon: .trash,
                        contentDescription: nil,
                        size: .medium,
                        tint: .content.contentCritical
                    )
                }
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
