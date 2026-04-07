package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

public enum class HistoryTimelineItemVoice {
    Positive,
    Critical,
    Neutral,
}

@Stable
public data class HistoryTimelineItem(
    val label: String,
    val voice: HistoryTimelineItemVoice,
    val subheading: String? = null,
)

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
    Row(
        modifier = modifier.defaultMinSize(minHeight = LocalSizes.current.size1600),
    ) {
        CoreHistoryTimelineIndicator(
            voice = item.voice,
            isLast = isLast,
            isFirst = isFirst,
            modifier = Modifier,
        )

        Column {
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
        }
    }
}

@Composable
private fun CoreHistoryTimelineIndicator(
    voice: HistoryTimelineItemVoice,
    isLast: Boolean,
    isFirst: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing50),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!isFirst) {
            Box(
                modifier = Modifier
                    .background(
                        color = LocalColors.current.background.bgNeutralSubtle,
                        shape = LocalShapes.current.radiusFull,
                    )
                    .width(width = LocalSizes.current.size50)
                    .height(height = LocalSizes.current.size150),
            )
        }

        Box(
            modifier = Modifier
                .padding(all = LocalSpaces.current.spacing200)
                .clip(shape = LocalShapes.current.radiusFull)
                .background(color = voice.color(selected = isLast))
                .requiredSize(size = LocalSizes.current.size250)
        )

        if (!isLast) {
            Box(
                modifier = Modifier
                    .background(
                        color = LocalColors.current.background.bgNeutralSubtle,
                        shape = LocalShapes.current.radiusFull,
                    )
                    .width(width = LocalSizes.current.size50)
                    .weight(weight = 1f),
            )
        }
    }
}

@Composable
private fun HistoryTimelineItemVoice.color(selected: Boolean): Color {
    return when (this) {
        HistoryTimelineItemVoice.Positive -> LocalColors.current.background.bgPositive
        HistoryTimelineItemVoice.Critical -> LocalColors.current.background.bgCritical
        HistoryTimelineItemVoice.Neutral if (!selected) -> LocalColors.current.background.bgNeutralSubtle
        HistoryTimelineItemVoice.Neutral -> LocalColors.current.background.bgNeutral
    }
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
                voice = HistoryTimelineItemVoice.Neutral,
            ),
        )
    )
}


