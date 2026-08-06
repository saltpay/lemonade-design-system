package com.teya.lemonade

import androidx.compose.runtime.Composable

@Composable
internal actual fun HideSystemBarsEffect(enabled: Boolean) {
}

@Composable
internal actual fun ForcedHiddenNavBarBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
}
