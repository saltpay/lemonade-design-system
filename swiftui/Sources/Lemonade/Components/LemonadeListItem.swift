import SwiftUI

// MARK: - SelectListItemType

/// Defines the selection behavior type for SelectListItem.
/// - `single`: Radio button selection (only one item can be selected)
/// - `multiple`: Checkbox selection (multiple items can be selected)
/// - `toggle`: Switch control in trailing slot (toggles on each tap)
public enum SelectListItemType {
    case single
    case multiple
    case toggle
}

// MARK: - LemonadeListItemVoice

/// Defines the tone of voice for ListItem.
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

// MARK: - Internal ListItem Helpers

extension LemonadeUi {
    /// Convenience overload that composes standard label and description content from string
    /// parameters and delegates to the content-slot variant of ListItem.
    ///
    /// - Parameters:
    ///   - label: Label String to be displayed
    ///   - description: Optional description text displayed below the label
    ///   - voice: LemonadeListItemVoice to define tone of voice
    ///   - navigationIndicator: Shows a chevron-right navigation indicator
    ///   - enabled: Flag to define if component is enabled
    ///   - showDivider: Flag to show a divider below the list item
    ///   - onListItemClick: Optional callback triggered on click interaction
    ///   - leadingSlot: Slot content to be placed in leading position
    ///   - trailingSlot: Slot content to be placed in trailing position
    ///   - slotContent: Optional slot content below the label and description
    @ViewBuilder
    static func ListItem<LeadingContent: View, TrailingContent: View, SlotContent: View>(
        label: String,
        description: String? = nil,
        voice: LemonadeListItemVoice = .neutral,
        navigationIndicator: Bool = false,
        isLoading: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        onListItemClick: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent,
        @ViewBuilder slotContent: @escaping () -> SlotContent = { EmptyView() }
    ) -> some View {
        if isLoading {
            ListItemSkeletonView(showDivider: showDivider)
        } else {
            ListItem(
                voice: voice,
                navigationIndicator: navigationIndicator,
                enabled: enabled,
                showDivider: showDivider,
                onListItemClick: onListItemClick,
                leadingSlot: leadingSlot,
                trailingSlot: trailingSlot,
                contentSlot: {
                    LemonadeUi.Text(
                        label,
                        textStyle: LemonadeTypography.shared.bodyMediumMedium,
                        color: voice.contentColor
                    )

                    if let description = description {
                        LemonadeUi.Text(
                            description,
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )
                    }

                    if SlotContent.self != EmptyView.self {
                        slotContent()
                    }
                }
            )
        }
    }

    /// Foundational list-item overload that accepts a generic content slot for custom content,
    /// delegating layout and interaction handling to LemonadeCoreListItemView.
    ///
    /// - Parameters:
    ///   - voice: LemonadeListItemVoice to define tone of voice
    ///   - navigationIndicator: Shows a chevron-right navigation indicator
    ///   - enabled: Flag to define if component is enabled
    ///   - showDivider: Flag to show a divider below the list item
    ///   - onListItemClick: Optional callback triggered on click interaction
    ///   - leadingSlot: Slot content to be placed in leading position
    ///   - trailingSlot: Slot content to be placed in trailing position
    ///   - contentSlot: Content slot for the main body of the list item
    @ViewBuilder
    static func ListItem<ContentSlot: View, LeadingContent: View, TrailingContent: View>(
        voice: LemonadeListItemVoice = .neutral,
        navigationIndicator: Bool = false,
        enabled: Bool = true,
        showDivider: Bool = false,
        onListItemClick: (() -> Void)? = nil,
        @ViewBuilder leadingSlot: @escaping () -> LeadingContent,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent,
        @ViewBuilder contentSlot: @escaping () -> ContentSlot
    ) -> some View {
        LemonadeCoreListItemView(
            contentSlot: contentSlot,
            voice: voice,
            navigationIndicator: navigationIndicator,
            enabled: enabled,
            showDivider: showDivider,
            onListItemClick: onListItemClick,
            leadingSlot: leadingSlot,
            trailingSlot: trailingSlot
        )
    }
}

// MARK: - Core ListItem View

struct LemonadeCoreListItemView<ContentSlot: View, LeadingContent: View, TrailingContent: View>: View {
    let contentSlot: () -> ContentSlot
    let voice: LemonadeListItemVoice
    let navigationIndicator: Bool
    let enabled: Bool
    let showDivider: Bool
    let onListItemClick: (() -> Void)?
    let leadingSlot: () -> LeadingContent
    let trailingSlot: () -> TrailingContent

    private var hasLeading: Bool {
        LeadingContent.self != EmptyView.self
    }

    private var hasTrailing: Bool {
        TrailingContent.self != EmptyView.self
    }

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
        HStack(spacing: 0) {
            if hasLeading {
                leadingSlot()
                    .frame(maxHeight: .infinity, alignment: .top)
                    .padding(.trailing, LemonadeTheme.spaces.spacing300)
                    .padding(.vertical, LemonadeTheme.spaces.spacing50)
                    .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
            }

            HStack(spacing: 0) {
                VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing0) {
                    contentSlot()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)

                HStack(spacing: 0) {
                    if hasTrailing {
                        trailingSlot()
                    }

                    if navigationIndicator {
                        LemonadeUi.Icon(
                            icon: .chevronRight,
                            contentDescription: "Navigation indicator",
                            size: .medium,
                            tint: LemonadeTheme.colors.content.contentTertiary
                        )
                        .opacity(0.5)
                        .padding(.leading, LemonadeTheme.spaces.spacing100)
                    }
                }
            }
        }
        .padding(.horizontal, LemonadeTheme.spaces.spacing300)
        .padding(.vertical, LemonadeTheme.spaces.spacing300)
    }
}

// MARK: - SafeArea Wrapper

struct ListItemSafeArea<Content: View>: View {
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

struct ListItemButtonStyle: ButtonStyle {
    let voice: LemonadeListItemVoice

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .background(
                configuration.isPressed
                    ? voice.interactionBackground
                    : Color.clear
            )
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
            .contentShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
            .animation(.easeInOut(duration: 0.15), value: configuration.isPressed)
    }
}

// MARK: - ListItem Skeleton (Loading State)

private struct ListItemSkeletonView: View {
    let showDivider: Bool

    var body: some View {
        ListItemSafeArea(showDivider: showDivider) {
            HStack(spacing: 0) {
                LemonadeUi.CircleSkeleton(size: .xLarge)
                    .padding(.trailing, LemonadeTheme.spaces.spacing300)

                HStack(spacing: 0) {
                    VStack(alignment: .leading, spacing: 0) {
                        LemonadeUi.LineSkeleton(size: .medium)
                        LemonadeUi.LineSkeleton(size: .small)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    Spacer()
                        .frame(width: LemonadeTheme.spaces.spacing300)

                    LemonadeUi.LineSkeleton(size: .medium)
                        .frame(width: 54)
                }
            }
            .padding(.horizontal, LemonadeTheme.spaces.spacing300)
            .padding(.vertical, LemonadeTheme.spaces.spacing300)
        }
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
                description: "Description"
            )

            // SelectListItem - Multiple with divider
            LemonadeUi.SelectListItem(
                label: "Multiple Selection",
                type: .multiple,
                checked: false,
                onItemClicked: {},
                showDivider: true,
                description: "Description"
            )

            LemonadeUi.HorizontalDivider()
                .padding(.vertical, LemonadeTheme.spaces.spacing200)

            // ResourceListItem with divider
            LemonadeUi.ResourceListItem(
                label: "Resource Label",
                value: "$100.00",
                description: "Metadata",
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
                .padding(.vertical, LemonadeTheme.spaces.spacing200)

            // ActionListItem with divider
            LemonadeUi.ActionListItem(
                label: "Action Item",
                description: "Description",
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
                        tint: LemonadeTheme.colors.content.contentCritical
                    )
                }
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
