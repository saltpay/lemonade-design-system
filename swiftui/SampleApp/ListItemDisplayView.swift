import SwiftUI
import Lemonade

struct ListItemDisplayView: View {
    @State private var singleSelection = 0
    @State private var multipleSelections: Set<Int> = [0]

    var body: some View {
        ScrollView(.vertical) {
            VStack {
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
                            divider: index < 2,
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
                            divider: index < 2
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
                        divider: true,
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
                        divider: true,
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
                        divider: false,
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
                        divider: true,
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
                        divider: true,
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
                        divider: true,
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
                        divider: true,
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
                        divider: true,
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

                // MARK: - ActionListItem - Critical Voice
                LemonadeUi.Card(
                    contentPadding: .none,
                    header: CardHeaderConfig(title: "ActionListItem - Critical Voice")
                ) {
                    LemonadeUi.ActionListItem(
                        label: "Delete Account",
                        voice: .critical,
                        divider: true,
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

                    LemonadeUi.ActionListItem(
                        label: "Log Out",
                        supportText: "You will need to sign in again",
                        voice: .critical,
                        divider: true,
                        onItemClicked: {},
                        leadingSlot: {
                            LemonadeUi.Icon(
                                icon: .logOut,
                                contentDescription: nil,
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentCritical
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
                        divider: true,
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
            }
            .padding(LemonadeTheme.spaces.spacing400)
        }
        .background(LemonadeTheme.colors.background.bgSubtle)
        .navigationTitle("ListItem")
    }
}

#Preview {
    NavigationView {
        ListItemDisplayView()
    }
}
