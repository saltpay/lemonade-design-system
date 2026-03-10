import SwiftUI
import Lemonade

struct TileDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing600) {
                // MARK: - Variants
                sectionView(title: "Variants") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Tile(
                                label: "Neutral",
                                icon: .heart,
                                variant: .neutral
                            )
                            LemonadeUi.Text(
                                "Neutral",
                                textStyle: LemonadeTypography.shared.bodySmallRegular
                            )
                        }

                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Tile(
                                label: "Muted",
                                icon: .star,
                                variant: .muted
                            )
                            LemonadeUi.Text(
                                "Muted",
                                textStyle: LemonadeTypography.shared.bodySmallRegular
                            )
                        }

                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Tile(
                                label: "Selected",
                                icon: .circleCheck,
                                variant: .selected
                            )
                            LemonadeUi.Text(
                                "Selected",
                                textStyle: LemonadeTypography.shared.bodySmallRegular
                            )
                        }
                    }
                }

                sectionView(title: "OnColor Variant") {
                    VStack(spacing: LemonadeTheme.spaces.spacing200) {
                        LemonadeUi.Tile(
                            label: "OnColor",
                            icon: .check,
                            variant: .onColor
                        )
                        LemonadeUi.Text(
                            "Use on brand backgrounds",
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: .content.contentOnBrandHigh
                        )
                    }
                    .frame(maxWidth: .infinity)
                    .padding(LemonadeTheme.spaces.spacing400)
                    .background(.bg.bgBrand)
                    .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))
                }

                sectionView(title: "Alignment") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Start",
                            icon: .arrowLeft,
                            variant: .neutral,
                            alignment: .leading
                        )

                        LemonadeUi.Tile(
                            label: "Center",
                            icon: .arrowLeftRight,
                            variant: .neutral,
                            alignment: .center
                        )

                        LemonadeUi.Tile(
                            label: "End",
                            icon: .arrowRight,
                            variant: .neutral,
                            alignment: .trailing
                        )
                    }
                }

                // MARK: - Features
                sectionView(title: "With Addon (Badge)") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Messages",
                            icon: .envelope,
                            variant: .neutral
                        ) {
                            LemonadeUi.Badge(text: "5", size: .xSmall)
                        }

                        LemonadeUi.Tile(
                            label: "Updates",
                            icon: .bell,
                            variant: .neutral
                        ) {
                            LemonadeUi.Badge(text: "New", size: .xSmall)
                        }
                    }
                }

                sectionView(title: "Interactive") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Tap me",
                            icon: .handCoins,
                            onClick: {
                                print("Tile tapped!")
                            },
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Click",
                            icon: .fingerPrint,
                            onClick: {
                                print("Click!")
                            },
                            variant: .muted
                        )
                    }
                }

                sectionView(title: "Disabled") {
                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Disabled",
                            icon: .padlock,
                            enabled: false,
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Disabled",
                            icon: .padlock,
                            enabled: false,
                            variant: .muted
                        )
                    }
                }

                // MARK: - Layout Behavior
                sectionView(title: "Default Size (min 120pt)") {
                    LemonadeUi.Tile(
                        label: "Default",
                        icon: .heart,
                        variant: .neutral
                    )
                }

                sectionView(title: "Stretched (stretched: true)") {
                    LemonadeUi.Tile(
                        label: "Single Stretched",
                        icon: .heart,
                        onClick: {},
                        variant: .neutral,
                        stretched: true
                    )

                    HStack(spacing: LemonadeTheme.spaces.spacing400) {
                        LemonadeUi.Tile(
                            label: "Transfer",
                            icon: .arrowLeftRight,
                            onClick: {},
                            variant: .neutral,
                            stretched: true
                        )

                        LemonadeUi.Tile(
                            label: "Pay",
                            icon: .card,
                            onClick: {},
                            variant: .neutral,
                            stretched: true
                        )

                        LemonadeUi.Tile(
                            label: "Request",
                            icon: .download,
                            onClick: {},
                            variant: .neutral,
                            stretched: true
                        )
                    }
                }

                sectionView(title: "Tight Container (200pt for 3 tiles)") {
                    HStack(spacing: LemonadeTheme.spaces.spacing200) {
                        LemonadeUi.Tile(
                            label: "One",
                            icon: .heart,
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Two",
                            icon: .star,
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Three",
                            icon: .check,
                            variant: .neutral
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
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Pay",
                            icon: .card,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Request",
                            icon: .download,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Scan",
                            icon: .qrCode,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "Top Up",
                            icon: .plus,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.Tile(
                            label: "More",
                            icon: .ellipsisHorizontal,
                            onClick: {},
                            variant: .neutral
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
                                variant: .muted
                            ) {
                                LemonadeUi.Badge(text: "3", size: .xSmall)
                            }

                            LemonadeUi.Tile(
                                label: "Inventory",
                                icon: .package,
                                onClick: {},
                                variant: .muted
                            )
                        }

                        HStack(spacing: LemonadeTheme.spaces.spacing400) {
                            LemonadeUi.Tile(
                                label: "Reports",
                                icon: .chart,
                                onClick: {},
                                variant: .muted
                            )

                            LemonadeUi.Tile(
                                label: "Settings",
                                icon: .gear,
                                onClick: {},
                                variant: .muted
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
