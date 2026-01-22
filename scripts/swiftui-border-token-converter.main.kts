#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class BorderWidthResource(
    val borderWidthValue: Int,
)

fun main() {
    val borderWidthTokensFile = File("tokens/border-width.json")
    val outputDir = File("swiftui/Sources/Lemonade")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!borderWidthTokensFile.exists() || !borderWidthTokensFile.isFile) {
            error(message = "File $borderWidthTokensFile does not exist in system")
        }
        val borderWidthResources = readFileResourceFile(
            file = borderWidthTokensFile,
            resourceMap = { jsonObject ->
                BorderWidthResource(
                    borderWidthValue = jsonObject.getInt("resolvedValue"),
                )
            },
        ).sortedBy { it.value.borderWidthValue }
        println("✓ Loaded border width resources")

        val code = buildBorderWidthCode(
            scriptFilePath = "scripts/swiftui-border-token-converter.main.kts",
            resources = borderWidthResources,
        )
        val outputFile = File(outputDir, "LemonadeBorderWidth.swift")
        outputFile.writeText(code)
        println("✓ Border width file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${borderWidthTokensFile.name}: ${error.message}")
    }
}

private fun buildBorderWidthCode(
    scriptFilePath: String,
    resources: List<ResourceData<BorderWidthResource>>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design Border Width tokens.")
        appendLine("/// Sets a small, clear set of predefined border width values for UI elements to ensure")
        appendLine("/// consistent, scalable borders across the product.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()

        // Generate group protocols
        val groupedResources = resources.groupBy { resource -> resource.groupFullName }
        groupedResources.forEach { (groupName, groupResources) ->
            if (groupName != null) {
                appendLine("/// ${groupName} border width values")
                appendLine("public protocol ${groupName}BorderWidth {")
                groupResources.forEach { resource ->
                    appendLine("    var ${resource.name}: CGFloat { get }")
                }
                appendLine("}")
                appendLine()
            }
        }

        appendLine("/// Protocol defining border width categories")
        appendLine("public protocol LemonadeBorderWidth {")
        groupedResources.forEach { (groupName, _) ->
            if (groupName != null) {
                appendLine("    var ${groupName.replaceFirstChar { char -> char.lowercase() }}: ${groupName}BorderWidth { get }")
            }
        }
        appendLine("}")
        appendLine()

        // Generate implementation structs
        groupedResources.forEach { (groupName, groupResources) ->
            if (groupName != null) {
                appendLine("internal struct ${groupName}BorderWidthImpl: ${groupName}BorderWidth {")
                groupResources.forEach { resource ->
                    appendLine("    let ${resource.name}: CGFloat = ${resource.value.borderWidthValue}")
                }
                appendLine("}")
                appendLine()
            }
        }

        appendLine("/// Default border width implementation")
        appendLine("public struct LemonadeBorderWidthTokens: LemonadeBorderWidth {")
        groupedResources.forEach { (groupName, _) ->
            if (groupName != null) {
                appendLine("    public let ${groupName.replaceFirstChar { char -> char.lowercase() }}: ${groupName}BorderWidth = ${groupName}BorderWidthImpl()")
            }
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

main()
