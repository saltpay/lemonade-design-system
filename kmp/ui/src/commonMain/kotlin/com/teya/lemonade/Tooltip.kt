package com.teya.lemonade

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay

/**
 * Tooltip component that displays explanatory text in a small popup.
 *
 * A Tooltip shows helpful information when the user needs additional context about an element.
 * It features a dark background with white text and rounded corners.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tooltip(
 *     text = "This is helpful information"
 * )
 * ```
 *
 * ## Parameters
 * - `text`: The text content to display in the tooltip.
 * - `modifier`: Optional [Modifier] for additional styling and layout adjustments.
 */
@Composable
public fun LemonadeUi.Tooltip(
    text: String,
    modifier: Modifier = Modifier,
) {
    CoreTooltip(
        text = text,
        modifier = modifier,
    )
}

/**
 * TooltipBox component that wraps content and shows a tooltip on long press.
 *
 * This component provides an interactive way to display tooltips by responding to long press
 * gestures on the wrapped content. The tooltip automatically appears above the content when
 * long pressed and dismisses after 2 seconds.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.TooltipBox(
 *     tooltipText = "Additional information"
 * ) {
 *     LemonadeUi.Button(
 *         label = "Press and hold",
 *         onClick = {}
 *     )
 * }
 * ```
 *
 * ## Parameters
 * - `tooltipText`: The text to display in the tooltip.
 * - `modifier`: Optional [Modifier] for additional styling and layout adjustments.
 * - `content`: The composable content that will trigger the tooltip on long press.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun LemonadeUi.TooltipBox(
    tooltipText: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var showTooltip by remember { mutableStateOf(false) }

    LaunchedEffect(showTooltip) {
        if (showTooltip) {
            delay(2000)
            showTooltip = false
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showTooltip) {
            LemonadeUi.Tooltip(
                text = tooltipText,
                modifier = Modifier.padding(bottom = LocalSpaces.current.spacing200),
            )
        }

        Box(
            modifier = Modifier.combinedClickable(
                onClick = {},
                onLongClick = {
                    showTooltip = true
                },
            ),
        ) {
            content()
        }
    }
}

@Composable
private fun CoreTooltip(
    text: String,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .clip(LocalShapes.current.radius300)
            .background(LocalColors.current.background.bgDefaultInverse)
            .padding(
                horizontal = LocalSpaces.current.spacing300,
                vertical = LocalSpaces.current.spacing200,
            ),
    ) {
        LemonadeUi.Text(
            text = text,
            textStyle = LocalTypographies.current.bodySmallRegular,
            color = LocalColors.current.content.contentPrimaryInverse,
        )
    }
}

@LemonadePreview
@Composable
internal fun PreviewTooltip() {
    Column(
        modifier = Modifier.padding(LocalSpaces.current.spacing400),
    ) {
        LemonadeUi.Tooltip(
            text = "This is a helpful tooltip",
        )
    }
}
