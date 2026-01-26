package com.teya.lemonade.app

import androidx.compose.runtime.Composable
import com.teya.lemonade.Displays

@Composable
internal actual fun App() {
    // TODO - make the mobile backstack and shenanigans
}

internal actual val platformScreens: Map<Displays, @Composable ((onNavigate: (Displays) -> Unit) -> Unit)> =
    emptyMap()