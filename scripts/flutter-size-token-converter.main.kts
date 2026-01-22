#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import org.json.JSONObject
import java.io.File

private data class SizeToken(
    val name: String = "",
    val value: Double = 0.0,
    val description: String = "",
)

fun main() {
    val sizeTokensFile = File("tokens/size.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!sizeTokensFile.exists() || !sizeTokensFile.isFile) {
            error(message = "File $sizeTokensFile does not exist in system.")
        }

        val sizeResources = readFileResourceFile(
            file = sizeTokensFile,
            resourceMap = { jsonObject ->
                SizeToken(
                    value = jsonObject.getDouble("resolvedValue"),
                )
            },
        )

        val SizeTokens = sizeResources.map { resource ->
            SizeToken(
                name = resource.name,
                value = resource.value.value,
                description = generateDescription(resource.name, resource.value.value),
            )
        }.sortedBy { it.value }

        val resultFileText = buildSizeClass(SizeTokens)

        val outputFile = File(outputDir, "sizes.dart")
        outputFile.writeText(resultFileText)

        println("✓ Converted ${sizeTokensFile.path} -> ${outputFile.path}")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${sizeTokensFile.path}: ${error.message}")
    }
}

private fun buildSizeClass(tokens: List<SizeToken>): String {
    return buildString {
        append(defaultAutoGenerationMessage("Size values"))
        appendLine("import 'dart:ui';")
        appendLine()
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Size configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Defines size values used for width and height.")
        appendLine("@immutable")
        appendLine("class LemonadeSizes {")
        appendLine("  /// Creates a [LemonadeSizes] configuration.")
        append("  const LemonadeSizes({")

        tokens.forEach { token ->
            appendLine()
            append("    this.${token.name} = ${token.value},")
        }
        appendLine()
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [LemonadeSizes] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeSizes.lerp(")
        appendLine("    LemonadeSizes a,")
        appendLine("    LemonadeSizes b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeSizes(")
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
        appendLine("      other is LemonadeSizes &&")
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
        tokens.forEachIndexed { index, token ->
            val isLast = index == tokens.size - 1
            val ending = if (isLast) "," else ","
            appendLine("    ${token.name}$ending")
        }
        appendLine("  ]);")
        appendLine()

        appendLine("  /// Helper method to access [LemonadeSizes] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeSizes of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.sizes;")
        appendLine("  }")
        appendLine("}")
    }
}

private fun generateDescription(tokenName: String, value: Double): String {
    val intValue = value.toInt()
    return "Size value of ${intValue}px from token `$tokenName`"
}

main()
