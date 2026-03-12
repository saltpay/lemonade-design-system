#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

private data class RadiusToken(
    val name: String = "",
    val group: String? = null,
    val value: Double = 0.0,
    val description: String = "",
)

fun main() {
    val radiusTokensFile = File("tokens/radius.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!radiusTokensFile.exists() || !radiusTokensFile.isFile) {
            error(message = "File $radiusTokensFile does not exist in system.")
        }

        val radiusResources = readFileResourceFile(
            file = radiusTokensFile,
            resourceMap = { jsonObject ->
                RadiusToken(
                    value = jsonObject.getDouble("resolvedValue"),
                )
            },
        )

        val allRadiusTokens = radiusResources.map { resource ->
            RadiusToken(
                name = resource.name,
                group = resource.groups.firstOrNull(),
                value = resource.value.value,
                description = generateDescription(resource.name, resource.value.value),
            )
        }.sortedBy { it.value }

        val primitiveTokens = allRadiusTokens.filter { it.group == null }
        val groupedTokens = allRadiusTokens.filter { it.group != null }.groupBy { it.group!! }

        val radiusFileText = buildRadiusClass(primitiveTokens, groupedTokens)
        val shapesFileText = buildShapesClass(primitiveTokens, groupedTokens)

        val radiusOutputFile = File(outputDir, "radius.dart")
        radiusOutputFile.writeText(radiusFileText)

        val shapesOutputFile = File(outputDir, "shapes.dart")
        shapesOutputFile.writeText(shapesFileText)

        println("✓ Converted ${radiusTokensFile.path} -> ${radiusOutputFile.path}")
        println("✓ Converted ${radiusTokensFile.path} -> ${shapesOutputFile.path}")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${radiusTokensFile.path}: ${error.message}")
    }
}

private fun buildRadiusClass(
    primitiveTokens: List<RadiusToken>,
    groupedTokens: Map<String, List<RadiusToken>>,
): String {
    return buildString {
        append(defaultAutoGenerationMessage("Radius values"))
        appendLine("import 'dart:ui';")
        appendLine()
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()

        // Sub-classes for each semantic group (like LemonadeBaseOpacity)
        groupedTokens.forEach { (groupName, groupTokens) ->
            val className = "Lemonade${groupName.capitalize()}Radius"
            appendLine("/// ${groupName.capitalize()} radius values")
            appendLine("@immutable")
            appendLine("class $className {")
            appendLine("  /// Creates a [$className] configuration.")
            append("  const $className({")
            groupTokens.forEach { token ->
                appendLine()
                append("    this.${token.name} = ${token.value},")
            }
            appendLine()
            appendLine("  });")
            appendLine()
            appendLine("  /// Linearly interpolates between two [$className] objects.")
            appendLine("  factory $className.lerp(")
            appendLine("    $className a,")
            appendLine("    $className b,")
            appendLine("    double t,")
            appendLine("  ) {")
            appendLine("    if (identical(a, b)) return a;")
            appendLine()
            appendLine("    return $className(")
            groupTokens.forEach { token ->
                appendLine("      ${token.name}: lerpDouble(a.${token.name}, b.${token.name}, t)!,")
            }
            appendLine("    );")
            appendLine("  }")
            appendLine()
            groupTokens.forEach { token ->
                appendLine("  /// ${token.description}")
                appendLine("  final double ${token.name};")
                appendLine()
            }
            appendLine("  @override")
            appendLine("  bool operator ==(Object other) =>")
            appendLine("      identical(this, other) ||")
            appendLine("      other is $className &&")
            appendLine("          runtimeType == other.runtimeType &&")
            groupTokens.forEachIndexed { index, token ->
                val ending = if (index == groupTokens.size - 1) ";" else " &&"
                appendLine("          ${token.name} == other.${token.name}$ending")
            }
            appendLine()
            appendLine("  @override")
            if (groupTokens.size == 1) {
                appendLine("  int get hashCode => ${groupTokens.first().name}.hashCode;")
            } else {
                appendLine("  int get hashCode => Object.hash(")
                groupTokens.forEach { token -> appendLine("    ${token.name},") }
                appendLine("  );")
            }
            appendLine("}")
            appendLine()
        }

        // Main LemonadeRadius class
        appendLine("/// Radius configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Sets a small, clear set of predefined radius values for UI elements to ensure")
        appendLine("/// consistent, scalable rounding across the product.")
        appendLine("@immutable")
        appendLine("class LemonadeRadius {")
        appendLine("  /// Creates a [LemonadeRadius] configuration.")
        append("  const LemonadeRadius({")
        primitiveTokens.forEach { token ->
            appendLine()
            append("    this.${token.name} = ${token.value},")
        }
        groupedTokens.forEach { (groupName, _) ->
            val prop = groupName.replaceFirstChar { it.lowercase() }
            val cls = "Lemonade${groupName.capitalize()}Radius"
            appendLine()
            append("    this.$prop = const $cls(),")
        }
        appendLine()
        appendLine("  });")
        appendLine()
        appendLine("  /// Linearly interpolates between two [LemonadeRadius] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeRadius.lerp(")
        appendLine("    LemonadeRadius a,")
        appendLine("    LemonadeRadius b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeRadius(")
        primitiveTokens.forEach { token ->
            appendLine("      ${token.name}: lerpDouble(a.${token.name}, b.${token.name}, t)!,")
        }
        groupedTokens.forEach { (groupName, _) ->
            val prop = groupName.replaceFirstChar { it.lowercase() }
            val cls = "Lemonade${groupName.capitalize()}Radius"
            appendLine("      $prop: $cls.lerp(a.$prop, b.$prop, t),")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()
        primitiveTokens.forEach { token ->
            appendLine("  /// ${token.description}")
            appendLine("  final double ${token.name};")
            appendLine()
        }
        groupedTokens.forEach { (groupName, _) ->
            val prop = groupName.replaceFirstChar { it.lowercase() }
            val cls = "Lemonade${groupName.capitalize()}Radius"
            appendLine("  /// ${groupName.capitalize()} radius values")
            appendLine("  final $cls $prop;")
            appendLine()
        }
        val allProps = primitiveTokens.map { it.name } + groupedTokens.keys.map { it.replaceFirstChar { c -> c.lowercase() } }
        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeRadius &&")
        appendLine("          runtimeType == other.runtimeType &&")
        allProps.forEachIndexed { index, prop ->
            val ending = if (index == allProps.size - 1) ";" else " &&"
            appendLine("          $prop == other.$prop$ending")
        }
        appendLine()
        appendLine("  @override")
        appendLine("  int get hashCode => Object.hash(")
        allProps.forEach { prop -> appendLine("    $prop,") }
        appendLine("  );")
        appendLine()
        appendLine("  /// Helper method to access [LemonadeRadius] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeRadius of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.radius;")
        appendLine("  }")
        appendLine("}")
    }
}

private fun buildShapesClass(
    primitiveTokens: List<RadiusToken>,
    groupedTokens: Map<String, List<RadiusToken>>,
): String {
    return buildString {
        append(defaultAutoGenerationMessage("Shape values"))
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()

        // Sub-classes per semantic group
        groupedTokens.forEach { (groupName, groupTokens) ->
            val className = "Lemonade${groupName.capitalize()}Shapes"
            appendLine("/// ${groupName.capitalize()} shape values")
            appendLine("@immutable")
            appendLine("class $className {")
            appendLine("  /// Creates a [$className] configuration.")
            append("  const $className({")
            groupTokens.forEach { token ->
                appendLine()
                append("    this.${token.name} = const RoundedRectangleBorder(")
                appendLine()
                append("      borderRadius: BorderRadius.all(Radius.circular(${token.value})),")
                appendLine()
                append("    ),")
            }
            appendLine()
            appendLine("  });")
            appendLine()
            appendLine("  /// Linearly interpolates between two [$className] objects.")
            appendLine("  factory $className.lerp(")
            appendLine("    $className a,")
            appendLine("    $className b,")
            appendLine("    double t,")
            appendLine("  ) {")
            appendLine("    if (identical(a, b)) return a;")
            appendLine()
            appendLine("    return $className(")
            groupTokens.forEach { token ->
                appendLine("      ${token.name}: ShapeBorder.lerp(a.${token.name}, b.${token.name}, t)!,")
            }
            appendLine("    );")
            appendLine("  }")
            appendLine()
            groupTokens.forEach { token ->
                appendLine("  /// ${token.description}")
                appendLine("  final ShapeBorder ${token.name};")
                appendLine()
            }
            appendLine("  @override")
            appendLine("  bool operator ==(Object other) =>")
            appendLine("      identical(this, other) ||")
            appendLine("      other is $className &&")
            appendLine("          runtimeType == other.runtimeType &&")
            groupTokens.forEachIndexed { index, token ->
                val ending = if (index == groupTokens.size - 1) ";" else " &&"
                appendLine("          ${token.name} == other.${token.name}$ending")
            }
            appendLine()
            appendLine("  @override")
            if (groupTokens.size == 1) {
                appendLine("  int get hashCode => ${groupTokens.first().name}.hashCode;")
            } else {
                appendLine("  int get hashCode => Object.hash(")
                groupTokens.forEach { token -> appendLine("    ${token.name},") }
                appendLine("  );")
            }
            appendLine("}")
            appendLine()
        }

        // Main LemonadeShapes class
        appendLine("/// Shape configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Provides predefined shape values based on radius tokens for consistent")
        appendLine("/// rounded corners across the product.")
        appendLine("@immutable")
        appendLine("class LemonadeShapes {")
        appendLine("  /// Creates a [LemonadeShapes] configuration.")
        append("  const LemonadeShapes({")
        primitiveTokens.forEach { token ->
            appendLine()
            append("    this.${token.name} = const RoundedRectangleBorder(")
            appendLine()
            append("      borderRadius: BorderRadius.all(Radius.circular(${token.value})),")
            appendLine()
            append("    ),")
        }
        groupedTokens.forEach { (groupName, _) ->
            val prop = groupName.replaceFirstChar { it.lowercase() }
            val cls = "Lemonade${groupName.capitalize()}Shapes"
            appendLine()
            append("    this.$prop = const $cls(),")
        }
        appendLine()
        appendLine("  });")
        appendLine()
        appendLine("  /// Linearly interpolates between two [LemonadeShapes] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeShapes.lerp(")
        appendLine("    LemonadeShapes a,")
        appendLine("    LemonadeShapes b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeShapes(")
        primitiveTokens.forEach { token ->
            appendLine("      ${token.name}: ShapeBorder.lerp(a.${token.name}, b.${token.name}, t)!,")
        }
        groupedTokens.forEach { (groupName, _) ->
            val prop = groupName.replaceFirstChar { it.lowercase() }
            val cls = "Lemonade${groupName.capitalize()}Shapes"
            appendLine("      $prop: $cls.lerp(a.$prop, b.$prop, t),")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()
        primitiveTokens.forEach { token ->
            appendLine("  /// ${token.description}")
            appendLine("  final ShapeBorder ${token.name};")
            appendLine()
        }
        groupedTokens.forEach { (groupName, _) ->
            val prop = groupName.replaceFirstChar { it.lowercase() }
            val cls = "Lemonade${groupName.capitalize()}Shapes"
            appendLine("  /// ${groupName.capitalize()} shape values")
            appendLine("  final $cls $prop;")
            appendLine()
        }
        val allProps = primitiveTokens.map { it.name } + groupedTokens.keys.map { it.replaceFirstChar { c -> c.lowercase() } }
        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeShapes &&")
        appendLine("          runtimeType == other.runtimeType &&")
        allProps.forEachIndexed { index, prop ->
            val ending = if (index == allProps.size - 1) ";" else " &&"
            appendLine("          $prop == other.$prop$ending")
        }
        appendLine()
        appendLine("  @override")
        appendLine("  int get hashCode => Object.hash(")
        allProps.forEach { prop -> appendLine("    $prop,") }
        appendLine("  );")
        appendLine()
        appendLine("  /// Helper method to access [LemonadeShapes] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeShapes of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.shapes;")
        appendLine("  }")
        appendLine("}")
    }
}

private fun generateDescription(tokenName: String, value: Double): String {
    val intValue = if (value >= 999.0) "999" else value.toInt().toString()
    return "Radius value of ${intValue}px from token `$tokenName`"
}

main()
