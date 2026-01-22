#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

private data class RadiusToken(
    val name: String = "",
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
        
        val radiusTokens = radiusResources.map { resource ->
            RadiusToken(
                name = resource.name,
                value = resource.value.value,
                description = generateDescription(resource.name, resource.value.value),
            )
        }.sortedBy { it.value }

        val radiusFileText = buildRadiusClass(radiusTokens)
        val shapesFileText = buildShapesClass(radiusTokens)

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

private fun buildRadiusClass(tokens: List<RadiusToken>): String {
    return buildString {
        append(defaultAutoGenerationMessage("Radius values"))
        appendLine("import 'dart:ui';")
        appendLine()
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Radius configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Sets a small, clear set of predefined radius values for UI elements to ensure")
        appendLine("/// consistent, scalable rounding across the product.")
        appendLine("@immutable")
        appendLine("class LemonadeRadius {")
        appendLine("  /// Creates a [LemonadeRadius] configuration.")
        append("  const LemonadeRadius({")
        
        tokens.forEach { token ->
            appendLine()
            append("    this.${token.name} = ${token.value},")
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
        tokens.forEach { token ->
            appendLine("      ${token.name}: lerpDouble(a.${token.name}, b.${token.name}, t)!,")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()

        tokens.forEach { token ->
            appendLine("  /// ${token.description}")
            appendLine("  final double ${token.name};")
            appendLine()
        }

        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeRadius &&")
        appendLine("          runtimeType == other.runtimeType &&")
        tokens.forEachIndexed { index, token ->
            val isLast = index == tokens.size - 1
            val ending = if (isLast) ";" else " &&"
            appendLine("          ${token.name} == other.${token.name}$ending")
        }
        appendLine()

        appendLine("  @override")
        append("  int get hashCode => Object.hash(")
        appendLine()
        tokens.forEachIndexed { index, token ->
            val isLast = index == tokens.size - 1
            val ending = if (isLast) "," else ","
            appendLine("    ${token.name}$ending")
        }
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

private fun buildShapesClass(tokens: List<RadiusToken>): String {
    return buildString {
        append(defaultAutoGenerationMessage("Shape values"))
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Shape configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Provides predefined shape values based on radius tokens for consistent")
        appendLine("/// rounded corners across the product.")
        appendLine("@immutable")
        appendLine("class LemonadeShapes {")
        appendLine("  /// Creates a [LemonadeShapes] configuration.")
        append("  const LemonadeShapes({")
        
        tokens.forEach { token ->
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
        tokens.forEach { token ->
            appendLine("      ${token.name}: ShapeBorder.lerp(a.${token.name}, b.${token.name}, t)!,")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()

        tokens.forEach { token ->
            appendLine("  /// ${token.description}")
            appendLine("  final ShapeBorder ${token.name};")
            appendLine()
        }

        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeShapes &&")
        appendLine("          runtimeType == other.runtimeType &&")
        tokens.forEachIndexed { index, token ->
            val isLast = index == tokens.size - 1
            val ending = if (isLast) ";" else " &&"
            appendLine("          ${token.name} == other.${token.name}$ending")
        }
        appendLine()

        appendLine("  @override")
        append("  int get hashCode => Object.hash(")
        appendLine()
        tokens.forEachIndexed { index, token ->
            val isLast = index == tokens.size - 1
            val ending = if (isLast) "," else ","
            appendLine("    ${token.name}$ending")
        }
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
