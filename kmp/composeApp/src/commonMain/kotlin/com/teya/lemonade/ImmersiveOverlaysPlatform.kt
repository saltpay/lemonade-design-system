package com.teya.lemonade

import androidx.compose.runtime.Composable

// Platform hooks for the Immersive Overlays sample screen. They live here rather than next to the
// screen in mobileMain because iosMain is a sibling of mobileMain over the same iOS targets, not a
// descendant of it, so it cannot actualise anything mobileMain declares.

/**
 * Hides the host window's system bars while [enabled], and restores them when it flips back or
 * when this composable leaves composition. Only Android has per-window system-bar state.
 */
@Composable
internal expect fun HideSystemBarsEffect(enabled: Boolean)

/**
 * The Android-only `hideNavigationBar = true` [LemonadeUi.BottomSheet] overload, so the forced
 * override can be exercised from the shared sample screen. A no-op on every other target.
 */
@Composable
internal expect fun ForcedHiddenNavBarBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
)
