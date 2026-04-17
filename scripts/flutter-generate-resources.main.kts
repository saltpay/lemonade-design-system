#!/usr/bin/env kotlin

private val scripts = listOf(
    "kotlin scripts/flutter-color-token-converter.main.kts",
    "kotlin scripts/flutter-theme-token-converter.main.kts",
    "kotlin scripts/flutter-spacing-token-converter.main.kts",
    "kotlin scripts/flutter-radius-token-converter.main.kts",
    "kotlin scripts/flutter-opacity-token-converter.main.kts",
    "kotlin scripts/flutter-border-token-converter.main.kts",
    "kotlin scripts/flutter-shadow-token-converter.main.kts",
    "kotlin scripts/flutter-size-token-converter.main.kts",
    "kotlin scripts/flutter-svg-converter.main.kts",
)

fun main() {
    println("Running ${scripts.size} Flutter scripts")
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
