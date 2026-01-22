import SwiftUI
import Lemonade

struct BrandLogosDisplayView: View {
    @State private var searchText = ""

    private var filteredLogos: [LemonadeBrandLogo] {
        if searchText.isEmpty {
            return Array(LemonadeBrandLogo.allCases)
        }
        return LemonadeBrandLogo.allCases.filter { logo in
            logo.rawValue.localizedCaseInsensitiveContains(searchText)
        }
    }

    private let columns = [
        GridItem(.adaptive(minimum: 100), spacing: 16)
    ]

    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns, spacing: 16) {
                ForEach(filteredLogos, id: \.rawValue) { logo in
                    VStack(spacing: 8) {
                        LemonadeUi.BrandLogo(
                            logo: logo,
                            size: .xxLarge
                        )

                        Text(logo.rawValue)
                            .font(.system(size: 10))
                            .foregroundColor(.secondary)
                            .lineLimit(2)
                            .multilineTextAlignment(.center)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(height: 80)
                    .background(Color(uiColor: .secondarySystemGroupedBackground))
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                }
            }
            .padding()
        }
        .searchable(text: $searchText, prompt: "Search brand logos")
        .navigationTitle("Brand Logos (\(filteredLogos.count))")
    }
}

#Preview {
    NavigationView {
        BrandLogosDisplayView()
    }
}
