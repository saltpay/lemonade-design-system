package com.teya.lemonade

import kotlinx.serialization.Serializable

@Serializable
internal data object TopBarDisplay : Displays {
    override val label: String = "TopBar"
}

internal actual val platformSpecificEntries: List<Displays> = listOf(
    TopBarDisplay,
)
