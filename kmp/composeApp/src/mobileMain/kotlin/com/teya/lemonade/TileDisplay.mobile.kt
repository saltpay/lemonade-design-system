package com.teya.lemonade

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTileVariant

@Composable
internal actual fun LemonadeUi.SampleTile(
    label: String,
    icon: LemonadeIcons,
    modifier: Modifier,
    enabled: Boolean,
    onClick: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
    variant: LemonadeTileVariant,
    addon: @Composable (() -> Unit)?
) {
    LemonadeUi.Tile(
        label = label,
        icon = icon,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        interactionSource = interactionSource,
        variant = variant,
        addon = if (addon != null) {
            { addon() }
        } else {
            null
        },
    )
}