package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

internal actual val platformCheckboxPropertiesProperties: CheckboxPlatformProps
    @Composable get() {
        return CheckboxPlatformProps(
            checkboxSize = 16.dp,
            labelStyle = LocalTypographies.current.bodySmallMedium,
            supportTextStyle = LocalTypographies.current.bodySmallRegular,
            focusVisible = true,
        )
    }
