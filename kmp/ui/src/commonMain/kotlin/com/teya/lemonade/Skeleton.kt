package com.teya.lemonade

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider


/**
 * Skeleton component, a loading placeholder that shows a shimmer animation.
 *
 * Skeleton is used to indicate that content is loading. It displays an animated
 * gradient that sweeps horizontally, giving the user a visual cue that data is
 * being fetched or processed.
 *
 * ## Usage
 * ```kotlin
 * // Text line placeholder
 * LemonadeUi.Skeleton(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(16.dp),
 * )
 *
 * // Circle avatar placeholder
 * LemonadeUi.Skeleton(
 *     modifier = Modifier.size(48.dp),
 *     shape = LemonadeTheme.shapes.radiusFull,
 * )
 * ```
 *
 * ## Parameters
 * @param modifier Optional [Modifier] for additional styling and layout adjustments.
 *   The caller is expected to set width and height via this modifier.
 * @param shape The [Shape] to be applied to the skeleton. Defaults to [LemonadeShapes.radius200].
 */
@Composable
public fun LemonadeUi.Skeleton(
    modifier: Modifier = Modifier,
    shape: Shape = LocalShapes.current.radius200,
) {
    CoreSkeleton(
        shape = shape,
        modifier = modifier,
    )
}

@Composable
private fun CoreSkeleton(
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    val baseColor = LocalColors.current.background.bgSubtle
    val highlightColor = LocalColors.current.background.bgElevated

    val infiniteTransition = rememberInfiniteTransition(label = "SkeletonShimmer")

    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ShimmerProgress",
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor,
        ),
        start = Offset(
            x = shimmerProgress * 1000f - 300f,
            y = 0f,
        ),
        end = Offset(
            x = shimmerProgress * 1000f + 300f,
            y = 0f,
        ),
    )

    Box(
        modifier = modifier
            .background(
                brush = shimmerBrush,
                shape = shape,
            ),
    )
}

private data class SkeletonPreviewData(
    val shape: Shape,
    val label: String,
)

private class SkeletonPreviewProvider : PreviewParameterProvider<SkeletonPreviewData> {
    override val values: Sequence<SkeletonPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<SkeletonPreviewData> {
        return buildList {
            add(
                element = SkeletonPreviewData(
                    shape = InternalLemonadeShapes().radius200,
                    label = "radius200",
                )
            )
            add(
                element = SkeletonPreviewData(
                    shape = InternalLemonadeShapes().radius400,
                    label = "radius400",
                )
            )
            add(
                element = SkeletonPreviewData(
                    shape = InternalLemonadeShapes().radiusFull,
                    label = "radiusFull",
                )
            )
        }.asSequence()
    }
}

@Composable
@LemonadePreview
private fun SkeletonPreview(
    @PreviewParameter(SkeletonPreviewProvider::class)
    previewData: SkeletonPreviewData,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
        LemonadeUi.Skeleton(
            modifier = Modifier
                .width(width = 200.dp)
                .height(height = 16.dp),
            shape = previewData.shape,
        )
    }
}
