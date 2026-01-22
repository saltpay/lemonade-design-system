package com.teya.lemonade

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIconButtonSize
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun IconButtonDisplay() {
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
        IconButtonSection(title = "Sizes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeIconButtonSize.entries.forEach { size ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.IconButton(
                            icon = LemonadeIcons.Heart,
                            contentDescription = null,
                            size = size,
                            onClick = {}
                        )
                        LemonadeUi.Text(
                            text = size.name,
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }
                }
            }
        }

        // Variants
        IconButtonSection(title = "Variants") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeIconButtonVariant.entries.forEach { variant ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.IconButton(
                            icon = LemonadeIcons.Heart,
                            contentDescription = null,
                            variant = variant,
                            onClick = {}
                        )
                        LemonadeUi.Text(
                            text = variant.name,
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }
                }
            }
        }

        // States
        IconButtonSection(title = "States") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.IconButton(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        enabled = true,
                        onClick = {}
                    )
                    LemonadeUi.Text(
                        text = "Enabled",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.IconButton(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        enabled = false,
                        onClick = {}
                    )
                    LemonadeUi.Text(
                        text = "Disabled",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }
            }
        }

        // All Combinations
        IconButtonSection(title = "All Combinations") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeIconButtonVariant.entries.forEach { variant ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.Text(
                            text = variant.name,
                            textStyle = LemonadeTheme.typography.bodySmallRegular,
                            color = LemonadeTheme.colors.content.contentSecondary
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LemonadeIconButtonSize.entries.forEach { size ->
                                LemonadeUi.IconButton(
                                    icon = LemonadeIcons.Heart,
                                    contentDescription = null,
                                    variant = variant,
                                    size = size,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }

        // Different Icons
        IconButtonSection(title = "Different Icons") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                listOf(
                    LemonadeIcons.Heart,
                    LemonadeIcons.Star,
                    LemonadeIcons.CircleCheck,
                    LemonadeIcons.CircleX,
                    LemonadeIcons.CircleInfo,
                ).forEach { icon ->
                    LemonadeUi.IconButton(
                        icon = icon,
                        contentDescription = null,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun IconButtonSection(
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
