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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        TooltipSection(title = "Interactive (Tap to Toggle)") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Text(
                    text = "Tap buttons below to toggle their tooltips",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentSecondary,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                ) {
                    var showPrimary by remember { mutableStateOf(value = false) }
                    LemonadeUi.TooltipBox(
                        tooltipText = "Primary action button",
                        isVisible = showPrimary,
                    ) {
                        LemonadeUi.Button(
                            label = "Press me",
                            onClick = { showPrimary = !showPrimary },
                            variant = LemonadeButtonVariant.Primary,
                            size = LemonadeButtonSize.Medium,
                        )
                    }

                    var showSecondary by remember { mutableStateOf(value = false) }
                    LemonadeUi.TooltipBox(
                        tooltipText = "Secondary action",
                        isVisible = showSecondary,
                    ) {
                        LemonadeUi.Button(
                            label = "Or me",
                            onClick = { showSecondary = !showSecondary },
                            variant = LemonadeButtonVariant.Secondary,
                            size = LemonadeButtonSize.Medium,
                        )
                    }
                }

                var showNeutral by remember { mutableStateOf(value = false) }
                LemonadeUi.TooltipBox(
                    tooltipText = "This tooltip appears on tap",
                    isVisible = showNeutral,
                ) {
                    LemonadeUi.Button(
                        label = "Tap for tooltip",
                        onClick = { showNeutral = !showNeutral },
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
