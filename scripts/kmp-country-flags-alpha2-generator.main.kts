#!/usr/bin/env kotlin

import java.io.File

private val svgDir = File("svg/flags")
private val flagsFile = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core/LemonadeCountryFlags.kt")
private val subRegionPattern = Regex("^[A-Z]{2}-[A-Z]{2,}-")

fun main() {
    if (!svgDir.exists()) {
        println("SVG flags directory not found: $svgDir")
        return
    }
    if (!flagsFile.exists()) {
        println("LemonadeCountryFlags.kt not found: $flagsFile")
        return
    }

    if(flagsFile.readText().contains("companion object")){
        println("$flagsFile already contains a companion object, make sure to run this script with a freshly generated flags file.")
        return
    }

    val svgFiles = svgDir.listFiles { file -> file.extension == "svg" }
        ?.map { it.nameWithoutExtension }
        ?.sorted()
        ?: emptyList()

    val mainFlags = svgFiles.filter { name -> !subRegionPattern.containsMatchIn(name) }

    println("Total SVG files: ${svgFiles.size}")
    println("Sub-region files filtered out: ${svgFiles.size - mainFlags.size}")

    val alpha2ToEnum = mutableMapOf<String, String>()
    mainFlags.forEach { name ->
        val alpha2 = name.take(2)
        val enumName = name.split("-").joinToString("") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
        if (alpha2 in alpha2ToEnum) {
            println("WARNING: Duplicate alpha-2 '$alpha2' — keeping '${alpha2ToEnum[alpha2]}', skipping '$enumName'")
        } else {
            alpha2ToEnum[alpha2] = enumName
        }
    }

    println("Alpha-2 mappings: ${alpha2ToEnum.size}")

    val whenEntries = alpha2ToEnum.entries
        .sortedBy { it.key }
        .joinToString("\n") { (alpha2, enumName) ->
            "                \"$alpha2\" -> $enumName"
        }

    val companionObject = buildString {
        appendLine()
        appendLine("    public companion object {")
        appendLine("        public fun getOrNull(alpha2: String): LemonadeCountryFlags? =")
        appendLine("            when (alpha2.uppercase()) {")
        appendLine(whenEntries)
        appendLine("                else -> null")
        appendLine("            }")
        appendLine("    }")
    }

    val content = flagsFile.readText()
    val lines = content.lines().toMutableList()

    val closingBraceIndex = lines.indexOfLast { it.trimStart() == "}" }
    if (closingBraceIndex == -1) {
        println("ERROR: Could not find closing brace in LemonadeCountryFlags.kt")
        return
    }

    // Find the last enum entry line (the line just before the closing brace, ignoring blanks)
    val lastEntryIndex = (closingBraceIndex - 1 downTo 0).firstOrNull { lines[it].isNotBlank() }
    if (lastEntryIndex == null) {
        println("ERROR: Could not find last enum entry in LemonadeCountryFlags.kt")
        return
    }

    // Add semicolon to the last enum entry if it doesn't already have one
    val lastEntry = lines[lastEntryIndex].trimEnd()
    if (!lastEntry.endsWith(";")) {
        lines[lastEntryIndex] = lastEntry.removeSuffix(",") + ";"
    }

    // Insert the companion object before the closing brace
    lines.add(closingBraceIndex, companionObject)

    val result = lines.joinToString("\n")

    flagsFile.writeText(result)
    println("Successfully injected companion object into LemonadeCountryFlags.kt")
}

main()
