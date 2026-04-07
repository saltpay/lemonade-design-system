package com.teya.lemonade

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

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
            )
        }
    }
}

@Composable
private fun CoreHistoryTimelineItem(
    item: HistoryTimelineItem,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    var contentSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Row(
        modifier = modifier.defaultMinSize(minHeight = LocalSizes.current.size1600),
    ) {
        CoreHistoryTimelineIndicator(
            voice = item.voice,
            isLast = isLast,
            contentSize = contentSize,
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
        }
    }
}

@Composable
private fun CoreHistoryTimelineIndicator(
    voice: HistoryTimelineItemVoice,
    contentSize: IntSize,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val spacing = LocalSpaces.current
    val indicatorHeight by animateDpAsState(
        targetValue = with(density) {
            contentSize.height.toDp() - spacing.spacing300
        }
            .coerceAtLeast(minimumValue = 0.dp)
    )
    LemonadeBadgeBox(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .clip(shape = LocalShapes.current.radiusFull)
                    .background(color = voice.color(selected = isLast))
                    .requiredSize(size = LocalSizes.current.size250)
            )
        },
        badge = {
            Box(
                modifier = Modifier
                    .background(
                        color = LocalColors.current.background.bgNeutralSubtle,
                        shape = LocalShapes.current.radiusFull,
                    )
                    .width(width = LocalSizes.current.size50)
                    .height(height = indicatorHeight),
            )
        },
        badgeOffset = { contentSize ->
            with(density) {
                DpOffset(
                    x = (contentSize.width / 2f).toDp(),
                    y = contentSize.height.toDp() + spacing.spacing100,
                )
            }
        }
    )
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


