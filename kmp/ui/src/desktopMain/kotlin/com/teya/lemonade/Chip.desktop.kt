package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

internal actual val chipPlatformDimensions: ChipPlatformDimensions
    @Composable get() {
        return ChipPlatformDimensions(
            labelFontStyle = LocalTypographies.current.bodyXSmallMedium,
            counterFontStyle = LocalTypographies.current.bodyXSmallSemiBold,
            actionsSize = 16.dp,
            minSize = DpSize(
                width = 48.dp,
                height = 24.dp,
            ),
        )
    }
