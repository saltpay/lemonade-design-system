package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeBadgeSize
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeListItemVoice
import com.teya.lemonade.core.TagVoice

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
            .padding(all = LemonadeTheme.spaces.spacing400)
    ) {
        // ActionListItem
        ActionListItemSection(title = "ActionListItem") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
            ) {
                LemonadeUi.ActionListItem(
                    label = "Settings",
                    showNavigationIndicator = true,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Gear,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                        )
                    }
                )

                LemonadeUi.ActionListItem(
                    label = "Notifications",
                    supportText = "Manage your notifications",
                    showNavigationIndicator = true,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Bell,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                        )
                    }
                )

                LemonadeUi.ActionListItem(
                    label = "Privacy",
                    showNavigationIndicator = true,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Padlock,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                        )
                    }
                )
            }
        }

        // ActionListItem with Trailing Slot
        ActionListItemSection(title = "ActionListItem with Trailing") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
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
                    trailingSlot = {
                        LemonadeUi.Badge(text = "3", size = LemonadeBadgeSize.Small)
                    }
                )

                LemonadeUi.ActionListItem(
                    label = "New Features",
                    showNavigationIndicator = true,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Sparkles,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium
                        )
                    },
                    trailingSlot = {
                        LemonadeUi.Tag(label = "New", voice = TagVoice.Positive)
                    }
                )
            }
        }

        // ActionListItem - Critical Voice
        ActionListItemSection(title = "ActionListItem - Critical Voice") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
            ) {
                LemonadeUi.ActionListItem(
                    label = "Delete Account",
                    voice = LemonadeListItemVoice.Critical,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Trash,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                            tint = LemonadeTheme.colors.content.contentCritical
                        )
                    }
                )

                LemonadeUi.ActionListItem(
                    label = "Log Out",
                    supportText = "You will need to sign in again",
                    voice = LemonadeListItemVoice.Critical,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.LogOut,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                            tint = LemonadeTheme.colors.content.contentCritical
                        )
                    }
                )
            }
        }

        // Disabled States
        ActionListItemSection(title = "Disabled States") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
            ) {
                LemonadeUi.ActionListItem(
                    label = "Disabled Action",
                    enabled = false,
                    onItemClicked = {},
                    leadingSlot = {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Gear,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionListItemSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary
        )
        content()
    }
}
