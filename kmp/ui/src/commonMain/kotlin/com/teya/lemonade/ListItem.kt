package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import com.teya.lemonade.core.CheckboxStatus
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeListItemVoice
import com.teya.lemonade.core.SelectListItemType
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.TagVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A list item with the sole purpose of selection of a single or multiple items, with behaviour
 *  changing depending if selection is singular or not.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.SelectListItem(
 *     label = "Label"
 *     supportText = "Support Text"
 *     type = SelectListItemType.Single,
 *     checked = true,
 *     onItemClicked = { /* trigger an action */ }
 *     enabled = false,
 *     showDivider = true,
 *     leadingSlot = { /* slot composable for any item */ },
 *     trailingSlot = { /* slot composable for any item */ },
 * )
 * ```
 *
 * @param label - Label to be displayed in the selection item.
 * @param type - [SelectListItemType], that will define the selection behaviour and selection component.
 * @param checked - Flag defining if item is selected or not.
 * @param onItemClicked - Callback that is triggered on click interaction with list item.
 * @param modifier - [Modifier] to be applied to the base container of component.
 * @param enabled - Flag that defines if the component is enabled or not. If disabled, click interactions
 *  and visual states are disabled.
 * @param interactionSource - Selection list item [MutableInteractionSource] for interaction events.
 * @param showDivider - Flag to show a divider below the list item.
 * @param supportText - Text to be displayed below the [label] as a support text.
 * @param leadingSlot - A Slot to be placed in the leading position of the list item.
 * @param trailingSlot - A Slot to be placed in the trailing position of the list item.
 */
@Composable
public fun LemonadeUi.SelectListItem(
    label: String,
    type: SelectListItemType,
    checked: Boolean,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showDivider: Boolean = false,
    supportText: String? = null,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
) {
    CoreListItem(
        modifier = modifier,
        label = label,
        supportText = supportText,
        interactionSource = interactionSource,
        showDivider = showDivider,
        role = when (type) {
            SelectListItemType.Single -> Role.RadioButton
            SelectListItemType.Multiple -> Role.Checkbox
        },
        onListItemClick = {
            when (type) {
                SelectListItemType.Single -> {
                    if (!checked) {
                        onItemClicked()
                    }
                }

                SelectListItemType.Multiple -> {
                    onItemClicked()
                }
            }
        },
        enabled = enabled,
        leadingSlot = leadingSlot,
        trailingSlot = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing200)
            ) {
                if (trailingSlot != null) {
                    trailingSlot()
                }

                when (type) {
                    SelectListItemType.Single -> {
                        LemonadeUi.RadioButton(
                            enabled = enabled,
                            checked = checked,
                            onRadioButtonClicked = onItemClicked,
                        )
                    }

                    SelectListItemType.Multiple -> {
                        LemonadeUi.Checkbox(
                            enabled = enabled,
                            onCheckboxClicked = onItemClicked,
                            status = if (checked) {
                                CheckboxStatus.Checked
                            } else {
                                CheckboxStatus.Unchecked
                            },
                        )
                    }
                }
            }
        },
    )
}

/**
 * A list item for resource info display.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.ResourceListItem(
 *     label = "Label"
 *     value = "Value",
 *     supportText = "Support Text"
 *     onItemClicked = { /* trigger an action */ }
 *     enabled = true,
 *     showDivider = true,
 *     leadingSlot = { /* slot composable for any item */ },
 *     addonSlot = { /* slot composable for any item */ },
 * )
 * ```
 * @param leadingSlot - slot component to be placed in the leading position of the list item.
 * @param label - main [String] to be displayed.
 * @param value - value [String] to be displayed in the trailing position.
 * @param modifier - [Modifier] to be applied to the base container of component.
 * @param addonSlot - slot to be displayed below the [value] parameter.
 * @param interactionSource - [MutableInteractionSource] of the component.
 * @param onItemClicked - callback called when component is tapped.
 * @param enabled - flag to define if the component is enabled or not. If disabled, click interactions
 *  and visual states are disabled.
 * @param supportText - [String] to be displayed as support text.
 * @param showDivider - flag to show a divider below the list item.
 */
@Composable
public fun LemonadeUi.ResourceListItem(
    leadingSlot: @Composable BoxScope.() -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    addonSlot: (@Composable ColumnScope.() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onItemClicked: (() -> Unit)? = null,
    enabled: Boolean = true,
    supportText: String? = null,
    showDivider: Boolean = false,
) {
    CoreListItem(
        label = label,
        supportText = supportText,
        leadingSlot = {
            Box(
                content = leadingSlot,
                contentAlignment = Alignment.Center,
                modifier = if (enabled) {
                    Modifier
                } else {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                },
            )
        },
        trailingSlot = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing50),
                horizontalAlignment = Alignment.End,
                modifier = if (enabled) {
                    Modifier
                } else {
                    Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                },
            ) {
                LemonadeUi.Text(
                    text = value,
                    textStyle = LocalTypographies.current.bodyMediumMedium,
                    textAlign = TextAlign.Left
                )

                if (addonSlot != null) {
                    addonSlot()
                }
            }
        },
        onListItemClick = onItemClicked,
        role = null,
        enabled = enabled,
        modifier = modifier,
        showDivider = showDivider,
        interactionSource = interactionSource,
    )
}

/**
 * Basic building block for list items.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.ActionListItem(
 *     label = "Label"
 *     supportText = "Support Text"
 *     onItemClicked = { /* trigger an action */ }
 *     enabled = false,
 *     showDivider = true,
 *     leadingSlot = { /* slot composable for any item */ },
 *     trailingSlot = { /* slot composable for any item */ },
 * )
 * ```
 * @param label - label [String] to be displayed in the list item.
 * @param modifier - [Modifier] to be applied to the base container of component.
 * @param supportText - text [String] to be displayed as Support Text.
 * @param leadingSlot - slot content to be placed in the leading position of the component.
 * @param trailingSlot - slot content to be placed in the trailing position of the component.
 * @param voice - [LemonadeListItemVoice] to define the tone of voice. This will effectively define
 *  color of the background while it's hovered or pressed, alongside the content's tints. Defaults to [LemonadeListItemVoice.Neutral].
 * @param showNavigationIndicator - [Boolean] indicates navigation visually.
 * @param enabled - [Boolean] flag to define if the component is enabled or not. If disabled, click interactions
 *  and visual states are disabled.
 * @param onItemClicked - callback called when component is tapped.
 * @param role - [Role] interaction semantics.
 * @param interactionSource - [MutableInteractionSource] to be had within the component.
 * @param showDivider - [Boolean] flag to show a divider below the list item.
 */
@Composable
public fun LemonadeUi.ActionListItem(
    label: String,
    modifier: Modifier = Modifier,
    supportText: String? = null,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    voice: LemonadeListItemVoice = LemonadeListItemVoice.Neutral,
    enabled: Boolean = true,
    onItemClicked: (() -> Unit)? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showNavigationIndicator: Boolean = false,
    showDivider: Boolean = false,
) {
    CoreListItem(
        label = label,
        supportText = supportText,
        leadingSlot = leadingSlot,
        trailingSlot = {
            Row(
                modifier = Modifier.then(
                    other = if (enabled) {
                        Modifier
                    } else {
                        Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                    }
                )
            ) {
                if (trailingSlot !== null) {
                    trailingSlot()
                }

                if (showNavigationIndicator) {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.ChevronRight,
                        tint = LocalColors.current.content.contentTertiary,
                        size = LemonadeAssetSize.Medium,
                        contentDescription = "Navigation indicator"
                    )
                }
            }

        },
        voice = voice,
        onListItemClick = onItemClicked,
        role = role,
        enabled = enabled,
        modifier = modifier,
        showDivider = showDivider,
        interactionSource = interactionSource,
    )
}


@Composable
private fun CoreListItem(
    label: String,
    supportText: String?,
    leadingSlot: (@Composable RowScope.() -> Unit)?,
    trailingSlot: (@Composable RowScope.() -> Unit)?,
    voice: LemonadeListItemVoice = LemonadeListItemVoice.Neutral,
    onListItemClick: (() -> Unit)?,
    role: Role?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    showDivider: Boolean,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovering by interactionSource.collectIsHoveredAsState()

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isHovering || isPressed) {
            voice.interactionBackground
        } else {
            voice.interactionBackground.copy(
                alpha = LocalOpacities.current.base.opacity0,
            )
        }
    )
    SafeArea(modifier = modifier, showDivider = showDivider) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
            modifier = Modifier
                .clip(shape = LocalShapes.current.radius300)
                .then(
                    other = if (onListItemClick != null) {
                        Modifier.clickable(
                            enabled = enabled,
                            role = role,
                            onClick = onListItemClick,
                            interactionSource = interactionSource,
                            indication = null,
                        )
                    } else {
                        Modifier
                    }
                )
                .background(color = animatedBackgroundColor)
                .defaultMinSize(minHeight = LocalSizes.current.size1200)
                .padding(
                    horizontal = LocalSpaces.current.spacing300,
                    vertical = LocalSpaces.current.spacing300,
                ),
        ) {
            if (leadingSlot != null) {
                Row(
                    modifier = Modifier
                        .then(
                            other = if (!enabled) {
                                Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                            } else {
                                Modifier
                            },
                        ),
                ) {
                    leadingSlot()
                }
            }

            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .then(
                        other = if (!enabled) {
                            Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                        } else {
                            Modifier
                        },
                    ),
            ) {
                LemonadeUi.Text(
                    text = label,
                    textStyle = LocalTypographies.current.bodyMediumMedium,
                    color = voice.contentColor,
                )

                if (supportText != null) {
                    LemonadeUi.Text(
                        text = supportText,
                        textStyle = LocalTypographies.current.bodySmallRegular,
                        color = LocalColors.current.content.contentSecondary,
                    )
                }
            }

            if (trailingSlot != null) {
                trailingSlot()
            }
        }
    }
}

@Composable
private fun SafeArea(
    modifier: Modifier = Modifier,
    showDivider: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.background(color = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .padding(LocalSpaces.current.spacing100)
                .background(color = Color.Transparent),
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }

        if (showDivider) {
            LemonadeUi.HorizontalDivider(
                modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing400)
            )
        }
    }
}


private val LemonadeListItemVoice.interactionBackground: Color
    @Composable get() {
        return when (this) {
            LemonadeListItemVoice.Neutral -> LocalColors.current.interaction.bgSubtleInteractive
            LemonadeListItemVoice.Critical -> LocalColors.current.interaction.bgCriticalSubtleInteractive
        }
    }

private val LemonadeListItemVoice.contentColor: Color
    @Composable get() {
        return when (this) {
            LemonadeListItemVoice.Neutral -> LocalColors.current.content.contentPrimary
            LemonadeListItemVoice.Critical -> LocalColors.current.content.contentCritical
        }
    }

private data class SelectionListItemPreviewData(
    val type: SelectListItemType,
    val supportText: Boolean,
    val enabled: Boolean,
    val leading: Boolean,
    val trailing: Boolean,
)

private class SelectionListItemPreviewProvider :
    PreviewParameterProvider<SelectionListItemPreviewData> {
    override val values: Sequence<SelectionListItemPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<SelectionListItemPreviewData> {
        return buildList {
            SelectListItemType.entries.forEach { type ->
                listOf(true, false).forEach { enabled ->
                    listOf(true, false).forEach { leading ->
                        listOf(true, false).forEach { trailing ->
                            listOf(true, false).forEach { withSupportText ->
                                add(
                                    SelectionListItemPreviewData(
                                        type = type,
                                        supportText = withSupportText,
                                        enabled = enabled,
                                        leading = leading,
                                        trailing = trailing,
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

@LemonadePreview
@Composable
private fun SelectListItemPreview(
    @PreviewParameter(SelectionListItemPreviewProvider::class)
    previewData: SelectionListItemPreviewData,
) {
    LemonadeUi.SelectListItem(
        label = "Label",
        supportText = "Support text".takeIf { previewData.supportText },
        type = previewData.type,
        checked = previewData.enabled,
        onItemClicked = { /* Nothing */ },
        leadingSlot = if (previewData.leading) {
            {
                LemonadeUi.Icon(
                    icon = LemonadeIcons.Check,
                    contentDescription = "Leading icon",
                )
            }
        } else {
            null
        },
        trailingSlot = if (previewData.leading) {
            {
                LemonadeUi.Icon(
                    icon = LemonadeIcons.Airplane,
                    contentDescription = "Trailing icon",
                )
            }
        } else {
            null
        },
    )
}

private data class ResourceListItemPreviewData(
    val withAddonSlot: Boolean,
    val enabled: Boolean,
    val supportText: Boolean,
)

private class ResourceListItemPreviewProvider :
    PreviewParameterProvider<ResourceListItemPreviewData> {
    override val values: Sequence<ResourceListItemPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<ResourceListItemPreviewData> {
        return buildList {
            listOf(true, false).forEach { addonSlot ->
                listOf(true, false).forEach { enabled ->
                    listOf(true, false).forEach { withSupportText ->
                        add(
                            ResourceListItemPreviewData(
                                withAddonSlot = addonSlot,
                                enabled = enabled,
                                supportText = withSupportText,
                            )
                        )
                    }
                }
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun ResourceListItemPreview(
    @PreviewParameter(ResourceListItemPreviewProvider::class)
    previewData: ResourceListItemPreviewData,
) {
    LemonadeUi.ResourceListItem(
        label = "Label",
        showDivider = true,
        supportText = "Metadata 1 * Metadata 2\nSupport text".takeIf { previewData.supportText },
        value = "Value",
        enabled = previewData.enabled,
        addonSlot = if (previewData.withAddonSlot) {
            {
                LemonadeUi.Tag(
                    label = "Approved",
                    voice = TagVoice.Positive,
                )
            }
        } else {
            null
        },
        leadingSlot = {
            LemonadeUi.SymbolContainer(
                icon = LemonadeIcons.Heart,
                voice = SymbolContainerVoice.Neutral,
                size = SymbolContainerSize.Large,
                contentDescription = null,
            )
        }
    )
}

private data class ActionListItemPreviewData(
    val voice: Boolean,
    val enabled: Boolean,
    val supportText: Boolean,
    val trailingSlot: Boolean
)

private class ActionListItemPreviewProvider :
    PreviewParameterProvider<ActionListItemPreviewData> {
    override val values: Sequence<ActionListItemPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<ActionListItemPreviewData> {
        return buildList {
            listOf(true, false).forEach { voice ->
                listOf(true, false).forEach { enabled ->
                    listOf(true, false).forEach { withSupportText ->
                        listOf(true, false).forEach { trailingSlot ->
                            add(
                                ActionListItemPreviewData(
                                    voice = voice,
                                    enabled = enabled,
                                    supportText = withSupportText,
                                    trailingSlot = trailingSlot
                                )
                            )
                        }
                    }
                }
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun ActionListItemPreviewProvider(
    @PreviewParameter(ActionListItemPreviewProvider::class)
    previewData: ActionListItemPreviewData,
) {
    LemonadeUi.ActionListItem(
        label = "Label",
        showDivider = true,
        supportText = "Support text".takeIf { previewData.supportText },
        enabled = previewData.enabled,
        trailingSlot = if (previewData.trailingSlot) {
            {
                LemonadeUi.Tag(
                    label = "New",
                    voice = TagVoice.Warning,
                )
            }
        } else {
            null
        },
        leadingSlot = {
            LemonadeUi.Icon(
                icon = LemonadeIcons.Heart,
                size = LemonadeAssetSize.Medium,
                contentDescription = null,
            )
        }
    )
}