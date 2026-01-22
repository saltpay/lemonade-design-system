import SwiftUI
import Lemonade

struct TextDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Display Styles
                sectionView(title: "Display") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.Text("Display Large", textStyle: LemonadeTypography().displayLarge)
                        LemonadeUi.Text("Display Medium", textStyle: LemonadeTypography().displayMedium)
                        LemonadeUi.Text("Display Small", textStyle: LemonadeTypography().displaySmall)
                    }
                }

                // Heading Styles
                sectionView(title: "Heading") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("Heading XLarge", textStyle: LemonadeTypography().headingXLarge)
                        LemonadeUi.Text("Heading Large", textStyle: LemonadeTypography().headingLarge)
                        LemonadeUi.Text("Heading Medium", textStyle: LemonadeTypography().headingMedium)
                        LemonadeUi.Text("Heading Small", textStyle: LemonadeTypography().headingSmall)
                        LemonadeUi.Text("Heading XSmall", textStyle: LemonadeTypography().headingXSmall)
                        LemonadeUi.Text("Heading XXSmall", textStyle: LemonadeTypography().headingXXSmall)
                    }
                }

                // Body Styles
                sectionView(title: "Body") {
                    VStack(alignment: .leading, spacing: 12) {
                        Group {
                            LemonadeUi.Text("Body XLarge Regular", textStyle: LemonadeTypography().bodyXLargeRegular)
                            LemonadeUi.Text("Body XLarge Medium", textStyle: LemonadeTypography().bodyXLargeMedium)
                            LemonadeUi.Text("Body XLarge SemiBold", textStyle: LemonadeTypography().bodyXLargeSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body Large Regular", textStyle: LemonadeTypography().bodyLargeRegular)
                            LemonadeUi.Text("Body Large Medium", textStyle: LemonadeTypography().bodyLargeMedium)
                            LemonadeUi.Text("Body Large SemiBold", textStyle: LemonadeTypography().bodyLargeSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body Medium Regular", textStyle: LemonadeTypography().bodyMediumRegular)
                            LemonadeUi.Text("Body Medium Medium", textStyle: LemonadeTypography().bodyMediumMedium)
                            LemonadeUi.Text("Body Medium SemiBold", textStyle: LemonadeTypography().bodyMediumSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body Small Regular", textStyle: LemonadeTypography().bodySmallRegular)
                            LemonadeUi.Text("Body Small Medium", textStyle: LemonadeTypography().bodySmallMedium)
                            LemonadeUi.Text("Body Small SemiBold", textStyle: LemonadeTypography().bodySmallSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body XSmall Regular", textStyle: LemonadeTypography().bodyXSmallRegular)
                            LemonadeUi.Text("Body XSmall Medium", textStyle: LemonadeTypography().bodyXSmallMedium)
                            LemonadeUi.Text("Body XSmall SemiBold", textStyle: LemonadeTypography().bodyXSmallSemiBold)
                        }
                    }
                }

                // Text Colors
                sectionView(title: "Colors") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("Primary", textStyle: LemonadeTypography().bodyMediumRegular, color: LemonadeTheme.colors.content.contentPrimary)
                        LemonadeUi.Text("Secondary", textStyle: LemonadeTypography().bodyMediumRegular, color: LemonadeTheme.colors.content.contentSecondary)
                        LemonadeUi.Text("Tertiary", textStyle: LemonadeTypography().bodyMediumRegular, color: LemonadeTheme.colors.content.contentTertiary)
                        LemonadeUi.Text("Critical", textStyle: LemonadeTypography().bodyMediumRegular, color: LemonadeTheme.colors.content.contentCritical)
                        LemonadeUi.Text("Positive", textStyle: LemonadeTypography().bodyMediumRegular, color: LemonadeTheme.colors.content.contentPositive)
                        LemonadeUi.Text("Info", textStyle: LemonadeTypography().bodyMediumRegular, color: LemonadeTheme.colors.content.contentInfo)
                    }
                }

                // Overflow
                sectionView(title: "Overflow") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("This is a very long text that will be truncated at the end with ellipsis because it exceeds the available width", textStyle: LemonadeTypography().bodyMediumRegular, overflow: .tail, maxLines: 1)

                        LemonadeUi.Text("This text truncates in the middle when it exceeds the available width in the container", textStyle: LemonadeTypography().bodyMediumRegular, overflow: .middle, maxLines: 1)

                        LemonadeUi.Text("This text allows multiple lines but is limited to 2 lines maximum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore.", textStyle: LemonadeTypography().bodyMediumRegular, maxLines: 2)
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Text")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
                .foregroundColor(.secondary)

            content()
        }
    }
}

#Preview {
    NavigationView {
        TextDisplayView()
    }
}
