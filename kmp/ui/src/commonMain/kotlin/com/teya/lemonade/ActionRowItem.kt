package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons

/**
 * A horizontal row component used for displaying a primary action with optional secondary elements.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.ActionRowItem(
 *     label = "Add New Item",
 *     onClick = { /* trigger an action */ },
 *     leadingIcon = LemonadeIcons.Plus,
 *     enabled = true,
 *     trailingSlot = {
 *         LemonadeUi.Icon(
 *             icon = LemonadeIcons.ChevronRight,
 *             size = LemonadeAssetSize.Medium,
 *             contentDescription = null,
 *         )
 *     },
 * )
 * ```
 *
 * @param label - Label text to be displayed in the action row.
 * @param onClick - Callback that is triggered on click interaction with the row.
 * @param modifier - [Modifier] to be applied to the base container of component.
 * @param leadingIcon - Optional icon to be displayed before the label.
 * @param enabled - Flag that defines if the component is enabled or not. If disabled, click interactions
 *  and visual states are disabled.
 * @param trailingSlot - A Slot to be placed in the trailing position of the row.
 */
@Composable
public fun LemonadeUi.ActionRowItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: LemonadeIcons? = null,
    enabled: Boolean = true,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = LocalShapes.current.radius300)
            .background(color = LocalColors.current.background.bgDefault)
            .border(
                width = LocalBorderWidths.current.base.border25,
                color = LocalColors.current.border.borderNeutralLow,
                shape = LocalShapes.current.radius300,
            )
            .clickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(
                horizontal = LocalSpaces.current.spacing400,
                vertical = LocalSpaces.current.spacing300,
            )
            .then(
                other = if (!enabled) {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                } else {
                    Modifier
                },
            ),
    ) {
        if (leadingIcon != null) {
            LemonadeUi.Icon(
                icon = leadingIcon,
                size = LemonadeAssetSize.Medium,
                tint = LocalColors.current.content.contentPrimary,
                contentDescription = null,
            )
        }

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.bodyMediumMedium,
            color = LocalColors.current.content.contentPrimary,
            modifier = Modifier.weight(weight = 1f),
        )

        if (trailingSlot != null) {
            trailingSlot()
        }
    }
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun ActionRowItemPreview() {
    LemonadeUi.ActionRowItem(
        label = "Add New Item",
        onClick = { },
        leadingIcon = LemonadeIcons.Plus,
    )
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun ActionRowItemWithTrailingPreview() {
    LemonadeUi.ActionRowItem(
        label = "Edit Settings",
        onClick = { },
        leadingIcon = LemonadeIcons.Gear,
        trailingSlot = {
            LemonadeUi.Icon(
                icon = LemonadeIcons.ChevronRight,
                size = LemonadeAssetSize.Medium,
                tint = LocalColors.current.content.contentTertiary,
                contentDescription = null,
            )
        },
    )
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun ActionRowItemDisabledPreview() {
    LemonadeUi.ActionRowItem(
        label = "Disabled Action",
        onClick = { },
        leadingIcon = LemonadeIcons.Trash,
        enabled = false,
    )
}
