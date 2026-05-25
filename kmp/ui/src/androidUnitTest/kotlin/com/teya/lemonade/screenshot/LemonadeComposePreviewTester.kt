package com.teya.lemonade.screenshot

import android.content.res.Configuration
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
import com.teya.lemonade.LemonadeTheme
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.android.screenshotid.AndroidPreviewScreenshotIdBuilder
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import java.io.File

/**
 * Custom Roborazzi preview tester that wraps every discovered `@Preview` in
 * [LemonadeTheme], selecting the light/dark colour set from the preview's
 * `uiMode`. Everything else (preview discovery, locale/device/fontScale,
 * golden-file naming) is delegated to Roborazzi's default
 * [AndroidComposePreviewTester] so goldens stay deterministic and consistent
 * with the upstream behaviour.
 *
 * Roborazzi 1.46.1 has no content-wrapping hook (the `Capturer` interface was
 * added in a later release), so [test] injects the theme around the preview
 * content while mirroring the capture and naming logic of
 * `AndroidComposePreviewTester.test` (same public helpers, same option/clock
 * setup, same file-path derivation) for golden determinism.
 *
 * The [LemonadeDarkTheme] branch is provisioned for previews that declare a
 * night `uiMode` (e.g. a stacked `@Preview(uiMode = UI_MODE_NIGHT_YES)`); no
 * `:ui` preview does so today, so dark goldens are not produced. Dark coverage
 * is author-opt-in - the multipreview [com.teya.lemonade.LemonadePreview]
 * varies locale (RTL/LTR), not `uiMode`.
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

    override fun testParameters(): List<AndroidPreviewJUnit4TestParameter> = delegate.testParameters()

    override fun test(testParameter: AndroidPreviewJUnit4TestParameter) {
        val preview = testParameter.preview
        val filePath = filePathFor(testParameter = testParameter)
        val isNight = isNightMode(previewInfo = preview.previewInfo)
        val colors = if (isNight) {
            LemonadeDarkTheme
        } else {
            LemonadeLightTheme
        }

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

        captureRoboImage(
            filePath = filePath,
            roborazziComposeOptions = roborazziComposeOptions,
        ) {
            LemonadeTheme(colors = colors) {
                preview()
            }
        }
    }

    private fun isNightMode(previewInfo: AndroidPreviewInfo): Boolean {
        val nightMask = previewInfo.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightMask == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Reconstructs the golden-file path the way `AndroidComposePreviewTester.test`
     * does in Roborazzi 1.46.1 (same naming strategy, screenshot id and path
     * prefix), so switching to this custom tester does not relocate or rename
     * any goldens.
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

    private fun screenshotIdFor(preview: ComposablePreview<AndroidPreviewInfo>): String =
        AndroidPreviewScreenshotIdBuilder(preview)
            .ignoreClassName()
            .build()
}
