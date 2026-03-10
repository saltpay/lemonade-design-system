#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

// === Pack Configuration ===

enum class PackType { ICON, FLAG, BRAND_LOGO }

data class PackConfig(
    val name: String,
    val sourceDir: String,
    val kmpEnumName: String,
    val swiftEnumName: String,
    val swiftFileName: String,
    val packType: PackType,
)

val packs = listOf(
    PackConfig("icons", "svg/icons", "LemonadeIcons", "LemonadeIcon", "LemonadeIcons.swift", PackType.ICON),
    PackConfig("flags", "svg/flags", "LemonadeCountryFlags", "LemonadeCountryFlag", "LemonadeCountryFlags.swift", PackType.FLAG),
    PackConfig("brandLogos", "svg/brandLogos", "LemonadeBrandLogos", "LemonadeBrandLogo", "LemonadeBrandLogos.swift", PackType.BRAND_LOGO),
)

// === rsvg-convert check ===

fun checkRsvgConvert() {
    try {
        val process = ProcessBuilder("rsvg-convert", "--version")
            .redirectErrorStream(true)
            .start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            error("rsvg-convert is not working properly. Install with: brew install librsvg (macOS) or apt-get install librsvg2-bin (Linux)")
        }
        println("✓ rsvg-convert available")
    } catch (e: Exception) {
        error("rsvg-convert not found. Install with: brew install librsvg (macOS) or apt-get install librsvg2-bin (Linux)")
    }
}

// === KMP Generation ===

private fun convertSvgToVectorDrawable(svgContent: String): String {
    val widthRegex = Regex("width=\"([^\"]*)")
    val heightRegex = Regex("height=\"([^\"]*)")
    val viewBoxRegex = Regex("viewBox=\"([^\"]*)")

    val width = widthRegex.find(svgContent)?.groupValues?.get(1)?.replace("px", "")?.toDoubleOrNull()?.toInt() ?: 24
    val height = heightRegex.find(svgContent)?.groupValues?.get(1)?.replace("px", "")?.toDoubleOrNull()?.toInt() ?: 24
    val viewBox = viewBoxRegex.find(svgContent)?.groupValues?.get(1)?.split(" ")

    val viewportWidth = viewBox?.getOrNull(2)?.toDoubleOrNull() ?: width.toDouble()
    val viewportHeight = viewBox?.getOrNull(3)?.toDoubleOrNull() ?: height.toDouble()

    val vectorDrawable = buildString {
        appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
        appendLine("<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"")
        appendLine("    android:width=\"${width}dp\"")
        appendLine("    android:height=\"${height}dp\"")
        appendLine("    android:viewportWidth=\"$viewportWidth\"")
        appendLine("    android:viewportHeight=\"$viewportHeight\">")

        val pathRegex = Regex("<path[^>]*d=\"([^\"]*)[^>]*>")
        val fillRegex = Regex("fill=\"([^\"]*)")
        val fillRuleRegex = Regex("fill-rule=\"([^\"]*)")

        pathRegex.findAll(svgContent).forEach { match ->
            val pathData = match.groupValues[1]
            val fillMatch = fillRegex.find(match.value)
            val fillRuleMatch = fillRuleRegex.find(match.value)

            appendLine("    <path")
            appendLine("        android:pathData=\"$pathData\"")

            val fillColor = fillMatch?.groupValues?.get(1) ?: "black"
            if (fillColor != "none") {
                val androidColor = when(fillColor) {
                    "black" -> "#FF000000"
                    "white" -> "#FFFFFFFF"
                    else -> fillColor
                }
                appendLine("        android:fillColor=\"$androidColor\"")
            }

            fillRuleMatch?.let {
                val fillRule = when (it.groupValues[1]) {
                    "evenodd" -> "evenOdd"
                    else -> "nonZero"
                }
                appendLine("        android:fillType=\"$fillRule\"")
            }

            appendLine("        />")
        }

        appendLine("</vector>")
    }

    return vectorDrawable
}

private fun generateKmpAssets(pack: PackConfig, svgFiles: List<File>) {
    val outputDir = File("kmp/ui/src/commonMain/composeResources/drawable")
    if (!outputDir.exists()) outputDir.mkdirs()

    val iconNames = mutableListOf<String>()

    svgFiles.forEach { svgFile ->
        try {
            val svgContent = svgFile.readText()
            val fileName = svgFile.nameWithoutExtension
            val vectorDrawable = convertSvgToVectorDrawable(svgContent)

            val outputFileName = fileName.replace("-", "_")
            val outputFile = File(outputDir, "gen_$outputFileName.xml")
            outputFile.writeText(vectorDrawable)

            iconNames.add(fileName)
        } catch (e: Exception) {
            println("✗ KMP: Failed to convert ${svgFile.name}: ${e.message}")
        }
    }

    if (iconNames.isNotEmpty()) {
        val sortedNames = iconNames.sorted()
        generateKmpEnum(pack, sortedNames)
        generateKmpExtension(pack, sortedNames)
        println("✓ KMP: Generated ${sortedNames.size} drawables + enum + extension for ${pack.kmpEnumName}")
    }
}

private fun generateKmpEnum(pack: PackConfig, iconNames: List<String>) {
    val coreFile = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core/${pack.kmpEnumName}.kt")

    val content = buildString {
        appendLine("package com.teya.lemonade.core")
        appendLine()
        appendLine("/**")
        appendLine(" * Auto-generated enum class containing all ${pack.name} converted from SVG files.")
        appendLine(" *")
        appendLine(" * ⚠️ **DO NOT MODIFY THIS FILE MANUALLY** ⚠️")
        appendLine(" *")
        appendLine(" * This file is automatically generated by the svg-asset-converter.main.kts script.")
        appendLine(" * All changes will be overwritten when the script is run again.")
        appendLine(" *")
        appendLine(" * To add new ${pack.name}:")
        appendLine(" * 1. Add your SVG file to the ${pack.sourceDir}/ directory")
        appendLine(" * 2. Run: kotlin scripts/svg-asset-converter.main.kts")
        appendLine(" *")
        appendLine(" * @generated by svg-asset-converter.main.kts")
        appendLine(" */")
        appendLine("public enum class ${pack.kmpEnumName} : LemonadeAsset {")

        iconNames.forEach { iconName ->
            val enumName = iconName.split("-").joinToString("") { it.replaceFirstChar { char -> char.uppercase() } }
            appendLine("    $enumName,")
        }

        appendLine("}")
    }

    coreFile.parentFile.mkdirs()
    coreFile.writeText(content)
}

private fun generateKmpExtension(pack: PackConfig, iconNames: List<String>) {
    val extensionFile = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade/${pack.kmpEnumName}Extension.kt")

    val content = buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import com.teya.lemonade.core.${pack.kmpEnumName}")
        appendLine("import org.jetbrains.compose.resources.DrawableResource")
        appendLine()
        appendLine("/**")
        appendLine(" * Auto-generated extension for ${pack.kmpEnumName} enum providing drawable resources.")
        appendLine(" *")
        appendLine(" * ⚠️ **DO NOT MODIFY THIS FILE MANUALLY** ⚠️")
        appendLine(" *")
        appendLine(" * This file is automatically generated by the svg-asset-converter.main.kts script.")
        appendLine(" * All changes will be overwritten when the script is run again.")
        appendLine(" *")
        appendLine(" * @generated by svg-asset-converter.main.kts")
        appendLine(" */")
        appendLine("@Suppress(\"MaxLineLength\")")
        appendLine("public val ${pack.kmpEnumName}.drawableResource: DrawableResource")
        appendLine("    get() = when (this) {")

        iconNames.forEach { iconName ->
            val enumName = iconName.split("-").joinToString("") { it.replaceFirstChar { char -> char.uppercase() } }
            val resourceName = "gen_" + iconName.replace("-", "_")
            appendLine("        ${pack.kmpEnumName}.$enumName -> LemonadeRes.drawable.$resourceName")
        }

        appendLine("    }")
    }

    extensionFile.parentFile.mkdirs()
    extensionFile.writeText(content)
}

// === SwiftUI Generation ===

private fun convertSvgToPdf(svgFile: File, pdfFile: File) {
    val process = ProcessBuilder("rsvg-convert", "-f", "pdf", "-o", pdfFile.absolutePath, svgFile.absolutePath)
        .redirectErrorStream(true)
        .start()
    val output = process.inputStream.bufferedReader().readText()
    val exitCode = process.waitFor()
    if (exitCode != 0) {
        error("rsvg-convert failed for ${svgFile.name}: $output")
    }
}

private fun generateSwiftAssets(pack: PackConfig, svgFiles: List<File>) {
    val outputDir = File("swiftui/Sources/Lemonade")
    val assetsDir = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")

    if (!outputDir.exists()) outputDir.mkdirs()
    if (!assetsDir.exists()) assetsDir.mkdirs()

    // Create root Contents.json for asset catalog
    val rootContentsJson = File(assetsDir, "Contents.json")
    rootContentsJson.writeText("""{
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
""")

    // Create imagesets with converted PDFs
    svgFiles.forEach { svgFile ->
        val name = svgFile.nameWithoutExtension
        val imagesetDir = File(assetsDir, "$name.imageset")
        if (!imagesetDir.exists()) imagesetDir.mkdirs()

        val pdfFile = File(imagesetDir, "$name.pdf")
        try {
            convertSvgToPdf(svgFile, pdfFile)
        } catch (e: Exception) {
            println("✗ SwiftUI: Failed to convert ${svgFile.name} to PDF: ${e.message}")
            return@forEach
        }

        // Write Contents.json based on pack type
        val contentsJson = File(imagesetDir, "Contents.json")
        contentsJson.writeText(buildImagesetContentsJson(pack.packType, "$name.pdf"))
    }

    // Generate Swift enum file
    val swiftCode = buildSwiftEnum(pack, svgFiles)
    val outputFile = File(outputDir, pack.swiftFileName)
    outputFile.writeText(swiftCode)

    println("✓ SwiftUI: Generated ${svgFiles.size} imagesets + ${pack.swiftFileName}")
}

private fun buildImagesetContentsJson(packType: PackType, pdfFileName: String): String {
    val properties = when (packType) {
        PackType.ICON -> """
  "properties" : {
    "preserves-vector-representation" : true,
    "template-rendering-intent" : "template"
  }"""
        PackType.FLAG, PackType.BRAND_LOGO -> """
  "properties" : {
    "preserves-vector-representation" : true
  }"""
    }

    return """{
  "images" : [
    {
      "filename" : "$pdfFileName",
      "idiom" : "universal"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  },
$properties
}
"""
}

private fun buildSwiftEnum(pack: PackConfig, svgFiles: List<File>): String {
    return when (pack.packType) {
        PackType.ICON -> buildIconSwiftEnum(pack, svgFiles)
        PackType.FLAG -> buildFlagSwiftEnum(pack, svgFiles)
        PackType.BRAND_LOGO -> buildBrandLogoSwiftEnum(pack, svgFiles)
    }
}

private fun buildIconSwiftEnum(pack: PackConfig, svgFiles: List<File>): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine("import UIKit")
        appendLine()
        appendLine("/// Lemonade Design System Icons.")
        appendLine("/// Icons are loaded from the asset catalog bundled with this module.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = "scripts/svg-asset-converter.main.kts").lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Icon token enum")
        appendLine("public enum ${pack.swiftEnumName}: String, CaseIterable {")
        svgFiles.forEach { file ->
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

private fun buildFlagSwiftEnum(pack: PackConfig, svgFiles: List<File>): String {
    return buildString {
        appendLine("import Foundation")
        appendLine()
        appendLine("/// Lemonade Design System Country Flags.")
        appendLine("/// Country flags are loaded from the asset catalog bundled with this module.")
        appendLine("/// Use the LemonadeUi.CountryFlag component to display flags.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = "scripts/svg-asset-converter.main.kts").lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Country flag token enum")
        appendLine("public enum ${pack.swiftEnumName}: String, CaseIterable {")
        svgFiles.forEach { file ->
            val enumCase = file.nameWithoutExtension
                .split("-")
                .joinToString("") { part -> part.replaceFirstChar { it.uppercase() } }
                .replaceFirstChar { it.lowercase() }
            appendLine("    case $enumCase = \"${file.nameWithoutExtension}\"")
        }
        appendLine()
        appendLine("    /// Returns the country code (first part of the flag name, e.g., \"PT\" from \"PT-portugal\")")
        appendLine("    public var countryCode: String {")
        appendLine("        let parts = rawValue.split(separator: \"-\")")
        appendLine("        return parts.first.map(String.init) ?? rawValue")
        appendLine("    }")
        appendLine()
        appendLine("    /// Returns the country name (derived from the flag name, e.g., \"Portugal\" from \"PT-portugal\")")
        appendLine("    public var countryName: String {")
        appendLine("        let parts = rawValue.split(separator: \"-\").dropFirst()")
        appendLine("        return parts.map { \$0.capitalized }.joined(separator: \" \")")
        appendLine("    }")
        appendLine("}")
    }
}

private fun buildBrandLogoSwiftEnum(pack: PackConfig, svgFiles: List<File>): String {
    return buildString {
        appendLine("import Foundation")
        appendLine()
        appendLine("/// Lemonade Design System Brand Logos.")
        appendLine("/// Brand logos are loaded from the asset catalog bundled with this module.")
        appendLine("/// Use the LemonadeUi.BrandLogo component to display brand logos.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = "scripts/svg-asset-converter.main.kts").lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Brand logo token enum")
        appendLine("public enum ${pack.swiftEnumName}: String, CaseIterable {")
        svgFiles.forEach { file ->
            val logoName = file.nameWithoutExtension
                .split("-", "_", " ")
                .mapIndexed { index, word ->
                    if (index == 0) word.lowercase()
                    else word.replaceFirstChar { it.uppercase() }
                }
                .joinToString("")
            appendLine("    case $logoName = \"${file.nameWithoutExtension}\"")
        }
        appendLine("}")
    }
}

// === Main entry point ===

fun main() {
    // Check rsvg-convert is available
    checkRsvgConvert()

    packs.forEach { pack ->
        println("=== Processing ${pack.name} ===")
        val svgDir = File(pack.sourceDir)
        if (!svgDir.exists() || !svgDir.isDirectory) {
            println("⚠️ Source directory does not exist: ${pack.sourceDir}, skipping")
            return@forEach
        }

        val svgFiles = svgDir.listFiles { file -> file.isFile && file.extension == "svg" }
            ?.sortedBy { it.nameWithoutExtension } ?: emptyList()

        if (svgFiles.isEmpty()) {
            println("⚠️ No SVG files found in ${pack.sourceDir}, skipping")
            return@forEach
        }

        println("Found ${svgFiles.size} SVG files")

        // KMP generation
        generateKmpAssets(pack, svgFiles)

        // SwiftUI generation
        generateSwiftAssets(pack, svgFiles)

        println("✓ ${pack.name} complete")
    }
    println("=== All packs processed ===")
}

main()
