package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

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
 * TooltipBox component that wraps content and shows a tooltip when visible.
 *
 * This component provides an interactive way to display tooltips. The tooltip
 * appears above the content when [isVisible] is true and can be controlled
 * externally by the caller.
 *
 * ## Usage
 * ```kotlin
 * var showTooltip by remember { mutableStateOf(false) }
 * LemonadeUi.TooltipBox(
 *     tooltipText = "Additional information",
 *     isVisible = showTooltip,
 * ) {
 *     LemonadeUi.Button(
 *         label = "Tap to toggle",
 *         onClick = { showTooltip = !showTooltip },
 *     )
 * }
 * ```
 *
 * @param tooltipText - [String] text to display in the tooltip.
 * @param isVisible - [Boolean] controls whether the tooltip is shown.
 * @param modifier - [Modifier] for additional styling.
 * @param content - composable content that the tooltip is anchored to.
 */
@Composable
public fun LemonadeUi.TooltipBox(
    tooltipText: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isVisible) {
            LemonadeUi.Tooltip(
                text = tooltipText,
                modifier = Modifier.padding(bottom = LocalSpaces.current.spacing200),
            )
        }

        content()
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
