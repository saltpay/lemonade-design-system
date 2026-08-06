package com.teya.lemonade

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
internal actual fun HideSystemBarsEffect(enabled: Boolean) {
    val view = LocalView.current
    DisposableEffect(enabled) {
        val window = view.context
            .findActivity()
            ?.window
        if (window == null || !enabled) {
            return@DisposableEffect onDispose { }
        }

        val controller = WindowCompat.getInsetsController(window, view)
        val previousBehavior = controller.systemBarsBehavior
        hideSystemBars(controller = controller)

        // Below API 30 the platform drops the hide flags whenever the window loses focus, so an
        // overlay opening and closing would otherwise leave the bars up for good. Re-asserting on
        // focus is what an immersive host is expected to do there.
        val onWindowFocus = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
            if (hasFocus) {
                hideSystemBars(controller = controller)
            }
        }
        view.viewTreeObserver.addOnWindowFocusChangeListener(onWindowFocus)

        onDispose {
            view.viewTreeObserver.removeOnWindowFocusChangeListener(onWindowFocus)
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = previousBehavior
        }
    }
}

@Composable
internal actual fun ForcedHiddenNavBarBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    LemonadeUi.BottomSheet(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        hideNavigationBar = true,
    ) {
        ImmersiveOverlayContent(
            title = "hideNavigationBar = true",
            body = "This sheet forces the navigation bar hidden even when the host window shows it.",
            onClose = onDismissRequest,
        )
    }
}

private fun hideSystemBars(controller: WindowInsetsControllerCompat) {
    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    controller.hide(WindowInsetsCompat.Type.systemBars())
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
