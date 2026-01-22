#!/usr/bin/env kotlin

@file:Import("swiftui-resource-file-loading.main.kts")

import java.io.File

/**
 * This script generates Swift code for brand logo resources.
 * Brand logos are expected to be in PDF format in the svg/pdf/brandLogos directory.
 * The generated code creates an enum with all available brand logos.
 * It also copies brand logos to the asset catalog in the proper iOS format.
 */

fun main() {
    val brandLogosDir = File("svg/pdf/brandLogos")
    val outputDir = File("swiftui/Sources/Lemonade")
    val assetsDir = File("swiftui/Sources/Lemonade/Resources/Assets.xcassets")

    try {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Check if brand logos directory exists
        if (!brandLogosDir.exists() || !brandLogosDir.isDirectory) {
            println("⚠️ Brand logos directory does not exist yet: $brandLogosDir")
            return
        }

        // Get all PDF files
        val logoFiles = brandLogosDir.listFiles { file ->
            file.isFile && file.extension == "pdf"
        }?.sortedBy { it.nameWithoutExtension } ?: emptyList()

        if (logoFiles.isEmpty()) {
            println("⚠️ No brand logo files found in: $brandLogosDir")
            return
        }

        println("✓ Found ${logoFiles.size} brand logo files")

        // Generate Swift code
        val code = buildBrandLogosCode(
            scriptFilePath = "scripts/swiftui-brandlogos-converter.main.kts",
            logoFiles = logoFiles,
        )
        val outputFile = File(outputDir, "LemonadeBrandLogos.swift")
        outputFile.writeText(code)
        println("✓ Brand logos Swift file created")

        // Create asset catalog structure
        createBrandLogosAssetCatalog(assetsDir, logoFiles)
        println("✓ Asset catalog created with ${logoFiles.size} brand logos")

        println("✓ Implementation generated")
    } catch (error: Throwable) {
        println("✗ Failed to convert brand logos: ${error.message}")
        error.printStackTrace()
    }
}

private fun createBrandLogosAssetCatalog(assetsDir: File, logoFiles: List<File>) {
    // Create Assets.xcassets directory if it doesn't exist
    if (!assetsDir.exists()) {
        assetsDir.mkdirs()
    }

    // Create imageset for each brand logo
    logoFiles.forEach { logoFile ->
        val logoName = logoFile.nameWithoutExtension
        val imagesetDir = File(assetsDir, "$logoName.imageset")

        if (!imagesetDir.exists()) {
            imagesetDir.mkdirs()
        }

        // Copy the PDF file to the imageset
        val destFile = File(imagesetDir, logoFile.name)
        logoFile.copyTo(destFile, overwrite = true)

        // Create Contents.json for the imageset (brand logos preserve colors, not template)
        val contentsJson = File(imagesetDir, "Contents.json")
        contentsJson.writeText("""{
  "images" : [
    {
      "filename" : "${logoFile.name}",
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

private fun buildBrandLogosCode(
    scriptFilePath: String,
    logoFiles: List<File>,
): String {
    return buildString {
        appendLine("import Foundation")
        appendLine()
        appendLine("/// Lemonade Design System Brand Logos.")
        appendLine("/// Brand logos are loaded from the asset catalog bundled with this module.")
        appendLine("/// Use the LemonadeUi.BrandLogo component to display brand logos.")
        appendLine("///")
        defaultSwiftAutoGenerationMessage(scriptFilePath = scriptFilePath).lines().forEach { line ->
            appendLine("/// $line")
        }
        appendLine()
        appendLine("/// Brand logo token enum")
        appendLine("public enum LemonadeBrandLogo: String, CaseIterable {")
        logoFiles.forEach { file ->
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

main()
