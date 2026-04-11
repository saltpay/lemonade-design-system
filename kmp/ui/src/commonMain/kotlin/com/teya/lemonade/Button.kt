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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonType
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTextStyle

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
 * @param variant - [LemonadeButtonVariant] for the color palette (Primary, Secondary, Neutral, Critical).
 * @param type - [LemonadeButtonType] for the fill treatment (Solid, Subtle, Ghost).
 * @param size - [LemonadeButtonSize] to size the Button accordingly.
 * @param modifier - [Modifier] to be applied to the Button.
 * @param enabled - [Boolean] flag to enable or disable the Button.
 * @param loading - [Boolean] flag to enable the loading state.
 * @param interactionSource - [MutableInteractionSource] to be applied to the Button.
 */
@Composable
public fun LemonadeUi.Button(
    label: String,
    onClick: () -> Unit,
    leadingIcon: LemonadeIcons? = null,
    trailingIcon: LemonadeIcons? = null,
    variant: LemonadeButtonVariant = LemonadeButtonVariant.Primary,
    type: LemonadeButtonType = LemonadeButtonType.Solid,
    size: LemonadeButtonSize = LemonadeButtonSize.Large,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = resolveButtonColors(
        variant = variant,
        type = type,
    )
    CoreButton(
        colors = colors,
        size = size,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick,
        loading = loading,
        leadingSlot = null,
        trailingSlot = null,
        expandContents = false,
        contentSlot = {
            if (loading) {
                LemonadeUi.Spinner(
                    tint = colors.contentColor,
                )
            } else {
                if (leadingIcon != null) {
                    LemonadeUi.Icon(
                        icon = leadingIcon,
                        tint = colors.contentColor,
                        contentDescription = null,
                    )
                }

                LemonadeUi.Text(
                    text = label,
                    textStyle = size.contentData.textStyle,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing200),
                )

                if (trailingIcon != null) {
                    LemonadeUi.Icon(
                        icon = trailingIcon,
                        tint = colors.contentColor,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}

/**
 * Lemonade labeled button component with slot-based leading and trailing content.
 *
 * @param label - [String] to be displayed as the Button's label.
 * @param onClick - Callback to be invoked when the Button is clicked.
 * @param modifier - [Modifier] to be applied to the Button.
 * @param variant - [LemonadeButtonVariant] for the color palette (Primary, Secondary, Neutral, Critical).
 * @param type - [LemonadeButtonType] for the fill treatment (Solid, Subtle, Ghost).
 * @param size - [LemonadeButtonSize] to size the Button accordingly.
 * @param leadingSlot - Optional composable slot shown before the label.
 * @param trailingSlot - Optional composable slot shown after the label.
 * @param expandContents - [Boolean] flag to expand the content area.
 * @param enabled - [Boolean] flag to enable or disable the Button.
 * @param loading - [Boolean] flag to enable the loading state.
 * @param interactionSource - [MutableInteractionSource] to be applied to the Button.
 */
@Composable
public fun LemonadeUi.Button(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: LemonadeButtonVariant = LemonadeButtonVariant.Primary,
    type: LemonadeButtonType = LemonadeButtonType.Solid,
    size: LemonadeButtonSize = LemonadeButtonSize.Large,
    leadingSlot: (@Composable RowScope.(colors: LemonadeButtonColors) -> Unit)? = null,
    trailingSlot: (@Composable RowScope.(colors: LemonadeButtonColors) -> Unit)? = null,
    expandContents: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = resolveButtonColors(
        variant = variant,
        type = type,
    )
    CoreButton(
        colors = colors,
        size = size,
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick,
        loading = loading,
        modifier = modifier,
        leadingSlot = leadingSlot.takeIf { !loading },
        trailingSlot = trailingSlot.takeIf { !loading },
        expandContents = expandContents,
        contentSlot = {
            if (loading) {
                LemonadeUi.Spinner(
                    tint = colors.contentColor,
                )
            } else {
                LemonadeUi.Text(
                    text = label,
                    textStyle = size.contentData.textStyle,
                    color = colors.contentColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing200),
                )
            }
        },
    )
}

@Stable
public class LemonadeButtonColors internal constructor(
    public val contentColor: Color,
    public val solidBackgroundColor: Color,
    public val pressedBackgroundColor: Color,
)

@Stable
private data class LemonadeButtonContentData(
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val requiredHeight: Dp,
    val minWidth: Dp,
    val shape: Shape,
    val textStyle: LemonadeTextStyle,
)

private val LemonadeButtonSize.contentData: LemonadeButtonContentData
    @Composable get() {
        return when (this) {
            LemonadeButtonSize.XSmall -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing100,
                horizontalPadding = LocalSpaces.current.spacing200,
                requiredHeight = LocalSizes.current.size1000,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius200,
                textStyle = LocalTypographies.current.bodySmallSemiBold,
            )

            LemonadeButtonSize.Small -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing200,
                horizontalPadding = LocalSpaces.current.spacing300,
                requiredHeight = LocalSizes.current.size1000,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius300,
                textStyle = LocalTypographies.current.bodySmallSemiBold,
            )

            LemonadeButtonSize.Medium -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing300,
                horizontalPadding = LocalSpaces.current.spacing400,
                requiredHeight = LocalSizes.current.size1200,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius300,
                textStyle = LocalTypographies.current.bodyMediumSemiBold,
            )

            LemonadeButtonSize.Large -> LemonadeButtonContentData(
                verticalPadding = LocalSpaces.current.spacing300,
                horizontalPadding = LocalSpaces.current.spacing400,
                requiredHeight = LocalSizes.current.size1400,
                minWidth = LocalSizes.current.size1600,
                shape = LocalShapes.current.radius400,
                textStyle = LocalTypographies.current.bodyMediumSemiBold,
            )
        }
    }

// MARK: - Color Resolution

@Composable
private fun resolveButtonColors(
    variant: LemonadeButtonVariant,
    type: LemonadeButtonType,
): LemonadeButtonColors {
    return when (variant) {
        LemonadeButtonVariant.Primary -> when (type) {
            LemonadeButtonType.Solid -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentOnBrandHigh,
                solidBackgroundColor = LocalColors.current.background.bgBrand,
                pressedBackgroundColor = LocalColors.current.interaction.bgBrandInteractive,
            )

            LemonadeButtonType.Subtle -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentBrandHigh,
                solidBackgroundColor = LocalColors.current.background.bgBrandSubtle,
                pressedBackgroundColor = LocalColors.current.interaction.bgSubtlePressed,
            )

            LemonadeButtonType.Ghost -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentBrandHigh,
                solidBackgroundColor = Color.Transparent,
                pressedBackgroundColor = LocalColors.current.interaction.bgSubtlePressed,
            )
        }

        LemonadeButtonVariant.Secondary -> when (type) {
            LemonadeButtonType.Solid -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimaryInverse,
                solidBackgroundColor = LocalColors.current.background.bgSubtleInverse,
                pressedBackgroundColor = LocalColors.current.interaction.bgNeutralPressed,
            )

            LemonadeButtonType.Subtle -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimary,
                solidBackgroundColor = LocalColors.current.background.bgNeutralSubtle,
                pressedBackgroundColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
            )

            LemonadeButtonType.Ghost -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimary,
                solidBackgroundColor = Color.Transparent,
                pressedBackgroundColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
            )
        }

        LemonadeButtonVariant.Neutral -> when (type) {
            LemonadeButtonType.Solid -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimary,
                solidBackgroundColor = LocalColors.current.background.bgElevated,
                pressedBackgroundColor = LocalColors.current.interaction.bgElevatedPressed,
            )

            LemonadeButtonType.Subtle -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimary,
                solidBackgroundColor = LocalColors.current.background.bgNeutralSubtle,
                pressedBackgroundColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
            )

            LemonadeButtonType.Ghost -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentPrimary,
                solidBackgroundColor = Color.Transparent,
                pressedBackgroundColor = LocalColors.current.interaction.bgNeutralSubtlePressed,
            )
        }

        LemonadeButtonVariant.Critical -> when (type) {
            LemonadeButtonType.Solid -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentAlwaysLight,
                solidBackgroundColor = LocalColors.current.background.bgCritical,
                pressedBackgroundColor = LocalColors.current.interaction.bgCriticalInteractive,
            )

            LemonadeButtonType.Subtle -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentCritical,
                solidBackgroundColor = LocalColors.current.background.bgCriticalSubtle,
                pressedBackgroundColor = LocalColors.current.interaction.bgCriticalSubtleInteractive,
            )

            LemonadeButtonType.Ghost -> LemonadeButtonColors(
                contentColor = LocalColors.current.content.contentCritical,
                solidBackgroundColor = Color.Transparent,
                pressedBackgroundColor = LocalColors.current.interaction.bgCriticalSubtlePressed,
            )
        }
    }
}

@Composable
private fun CoreButton(
    contentSlot: @Composable RowScope.() -> Unit,
    leadingSlot: (@Composable RowScope.(LemonadeButtonColors) -> Unit)?,
    trailingSlot: (@Composable RowScope.(LemonadeButtonColors) -> Unit)?,
    onClick: () -> Unit,
    colors: LemonadeButtonColors,
    size: LemonadeButtonSize,
    expandContents: Boolean,
    enabled: Boolean,
    loading: Boolean,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            colors.pressedBackgroundColor
        } else {
            colors.solidBackgroundColor
        },
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .defaultMinSize(minWidth = size.contentData.minWidth)
            .requiredHeight(height = size.contentData.requiredHeight)
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
                indication = LocalEffects.current.interactionIndication,
            ).background(color = animatedBackgroundColor),
        content = {
            leadingSlot?.invoke(this, colors)
            Row(
                horizontalArrangement = Arrangement.Center,
                content = contentSlot,
                modifier = Modifier
                    .then(
                        other = if (expandContents) {
                            Modifier.weight(1f)
                        } else {
                            Modifier
                        },
                    ).padding(
                        vertical = size.contentData.verticalPadding,
                        horizontal = size.contentData.horizontalPadding,
                    ),
            )
            trailingSlot?.invoke(this, colors)
        },
    )
}

// MARK: - Previews

private data class ButtonPreviewData(
    val leadingIcon: Boolean,
    val trailingIcon: Boolean,
    val enabled: Boolean,
    val loading: Boolean,
    val size: LemonadeButtonSize,
    val variant: LemonadeButtonVariant,
    val type: LemonadeButtonType,
)

private class ButtonPreviewProvider : PreviewParameterProvider<ButtonPreviewData> {
    override val values: Sequence<ButtonPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<ButtonPreviewData> {
        return buildList {
            LemonadeButtonSize.entries.forEach { size ->
                LemonadeButtonVariant.entries.forEach { variant ->
                    LemonadeButtonType.entries.forEach { type ->
                        listOf(true, false).forEach { leadingIcon ->
                            listOf(true, false).forEach { trailingIcon ->
                                listOf(true, false).forEach { loading ->
                                    listOf(true, false).forEach { enabled ->
                                        add(
                                            element = ButtonPreviewData(
                                                leadingIcon = leadingIcon,
                                                trailingIcon = trailingIcon,
                                                enabled = enabled,
                                                loading = loading,
                                                size = size,
                                                variant = variant,
                                                type = type,
                                            ),
                                        )
                                    }
                                }
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
        type = previewData.type,
    )
}
