@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

public enum class LemonadeBoxSelectionVariant {
    Filled,
    Outlined,
}

/**
 * Surface behind a [LemonadeBoxSelectionVariant.Filled] box selection.
 * Ignored by [LemonadeBoxSelectionVariant.Outlined], which has no fill.
 */
public enum class LemonadeBoxSelectionBackground {
    Default,
    Elevated,
}
