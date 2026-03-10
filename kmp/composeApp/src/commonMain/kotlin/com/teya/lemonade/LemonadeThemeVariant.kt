package com.teya.lemonade

internal sealed interface LemonadeThemeVariant {
    val label: String

    data object Standard : LemonadeThemeVariant {
        override val label: String = "Standard"
    }

    data object Expressive : LemonadeThemeVariant {
        override val label: String = "Expressive"
    }

    companion object {
        val entries: List<LemonadeThemeVariant> = listOf(Standard, Expressive)
        val Default: LemonadeThemeVariant = Standard
    }
}
