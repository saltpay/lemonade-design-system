package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.TooltipFooterActionVariant
import com.teya.lemonade.core.TooltipIndicatorPlacement

@Composable
internal fun TooltipDisplay() {
    SampleScreenDisplayColumn("Tooltip", itemsSpacing = LemonadeTheme.spaces.spacing600) {
        TooltipSection("Indicator Placements") {
            TooltipIndicatorPlacement.entries.forEach { placement ->
                LemonadeUi.Tooltip(
                    content = "Tap here to see everything you sold today.",
                    title = placement.name,
                    indicatorPlacement = placement,
                )
            }
        }

        TooltipSection("Content Only") {
            LemonadeUi.Tooltip(
                content = "A tooltip with no title, no cover and no footer.",
                indicatorPlacement = TooltipIndicatorPlacement.TopCenter,
            )
        }

        TooltipSection("With Close Button") {
            TooltipWithCloseButton()
        }

        TooltipSection("With Cover") {
            LemonadeUi.Tooltip(
                content = "The cover slot takes any composable — an illustration, a screenshot, a video.",
                title = "Cover slot",
                indicatorPlacement = TooltipIndicatorPlacement.BottomCenter,
                cover = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = LemonadeTheme.colors.background.bgBrandSubtle),
                    )
                },
            )
        }

        TooltipSection("With Footer") {
            TooltipTourExample()
        }

        TooltipSection("Footer Without Step Counter") {
            LemonadeUi.Tooltip(
                content = "A footer can hold actions on their own — the step counter is optional.",
                title = "Actions only",
                indicatorPlacement = TooltipIndicatorPlacement.BottomRight,
                footer = {
                    Spacer(modifier = Modifier.weight(weight = 1f))
                    Action(
                        label = "Got it",
                        onClick = {},
                    )
                },
            )
        }
    }
}

@Composable
private fun TooltipWithCloseButton() {
    var dismissed by remember { mutableStateOf(false) }

    if (dismissed) {
        LemonadeUi.Button(
            label = "Show again",
            onClick = { dismissed = false },
        )
    } else {
        LemonadeUi.Tooltip(
            content = "Close buttons appear only when you pass onCloseClick.",
            title = "Dismissible",
            indicatorPlacement = TooltipIndicatorPlacement.TopRight,
            onCloseClick = { dismissed = true },
            closeContentDescription = "Close",
        )
    }
}

@Composable
private fun TooltipTourExample() {
    val totalSteps = 3
    var step by remember { mutableStateOf(1) }

    LemonadeUi.Tooltip(
        content = "The footer is a scoped slot — only the step counter and actions belong in it.",
        title = "Step $step",
        indicatorPlacement = TooltipIndicatorPlacement.TopLeft,
        onCloseClick = { step = 1 },
        closeContentDescription = "Close",
        footer = {
            StepCounter(
                currentStep = step,
                totalSteps = totalSteps,
                modifier = Modifier.weight(weight = 1f),
            )
            Action(
                label = "Skip",
                onClick = { step = 1 },
                variant = TooltipFooterActionVariant.Secondary,
            )
            Action(
                label = "Next",
                onClick = { step = if (step < totalSteps) step + 1 else 1 },
            )
        },
    )
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
