import SwiftUI
import Lemonade

struct RadiusDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                ForEach(radiusItems, id: \.name) { item in
                    HStack(spacing: 16) {
                        Text(item.name)
                            .font(.caption)
                            .frame(width: 100, alignment: .leading)

                        Text("\(Int(item.value))pt")
                            .font(.caption)
                            .foregroundColor(.secondary)
                            .frame(width: 50)

                        RoundedRectangle(cornerRadius: item.value)
                            .fill(LemonadePrimitiveColors.Solid.Blue.blue500)
                            .frame(width: 80, height: 80)

                        Spacer()
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Radius")
    }
}

private struct RadiusItem {
    let name: String
    let value: CGFloat
}

private let radiusItems: [RadiusItem] = [
    RadiusItem(name: "radius0", value: LemonadeRadius.radius0.value),
    RadiusItem(name: "radius50", value: LemonadeRadius.radius50.value),
    RadiusItem(name: "radius100", value: LemonadeRadius.radius100.value),
    RadiusItem(name: "radius150", value: LemonadeRadius.radius150.value),
    RadiusItem(name: "radius200", value: LemonadeRadius.radius200.value),
    RadiusItem(name: "radius300", value: LemonadeRadius.radius300.value),
    RadiusItem(name: "radius400", value: LemonadeRadius.radius400.value),
    RadiusItem(name: "radius500", value: LemonadeRadius.radius500.value),
    RadiusItem(name: "radius600", value: LemonadeRadius.radius600.value),
    RadiusItem(name: "radius800", value: LemonadeRadius.radius800.value),
    RadiusItem(name: "radiusFull", value: LemonadeRadius.radiusFull.value),
]

#Preview {
    NavigationView {
        RadiusDisplayView()
    }
}
