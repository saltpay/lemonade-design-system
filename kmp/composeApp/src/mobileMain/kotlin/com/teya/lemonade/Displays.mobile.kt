package com.teya.lemonade

import kotlinx.serialization.Serializable

@Serializable
internal data object TopBarDisplay : Displays {
    override val label: String = "TopBar"
}

@Serializable
internal data object SearchTopBarDisplay : Displays {
    override val label: String = "SearchTopBar"
}

@Serializable
internal data object DropdownDisplay : Displays {
    override val label: String = "Dropdown"
}

@Serializable
internal data object DialogDisplay : Displays {
    override val label: String = "Dialog"
}

@Serializable
internal data object CollapsedTopBarDisplay : Displays {
    override val label: String = "CollapsedTopBar"
}

@Serializable
internal data object CompactLargeTopBarDisplay : Displays {
    override val label: String = "CompactLargeTopBar"
}

@Serializable
internal data object CompactLargeSearchTopBarDisplay : Displays {
    override val label: String = "CompactLargeSearchTopBar"
}

@Serializable
internal data object BottomSheetDisplay : Displays {
    override val label: String = "BottomSheet"
}

internal actual val platformSpecificEntries: List<Displays> = listOf(
    TopBarDisplay,
    SearchTopBarDisplay,
    CollapsedTopBarDisplay,
    CompactLargeTopBarDisplay,
    CompactLargeSearchTopBarDisplay,
    DropdownDisplay,
    DialogDisplay,
    BottomSheetDisplay,
)
