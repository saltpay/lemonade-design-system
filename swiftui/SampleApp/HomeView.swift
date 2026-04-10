import SwiftUI
import Lemonade

struct HomeView: View {
    @EnvironmentObject private var styleHandler: LemonadeStyleHandler
    @State private var searchText: String = ""

    private struct DemoItem: Identifiable {
        let id = UUID()
        let title: String
        let destination: AnyView
    }

    private struct DemoSection: Identifiable {
        let id = UUID()
        let title: String
        let items: [DemoItem]
    }

    private var sections: [DemoSection] {
        [
            DemoSection(
                title: "Foundations",
                items: [
                    DemoItem(title: "Colors", destination: AnyView(ColorsDisplayView())),
                    DemoItem(title: "Spacing", destination: AnyView(SpacingDisplayView())),
                    DemoItem(title: "Radius", destination: AnyView(RadiusDisplayView())),
                    DemoItem(title: "Shadows", destination: AnyView(ShadowsDisplayView())),
                    DemoItem(title: "Sizes", destination: AnyView(SizesDisplayView())),
                    DemoItem(title: "Opacity", destination: AnyView(OpacityDisplayView())),
                    DemoItem(title: "Border Width", destination: AnyView(BorderWidthDisplayView()))
                ]
            ),
            DemoSection(
                title: "Assets",
                items: [
                    DemoItem(title: "Icons", destination: AnyView(IconsDisplayView())),
                    DemoItem(title: "Brand Logos", destination: AnyView(BrandLogosDisplayView())),
                    DemoItem(title: "Country Flags", destination: AnyView(FlagsDisplayView()))
                ]
            ),
            DemoSection(
                title: "Typography",
                items: [
                    DemoItem(title: "Text", destination: AnyView(TextDisplayView()))
                ]
            ),
            DemoSection(
                title: "Form Controls",
                items: [
                    DemoItem(title: "Button", destination: AnyView(ButtonDisplayView())),
                    DemoItem(title: "IconButton", destination: AnyView(IconButtonDisplayView())),
                    DemoItem(title: "Checkbox", destination: AnyView(CheckboxDisplayView())),
                    DemoItem(title: "RadioButton", destination: AnyView(RadioButtonDisplayView())),
                    DemoItem(title: "Switch", destination: AnyView(SwitchDisplayView())),
                    DemoItem(title: "DatePicker", destination: AnyView(DatePickerDisplayView()))
                ]
            ),
            DemoSection(
                title: "Input Fields",
                items: [
                    DemoItem(title: "TextField", destination: AnyView(TextFieldDisplayView())),
                    DemoItem(title: "SearchField", destination: AnyView(SearchFieldDisplayView())),
                    DemoItem(title: "SelectField", destination: AnyView(SelectFieldDisplayView()))
                ]
            ),
            DemoSection(
                title: "Display Components",
                items: [
                    DemoItem(title: "Tag", destination: AnyView(TagDisplayView())),
                    DemoItem(title: "Badge", destination: AnyView(BadgeDisplayView())),
                    DemoItem(title: "SymbolContainer", destination: AnyView(SymbolContainerDisplayView())),
                    DemoItem(title: "Card", destination: AnyView(CardDisplayView())),
                    DemoItem(title: "Divider", destination: AnyView(DividerDisplayView())),
                    DemoItem(title: "Notice", destination: AnyView(NoticeDisplayView()))
                ]
            ),
            DemoSection(
                title: "Selection & Lists",
                items: [
                    DemoItem(title: "Chip", destination: AnyView(ChipDisplayView())),
                    DemoItem(title: "ListItem", destination: AnyView(ListItemDisplayView())),
                    DemoItem(title: "ContentListItem", destination: AnyView(ContentListItemDisplayView())),
                    DemoItem(title: "SegmentedControl", destination: AnyView(SegmentedControlDisplayView()))
                ]
            ),
            DemoSection(
                title: "Navigation",
                items: [
                    DemoItem(title: "Link", destination: AnyView(LinkDisplayView())),
                    DemoItem(title: "Tabs", destination: AnyView(TabsDisplayView())),
                    DemoItem(title: "Tile", destination: AnyView(TileDisplayView()))
                ]
            ),
            DemoSection(
                title: "Feedback",
                items: [
                    DemoItem(title: "Skeleton", destination: AnyView(SkeletonDisplayView())),
                    DemoItem(title: "Spinner", destination: AnyView(SpinnerDisplayView())),
                    DemoItem(title: "Toast", destination: AnyView(ToastDisplayView()))
                ]
            )
        ]
    }

    private var filteredSections: [DemoSection] {
        let text = searchText.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !text.isEmpty else { return sections }

        // Filter items within each section and drop empty sections
        return sections.compactMap { section in
            let matchedItems = section.items.filter { item in
                item.title.localizedCaseInsensitiveContains(text) ||
                section.title.localizedCaseInsensitiveContains(text)
            }
            return matchedItems.isEmpty ? nil : DemoSection(title: section.title, items: matchedItems)
        }
    }

    private var selectedStyleIndex: Int {
        LemonadeStyle.allCases.firstIndex(of: styleHandler.currentStyle) ?? 0
    }

    var body: some View {
        List {
            Section {
                LemonadeUi.SegmentedControl(
                    properties: LemonadeStyle.allCases.map { .label($0.label) },
                    selectedTab: selectedStyleIndex,
                    onTabSelected: { index in
                        styleHandler.currentStyle = LemonadeStyle.allCases[index]
                    }
                )
            }

            ForEach(filteredSections) { section in
                Section(section.title) {
                    ForEach(section.items) { item in
                        NavigationLink(item.title) { item.destination }
                    }
                }
            }
        }
        .navigationTitle("Lemonade DS")
        .searchable(text: $searchText, placement: .navigationBarDrawer(displayMode: .automatic))
        .textInputAutocapitalization(.never)
        .disableAutocorrection(true)
    }
}

#Preview {
    NavigationStack {
        HomeView()
    }
    .environmentObject(LemonadeStyleHandler())
}
