package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.ToastVoice

@Composable
internal fun ToastDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Neutral
        ToastSection(title = "Neutral") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
            ) {
                LemonadeUi.Toast(
                    message = "Settings updated",
                    voice = ToastVoice.Neutral,
                )
                LemonadeUi.Toast(
                    message = "Changes saved",
                    voice = ToastVoice.Neutral,
                    leadingIcon = LemonadeIcons.Check,
                )
            }
        }

        // Info
        ToastSection(title = "Info") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
            ) {
                LemonadeUi.Toast(
                    message = "New version available",
                    voice = ToastVoice.Info,
                )
                LemonadeUi.Toast(
                    message = "3 items in your cart",
                    voice = ToastVoice.Info,
                )
            }
        }

        // Warning
        ToastSection(title = "Warning") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
            ) {
                LemonadeUi.Toast(
                    message = "Connection unstable",
                    voice = ToastVoice.Warning,
                )
                LemonadeUi.Toast(
                    message = "Battery running low",
                    voice = ToastVoice.Warning,
                )
            }
        }

        // Critical
        ToastSection(title = "Critical") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
            ) {
                LemonadeUi.Toast(
                    message = "Payment failed",
                    voice = ToastVoice.Critical,
                )
                LemonadeUi.Toast(
                    message = "Unable to connect to server",
                    voice = ToastVoice.Critical,
                )
            }
        }

        // Positive
        ToastSection(title = "Positive") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
            ) {
                LemonadeUi.Toast(
                    message = "Payment successful",
                    voice = ToastVoice.Positive,
                )
                LemonadeUi.Toast(
                    message = "File uploaded",
                    voice = ToastVoice.Positive,
                )
            }
        }

        // Without Icon
        ToastSection(title = "Without Icon") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
            ) {
                LemonadeUi.Toast(
                    message = "Settings updated",
                    voice = ToastVoice.Neutral,
                    leadingIcon = null,
                )
                LemonadeUi.Toast(
                    message = "New notification received",
                    voice = ToastVoice.Info,
                    leadingIcon = null,
                )
                LemonadeUi.Toast(
                    message = "Check your connection",
                    voice = ToastVoice.Warning,
                    leadingIcon = null,
                )
                LemonadeUi.Toast(
                    message = "Action failed",
                    voice = ToastVoice.Critical,
                    leadingIcon = null,
                )
                LemonadeUi.Toast(
                    message = "Task completed",
                    voice = ToastVoice.Positive,
                    leadingIcon = null,
                )
            }
        }
    }
}

@Composable
private fun ToastSection(
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
