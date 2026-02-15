package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.StatBlockTrendVoice
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A display component for showing a statistic with label, value, and optional trend indicator.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.StatBlock(
 *     label = "Revenue",
 *     value = "$12,450",
 *     trend = "+12.5%",
 *     trendIcon = LemonadeIcons.ArrowUp,
 *     trendVoice = StatBlockTrendVoice.Positive,
 * )
 * ```
 *
 * @param label - [String] descriptive label for the statistic.
 * @param value - [String] the main value to display prominently.
 * @param modifier - [Modifier] to be applied to the root container.
 * @param trend - optional [String] trend text to display (e.g. "+12.5%").
 * @param trendIcon - optional [LemonadeIcons] icon to show alongside the trend text.
 * @param trendVoice - [StatBlockTrendVoice] to define the tone of the trend indicator.
 *  Defaults to [StatBlockTrendVoice.Neutral].
 */
@Composable
public fun LemonadeUi.StatBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    trend: String? = null,
    trendIcon: LemonadeIcons? = null,
    trendVoice: StatBlockTrendVoice = StatBlockTrendVoice.Neutral,
) {
    CoreStatBlock(
        label = label,
        value = value,
        trend = trend,
        trendIcon = trendIcon,
        trendVoice = trendVoice,
        modifier = modifier,
    )
}

@Composable
private fun CoreStatBlock(
    label: String,
    value: String,
    trend: String?,
    trendIcon: LemonadeIcons?,
    trendVoice: StatBlockTrendVoice,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
        modifier = modifier,
    ) {
        LemonadeUi.Text(
            text = value,
            textStyle = LocalTypographies.current.headingMedium,
            color = LocalColors.current.content.contentPrimary,
        )

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.bodySmallRegular,
            color = LocalColors.current.content.contentSecondary,
        )

        if (trend != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
            ) {
                if (trendIcon != null) {
                    LemonadeUi.Icon(
                        icon = trendIcon,
                        contentDescription = null,
                        tint = trendVoice.trendColor,
                        size = LemonadeAssetSize.Small,
                    )
                }

                LemonadeUi.Text(
                    text = trend,
                    textStyle = LocalTypographies.current.bodySmallMedium,
                    color = trendVoice.trendColor,
                )
            }
        }
    }
}

private val StatBlockTrendVoice.trendColor: Color
    @Composable get() {
        return when (this) {
            StatBlockTrendVoice.Neutral -> LocalColors.current.content.contentSecondary
            StatBlockTrendVoice.Positive -> LocalColors.current.content.contentPositive
            StatBlockTrendVoice.Critical -> LocalColors.current.content.contentCritical
        }
    }

private data class StatBlockPreviewData(
    val trend: String?,
    val trendVoice: StatBlockTrendVoice,
    val withIcon: Boolean,
)

private class StatBlockPreviewProvider :
    PreviewParameterProvider<StatBlockPreviewData> {
    override val values: Sequence<StatBlockPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<StatBlockPreviewData> {
        return buildList {
            add(
                element = StatBlockPreviewData(
                    trend = null,
                    trendVoice = StatBlockTrendVoice.Neutral,
                    withIcon = false,
                )
            )
            listOf(true, false).forEach { withIcon ->
                StatBlockTrendVoice.entries.forEach { voice ->
                    add(
                        element = StatBlockPreviewData(
                            trend = "+12.5%",
                            trendVoice = voice,
                            withIcon = withIcon,
                        )
                    )
                }
            }
        }.asSequence()
    }
}

@Composable
@LemonadePreview
private fun StatBlockPreview(
    @PreviewParameter(StatBlockPreviewProvider::class)
    previewData: StatBlockPreviewData,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
        LemonadeUi.StatBlock(
            label = "Revenue",
            value = "$12,450",
            trend = previewData.trend,
            trendIcon = LemonadeIcons.ArrowUp.takeIf { previewData.withIcon },
            trendVoice = previewData.trendVoice,
        )
    }
}
