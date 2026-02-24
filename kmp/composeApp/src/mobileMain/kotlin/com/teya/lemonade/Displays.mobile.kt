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
internal data object BottomSheetDisplay : Displays {
    override val label: String = "BottomSheet"
}

internal actual val platformSpecificEntries: List<Displays> = listOf(
    TopBarDisplay,
    SearchTopBarDisplay,
    DropdownDisplay,
    DialogDisplay,
    BottomSheetDisplay,
)
