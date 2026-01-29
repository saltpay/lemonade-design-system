import SwiftUI
import Lemonade

struct HomeView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: LemonadeTheme.spaces.spacing400) {
                ForEach(DisplayRegistry.homeItems, id: \.title) { displayData in
                    LemonadeUi.Card(
                        contentPadding: .none,
                        header: CardHeaderConfig(title: displayData.title)
                    ) {
                        ForEach(Array(displayData.items.enumerated()), id: \.element.label) { index, item in
                            NavigationLink(destination: item.destination) {
                                LemonadeUi.ActionListItem(
                                    label: item.label,
                                    divider: index != displayData.items.count - 1,
                                    leadingSlot: { EmptyView() },
                                    trailingSlot: {
                                        LemonadeUi.Icon(
                                            icon: .chevronRight,
                                            contentDescription: "Navigation indicator",
                                            size: .medium,
                                            tint: LemonadeTheme.colors.content.contentTertiary
                                        )
                                    }
                                )
                            }
                            .buttonStyle(.plain)
                        }
                    }
                }
            }
            .padding(LemonadeTheme.spaces.spacing400)
        }
        .background(LemonadeTheme.colors.background.bgSubtle)
        .navigationTitle("Lemonade DS")
    }
}

// MARK: - Display Item

struct DisplayItem: Identifiable {
    let id = UUID()
    let label: String
    let destination: AnyView

    init<V: View>(label: String, destination: V) {
        self.label = label
        self.destination = AnyView(destination)
    }
}

// MARK: - Display Data

struct DisplayData {
    let title: String
    let items: [DisplayItem]
}

// MARK: - Display Registry

enum DisplayRegistry {
    static let homeItems: [DisplayData] = [
        DisplayData(
            title: "Foundations",
            items: [
                DisplayItem(label: "Colors", destination: ColorsDisplayView()),
                DisplayItem(label: "Spacing", destination: SpacingDisplayView()),
                DisplayItem(label: "Radius", destination: RadiusDisplayView()),
                DisplayItem(label: "Shadows", destination: ShadowsDisplayView()),
                DisplayItem(label: "Sizes", destination: SizesDisplayView()),
                DisplayItem(label: "Opacity", destination: OpacityDisplayView()),
                DisplayItem(label: "Border Width", destination: BorderWidthDisplayView()),
            ]
        ),
        DisplayData(
            title: "Assets",
            items: [
                DisplayItem(label: "Icons", destination: IconsDisplayView()),
                DisplayItem(label: "Brand Logos", destination: BrandLogosDisplayView()),
                DisplayItem(label: "Country Flags", destination: FlagsDisplayView()),
            ]
        ),
        DisplayData(
            title: "Typography",
            items: [
                DisplayItem(label: "Text", destination: TextDisplayView()),
            ]
        ),
        DisplayData(
            title: "Form Controls",
            items: [
                DisplayItem(label: "Button", destination: ButtonDisplayView()),
                DisplayItem(label: "IconButton", destination: IconButtonDisplayView()),
                DisplayItem(label: "Checkbox", destination: CheckboxDisplayView()),
                DisplayItem(label: "RadioButton", destination: RadioButtonDisplayView()),
                DisplayItem(label: "Switch", destination: SwitchDisplayView()),
            ]
        ),
        DisplayData(
            title: "Input Fields",
            items: [
                DisplayItem(label: "TextField", destination: TextFieldDisplayView()),
                DisplayItem(label: "SearchField", destination: SearchFieldDisplayView()),
            ]
        ),
        DisplayData(
            title: "Display Components",
            items: [
                DisplayItem(label: "Tag", destination: TagDisplayView()),
                DisplayItem(label: "Badge", destination: BadgeDisplayView()),
                DisplayItem(label: "SymbolContainer", destination: SymbolContainerDisplayView()),
                DisplayItem(label: "Card", destination: CardDisplayView()),
                DisplayItem(label: "Divider", destination: DividerDisplayView()),
            ]
        ),
        DisplayData(
            title: "Selection & Lists",
            items: [
                DisplayItem(label: "Chip", destination: ChipDisplayView()),
                DisplayItem(label: "ListItem", destination: ListItemDisplayView()),
                DisplayItem(label: "SegmentedControl", destination: SegmentedControlDisplayView()),
            ]
        ),
        DisplayData(
            title: "Navigation",
            items: [
                DisplayItem(label: "Tile", destination: TileDisplayView()),
            ]
        ),
    ]
}

#Preview {
    NavigationView {
        HomeView()
    }
}
