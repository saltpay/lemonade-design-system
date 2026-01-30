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
                            .foregroundStyle(.secondary)
                            .textCase(.uppercase)

                        HStack(spacing: 0) {
                            ForEach(group.colors, id: \.name) { colorItem in
                                VStack {
                                    Text(colorItem.name)
                                        .font(.caption2)
                                        .fontWeight(.semibold)
                                        .foregroundStyle(textColor(for: colorItem.color))
                                }
                                .frame(maxWidth: .infinity)
                                .frame(height: 60)
                                .background(colorItem.color)
                            }
                        }
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Colors")
    }

    private func textColor(for backgroundColor: Color) -> Color {
        // Simple luminance check
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
    ColorGroup(title: "Yellow", colors: [
        ColorItem(name: "50", color: LemonadePrimitiveColors.Solid.Yellow.yellow50),
        ColorItem(name: "100", color: LemonadePrimitiveColors.Solid.Yellow.yellow100),
        ColorItem(name: "200", color: LemonadePrimitiveColors.Solid.Yellow.yellow200),
        ColorItem(name: "300", color: LemonadePrimitiveColors.Solid.Yellow.yellow300),
        ColorItem(name: "400", color: LemonadePrimitiveColors.Solid.Yellow.yellow400),
        ColorItem(name: "500", color: LemonadePrimitiveColors.Solid.Yellow.yellow500),
        ColorItem(name: "600", color: LemonadePrimitiveColors.Solid.Yellow.yellow600),
        ColorItem(name: "700", color: LemonadePrimitiveColors.Solid.Yellow.yellow700),
        ColorItem(name: "800", color: LemonadePrimitiveColors.Solid.Yellow.yellow800),
        ColorItem(name: "900", color: LemonadePrimitiveColors.Solid.Yellow.yellow900),
    ]),
    ColorGroup(title: "Green", colors: [
        ColorItem(name: "50", color: LemonadePrimitiveColors.Solid.Green.green50),
        ColorItem(name: "100", color: LemonadePrimitiveColors.Solid.Green.green100),
        ColorItem(name: "200", color: LemonadePrimitiveColors.Solid.Green.green200),
        ColorItem(name: "300", color: LemonadePrimitiveColors.Solid.Green.green300),
        ColorItem(name: "400", color: LemonadePrimitiveColors.Solid.Green.green400),
        ColorItem(name: "500", color: LemonadePrimitiveColors.Solid.Green.green500),
        ColorItem(name: "600", color: LemonadePrimitiveColors.Solid.Green.green600),
        ColorItem(name: "700", color: LemonadePrimitiveColors.Solid.Green.green700),
        ColorItem(name: "800", color: LemonadePrimitiveColors.Solid.Green.green800),
        ColorItem(name: "900", color: LemonadePrimitiveColors.Solid.Green.green900),
    ]),
    ColorGroup(title: "Blue", colors: [
        ColorItem(name: "50", color: LemonadePrimitiveColors.Solid.Blue.blue50),
        ColorItem(name: "100", color: LemonadePrimitiveColors.Solid.Blue.blue100),
        ColorItem(name: "200", color: LemonadePrimitiveColors.Solid.Blue.blue200),
        ColorItem(name: "300", color: LemonadePrimitiveColors.Solid.Blue.blue300),
        ColorItem(name: "400", color: LemonadePrimitiveColors.Solid.Blue.blue400),
        ColorItem(name: "500", color: LemonadePrimitiveColors.Solid.Blue.blue500),
        ColorItem(name: "600", color: LemonadePrimitiveColors.Solid.Blue.blue600),
        ColorItem(name: "700", color: LemonadePrimitiveColors.Solid.Blue.blue700),
        ColorItem(name: "800", color: LemonadePrimitiveColors.Solid.Blue.blue800),
        ColorItem(name: "900", color: LemonadePrimitiveColors.Solid.Blue.blue900),
    ]),
    ColorGroup(title: "Red", colors: [
        ColorItem(name: "50", color: LemonadePrimitiveColors.Solid.Red.red50),
        ColorItem(name: "100", color: LemonadePrimitiveColors.Solid.Red.red100),
        ColorItem(name: "200", color: LemonadePrimitiveColors.Solid.Red.red200),
        ColorItem(name: "300", color: LemonadePrimitiveColors.Solid.Red.red300),
        ColorItem(name: "400", color: LemonadePrimitiveColors.Solid.Red.red400),
        ColorItem(name: "500", color: LemonadePrimitiveColors.Solid.Red.red500),
        ColorItem(name: "600", color: LemonadePrimitiveColors.Solid.Red.red600),
        ColorItem(name: "700", color: LemonadePrimitiveColors.Solid.Red.red700),
        ColorItem(name: "800", color: LemonadePrimitiveColors.Solid.Red.red800),
        ColorItem(name: "900", color: LemonadePrimitiveColors.Solid.Red.red900),
    ]),
    ColorGroup(title: "Neutral", colors: [
        ColorItem(name: "50", color: LemonadePrimitiveColors.Solid.Neutral.neutral50),
        ColorItem(name: "100", color: LemonadePrimitiveColors.Solid.Neutral.neutral100),
        ColorItem(name: "200", color: LemonadePrimitiveColors.Solid.Neutral.neutral200),
        ColorItem(name: "300", color: LemonadePrimitiveColors.Solid.Neutral.neutral300),
        ColorItem(name: "400", color: LemonadePrimitiveColors.Solid.Neutral.neutral400),
        ColorItem(name: "500", color: LemonadePrimitiveColors.Solid.Neutral.neutral500),
        ColorItem(name: "600", color: LemonadePrimitiveColors.Solid.Neutral.neutral600),
        ColorItem(name: "700", color: LemonadePrimitiveColors.Solid.Neutral.neutral700),
        ColorItem(name: "800", color: LemonadePrimitiveColors.Solid.Neutral.neutral800),
        ColorItem(name: "900", color: LemonadePrimitiveColors.Solid.Neutral.neutral900),
    ]),
]

#Preview {
    NavigationStack {
        ColorsDisplayView()
    }
}
