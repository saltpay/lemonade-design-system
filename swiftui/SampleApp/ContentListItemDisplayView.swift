import SwiftUI
import Lemonade

struct ContentListItemDisplayView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: .space.spacing400) {
                // MARK: - Horizontal Simple
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Horizontal Simple")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Account holder",
                        value: "John Doe"
                    )

                    LemonadeUi.ContentListItem(
                        label: "Account number",
                        value: "1234-5678"
                    )
                }

                // MARK: - Horizontal with Leading + Trailing
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Horizontal with Leading + Trailing")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Label",
                        value: "Value",
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .heart,
                                contentDescription: nil,
                                size: .medium
                            )
                        },
                        trailingSlot: {
                            LemonadeUi.Icon(
                                icon: .pencilLine,
                                contentDescription: "Edit",
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentBrand
                            )
                        }
                    )
                }

                // MARK: - Horizontal with Content Slot
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Horizontal with Content Slot")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Status",
                        value: "Active",
                        contentSlot: {
                            LemonadeUi.Tag(label: "Available", voice: .positive)
                        }
                    )
                }

                // MARK: - Vertical Small
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Vertical Small")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Balance",
                        value: "$1,234.56",
                        layout: .vertical
                    )
                }

                // MARK: - Vertical Small with Leading + Trailing
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Vertical Small with Leading + Trailing")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Balance",
                        value: "$1,234.56",
                        layout: .vertical,
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .heart,
                                contentDescription: nil,
                                size: .medium
                            )
                        },
                        trailingSlot: {
                            LemonadeUi.Icon(
                                icon: .pencilLine,
                                contentDescription: "Edit",
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentBrand
                            )
                        }
                    )
                }

                // MARK: - Vertical Large (with Content Slot)
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Vertical Large")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Total Balance",
                        value: "$5,000.00",
                        layout: .vertical,
                        contentSlot: {
                            LemonadeUi.Tag(label: "Available", voice: .positive)
                        }
                    )
                }

                // MARK: - Vertical Large with All Slots
                LemonadeUi.Card(
                    contentPadding: .medium,
                    header: CardHeaderConfig(title: "Vertical Large with All Slots")
                ) {
                    LemonadeUi.ContentListItem(
                        label: "Total Balance",
                        value: "$5,000.00",
                        layout: .vertical,
                        leadingSlot: {
                            LemonadeUi.SymbolContainer(
                                icon: .heart,
                                contentDescription: nil,
                                size: .medium
                            )
                        },
                        trailingSlot: {
                            LemonadeUi.Icon(
                                icon: .pencilLine,
                                contentDescription: "Edit",
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentBrand
                            )
                        },
                        contentSlot: {
                            LemonadeUi.Tag(label: "Available", voice: .positive)
                        }
                    )
                }
            }
            .padding(.space.spacing400)
        }
        .background(.bg.bgSubtle)
        .navigationTitle("ContentListItem")
    }
}

#Preview {
    NavigationStack {
        ContentListItemDisplayView()
    }
}
