package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun ToastDisplay() {
    val toastState = LocalLemonadeToastState.current

    SampleScreenDisplayColumn("Toast", itemsSpacing = LemonadeTheme.spaces.spacing600) {
        ToastSection("Voice Variants") {
            LemonadeUi.Button(
                label = "Success Toast",
                onClick = {
                    toastState.show(
                        label = "Changes saved successfully",
                        voice = ToastVoice.Success,
                    )
                },
            )
            LemonadeUi.Button(
                label = "Error Toast",
                onClick = {
                    toastState.show(
                        label = "Something went wrong",
                        voice = ToastVoice.Error,
                    )
                },
            )
            LemonadeUi.Button(
                label = "Neutral Toast",
                onClick = {
                    toastState.show(
                        label = "Copied to clipboard",
                        voice = ToastVoice.Neutral,
                    )
                },
            )
            LemonadeUi.Button(
                label = "Neutral with Icon",
                onClick = {
                    toastState.show(
                        label = "Link copied",
                        voice = ToastVoice.Neutral,
                        icon = LemonadeIcons.Link,
                    )
                },
            )
        }

        ToastSection("Durations") {
            LemonadeUi.Button(
                label = "Short (3s)",
                onClick = {
                    toastState.show(
                        label = "Short duration toast",
                        voice = ToastVoice.Neutral,
                        duration = ToastDuration.Short,
                    )
                },
            )
            LemonadeUi.Button(
                label = "Medium (6s)",
                onClick = {
                    toastState.show(
                        label = "Medium duration toast",
                        voice = ToastVoice.Neutral,
                        duration = ToastDuration.Medium,
                    )
                },
            )
            LemonadeUi.Button(
                label = "Long (9s)",
                onClick = {
                    toastState.show(
                        label = "Long duration toast",
                        voice = ToastVoice.Neutral,
                        duration = ToastDuration.Long,
                    )
                },
            )
        }

        ToastSection("Behaviors") {
            LemonadeUi.Button(
                label = "Non-dismissible",
                onClick = {
                    toastState.show(
                        label = "You can't swipe this away",
                        voice = ToastVoice.Neutral,
                        dismissible = false,
                        duration = ToastDuration.Short,
                    )
                },
            )
            LemonadeUi.Button(
                label = "Replace toast",
                onClick = {
                    toastState.show(
                        label = "First toast",
                        voice = ToastVoice.Success,
                    )
                },
            )
        }
    }
}

@Composable
private fun ToastSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
        )
        content()
    }
}
