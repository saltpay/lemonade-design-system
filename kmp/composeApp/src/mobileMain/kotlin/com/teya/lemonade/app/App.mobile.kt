package com.teya.lemonade.app

import androidx.compose.runtime.Composable
import com.teya.lemonade.DialogDisplay
import com.teya.lemonade.DialogSampleDisplay
import com.teya.lemonade.Displays
import com.teya.lemonade.DropdownDisplay
import com.teya.lemonade.DropdownSampleDisplay
import com.teya.lemonade.SearchTopBarDisplay
import com.teya.lemonade.SearchTopBarSampleDisplay
import com.teya.lemonade.TopBarDisplay
import com.teya.lemonade.TopBarSampleDisplay

@Composable
internal actual fun App() {
    // TODO - make the mobile backstack and shenanigans
}

internal actual val platformScreens: Map<Displays, @Composable ((onNavigate: (Displays) -> Unit) -> Unit)> =
    mapOf(
        DropdownDisplay to { _ -> DropdownSampleDisplay() },
        DialogDisplay to { _ -> DialogSampleDisplay() },
        SearchTopBarDisplay to { _ -> SearchTopBarSampleDisplay() },
        TopBarDisplay to { _ -> TopBarSampleDisplay() },
    )
