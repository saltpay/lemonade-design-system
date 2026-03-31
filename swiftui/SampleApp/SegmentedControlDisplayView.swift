import SwiftUI
import Lemonade

struct SegmentedControlDisplayView: View {
    @State private var selectedTab1 = 0
    @State private var selectedTab2 = 1
    @State private var selectedTab3 = 0
    @State private var selectedTabMedium = 0
    @State private var selectedTabSmall = 0
    @State private var selectedTabIconOnly = 0

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Large (default)
                sectionView(title: "Large (Default)") {
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
                            .foregroundStyle(.content.contentSecondary)
                    }
                }

                // Medium
                sectionView(title: "Medium") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(label: "Tab 1"),
                                LemonadeTabButtonProperties(label: "Tab 2"),
                                LemonadeTabButtonProperties(label: "Tab 3"),
                            ],
                            selectedTab: selectedTabMedium,
                            size: .medium,
                            onTabSelected: { selectedTabMedium = $0 }
                        )

                        Text("Selected: Tab \(selectedTabMedium + 1)")
                            .font(.caption)
                            .foregroundStyle(.content.contentSecondary)
                    }
                }

                // Small
                sectionView(title: "Small") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(label: "Tab 1"),
                                LemonadeTabButtonProperties(label: "Tab 2"),
                                LemonadeTabButtonProperties(label: "Tab 3"),
                            ],
                            selectedTab: selectedTabSmall,
                            size: .small,
                            onTabSelected: { selectedTabSmall = $0 }
                        )

                        Text("Selected: Tab \(selectedTabSmall + 1)")
                            .font(.caption)
                            .foregroundStyle(.content.contentSecondary)
                    }
                }

                // With Icons
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
                            .foregroundStyle(.content.contentSecondary)
                    }
                }

                // Icon Only (Small)
                sectionView(title: "Icon Only (Small)") {
                    VStack(spacing: 16) {
                        LemonadeUi.SegmentedControl(
                            properties: [
                                LemonadeTabButtonProperties(icon: .heart),
                                LemonadeTabButtonProperties(icon: .star),
                                LemonadeTabButtonProperties(icon: .gear),
                            ],
                            selectedTab: selectedTabIconOnly,
                            size: .small,
                            onTabSelected: { selectedTabIconOnly = $0 }
                        )
                    }
                }

                // Toggle
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
                            .foregroundStyle(.content.contentSecondary)
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
                .foregroundStyle(.content.contentSecondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        SegmentedControlDisplayView()
    }
}
