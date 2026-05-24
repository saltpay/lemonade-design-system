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
 * added in a later release), so [test] re-implements the default capture path
 * verbatim, injecting the theme around the preview content. The naming/path
 * logic mirrors `AndroidComposePreviewTester.test` exactly, using the same
 * public helpers, so the produced file paths are identical to the default
 * tester's.
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

        val roborazziComposeOptions = preview
            .toRoborazziComposeOptions()
            .builder()
            .composeTestRule(testParameter.composeTestRule)
            .build()

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
     * Reconstructs the golden-file path exactly as
     * `AndroidComposePreviewTester.test` does in Roborazzi 1.46.1, so switching
     * to this custom tester does not relocate or rename any goldens.
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
