#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

data class ThemeResourceData(
    val valueGroup: String,
    val valueName: String,
)

fun main() {
    val colorTokensFile = File("tokens/theme-colors.json")
    val outputDir = File("flutter/lib/src")

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
                    val valueName = if (groups.contains("Alpha")) {
                        val parts = aliasName.split("/")
                        if (parts.size >= 3) {
                            "${parts[0].sanitizedValueName()}${parts[2]}"
                        } else {
                            aliasName.sanitizedValueName()
                        }
                    } else {
                        aliasName.sanitizedValueName()
                    }
                    
                    ThemeResourceData(
                        valueName = valueName,
                        valueGroup = if (groups.contains("Alpha")) {
                            "alpha.${groups.first().camelCase()}"
                        } else {
                            "solid.${groups.first().camelCase()}"
                        },
                    )
                } else {
                    null
                }
            },
        ).filterNull()
        println("✓ Loaded theme resource")

        val interfaceCode = buildThemeInterfaceCode(
            scriptFilePath = "scripts/flutter-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Interface generated")

        val classCode = buildThemeCode(
            fileName = "LemonadeLightColors",
            scriptFilePath = "scripts/flutter-theme-token-converter.main.kts",
            resources = themeResources,
        )
        println("✓ Implementation generated")

        val interfaceOutputFile = File(outputDir, "foundation/semantic_colors.dart")
        interfaceOutputFile.writeText(interfaceCode)
        val classOutputFile = File(outputDir, "theme/colors.dart")
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
        append(defaultAutoGenerationMessage("Semantic colors"))
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Semantic color tokens from Lemonade Design System Foundations")
        appendLine("/// Organized by usage categories: Background, Content, Border, and Interaction")
        appendLine("/// These tokens map to primitive colors and provide semantic meaning for UI elements")
        appendLine("interface class LemonadeSemanticColors {")
        
        groupedThemeResources.keys.forEach { groupName ->
            if (groupName != null) {
                val propertyName = groupName.sanitizedValueName().camelCase()
                appendLine("  /// ${groupName.capitalize()} state colors for UI elements")
                appendLine("  final Lemonade${groupName.capitalize()}Colors $propertyName;")
                appendLine()
            }
        }

        appendLine("  const LemonadeSemanticColors({")
        groupedThemeResources.keys.forEach { groupName ->
            if (groupName != null) {
                val propertyName = groupName.sanitizedValueName().camelCase()
                appendLine("    required this.$propertyName,")
            }
        }
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [LemonadeSemanticColors] objects.")
        appendLine("  factory LemonadeSemanticColors.lerp(")
        appendLine("    LemonadeSemanticColors a,")
        appendLine("    LemonadeSemanticColors b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine("    return LemonadeSemanticColors(")
        groupedThemeResources.keys.forEach { groupName ->
            if (groupName != null) {
                val propertyName = groupName.sanitizedValueName().camelCase()
                appendLine("      $propertyName: Lemonade${groupName.capitalize()}Colors.lerp(a.$propertyName, b.$propertyName, t),")
            }
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()

        appendLine("  /// Obtains the instance of [LemonadeSemanticColors] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeSemanticColors of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.colors;")
        appendLine("  }")
        appendLine("}")
        appendLine()

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
    }
}

private fun buildGroupInterfaceCode(
    groupName: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    return buildString {
        appendLine("/// ${groupName.capitalize()} state colors for UI elements")
        appendLine("interface class Lemonade${groupName.capitalize()}Colors {")
        
        resources.forEach { resource ->
            appendLine("  final Color ${resource.name};")
        }
        appendLine()

        appendLine("  const Lemonade${groupName.capitalize()}Colors({")
        resources.forEach { resource ->
            appendLine("    required this.${resource.name},")
        }
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [Lemonade${groupName.capitalize()}Colors] objects.")
        appendLine("  factory Lemonade${groupName.capitalize()}Colors.lerp(")
        appendLine("    Lemonade${groupName.capitalize()}Colors a,")
        appendLine("    Lemonade${groupName.capitalize()}Colors b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine("    return Lemonade${groupName.capitalize()}Colors(")
        resources.forEach { resource ->
            appendLine("      ${resource.name}: Color.lerp(a.${resource.name}, b.${resource.name}, t)!,")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine("}")
        if (groupName != resources.groupBy { it.groups.firstOrNull() }.keys.last()) {
            appendLine()
        }
    }
}

private fun buildThemeCode(
    fileName: String,
    scriptFilePath: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    val groupedThemeResources = resources.groupBy { it.groups.firstOrNull() }
    return buildString {
        append(defaultAutoGenerationMessage("Semantic colors"))
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Light theme implementation of semantic colors")
        appendLine("/// See [LemonadeSemanticColors] for details on the color structure.")
        appendLine("/// ")
        appendLine("/// This API is internal to Lemonade using it directly in general is a mistake, ")
        appendLine("/// accessing the colors directly is not recommended, use the LemonadeTheme ")
        appendLine("/// instead.")
        appendLine("@internal")
        appendLine("final class $fileName implements LemonadeSemanticColors {")
        appendLine("  const $fileName();")
        appendLine()

        groupedThemeResources.forEach { (groupName, _) ->
            if (groupName != null) {
                val propertyName = groupName.sanitizedValueName().camelCase()
                appendLine("  @override")
                appendLine("  Lemonade${groupName.capitalize()}Colors get $propertyName => _Light${groupName.capitalize()}Colors();")
                if (groupName != groupedThemeResources.keys.last()) {
                    appendLine()
                }
            }
        }
        appendLine("}")
        appendLine()

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
    }
}

private fun buildGroupClassCode(
    groupName: String,
    resources: List<ResourceData<ThemeResourceData>>,
): String {
    return buildString {
        appendLine("final class _Light${groupName.capitalize()}Colors implements Lemonade${groupName.capitalize()}Colors {")
        resources.forEach { resource ->
            appendLine("  @override")
            appendLine("  Color get ${resource.name} => LemonadePrimitiveColors.${resource.value.valueGroup}.${resource.value.valueName};")
            if (resource != resources.last()) {
                appendLine()
            }
        }
        appendLine("}")
        if (groupName != resources.groupBy { it.groups.firstOrNull() }.keys.last()) {
            appendLine()
        }
    }
}

main()
