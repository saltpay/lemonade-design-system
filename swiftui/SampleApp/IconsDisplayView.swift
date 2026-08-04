import SwiftUI
import Lemonade

/// All ~280 icons, each paired with a pre-lowercased search haystack.
///
/// Built once per process at file scope so a keystroke costs a plain `contains`
/// per case instead of re-deriving the haystack (and paying for
/// `localizedCaseInsensitiveContains`, which bridges to ICU on every call).
private let iconIndex: [(icon: LemonadeIcon, haystack: String)] =
    LemonadeIcon.allCases.map { ($0, $0.rawValue.lowercased()) }

private let allIcons: [LemonadeIcon] = iconIndex.map(\.icon)

private func filteredIcons(matching searchText: String) -> [LemonadeIcon] {
    guard !searchText.isEmpty else { return allIcons }
    let query = searchText.lowercased()
    return iconIndex.compactMap { $0.haystack.contains(query) ? $0.icon : nil }
}

struct IconsDisplayView: View {
    @State private var searchText = ""

    private let columns = [
        GridItem(.adaptive(minimum: 80), spacing: 16)
    ]

    var body: some View {
        // Evaluated once per body pass and reused by both the grid and the title.
        let icons = filteredIcons(matching: searchText)

        return ScrollView {
            LazyVGrid(columns: columns, spacing: 16) {
                ForEach(icons, id: \.rawValue) { icon in
                    VStack(spacing: 8) {
                        icon.image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24, height: 24)
                            .foregroundStyle(.primary)

                        Text(icon.rawValue)
                            .font(.system(size: 8))
                            .foregroundStyle(.content.contentSecondary)
                            .lineLimit(2)
                            .multilineTextAlignment(.center)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(height: 80)
                    .background(.bg.bgSubtle)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                }
            }
            .padding()
        }
        .searchable(text: $searchText, prompt: "Search icons")
        .navigationTitle("Icons (\(icons.count))")
    }
}

#Preview {
    NavigationStack {
        IconsDisplayView()
    }
}
