package com.teya.lemonade

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
public fun LemonadeTheme(
    colors: LemonadeSemanticColors = if (isSystemInDarkTheme()) {
        LemonadeDarkTheme
    } else {
        LemonadeLightTheme
    },
    typography: LemonadeTypographyProvider = LemonadeTheme.typography,
    radius: LemonadeRadiusValues = LemonadeTheme.radius,
    shapes: LemonadeShapes = LemonadeTheme.shapes,
    opacities: LemonadeOpacity = LemonadeTheme.opacities,
    spaces: LemonadeSpaceValues = LemonadeTheme.spaces,
    borderWidths: LemonadeBorderWidth = LemonadeTheme.borderWidths,
    sizes: LemonadeSizeValues = LemonadeTheme.sizes,
    effects: LemonadeEffects = LemonadeTheme.effects,
    animations: LemonadeAnimationMode = LemonadeTheme.animations,
    content: @Composable () -> Unit,
) {
    val animationsEnabled = when (animations) {
        LemonadeAnimationMode.Full -> true
        LemonadeAnimationMode.None -> false
        LemonadeAnimationMode.System -> rememberSystemAnimationsEnabled()
    }
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
        LocalAnimations provides animations,
        LocalAnimationsEnabled provides animationsEnabled,
        content = content,
    )
}

@Deprecated(
    message = "Use the overload with an animations parameter.",
    replaceWith = ReplaceWith(
        expression = "LemonadeTheme(colors, typography, radius, shapes, opacities, spaces, " +
            "borderWidths, sizes, effects, content = content)",
    ),
    level = DeprecationLevel.HIDDEN,
)
@Composable
public fun LemonadeTheme(
    colors: LemonadeSemanticColors = if (isSystemInDarkTheme()) {
        LemonadeDarkTheme
    } else {
        LemonadeLightTheme
    },
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
    LemonadeTheme(
        colors = colors,
        typography = typography,
        radius = radius,
        shapes = shapes,
        opacities = opacities,
        spaces = spaces,
        borderWidths = borderWidths,
        sizes = sizes,
        effects = effects,
        animations = LemonadeTheme.animations,
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

    public val animations: LemonadeAnimationMode
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalAnimations.current
        }

    public val animationsEnabled: Boolean
        @Composable
        @ReadOnlyComposable
        get() {
            return LocalAnimationsEnabled.current
        }
}
