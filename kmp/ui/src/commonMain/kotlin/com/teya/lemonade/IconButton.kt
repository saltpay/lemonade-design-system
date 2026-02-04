package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIconButtonSize
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Lemonade icon button component. Used for simple click actions with only an icon.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.IconButton(
 *   icon = LemonadeIcons.Heart,
 *   contentDescription = "Favorite",
 *   onClick = { println("icon button clicked!") },
 * )
 * ```
 *
 * @param icon - [LemonadeIcons] to be displayed as the Button's icon.
 * @param contentDescription - [String] content description for accessibility.
 * @param onClick - Callback to be invoked when the Button is clicked.
 * @param modifier - [Modifier] to be applied to the Button.
 * @param interactionSource - [MutableInteractionSource] to be applied to the Button.
 * @param enabled - [Boolean] flag to enable or disable the Button.
 * @param variant - [LemonadeIconButtonVariant] to style the Button accordingly.
 * @param size - [LemonadeIconButtonSize] to size the Button accordingly.
 */
@Composable
public fun LemonadeUi.IconButton(
    icon: LemonadeIcons,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    variant: LemonadeIconButtonVariant = LemonadeIconButtonVariant.Subtle,
    size: LemonadeIconButtonSize = LemonadeIconButtonSize.Medium,
) {
    CoreIconButton(
        icon = icon,
        contentDescription = contentDescription,
        onClick = onClick,
        enabled = enabled,
        variant = variant,
        size = size,
        interactionSource = interactionSource,
        modifier = modifier,
    )
}

@Suppress("LongParameterList")
@Composable
private fun CoreIconButton(
    icon: LemonadeIcons,
    contentDescription: String?,
    onClick: () -> Unit,
    enabled: Boolean,
    variant: LemonadeIconButtonVariant,
    size: LemonadeIconButtonSize,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
) {
    val isHovering by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedBackgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> variant.backgroundColor.backgroundPressedColor
            isHovering -> variant.backgroundColor.backgroundHoverColor
            else -> variant.backgroundColor.backgroundColor
        },
    )

    LemonadeUi.Icon(
        icon = icon,
        contentDescription = contentDescription,
        size = size.sizeData.iconSize,
        modifier = modifier
            .then(
                other = if (!enabled) {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                } else {
                    Modifier
                },
            ).clip(shape = size.sizeData.shape)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = LocalEffects.current.interactionIndication,
                enabled = enabled,
            ).background(
                color = animatedBackgroundColor,
            ).padding(all = size.sizeData.innerPaddings),
    )
}

private data class IconButtonColorData(
    val backgroundColor: Color,
    val backgroundHoverColor: Color,
    val backgroundPressedColor: Color,
)

private data class IconButtonSizeData(
    val iconSize: LemonadeAssetSize,
    val innerPaddings: Dp,
    val shape: Shape,
)

private val LemonadeIconButtonVariant.backgroundColor: IconButtonColorData
    @Composable get() {
        return when (this) {
            LemonadeIconButtonVariant.Ghost -> IconButtonColorData(
                backgroundColor = Color.Transparent,
                backgroundHoverColor = LocalColors.current.interaction.bgSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
            )

            LemonadeIconButtonVariant.Subtle -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgNeutralSubtle,
                backgroundHoverColor = LocalColors.current.interaction.bgNeutralSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
            )
        }
    }

private val LemonadeIconButtonSize.sizeData: IconButtonSizeData
    @Composable get() {
        return when (this) {
            LemonadeIconButtonSize.Large -> IconButtonSizeData(
                iconSize = LemonadeAssetSize.Large,
                innerPaddings = LocalSpaces.current.spacing400,
                shape = LocalShapes.current.radius400,
            )

            LemonadeIconButtonSize.Medium -> IconButtonSizeData(
                iconSize = LemonadeAssetSize.Large,
                innerPaddings = LocalSpaces.current.spacing200,
                shape = LocalShapes.current.radius300,
            )

            LemonadeIconButtonSize.Small -> IconButtonSizeData(
                iconSize = LemonadeAssetSize.Small,
                innerPaddings = LocalSpaces.current.spacing200,
                shape = LocalShapes.current.radius300,
            )
        }
    }

private data class IconButtonPreviewData(
    val size: LemonadeIconButtonSize,
    val variant: LemonadeIconButtonVariant,
    val enabled: Boolean,
)

private class IconButtonPreviewProvider : PreviewParameterProvider<IconButtonPreviewData> {
    override val values: Sequence<IconButtonPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<IconButtonPreviewData> =
        buildList {
            listOf(true, false).forEach { enabled ->
                LemonadeIconButtonSize.entries.forEach { size ->
                    LemonadeIconButtonVariant.entries.forEach { variant ->
                        add(
                            element = IconButtonPreviewData(
                                size = size,
                                variant = variant,
                                enabled = enabled,
                            ),
                        )
                    }
                }
            }
        }.asSequence()
}

@Composable
@LemonadePreview
private fun SymbolContainerPreview(
    @PreviewParameter(IconButtonPreviewProvider::class)
    previewData: IconButtonPreviewData,
) {
    LemonadeUi.IconButton(
        icon = LemonadeIcons.Heart,
        size = previewData.size,
        variant = previewData.variant,
        enabled = previewData.enabled,
        contentDescription = null,
        onClick = { /* Click action */ },
    )
}
