#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import org.json.JSONObject
import java.io.File

private data class SpacingToken(
    val name: String = "",
    val value: Double = 0.0,
    val description: String = "",
)

fun main() {
    val spacingTokensFile = File("tokens/spacing.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!spacingTokensFile.exists() || !spacingTokensFile.isFile) {
            error(message = "File $spacingTokensFile does not exist in system.")
        }

        val spacingResources = readFileResourceFile(
            file = spacingTokensFile,
            resourceMap = { jsonObject ->
                SpacingToken(
                    value = jsonObject.getDouble("resolvedValue"),
                )
            },
        )
        
        val spacingTokens = spacingResources.map { resource ->
            SpacingToken(
                name = resource.name,
                value = resource.value.value,
                description = generateDescription(resource.name, resource.value.value),
            )
        }.sortedBy { it.value }

        val resultFileText = buildSpacingClass(spacingTokens)

        val outputFile = File(outputDir, "spaces.dart")
        outputFile.writeText(resultFileText)

        println("✓ Converted ${spacingTokensFile.path} -> ${outputFile.path}")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${spacingTokensFile.path}: ${error.message}")
    }
}

private fun buildSpacingClass(tokens: List<SpacingToken>): String {
    return buildString {
        append(defaultAutoGenerationMessage("Spacing values"))
        appendLine("import 'dart:ui';")
        appendLine()
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Spacing configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Defines spacing values used for padding, margins, and gaps.")
        appendLine("@immutable")
        appendLine("class LemonadeSpaces {")
        appendLine("  /// Creates a [LemonadeSpaces] configuration.")
        append("  const LemonadeSpaces({")
        
        tokens.forEach { token ->
            appendLine()
            append("    this.${token.name} = ${token.value},")
        }
        appendLine()
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [LemonadeSpaces] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeSpaces.lerp(")
        appendLine("    LemonadeSpaces a,")
        appendLine("    LemonadeSpaces b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeSpaces(")
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
        appendLine("      other is LemonadeSpaces &&")
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

        appendLine("  /// Helper method to access [LemonadeSpaces] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeSpaces of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.spaces;")
        appendLine("  }")
        appendLine("}")
    }
}

private fun generateDescription(tokenName: String, value: Double): String {
    val intValue = value.toInt()
    return "Spacing value of ${intValue}px from token `$tokenName`"
}

main()
