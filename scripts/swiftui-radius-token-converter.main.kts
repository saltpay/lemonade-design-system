#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class RadiusResource(
    val radiusValue: Int,
)

fun main() {
    val radiusTokensFile = File("tokens/radius.json")
    val outputDir = File("swiftui/Sources/Lemonade")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        if (!radiusTokensFile.exists() || !radiusTokensFile.isFile) {
            error(message = "File $radiusTokensFile does not exist in system")
        }
        val radiusResources = readFileResourceFile(
            file = radiusTokensFile,
            resourceMap = { jsonObject ->
                RadiusResource(
                    radiusValue = jsonObject.getInt("resolvedValue"),
                )
            },
        ).sortedBy { it.value.radiusValue }
        println("✓ Loaded radius resources")

        val code = buildRadiusCode(
            scriptFilePath = "scripts/swiftui-radius-token-converter.main.kts",
            resources = radiusResources,
        )
        val outputFile = File(outputDir, "LemonadeRadius.swift")
        outputFile.writeText(code)
        println("✓ Radius file created")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert ${radiusTokensFile.name}: ${error.message}")
    }
}

private fun buildRadiusCode(
    scriptFilePath: String,
    resources: List<ResourceData<RadiusResource>>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design System Radius tokens.")
        appendLine("/// Sets a small, clear set of predefined radius values for UI elements to ensure")
        appendLine("/// consistent, scalable rounding across the product.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Radius token enum")
        appendLine("public enum LemonadeRadius {")
        resources.forEach { resource ->
            appendLine("    case ${resource.groupFullName?.replaceFirstChar { it.lowercase() }}")
        }
        appendLine()
        appendLine("    /// Returns the CGFloat value for this radius token")
        appendLine("    public var value: CGFloat {")
        appendLine("        switch self {")
        resources.forEach { resource ->
            appendLine("        case .${resource.groupFullName?.replaceFirstChar { it.lowercase() }}: return ${resource.value.radiusValue}")
        }
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    /// Returns a RoundedRectangle shape with this radius")
        appendLine("    public var shape: RoundedRectangle {")
        appendLine("        RoundedRectangle(cornerRadius: value)")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Protocol for radius values")
        appendLine("public protocol LemonadeRadiusValues {")
        resources.forEach { resource ->
            appendLine("    var ${resource.name}: CGFloat { get }")
        }
        appendLine("}")
        appendLine()
        appendLine("/// Protocol for shape values")
        appendLine("public protocol LemonadeShapes {")
        resources.forEach { resource ->
            appendLine("    var ${resource.name}: RoundedRectangle { get }")
        }
        appendLine("}")
        appendLine()
        appendLine("/// Default radius values implementation")
        appendLine("public struct LemonadeRadiusValuesImpl: LemonadeRadiusValues {")
        resources.forEach { resource ->
            appendLine("    public let ${resource.name}: CGFloat = LemonadeRadius.${resource.groupFullName?.replaceFirstChar { it.lowercase() }}.value")
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
        appendLine()
        appendLine("/// Default shapes implementation")
        appendLine("public struct LemonadeShapesImpl: LemonadeShapes {")
        resources.forEach { resource ->
            appendLine("    public var ${resource.name}: RoundedRectangle { LemonadeRadius.${resource.groupFullName?.replaceFirstChar { it.lowercase() }}.shape }")
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

main()
