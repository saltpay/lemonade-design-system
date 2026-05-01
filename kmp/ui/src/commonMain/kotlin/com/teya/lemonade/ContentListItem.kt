package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeContentListItemLayout
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice

/**
 * A display-only list item for showing label-value pairs.
 *
 * Supports horizontal (label left, value right) and vertical (label top, value bottom) layouts.
 * In vertical layout, providing a [contentSlot] switches the value to a larger typography.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.ContentListItem(
 *     label = "Account holder",
 *     value = "John Doe",
 * )
 *
 * LemonadeUi.ContentListItem(
 *     label = "Balance",
 *     value = "$1,234.56",
 *     layout = LemonadeContentListItemLayout.Vertical,
 *     contentSlot = { LemonadeUi.Tag(label = "Available", voice = TagVoice.Positive) },
 * )
 * ```
 *
 * @param label - Label [String] describing the data field.
 * @param value - Value [String] to display.
 * @param layout - [LemonadeContentListItemLayout] horizontal or vertical arrangement.
 * @param modifier - [Modifier] to be applied to the root container.
 * @param showDivider - Whether to display a bottom divider below the item.
 * @param leadingSlot - Optional slot for a leading element (e.g. SymbolContainer).
 * @param trailingSlot - Optional slot for a trailing element (e.g. icon action).
 * @param contentSlot - Optional slot for additional content. In vertical layout, this also
 *   switches the value typography to bodyXLargeSemiBold.
 * @param verticalAlignment - Vertical alignment for horizontal layout (default [Alignment.CenterVertically]).
 * @param valueTextAlign - Text alignment for the value in horizontal layout (default [TextAlign.End]).
 */
@Composable
public fun LemonadeUi.ContentListItem(
    label: String,
    value: String,
    layout: LemonadeContentListItemLayout = LemonadeContentListItemLayout.Horizontal,
    modifier: Modifier = Modifier,
    showDivider: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    valueTextAlign: TextAlign = TextAlign.End,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val contentModifier = Modifier.padding(all = LocalSpaces.current.spacing400)

    Column(modifier = modifier) {
        when (layout) {
            LemonadeContentListItemLayout.Horizontal -> HorizontalContentListItem(
                label = label,
                value = value,
                modifier = contentModifier,
                verticalAlignment = verticalAlignment,
                valueTextAlign = valueTextAlign,
                leadingSlot = leadingSlot,
                trailingSlot = trailingSlot,
                contentSlot = contentSlot,
            )

            LemonadeContentListItemLayout.Vertical -> VerticalContentListItem(
                label = label,
                value = value,
                modifier = contentModifier,
                leadingSlot = leadingSlot,
                trailingSlot = trailingSlot,
                contentSlot = contentSlot,
            )
        }

        if (showDivider) {
            LemonadeUi.HorizontalDivider(
                modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing400),
            )
        }
    }
}

@Composable
private fun HorizontalContentListItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    valueTextAlign: TextAlign = TextAlign.End,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        modifier = modifier,
    ) {
        if (leadingSlot != null) {
            leadingSlot()
        }

        Column(
            modifier = Modifier.weight(weight = 1f),
        ) {
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodyMediumRegular,
                color = LocalColors.current.content.contentSecondary,
            )

            if (contentSlot != null) {
                contentSlot()
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
            modifier = Modifier.weight(weight = 1f),
        ) {
            LemonadeUi.Text(
                text = value,
                textStyle = LocalTypographies.current.bodyMediumMedium,
                color = LocalColors.current.content.contentPrimary,
                textAlign = valueTextAlign,
                modifier = Modifier.weight(weight = 1f),
            )

            if (trailingSlot != null) {
                trailingSlot()
            }
        }
    }
}

@Composable
private fun VerticalContentListItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        modifier = modifier,
    ) {
        if (leadingSlot != null) {
            leadingSlot()
        }

        Column(
            modifier = Modifier.weight(weight = 1f),
        ) {
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodySmallRegular,
                color = LocalColors.current.content.contentSecondary,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
            ) {
                LemonadeUi.Text(
                    text = value,
                    textStyle = if (contentSlot != null) {
                        LocalTypographies.current.bodyXLargeSemiBold
                    } else {
                        LocalTypographies.current.bodyMediumMedium
                    },
                    color = LocalColors.current.content.contentPrimary,
                    modifier = Modifier.weight(weight = 1f),
                )

                if (trailingSlot != null) {
                    trailingSlot()
                }
            }

            if (contentSlot != null) {
                contentSlot()
            }
        }
    }
}

private data class ContentListItemPreviewData(
    val layout: LemonadeContentListItemLayout,
    val hasLeading: Boolean,
    val hasTrailing: Boolean,
    val hasContentSlot: Boolean,
    val showDivider: Boolean,
)

private class ContentListItemPreviewProvider :
    PreviewParameterProvider<ContentListItemPreviewData> {
    override val values: Sequence<ContentListItemPreviewData> =
        buildList {
            LemonadeContentListItemLayout.entries.forEach { layout ->
                listOf(true, false).forEach { leading ->
                    listOf(true, false).forEach { trailing ->
                        listOf(true, false).forEach { contentSlot ->
                            listOf(true, false).forEach { divider ->
                                add(
                                    ContentListItemPreviewData(
                                        layout = layout,
                                        hasLeading = leading,
                                        hasTrailing = trailing,
                                        hasContentSlot = contentSlot,
                                        showDivider = divider,
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
private fun ContentListItemPreview(
    @PreviewParameter(ContentListItemPreviewProvider::class)
    previewData: ContentListItemPreviewData,
) {
    LemonadeUi.ContentListItem(
        label = "Label",
        value = "Value",
        layout = previewData.layout,
        showDivider = previewData.showDivider,
        leadingSlot = if (previewData.hasLeading) {
            {
                LemonadeUi.SymbolContainer(
                    icon = LemonadeIcons.Heart,
                    voice = SymbolContainerVoice.Neutral,
                    size = SymbolContainerSize.Medium,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        trailingSlot = if (previewData.hasTrailing) {
            {
                LemonadeUi.Icon(
                    icon = LemonadeIcons.PencilLine,
                    tint = LocalColors.current.content.contentBrand,
                    size = LemonadeAssetSize.Medium,
                    contentDescription = "Edit",
                )
            }
        } else {
            null
        },
        contentSlot = if (previewData.hasContentSlot) {
            {
                LemonadeUi.Text(
                    text = "Extra content",
                    textStyle = LocalTypographies.current.bodySmallRegular,
                    color = LocalColors.current.content.contentSecondary,
                )
            }
        } else {
            null
        },
    )
}
