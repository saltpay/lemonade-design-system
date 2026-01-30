import SwiftUI
import Lemonade

struct CardDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Basic Card
                sectionView(title: "Basic Card") {
                    LemonadeUi.Card(contentPadding: .medium) {
                        LemonadeUi.Text("This is a basic card with medium padding.")
                    }
                }

                // Padding Variants
                sectionView(title: "Padding Variants") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(contentPadding: .none) {
                            LemonadeUi.Text("No padding")
                                .padding()
                        }

                        LemonadeUi.Card(contentPadding: .xSmall) {
                            LemonadeUi.Text("XSmall padding")
                        }

                        LemonadeUi.Card(contentPadding: .small) {
                            LemonadeUi.Text("Small padding")
                        }

                        LemonadeUi.Card(contentPadding: .medium) {
                            LemonadeUi.Text("Medium padding")
                        }
                    }
                }

                // Background Variants
                sectionView(title: "Background Variants") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(contentPadding: .medium, background: .default) {
                            LemonadeUi.Text("Default background")
                        }

                        LemonadeUi.Card(contentPadding: .medium, background: .subtle) {
                            LemonadeUi.Text("Subtle background")
                        }
                    }
                }

                // With Header
                sectionView(title: "With Header") {
                    VStack(spacing: 16) {
                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(title: "Card Title")
                        ) {
                            LemonadeUi.Text("Card content goes here. This is an example of a card with a header.")
                        }

                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "With Trailing Slot",
                                trailingSlot: {
                                    LemonadeUi.Tag(label: "New", voice: .positive)
                                }
                            )
                        ) {
                            LemonadeUi.Text("This card has a header with a trailing tag.")
                        }

                        LemonadeUi.Card(
                            contentPadding: .medium,
                            header: CardHeaderConfig(
                                title: "Actions",
                                trailingSlot: {
                                    LemonadeUi.Icon(
                                        icon: .ellipsisVertical,
                                        contentDescription: "More options",
                                        size: .medium
                                    )
                                }
                            )
                        ) {
                            LemonadeUi.Text("Card with action icon in header.")
                        }
                    }
                }

                // Complex Content
                sectionView(title: "Complex Content") {
                    LemonadeUi.Card(
                        contentPadding: .medium,
                        header: CardHeaderConfig(
                            title: "Order Summary",
                            trailingSlot: {
                                LemonadeUi.Tag(label: "Confirmed", voice: .positive)
                            }
                        )
                    ) {
                        VStack(alignment: .leading, spacing: 12) {
                            HStack {
                                Text("Subtotal")
                                Spacer()
                                Text("$99.00")
                            }

                            HStack {
                                Text("Shipping")
                                Spacer()
                                Text("$5.00")
                            }

                            Divider()

                            HStack {
                                Text("Total")
                                    .fontWeight(.bold)
                                Spacer()
                                Text("$104.00")
                                    .fontWeight(.bold)
                            }
                        }
                    }
                }

                // Nested Cards
                sectionView(title: "Nested Cards") {
                    LemonadeUi.Card(
                        contentPadding: .medium,
                        header: CardHeaderConfig(title: "Payment Methods")
                    ) {
                        VStack(spacing: 12) {
                            LemonadeUi.Card(contentPadding: .small, background: .subtle) {
                                HStack {
                                    LemonadeUi.Icon(icon: .card, contentDescription: nil, size: .medium)
                                    VStack(alignment: .leading) {
                                        Text("Visa ending in 4242")
                                        Text("Expires 12/25")
                                            .font(.caption)
                                            .foregroundStyle(.content.contentSecondary)
                                    }
                                    Spacer()
                                    LemonadeUi.Tag(label: "Default", voice: .info)
                                }
                            }

                            LemonadeUi.Card(contentPadding: .small, background: .subtle) {
                                HStack {
                                    LemonadeUi.Icon(icon: .card, contentDescription: nil, size: .medium)
                                    VStack(alignment: .leading) {
                                        Text("Mastercard ending in 1234")
                                        Text("Expires 06/24")
                                            .font(.caption)
                                            .foregroundStyle(.content.contentSecondary)
                                    }
                                    Spacer()
                                }
                            }
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
