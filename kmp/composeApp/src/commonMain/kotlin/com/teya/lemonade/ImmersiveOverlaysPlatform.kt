package com.teya.lemonade

import androidx.compose.runtime.Composable

// Hooks for the Immersive Overlays sample screen. Here rather than beside it in mobileMain because
// iosMain is a sibling of mobileMain, not a descendant, so it cannot actualise what mobileMain declares.

@Composable
internal expect fun HideSystemBarsEffect(enabled: Boolean)

@Composable
internal expect fun ForcedHiddenNavBarBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
)
