package com.teya.lemonade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.LemonadeIcons
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Input field designated to use for search and querying.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.SearchField(
 *    input = inputtedContent,
 *    onInputChanged = { inputtedContent = it },
 *    enabled = enabled,
 * )
 * ```
 *
 * @param input - [String] to be displayed as the component's label.
 * @param onInputChanged - Callback to be invoked when the input changes.
 * @param placeholder - optional [String] to be displayed as the component's placeholder text.
 * @param onInputClear - Callback to be invoked when the user request the input to be cleared.
 * @param interactionSource - [MutableInteractionSource] to be applied to the component.
 * @param keyboardActions - [KeyboardActions] to be applied to the component.
 * @param keyboardOptions - [KeyboardOptions] to be applied to the component.
 * @param enabled - [Boolean] flag to enable or disable the component. This will affect component opacity.
 * @param modifier - [Modifier] to be applied to the component.
 */
@Composable
@ExperimentalLemonadeComponent
public fun LemonadeUi.SearchField(
    input: String,
    onInputChanged: (String) -> Unit,
    placeholder: String? = null,
    onInputClear: () -> Unit = { onInputChanged("") },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    CoreSearchField(
        input = input,
        onInputChanged = onInputChanged,
        placeholder = placeholder,
        onInputClear = onInputClear,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        modifier = modifier.clearFocusOnKeyboardDismiss(),
    )
}

@Composable
internal fun CoreSearchField(
    input: String,
    onInputChanged: (String) -> Unit,
    leadingIcon: LemonadeIcons = LemonadeIcons.Search,
    onLeadingIconClicked: (() -> Unit)? = null,
    placeholder: String? = null,
    onInputClear: () -> Unit = { onInputChanged("") },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = input,
        onValueChange = onInputChanged,
        interactionSource = interactionSource,
        enabled = enabled,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        textStyle = LocalTypographies.current.bodyMediumRegular.textStyle,
        singleLine = true,
        modifier = modifier.then(
            other = if (enabled) {
                Modifier
            } else {
                Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
            },
        ),
        decorationBox = { innerTextField ->
            CoreSearchFieldDecorationBox(
                innerTextField = innerTextField,
                interactionSource = interactionSource,
                placeholder = placeholder,
                input = input,
                enabled = enabled,
                onInputClear = onInputClear,
                leadingIcon = leadingIcon,
                onLeadingIconClicked = onLeadingIconClicked,
            )
        },
    )
}

@Suppress("LongMethod", "LongParameterList")
@Composable
private fun CoreSearchFieldDecorationBox(
    input: String,
    placeholder: String?,
    enabled: Boolean,
    onInputClear: () -> Unit,
    leadingIcon: LemonadeIcons,
    onLeadingIconClicked: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchFieldShape = LocalShapes.current.radiusFull
    val isFocused by interactionSource.collectIsFocusedAsState()

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isFocused) {
            LocalColors.current.background.bgDefault
        } else {
            LocalColors.current.background.bgElevated
        },
    )
    val animatedSelectionColor by animateColorAsState(
        targetValue = if (isFocused) {
            LocalColors.current.border.borderSelected
        } else {
            LocalColors.current.border.borderSelected.copy(
                alpha = LocalOpacities.current.base.opacity0,
            )
        },
    )

    val animatedFocusedShadowColor by animateColorAsState(
        targetValue = if (isFocused) {
            LocalColors.current.background.bgElevatedHigh
        } else {
            LocalColors.current.background.bgElevatedHigh.copy(
                alpha = LocalOpacities.current.base.opacity0,
            )
        },
    )

    val animatedFocusedShadowSpread by animateDpAsState(
        targetValue = if (isFocused) {
            LocalBorderWidths.current.base.border50
        } else {
            Dp.Hairline
        },
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
        modifier = modifier
            .shadowBorder(
                width = animatedFocusedShadowSpread,
                shape = searchFieldShape,
                color = animatedFocusedShadowColor,
            ).clip(shape = searchFieldShape)
            .height(LocalSizes.current.size1100)
            .background(color = animatedBackgroundColor)
            .border(
                width = LocalBorderWidths.current.state.borderSelected,
                shape = searchFieldShape,
                color = animatedSelectionColor,
            ).padding(horizontal = LocalSpaces.current.spacing300),
    ) {
        AnimatedContent(
            targetState = leadingIcon,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(150),
                ) togetherWith fadeOut(
                    animationSpec = tween(150),
                )
            },
        ) { icon ->
            LemonadeUi.Icon(
                icon = icon,
                tint = LocalColors.current.content.contentPrimary,
                contentDescription = null,
                modifier = Modifier
                    .then(
                        other = if (onLeadingIconClicked != null) {
                            Modifier.clickable(
                                onClick = onLeadingIconClicked,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            )
                        } else {
                            Modifier
                        },
                    ),
            )
        }

        Box(modifier = Modifier.weight(weight = 1f)) {
            innerTextField()

            if (placeholder != null && input.isEmpty()) {
                LemonadeUi.Text(
                    text = placeholder,
                    textStyle = LocalTypographies.current.bodyMediumRegular,
                    color = LocalColors.current.content.contentTertiary,
                )
            }
        }

        val clearButtonAnimationSpec = tween<Float>(
            durationMillis = 150,
            easing = EaseInOut,
        )
        AnimatedVisibility(
            visible = input.isNotEmpty() && enabled,
            enter = fadeIn(animationSpec = clearButtonAnimationSpec),
            exit = fadeOut(animationSpec = clearButtonAnimationSpec),
        ) {
            LemonadeUi.Icon(
                icon = LemonadeIcons.CircleXSolid,
                tint = LocalColors.current.content.contentSecondary,
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        onClick = onInputClear,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ),
            )
        }
    }
}

private data class SearchFieldPreviewData(
    val withContent: Boolean,
    val enabled: Boolean,
)

private class SearchFieldPreviewProvider : PreviewParameterProvider<SearchFieldPreviewData> {
    override val values: Sequence<SearchFieldPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<SearchFieldPreviewData> =
        buildList {
            listOf(true, false).forEach { withContent ->
                listOf(true, false).forEach { enabled ->
                    add(
                        SearchFieldPreviewData(
                            withContent = withContent,
                            enabled = enabled,
                        ),
                    )
                }
            }
        }.asSequence()
}

@LemonadePreview
@Composable
private fun LemonadeSearchFieldPreview(
    @PreviewParameter(SearchFieldPreviewProvider::class)
    previewData: SearchFieldPreviewData,
) {
    @OptIn(ExperimentalLemonadeComponent::class)
    LemonadeUi.SearchField(
        onInputChanged = { /* Nothing */ },
        placeholder = "This is a placeholder",
        enabled = previewData.enabled,
        input = if (previewData.withContent) {
            "Sample text"
        } else {
            ""
        },
    )
}
