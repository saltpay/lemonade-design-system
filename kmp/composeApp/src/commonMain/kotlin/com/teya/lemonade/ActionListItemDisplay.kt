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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeBadgeSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeListItemVoice
import com.teya.lemonade.core.TagVoice

@Suppress("LongMethod")
@Composable
internal fun ActionListItemDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .background(LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        // ActionListItem
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ActionListItem"),
        ) {
            LemonadeUi.ActionListItem(
                label = "Settings",
                showNavigationIndicator = true,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Gear,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
            )

            LemonadeUi.ActionListItem(
                label = "Notifications",
                supportText = "Manage your notifications",
                showNavigationIndicator = true,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Bell,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
            )

            LemonadeUi.ActionListItem(
                label = "Privacy",
                showNavigationIndicator = true,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Padlock,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
            )
        }

        // ActionListItem - Label Is Secondary
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ActionListItem - Label Is Secondary"),
        ) {
            LemonadeUi.ActionListItem(
                label = "Last used",
                supportText = "170 Oaklands Grove, London,...",
                labelIsSecondary = true,
                showNavigationIndicator = true,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.MapPin,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentSecondary,
                    )
                },
            )

            LemonadeUi.ActionListItem(
                label = "Company address",
                supportText = "170 Oaklands Grove, London,...",
                labelIsSecondary = true,
                showNavigationIndicator = true,
                onItemClicked = {},
                showDivider = false,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.MapPin,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentSecondary,
                    )
                },
            )
        }

        // ActionListItem - Trailing Alignment
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ActionListItem - Trailing Alignment"),
        ) {
            LemonadeUi.ActionListItem(
                label = "Top aligned",
                supportText = "trailingVerticalAlignment: Top\nSecond line",
                showNavigationIndicator = true,
                showDivider = true,
                trailingVerticalAlignment = Alignment.Top,
                onItemClicked = {},
                trailingSlot = {
                    LemonadeUi.Tag(label = "Top", voice = TagVoice.Info)
                },
            )

            LemonadeUi.ActionListItem(
                label = "Center aligned",
                supportText = "trailingVerticalAlignment: CenterVertically\nSecond line",
                showNavigationIndicator = true,
                showDivider = true,
                trailingVerticalAlignment = Alignment.CenterVertically,
                onItemClicked = {},
                trailingSlot = {
                    LemonadeUi.Tag(label = "Center", voice = TagVoice.Positive)
                },
            )

            LemonadeUi.ActionListItem(
                label = "Bottom aligned",
                supportText = "trailingVerticalAlignment: Bottom\nSecond line",
                showNavigationIndicator = true,
                showDivider = false,
                trailingVerticalAlignment = Alignment.Bottom,
                onItemClicked = {},
                trailingSlot = {
                    LemonadeUi.Tag(label = "Bottom", voice = TagVoice.Warning)
                },
            )
        }

        // ActionListItem with Trailing Slot
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ActionListItem with Trailing"),
        ) {
            LemonadeUi.ActionListItem(
                label = "Updates Available",
                showNavigationIndicator = true,
                onItemClicked = {},
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Download,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
                showDivider = true,
                trailingSlot = {
                    LemonadeUi.Badge(text = "3", size = LemonadeBadgeSize.Small)
                },
            )

            LemonadeUi.ActionListItem(
                label = "New Features",
                showNavigationIndicator = true,
                onItemClicked = {},
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Sparkles,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
                showDivider = true,
                trailingSlot = {
                    LemonadeUi.Tag(label = "New", voice = TagVoice.Positive)
                },
            )
        }
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ActionListItem - Critical Voice"),
        ) {
            LemonadeUi.ActionListItem(
                label = "Delete Account",
                voice = LemonadeListItemVoice.Critical,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Trash,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentCritical,
                    )
                },
            )

            LemonadeUi.ActionListItem(
                label = "Log Out",
                supportText = "You will need to sign in again",
                voice = LemonadeListItemVoice.Critical,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.LogOut,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                        tint = LemonadeTheme.colors.content.contentCritical,
                    )
                },
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Disabled States"),
        ) {
            LemonadeUi.ActionListItem(
                label = "Disabled Action",
                enabled = false,
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Gear,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Loading State"),
        ) {
            LemonadeUi.ActionListItem(
                label = "",
                isLoading = true,
                showDivider = true,
            )

            LemonadeUi.ActionListItem(
                label = "",
                isLoading = true,
                showDivider = true,
            )

            LemonadeUi.ActionListItem(
                label = "",
                isLoading = true,
            )
        }
    }
}
