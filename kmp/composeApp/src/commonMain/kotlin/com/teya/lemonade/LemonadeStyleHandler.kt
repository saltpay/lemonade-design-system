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
        when (handler.currentVariant) {
            LemonadeThemeVariant.Standard -> LemonadeTheme(colors = handler.currentStyle.colors) {
                content()
            }
            LemonadeThemeVariant.Expressive -> LemonadeExpressiveTheme(colors = handler.currentStyle.colors) {
                content()
            }
        }
    }
}
