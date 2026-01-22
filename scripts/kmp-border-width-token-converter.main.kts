#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

data class BorderWidthResource(
    val borderWidthValue: Int,
)

fun main() {
    val borderWidthTokensFile = File("tokens/border-width.json")
    val implementationOutputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!implementationOutputDir.exists()) {
            implementationOutputDir.mkdirs()
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

        val implementationCode = buildFullImplementationFile(
            scriptFilePath = "scripts/kmp-border-width-token-converter.main.kts",
            resources = borderWidthResources,
        )
        val implementationOutputFile = File(implementationOutputDir, "LemonadeBorderWidthExtension.kt")
        implementationOutputFile.writeText(implementationCode)
        println("✓ Border width implementation file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${borderWidthTokensFile.name}: ${error.message}")
    }
}

private fun buildFullImplementationFile(
    scriptFilePath: String,
    resources: List<ResourceData<BorderWidthResource>>,
): String {
    val definitionCode = buildBorderWidthDefinitionCode(
        scriptFilePath = scriptFilePath,
        resources = resources,
    )
    val implementationCode = buildBorderWidthImplementationCode(
        resources = resources,
    )

    return buildString {
        appendLine(definitionCode)
        append(implementationCode)
    }
}

private fun buildBorderWidthDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<BorderWidthResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.ui.unit.Dp")
        appendLine("import androidx.compose.ui.unit.dp")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design Border Width tokens.")
        appendLine(" * Sets a small, clear set of predefined border width values for UI elements to ensure")
        appendLine(" *  consistent, scalable rounding across the product.")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public interface LemonadeBorderWidth {")
        val groupedResources = resources.groupBy { resource -> resource.groupFullName }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    public val ${groupName?.sanitizedValueName()}: $groupName")
        }
        groupedResources.forEach { (groupName, groupResources) ->
            appendLine("    public interface $groupName {")
            groupResources.forEach { resource ->
                appendLine("        public val ${resource.name}: Dp")
            }
            appendLine("    }")
        }
        appendLine("}")
    }
}

private fun buildBorderWidthImplementationCode(
    resources: List<ResourceData<BorderWidthResource>>,
): String {
    return buildString {
        appendLine("internal class InternalLemonadeBorderWidth: LemonadeBorderWidth {")
        resources
            .groupBy { resource -> resource.groupFullName }
            .forEach { (groupName, groupResources) ->
                if (groupName != null) {
                    appendLine("    override val ${groupName.sanitizedValueName()} = object: LemonadeBorderWidth.$groupName {")
                    groupResources.forEach { resource ->
                        appendLine("        override val ${resource.name}: Dp = ${resource.value.borderWidthValue}.dp")
                    }
                    appendLine("    }")
                }
            }
        appendLine("}")
    }
}

main()
