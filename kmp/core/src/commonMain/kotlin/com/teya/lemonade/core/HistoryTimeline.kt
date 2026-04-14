package com.teya.lemonade.core

/**
 * Semantic voice for a History Timeline component, determining the color of its indicator dot.
 *
 * - [Positive] — indicates a successful or completed step (e.g. payment received, order shipped).
 * - [Critical] — indicates a failure or issue requiring attention (e.g. payment failed, delivery error).
 * - [Neutral] — indicates a pending or informational step with no positive/negative connotation.
 */
public enum class HistoryTimelineItemVoice {
    Positive,
    Critical,
    Neutral,
}
