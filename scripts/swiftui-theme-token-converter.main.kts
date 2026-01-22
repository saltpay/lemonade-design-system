#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class ThemeResourceData(
    val valueGroup: String,
    val valueName: String,
)

fun main() {
    val colorTokensFile = File("tokens/theme-colors.json")
    val outputDir = File("swiftui/Sources/Lemonade")

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
                        valueName = aliasName.sanitizedSwiftValueName(),
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

        val interfaceCode = buildThemeProtocolCode(
            scriptFilePath = "scripts/swiftui-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Protocol generated")

        val classCode = buildThemeCode(
            themeName = "LemonadeLightTheme",
            scriptFilePath = "scripts/swiftui-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Implementation generated")

        val interfaceOutputFile = File(outputDir, "LemonadeSemanticColors.swift")
        interfaceOutputFile.writeText(interfaceCode)
        val classOutputFile = File(outputDir, "LemonadeLightTheme.swift")
        classOutputFile.writeText(classCode)
        println("✓ Definition & Implementation files created")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${colorTokensFile.name}: ${error.message}")
    }
}

private fun buildThemeProtocolCode(
    scriptFilePath: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    val groupedThemeResources = resources.groupBy { it.groups.firstOrNull() }
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Semantic color tokens from Lemonade DS - Foundations")
        appendLine("/// Organized by usage categories: Background, Content, Border, and Interaction")
        appendLine("/// These tokens map to primitive colors and provide semantic meaning for UI elements")
        appendLine("/// See [Lemonade semantic colors](https://www.figma.com/design/3DI1AqqkYgRJgYCjOXjbDO/Review-and-update-colors?node-id=97-4923)")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        // Generate color group protocols
        groupedThemeResources.forEach { (groupName, resources) ->
            if (groupName != null) {
                append(
                    buildGroupProtocolCode(
                        groupName = groupName,
                        resources = resources,
                    )
                )
                appendLine()
            }
        }
        appendLine("/// Protocol defining semantic color categories")
        appendLine("public protocol LemonadeSemanticColors {")
        groupedThemeResources.keys.forEach { groupName ->
            if (groupName != null) {
                appendLine("    var ${groupName.sanitizedSwiftValueName()}: ${groupName}Colors { get }")
            }
        }
        appendLine("}")
    }
}

private fun buildGroupProtocolCode(
    groupName: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    return buildString {
        appendLine("/// ${groupName} color definitions")
        appendLine("public protocol ${groupName}Colors {")
        resources.forEach { resource ->
            appendLine("    var ${resource.name}: Color { get }")
        }
        appendLine("}")
    }
}

private fun buildThemeCode(
    themeName: String,
    scriptFilePath: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    val groupedThemeResources = resources.groupBy { it.groups.firstOrNull() }
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Light theme implementation of semantic colors")
        appendLine("/// See LemonadeSemanticColors for details on the color structure.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        // Generate group structs
        groupedThemeResources.forEach { (groupName, resources) ->
            if (groupName != null) {
                append(
                    buildGroupStructCode(
                        groupName = groupName,
                        resources = resources,
                    )
                )
                appendLine()
            }
        }
        appendLine("/// Light theme implementation")
        appendLine("public struct $themeName: LemonadeSemanticColors {")
        groupedThemeResources.forEach { (groupName, _) ->
            if (groupName != null) {
                appendLine("    public let ${groupName.sanitizedSwiftValueName()}: ${groupName}Colors = ${groupName}ColorsImpl()")
            }
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

private fun buildGroupStructCode(
    groupName: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    return buildString {
        appendLine("internal struct ${groupName}ColorsImpl: ${groupName}Colors {")
        resources.forEach { resource ->
            appendLine("    var ${resource.name}: Color { LemonadePrimitiveColors.${resource.value.valueGroup}.${resource.value.valueName} }")
        }
        appendLine("}")
    }
}

main()
