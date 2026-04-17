import SwiftUI
import Lemonade

struct TextDisplayView: View {
    private let categorizedStyles: [(category: String, groups: [[TypographyEntry]])] = {
        let entries = Mirror(reflecting: LemonadeTypography.shared)
            .children
            .compactMap { child -> TypographyEntry? in
                guard let style = child.value as? LemonadeTextStyle,
                      let name = child.label
                else { return nil }
                return TypographyEntry(label: name.typographyDisplayLabel(), style: style)
            }
            .sorted { $0.style.fontSize > $1.style.fontSize }

        let byCategory = Dictionary(grouping: entries) { $0.category }
        let categoryOrder = ["Display", "Heading", "Body"]
        let orderedCategories = byCategory.keys.sorted { a, b in
            let ia = categoryOrder.firstIndex(of: a) ?? categoryOrder.count
            let ib = categoryOrder.firstIndex(of: b) ?? categoryOrder.count
            return ia < ib
        }

        return orderedCategories.compactMap { category in
            guard let styles = byCategory[category] else { return nil }
            let groups = Dictionary(grouping: styles) { $0.subCategory ?? "" 
                .sorted { a, b in
                    // preserve size-descending order by taking the max fontSize in each group
                    let maxA = a.value.map(\.style.fontSize).max() ?? 0
                    let maxB = b.value.map(\.style.fontSize).max() ?? 0
                    return maxA > maxB
                }
                .map(\.value)
            return (category: category, groups: groups)
        }
    }()

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                ForEach(categorizedStyles, id: \.category) { section in
                    sectionView(title: section.category) {
                        VStack(alignment: .leading, spacing: 12) {
                            ForEach(section.groups.indices, id: \.self) { index in
                                if index > 0 { Divider() }
                                ForEach(section.groups[index]) { entry in
                                    LemonadeUi.Text(entry.label, textStyle: entry.style)
                                }
                            }
                        }
                    }
                }

                // Text Colors — not part of typography struct, kept manual
                sectionView(title: "Colors") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("Primary", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentPrimary)
                        LemonadeUi.Text("Secondary", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentSecondary)
                        LemonadeUi.Text("Tertiary", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentTertiary)
                        LemonadeUi.Text("Critical", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentCritical)
                        LemonadeUi.Text("Positive", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentPositive)
                        LemonadeUi.Text("Info", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentInfo)
                    }
                }

                // Overflow — behavioural examples, kept manual
                sectionView(title: "Overflow") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text(
                            "This is a very long text that will be truncated at the end with ellipsis because it exceeds the available width",
                            textStyle: LemonadeTypography.shared.bodyMediumRegular,
                            overflow: .tail,
                            maxLines: 1
                        )
                        LemonadeUi.Text(
                            "This text truncates in the middle when it exceeds the available width in the container",
                            textStyle: LemonadeTypography.shared.bodyMediumRegular,
                            overflow: .middle,
                            maxLines: 1
                        )
                        LemonadeUi.Text(
                            "This text allows multiple lines but is limited to 2 lines maximum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore.",
                            textStyle: LemonadeTypography.shared.bodyMediumRegular,
                            maxLines: 2
                        )
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Text")
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

// MARK: - Helpers

private struct TypographyEntry: Identifiable {
    var id: String { label }
    let label: String
    let style: LemonadeTextStyle

    var category: String {
        label.components(separatedBy: " ").first ?? ""
    }

    // Returns nil for Display and Heading, which have no weight variants per size.
    var subCategory: String? {
        let parts = label.components(separatedBy: " ")
        return parts.count > 2 ? parts[1] : nil
    }
}

private extension String {
    func typographyDisplayLabel() -> String {
        let spaced = replacingOccurrences(
            of: "([a-z])([A-Z0-9])",
            with: "$1 $2",
            options: .regularExpression
        )
        return spaced.prefix(1).uppercased() + spaced.dropFirst()
    }
}

#Preview {
    NavigationStack {
        TextDisplayView()
    }
}
