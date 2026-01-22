#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import org.json.JSONObject
import java.io.File

private data class ShadowResourceValue(
    val floatValue: Double = 0.0,
    val colorValue: Color? = null,
)

private data class ShadowResourceGroup(
    val groupName: String,
    val levels: List<ShadowResource>,
)

private data class ShadowResource(
    val blur: Double = 0.0,
    val spread: Double = 0.0,
    val offsetX: Double = 0.0,
    val offsetY: Double = 0.0,
    val color: Color = Color(0.0, 0.0, 0.0, 0.0),
)

fun main() {
    val shadowTokensFile = File("tokens/shadow.json")
    val outputDir = File("flutter/lib/src/foundation")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!shadowTokensFile.exists() || !shadowTokensFile.isFile) {
            error(message = "File $shadowTokensFile does not exist in system")
        }

        val shadowResources = readFileResourceFile(
            file = shadowTokensFile,
            resourceMap = { jsonObject ->
                val resolvedValue = jsonObject.optDouble("resolvedValue")
                if (!resolvedValue.isNaN()) {
                    ShadowResourceValue(floatValue = resolvedValue)
                } else {
                    val colorObj = jsonObject.getJSONObject("resolvedValue")
                    ShadowResourceValue(
                        colorValue = Color(
                            r = colorObj.getDouble("r"),
                            g = colorObj.getDouble("g"),
                            b = colorObj.getDouble("b"),
                            a = colorObj.getDouble("a"),
                        )
                    )
                }
            },
        ).toShadowResource()
        println("✓ Loaded shadow resources")

        val generatedCode = buildShadowsClass(
            scriptFilePath = "scripts/flutter-shadow-token-converter.main.kts",
            resources = shadowResources,
        )

        val outputFile = File(outputDir, "shadows.dart")
        outputFile.writeText(generatedCode)

        // Format the file using dart format
        ProcessBuilder("dart", "format", outputFile.absolutePath).start().waitFor()

        println("✓ Shadows file created")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${shadowTokensFile.name}: ${error.message}")
    }
}

private fun List<ResourceData<ShadowResourceValue>>.toShadowResource(): List<ShadowResourceGroup> {
    val groups = groupBy { resourceData -> resourceData.groups[1] }
    return groups.map { (groupName, resources) ->
        val groupLevels = resources.groupBy { groupResource -> groupResource.groups[2] }
        val levels = groupLevels.map { (_, levelResources) ->
            var levelResource = ShadowResource()
            levelResources.forEach { resourceValue ->
                levelResource = when {
                    resourceValue.name.contains("Blur") -> levelResource.copy(blur = resourceValue.value.floatValue)
                    resourceValue.name.contains("Spread") -> levelResource.copy(spread = resourceValue.value.floatValue)
                    resourceValue.name.contains("OffsetX") -> levelResource.copy(offsetX = resourceValue.value.floatValue)
                    resourceValue.name.contains("OffsetY") -> levelResource.copy(offsetY = resourceValue.value.floatValue)
                    resourceValue.name.contains("Color") && resourceValue.value.colorValue != null -> 
                        levelResource.copy(color = resourceValue.value.colorValue!!)
                    else -> levelResource
                }
            }
            levelResource
        }
        ShadowResourceGroup(
            groupName = groupName.lowercase(),
            levels = levels,
        )
    }.sortedBy { 
        when (it.groupName) {
            "xsmall" -> 1
            "small" -> 2
            "medium" -> 3
            "large" -> 4
            else -> 5
        }
    }
}

private fun buildShadowsClass(
    scriptFilePath: String,
    resources: List<ShadowResourceGroup>,
): String {
    return buildString {
        append(defaultAutoGenerationMessage("Shadow values"))
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        appendLine("/// Shadow configuration for the Lemonade Design System.")
        appendLine("///")
        appendLine("/// Defines shadow styles used for elevation and depth.")
        appendLine("@immutable")
        appendLine("class LemonadeShadows {")
        appendLine("  /// Creates a [LemonadeShadows] configuration.")
        append("  const LemonadeShadows({")
        
        resources.forEach { resource ->
            appendLine()
            append("    this.${resource.groupName} = const <BoxShadow>[")
            appendLine()
            resource.levels.forEach { level ->
                append("      BoxShadow(")
                appendLine()
                append("        color: Color(${level.color.toHex()}),")
                appendLine()
                append("        blurRadius: ${level.blur.formatValue()},")
                appendLine()
                append("        offset: Offset(${level.offsetX.formatValue()}, ${level.offsetY.formatValue()}),")
                appendLine()
                if (level.spread != 0.0) {
                    append("        spreadRadius: ${level.spread.formatValue()},")
                    appendLine()
                }
                append("      ),")
                appendLine()
            }
            append("    ],")
        }
        appendLine()
        appendLine("  });")
        appendLine()

        appendLine("  /// Linearly interpolates between two [LemonadeShadows] objects.")
        appendLine("  /// If they are identical, returns [a].")
        appendLine("  factory LemonadeShadows.lerp(")
        appendLine("    LemonadeShadows a,")
        appendLine("    LemonadeShadows b,")
        appendLine("    double t,")
        appendLine("  ) {")
        appendLine("    if (identical(a, b)) return a;")
        appendLine()
        appendLine("    return LemonadeShadows(")
        resources.forEach { resource ->
            appendLine("      ${resource.groupName}: BoxShadow.lerpList(a.${resource.groupName}, b.${resource.groupName}, t)!,")
        }
        appendLine("    );")
        appendLine("  }")
        appendLine()

        resources.forEach { resource ->
            appendLine("  /// ${generateDescription(resource.groupName)}")
            appendLine("  final List<BoxShadow> ${resource.groupName};")
            appendLine()
        }

        appendLine("  @override")
        appendLine("  bool operator ==(Object other) =>")
        appendLine("      identical(this, other) ||")
        appendLine("      other is LemonadeShadows &&")
        appendLine("          runtimeType == other.runtimeType &&")
        resources.forEachIndexed { index, resource ->
            val isLast = index == resources.size - 1
            val ending = if (isLast) ";" else " &&"
            appendLine("          ${resource.groupName} == other.${resource.groupName}$ending")
        }
        appendLine()

        appendLine("  @override")
        append("  int get hashCode => Object.hash(")
        resources.forEachIndexed { index, resource ->
            val isLast = index == resources.size - 1
            val ending = if (isLast) ");" else ", "
            append("${resource.groupName}$ending")
        }
        appendLine()
        appendLine()

        appendLine("  /// Helper method to access [LemonadeShadows] from the closest")
        appendLine("  /// [LemonadeTheme] ancestor.")
        appendLine("  static LemonadeShadows of(BuildContext context) {")
        appendLine("    final theme = LemonadeTheme.of(context);")
        appendLine("    return theme.shadows;")
        appendLine("  }")
        appendLine("}")
    }
}

private fun generateDescription(tokenName: String): String {
    return when (tokenName) {
        "xsmall" -> "Extra small shadow style"
        "small" -> "Small shadow style"
        "medium" -> "Medium shadow style"
        "large" -> "Large shadow style"
        else -> "Shadow style for $tokenName"
    }
}

private fun Double.formatValue(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        String.format("%.2f", this).trimEnd('0').trimEnd('.')
    }
}

main()
