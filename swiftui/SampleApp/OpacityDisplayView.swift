import SwiftUI
import Lemonade

struct OpacityDisplayView: View {
    private let opacityTokens = LemonadeOpacityTokens()

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                Text("Base Opacities")
                    .font(.headline)

                VStack(alignment: .leading, spacing: 12) {
                    ForEach(baseOpacityItems, id: \.name) { item in
                        OpacityRow(item: item)
                    }
                }

                Text("State Opacities")
                    .font(.headline)
                    .padding(.top, 8)

                VStack(alignment: .leading, spacing: 12) {
                    ForEach(stateOpacityItems, id: \.name) { item in
                        OpacityRow(item: item)
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Opacity")
    }
}

private struct OpacityRow: View {
    let item: OpacityItem

    var body: some View {
        HStack(spacing: 16) {
            Text(item.name)
                .font(.caption)
                .frame(width: 120, alignment: .leading)

            Text("\(Int(item.value * 100))%")
                .font(.caption)
                .foregroundStyle(.secondary)
                .frame(width: 50)

            Rectangle()
                .fill(LemonadePrimitiveColors.Solid.Green.green500.opacity(item.value))
                .frame(height: 40)
                .clipShape(RoundedRectangle(cornerRadius: 8))
        }
    }
}

private struct OpacityItem {
    let name: String
    let value: Double
}

private let baseOpacityItems: [OpacityItem] = [
    OpacityItem(name: "opacity0", value: 0.0),
    OpacityItem(name: "opacity5", value: 0.05),
    OpacityItem(name: "opacity10", value: 0.10),
    OpacityItem(name: "opacity20", value: 0.20),
    OpacityItem(name: "opacity30", value: 0.30),
    OpacityItem(name: "opacity40", value: 0.40),
    OpacityItem(name: "opacity50", value: 0.50),
    OpacityItem(name: "opacity60", value: 0.60),
    OpacityItem(name: "opacity70", value: 0.70),
    OpacityItem(name: "opacity80", value: 0.80),
    OpacityItem(name: "opacity90", value: 0.90),
    OpacityItem(name: "opacity100", value: 1.0),
]

private let stateOpacityItems: [OpacityItem] = [
    OpacityItem(name: "opacityPressed", value: 0.20),
    OpacityItem(name: "opacityDisabled", value: 0.40),
]

#Preview {
    NavigationStack {
        OpacityDisplayView()
    }
}
