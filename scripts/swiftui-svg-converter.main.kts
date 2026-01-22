#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

/**
 * This script generates Swift code for icon resources.
 * Icons are expected to be in PDF format in the svg/pdf/icons directory.
 * The generated code creates an enum with all available icons.
 * It also copies icons to the asset catalog in the proper iOS format.
 */

fun main() {
    val iconsDir = File("svg/pdf/icons")
    val outputDir = File("swiftui/Sources/Lemonade")
    val assetsDir = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Check if icons directory exists
        if (!iconsDir.exists() || !iconsDir.isDirectory) {
            println("⚠️ Icons directory does not exist yet: $iconsDir")
            println("Creating placeholder icon extension file...")

            // Create a placeholder file that can be filled in later
            val placeholderCode = buildPlaceholderIconCode(
                scriptFilePath = "scripts/swiftui-svg-converter.main.kts"
            )
            val outputFile = File(outputDir, "LemonadeIcons.swift")
            outputFile.writeText(placeholderCode)
            println("✓ Placeholder icon file created")
            return
        }

        // Get all SVG/PDF files
        val iconFiles = iconsDir.listFiles { file ->
            file.isFile && (file.extension == "svg" || file.extension == "pdf")
        }?.sortedBy { it.nameWithoutExtension } ?: emptyList()

        if (iconFiles.isEmpty()) {
            println("⚠️ No icon files found in: $iconsDir")
            val placeholderCode = buildPlaceholderIconCode(
                scriptFilePath = "scripts/swiftui-svg-converter.main.kts"
            )
            val outputFile = File(outputDir, "LemonadeIcons.swift")
            outputFile.writeText(placeholderCode)
            println("✓ Placeholder icon file created")
            return
        }

        println("✓ Found ${iconFiles.size} icon files")

        // Generate Swift code
        val code = buildIconCode(
            scriptFilePath = "scripts/swiftui-svg-converter.main.kts",
            iconFiles = iconFiles,
        )
        val outputFile = File(outputDir, "LemonadeIcons.swift")
        outputFile.writeText(code)
        println("✓ Icons Swift file created")

        // Create asset catalog structure
        createAssetCatalog(assetsDir, iconFiles)
        println("✓ Asset catalog created with ${iconFiles.size} icons")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert icons: ${error.message}")
        error.printStackTrace()
    }
}

private fun createAssetCatalog(assetsDir: File, iconFiles: List<File>) {
    // Create Assets.xcassets directory if it doesn't exist
    if (!assetsDir.exists()) {
        assetsDir.mkdirs()
    }

    // Create root Contents.json
    val rootContentsJson = File(assetsDir, "Contents.json")
    rootContentsJson.writeText("""{
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
""")

    // Create imageset for each icon
    iconFiles.forEach { iconFile ->
        val iconName = iconFile.nameWithoutExtension
        val imagesetDir = File(assetsDir, "$iconName.imageset")

        if (!imagesetDir.exists()) {
            imagesetDir.mkdirs()
        }

        // Copy the PDF file to the imageset
        val destFile = File(imagesetDir, iconFile.name)
        iconFile.copyTo(destFile, overwrite = true)

        // Create Contents.json for the imageset
        val contentsJson = File(imagesetDir, "Contents.json")
        contentsJson.writeText("""{
  "images" : [
    {
      "filename" : "${iconFile.name}",
      "idiom" : "universal"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  },
  "properties" : {
    "preserves-vector-representation" : true,
    "template-rendering-intent" : "template"
  }
}
""")
    }
}

private fun buildPlaceholderIconCode(
    scriptFilePath: String,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine()
        appendLine("/// Lemonade Design System Icons.")
        appendLine("/// Icons are loaded from the asset catalog.")
        appendLine("///")
        appendLine("/// Note: This is a placeholder file. Add PDF icons to svg/pdf/icons/ directory")
        appendLine("/// and run the converter script to generate the full implementation.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Icon token enum")
        appendLine("public enum LemonadeIcon: String, CaseIterable {")
        appendLine("    // Placeholder - icons will be added when PDF files are available")
        appendLine("    case placeholder = \"placeholder\"")
        appendLine()
        appendLine("    /// Returns the Image for this icon")
        appendLine("    /// Note: Icons must be added to your app's asset catalog with matching names")
        appendLine("    public var image: Image {")
        appendLine("        Image(rawValue, bundle: .lemonade)")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Icon size enum")
        appendLine("public enum LemonadeIconSize {")
        appendLine("    case small")
        appendLine("    case medium")
        appendLine("    case large")
        appendLine()
        appendLine("    public var value: CGFloat {")
        appendLine("        switch self {")
        appendLine("        case .small: return 16")
        appendLine("        case .medium: return 24")
        appendLine("        case .large: return 32")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("public extension View {")
        appendLine("    /// Applies Lemonade icon styling")
        appendLine("    func lemonadeIcon(size: LemonadeIconSize = .medium, color: Color? = nil) -> some View {")
        appendLine("        self")
        appendLine("            .frame(width: size.value, height: size.value)")
        appendLine("            .foregroundColor(color)")
        appendLine("    }")
        appendLine("}")
    }
}

private fun buildIconCode(
    scriptFilePath: String,
    iconFiles: List<File>,
): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine("import UIKit")
        appendLine()
        appendLine("/// Lemonade Design System Icons.")
        appendLine("/// Icons are loaded from the asset catalog bundled with this module.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Icon token enum")
        appendLine("public enum LemonadeIcon: String, CaseIterable {")
        iconFiles.forEach { file ->
            val iconName = file.nameWithoutExtension
                .split("-", "_", " ")
                .mapIndexed { index, word ->
                    if (index == 0) word.lowercase()
                    else word.replaceFirstChar { it.uppercase() }
                }
                .joinToString("")
            appendLine("    case $iconName = \"${file.nameWithoutExtension}\"")
        }
        appendLine()
        appendLine("    /// Returns the Image for this icon from the Lemonade bundle's asset catalog")
        appendLine("    public var image: Image {")
        appendLine("        if let uiImage = UIImage(named: rawValue, in: .lemonade, compatibleWith: nil) {")
        appendLine("            return Image(uiImage: uiImage).renderingMode(.template)")
        appendLine("        }")
        appendLine("        return Image(systemName: \"questionmark.square\")")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("/// Icon size enum")
        appendLine("public enum LemonadeIconSize {")
        appendLine("    case small")
        appendLine("    case medium")
        appendLine("    case large")
        appendLine()
        appendLine("    public var value: CGFloat {")
        appendLine("        switch self {")
        appendLine("        case .small: return 16")
        appendLine("        case .medium: return 24")
        appendLine("        case .large: return 32")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("public extension View {")
        appendLine("    /// Applies Lemonade icon styling")
        appendLine("    func lemonadeIcon(size: LemonadeIconSize = .medium, color: Color? = nil) -> some View {")
        appendLine("        self")
        appendLine("            .frame(width: size.value, height: size.value)")
        appendLine("            .foregroundColor(color)")
        appendLine("    }")
        appendLine("}")
    }
}

main()
