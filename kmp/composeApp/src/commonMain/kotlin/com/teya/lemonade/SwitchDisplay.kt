package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun SwitchDisplay() {
    var isOn1 by remember { mutableStateOf(false) }
    var isOn2 by remember { mutableStateOf(true) }
    var isOn3 by remember { mutableStateOf(false) }
    var isOn4 by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // States
        SwitchSection(title = "States") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.Switch(
                        checked = false,
                        onCheckedChange = {}
                    )
                    LemonadeUi.Text(
                        text = "Off",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.Switch(
                        checked = true,
                        onCheckedChange = {}
                    )
                    LemonadeUi.Text(
                        text = "On",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }
            }
        }

        // Interactive
        SwitchSection(title = "Interactive") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Text(
                    text = "Dark Mode",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                )
                LemonadeUi.Switch(
                    checked = isOn1,
                    onCheckedChange = { isOn1 = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Text(
                    text = "Notifications",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                )
                LemonadeUi.Switch(
                    checked = isOn2,
                    onCheckedChange = { isOn2 = it }
                )
            }
        }

        // With Label
        SwitchSection(title = "With Label") {
            LemonadeUi.Switch(
                checked = isOn3,
                onCheckedChange = { isOn3 = it },
                label = "Enable push notifications"
            )

            LemonadeUi.Switch(
                checked = isOn4,
                onCheckedChange = { isOn4 = it },
                label = "Auto-update apps"
            )
        }

        // With Support Text
        SwitchSection(title = "With Support Text") {
            LemonadeUi.Switch(
                checked = true,
                onCheckedChange = {},
                label = "Location Services",
                supportText = "Allow app to access your location"
            )

            LemonadeUi.Switch(
                checked = false,
                onCheckedChange = {},
                label = "Analytics",
                supportText = "Help us improve by sharing anonymous usage data"
            )
        }

        // Disabled
        SwitchSection(title = "Disabled") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Text(
                    text = "Disabled Off",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentSecondary
                )
                LemonadeUi.Switch(
                    checked = false,
                    onCheckedChange = {},
                    enabled = false
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Text(
                    text = "Disabled On",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentSecondary
                )
                LemonadeUi.Switch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false
                )
            }

            LemonadeUi.Switch(
                checked = true,
                onCheckedChange = {},
                label = "Disabled with label",
                enabled = false
            )
        }
    }
}

@Composable
private fun SwitchSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary
        )
        content()
    }
}
