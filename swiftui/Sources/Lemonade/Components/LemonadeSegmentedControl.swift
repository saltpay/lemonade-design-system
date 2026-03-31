import SwiftUI

// MARK: - Tab Button Properties

/// Properties for a single tab button in a segmented control.
public struct LemonadeTabButtonProperties: Identifiable, Hashable {
    public let id: String
    /// The label text for the tab. When nil, only the icon is shown.
    public let label: String?
    /// Optional icon to display before the label.
    public let icon: LemonadeIcon?

    private init(id: String? = nil, label: String?, icon: LemonadeIcon?) {
        self.id = id ?? UUID().uuidString
        self.label = label
        self.icon = icon
    }

    /// Creates a tab with a text label.
    public static func label(_ label: String) -> Self {
        Self(label: label, icon: nil)
    }

    /// Creates a tab with a text label and an icon.
    public static func labelAndIcon(_ label: String, icon: LemonadeIcon) -> Self {
        Self(label: label, icon: icon)
    }

    /// Creates a tab with only an icon.
    public static func icon(_ icon: LemonadeIcon) -> Self {
        Self(label: nil, icon: icon)
    }
}

/// Size variants for the segmented control.
public enum LemonadeSegmentedControlSize {
    case small
    case medium
    case large
}

private extension LemonadeSegmentedControlSize {
    var containerHeight: CGFloat {
        switch self {
        case .small: return LemonadeTheme.sizes.size800   // 32
        case .medium: return LemonadeTheme.sizes.size1000  // 40
        case .large: return LemonadeTheme.sizes.size1200   // 48
        }
    }

    var buttonContentGap: CGFloat {
        switch self {
        case .small: return LemonadeTheme.spaces.spacing50   // 2
        case .medium, .large: return LemonadeTheme.spaces.spacing100  // 4
        }
    }

    var textStyle: LemonadeTextStyle {
        switch self {
        case .small: return LemonadeTypography.shared.bodyXSmallMedium
        case .medium, .large: return LemonadeTypography.shared.bodySmallMedium
        }
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
    ///         .labelAndIcon("Tab 1", icon: .heart),
    ///         .labelAndIcon("Tab 2", icon: .laptop),
    ///         .label("Tab 3"),
    ///     ],
    ///     selectedTab: selectedTabIndex,
    ///     onTabSelected: { tabIndex in /* ... */ }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - properties: A list of `LemonadeTabButtonProperties` that represent the tab buttons' information.
    ///   - selectedTab: Int that indicates what is the index of the selected tab.
    ///   - size: The size of the segmented control. Defaults to `.large`.
    ///   - onTabSelected: A callback invoked when a tab is selected.
    /// - Returns: A styled SegmentedControl view
    @ViewBuilder
    static func SegmentedControl(
        properties: [LemonadeTabButtonProperties],
        selectedTab: Int,
        size: LemonadeSegmentedControlSize = .large,
        onTabSelected: @escaping (Int) -> Void
    ) -> some View {
        LemonadeSegmentedControlView(
            properties: properties,
            selectedTab: selectedTab,
            size: size,
            onTabSelected: onTabSelected
        )
    }
}

// MARK: - Internal Segmented Control View

private struct LemonadeSegmentedControlView: View {
    let properties: [LemonadeTabButtonProperties]
    let selectedTab: Int
    let size: LemonadeSegmentedControlSize
    let onTabSelected: (Int) -> Void

    private var clampedSelectedTab: Int {
        min(max(selectedTab, 0), properties.count - 1)
    }

    var body: some View {
        #if canImport(UIKit)
        ZStack {
            LemonadeNativeSegmentedControl(
                segmentLabels: properties.map { $0.label ?? $0.icon?.rawValue ?? "" },
                selectedIndex: clampedSelectedTab,
                onSelectionChanged: onTabSelected
            )

            labelsOverlay
        }
        .frame(height: size.containerHeight)
        #else
        labelsOverlay
            .frame(height: size.containerHeight)
            .background(
                RoundedRectangle(cornerRadius: LemonadeTheme.radius.radiusFull)
                    .fill(LemonadeTheme.colors.background.bgElevated)
            )
        #endif
    }

    private var labelsOverlay: some View {
        HStack(spacing: 0) {
            ForEach(properties.indices, id: \.self) { index in
                let property = properties[index]
                let tintColor = index == clampedSelectedTab
                    ? LemonadeTheme.colors.content.contentPrimary
                    : LemonadeTheme.colors.content.contentSecondary

                HStack(spacing: size.buttonContentGap) {
                    if let icon = property.icon {
                        LemonadeUi.Icon(
                            icon: icon,
                            contentDescription: property.label,
                            size: .small,
                            tint: tintColor
                        )
                    }

                    if let label = property.label {
                        LemonadeUi.Text(
                            label,
                            textStyle: size.textStyle,
                            textAlign: .center,
                            color: tintColor,
                            maxLines: 1
                        )
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .accessibilityHidden(true)
            }
        }
        .allowsHitTesting(false)
    }
}

// MARK: - Native UISegmentedControl (provides Liquid Glass on iOS 26)

#if canImport(UIKit)
import UIKit

private struct LemonadeNativeSegmentedControl: UIViewRepresentable {
    let segmentLabels: [String]
    let selectedIndex: Int
    let onSelectionChanged: (Int) -> Void

    func makeCoordinator() -> Coordinator {
        Coordinator(onSelectionChanged: onSelectionChanged)
    }

    func makeUIView(context: Context) -> UISegmentedControl {
        let control = UISegmentedControl(items: segmentLabels)
        control.selectedSegmentIndex = min(selectedIndex, segmentLabels.count - 1)
        control.backgroundColor = .clear
        control.setContentHuggingPriority(.defaultLow, for: .horizontal)
        control.setContentHuggingPriority(.defaultLow, for: .vertical)
        control.setContentCompressionResistancePriority(.defaultLow, for: .vertical)

        // Hide native text — custom SwiftUI overlay handles visuals
        let clearAttributes: [NSAttributedString.Key: Any] = [.foregroundColor: UIColor.clear]
        control.setTitleTextAttributes(clearAttributes, for: .normal)
        control.setTitleTextAttributes(clearAttributes, for: .selected)

        control.addTarget(
            context.coordinator,
            action: #selector(Coordinator.segmentChanged(_:)),
            for: .valueChanged
        )
        return control
    }

    func updateUIView(_ uiView: UISegmentedControl, context: Context) {
        context.coordinator.onSelectionChanged = onSelectionChanged

        // Reconcile segment count if properties changed
        if uiView.numberOfSegments != segmentLabels.count {
            uiView.removeAllSegments()
            for (index, label) in segmentLabels.enumerated() {
                uiView.insertSegment(withTitle: label, at: index, animated: false)
            }
            let clearAttributes: [NSAttributedString.Key: Any] = [.foregroundColor: UIColor.clear]
            uiView.setTitleTextAttributes(clearAttributes, for: .normal)
            uiView.setTitleTextAttributes(clearAttributes, for: .selected)
        }

        let clampedIndex = min(selectedIndex, segmentLabels.count - 1)
        if uiView.selectedSegmentIndex != clampedIndex {
            uiView.selectedSegmentIndex = clampedIndex
        }
    }

    class Coordinator: NSObject {
        var onSelectionChanged: (Int) -> Void

        init(onSelectionChanged: @escaping (Int) -> Void) {
            self.onSelectionChanged = onSelectionChanged
        }

        @MainActor @objc func segmentChanged(_ control: UISegmentedControl) {
            onSelectionChanged(control.selectedSegmentIndex)
        }
    }
}
#endif

// MARK: - Previews

#if DEBUG
struct LemonadeSegmentedControl_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            // Large (default)
            LemonadeUi.SegmentedControl(
                properties: [
                    .label("Tab 1"),
                    .label("Tab 2"),
                    .label("Tab 3"),
                ],
                selectedTab: 1,
                onTabSelected: { _ in }
            )

            // Medium
            LemonadeUi.SegmentedControl(
                properties: [
                    .label("Tab 1"),
                    .label("Tab 2"),
                    .label("Tab 3"),
                ],
                selectedTab: 0,
                size: .medium,
                onTabSelected: { _ in }
            )

            // Small
            LemonadeUi.SegmentedControl(
                properties: [
                    .label("Tab 1"),
                    .label("Tab 2"),
                ],
                selectedTab: 0,
                size: .small,
                onTabSelected: { _ in }
            )

            // Icon only (small)
            LemonadeUi.SegmentedControl(
                properties: [
                    .icon(.heart),
                    .icon(.star),
                    .icon(.gear),
                ],
                selectedTab: 0,
                size: .small,
                onTabSelected: { _ in }
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
