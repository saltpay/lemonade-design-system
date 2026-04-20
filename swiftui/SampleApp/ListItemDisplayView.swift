import SwiftUI
import Lemonade

private struct OutlinedOption: Identifiable {
    let id = UUID()
    let label: String
    let icon: LemonadeIcon
}

private struct TrailingPreset {
    let label: String
    let voice: TagVoice
}

private let trailingPresets: [TrailingPreset] = [
    TrailingPreset(label: "New", voice: .info),
    TrailingPreset(label: "Recommended", voice: .positive),
    TrailingPreset(label: "Popular", voice: .neutral)
]

struct ListItemDisplayView: View {
    @State private var singleSelection = 0
    @State private var multipleSelections: Set<Int> = [0]
    @State private var toggleStates: [Bool] = [true, false, true]
    @State private var outlinedWithLeading = 0
    @State private var outlinedWithTrailing = 1
    @State private var outlinedLabelOnly = 0
    @State private var outlinedWithSupport = 0
    @State private var outlinedMultiple: Set<Int> = [0]

    private let outlinedOptions: [OutlinedOption] = [
        OutlinedOption(label: "Option A", icon: .heart),
        OutlinedOption(label: "Option B", icon: .star),
        OutlinedOption(label: "Option C", icon: .sparkles),
        OutlinedOption(label: "Option D", icon: .gift)
    ]

    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: .space.spacing400) {
                // MARK: - SelectListItem - Single
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "SelectListItem - Single")
                ) {
                    ForEach(0..<3, id: \.self) { index in
                        LemonadeUi.SelectListItem(
                            label: "Option \(index + 1)",
                            type: .single,
                            checked: singleSelection == index,
                            onItemClicked: { singleSelection = index },
                            showDivider: index < 2,
                            supportText: index == 0 ? "With support text" : nil
                        )
                    }
                }

                // MARK: - SelectListItem - Multiple
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "SelectListItem - Multiple")
                ) {
                    ForEach(0..<3, id: \.self) { index in
                        LemonadeUi.SelectListItem(
                            label: "Item \(index + 1)",
                            type: .multiple,
                            checked: multipleSelections.contains(index),
                            onItemClicked: {
                                if multipleSelections.contains(index) {
                                    multipleSelections.remove(index)
                                } else {
                                    multipleSelections.insert(index)
                                }
                            },
                            showDivider: index < 2
                        )
                    }
                }

                // MARK: - SelectListItem - Toggle
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "SelectListItem - Toggle")
                ) {
                    ForEach(0..<3, id: \.self) { index in
                        LemonadeUi.SelectListItem(
                            label: "Setting \(index + 1)",
                            type: .toggle,
                            checked: toggleStates[index],
                            onItemClicked: { toggleStates[index].toggle() },
                            showDivider: index < 2,
                            supportText: index == 0 ? "With support text" : nil
                        )
                    }
                }

                // MARK: - SelectListItem with Leading
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "SelectListItem with Leading")
                ) {
                    LemonadeUi.SelectListItem(
                        label: "With Icon",
                        type: .single,
                        checked: true,
                        onItemClicked: {},
                        supportText: "Leading icon example",
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .star,
                                contentDescription: nil,
                                size: .medium
                            )
                        }
                    )
                }

                // MARK: - Disabled States (Select)
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "Disabled States")
                ) {
                    LemonadeUi.SelectListItem(
                        label: "Disabled Option",
                        type: .single,
                        checked: false,
                        onItemClicked: {},
                        enabled: false
                    )
                }

                // MARK: - ResourceListItem
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ResourceListItem")
                ) {
                    LemonadeUi.ResourceListItem(
                        label: "Account Balance",
                        value: "$1,234.56",
                        supportText: "Updated today",
                        showDivider: true,
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .money,
                                contentDescription: nil,
                                voice: .info,
                                size: .large
                            )
                        }
                    )

                    LemonadeUi.ResourceListItem(
                        label: "Savings",
                        value: "$5,000.00",
                        showDivider: false,
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .coins,
                                contentDescription: nil,
                                voice: .positive,
                                size: .large
                            )
                        }
                    )
                }

                // MARK: - ResourceListItem with Addon
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ResourceListItem with Addon")
                ) {
                    LemonadeUi.ResourceListItem(
                        label: "Last Transaction",
                        value: "-$50.00",
                        supportText: "Yesterday",
                        showDivider: false,
                        addonSlot: {
                            LemonadeUi.Tag(label: "Pending", voice: .warning)
                        },
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .arrowUpRight,
                                contentDescription: nil,
                                voice: .critical,
                                size: .large
                            )
                        }
                    )
                }

                // MARK: - ActionListItem
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ActionListItem")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "Settings",
                        showNavigationIndicator: true,
                        showDivider: true,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .gear,
                                contentDescription: nil,
                                size: .medium
                            )
                        }
                    )

                    LemonadeUi.ActionListItem(
                        label: "Notifications",
                        supportText: "Manage your notifications",
                        showNavigationIndicator: true,
                        showDivider: true,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .bell,
                                contentDescription: nil,
                                size: .medium
                            )
                        }
                    )

                    LemonadeUi.ActionListItem(
                        label: "Privacy",
                        showNavigationIndicator: true,
                        showDivider: false,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .padlock,
                                contentDescription: nil,
                                size: .medium
                            )
                        }
                    )
                }

                // MARK: - ActionListItem with Trailing
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ActionListItem with Trailing")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "Updates Available",
                        showNavigationIndicator: true,
                        showDivider: true,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .download,
                                contentDescription: nil,
                                size: .medium
                            )
                        },
                        trailingSlot: {
                            LemonadeUi.Badge(text: "3", size: .small)
                        }
                    )

                    LemonadeUi.ActionListItem(
                        label: "New Features",
                        showNavigationIndicator: true,
                        showDivider: false,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .sparkles,
                                contentDescription: nil,
                                size: .medium
                            )
                        },
                        trailingSlot: {
                            LemonadeUi.Tag(label: "New", voice: .positive)
                        }
                    )
                }

                // MARK: - ActionListItem - Figma reference (multi-line support text)
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ActionListItem - Figma reference")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "Account ***4236",
                        supportText: "PT50 0002 0123 1234…\n1 store linked",
                        showNavigationIndicator: true,
                        showDivider: false,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .bank,
                                contentDescription: nil,
                                voice: .neutral,
                                size: .large
                            )
                        },
                        trailingSlot: {
                            LemonadeUi.Tag(label: "Settlements", voice: .positive)
                        }
                    )
                }

                // MARK: - ActionListItem - Critical Voice
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ActionListItem - Critical Voice")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "Delete Account",
                        voice: .critical,
                        showDivider: true,
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

                    LemonadeUi.ActionListItem(
                        label: "Log Out",
                        supportText: "You will need to sign in again",
                        voice: .critical,
                        showDivider: false,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .logOut,
                                contentDescription: nil,
                                size: .medium,
                                tint: .content.contentCritical
                            )
                        }
                    )
                }

                // MARK: - Disabled States (Action)
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "Disabled States")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "Disabled Action",
                        enabled: false,
                        showDivider: false,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .gear,
                                contentDescription: nil,
                                size: .medium
                            )
                        }
                    )
                }

                // MARK: - Loading State
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "Loading State")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "",
                        isLoading: true,
                        showDivider: true
                    )

                    LemonadeUi.ActionListItem(
                        label: "",
                        isLoading: true,
                        showDivider: true
                    )

                    LemonadeUi.ActionListItem(
                        label: "",
                        isLoading: true,
                        showDivider: false
                    )
                }

                // MARK: - Outlined — Leading icon only
                LemonadeUi.Card(
                    header: CardHeaderConfig(title: "Outlined — Leading icon only")
                ) {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        ForEach(Array(outlinedOptions.enumerated()), id: \.element.id) { index, option in
                            let isChecked = outlinedWithLeading == index
                            LemonadeUi.SelectListItem(
                                label: option.label,
                                type: .single,
                                checked: isChecked,
                                onItemClicked: { outlinedWithLeading = index },
                                variant: .outlined,
                                leadingSlot: {
                                    LemonadeUi.SymbolContainer(
                                        icon: option.icon,
                                        contentDescription: nil,
                                        voice: isChecked ? .positive : .neutral,
                                        size: .large,
                                        shape: .rounded
                                    )
                                }
                            )
                        }
                    }
                }

                // MARK: - Outlined — Leading + trailing tag
                LemonadeUi.Card(
                    header: CardHeaderConfig(title: "Outlined — Leading + trailing tag")
                ) {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        ForEach(Array(outlinedOptions.prefix(3).enumerated()), id: \.element.id) { index, option in
                            let isChecked = outlinedWithTrailing == index
                            let preset = trailingPresets[index]
                            LemonadeUi.SelectListItem(
                                label: option.label,
                                type: .single,
                                checked: isChecked,
                                onItemClicked: { outlinedWithTrailing = index },
                                variant: .outlined,
                                leadingSlot: {
                                    LemonadeUi.SymbolContainer(
                                        icon: option.icon,
                                        contentDescription: nil,
                                        voice: isChecked ? .positive : .neutral,
                                        size: .large,
                                        shape: .rounded
                                    )
                                },
                                trailingSlot: {
                                    LemonadeUi.Tag(label: preset.label, voice: preset.voice)
                                }
                            )
                        }
                    }
                }

                // MARK: - Outlined — Label only
                LemonadeUi.Card(
                    header: CardHeaderConfig(title: "Outlined — Label only (no leading, no trailing)")
                ) {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        ForEach(0..<3, id: \.self) { index in
                            LemonadeUi.SelectListItem(
                                label: "Option \(index + 1)",
                                type: .single,
                                checked: outlinedLabelOnly == index,
                                onItemClicked: { outlinedLabelOnly = index },
                                variant: .outlined
                            )
                        }
                    }
                }

                // MARK: - Outlined — With support text
                LemonadeUi.Card(
                    header: CardHeaderConfig(title: "Outlined — With support text")
                ) {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        ForEach(Array(outlinedOptions.prefix(3).enumerated()), id: \.element.id) { index, option in
                            LemonadeUi.SelectListItem(
                                label: option.label,
                                type: .single,
                                checked: outlinedWithSupport == index,
                                onItemClicked: { outlinedWithSupport = index },
                                variant: .outlined,
                                supportText: "Short description for \(option.label.lowercased())",
                                leadingSlot: {
                                    LemonadeUi.SymbolContainer(
                                        icon: option.icon,
                                        contentDescription: nil,
                                        voice: .neutral,
                                        size: .large,
                                        shape: .rounded
                                    )
                                }
                            )
                        }
                    }
                }

                // MARK: - Outlined — Multiple
                LemonadeUi.Card(
                    header: CardHeaderConfig(title: "Outlined — Multiple")
                ) {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        ForEach(Array(outlinedOptions.enumerated()), id: \.element.id) { index, option in
                            let isChecked = outlinedMultiple.contains(index)
                            LemonadeUi.SelectListItem(
                                label: option.label,
                                type: .multiple,
                                checked: isChecked,
                                onItemClicked: {
                                    if isChecked {
                                        outlinedMultiple.remove(index)
                                    } else {
                                        outlinedMultiple.insert(index)
                                    }
                                },
                                variant: .outlined,
                                supportText: index == 0 ? "Tap to toggle" : nil,
                                leadingSlot: {
                                    LemonadeUi.SymbolContainer(
                                        icon: option.icon,
                                        contentDescription: nil,
                                        voice: isChecked ? .positive : .neutral,
                                        size: .large,
                                        shape: .rounded
                                    )
                                }
                            )
                        }
                    }
                }

                // MARK: - Outlined — Disabled states
                LemonadeUi.Card(
                    header: CardHeaderConfig(title: "Outlined — Disabled states")
                ) {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        LemonadeUi.SelectListItem(
                            label: "Disabled, no leading",
                            type: .single,
                            checked: false,
                            onItemClicked: {},
                            variant: .outlined,
                            enabled: false
                        )

                        LemonadeUi.SelectListItem(
                            label: "Disabled, with leading",
                            type: .single,
                            checked: false,
                            onItemClicked: {},
                            variant: .outlined,
                            enabled: false,
                            leadingSlot: {
                                LemonadeUi.SymbolContainer(
                                    icon: .padlock,
                                    contentDescription: nil,
                                    voice: .neutral,
                                    size: .large,
                                    shape: .rounded
                                )
                            }
                        )

                        LemonadeUi.SelectListItem(
                            label: "Disabled, with trailing tag",
                            type: .single,
                            checked: false,
                            onItemClicked: {},
                            variant: .outlined,
                            enabled: false,
                            leadingSlot: {
                                LemonadeUi.SymbolContainer(
                                    icon: .bell,
                                    contentDescription: nil,
                                    voice: .neutral,
                                    size: .large,
                                    shape: .rounded
                                )
                            },
                            trailingSlot: {
                                LemonadeUi.Tag(label: "Coming Soon")
                            }
                        )
                    }
                }
            }
            .padding(.space.spacing400)
        }
        .background(.bg.bgSubtle)
        .navigationTitle("ListItem")
    }
}

#Preview {
    NavigationStack {
        ListItemDisplayView()
    }
}
