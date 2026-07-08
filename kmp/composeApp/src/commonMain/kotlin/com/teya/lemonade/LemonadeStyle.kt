package com.teya.lemonade

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

internal sealed interface LemonadeStyle {
    val label: String

    @Composable
    fun resolveColors(): LemonadeSemanticColors

    data object Light : LemonadeStyle {
        override val label: String = "Light"

        @Composable
        override fun resolveColors(): LemonadeSemanticColors = LemonadeLightTheme
    }

    data object Dark : LemonadeStyle {
        override val label: String = "Dark"

        @Composable
        override fun resolveColors(): LemonadeSemanticColors = LemonadeDarkTheme
    }

    data object System : LemonadeStyle {
        override val label: String = "System"

        @Composable
        override fun resolveColors(): LemonadeSemanticColors =
            if (isSystemInDarkTheme()) {
                LemonadeDarkTheme
            } else {
                LemonadeLightTheme
            }
    }

    companion object {
        val entries: List<LemonadeStyle> = listOf(Light, Dark, System)
        val Default: LemonadeStyle = System
    }
}
