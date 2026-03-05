import SwiftUI
import Lemonade

struct TabsDisplayView: View {
    @State private var hugSelectedIndex = 0
    @State private var stretchSelectedIndex = 0
    @State private var manyTabsSelectedIndex = 2

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing800) {
                sectionView(title: "Hug (default)") {
                    LemonadeUi.Tabs(
                        tabs: ["Overview", "Details", "Reviews"],
                        selectedIndex: hugSelectedIndex,
                        onTabSelected: { hugSelectedIndex = $0 }
                    )
                }

                sectionView(title: "Stretch") {
                    LemonadeUi.Tabs(
                        tabs: ["Tab A", "Tab B", "Tab C"],
                        selectedIndex: stretchSelectedIndex,
                        onTabSelected: { stretchSelectedIndex = $0 },
                        itemsSize: .stretch
                    )
                }

                sectionView(title: "Many Tabs (scrollable)") {
                    LemonadeUi.Tabs(
                        tabs: ["Dashboard", "Analytics", "Reports", "Settings", "Users", "Activity"],
                        selectedIndex: manyTabsSelectedIndex,
                        onTabSelected: { manyTabsSelectedIndex = $0 }
                    )
                }
            }
            .padding(.vertical)
        }
        .navigationTitle("Tabs")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            Text(title)
                .font(.headline)
                .foregroundStyle(.content.contentSecondary)
                .padding(.horizontal)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        TabsDisplayView()
    }
}
