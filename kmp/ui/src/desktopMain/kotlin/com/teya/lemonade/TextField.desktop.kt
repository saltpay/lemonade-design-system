package com.teya.lemonade

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
public fun LemonadeUi.TextField(
    input: String,
    onInputChanged: (String) -> Unit,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    placeholderText: String? = null,
    errorMessage: String? = null,
    size: TextFieldSize = TextFieldSize.Medium,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: Boolean = false,
    enabled: Boolean = true,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    CoreTextField(
        input = input,
        onInputChanged = onInputChanged,
        label = label,
        optionalIndicator = optionalIndicator,
        supportText = supportText,
        errorMessage = errorMessage,
        size = null,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        error = error,
        enabled = enabled,
        modifier = modifier,
        textBoxContent = { innerTextField ->
            DefaultTextBox(
                placeholderText = placeholderText,
                size = size,
                showPlaceholder = input.isEmpty(),
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                innerTextField = innerTextField,
                modifier = modifier,
            )
        },
    )
}
