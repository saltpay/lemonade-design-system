package com.teya.lemonade.core

import kotlinx.serialization.Serializable

/**
 * Represents a text style with typographic properties.
 *
 * @property fontSize The font size in sp (scale-independent pixels)
 * @property lineHeight The line height in sp
 * @property fontWeight The font weight (400 = Normal, 500 = Medium, 600 = SemiBold, 700 = Bold)
 * @property letterSpacing The letter spacing in sp, null if default
 */
@Serializable
public data class LemonadeTextStyle(
    val fontSize: Float,
    val lineHeight: Float,
    val fontWeight: Int,
    val letterSpacing: Float? = null
)

/**
 * Interface defining all available text styles in the Lemonade Design System.
 */
public abstract class LemonadeTypography {
    // Display styles
    public abstract val displayXSmall: LemonadeTextStyle
    public abstract val displaySmall: LemonadeTextStyle
    public abstract val displayMedium: LemonadeTextStyle
    public abstract val displayLarge: LemonadeTextStyle

    // Heading styles
    public abstract val headingXLarge: LemonadeTextStyle
    public abstract val headingLarge: LemonadeTextStyle
    public abstract val headingMedium: LemonadeTextStyle
    public abstract val headingSmall: LemonadeTextStyle
    public abstract val headingXSmall: LemonadeTextStyle
    public abstract val headingXXSmall: LemonadeTextStyle

    // Body XLarge styles
    public abstract val bodyXLargeRegular: LemonadeTextStyle
    public abstract val bodyXLargeMedium: LemonadeTextStyle
    public abstract val bodyXLargeSemiBold: LemonadeTextStyle

    // Body Large styles
    public abstract val bodyLargeRegular: LemonadeTextStyle
    public abstract val bodyLargeMedium: LemonadeTextStyle
    public abstract val bodyLargeSemiBold: LemonadeTextStyle

    // Body Medium styles
    public abstract val bodyMediumRegular: LemonadeTextStyle
    public abstract val bodyMediumMedium: LemonadeTextStyle
    public abstract val bodyMediumSemiBold: LemonadeTextStyle
    public abstract val bodyMediumBold: LemonadeTextStyle

    // Body Small styles
    public abstract val bodySmallRegular: LemonadeTextStyle
    public abstract val bodySmallMedium: LemonadeTextStyle
    public abstract val bodySmallSemiBold: LemonadeTextStyle

    // Body XSmall styles
    public abstract val bodyXSmallRegular: LemonadeTextStyle
    public abstract val bodyXSmallMedium: LemonadeTextStyle
    public abstract val bodyXSmallSemiBold: LemonadeTextStyle
    public abstract val bodyXSmallOverline: LemonadeTextStyle
}