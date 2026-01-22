#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File
import kotlin.collections.sortedBy

private data class OpacityResource(
    val opacityValue: Double,
)

fun main() {
    val inputFile = File("tokens/opacity.json")
    val outputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")
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
            scriptFilePath = "scripts/kmp-opacity-token-converter.main.kts",
            opacitiesResource = opacityResources,
        )
        val outputTokenFile = File(outputDir, "LemonadeOpacity.kt")
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
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.runtime.Stable")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Opacity tokens.")
        appendLine(" * Provides a consistent and scalable way to manage opacity across interfaces.")
        appendLine(" * These values can be used for disabled components and general transparency handling.")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        append(generateOpacityInterfaceCode(opacitiesResource = opacitiesResource))
        appendLine()
        append(generateOpacityValuesCode(opacitiesResource = opacitiesResource))
    }
}

private fun generateOpacityInterfaceCode(
    opacitiesResource: List<ResourceData<OpacityResource>>,
): String {
    return buildString {
        appendLine("public interface LemonadeOpacity {")
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, _) ->
                if (groupName != null) {
                    appendLine("    public val ${groupName.replaceFirstChar { char -> char.lowercase() }}: $groupName")
                }
            }
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, groupOpacities) ->
                if (groupName != null) {
                    appendLine("    public interface $groupName {")
                    groupOpacities.forEach { opacity ->
                        appendLine("        public val ${opacity.name}: Float")
                    }
                    appendLine("    }")
                }
            }
        appendLine("}")
    }
}

private fun generateOpacityValuesCode(
    opacitiesResource: List<ResourceData<OpacityResource>>,
): String {
    return buildString {
        appendLine("@Stable")
        appendLine("internal class InternalLemonadeOpacityTokens: LemonadeOpacity {")
        opacitiesResource
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, groupOpacities) ->
                if (groupName != null) {
                    appendLine("    override val ${groupName.replaceFirstChar { char -> char.lowercase() }}: LemonadeOpacity.$groupName = object: LemonadeOpacity.$groupName {")
                    groupOpacities.forEach { opacity ->
                        appendLine("        override val ${opacity.name}: Float = ${opacity.value.opacityValue / 100f}f")
                    }
                    appendLine("    }")
                }
            }
        appendLine("}")
    }
}

main()
