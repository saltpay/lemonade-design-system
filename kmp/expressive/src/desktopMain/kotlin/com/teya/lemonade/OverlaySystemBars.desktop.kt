package com.teya.lemonade

import androidx.compose.runtime.Composable

@Composable
internal actual fun hostHiddenSystemBars(): HiddenSystemBars = HiddenSystemBars()

@Composable
internal actual fun MirrorHiddenSystemBars(hidden: HiddenSystemBars) {
}
