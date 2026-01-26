package com.teya.lemonade

import kotlinx.serialization.Serializable

@Serializable
internal data object NavigationBarDisplay : Displays {
    override val label: String = "NavigationBar"
}

internal actual val platformSpecificEntries: List<Displays> = listOf(
    NavigationBarDisplay,
)
