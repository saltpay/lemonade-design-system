package com.teya.lemonade

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teya.lemonade.core.LemonadeAssetSize
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
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                ) {
                    LemonadeUi.Tile(
                        label = "Filled",
                        icon = LemonadeIcons.Heart,
                        variant = LemonadeTileVariant.Filled,
                    )
                    LemonadeUi.Text(
                        text = "Filled",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                ) {
                    LemonadeUi.Tile(
                        label = "Outlined",
                        icon = LemonadeIcons.Star,
                        variant = LemonadeTileVariant.Outlined,
                    )
                    LemonadeUi.Text(
                        text = "Outlined",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                    )
                }
            }
        }

        // Selected
        TileSection(title = "Selected") {
            var isFilledSelected by remember { mutableStateOf(value = true) }
            var isOutlinedSelected by remember { mutableStateOf(value = true) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Filled",
                    icon = LemonadeIcons.CircleCheck,
                    variant = LemonadeTileVariant.Filled,
                    isSelected = isFilledSelected,
                    onClick = { isFilledSelected = !isFilledSelected },
                )
                LemonadeUi.Tile(
                    label = "Outlined",
                    icon = LemonadeIcons.CircleCheck,
                    variant = LemonadeTileVariant.Outlined,
                    isSelected = isOutlinedSelected,
                    onClick = { isOutlinedSelected = !isOutlinedSelected },
                )
            }
        }

        // Support Text
        TileSection(title = "Support Text") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Filled",
                    icon = LemonadeIcons.Heart,
                    variant = LemonadeTileVariant.Filled,
                    supportText = "Support text",
                )

                LemonadeUi.Tile(
                    label = "Outlined",
                    icon = LemonadeIcons.Star,
                    variant = LemonadeTileVariant.Outlined,
                    supportText = "Support text",
                )
            }
        }

        // Top Accessory
        TileSection(title = "Top Accessory") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Accessory",
                    icon = LemonadeIcons.Heart,
                    variant = LemonadeTileVariant.Filled,
                    topAccessory = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.CircleInfo,
                            size = LemonadeAssetSize.Small,
                            contentDescription = null,
                        )
                    },
                )
            }
        }

        // Leading Slot
        TileSection(title = "Leading Slot") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Custom",
                    variant = LemonadeTileVariant.Filled,
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.ShoppingBag,
                            size = LemonadeAssetSize.Medium,
                            contentDescription = null,
                        )
                    },
                )
            }
        }

        // Interactive
        TileSection(title = "Interactive") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Tap me",
                    icon = LemonadeIcons.HandCoins,
                    onClick = { println("Tile tapped!") },
                    variant = LemonadeTileVariant.Filled,
                )

                LemonadeUi.Tile(
                    label = "Click",
                    icon = LemonadeIcons.FingerPrint,
                    onClick = { println("Click!") },
                    variant = LemonadeTileVariant.Outlined,
                )
            }
        }

        // Disabled
        TileSection(title = "Disabled") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Disabled",
                    icon = LemonadeIcons.Padlock,
                    enabled = false,
                    variant = LemonadeTileVariant.Filled,
                )

                LemonadeUi.Tile(
                    label = "Disabled",
                    icon = LemonadeIcons.Padlock,
                    enabled = false,
                    variant = LemonadeTileVariant.Outlined,
                )
            }
        }

        // Custom Background
        TileSection(title = "Custom Background") {
            val customBg = Color(red = 0.96f, green = 0.97f, blue = 0.65f)
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                LemonadeUi.Tile(
                    label = "Filled",
                    icon = LemonadeIcons.Heart,
                    variant = LemonadeTileVariant.Filled,
                    backgroundColor = customBg,
                )

                LemonadeUi.Tile(
                    label = "Outlined",
                    icon = LemonadeIcons.Star,
                    variant = LemonadeTileVariant.Outlined,
                    backgroundColor = customBg,
                )

                LemonadeUi.Tile(
                    label = "Disabled",
                    icon = LemonadeIcons.Padlock,
                    enabled = false,
                    variant = LemonadeTileVariant.Filled,
                    backgroundColor = customBg,
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
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    LemonadeUi.Tile(
                        label = "Transfer",
                        icon = LemonadeIcons.ArrowLeftRight,
                        onClick = {},
                        variant = LemonadeTileVariant.Filled,
                    )
                    LemonadeUi.Tile(
                        label = "Pay",
                        icon = LemonadeIcons.Card,
                        onClick = {},
                        variant = LemonadeTileVariant.Filled,
                    )
                    LemonadeUi.Tile(
                        label = "Request",
                        icon = LemonadeIcons.Download,
                        onClick = {},
                        variant = LemonadeTileVariant.Filled,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    LemonadeUi.Tile(
                        label = "Scan",
                        icon = LemonadeIcons.QrCode,
                        onClick = {},
                        variant = LemonadeTileVariant.Filled,
                    )
                    LemonadeUi.Tile(
                        label = "Top Up",
                        icon = LemonadeIcons.Plus,
                        onClick = {},
                        variant = LemonadeTileVariant.Filled,
                    )
                    LemonadeUi.Tile(
                        label = "More",
                        icon = LemonadeIcons.EllipsisHorizontal,
                        onClick = {},
                        variant = LemonadeTileVariant.Filled,
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
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    LemonadeUi.Tile(
                        label = "Orders",
                        icon = LemonadeIcons.ShoppingBag,
                        onClick = {},
                        variant = LemonadeTileVariant.Outlined,
                    )
                    LemonadeUi.Tile(
                        label = "Inventory",
                        icon = LemonadeIcons.Package,
                        onClick = {},
                        variant = LemonadeTileVariant.Outlined,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    LemonadeUi.Tile(
                        label = "Reports",
                        icon = LemonadeIcons.Chart,
                        onClick = {},
                        variant = LemonadeTileVariant.Outlined,
                    )
                    LemonadeUi.Tile(
                        label = "Settings",
                        icon = LemonadeIcons.Gear,
                        onClick = {},
                        variant = LemonadeTileVariant.Outlined,
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
