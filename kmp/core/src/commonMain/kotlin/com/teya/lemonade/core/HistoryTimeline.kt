@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

/**
 * Defines the visual voice of a HistoryItem indicator dot.
 *
 * [Neutral] renders a dark dot for the current step and a muted dot for past steps.
 * [Positive] renders a green dot, used to highlight success or completed states.
 * [Critical] renders a red dot, used to highlight failure or error states.
 */
public enum class HistoryItemVoice {
    Neutral,
    Positive,
    Critical,
}
