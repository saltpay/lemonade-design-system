import SwiftUI
import Lemonade

struct SpacingDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                ForEach(spacingItems, id: \.name) { item in
                    HStack(spacing: 16) {
                        Text(item.name)
                            .font(.caption)
                            .frame(width: 100, alignment: .leading)

                        Text("\(Int(item.value))pt")
                            .font(.caption)
                            .foregroundColor(.secondary)
                            .frame(width: 50)

                        Rectangle()
                            .fill(LemonadePrimitiveColors.Solid.Green.green500)
                            .frame(width: item.value, height: 24)
                            .clipShape(RoundedRectangle(cornerRadius: 4))

                        Spacer()
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Spacing")
    }
}

private struct SpacingItem {
    let name: String
    let value: CGFloat
}

private let spacingItems: [SpacingItem] = [
    SpacingItem(name: "spacing0", value: LemonadeSpacing.spacing0.value),
    SpacingItem(name: "spacing50", value: LemonadeSpacing.spacing50.value),
    SpacingItem(name: "spacing100", value: LemonadeSpacing.spacing100.value),
    SpacingItem(name: "spacing200", value: LemonadeSpacing.spacing200.value),
    SpacingItem(name: "spacing300", value: LemonadeSpacing.spacing300.value),
    SpacingItem(name: "spacing400", value: LemonadeSpacing.spacing400.value),
    SpacingItem(name: "spacing500", value: LemonadeSpacing.spacing500.value),
    SpacingItem(name: "spacing600", value: LemonadeSpacing.spacing600.value),
    SpacingItem(name: "spacing800", value: LemonadeSpacing.spacing800.value),
    SpacingItem(name: "spacing1000", value: LemonadeSpacing.spacing1000.value),
    SpacingItem(name: "spacing1200", value: LemonadeSpacing.spacing1200.value),
    SpacingItem(name: "spacing1400", value: LemonadeSpacing.spacing1400.value),
    SpacingItem(name: "spacing1600", value: LemonadeSpacing.spacing1600.value),
    SpacingItem(name: "spacing1800", value: LemonadeSpacing.spacing1800.value),
    SpacingItem(name: "spacing2000", value: LemonadeSpacing.spacing2000.value),
]

#Preview {
    NavigationView {
        SpacingDisplayView()
    }
}
