#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

data class DimensionResource(
    val dimensionValue: Int,
)

fun main() {
    val sizeTokensFile = File("tokens/size.json")
    val definitionOutputDir = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core")
    val implementationOutputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!implementationOutputDir.exists()) {
            implementationOutputDir.mkdirs()
        }

        if (!definitionOutputDir.exists()) {
            definitionOutputDir.mkdirs()
        }

        if (!sizeTokensFile.exists() || !sizeTokensFile.isFile) {
            error(message = "File $sizeTokensFile does not exist in system")
        }
        val sizeResources = readFileResourceFile(
            file = sizeTokensFile,
            resourceMap = { jsonObject ->
                DimensionResource(
                    dimensionValue = jsonObject.getInt("resolvedValue"),
                )
            },
        ).sortedBy { it.value.dimensionValue }
        println("✓ Loaded size resources")

        val definitionCode = buildSpacingDefinitionCode(
            scriptFilePath = "scripts/kmp-dimension-token-converter.main.kts",
            resources = sizeResources,
        )
        val definitionOutputFile = File(definitionOutputDir, "LemonadeSizes.kt")
        definitionOutputFile.writeText(definitionCode)
        println("✓ Sizes definition file created")

        val implementationCode = buildSpacingImplementationCode(
            scriptFilePath = "scripts/kmp-dimension-token-converter.main.kts",
            resources = sizeResources,
        )
        val implementationOutputFile = File(implementationOutputDir, "LemonadeSizesExtension.kt")
        implementationOutputFile.writeText(implementationCode)
        println("✓ Sizes implementation file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${sizeTokensFile.name}: ${error.message}")
    }
}

private fun buildSpacingDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<DimensionResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade.core")
        appendLine()
        appendLine("/**")
        appendLine(" * Core definition of the LemonadeSizes tokens.")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public enum class LemonadeSizes {")
        resources.forEach { resource ->
            appendLine("    ${resource.groupFullName},")
        }
        appendLine("}")
    }
}

private fun buildSpacingImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<DimensionResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.ui.unit.Dp")
        appendLine("import androidx.compose.ui.unit.dp")
        appendLine("import com.teya.lemonade.core.LemonadeSizes")
        appendLine("import androidx.compose.runtime.Stable")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Size tokens.")
        appendLine(" * Provides a consistent and scalable way to manage spacing across interfaces.")
        appendLine(" * These values can be used for margins, paddings, and gaps to create balanced layouts.")
        appendLine(" * ")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public val LemonadeSizes.dp: Dp")
        appendLine("    get() = when (this) {")
        resources.forEach { resource ->
            appendLine("        LemonadeSizes.${resource.groupFullName} -> ${resource.value.dimensionValue}.dp")
        }
        appendLine("    }")
        appendLine()
        appendLine("public interface LemonadeSizeValues {")
        resources.forEach { resource ->
            appendLine("    public val ${resource.name}: Dp")
        }
        appendLine("}")
        appendLine()
        appendLine("@Stable")
        appendLine("internal data class InternalLemonadeSizeValues(")
        resources.forEach { resource ->
            appendLine("    override val ${resource.name}: Dp = LemonadeSizes.${resource.groupFullName}.dp,")
        }
        appendLine("): LemonadeSizeValues")
    }
}

main()
