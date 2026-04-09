package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.LemonadeTileVariant

/**
 * Lemonade tile component. Used for displaying an icon with a label in a selectable card layout.
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tile(
 *   label = "Transfer",
 *   icon = LemonadeIcons.ArrowLeftRight,
 *   onClick = { println("tile tapped!") },
 * )
 * ```
 * @param label - [String] to be displayed as the Tile's label.
 * @param icon - [LemonadeIcons] displayed above the label.
 * @param modifier - [Modifier] to be applied to the Tile.
 * @param enabled - [Boolean] flag to enable or disable the Tile.
 * @param isSelected - [Boolean] flag to apply selected styling to the Tile.
 * @param supportText - Optional [String] to be displayed below the label.
 * @param alignment - [Alignment.Horizontal] to align the Tile's content horizontally.
 * @param onClick - Callback to be invoked when the Tile is clicked.
 * @param interactionSource - [MutableInteractionSource] to be applied to the Tile.
 * @param variant - [LemonadeTileVariant] to style the Tile accordingly.
 * @param addon - Optional composable content displayed as a badge overlay on the Tile.
 */
@Composable
public fun LemonadeUi.Tile(
    label: String,
    icon: LemonadeIcons,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    supportText: String? = null,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Filled,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    CoreTile(
        modifier = modifier,
        addon = addon,
        variant = variant,
        onClick = onClick,
        enabled = enabled,
        isSelected = isSelected,
        interactionSource = interactionSource,
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
                horizontalAlignment = alignment,
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = 120.dp,
                        minHeight = 88.dp,
                    )
                    .padding(all = LocalSpaces.current.spacing300),
            ) {
                LemonadeUi.Icon(
                    icon = icon,
                    size = LemonadeAssetSize.Medium,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.weight(weight = 1f))

                Column(
                    horizontalAlignment = alignment,
                ) {
                    LemonadeUi.Text(
                        text = label,
                        textStyle = LocalTypographies.current.bodySmallMedium,
                        color = LocalColors.current.content.contentPrimary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )

                    if (supportText != null) {
                        LemonadeUi.Text(
                            text = supportText,
                            textStyle = LocalTypographies.current.bodySmallRegular,
                            color = LocalColors.current.content.contentSecondary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }
            }
        },
    )
}

@Suppress("LongMethod", "LongParameterList")
@Composable
private fun CoreTile(
    variant: LemonadeTileVariant,
    addon: (@Composable BoxScope.() -> Unit)?,
    content: @Composable BoxScope.() -> Unit,
    enabled: Boolean,
    isSelected: Boolean,
    onClick: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val tileShape = LocalShapes.current.radius500
    val baseTileData = variant.data
    val tileData = if (isSelected) {
        baseTileData.copy(
            backgroundColor = LocalColors.current.background.bgBrandSubtle,
            backgroundPressedColor = LocalColors.current.interaction.bgBrandElevatedPressed,
            borderColor = LocalColors.current.border.borderSelected,
            borderWidth = LocalBorderWidths.current.base.border50,
        )
    } else {
        baseTileData
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            tileData.backgroundPressedColor
        } else {
            tileData.backgroundColor
        },
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = tileData.borderColor,
    )
    val animatedBorderWidth by animateDpAsState(
        targetValue = tileData.borderWidth,
    )

    val spaces = LocalSpaces.current
    LemonadeBadgeBox(
        modifier = modifier,
        badgeOffset = {
            DpOffset(
                x = spaces.spacing200,
                y = spaces.spacing200,
            )
        },
        badge = { addon?.invoke(this) },
        content = {
            Box(
                contentAlignment = Alignment.Center,
                content = content,
                modifier = Modifier
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
                        other = tileData.shadow?.let { lemonadeShadow ->
                            Modifier.lemonadeShadow(
                                shadow = lemonadeShadow,
                                shape = tileShape,
                            )
                        }
                            ?: Modifier,
                    ).then(
                        other = Modifier.border(
                            color = animatedBorderColor,
                            shape = tileShape,
                            width = animatedBorderWidth,
                        ),
                    ).clip(shape = tileShape)
                    .then(
                        other = if (onClick != null) {
                            Modifier.clickable(
                                onClick = onClick,
                                interactionSource = interactionSource,
                                role = Role.Button,
                                enabled = enabled,
                                indication = LocalEffects.current.interactionIndication,
                            )
                        } else {
                            Modifier
                        },
                    ).background(
                        color = animatedBackgroundColor,
                        shape = tileShape,
                    ),
            )
        },
    )
}

internal data class TileData(
    val backgroundColor: Color,
    val backgroundPressedColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val shadow: LemonadeShadow?,
)

internal val LemonadeTileVariant.data: TileData
    @Composable get() {
        return when (this) {
            LemonadeTileVariant.Filled,
            @Suppress("DEPRECATION")
            LemonadeTileVariant.Neutral,
            -> TileData(
                backgroundColor = LocalColors.current.background.bgElevated,
                backgroundPressedColor = LocalColors.current.interaction.bgElevatedPressed,
                borderColor = LocalColors.current.border.borderNeutralMedium,
                borderWidth = LocalBorderWidths.current.base.border25,
                shadow = null,
            )

            LemonadeTileVariant.Outlined,
            @Suppress("DEPRECATION")
            LemonadeTileVariant.Muted,
            -> TileData(
                backgroundColor = LocalColors.current.background.bgDefault,
                backgroundPressedColor = LocalColors.current.interaction.bgDefaultPressed,
                borderColor = LocalColors.current.border.borderNeutralMedium,
                borderWidth = LocalBorderWidths.current.base.border25,
                shadow = LemonadeShadow.Xsmall,
            )

            @Suppress("DEPRECATION")
            LemonadeTileVariant.OnColor,
            -> TileData(
                backgroundColor = LocalColors.current.background.bgBrandElevated,
                backgroundPressedColor = LocalColors.current.interaction.bgBrandElevatedPressed,
                borderColor = LocalColors.current.border.borderNeutralMediumInverse,
                borderWidth = LocalBorderWidths.current.base.border25,
                shadow = null,
            )

            @Suppress("DEPRECATION")
            LemonadeTileVariant.Selected,
            -> TileData(
                backgroundColor = LocalColors.current.background.bgBrandSubtle,
                backgroundPressedColor = LocalColors.current.interaction.bgBrandElevatedPressed,
                borderColor = LocalColors.current.border.borderSelected,
                borderWidth = LocalBorderWidths.current.base.border50,
                shadow = null,
            )
        }
    }

private data class TilePreviewData(
    val enabled: Boolean,
    val withAddon: Boolean,
    val variant: LemonadeTileVariant,
    val alignment: Alignment.Horizontal,
    val isSelected: Boolean,
)

private class TilePreviewProvider : PreviewParameterProvider<TilePreviewData> {
    override val values: Sequence<TilePreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<TilePreviewData> {
        return buildList {
            listOf(true, false).forEach { enabled ->
                listOf(true, false).forEach { withAddon ->
                    listOf(
                        LemonadeTileVariant.Filled,
                        LemonadeTileVariant.Outlined,
                    ).forEach { variant ->
                        listOf(
                            Alignment.Start,
                            Alignment.CenterHorizontally,
                            Alignment.End,
                        ).forEach { alignment ->
                            listOf(true, false).forEach { selected ->
                                add(
                                    TilePreviewData(
                                        enabled = enabled,
                                        withAddon = withAddon,
                                        variant = variant,
                                        alignment = alignment,
                                        isSelected = selected,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun LemonadeTilePreview(
    @PreviewParameter(TilePreviewProvider::class)
    previewData: TilePreviewData,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = LocalColors.current.background.bgBrand.copy(
                    alpha = LocalOpacities.current.base.opacity0,
                ),
            ).padding(all = 30.dp),
    ) {
        LemonadeUi.Tile(
            label = "Label",
            icon = LemonadeIcons.Heart,
            enabled = previewData.enabled,
            isSelected = previewData.isSelected,
            alignment = previewData.alignment,
            variant = previewData.variant,
            addon = if (previewData.withAddon) {
                {
                    LemonadeUi.Badge(text = "Addon")
                }
            } else {
                null
            },
        )
    }
}
