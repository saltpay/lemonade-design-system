import SwiftUI
import Lemonade

struct SelectFieldDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing600) {
                // Basic
                sectionView(title: "Basic") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: "ABC",
                        placeholderText: "Select an option"
                    )
                }

                // With Label
                sectionView(title: "With Label") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: nil,
                        placeholderText: "Select a category",
                        label: "Category"
                    )
                }

                // Filled
                sectionView(title: "Filled") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: "English",
                        label: "Language"
                    )
                }

                // With Leading Icon
                sectionView(title: "With Leading Icon") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: "Favourites",
                        label: "Collection"
                    ) {
                        LemonadeUi.Icon(
                            icon: .heart,
                            contentDescription: nil,
                            tint: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }

                // With Error
                sectionView(title: "With Error") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: nil,
                        placeholderText: "Select an option",
                        label: "Required Field",
                        errorMessage: "Please select an option",
                        error: true
                    )
                }

                // With Support Text
                sectionView(title: "With Support Text") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: nil,
                        placeholderText: "Select a country",
                        label: "Country",
                        optionalIndicator: "Optional",
                        supportText: "Choose your country of residence"
                    )
                }

                // Disabled
                sectionView(title: "Disabled") {
                    LemonadeUi.SelectField(
                        onClick: {},
                        selectedValue: "Locked value",
                        label: "Disabled Field",
                        enabled: false
                    )
                }
            }
            .padding(LemonadeTheme.spaces.spacing400)
        }
        .navigationTitle("SelectField")
    }

    private func sectionView<Content: View>(
        title: String,
        @ViewBuilder content: () -> Content
    ) -> some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            SwiftUI.Text(title)
                .font(.headline)
                .foregroundStyle(LemonadeTheme.colors.content.contentSecondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        SelectFieldDisplayView()
    }
}
