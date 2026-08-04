package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonType
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeIconButtonShape
import com.teya.lemonade.core.LemonadeIcons

private data class IconButtonSectionSpec(
    val title: String,
    val variant: LemonadeButtonVariant,
    val type: LemonadeButtonType,
)

private val iconButtonSectionSpecs: List<IconButtonSectionSpec> = listOf(
    IconButtonSectionSpec(
        title = "Primary Solid",
        variant = LemonadeButtonVariant.Primary,
        type = LemonadeButtonType.Solid,
    ),
    IconButtonSectionSpec(
        title = "Secondary Solid",
        variant = LemonadeButtonVariant.Secondary,
        type = LemonadeButtonType.Solid,
    ),
    IconButtonSectionSpec(
        title = "Neutral Subtle",
        variant = LemonadeButtonVariant.Neutral,
        type = LemonadeButtonType.Subtle,
    ),
    IconButtonSectionSpec(
        title = "Neutral Ghost",
        variant = LemonadeButtonVariant.Neutral,
        type = LemonadeButtonType.Ghost,
    ),
    IconButtonSectionSpec(
        title = "Critical Subtle",
        variant = LemonadeButtonVariant.Critical,
        type = LemonadeButtonType.Subtle,
    ),
    IconButtonSectionSpec(
        title = "Critical Solid",
        variant = LemonadeButtonVariant.Critical,
        type = LemonadeButtonType.Solid,
    ),
)

private val differentIcons: List<LemonadeIcons> = listOf(
    LemonadeIcons.Heart,
    LemonadeIcons.Star,
    LemonadeIcons.CircleCheck,
    LemonadeIcons.CircleX,
    LemonadeIcons.CircleInfo,
)

@Composable
internal fun IconButtonDisplay() {
    SampleScreenDisplayLazyColumn(title = "IconButton") {
        items(
            items = iconButtonSectionSpecs,
            key = { spec -> spec.title },
        ) { spec ->
            IconButtonSection(title = spec.title) {
                SizesRow(
                    variant = spec.variant,
                    type = spec.type,
                )
            }
        }

        item(key = "Loading") {
            IconButtonSection(title = "Loading") {
                IconButtonRow {
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
        }

        item(key = "Circular") {
            IconButtonSection(title = "Circular") {
                IconButtonRow {
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
        }

        item(key = "Disabled") {
            IconButtonSection(title = "Disabled") {
                IconButtonRow {
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
        }

        item(key = "Different Icons") {
            IconButtonSection(title = "Different Icons") {
                IconButtonRow {
                    differentIcons.forEach { icon ->
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
}

@Composable
private fun IconButtonRow(content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing400),
    ) {
        content()
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
        LemonadeButtonSize.entries.forEach { size ->
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
        modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing600),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
