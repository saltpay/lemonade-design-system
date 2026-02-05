import SwiftUI
import Lemonade

struct TileDisplayView: View {
    @State private var selected: Int? = 0
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: .space.spacing600) {
                // Types
                sectionView(title: "Types") {
                    HStack(spacing: .space.spacing300) {
                        LemonadeUi.ActionTile(
                            label: "Action",
                            icon: .heart,
                            onClick: {},
                            variant: .neutral,
                            
                        )

                        LemonadeUi.SelectionTile(
                            label: "Selection",
                            isSelected: selected == 0,
                            icon: .star,
                            onClick: {
                                if selected == 0 {
                                    selected = nil
                                } else {
                                    selected = 0
                                }
                            }
                            
                        )
                    }
                }
                
                // Variants
                sectionView(title: "Variants") {
                    HStack(spacing: .space.spacing300) {
                        LemonadeUi.ActionTile(
                            label: "Neutral",
                            icon: .heart,
                            onClick: {},
                            variant: .neutral,
                            
                        )

                        LemonadeUi.ActionTile(
                            label: "Muted",
                            icon: .star,
                            onClick: {},
                            variant: .muted
                        )
                    }
                }

                // OnColor Variant
                sectionView(title: "OnColor Variant") {
                    VStack(spacing: .space.spacing200) {
                        LemonadeUi.ActionTile(
                            label: "OnColor",
                            icon: .heart,
                            onClick: {},
                            variant: .onColor
                        )
                        LemonadeUi.Text(
                            "Use on brand backgrounds",
                            textStyle: LemonadeTypography.shared.bodySmallRegular,
                            color: .content.contentOnBrandHigh
                        )
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.space.spacing400)
                    .background(.bg.bgBrand)
                    .clipShape(RoundedRectangle(cornerRadius: .radius.radius500))
                }
                
                // With Custom Leading
                sectionView(title: "With Custom Leading") {
                    HStack(spacing: .space.spacing400) {
                        LemonadeUi.ActionTile(
                            label: "United Kingdom",
                            leadingSlot: {
                                LemonadeUi.CountryFlag(flag: .gBUnitedKingdom)
                            },
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Label",
                            leadingSlot: {
                                LemonadeUi.SymbolContainer(
                                    icon: .heart,
                                    contentDescription: nil)
                            },
                            onClick: {},
                            variant: .neutral
                        )
                    }
                }

                // With Addon
                sectionView(title: "With Addon (Badge)") {
                    HStack(spacing: .space.spacing400) {
                        LemonadeUi.ActionTile(
                            label: "Messages",
                            icon: .envelope,
                            onClick: {},
                            variant: .neutral
                        ) {
                            LemonadeUi.Badge(text: "5", size: .xSmall)
                        }

                        LemonadeUi.ActionTile(
                            label: "Updates",
                            icon: .bell,
                            onClick: {},
                            variant: .neutral
                        ) {
                            LemonadeUi.Badge(text: "New", size: .xSmall)
                        }
                    }
                }

                // Interactive
                sectionView(title: "Interactive") {
                    HStack(spacing: .space.spacing400) {
                        LemonadeUi.ActionTile(
                            label: "Tap me",
                            icon: .handCoins,
                            onClick: {
                                print("Tile tapped!")
                            },
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Click",
                            icon: .fingerPrint,
                            onClick: {
                                print("Click!")
                            },
                            variant: .muted
                        )
                    }
                }

                // Disabled
                sectionView(title: "Disabled") {
                    HStack(spacing: .space.spacing400) {
                        LemonadeUi.ActionTile(
                            label: "Disabled",
                            icon: .padlock,
                            enabled: false,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Disabled",
                            icon: .padlock,
                            enabled: false,
                            onClick: {},
                            variant: .muted
                        )
                    }
                }

                // Use Case: Quick Actions
                sectionView(title: "Use Case: Quick Actions") {
                    LazyVGrid(columns: [
                        GridItem(.flexible()),
                        GridItem(.flexible()),
                        GridItem(.flexible())
                    ], spacing: .space.spacing400) {
                        LemonadeUi.ActionTile(
                            label: "Transfer",
                            icon: .arrowLeftRight,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Pay",
                            icon: .card,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Request",
                            icon: .download,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Scan",
                            icon: .qrCode,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "Top Up",
                            icon: .plus,
                            onClick: {},
                            variant: .neutral
                        )

                        LemonadeUi.ActionTile(
                            label: "More",
                            icon: .ellipsisHorizontal,
                            onClick: {},
                            variant: .neutral
                        )
                    }
                }

                // Use Case: Dashboard
                sectionView(title: "Use Case: Dashboard") {
                    VStack(spacing: .space.spacing400) {
                        HStack(spacing: .space.spacing400) {
                            LemonadeUi.ActionTile(
                                label: "Orders",
                                icon: .shoppingBag,
                                onClick: {},
                                variant: .muted
                            ) {
                                LemonadeUi.Badge(text: "3", size: .xSmall)
                            }

                            LemonadeUi.ActionTile(
                                label: "Inventory",
                                icon: .package,
                                onClick: {},
                                variant: .muted
                            )
                        }

                        HStack(spacing: .space.spacing400) {
                            LemonadeUi.ActionTile(
                                label: "Reports",
                                icon: .chart,
                                onClick: {},
                                variant: .muted
                            )

                            LemonadeUi.ActionTile(
                                label: "Settings",
                                icon: .gear,
                                onClick: {},
                                variant: .muted
                            )
                        }
                    }
                }
                
                sectionView(title: "Deprecated Tile") {
                    HStack(spacing: .space.spacing200) {
                        // Neutral
                        LemonadeUi.Tile(
                            label: "Neutral",
                            icon: .heart,
                        )
                        
                        // Muted
                        LemonadeUi.Tile(
                            label: "Muted",
                            icon: .heart,
                            variant: .muted
                        )
                    
                        // Disabled
                        LemonadeUi.Tile(
                            label: "Disabled",
                            icon: .heart,
                            enabled: false,
                            onClick: {},
                        )
                    }
                    
                    // OnColor
                    LemonadeUi.Tile(
                        label: "OnColor",
                        icon: .heart,
                        variant: .onColor
                    )
                        .padding(.space.spacing300)
                        .background(.bg.bgBrand)
                }
            }
            .padding(.space.spacing400)
        }
        .navigationTitle("Tile")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: .space.spacing300) {
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

