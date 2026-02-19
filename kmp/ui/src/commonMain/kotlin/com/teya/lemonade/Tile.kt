package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.LemonadeTileVariant
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
public fun LemonadeUi.Tile(
    label: String,
    icon: LemonadeIcons,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    CoreTile(
        variant = variant,
        label = label,
        leadingSlot = {
            LemonadeUi.Icon(
                icon = icon,
                contentDescription = null
            )
        },
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        addon = addon
    )
}

@Composable
public fun LemonadeUi.Tile(
    label: String,
    leadingSlot: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    CoreTile(
        label = label,
        leadingSlot = leadingSlot,
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        addon = addon
    )
}

@Composable
public fun LemonadeUi.Tile(
    label: String,
    icon: LemonadeIcons,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    CoreTile(
        label = label,
        leadingSlot = {
            LemonadeUi.Icon(
                icon = icon,
                contentDescription = null
            )
        },
        enabled = enabled,
        onClick = onClick,
        isSelected = isSelected,
        modifier = modifier,
        interactionSource = interactionSource,
        addon = addon
    )
}

@Composable
public fun LemonadeUi.Tile(
    label: String,
    leadingSlot: @Composable () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    CoreTile(
        label = label,
        leadingSlot = leadingSlot,
        enabled = enabled,
        onClick = onClick,
        isSelected = isSelected,
        modifier = modifier,
        interactionSource = interactionSource,
        addon = addon
    )
}

@Composable
internal fun LemonadeUi.MobileTile(
    label: String,
    icon: LemonadeIcons,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    addon: (@Composable BoxScope.() -> Unit)? = null,
) {
    CoreTile(
        label = label,
        leadingSlot = {
            LemonadeUi.Icon(icon = icon, contentDescription = null)
        },
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        addon = addon
    )
}

@Composable
private fun CoreTile(
    variant: LemonadeTileVariant = LemonadeTileVariant.Neutral,
    label: String,
    leadingSlot: @Composable () -> Unit,
    isSelected: Boolean = false,
    enabled: Boolean,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    addon: @Composable (BoxScope.() -> Unit)? = null,
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val addonOffset = object {
        val x = LemonadeTheme.spaces.spacing400.value.toInt()
        val y = -(LemonadeTheme.spaces.spacing400.value).toInt()
    }

    val tileShape = LocalShapes.current.radius500
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            variant.data.backgroundPressedColor
        } else {
            variant.data.backgroundColor
        },
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) {
            LocalColors.current.border.borderSelected
        } else {
            Color.Transparent
        },
    )

    Box(modifier = modifier, propagateMinConstraints = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing400),
            modifier = Modifier
                .semantics { selected = isSelected }
                .defaultMinSize(minWidth = LocalSizes.current.size2500)
                .then(
                    other = if (!enabled) {
                        Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                    } else {
                        Modifier
                    },
                )
                .border(
                    width = if (isSelected) {
                        LocalBorderWidths.current.state.borderSelected
                    } else {
                        LocalBorderWidths.current.base.border0
                    },
                    color = animatedBorderColor,
                    shape = tileShape,
                )
                .then(
                    other = if (isPressed) {
                        variant.data.shadowPressed?.let { lemonadeShadow ->
                            Modifier.lemonadeShadow(
                                shadow = lemonadeShadow,
                                shape = tileShape,
                            )
                        }
                            ?: Modifier
                    } else {
                        variant.data.shadow?.let { lemonadeShadow ->
                            Modifier.lemonadeShadow(
                                shadow = lemonadeShadow,
                                shape = tileShape,
                            )
                        }
                            ?: Modifier
                    }
                )
                .clip(shape = tileShape)
                .then(
                    other = if (onClick != null) {
                        Modifier.clickable(
                            onClick = onClick,
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Button,
                            enabled = enabled,
                        )
                    } else {
                        Modifier
                    }
                )
                .background(
                    color = if (isSelected) {
                        LocalColors.current.background.bgBrandSubtle
                    } else {
                        animatedBackgroundColor
                    },
                    shape = tileShape,
                )
                .padding(
                    horizontal = LocalSpaces.current.spacing400,
                    vertical = LocalSpaces.current.spacing400,
                ),
        ) {
            leadingSlot()

            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodySmallSemiBold,
                color = LocalColors.current.content.contentPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        if (addon != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .wrapContentSize(align = Alignment.TopEnd)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            constraints.copy(minWidth = 0, minHeight = 0),
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                x = addonOffset.x,
                                y = addonOffset.y,
                            )
                        }
                    },
                content = addon,
            )
        }
    }
}

internal data class TileData(
    val backgroundColor: Color,
    val backgroundPressedColor: Color,
    val borderColor: Color?,
    val shadow: LemonadeShadow?,
    val shadowPressed: LemonadeShadow?
)

internal val LemonadeTileVariant.data: TileData
    @Composable get() {
        return when (this) {
            LemonadeTileVariant.Neutral -> TileData(
                backgroundColor = LocalColors.current.background.bgElevated,
                backgroundPressedColor = LocalColors.current.interaction.bgElevatedPressed,
                borderColor = LocalColors.current.border.borderNeutralMedium,
                shadow = null,
                shadowPressed = null
            )

            LemonadeTileVariant.Muted -> TileData(
                backgroundColor = LocalColors.current.background.bgDefault,
                backgroundPressedColor = LocalColors.current.interaction.bgDefaultPressed,
                borderColor = LocalColors.current.border.borderNeutralMedium,
                shadow = LemonadeShadow.Xsmall,
                shadowPressed = null,
            )

            LemonadeTileVariant.OnColor -> TileData(
                backgroundColor = LocalColors.current.background.bgBrandElevated,
                backgroundPressedColor = LocalColors.current.interaction.bgBrandElevatedPressed,
                borderColor = LocalColors.current.border.borderNeutralMediumInverse,
                shadow = null,
                shadowPressed = null
            )
        }
    }

private data class TilePreviewData(
    val enabled: Boolean,
    val withAddon: Boolean,
    val variant: LemonadeTileVariant,
)

private class TilePreviewProvider : PreviewParameterProvider<TilePreviewData> {
    override val values: Sequence<TilePreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<TilePreviewData> {
        return buildList {
            listOf(true, false).forEach { enabled ->
                listOf(true, false).forEach { withAddon ->
                    LemonadeTileVariant.entries.forEach { variant ->
                        add(
                            TilePreviewData(
                                enabled = enabled,
                                withAddon = withAddon,
                                variant = variant,
                            )
                        )
                    }
                }
            }
        }.asSequence()
    }
}

@Suppress("DEPRECATION")
@LemonadePreview
@Composable
private fun LemonadeLabeledRadioButtonPreview(
    @PreviewParameter(TilePreviewProvider::class)
    previewData: TilePreviewData,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = if (previewData.variant == LemonadeTileVariant.OnColor) {
                    LocalColors.current.background.bgBrand
                } else {
                    LocalColors.current.background.bgBrand.copy(
                        alpha = LocalOpacities.current.base.opacity0,
                    )
                }
            )
            .padding(30.dp),
    ) {
        LemonadeUi.MobileTile(
            label = "Label",
            icon = LemonadeIcons.Heart,
            enabled = previewData.enabled,
            variant = previewData.variant,
            onClick = {},
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
