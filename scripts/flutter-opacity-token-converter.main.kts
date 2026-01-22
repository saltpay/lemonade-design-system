#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

private data class OpacityToken(
    val name: String = "",
    val group: String = "",
    val value: Double = 0.0,
    val description: String = "",
)

fun main() {
    val opacityTokensFile = File("tokens/opacity.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!opacityTokensFile.exists() || !opacityTokensFile.isFile) {
            error(message = "File $opacityTokensFile does not exist in system.")
        }

        val opacityResources = readFileResourceFile(
            file = opacityTokensFile,
            resourceMap = { jsonObject ->
                OpacityToken(
                    value = jsonObject.getDouble("resolvedValue"),
                )
            },
        )
        
        val opacityTokens = opacityResources.map { resource ->
            val rawTokenName = resource.name
            OpacityToken(
                name = rawTokenName,
                group = (resource.groups.firstOrNull() ?: "default"),
                value = resource.value.value / 100.0, // Convert percentage to decimal
                description = generateDescription(rawTokenName, resource.value.value),
            )
        }.sortedBy { it.value }

        val resultFileText = buildOpacityClass(opacityTokens)

        val outputFile = File(outputDir, "opacity.dart")
        outputFile.writeText(resultFileText)

        println("✓ Converted ${opacityTokensFile.path} -> ${outputFile.path}")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${opacityTokensFile.path}: ${error.message}")
    }
}

private fun buildOpacityClass(tokens: List<OpacityToken>): String {
    val groupedTokens = tokens.groupBy { it.group }
    
    return buildString {
        append(defaultAutoGenerationMessage("Opacity values"))
        appendLine("import 'dart:ui';")
        appendLine()
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Opacity configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Provides a consistent and scalable way to manage opacity across interfaces.")
        appendLine("/// These values can be used for disabled components and general transparency handling.")
        appendLine("@immutable")
        appendLine("class LemonadeOpacity {")
        appendLine("  /// Creates a [LemonadeOpacity] configuration.")
        appendLine("  const LemonadeOpacity({")
        
        groupedTokens.forEach { (groupName, _) ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            appendLine("    this.$propertyName = const Lemonade${groupName.capitalize()}Opacity(),")
        }
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [LemonadeOpacity] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeOpacity.lerp(")
        appendLine("    LemonadeOpacity a,")
        appendLine("    LemonadeOpacity b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeOpacity(")
        groupedTokens.forEach { (groupName, _) ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            appendLine("      $propertyName: Lemonade${groupName.capitalize()}Opacity.lerp(a.$propertyName, b.$propertyName, t),")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()

        groupedTokens.forEach { (groupName, _) ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            appendLine("  /// ${groupName.capitalize()} opacity values")
            appendLine("  final Lemonade${groupName.capitalize()}Opacity $propertyName;")
            appendLine()
        }

        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeOpacity &&")
        appendLine("          runtimeType == other.runtimeType &&")
        groupedTokens.keys.forEachIndexed { index, groupName ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            val isLast = index == groupedTokens.size - 1
            val ending = if (isLast) ";" else " &&"
            appendLine("          $propertyName == other.$propertyName$ending")
        }
        appendLine()

        appendLine("  @override")
        append("  int get hashCode => Object.hash(")
        appendLine()
        groupedTokens.keys.forEachIndexed { index, groupName ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            val isLast = index == groupedTokens.size - 1
            val ending = if (isLast) "," else ","
            appendLine("    $propertyName$ending")
        }
        appendLine("  );")
        appendLine()

        appendLine("  /// Helper method to access [LemonadeOpacity] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeOpacity of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.opacity;")
        appendLine("  }")
        appendLine("}")
        appendLine()

        groupedTokens.forEach { (groupName, groupTokens) ->
            appendLine("/// ${groupName.capitalize()} opacity values for consistent transparency handling")
            appendLine("@immutable")
            appendLine("class Lemonade${groupName.capitalize()}Opacity {")
            appendLine("  /// Creates a [Lemonade${groupName.capitalize()}Opacity] configuration.")
            append("  const Lemonade${groupName.capitalize()}Opacity({")
            
            groupTokens.forEach { token ->
                appendLine()
                append("    this.${token.name} = ${token.value},")
            }
            appendLine()
            appendLine("  });")
            appendLine()

            appendLine("  /// Linearly interpolates between two [Lemonade${groupName.capitalize()}Opacity] objects.")
            appendLine("  /// If they are identical, returns [a].")
            appendLine("  factory Lemonade${groupName.capitalize()}Opacity.lerp(")
            appendLine("    Lemonade${groupName.capitalize()}Opacity a,")
            appendLine("    Lemonade${groupName.capitalize()}Opacity b,")
            appendLine("    double t,")
            appendLine("  ) {")
            appendLine("    if (identical(a, b)) return a;")
            appendLine()
            appendLine("    return Lemonade${groupName.capitalize()}Opacity(")
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
            appendLine("      other is Lemonade${groupName.capitalize()}Opacity &&")
            appendLine("          runtimeType == other.runtimeType &&")
            groupTokens.forEachIndexed { index, token ->
                val isLast = index == groupTokens.size - 1
                val ending = if (isLast) ";" else " &&"
                appendLine("          ${token.name} == other.${token.name}$ending")
            }
            appendLine()

            appendLine("  @override")
            append("  int get hashCode => ")
            if (groupTokens.size == 1) {
                appendLine("${groupTokens.first().name}.hashCode;")
            } else {
                appendLine("Object.hash(")
                groupTokens.forEachIndexed { index, token ->
                    val isLast = index == groupTokens.size - 1
                    val ending = if (isLast) "," else ","
                    appendLine("    ${token.name}$ending")
                }
                appendLine("  );")
            }
            appendLine("}")
            
            if (groupName != groupedTokens.keys.last()) {
                appendLine()
            }
        }
    }
}

private fun generateDescription(tokenName: String, value: Double): String {
    val percentage = value.toInt()
    return "Opacity value of ${percentage}% from token `$tokenName`"
}

main()
