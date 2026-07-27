package com.teya.lemonade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonType
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeIconButtonShape
import com.teya.lemonade.core.LemonadeIcons

private const val SEARCH_FIELD_ANIMATION_MILLIS = 150

private val SearchFieldFadeSpec = tween<Float>(
    durationMillis = SEARCH_FIELD_ANIMATION_MILLIS,
    easing = EaseInOut,
)

// Same curve as the fade, but typed for the size animation. Without it `expandHorizontally` and
// `shrinkHorizontally` fall back to their default spring and the width drifts out of step with the
// opacity and scale.
private val SearchFieldSizeSpec = tween<IntSize>(
    durationMillis = SEARCH_FIELD_ANIMATION_MILLIS,
    easing = EaseInOut,
)

// The cancel button pops in from — and collapses back to — slightly under its full size, so the
// entrance reads as the button growing into the gap the field gives up rather than blinking in.
private const val CANCEL_BUTTON_COLLAPSED_SCALE = 0.8f

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
 * @param dismissible - [Boolean] flag controlling the trailing cancel button, on by default. The
 * button shows up as soon as there is something to dismiss (the field is focused or holds input),
 * and tapping it hides the keyboard and clears the focus. Turn it off for hosts that already provide
 * their own dismissal affordance.
 * @param onCancel - Callback to be invoked after the search has been dismissed through the cancel
 * button. The input itself is left untouched; clear it from this callback if the search should be
 * reset as well.
 * @param cancelContentDescription - optional [String] content description for the cancel button, for
 * accessibility. The component leaves it unset by default so the label can be localised by the
 * consumer; supply one whenever the field is [dismissible].
 * @param interactionSource - [MutableInteractionSource] to be applied to the component.
 * @param keyboardActions - [KeyboardActions] to be applied to the component.
 * @param keyboardOptions - [KeyboardOptions] to be applied to the component.
 * @param enabled - [Boolean] flag to enable or disable the component. This will affect component opacity.
 * @param modifier - [Modifier] to be applied to the component.
 */
@Suppress("LongParameterList")
@Composable
@ExperimentalLemonadeComponent
public fun LemonadeUi.SearchField(
    input: String,
    onInputChanged: (String) -> Unit,
    placeholder: String? = null,
    onInputClear: () -> Unit = { onInputChanged("") },
    dismissible: Boolean = true,
    onCancel: () -> Unit = {},
    cancelContentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
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
            modifier = Modifier
                .weight(weight = 1f)
                .clearFocusOnKeyboardDismiss(),
        )

        // The cancel button only earns its space once there is something to dismiss: an active
        // focus or a query already typed in. That mirrors the Figma states, where the resting empty
        // field is the only one without it.
        AnimatedVisibility(
            visible = dismissible && enabled && (isFocused || input.isNotEmpty()),
            enter = fadeIn(animationSpec = SearchFieldFadeSpec) +
                scaleIn(
                    animationSpec = SearchFieldFadeSpec,
                    initialScale = CANCEL_BUTTON_COLLAPSED_SCALE,
                ) + expandHorizontally(animationSpec = SearchFieldSizeSpec),
            exit = fadeOut(animationSpec = SearchFieldFadeSpec) +
                scaleOut(
                    animationSpec = SearchFieldFadeSpec,
                    targetScale = CANCEL_BUTTON_COLLAPSED_SCALE,
                ) + shrinkHorizontally(animationSpec = SearchFieldSizeSpec),
        ) {
            LemonadeUi.IconButton(
                icon = LemonadeIcons.Times,
                contentDescription = cancelContentDescription,
                onClick = {
                    // `clearFocus` is what dismisses the keyboard too — it is the single dismissal
                    // idiom this module already uses (see the TopBar search leading icon).
                    focusManager.clearFocus()
                    onCancel()
                },
                variant = LemonadeButtonVariant.Neutral,
                type = LemonadeButtonType.Solid,
                size = LemonadeButtonSize.Small,
                shape = LemonadeIconButtonShape.Circular,
                // The gap lives inside the animated content so that it collapses together with the
                // button instead of leaving a permanent trailing gutter next to the field.
                modifier = Modifier.padding(start = LocalSpaces.current.spacing200),
            )
        }
    }
}

// Kept only to preserve the binary symbol released before the cancel button existed. It delegates
// with the current defaults, so already-compiled callers pick the cancel button up on upgrade just
// like a recompiled one would. No `replaceWith`: a HIDDEN deprecation is invisible to source
// resolution, so the quick-fix would never be offered.
@Deprecated(
    message = "Use the overload with dismissible, onCancel and cancelContentDescription parameters.",
    level = DeprecationLevel.HIDDEN,
)
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
    SearchField(
        input = input,
        onInputChanged = onInputChanged,
        placeholder = placeholder,
        onInputClear = onInputClear,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        modifier = modifier,
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
    val contentColor = LocalColors.current.content.contentPrimary
    BasicTextField(
        value = input,
        onValueChange = onInputChanged,
        interactionSource = interactionSource,
        enabled = enabled,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(contentColor),
        textStyle = LocalTypographies.current.bodyMediumRegular.textStyle.copy(
            color = contentColor,
        ),
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
                                indication = LocalEffects.current.interactionIndication,
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        AnimatedVisibility(
            visible = input.isNotEmpty() && enabled,
            enter = fadeIn(animationSpec = SearchFieldFadeSpec),
            exit = fadeOut(animationSpec = SearchFieldFadeSpec),
        ) {
            LemonadeUi.Icon(
                icon = LemonadeIcons.CircleXSolid,
                tint = LocalColors.current.content.contentSecondary,
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        onClick = onInputClear,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalEffects.current.interactionIndication,
                    ),
            )
        }
    }
}

private data class SearchFieldPreviewData(
    val withContent: Boolean,
    val enabled: Boolean,
    val dismissible: Boolean,
)

private class SearchFieldPreviewProvider : PreviewParameterProvider<SearchFieldPreviewData> {
    override val values: Sequence<SearchFieldPreviewData> = buildAllVariants()

    // Previews never hold focus, so the cancel button is only on screen for the enabled-with-content
    // case. Varying `dismissible` anywhere else would render a duplicate of an existing variant.
    private fun buildAllVariants(): Sequence<SearchFieldPreviewData> =
        buildList {
            listOf(true, false).forEach { withContent ->
                listOf(true, false).forEach { enabled ->
                    add(
                        SearchFieldPreviewData(
                            withContent = withContent,
                            enabled = enabled,
                            dismissible = withContent && enabled,
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
        dismissible = previewData.dismissible,
        cancelContentDescription = "Cancel search",
        input = if (previewData.withContent) {
            "Sample text"
        } else {
            ""
        },
        modifier = Modifier.fillMaxWidth(),
    )
}
