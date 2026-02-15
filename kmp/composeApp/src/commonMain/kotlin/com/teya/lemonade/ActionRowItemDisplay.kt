package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun ActionRowItemDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .background(color = LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Basic"),
        ) {
            LemonadeUi.ActionRowItem(
                label = "Simple Action",
                onClick = {},
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "With Leading Icon"),
        ) {
            LemonadeUi.ActionRowItem(
                label = "Add New Item",
                onClick = {},
                leadingIcon = LemonadeIcons.Plus,
            )

            LemonadeUi.ActionRowItem(
                label = "Edit Settings",
                onClick = {},
                leadingIcon = LemonadeIcons.Gear,
            )

            LemonadeUi.ActionRowItem(
                label = "View Details",
                onClick = {},
                leadingIcon = LemonadeIcons.CircleInfo,
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "With Trailing Content"),
        ) {
            LemonadeUi.ActionRowItem(
                label = "Navigate",
                onClick = {},
                leadingIcon = LemonadeIcons.Home,
                trailingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.ChevronRight,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentTertiary,
                        contentDescription = null,
                    )
                },
            )

            LemonadeUi.ActionRowItem(
                label = "Search Items",
                onClick = {},
                leadingIcon = LemonadeIcons.Search,
                trailingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.ChevronRight,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentTertiary,
                        contentDescription = null,
                    )
                },
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Multiple Actions"),
        ) {
            LemonadeUi.ActionRowItem(
                label = "Add",
                onClick = {},
                leadingIcon = LemonadeIcons.Plus,
            )

            LemonadeUi.ActionRowItem(
                label = "Edit",
                onClick = {},
                leadingIcon = LemonadeIcons.PencilLine,
            )

            LemonadeUi.ActionRowItem(
                label = "Delete",
                onClick = {},
                leadingIcon = LemonadeIcons.Trash,
            )

            LemonadeUi.ActionRowItem(
                label = "Copy",
                onClick = {},
                leadingIcon = LemonadeIcons.Copy,
            )

            LemonadeUi.ActionRowItem(
                label = "Share",
                onClick = {},
                leadingIcon = LemonadeIcons.Share,
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Disabled State"),
        ) {
            LemonadeUi.ActionRowItem(
                label = "Disabled Action",
                onClick = {},
                leadingIcon = LemonadeIcons.Padlock,
                enabled = false,
            )

            LemonadeUi.ActionRowItem(
                label = "Disabled with Trailing",
                onClick = {},
                leadingIcon = LemonadeIcons.Gear,
                enabled = false,
                trailingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.ChevronRight,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentTertiary,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}
