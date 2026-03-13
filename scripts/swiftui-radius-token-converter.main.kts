#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

private data class RadiusResource(
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
    val primitiveResources = resources.filter { it.groups.isEmpty() }
    val groupedResources = resources
        .filter { it.groups.isNotEmpty() }
        .filter { it.groupFullName != null }
        .groupBy { it.groupFullName!! }

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
        primitiveResources.forEach { resource ->
            appendLine("    case ${resource.name}")
        }
        appendLine()
        appendLine("    /// Returns the CGFloat value for this radius token")
        appendLine("    public var value: CGFloat {")
        appendLine("        switch self {")
        primitiveResources.forEach { resource ->
            appendLine("        case .${resource.name}: return ${resource.value.radiusValue}")
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

        // Sub-protocols per semantic group
        groupedResources.forEach { (groupName, groupTokens) ->
            appendLine("/// ${groupName} radius values")
            appendLine("public protocol ${groupName}RadiusValues {")
            groupTokens.forEach { token ->
                appendLine("    var ${token.name}: CGFloat { get }")
            }
            appendLine("}")
            appendLine()
            appendLine("/// ${groupName} shape values")
            appendLine("public protocol ${groupName}Shapes {")
            groupTokens.forEach { token ->
                appendLine("    var ${token.name}: RoundedRectangle { get }")
            }
            appendLine("}")
            appendLine()
        }

        // Root protocols: primitives flat + semantic nested
        appendLine("/// Protocol for radius values")
        appendLine("public protocol LemonadeRadiusValues {")
        primitiveResources.forEach { resource ->
            appendLine("    var ${resource.name}: CGFloat { get }")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    var ${groupName.replaceFirstChar { it.lowercase() }}: ${groupName}RadiusValues { get }")
        }
        appendLine("}")
        appendLine()
        appendLine("/// Protocol for shape values")
        appendLine("public protocol LemonadeShapes {")
        primitiveResources.forEach { resource ->
            appendLine("    var ${resource.name}: RoundedRectangle { get }")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    var ${groupName.replaceFirstChar { it.lowercase() }}: ${groupName}Shapes { get }")
        }
        appendLine("}")
        appendLine()

        // Sub-impl structs
        groupedResources.forEach { (groupName, groupTokens) ->
            appendLine("internal struct ${groupName}RadiusValuesImpl: ${groupName}RadiusValues {")
            groupTokens.forEach { token ->
                appendLine("    let ${token.name}: CGFloat = ${token.value.radiusValue}")
            }
            appendLine("}")
            appendLine()
            appendLine("internal struct ${groupName}ShapesImpl: ${groupName}Shapes {")
            groupTokens.forEach { token ->
                appendLine("    var ${token.name}: RoundedRectangle { RoundedRectangle(cornerRadius: ${token.value.radiusValue}) }")
            }
            appendLine("}")
            appendLine()
        }

        // Root impl structs
        appendLine("/// Default radius values implementation")
        appendLine("public struct LemonadeRadiusValuesImpl: LemonadeRadiusValues {")
        primitiveResources.forEach { resource ->
            appendLine("    public let ${resource.name}: CGFloat = LemonadeRadius.${resource.name}.value")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    public let ${groupName.replaceFirstChar { it.lowercase() }}: ${groupName}RadiusValues = ${groupName}RadiusValuesImpl()")
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
        appendLine()
        appendLine("/// Default shapes implementation")
        appendLine("public struct LemonadeShapesImpl: LemonadeShapes {")
        primitiveResources.forEach { resource ->
            appendLine("    public var ${resource.name}: RoundedRectangle { LemonadeRadius.${resource.name}.shape }")
        }
        groupedResources.forEach { (groupName, _) ->
            appendLine("    public let ${groupName.replaceFirstChar { it.lowercase() }}: ${groupName}Shapes = ${groupName}ShapesImpl()")
        }
        appendLine()
        appendLine("    public init() {}")
        appendLine("}")
    }
}

main()
