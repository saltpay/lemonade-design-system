import SwiftUI
import Lemonade

struct IconsDisplayView: View {
    @State private var searchText = ""

    private var filteredIcons: [LemonadeIcon] {
        if searchText.isEmpty {
            return Array(LemonadeIcon.allCases)
        }
        return LemonadeIcon.allCases.filter { icon in
            icon.rawValue.localizedCaseInsensitiveContains(searchText)
        }
    }

    private let columns = [
        GridItem(.adaptive(minimum: 80), spacing: 16)
    ]

    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns, spacing: 16) {
                ForEach(filteredIcons, id: \.rawValue) { icon in
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
        .navigationTitle("Icons (\(filteredIcons.count))")
    }
}

#Preview {
    NavigationStack {
        IconsDisplayView()
    }
}
