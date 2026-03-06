package com.teya.lemonade

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.teya.lemonade.app.App

internal fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Lemonade",
        ) {
            LemonadeStyledTheme {
                App()
            }
        }
    }
}
