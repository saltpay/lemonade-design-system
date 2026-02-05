package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.teya.lemonade.core.LemonadeTextStyle
import com.teya.lemonade.core.LemonadeTypography
import org.jetbrains.compose.resources.Font

/**
 * This font family is based on the Figtree font, which is a modern sans-serif typeface.
 * It is used throughout the app for various text styles, providing a clean and readable
 * typography that aligns with the Lemonade design system.
 *
 *
 * See [Lemonade  typography](https://www.figma.com/design/mmSKfenwtw1xujWwXvs9wJ/Lemonade-DS---Foundations?node-id=207-17406&t=OWFace9zKbJJOo1E-4)
 *
 * See [Figtree font](https://fonts.google.com/specimen/Figtree)
 */
public val lemonadeFontFamily: FontFamily
    @Composable get() {
        return FontFamily(
            Font(LemonadeRes.font.Figtree_Regular, FontWeight.Normal),
            Font(LemonadeRes.font.Figtree_Medium, FontWeight.Medium),
            Font(LemonadeRes.font.Figtree_SemiBold, FontWeight.SemiBold)
        )
    }

/**
 * Converts a [LemonadeTextStyle] to a Compose [TextStyle].
 */
public val LemonadeTextStyle.textStyle: TextStyle
    @Composable get() {
        val spacing = letterSpacing
            ?: 0f
        return TextStyle(
            fontFamily = lemonadeFontFamily,
            fontWeight = when (fontWeight) {
                400 -> FontWeight.Normal
                500 -> FontWeight.Medium
                600 -> FontWeight.SemiBold
                700 -> FontWeight.Bold
                else -> FontWeight.Normal
            },
            fontSize = fontSize.sp,
            lineHeight = lineHeight.sp,
            letterSpacing = spacing.sp,
            fontFeatureSettings = "psum"
        )
    }

/**
 * Convenience extension to convert a [LemonadeTypography] enum value directly to a Compose [TextStyle].
 */
public val LemonadeTypography.textStyle: TextStyle
    @Composable get() = style.textStyle

/**
 * Provides typography styles for the Lemonade theme.
 * Subclass to override specific styles while keeping defaults from [LemonadeTypography] enum.
 */
public open class LemonadeTypographyProvider {
    // Display styles
    public open val displayXSmall: LemonadeTextStyle get() = LemonadeTypography.DisplayXSmall.style
    public open val displaySmall: LemonadeTextStyle get() = LemonadeTypography.DisplaySmall.style
    public open val displayMedium: LemonadeTextStyle get() = LemonadeTypography.DisplayMedium.style
    public open val displayLarge: LemonadeTextStyle get() = LemonadeTypography.DisplayLarge.style

    // Heading styles
    public open val headingXLarge: LemonadeTextStyle get() = LemonadeTypography.HeadingXLarge.style
    public open val headingLarge: LemonadeTextStyle get() = LemonadeTypography.HeadingLarge.style
    public open val headingMedium: LemonadeTextStyle get() = LemonadeTypography.HeadingMedium.style
    public open val headingSmall: LemonadeTextStyle get() = LemonadeTypography.HeadingSmall.style
    public open val headingXSmall: LemonadeTextStyle get() = LemonadeTypography.HeadingXSmall.style
    public open val headingXXSmall: LemonadeTextStyle get() = LemonadeTypography.HeadingXXSmall.style

    // Body XLarge styles
    public open val bodyXLargeRegular: LemonadeTextStyle get() = LemonadeTypography.BodyXLargeRegular.style
    public open val bodyXLargeMedium: LemonadeTextStyle get() = LemonadeTypography.BodyXLargeMedium.style
    public open val bodyXLargeSemiBold: LemonadeTextStyle get() = LemonadeTypography.BodyXLargeSemiBold.style

    // Body Large styles
    public open val bodyLargeRegular: LemonadeTextStyle get() = LemonadeTypography.BodyLargeRegular.style
    public open val bodyLargeMedium: LemonadeTextStyle get() = LemonadeTypography.BodyLargeMedium.style
    public open val bodyLargeSemiBold: LemonadeTextStyle get() = LemonadeTypography.BodyLargeSemiBold.style

    // Body Medium styles
    public open val bodyMediumRegular: LemonadeTextStyle get() = LemonadeTypography.BodyMediumRegular.style
    public open val bodyMediumMedium: LemonadeTextStyle get() = LemonadeTypography.BodyMediumMedium.style
    public open val bodyMediumSemiBold: LemonadeTextStyle get() = LemonadeTypography.BodyMediumSemiBold.style
    public open val bodyMediumBold: LemonadeTextStyle get() = LemonadeTypography.BodyMediumBold.style

    // Body Small styles
    public open val bodySmallRegular: LemonadeTextStyle get() = LemonadeTypography.BodySmallRegular.style
    public open val bodySmallMedium: LemonadeTextStyle get() = LemonadeTypography.BodySmallMedium.style
    public open val bodySmallSemiBold: LemonadeTextStyle get() = LemonadeTypography.BodySmallSemiBold.style

    // Body XSmall styles
    public open val bodyXSmallRegular: LemonadeTextStyle get() = LemonadeTypography.BodyXSmallRegular.style
    public open val bodyXSmallMedium: LemonadeTextStyle get() = LemonadeTypography.BodyXSmallMedium.style
    public open val bodyXSmallSemiBold: LemonadeTextStyle get() = LemonadeTypography.BodyXSmallSemiBold.style
    public open val bodyXSmallOverline: LemonadeTextStyle get() = LemonadeTypography.BodyXSmallOverline.style
}
