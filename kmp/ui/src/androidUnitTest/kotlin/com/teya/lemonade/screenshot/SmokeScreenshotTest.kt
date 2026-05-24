package com.teya.lemonade.screenshot

import com.github.takahirom.roborazzi.captureRoboImage
import com.teya.lemonade.LemonadeLightTheme
import com.teya.lemonade.LemonadeTheme
import com.teya.lemonade.LemonadeUi
import com.teya.lemonade.Tag
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * De-risk smoke test: proves Robolectric + Roborazzi can render a real Lemonade
 * composable wrapped in the light theme into a non-blank PNG. This isolates the
 * compose-resources (font loading via [com.teya.lemonade.LemonadeRes]) and basic
 * render-path risks before the preview-scanner auto-discovery work.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class SmokeScreenshotTest {
    @Test
    fun rendersTagInLightTheme() {
        captureRoboImage(filePath = "build/smoke/smoke.png") {
            LemonadeTheme(colors = LemonadeLightTheme) {
                LemonadeUi.Tag(label = "Smoke")
            }
        }
    }
}
