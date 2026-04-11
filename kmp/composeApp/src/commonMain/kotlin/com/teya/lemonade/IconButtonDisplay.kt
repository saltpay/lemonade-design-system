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
import com.teya.lemonade.core.LemonadeButtonType
import com.teya.lemonade.core.LemonadeButtonVariant
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
                variant = LemonadeButtonVariant.Primary,
                type = LemonadeButtonType.Solid,
            )
        }

        // Secondary Solid
        IconButtonSection(title = "Secondary Solid") {
            SizesRow(
                variant = LemonadeButtonVariant.Secondary,
                type = LemonadeButtonType.Solid,
            )
        }

        // Neutral Subtle (default)
        IconButtonSection(title = "Neutral Subtle") {
            SizesRow(
                variant = LemonadeButtonVariant.Neutral,
                type = LemonadeButtonType.Subtle,
            )
        }

        // Neutral Ghost
        IconButtonSection(title = "Neutral Ghost") {
            SizesRow(
                variant = LemonadeButtonVariant.Neutral,
                type = LemonadeButtonType.Ghost,
            )
        }

        // Critical Subtle
        IconButtonSection(title = "Critical Subtle") {
            SizesRow(
                variant = LemonadeButtonVariant.Critical,
                type = LemonadeButtonType.Subtle,
            )
        }

        // Critical Solid
        IconButtonSection(title = "Critical Solid") {
            SizesRow(
                variant = LemonadeButtonVariant.Critical,
                type = LemonadeButtonType.Solid,
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
                    variant = LemonadeButtonVariant.Primary,
                    type = LemonadeButtonType.Solid,
                    loading = true,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeButtonVariant.Neutral,
                    type = LemonadeButtonType.Subtle,
                    loading = true,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeButtonVariant.Critical,
                    type = LemonadeButtonType.Solid,
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
                    variant = LemonadeButtonVariant.Primary,
                    type = LemonadeButtonType.Solid,
                    shape = LemonadeIconButtonShape.Circular,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeButtonVariant.Neutral,
                    type = LemonadeButtonType.Subtle,
                    shape = LemonadeIconButtonShape.Circular,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    variant = LemonadeButtonVariant.Critical,
                    type = LemonadeButtonType.Solid,
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
                    variant = LemonadeButtonVariant.Primary,
                    type = LemonadeButtonType.Solid,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    enabled = false,
                    variant = LemonadeButtonVariant.Neutral,
                    type = LemonadeButtonType.Subtle,
                )
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Heart,
                    contentDescription = null,
                    onClick = {},
                    enabled = false,
                    variant = LemonadeButtonVariant.Neutral,
                    type = LemonadeButtonType.Ghost,
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
    variant: LemonadeButtonVariant,
    type: LemonadeButtonType,
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
