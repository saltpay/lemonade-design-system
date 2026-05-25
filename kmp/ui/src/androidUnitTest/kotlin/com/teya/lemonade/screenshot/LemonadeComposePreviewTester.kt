package com.teya.lemonade.screenshot

import com.github.takahirom.roborazzi.AndroidComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester.TestParameter.JUnit4TestParameter.AndroidPreviewJUnit4TestParameter
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziRecordFilePathStrategy
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.composeTestRule
import com.github.takahirom.roborazzi.manualAdvance
import com.github.takahirom.roborazzi.provideRoborazziContext
import com.github.takahirom.roborazzi.roborazziDefaultNamingStrategy
import com.github.takahirom.roborazzi.roborazziRecordFilePathStrategy
import com.github.takahirom.roborazzi.roborazziSystemPropertyOutputDirectory
import com.github.takahirom.roborazzi.toRoborazziComposeOptions
import com.teya.lemonade.LemonadeDarkTheme
import com.teya.lemonade.LemonadeLightTheme
import com.teya.lemonade.LemonadeSemanticColors
import com.teya.lemonade.LemonadeTheme
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.android.screenshotid.AndroidPreviewScreenshotIdBuilder
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import java.io.File

/**
 * Custom Roborazzi preview tester that captures every discovered `@Preview` in
 * both [LemonadeLightTheme] and [LemonadeDarkTheme]. Preview discovery,
 * locale/device/fontScale and the base golden-file naming are delegated to
 * Roborazzi's default [AndroidComposePreviewTester] so goldens stay
 * deterministic and consistent with the upstream behaviour.
 *
 * Product rules applied on top of the default tester:
 * - **LTR only.** [testParameters] drops RTL variations (Arabic locale, `"ar"`)
 *   contributed by the multipreview [com.teya.lemonade.LemonadePreview], keeping
 *   LTR (`"en"`) and bare/empty-locale previews. `@LemonadePreview` itself is
 *   unchanged, so the IDE still renders both RTL and LTR.
 * - **Light and dark.** Each kept preview is captured twice - once per theme -
 *   into two distinct golden paths suffixed `_Light` / `_Dark` before the file
 *   extension.
 *
 * Roborazzi 1.46.1 has no content-wrapping hook (the `Capturer` interface was
 * added in a later release), so [test] injects the theme around the preview
 * content while mirroring the capture and naming logic of
 * `AndroidComposePreviewTester.test` (same public helpers, same option/clock
 * setup, same file-path derivation) for golden determinism.
 *
 * TODO: When Roborazzi is upgraded to a version that exposes a content-wrapping
 * hook (the `Capturer` interface on `AndroidComposePreviewTester`), drop this
 * [test] override and inject the theme through that hook instead - it preserves
 * the upstream capture/naming path and removes the maintenance burden here.
 *
 * Wired in via `roborazzi.generateComposePreviewRobolectricTests` in
 * `LemonadeScreenshotPlugin`.
 */
@OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
public class LemonadeComposePreviewTester :
    ComposePreviewTester<AndroidPreviewJUnit4TestParameter> {
    private val delegate: AndroidComposePreviewTester = AndroidComposePreviewTester()

    override fun options(): ComposePreviewTester.Options = delegate.options()

    override fun testParameters(): List<AndroidPreviewJUnit4TestParameter> =
        delegate.testParameters().filterNot { parameter ->
            parameter.preview.previewInfo.locale == RTL_LOCALE
        }

    override fun test(testParameter: AndroidPreviewJUnit4TestParameter) {
        val preview = testParameter.preview
        val basePath = filePathFor(testParameter = testParameter)

        val variation = testParameter.composeRoboComposePreviewOptionVariation
        val optionsBuilder = preview
            .toRoborazziComposeOptions()
            .builder()
        optionsBuilder.composeTestRule(testParameter.composeTestRule)
        // Mirror AndroidComposePreviewTester.test: when the preview's
        // @RoboComposePreviewOptions declares a manual clock, advance it by the
        // requested amount before capture. Order matters - the compose test
        // rule must be configured first.
        variation.manualClockOptions?.let { clock ->
            optionsBuilder.manualAdvance(
                composeTestRule = testParameter.composeTestRule,
                advanceTimeMillis = clock.advanceTimeMillis,
            )
        }
        val roborazziComposeOptions = optionsBuilder.build()

        Theme.entries.forEach { theme ->
            captureRoboImage(
                filePath = pathWithThemeSuffix(basePath = basePath, theme = theme),
                roborazziComposeOptions = roborazziComposeOptions,
            ) {
                LemonadeTheme(colors = theme.colors) {
                    preview()
                }
            }
        }
    }

    /**
     * Reconstructs the golden-file path the way `AndroidComposePreviewTester.test`
     * does in Roborazzi 1.46.1 (same naming strategy, screenshot id and path
     * prefix), so switching to this custom tester does not relocate or rename
     * any goldens. The returned path still ends in the image extension; the
     * `_Light` / `_Dark` theme suffix is inserted by [pathWithThemeSuffix].
     */
    private fun filePathFor(testParameter: AndroidPreviewJUnit4TestParameter): String {
        val preview = testParameter.preview
        val pathPrefix = if (
            roborazziRecordFilePathStrategy() ==
            RoborazziRecordFilePathStrategy.RelativePathFromCurrentDirectory
        ) {
            roborazziSystemPropertyOutputDirectory() + File.separator
        } else {
            ""
        }
        val name = roborazziDefaultNamingStrategy().generateOutputName(
            preview.declaringClass,
            screenshotIdFor(preview = preview),
        )
        val variation = testParameter.composeRoboComposePreviewOptionVariation.nameWithPrefix()
        val extension = provideRoborazziContext().imageExtension
        return "$pathPrefix$name$variation.$extension"
    }

    /**
     * Inserts the theme suffix immediately before the file extension of a path
     * produced by [filePathFor], e.g. `Foo.png` -> `Foo_Light.png`. Keeps the
     * two themed paths distinct and deterministic without duplicating the
     * reconstruction logic.
     */
    private fun pathWithThemeSuffix(
        basePath: String,
        theme: Theme,
    ): String {
        val base = basePath.substringBeforeLast(delimiter = '.')
        val extension = basePath.substringAfterLast(delimiter = '.')
        return "$base${theme.suffix}.$extension"
    }

    private fun screenshotIdFor(preview: ComposablePreview<AndroidPreviewInfo>): String =
        AndroidPreviewScreenshotIdBuilder(preview)
            .ignoreClassName()
            .build()

    /** The Lemonade theme variants every preview is captured in. */
    private enum class Theme(
        val colors: LemonadeSemanticColors,
        val suffix: String,
    ) {
        Light(
            colors = LemonadeLightTheme,
            suffix = "_Light",
        ),
        Dark(
            colors = LemonadeDarkTheme,
            suffix = "_Dark",
        ),
    }
}

/** Android locale code of the RTL (Arabic) preview variation that we exclude. */
private const val RTL_LOCALE = "ar"
