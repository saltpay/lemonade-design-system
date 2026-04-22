import SwiftUI
import Lemonade

struct TileDisplayView: View {
    @State private var isFilledSelected = true
    @State private var isOutlinedSelected = true

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing600) {
                // MARK: - Variants
                sectionView(title: "Variants") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Tile(
                                label: "Filled",
                                icon: .heart,
                                onClick: {},
                                variant: .filled
                            )
                            LemonadeUi.Text(
                                "Filled",
                                textStyle: LemonadeTypography.shared.bodySmallRegular
                            )
                        }

                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Tile(
                                label: "Outlined",
                                icon: .star,
                                onClick: {},
                                variant: .outlined
                            )
                            LemonadeUi.Text(
                                "Outlined",
                                textStyle: LemonadeTypography.shared.bodySmallRegular
                            )
                        }
                    }
                }

                // MARK: - Selected
                sectionView(title: "Selected") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Filled",
                            icon: .circleCheck,
                            isSelected: isFilledSelected,
                            onClick: { isFilledSelected.toggle() },
                            variant: .filled
                        )
                        LemonadeUi.Tile(
                            label: "Outlined",
                            icon: .circleCheck,
                            isSelected: isOutlinedSelected,
                            onClick: { isOutlinedSelected.toggle() },
                            variant: .outlined
                        )
                    }
                }

                // MARK: - Support Text
                sectionView(title: "Support Text") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Filled",
                            icon: .heart,
                            supportText: "Long support text example to check how it wraps and looks on smaller screens",
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Outlined",
                            icon: .star,
//                            supportText: "Support",
                            supportText: "Long support text example to check how it wraps and looks on smaller screens",
                            variant: .outlined,
                            stretched: true
                        )
                    }
                }

                // MARK: - Top Accessory
                sectionView(title: "Top Accessory") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Accessory",
                            icon: .heart,
                            variant: .filled
                        ) {
                            LemonadeUi.Icon(
                                icon: .circleInfo,
                                contentDescription: nil,
                                size: .small
                            )
                        }
                    }
                }

                // MARK: - Leading Slot
                sectionView(title: "Leading Slot") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Custom",
                            variant: .filled,
                            leadingSlot: {
                                LemonadeUi.Icon(
                                    icon: .shoppingBag,
                                    contentDescription: nil,
                                    size: .medium
                                )
                            }
                        )
                    }
                }

                // MARK: - Features
                sectionView(title: "Interactive") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Tap me",
                            icon: .handCoins,
                            onClick: {
                                print("Tile tapped!")
                            },
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Click",
                            icon: .fingerPrint,
                            onClick: {
                                print("Click!")
                            },
                            variant: .outlined
                        )
                    }
                }

                sectionView(title: "Disabled") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Disabled",
                            icon: .padlock,
                            enabled: false,
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Disabled",
                            icon: .padlock,
                            enabled: false,
                            variant: .outlined
                        )
                    }
                }

                // MARK: - Layout Behavior
                sectionView(title: "Default Size (min 120pt)") {
                    LemonadeUi.Tile(
                        label: "Default",
                        icon: .heart,
                        variant: .filled
                    )
                }

                sectionView(title: "Stretched (stretched: true)") {
                    LemonadeUi.Tile(
                        label: "Single Stretched",
                        icon: .heart,
                        onClick: {},
                        variant: .filled,
                        stretched: true
                    )

                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Transfer",
                            icon: .arrowLeftRight,
                            onClick: {},
                            variant: .filled,
                            stretched: true
                        )

                        LemonadeUi.Tile(
                            label: "Pay",
                            icon: .card,
                            onClick: {},
                            variant: .filled,
                            stretched: true
                        )

                        LemonadeUi.Tile(
                            label: "Request",
                            icon: .download,
                            onClick: {},
                            variant: .filled,
                            stretched: true
                        )
                    }
                }

                sectionView(title: "Tight Container (200pt for 3 tiles)") {
                    HStack(spacing: LemonadeTheme.spaces.spacing200) {
                        LemonadeUi.Tile(
                            label: "One",
                            icon: .heart,
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Two",
                            icon: .star,
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Three",
                            icon: .check,
                            variant: .filled
                        )
                    }
                    .frame(width: 200)
                }

                // MARK: - Use Cases
                sectionView(title: "Use Case: Quick Actions") {
                    LazyVGrid(columns: [
                        GridItem(.flexible()),
                        GridItem(.flexible()),
                        GridItem(.flexible())
                    ], spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Transfer",
                            icon: .arrowLeftRight,
                            onClick: {},
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Pay",
                            icon: .card,
                            onClick: {},
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Request",
                            icon: .download,
                            onClick: {},
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Scan",
                            icon: .qrCode,
                            onClick: {},
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "Top Up",
                            icon: .plus,
                            onClick: {},
                            variant: .filled
                        )

                        LemonadeUi.Tile(
                            label: "More",
                            icon: .ellipsisHorizontal,
                            onClick: {},
                            variant: .filled
                        )
                    }
                }

                sectionView(title: "Use Case: Dashboard") {
                    VStack(spacing: LemonadeTheme.spaces.spacing400) {
                        HStack(spacing: LemonadeTheme.spaces.spacing400) {
                            LemonadeUi.Tile(
                                label: "Orders",
                                icon: .shoppingBag,
                                onClick: {},
                                variant: .outlined
                            )

                            LemonadeUi.Tile(
                                label: "Inventory",
                                icon: .package,
                                onClick: {},
                                variant: .outlined
                            )
                        }

                        HStack(spacing: LemonadeTheme.spaces.spacing400) {
                            LemonadeUi.Tile(
                                label: "Reports",
                                icon: .chart,
                                onClick: {},
                                variant: .outlined
                            )

                            LemonadeUi.Tile(
                                label: "Settings",
                                icon: .gear,
                                onClick: {},
                                variant: .outlined
                            )
                        }
                    }
                }
            }
            .padding(LemonadeTheme.spaces.spacing400)
        }
        .navigationTitle("Tile")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            LemonadeUi.Text(
                title,
                textStyle: LemonadeTypography.shared.headingXSmall,
                color: .content.contentSecondary
            )

            content()
        }
    }
}

#Preview {
    NavigationStack {
        TileDisplayView()
    }
}
