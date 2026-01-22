package com.teya.lemonade

internal actual fun SwitchState.getSwitchProps(): SwitchSizeProps {
    return this.defaultSwitchProps()
}