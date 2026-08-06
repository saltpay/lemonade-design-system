package com.teya.lemonade

import androidx.compose.runtime.Composable

// Hooks for the Immersive Overlays sample screen. They live here rather than beside the screen in
// mobileMain because iosMain is a sibling of mobileMain, not a descendant, so it cannot actualise
// anything mobileMain declares.

/** Hides the host window's system bars while [enabled], and restores them when it flips back. */
@Composable
internal expect fun HideSystemBarsEffect(enabled: Boolean)

/** The Android-only `hideNavigationBar = true` [LemonadeUi.BottomSheet] overload. */
@Composable
internal expect fun ForcedHiddenNavBarBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
)
