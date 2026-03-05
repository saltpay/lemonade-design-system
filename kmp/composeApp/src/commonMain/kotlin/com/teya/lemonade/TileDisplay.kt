package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.teya.lemonade.core.LemonadeBadgeSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTileVariant

@Suppress("LongMethod")
@Composable
internal fun TileDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Variants
        TileSection(title = "Variants") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                ) {
                    LemonadeUi.Tile(
                        label = "Neutral",
                        icon = LemonadeIcons.Heart,
                        variant = LemonadeTileVariant.Neutral,
                    )
                    LemonadeUi.Text(
                        text = "Neutral",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                ) {
                    LemonadeUi.Tile(
                        label = "Muted",
                        icon = LemonadeIcons.Star,
                        variant = LemonadeTileVariant.Muted,
                    )
                    LemonadeUi.Text(
                        text = "Muted",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                    )
                }
            }
        }

        // OnColor Variant
        TileSection(title = "OnColor Variant") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(LemonadeTheme.radius.radius300))
                    .background(LemonadeTheme.colors.background.bgBrand)
                    .padding(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Tile(
                    label = "OnColor",
                    icon = LemonadeIcons.Check,
                    variant = LemonadeTileVariant.OnColor,
                )
                LemonadeUi.Text(
                    text = "Use on brand backgrounds",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentOnBrandHigh,
                )
            }
        }

        // With Addon (Badge)
        TileSection(title = "With Addon (Badge)") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Tile(
                    label = "Messages",
                    icon = LemonadeIcons.Envelope,
                    variant = LemonadeTileVariant.Neutral,
                    addon = {
                        LemonadeUi.Badge(text = "5", size = LemonadeBadgeSize.XSmall)
                    },
                )

                LemonadeUi.Tile(
                    label = "Updates",
                    icon = LemonadeIcons.Bell,
                    variant = LemonadeTileVariant.Neutral,
                    addon = {
                        LemonadeUi.Badge(text = "New", size = LemonadeBadgeSize.XSmall)
                    },
                )
            }
        }

        // Interactive
        TileSection(title = "Interactive") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Tile(
                    label = "Tap me",
                    icon = LemonadeIcons.HandCoins,
                    onClick = { println("Tile tapped!") },
                    variant = LemonadeTileVariant.Neutral,
                )

                LemonadeUi.Tile(
                    label = "Click",
                    icon = LemonadeIcons.FingerPrint,
                    onClick = { println("Click!") },
                    variant = LemonadeTileVariant.Muted,
                )
            }
        }

        // Disabled
        TileSection(title = "Disabled") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Tile(
                    label = "Disabled",
                    icon = LemonadeIcons.Padlock,
                    enabled = false,
                    variant = LemonadeTileVariant.Neutral,
                )

                LemonadeUi.Tile(
                    label = "Disabled",
                    icon = LemonadeIcons.Padlock,
                    enabled = false,
                    variant = LemonadeTileVariant.Muted,
                )
            }
        }

        // Use Case: Quick Actions
        TileSection(title = "Use Case: Quick Actions") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                ) {
                    LemonadeUi.Tile(
                        label = "Transfer",
                        icon = LemonadeIcons.ArrowLeftRight,
                        onClick = {},
                        variant = LemonadeTileVariant.Neutral,
                    )
                    LemonadeUi.Tile(
                        label = "Pay",
                        icon = LemonadeIcons.Card,
                        onClick = {},
                        variant = LemonadeTileVariant.Neutral,
                    )
                    LemonadeUi.Tile(
                        label = "Request",
                        icon = LemonadeIcons.Download,
                        onClick = {},
                        variant = LemonadeTileVariant.Neutral,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                ) {
                    LemonadeUi.Tile(
                        label = "Scan",
                        icon = LemonadeIcons.QrCode,
                        onClick = {},
                        variant = LemonadeTileVariant.Neutral,
                    )
                    LemonadeUi.Tile(
                        label = "Top Up",
                        icon = LemonadeIcons.Plus,
                        onClick = {},
                        variant = LemonadeTileVariant.Neutral,
                    )
                    LemonadeUi.Tile(
                        label = "More",
                        icon = LemonadeIcons.EllipsisHorizontal,
                        onClick = {},
                        variant = LemonadeTileVariant.Neutral,
                    )
                }
            }
        }

        // Use Case: Dashboard
        TileSection(title = "Use Case: Dashboard") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                ) {
                    LemonadeUi.Tile(
                        label = "Orders",
                        icon = LemonadeIcons.ShoppingBag,
                        onClick = {},
                        variant = LemonadeTileVariant.Muted,
                        addon = {
                            LemonadeUi.Badge(text = "3", size = LemonadeBadgeSize.XSmall)
                        },
                    )
                    LemonadeUi.Tile(
                        label = "Inventory",
                        icon = LemonadeIcons.Package,
                        onClick = {},
                        variant = LemonadeTileVariant.Muted,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                ) {
                    LemonadeUi.Tile(
                        label = "Reports",
                        icon = LemonadeIcons.Chart,
                        onClick = {},
                        variant = LemonadeTileVariant.Muted,
                    )
                    LemonadeUi.Tile(
                        label = "Settings",
                        icon = LemonadeIcons.Gear,
                        onClick = {},
                        variant = LemonadeTileVariant.Muted,
                    )
                }
            }
        }
    }
}

@Composable
private fun TileSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
