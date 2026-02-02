import SwiftUI
import Lemonade

struct FlagsDisplayView: View {
    @State private var searchText = ""

    private var filteredFlags: [LemonadeCountryFlag] {
        if searchText.isEmpty {
            return Array(LemonadeCountryFlag.allCases)
        }
        return LemonadeCountryFlag.allCases.filter { flag in
            flag.rawValue.localizedCaseInsensitiveContains(searchText) ||
            flag.countryCode.localizedCaseInsensitiveContains(searchText) ||
            flag.countryName.localizedCaseInsensitiveContains(searchText)
        }
    }

    private let columns = [
        GridItem(.adaptive(minimum: 80), spacing: 12)
    ]

    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns, spacing: 12) {
                ForEach(filteredFlags, id: \.rawValue) { flag in
                    VStack(spacing: 6) {
                        LemonadeUi.CountryFlag(
                            flag: flag,
                            size: .xxLarge
                        )

                        Text(flag.countryCode)
                            .font(.system(size: 11, weight: .semibold))
                            .foregroundStyle(.primary)

                        Text(flag.countryName)
                            .font(.system(size: 8))
                            .foregroundStyle(.content.contentSecondary)
                            .lineLimit(2)
                            .multilineTextAlignment(.center)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(height: 90)
                    .background(.bg.bgSubtle)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                }
            }
            .padding()
        }
        .searchable(text: $searchText, prompt: "Search by code or country name")
        .navigationTitle("Country Flags (\(filteredFlags.count))")
    }
}

#Preview {
    NavigationStack {
        FlagsDisplayView()
    }
}
