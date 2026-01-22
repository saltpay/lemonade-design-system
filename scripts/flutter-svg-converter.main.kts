#!/usr/bin/env kotlin

@file:Import("flutter-resource-file-loading.main.kts")

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Asset category configuration for SVG conversion
 */
data class AssetCategory(
    val name: String,
    val sourceDir: String,
    val targetDir: String,
    val enumName: String,
)

fun main() {
    // Define asset categories - easily extensible for future categories
    val assetCategories = listOf(
        AssetCategory(
            name = "icons",
            sourceDir = "svg/icons",
            targetDir = "flutter/assets/svg/icons",
            enumName = "LemonadeIcons",
        ),
        AssetCategory(
            name = "brandLogos",
            sourceDir = "svg/brandLogos",
            targetDir = "flutter/assets/svg/brandLogos",
            enumName = "LemonadeBrandLogos",
        ),
        AssetCategory(
            name = "flags",
            sourceDir = "svg/flags",
            targetDir = "flutter/assets/svg/flags",
            enumName = "LemonadeFlags",
        ),
    )

    val outputFile = File("flutter/lib/src/foundation/assets.dart")

    try {
        // Process all asset categories
        val allAssetData = assetCategories.map { category ->
            processAssetCategory(category)
        }

        // Generate the Dart code
        val generatedCode = generateAssetsCode(allAssetData)
        
        // Ensure output directory exists
        outputFile.parentFile.mkdirs()
        
        // Write the generated code
        outputFile.writeText(generatedCode)

        // Format the file using dart format
        ProcessBuilder("dart", "format", outputFile.absolutePath).start().waitFor()

        println("✓ Successfully generated ${outputFile.name}!")
        println("  Total categories: ${assetCategories.size}")
        allAssetData.forEach { data ->
            println("  - ${data.category.enumName}: ${data.assets.size} assets")
        }
    } catch (error: Throwable) {
        println("✗ Failed to generate assets: ${error.message}")
        error.printStackTrace()
    }
}

/**
 * Data class to hold processed asset information
 */
data class AssetData(
    val category: AssetCategory,
    val assets: List<AssetInfo>,
)

/**
 * Data class to hold individual asset information
 */
data class AssetInfo(
    val fileName: String,
    val enumConstantName: String,
    val assetPath: String,
)

/**
 * Process a single asset category: copy SVGs and collect asset information
 */
fun processAssetCategory(category: AssetCategory): AssetData {
    val sourceDir = File(category.sourceDir)
    val targetDir = File(category.targetDir)

    if (!sourceDir.exists() || !sourceDir.isDirectory) {
        println("⚠ Warning: Source directory ${category.sourceDir} does not exist, skipping ${category.name}")
        return AssetData(category, emptyList())
    }

    println("Processing ${category.name}...")
    
    // Create target directory if it doesn't exist
    targetDir.mkdirs()

    // Get all SVG files
    val svgFiles = (sourceDir.listFiles { file ->
        file.isFile && file.extension == "svg"
    }?.toList() ?: emptyList()).sorted()

    println("Found ${svgFiles.size} SVG files in ${category.sourceDir}")

    // Copy files and collect asset information
    val assets = svgFiles.map { svgFile ->
        // Copy the SVG file to target directory, cleaning the content
        val targetFile = File(targetDir, svgFile.name)
        val cleanedContent = cleanSvgContent(svgFile.readText())
        targetFile.writeText(cleanedContent)

        // Generate enum constant name and asset path
        val enumConstantName = svgFile.nameWithoutExtension.toEnumConstantName()
        val assetPath = "packages/lemonade_design_system/assets/svg/${category.name}/${svgFile.name}"

        AssetInfo(
            fileName = svgFile.name,
            enumConstantName = enumConstantName,
            assetPath = assetPath,
        )
    }

    println("✓ Copied ${assets.size} SVG files to ${category.targetDir}")
    
    return AssetData(category, assets)
}

/**
 * Clean SVG content by removing invalid CSS properties that flutter_svg doesn't support
 */
fun cleanSvgContent(content: String): String {
    // Remove fill:color(display-p3 ...) from style attributes as it's not supported by flutter_svg
    return content.replace(Regex("""fill:color\(display-p3[^;]*;"""), "")
}

/**
 * Convert a filename to an enum constant name
 * Examples:
 *   "arrow-down.svg" -> "arrowDown"
 *   "circle-x.svg" -> "circleX"
 *   "brand-teya-symbol.svg" -> "brandTeyaSymbol"
 */
fun String.toEnumConstantName(): String {
    return split("-")
        .mapIndexed { index, word ->
            if (index == 0) {
                word.lowercase()
            } else {
                word.replaceFirstChar { it.uppercase() }
            }
        }
        .joinToString("")
}

/**
 * Generate the complete Dart code for all asset categories
 */
fun generateAssetsCode(allAssetData: List<AssetData>): String {
    return buildString {
        appendLine("import 'package:lemonade_design_system/lemonade_design_system.dart';")
        appendLine()
        append(defaultAutoGenerationMessage("Assets"))
        appendLine()
        
        // Generate an enum for each category
        allAssetData.forEachIndexed { index, assetData ->
            if (assetData.assets.isNotEmpty()) {
                append(generateEnumCode(assetData))
                
                // Add blank line between enums (except after the last one)
                if (index < allAssetData.lastIndex) {
                    appendLine()
                }
            }
        }
    }
}

/**
 * Generate Dart enum code for a single asset category
 */
fun generateEnumCode(assetData: AssetData): String {
    val category = assetData.category
    val assets = assetData.assets
    
    return buildString {
        appendLine("/// {@template ${category.enumName}}")
        appendLine("/// Assets available in the Lemonade Design System Foundations package.")
        appendLine("/// {@endtemplate}")
        appendLine("enum ${category.enumName} {")
        
        assets.forEachIndexed { index, asset ->
            append("  ${asset.enumConstantName}('${asset.assetPath}')")
            
            // Add comma or semicolon
            if (index < assets.lastIndex) {
                appendLine(",")
            } else {
                appendLine(";")
            }
        }
        
        appendLine()
        appendLine("  /// {@macro ${category.enumName}}")
        appendLine("  const ${category.enumName}(this.assetPath);")
        appendLine()
        appendLine("  /// The asset path for this ${category.name.removeSuffix("s").lowercase()}")
        appendLine("  @internal")
        appendLine("  final String assetPath;")
        appendLine("}")
    }
}

main()
