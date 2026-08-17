package com.teya.lemonade

/**
 * Controls whether Lemonade components animate.
 *
 * Disabling animations removes their runtime cost entirely — continuous animations such as the
 * skeleton shimmer are never started, transitions apply their final state in a single frame, and
 * programmatic scrolls jump directly to their target — which improves frame times and battery
 * life on low-end hardware.
 */
public enum class LemonadeAnimationMode {
    /** All animations run normally. */
    Full,

    /**
     * Follows the platform preference: the animator duration scale on Android and Reduce Motion
     * on iOS. Desktop has no equivalent preference and behaves like [Full]. The preference is
     * read once when the theme first composes.
     */
    System,

    /** All animations are disabled. */
    None,
}
