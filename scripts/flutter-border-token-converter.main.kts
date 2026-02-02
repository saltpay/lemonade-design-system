#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File

private data class BorderToken(
    val name: String = "",
    val group: String = "",
    val value: Double = 0.0,
    val description: String = "",
    val aliasName: String? = null,
)

fun main() {
    val borderTokensFile = File("tokens/border-width.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!borderTokensFile.exists() || !borderTokensFile.isFile) {
            error(message = "File $borderTokensFile does not exist in system.")
        }

        val borderResources = readFileResourceFile(
            file = borderTokensFile,
            resourceMap = { resolvedValueObject ->
                val aliasName = resolvedValueObject.optString("aliasName")

                BorderToken(
                    value = resolvedValueObject.getDouble("resolvedValue"),
                    aliasName = aliasName,
                )
            },
        )

        val borderTokens = borderResources.map { resource ->
            val rawTokenName = resource.name
            BorderToken(
                name = rawTokenName,
                group = (resource.groups.firstOrNull() ?: "default"),
                value = resource.value.value,
                description = generateDescription(rawTokenName, resource.value.value),
                aliasName = resource.value.aliasName,
            )
        }.sortedBy { it.value }

        val resultFileText = buildBorderClass(borderTokens)

        val outputFile = File(outputDir, "border.dart")
        outputFile.writeText(resultFileText)

        println("✓ Converted ${borderTokensFile.path} -> ${outputFile.path}")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${borderTokensFile.path}: ${error.message}")
    }
}

private fun buildBorderClass(tokens: List<BorderToken>): String {
    val groupedTokens = tokens.groupBy { it.group }

    return buildString {
        append(defaultAutoGenerationMessage("Border width values"))
        appendLine("import 'dart:ui';")
        appendLine()
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Border width configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Provides a consistent and scalable way to manage border widths across interfaces.")
        appendLine("/// These values can be used for borders, outlines, and focus indicators.")
        appendLine("@immutable")
        appendLine("class LemonadeBorder {")
        appendLine("  /// Creates a [LemonadeBorder] configuration.")
        appendLine("  const LemonadeBorder({")

        groupedTokens.forEach { (groupName, _) ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            appendLine("    this.$propertyName = const Lemonade${groupName.capitalize()}Border(),")
        }
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [LemonadeBorder] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeBorder.lerp(")
        appendLine("    LemonadeBorder a,")
        appendLine("    LemonadeBorder b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeBorder(")
        groupedTokens.forEach { (groupName, _) ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            appendLine("      $propertyName: Lemonade${groupName.capitalize()}Border.lerp(a.$propertyName, b.$propertyName, t),")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()

        groupedTokens.forEach { (groupName, _) ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            appendLine("  /// ${groupName.capitalize()} border width values")
            appendLine("  final Lemonade${groupName.capitalize()}Border $propertyName;")
            appendLine()
        }

        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeBorder &&")
        appendLine("          runtimeType == other.runtimeType &&")
        groupedTokens.keys.forEachIndexed { index, groupName ->
            val propertyName = groupName.replaceFirstChar { it.lowercase() }
            val isLast = index == groupedTokens.size - 1
            val ending = if (isLast) ";" else " &&"
            appendLine("          $propertyName == other.$propertyName$ending")
        }
        appendLine()

        appendLine("  @override")
        append("  int get hashCode => ")
        if (groupedTokens.size == 1) {
            val propertyName = groupedTokens.keys.first().replaceFirstChar { it.lowercase() }
            appendLine("$propertyName.hashCode;")
        } else {
            appendLine("Object.hash(")
            groupedTokens.keys.forEachIndexed { index, groupName ->
                val propertyName = groupName.replaceFirstChar { it.lowercase() }
                val isLast = index == groupedTokens.size - 1
                val ending = if (isLast) "," else ","
                appendLine("    $propertyName$ending")
            }
            appendLine("  );")
        }
        appendLine()

        appendLine("  /// Helper method to access [LemonadeBorder] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeBorder of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.border;")
        appendLine("  }")
        appendLine("}")
        appendLine()

        groupedTokens.forEach { (groupName, groupTokens) ->
            appendLine("/// ${groupName.capitalize()} border width values for consistent border styling")
            appendLine("@immutable")
            appendLine("class Lemonade${groupName.capitalize()}Border {")

            if (groupName.equals("State", ignoreCase = true)) {
                appendLine("  /// Creates a [Lemonade${groupName.capitalize()}Border] configuration.")
                appendLine("  const Lemonade${groupName.capitalize()}Border({")
                appendLine("    this.base = const LemonadeBaseBorder(),")
                appendLine("  });")
                appendLine()
                appendLine("  /// Base border configuration for referencing values")
                appendLine("  final LemonadeBaseBorder base;")
                appendLine()
            } else {
                appendLine("  /// Creates a [Lemonade${groupName.capitalize()}Border] configuration.")
                append("  const Lemonade${groupName.capitalize()}Border({")

                groupTokens.forEach { token ->
                    appendLine()
                    append("    this.${token.name} = ${token.value},")
                }
                appendLine()
                appendLine("  });")
                appendLine()
            }

            appendLine("  /// Linearly interpolates between two [Lemonade${groupName.capitalize()}Border] objects.")
            appendLine("  /// If they are identical, returns [a].")
            appendLine("  factory Lemonade${groupName.capitalize()}Border.lerp(")
            appendLine("    Lemonade${groupName.capitalize()}Border a,")
            appendLine("    Lemonade${groupName.capitalize()}Border b,")
            appendLine("    double t,")
            appendLine("  ) {")
            appendLine("    if (identical(a, b)) return a;")
            appendLine()
            appendLine("    return Lemonade${groupName.capitalize()}Border(")

            if (groupName.equals("State", ignoreCase = true)) {
                appendLine("      base: LemonadeBaseBorder.lerp(a.base, b.base, t),")
            } else {
                groupTokens.forEach { token ->
                    appendLine("      ${token.name}: lerpDouble(a.${token.name}, b.${token.name}, t)!,")
                }
            }
            appendLine("    );")
            appendLine("  }")
            appendLine()

            groupTokens.forEach { token ->
                if (groupName.equals("State", ignoreCase = true)) {
                    val aliasReference = getAliasReference(token.aliasName)
                    appendLine("  /// ${token.description} (references ${token.aliasName})")
                    appendLine("  double get ${token.name} => ${aliasReference};")
                } else {
                    appendLine("  /// ${token.description}")
                    appendLine("  final double ${token.name};")
                }
                appendLine()
            }

            appendLine("  @override")
            appendLine("  bool operator ==(Object other) =>")
            appendLine("      identical(this, other) ||")
            appendLine("      other is Lemonade${groupName.capitalize()}Border &&")
            appendLine("          runtimeType == other.runtimeType &&")

            if (groupName.equals("State", ignoreCase = true)) {
                appendLine("          base == other.base;")
            } else {
                groupTokens.forEachIndexed { index, token ->
                    val isLast = index == groupTokens.size - 1
                    val ending = if (isLast) ";" else " &&"
                    appendLine("          ${token.name} == other.${token.name}$ending")
                }
            }
            appendLine()

            appendLine("  @override")
            if (groupName.equals("State", ignoreCase = true)) {
                appendLine("  int get hashCode => base.hashCode;")
            } else {
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
            }
            appendLine("}")

            if (groupName != groupedTokens.keys.last()) {
                appendLine()
            }
        }
    }
}

private fun generateDescription(tokenName: String, value: Double): String {
    return "Border width value of ${value}px from token `$tokenName`"
}

private fun getAliasReference(aliasName: String?): String {
    if (aliasName == null) return "0.0"

    val parts = aliasName.split("/")
    if (parts.size == 2) {
        val group = parts[0]
        val tokenName = parts[1].replace("-", "")
        return "base.$tokenName"
    }

    return "0.0"
}

main()
