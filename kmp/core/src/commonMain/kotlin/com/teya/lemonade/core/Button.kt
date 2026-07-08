package com.teya.lemonade.core

public enum class LemonadeButtonVariant {
    Primary,
    Secondary,
    Neutral,
    Critical,

    /** For use on top of brand-filled surfaces. Rendered as a single Subtle treatment. */
    OnBrand,

    /** For use on top of color-filled (voice) surfaces. Rendered as a single Subtle treatment. */
    OnColor,
}

public enum class LemonadeButtonType {
    Solid,
    Subtle,
    Ghost,
}

public enum class LemonadeButtonSize {
    XSmall,
    Small,
    Medium,
    Large,
}
