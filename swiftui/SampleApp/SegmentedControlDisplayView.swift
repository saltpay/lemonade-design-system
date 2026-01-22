import SwiftUI
import Lemonade

struct SegmentedControlDisplayView: View {
    @State private var selectedTab1 = 0
    @State private var selectedTab2 = 1
    @State private var selectedTab3 = 0

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Basic tabs
                sectionView(title: "Basic Tabs") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(label: "Tab 1"),
                                LemonadeTabButtonProperties(label: "Tab 2"),
                                LemonadeTabButtonProperties(label: "Tab 3"),
                            ],
                            selectedTab: selectedTab1,
                            onTabSelected: { selectedTab1 = $0 }
                        )

                        Text("Selected: Tab \(selectedTab1 + 1)")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                }

                // Tabs with icons
                sectionView(title: "With Icons") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(label: "Favorites", icon: .heart),
                                LemonadeTabButtonProperties(label: "Bookmarks", icon: .star),
                                LemonadeTabButtonProperties(label: "Settings", icon: .gear),
                            ],
                            selectedTab: selectedTab2,
                            onTabSelected: { selectedTab2 = $0 }
                        )

                        Text("Selected: \(["Favorites", "Bookmarks", "Settings"][selectedTab2])")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                }

                // Two tabs toggle
                sectionView(title: "Toggle Style") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(label: "On"),
                                LemonadeTabButtonProperties(label: "Off"),
                            ],
                            selectedTab: selectedTab3,
                            onTabSelected: { selectedTab3 = $0 }
                        )

                        Text("Status: \(selectedTab3 == 0 ? "On" : "Off")")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                }

                // Use case example
                sectionView(title: "Filter Example") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(label: "All"),
                                LemonadeTabButtonProperties(label: "Active"),
                                LemonadeTabButtonProperties(label: "Completed"),
                            ],
                            selectedTab: 0,
                            onTabSelected: { _ in }
                        )
                    }
                }
            }
            .padding()
        }
        .navigationTitle("SegmentedControl")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
                .foregroundColor(.secondary)

            content()
        }
    }
}

#Preview {
    NavigationView {
        SegmentedControlDisplayView()
    }
}
