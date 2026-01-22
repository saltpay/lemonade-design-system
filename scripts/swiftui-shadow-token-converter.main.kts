#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

data class ShadowResourceValue(
    val floatValue: Float,
)

data class ShadowResourceGroup(
    val groupName: String,
    val levels: List<ShadowResource>,
)

data class ShadowResource(
    val blur: Float = 0f,
    val spread: Float = 0f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
)

fun main() {
    val shadowTokensFile = File("tokens/shadow.json")
    val outputDir = File("swiftui/Sources/Lemonade")

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
                ShadowResourceValue(
                    floatValue = jsonObject.optInt("resolvedValue")
                        ?.toFloat()
                        ?: jsonObject.getFloat("resolvedValue"),
                )
            },
        ).toShadowResource()
        println("✓ Loaded shadow resources")

        val code = buildShadowCode(
            scriptFilePath = "scripts/swiftui-shadow-token-converter.main.kts",
            resources = shadowResources,
        )
        val outputFile = File(outputDir, "LemonadeShadow.swift")
        outputFile.writeText(code)
        println("✓ Shadow file created")

        println("✓ Implementation generated")
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
                    else -> levelResource
                }
            }
            levelResource
        }
        ShadowResourceGroup(
            groupName = groupName,
            levels = levels,
        )
    }
}

private fun buildShadowCode(
    scriptFilePath: String,
    resources: List<ShadowResourceGroup>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design System Shadow tokens.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Shadow data structure")
        appendLine("public struct LemonadeShadowData {")
        appendLine("    public let blur: CGFloat")
        appendLine("    public let spread: CGFloat")
        appendLine("    public let offsetX: CGFloat")
        appendLine("    public let offsetY: CGFloat")
        appendLine()
        appendLine("    public init(blur: CGFloat, spread: CGFloat, offsetX: CGFloat, offsetY: CGFloat) {")
        appendLine("        self.blur = blur")
        appendLine("        self.spread = spread")
        appendLine("        self.offsetX = offsetX")
        appendLine("        self.offsetY = offsetY")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Shadow token enum")
        appendLine("public enum LemonadeShadow {")
        resources.forEach { resource ->
            appendLine("    case ${resource.groupName.replaceFirstChar { it.lowercase() }}")
        }
        appendLine("    case none")
        appendLine()
        appendLine("    /// Returns the shadow data layers for this shadow token")
        appendLine("    public var shadowLayers: [LemonadeShadowData] {")
        appendLine("        switch self {")
        resources.forEach { resource ->
            appendLine("        case .${resource.groupName.replaceFirstChar { it.lowercase() }}:")
            appendLine("            return [")
            resource.levels.forEach { level ->
                appendLine("                LemonadeShadowData(blur: ${level.blur}, spread: ${level.spread}, offsetX: ${level.offsetX}, offsetY: ${level.offsetY}),")
            }
            appendLine("            ]")
        }
        appendLine("        case .none:")
        appendLine("            return [LemonadeShadowData(blur: 0, spread: 0, offsetX: 0, offsetY: 0)]")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// View modifier for applying Lemonade shadows")
        appendLine("public struct LemonadeShadowModifier: ViewModifier {")
        appendLine("    let shadow: LemonadeShadow")
        appendLine("    let color: Color")
        appendLine()
        appendLine("    public init(shadow: LemonadeShadow, color: Color = Color(red: 21/255, green: 34/255, blue: 21/255).opacity(0.18)) {")
        appendLine("        self.shadow = shadow")
        appendLine("        self.color = color")
        appendLine("    }")
        appendLine()
        appendLine("    public func body(content: Content) -> some View {")
        appendLine("        shadow.shadowLayers.reduce(AnyView(content)) { view, shadowData in")
        appendLine("            AnyView(")
        appendLine("                view.shadow(")
        appendLine("                    color: color,")
        appendLine("                    radius: shadowData.blur / 2,")
        appendLine("                    x: shadowData.offsetX,")
        appendLine("                    y: shadowData.offsetY")
        appendLine("                )")
        appendLine("            )")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("public extension View {")
        appendLine("    /// Applies a Lemonade shadow to the view")
        appendLine("    func lemonadeShadow(_ shadow: LemonadeShadow, color: Color = Color(red: 21/255, green: 34/255, blue: 21/255).opacity(0.18)) -> some View {")
        appendLine("        modifier(LemonadeShadowModifier(shadow: shadow, color: color))")
        appendLine("    }")
        appendLine("}")
    }
}

main()
