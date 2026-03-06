package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

internal class LemonadeStyleHandler(initialStyle: LemonadeStyle = LemonadeStyle.Default) {
    var currentStyle: LemonadeStyle by mutableStateOf(initialStyle)
}

internal val LocalLemonadeStyleHandler = staticCompositionLocalOf {
    LemonadeStyleHandler()
}

@Composable
internal fun rememberLemonadeStyleHandler(initialStyle: LemonadeStyle = LemonadeStyle.Default): LemonadeStyleHandler =
    remember { LemonadeStyleHandler(initialStyle) }

@Composable
internal fun LemonadeStyledTheme(
    handler: LemonadeStyleHandler = rememberLemonadeStyleHandler(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalLemonadeStyleHandler provides handler) {
        LemonadeTheme(colors = handler.currentStyle.colors) {
            content()
        }
    }
}
