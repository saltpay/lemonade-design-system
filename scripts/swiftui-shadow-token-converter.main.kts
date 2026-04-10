#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")
@file:Import("shadow-token-commons.main.kts")

import java.io.File

data class ShadowColorValue(
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float,
)

data class ShadowResourceValue(
    val floatValue: Float? = null,
    val colorValue: ShadowColorValue? = null,
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
    val color: ShadowColorValue = ShadowColorValue(r = 0f, g = 0f, b = 0f, a = 0.10f),
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
                val resolvedValue = jsonObject.get("resolvedValue")
                when {
                    resolvedValue is Number -> ShadowResourceValue(floatValue = resolvedValue.toFloat())
                    resolvedValue is org.json.JSONObject -> ShadowResourceValue(
                        colorValue = ShadowColorValue(
                            r = resolvedValue.getDouble("r").toFloat(),
                            g = resolvedValue.getDouble("g").toFloat(),
                            b = resolvedValue.getDouble("b").toFloat(),
                            a = resolvedValue.getDouble("a").toFloat(),
                        )
                    )
                    else -> ShadowResourceValue()
                }
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
    } catch (error: Throwable) {
        println("✗ Failed to convert ${shadowTokensFile.name}: ${error.message}")
    }
}

private fun List<ResourceData<ShadowResourceValue>>.toShadowResource(): List<ShadowResourceGroup> {
    val groups = groupBy { resourceData -> resourceData.groups[1] }
    return groups
        .entries
        .sortedBy { (groupName, _) -> shadowGroupOrder.indexOf(groupName).takeIf { it >= 0 } ?: Int.MAX_VALUE }
        .map { (groupName, resources) ->
            val groupLevels = resources.groupBy { groupResource -> groupResource.groups[2] }
            val levels = groupLevels.map { (_, levelResources) ->
                var levelResource = ShadowResource()
                levelResources.forEach { resourceValue ->
                    levelResource = when {
                        resourceValue.name.contains("Blur") -> levelResource.copy(blur = resourceValue.value.floatValue ?: levelResource.blur)
                        resourceValue.name.contains("Spread") -> levelResource.copy(spread = resourceValue.value.floatValue ?: levelResource.spread)
                        resourceValue.name.contains("OffsetX") -> levelResource.copy(offsetX = resourceValue.value.floatValue ?: levelResource.offsetX)
                        resourceValue.name.contains("OffsetY") -> levelResource.copy(offsetY = resourceValue.value.floatValue ?: levelResource.offsetY)
                        resourceValue.name.contains("Color") -> levelResource.copy(color = resourceValue.value.colorValue ?: levelResource.color)
                        else -> levelResource
                    }
                }
                levelResource
            }
            ShadowResourceGroup(groupName = groupName, levels = levels)
        }
}

private fun String.toDisplayName(): String = when (lowercase()) {
    "xsmall" -> "X-Small"
    "xlarge" -> "X-Large"
    else -> replaceFirstChar { it.uppercase() }
}

private fun ShadowColorValue.toSwiftColor(): String {
    fun fmt(v: Float) = String.format(java.util.Locale.US, "%.2f", v)
    return "Color(red: ${fmt(r)}, green: ${fmt(g)}, blue: ${fmt(b)}, opacity: ${fmt(a)})"
}

// The modifier body is static — no token data is interpolated — so it is kept
// as a single string rather than per-line appendLine() calls.
// Single GeometryReader measures content once; ForEach renders each layer
// independently so shadows don't compound (each shadows the original shape,
// matching Figma's independent layer model). No drawingGroup() in the mask —
// content is already flattened by compositingGroup().
private val swiftModifierCode = """
    |/// View modifier for applying Lemonade shadows
    |public struct LemonadeShadowModifier: ViewModifier {
    |    let shadow: LemonadeShadow
    |
    |    public init(shadow: LemonadeShadow) {
    |        self.shadow = shadow
    |    }
    |
    |    public func body(content: Content) -> some View {
    |        let composited = content.compositingGroup()
    |        return composited.background {
    |            GeometryReader { proxy in
    |                ForEach(Array(shadow.shadowLayers.enumerated()), id: \.offset) { _, layer in
    |                    let spreadW = max(0, proxy.size.width + layer.spread * 2)
    |                    let spreadH = max(0, proxy.size.height + layer.spread * 2)
    |                    let scaleX = proxy.size.width == 0 ? 1 : spreadW / proxy.size.width
    |                    let scaleY = proxy.size.height == 0 ? 1 : spreadH / proxy.size.height
    |                    Rectangle()
    |                        .fill(layer.color)
    |                        .frame(width: spreadW, height: spreadH)
    |                        .mask {
    |                            composited
    |                                .scaleEffect(x: scaleX, y: scaleY)
    |                        }
    |                        .offset(x: layer.offsetX - layer.spread, y: layer.offsetY - layer.spread)
    |                        .blur(radius: layer.blur / 2)
    |                        .overlay(alignment: .topLeading) {
    |                            composited
    |                                .frame(width: proxy.size.width, height: proxy.size.height)
    |                                .blendMode(.destinationOut)
    |                        }
    |                        .compositingGroup()
    |                }
    |            }
    |        }
    |    }
    |}
    |
    |public extension View {
    |    /// Applies a Lemonade shadow to the view
    |    func lemonadeShadow(_ shadow: LemonadeShadow) -> some View {
    |        modifier(LemonadeShadowModifier(shadow: shadow))
    |    }
    |}
    """.trimMargin()

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
        appendLine("public struct LemonadeShadowData: Sendable {")
        appendLine("    public let blur: CGFloat")
        appendLine("    public let spread: CGFloat")
        appendLine("    public let offsetX: CGFloat")
        appendLine("    public let offsetY: CGFloat")
        appendLine("    public let color: Color")
        appendLine()
        appendLine("    public init(blur: CGFloat, spread: CGFloat, offsetX: CGFloat, offsetY: CGFloat, color: Color) {")
        appendLine("        self.blur = blur")
        appendLine("        self.spread = spread")
        appendLine("        self.offsetX = offsetX")
        appendLine("        self.offsetY = offsetY")
        appendLine("        self.color = color")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Shadow token enum")
        appendLine("public enum LemonadeShadow: CaseIterable, Sendable {")
        resources.forEach { resource ->
            appendLine("    case ${resource.groupName.replaceFirstChar { it.lowercase() }}")
        }
        appendLine("    case none")
        appendLine()
        appendLine("    public var displayName: String {")
        appendLine("        switch self {")
        resources.forEach { resource ->
            val caseName = resource.groupName.replaceFirstChar { it.lowercase() }
            appendLine("        case .${caseName}: return \"${caseName.toDisplayName()}\"")
        }
        appendLine("        case .none: return \"None\"")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    /// Returns the shadow data layers for this shadow token")
        appendLine("    public var shadowLayers: [LemonadeShadowData] {")
        appendLine("        switch self {")
        resources.forEach { resource ->
            val caseName = resource.groupName.replaceFirstChar { it.lowercase() }
            appendLine("        case .${caseName}:")
            appendLine("            return [")
            resource.levels.forEach { level ->
                appendLine("                LemonadeShadowData(blur: ${level.blur}, spread: ${level.spread}, offsetX: ${level.offsetX}, offsetY: ${level.offsetY}, color: ${level.color.toSwiftColor()}),")
            }
            appendLine("            ]")
        }
        appendLine("        case .none:")
        appendLine("            return [LemonadeShadowData(blur: 0, spread: 0, offsetX: 0, offsetY: 0, color: .clear)]")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        append(swiftModifierCode)
    }
}

main()
