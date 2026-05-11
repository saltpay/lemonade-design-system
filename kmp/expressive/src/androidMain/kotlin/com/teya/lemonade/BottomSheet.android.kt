package com.teya.lemonade

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.teya.lemonade.core.LemonadeBottomSheetVariant

/**
 * Android-specific [BottomSheet][LemonadeUi.BottomSheet] variant that can hide the system
 * navigation bar (back / home / recent buttons) inside the sheet's dialog window.
 *
 * When [hideNavigationBar] is `true`, the dialog window is configured edge-to-edge and
 * the navigation bar is hidden using [WindowInsetsControllerCompat]. The sheet's content
 * window insets are adjusted to [WindowInsets] status bar so that the
 * [ModalBottomSheet] does not miscalculate its offset from the now-hidden navigation bar.
 *
 * When [hideNavigationBar] is `false`, this behaves identically to the common
 * [LemonadeUi.BottomSheet].
 *
 * @param expanded Whether the bottom sheet is currently visible.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the bottom sheet.
 * @param hideNavigationBar Whether to hide the system navigation bar in the dialog window.
 * @param showDragHandle Whether to display the drag handle at the top of the sheet.
 * @param skipPartiallyExpanded Whether the partially expanded state should be skipped.
 * @param background The background variant of the bottom sheet. Defaults to
 *   [LemonadeBottomSheetVariant.Default].
 * @param content A composable lambda with [ColumnScope] receiver that defines the sheet's content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LemonadeUi.BottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    hideNavigationBar: Boolean,
    showDragHandle: Boolean = true,
    skipPartiallyExpanded: Boolean = false,
    background: LemonadeBottomSheetVariant = LemonadeBottomSheetVariant.Default,
    content: @Composable ColumnScope.() -> Unit,
) {
    CoreBottomSheet(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        showDragHandle = showDragHandle,
        skipPartiallyExpanded = skipPartiallyExpanded,
        background = background,
        contentWindowInsets = if (hideNavigationBar) {
            { WindowInsets.statusBars }
        } else {
            { BottomSheetDefaults.windowInsets }
        },
        content = {
            if (hideNavigationBar) {
                HideNavigationBarEffect()
            }
            content()
        },
    )
}

/**
 * Configures the dialog window created by [ModalBottomSheet] to be edge-to-edge and
 * hides its navigation bar.
 *
 * [ModalBottomSheet] renders inside its own dialog window, so the dialog window must be
 * obtained via [DialogWindowProvider] and configured independently from the Activity window.
 * [DisposableEffect] with key [Unit] runs once when the dialog enters composition, which
 * is early enough — layout happens after composition.
 */
@Composable
private fun HideNavigationBarEffect() {
    val view = LocalView.current
    DisposableEffect(Unit) {
        val dialogWindow = (view.parent as? DialogWindowProvider)
            ?.window
            ?: return@DisposableEffect onDispose { /* Nothing */ }

        val insetsController = WindowCompat.getInsetsController(
            dialogWindow,
            dialogWindow.decorView,
        )
        WindowCompat.setDecorFitsSystemWindows(
            dialogWindow,
            false,
        )
        with(insetsController) {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        onDispose {
            with(insetsController) {
                show(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
}
