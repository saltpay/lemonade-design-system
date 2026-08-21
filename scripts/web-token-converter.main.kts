#!/usr/bin/env kotlin

@file:Import("web-resource-file-loading.main.kts")
@file:Import("web-token-commons.main.kts")

import java.io.File

val SCRIPT_PATH = "scripts/web-token-converter.main.kts"

data class CssVar(val name: String, val value: String)

/** One scalar token collection and how its values become CSS. */
private data class ScalarSource(
    val fileName: String,
    val category: String,
    val strip: String?,
    val format: (Double) -> String,
)

/** Opacity tokens are authored 0-100; CSS wants 0-1. */
private fun opacityValue(raw: Double): String = trimNumber(raw / 100.0)

private val SCALAR_SOURCES = listOf(
    ScalarSource("spacing.tokens.json", "spacing", "spacing", ::remValue),
    ScalarSource("radius.tokens.json", "radius", "radius", ::remValue),
    ScalarSource("size.tokens.json", "size", "size", ::remValue),
    ScalarSource("border-width.tokens.json", "border-width", "border", ::pxValue),
    ScalarSource("opacity.tokens.json", "opacity", "opacity", ::opacityValue),
)

fun scalarVars(): List<CssVar> {
    val vars = mutableListOf<CssVar>()
    SCALAR_SOURCES.forEach { source ->
        readFileResourceFileRaw(tokenFile(source.fileName)) { path, resolved ->
            val raw = resolved.get("resolvedValue")
            require(raw is Number) {
                "${source.fileName}: token '$path' is ${raw::class.simpleName}, expected a number"
            }
            vars.add(
                CssVar(
                    name = cssVar(source.category, leafOf(path), source.strip),
                    value = source.format(raw.toDouble()),
                )
            )
        }
    }
    return vars
}

fun cssBanner(): String = buildString {
    appendLine("/**")
    append(defaultAutoGenerationMessage(scriptFilePath = SCRIPT_PATH))
    appendLine(" */")
}

/**
 * Writes web/styles/tokens.css.
 *
 * [sections] is an ordered list of (selector, declarations). Everything theme-neutral
 * goes under `:root`; Task 4 adds the theme selectors.
 */
fun writeTokensCss(sections: List<Pair<String, List<CssVar>>>) {
    // Guards the border-selected class of bug: two tokens mapping to one property
    // would silently drop one, and nothing downstream would notice. The same
    // property legitimately appears across selectors (e.g. light vs dark), so the
    // guard only rejects duplicates *within* one selector.
    sections.forEach { (selector, vars) ->
        val duplicates = vars.groupBy { it.name }.filterValues { it.size > 1 }
        require(duplicates.isEmpty()) {
            "duplicate CSS custom properties in '$selector': ${duplicates.keys.sorted()}"
        }
    }

    val output = buildString {
        append(cssBanner())
        sections.forEach { (selector, vars) ->
            val isAtRule = selector.startsWith("@")
            appendLine()
            appendLine("$selector {")
            vars.forEach { appendLine("  ${it.name}: ${it.value};") }
            appendLine("}")
            if (isAtRule) appendLine("}")
        }
    }

    val target = File("web/styles/tokens.css")
    target.parentFile.mkdirs()
    target.writeText(output)
    println("✓ web/styles/tokens.css written (${sections.sumOf { it.second.size }} properties)")
}

/** Every semantic colour for one Figma mode, as CSS custom properties. */
fun themeVars(mode: String): List<CssVar> {
    val files = tokenFiles("theme-colors.")
    // Regenerating from a subset would leave the other theme silently stale, and the
    // drift job would stay green because the tree stays clean.
    requireModes(files, "Light", "Dark")

    val vars = mutableListOf<CssVar>()
    readFileResourceFileByModeRaw(files, mode) { path, resolved ->
        vars.add(
            CssVar(
                name = cssVar("color", leafOf(path)),
                value = rgbValue(
                    r = resolved.getDouble("r"),
                    g = resolved.getDouble("g"),
                    b = resolved.getDouble("b"),
                    a = resolved.getDouble("a"),
                ),
            )
        )
    }
    return vars
}

/**
 * The four theme selectors.
 *
 * Light lives on bare `:root` so that importing tokens.css and setting nothing at all
 * is already correct. Dark then applies two ways: automatically from the OS, and
 * explicitly via the attribute, which always wins. The attribute is matched on any
 * element rather than only `<html>`, so a dark card inside a light page works.
 *
 * `color-scheme` makes native scrollbars, form controls and browser UI follow the
 * theme; without it the page is themed but the scrollbar is glaringly not.
 */
fun themeSections(): List<Pair<String, List<CssVar>>> {
    val light = themeVars("Light")
    val dark = themeVars("Dark")
    require(light.map { it.name } == dark.map { it.name }) {
        "light and dark themes declare different colour tokens — one export is stale"
    }
    val scheme = { value: String -> CssVar("color-scheme", value) }
    return listOf(
        ":root" to light + scheme("light"),
        "@media (prefers-color-scheme: dark) { :root:not([data-lmnd-theme=\"light\"])" to
            dark + scheme("dark"),
        "[data-lmnd-theme=\"dark\"]" to dark + scheme("dark"),
        "[data-lmnd-theme=\"light\"]" to light + scheme("light"),
    )
}

fun main() {
    try {
        val scalars = scalarVars()
        println("✓ Loaded ${scalars.size} scalar tokens")
        val themes = themeSections()
        println("✓ Loaded ${themes.first().second.size - 1} colours per theme")
        val sections = listOf(":root" to scalars) + themes
        writeTokensCss(sections)
    } catch (error: Throwable) {
        println("✗ Failed to generate web tokens: ${error.message}")
        throw error
    }
}

main()
