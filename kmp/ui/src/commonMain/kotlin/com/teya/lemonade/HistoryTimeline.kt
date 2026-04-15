package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.HistoryItemVoice

private val HistoryItemIndicatorWidth: Dp = 20.dp
private val HistoryItemDotSize: Dp = 10.dp
private val HistoryItemDotTopOffset: Dp = 7.dp
private val HistoryItemLineThickness: Dp = 1.dp

/**
 * Describes a single row inside a [LemonadeUi.HistoryTimeline].
 *
 * @param label Primary row text.
 * @param subheading Optional secondary text rendered immediately below [label].
 * @param description Optional tertiary paragraph text rendered below the subheading.
 * @param voice Semantic color of the indicator dot when this row is the current step.
 * @param contentSlot Optional slot for custom composable content (e.g. a button or tag)
 *   rendered below the description.
 */
public class HistoryTimelineItem(
    public val label: String,
    public val subheading: String? = null,
    public val description: String? = null,
    public val voice: HistoryItemVoice = HistoryItemVoice.Neutral,
    public val contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
)

/**
 * A vertical list of history steps with a left-aligned indicator column. The current row uses
 * the item's [HistoryItemVoice] colour; past rows render muted. A connecting line joins every
 * row except the last.
 *
 * For rows with custom content, set [HistoryTimelineItem.contentSlot]. For full composable
 * control per row (including mixing non-HistoryItem composables) use the standalone
 * [LemonadeUi.HistoryItem] inside your own column.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.HistoryTimeline(
 *     items = listOf(
 *         HistoryTimelineItem(
 *             label = "Find a Visa PayPoint next to you",
 *             description = "PayPoint locations collect cash deposits on our behalf",
 *             voice = HistoryItemVoice.Positive,
 *             contentSlot = {
 *                 LemonadeUi.Button(
 *                     label = "Find a PayPoint",
 *                     onClick = { /* ... */ },
 *                 )
 *             },
 *         ),
 *         HistoryTimelineItem(
 *             label = "Enter the amount and generate a barcode",
 *             description = "Show the barcode to the shopkeeper and deposit the funds",
 *         ),
 *     ),
 *     currentIndex = 0,
 * )
 * ```
 *
 * @param items Ordered list of [HistoryTimelineItem]s to display.
 * @param modifier [Modifier] applied to the root column.
 * @param currentIndex Index of the row to render as the current step. Pass `null` to render
 *   every row in its non-current (muted) state. Defaults to `0` (first row is current).
 */
@Composable
public fun LemonadeUi.HistoryTimeline(
    items: List<HistoryTimelineItem>,
    modifier: Modifier = Modifier,
    currentIndex: Int? = 0,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            LemonadeUi.HistoryItem(
                label = item.label,
                subheading = item.subheading,
                description = item.description,
                voice = item.voice,
                isCurrent = index == currentIndex,
                isLast = index == items.lastIndex,
                contentSlot = item.contentSlot,
            )
        }
    }
}

/**
 * A single timeline row with an indicator column and a stack of label, subheading, description
 * and an optional content slot.
 *
 * Prefer [LemonadeUi.HistoryTimeline] for a list; use this standalone form when the caller
 * needs full composable control per row.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.HistoryItem(
 *     label = "Paid",
 *     subheading = "10:24",
 *     description = "Payment was successfully processed.",
 *     voice = HistoryItemVoice.Positive,
 *     isCurrent = true,
 * )
 * ```
 *
 * @param label Primary row text.
 * @param modifier [Modifier] applied to the root row.
 * @param subheading Optional secondary text rendered immediately below [label].
 * @param description Optional tertiary paragraph text rendered below the subheading.
 * @param voice Semantic colour of the indicator dot. Only applied when [isCurrent] is `true`;
 *   non-current rows always render a muted dot.
 * @param isCurrent Whether this row is the current (active) step.
 * @param isLast Whether this is the last row in the timeline. Hides the connecting line and
 *   the trailing bottom padding.
 * @param contentSlot Optional slot for custom composable content (e.g. a button or tag)
 *   rendered below the description.
 */
@Composable
public fun LemonadeUi.HistoryItem(
    label: String,
    modifier: Modifier = Modifier,
    subheading: String? = null,
    description: String? = null,
    voice: HistoryItemVoice = HistoryItemVoice.Neutral,
    isCurrent: Boolean = false,
    isLast: Boolean = false,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val spaces = LocalSpaces.current
    val typographies = LocalTypographies.current
    val contentColors = LocalColors.current.content

    Row(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(space = spaces.spacing300),
    ) {
        HistoryItemIndicator(
            voice = voice,
            isCurrent = isCurrent,
            isLast = isLast,
        )

        Column(
            modifier = Modifier.padding(bottom = if (isLast) 0.dp else spaces.spacing400),
            verticalArrangement = Arrangement.spacedBy(space = spaces.spacing200),
        ) {
            Column {
                LemonadeUi.Text(
                    text = label,
                    textStyle = typographies.bodyMediumMedium,
                    color = contentColors.contentPrimary,
                )

                if (subheading != null) {
                    LemonadeUi.Text(
                        text = subheading,
                        textStyle = typographies.bodySmallRegular,
                        color = contentColors.contentSecondary,
                    )
                }
            }

            if (description != null) {
                LemonadeUi.Text(
                    text = description,
                    textStyle = typographies.bodySmallRegular,
                    color = contentColors.contentSecondary,
                )
            }

            if (contentSlot != null) {
                contentSlot()
            }
        }
    }
}

private val HistoryItemVoice.currentDotColor: Color
    @Composable get() {
        return when (this) {
            HistoryItemVoice.Positive -> LocalColors.current.background.bgPositive
            HistoryItemVoice.Critical -> LocalColors.current.background.bgCritical
            HistoryItemVoice.Neutral -> LocalColors.current.background.bgNeutral
        }
    }

@Composable
private fun HistoryItemIndicator(
    voice: HistoryItemVoice,
    isCurrent: Boolean,
    isLast: Boolean,
) {
    val mutedColor = LocalColors.current.border.borderNeutralMedium
    val dotColor: Color = if (isCurrent) {
        voice.currentDotColor
    } else {
        mutedColor
    }

    Column(
        modifier = Modifier
            .width(width = HistoryItemIndicatorWidth)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(height = HistoryItemDotTopOffset))
        Box(
            modifier = Modifier
                .size(size = HistoryItemDotSize)
                .clip(shape = CircleShape)
                .background(color = dotColor),
        )
        if (!isLast) {
            Box(
                modifier = Modifier
                    .width(width = HistoryItemLineThickness)
                    .weight(weight = 1f)
                    .background(color = mutedColor),
            )
        }
    }
}

private data class HistoryItemPreviewData(
    val voice: HistoryItemVoice,
    val isCurrent: Boolean,
    val hasDescription: Boolean,
    val hasContentSlot: Boolean,
)

private class HistoryItemPreviewProvider : PreviewParameterProvider<HistoryItemPreviewData> {
    override val values: Sequence<HistoryItemPreviewData> = buildList {
        HistoryItemVoice.entries.forEach { voice ->
            listOf(true, false).forEach { current ->
                listOf(true, false).forEach { description ->
                    listOf(true, false).forEach { contentSlot ->
                        add(
                            HistoryItemPreviewData(
                                voice = voice,
                                isCurrent = current,
                                hasDescription = description,
                                hasContentSlot = contentSlot,
                            ),
                        )
                    }
                }
            }
        }
    }.asSequence()
}

@LemonadePreview
@Composable
private fun HistoryItemPreview(
    @PreviewParameter(HistoryItemPreviewProvider::class)
    previewData: HistoryItemPreviewData,
) {
    LemonadeUi.HistoryItem(
        label = "Label",
        subheading = "Subheading",
        description = if (previewData.hasDescription) {
            "Description for the timeline step providing extra context."
        } else {
            null
        },
        voice = previewData.voice,
        isCurrent = previewData.isCurrent,
        contentSlot = if (previewData.hasContentSlot) {
            {
                LemonadeUi.Text(
                    text = "Custom slot content",
                    textStyle = LocalTypographies.current.bodySmallRegular,
                    color = LocalColors.current.content.contentSecondary,
                )
            }
        } else {
            null
        },
    )
}

@LemonadePreview
@Composable
private fun HistoryTimelinePreview() {
    LemonadeUi.HistoryTimeline(
        items = listOf(
            HistoryTimelineItem(
                label = "Find a Visa PayPoint next to you",
                description = "PayPoint locations collect cash deposits on our behalf",
                voice = HistoryItemVoice.Positive,
            ),
            HistoryTimelineItem(
                label = "Enter the amount and generate a barcode",
                description = "Show the barcode to the shopkeeper and deposit the funds",
            ),
            HistoryTimelineItem(
                label = "After deposit, your money will be available in 10 minutes",
                description = "We'll notify you once the funds are in your account",
            ),
        ),
        currentIndex = 0,
    )
}
