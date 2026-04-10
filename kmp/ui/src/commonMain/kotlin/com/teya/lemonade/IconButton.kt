package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIconButtonShape
import com.teya.lemonade.core.LemonadeIconButtonSize
import com.teya.lemonade.core.LemonadeIconButtonType
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons

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
 * @param variant - [LemonadeIconButtonVariant] for the color palette (Primary, Secondary, Neutral, Critical).
 * @param type - [LemonadeIconButtonType] for the fill treatment (Solid, Subtle, Ghost).
 * @param size - [LemonadeIconButtonSize] to size the Button accordingly.
 * @param loading - [Boolean] flag to show a loading spinner.
 * @param shape - [LemonadeIconButtonShape] for the button shape (Rounded, Circular).
 */
@Composable
public fun LemonadeUi.IconButton(
    icon: LemonadeIcons,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    variant: LemonadeIconButtonVariant = LemonadeIconButtonVariant.Neutral,
    type: LemonadeIconButtonType = LemonadeIconButtonType.Subtle,
    size: LemonadeIconButtonSize = LemonadeIconButtonSize.Medium,
    loading: Boolean = false,
    shape: LemonadeIconButtonShape = LemonadeIconButtonShape.Rounded,
) {
    CoreIconButton(
        icon = icon,
        contentDescription = contentDescription,
        onClick = onClick,
        enabled = enabled,
        variant = variant,
        type = type,
        size = size,
        loading = loading,
        shape = shape,
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
    type: LemonadeIconButtonType,
    size: LemonadeIconButtonSize,
    loading: Boolean,
    shape: LemonadeIconButtonShape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
) {
    val isHovering by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = resolveColors(
        variant = variant,
        type = type,
    )
    val animatedBackgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> colors.backgroundPressedColor
            isHovering -> colors.backgroundHoverColor
            else -> colors.backgroundColor
        },
    )
    val sizeData = size.toSizeData(shape = shape)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                other = if (!enabled) {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                } else {
                    Modifier
                },
            )
            .clip(shape = sizeData.shape)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = LocalEffects.current.interactionIndication,
                enabled = enabled && !loading,
            )
            .background(color = animatedBackgroundColor)
            .padding(all = sizeData.innerPaddings),
    ) {
        if (loading) {
            LemonadeUi.Spinner(
                size = sizeData.spinnerSize,
                tint = colors.contentColor,
            )
        } else {
            LemonadeUi.Icon(
                icon = icon,
                contentDescription = contentDescription,
                size = sizeData.iconSize,
                tint = colors.contentColor,
            )
        }
    }
}

// MARK: - Color Resolution

private data class IconButtonColorData(
    val backgroundColor: Color,
    val backgroundHoverColor: Color,
    val backgroundPressedColor: Color,
    val contentColor: Color,
)

@Composable
private fun resolveColors(
    variant: LemonadeIconButtonVariant,
    type: LemonadeIconButtonType,
): IconButtonColorData {
    return when (variant) {
        LemonadeIconButtonVariant.Primary -> when (type) {
            LemonadeIconButtonType.Solid -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgBrand,
                backgroundHoverColor = LocalColors.current.interaction.bgBrandInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgBrandPressed,
                contentColor = LocalColors.current.content.contentOnBrandHigh,
            )
            LemonadeIconButtonType.Subtle -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgBrandSubtle,
                backgroundHoverColor = LocalColors.current.interaction.bgSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgSubtlePressed,
                contentColor = LocalColors.current.content.contentBrandHigh,
            )
            LemonadeIconButtonType.Ghost -> IconButtonColorData(
                backgroundColor = Color.Transparent,
                backgroundHoverColor = LocalColors.current.interaction.bgSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgSubtlePressed,
                contentColor = LocalColors.current.content.contentBrandHigh,
            )
        }

        LemonadeIconButtonVariant.Secondary -> when (type) {
            LemonadeIconButtonType.Solid -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgSubtleInverse,
                backgroundHoverColor = LocalColors.current.interaction.bgNeutralInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralPressed,
                contentColor = LocalColors.current.content.contentPrimaryInverse,
            )
            LemonadeIconButtonType.Subtle -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgNeutralSubtle,
                backgroundHoverColor = LocalColors.current.interaction.bgNeutralSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
                contentColor = LocalColors.current.content.contentPrimary,
            )
            LemonadeIconButtonType.Ghost -> IconButtonColorData(
                backgroundColor = Color.Transparent,
                backgroundHoverColor = LocalColors.current.interaction.bgSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
                contentColor = LocalColors.current.content.contentPrimary,
            )
        }

        LemonadeIconButtonVariant.Neutral -> when (type) {
            LemonadeIconButtonType.Solid -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgElevated,
                backgroundHoverColor = LocalColors.current.interaction.bgElevatedInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgElevatedPressed,
                contentColor = LocalColors.current.content.contentPrimary,
            )
            LemonadeIconButtonType.Subtle -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgNeutralSubtle,
                backgroundHoverColor = LocalColors.current.interaction.bgNeutralSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
                contentColor = LocalColors.current.content.contentPrimary,
            )
            LemonadeIconButtonType.Ghost -> IconButtonColorData(
                backgroundColor = Color.Transparent,
                backgroundHoverColor = LocalColors.current.interaction.bgSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
                contentColor = LocalColors.current.content.contentPrimary,
            )
        }

        LemonadeIconButtonVariant.Critical -> when (type) {
            LemonadeIconButtonType.Solid -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgCritical,
                backgroundHoverColor = LocalColors.current.interaction.bgCriticalInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgCriticalPressed,
                contentColor = LocalColors.current.content.contentAlwaysLight,
            )
            LemonadeIconButtonType.Subtle -> IconButtonColorData(
                backgroundColor = LocalColors.current.background.bgCriticalSubtle,
                backgroundHoverColor = LocalColors.current.interaction.bgCriticalSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgCriticalSubtlePressed,
                contentColor = LocalColors.current.content.contentCritical,
            )
            LemonadeIconButtonType.Ghost -> IconButtonColorData(
                backgroundColor = Color.Transparent,
                backgroundHoverColor = LocalColors.current.interaction.bgSubtleInteractive,
                backgroundPressedColor = LocalColors.current.interaction.bgCriticalSubtlePressed,
                contentColor = LocalColors.current.content.contentCritical,
            )
        }
    }
}

// MARK: - Size Data

private data class IconButtonSizeData(
    val iconSize: LemonadeAssetSize,
    val spinnerSize: LemonadeAssetSize,
    val innerPaddings: Dp,
    val shape: Shape,
)

@Composable
private fun LemonadeIconButtonSize.toSizeData(
    shape: LemonadeIconButtonShape,
): IconButtonSizeData {
    return when (this) {
        LemonadeIconButtonSize.Large -> IconButtonSizeData(
            iconSize = LemonadeAssetSize.Large,
            spinnerSize = LemonadeAssetSize.Small,
            innerPaddings = LocalSpaces.current.spacing400,
            shape = shape.resolveShape(roundedShape = LocalShapes.current.radius400),
        )

        LemonadeIconButtonSize.Medium -> IconButtonSizeData(
            iconSize = LemonadeAssetSize.Large,
            spinnerSize = LemonadeAssetSize.Small,
            innerPaddings = LocalSpaces.current.spacing200,
            shape = shape.resolveShape(roundedShape = LocalShapes.current.radius300),
        )

        LemonadeIconButtonSize.Small -> IconButtonSizeData(
            iconSize = LemonadeAssetSize.Small,
            spinnerSize = LemonadeAssetSize.XSmall,
            innerPaddings = LocalSpaces.current.spacing200,
            shape = shape.resolveShape(roundedShape = LocalShapes.current.radius300),
        )
    }
}

@Composable
private fun LemonadeIconButtonShape.resolveShape(roundedShape: Shape): Shape {
    return when (this) {
        LemonadeIconButtonShape.Rounded -> roundedShape
        LemonadeIconButtonShape.Circular -> LocalShapes.current.radiusFull
    }
}

// MARK: - Previews

private data class IconButtonPreviewData(
    val size: LemonadeIconButtonSize,
    val variant: LemonadeIconButtonVariant,
    val type: LemonadeIconButtonType,
    val enabled: Boolean,
)

private class IconButtonPreviewProvider : PreviewParameterProvider<IconButtonPreviewData> {
    override val values: Sequence<IconButtonPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<IconButtonPreviewData> {
        return buildList {
            listOf(true, false).forEach { enabled ->
                LemonadeIconButtonSize.entries.forEach { size ->
                    LemonadeIconButtonVariant.entries.forEach { variant ->
                        LemonadeIconButtonType.entries.forEach { type ->
                            add(
                                element = IconButtonPreviewData(
                                    size = size,
                                    variant = variant,
                                    type = type,
                                    enabled = enabled,
                                ),
                            )
                        }
                    }
                }
            }
        }.asSequence()
    }
}

@Composable
@LemonadePreview
private fun IconButtonPreview(
    @PreviewParameter(IconButtonPreviewProvider::class)
    previewData: IconButtonPreviewData,
) {
    LemonadeUi.IconButton(
        icon = LemonadeIcons.Heart,
        size = previewData.size,
        variant = previewData.variant,
        type = previewData.type,
        enabled = previewData.enabled,
        contentDescription = null,
        onClick = {},
    )
}
