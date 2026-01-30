import SwiftUI
import Lemonade

struct ListItemDisplayView: View {
    @State private var singleSelection = 0
    @State private var multipleSelections: Set<Int> = [0]

    var body: some View {
        ScrollView(.vertical) {
            VStack(alignment: .leading, spacing: 24) {
                // SelectListItem - Single
                sectionView(title: "SelectListItem - Single") {
                    VStack(spacing: 0) {
                        ForEach(0..<3, id: \.self) { index in
                            LemonadeUi.SelectListItem(
                                label: "Option \(index + 1)",
                                type: .single,
                                checked: singleSelection == index,
                                onItemClicked: { singleSelection = index },
                                supportText: index == 0 ? "With support text" : nil
                            )
                        }
                    }
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // SelectListItem - Multiple
                sectionView(title: "SelectListItem - Multiple") {
                    VStack(spacing: 0) {
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
                                }
                            )
                        }
                    }
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // SelectListItem with Leading Slot
                sectionView(title: "SelectListItem with Leading") {
                    VStack(spacing: 0) {
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // Disabled SelectListItem
                sectionView(title: "Disabled States") {
                    VStack(spacing: 0) {
                        LemonadeUi.SelectListItem(
                            label: "Disabled Option",
                            type: .single,
                            checked: false,
                            onItemClicked: {},
                            enabled: false
                        )
                    }
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                Divider().padding(.vertical, 8)

                // ResourceListItem
                sectionView(title: "ResourceListItem") {
                    VStack(spacing: 0) {
                        LemonadeUi.ResourceListItem(
                            label: "Account Balance",
                            value: "$1,234.56",
                            supportText: "Updated today",
                            onItemClicked: {},
                            leadingSlot: {
                                LemonadeUi.SymbolContainer(
                                    icon: .money,
                                    contentDescription: nil,
                                    voice: .info,
                                    size: .large
                                )
                            },
                        )

                        LemonadeUi.ResourceListItem(
                            label: "Savings",
                            value: "$5,000.00",
                            onItemClicked: {},
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // ResourceListItem with Addon
                sectionView(title: "ResourceListItem with Addon") {
                    VStack(spacing: 0) {
                        LemonadeUi.ResourceListItem(
                            label: "Last Transaction",
                            value: "-$50.00",
                            supportText: "Yesterday",
                            onItemClicked: {},
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                Divider().padding(.vertical, 8)

                // ActionListItem
                sectionView(title: "ActionListItem") {
                    VStack(spacing: 0) {
                        LemonadeUi.ActionListItem(
                            label: "Settings",
                            showNavigationIndicator: true,
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // ActionListItem with Trailing Slot
                sectionView(title: "ActionListItem with Trailing") {
                    VStack(spacing: 0) {
                        LemonadeUi.ActionListItem(
                            label: "Updates Available",
                            showNavigationIndicator: true,
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // ActionListItem - Critical
                sectionView(title: "ActionListItem - Critical Voice") {
                    VStack(spacing: 0) {
                        LemonadeUi.ActionListItem(
                            label: "Delete Account",
                            voice: .critical,
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }

                // Disabled ActionListItem
                sectionView(title: "Disabled States") {
                    VStack(spacing: 0) {
                        LemonadeUi.ActionListItem(
                            label: "Disabled Action",
                            enabled: false,
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
                    .background(.bg.bgDefault)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
                }
            }
            .padding(LemonadeTheme.spaces.spacing400)
        }
        .background(.bg.bgSubtle)
        .navigationTitle("ListItem")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            LemonadeUi.Text(
                title,
                textStyle: LemonadeTypography.shared.headingXSmall,
                color: .content.contentSecondary
            )

            content()
        }
    }
}

#Preview {
    NavigationStack {
        ListItemDisplayView()
    }
}
