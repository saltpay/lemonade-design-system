import SwiftUI
import Lemonade

struct ShadowsDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 48) {
                ForEach(shadowItems, id: \.name) { item in
                    VStack(alignment: .leading, spacing: 8) {
                        Text(item.name)
                            .font(.caption)
                            .foregroundStyle(.secondary)

                        RoundedRectangle(cornerRadius: 12)
                            .fill(.white)
                            .frame(height: 100)
                            .lemonadeShadow(item.shadow)
                    }
                }
            }
            .padding(32)
        }
        .background(Color(uiColor: .systemGroupedBackground))
        .navigationTitle("Shadows")
    }
}

private struct ShadowItem {
    let name: String
    let shadow: LemonadeShadow
}

private let shadowItems: [ShadowItem] = [
    ShadowItem(name: "None", shadow: .none),
    ShadowItem(name: "X-Small", shadow: .xsmall),
    ShadowItem(name: "Small", shadow: .small),
    ShadowItem(name: "Medium", shadow: .medium),
    ShadowItem(name: "Large", shadow: .large),
]

#Preview {
    NavigationStack {
        ShadowsDisplayView()
    }
}
