@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

public enum class LemonadeTileVariant {
    Filled,
    Outlined,

    @Deprecated(
        message = "Use Filled instead",
        replaceWith = ReplaceWith(expression = "LemonadeTileVariant.Filled"),
    )
    Neutral,

    @Deprecated(
        message = "Use Outlined instead",
        replaceWith = ReplaceWith(expression = "LemonadeTileVariant.Outlined"),
    )
    Muted,

    @Deprecated(
        message = "Use Filled instead. OnColor variant has been removed.",
        replaceWith = ReplaceWith(expression = "LemonadeTileVariant.Filled"),
    )
    OnColor,

    @Deprecated(
        message = "Use Filled with isSelected = true instead",
        replaceWith = ReplaceWith(expression = "LemonadeTileVariant.Filled"),
    )
    Selected,
}
