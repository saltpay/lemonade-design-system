package com.teya.lemonade

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

private const val FLOAT_VISIBILITY_THRESHOLD = 0.01f

private val ColorDefaultSpring: AnimationSpec<Color> = spring()

private val DpDefaultSpring: AnimationSpec<Dp> = spring(visibilityThreshold = Dp.VisibilityThreshold)

private val FloatDefaultSpring: AnimationSpec<Float> =
    spring(visibilityThreshold = FLOAT_VISIBILITY_THRESHOLD)

/**
 * Returns this spec while animations are enabled, or [snap] when [LemonadeTheme.animations]
 * disables them, so the value applies in a single frame.
 */
@Composable
@ReadOnlyComposable
internal fun <T> AnimationSpec<T>.orSnap(): AnimationSpec<T> =
    if (LemonadeTheme.animationsEnabled) this else snap()

/**
 * Returns this spec while animations are enabled, or [snap] when [LemonadeTheme.animations]
 * disables them, so the value applies in a single frame.
 */
@Composable
@ReadOnlyComposable
internal fun <T> FiniteAnimationSpec<T>.orSnap(): FiniteAnimationSpec<T> =
    if (LemonadeTheme.animationsEnabled) this else snap()

/**
 * Returns this transform while [animationsEnabled] is true, or a fully snapping transform
 * otherwise. Meant for `transitionSpec` lambdas, which are not composable — hoist
 * [LemonadeTheme.animationsEnabled] in the enclosing composable and pass it in.
 */
internal fun ContentTransform.orSnap(animationsEnabled: Boolean): ContentTransform =
    if (animationsEnabled) this else snapContentTransform()

private fun snapContentTransform(): ContentTransform =
    ContentTransform(
        targetContentEnter = fadeIn(animationSpec = snap()),
        initialContentExit = fadeOut(animationSpec = snap()),
        sizeTransform = SizeTransform { _, _ -> snap() },
    )

/**
 * [animateColorAsState] honoring [LemonadeTheme.animations]. Defaults match the stock overload.
 */
@Composable
internal fun lemonadeAnimateColorAsState(
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = ColorDefaultSpring,
    label: String = "ColorAnimation",
): State<Color> =
    animateColorAsState(
        targetValue = targetValue,
        animationSpec = animationSpec.orSnap(),
        label = label,
    )

/**
 * [animateDpAsState] honoring [LemonadeTheme.animations]. Defaults match the stock overload.
 */
@Composable
internal fun lemonadeAnimateDpAsState(
    targetValue: Dp,
    animationSpec: AnimationSpec<Dp> = DpDefaultSpring,
    label: String = "DpAnimation",
): State<Dp> =
    animateDpAsState(
        targetValue = targetValue,
        animationSpec = animationSpec.orSnap(),
        label = label,
    )

/**
 * [animateFloatAsState] honoring [LemonadeTheme.animations]. Defaults match the stock overload.
 */
@Composable
internal fun lemonadeAnimateFloatAsState(
    targetValue: Float,
    animationSpec: AnimationSpec<Float> = FloatDefaultSpring,
    label: String = "FloatAnimation",
): State<Float> =
    animateFloatAsState(
        targetValue = targetValue,
        animationSpec = animationSpec.orSnap(),
        label = label,
    )

/**
 * [animateValueAsState] honoring [LemonadeTheme.animations].
 */
@Composable
internal fun <T, V : AnimationVector> lemonadeAnimateValueAsState(
    targetValue: T,
    typeConverter: TwoWayConverter<T, V>,
    animationSpec: AnimationSpec<T>,
    label: String = "ValueAnimation",
): State<T> =
    animateValueAsState(
        targetValue = targetValue,
        typeConverter = typeConverter,
        animationSpec = animationSpec.orSnap(),
        label = label,
    )
