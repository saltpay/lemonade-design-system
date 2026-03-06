package com.teya.lemonade

internal sealed interface LemonadeStyle {
    val label: String
    val colors: LemonadeSemanticColors

    data object Light : LemonadeStyle {
        override val label: String = "Light"
        override val colors: LemonadeSemanticColors = LemonadeLightTheme
    }

    data object Dark : LemonadeStyle {
        override val label: String = "Dark"
        override val colors: LemonadeSemanticColors = LemonadeDarkTheme
    }

    companion object {
        val entries: List<LemonadeStyle> = listOf(Light, Dark)
        val Default: LemonadeStyle = Light
    }
}
