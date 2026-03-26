package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
public fun LemonadeTheme(
    colors: LemonadeSemanticColors = LemonadeTheme.colors,
    typography: LemonadeTypographyProvider = LemonadeTheme.typography,
    radius: LemonadeRadiusValues = LemonadeTheme.radius,
    shapes: LemonadeShapes = LemonadeTheme.shapes,
    opacities: LemonadeOpacity = LemonadeTheme.opacities,
    spaces: LemonadeSpaceValues = LemonadeTheme.spaces,
    borderWidths: LemonadeBorderWidth = LemonadeTheme.borderWidths,
    sizes: LemonadeSizeValues = LemonadeTheme.sizes,
    effects: LemonadeEffects = LemonadeTheme.effects,
    content: @Composable () -> Unit,
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
        LocalEffects provides effects,
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

    public val typography: LemonadeTypographyProvider
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

    public val effects: LemonadeEffects
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalEffects.current
        }
}
