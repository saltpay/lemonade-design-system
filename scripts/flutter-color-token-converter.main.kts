#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

fun main() {
    val colorTokensFile = File("tokens/primitive-colors.json")
    val outputDir = File("flutter/lib/src/foundation")
    try {
        if (!colorTokensFile.exists() || !colorTokensFile.isFile) {
            error(message = "File $colorTokensFile does not exist in system.")
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        println("Reading colors from 'tokens/primitive-colors.json' file.")
        val colors = readFileResourceFile(
            file = colorTokensFile,
            resourceMap = { jsonObject ->
                val resolvedValue = jsonObject.getJSONObject("resolvedValue")
                Color(
                    r = resolvedValue.getDouble("r"),
                    g = resolvedValue.getDouble("g"),
                    b = resolvedValue.getDouble("b"),
                    a = resolvedValue.getDouble("a"),
                )
            }
        )
        println("Colors read and parsed.")
        val generatedCode = generateColorCodes(
            scriptFilePath = "scripts/flutter-color-token-converter.main.kts",
            resources = colors,
        )
        val outputFile = File(outputDir, "primitive_colors.dart")
        outputFile.writeText(generatedCode)

        // Format the file using dart format
        ProcessBuilder("dart", "format", outputFile.absolutePath).start().waitFor()

        println("✓ Converted ${colorTokensFile.name} -> ${outputFile.name}!")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${colorTokensFile.name}: ${error.message}")
    }
}

private fun generateColorCodes(
    scriptFilePath: String,
    resources: List<ResourceData<Color>>,
): String {
    val (alphaGroup, solidGroup) = resources.partition { resource -> resource.groups.contains("Alpha") }
    return buildString {
        append(defaultAutoGenerationMessage("Primitive colors"))
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Primitive colors from Lemonade Design System Foundations")
        appendLine("/// Supports both solid and alpha color variants.")
        appendLine("/// ")
        appendLine("/// This API is internal to Lemonade using it directly in general is a mistake, ")
        appendLine("/// accessing the colors directly is not recommended, use the LemonadeTheme ")
        appendLine("/// instead.")
        appendLine("@internal")
        appendLine("abstract final class LemonadePrimitiveColors {")
        appendLine("  /// Solid colors with full opacity")
        appendLine("  static final solid = _Solid();")
        appendLine()
        appendLine("  /// Alpha colors with transparency")
        appendLine("  static final alpha = _Alpha();")
        appendLine("}")
        appendLine()
        
        append(buildPrimitiveColorGroup("Solid", solidGroup))
        appendLine()
        append(buildPrimitiveColorGroup("Alpha", alphaGroup))
    }
}

private fun buildPrimitiveColorGroup(
    label: String,
    resources: List<ResourceData<Color>>,
): String {
    val groups = resources.groupBy { resource -> resource.groupFullName }
    
    return buildString {
        appendLine("class _$label {")
        
        groups.forEach { (groupName, _) ->
            if (groupName != null) {
                val className = if (label == "Alpha") groupName.removeSuffix("Alpha") else groupName
                appendLine("  /// $className color palette")
                appendLine("  final _${className}$label ${className.camelCase()} = _${className}$label();")
                if (groupName != groups.keys.last()) {
                    appendLine()
                }
            }
        }
        
        appendLine("}")
        appendLine()
        
        groups.forEach { (groupName, groupResources) ->
            if (groupName != null) {
                val className = if (label == "Alpha") groupName.removeSuffix("Alpha") else groupName
                append(buildColorGroupCode(className, groupResources, label))
                if (groupName != groups.keys.last()) {
                    appendLine()
                }
            }
        }
    }
}

private fun buildColorGroupCode(
    groupName: String,
    resources: List<ResourceData<Color>>,
    type: String,
): String {
    return buildString {
        appendLine("class _$groupName$type {")
        
        resources.forEach { resource ->
            val colorName = if (type == "Alpha") {
                resource.name.replace("alpha", groupName.camelCase())
            } else {
                resource.name
            }
            append(colorCode(colorName, resource.value))
            if (resource != resources.last()) {
                appendLine()
            }
        }
        
        appendLine("}")
    }
}

private fun colorCode(
    colorName: String,
    color: Color,
): String {
    return buildString {
        appendLine("  final Color ${colorName} = const Color(${color.toHex()});")
    }
}

private val Double.formattedFloat
    get() = String.format("%.7f", this)
        .replace(
            oldChar = ',',
            newChar = '.'
        )

main()
