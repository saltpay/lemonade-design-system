#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

data class TypographyTokenValue(
    val floatValue: Double?,
    val stringValue: String?,
)

fun main() {
    val typographyTokensFile = File("tokens/typography.json")
    val definitionOutputDir = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core")
    val implementationOutputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!implementationOutputDir.exists()) implementationOutputDir.mkdirs()
        if (!definitionOutputDir.exists()) definitionOutputDir.mkdirs()

        if (!typographyTokensFile.exists() || !typographyTokensFile.isFile) {
            error(message = "File $typographyTokensFile does not exist in system")
        }

        val allResources = readFileResourceFile(
            file = typographyTokensFile,
            resourceMap = { jsonObject ->
                val resolvedValue = jsonObject.get("resolvedValue")
                when (resolvedValue) {
                    is Number -> TypographyTokenValue(floatValue = resolvedValue.toDouble(), stringValue = null)
                    is String -> TypographyTokenValue(floatValue = null, stringValue = resolvedValue)
                    else -> null
                }
            },
        ).filterNull()
        println("✓ Loaded typography resources")

        val fontSizeResources = allResources
            .filter { it.groups.firstOrNull() == "FontSize" }
            .sortedBy { it.value.floatValue }
        val fontWeightResources = allResources
            .filter { it.groups.firstOrNull() == "FontWeight" }
        val lineHeightResources = allResources
            .filter { it.groups.firstOrNull() == "LineHeight" }
            .sortedBy { it.value.floatValue }
        val scriptFilePath = "scripts/kmp-typography-token-converter.main.kts"

        File(definitionOutputDir, "LemonadeFontSizes.kt").writeText(
            buildFontSizesDefinitionCode(scriptFilePath, fontSizeResources)
        )
        println("✓ LemonadeFontSizes.kt created")

        File(implementationOutputDir, "LemonadeFontSizesExtension.kt").writeText(
            buildFontSizesImplementationCode(scriptFilePath, fontSizeResources)
        )
        println("✓ LemonadeFontSizesExtension.kt created")

        File(definitionOutputDir, "LemonadeFontWeights.kt").writeText(
            buildFontWeightsDefinitionCode(scriptFilePath, fontWeightResources)
        )
        println("✓ LemonadeFontWeights.kt created")

        File(implementationOutputDir, "LemonadeFontWeightsExtension.kt").writeText(
            buildFontWeightsImplementationCode(scriptFilePath, fontWeightResources)
        )
        println("✓ LemonadeFontWeightsExtension.kt created")

        File(definitionOutputDir, "LemonadeLineHeights.kt").writeText(
            buildLineHeightsDefinitionCode(scriptFilePath, lineHeightResources)
        )
        println("✓ LemonadeLineHeights.kt created")

        File(implementationOutputDir, "LemonadeLineHeightsExtension.kt").writeText(
            buildLineHeightsImplementationCode(scriptFilePath, lineHeightResources)
        )
        println("✓ LemonadeLineHeightsExtension.kt created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${typographyTokensFile.name}: ${error.message}")
    }
}

private fun buildFontSizesDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("package com.teya.lemonade.core")
    appendLine()
    appendLine("/**")
    appendLine(" * Core definition of the LemonadeFontSizes tokens.")
    appendLine(" *")
    append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
    appendLine(" */")
    appendLine("public enum class LemonadeFontSizes {")
    // Typography tokens have path separators (e.g. "font-size/font-size-1200"), so
    // groupFullName collapses all tokens in the same group to the same value ("FontSize").
    // Use name.replaceFirstChar instead to get unique enum entries (e.g. "FontSize1200").
    resources.forEach { resource ->
        appendLine("    ${resource.name.replaceFirstChar { it.uppercase() }},")
    }
    appendLine("}")
}

private fun buildFontSizesImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("package com.teya.lemonade")
    appendLine()
    appendLine("import androidx.compose.runtime.Stable")
    appendLine("import androidx.compose.ui.unit.TextUnit")
    appendLine("import androidx.compose.ui.unit.sp")
    appendLine("import com.teya.lemonade.core.LemonadeFontSizes")
    appendLine()
    appendLine("/**")
    appendLine(" * Lemonade Design System Font Size tokens.")
    appendLine(" * Provides font sizes as TextUnit (sp) values.")
    appendLine(" *")
    append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
    appendLine(" */")
    appendLine("public val LemonadeFontSizes.sp: TextUnit")
    appendLine("    get() = when (this) {")
    resources.forEach { resource ->
        val spValue = resource.value.floatValue?.toInt() ?: 0
        appendLine("        LemonadeFontSizes.${resource.name.replaceFirstChar { it.uppercase() }} -> ${spValue}.sp")
    }
    appendLine("    }")
    appendLine()
    appendLine("public interface LemonadeFontSizeValues {")
    resources.forEach { resource ->
        appendLine("    public val ${resource.name}: TextUnit")
    }
    appendLine("}")
    appendLine()
    appendLine("@Stable")
    appendLine("internal data class InternalLemonadeFontSizeValues(")
    resources.forEach { resource ->
        appendLine("    override val ${resource.name}: TextUnit = LemonadeFontSizes.${resource.name.replaceFirstChar { it.uppercase() }}.sp,")
    }
    appendLine(") : LemonadeFontSizeValues")
}

private fun buildFontWeightsDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("package com.teya.lemonade.core")
    appendLine()
    appendLine("/**")
    appendLine(" * Core definition of the LemonadeFontWeights tokens.")
    appendLine(" *")
    append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
    appendLine(" */")
    appendLine("public enum class LemonadeFontWeights {")
    resources.forEach { resource ->
        appendLine("    ${resource.name.replaceFirstChar { it.uppercase() }},") // see LemonadeFontSizes for why name is used instead of groupFullName
    }
    appendLine("}")
}

private fun buildFontWeightsImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("package com.teya.lemonade")
    appendLine()
    appendLine("import androidx.compose.runtime.Stable")
    appendLine("import androidx.compose.ui.text.font.FontWeight")
    appendLine("import com.teya.lemonade.core.LemonadeFontWeights")
    appendLine()
    appendLine("/**")
    appendLine(" * Lemonade Design System Font Weight tokens.")
    appendLine(" *")
    append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
    appendLine(" */")
    appendLine("public val LemonadeFontWeights.fontWeight: FontWeight")
    appendLine("    get() = when (this) {")
    resources.forEach { resource ->
        val fontWeightExpression = when (resource.value.stringValue) {
            "Bold" -> "FontWeight.Bold"
            "SemiBold" -> "FontWeight.SemiBold"
            "Medium" -> "FontWeight.Medium"
            "Regular" -> "FontWeight.Normal"
            else -> "FontWeight.Normal"
        }
        appendLine("        LemonadeFontWeights.${resource.name.replaceFirstChar { it.uppercase() }} -> $fontWeightExpression")
    }
    appendLine("    }")
    appendLine()
    appendLine("public interface LemonadeFontWeightValues {")
    resources.forEach { resource ->
        appendLine("    public val ${resource.name}: FontWeight")
    }
    appendLine("}")
    appendLine()
    appendLine("@Stable")
    appendLine("internal data class InternalLemonadeFontWeightValues(")
    resources.forEach { resource ->
        appendLine("    override val ${resource.name}: FontWeight = LemonadeFontWeights.${resource.name.replaceFirstChar { it.uppercase() }}.fontWeight,")
    }
    appendLine(") : LemonadeFontWeightValues")
}

private fun buildLineHeightsDefinitionCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("package com.teya.lemonade.core")
    appendLine()
    appendLine("/**")
    appendLine(" * Core definition of the LemonadeLineHeights tokens.")
    appendLine(" *")
    append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
    appendLine(" */")
    appendLine("public enum class LemonadeLineHeights {")
    resources.forEach { resource ->
        appendLine("    ${resource.name.replaceFirstChar { it.uppercase() }},") // see LemonadeFontSizes for why name is used instead of groupFullName
    }
    appendLine("}")
}

private fun buildLineHeightsImplementationCode(
    scriptFilePath: String,
    resources: List<ResourceData<TypographyTokenValue>>,
): String = buildString {
    appendLine("package com.teya.lemonade")
    appendLine()
    appendLine("import androidx.compose.runtime.Stable")
    appendLine("import androidx.compose.ui.unit.TextUnit")
    appendLine("import androidx.compose.ui.unit.sp")
    appendLine("import com.teya.lemonade.core.LemonadeLineHeights")
    appendLine()
    appendLine("/**")
    appendLine(" * Lemonade Design System Line Height tokens.")
    appendLine(" *")
    append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
    appendLine(" */")
    appendLine("public val LemonadeLineHeights.sp: TextUnit")
    appendLine("    get() = when (this) {")
    resources.forEach { resource ->
        val spValue = resource.value.floatValue?.toInt() ?: 0
        appendLine("        LemonadeLineHeights.${resource.name.replaceFirstChar { it.uppercase() }} -> ${spValue}.sp")
    }
    appendLine("    }")
    appendLine()
    appendLine("public interface LemonadeLineHeightValues {")
    resources.forEach { resource ->
        appendLine("    public val ${resource.name}: TextUnit")
    }
    appendLine("}")
    appendLine()
    appendLine("@Stable")
    appendLine("internal data class InternalLemonadeLineHeightValues(")
    resources.forEach { resource ->
        appendLine("    override val ${resource.name}: TextUnit = LemonadeLineHeights.${resource.name.replaceFirstChar { it.uppercase() }}.sp,")
    }
    appendLine(") : LemonadeLineHeightValues")
}

main()
