package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Which system bars a window keeps hidden.
 *
 * [LemonadeUi.BottomSheet], [LemonadeUi.Dialog] and [LemonadeUi.Dropdown] each draw into a window
 * of their own on Android. A new window starts out asking for every system bar, so an app running
 * fully immersive would see the bars come back the moment an overlay opens. Capturing the host
 * window's state and mirroring it onto the overlay window keeps them hidden.
 */
@Immutable
internal data class HiddenSystemBars(
    val statusBar: Boolean = false,
    val navigationBar: Boolean = false,
) {
    val any: Boolean
        get() = statusBar || navigationBar

    operator fun plus(other: HiddenSystemBars): HiddenSystemBars =
        HiddenSystemBars(
            statusBar = statusBar || other.statusBar,
            navigationBar = navigationBar || other.navigationBar,
        )
}

/**
 * The system bars the *host* window currently hides.
 *
 * Call this from the composition that hosts the overlay — outside the overlay itself — so it reads
 * the host window rather than the overlay's own. Returns nothing hidden on platforms that have no
 * per-window system bars.
 */
@Composable
internal expect fun hostHiddenSystemBars(): HiddenSystemBars

/**
 * Mirrors [hidden] onto the overlay window this is composed into, so the overlay keeps the same
 * bars hidden as its host. Bars are only ever hidden, never shown.
 *
 * Call this as the first thing inside the overlay's content, where [androidx.compose.ui.platform.LocalView]
 * resolves to the overlay's own window.
 */
@Composable
internal expect fun MirrorHiddenSystemBars(hidden: HiddenSystemBars)
