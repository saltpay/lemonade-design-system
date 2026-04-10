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
import com.teya.lemonade.core.LemonadeIconButtonShape
import com.teya.lemonade.core.LemonadeIconButtonSize
import com.teya.lemonade.core.LemonadeIconButtonType
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons

@Suppress("LongMethod")
@Composable
internal fun IconButtonDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        // Primary Solid
        IconButtonSection(title = "Primary Solid") {
            SizesRow(
                variant = LemonadeIconButtonVariant.Primary,
                type = LemonadeIconButtonType.Solid,
            )
        }

        // Secondary Solid
        IconButtonSection(title = "Secondary Solid") {
            SizesRow(
                variant = LemonadeIconButtonVariant.Secondary,
                type = LemonadeIconButtonType.Solid,
            )
        }

        // Neutral Subtle (default)
        IconButtonSection(title = "Neutral Subtle") {
            SizesRow(
                variant = LemonadeIconButtonVariant.Neutral,
                type = LemonadeIconButtonType.Subtle,
            )
        }

        // Neutral Ghost
        IconButtonSection(title = "Neutral Ghost") {
            SizesRow(
                variant = LemonadeIconButtonVariant.Neutral,
                type = LemonadeIconButtonType.Ghost,
            )
        }

        // Critical Subtle
        IconButtonSection(title = "Critical Subtle") {
            SizesRow(
                variant = LemonadeIconButtonVariant.Critical,
                type = LemonadeIconButtonType.Subtle,
            )
        }

        // Critical Solid
        IconButtonSection(title = "Critical Solid") {
            SizesRow(
                variant = LemonadeIconButtonVariant.Critical,
                type = LemonadeIconButtonType.Solid,
            )
        }

        // Loading
        IconButtonSection(title = "Loading") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Primary,
                    type = LemonadeIconButtonType.Solid,
                    loading = true,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Neutral,
                    type = LemonadeIconButtonType.Subtle,
                    loading = true,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Critical,
                    type = LemonadeIconButtonType.Solid,
                    loading = true,
                )
            }
        }

        // Circular
        IconButtonSection(title = "Circular") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Primary,
                    type = LemonadeIconButtonType.Solid,
                    shape = LemonadeIconButtonShape.Circular,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Neutral,
                    type = LemonadeIconButtonType.Subtle,
                    shape = LemonadeIconButtonShape.Circular,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Critical,
                    type = LemonadeIconButtonType.Solid,
                    shape = LemonadeIconButtonShape.Circular,
                )
            }
        }

        // Disabled
        IconButtonSection(title = "Disabled") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    enabled = false,
                    variant = LemonadeIconButtonVariant.Primary,
                    type = LemonadeIconButtonType.Solid,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    enabled = false,
                    variant = LemonadeIconButtonVariant.Neutral,
                    type = LemonadeIconButtonType.Subtle,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    enabled = false,
                    variant = LemonadeIconButtonVariant.Neutral,
                    type = LemonadeIconButtonType.Ghost,
                )
            }
        }

        // Different Icons
        IconButtonSection(title = "Different Icons") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing400),
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
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun SizesRow(
    variant: LemonadeIconButtonVariant,
    type: LemonadeIconButtonType,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing400),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LemonadeIconButtonSize.entries.forEach { size ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = variant,
                    type = type,
                    size = size,
                )
                LemonadeUi.Text(
                    text = size.name,
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }
    }
}

@Composable
private fun IconButtonSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
