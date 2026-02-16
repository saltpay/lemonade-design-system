package com.teya.lemonade

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeSkeletonSize
import com.teya.lemonade.core.LemonadeSkeletonVariant
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
 * )
 *
 * // Circle avatar placeholder
 * LemonadeUi.Skeleton(
 *     modifier = Modifier.size(48.dp),
 * )
 * ```
 *
 * ## Parameters
 * @param modifier Optional [Modifier] for additional styling and layout adjustments.
 */
@Composable
public fun LemonadeUi.Skeleton(
    modifier: Modifier = Modifier,
    size: LemonadeSkeletonSize = LemonadeSkeletonSize.Medium,
    variant: LemonadeSkeletonVariant = LemonadeSkeletonVariant.Line
) {
    CoreSkeleton(
        modifier = modifier,
        size = size,
        variant = variant
    )
}

@Composable
private fun CoreSkeleton(
    size: LemonadeSkeletonSize,
    modifier: Modifier = Modifier,
    variant: LemonadeSkeletonVariant
) {

    val infiniteTransition = rememberInfiniteTransition(label = "SkeletonShimmer")

    val fadeOpacity by infiniteTransition.animateFloat(
        initialValue = LocalOpacities.current.base.opacity20,
        targetValue = LocalOpacities.current.base.opacity60,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "FadeOpacity",
    )

    Box(
        modifier = modifier
            .height(height = size.size)
            .padding(vertical = variant.variantData.verticalSpacing)
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .clip(shape = variant.variantData.radius)
                .alpha(alpha = fadeOpacity)
                .background(color = LocalColors.current.background.bgElevatedHigh)
        )
    }
}

private val LemonadeSkeletonSize.size: Dp
    @Composable get() {
        return when (this) {
            LemonadeSkeletonSize.XSmall -> LocalSizes.current.size400
            LemonadeSkeletonSize.Small -> LocalSizes.current.size500
            LemonadeSkeletonSize.Medium -> LocalSizes.current.size600
            LemonadeSkeletonSize.Large -> LocalSizes.current.size700
            LemonadeSkeletonSize.XLarge -> LocalSizes.current.size800
            LemonadeSkeletonSize.XXLarge -> LocalSizes.current.size900
        }
    }

@Stable
private data class LemonadeSkeletonShapes(
    val verticalSpacing: Dp,
    val radius: Shape
)

private val LemonadeSkeletonVariant.variantData: LemonadeSkeletonShapes
    @Composable get() {
        return when (this) {
            LemonadeSkeletonVariant.Line -> LemonadeSkeletonShapes(
                verticalSpacing = LocalSpaces.current.spacing100,
                radius = LocalShapes.current.radius100

            )

            LemonadeSkeletonVariant.Circle -> LemonadeSkeletonShapes(
                verticalSpacing = LocalSpaces.current.spacing0,
                radius = LocalShapes.current.radiusFull

            )
        }
    }

private data class SkeletonPreviewData(
    val shape: Shape,
)

private class SkeletonPreviewProvider : PreviewParameterProvider<SkeletonPreviewData> {
    override val values: Sequence<SkeletonPreviewData> = buildAllVariants()

    private fun buildAllVariants(): Sequence<SkeletonPreviewData> {
        return buildList {
            add(
                element = SkeletonPreviewData(
                    shape = InternalLemonadeShapes().radius200,
                )
            )
            add(
                element = SkeletonPreviewData(
                    shape = InternalLemonadeShapes().radius400,
                )
            )
            add(
                element = SkeletonPreviewData(
                    shape = InternalLemonadeShapes().radiusFull,
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
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        LemonadeUi.Skeleton(
            modifier = Modifier
                .width(width = 200.dp)
                .height(height = 16.dp),
        )
    }
}
