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
 * Enum defining all available text styles in the Lemonade Design System.
 * Each case carries its default [LemonadeTextStyle]. Exported via SKIE as a
 * `@frozen` Swift enum for easy iOS mapping.
 */
public enum class LemonadeTypography(public val style: LemonadeTextStyle) {
    // Display styles
    DisplayXSmall(LemonadeTextStyle(fontSize = 24f, lineHeight = 32f, fontWeight = 600)),
    DisplaySmall(LemonadeTextStyle(fontSize = 28f, lineHeight = 36f, fontWeight = 600)),
    DisplayMedium(LemonadeTextStyle(fontSize = 36f, lineHeight = 44f, fontWeight = 600)),
    DisplayLarge(LemonadeTextStyle(fontSize = 48f, lineHeight = 56f, fontWeight = 600)),

    // Heading styles
    HeadingXLarge(LemonadeTextStyle(fontSize = 40f, lineHeight = 48f, fontWeight = 600)),
    HeadingLarge(LemonadeTextStyle(fontSize = 32f, lineHeight = 40f, fontWeight = 600)),
    HeadingMedium(LemonadeTextStyle(fontSize = 28f, lineHeight = 36f, fontWeight = 600)),
    HeadingSmall(LemonadeTextStyle(fontSize = 24f, lineHeight = 32f, fontWeight = 600)),
    HeadingXSmall(LemonadeTextStyle(fontSize = 18f, lineHeight = 26f, fontWeight = 600)),
    HeadingXXSmall(LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 600)),

    // Body XLarge styles
    BodyXLargeRegular(LemonadeTextStyle(fontSize = 20f, lineHeight = 28f, fontWeight = 400)),
    BodyXLargeMedium(LemonadeTextStyle(fontSize = 20f, lineHeight = 28f, fontWeight = 500)),
    BodyXLargeSemiBold(LemonadeTextStyle(fontSize = 20f, lineHeight = 28f, fontWeight = 600)),

    // Body Large styles
    BodyLargeRegular(LemonadeTextStyle(fontSize = 18f, lineHeight = 28f, fontWeight = 400)),
    BodyLargeMedium(LemonadeTextStyle(fontSize = 18f, lineHeight = 28f, fontWeight = 500)),
    BodyLargeSemiBold(LemonadeTextStyle(fontSize = 18f, lineHeight = 28f, fontWeight = 600)),

    // Body Medium styles
    BodyMediumRegular(LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 400)),
    BodyMediumMedium(LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 500)),
    BodyMediumSemiBold(LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 600)),
    BodyMediumBold(LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 700)),

    // Body Small styles
    BodySmallRegular(LemonadeTextStyle(fontSize = 14f, lineHeight = 20f, fontWeight = 400)),
    BodySmallMedium(LemonadeTextStyle(fontSize = 14f, lineHeight = 20f, fontWeight = 500)),
    BodySmallSemiBold(LemonadeTextStyle(fontSize = 14f, lineHeight = 20f, fontWeight = 600)),

    // Body XSmall styles
    BodyXSmallRegular(LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 400)),
    BodyXSmallMedium(LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 500)),
    BodyXSmallSemiBold(LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 600)),
    BodyXSmallOverline(LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 600, letterSpacing = 1.5f)),
}