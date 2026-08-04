package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Suppress("LongMethod")
@Composable
internal fun SwitchDisplay() {
    var stateOff by rememberSaveable { mutableStateOf(false) }
    var stateOn by rememberSaveable { mutableStateOf(true) }
    var isOn1 by rememberSaveable { mutableStateOf(false) }
    var isOn2 by rememberSaveable { mutableStateOf(true) }
    var isOn3 by rememberSaveable { mutableStateOf(false) }
    var isOn4 by rememberSaveable { mutableStateOf(true) }
    var locationServices by rememberSaveable { mutableStateOf(true) }
    var analytics by rememberSaveable { mutableStateOf(false) }

    SampleScreenDisplayLazyColumn(title = "Switch") {
        item(key = "states") {
            SwitchSection(title = "States") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600),
                    verticalAlignment = Alignment.Top,
                ) {
                    SwitchStateSwatch(
                        checked = stateOff,
                        onCheckedChange = { value -> stateOff = value },
                    )

                    SwitchStateSwatch(
                        checked = stateOn,
                        onCheckedChange = { value -> stateOn = value },
                    )
                }
            }
        }

        item(key = "interactive") {
            SwitchSection(title = "Interactive") {
                SwitchRow(
                    label = "Dark Mode",
                    checked = isOn1,
                    onCheckedChange = { value -> isOn1 = value },
                )

                SwitchRow(
                    label = "Notifications",
                    checked = isOn2,
                    onCheckedChange = { value -> isOn2 = value },
                )
            }
        }

        item(key = "with-label") {
            SwitchSection(title = "With Label") {
                LemonadeUi.Switch(
                    checked = isOn3,
                    onCheckedChange = { value -> isOn3 = value },
                    label = "Enable push notifications",
                )

                LemonadeUi.Switch(
                    checked = isOn4,
                    onCheckedChange = { value -> isOn4 = value },
                    label = "Auto-update apps",
                )
            }
        }

        item(key = "with-support-text") {
            SwitchSection(title = "With Support Text") {
                LemonadeUi.Switch(
                    checked = locationServices,
                    onCheckedChange = { value -> locationServices = value },
                    label = "Location Services",
                    supportText = "Allow app to access your location",
                )

                LemonadeUi.Switch(
                    checked = analytics,
                    onCheckedChange = { value -> analytics = value },
                    label = "Analytics",
                    supportText = "Help us improve by sharing anonymous usage data",
                )
            }
        }

        item(key = "disabled") {
            SwitchSection(title = "Disabled") {
                SwitchRow(
                    label = "Disabled Off",
                    checked = false,
                    onCheckedChange = {},
                    enabled = false,
                )

                SwitchRow(
                    label = "Disabled On",
                    checked = true,
                    onCheckedChange = {},
                    enabled = false,
                )

                LemonadeUi.Switch(
                    checked = true,
                    onCheckedChange = {},
                    label = "Disabled with label",
                    enabled = false,
                )
            }
        }
    }
}

@Composable
private fun SwitchStateSwatch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
    ) {
        LemonadeUi.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        LemonadeUi.Text(
            text = if (checked) "On" else "Off",
            textStyle = LemonadeTheme.typography.bodySmallRegular,
        )
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LemonadeUi.Text(
            text = label,
            textStyle = LemonadeTheme.typography.bodyMediumRegular,
            color = if (enabled) {
                LemonadeTheme.colors.content.contentPrimary
            } else {
                LemonadeTheme.colors.content.contentSecondary
            },
        )
        LemonadeUi.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    }
}

@Composable
private fun SwitchSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
        modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing600),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
