package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
internal fun TooltipDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Standalone
        TooltipSection(title = "Standalone") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Tooltip(
                    text = "This is a helpful tooltip",
                )

                LemonadeUi.Tooltip(
                    text = "Short tip",
                )

                LemonadeUi.Tooltip(
                    text = "This is a longer tooltip with more detailed information",
                )
            }
        }

        // Interactive
        TooltipSection(title = "Interactive (Long Press)") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Text(
                    text = "Long press any element below to see its tooltip",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentSecondary,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                ) {
                    LemonadeUi.TooltipBox(
                        tooltipText = "Primary action button",
                    ) {
                        LemonadeUi.Button(
                            label = "Press me",
                            onClick = {},
                            variant = LemonadeButtonVariant.Primary,
                            size = LemonadeButtonSize.Medium,
                        )
                    }

                    LemonadeUi.TooltipBox(
                        tooltipText = "Secondary action",
                    ) {
                        LemonadeUi.Button(
                            label = "Or me",
                            onClick = {},
                            variant = LemonadeButtonVariant.Secondary,
                            size = LemonadeButtonSize.Medium,
                        )
                    }
                }

                LemonadeUi.TooltipBox(
                    tooltipText = "This tooltip appears on long press",
                ) {
                    LemonadeUi.Button(
                        label = "Long press for tooltip",
                        onClick = {},
                        variant = LemonadeButtonVariant.Neutral,
                        size = LemonadeButtonSize.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun TooltipSection(
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
