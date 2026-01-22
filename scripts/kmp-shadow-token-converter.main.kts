#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

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
    val definitionOutputDir = File("kmp/core/src/commonMain/kotlin/com/teya/lemonade/core")
    val implementationOutputDir = File("kmp/ui/src/commonMain/kotlin/com/teya/lemonade")

    try {
        if (!implementationOutputDir.exists()) {
            implementationOutputDir.mkdirs()
        }

        if (!definitionOutputDir.exists()) {
            definitionOutputDir.mkdirs()
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

        val definitionCode = buildDefinitionCode(
            scriptFilePath = "scripts/kmp-shadow-token-converter.main.kts",
            resources = shadowResources,
        )
        val definitionOutputFile = File(definitionOutputDir, "LemonadeShadows.kt")
        definitionOutputFile.writeText(definitionCode)
        println("✓ Shadows definition file created")

        val implementationCode = buildImplementationCode(
            scriptFilePath = "scripts/kmp-shadow-token-converter.main.kts",
            resources = shadowResources,
        )
        val implementationOutputFile = File(implementationOutputDir, "LemonadeShadowsSequence.kt")
        implementationOutputFile.writeText(implementationCode)
        println("✓ Shadows sequence implementation file created")

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

private fun buildDefinitionCode(
    scriptFilePath: String,
    resources: List<ShadowResourceGroup>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade.core")
        appendLine()
        appendLine("/**")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("public enum class LemonadeShadow {")
        resources.forEach { resource ->
            appendLine("    ${resource.groupName},")
        }
        appendLine("    None,")
        appendLine("}")
    }
}

private fun buildImplementationCode(
    scriptFilePath: String,
    resources: List<ShadowResourceGroup>,
): String {
    return buildString {
        appendLine("package com.teya.lemonade")
        appendLine()
        appendLine("import com.teya.lemonade.core.LemonadeShadow")
        appendLine()
        appendLine("/**")
        append(defaultAutoGenerationMessage(scriptFilePath = scriptFilePath))
        appendLine(" */")
        appendLine("internal val LemonadeShadow.shadowDataSequence: Sequence<LemonadeShadowData>")
        appendLine("    get() = when(this){")
        resources.forEach { resource ->
            appendLine("        LemonadeShadow.${resource.groupName} -> sequenceOf(")
            resource.levels.forEach { resourceLevel ->
                appendLine("            LemonadeShadowData(")
                appendLine("                blur = ${resourceLevel.blur}f,")
                appendLine("                spread = ${resourceLevel.spread}f,")
                appendLine("                offsetX = ${resourceLevel.offsetX}f,")
                appendLine("                offsetY = ${resourceLevel.offsetY}f,")
                appendLine("            ),")
            }
            appendLine("        )")
        }
        appendLine("        LemonadeShadow.None -> sequenceOf(")
        appendLine("            LemonadeShadowData(")
        appendLine("                blur = 0f,")
        appendLine("                spread = 0f,")
        appendLine("                offsetX = 0f,")
        appendLine("                offsetY = 0f,")
        appendLine("            ),")
        appendLine("        )")
        appendLine("    }")
        appendLine()
    }
}

main()
