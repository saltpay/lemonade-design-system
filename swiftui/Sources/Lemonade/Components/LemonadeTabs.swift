import SwiftUI

// MARK: - Tab Items Size

public enum TabItemsSize {
    case hug
    case stretch
}

// MARK: - Tabs Component

public extension LemonadeUi {
    /// A horizontal tab bar with selection indicator.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.Tabs(
    ///     tabs: ["Overview", "Details", "Reviews"],
    ///     selectedIndex: 0,
    ///     onTabSelected: { index in }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - tabs: List of tab labels to display
    ///   - selectedIndex: Index of the currently selected tab
    ///   - onTabSelected: Callback invoked with the tab index when tapped
    ///   - itemsSize: `.hug` for content-sized scrollable tabs, `.stretch` for equal-width tabs
    /// - Returns: A styled tabs view
    @ViewBuilder
    static func Tabs(
        tabs: [String],
        selectedIndex: Int,
        onTabSelected: @escaping (Int) -> Void,
        itemsSize: TabItemsSize = .hug
    ) -> some View {
        LemonadeTabsView(
            tabs: tabs,
            selectedIndex: selectedIndex,
            onTabSelected: onTabSelected,
            itemsSize: itemsSize
        )
    }
}

// MARK: - Internal Tabs View

private struct LemonadeTabsView: View {
    let tabs: [String]
    let selectedIndex: Int
    let onTabSelected: (Int) -> Void
    let itemsSize: TabItemsSize

    var body: some View {
        VStack(spacing: 0) {
            switch itemsSize {
            case .hug:
                ScrollView(.horizontal, showsIndicators: false) {
                    tabRow
                }
            case .stretch:
                tabRow
            }

            Rectangle()
                .fill(LemonadeTheme.colors.border.borderNeutralLow)
                .frame(height: LemonadeTheme.borderWidth.base.border25)
        }
    }

    private var tabRow: some View {
        HStack(spacing: LemonadeTheme.spaces.spacing200) {
            ForEach(Array(tabs.enumerated()), id: \.offset) { index, label in
                TabItemView(
                    label: label,
                    isSelected: index == selectedIndex,
                    stretch: itemsSize == .stretch
                ) {
                    onTabSelected(index)
                }
            }
        }
    }
}

// MARK: - Tab Item View

private struct TabItemView: View {
    let label: String
    let isSelected: Bool
    let stretch: Bool
    let onClick: () -> Void

    var body: some View {
        SwiftUI.Button(action: onClick) {
            VStack(spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodySmallMedium,
                    color: isSelected
                        ? LemonadeTheme.colors.content.contentBrand
                        : LemonadeTheme.colors.content.contentSecondary,
                    overflow: .tail,
                    maxLines: 1
                )
                .padding(.horizontal, LemonadeTheme.spaces.spacing300)
                .padding(.vertical, LemonadeTheme.spaces.spacing200)

                if isSelected {
                    Rectangle()
                        .fill(LemonadeTheme.colors.content.contentBrand)
                        .frame(height: LemonadeTheme.borderWidth.base.border50)
                }
            }
            .frame(maxWidth: stretch ? .infinity : nil)
        }
        .buttonStyle(.plain)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeTabs_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LemonadeTheme.spaces.spacing800) {
            LemonadeUi.Tabs(
                tabs: ["Overview", "Details", "Reviews"],
                selectedIndex: 1,
                onTabSelected: { _ in }
            )

            LemonadeUi.Tabs(
                tabs: ["Dashboard", "Analytics", "Reports", "Settings", "Users", "Activity"],
                selectedIndex: 2,
                onTabSelected: { _ in }
            )

            LemonadeUi.Tabs(
                tabs: ["Tab A", "Tab B", "Tab C"],
                selectedIndex: 0,
                onTabSelected: { _ in },
                itemsSize: .stretch
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
#endif
