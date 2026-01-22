#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

private data class OpacityResource(
    val opacityValue: Double,
)

fun main() {
    val inputFile = File("tokens/opacity.json")
    val outputDir = File("swiftui/Sources/Lemonade")
    try {
        if (!inputFile.exists() || !inputFile.isFile) {
            error(message = "File $inputFile does not exist in system.")
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val opacityResources = readFileResourceFile(
            file = inputFile,
            resourceMap = { jsonObject ->
                OpacityResource(
                    opacityValue = jsonObject.getDouble("resolvedValue"),
                )
            },
        ).sortedBy { it.value.opacityValue }
        println("✓ Opacities read and parsed.")

        val fullOpacityCode = generateFullOpacityCode(
            scriptFilePath = "scripts/swiftui-opacity-token-converter.main.kts",
            opacitiesResource = opacityResources,
        )
        val outputTokenFile = File(outputDir, "LemonadeOpacity.swift")
        outputTokenFile.writeText(fullOpacityCode)

        println("✓ Converted ${inputFile.name} -> ${outputTokenFile.name}")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${inputFile.name}: ${error.message}")
    }
}

private fun generateFullOpacityCode(
    scriptFilePath: String,
    opacitiesResource: List<ResourceData<OpacityResource>>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design System Opacity tokens.")
        appendLine("/// Provides a consistent and scalable way to manage opacity across interfaces.")
        appendLine("/// These values can be used for disabled components and general transparency handling.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        append(generateOpacityProtocolCode(opacitiesResource = opacitiesResource))
        appendLine()
        append(generateOpacityValuesCode(opacitiesResource = opacitiesResource))
    }
}

private fun generateOpacityProtocolCode(
    opacitiesResource: List<ResourceData<OpacityResource>>,
): String {
    return buildString {
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, groupOpacities) ->
                if (groupName != null) {
                    appendLine("/// ${groupName} opacity values")
                    appendLine("public protocol ${groupName}Opacity {")
                    groupOpacities.forEach { opacity ->
                        appendLine("    var ${opacity.name}: Double { get }")
                    }
                    appendLine("}")
                    appendLine()
                }
            }
        appendLine("/// Protocol defining opacity categories")
        appendLine("public protocol LemonadeOpacity {")
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, _) ->
                if (groupName != null) {
                    appendLine("    var ${groupName.replaceFirstChar { char -> char.lowercase() }}: ${groupName}Opacity { get }")
                }
            }
        appendLine("}")
    }
}

private fun generateOpacityValuesCode(
    opacitiesResource: List<ResourceData<OpacityResource>>,
): String {
    return buildString {
        // Generate implementation structs for each group
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, groupOpacities) ->
                if (groupName != null) {
                    appendLine("internal struct ${groupName}OpacityImpl: ${groupName}Opacity {")
                    groupOpacities.forEach { opacity ->
                        val opacityValue = opacity.value.opacityValue / 100.0
                        appendLine("    let ${opacity.name}: Double = $opacityValue")
                    }
                    appendLine("}")
                    appendLine()
                }
            }

        appendLine("/// Default opacity implementation")
        appendLine("public struct LemonadeOpacityTokens: LemonadeOpacity {")
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, _) ->
                if (groupName != null) {
                    appendLine("    public let ${groupName.replaceFirstChar { char -> char.lowercase() }}: ${groupName}Opacity = ${groupName}OpacityImpl()")
                }
            }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

main()
