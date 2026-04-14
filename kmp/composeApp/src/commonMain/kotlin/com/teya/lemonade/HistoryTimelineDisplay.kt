package com.teya.lemonade

import androidx.compose.runtime.Composable

@Suppress("LongMethod")
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
            header = CardHeaderConfig(title = "Selected State"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Completed step",
                        subheading = "Positive voice (unaffected by selected)",
                        voice = HistoryTimelineItemVoice.Positive,
                        selected = true,
                    ),
                    HistoryTimelineItem(
                        label = "Neutral selected",
                        subheading = "Stronger indicator color",
                        voice = HistoryTimelineItemVoice.Neutral,
                        selected = true,
                    ),
                    HistoryTimelineItem(
                        label = "Neutral unselected",
                        subheading = "Subtle indicator color",
                        voice = HistoryTimelineItemVoice.Neutral,
                        selected = false,
                    ),
                ),
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "With Descriptions"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Account created",
                        subheading = "January 10, 2026",
                        description = "Welcome to the platform! Your account has been set up and is ready to use.",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Identity verification",
                        subheading = "January 12, 2026",
                        description = "Please upload a valid government-issued ID to complete verification.",
                        voice = HistoryTimelineItemVoice.Neutral,
                        selected = true,
                    ),
                    HistoryTimelineItem(
                        label = "First transaction",
                        subheading = "Pending",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Order Tracking"),
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
                        description = "Your order is being packed at the warehouse.",
                        voice = HistoryTimelineItemVoice.Neutral,
                        selected = true,
                    ),
                    HistoryTimelineItem(
                        label = "Out for delivery",
                        subheading = "Estimated March 18, 2026",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Payment Dispute"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Transaction processed",
                        subheading = "April 1, 2026 · 14:30",
                        voice = HistoryTimelineItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Dispute opened",
                        subheading = "April 3, 2026 · 10:15",
                        description = "Customer reported an unauthorized charge of $249.99.",
                        voice = HistoryTimelineItemVoice.Critical,
                    ),
                    HistoryTimelineItem(
                        label = "Under review",
                        subheading = "April 4, 2026 · 09:00",
                        description = "Our team is investigating the dispute. Resolution expected within 5 business days.",
                        voice = HistoryTimelineItemVoice.Neutral,
                        selected = true,
                    ),
                    HistoryTimelineItem(
                        label = "Resolution",
                        subheading = "Pending",
                        voice = HistoryTimelineItemVoice.Neutral,
                    ),
                ),
            )
        }
    }
}
