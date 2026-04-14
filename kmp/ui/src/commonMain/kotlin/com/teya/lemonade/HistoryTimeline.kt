package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.HistoryTimelineItemVoice

/**
 * Represents a single entry in a [HistoryTimeline].
 *
 * Each item is displayed as a row with a colored indicator dot on the left and text content on
 * the right. The indicator color is determined by the [voice] and [selected] combination.
 *
 * @param label - [String] primary text displayed for this timeline entry.
 * @param voice - [HistoryTimelineItemVoice] semantic voice that colors the indicator dot.
 * @param selected - [Boolean] when true, a [HistoryTimelineItemVoice.Neutral] item uses a
 *   stronger indicator color. Has no effect on Positive or Critical voices. Defaults to false.
 * @param subheading - Optional [String] secondary text shown below the label (e.g. a date or timestamp).
 * @param description - Optional [String] longer description shown below the subheading with extra vertical padding.
 */
@Stable
public data class HistoryTimelineItem(
    val label: String,
    val voice: HistoryTimelineItemVoice,
    val selected: Boolean = false,
    val subheading: String? = null,
    val description: String? = null,
)

/**
 * Lemonade history timeline component. Displays a vertical list of chronological events, each
 * with a colored indicator dot connected by a subtle line.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.HistoryTimeline(
 *     items = listOf(
 *         HistoryTimelineItem(
 *             label = "Order placed",
 *             subheading = "March 15, 2026",
 *             voice = HistoryTimelineItemVoice.Positive,
 *         ),
 *         HistoryTimelineItem(
 *             label = "Shipping",
 *             subheading = "In transit",
 *             voice = HistoryTimelineItemVoice.Neutral,
 *             selected = true,
 *         ),
 *     ),
 * )
 * ```
 *
 * @param items - [List] of [HistoryTimelineItem] entries to display in order.
 * @param modifier - [Modifier] to be applied to the timeline container.
 */
@Composable
public fun LemonadeUi.HistoryTimeline(
    items: List<HistoryTimelineItem>,
    modifier: Modifier = Modifier,
) {
    CoreHistoryTimeline(
        items = items,
        modifier = modifier,
    )
}

@Composable
private fun CoreHistoryTimeline(
    items: List<HistoryTimelineItem>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            CoreHistoryTimelineItem(
                item = item,
                isLast = index == items.lastIndex,
                isFirst = index == 0,
            )
        }
    }
}

@Composable
private fun CoreHistoryTimelineItem(
    item: HistoryTimelineItem,
    isLast: Boolean,
    isFirst: Boolean,
    modifier: Modifier = Modifier,
) {
    var contentSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Row(
        modifier = modifier.defaultMinSize(minHeight = LocalSizes.current.size1600),
    ) {
        Box(
            modifier = Modifier
                .width(width = LocalSizes.current.size800)
                .height(
                    height = with(LocalDensity.current) {
                        maxOf(
                            a = LocalSizes.current.size1600,
                            b = contentSize.height.toDp(),
                        )
                    },
                ),
            content = {
                CoreHistoryTimelineIndicator(
                    voice = item.voice,
                    selected = item.selected,
                    isLast = isLast,
                    isFirst = isFirst,
                    modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing100),
                )
            },
        )

        Column(
            modifier = Modifier.onSizeChanged { contentSize = it },
        ) {
            LemonadeUi.Text(
                text = item.label,
                textStyle = LocalTypographies.current.bodyMediumMedium,
            )

            item.subheading?.let {
                LemonadeUi.Text(
                    text = item.subheading,
                    textStyle = LocalTypographies.current.bodySmallRegular,
                    color = LocalColors.current.content.contentSecondary,
                )
            }

            item.description?.let {
                LemonadeUi.Text(
                    text = item.description,
                    textStyle = LocalTypographies.current.bodySmallRegular,
                    color = LocalColors.current.content.contentSecondary,
                    modifier = Modifier.padding(
                        top = LocalSpaces.current.spacing200,
                        bottom = LocalSpaces.current.spacing400,
                    ),
                )
            }
        }
    }
}

@Composable
private fun CoreHistoryTimelineIndicator(
    voice: HistoryTimelineItemVoice,
    selected: Boolean,
    isLast: Boolean,
    isFirst: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
    ) {
        if (!isFirst) {
            Box(
                modifier = Modifier
                    .background(
                        color = LocalColors.current.background.bgNeutralSubtle,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = Int.MAX_VALUE.dp,
                            bottomEnd = Int.MAX_VALUE.dp,
                        ),
                    ).size(size = LocalSizes.current.size50),
            )
        } else {
            Spacer(
                modifier = Modifier.height(height = LocalSizes.current.size50),
            )
        }

        Box(
            modifier = Modifier
                .clip(shape = LocalShapes.current.radiusFull)
                .background(color = voice.color(selected = selected))
                .size(size = LocalSizes.current.size250),
        )

        if (!isLast) {
            Box(
                modifier = Modifier
                    .background(
                        color = LocalColors.current.background.bgNeutralSubtle,
                        shape = RoundedCornerShape(
                            topStart = Int.MAX_VALUE.dp,
                            topEnd = Int.MAX_VALUE.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp,
                        ),
                    ).width(width = LocalSizes.current.size50)
                    .weight(weight = 1f),
            )
        }
    }
}

@Composable
private fun HistoryTimelineItemVoice.color(selected: Boolean): Color =
    when (this) {
        HistoryTimelineItemVoice.Positive -> LocalColors.current.background.bgPositive
        HistoryTimelineItemVoice.Critical -> LocalColors.current.background.bgCritical
        HistoryTimelineItemVoice.Neutral if (!selected) -> LocalColors.current.background.bgNeutralSubtle
        HistoryTimelineItemVoice.Neutral -> LocalColors.current.background.bgNeutral
    }

@Composable
@LemonadePreview
private fun HistoryTimelinePreview() {
    LemonadeUi.HistoryTimeline(
        items = listOf(
            HistoryTimelineItem(
                label = "Label",
                subheading = "Subheading",
                voice = HistoryTimelineItemVoice.Positive,
            ),
            HistoryTimelineItem(
                label = "Label",
                subheading = "Subheading",
                description = "Description",
                voice = HistoryTimelineItemVoice.Critical,
            ),
            HistoryTimelineItem(
                label = "Label",
                subheading = "Subheading",
                voice = HistoryTimelineItemVoice.Neutral,
            ),
            HistoryTimelineItem(
                label = "Label",
                subheading = "Subheading",
                description = "Description",
                voice = HistoryTimelineItemVoice.Neutral,
                selected = true,
            ),
        ),
    )
}
