package com.teya.lemonade

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

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
    color: Color = LocalColors.current.border.borderNeutralHigh,
    isDashed: Boolean = false,
) {
    Canvas(
        modifier = modifier.height(1.dp)
    ) {
        val pathEffect = if (isDashed) {
            PathEffect.dashPathEffect(
                intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                phase = 0f
            )
        } else {
            null
        }

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx(),
            pathEffect = pathEffect
        )
    }
}

/**
 * A vertical divider to separate content.
 *
 * ## Usage
 * ```kotlin
 * // Simple vertical divider
 * LemonadeUi.VerticalDivider()
 *
 * // Dashed vertical divider
 * LemonadeUi.VerticalDivider(isDashed = true)
 * ```
 *
 * @param modifier - [Modifier] to be applied to the divider.
 * @param color - [Color] of the divider. Defaults to borderNeutralLow.
 * @param isDashed - [Boolean] flag to define if the divider is dashed or not.
 */
@Composable
public fun LemonadeUi.VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = LocalColors.current.border.borderNeutralLow,
    isDashed: Boolean = false,
) {
    CoreVerticalDivider(
        modifier = modifier.fillMaxHeight(),
        color = color,
        isDashed = isDashed,
    )
}

@Composable
private fun CoreVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = LocalColors.current.border.borderNeutralLow,
    isDashed: Boolean = false,
) {
    Canvas(
        modifier = modifier.width(1.dp)
    ) {
        val pathEffect = if (isDashed) {
            PathEffect.dashPathEffect(
                intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                phase = 0f
            )
        } else {
            null
        }

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = 1.dp.toPx(),
            pathEffect = pathEffect
        )
    }
}

// region Previews

private data class HorizontalDividerPreviewData(
    val label: String?,
    val isDashed: Boolean,
)

private class HorizontalDividerPreviewProvider :
    PreviewParameterProvider<HorizontalDividerPreviewData> {
    override val values: Sequence<HorizontalDividerPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<HorizontalDividerPreviewData> {
        return buildList {
            listOf(null, "OR").forEach { label ->
                listOf(true, false).forEach { isDashed ->
                    add(
                        HorizontalDividerPreviewData(
                            label = label,
                            isDashed = isDashed,
                        )
                    )
                }
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun HorizontalDividerPreview(
    @PreviewParameter(HorizontalDividerPreviewProvider::class)
    previewData: HorizontalDividerPreviewData,
) {
    LemonadeUi.HorizontalDivider(
        label = previewData.label,
        isDashed = previewData.isDashed,
    )
}

private data class VerticalDividerPreviewData(
    val isDashed: Boolean,
)

private class VerticalDividerPreviewProvider :
    PreviewParameterProvider<VerticalDividerPreviewData> {
    override val values: Sequence<VerticalDividerPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<VerticalDividerPreviewData> {
        return buildList {
            listOf(true, false).forEach { isDashed ->
                add(
                    VerticalDividerPreviewData(
                        isDashed = isDashed,
                    )
                )
            }
        }.asSequence()
    }
}

@LemonadePreview
@Composable
private fun VerticalDividerPreview(
    @PreviewParameter(VerticalDividerPreviewProvider::class)
    previewData: VerticalDividerPreviewData,
) {
    LemonadeUi.VerticalDivider(
        modifier = Modifier.height(48.dp),
        isDashed = previewData.isDashed,
    )
}

// endregion

