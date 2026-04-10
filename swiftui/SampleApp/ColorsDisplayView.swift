import SwiftUI
import Lemonade

struct ColorsDisplayView: View {
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 24) {
                ForEach(colorGroups, id: \.title) { group in
                    VStack(alignment: .leading, spacing: 8) {
                        Text(group.title)
                            .font(.caption)
                            .foregroundStyle(.content.contentSecondary)
                            .textCase(.uppercase)

                        VStack(spacing: 0) {
                            ForEach(group.colors, id: \.name) { colorItem in
                                HStack {
                                    Text(colorItem.name)
                                        .font(.caption2)
                                        .fontWeight(.semibold)
                                        .foregroundStyle(textColor(for: colorItem.color))
                                    Spacer()
                                }
                                .padding(.horizontal, 12)
                                .frame(height: 40)
                                .background(colorItem.color)
                            }
                        }
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Semantic Colors")
    }

    private func textColor(for backgroundColor: Color) -> Color {
        return .primary
    }
}

// MARK: - Color Data

struct ColorItem: Identifiable {
    let id = UUID()
    let name: String
    let color: Color
}

struct ColorGroup: Identifiable {
    let id = UUID()
    let title: String
    let colors: [ColorItem]
}

private let colorGroups: [ColorGroup] = [
    ColorGroup(title: "Background", colors: [
        ColorItem(name: "bgDefault", color: .bg.bgDefault),
        ColorItem(name: "bgSubtle", color: .bg.bgSubtle),
        ColorItem(name: "bgElevated", color: .bg.bgElevated),
        ColorItem(name: "bgElevatedHigh", color: .bg.bgElevatedHigh),
        ColorItem(name: "bgBrand", color: .bg.bgBrand),
        ColorItem(name: "bgBrandHigh", color: .bg.bgBrandHigh),
        ColorItem(name: "bgBrandSubtle", color: .bg.bgBrandSubtle),
        ColorItem(name: "bgPositive", color: .bg.bgPositive),
        ColorItem(name: "bgPositiveSubtle", color: .bg.bgPositiveSubtle),
        ColorItem(name: "bgCritical", color: .bg.bgCritical),
        ColorItem(name: "bgCriticalSubtle", color: .bg.bgCriticalSubtle),
        ColorItem(name: "bgCaution", color: .bg.bgCaution),
        ColorItem(name: "bgCautionSubtle", color: .bg.bgCautionSubtle),
        ColorItem(name: "bgInfo", color: .bg.bgInfo),
        ColorItem(name: "bgInfoSubtle", color: .bg.bgInfoSubtle),
        ColorItem(name: "bgNeutral", color: .bg.bgNeutral),
        ColorItem(name: "bgNeutralSubtle", color: .bg.bgNeutralSubtle),
    ]),
    ColorGroup(title: "Content", colors: [
        ColorItem(name: "contentPrimary", color: .content.contentPrimary),
        ColorItem(name: "contentSecondary", color: .content.contentSecondary),
        ColorItem(name: "contentTertiary", color: .content.contentTertiary),
        ColorItem(name: "contentBrand", color: .content.contentBrand),
        ColorItem(name: "contentBrandHigh", color: .content.contentBrandHigh),
        ColorItem(name: "contentPositive", color: .content.contentPositive),
        ColorItem(name: "contentCritical", color: .content.contentCritical),
        ColorItem(name: "contentCaution", color: .content.contentCaution),
        ColorItem(name: "contentInfo", color: .content.contentInfo),
        ColorItem(name: "contentNeutral", color: .content.contentNeutral),
    ]),
    ColorGroup(title: "Border", colors: [
        ColorItem(name: "borderNeutralLow", color: .border.borderNeutralLow),
        ColorItem(name: "borderNeutralMedium", color: .border.borderNeutralMedium),
        ColorItem(name: "borderNeutralHigh", color: .border.borderNeutralHigh),
        ColorItem(name: "borderBrand", color: .border.borderBrand),
        ColorItem(name: "borderSelected", color: .border.borderSelected),
        ColorItem(name: "borderPositive", color: .border.borderPositive),
        ColorItem(name: "borderCritical", color: .border.borderCritical),
        ColorItem(name: "borderCaution", color: .border.borderCaution),
        ColorItem(name: "borderInfo", color: .border.borderInfo),
    ]),
]

#Preview {
    NavigationStack {
        ColorsDisplayView()
    }
}
