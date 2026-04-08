import SwiftUI
import Lemonade

struct CardDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Background Variants
                sectionView(title: "Backgrounds") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(contentPadding: .medium, background: .default) {
                            LemonadeUi.Text("Default")
                        }

                        LemonadeUi.Card(contentPadding: .medium, background: .subtle) {
                            LemonadeUi.Text("Subtle")
                        }

                        LemonadeUi.Card(contentPadding: .medium, background: .elevated) {
                            LemonadeUi.Text("Elevated")
                        }
                    }
                }

                // Spacing Variants
                sectionView(title: "Spacing") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(contentPadding: .none) {
                            LemonadeUi.Text("None")
                        }

                        LemonadeUi.Card(contentPadding: .xSmall) {
                            LemonadeUi.Text("XSmall")
                        }

                        LemonadeUi.Card(contentPadding: .small) {
                            LemonadeUi.Text("Small")
                        }

                        LemonadeUi.Card(contentPadding: .medium) {
                            LemonadeUi.Text("Medium")
                        }
                    }
                }

                // Heading Styles
                sectionView(title: "Heading Styles") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "Default Heading",
                                trailingSlot: {
                                    LemonadeUi.Tag(label: "Tag", voice: .neutral)
                                }
                            )
                        ) {
                            LemonadeUi.Text("Card with default heading style.")
                        }

                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "Overline Heading",
                                headingStyle: .overline,
                                trailingSlot: {
                                    LemonadeUi.Tag(label: "Tag", voice: .neutral)
                                }
                            )
                        ) {
                            LemonadeUi.Text("Card with overline heading style.")
                        }
                    }
                }

                // Header Slots
                sectionView(title: "Header Slots") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "Leading Icon",
                                leadingSlot: {
                                    LemonadeUi.Icon(
                                        icon: .store,
                                        contentDescription: nil,
                                        size: .medium
                                    )
                                }
                            )
                        ) {
                            LemonadeUi.Text("Header with leading slot.")
                        }

                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "Navigation",
                                showNavigationIndicator: true
                            )
                        ) {
                            LemonadeUi.Text("Header with navigation indicator.")
                        }

                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "All Slots",
                                leadingSlot: {
                                    LemonadeUi.Icon(
                                        icon: .store,
                                        contentDescription: nil,
                                        size: .medium
                                    )
                                },
                                trailingSlot: {
                                    LemonadeUi.Tag(label: "Active", voice: .positive)
                                },
                                showNavigationIndicator: true
                            )
                        ) {
                            LemonadeUi.Text("Leading, trailing, and navigation combined.")
                        }
                    }
                }
            }
            .padding()
        }
        .background(.bg.bgSubtle)
        .navigationTitle("Card")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
                .foregroundStyle(.content.contentSecondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        CardDisplayView()
    }
}
