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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeListItemVoice
import com.teya.lemonade.core.LemonadeSkeletonSize
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.TagVoice

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
 * @param isLoading - shows a skeleton loading placeholder instead of content.
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
    isLoading: Boolean = false,
    enabled: Boolean = true,
    supportText: String? = null,
    showDivider: Boolean = false,
) {
    LemonadeUi.ListItem(
        label = label,
        supportText = supportText,
        isLoading = isLoading,
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
                    textAlign = TextAlign.Left,
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
 * @param voice - [LemonadeListItemVoice] to define the tone of voice. This will effectively
 *  define color of the background while it's hovered or pressed, alongside the content's
 *  tints. Defaults to [LemonadeListItemVoice.Neutral].
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
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onItemClicked: (() -> Unit)? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showNavigationIndicator: Boolean = false,
    showDivider: Boolean = false,
) {
    LemonadeUi.ListItem(
        label = label,
        supportText = supportText,
        isLoading = isLoading,
        leadingSlot = leadingSlot,
        trailingSlot = if (trailingSlot != null) {
            {
                Row(
                    modifier = Modifier.then(
                        other = if (enabled) {
                            Modifier
                        } else {
                            Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                        },
                    ),
                ) {
                    trailingSlot()
                }
            }
        } else {
            null
        },
        navigationIndicator = showNavigationIndicator,
        voice = voice,
        onListItemClick = onItemClicked,
        role = role,
        enabled = enabled,
        modifier = modifier,
        showDivider = showDivider,
        interactionSource = interactionSource,
    )
}

/**
 * Convenience overload that composes standard label and support-text content from string parameters
 * and delegates to the content-slot variant of [ListItem].
 *
 * @param label - Label [String] to be displayed in the list item.
 * @param supportText - Optional support text [String] displayed below the [label].
 * @param leadingSlot - A slot to be placed in the leading position of the list item.
 * @param trailingSlot - A slot to be placed in the trailing position of the list item.
 * @param voice - [LemonadeListItemVoice] that defines the visual voice of the list item.
 * @param navigationIndicator - Shows a chevron-right navigation indicator.
 * @param onListItemClick - Optional callback triggered on click interaction with the list item.
 * @param role - Optional semantic [Role] applied to the list item for accessibility.
 * @param enabled - Flag that defines if the component is enabled or not. If disabled, click
 *  interactions and visual states are disabled.
 * @param modifier - [Modifier] to be applied to the base container of the component.
 * @param showDivider - Flag to show a divider below the list item.
 * @param interactionSource - [MutableInteractionSource] for interaction events.
 * @param slotContent - Optional slot content below the label and support text.
 */
@Composable
public fun LemonadeUi.ListItem(
    label: String,
    modifier: Modifier = Modifier,
    supportText: String? = null,
    onListItemClick: (() -> Unit)? = null,
    voice: LemonadeListItemVoice = LemonadeListItemVoice.Neutral,
    navigationIndicator: Boolean = false,
    isLoading: Boolean = false,
    role: Role? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showDivider: Boolean = false,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    slotContent: (@Composable ColumnScope.() -> Unit)? = null,
) {
    if (isLoading) {
        ListItemSkeleton(
            modifier = modifier,
            showDivider = showDivider,
        )
    } else {
        LemonadeUi.ListItem(
            leadingSlot = leadingSlot,
            trailingSlot = trailingSlot,
            voice = voice,
            navigationIndicator = navigationIndicator,
            onListItemClick = onListItemClick,
            role = role,
            enabled = enabled,
            modifier = modifier,
            showDivider = showDivider,
            interactionSource = interactionSource,
            contentSlot = {
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

                if (slotContent != null) {
                    slotContent()
                }
            },
        )
    }
}

/**
 * Foundational list-item overload that accepts a generic content slot for custom content,
 * delegating layout and interaction handling to [CoreListItem].
 *
 * @param contentSlot - Composable content slot for the main body of the list item.
 * @param leadingSlot - A slot to be placed in the leading position of the list item.
 * @param trailingSlot - A slot to be placed in the trailing position of the list item.
 * @param voice - [LemonadeListItemVoice] that defines the visual voice of the list item.
 * @param navigationIndicator - Shows a chevron-right navigation indicator.
 * @param onListItemClick - Optional callback triggered on click interaction with the list item.
 * @param role - Optional semantic [Role] applied to the list item for accessibility.
 * @param enabled - Flag that defines if the component is enabled or not. If disabled, click
 *  interactions and visual states are disabled.
 * @param modifier - [Modifier] to be applied to the base container of the component.
 * @param showDivider - Flag to show a divider below the list item.
 * @param interactionSource - [MutableInteractionSource] for interaction events.
 */
@Composable
public fun LemonadeUi.ListItem(
    contentSlot: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    onListItemClick: (() -> Unit)? = null,
    voice: LemonadeListItemVoice = LemonadeListItemVoice.Neutral,
    navigationIndicator: Boolean = false,
    role: Role? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showDivider: Boolean = false,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
) {
    CoreListItem(
        contentSlot = contentSlot,
        leadingSlot = leadingSlot,
        trailingSlot = trailingSlot,
        voice = voice,
        navigationIndicator = navigationIndicator,
        onListItemClick = onListItemClick,
        role = role,
        enabled = enabled,
        modifier = modifier,
        showDivider = showDivider,
        interactionSource = interactionSource,
    )
}

@Composable
private fun CoreListItem(
    contentSlot: @Composable ColumnScope.() -> Unit,
    leadingSlot: (@Composable RowScope.() -> Unit)?,
    trailingSlot: (@Composable RowScope.() -> Unit)?,
    voice: LemonadeListItemVoice = LemonadeListItemVoice.Neutral,
    navigationIndicator: Boolean = false,
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
        },
    )
    SafeArea(modifier = modifier, showDivider = showDivider) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(shape = LocalShapes.current.radius500)
                .then(
                    other = if (onListItemClick != null) {
                        Modifier.clickable(
                            enabled = enabled,
                            role = role,
                            onClick = onListItemClick,
                            interactionSource = interactionSource,
                            indication = LocalEffects.current.interactionIndication,
                        )
                    } else {
                        Modifier
                    },
                ).background(color = animatedBackgroundColor)
                .defaultMinSize(minHeight = LocalSizes.current.size1200)
                .padding(
                    horizontal = LocalSpaces.current.spacing300,
                    vertical = LocalSpaces.current.spacing300,
                ),
        ) {
            if (leadingSlot != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(end = LocalSpaces.current.spacing300)
                        .padding(vertical = LocalSpaces.current.spacing50)
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

            Row(
                modifier = Modifier.weight(weight = 1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    content = contentSlot,
                    modifier = Modifier
                        .weight(weight = 1f)
                        .then(
                            other = if (!enabled) {
                                Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                            } else {
                                Modifier
                            },
                        ),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (trailingSlot != null) {
                        trailingSlot()
                    }

                    if (navigationIndicator) {
                        LemonadeUi.Icon(
                            icon = LemonadeIcons.ChevronRight,
                            tint = LocalColors.current.content.contentTertiary,
                            size = LemonadeAssetSize.Medium,
                            contentDescription = null,
                            modifier = Modifier
                                .then(
                                    other = if (!enabled) {
                                        Modifier.alpha(alpha = LocalOpacities.current.state.opacityDisabled)
                                    } else {
                                        Modifier
                                    },
                                ).padding(start = LocalSpaces.current.spacing100),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SafeArea(
    modifier: Modifier = Modifier,
    showDivider: Boolean,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.background(color = Color.Transparent),
    ) {
        Column(
            modifier = Modifier
                .padding(LocalSpaces.current.spacing100)
                .background(color = Color.Transparent),
            verticalArrangement = Arrangement.Center,
        ) {
            content()
        }

        if (showDivider) {
            LemonadeUi.HorizontalDivider(
                modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing400),
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

@Composable
private fun ListItemSkeleton(
    modifier: Modifier = Modifier,
    showDivider: Boolean = false,
) {
    SafeArea(modifier = modifier, showDivider = showDivider) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    horizontal = LocalSpaces.current.spacing300,
                    vertical = LocalSpaces.current.spacing300,
                ),
        ) {
            LemonadeUi.CircleSkeleton(
                size = LemonadeSkeletonSize.XLarge,
                modifier = Modifier.padding(end = LocalSpaces.current.spacing300),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(weight = 1f),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
                    modifier = Modifier.weight(weight = 1f),
                ) {
                    LemonadeUi.LineSkeleton(
                        size = LemonadeSkeletonSize.Medium,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    LemonadeUi.LineSkeleton(
                        size = LemonadeSkeletonSize.Small,
                        modifier = Modifier.fillMaxWidth(fraction = 0.6f),
                    )
                }

                LemonadeUi.LineSkeleton(
                    size = LemonadeSkeletonSize.Medium,
                    modifier = Modifier
                        .padding(start = LocalSpaces.current.spacing300)
                        .width(width = 54.dp),
                )
            }
        }
    }
}

private data class ResourceListItemPreviewData(
    val withAddonSlot: Boolean,
    val enabled: Boolean,
    val supportText: Boolean,
)

private class ResourceListItemPreviewProvider :
    PreviewParameterProvider<ResourceListItemPreviewData> {
    override val values: Sequence<ResourceListItemPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<ResourceListItemPreviewData> =
        buildList {
            listOf(true, false).forEach { addonSlot ->
                listOf(true, false).forEach { enabled ->
                    listOf(true, false).forEach { withSupportText ->
                        add(
                            ResourceListItemPreviewData(
                                withAddonSlot = addonSlot,
                                enabled = enabled,
                                supportText = withSupportText,
                            ),
                        )
                    }
                }
            }
        }.asSequence()
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
        },
    )
}

private data class ActionListItemPreviewData(
    val voice: Boolean,
    val enabled: Boolean,
    val supportText: Boolean,
    val trailingSlot: Boolean,
    val showNavigationIndicator: Boolean,
)

private class ActionListItemPreviewProvider :
    PreviewParameterProvider<ActionListItemPreviewData> {
    override val values: Sequence<ActionListItemPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<ActionListItemPreviewData> =
        buildList {
            listOf(true, false).forEach { voice ->
                listOf(true, false).forEach { enabled ->
                    listOf(true, false).forEach { withSupportText ->
                        listOf(true, false).forEach { trailingSlot ->
                            listOf(true, false).forEach { showNavigationIndicator ->
                                add(
                                    ActionListItemPreviewData(
                                        voice = voice,
                                        enabled = enabled,
                                        supportText = withSupportText,
                                        trailingSlot = trailingSlot,
                                        showNavigationIndicator = showNavigationIndicator,
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
private fun ActionListItemPreview(
    @PreviewParameter(ActionListItemPreviewProvider::class)
    previewData: ActionListItemPreviewData,
) {
    LemonadeUi.ActionListItem(
        label = "Label",
        showDivider = true,
        supportText = "Support Text".takeIf { previewData.supportText },
        enabled = previewData.enabled,
        voice = if (previewData.voice) LemonadeListItemVoice.Critical else LemonadeListItemVoice.Neutral,
        showNavigationIndicator = previewData.showNavigationIndicator,
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
        },
    )
}
