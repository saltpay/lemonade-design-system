package com.teya.lemonade

import androidx.compose.runtime.Composable

@Composable
internal actual fun HideSystemBarsEffect(enabled: Boolean) = Unit

@Composable
internal actual fun ForcedHiddenNavBarBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) = Unit
