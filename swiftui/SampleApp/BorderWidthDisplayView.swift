import SwiftUI
import Lemonade

struct BorderWidthDisplayView: View {
    private let borderWidthTokens = LemonadeBorderWidthTokens()

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                ForEach(borderWidthItems, id: \.name) { item in
                    HStack(spacing: 16) {
                        Text(item.name)
                            .font(.caption)
                            .frame(width: 120, alignment: .leading)

                        Text("\(Int(item.value))pt")
                            .font(.caption)
                            .foregroundStyle(.content.contentSecondary)
                            .frame(width: 50)

                        RoundedRectangle(cornerRadius: 8)
                            .stroke(LemonadePrimitiveColors.Solid.Blue.blue500, lineWidth: item.value)
                            .frame(width: 80, height: 60)

                        Spacer()
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Border Width")
    }
}

private struct BorderWidthItem {
    let name: String
    let value: CGFloat
}

private let borderWidthItems: [BorderWidthItem] = [
    BorderWidthItem(name: "width0", value: 0),
    BorderWidthItem(name: "width100", value: 1),
    BorderWidthItem(name: "width200", value: 2),
    BorderWidthItem(name: "width300", value: 3),
    BorderWidthItem(name: "width400", value: 4),
]

#Preview {
    NavigationStack {
        BorderWidthDisplayView()
    }
}
