package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTileVariant
import kotlin.String
import kotlin.text.String

/**
 * Tile component to be used in multiple selection and display of sorts.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tile(
 *     label = "Label",
 *     description = "This is the tile description",
 *     leadingIcon = LemonadeIcons.Heart,
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
 * @param leadingIcon - [LemonadeIcons] to be displayed in the component.
 * @param modifier - [Modifier] to be applied to the component.
 * @param description - [String] optional description to be shown below the label.
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
    leadingIcon: LemonadeIcons,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    CoreTile(
        leadingIcon = leadingIcon,
        label = label,
        description = description,
        enabled = enabled,
        variant = variant,
        addon = addon,
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier,
    )
}

@Composable
private fun CoreTile(
    leadingIcon: LemonadeIcons,
    label: String,
    description: String?,
    enabled: Boolean,
    variant: LemonadeTileVariant,
    addon: (@Composable RowScope.() -> Unit)?,
    onClick: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val tileShape = LocalShapes.current.radius500
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            variant.data.backgroundPressedColor
        } else {
            variant.data.backgroundColor
        },
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
        modifier = modifier
            .then(
                other = if (!enabled) {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                } else {
                    Modifier
                },
            ).then(
                other = if (isFocused) {
                    Modifier
                        .border(
                            width = LocalBorderWidths.current.base.border25,
                            color = LocalColors.current.border.borderSelected,
                            shape = tileShape,
                        ).padding(all = LocalSpaces.current.spacing50)
                } else {
                    Modifier
                },
            ).then(
                other = variant.data.shadow?.let { lemonadeShadow ->
                    Modifier.lemonadeShadow(
                        shadow = lemonadeShadow,
                        shape = tileShape,
                    )
                }
                    ?: Modifier,
            ).then(
                other = variant.data.borderColor?.let { borderColor ->
                    Modifier.border(
                        color = borderColor,
                        shape = tileShape,
                        width = LocalBorderWidths.current.base.border25,
                    )
                }
                    ?: Modifier,
            ).clip(shape = tileShape)
            .then(
                other = if (onClick != null) {
                    Modifier.clickable(
                        onClick = onClick,
                        interactionSource = interactionSource,
                        role = Role.Button,
                        enabled = enabled,
                    )
                } else {
                    Modifier
                },
            ).background(
                color = backgroundColor,
                shape = tileShape,
            ).padding(all = LocalSpaces.current.spacing400),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LemonadeUi.Icon(
                icon = leadingIcon,
                size = LemonadeAssetSize.Medium,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (addon != null) {
                addon()
            }
        }

        Column {
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodySmallMedium,
                color = LocalColors.current.content.contentPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            if (description != null) {
                LemonadeUi.Text(
                    text = description,
                    textStyle = LocalTypographies.current.bodySmallRegular,
                    color = LocalColors.current.content.contentSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            }
        }
    }
}
