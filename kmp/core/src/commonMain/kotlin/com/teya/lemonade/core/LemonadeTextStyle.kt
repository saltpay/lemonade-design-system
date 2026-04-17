package com.teya.lemonade.core

/**
 * Represents a text style with typographic properties.
 *
 * @property fontSize The font size in sp (scale-independent pixels)
 * @property lineHeight The line height in sp
 * @property fontWeight The font weight (400 = Normal, 500 = Medium, 600 = SemiBold, 700 = Bold)
 * @property letterSpacing The letter spacing in sp, null if default
 */
public data class LemonadeTextStyle(
    val fontSize: Float,
    val lineHeight: Float,
    val fontWeight: Int,
    val letterSpacing: Float? = null,
)

/**
 * Enum defining all available text styles in the Lemonade Design System.
 * Each case carries its default [LemonadeTextStyle]. Exported via SKIE as a
 * `@frozen` Swift enum for easy iOS mapping.
 */
public enum class LemonadeTypography(public val style: LemonadeTextStyle) {
    // Display styles
    DisplayXSmall(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize600.value, lineHeight = LemonadeLineHeights.LineHeight800.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),
    DisplaySmall(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize700.value, lineHeight = LemonadeLineHeights.LineHeight900.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),
    DisplayMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize900.value, lineHeight = LemonadeLineHeights.LineHeight1100.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),
    DisplayLarge(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize1200.value, lineHeight = LemonadeLineHeights.LineHeight1400.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),
    DisplayXLarge(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize1400.value, lineHeight = LemonadeLineHeights.LineHeight1600.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),
    Display2XLarge(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize1600.value, lineHeight = LemonadeLineHeights.LineHeight1800.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),
    Display3XLarge(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize1800.value, lineHeight = LemonadeLineHeights.LineHeight2000.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = -0.25f)),

    // Heading styles
    HeadingXLarge(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize1000.value, lineHeight = LemonadeLineHeights.LineHeight1200.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    HeadingLarge(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize800.value, lineHeight = LemonadeLineHeights.LineHeight1000.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    HeadingMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize700.value, lineHeight = LemonadeLineHeights.LineHeight900.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    HeadingSmall(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize600.value, lineHeight = LemonadeLineHeights.LineHeight800.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    HeadingXSmall(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize450.value, lineHeight = LemonadeLineHeights.LineHeight650.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    HeadingXXSmall(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize400.value, lineHeight = LemonadeLineHeights.LineHeight600.value, fontWeight = LemonadeFontWeights.Semibold.weight)),

    // Body XLarge styles
    BodyXLargeRegular(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize500.value, lineHeight = LemonadeLineHeights.LineHeight700.value, fontWeight = LemonadeFontWeights.Regular.weight)),
    BodyXLargeMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize500.value, lineHeight = LemonadeLineHeights.LineHeight700.value, fontWeight = LemonadeFontWeights.Medium.weight)),
    BodyXLargeSemiBold(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize500.value, lineHeight = LemonadeLineHeights.LineHeight700.value, fontWeight = LemonadeFontWeights.Semibold.weight)),

    // Body Large styles
    BodyLargeRegular(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize450.value, lineHeight = LemonadeLineHeights.LineHeight700.value, fontWeight = LemonadeFontWeights.Regular.weight)),
    BodyLargeMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize450.value, lineHeight = LemonadeLineHeights.LineHeight700.value, fontWeight = LemonadeFontWeights.Medium.weight)),
    BodyLargeSemiBold(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize450.value, lineHeight = LemonadeLineHeights.LineHeight700.value, fontWeight = LemonadeFontWeights.Semibold.weight)),

    // Body Medium styles
    BodyMediumRegular(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize400.value, lineHeight = LemonadeLineHeights.LineHeight600.value, fontWeight = LemonadeFontWeights.Regular.weight)),
    BodyMediumMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize400.value, lineHeight = LemonadeLineHeights.LineHeight600.value, fontWeight = LemonadeFontWeights.Medium.weight)),
    BodyMediumSemiBold(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize400.value, lineHeight = LemonadeLineHeights.LineHeight600.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    BodyMediumBold(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize400.value, lineHeight = LemonadeLineHeights.LineHeight600.value, fontWeight = LemonadeFontWeights.Bold.weight)),

    // Body Small styles
    BodySmallRegular(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize350.value, lineHeight = LemonadeLineHeights.LineHeight500.value, fontWeight = LemonadeFontWeights.Regular.weight)),
    BodySmallMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize350.value, lineHeight = LemonadeLineHeights.LineHeight500.value, fontWeight = LemonadeFontWeights.Medium.weight)),
    BodySmallSemiBold(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize350.value, lineHeight = LemonadeLineHeights.LineHeight500.value, fontWeight = LemonadeFontWeights.Semibold.weight)),

    // Body XSmall styles
    BodyXSmallRegular(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize300.value, lineHeight = LemonadeLineHeights.LineHeight400.value, fontWeight = LemonadeFontWeights.Regular.weight)),
    BodyXSmallMedium(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize300.value, lineHeight = LemonadeLineHeights.LineHeight400.value, fontWeight = LemonadeFontWeights.Medium.weight)),
    BodyXSmallSemiBold(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize300.value, lineHeight = LemonadeLineHeights.LineHeight400.value, fontWeight = LemonadeFontWeights.Semibold.weight)),
    BodyXSmallOverline(LemonadeTextStyle(fontSize = LemonadeFontSizes.FontSize300.value, lineHeight = LemonadeLineHeights.LineHeight400.value, fontWeight = LemonadeFontWeights.Semibold.weight, letterSpacing = 1.5f)),
}
