import SwiftUI
import Lemonade

struct TextDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Display Styles
                sectionView(title: "Display") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.Text("Display Large", textStyle: LemonadeTypography.shared.displayLarge)
                        LemonadeUi.Text("Display Medium", textStyle: LemonadeTypography.shared.displayMedium)
                        LemonadeUi.Text("Display Small", textStyle: LemonadeTypography.shared.displaySmall)
                    }
                }

                // Heading Styles
                sectionView(title: "Heading") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("Heading XLarge", textStyle: LemonadeTypography.shared.headingXLarge)
                        LemonadeUi.Text("Heading Large", textStyle: LemonadeTypography.shared.headingLarge)
                        LemonadeUi.Text("Heading Medium", textStyle: LemonadeTypography.shared.headingMedium)
                        LemonadeUi.Text("Heading Small", textStyle: LemonadeTypography.shared.headingSmall)
                        LemonadeUi.Text("Heading XSmall", textStyle: LemonadeTypography.shared.headingXSmall)
                        LemonadeUi.Text("Heading XXSmall", textStyle: LemonadeTypography.shared.headingXXSmall)
                    }
                }

                // Body Styles
                sectionView(title: "Body") {
                    VStack(alignment: .leading, spacing: 12) {
                        Group {
                            LemonadeUi.Text("Body XLarge Regular", textStyle: LemonadeTypography.shared.bodyXLargeRegular)
                            LemonadeUi.Text("Body XLarge Medium", textStyle: LemonadeTypography.shared.bodyXLargeMedium)
                            LemonadeUi.Text("Body XLarge SemiBold", textStyle: LemonadeTypography.shared.bodyXLargeSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body Large Regular", textStyle: LemonadeTypography.shared.bodyLargeRegular)
                            LemonadeUi.Text("Body Large Medium", textStyle: LemonadeTypography.shared.bodyLargeMedium)
                            LemonadeUi.Text("Body Large SemiBold", textStyle: LemonadeTypography.shared.bodyLargeSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body Medium Regular", textStyle: LemonadeTypography.shared.bodyMediumRegular)
                            LemonadeUi.Text("Body Medium Medium", textStyle: LemonadeTypography.shared.bodyMediumMedium)
                            LemonadeUi.Text("Body Medium SemiBold", textStyle: LemonadeTypography.shared.bodyMediumSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body Small Regular", textStyle: LemonadeTypography.shared.bodySmallRegular)
                            LemonadeUi.Text("Body Small Medium", textStyle: LemonadeTypography.shared.bodySmallMedium)
                            LemonadeUi.Text("Body Small SemiBold", textStyle: LemonadeTypography.shared.bodySmallSemiBold)
                        }

                        Divider()

                        Group {
                            LemonadeUi.Text("Body XSmall Regular", textStyle: LemonadeTypography.shared.bodyXSmallRegular)
                            LemonadeUi.Text("Body XSmall Medium", textStyle: LemonadeTypography.shared.bodyXSmallMedium)
                            LemonadeUi.Text("Body XSmall SemiBold", textStyle: LemonadeTypography.shared.bodyXSmallSemiBold)
                        }
                    }
                }

                // Text Colors
                sectionView(title: "Colors") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("Primary", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentPrimary)
                        LemonadeUi.Text("Secondary", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentSecondary)
                        LemonadeUi.Text("Tertiary", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentTertiary)
                        LemonadeUi.Text("Critical", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentCritical)
                        LemonadeUi.Text("Positive", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentPositive)
                        LemonadeUi.Text("Info", textStyle: LemonadeTypography.shared.bodyMediumRegular, color: LemonadeTheme.colors.content.contentInfo)
                    }
                }

                // Overflow
                sectionView(title: "Overflow") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Text("This is a very long text that will be truncated at the end with ellipsis because it exceeds the available width", textStyle: LemonadeTypography.shared.bodyMediumRegular, overflow: .tail, maxLines: 1)

                        LemonadeUi.Text("This text truncates in the middle when it exceeds the available width in the container", textStyle: LemonadeTypography.shared.bodyMediumRegular, overflow: .middle, maxLines: 1)

                        LemonadeUi.Text("This text allows multiple lines but is limited to 2 lines maximum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore.", textStyle: LemonadeTypography.shared.bodyMediumRegular, maxLines: 2)
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
                .foregroundStyle(.secondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        TextDisplayView()
    }
}
