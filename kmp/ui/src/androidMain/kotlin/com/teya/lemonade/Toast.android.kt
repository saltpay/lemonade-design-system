package com.teya.lemonade

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.coroutines.delay
import android.graphics.Color as AndroidColor

// Let the window's flags/size/gravity settle before animating, else the enter flicks.
private const val SHOW_DELAY_MS = 100L

@Composable
internal actual fun PlatformToastHost(
    modifier: Modifier,
    toastState: LemonadeToastState,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) { content() }
    ToastOverlayWindow(toastState = toastState)
}

/**
 * Draws the toast in its own [Dialog] window so it z-orders above any open ModalBottomSheet / Dialog.
 * `FLAG_NOT_FOCUSABLE` (implies `FLAG_NOT_TOUCH_MODAL`) passes touches outside the toast through to the
 * content beneath and never takes input focus.
 */
@Composable
private fun ToastOverlayWindow(toastState: LemonadeToastState) {
    val toast = toastState.currentToast

    // Outlive `currentToast` clearing so the exit animation can play before the Dialog unmounts.
    var lastToast by remember { mutableStateOf<ToastData?>(null) }
    if (toast != null) lastToast = toast

    val animState = remember { MutableTransitionState(false) }
    val animationSettled by remember {
        derivedStateOf { !animState.currentState && !animState.targetState }
    }

    LaunchedEffect(toast, animationSettled) {
        if (toast == null && animationSettled) lastToast = null
    }

    val displayToast = lastToast ?: return

    Dialog(
        onDismissRequest = { toastState.dismiss() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        val layoutDirection = LocalLayoutDirection.current
        val margins = rememberToastPadding(displayToast.paddingValues, layoutDirection)
        val startInset = margins.calculateStartPadding(layoutDirection)
        val endInset = margins.calculateEndPadding(layoutDirection)
        ConfigureToastWindow(bottomInset = margins.calculateBottomPadding())
        // The window is centered; start/end insets only cap the width (the toast keeps clear of the
        // screen edges). Unlike the inline host they don't shift it horizontally — a bottom-centered
        // toast has no caller that needs asymmetric horizontal positioning.
        val maxToastWidth = (LocalConfiguration.current.screenWidthDp.dp - startInset - endInset)
            .coerceAtLeast(0.dp)

        LaunchedEffect(toast) {
            if (toast != null) {
                delay(SHOW_DELAY_MS)
                animState.targetState = true
            } else {
                animState.targetState = false
            }
        }

        AnimatedVisibility(
            visibleState = animState,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
        ) {
            Box(modifier = Modifier.widthIn(max = maxToastWidth)) {
                SwipeableToast(
                    toast = displayToast,
                    onDismiss = { toastState.dismiss() },
                )
            }
        }
    }
}

/**
 * Sizes the dialog window to the toast and lifts it [bottomInset] (plus the navigation-bar inset)
 * above the bottom via a window attribute, not padding — so the frame hugs the pill and taps
 * everywhere else fall through. The navigation-bar inset resolves to zero when the window already
 * sits above the bars and to the bar height on edge-to-edge screens, so the toast never lands under
 * the navigation bar.
 */
@Composable
private fun ConfigureToastWindow(bottomInset: Dp) {
    val view = LocalView.current
    val density = LocalDensity.current
    val navigationBarInsetPx = WindowInsets.navigationBars.getBottom(density)
    val bottomInsetPx = with(density) { bottomInset.roundToPx() } + navigationBarInsetPx
    DisposableEffect(bottomInsetPx) {
        (view.parent as? DialogWindowProvider)?.window?.apply {
            setBackgroundDrawable(ColorDrawable(AndroidColor.TRANSPARENT))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            )
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            attributes = attributes.apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                y = bottomInsetPx
            }
        }
        onDispose { }
    }
}
