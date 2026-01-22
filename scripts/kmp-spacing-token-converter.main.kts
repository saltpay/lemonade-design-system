#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

data class SpacingResource(
    val spacingValue: Int,
)

fun main() {
    val spaceTokensFile = File("tokens/spacing.json")
    val definitionOutputDir = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core")
    val implementationOutputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!implementationOutputDir.exists()) {
            implementationOutputDir.mkdirs()
        }

        if (!definitionOutputDir.exists()) {
            definitionOutputDir.mkdirs()
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

        val definitionCode = buildSpacingDefinitionCode(
            scriptFilePath = "scripts/kmp-spacing-token-converter.main.kts",
            resources = spaceResources,
        )
        val definitionOutputFile = File(definitionOutputDir, "LemonadeSpaces.kt")
        definitionOutputFile.writeText(definitionCode)
        println("✓ Spaces definition file created")

        val implementationCode = buildSpacingImplementationCode(
            scriptFilePath = "scripts/kmp-spacing-token-converter.main.kts",
            resources = spaceResources,
        )
        val implementationOutputFile = File(implementationOutputDir, "LemonadeSpacesExtension.kt")
        implementationOutputFile.writeText(implementationCode)
        println("✓ Spaces implementation file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${spaceTokensFile.name}: ${error.message}")
    }
}

private fun buildSpacingDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<SpacingResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade.core")
        appendLine()
        appendLine("/**")
        appendLine(" * Core definition of the LemonadeSpaces tokens.")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public enum class LemonadeSpaces {")
        resources.forEach { resource ->
            appendLine("    ${resource.groupFullName},")
        }
        appendLine("}")
    }
}

private fun buildSpacingImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<SpacingResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.ui.unit.Dp")
        appendLine("import androidx.compose.ui.unit.dp")
        appendLine("import com.teya.lemonade.core.LemonadeSpaces")
        appendLine("import androidx.compose.runtime.Stable")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Space tokens.")
        appendLine(" * Provides a consistent and scalable way to manage spacing across interfaces.")
        appendLine(" * These values can be used for margins, paddings, and gaps to create balanced layouts.")
        appendLine(" * ")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public val LemonadeSpaces.dp: Dp")
        appendLine("    get() = when (this) {")
        resources.forEach { resource ->
            appendLine("        LemonadeSpaces.${resource.groupFullName} -> ${resource.value.spacingValue}.dp")
        }
        appendLine("    }")
        appendLine()
        appendLine("public interface LemonadeSpaceValues {")
        resources.forEach { resource ->
            appendLine("    public val ${resource.name}: Dp")
        }
        appendLine("}")
        appendLine()
        appendLine("@Stable")
        appendLine("internal data class InternalLemonadeSpaceValues(")
        resources.forEach { resource ->
            appendLine("    override val ${resource.name}: Dp = LemonadeSpaces.${resource.groupFullName}.dp,")
        }
        appendLine("): LemonadeSpaceValues")
    }
}

main()
