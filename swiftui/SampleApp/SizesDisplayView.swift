import SwiftUI
import Lemonade

struct SizesDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                ForEach(sizeItems, id: \.name) { item in
                    HStack(spacing: 16) {
                        Text(item.name)
                            .font(.caption)
                            .frame(width: 80, alignment: .leading)

                        Text("\(Int(item.value))pt")
                            .font(.caption)
                            .foregroundColor(.secondary)
                            .frame(width: 50)

                        Circle()
                            .fill(LemonadePrimitiveColors.Solid.Purple.purple500)
                            .frame(width: min(item.value, 100), height: min(item.value, 100))

                        Spacer()
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Sizes")
    }
}

private struct SizeItem {
    let name: String
    let value: CGFloat
}

private let sizeItems: [SizeItem] = [
    SizeItem(name: "size100", value: LemonadeSizes.size100.value),
    SizeItem(name: "size200", value: LemonadeSizes.size200.value),
    SizeItem(name: "size300", value: LemonadeSizes.size300.value),
    SizeItem(name: "size400", value: LemonadeSizes.size400.value),
    SizeItem(name: "size500", value: LemonadeSizes.size500.value),
    SizeItem(name: "size600", value: LemonadeSizes.size600.value),
    SizeItem(name: "size700", value: LemonadeSizes.size700.value),
    SizeItem(name: "size800", value: LemonadeSizes.size800.value),
    SizeItem(name: "size900", value: LemonadeSizes.size900.value),
    SizeItem(name: "size1000", value: LemonadeSizes.size1000.value),
    SizeItem(name: "size1100", value: LemonadeSizes.size1100.value),
    SizeItem(name: "size1200", value: LemonadeSizes.size1200.value),
]

#Preview {
    NavigationView {
        SizesDisplayView()
    }
}
