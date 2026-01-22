#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class SpacingResource(
    val spacingValue: Int,
)

fun main() {
    val spaceTokensFile = File("tokens/spacing.json")
    val outputDir = File("swiftui/Sources/Lemonade")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!spaceTokensFile.exists() || !spaceTokensFile.isFile) {
            error(message = "File $spaceTokensFile does not exist in system")
        }
        val spaceResources = readFileResourceFile(
            file = spaceTokensFile,
            resourceMap = { jsonObject ->
                SpacingResource(
                    spacingValue = jsonObject.getInt("resolvedValue"),
                )
            },
        ).sortedBy { it.value.spacingValue }
        println("✓ Loaded space resources")

        val code = buildSpacingCode(
            scriptFilePath = "scripts/swiftui-spacing-token-converter.main.kts",
            resources = spaceResources,
        )
        val outputFile = File(outputDir, "LemonadeSpacing.swift")
        outputFile.writeText(code)
        println("✓ Spacing file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${spaceTokensFile.name}: ${error.message}")
    }
}

private fun buildSpacingCode(
    scriptFilePath: String,
    resources: List<ResourceData<SpacingResource>>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design System Space tokens.")
        appendLine("/// Provides a consistent and scalable way to manage spacing across interfaces.")
        appendLine("/// These values can be used for margins, paddings, and gaps to create balanced layouts.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Spacing token enum")
        appendLine("public enum LemonadeSpacing {")
        resources.forEach { resource ->
            appendLine("    case ${resource.groupFullName?.replaceFirstChar { it.lowercase() }}")
        }
        appendLine()
        appendLine("    /// Returns the CGFloat value for this spacing token")
        appendLine("    public var value: CGFloat {")
        appendLine("        switch self {")
        resources.forEach { resource ->
            appendLine("        case .${resource.groupFullName?.replaceFirstChar { it.lowercase() }}: return ${resource.value.spacingValue}")
        }
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Protocol for spacing values")
        appendLine("public protocol LemonadeSpaceValues {")
        resources.forEach { resource ->
            appendLine("    var ${resource.name}: CGFloat { get }")
        }
        appendLine("}")
        appendLine()
        appendLine("/// Default spacing values implementation")
        appendLine("public struct LemonadeSpaceValuesImpl: LemonadeSpaceValues {")
        resources.forEach { resource ->
            appendLine("    public let ${resource.name}: CGFloat = LemonadeSpacing.${resource.groupFullName?.replaceFirstChar { it.lowercase() }}.value")
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

main()
