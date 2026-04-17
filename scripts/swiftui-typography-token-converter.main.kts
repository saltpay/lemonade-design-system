#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class TypographyTokenValue(
    val floatValue: Double?,
    val stringValue: String?,
)

fun main() {
    val typographyTokensFile = File("tokens/typography.json")
    val outputDir = File("swiftui/Sources/Lemonade")

    try {
        if (!outputDir.exists()) outputDir.mkdirs()

        if (!typographyTokensFile.exists() || !typographyTokensFile.isFile) {
            error(message = "File $typographyTokensFile does not exist in system")
        }

        val allResources = readFileResourceFile(
            file = typographyTokensFile,
            resourceMap = { jsonObject ->
                val resolvedValue = jsonObject.get("resolvedValue")
                when (resolvedValue) {
                    is Number -> TypographyTokenValue(floatValue = resolvedValue.toDouble(), stringValue = null)
                    is String -> TypographyTokenValue(floatValue = null, stringValue = resolvedValue)
                    else -> null
                }
            },
        ).filterNull()
        println("✓ Loaded typography resources")

        val fontSizeResources = allResources
            .filter { it.groups.firstOrNull() == "FontSize" }
            .sortedBy { it.value.floatValue }
        val fontWeightResources = allResources
            .filter { it.groups.firstOrNull() == "FontWeight" }
        val lineHeightResources = allResources
            .filter { it.groups.firstOrNull() == "LineHeight" }
            .sortedBy { it.value.floatValue }
        val scriptFilePath = "scripts/swiftui-typography-token-converter.main.kts"

        File(outputDir, "LemonadeFontSizes.swift").writeText(
            buildFontSizesCode(scriptFilePath, fontSizeResources)
        )
        println("✓ LemonadeFontSizes.swift created")

        File(outputDir, "LemonadeFontWeights.swift").writeText(
            buildFontWeightsCode(scriptFilePath, fontWeightResources)
        )
        println("✓ LemonadeFontWeights.swift created")

        File(outputDir, "LemonadeLineHeights.swift").writeText(
            buildLineHeightsCode(scriptFilePath, lineHeightResources)
        )
        println("✓ LemonadeLineHeights.swift created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${typographyTokensFile.name}: ${error.message}")
    }
}

private fun buildFontSizesCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("import SwiftUI")
    appendLine()
    appendLine("/// Lemonade Design System Font Size tokens.")
    appendLine("/// Provides a consistent way to manage font sizes across interfaces.")
    appendLine("///")
    defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
        appendLine("/// $line")
    }
    appendLine()
    appendLine("/// Font size token enum")
    appendLine("public enum LemonadeFontSizes {")
    // Typography tokens have path separators (e.g. "font-size/font-size-1200"), so
    // groupFullName collapses all tokens in the same group to the same value ("FontSize").
    // Use resource.name directly for Swift enum case names (already camelCase, e.g. "fontSize1200").
    resources.forEach { resource ->
        appendLine("    case ${resource.name}")
    }
    appendLine()
    appendLine("    /// Returns the CGFloat value for this font size token")
    appendLine("    public var value: CGFloat {")
    appendLine("        switch self {")
    resources.forEach { resource ->
        val cgValue = requireNotNull(resource.value.floatValue) {
            "Missing numeric value for token '${resource.name}'"
        }.toInt()
        appendLine("        case .${resource.name}: return $cgValue")
    }
    appendLine("        }")
    appendLine("    }")
    appendLine("}")
    appendLine()
    appendLine("/// Protocol for font size values")
    appendLine("public protocol LemonadeFontSizeValues {")
    resources.forEach { resource ->
        appendLine("    var ${resource.name}: CGFloat { get }")
    }
    appendLine("}")
    appendLine()
    appendLine("/// Default font size values implementation")
    appendLine("public struct LemonadeFontSizeValuesImpl: LemonadeFontSizeValues {")
    resources.forEach { resource ->
        appendLine("    public let ${resource.name}: CGFloat = LemonadeFontSizes.${resource.name}.value")
    }
    appendLine()
    appendLine("    public init() {}")
    appendLine("}")
}

private fun buildFontWeightsCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("import SwiftUI")
    appendLine()
    appendLine("/// Lemonade Design System Font Weight tokens.")
    appendLine("/// Maps design token weights to SwiftUI Font.Weight values.")
    appendLine("///")
    defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
        appendLine("/// $line")
    }
    appendLine()
    appendLine("/// Font weight token enum")
    appendLine("public enum LemonadeFontWeights {")
    // see LemonadeFontSizes for why resource.name is used instead of groupFullName
    resources.forEach { resource ->
        appendLine("    case ${resource.name}")
    }
    appendLine()
    appendLine("    /// Returns the Font.Weight value for this font weight token")
    appendLine("    public var value: Font.Weight {")
    appendLine("        switch self {")
    resources.forEach { resource ->
        val fontWeightExpression = when (resource.value.stringValue) {
            "Bold" -> ".bold"
            "SemiBold" -> ".semibold"
            "Medium" -> ".medium"
            "Regular" -> ".regular"
            else -> ".regular"
        }
        appendLine("        case .${resource.name}: return $fontWeightExpression")
    }
    appendLine("        }")
    appendLine("    }")
    appendLine("}")
    appendLine()
    appendLine("/// Protocol for font weight values")
    appendLine("public protocol LemonadeFontWeightValues {")
    resources.forEach { resource ->
        appendLine("    var ${resource.name}: Font.Weight { get }")
    }
    appendLine("}")
    appendLine()
    appendLine("/// Default font weight values implementation")
    appendLine("public struct LemonadeFontWeightValuesImpl: LemonadeFontWeightValues {")
    resources.forEach { resource ->
        appendLine("    public let ${resource.name}: Font.Weight = LemonadeFontWeights.${resource.name}.value")
    }
    appendLine()
    appendLine("    public init() {}")
    appendLine("}")
}

private fun buildLineHeightsCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("import SwiftUI")
    appendLine()
    appendLine("/// Lemonade Design System Line Height tokens.")
    appendLine("/// Provides a consistent way to manage line heights across interfaces.")
    appendLine("///")
    defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
        appendLine("/// $line")
    }
    appendLine()
    appendLine("/// Line height token enum")
    appendLine("public enum LemonadeLineHeights {")
    // see LemonadeFontSizes for why resource.name is used instead of groupFullName
    resources.forEach { resource ->
        appendLine("    case ${resource.name}")
    }
    appendLine()
    appendLine("    /// Returns the CGFloat value for this line height token")
    appendLine("    public var value: CGFloat {")
    appendLine("        switch self {")
    resources.forEach { resource ->
        val cgValue = requireNotNull(resource.value.floatValue) {
            "Missing numeric value for token '${resource.name}'"
        }.toInt()
        appendLine("        case .${resource.name}: return $cgValue")
    }
    appendLine("        }")
    appendLine("    }")
    appendLine("}")
    appendLine()
    appendLine("/// Protocol for line height values")
    appendLine("public protocol LemonadeLineHeightValues {")
    resources.forEach { resource ->
        appendLine("    var ${resource.name}: CGFloat { get }")
    }
    appendLine("}")
    appendLine()
    appendLine("/// Default line height values implementation")
    appendLine("public struct LemonadeLineHeightValuesImpl: LemonadeLineHeightValues {")
    resources.forEach { resource ->
        appendLine("    public let ${resource.name}: CGFloat = LemonadeLineHeights.${resource.name}.value")
    }
    appendLine()
    appendLine("    public init() {}")
    appendLine("}")
}

main()
