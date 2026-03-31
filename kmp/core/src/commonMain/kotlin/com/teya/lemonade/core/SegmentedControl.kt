@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

public class TabButtonProperties private constructor(
    public val label: String?,
    public val icon: LemonadeIcons?,
) {
    public companion object {
        public fun label(
            label: String,
        ): TabButtonProperties {
            return TabButtonProperties(
                label = label,
                icon = null,
            )
        }

        public fun labelAndIcon(
            label: String,
            icon: LemonadeIcons,
        ): TabButtonProperties {
            return TabButtonProperties(
                label = label,
                icon = icon,
            )
        }

        public fun icon(
            icon: LemonadeIcons,
        ): TabButtonProperties {
            return TabButtonProperties(
                label = null,
                icon = icon,
            )
        }
    }
}

public enum class LemonadeSegmentedControlSize {
    Small,
    Medium,
    Large,
}
