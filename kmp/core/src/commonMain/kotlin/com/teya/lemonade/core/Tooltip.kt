@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

/**
 * Where the tooltip draws its indicator — the small arrow that points back at the element the
 * tooltip describes.
 *
 * Placement is physical, not directional: [TopLeft] anchors the indicator to the left edge in both
 * LTR and RTL layouts, so a caller that positions the tooltip by absolute coordinates does not have
 * to compensate for the layout direction.
 */
public enum class TooltipIndicatorPlacement {
    /** No indicator. The tooltip is a plain floating container. */
    None,

    TopLeft,
    TopCenter,
    TopRight,
    BottomLeft,
    BottomCenter,
    BottomRight,
}

/**
 * Emphasis of a tooltip footer action.
 */
public enum class TooltipFooterActionVariant {
    /** Filled action. Use for the single most important action in the footer. */
    Primary,

    /** Outlined action. Use for secondary or dismissive actions. */
    Secondary,
}
