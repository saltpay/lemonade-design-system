#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class SizeResource(
    val sizeValue: Int,
)

fun main() {
    val sizeTokensFile = File("tokens/size.json")
    val outputDir = File("swiftui/Sources/Lemonade")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!sizeTokensFile.exists() || !sizeTokensFile.isFile) {
            error(message = "File $sizeTokensFile does not exist in system")
        }
        val sizeResources = readFileResourceFile(
            file = sizeTokensFile,
            resourceMap = { jsonObject ->
                SizeResource(
                    sizeValue = jsonObject.getInt("resolvedValue"),
                )
            },
        ).sortedBy { it.value.sizeValue }
        println("✓ Loaded size resources")

        val code = buildSizeCode(
            scriptFilePath = "scripts/swiftui-size-token-converter.main.kts",
            resources = sizeResources,
        )
        val outputFile = File(outputDir, "LemonadeSizes.swift")
        outputFile.writeText(code)
        println("✓ Sizes file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${sizeTokensFile.name}: ${error.message}")
    }
}

private fun buildSizeCode(
    scriptFilePath: String,
    resources: List<ResourceData<SizeResource>>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design System Size tokens.")
        appendLine("/// Provides a consistent and scalable way to manage sizes across interfaces.")
        appendLine("/// These values can be used for component dimensions to create balanced layouts.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Size token enum")
        appendLine("public enum LemonadeSizes {")
        resources.forEach { resource ->
            appendLine("    case ${resource.groupFullName?.replaceFirstChar { it.lowercase() }}")
        }
        appendLine()
        appendLine("    /// Returns the CGFloat value for this size token")
        appendLine("    public var value: CGFloat {")
        appendLine("        switch self {")
        resources.forEach { resource ->
            appendLine("        case .${resource.groupFullName?.replaceFirstChar { it.lowercase() }}: return ${resource.value.sizeValue}")
        }
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Protocol for size values")
        appendLine("public protocol LemonadeSizeValues {")
        resources.forEach { resource ->
            appendLine("    var ${resource.name}: CGFloat { get }")
        }
        appendLine("}")
        appendLine()
        appendLine("/// Default size values implementation")
        appendLine("public struct LemonadeSizeValuesImpl: LemonadeSizeValues {")
        resources.forEach { resource ->
            appendLine("    public let ${resource.name}: CGFloat = LemonadeSizes.${resource.groupFullName?.replaceFirstChar { it.lowercase() }}.value")
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

main()
