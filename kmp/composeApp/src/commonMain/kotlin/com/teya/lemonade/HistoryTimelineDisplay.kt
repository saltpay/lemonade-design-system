package com.teya.lemonade

import androidx.compose.runtime.Composable

@Composable
internal fun HistoryTimelineDisplay() {
    SampleScreenDisplayColumn(
        title = "HistoryTimeline",
    ) {
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Voices"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Positive",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Critical",
                        voice = HistoryTimelineItemVoice.Critical,
                    ),
                    HistoryTimelineItem(
                        label = "Neutral",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "With Subheadings"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Order placed",
                        subheading = "March 15, 2026 · 09:42",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Payment received",
                        subheading = "March 15, 2026 · 09:43",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Preparing shipment",
                        subheading = "March 16, 2026 · 11:12",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                    HistoryTimelineItem(
                        label = "Shipped",
                        subheading = "March 17, 2026 · 08:05",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Critical State"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Payment received",
                        subheading = "April 1, 2026",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Shipping failed",
                        subheading = "Carrier returned the package",
                        voice = HistoryTimelineItemVoice.Critical,
                    ),
                    HistoryTimelineItem(
                        label = "Redelivery scheduled",
                        subheading = "April 5, 2026",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Labels Only"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Submitted",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Reviewed",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Approved",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }
    }
}
