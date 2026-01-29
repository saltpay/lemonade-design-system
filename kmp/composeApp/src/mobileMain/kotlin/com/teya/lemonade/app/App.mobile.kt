package com.teya.lemonade.app

import androidx.compose.runtime.Composable
import com.teya.lemonade.Displays
import com.teya.lemonade.NavigationBarDisplay
import com.teya.lemonade.NavigationBarSampleDisplay

@Composable
internal actual fun App() {
    // TODO - make the mobile backstack and shenanigans
}

internal actual val platformScreens: Map<Displays, @Composable ((onNavigate: (Displays) -> Unit) -> Unit)> =
    mapOf(
        NavigationBarDisplay to { _ -> NavigationBarSampleDisplay() },
    )