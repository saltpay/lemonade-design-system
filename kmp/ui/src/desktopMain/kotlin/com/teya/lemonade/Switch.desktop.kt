package com.teya.lemonade

import androidx.compose.ui.unit.dp

internal actual fun SwitchState.getSwitchProps(): SwitchSizeProps {
    return when(this){
        SwitchState.Default -> SwitchSizeProps(
            minHeight = 20.dp,
            minWidth = 32.dp,
            minIndicatorHeight = 14.dp,
            minIndicatorWidth = 14.dp,
        )
        SwitchState.Hover -> SwitchSizeProps(
            minHeight = 20.dp,
            minWidth = 32.dp,
            minIndicatorHeight = 14.dp,
            minIndicatorWidth = 16.dp,
        )
        SwitchState.Pressed -> SwitchSizeProps(
            minHeight = 20.dp,
            minWidth = 32.dp,
            minIndicatorHeight = 14.dp,
            minIndicatorWidth = 20.dp,
        )
    }
}