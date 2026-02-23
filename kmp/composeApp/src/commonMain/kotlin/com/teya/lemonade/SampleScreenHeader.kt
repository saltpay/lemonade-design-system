package com.teya.lemonade

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.lerp

@Suppress("UnusedParameter")
@Composable
internal fun SampleScreenHeader(
    modifier: Modifier = Modifier,
    title: String?,
    background: Color = LemonadeTheme.colors.background.bgDefault,
    progress: Float,
    onBack: (() -> Unit)? = null,
) {
    if (title.isNullOrBlank()) return

    val verticalPad = lerp(
        start = LemonadeTheme.spaces.spacing300,
        stop = LemonadeTheme.spaces.spacing100,
        fraction = progress,
    )

    val targetBgAlpha = if (progress > 0.7f) {
        1F
    } else {
        progress
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = targetBgAlpha,
        label = "HeaderBackgroundAlpha",
    )

    val targetFontSize = if (progress > 0f) {
        LemonadeTheme.typography.headingXXSmall.fontSize
    } else {
        LemonadeTheme.typography.headingSmall.fontSize
    }

    val animatedFontSize by animateFloatAsState(
        targetValue = targetFontSize,
        animationSpec = tween(
            durationMillis = 200,
            easing = EaseInOut,
        ),
        label = "HeaderFontSize",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background.copy(animatedAlpha))
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = LemonadeTheme.spaces.spacing400,
                    vertical = verticalPad,
                ),
        ) {
            LemonadeUi.Text(
                text = title,
                textStyle = LemonadeTheme.typography.headingMedium.copy(fontSize = animatedFontSize),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LemonadeTheme.borderWidths.base.border25)
                .alpha(progress)
                .background(LemonadeTheme.colors.border.borderNeutralLow),
        )
    }
}
