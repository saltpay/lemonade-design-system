package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

internal actual val platformRadioButtonPropertiesProperties: RadioButtonPlatformProps
    @Composable get() {
        return RadioButtonPlatformProps(
            componentSize = 16.dp,
            checkedCircleSize = 8.dp,
            labelStyle = LocalTypographies.current.bodySmallMedium,
            supportTextStyle = LocalTypographies.current.bodySmallRegular,
            focusVisible = true,
        )
    }
