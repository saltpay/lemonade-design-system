import SwiftUI
import Lemonade

struct FlagsDisplayView: View {
    @State private var searchText = ""
    @State private var shape: LemonadeCountryFlagShape = .circular

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

    private let sizes: [(size: LemonadeCountryFlagSize, label: String)] = [
        (.small, "S"),
        (.medium, "M"),
        (.large, "L"),
        (.xLarge, "XL"),
        (.xxLarge, "2XL"),
        (.xxxLarge, "3XL"),
        (.xxxxLarge, "4XL")
    ]

    private var allSizesRow: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("All sizes")
                .font(.system(size: 11, weight: .semibold))
                .foregroundStyle(.content.contentSecondary)

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(alignment: .bottom, spacing: 12) {
                    ForEach(sizes, id: \.size) { item in
                        VStack(spacing: 4) {
                            LemonadeUi.CountryFlag(
                                flag: .gBUnitedKingdom,
                                size: item.size,
                                shape: shape
                            )

                            Text(item.label)
                                .font(.system(size: 9))
                                .foregroundStyle(.content.contentSecondary)
                        }
                    }
                }
            }
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(.bg.bgSubtle)
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }

    var body: some View {
        VStack(spacing: 0) {
            Picker("Shape", selection: $shape) {
                Text("Circular").tag(LemonadeCountryFlagShape.circular)
                Text("Rounded").tag(LemonadeCountryFlagShape.rounded)
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)
            .padding(.top)

            ScrollView {
                allSizesRow
                    .padding(.horizontal)
                    .padding(.top)

                LazyVGrid(columns: columns, spacing: 12) {
                    ForEach(filteredFlags, id: \.rawValue) { flag in
                        VStack(spacing: 6) {
                            LemonadeUi.CountryFlag(
                                flag: flag,
                                size: .xxLarge,
                                shape: shape
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
