package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeBadgeSize
import com.teya.lemonade.core.LemonadeIcons

@Suppress("LongMethod")
@Composable
internal fun BadgeDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Sizes
        BadgeSection(title = "Sizes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                ) {
                    LemonadeUi.Badge(text = "New", size = LemonadeBadgeSize.XSmall)
                    LemonadeUi.Text(
                        text = "XSmall",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
                ) {
                    LemonadeUi.Badge(text = "New", size = LemonadeBadgeSize.Small)
                    LemonadeUi.Text(
                        text = "Small",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                    )
                }
            }
        }

        // With Numbers
        BadgeSection(title = "With Numbers") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Badge(text = "1", size = LemonadeBadgeSize.XSmall)
                LemonadeUi.Badge(text = "5", size = LemonadeBadgeSize.Small)
                LemonadeUi.Badge(text = "99", size = LemonadeBadgeSize.Small)
                LemonadeUi.Badge(text = "99+", size = LemonadeBadgeSize.Small)
            }
        }

        // Labels
        BadgeSection(title = "Labels") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Badge(text = "New", size = LemonadeBadgeSize.Small)
                LemonadeUi.Badge(text = "Hot", size = LemonadeBadgeSize.Small)
                LemonadeUi.Badge(text = "Sale", size = LemonadeBadgeSize.Small)
                LemonadeUi.Badge(text = "Beta", size = LemonadeBadgeSize.Small)
            }
        }

        // In Context
        BadgeSection(title = "In Context") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600),
            ) {
                // Notification icons with badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing800),
                ) {
                    Box {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Bell,
                            contentDescription = "Notifications",
                            size = LemonadeAssetSize.Large,
                        )
                        LemonadeUi.Badge(
                            text = "3",
                            size = LemonadeBadgeSize.XSmall,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-8).dp),
                        )
                    }

                    Box {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Envelope,
                            contentDescription = "Messages",
                            size = LemonadeAssetSize.Large,
                        )
                        LemonadeUi.Badge(
                            text = "12",
                            size = LemonadeBadgeSize.XSmall,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-8).dp),
                        )
                    }

                    Box {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.ShoppingBag,
                            contentDescription = "Cart",
                            size = LemonadeAssetSize.Large,
                        )
                        LemonadeUi.Badge(
                            text = "99+",
                            size = LemonadeBadgeSize.XSmall,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 12.dp, y = (-8).dp),
                        )
                    }
                }

                // Menu item with badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(LemonadeTheme.radius.radius300))
                        .background(LemonadeTheme.colors.background.bgElevated)
                        .padding(LemonadeTheme.spaces.spacing400),
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Inbox,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                    LemonadeUi.Text(
                        text = "Inbox",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    LemonadeUi.Badge(text = "5", size = LemonadeBadgeSize.Small)
                }

                // Tab-like items with badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing100),
                    ) {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.Home,
                            contentDescription = null,
                            size = LemonadeAssetSize.Medium,
                        )
                        LemonadeUi.Text(
                            text = "Home",
                            textStyle = LemonadeTheme.typography.bodySmallRegular,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing100),
                    ) {
                        Box {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Bell,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                            LemonadeUi.Badge(
                                text = "2",
                                size = LemonadeBadgeSize.XSmall,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-8).dp),
                            )
                        }
                        LemonadeUi.Text(
                            text = "Alerts",
                            textStyle = LemonadeTheme.typography.bodySmallRegular,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing100),
                    ) {
                        Box {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.User,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                            LemonadeUi.Badge(
                                text = "New",
                                size = LemonadeBadgeSize.XSmall,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 12.dp, y = (-8).dp),
                            )
                        }
                        LemonadeUi.Text(
                            text = "Profile",
                            textStyle = LemonadeTheme.typography.bodySmallRegular,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeSection(
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
