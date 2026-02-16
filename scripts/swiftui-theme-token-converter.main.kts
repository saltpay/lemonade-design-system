#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import org.json.JSONObject
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

        // Read the JSON to extract mode keys
        val fileContent = colorTokensFile.readText()
        val json = JSONObject(fileContent)
        val modesObject = json.getJSONObject("modes")
        val modeKeys = modesObject.keys().asSequence().toList()
        
        // Generate code for each theme mode
        modeKeys.forEach { modeKey ->
            val modeName = modesObject.getString(modeKey)
            val themeName = when {
                modeName.equals("Light", ignoreCase = true) -> "LemonadeLightTheme"
                modeName.equals("Dark", ignoreCase = true) -> "LemonadeDarkTheme"
                else -> "Lemonade${modeName}Theme"
            }
            
            val themeResources = readFileResourceFileByMode(
                file = colorTokensFile,
                modeKey = modeKey,
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
            
            println("✓ Loaded $modeName theme resource")

            val classCode = buildThemeCode(
                themeName = themeName,
                themeDisplayName = modeName,
                scriptFilePath = "scripts/swiftui-theme-token-converter.main.kts",
                resources = themeResources,
            )
            println("✓ $modeName implementation generated")

            val classOutputFile = File(outputDir, "$themeName.swift")
            classOutputFile.writeText(classCode)
            println("✓ $themeName.swift created")
        }

        // Generate the protocol once using the first mode's resources
        val lightModeKey = modeKeys.first()
        val themeResources = readFileResourceFileByMode(
            file = colorTokensFile,
            modeKey = lightModeKey,
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

        val interfaceCode = buildThemeProtocolCode(
            scriptFilePath = "scripts/swiftui-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Protocol generated")

        val interfaceOutputFile = File(outputDir, "LemonadeSemanticColors.swift")
        interfaceOutputFile.writeText(interfaceCode)
        println("✓ Definition & Implementation files created")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${colorTokensFile.name}: ${error.message}")
        error.printStackTrace()
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
    themeDisplayName: String = "Light",
): String {
    val groupedThemeResources = resources.groupBy { it.groups.firstOrNull() }
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// $themeDisplayName theme implementation of semantic colors")
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
        appendLine("/// $themeDisplayName theme implementation")
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
