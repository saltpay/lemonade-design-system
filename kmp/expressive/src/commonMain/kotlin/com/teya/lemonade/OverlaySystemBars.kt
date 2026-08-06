package com.teya.lemonade

import androidx.compose.runtime.Composable

/** The mirror returned by platforms that have no per-window system bars. */
internal val NoOpSystemBarsMirror: @Composable () -> Unit = {}

/**
 * Captures the system bars the host window hides, and returns a composable that mirrors them onto
 * an overlay's own window. Bars are only ever mirrored hidden, never shown.
 *
 * Overlays draw into a window of their own on Android, and a new window starts out asking for every
 * system bar. Call this from the composition hosting the overlay, then invoke the result as the
 * first thing inside the overlay's content.
 *
 * Below API 30 this is inert — measured on an API 23 emulator, mirroring changes nothing. There the
 * platform drops the host's hide flags on every window focus change, so an immersive host has to
 * re-assert them from `onWindowFocusChanged`, and a brief flash as an overlay is dismissed is not
 * something the overlay can prevent.
 *
 * @param forceHideNavigationBar Hide the navigation bar even when the host window shows it.
 */
@Composable
internal expect fun systemBarsMirror(forceHideNavigationBar: Boolean = false): @Composable () -> Unit
