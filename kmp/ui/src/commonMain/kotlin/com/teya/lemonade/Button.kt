package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTextStyle
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Lemonade labeled button component. Used for simple click actions with a text and optional icons.
 * ## Usage
 * ```kotlin
 * LemonadeUi.Button(
 *   label = "click me!",
 *   onClick = { println("button clicked!") },
 * )
 * ```
 * @param label - [String] to be displayed as the Button's label.
 * @param onClick - Callback to be invoked when the Button is clicked.
 * @param leadingIcon - [LemonadeIcons] shown before the label.
 * @param trailingIcon - [LemonadeIcons] shown after the label.
 * @param variant - [LemonadeButtonVariant] to style the Button accordingly.
 * @param size - [LemonadeButtonSize] to size the Button accordingly.
 * @param modifier - [Modifier] to be applied to the Button.
 * @param enabled - [Boolean] flag to enable or disable the Button.
 * @param loading - [Boolean] flag to enable the loading state.
 * @param interactionSource - [MutableInteractionSource] to be applied to the Button.
 */
@Composable
@ExperimentalLemonadeComponent
public fun LemonadeUi.Button(
    label: String,
    onClick: () -> Unit,
    leadingIcon: LemonadeIcons? = null,
    trailingIcon: LemonadeIcons? = null,
    variant: LemonadeButtonVariant = LemonadeButtonVariant.Primary,
    size: LemonadeButtonSize = LemonadeButtonSize.Large,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    CoreButton(
        variant = variant,
        size = size,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick,
        loading = loading,
        contentSlot = {
            if (loading) {
                LemonadeUi.Spinner(
                    tint = variant.variantData.contentColor,
                )
            } else {
                if (leadingIcon != null) {
                    LemonadeUi.Icon(
                        icon = leadingIcon,
                        tint = variant.variantData.contentColor,
                        contentDescription = null,
                    )
                }

                LemonadeUi.Text(
                    text = label,
                    textStyle = size.contentData.textStyle,
                    color = variant.variantData.contentColor,
                    modifier = Modifier
                        .padding(horizontal = LocalSpaces.current.spacing200),
                )

                if (trailingIcon != null) {
                    LemonadeUi.Icon(
                        icon = trailingIcon,
                        tint = variant.variantData.contentColor,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}

@Stable
private data class LemonadeButtonColors(
    val contentColor: Color,
    val solidBackgroundColor: Color,
    val pressedBackgroundColor: Color,
    val brushBackgroundColor: Brush? = null,
)

@Stable
private data class LemonadeButtonContentData(
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val minHeight: Dp,
    val minWidth: Dp,
    val shape: Shape,
    val textStyle: LemonadeTextStyle,
)

private val LemonadeButtonSize.contentData: LemonadeButtonContentData
    @Composable get() {
        return when (this) {
            LemonadeButtonSize.Small -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing200,
                horizontalPadding = LocalSpaces.current.spacing300,
                minHeight = LocalSizes.current.size1000,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius300,
                textStyle = LocalTypographies.current.bodySmallSemiBold,
            )

            LemonadeButtonSize.Medium -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing300,
                horizontalPadding = LocalSpaces.current.spacing400,
                minHeight = LocalSizes.current.size1200,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius300,
                textStyle = LocalTypographies.current.bodyMediumSemiBold,
            )

            LemonadeButtonSize.Large -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing300,
                horizontalPadding = LocalSpaces.current.spacing400,
                minHeight = LocalSizes.current.size1400,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius400,
                textStyle = LocalTypographies.current.bodyMediumSemiBold,
            )
        }
    }

private val LemonadeButtonVariant.variantData: LemonadeButtonColors
    @Composable get() {
        return when (this) {
            LemonadeButtonVariant.Primary -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentOnBrandHigh,
                solidBackgroundColor = LocalColors.current.background.bgBrand,
                pressedBackgroundColor = LocalColors.current.interaction.bgBrandInteractive,
            )

            LemonadeButtonVariant.Secondary -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimaryInverse,
                solidBackgroundColor = LocalColors.current.background.bgSubtleInverse,
                pressedBackgroundColor = LocalColors.current.interaction.bgNeutralPressed,
            )

            LemonadeButtonVariant.Neutral -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimary,
                solidBackgroundColor = LocalColors.current.background.bgElevated,
                pressedBackgroundColor = LocalColors.current.interaction.bgElevatedPressed,
            )

            LemonadeButtonVariant.CriticalSubtle -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentCritical,
                solidBackgroundColor = LocalColors.current.background.bgCriticalSubtle,
                pressedBackgroundColor = LocalColors.current.interaction.bgCriticalSubtleInteractive,
            )

            LemonadeButtonVariant.CriticalSolid -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentAlwaysLight,
                solidBackgroundColor = LocalColors.current.background.bgCritical,
                pressedBackgroundColor = LocalColors.current.interaction.bgCriticalInteractive,
            )

            LemonadeButtonVariant.Special -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentOnBrandHigh,
                solidBackgroundColor = LocalColors.current.background.bgBrand,
                pressedBackgroundColor = LocalColors.current.interaction.bgBrandInteractive,
                // TODO: update the gradient colours
                brushBackgroundColor = Brush.horizontalGradient(
                    colors = listOf(
                        LocalColors.current.background.bgBrandElevated,
                        LocalColors.current.background.bgBrandElevated
                            .copy(alpha = 0f),
                    ),
                ),
            )
        }
    }

@Suppress("LongParameterList")
@Composable
private fun CoreButton(
    contentSlot: @Composable RowScope.() -> Unit,
    onClick: () -> Unit,
    variant: LemonadeButtonVariant,
    size: LemonadeButtonSize,
    enabled: Boolean,
    loading: Boolean,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            variant.variantData.pressedBackgroundColor
        } else {
            variant.variantData.solidBackgroundColor
        },
    )

    Row(
        content = contentSlot,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .defaultMinSize(
                minWidth = size.contentData.minWidth,
            ).height(size.contentData.minHeight)
            .then(
                other = if (!enabled) {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                } else {
                    Modifier
                },
            ).clip(shape = size.contentData.shape)
            .clickable(
                enabled = enabled && !loading,
                onClick = onClick,
                interactionSource = interactionSource,
                role = Role.Button,
                indication = null,
            ).background(color = animatedBackgroundColor)
            .then(
                other = variant.variantData.brushBackgroundColor?.let { brush ->
                    Modifier.background(brush = brush)
                }
                    ?: Modifier,
            ).padding(
                vertical = size.contentData.verticalPadding,
                horizontal = size.contentData.horizontalPadding,
            ),
    )
}

private data class ButtonPreviewData(
    val leadingIcon: Boolean,
    val trailingIcon: Boolean,
    val enabled: Boolean,
    val loading: Boolean,
    val size: LemonadeButtonSize,
    val variant: LemonadeButtonVariant,
)

private class ButtonPreviewProvider : PreviewParameterProvider<ButtonPreviewData> {
    override val values: Sequence<ButtonPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<ButtonPreviewData> =
        buildList {
            LemonadeButtonSize.entries.forEach { size ->
                LemonadeButtonVariant.entries.forEach { variant ->
                    listOf(true, false).forEach { leadingIcon ->
                        listOf(true, false).forEach { trailingIcon ->
                            listOf(true, false).forEach { loading ->
                                listOf(true, false).forEach { enabled ->
                                    add(
                                        ButtonPreviewData(
                                            leadingIcon = leadingIcon,
                                            trailingIcon = trailingIcon,
                                            enabled = enabled,
                                            loading = loading,
                                            size = size,
                                            variant = variant,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }.asSequence()
}

@LemonadePreview
@Composable
private fun LemonadeLabeledRadioButtonPreview(
    @PreviewParameter(ButtonPreviewProvider::class)
    previewData: ButtonPreviewData,
) {
    @OptIn(ExperimentalLemonadeComponent::class)
    LemonadeUi.Button(
        label = "Label",
        onClick = { /* Nothing */ },
        leadingIcon = LemonadeIcons.Heart.takeIf { previewData.leadingIcon },
        trailingIcon = LemonadeIcons.Heart.takeIf { previewData.trailingIcon },
        enabled = previewData.enabled,
        loading = previewData.loading,
        size = previewData.size,
        variant = previewData.variant,
    )
}
