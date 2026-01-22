package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeCountryFlags
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeTextStyle
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * The Text Field component allows users to enter or edit text and adapts seamlessly across both
 *  mobile and web platforms. It supports multiple interaction states, sizes, and configurations
 *  to accommodate a wide range of design contexts. The component ensures consistency in form
 *  design, maintaining clarity, accessibility, and usability across devices.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.TextField(
 *     input = "Sample text",
 *     onInputChange = { /* Nothing */ },
 *     enabled = true,
 *     error = false,
 *     label = "Label",
 *     supportText = "Support Text",
 *     optionalIndicator = "Optional",
 *     leadingContent = {
 * *       LemonadeUi.Icon(
 * *           icon = LemonadeIcons.PadlockOpen,
 * *           contentDescription = null,
 * *       )
 * *   },
 * )
 * ```
 * @param input - The inputted text
 * @param onInputChanged - The callback called when the user inputs content into the text box
 * @param label - Label to be displayed on the top left of the text field
 * @param optionalIndicator - Optional text to be displayed on the top right of the text field
 * @param supportText - Support text to be displayed below the text field
 * @param placeholderText - Placeholder text to be displayed when the text field is empty
 * @param errorMessage - Error message to be displayed when the text field has an error
 * @param interactionSource - [MutableInteractionSource] to be applied to the text field
 * @param keyboardActions - [KeyboardActions] to be applied to the text field
 * @param keyboardOptions - [KeyboardOptions] to be applied to the text field
 * @param visualTransformation - [VisualTransformation] to be applied to the text field
 * @param error - Whether the text field has an error
 * @param enabled - Whether the text field is enabled
 * @param leadingContent - Content to be displayed on the top left of the text field
 * @param trailingContent - Content to be displayed on the top right of the text field
 * @param modifier - [Modifier] to be applied to the root container of the text field
 */
@Composable
public fun LemonadeUi.TextField(
    input: String,
    onInputChanged: (String) -> Unit,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    placeholderText: String? = null,
    errorMessage: String? = null,
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
    val animatedAlpha by animateFloatAsState(
        targetValue = if (enabled) {
            LocalOpacities.current.base.opacity100
        } else {
            LocalOpacities.current.state.opacityDisabled
        },
    )
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
                size = null,
                showPlaceholder = input.isEmpty(),
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                innerTextField = innerTextField,
                modifier = Modifier.alpha(alpha = animatedAlpha),
            )
        }
    )
}

/**
 * A text input combined with a selectable element, allowing users to choose a prefix or
 *  category (e.g., country code) before entering text. Ideal for structured inputs like phone
 *  numbers or units.
 *
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.TextFieldWithSelector(
 *     input = "Sample text",
 *     onInputChanged = { /* Nothing */ },
 *     enabled = true,
 *     error = false,
 *     label = "Label",
 *     supportText = "Support Text",
 *     optionalIndicator = "Optional",
 *     leadingAction = { /* handle leadingContent clicked */ }
 *     leadingContent = {
 *         Row(
 *             horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
 *             verticalAlignment = Alignment.CenterVertically,
 *             modifier = Modifier.padding(all = LemonadeTheme.spaces.spacing400),
 *         ) {
 *             LemonadeUi.CountryFlag(flag = LemonadeCountryFlags.BRBrazil)
 *             LemonadeUi.Text(text = "Brazil")
 *             LemonadeUi.Icon(
 *                 icon = LemonadeIcons.ChevronDown,
 *                 contentDescription = null,
 *             )
 *         }
 *     },
 * )
 * ```
 * @param input - The inputted text
 * @param onInputChanged - The callback called when the user inputs content into the text box
 * @param leadingAction - Action triggered when the leading [leadingContent] is clicked
 * @param leadingContent - Content to be displayed on the top left of the text field
 * @param label - Label to be displayed on the top left of the text field
 * @param optionalIndicator - Optional text to be displayed on the top right of the text field
 * @param supportText - Support text to be displayed below the text field
 * @param placeholderText - Placeholder text to be displayed when the text field is empty
 * @param errorMessage - Error message to be displayed when the text field has an error
 * @param interactionSource - [MutableInteractionSource] to be applied to the text field
 * @param keyboardActions - [KeyboardActions] to be applied to the text field
 * @param keyboardOptions - [KeyboardOptions] to be applied to the text field
 * @param visualTransformation - [VisualTransformation] to be applied to the text field
 * @param error - Whether the text field has an error
 * @param enabled - Whether the text field is enabled
 * @param trailingContent - Content to be displayed on the top right of the text field
 * @param modifier - [Modifier] to be applied to the root container of the text field
 */
@Composable
public fun LemonadeUi.TextFieldWithSelector(
    input: String,
    onInputChanged: (String) -> Unit,
    leadingAction: () -> Unit,
    leadingContent: @Composable BoxScope.() -> Unit,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    placeholderText: String? = null,
    errorMessage: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: Boolean = false,
    enabled: Boolean = true,
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
        modifier = modifier.clearFocusOnKeyboardDismiss(),
        textBoxContent = { innerTextField ->
            DefaultTextFieldWithSelector(
                placeholderText = placeholderText,
                size = null,
                showPlaceholder = input.isEmpty(),
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                innerTextField = innerTextField,
                leadingAction = leadingAction,
                interactionSource = interactionSource,
                enabled = enabled,
            )
        }
    )
}

@Composable
internal fun CoreTextField(
    input: String,
    onInputChanged: (String) -> Unit,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    errorMessage: String? = null,
    size: TextFieldSize? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: Boolean = false,
    enabled: Boolean = true,
    textBoxContent: @Composable BoxScope.(@Composable () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = input,
        onValueChange = onInputChanged,
        interactionSource = interactionSource,
        enabled = enabled,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        textStyle = size.data.contentStyle.textStyle,
        singleLine = true,
        modifier = modifier,
        decorationBox = { innerTextField ->
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
        }
    )
}

@Composable
private fun CoreTextFieldDecorator(
    label: String?,
    optionalIndicator: String?,
    supportText: String?,
    errorMessage: String?,
    size: TextFieldSize?,
    interactionSource: MutableInteractionSource,
    error: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    textBoxContent: @Composable BoxScope.() -> Unit,
) {
    val isHovering by interactionSource.collectIsHoveredAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val animatedBackgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> LocalColors.current.background.bgElevated
            error && !isFocused -> LocalColors.current.background.bgCriticalSubtle
            isHovering -> LocalColors.current.interaction.bgSubtleInteractive
            else -> LocalColors.current.background.bgSubtle.copy(
                alpha = LocalOpacities.current.base.opacity0,
            )
        }
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            !enabled -> LocalColors.current.border.borderNeutralMedium.copy(
                alpha = LocalOpacities.current.base.opacity0,
            )

            isFocused -> LocalColors.current.border.borderSelected
            error && !isFocused -> LocalColors.current.border.borderCritical
            else -> LocalColors.current.border.borderNeutralMedium
        },
    )
    val focusShadowBorderWidth by animateDpAsState(
        targetValue = if (isFocused) {
            LocalBorderWidths.current.base.border50
        } else {
            LocalBorderWidths.current.base.border0
        },
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing50),
        modifier = modifier,
    ) {
        if (label != null || optionalIndicator != null) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
                modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing50),
            ) {
                if (label != null) {
                    LemonadeUi.Text(
                        text = label,
                        textStyle = LocalTypographies.current.bodySmallMedium,
                        modifier = Modifier.weight(weight = 1f),
                        color = if (enabled) {
                            LocalColors.current.content.contentPrimary
                        } else {
                            LocalColors.current.content.contentSecondary
                        },
                    )
                } else {
                    Spacer(modifier = Modifier.weight(weight = 1f))
                }

                if (optionalIndicator != null) {
                    LemonadeUi.Text(
                        text = optionalIndicator,
                        textStyle = LocalTypographies.current.bodySmallRegular,
                        color = LocalColors.current.content.contentSecondary,
                    )
                }
            }
        }

        Box(
            content = textBoxContent,
            modifier = Modifier
                .then(
                    other = size.data.minHeight
                        ?.let { minHeight -> Modifier.requiredHeight(height = minHeight) }
                        ?: Modifier,
                )
                .shadowBorder(
                    width = focusShadowBorderWidth,
                    shape = size.data.containerShape,
                    color = LocalColors.current.background.bgElevated,
                )
                .border(
                    color = animatedBorderColor,
                    width = LocalBorderWidths.current.base.border25,
                    shape = size.data.containerShape,
                )
                .background(
                    color = LocalColors.current.background.bgDefault,
                    shape = size.data.containerShape,
                )
                .background(
                    color = animatedBackgroundColor,
                    shape = size.data.containerShape,
                )
        )

        when {
            enabled && error && errorMessage != null -> {
                LemonadeUi.Text(
                    text = errorMessage,
                    textStyle = LocalTypographies.current.bodyXSmallRegular,
                    color = LocalColors.current.content.contentCritical,
                    modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing50),
                )
            }

            supportText != null -> {
                LemonadeUi.Text(
                    text = supportText,
                    textStyle = LocalTypographies.current.bodyXSmallRegular,
                    color = LocalColors.current.content.contentSecondary,
                    modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing50),
                )
            }
        }
    }
}

@Composable
internal fun BoxScope.DefaultTextFieldWithSelector(
    placeholderText: String?,
    size: TextFieldSize?,
    showPlaceholder: Boolean,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    leadingAction: () -> Unit,
    leadingContent: @Composable BoxScope.() -> Unit,
    trailingContent: (@Composable RowScope.() -> Unit)?,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (enabled) {
            LocalOpacities.current.base.opacity100
        } else {
            LocalOpacities.current.state.opacityDisabled
        },
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.align(alignment = Alignment.CenterStart),
    ) {
        Box(
            content = leadingContent,
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .alpha(alpha = animatedAlpha)
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    onClick = leadingAction,
                    indication = null,
                )
                .defaultMinSize(
                    minHeight = size.data.minHeight ?: Dp.Unspecified,
                ),
        )

        Box(
            modifier = Modifier
                .background(color = LocalColors.current.border.borderNeutralMedium)
                .width(width = LocalBorderWidths.current.base.border25)
                .defaultMinSize(
                    minHeight = size.data.minHeight ?: Dp.Unspecified,
                ),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = size.data.itemsSpacing),
            modifier = Modifier
                .alpha(alpha = animatedAlpha)
                .weight(weight = 1f)
                .padding(
                    horizontal = size.data.horizontalPadding,
                    vertical = size.data.verticalPadding,
                ),
        ) {
            if (showPlaceholder && placeholderText != null) {
                LemonadeUi.Text(
                    text = placeholderText,
                    textStyle = size.data.contentStyle,
                    color = LocalColors.current.content.contentSecondary,
                    modifier = Modifier.weight(weight = 1f),
                )
            } else {
                Box(
                    content = { innerTextField() },
                    modifier = Modifier.weight(weight = 1f),
                )
            }

            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}

@Composable
internal fun BoxScope.DefaultTextBox(
    placeholderText: String?,
    size: TextFieldSize?,
    showPlaceholder: Boolean,
    leadingContent: (@Composable RowScope.() -> Unit)?,
    trailingContent: (@Composable RowScope.() -> Unit)?,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = size.data.itemsSpacing),
        modifier = modifier
            .align(alignment = Alignment.CenterStart)
            .padding(
                horizontal = size.data.horizontalPadding,
                vertical = size.data.verticalPadding,
            ),
    ) {
        if (leadingContent != null) {
            leadingContent()
        }

        if (showPlaceholder && placeholderText != null) {
            LemonadeUi.Text(
                text = placeholderText,
                textStyle = size.data.contentStyle,
                color = LocalColors.current.content.contentSecondary,
                modifier = Modifier.weight(weight = 1f),
            )
        } else {
            Box(
                content = { innerTextField() },
                modifier = Modifier.weight(weight = 1f),
            )
        }

        if (trailingContent != null) {
            trailingContent()
        }
    }
}

/**
 * This is only used for desktop environment. On mobile, it defaults to the null variant.
 */
private val TextFieldSize?.data: TextFieldData
    @Composable get() = when (this) {
        TextFieldSize.Small -> TextFieldData(
            containerShape = LocalShapes.current.radius200,
            horizontalPadding = LocalSpaces.current.spacing200,
            verticalPadding = LocalSpaces.current.spacing100,
            itemsSpacing = LocalSpaces.current.spacing200,
            contentStyle = LocalTypographies.current.bodySmallRegular,
            minHeight = null,
        )

        TextFieldSize.Medium -> TextFieldData(
            containerShape = LocalShapes.current.radius200,
            horizontalPadding = LocalSpaces.current.spacing300,
            verticalPadding = LocalSpaces.current.spacing200,
            itemsSpacing = LocalSpaces.current.spacing300,
            contentStyle = LocalTypographies.current.bodySmallRegular,
            minHeight = null,
        )

        TextFieldSize.Large -> TextFieldData(
            containerShape = LocalShapes.current.radius300,
            horizontalPadding = LocalSpaces.current.spacing300,
            verticalPadding = LocalSpaces.current.spacing300,
            itemsSpacing = LocalSpaces.current.spacing300,
            contentStyle = LocalTypographies.current.bodyMediumRegular,
            minHeight = null,
        )

        null -> {
            TextFieldData(
                containerShape = LocalShapes.current.radius300,
                horizontalPadding = LocalSpaces.current.spacing300,
                verticalPadding = LocalSpaces.current.spacing300,
                itemsSpacing = LocalSpaces.current.spacing300,
                contentStyle = LocalTypographies.current.bodyMediumRegular,
                minHeight = LocalSizes.current.size1400,
            )
        }
    }

public enum class TextFieldSize {
    Small,
    Medium,
    Large,
}

private data class TextFieldData(
    val minHeight: Dp?,
    val containerShape: Shape,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val itemsSpacing: Dp,
    val contentStyle: LemonadeTextStyle,
)

private data class TextFieldPreviewData(
    val enabled: Boolean,
    val error: Boolean,
    val withLeadingIcon: Boolean,
    val withTrailingIcon: Boolean,
    val withOuterContent: Boolean,
    val size: TextFieldSize?,
)

private class TextFieldPreviewProvider : PreviewParameterProvider<TextFieldPreviewData> {
    override val values: Sequence<TextFieldPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<TextFieldPreviewData> {
        return buildList {
            listOf(true, false).forEach { withOuterContent ->
                listOf(true, false).forEach { enabled ->
                    listOf(true, false).forEach { error ->
                        listOf(true, false).forEach { leading ->
                            listOf(true, false).forEach { trailing ->
                                add(
                                    element = TextFieldPreviewData(
                                        enabled = enabled,
                                        error = error,
                                        withLeadingIcon = leading,
                                        withTrailingIcon = trailing,
                                        withOuterContent = withOuterContent,
                                        size = null,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }.asSequence()
    }
}

@Composable
@LemonadePreview
private fun TextInputPreview(
    @PreviewParameter(TextFieldPreviewProvider::class)
    previewData: TextFieldPreviewData,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
        LemonadeUi.TextField(
            input = "Sample text",
            onInputChanged = {/* Nothing */ },
            enabled = previewData.enabled,
            error = previewData.error,
            label = "Label".takeIf { previewData.withOuterContent },
            supportText = "Support Text".takeIf { previewData.withOuterContent },
            optionalIndicator = "Optional".takeIf { previewData.withOuterContent },
            errorMessage = "This is an error message".takeIf { previewData.withOuterContent },
            leadingContent = if (previewData.withLeadingIcon) {
                {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.PadlockOpen,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
            trailingContent = if (previewData.withTrailingIcon) {
                {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.PadlockOpen,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
        )
    }
}

@Composable
@LemonadePreview
private fun TextInputWithSelectorPreview(
    @PreviewParameter(TextFieldPreviewProvider::class)
    previewData: TextFieldPreviewData,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
        LemonadeUi.TextFieldWithSelector(
            input = "Sample text",
            onInputChanged = {/* Nothing */ },
            enabled = previewData.enabled,
            error = previewData.error,
            label = "Label".takeIf { previewData.withOuterContent },
            supportText = "Support Text".takeIf { previewData.withOuterContent },
            optionalIndicator = "Optional".takeIf { previewData.withOuterContent },
            errorMessage = "This is an error message".takeIf { previewData.withOuterContent },
            leadingAction = { /**/ },
            leadingContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(all = LemonadeTheme.spaces.spacing400),
                ) {
                    LemonadeUi.CountryFlag(flag = LemonadeCountryFlags.BRBrazil)
                    LemonadeUi.Text(text = "Brazil")
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.ChevronDown,
                        contentDescription = null,
                    )
                }
            },
        )
    }
}
