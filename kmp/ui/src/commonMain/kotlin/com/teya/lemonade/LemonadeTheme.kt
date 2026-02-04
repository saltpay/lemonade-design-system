package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.teya.lemonade.core.LemonadeTextStyle
import com.teya.lemonade.core.LemonadeTypography

@Composable
public fun LemonadeTheme(
    colors: LemonadeSemanticColors = LemonadeTheme.colors,
    typography: LemonadeTypography = LemonadeTheme.typography,
    radius: LemonadeRadiusValues = LemonadeTheme.radius,
    shapes: LemonadeShapes = LemonadeTheme.shapes,
    opacities: LemonadeOpacity = LemonadeTheme.opacities,
    spaces: LemonadeSpaceValues = LemonadeTheme.spaces,
    borderWidths: LemonadeBorderWidth = LemonadeTheme.borderWidths,
    sizes: LemonadeSizeValues = LemonadeTheme.sizes,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalTypographies provides typography,
        LocalColors provides colors,
        LocalContentColors provides colors.content.contentNeutral,
        LocalTextStyles provides typography.bodyMediumRegular,
        LocalRadius provides radius,
        LocalShapes provides shapes,
        LocalOpacities provides opacities,
        LocalSpaces provides spaces,
        LocalBorderWidths provides borderWidths,
        LocalSizes provides sizes,
        content = content,
    )
}

public object LemonadeTheme {
    public val colors: LemonadeSemanticColors
        @ReadOnlyComposable
        @Composable
        get() {
            return LocalColors.current
        }

    public val typography: LemonadeTypography
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalTypographies.current
        }

    public val radius: LemonadeRadiusValues
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalRadius.current
        }

    public val shapes: LemonadeShapes
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalShapes.current
        }

    public val opacities: LemonadeOpacity
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalOpacities.current
        }

    public val spaces: LemonadeSpaceValues
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalSpaces.current
        }

    public val borderWidths: LemonadeBorderWidth
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalBorderWidths.current
        }

    public val sizes: LemonadeSizeValues
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalSizes.current
        }
}

internal val LocalColors: ProvidableCompositionLocal<LemonadeSemanticColors> =
    staticCompositionLocalOf {
        LemonadeLightTheme
    }

internal val LocalTypographies: ProvidableCompositionLocal<LemonadeTypography> =
    staticCompositionLocalOf {
        DefaultLemonadeTypography
    }

internal val LocalContentColors: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    LemonadeLightTheme.content.contentNeutral
}

internal val LocalTextStyles: ProvidableCompositionLocal<LemonadeTextStyle> =
    staticCompositionLocalOf {
        DefaultLemonadeTypography.bodyMediumRegular
    }

internal val LocalRadius: ProvidableCompositionLocal<LemonadeRadiusValues> =
    staticCompositionLocalOf {
        InternalLemonadeRadiusValues()
    }

internal val LocalShapes: ProvidableCompositionLocal<LemonadeShapes> = staticCompositionLocalOf {
    InternalLemonadeShapes()
}

internal val LocalSpaces: ProvidableCompositionLocal<LemonadeSpaceValues> =
    staticCompositionLocalOf {
        InternalLemonadeSpaceValues()
    }

internal val LocalOpacities: ProvidableCompositionLocal<LemonadeOpacity> =
    staticCompositionLocalOf {
        InternalLemonadeOpacityTokens()
    }

internal val LocalBorderWidths: ProvidableCompositionLocal<LemonadeBorderWidth> =
    staticCompositionLocalOf {
        InternalLemonadeBorderWidth()
    }

internal val LocalSizes: ProvidableCompositionLocal<LemonadeSizeValues> = staticCompositionLocalOf {
    InternalLemonadeSizeValues()
}