package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTextStyle
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A compact element used to display information, trigger actions, or represent selections.
 *  Commonly used for tags, filters, or interactive choices in dense interfaces.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Chip(
 *     label = "Label",
 *     selected = true,
 *     leadingPainter = painterResource(resource = LemonadeIcons.Airplane.drawableResource),
 * )
 * ```
 *
 * ## Parameters
 * @param label: The text to be displayed in the chip.
 * @param selected: Set to 'true' if the chip is in the selected state.
 * @param modifier: Optional - [Modifier] to be applied to the root container of the chip.
 * @param counter: Optional - [Int] number to be displayed in the chip
 *  in case it is counting the amount of a subject.
 * @param leadingPainter: Optional - [Painter] to be displayed
 *  in the leading position of the chip.
 * @param trailingIcon: Optional - [LemonadeIcons] to be displayed
 *  in the trailing position of the chip.
 * @param enabled: Optional - controls the enabled state of the chip.
 *  When `false`, interaction is disabled and it is visually styled
 *  as such. Defaults to true.
 * @param onChipClicked: Optional - sets the callback for when
 *  the chip is clicked. If null the clickable interactions will be
 *  automatically disabled.
 * @param onTrailingIconClick: Optional - Callback action triggered
 *  when the [trailingIcon] is clicked.
 *  Needs [trailingIcon] to not be null.
 * @param interactionSource: Optional - [MutableInteractionSource]
 *  used to observe interaction states like hover and press to drive
 *  visual feedback.
 */
@Composable
public fun LemonadeUi.Chip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    leadingPainter: Painter? = null,
    trailingIcon: LemonadeIcons? = null,
    counter: Int? = null,
    enabled: Boolean = true,
    onChipClicked: (() -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    CoreChip(
        label = label,
        selected = selected,
        enabled = enabled,
        counter = counter,
        leadingSlot = if (leadingPainter != null) {
            {
                Image(
                    painter = leadingPainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(shape = LocalShapes.current.radiusFull)
                        .border(
                            width = LocalBorderWidths.current.base.border25,
                            shape = LocalShapes.current.radiusFull,
                            color = LocalColors.current.border.borderNeutralMedium,
                        ).requiredSize(size = 20.dp),
                )
            }
        } else {
            null
        },
        trailingSlot = if (trailingIcon != null) {
            {
                LemonadeUi.Icon(
                    icon = trailingIcon,
                    tint = LocalChipContentColor.current.invoke(),
                    size = LemonadeAssetSize.Small,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        onChipClicked = onChipClicked,
        onTrailingIconClick = onTrailingIconClick,
        modifier = modifier,
        interactionSource = interactionSource,
    )
}

/**
 * A compact element used to display information, trigger actions, or represent selections.
 *  Commonly used for tags, filters, or interactive choices in dense interfaces.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Chip(
 *     label = "Label",
 *     selected = true,
 *     leadingIcon = LemonadeIcons.Airplane,
 * )
 * ```
 *
 * ## Parameters
 * @param label: The text to be displayed in the chip.
 * @param selected: Set to 'true' if the chip is in the selected state.
 * @param modifier: Optional - [Modifier] to be applied to the root container of the chip.
 * @param counter: Optional - [Int] number to be displayed in the chip
 *  in case it is counting the amount of a subject.
 * @param leadingIcon: Optional - [LemonadeIcons] to be displayed
 *  in the leading position of the chip.
 * @param trailingIcon: Optional - [LemonadeIcons] to be displayed
 *  in the trailing position of the chip.
 * @param enabled: Optional - controls the enabled state of the chip.
 *  When `false`, interaction is disabled and it is visually styled
 *  as such. Defaults to true.
 * @param onChipClicked: Optional - sets the callback for when
 *  the chip is clicked. If null the clickable interactions will be
 *  automatically disabled.
 * @param onTrailingIconClick: Optional - Callback action triggered
 *  when the [trailingIcon] is clicked.
 *  Needs [trailingIcon] to not be null.
 * @param interactionSource: Optional - [MutableInteractionSource]
 *  used to observe interaction states like hover and press to drive
 *  visual feedback.
 */
@Composable
public fun LemonadeUi.Chip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    leadingIcon: LemonadeIcons? = null,
    trailingIcon: LemonadeIcons? = null,
    counter: Int? = null,
    enabled: Boolean = true,
    onChipClicked: (() -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    CoreChip(
        label = label,
        selected = selected,
        enabled = enabled,
        counter = counter,
        leadingSlot = if (leadingIcon != null) {
            {
                LemonadeUi.Icon(
                    icon = leadingIcon,
                    tint = LocalChipContentColor.current.invoke(),
                    size = LemonadeAssetSize.Small,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        trailingSlot = if (trailingIcon != null) {
            {
                LemonadeUi.Icon(
                    icon = trailingIcon,
                    tint = LocalChipContentColor.current.invoke(),
                    size = LemonadeAssetSize.Small,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        onChipClicked = onChipClicked,
        onTrailingIconClick = onTrailingIconClick,
        modifier = modifier,
        interactionSource = interactionSource,
    )
}

@Suppress("LongMethod", "LongParameterList")
@Composable
internal fun CoreChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    counter: Int?,
    leadingSlot: (@Composable BoxScope.() -> Unit)?,
    trailingSlot: (@Composable BoxScope.() -> Unit)?,
    onChipClicked: (() -> Unit)?,
    onTrailingIconClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val platformDimensions = chipPlatformDimensions
    val props = getChipProps(selected = selected)

    val isHover by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedBorderColor by animateColorAsState(targetValue = props.borderColor)
    val animatedContentColor by animateColorAsState(targetValue = props.contentColor)
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isHover || isPressed) {
            props.pressedBackgroundColor
        } else {
            props.backgroundColor
        },
    )
    CompositionLocalProvider(LocalChipContentColor provides { animatedContentColor }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .then(
                    other = if (!enabled) {
                        Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                    } else {
                        Modifier
                    },
                ).clip(shape = LocalShapes.current.radiusFull)
                .defaultMinSize(
                    minWidth = platformDimensions.minSize.width,
                    minHeight = platformDimensions.minSize.height,
                ).clickable(
                    role = Role.Button,
                    enabled = onChipClicked != null && enabled,
                    onClick = { onChipClicked?.invoke() },
                    interactionSource = interactionSource,
                    indication = null,
                ).border(
                    color = animatedBorderColor,
                    shape = LocalShapes.current.radiusFull,
                    width = 1.dp,
                ).background(
                    color = animatedBackgroundColor,
                    shape = LocalShapes.current.radiusFull,
                ).padding(
                    horizontal = LocalSpaces.current.spacing200,
                    vertical = LocalSpaces.current.spacing100,
                ),
        ) {
            if (leadingSlot != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    content = leadingSlot,
                    modifier = Modifier
                        .requiredSize(size = platformDimensions.actionsSize),
                )
            }

            LemonadeUi.Text(
                text = label,
                color = animatedContentColor,
                textStyle = platformDimensions.labelFontStyle,
                modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing100),
            )

            if (counter != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = LocalSpaces.current.spacing100)
                        .defaultMinSize(
                            minWidth = 18.dp,
                            minHeight = 16.dp,
                        ).background(
                            color = LocalColors.current.background.bgBrand,
                            shape = LocalShapes.current.radiusFull,
                        ).padding(horizontal = LocalSpaces.current.spacing100),
                ) {
                    LemonadeUi.Text(
                        text = counter.toString(),
                        maxLines = 1,
                        textStyle = platformDimensions.counterFontStyle.textStyle.copy(
                            textAlign = TextAlign.Center,
                            color = LocalColors.current.content.contentOnBrandHigh,
                        ),
                    )
                }
            }

            if (trailingSlot != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    content = trailingSlot,
                    modifier = Modifier
                        .padding(start = LocalSpaces.current.spacing50)
                        .then(
                            other = if (onTrailingIconClick != null) {
                                Modifier.clickable(
                                    onClick = onTrailingIconClick,
                                    role = Role.Button,
                                    interactionSource = interactionSource,
                                    indication = null,
                                )
                            } else {
                                Modifier
                            },
                        ).requiredSize(size = platformDimensions.actionsSize),
                )
            }
        }
    }
}

private val LocalChipContentColor: ProvidableCompositionLocal<@Composable () -> Color> =
    staticCompositionLocalOf {
        { LocalColors.current.content.contentPrimary }
    }

internal expect val chipPlatformDimensions: ChipPlatformDimensions

internal data class ChipPlatformDimensions(
    val labelFontStyle: LemonadeTextStyle,
    val counterFontStyle: LemonadeTextStyle,
    val actionsSize: Dp,
    val minSize: DpSize,
)

@Composable
internal fun defaultChipDimensions(): ChipPlatformDimensions =
    ChipPlatformDimensions(
        labelFontStyle = LocalTypographies.current.bodySmallMedium,
        counterFontStyle = LocalTypographies.current.bodyXSmallSemiBold,
        actionsSize = 16.dp,
        minSize = DpSize(
            width = 64.dp,
            height = 32.dp,
        ),
    )

private data class ChipProps(
    val backgroundColor: Color,
    val pressedBackgroundColor: Color,
    val contentColor: Color,
    val actionColor: Color,
    val borderColor: Color,
)

@Composable
private fun getChipProps(selected: Boolean): ChipProps =
    if (selected) {
        ChipProps(
            backgroundColor = LocalColors.current.background.bgBrandHigh,
            pressedBackgroundColor = LocalColors.current.interaction.bgBrandHighInteractive,
            contentColor = LocalColors.current.content.contentBrandInverse,
            actionColor = LocalColors.current.content.contentBrandInverse,
            borderColor = LocalColors.current.border.borderNeutralMedium,
        )
    } else {
        ChipProps(
            backgroundColor = LocalColors.current.background.bgDefault,
            pressedBackgroundColor = LocalColors.current.interaction.bgSubtleInteractive,
            contentColor = LocalColors.current.content.contentPrimary,
            actionColor = LocalColors.current.content.contentSecondary,
            borderColor = LocalColors.current.border.borderNeutralMedium,
        )
    }

private data class ChipPreviewData(
    val counter: Int?,
    val isSelected: Boolean,
    val enabled: Boolean,
    val leadingIcon: LemonadeIcons?,
    val trailingIcon: LemonadeIcons?,
)

private class ChipPreviewProvider : PreviewParameterProvider<ChipPreviewData> {
    override val values: Sequence<ChipPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<ChipPreviewData> =
        buildList {
            listOf(true, false).forEach { enabled ->
                listOf(true, false).forEach { withCounter ->
                    listOf(true, false).forEach { selected ->
                        listOf(true, false).forEach { withLeadingIcon ->
                            listOf(true, false).forEach { withTrailingIcon ->
                                add(
                                    ChipPreviewData(
                                        isSelected = selected,
                                        enabled = enabled,
                                        counter = 5.takeIf { withCounter },
                                        leadingIcon = LemonadeIcons.Airplane.takeIf { withLeadingIcon },
                                        trailingIcon = LemonadeIcons.Airplane.takeIf { withTrailingIcon },
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }.asSequence()
}

@LemonadePreview
@Composable
private fun ChipPreview(
    @PreviewParameter(ChipPreviewProvider::class)
    previewData: ChipPreviewData,
) {
    LemonadeUi.Chip(
        label = "Label",
        selected = previewData.isSelected,
        enabled = previewData.enabled,
        counter = previewData.counter,
        leadingIcon = previewData.leadingIcon,
        trailingIcon = previewData.trailingIcon,
    )
}
