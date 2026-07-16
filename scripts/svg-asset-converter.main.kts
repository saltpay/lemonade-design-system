#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File
import java.security.MessageDigest
import org.json.JSONObject

// === Name conversion utilities ===

fun String.toPascalCase(vararg delimiters: String = arrayOf("-")): String =
    split(*delimiters).joinToString("") { it.replaceFirstChar { char -> char.uppercase() } }

fun String.toCamelCase(vararg delimiters: String = arrayOf("-", "_", " ")): String =
    split(*delimiters).mapIndexed { index, word ->
        if (index == 0) word.lowercase()
        else word.replaceFirstChar { it.uppercase() }
    }.joinToString("")

// === Pack Configuration ===

enum class PackType { ICON, FLAG, BRAND_LOGO }

data class PackConfig(
    val name: String,
    val sourceDir: String,
    val kmpEnumName: String,
    val swiftEnumName: String,
    val swiftFileName: String,
    val packType: PackType,
    /**
     * When true, files in [sourceDir] named `<name>-dark.svg` are treated as the dark-theme
     * variant of `<name>.svg` rather than as assets in their own right, and are kept out of
     * the generated enums. Assets without a dark variant fall back to their light one.
     *
     * This makes `-dark` a reserved suffix for the pack, so leave it false for packs whose
     * asset names may legitimately end in it.
     */
    val supportsDark: Boolean = false,
    /**
     * Superseded assets, as `old asset name` -> `replacement asset name`. Both files must stay
     * in [sourceDir]: the old one still generates its entry and drawables, so already-published
     * enum entries keep working, but the entry is marked deprecated and points callers at the
     * replacement. Removing the old asset instead would delete a public enum entry, which is a
     * breaking change.
     */
    val deprecations: Map<String, String> = emptyMap(),
)

/** File name suffix marking a dark-theme variant, for packs with `supportsDark`. */
val DARK_SUFFIX = "-dark"

val packs = listOf(
    PackConfig("icons", "svg/icons", "LemonadeIcons", "LemonadeIcon", "LemonadeIcons.swift", PackType.ICON),
    PackConfig("flags", "svg/flags", "LemonadeCountryFlags", "LemonadeCountryFlag", "LemonadeCountryFlags.swift", PackType.FLAG),
    PackConfig(
        "brandLogos",
        "svg/brandLogos",
        "LemonadeBrandLogos",
        "LemonadeBrandLogo",
        "LemonadeBrandLogos.swift",
        PackType.BRAND_LOGO,
        supportsDark = true,
        // "dinners" is a misspelling of the Diners Club brand. The correctly spelled asset
        // supersedes it; the old one stays so the published Dinners entry keeps working.
        deprecations = mapOf("dinners" to "diners"),
    ),
)

// === Content-hash cache ===

val cacheFile = File(".cache/svg-converter-hashes.json")

fun sha256(file: File): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = file.readBytes()
    return digest.digest(bytes).joinToString("") { "%02x".format(it) }
}

fun loadCache(): MutableMap<String, String> {
    if (!cacheFile.exists()) return mutableMapOf()
    return try {
        val json = JSONObject(cacheFile.readText())
        json.keySet().associateWith { json.getString(it) }.toMutableMap()
    } catch (e: Exception) {
        println("⚠️ Failed to load cache, starting fresh: ${e.message}")
        mutableMapOf()
    }
}

fun saveCache(cache: Map<String, String>) {
    cacheFile.parentFile.mkdirs()
    cacheFile.writeText(JSONObject(cache.toSortedMap()).toString(2))
}

// === Pre-compiled regexes for SVG parsing ===

private val svgWidthRegex = Regex("width=\"([^\"]*)")
private val svgHeightRegex = Regex("height=\"([^\"]*)")
private val svgViewBoxRegex = Regex("viewBox=\"([^\"]*)")
private val svgPathRegex = Regex("<path[^>]*d=\"([^\"]*)[^>]*>")
private val svgFillRegex = Regex("fill=\"([^\"]*)")
private val svgFillRuleRegex = Regex("fill-rule=\"([^\"]*)")

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
    val width = svgWidthRegex.find(svgContent)?.groupValues?.get(1)?.replace("px", "")?.toDoubleOrNull()?.toInt() ?: 24
    val height = svgHeightRegex.find(svgContent)?.groupValues?.get(1)?.replace("px", "")?.toDoubleOrNull()?.toInt() ?: 24
    val viewBox = svgViewBoxRegex.find(svgContent)?.groupValues?.get(1)?.split(" ")

    val viewportWidth = viewBox?.getOrNull(2)?.toDoubleOrNull() ?: width.toDouble()
    val viewportHeight = viewBox?.getOrNull(3)?.toDoubleOrNull() ?: height.toDouble()

    return buildString {
        appendLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
        appendLine("<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"")
        appendLine("    android:width=\"${width}dp\"")
        appendLine("    android:height=\"${height}dp\"")
        appendLine("    android:viewportWidth=\"$viewportWidth\"")
        appendLine("    android:viewportHeight=\"$viewportHeight\">")

        svgPathRegex.findAll(svgContent).forEach { match ->
            val pathData = match.groupValues[1]
            val fillMatch = svgFillRegex.find(match.value)
            val fillRuleMatch = svgFillRuleRegex.find(match.value)

            appendLine("    <path")
            appendLine("        android:pathData=\"$pathData\"")

            val fillColor = fillMatch?.groupValues?.get(1) ?: "black"
            if (fillColor != "none") {
                val androidColor = when (fillColor) {
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
}

private fun generateKmpAssets(
    pack: PackConfig,
    svgFiles: List<File>,
    darkSvgFiles: Map<String, File>,
    changedFiles: Set<String>,
) {
    val outputDir = File("kmp/ui/src/commonMain/composeResources/drawable")
    outputDir.mkdirs()

    val iconNames = mutableListOf<String>()
    var convertedCount = 0

    svgFiles.forEach { svgFile ->
        try {
            val fileName = svgFile.nameWithoutExtension
            iconNames.add(fileName)

            val outputFileName = fileName.replace("-", "_")
            val outputFile = File(outputDir, "gen_$outputFileName.xml")

            if (svgFile.path !in changedFiles && outputFile.exists()) return@forEach

            val svgContent = svgFile.readText()
            val vectorDrawable = convertSvgToVectorDrawable(svgContent)
            outputFile.writeText(vectorDrawable)
            convertedCount++
        } catch (e: Exception) {
            println("✗ KMP: Failed to convert ${svgFile.name}: ${e.message}")
        }
    }

    darkSvgFiles.forEach { (name, svgFile) ->
        try {
            val outputFile = File(outputDir, "gen_${kmpDarkResourceSuffix(name)}.xml")

            if (svgFile.path !in changedFiles && outputFile.exists()) return@forEach

            val vectorDrawable = convertSvgToVectorDrawable(svgFile.readText())
            outputFile.writeText(vectorDrawable)
            convertedCount++
        } catch (e: Exception) {
            println("✗ KMP: Failed to convert dark variant ${svgFile.name}: ${e.message}")
        }
    }

    // Drop stale dark drawables whose source SVG was removed.
    if (pack.supportsDark) {
        svgFiles.forEach { svgFile ->
            val name = svgFile.nameWithoutExtension
            if (name !in darkSvgFiles) {
                File(outputDir, "gen_${kmpDarkResourceSuffix(name)}.xml").delete()
            }
        }
    }

    if (iconNames.isNotEmpty()) {
        val sortedNames = iconNames.sorted()
        val total = sortedNames.size + darkSvgFiles.size
        generateKmpEnum(pack, sortedNames)
        generateKmpExtension(pack, sortedNames, darkSvgFiles.keys)
        println("✓ KMP: Converted $convertedCount/$total drawables (${total - convertedCount} cached) + enum + extension for ${pack.kmpEnumName}")
    }
}

private fun kmpDarkResourceSuffix(name: String): String = "${name.replace("-", "_")}_dark"

/**
 * Fails the run if a dark-theme variant reached the asset list of a [PackConfig.supportsDark] pack.
 *
 * The enums generated from these names are published API: a leaked variant would add an entry
 * (e.g. `VisaDark`) that consumers could select by hand, defeating the theme-driven resolution.
 * Worse, it would land silently - the API classifier reads a new entry as ADDITIONS_ONLY, and
 * removing it afterwards is a breaking change. Cheaper to stop here.
 */
private fun requireNoDarkVariantsLeaked(pack: PackConfig, assetNames: List<String>) {
    if (!pack.supportsDark) return
    val leaked = assetNames.filter { it.endsWith(DARK_SUFFIX) }
    if (leaked.isNotEmpty()) {
        error(
            "${pack.name}: dark variant(s) reached the ${pack.kmpEnumName} asset list: " +
                "${leaked.joinToString { "$it.svg" }}. They must be paired to a light asset, " +
                "not generated as entries of their own."
        )
    }
}

private fun generateKmpEnum(pack: PackConfig, iconNames: List<String>) {
    requireNoDarkVariantsLeaked(pack, iconNames)

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

        iconNames.forEachIndexed { index, iconName ->
            pack.deprecations[iconName]?.let { replacement ->
                // ktlint's spacing-between-declarations-with-annotations wants a blank line
                // ahead of an annotated entry, but not one straight after the opening brace.
                if (index > 0) appendLine()
                appendLine("    @Deprecated(")
                appendLine("        message = \"Superseded by ${replacement.toPascalCase()}.\",")
                appendLine("        replaceWith = ReplaceWith(\"${replacement.toPascalCase()}\"),")
                appendLine("    )")
            }
            appendLine("    ${iconName.toPascalCase()},")
        }

        appendLine("}")
    }

    coreFile.parentFile.mkdirs()
    coreFile.writeText(content)
}

/**
 * The `when` branches below map every enum entry, including any deprecated ones, so the
 * generated file has to opt out of its own deprecation warnings.
 */
private fun suppressAnnotation(pack: PackConfig): String {
    val names = buildList {
        add("MaxLineLength")
        if (pack.deprecations.isNotEmpty()) add("DEPRECATION")
    }
    return "@Suppress(${names.joinToString { "\"$it\"" }})"
}

private fun generateKmpExtension(pack: PackConfig, iconNames: List<String>, darkNames: Set<String> = emptySet()) {
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
        appendLine(suppressAnnotation(pack))
        appendLine("public val ${pack.kmpEnumName}.drawableResource: DrawableResource")
        appendLine("    get() = when (this) {")

        iconNames.forEach { iconName ->
            val resourceName = "gen_" + iconName.replace("-", "_")
            appendLine("        ${pack.kmpEnumName}.${iconName.toPascalCase()} -> LemonadeRes.drawable.$resourceName")
        }

        appendLine("    }")

        if (pack.supportsDark) {
            appendLine()
            appendLine("/**")
            appendLine(" * Auto-generated extension for ${pack.kmpEnumName} enum providing dark-theme drawable resources.")
            appendLine(" *")
            appendLine(" * Entries without a `<name>$DARK_SUFFIX.svg` in ${pack.sourceDir}/ resolve to their light")
            appendLine(" * drawable, which is the correct rendering for assets that read equally well on")
            appendLine(" * both surfaces.")
            appendLine(" *")
            appendLine(" * ⚠️ **DO NOT MODIFY THIS FILE MANUALLY** ⚠️")
            appendLine(" *")
            appendLine(" * This file is automatically generated by the svg-asset-converter.main.kts script.")
            appendLine(" * All changes will be overwritten when the script is run again.")
            appendLine(" *")
            appendLine(" * @generated by svg-asset-converter.main.kts")
            appendLine(" */")
            appendLine(suppressAnnotation(pack))
            appendLine("public val ${pack.kmpEnumName}.darkDrawableResource: DrawableResource")
            appendLine("    get() = when (this) {")

            iconNames.forEach { iconName ->
                val resourceName = if (iconName in darkNames) {
                    "gen_" + kmpDarkResourceSuffix(iconName)
                } else {
                    "gen_" + iconName.replace("-", "_")
                }
                appendLine("        ${pack.kmpEnumName}.${iconName.toPascalCase()} -> LemonadeRes.drawable.$resourceName")
            }

            appendLine("    }")
        }
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

private fun ensureAssetCatalogRoot(assetsDir: File) {
    assetsDir.mkdirs()
    val rootContentsJson = File(assetsDir, "Contents.json")
    rootContentsJson.writeText("""{
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
""")
}

private fun generateSwiftAssets(
    pack: PackConfig,
    svgFiles: List<File>,
    darkSvgFiles: Map<String, File>,
    changedFiles: Set<String>,
) {
    val outputDir = File("swiftui/Sources/Lemonade")
    val assetsDir = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")

    outputDir.mkdirs()

    var convertedCount = 0

    // Create imagesets with converted PDFs
    svgFiles.forEach { svgFile ->
        val name = svgFile.nameWithoutExtension
        val imagesetDir = File(assetsDir, "$name.imageset")
        val pdfFile = File(imagesetDir, "$name.pdf")
        val darkSvgFile = darkSvgFiles[name]
        val darkPdfFile = File(imagesetDir, "$name-dark.pdf")

        val lightStale = svgFile.path in changedFiles || !pdfFile.exists()
        val darkStale = darkSvgFile != null && (darkSvgFile.path in changedFiles || !darkPdfFile.exists())
        // A dark variant that was removed leaves an orphan PDF behind; rewrite the imageset.
        val darkOrphaned = darkSvgFile == null && darkPdfFile.exists()

        if (!lightStale && !darkStale && !darkOrphaned) return@forEach

        imagesetDir.mkdirs()
        try {
            if (lightStale) {
                convertSvgToPdf(svgFile, pdfFile)
            }
            if (darkSvgFile != null) {
                convertSvgToPdf(darkSvgFile, darkPdfFile)
            } else {
                darkPdfFile.delete()
            }
        } catch (e: Exception) {
            println("✗ SwiftUI: Failed to convert ${svgFile.name} to PDF: ${e.message}")
            return@forEach
        }

        // Write Contents.json based on pack type
        val contentsJson = File(imagesetDir, "Contents.json")
        contentsJson.writeText(
            buildImagesetContentsJson(
                packType = pack.packType,
                pdfFileName = "$name.pdf",
                darkPdfFileName = darkSvgFile?.let { "$name-dark.pdf" },
            )
        )
        convertedCount++
    }

    // Generate Swift enum file (always, since it lists all entries)
    val swiftCode = buildSwiftEnum(pack, svgFiles)
    val outputFile = File(outputDir, pack.swiftFileName)
    outputFile.writeText(swiftCode)

    println("✓ SwiftUI: Converted $convertedCount/${svgFiles.size} imagesets (${svgFiles.size - convertedCount} cached) + ${pack.swiftFileName}")
}

private fun buildImagesetContentsJson(
    packType: PackType,
    pdfFileName: String,
    darkPdfFileName: String? = null,
): String {
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

    // The dark entry is resolved by UIKit from the trait collection, which is the same
    // mechanism the generated colorsets use. No Swift-side branching is needed.
    val darkImage = darkPdfFileName?.let {
        """,
    {
      "appearances" : [
        {
          "appearance" : "luminosity",
          "value" : "dark"
        }
      ],
      "filename" : "$it",
      "idiom" : "universal"
    }"""
    }.orEmpty()

    return """{
  "images" : [
    {
      "filename" : "$pdfFileName",
      "idiom" : "universal"
    }$darkImage
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
    requireNoDarkVariantsLeaked(pack, svgFiles.map { it.nameWithoutExtension })

    return when (pack.packType) {
        PackType.ICON -> buildIconSwiftEnum(pack, svgFiles)
        PackType.FLAG -> buildFlagSwiftEnum(pack, svgFiles)
        PackType.BRAND_LOGO -> buildBrandLogoSwiftEnum(pack, svgFiles)
    }
}

private fun buildIconSwiftEnum(pack: PackConfig, svgFiles: List<File>): String {
    return buildString {
        appendLine("import SwiftUI")
        appendLine("#if canImport(UIKit)")
        appendLine("import UIKit")
        appendLine("#endif")
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
            appendLine("    case ${file.nameWithoutExtension.toCamelCase()} = \"${file.nameWithoutExtension}\"")
        }
        appendLine()
        appendLine("    /// Returns the Image for this icon from the Lemonade bundle's asset catalog")
        appendLine("    public var image: Image {")
        appendLine("        #if canImport(UIKit)")
        appendLine("        if let uiImage = UIImage(named: rawValue, in: .lemonade, compatibleWith: nil) {")
        appendLine("            return Image(uiImage: uiImage).renderingMode(.template)")
        appendLine("        }")
        appendLine("        return Image(systemName: \"questionmark.square\")")
        appendLine("        #else")
        appendLine("        return Image(self.rawValue, bundle: .lemonade).renderingMode(.template)")
        appendLine("        #endif")
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
            val enumCase = file.nameWithoutExtension.toPascalCase().replaceFirstChar { it.lowercase() }
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
            val name = file.nameWithoutExtension
            pack.deprecations[name]?.let { replacement ->
                appendLine("    @available(*, deprecated, renamed: \"${replacement.toCamelCase()}\")")
            }
            appendLine("    case ${name.toCamelCase()} = \"$name\"")
        }
        if (pack.deprecations.isNotEmpty()) {
            appendLine()
            appendLine("    /// Swift cannot synthesise `CaseIterable` for an enum carrying `@available` on a")
            appendLine("    /// case, so `allCases` is written out here.")
            appendLine("    ///")
            appendLine("    /// Deprecated aliases are omitted: each renders the same asset as its replacement,")
            appendLine("    /// so listing both would show the logo twice in a gallery. The cases themselves stay")
            appendLine("    /// usable - referencing one just warns and points at the replacement.")
            appendLine("    public static var allCases: [${pack.swiftEnumName}] {")
            appendLine("        [")
            svgFiles
                .map { it.nameWithoutExtension }
                .filterNot { it in pack.deprecations }
                .forEach { appendLine("            .${it.toCamelCase()},") }
            appendLine("        ]")
            appendLine("    }")
        }
        appendLine("}")
    }
}

// === Main entry point ===

fun main() {
    val forceRebuild = args.contains("--force")

    checkRsvgConvert()

    val cache = if (forceRebuild) {
        println("⚡ Force mode: rebuilding all assets")
        mutableMapOf()
    } else {
        loadCache()
    }

    val assetsDir = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")
    ensureAssetCatalogRoot(assetsDir)

    val updatedCache = cache.toMutableMap()

    packs.forEach { pack ->
        println("=== Processing ${pack.name} ===")
        val svgDir = File(pack.sourceDir)
        if (!svgDir.exists() || !svgDir.isDirectory) {
            println("⚠️ Source directory does not exist: ${pack.sourceDir}, skipping")
            return@forEach
        }

        val allSvgFiles = svgDir.listFiles { file -> file.isFile && file.extension == "svg" }
            ?.sortedBy { it.nameWithoutExtension } ?: emptyList()

        if (allSvgFiles.isEmpty()) {
            println("⚠️ No SVG files found in ${pack.sourceDir}, skipping")
            return@forEach
        }

        println("Found ${allSvgFiles.size} SVG files")

        // Split the dark-theme variants ("<name>$DARK_SUFFIX.svg") out of the pack and pair each
        // to the light asset of the same base name. Only the light assets become enum entries;
        // assets without a variant fall back to their light drawable.
        val (darkFiles, svgFiles) = if (pack.supportsDark) {
            allSvgFiles.partition { it.nameWithoutExtension.endsWith(DARK_SUFFIX) }
        } else {
            emptyList<File>() to allSvgFiles
        }

        val darkSvgFiles = darkFiles.associateBy { it.nameWithoutExtension.removeSuffix(DARK_SUFFIX) }
        val lightNames = svgFiles.map { it.nameWithoutExtension }.toSet()

        // A "<name>$DARK_SUFFIX.svg" with no "<name>.svg" beside it is ambiguous: either a typo,
        // or an asset whose real name happens to end in "$DARK_SUFFIX". Neither should be guessed at.
        val orphanDarkNames = darkSvgFiles.keys - lightNames
        if (orphanDarkNames.isNotEmpty()) {
            println("⚠️ ${pack.name}: dark variant(s) with no matching light asset, ignored: ${orphanDarkNames.sorted().joinToString { "$it$DARK_SUFFIX.svg" }}")
        }
        val pairedDarkSvgFiles = darkSvgFiles.filterKeys { it in lightNames }
        if (pack.supportsDark) {
            println("Found ${pairedDarkSvgFiles.size} dark variant(s); ${lightNames.size - pairedDarkSvgFiles.size} asset(s) fall back to light")
        }

        // Determine which files changed by comparing content hashes
        val changedFiles = mutableSetOf<String>()
        val newHashes = mutableMapOf<String, String>()
        (svgFiles + pairedDarkSvgFiles.values).forEach { svgFile ->
            val hash = sha256(svgFile)
            newHashes[svgFile.path] = hash
            if (hash != cache[svgFile.path]) {
                changedFiles.add(svgFile.path)
            }
        }

        // Prune cache entries for deleted SVGs in this pack
        val currentPaths = (svgFiles + pairedDarkSvgFiles.values).map { it.path }.toSet()
        val deletionsOccurred = updatedCache.keys.removeAll { it.startsWith("${pack.sourceDir}/") && it !in currentPaths }

        // Cache unchanged files immediately; changed files are cached after successful conversion
        newHashes.forEach { (path, hash) ->
            if (path !in changedFiles) {
                updatedCache[path] = hash
            }
        }

        // Also check for missing outputs (e.g., generated files deleted while cache intact)
        val kmpDrawableDir = File("kmp/ui/src/commonMain/composeResources/drawable")
        val assetsRoot = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")
        val missingOutputs = svgFiles.any { svgFile ->
            val name = svgFile.nameWithoutExtension
            val kmpFile = File(kmpDrawableDir, "gen_${name.replace("-", "_")}.xml")
            val pdfFile = File(assetsRoot, "$name.imageset/$name.pdf")
            val darkOutputsStale = if (name in pairedDarkSvgFiles) {
                !File(kmpDrawableDir, "gen_${kmpDarkResourceSuffix(name)}.xml").exists() ||
                    !File(assetsRoot, "$name.imageset/$name-dark.pdf").exists()
            } else {
                false
            }
            !kmpFile.exists() || !pdfFile.exists() || darkOutputsStale
        }

        // NOTE: this early return also skips the enum/extension codegen below. That is load
        // bearing: LemonadeCountryFlags.kt is rewritten here and then has its `getOrNull`
        // companion injected by kmp-country-flags-alpha2-generator.main.kts, so regenerating
        // it without re-running that post-generator drops public API. Run the whole pipeline
        // via scripts/generate-resources.main.kts, which sequences the two correctly.
        if (changedFiles.isEmpty() && !deletionsOccurred && !missingOutputs) {
            println("✓ ${pack.name}: no changes detected, skipping conversions")
            return@forEach
        }

        if (changedFiles.isNotEmpty()) {
            println("${changedFiles.size} file(s) changed")
        }
        if (deletionsOccurred) {
            println("Deletions detected, regenerating enum/extension files")
        }

        generateKmpAssets(pack, svgFiles, pairedDarkSvgFiles, changedFiles)
        generateSwiftAssets(pack, svgFiles, pairedDarkSvgFiles, changedFiles)

        // Only cache changed files after successful conversion
        changedFiles.forEach { path -> updatedCache[path] = newHashes[path]!! }

        println("✓ ${pack.name} complete")
    }

    saveCache(updatedCache)
    println("=== All packs processed ===")
}

main()
