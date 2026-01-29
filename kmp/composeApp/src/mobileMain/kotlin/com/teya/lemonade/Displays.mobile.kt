package com.teya.lemonade

import kotlinx.serialization.Serializable

@Serializable
internal data object NavigationBarDisplay : Displays {
    override val label: String = "NavigationBar"
}

@Serializable
internal data object SearchNavigationBarDisplay : Displays {
    override val label: String = "SearchNavigationBar"
}

internal actual val platformSpecificEntries: List<Displays> = listOf(
    NavigationBarDisplay,
    SearchNavigationBarDisplay,
)
