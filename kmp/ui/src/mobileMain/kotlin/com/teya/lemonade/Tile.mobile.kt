package com.teya.lemonade

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTileVariant
import kotlin.text.String

/**
 * Tile component to be used in a grid or multiple selection and display of sorts.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tile(
 *     label = "Label",
 *     icon = LemonadeIcons.Heart,
 *     onClick = { /* clicked! */ },
 *     variant = it,
 *     enabled = enabled,
 *     addon = if (withAddon) {
 *         {
 *             LemonadeUi.Badge(text = "Tag")
 *         }
 *     } else {
 *         null
 *     }
 * )
 * ```
 *
 * @param label - [String] to be displayed in the component.
 * @param icon - [LemonadeIcons] to be displayed in the component.
 * @param modifier - [Modifier] to be applied to the component.
 * @param enabled - [Boolean] flag that indicates if the component is enabled. This will disable touch
 *  interactions and have visual effects.
 * @param onClick - Optional callback to be called when the component is clicked.
 * @param interactionSource - [MutableInteractionSource] that handles interaction source management.
 * @param variant - [LemonadeTileVariant] to be applied to the component. This will affect the
 *  background colors and border colors.
 * @param addon - Slot component to be displayed in the top end of the component.
 */
@Composable
public fun LemonadeUi.Tile(
    label: String,
    icon: LemonadeIcons,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    LemonadeUi.MobileTile(
        label = label,
        icon = icon,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        interactionSource = interactionSource,
        variant = variant,
        addon = addon,
    )
}