package com.teya.lemonade.app

import androidx.compose.runtime.Composable
import com.teya.lemonade.BottomSheetDisplay
import com.teya.lemonade.BottomSheetSampleDisplay
import com.teya.lemonade.CollapsedTopBarDisplay
import com.teya.lemonade.CollapsedTopBarSampleDisplay
import com.teya.lemonade.CompactLargeSearchTopBarDisplay
import com.teya.lemonade.CompactLargeSearchTopBarSampleDisplay
import com.teya.lemonade.CompactLargeTopBarDisplay
import com.teya.lemonade.CompactLargeTopBarSampleDisplay
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
        BottomSheetDisplay to { _ -> BottomSheetSampleDisplay() },
        SearchTopBarDisplay to { _ -> SearchTopBarSampleDisplay() },
        TopBarDisplay to { _ -> TopBarSampleDisplay() },
        CollapsedTopBarDisplay to { _ -> CollapsedTopBarSampleDisplay() },
        CompactLargeTopBarDisplay to { _ -> CompactLargeTopBarSampleDisplay() },
        CompactLargeSearchTopBarDisplay to { _ -> CompactLargeSearchTopBarSampleDisplay() },
    )
