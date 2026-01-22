import SwiftUI
import Lemonade

struct HomeView: View {
    var body: some View {
        List {
            Section("Foundations") {
                NavigationLink("Colors") {
                    ColorsDisplayView()
                }
                NavigationLink("Spacing") {
                    SpacingDisplayView()
                }
                NavigationLink("Radius") {
                    RadiusDisplayView()
                }
                NavigationLink("Shadows") {
                    ShadowsDisplayView()
                }
                NavigationLink("Sizes") {
                    SizesDisplayView()
                }
                NavigationLink("Opacity") {
                    OpacityDisplayView()
                }
                NavigationLink("Border Width") {
                    BorderWidthDisplayView()
                }
            }

            Section("Assets") {
                NavigationLink("Icons") {
                    IconsDisplayView()
                }
                NavigationLink("Brand Logos") {
                    BrandLogosDisplayView()
                }
                NavigationLink("Country Flags") {
                    FlagsDisplayView()
                }
            }

            Section("Typography") {
                NavigationLink("Text") {
                    TextDisplayView()
                }
            }

            Section("Form Controls") {
                NavigationLink("Button") {
                    ButtonDisplayView()
                }
                NavigationLink("IconButton") {
                    IconButtonDisplayView()
                }
                NavigationLink("Checkbox") {
                    CheckboxDisplayView()
                }
                NavigationLink("RadioButton") {
                    RadioButtonDisplayView()
                }
                NavigationLink("Switch") {
                    SwitchDisplayView()
                }
            }

            Section("Input Fields") {
                NavigationLink("TextField") {
                    TextFieldDisplayView()
                }
                NavigationLink("SearchField") {
                    SearchFieldDisplayView()
                }
            }

            Section("Display Components") {
                NavigationLink("Tag") {
                    TagDisplayView()
                }
                NavigationLink("Badge") {
                    BadgeDisplayView()
                }
                NavigationLink("SymbolContainer") {
                    SymbolContainerDisplayView()
                }
                NavigationLink("Card") {
                    CardDisplayView()
                }
            }

            Section("Selection & Lists") {
                NavigationLink("Chip") {
                    ChipDisplayView()
                }
                NavigationLink("ListItem") {
                    ListItemDisplayView()
                }
                NavigationLink("SegmentedControl") {
                    SegmentedControlDisplayView()
                }
            }

            Section("Navigation") {
                NavigationLink("Tile") {
                    TileDisplayView()
                }
            }
        }
        .navigationTitle("Lemonade DS")
    }
}

#Preview {
    NavigationView {
        HomeView()
    }
}
