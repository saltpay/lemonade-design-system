package com.teya.lemonade

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue

/**
 * The Text Field component allows users to enter or edit text and adapts seamlessly across both
 *  mobile and web platforms. It supports multiple interaction states, sizes, and configurations
 *  to accommodate a wide range of design contexts. The component ensures consistency in form
 *  design, maintaining clarity, accessibility, and usability across devices.
 *
 * This overload takes a [TextFieldState], which owns the text and the selection itself. Prefer it
 * over the [String] and [TextFieldValue] overloads for anything that filters or formats input.
 *
 * [inputTransformation] runs synchronously on every edit — keyboard, IME, paste, accessibility —
 * before the state commits, so a rejected character never reaches the buffer and the field has no
 * intermediate state to re-sync. [outputTransformation] decorates the text for display without
 * putting the decoration in the buffer, so the value stays canonical, the caret maps itself, and
 * the IME session is never restarted (rewriting a [TextFieldValue] on each keystroke restarts the
 * input connection, which is what makes a numeric keyboard flicker back to its letters layer).
 *
 * ## Usage
 * ```kotlin
 * val state = rememberTextFieldState()
 *
 * LemonadeUi.TextField(
 *     state = state,
 *     inputTransformation = InputTransformation.maxLength(34),
 *     outputTransformation = { /* replace(4, 4, " ") */ },
 *     label = "IBAN",
 * )
 * ```
 *
 * Pass stable [inputTransformation] and [outputTransformation] instances — a new instance on each
 * recomposition rebuilds the field's internal transformed state and can strand the input session.
 *
 * @param state - The [TextFieldState] holding the text and selection
 * @param label - Label to be displayed on the top left of the text field
 * @param optionalIndicator - Optional text to be displayed on the top right of the text field
 * @param supportText - Support text to be displayed below the text field
 * @param placeholderText - Placeholder text to be displayed when the text field is empty
 * @param errorMessage - Error message to be displayed when the text field has an error
 * @param interactionSource - [MutableInteractionSource] to be applied to the text field
 * @param keyboardOptions - [KeyboardOptions] to be applied to the text field
 * @param onKeyboardAction - Handler for the keyboard's action key
 * @param inputTransformation - Filters or rewrites each edit before it reaches the buffer
 * @param outputTransformation - Decorates the text for display only
 * @param error - Whether the text field has an error
 * @param enabled - Whether the text field is enabled
 * @param leadingContent - Content to be displayed on the top left of the text field
 * @param trailingContent - Content to be displayed on the top right of the text field
 * @param modifier - [Modifier] to be applied to the root container of the text field
 */
@Composable
public fun LemonadeUi.TextField(
    state: TextFieldState,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    placeholderText: String? = null,
    errorMessage: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onKeyboardAction: KeyboardActionHandler? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (enabled) {
            LocalOpacities.current.base.opacity100
        } else {
            LocalOpacities.current.state.opacityDisabled
        },
    )
    CoreTextField(
        state = state,
        label = label,
        optionalIndicator = optionalIndicator,
        supportText = supportText,
        errorMessage = errorMessage,
        size = null,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        error = error,
        enabled = enabled,
        modifier = modifier,
        textBoxContent = { innerTextField ->
            DefaultTextBox(
                placeholderText = placeholderText,
                size = null,
                showPlaceholder = state.text.isEmpty(),
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                innerTextField = innerTextField,
                modifier = Modifier.alpha(alpha = animatedAlpha),
            )
        },
    )
}

/**
 * [TextFieldState]-based CoreTextField.
 *
 * The decoration is the same tree the other two overloads use — BasicTextField's `decorationBox`
 * and `TextFieldDecorator` are the same inverted slot, so only the wrapper type differs.
 */
@Composable
internal fun CoreTextField(
    state: TextFieldState,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    errorMessage: String? = null,
    size: TextFieldSize? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onKeyboardAction: KeyboardActionHandler? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    error: Boolean = false,
    enabled: Boolean = true,
    textBoxContent: @Composable BoxScope.(@Composable () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = LocalColors.current.content.contentPrimary
    BasicTextField(
        state = state,
        interactionSource = interactionSource,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(contentColor),
        textStyle = size.data.contentStyle.textStyle.copy(
            color = contentColor,
        ),
        modifier = modifier,
        decorator = { innerTextField ->
            CoreTextFieldDecorator(
                label = label,
                optionalIndicator = optionalIndicator,
                supportText = supportText,
                errorMessage = errorMessage,
                size = size,
                interactionSource = interactionSource,
                error = error,
                enabled = enabled,
                textBoxContent = { textBoxContent(innerTextField) },
            )
        },
    )
}
