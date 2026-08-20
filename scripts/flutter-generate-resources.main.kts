#!/usr/bin/env kotlin

import java.io.File

/**
 * Runs every Flutter converter in order.
 *
 * Two things this deliberately does not do casually:
 *
 *  - It invokes the pinned Kotlin 2.3.20 by absolute path, never bare `kotlin`.
 *    The converters fail to compile on 2.4.0 with `Expected FirResolvedTypeRef
 *    with ConeKotlinType but was FirUserTypeRefImpl`, and `kotlin` on PATH is
 *    commonly Homebrew's 2.4.0.
 *  - It checks each child's exit code. A converter that fails writes nothing and
 *    returns non-zero; ignoring that lets this wrapper report success while
 *    having generated nothing at all.
 */

private val kotlinBin = File(System.getProperty("user.home"), ".local/kotlin-2.3.20/kotlinc/bin/kotlin")

private val scripts = listOf(
    "scripts/flutter-color-token-converter.main.kts",
    "scripts/flutter-theme-token-converter.main.kts",
    "scripts/flutter-spacing-token-converter.main.kts",
    "scripts/flutter-radius-token-converter.main.kts",
    "scripts/flutter-opacity-token-converter.main.kts",
    "scripts/flutter-border-token-converter.main.kts",
    "scripts/flutter-shadow-token-converter.main.kts",
    "scripts/flutter-size-token-converter.main.kts",
    "scripts/flutter-svg-converter.main.kts",
)

fun main() {
    if (!kotlinBin.canExecute()) {
        error(
            "Kotlin 2.3.20 not found at ${kotlinBin.path}. The converters do not compile on " +
                "2.4.0. Run .claude/skills/generate-tokens/scripts/run-converters.sh once to " +
                "install it, then retry."
        )
    }

    // A converter's compiled script is cached, and editing an @file:Import'ed
    // loader does not invalidate it — the converter would silently run the
    // previous loader's code and produce stale output that looks fresh.
    listOf(
        File(System.getProperty("user.home"), "Library/Caches/main.kts.compiled.cache"),
        File(System.getProperty("user.home"), ".cache/main.kts.compiled.cache"),
    ).forEach { it.deleteRecursively() }

    println("Running ${scripts.size} Flutter scripts with ${kotlinBin.path}")
    scripts.forEach { script ->
        println("-----------------------")
        println("Running script: $script")
        val exitCode = try {
            ProcessBuilder(kotlinBin.path, script)
                .inheritIO()
                .start()
                .waitFor()
        } catch (error: Throwable) {
            println("✗ Failed to launch '$script': $error")
            throw error
        }
        if (exitCode != 0) {
            error("✗ '$script' exited with $exitCode — stopping. Nothing further was generated.")
        }
        println("-----------------------")
    }
    println("✓ All ${scripts.size} Flutter scripts completed")
}

main()
