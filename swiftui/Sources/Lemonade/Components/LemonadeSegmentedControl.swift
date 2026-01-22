import SwiftUI

// MARK: - Tab Button Properties

/// Properties for a single tab button in a segmented control.
public struct LemonadeTabButtonProperties {
    /// The label text for the tab.
    public let label: String
    /// Optional icon to display before the label.
    public let icon: LemonadeIcon?

    public init(label: String, icon: LemonadeIcon? = nil) {
        self.label = label
        self.icon = icon
    }
}

// MARK: - Segmented Control Component

public extension LemonadeUi {
    /// A horizontal control used to select a single option from a set of two or more segments.
    /// Ideal for toggling between views or filtering content.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SegmentedControl(
    ///     properties: [
    ///         LemonadeTabButtonProperties(label: "Tab 1", icon: .heart),
    ///         LemonadeTabButtonProperties(label: "Tab 2", icon: .laptop),
    ///         LemonadeTabButtonProperties(label: "Tab 3"),
    ///     ],
    ///     selectedTab: selectedTabIndex,
    ///     onTabSelected: { tabIndex in /* ... */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - properties: A list of `LemonadeTabButtonProperties` that represent the tab buttons' information.
    ///   - selectedTab: Int that indicates what is the index of the selected tab.
    ///   - onTabSelected: A callback invoked when a tab is selected.
    /// - Returns: A styled SegmentedControl view
    @ViewBuilder
    static func SegmentedControl(
        properties: [LemonadeTabButtonProperties],
        selectedTab: Int,
        onTabSelected: @escaping (Int) -> Void
    ) -> some View {
        LemonadeSegmentedControlView(
            properties: properties,
            selectedTab: selectedTab,
            onTabSelected: onTabSelected
        )
    }
}

// MARK: - Internal Segmented Control View

private struct LemonadeSegmentedControlView: View {
    let properties: [LemonadeTabButtonProperties]
    let selectedTab: Int
    let onTabSelected: (Int) -> Void

    var body: some View {
        HStack(spacing: 0) {
            ForEach(Array(properties.enumerated()), id: \.offset) { index, property in
                LemonadeSegmentedTabButton(
                    property: property,
                    isSelected: index == selectedTab,
                    onTap: { onTabSelected(index) }
                )
            }
        }
        .padding(LemonadeTheme.spaces.spacing50)
        .background(
            RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius200)
                .fill(LemonadeTheme.colors.background.bgElevated)
        )
    }
}

// MARK: - Internal Tab Button

private struct LemonadeSegmentedTabButton: View {
    let property: LemonadeTabButtonProperties
    let isSelected: Bool
    let onTap: () -> Void

    @State private var isPressed = false
    @State private var isHovering = false

    private var backgroundColor: Color {
        if isSelected {
            return LemonadeTheme.colors.background.bgDefault
        } else if isPressed {
            return LemonadeTheme.colors.interaction.bgDefaultPressed
        } else if isHovering {
            return LemonadeTheme.colors.interaction.bgDefaultInteractive
        } else {
            return LemonadeTheme.colors.background.bgDefault.opacity(LemonadeTheme.opacity.base.opacity0)
        }
    }

    private var shadow: LemonadeShadow {
        isSelected ? .xsmall : .none
    }

    var body: some View {
        SwiftUI.Button(action: onTap) {
            HStack(spacing: LemonadeTheme.spaces.spacing200) {
                if let icon = property.icon {
                    LemonadeUi.Icon(
                        icon: icon,
                        contentDescription: property.label,
                        size: .small
                    )
                }

                LemonadeUi.Text(
                    property.label,
                    textStyle: LemonadeTypography.shared.bodySmallMedium
                )
                .lineLimit(1)
            }
            .frame(maxWidth: .infinity)
            .frame(minHeight: LemonadeTheme.sizes.size800)
            .frame(minWidth: LemonadeTheme.sizes.size1600)
            .padding(.vertical, LemonadeTheme.spaces.spacing100)
            .padding(.horizontal, LemonadeTheme.spaces.spacing200)
            .background(
                RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius200)
                    .fill(backgroundColor)
                    .animation(.easeInOut(duration: 0.1), value: backgroundColor)
            )
            .lemonadeShadow(shadow)
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius200))
        }
        .buttonStyle(LemonadeSegmentedButtonStyle(isPressed: $isPressed))
        .onHover { hovering in
            isHovering = hovering
        }
        .accessibilityAddTraits(.isButton)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}

// MARK: - Button Style

private struct LemonadeSegmentedButtonStyle: ButtonStyle {
    @Binding var isPressed: Bool

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .onChange(of: configuration.isPressed) { newValue in
                withAnimation(.easeInOut(duration: 0.1)) {
                    isPressed = newValue
                }
            }
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeSegmentedControl_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Basic tabs
            LemonadeUi.SegmentedControl(
                properties: [
                    LemonadeTabButtonProperties(label: "Tab 1"),
                    LemonadeTabButtonProperties(label: "Tab 2"),
                    LemonadeTabButtonProperties(label: "Tab 3"),
                ],
                selectedTab: 1,
                onTabSelected: { _ in }
            )

            // Tabs with icons
            LemonadeUi.SegmentedControl(
                properties: [
                    LemonadeTabButtonProperties(label: "Tab 1", icon: .heart),
                    LemonadeTabButtonProperties(label: "Tab 2", icon: .star),
                    LemonadeTabButtonProperties(label: "Tab 3", icon: .gear),
                ],
                selectedTab: 0,
                onTabSelected: { _ in }
            )

            // Two tabs
            LemonadeUi.SegmentedControl(
                properties: [
                    LemonadeTabButtonProperties(label: "On"),
                    LemonadeTabButtonProperties(label: "Off"),
                ],
                selectedTab: 0,
                onTabSelected: { _ in }
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
