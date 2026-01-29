package com.teya.lemonade

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

/**
 * A horizontal divider to separate content. Optionally displays a label in the center.
 *
 * ## Usage
 * ```kotlin
 * // Simple divider
 * LemonadeUi.HorizontalDivider()
 *
 * // Divider with label
 * LemonadeUi.HorizontalDivider(label = "OR")
 * ```
 *
 * @param modifier - [Modifier] to be applied to the divider.
 * @param label - Optional [String] label to display in the center of the divider.
 * @param color - [Color] of the divider. Defaults to borderNeutralLow.
 * @param isDashed - [Boolean] flag to define if the divider is dashed or not.
 */
@Composable
public fun LemonadeUi.HorizontalDivider(
    modifier: Modifier = Modifier,
    label: String? = null,
    color: Color = LocalColors.current.border.borderNeutralLow,
    isDashed: Boolean = false,
) {
    if (label != null) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing300),
        ) {
            CoreDivider(
                modifier = Modifier.weight(1f),
                color = color,
                isDashed = isDashed,
            )
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodyXSmallRegular,
                color = LocalColors.current.content.contentSecondary,
            )
            CoreDivider(
                modifier = Modifier.weight(1f),
                color = color,
                isDashed = isDashed,
            )
        }
    } else {
        CoreDivider(
            modifier = modifier.fillMaxWidth(),
            color = color,
            isDashed = isDashed,
        )
    }
}

@Composable
private fun CoreDivider(
    modifier: Modifier = Modifier,
    color: Color = LocalColors.current.border.borderNeutralLow,
    isDashed: Boolean = false,
) {
    val pathEffect = remember(isDashed) {
        if (isDashed) {
            PathEffect.dashPathEffect(
                intervals = floatArrayOf(4f, 4f),
                phase = 0f
            )
        } else {
            null
        }
    }

    Canvas(
        modifier = modifier.height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx(),
            pathEffect = pathEffect
        )
    }
}