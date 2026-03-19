package com.teya.lemonade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// ── Voice ────────────────────────────────────────────────────────────────

/**
 * Defines the tone of a toast notification.
 */
public enum class ToastVoice {
    Success,
    Error,
    Neutral,
}

// ── Duration ─────────────────────────────────────────────────────────────

/**
 * Duration for which a toast is displayed before auto-dismissal.
 */
public sealed class ToastDuration(public val millis: kotlin.Long) {
    /** 3 seconds */
    public data object Short : ToastDuration(3_000L)
    /** 6 seconds */
    public data object Medium : ToastDuration(6_000L)
    /** 9 seconds */
    public data object Long : ToastDuration(9_000L)
    /** Custom duration */
    public data class Custom(val customMillis: kotlin.Long) : ToastDuration(customMillis) {
        init {
            require(customMillis > 0) { "Custom toast duration must be positive, was $customMillis" }
        }
    }
}

// ── Toast Data ───────────────────────────────────────────────────────────

internal data class ToastData(
    val label: String,
    val voice: ToastVoice,
    val icon: LemonadeIcons?,
    val duration: ToastDuration,
    val dismissible: Boolean,
    val id: Int,
)

// ── State ────────────────────────────────────────────────────────────────

/**
 * State holder for the Lemonade Toast system. Obtain via [LocalLemonadeToastState].
 */
@Stable
public class LemonadeToastState {
    internal var currentToast by mutableStateOf<ToastData?>(null)
        private set

    private var nextId: Int = 0

    /**
     * Show a toast. If a toast is already visible, it is replaced immediately.
     *
     * @param label The text message to display.
     * @param voice The tone of voice — determines icon and icon color. Defaults to [ToastVoice.Neutral].
     * @param icon Optional custom icon. Only used when [voice] is [ToastVoice.Neutral].
     * @param duration How long the toast is displayed. Defaults to [ToastDuration.Short].
     * @param dismissible Whether the user can swipe to dismiss. Defaults to `true`.
     */
    public fun show(
        label: String,
        voice: ToastVoice = ToastVoice.Neutral,
        icon: LemonadeIcons? = null,
        duration: ToastDuration = ToastDuration.Short,
        dismissible: Boolean = true,
    ) {
        currentToast = ToastData(
            label = label,
            voice = voice,
            icon = icon,
            duration = duration,
            dismissible = dismissible,
            id = nextId++,
        )
    }

    /** Programmatically dismiss the current toast. */
    public fun dismiss() {
        currentToast = null
    }
}

// ── CompositionLocal ─────────────────────────────────────────────────────

/**
 * Provides the [LemonadeToastState] to the composition tree.
 * Must be used inside [LemonadeToastHost].
 */
public val LocalLemonadeToastState: ProvidableCompositionLocal<LemonadeToastState> = compositionLocalOf {
    error("No LemonadeToastState provided. Wrap your content with LemonadeToastHost.")
}

// ── Visual Component ─────────────────────────────────────────────────────

/**
 * A transient, non-interactive notification that appears at the bottom of the screen.
 *
 * ## Usage
 * ```kotlin
 * val toastState = LocalLemonadeToastState.current
 * toastState.show(label = "Changes saved", voice = ToastVoice.Success)
 * ```
 *
 * @param label The text to display.
 * @param modifier Modifier to apply to the toast container.
 * @param voice The tone — determines default icon and icon color. Defaults to [ToastVoice.Neutral].
 * @param icon Optional custom icon (only used when voice is [ToastVoice.Neutral]).
 */
@Composable
public fun LemonadeUi.Toast(
    label: String,
    modifier: Modifier = Modifier,
    voice: ToastVoice = ToastVoice.Neutral,
    icon: LemonadeIcons? = null,
) {
    CoreToast(
        label = label,
        voice = voice,
        icon = icon,
        modifier = modifier,
    )
}

@Composable
private fun CoreToast(
    label: String,
    voice: ToastVoice,
    icon: LemonadeIcons?,
    modifier: Modifier = Modifier,
) {
    val colors = LocalColors.current
    val spaces = LocalSpaces.current
    val shapes = LocalShapes.current
    val sizes = LocalSizes.current

    val resolvedIcon = when (voice) {
        ToastVoice.Success -> LemonadeIcons.CircleCheck
        ToastVoice.Error -> LemonadeIcons.CircleX
        ToastVoice.Neutral -> icon
    }

    val iconTint = when (voice) {
        ToastVoice.Success -> colors.content.contentPositiveOnColor
        ToastVoice.Error -> colors.content.contentCriticalOnColor
        ToastVoice.Neutral -> colors.content.contentNeutralOnColor
    }

    Row(
        modifier = modifier
            .heightIn(min = sizes.size1100)
            .lemonadeShadow(
                shadow = LemonadeShadow.Large,
                shape = shapes.radiusFull,
            )
            .clip(shape = shapes.radiusFull)
            .background(color = colors.background.bgDefaultInverse)
            .padding(
                horizontal = spaces.spacing400,
                vertical = spaces.spacing300,
            ),
        horizontalArrangement = Arrangement.spacedBy(spaces.spacing200),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (resolvedIcon != null) {
            LemonadeUi.Icon(
                icon = resolvedIcon,
                contentDescription = null,
                size = LemonadeAssetSize.Medium,
                tint = iconTint,
            )
        }

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.bodySmallMedium,
            color = colors.content.contentPrimaryInverse,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = spaces.spacing100),
        )
    }
}

// ── Host ─────────────────────────────────────────────────────────────────

private const val DRAG_DISMISS_THRESHOLD_DP = 25

/**
 * Provides [LemonadeToastState] to the composition tree and renders toast overlays
 * at the bottom of the screen with entry/exit animations.
 *
 * Place this at the root of your app, wrapping your main content.
 *
 * ## Usage
 * ```kotlin
 * LemonadeToastHost {
 *     // Your app content
 * }
 * ```
 */
@Composable
public fun LemonadeToastHost(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val toastState = remember { LemonadeToastState() }

    CompositionLocalProvider(LocalLemonadeToastState provides toastState) {
        Box(modifier = modifier.fillMaxSize()) {
            content()

            val toast = toastState.currentToast
            val haptic = LocalHapticFeedback.current
            val spaces = LocalSpaces.current

            // Haptic feedback + auto-dismiss timer
            LaunchedEffect(toast?.id) {
                if (toast != null) {
                    val feedbackType = if (toast.voice == ToastVoice.Neutral) {
                        HapticFeedbackType.TextHandleMove
                    } else {
                        HapticFeedbackType.LongPress
                    }
                    haptic.performHapticFeedback(feedbackType)
                    delay(toast.duration.millis)
                    toastState.dismiss()
                }
            }

            AnimatedContent(
                targetState = toast,
                contentAlignment = Alignment.BottomCenter,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                        initialOffsetY = { it * 2 },
                    ) togetherWith slideOutVertically(
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                        targetOffsetY = { it * 2 },
                    ) using SizeTransform(clip = false) { _, _ -> snap() }
                },
                contentKey = { it?.id },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = spaces.spacing600)
                    .padding(horizontal = spaces.spacing400),
            ) { animatedToast ->
                if (animatedToast != null) {
                    var offsetY by remember(animatedToast.id) { mutableStateOf(0f) }

                    val swipeModifier = if (animatedToast.dismissible) {
                        Modifier.pointerInput(animatedToast.id) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (offsetY > DRAG_DISMISS_THRESHOLD_DP.dp.toPx()) {
                                        toastState.dismiss()
                                    } else {
                                        offsetY = 0f
                                    }
                                },
                                onDragCancel = {
                                    offsetY = 0f
                                },
                                onVerticalDrag = { change, dragAmount ->
                                    if (dragAmount > 0) { // Only allow downward drag
                                        offsetY += dragAmount
                                        change.consume()
                                    }
                                },
                            )
                        }
                    } else {
                        Modifier
                    }

                    LemonadeUi.Toast(
                        label = animatedToast.label,
                        voice = animatedToast.voice,
                        icon = animatedToast.icon,
                        modifier = swipeModifier
                            .offset { IntOffset(0, offsetY.roundToInt()) }
                            .graphicsLayer {
                                alpha = 1f - (offsetY / (DRAG_DISMISS_THRESHOLD_DP.dp.toPx() * 4)).coerceIn(0f, 1f)
                            },
                    )
                }
            }
        }
    }
}
