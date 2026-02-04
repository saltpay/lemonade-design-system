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
 * Default implementation of [LemonadeTypography] following the Lemonade Design System specifications.
 */
internal object DefaultLemonadeTypography : LemonadeTypography() {
    // Display styles
    override val displayXSmall: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 24f, lineHeight = 32f, fontWeight = 600)
    override val displaySmall: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 28f, lineHeight = 36f, fontWeight = 600)
    override val displayMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 36f, lineHeight = 44f, fontWeight = 600)
    override val displayLarge: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 48f, lineHeight = 56f, fontWeight = 600)

    // Heading styles
    override val headingXLarge: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 40f, lineHeight = 48f, fontWeight = 600)
    override val headingLarge: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 32f, lineHeight = 40f, fontWeight = 600)
    override val headingMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 28f, lineHeight = 36f, fontWeight = 600)
    override val headingSmall: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 24f, lineHeight = 32f, fontWeight = 600)
    override val headingXSmall: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 18f, lineHeight = 26f, fontWeight = 600)
    override val headingXXSmall: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 600)

    // Body XLarge styles
    override val bodyXLargeRegular: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 20f, lineHeight = 28f, fontWeight = 400)
    override val bodyXLargeMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 20f, lineHeight = 28f, fontWeight = 500)
    override val bodyXLargeSemiBold: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 20f, lineHeight = 28f, fontWeight = 600)

    // Body Large styles
    override val bodyLargeRegular: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 18f, lineHeight = 28f, fontWeight = 400)
    override val bodyLargeMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 18f, lineHeight = 28f, fontWeight = 500)
    override val bodyLargeSemiBold: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 18f, lineHeight = 28f, fontWeight = 600)

    // Body Medium styles
    override val bodyMediumRegular: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 400)
    override val bodyMediumMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 500)
    override val bodyMediumSemiBold: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 600)
    override val bodyMediumBold: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 16f, lineHeight = 24f, fontWeight = 600)

    // Body Small styles
    override val bodySmallRegular: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 14f, lineHeight = 20f, fontWeight = 400)
    override val bodySmallMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 14f, lineHeight = 20f, fontWeight = 500)
    override val bodySmallSemiBold: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 14f, lineHeight = 20f, fontWeight = 600)

    // Body XSmall styles
    override val bodyXSmallRegular: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 400)
    override val bodyXSmallMedium: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 500)
    override val bodyXSmallSemiBold: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 600)
    override val bodyXSmallOverline: LemonadeTextStyle =
        LemonadeTextStyle(fontSize = 12f, lineHeight = 16f, fontWeight = 600, letterSpacing = 1.5f)
}
