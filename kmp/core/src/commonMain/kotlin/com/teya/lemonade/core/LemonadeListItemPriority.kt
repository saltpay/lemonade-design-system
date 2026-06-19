package com.teya.lemonade.core

/**
 * Defines which slot claims layout space first when the content (label) and trailing
 * content compete for the available width.
 *
 * - [Label]: the content slot keeps its full width; the trailing slot truncates to fit
 *   (down to a readable minimum width).
 * - [Trailing]: the trailing slot keeps its full width; the content slot truncates to
 *   fit. This is the default.
 */
public enum class LemonadeListItemPriority {
    Label,
    Trailing,
}
