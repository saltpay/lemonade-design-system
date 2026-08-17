package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

internal class LemonadeStyleHandler(
    initialStyle: LemonadeStyle = LemonadeStyle.Default,
    initialVariant: LemonadeThemeVariant = LemonadeThemeVariant.Default,
) {
    var currentStyle: LemonadeStyle by mutableStateOf(initialStyle)
    var currentVariant: LemonadeThemeVariant by mutableStateOf(initialVariant)
    var animations: LemonadeAnimationMode by mutableStateOf(LemonadeAnimationMode.System)
}

internal val LocalLemonadeStyleHandler = staticCompositionLocalOf {
    LemonadeStyleHandler()
}

@Composable
internal fun rememberLemonadeStyleHandler(
    initialStyle: LemonadeStyle = LemonadeStyle.Default,
    initialVariant: LemonadeThemeVariant = LemonadeThemeVariant.Default,
): LemonadeStyleHandler = remember { LemonadeStyleHandler(initialStyle, initialVariant) }

@Composable
internal fun LemonadeStyledTheme(
    handler: LemonadeStyleHandler = rememberLemonadeStyleHandler(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalLemonadeStyleHandler provides handler) {
        val colors = handler.currentStyle.resolveColors()
        val baseEffects = LemonadeTheme.effects
        val effects = remember(baseEffects, handler.animations) {
            object : LemonadeEffects by baseEffects {
                override val animations: LemonadeAnimationMode = handler.animations
            }
        }
        when (handler.currentVariant) {
            LemonadeThemeVariant.Standard -> LemonadeTheme(
                colors = colors,
                effects = effects,
            ) {
                content()
            }
            LemonadeThemeVariant.Expressive -> LemonadeExpressiveTheme(
                colors = colors,
                effects = effects,
            ) {
                content()
            }
        }
    }
}
