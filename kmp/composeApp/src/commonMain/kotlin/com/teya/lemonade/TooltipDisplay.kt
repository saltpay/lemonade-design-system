package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.TooltipFooterActionVariant
import com.teya.lemonade.core.TooltipIndicatorPlacement

private const val FEES_ANCHOR = "fees-info"
private const val TAKINGS_ANCHOR = "takings"
private const val REPORTS_ANCHOR = "reports"

@Composable
internal fun TooltipDisplay() {
    SampleScreenDisplayColumn("Tooltip", itemsSpacing = LemonadeTheme.spaces.spacing600) {
        TooltipSection("Anchored — on-demand help") {
            TooltipOnDemandHelpExample()
        }

        TooltipSection("Anchored — guided tour") {
            TooltipTourManagerExample()
        }

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
private fun TooltipOnDemandHelpExample() {
    val tooltips = LocalLemonadeTooltipState.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LemonadeUi.Text(
            text = "Daily fees",
            textStyle = LemonadeTheme.typography.bodyMediumRegular,
        )
        LemonadeUi.IconButton(
            icon = LemonadeIcons.CircleInfo,
            contentDescription = "About fees",
            onClick = {
                tooltips.show(
                    anchor = FEES_ANCHOR,
                    title = "Daily fees",
                    content = "Fees are deducted once a day, just after midnight.",
                )
            },
            modifier = Modifier.lemonadeTooltipAnchor(key = FEES_ANCHOR),
        )
    }
}

@Composable
private fun TooltipTourManagerExample() {
    val tooltips = LocalLemonadeTooltipState.current
    val toasts = LocalLemonadeToastState.current

    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LemonadeUi.Tag(
                label = "Takings",
                modifier = Modifier.lemonadeTooltipAnchor(key = TAKINGS_ANCHOR),
            )
            LemonadeUi.Tag(
                label = "Reports",
                modifier = Modifier.lemonadeTooltipAnchor(key = REPORTS_ANCHOR),
            )
        }

        LemonadeUi.Button(
            label = "Start tour",
            onClick = {
                tooltips.startTour(
                    steps = listOf(
                        LemonadeTooltipStep(
                            anchor = TAKINGS_ANCHOR,
                            title = "Daily takings",
                            content = "Everything you sold today, updated as it happens.",
                        ),
                        LemonadeTooltipStep(
                            anchor = REPORTS_ANCHOR,
                            title = "Reports",
                            content = "Dig into the numbers over any period you like.",
                        ),
                        LemonadeTooltipStep(
                            anchor = FEES_ANCHOR,
                            title = "Fees",
                            content = "Tap here whenever you want the fee breakdown.",
                        ),
                    ),
                    onFinish = { toasts.show(label = "Tour finished", voice = ToastVoice.Success) },
                    onSkip = { toasts.show(label = "Tour skipped") },
                )
            },
        )
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
