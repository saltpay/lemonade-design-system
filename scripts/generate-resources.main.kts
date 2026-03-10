#!/usr/bin/env kotlin

private val kmpTokenConverters = listOf(
    "kotlin scripts/kmp-color-token-converter.main.kts",
    "kotlin scripts/kmp-theme-token-converter.main.kts",
    "kotlin scripts/kmp-opacity-token-converter.main.kts",
    "kotlin scripts/kmp-radius-token-converter.main.kts",
    "kotlin scripts/kmp-spacing-token-converter.main.kts",
    "kotlin scripts/kmp-border-width-token-converter.main.kts",
    "kotlin scripts/kmp-shadow-token-converter.main.kts",
    "kotlin scripts/kmp-dimension-token-converter.main.kts",
)

private val swiftUiTokenConverters = listOf(
    "kotlin scripts/swiftui-color-token-converter.main.kts",
    "kotlin scripts/swiftui-theme-token-converter.main.kts",
    "kotlin scripts/swiftui-opacity-token-converter.main.kts",
    "kotlin scripts/swiftui-radius-token-converter.main.kts",
    "kotlin scripts/swiftui-spacing-token-converter.main.kts",
    "kotlin scripts/swiftui-border-token-converter.main.kts",
    "kotlin scripts/swiftui-shadow-token-converter.main.kts",
    "kotlin scripts/swiftui-size-token-converter.main.kts",
    "kotlin scripts/swiftui-color-assets-generator.main.kts",
)

private val svgAssetConverter = listOf(
    "kotlin scripts/svg-asset-converter.main.kts",
)

private val postSvgGenerators = listOf(
    "kotlin scripts/kmp-country-flags-alpha2-generator.main.kts",
)

fun runGroup(groupName: String, scripts: List<String>) {
    println("=======================")
    println(" $groupName")
    println("=======================")
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

fun main() {
    val allScripts = kmpTokenConverters + swiftUiTokenConverters + svgAssetConverter + postSvgGenerators
    println("Running ${allScripts.size} scripts")
    println()

    runGroup("KMP Token Converters", kmpTokenConverters)
    runGroup("SwiftUI Token Converters", swiftUiTokenConverters)
    runGroup("SVG Asset Converter", svgAssetConverter)
    runGroup("Post-SVG Generators", postSvgGenerators)

    println()
    println("All resource generation complete.")
}

main()
