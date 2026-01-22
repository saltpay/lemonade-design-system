#!/usr/bin/env kotlin

private val scripts = listOf(
    "kotlin scripts/kmp-color-token-converter.main.kts",
    "kotlin scripts/kmp-theme-token-converter.main.kts",
    "kotlin scripts/kmp-opacity-token-converter.main.kts",
    "kotlin scripts/kmp-radius-token-converter.main.kts",
    "kotlin scripts/kmp-spacing-token-converter.main.kts",
    "kotlin scripts/kmp-border-width-token-converter.main.kts",
    "kotlin scripts/kmp-shadow-token-converter.main.kts",
    "kotlin scripts/kmp-dimension-token-converter.main.kts",
    "kotlin scripts/kmp-svg-converter.main.kts -pack-dir icons/ -pack-name LemonadeIcons",
    "kotlin scripts/kmp-svg-converter.main.kts -pack-dir flags/ -pack-name LemonadeCountryFlags",
    "kotlin scripts/kmp-svg-converter.main.kts -pack-dir brandLogos/ -pack-name LemonadeBrandLogos",
)

fun main() {
    println("Running ${scripts.size} scripts")
    scripts.forEach { script ->
        println("-----------------------")
        try {
            println("Running script: $script")
            ProcessBuilder(script.split(" "))
                .inheritIO()
                .start()
                .waitFor()
        } catch (error: Throwable) {
            println("Failed to run script '$script': $error")
        }
        println("-----------------------")
    }
}

main()