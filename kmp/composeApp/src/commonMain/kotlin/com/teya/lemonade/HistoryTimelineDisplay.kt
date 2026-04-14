package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.HistoryItemVoice
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.TagVoice

@Composable
internal fun HistoryTimelineDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .background(LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        LemonadeUi.Card(
            contentPadding = LemonadeCardPadding.Medium,
            header = CardHeaderConfig(title = "Timeline — positive first"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Payment sent",
                        subheading = "10:24",
                        description = "Your transfer has been initiated successfully.",
                        voice = HistoryItemVoice.Positive,
                    ),
                    HistoryTimelineItem(
                        label = "Processing",
                        subheading = "10:23",
                        description = "Bank is reviewing the transaction.",
                    ),
                    HistoryTimelineItem(
                        label = "Initiated",
                        subheading = "10:22",
                    ),
                ),
                currentIndex = 0,
            )
        }

        LemonadeUi.Card(
            contentPadding = LemonadeCardPadding.Medium,
            header = CardHeaderConfig(title = "How it works — with content slot"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Find a Visa PayPoint next to you",
                        description = "PayPoint locations collect cash deposits on our behalf.",
                        voice = HistoryItemVoice.Positive,
                        contentSlot = {
                            LemonadeUi.Button(
                                label = "Find a PayPoint",
                                onClick = {},
                                variant = LemonadeButtonVariant.Secondary,
                            )
                        },
                    ),
                    HistoryTimelineItem(
                        label = "Enter the amount and generate a barcode",
                        description = "Show the barcode to the shopkeeper and deposit the funds.",
                    ),
                    HistoryTimelineItem(
                        label = "Your money will be available in 10 minutes",
                        description = "We'll notify you once the funds are in your account.",
                    ),
                ),
                currentIndex = 0,
            )
        }

        LemonadeUi.Card(
            contentPadding = LemonadeCardPadding.Medium,
            header = CardHeaderConfig(title = "Timeline — critical current"),
        ) {
            LemonadeUi.HistoryTimeline(
                items = listOf(
                    HistoryTimelineItem(
                        label = "Payment failed",
                        subheading = "Just now",
                        description = "Your card was declined by the issuer.",
                        voice = HistoryItemVoice.Critical,
                        contentSlot = {
                            LemonadeUi.Tag(
                                label = "Declined",
                                voice = TagVoice.Critical,
                            )
                        },
                    ),
                    HistoryTimelineItem(
                        label = "Processing",
                        subheading = "09:58",
                    ),
                    HistoryTimelineItem(
                        label = "Initiated",
                        subheading = "09:57",
                    ),
                ),
                currentIndex = 0,
            )
        }

        LemonadeUi.Card(
            contentPadding = LemonadeCardPadding.Medium,
            header = CardHeaderConfig(title = "Standalone HistoryItem — neutral current"),
        ) {
            Column {
                LemonadeUi.HistoryItem(
                    label = "Standalone current item",
                    subheading = "Subheading",
                    description = "Using the standalone HistoryItem composable.",
                    voice = HistoryItemVoice.Neutral,
                    isCurrent = true,
                )
                LemonadeUi.HistoryItem(
                    label = "Past item",
                    subheading = "Subheading",
                    voice = HistoryItemVoice.Neutral,
                    isLast = true,
                )
            }
        }
    }
}
