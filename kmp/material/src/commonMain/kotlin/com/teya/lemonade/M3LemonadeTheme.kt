package com.teya.lemonade

import androidx.compose.foundation.Indication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import com.teya.lemonade.core.LemonadeTypography

@Composable
public fun M3LemonadeTheme(
    colors: LemonadeSemanticColors = LemonadeTheme.colors,
    typography: LemonadeTypography = LemonadeTheme.typography,
    radius: LemonadeRadiusValues = LemonadeTheme.radius,
    shapes: LemonadeShapes = LemonadeTheme.shapes,
    opacities: LemonadeOpacity = LemonadeTheme.opacities,
    spaces: LemonadeSpaceValues = LemonadeTheme.spaces,
    borderWidths: LemonadeBorderWidth = LemonadeTheme.borderWidths,
    sizes: LemonadeSizeValues = LemonadeTheme.sizes,
    effects: LemonadeEffects = LemonadeTheme.effects,
    content: @Composable () -> Unit
) {
    LemonadeTheme(
        colors = colors,
        typography = typography,
        radius = radius,
        shapes = shapes,
        opacities = opacities,
        spaces = spaces,
        borderWidths = borderWidths,
        sizes = sizes,
        effects = m3LemonadeEffects(effects = effects),
    ) {
        MaterialTheme(
            colorScheme = m3LemonadeColorScheme(),
            shapes = m3LemonadeShapes(),
            typography = m3LemonadeTypography(),
            content = content,
        )
    }
}

@Composable
private fun m3LemonadeTypography(): Typography {
    val typography = LemonadeTheme.typography
    return Typography(
        displayLarge = typography.displayLarge.textStyle,
        displayMedium = typography.displayMedium.textStyle,
        displaySmall = typography.displaySmall.textStyle,
        headlineLarge = typography.headingLarge.textStyle,
        headlineMedium = typography.headingMedium.textStyle,
        headlineSmall = typography.headingSmall.textStyle,
        titleLarge = typography.headingXSmall.textStyle,
        titleMedium = typography.bodyLargeSemiBold.textStyle,
        titleSmall = typography.bodyMediumSemiBold.textStyle,
        bodyLarge = typography.bodyLargeRegular.textStyle,
        bodyMedium = typography.bodyMediumRegular.textStyle,
        bodySmall = typography.bodySmallRegular.textStyle,
        labelLarge = typography.bodyMediumMedium.textStyle,
        labelMedium = typography.bodySmallMedium.textStyle,
        labelSmall = typography.bodyXSmallMedium.textStyle,
    )
}

@Composable
private fun m3LemonadeShapes(): Shapes {
    val radius = LemonadeTheme.radius
    return Shapes(
        extraSmall = RoundedCornerShape(radius.radius100),
        small = RoundedCornerShape(radius.radius200),
        medium = RoundedCornerShape(radius.radius300),
        large = RoundedCornerShape(radius.radius400),
        extraLarge = RoundedCornerShape(radius.radius600),
    )
}

@Composable
private fun m3LemonadeColorScheme(): ColorScheme {
    val colors = LemonadeTheme.colors
    return ColorScheme(
        primary = colors.background.bgBrand,
        onPrimary = colors.content.contentOnBrandHigh,
        primaryContainer = colors.background.bgBrandSubtle,
        onPrimaryContainer = colors.content.contentBrand,
        inversePrimary = colors.content.contentBrandInverse,
        secondary = colors.background.bgNeutral,
        onSecondary = colors.content.contentNeutralOnColor,
        secondaryContainer = colors.background.bgNeutralSubtle,
        onSecondaryContainer = colors.content.contentNeutral,
        tertiary = colors.background.bgInfo,
        onTertiary = colors.content.contentInfoOnColor,
        tertiaryContainer = colors.background.bgInfoSubtle,
        onTertiaryContainer = colors.content.contentInfo,
        background = colors.background.bgDefault,
        onBackground = colors.content.contentPrimary,
        surface = colors.background.bgDefault,
        onSurface = colors.content.contentPrimary,
        surfaceVariant = colors.background.bgSubtle,
        onSurfaceVariant = colors.content.contentSecondary,
        surfaceTint = colors.background.bgBrand,
        inverseSurface = colors.background.bgDefaultInverse,
        inverseOnSurface = colors.content.contentPrimaryInverse,
        error = colors.background.bgCritical,
        onError = colors.content.contentAlwaysLight,
        errorContainer = colors.background.bgCriticalSubtle,
        onErrorContainer = colors.content.contentCritical,
        outline = colors.border.borderNeutralMedium,
        outlineVariant = colors.border.borderNeutralLow,
        scrim = colors.background.bgAlwaysDark,
        surfaceBright = colors.background.bgElevatedHigh,
        surfaceDim = colors.background.bgSubtle,
        surfaceContainer = colors.background.bgElevated,
        surfaceContainerHigh = colors.background.bgElevatedHigh,
        surfaceContainerHighest = colors.background.bgElevatedHigh,
        surfaceContainerLow = colors.background.bgSubtle,
        surfaceContainerLowest = colors.background.bgDefault,
        primaryFixed = colors.background.bgBrand,
        primaryFixedDim = colors.background.bgBrandSubtle,
        onPrimaryFixed = colors.content.contentOnBrandHigh,
        onPrimaryFixedVariant = colors.content.contentBrand,
        secondaryFixed = colors.background.bgNeutral,
        secondaryFixedDim = colors.background.bgNeutralSubtle,
        onSecondaryFixed = colors.content.contentNeutralOnColor,
        onSecondaryFixedVariant = colors.content.contentSecondary,
        tertiaryFixed = colors.background.bgInfo,
        tertiaryFixedDim = colors.background.bgInfoSubtle,
        onTertiaryFixed = colors.content.contentInfoOnColor,
        onTertiaryFixedVariant = colors.content.contentInfo,
    )
}

private fun m3LemonadeEffects(effects: LemonadeEffects): LemonadeEffects {
    return object : LemonadeEffects by effects {
        override val interactionIndication: Indication = ripple()
    }
}
