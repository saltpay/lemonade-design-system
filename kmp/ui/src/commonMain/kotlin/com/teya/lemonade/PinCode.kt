@file:OptIn(ExperimentalLemonadeComponent::class)

package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadePinCodeVariant

/**
 * A PIN code entry component combining a progress indicator with an input mechanism.
 *
 * The [variant] decides how characters are entered:
 * - [LemonadePinCodeVariant.Numeric] renders a built-in on-screen numpad with an optional
 *   biometry key. [onBiometryClick] is only honored in this variant.
 * - [LemonadePinCodeVariant.Alphanumeric] uses the device's system keyboard; no numpad is shown.
 *
 * The indicator renders [length] cells. When [masked] is true (the default) each entered character
 * shows as a filled dot; when false the typed characters are shown in boxes. The component appends
 * to and removes from [value] internally, never letting it grow past [length]; the secret stays in
 * the caller's hoisted state.
 *
 * The keypad emits the test tags `digit_0`..`digit_9`, `btn_delete` and `btn_biometry`; the
 * alphanumeric hidden field uses `pin_code_field`. These are a stable contract for E2E tests.
 *
 * ## Usage
 * ```kotlin
 * var pin by remember { mutableStateOf("") }
 *
 * LemonadeUi.PinCode(
 *     value = pin,
 *     onValueChange = { pin = it },
 *     onComplete = { code -> /* validate */ },
 * )
 * ```
 *
 * @param value The current entry. The component keeps it clamped to [length].
 * @param onValueChange Called whenever the entry changes (key press, delete, or system keyboard).
 * @param variant Whether input comes from the built-in numpad or the system keyboard.
 * @param length The number of characters to enter. Defaults to 6.
 * @param masked When true the indicator shows dots; when false it shows the typed characters.
 * @param error When true the indicator turns critical and shakes. Re-triggers on each rising edge.
 * @param submitting When true the indicator dims to a filled loading state and input is disabled.
 * @param onBiometryClick Optional biometry key on the numpad. Ignored for the alphanumeric variant.
 * @param onComplete Called once when [value] reaches [length].
 * @param modifier The [Modifier] applied to the root container of the component.
 */
@ExperimentalLemonadeComponent
@Composable
public fun LemonadeUi.PinCode(
    value: String,
    onValueChange: (String) -> Unit,
    variant: LemonadePinCodeVariant = LemonadePinCodeVariant.Numeric,
    length: Int = 6,
    masked: Boolean = true,
    error: Boolean = false,
    submitting: Boolean = false,
    onBiometryClick: (() -> Unit)? = null,
    onComplete: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    require(value = length > 0, lazyMessage = { "PinCode length must be greater than zero." })

    val haptic = LocalHapticFeedback.current
    val shakeOffset = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(error) {
        if (error) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            shakeOffset.snapTo(targetValue = 0f)
            ShakeKeyframes.forEach { target ->
                shakeOffset.animateTo(
                    targetValue = target,
                    animationSpec = tween(durationMillis = ShakeStepMillis, easing = LinearEasing),
                )
            }
        }
    }

    LaunchedEffect(value) {
        if (value.length == length) onComplete?.invoke(value)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing800),
    ) {
        when (variant) {
            LemonadePinCodeVariant.Numeric -> {
                PinCodeIndicator(
                    value = value,
                    length = length,
                    masked = masked,
                    error = error,
                    submitting = submitting,
                    shakeOffset = shakeOffset.value,
                )
                PinCodeNumpad(
                    onDigit = { digit -> if (value.length < length) onValueChange(value + digit) },
                    onDelete = { if (value.isNotEmpty()) onValueChange(value.dropLast(n = 1)) },
                    onBiometryClick = onBiometryClick,
                    enabled = !submitting,
                    hasValue = value.isNotEmpty(),
                )
            }

            LemonadePinCodeVariant.Alphanumeric -> {
                Box(contentAlignment = Alignment.Center) {
                    PinCodeIndicator(
                        value = value,
                        length = length,
                        masked = masked,
                        error = error,
                        submitting = submitting,
                        shakeOffset = shakeOffset.value,
                    )
                    PinCodeHiddenField(
                        value = value,
                        onValueChange = onValueChange,
                        length = length,
                        masked = masked,
                        enabled = !submitting,
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }
        }
    }
}

@Composable
private fun PinCodeIndicator(
    value: String,
    length: Int,
    masked: Boolean,
    error: Boolean,
    submitting: Boolean,
    shakeOffset: Float,
    modifier: Modifier = Modifier,
) {
    val filledCount = value.length.coerceAtMost(maximumValue = length)
    val rowModifier = modifier.offset { IntOffset(x = shakeOffset.dp.roundToPx(), y = 0) }

    if (masked) {
        Row(
            modifier = rowModifier.alpha(alpha = if (submitting) SubmittingAlpha else 1f),
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing600),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(times = length) { index ->
                val filled = submitting || index < filledCount
                val targetColor = when {
                    filled && error -> LocalColors.current.content.contentCritical
                    filled -> LocalColors.current.content.contentPrimary
                    else -> LocalColors.current.border.borderNeutralLow
                }
                val color by animateColorAsState(
                    targetValue = targetColor,
                    animationSpec = tween(durationMillis = DotAnimationMillis),
                )
                Box(
                    modifier = Modifier
                        .size(size = DotSize)
                        .clip(shape = CircleShape)
                        .background(color = color),
                )
            }
        }
    } else {
        Row(
            modifier = rowModifier
                .fillMaxWidth()
                .widthIn(max = IndicatorMaxWidth)
                .padding(horizontal = LocalSpaces.current.spacing400),
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(times = length) { index ->
                val character = value.getOrNull(index = index)
                val isActive = !submitting && index == filledCount
                val borderColor = when {
                    error -> LocalColors.current.border.borderCritical
                    isActive -> LocalColors.current.border.borderSelected
                    else -> LocalColors.current.border.borderNeutralLow
                }
                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = LocalSizes.current.size1600)
                        .clip(shape = LocalShapes.current.radius300)
                        .border(
                            width = LocalBorderWidths.current.base.border25,
                            color = borderColor,
                            shape = LocalShapes.current.radius300,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (character != null) {
                        LemonadeUi.Text(
                            text = character.toString(),
                            textStyle = LocalTypographies.current.headingMedium,
                            color = if (error) {
                                LocalColors.current.content.contentCritical
                            } else {
                                LocalColors.current.content.contentPrimary
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PinCodeNumpad(
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onBiometryClick: (() -> Unit)?,
    enabled: Boolean,
    hasValue: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = NumpadMaxWidth)
            .padding(horizontal = LocalSpaces.current.spacing400),
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing600),
    ) {
        NumpadRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing500),
            ) {
                row.forEach { digit ->
                    NumpadCell {
                        PinCodeKey(
                            label = digit,
                            testId = digitTestId(digit = digit),
                            onClick = { onDigit(digit) },
                            enabled = enabled,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing500),
        ) {
            NumpadCell {
                if (onBiometryClick != null) {
                    PinCodeIconKey(
                        icon = LemonadeIcons.FingerPrint,
                        testId = BiometryTestId,
                        onClick = onBiometryClick,
                        enabled = enabled,
                    )
                } else {
                    Spacer(modifier = Modifier.size(size = LocalSizes.current.size2000))
                }
            }

            NumpadCell {
                PinCodeKey(
                    label = "0",
                    testId = digitTestId(digit = "0"),
                    onClick = { onDigit("0") },
                    enabled = enabled,
                )
            }

            NumpadCell {
                val deleteAlpha by animateFloatAsState(
                    targetValue = if (hasValue) 1f else 0f,
                    animationSpec = tween(durationMillis = DeleteAlphaMillis),
                )
                PinCodeIconKey(
                    icon = LemonadeIcons.Backspace,
                    testId = DeleteTestId,
                    onClick = onDelete,
                    enabled = enabled && hasValue,
                    modifier = Modifier.alpha(alpha = deleteAlpha),
                )
            }
        }
    }
}

@Composable
private fun RowScope.NumpadCell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.weight(weight = 1f),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Composable
private fun PinCodeKey(
    label: String,
    testId: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .testTag(tag = testId)
            .size(size = LocalSizes.current.size2000)
            .clip(shape = CircleShape)
            .border(
                width = LocalBorderWidths.current.base.border25,
                color = LocalColors.current.border.borderNeutralLow,
                shape = CircleShape,
            ).clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalEffects.current.interactionIndication,
                role = Role.Button,
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.displaySmall,
            color = LocalColors.current.content.contentPrimary,
        )
    }
}

@Composable
private fun PinCodeIconKey(
    icon: LemonadeIcons,
    testId: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .testTag(tag = testId)
            .size(size = LocalSizes.current.size2000)
            .clip(shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalEffects.current.interactionIndication,
                role = Role.Button,
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        LemonadeUi.Icon(
            icon = icon,
            contentDescription = null,
            size = LemonadeAssetSize.XLarge,
            tint = LocalColors.current.content.contentTertiary,
        )
    }
}

@Composable
private fun PinCodeHiddenField(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int,
    masked: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    BasicTextField(
        value = value.take(n = length),
        onValueChange = { input -> onValueChange(input.take(n = length)) },
        enabled = enabled,
        singleLine = true,
        cursorBrush = SolidColor(value = Color.Transparent),
        textStyle = LocalTypographies.current.bodyMediumRegular.textStyle
            .copy(color = Color.Transparent),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (masked) KeyboardType.Password else KeyboardType.Ascii,
        ),
        modifier = modifier
            .testTag(tag = FieldTestId)
            .focusRequester(focusRequester = focusRequester),
    )
}

private fun digitTestId(digit: String): String = "digit_$digit"

private const val BiometryTestId = "btn_biometry"
private const val DeleteTestId = "btn_delete"
private const val FieldTestId = "pin_code_field"

private val NumpadRows = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
)
private val ShakeKeyframes = listOf(-10f, 10f, -8f, 8f, -5f, 5f, 0f)

private const val ShakeStepMillis = 50
private const val DotAnimationMillis = 150
private const val DeleteAlphaMillis = 200
private const val SubmittingAlpha = 0.2f

private val DotSize = 16.dp
private val NumpadMaxWidth = 360.dp
private val IndicatorMaxWidth = 360.dp

@LemonadePreview
@Composable
private fun PinCodeNumericMaskedPreview() {
    LemonadeUi.PinCode(
        value = "123",
        onValueChange = { /* preview only */ },
        onBiometryClick = { /* preview only */ },
    )
}

@LemonadePreview
@Composable
private fun PinCodeNumericErrorPreview() {
    LemonadeUi.PinCode(
        value = "123",
        onValueChange = { /* preview only */ },
        error = true,
    )
}

@LemonadePreview
@Composable
private fun PinCodeNumericSubmittingPreview() {
    LemonadeUi.PinCode(
        value = "123456",
        onValueChange = { /* preview only */ },
        submitting = true,
    )
}

@LemonadePreview
@Composable
private fun PinCodeAlphanumericUnmaskedPreview() {
    LemonadeUi.PinCode(
        value = "aB3",
        onValueChange = { /* preview only */ },
        variant = LemonadePinCodeVariant.Alphanumeric,
        masked = false,
    )
}
