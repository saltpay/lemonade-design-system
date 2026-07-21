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
 * What the tooltip host draws behind a tooltip while it is showing.
 *
 * Regardless of the mode, the host swallows taps outside the tooltip so that an outside tap can
 * dismiss it — the UI underneath is not interactive while a tooltip is up.
 */
public enum class TooltipScrim {
    /** Nothing is drawn behind the tooltip. The default for on-demand help. */
    None,

    /** A translucent scrim covers the whole host. */
    Dim,

    /** As [Dim], but punched through around the anchor so the described element stays lit. */
    Spotlight,
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
