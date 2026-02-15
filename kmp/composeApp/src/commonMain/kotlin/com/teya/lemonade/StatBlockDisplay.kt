package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.StatBlockTrendVoice

@Composable
internal fun StatBlockDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Basic
        StatBlockSection(title = "Basic") {
            LemonadeUi.StatBlock(
                label = "Revenue",
                value = "$12,450",
            )
        }

        // With Trend
        StatBlockSection(title = "With Trend") {
            LemonadeUi.StatBlock(
                label = "Sales",
                value = "1,234",
                trend = "+12.5%",
                trendIcon = LemonadeIcons.ArrowUp,
                trendVoice = StatBlockTrendVoice.Positive,
            )
        }

        // Negative Trend
        StatBlockSection(title = "Negative Trend") {
            LemonadeUi.StatBlock(
                label = "Returns",
                value = "89",
                trend = "-3.2%",
                trendIcon = LemonadeIcons.ArrowDown,
                trendVoice = StatBlockTrendVoice.Critical,
            )
        }

        // Multiple Stats
        StatBlockSection(title = "Multiple Stats") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
                modifier = Modifier.fillMaxWidth(),
            ) {
                LemonadeUi.StatBlock(
                    label = "Revenue",
                    value = "$12,450",
                    trend = "+12.5%",
                    trendIcon = LemonadeIcons.ArrowUp,
                    trendVoice = StatBlockTrendVoice.Positive,
                    modifier = Modifier.weight(weight = 1f),
                )

                LemonadeUi.StatBlock(
                    label = "Orders",
                    value = "1,234",
                    modifier = Modifier.weight(weight = 1f),
                )

                LemonadeUi.StatBlock(
                    label = "Returns",
                    value = "89",
                    trend = "-3.2%",
                    trendIcon = LemonadeIcons.ArrowDown,
                    trendVoice = StatBlockTrendVoice.Critical,
                    modifier = Modifier.weight(weight = 1f),
                )
            }
        }
    }
}

@Composable
private fun StatBlockSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
