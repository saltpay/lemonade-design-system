package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.LemonadeTileVariant
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

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
        modifier = modifier,
        addon = addon,
        variant = variant,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LemonadeUi.Icon(
                    icon = icon,
                    size = LemonadeAssetSize.Medium,
                    contentDescription = null,
                )

                LemonadeUi.Text(
                    text = label,
                    textStyle = LocalTypographies.current.bodyMediumMedium,
                    color = LocalColors.current.content.contentPrimary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
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
    onClick: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val tileShape = LocalShapes.current.radius500
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            variant.data.backgroundPressedColor
        } else {
            variant.data.backgroundColor
        },
    )
    LemonadeBadgeBox(
        modifier = modifier,
        badgeOffset = DpOffset(
            x = LocalSpaces.current.spacing200,
            y = LocalSpaces.current.spacing200,
        ),
        badge = { addon?.invoke(this) },
        content = {
            Box(
                contentAlignment = Alignment.Center,
                content = content,
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp)
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
                        color = animatedBackgroundColor,
                        shape = tileShape,
                    ).padding(
                        horizontal = LocalSpaces.current.spacing100,
                        vertical = LocalSpaces.current.spacing400,
                    ),
            )
        },
    )
}

internal data class TileData(
    val backgroundColor: Color,
    val backgroundPressedColor: Color,
    val borderColor: Color?,
    val shadow: LemonadeShadow?,
)

internal val LemonadeTileVariant.data: TileData
    @Composable get() {
        return when (this) {
            LemonadeTileVariant.Neutral -> TileData(
                backgroundColor = LocalColors.current.background.bgElevated,
                backgroundPressedColor = LocalColors.current.interaction.bgElevatedPressed,
                borderColor = LocalColors.current.border.borderNeutralMedium,
                shadow = null,
            )

            LemonadeTileVariant.Muted -> TileData(
                backgroundColor = LocalColors.current.background.bgDefault,
                backgroundPressedColor = LocalColors.current.interaction.bgDefaultPressed,
                borderColor = LocalColors.current.border.borderNeutralMedium,
                shadow = LemonadeShadow.Xsmall,
            )

            LemonadeTileVariant.OnColor -> TileData(
                backgroundColor = LocalColors.current.background.bgBrandElevated,
                backgroundPressedColor = LocalColors.current.interaction.bgBrandElevatedPressed,
                borderColor = LocalColors.current.border.borderNeutralMediumInverse,
                shadow = null,
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

    private fun buildAllVariants(): Sequence<TilePreviewData> =
        buildList {
            listOf(true, false).forEach { enabled ->
                listOf(true, false).forEach { withAddon ->
                    LemonadeTileVariant.entries.forEach { variant ->
                        add(
                            TilePreviewData(
                                enabled = enabled,
                                withAddon = withAddon,
                                variant = variant,
                            ),
                        )
                    }
                }
            }
        }.asSequence()
}

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
                },
            ).padding(30.dp),
    ) {
        LemonadeUi.MobileTile(
            label = "Label",
            icon = LemonadeIcons.Heart,
            enabled = previewData.enabled,
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
