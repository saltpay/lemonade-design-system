package com.teya.lemonade

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.teya.lemonade.core.TooltipIndicatorPlacement
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TooltipPositioningTest {
    private val host = Size(width = 393f, height = 852f)
    private val topAnchor = Rect(offset = Offset(x = 142f, y = 185f), size = Size(109f, 36f))
    private val bottomAnchor = Rect(offset = Offset(x = 123f, y = 461f), size = Size(146f, 36f))

    @Test
    fun `auto placement puts the tooltip below an anchor in the top half`() {
        assertTrue(resolvePointsUp(anchor = topAnchor, hostSize = host, forcedPlacement = null))
    }

    @Test
    fun `auto placement puts the tooltip above an anchor in the bottom half`() {
        assertFalse(resolvePointsUp(anchor = bottomAnchor, hostSize = host, forcedPlacement = null))
    }

    @Test
    fun `a forced bottom placement puts the tooltip above an anchor in the top half`() {
        assertFalse(
            resolvePointsUp(
                anchor = topAnchor,
                hostSize = host,
                forcedPlacement = TooltipIndicatorPlacement.BottomCenter,
            ),
        )
    }

    @Test
    fun `a forced top placement puts the tooltip below an anchor in the bottom half`() {
        assertTrue(
            resolvePointsUp(
                anchor = bottomAnchor,
                hostSize = host,
                forcedPlacement = TooltipIndicatorPlacement.TopCenter,
            ),
        )
    }

    @Test
    fun `an indicatorless placement falls back to auto placement`() {
        assertTrue(
            resolvePointsUp(
                anchor = topAnchor,
                hostSize = host,
                forcedPlacement = TooltipIndicatorPlacement.None,
            ),
        )
    }
}
