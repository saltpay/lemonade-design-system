#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

/**
 * This script generates Swift code for country flag resources.
 * Flags are expected to be in PDF format in the svg/pdf/flags directory.
 * The generated code creates an enum with all available country flags.
 * It also copies flags to the asset catalog in the proper iOS format.
 */

fun main() {
    val flagsDir = File("svg/pdf/flags")
    val outputDir = File("swiftui/Sources/Lemonade")
    val assetsDir = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Check if flags directory exists
        if (!flagsDir.exists() || !flagsDir.isDirectory) {
            println("⚠️ Flags directory does not exist yet: $flagsDir")
            return
        }

        // Get all PDF files
        val flagFiles = flagsDir.listFiles { file ->
            file.isFile && file.extension == "pdf"
        }?.sortedBy { it.nameWithoutExtension } ?: emptyList()

        if (flagFiles.isEmpty()) {
            println("⚠️ No flag files found in: $flagsDir")
            return
        }

        println("✓ Found ${flagFiles.size} flag files")

        // Generate Swift code
        val code = buildFlagsCode(
            scriptFilePath = "scripts/swiftui-flags-converter.main.kts",
            flagFiles = flagFiles,
        )
        val outputFile = File(outputDir, "LemonadeCountryFlags.swift")
        outputFile.writeText(code)
        println("✓ Country flags Swift file created")

        // Create asset catalog structure
        createFlagsAssetCatalog(assetsDir, flagFiles)
        println("✓ Asset catalog created with ${flagFiles.size} flags")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert flags: ${error.message}")
        error.printStackTrace()
    }
}

private fun createFlagsAssetCatalog(assetsDir: File, flagFiles: List<File>) {
    // Create Assets.xcassets directory if it doesn't exist
    if (!assetsDir.exists()) {
        assetsDir.mkdirs()
    }

    // Create imageset for each flag
    flagFiles.forEach { flagFile ->
        val flagName = flagFile.nameWithoutExtension
        val imagesetDir = File(assetsDir, "$flagName.imageset")

        if (!imagesetDir.exists()) {
            imagesetDir.mkdirs()
        }

        // Copy the PDF file to the imageset
        val destFile = File(imagesetDir, flagFile.name)
        flagFile.copyTo(destFile, overwrite = true)

        // Create Contents.json for the imageset (flags preserve colors, not template)
        val contentsJson = File(imagesetDir, "Contents.json")
        contentsJson.writeText("""{
  "images" : [
    {
      "filename" : "${flagFile.name}",
      "idiom" : "universal"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  },
  "properties" : {
    "preserves-vector-representation" : true
  }
}
""")
    }
}

/**
 * Converts a flag file name to a Swift enum case name.
 * Example: "AC-ascension-island" -> "ACAscensionIsland"
 */
private fun convertToEnumCase(fileName: String): String {
    return fileName
        .split("-")
        .joinToString("") { part ->
            part.replaceFirstChar { it.uppercase() }
        }
}

private fun buildFlagsCode(
    scriptFilePath: String,
    flagFiles: List<File>,
): String {
    return buildString {
        appendLine("import Foundation")
        appendLine()
        appendLine("/// Lemonade Design System Country Flags.")
        appendLine("/// Country flags are loaded from the asset catalog bundled with this module.")
        appendLine("/// Use the LemonadeUi.CountryFlag component to display flags.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Country flag token enum")
        appendLine("public enum LemonadeCountryFlag: String, CaseIterable {")
        flagFiles.forEach { file ->
            val enumCase = convertToEnumCase(file.nameWithoutExtension)
            // Make first character lowercase for Swift enum convention
            val swiftEnumCase = enumCase.replaceFirstChar { it.lowercase() }
            appendLine("    case $swiftEnumCase = \"${file.nameWithoutExtension}\"")
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
        appendLine("        return parts.map { \\$0.capitalized }.joined(separator: \" \")")
        appendLine("    }")
        appendLine("}")
    }
}

main()
