import SwiftUI
import Lemonade

// MARK: - TopBar Display View

struct TopBarDisplayView: View {

    private struct DemoItem: Identifiable {
        let id = UUID()
        let title: String
        let destination: AnyView
    }

    private let demos: [DemoItem] = [
        DemoItem(title: "Basic (native back)", destination: AnyView(BasicTopBarDemo())),
        DemoItem(title: "Basic (close button)", destination: AnyView(BasicCloseDemo())),
        DemoItem(title: "Basic with Trailing Slot", destination: AnyView(BasicTrailingSlotDemo())),
        DemoItem(title: "Basic with Bottom Slot", destination: AnyView(BasicBottomSlotDemo())),
        DemoItem(title: "Search", destination: AnyView(SearchTopBarDemo())),
        DemoItem(title: "Search with Expanded Label", destination: AnyView(SearchExpandedLabelDemo())),
        DemoItem(title: "Compact Large (pill)", destination: AnyView(CompactLargePillDemo())),
        DemoItem(title: "Compact Large", destination: AnyView(CompactLargeDemo())),
        DemoItem(title: "Compact Large with Subheading", destination: AnyView(CompactLargeSubheadingDemo())),
        DemoItem(title: "Compact Large + Search", destination: AnyView(CompactLargeSearchDemo())),
    ]

    var body: some View {
        List {
            ForEach(demos) { demo in
                NavigationLink(demo.title) {
                    demo.destination
                }
            }
        }
        .navigationTitle("TopBar")
    }
}

// MARK: - Sample Content

private struct SampleListContent: View {
    var itemCount: Int = 30

    var body: some View {
        LazyVStack(spacing: 0) {
            ForEach(0..<itemCount, id: \.self) { index in
                HStack {
                    LemonadeUi.SymbolContainer(
                        icon: .store,
                        contentDescription: nil,
                        voice: .neutral,
                        size: .medium
                    )

                    VStack(alignment: .leading, spacing: 2) {
                        SwiftUI.Text("Item \(index + 1)")
                            .font(.body)
                        SwiftUI.Text("Scroll to see collapse behavior")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                    }

                    Spacer()

                    LemonadeUi.Icon(
                        icon: .chevronRight,
                        contentDescription: nil,
                        size: .small,
                        tint: LemonadeTheme.colors.content.contentTertiary
                    )
                }
                .padding(.horizontal, LemonadeSpacing.spacing400.value)
                .padding(.vertical, LemonadeSpacing.spacing300.value)

                LemonadeUi.HorizontalDivider()
            }
        }
    }
}

// MARK: - 1. Basic TopBar Demos

private struct BasicTopBarDemo: View {
    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(
            label: "Settings",
            navigationAction: NavigationAction(action: .back, onAction: {})
        )
    }
}

private struct BasicCloseDemo: View {
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(
            label: "Edit Profile",
            navigationAction: NavigationAction(action: .close, onAction: { dismiss() })
        )
    }
}

private struct BasicTrailingSlotDemo: View {
    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(
            label: "Notifications",
            navigationAction: NavigationAction(action: .back, onAction: {})
        ) {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .bell, contentDescription: "Notifications")
                }
            }
            #if compiler(>=6.2)
            if #available(iOS 26, *) {
                ToolbarSpacer(.fixed, placement: .navigationBarTrailing)
            }
            #endif
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .ellipsisHorizontal, contentDescription: "More")
                }
            }
        }
    }
}

private struct BasicBottomSlotDemo: View {
    @State private var selectedTab = 0

    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(
            label: "Browse",
            navigationAction: NavigationAction(action: .back, onAction: {}),
            bottomSlot: {
                LemonadeUi.SegmentedControl(
                    properties: [.label("All"), .label("Recent"), .label("Favorites")],
                    selectedTab: selectedTab,
                    onTabSelected: { selectedTab = $0 }
                )
                .padding(.horizontal, LemonadeSpacing.spacing400.value)
                .padding(.bottom, LemonadeSpacing.spacing200.value)
            },
            toolbar: {
                ToolbarItem(placement: .navigationBarTrailing) {
                    EmptyView()
                }
            }
        )
    }
}

// MARK: - 2. Search TopBar Demos

private struct SearchTopBarDemo: View {
    @State private var searchQuery = ""

    private let allItems = (1...30).map { "Item \($0)" }

    private var filteredItems: [String] {
        guard !searchQuery.isEmpty else { return allItems }
        return allItems.filter { $0.localizedCaseInsensitiveContains(searchQuery) }
    }

    var body: some View {
        List {
            ForEach(filteredItems, id: \.self) { item in
                SwiftUI.Text(item)
            }
        }
        .lemonadeTopBar(
            label: "Search",
            searchInput: $searchQuery,
            navigationAction: NavigationAction(action: .back, onAction: {})
        )
    }
}

private struct SearchExpandedLabelDemo: View {
    @State private var searchQuery = ""

    private let products = [
        "iPhone 15 Pro", "MacBook Air M3", "iPad Pro", "Apple Watch Ultra",
        "AirPods Pro", "Mac Studio", "Apple TV 4K", "HomePod mini",
        "Vision Pro", "Mac Mini", "iMac 24\"", "MacBook Pro 16\"",
    ]

    private var filteredProducts: [String] {
        guard !searchQuery.isEmpty else { return products }
        return products.filter { $0.localizedCaseInsensitiveContains(searchQuery) }
    }

    var body: some View {
        List {
            ForEach(filteredProducts, id: \.self) { product in
                HStack {
                    SwiftUI.Text(product)
                    Spacer()
                    LemonadeUi.Icon(
                        icon: .chevronRight,
                        contentDescription: nil,
                        size: .small,
                        tint: LemonadeTheme.colors.content.contentTertiary
                    )
                }
            }
        }
        .lemonadeTopBar(
            label: "Products",
            searchInput: $searchQuery,
            searchPrompt: "Search products...",
            expandedLabel: "Discover",
            navigationAction: NavigationAction(action: .back, onAction: {})
        ) {
            ToolbarItem(placement: .navigationBarTrailing) {
                LemonadeUi.IconButton(
                    icon: .filter,
                    contentDescription: "Filter",
                    onClick: {},
                    variant: LemonadeIconButtonVariant.ghost
                )
            }
        }
    }
}

// MARK: - 3. Compact Large Demos

/// Helper: groups buttons in a pill (capsule) with glass effect.
private struct GlassPill<Content: View>: View {
    @ViewBuilder let content: () -> Content

    var body: some View {
        HStack(spacing: 4) {
            content()
        }
        .modifier(GlassCapsuleModifier())
    }
}

private struct GlassCapsuleModifier: ViewModifier {
    func body(content: Content) -> some View {
        #if compiler(>=6.2)
        if #available(iOS 26, *) {
            content.glassEffect(.regular.interactive(), in: .capsule)
        } else {
            content
                .padding(.horizontal, 4)
                .background(.ultraThinMaterial, in: Capsule())
        }
        #else
        content
            .padding(.horizontal, 4)
            .background(.ultraThinMaterial, in: Capsule())
        #endif
    }
}

private struct CompactLargePillDemo: View {
    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(label: "Home", subheading: "Pill trailing") {
            ToolbarItemGroup(placement: .topBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .bell, contentDescription: "Notifications")
                }
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .gear, contentDescription: "Settings")
                }
            }
        }
    }
}

private struct CompactLargeDemo: View {
    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(label: "Home", subheading: nil) {
            ToolbarItem(placement: .topBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .bell, contentDescription: "Notifications")
                }
            }
            ToolbarItem(placement: .topBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .gear, contentDescription: "Settings")
                }
            }
        }
    }
}

private struct CompactLargeSubheadingDemo: View {
    var body: some View {
        ScrollView {
            SampleListContent()
        }
        .lemonadeTopBar(label: "Home", subheading: "Welcome back, John") {
            ToolbarItem(placement: .topBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .bell, contentDescription: "Notifications")
                }
            }
        }
    }
}

// MARK: - 4. Compact Large + Search Demo

private struct CompactLargeSearchDemo: View {
    @State private var searchQuery = ""

    private let categories = [
        "Electronics", "Clothing", "Books", "Home & Garden",
        "Sports", "Toys", "Beauty", "Automotive",
        "Food & Beverages", "Health", "Music", "Movies",
        "Pets", "Office", "Travel", "Jewelry",
        "Baby", "Garden Tools", "Fitness", "Photography",
        "Gaming", "Art Supplies", "Kitchen", "Furniture",
    ]

    private var filteredCategories: [String] {
        guard !searchQuery.isEmpty else { return categories }
        return categories.filter { $0.localizedCaseInsensitiveContains(searchQuery) }
    }

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(filteredCategories, id: \.self) { category in
                    HStack {
                        SwiftUI.Text(category)
                        Spacer()
                        LemonadeUi.Icon(
                            icon: .chevronRight,
                            contentDescription: nil,
                            size: .small,
                            tint: LemonadeTheme.colors.content.contentTertiary
                        )
                    }
                    .padding(.horizontal, LemonadeSpacing.spacing400.value)
                    .padding(.vertical, LemonadeSpacing.spacing300.value)

                    LemonadeUi.HorizontalDivider()
                }
            }
        }
        .lemonadeTopBar(
            label: "Discover",
            subheading: "Find what you need",
            searchInput: $searchQuery,
            searchPrompt: "Search categories..."
        ) {
            ToolbarItem(placement: .topBarTrailing) {
                Button(action: {}) {
                    LemonadeUi.Icon(icon: .ellipsisHorizontal, contentDescription: "More")
                }
            }
        }
    }
}

// MARK: - Preview

#Preview {
    NavigationStack {
        TopBarDisplayView()
    }
}
