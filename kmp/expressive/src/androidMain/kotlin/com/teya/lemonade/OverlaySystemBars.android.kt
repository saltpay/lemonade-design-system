package com.teya.lemonade

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets as AndroidWindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.areStatusBarsVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal actual fun hostHiddenSystemBars(): HiddenSystemBars =
    HiddenSystemBars(
        statusBar = !WindowInsets.areStatusBarsVisible,
        navigationBar = !WindowInsets.areNavigationBarsVisible,
    )

@Composable
internal actual fun MirrorHiddenSystemBars(hidden: HiddenSystemBars) {
    val view = LocalView.current
    // Keyed on Unit rather than on `hidden`: the host state is captured once, when the overlay
    // opens, and held for its lifetime. A transient swipe-to-reveal underneath therefore cannot
    // make an already-open overlay bring its bars back halfway through.
    DisposableEffect(Unit) {
        if (hidden.any) {
            view.hideOverlaySystemBars(hidden)
        }
        // The controller targets the overlay's own window, which is about to be destroyed, so
        // there is nothing to restore. Showing the bars here animates them back in over the dying
        // window, which is the flicker #230 removed.
        onDispose { }
    }
}

private fun View.hideOverlaySystemBars(hidden: HiddenSystemBars) {
    val dialogWindow = (parent as? DialogWindowProvider)?.window
    if (dialogWindow != null) {
        dialogWindow.hideSystemBars(hidden)
    } else {
        hidePopupSystemBars(hidden)
    }
}

/**
 * [WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE] leaves this window out of the system-bar policy,
 * so the insets request lands before the window ever gets to decide what the bars do. Clearing the
 * flag straight afterwards restores input, by which point the window already asks for the bars to
 * stay hidden.
 */
private fun Window.hideSystemBars(hidden: HiddenSystemBars) {
    val wasFocusable =
        (attributes.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) == 0
    setFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    )
    WindowCompat.setDecorFitsSystemWindows(this, false)
    WindowCompat.getInsetsController(this, decorView).hideBars(hidden)
    if (wasFocusable) {
        clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
}

/**
 * A Compose `Popup` — what [LemonadeUi.Dropdown] draws into — is a bare view added straight to the
 * [WindowManager], with no [Window] to hang a controller off.
 */
private fun View.hidePopupSystemBars(hidden: HiddenSystemBars) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = windowInsetsController ?: return
        controller.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(hidden.insetTypes())
    } else {
        @Suppress("DEPRECATION")
        rootView.systemUiVisibility = rootView.systemUiVisibility or hidden.legacySystemUiFlags()
    }
}

private fun WindowInsetsControllerCompat.hideBars(hidden: HiddenSystemBars) {
    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    if (hidden.statusBar) {
        hide(WindowInsetsCompat.Type.statusBars())
    }
    if (hidden.navigationBar) {
        hide(WindowInsetsCompat.Type.navigationBars())
    }
}

private fun HiddenSystemBars.insetTypes(): Int {
    var types = 0
    if (statusBar) {
        types = types or AndroidWindowInsets.Type.statusBars()
    }
    if (navigationBar) {
        types = types or AndroidWindowInsets.Type.navigationBars()
    }
    return types
}

@Suppress("DEPRECATION")
private fun HiddenSystemBars.legacySystemUiFlags(): Int {
    var flags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (statusBar) {
        flags = flags or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (navigationBar) {
        flags = flags or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
    return flags
}
