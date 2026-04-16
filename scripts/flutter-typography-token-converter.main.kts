#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

data class TypographyTokenValue(
    val floatValue: Double?,
    val stringValue: String?,
)

private data class TypographyDoubleToken(
    val name: String,
    val value: Double,
    val description: String,
)

private data class TypographyWeightToken(
    val name: String,
    val dartDefault: String,
    val description: String,
)

fun main() {
    val typographyTokensFile = File("tokens/typography.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) outputDir.mkdirs()

        if (!typographyTokensFile.exists() || !typographyTokensFile.isFile) {
            error(message = "File $typographyTokensFile does not exist in system.")
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

        // Typography tokens have path separators (e.g. "font-size/font-size-1200"), so
        // groupFullName collapses all tokens in the same group to the same value ("FontSize").
        // Use resource.name directly for Dart field names (already camelCase, e.g. "fontSize1200").
        val fontSizeTokens = allResources
            .filter { it.groups.firstOrNull() == "FontSize" }
            .map { TypographyDoubleToken(
                name = it.name,
                value = it.value.floatValue ?: 0.0,
                description = "Font size of ${it.value.floatValue?.toInt()}px from token `${it.name}`",
            )}
            .sortedBy { it.value }

        val fontWeightTokens = allResources
            .filter { it.groups.firstOrNull() == "FontWeight" }
            .map { resource ->
                val dartDefault = when (resource.value.stringValue) {
                    "Bold" -> "FontWeight.bold"
                    "SemiBold" -> "FontWeight.w600"
                    "Medium" -> "FontWeight.w500"
                    "Regular" -> "FontWeight.normal"
                    else -> "FontWeight.normal"
                }
                TypographyWeightToken(
                    name = resource.name,
                    dartDefault = dartDefault,
                    description = "Font weight ${resource.value.stringValue} from token `${resource.name}`",
                )
            }

        val lineHeightTokens = allResources
            .filter { it.groups.firstOrNull() == "LineHeight" }
            .map { TypographyDoubleToken(
                name = it.name,
                value = it.value.floatValue ?: 0.0,
                description = "Line height of ${it.value.floatValue?.toInt()}px from token `${it.name}`",
            )}
            .sortedBy { it.value }

        val fontFamilyBase = allResources
            .filter { it.groups.firstOrNull() == "FontFamily" }
            .firstOrNull { it.name == "base" }
            ?.value?.stringValue ?: "Figtree"

        File(outputDir, "font_sizes.dart").writeText(
            buildFontSizesClass(fontSizeTokens, fontFamilyBase)
        )
        println("✓ Converted ${typographyTokensFile.path} -> font_sizes.dart")

        File(outputDir, "font_weights.dart").writeText(
            buildFontWeightsClass(fontWeightTokens)
        )
        println("✓ Converted ${typographyTokensFile.path} -> font_weights.dart")

        File(outputDir, "line_heights.dart").writeText(
            buildLineHeightsClass(lineHeightTokens)
        )
        println("✓ Converted ${typographyTokensFile.path} -> line_heights.dart")

    } catch (error: Throwable) {
        println("✗ Failed to convert ${typographyTokensFile.path}: ${error.message}")
    }
}

private fun buildFontSizesClass(tokens: List<TypographyDoubleToken>, fontFamilyBase: String): String = buildString {
    append(defaultAutoGenerationMessage("Font size values"))
    appendLine("import 'dart:ui';")
    appendLine()
    appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
    appendLine()
    appendLine("/// Font size configuration for the Lemonade Design System.")
    appendLine("///")
    appendLine("/// Defines font sizes used for text styling.")
    appendLine("@immutable")
    appendLine("class LemonadeFontSizes {")
    appendLine("  /// The base font family for the design system.")
    appendLine("  static const String fontFamily = '$fontFamilyBase';")
    appendLine()
    appendLine("  /// Creates a [LemonadeFontSizes] configuration.")
    append("  const LemonadeFontSizes({")
    tokens.forEach { token ->
        appendLine()
        append("    this.${token.name} = ${token.value},")
    }
    appendLine()
    appendLine("  });")
    appendLine()
    appendLine("  /// Linearly interpolates between two [LemonadeFontSizes] objects.")
    appendLine("  /// If they are identical, returns [a].")
    appendLine("  factory LemonadeFontSizes.lerp(")
    appendLine("    LemonadeFontSizes a,")
    appendLine("    LemonadeFontSizes b,")
    appendLine("    double t,")
    appendLine("  ) {")
    appendLine("    if (identical(a, b)) return a;")
    appendLine()
    appendLine("    return LemonadeFontSizes(")
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
    appendLine("      other is LemonadeFontSizes &&")
    appendLine("          runtimeType == other.runtimeType &&")
    tokens.forEachIndexed { index, token ->
        val isLast = index == tokens.size - 1
        val ending = if (isLast) ";" else " &&"
        appendLine("          ${token.name} == other.${token.name}$ending")
    }
    appendLine()
    appendLine("  @override")
    append("  int get hashCode => Object.hashAll([")
    appendLine()
    tokens.forEach { token ->
        appendLine("    ${token.name},")
    }
    appendLine("  ]);")
    appendLine()
    appendLine("  /// Helper method to access [LemonadeFontSizes] from the closest")
    appendLine("  /// [LemonadeTheme] ancestor.")
    appendLine("  static LemonadeFontSizes of(BuildContext context) {")
    appendLine("    final theme = LemonadeTheme.of(context);")
    appendLine("    return theme.fontSizes;")
    appendLine("  }")
    appendLine("}")
}

private fun buildFontWeightsClass(tokens: List<TypographyWeightToken>): String = buildString {
    append(defaultAutoGenerationMessage("Font weight values"))
    appendLine("import 'dart:ui';")
    appendLine()
    appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
    appendLine()
    appendLine("/// Font weight configuration for the Lemonade Design System.")
    appendLine("///")
    appendLine("/// Defines font weights used for text styling.")
    appendLine("@immutable")
    appendLine("class LemonadeFontWeights {")
    appendLine("  /// Creates a [LemonadeFontWeights] configuration.")
    append("  const LemonadeFontWeights({")
    tokens.forEach { token ->
        appendLine()
        append("    this.${token.name} = ${token.dartDefault},")
    }
    appendLine()
    appendLine("  });")
    appendLine()
    appendLine("  /// Linearly interpolates between two [LemonadeFontWeights] objects.")
    appendLine("  /// If they are identical, returns [a].")
    appendLine("  factory LemonadeFontWeights.lerp(")
    appendLine("    LemonadeFontWeights a,")
    appendLine("    LemonadeFontWeights b,")
    appendLine("    double t,")
    appendLine("  ) {")
    appendLine("    if (identical(a, b)) return a;")
    appendLine()
    appendLine("    return LemonadeFontWeights(")
    tokens.forEach { token ->
        appendLine("      ${token.name}: FontWeight.lerp(a.${token.name}, b.${token.name}, t) ?? a.${token.name},")
    }
    appendLine("    );")
    appendLine("  }")
    appendLine()
    tokens.forEach { token ->
        appendLine("  /// ${token.description}")
        appendLine("  final FontWeight ${token.name};")
        appendLine()
    }
    appendLine("  @override")
    appendLine("  bool operator ==(Object other) =>")
    appendLine("      identical(this, other) ||")
    appendLine("      other is LemonadeFontWeights &&")
    appendLine("          runtimeType == other.runtimeType &&")
    tokens.forEachIndexed { index, token ->
        val isLast = index == tokens.size - 1
        val ending = if (isLast) ";" else " &&"
        appendLine("          ${token.name} == other.${token.name}$ending")
    }
    appendLine()
    appendLine("  @override")
    append("  int get hashCode => Object.hashAll([")
    appendLine()
    tokens.forEach { token ->
        appendLine("    ${token.name},")
    }
    appendLine("  ]);")
    appendLine()
    appendLine("  /// Helper method to access [LemonadeFontWeights] from the closest")
    appendLine("  /// [LemonadeTheme] ancestor.")
    appendLine("  static LemonadeFontWeights of(BuildContext context) {")
    appendLine("    final theme = LemonadeTheme.of(context);")
    appendLine("    return theme.fontWeights;")
    appendLine("  }")
    appendLine("}")
}

private fun buildLineHeightsClass(tokens: List<TypographyDoubleToken>): String = buildString {
    append(defaultAutoGenerationMessage("Line height values"))
    appendLine("import 'dart:ui';")
    appendLine()
    appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
    appendLine()
    appendLine("/// Line height configuration for the Lemonade Design System.")
    appendLine("///")
    appendLine("/// Defines line heights used for text styling.")
    appendLine("@immutable")
    appendLine("class LemonadeLineHeights {")
    appendLine("  /// Creates a [LemonadeLineHeights] configuration.")
    append("  const LemonadeLineHeights({")
    tokens.forEach { token ->
        appendLine()
        append("    this.${token.name} = ${token.value},")
    }
    appendLine()
    appendLine("  });")
    appendLine()
    appendLine("  /// Linearly interpolates between two [LemonadeLineHeights] objects.")
    appendLine("  /// If they are identical, returns [a].")
    appendLine("  factory LemonadeLineHeights.lerp(")
    appendLine("    LemonadeLineHeights a,")
    appendLine("    LemonadeLineHeights b,")
    appendLine("    double t,")
    appendLine("  ) {")
    appendLine("    if (identical(a, b)) return a;")
    appendLine()
    appendLine("    return LemonadeLineHeights(")
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
    appendLine("      other is LemonadeLineHeights &&")
    appendLine("          runtimeType == other.runtimeType &&")
    tokens.forEachIndexed { index, token ->
        val isLast = index == tokens.size - 1
        val ending = if (isLast) ";" else " &&"
        appendLine("          ${token.name} == other.${token.name}$ending")
    }
    appendLine()
    appendLine("  @override")
    append("  int get hashCode => Object.hashAll([")
    appendLine()
    tokens.forEach { token ->
        appendLine("    ${token.name},")
    }
    appendLine("  ]);")
    appendLine()
    appendLine("  /// Helper method to access [LemonadeLineHeights] from the closest")
    appendLine("  /// [LemonadeTheme] ancestor.")
    appendLine("  static LemonadeLineHeights of(BuildContext context) {")
    appendLine("    final theme = LemonadeTheme.of(context);")
    appendLine("    return theme.lineHeights;")
    appendLine("  }")
    appendLine("}")
}

main()
