@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

public data class TabButtonProperties(
    val label: String? = null,
    val icon: LemonadeIcons? = null,
)

public enum class LemonadeSegmentedControlSize {
    Small,
    Medium,
    Large,
}
