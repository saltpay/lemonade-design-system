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
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeSkeletonSize
import com.teya.lemonade.core.LemonadeSkeletonVariant
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider


/**
 * A skeleton loading placeholder displayed as a horizontal line with a shimmer animation.
 *
 * [LineSkeleton] is used to indicate that text content is loading. It displays a
 * horizontal rectangle with an animated fade effect that cycles smoothly, providing
 * a visual cue that data is being fetched or processed.
 *
 * The height of the skeleton is determined by the [size] parameter, while the width
 * can be controlled through the [modifier] parameter.
 *
 * ## Animation
 * The skeleton animates with a fade in/out effect cycling over 1000ms using an ease-in-out curve.
 * The opacity oscillates between 20% and 60% to create a subtle shimmer effect.
 *
 * ## Variants
 * For circular placeholders, use [CircleSkeleton].
 * For block/card placeholders, use [BlockSkeleton].
 *
 * ## Usage
 * ```kotlin
 * // Text line placeholder with default size
 * LemonadeUi.LineSkeleton(
 *     modifier = Modifier
 *         .fillMaxWidth()
 * )
 *
 * // Text line placeholder with custom size
 * LemonadeUi.LineSkeleton(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .padding(horizontal = LemonadeTheme.spaces.spacing400),
 *     size = LemonadeSkeletonSize.Small,
 * )
 * ```
 *
 * ## Parameters
 * @param modifier Optional [Modifier] for layout adjustments. Width can be customized here.
 * @param size The height of the skeleton line. Defaults to [LemonadeSkeletonSize.Medium].
 */
@Composable
public fun LemonadeUi.LineSkeleton(
    modifier: Modifier = Modifier,
    size: LemonadeSkeletonSize = LemonadeSkeletonSize.Medium,
) {
    CoreSkeleton(
        modifier = modifier,
        size = size,
        variant = LemonadeSkeletonVariant.Line
    )
}

/**
 * A skeleton loading placeholder displayed as a circle with a shimmer animation.
 *
 * [CircleSkeleton] is used to indicate that avatar or circular images are loading.
 * It displays a perfect circle with an animated fade effect that cycles smoothly,
 * providing a visual cue that data is being fetched or processed.
 *
 * The size of the circular skeleton is determined by the [size] parameter.
 * All dimensions (width and height) are equal to maintain a perfect circle.
 *
 * ## Animation
 * The skeleton animates with a fade in/out effect cycling over 1000ms using an ease-in-out curve.
 * The opacity oscillates between 20% and 60% to create a subtle shimmer effect.
 *
 * ## Size Mapping
 * The [size] parameter maps to different diameter values defined in the design system:
 * (via `LemonadeSkeletonSize` and the underlying sizing tokens such as `LemonadeAssetSize`).
 * Refer to those types for the exact values used in this implementation.
 *
 * ## Variants
 * For text line placeholders, use [LineSkeleton].
 * For card/block placeholders, use [BlockSkeleton].
 *
 * ## Usage
 * ```kotlin
 * // Avatar placeholder with default size
 * LemonadeUi.CircleSkeleton(
 *     size = LemonadeSkeletonSize.Medium,
 * )
 *
 * // Smaller avatar placeholder
 * LemonadeUi.CircleSkeleton(
 *     size = LemonadeSkeletonSize.Small,
 * )
 * ```
 *
 * ## Parameters
 * @param modifier Optional [Modifier] for additional positioning or layout adjustments.
 * @param size The diameter of the circular skeleton. Defaults to [LemonadeSkeletonSize.Medium].
 */
@Composable
public fun LemonadeUi.CircleSkeleton(
    modifier: Modifier = Modifier,
    size: LemonadeSkeletonSize = LemonadeSkeletonSize.Medium,
) {
    CoreSkeleton(
        modifier = modifier,
        size = size,
        variant = LemonadeSkeletonVariant.Circle
    )
}

/**
 * A skeleton loading placeholder displayed as a large block/card with a shimmer animation.
 *
 * [BlockSkeleton] is used to indicate that card content, image content, or other
 * large block elements are loading. It displays a tall rectangle with an animated
 * fade effect that cycles smoothly, providing a visual cue that data is being
 * fetched or processed.
 *
 * The block skeleton has a default fixed height (spacing1600 -> 64.dp) suitable for card or image placeholders,
 * while the width can be controlled through the [modifier] parameter. It uses a large
 * rounded corner radius appropriate for prominent content areas.
 *
 * ## Animation
 * The skeleton animates with a fade in/out effect cycling over 1000ms using an ease-in-out curve.
 * The opacity oscillates between 20% and 60% to create a subtle shimmer effect.
 *
 * ## Variants
 * For text line placeholders, use [LineSkeleton].
 * For circular avatar placeholders, use [CircleSkeleton].
 *
 * ## Usage
 * ```kotlin
 * // Card/image placeholder with full width
 * LemonadeUi.BlockSkeleton(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .padding(horizontal = LemonadeTheme.spaces.spacing400)
 * )
 *
 * // Customized block placeholder
 * LemonadeUi.BlockSkeleton(
 *     modifier = Modifier
 *         .fillMaxWidth(0.9f)
 *         .padding(LemonadeTheme.spaces.spacing400)
 * )
 * ```
 *
 * ## Parameters
 * @param modifier Optional [Modifier] for layout adjustments. Width can be customized here.
 * @param size Currently unused for BlockSkeleton but kept for API consistency. Defaults to [LemonadeSkeletonSize.Medium].
 */
@Composable
public fun LemonadeUi.BlockSkeleton(
    modifier: Modifier = Modifier,
    size: LemonadeSkeletonSize = LemonadeSkeletonSize.Medium,
) {
    CoreSkeleton(
        modifier = modifier,
        size = size,
        variant = LemonadeSkeletonVariant.Block
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

    val skeletonSize = size.toSkeletonSizeDimensions(variant = variant)

    Box(
        modifier = modifier
            .height(height = skeletonSize.height)
            .width(width = skeletonSize.width)
            .padding(vertical = variant.variantData.verticalSpacing)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = variant.variantData.radius)
                .alpha(alpha = fadeOpacity)
                .background(color = LocalColors.current.background.bgElevatedHigh)
        )
    }
}

private val LemonadeAssetSize.dp: Dp
    @Composable get() {
        return when (this) {
            LemonadeAssetSize.XSmall -> LocalSizes.current.size300
            LemonadeAssetSize.Small -> LocalSizes.current.size400
            LemonadeAssetSize.Medium -> LocalSizes.current.size500
            LemonadeAssetSize.Large -> LocalSizes.current.size600
            LemonadeAssetSize.XLarge -> LocalSizes.current.size800
            LemonadeAssetSize.XXLarge -> LocalSizes.current.size1000
            LemonadeAssetSize.XXXLarge -> LocalSizes.current.size1200
        }
    }

@Stable
private data class SkeletonSizeDimensions(
    val width: Dp,
    val height: Dp,
)

@Composable
private fun LemonadeSkeletonSize.toSkeletonSizeDimensions(variant: LemonadeSkeletonVariant): SkeletonSizeDimensions {
    return when (variant) {
        LemonadeSkeletonVariant.Line -> {
            val heightSize = when (this) {
                LemonadeSkeletonSize.XSmall -> LocalSizes.current.size400
                LemonadeSkeletonSize.Small -> LocalSizes.current.size500
                LemonadeSkeletonSize.Medium -> LocalSizes.current.size600
                LemonadeSkeletonSize.Large -> LocalSizes.current.size700
                LemonadeSkeletonSize.XLarge -> LocalSizes.current.size800
                LemonadeSkeletonSize.XXLarge -> LocalSizes.current.size900
                else -> LocalSizes.current.size1000
            }
            SkeletonSizeDimensions(
                width = Dp.Unspecified,
                height = heightSize,
            )
        }

        LemonadeSkeletonVariant.Block -> {
            SkeletonSizeDimensions(
                width = Dp.Unspecified,
                height = LocalSizes.current.size1600,
            )
        }

        LemonadeSkeletonVariant.Circle -> {
            val size = when (this) {
                LemonadeSkeletonSize.XSmall -> LemonadeAssetSize.XSmall.dp
                LemonadeSkeletonSize.Small -> LemonadeAssetSize.Small.dp
                LemonadeSkeletonSize.Medium -> LemonadeAssetSize.Medium.dp
                LemonadeSkeletonSize.Large -> LemonadeAssetSize.Large.dp
                LemonadeSkeletonSize.XLarge -> LemonadeAssetSize.XLarge.dp
                LemonadeSkeletonSize.XXLarge -> LemonadeAssetSize.XXLarge.dp
                LemonadeSkeletonSize.XXXLarge -> LemonadeAssetSize.XXXLarge.dp
            }
            SkeletonSizeDimensions(
                width = size,
                height = size,
            )
        }
    }
}

@Stable
private data class LemonadeSkeletonVariants(
    val verticalSpacing: Dp,
    val radius: Shape
)

private val LemonadeSkeletonVariant.variantData: LemonadeSkeletonVariants
    @Composable get() {
        return when (this) {
            LemonadeSkeletonVariant.Line -> LemonadeSkeletonVariants(
                verticalSpacing = LocalSpaces.current.spacing100,
                radius = LocalShapes.current.radius100
            )

            LemonadeSkeletonVariant.Block -> LemonadeSkeletonVariants(
                verticalSpacing = LocalSpaces.current.spacing0,
                radius = LocalShapes.current.radius500
            )

            LemonadeSkeletonVariant.Circle -> LemonadeSkeletonVariants(
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
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        // Line variant with different sizes
        LemonadeUi.LineSkeleton(
            modifier = Modifier.width(width = 200.dp),
            size = LemonadeSkeletonSize.Small,
        )
        LemonadeUi.LineSkeleton(
            modifier = Modifier.width(width = 200.dp),
            size = LemonadeSkeletonSize.Medium,
        )
        LemonadeUi.LineSkeleton(
            modifier = Modifier.width(width = 200.dp),
            size = LemonadeSkeletonSize.Large,
        )

        // Circle variant with different sizes
        LemonadeUi.CircleSkeleton(
            size = LemonadeSkeletonSize.Small,
        )
        LemonadeUi.CircleSkeleton(
            size = LemonadeSkeletonSize.Medium,
        )
        LemonadeUi.CircleSkeleton(
            size = LemonadeSkeletonSize.Large,
        )
    }
}
