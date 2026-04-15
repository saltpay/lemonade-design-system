#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

private data class RadiusResource(
    val radiusValue: Int,
)

fun main() {
    val radiusTokensFile = File("tokens/radius.json")
    val definitionOutputDir = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core")
    val implementationOutputDir = File("kmp/tokens/src/commonMain/kotlin/com/teya/lemonade")

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
    val primitiveResources = resources.filter { it.groups.isEmpty() }

    return buildString {
        appendLine("package com.teya.lemonade.core")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Radius tokens.")
        appendLine(" * Sets a small, clear set of predefined radius values for UI elements to ensure")
        appendLine(" *  consistent, scalable rounding across the product.")
        appendLine(" *")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public enum class LemonadeRadius {")
        primitiveResources.forEach { resource ->
            appendLine("    ${resource.groupFullName},")
        }
        appendLine("}")
    }
}

private fun buildRadiusImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<RadiusResource>>,
): String {
    val primitiveResources = resources.filter { it.groups.isEmpty() }
    val groupedResources = resources
        .filter { it.groups.isNotEmpty() }
        .filter { it.groupFullName != null }
        .groupBy { it.groupFullName!! }

    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.foundation.shape.RoundedCornerShape")
        appendLine("import androidx.compose.runtime.Stable")
        appendLine("import androidx.compose.ui.graphics.Shape")
        appendLine("import androidx.compose.ui.unit.Dp")
        appendLine("import androidx.compose.ui.unit.dp")
        appendLine("import com.teya.lemonade.core.LemonadeRadius")
        appendLine()
        appendLine("/**")
        appendLine(" * Lemonade Design System Radius tokens.")
        appendLine(" * Provides a consistent and scalable way to manage radius across interfaces.")
        appendLine(" * These values can be used for cards, chips and rounded layouts in general.")
        appendLine(" *")
        appendLine(" *")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")

        // dp extension: primitives only
        appendLine("public val LemonadeRadius.dp: Dp")
        appendLine("    get() = when (this) {")
        primitiveResources.forEach { resource ->
            appendLine("        LemonadeRadius.${resource.groupFullName} -> ${resource.value.radiusValue}.dp")
        }
        appendLine("    }")
        appendLine()

        // LemonadeRadiusValues: primitives flat + semantic nested interface
        appendLine("public interface LemonadeRadiusValues {")
        primitiveResources.forEach { resource ->
            appendLine("    public val ${resource.name}: Dp")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    public val ${groupName.replaceFirstChar { it.lowercase() }}: $groupName")
        }
        groupedResources.forEach { (groupName, groupTokens) ->
            appendLine("    public interface $groupName {")
            groupTokens.forEach { token ->
                appendLine("        public val ${token.name}: Dp")
            }
            appendLine("    }")
        }
        appendLine("}")
        appendLine()

        // LemonadeShapes: same structure
        appendLine("public interface LemonadeShapes {")
        primitiveResources.forEach { resource ->
            appendLine("    public val ${resource.name}: Shape")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    public val ${groupName.replaceFirstChar { it.lowercase() }}: $groupName")
        }
        groupedResources.forEach { (groupName, groupTokens) ->
            appendLine("    public interface $groupName {")
            groupTokens.forEach { token ->
                appendLine("        public val ${token.name}: Shape")
            }
            appendLine("    }")
        }
        appendLine("}")
        appendLine()

        // Internal impl for each semantic group
        groupedResources.forEach { (groupName, groupTokens) ->
            appendLine("@Stable")
            appendLine("internal data class InternalLemonade${groupName}RadiusValues(")
            groupTokens.forEach { token ->
                appendLine("    override val ${token.name}: Dp = ${token.value.radiusValue}.dp,")
            }
            appendLine("): LemonadeRadiusValues.$groupName")
            appendLine()
            appendLine("@Stable")
            appendLine("internal data class InternalLemonade${groupName}Shapes(")
            groupTokens.forEach { token ->
                appendLine("    override val ${token.name}: Shape = RoundedCornerShape(size = ${token.value.radiusValue}.dp),")
            }
            appendLine("): LemonadeShapes.$groupName")
            appendLine()
        }

        // InternalLemonadeRadiusValues
        appendLine("@Stable")
        appendLine("internal data class InternalLemonadeRadiusValues(")
        primitiveResources.forEach { resource ->
            appendLine("    override val ${resource.name}: Dp = LemonadeRadius.${resource.groupFullName}.dp,")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    override val ${groupName.replaceFirstChar { it.lowercase() }}: LemonadeRadiusValues.$groupName = InternalLemonade${groupName}RadiusValues(),")
        }
        appendLine("): LemonadeRadiusValues")
        appendLine()

        // InternalLemonadeShapes
        appendLine("@Stable")
        appendLine("internal data class InternalLemonadeShapes(")
        primitiveResources.forEach { resource ->
            appendLine("    override val ${resource.name}: Shape = RoundedCornerShape(size = LemonadeRadius.${resource.groupFullName}.dp),")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    override val ${groupName.replaceFirstChar { it.lowercase() }}: LemonadeShapes.$groupName = InternalLemonade${groupName}Shapes(),")
        }
        appendLine("): LemonadeShapes")
    }
}

main()
