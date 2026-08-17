package com.teya.lemonade

import androidx.compose.foundation.Indication

/**
 * Lemonade Effects setup
 */
public interface LemonadeEffects {
    public val interactionIndication: Indication?

    /**
     * Controls whether Lemonade components animate. Declared with a default body so
     * implementations compiled against earlier releases keep linking; override it through
     * delegation to change only this slot:
     *
     * ```kotlin
     * object : LemonadeEffects by LemonadeTheme.effects {
     *     override val animations: LemonadeAnimationMode = LemonadeAnimationMode.None
     * }
     * ```
     */
    public val animations: LemonadeAnimationMode
        get() = LemonadeAnimationMode.System
}
