@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.json:json:20240303")

import org.json.JSONObject
import java.io.File

data class ResourceData<T>(
    val groups: List<String>,
    val groupFullName: String?,
    val name: String,
    val value: T,
)

data class Color(
    val r: Double,
    val g: Double,
    val b: Double,
    val a: Double,
)

fun defaultAutoGenerationMessage(description: String): String {
    return buildString {
        appendLine("/// GENERATED CODE - DO NOT MODIFY BY HAND")
        appendLine("/// *****************************************************")
        appendLine("/// $description from Lemonade Design System Foundations")
        appendLine("/// *****************************************************")
        appendLine()
        appendLine("// coverage:ignore-file")
        appendLine("// ignore_for_file: type=lint")
        appendLine("// ignore_for_file: public_member_api_docs, prefer_int_literals,")
        appendLine("// lines_longer_than_80_chars, dangling_library_doc_comments")
        appendLine("// dart format off")
        appendLine()
    }
}

fun <T> List<ResourceData<T?>>.filterNull(): List<ResourceData<T>> {
    val nonNullList = mutableListOf<ResourceData<T>>()
    forEach { resource ->
        if (resource.value != null) {
            nonNullList.add(
                ResourceData(
                    groups = resource.groups,
                    groupFullName = resource.groupFullName,
                    name = resource.name,
                    value = resource.value,
                )
            )
        }
    }
    return nonNullList
}

fun <T> readFileResourceFile(
    file: File,
    resourceMap: (JSONObject) -> T,
): List<ResourceData<T>> {
    val fileContent = file.readText()
    val json = JSONObject(fileContent)
    val variablesJsonArray = json.getJSONArray("variables")
    val resources = mutableListOf<ResourceData<T>>()
    println("Found ${variablesJsonArray.length()} variables")
    repeat(times = variablesJsonArray.length()) { index ->
        val variableJsonObject = variablesJsonArray.getJSONObject(index)
        if (!variableJsonObject.optBoolean("hiddenFromPublishing")) {
            val name = variableJsonObject.getString("name")
            val resolvedValues = variableJsonObject.getJSONObject("resolvedValuesByMode")
            resolvedValues.keys().asSequence().firstOrNull()?.let { resolvedValueKey ->
                val resolvedValueKeyObject = resolvedValues.getJSONObject(resolvedValueKey)
                resources.add(
                    ResourceData(
                        groups = name.sanitizedGroups(),
                        groupFullName = name.sanitizedClassName(),
                        name = name.sanitizedValueName(),
                        value = resourceMap(resolvedValueKeyObject),
                    )
                )
            }
        }
    }
    return resources
}

fun String.capitalize(): String {
    return if (isEmpty()) this else this[0].uppercaseChar() + substring(1)
}

fun String.camelCase(): String {
    return if (isEmpty()) this else this[0].lowercaseChar() + substring(1)
}

fun String.sanitizedGroups(): List<String> {
    val groups = split("/")
    val valueName = groups.subList(0, groups.lastIndex)
    return valueName.map { group ->
        group
            .split("-")
            .joinToString(separator = "") { word ->
                word.replaceFirstChar { char -> char.uppercase() }
            }
    }
}

fun String.sanitizedValueName(): String {
    val groups = split("/")
    var valueName = groups[groups.lastIndex]
    if (valueName.isValueNumberOnly()) {
        valueName = groups[groups.lastIndex - 1] + groups[groups.lastIndex]
    }
    return valueName
        .split("/")
        .joinToString("") { group ->
            group
                .split("-")
                .joinToString(separator = "") { word ->
                    word.replaceFirstChar { char -> char.uppercase() }
                }
        }
        .replaceFirstChar { char -> char.lowercase() }
}

fun String.sanitizedClassName(): String {
    val groups = split("/")
    val valueName = if(groups.size > 1){
        groups.subList(0, groups.lastIndex)
    } else {
        groups
    }
    return valueName.joinToString("") { group ->
        group
            .split("-")
            .joinToString(separator = "") { word ->
                word.replaceFirstChar { char -> char.uppercase() }
            }
    }
}

fun String.isValueNumberOnly(): Boolean {
    return all { it.isDigit() }
}

fun Color.toHex(): String {
    fun clamp(value: Int) = value.coerceIn(0, 255)

    val red = clamp((r * 255).toInt())
    val green = clamp((g * 255).toInt())
    val blue = clamp((b * 255).toInt())
    val alpha = clamp((a * 255).toInt())

    // Combine into AARRGGBB format
    val color = (alpha shl 24) or (red shl 16) or (green shl 8) or blue

    return String.format("0x%08X", color)
}
