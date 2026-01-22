#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import java.io.File

data class ThemeResourceData(
    val valueGroup: String,
    val valueName: String,
)

fun main() {
    val colorTokensFile = File("tokens/theme-colors.json")
    val outputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!colorTokensFile.exists() || !colorTokensFile.isFile) {
            error(message = "File $colorTokensFile does not exist in system")
        }

        val themeResources = readFileResourceFile(
            file = colorTokensFile,
            resourceMap = { jsonObject ->
                val aliasName = jsonObject.optString("aliasName")
                val groups = aliasName?.sanitizedGroups().orEmpty()
                if (!aliasName.isNullOrBlank() && groups.isNotEmpty()) {
                    ThemeResourceData(
                        valueName = aliasName.sanitizedValueName(),
                        valueGroup = if (groups.contains("Alpha")) {
                            "Alpha.${groups.first()}"
                        } else {
                            "Solid.${groups.first()}"
                        },
                    )
                } else {
                    null
                }
            },
        ).filterNull()
        println("✓ Loaded theme resource")

        val interfaceCode = buildThemeInterfaceCode(
            scriptFilePath = "scripts/kmp-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Interface generated")

        val classCode = buildThemeCode(
            fileName = "LemonadeLightTheme",
            scriptFilePath = "scripts/kmp-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Implementation generated")

        val interfaceOutputFile = File(outputDir, "LemonadeSemanticColors.kt")
        interfaceOutputFile.writeText(interfaceCode)
        val classOutputFile = File(outputDir, "LemonadeLightTheme.kt")
        classOutputFile.writeText(classCode)
        println("✓ Definition & Implementation files created")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${colorTokensFile.name}: ${error.message}")
    }
}

private fun buildThemeInterfaceCode(
    scriptFilePath: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    val groupedThemeResources = resources.groupBy { it.groups.firstOrNull() }
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.ui.graphics.Color")
        appendLine()
        appendLine("/**")
        appendLine(" * Semantic color tokens from Lemonade DS - Foundations")
        appendLine(" * Organized by usage categories: Background, Content, Border, and Interaction")
        appendLine(" * These tokens map to primitive colors and provide semantic meaning for UI elements")
        appendLine(" * See [Lemonade  semantic colors](https://www.figma.com/design/3DI1AqqkYgRJgYCjOXjbDO/Review-and-update-colors?node-id=97-4923)")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public interface LemonadeSemanticColors {")
        groupedThemeResources.keys.forEach { groupName ->
            if (groupName != null) {
                appendLine("    public val ${groupName.sanitizedValueName()}: ${groupName}Colors")
            }
        }
        groupedThemeResources.forEach { (groupName, resources) ->
            if (groupName != null) {
                append(
                    buildGroupInterfaceCode(
                        groupName = groupName,
                        resources = resources,
                    )
                )
            }
        }
        appendLine("}")
    }
}

private fun buildGroupInterfaceCode(
    groupName: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    return buildString {
        appendLine("    public interface ${groupName}Colors {")
        resources.forEach { resource ->
            appendLine("        public val ${resource.name}: Color")
        }
        appendLine("    }")
    }
}

private fun buildThemeCode(
    fileName: String,
    scriptFilePath: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    val groupedThemeResources = resources.groupBy { it.groups.firstOrNull() }
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import androidx.compose.runtime.Stable")
        appendLine()
        appendLine("/**")
        appendLine(" * Light theme implementation of semantic colors")
        appendLine(" * See [LemonadeSemanticColors] for details on the color structure.")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("@Stable")
        appendLine("@OptIn(InternalLemonadeApi::class)")
        appendLine("internal object $fileName : LemonadeSemanticColors {")
        groupedThemeResources.forEach { (groupName, resources) ->
            if (groupName != null) {
                append(
                    buildGroupClassCode(
                        groupName = groupName,
                        resources = resources,
                    )
                )
            }
        }
        appendLine("}")
    }
}

private fun buildGroupClassCode(
    groupName: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    return buildString {
        appendLine("    override val ${groupName.sanitizedValueName()} = object : LemonadeSemanticColors.${groupName}Colors {")
        resources.forEach { resource ->
            appendLine("        override val ${resource.name} = LemonadePrimitiveColors.${resource.value.valueGroup}.${resource.value.valueName}")
        }
        appendLine("    }")
    }
}

main()
