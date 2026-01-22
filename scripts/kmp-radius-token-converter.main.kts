#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

data class RadiusResource(
    val radiusValue: Int,
)

fun main() {
    val radiusTokensFile = File("tokens/radius.json")
    val definitionOutputDir = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core")
    val implementationOutputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!implementationOutputDir.exists()) {
            implementationOutputDir.mkdirs()
        }

        if (!definitionOutputDir.exists()) {
            definitionOutputDir.mkdirs()
        }

        if (!radiusTokensFile.exists() || !radiusTokensFile.isFile) {
            error(message = "File $radiusTokensFile does not exist in system")
        }
        val radiusResources = readFileResourceFile(
            file = radiusTokensFile,
            resourceMap = { jsonObject ->
                RadiusResource(
                    radiusValue = jsonObject.getInt("resolvedValue"),
                )
            },
        ).sortedBy { it.value.radiusValue }
        println("✓ Loaded radius resources")

        val definitionCode = buildRadiusDefinitionCode(
            scriptFilePath = "scripts/kmp-radius-token-converter.main.kts",
            resources = radiusResources,
        )
        val definitionOutputFile = File(definitionOutputDir, "LemonadeRadius.kt")
        definitionOutputFile.writeText(definitionCode)
        println("✓ Radius definition file created")

        val implementationCode = buildRadiusImplementationCode(
            scriptFilePath = "scripts/kmp-radius-token-converter.main.kts",
            resources = radiusResources,
        )
        val implementationOutputFile = File(implementationOutputDir, "LemonadeRadiusExtension.kt")
        implementationOutputFile.writeText(implementationCode)
        println("✓ Radius implementation file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${radiusTokensFile.name}: ${error.message}")
    }
}

private fun buildRadiusDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<RadiusResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade.core")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Radius tokens.")
        appendLine(" * Sets a small, clear set of predefined radius values for UI elements to ensure")
        appendLine(" *  consistent, scalable rounding across the product.")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public enum class LemonadeRadius {")
        resources.forEach { resource ->
            appendLine("    ${resource.groupFullName},")
        }
        appendLine("}")
    }
}

private fun buildRadiusImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<RadiusResource>>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.foundation.shape.RoundedCornerShape")
        appendLine("import androidx.compose.ui.graphics.Shape")
        appendLine("import androidx.compose.ui.unit.Dp")
        appendLine("import androidx.compose.ui.unit.dp")
        appendLine("import com.teya.lemonade.core.LemonadeRadius")
        appendLine("import androidx.compose.runtime.Stable")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Radius tokens.")
        appendLine(" * Provides a consistent and scalable way to manage radius across interfaces.")
        appendLine(" * These values can be used for cards, chips and rounded layouts in general.")
        appendLine(" * ")
        appendLine(" * ")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public val LemonadeRadius.dp: Dp")
        appendLine("    get() = when (this) {")
        resources.forEach { resource ->
            appendLine("        LemonadeRadius.${resource.groupFullName} -> ${resource.value.radiusValue}.dp")
        }
        appendLine("    }")
        appendLine()
        appendLine("public interface LemonadeRadiusValues {")
        resources.forEach { resource ->
            appendLine("    public val ${resource.name}: Dp")
        }
        appendLine("}")
        appendLine()
        appendLine("public interface LemonadeShapes {")
        resources.forEach { resource ->
            appendLine("    public val ${resource.name}: Shape")
        }
        appendLine("}")
        appendLine()
        appendLine("@Stable")
        appendLine("internal data class InternalLemonadeRadiusValues(")
        resources.forEach { resource ->
            appendLine("    override val ${resource.name}: Dp = LemonadeRadius.${resource.groupFullName}.dp,")
        }
        appendLine("): LemonadeRadiusValues")
        appendLine()
        appendLine("@Stable")
        appendLine("internal data class InternalLemonadeShapes(")
        resources.forEach { resource ->
            appendLine("    override val ${resource.name}: Shape = RoundedCornerShape(size = LemonadeRadius.${resource.groupFullName}.dp),")
        }
        appendLine("): LemonadeShapes")
    }
}

main()
