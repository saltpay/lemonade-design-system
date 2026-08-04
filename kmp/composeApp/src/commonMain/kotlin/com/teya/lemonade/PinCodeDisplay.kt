@file:OptIn(ExperimentalLemonadeComponent::class)

package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadePinCodeVariant

private const val SAMPLE_PIN = "159999"

@Composable
internal fun PinCodeDisplay() {
    SampleScreenDisplayLazyColumn(
        title = "PinCode",
        background = LemonadeTheme.colors.background.bgDefault,
    ) {
        item(key = "Numeric") {
            PinCodeSection("Numeric") {
                var pin by remember { mutableStateOf("") }
                var error by remember { mutableStateOf(false) }

                LemonadeUi.PinCode(
                    value = pin,
                    onValueChange = {
                        pin = it
                        error = false
                    },
                    error = error,
                    onComplete = { code -> error = code != SAMPLE_PIN },
                )
                LemonadeUi.Text(
                    text = "Entered: ${pin.length} digit(s)",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }

        item(key = "Alphanumeric") {
            PinCodeSection("Alphanumeric (system keyboard)") {
                var pin by remember { mutableStateOf("") }
                LemonadeUi.PinCode(
                    value = pin,
                    onValueChange = { pin = it },
                    variant = LemonadePinCodeVariant.Alphanumeric,
                )
            }
        }

        item(key = "Autofill disabled") {
            PinCodeSection("Autofill disabled") {
                var pin by remember { mutableStateOf("") }
                LemonadeUi.PinCode(
                    value = pin,
                    onValueChange = { pin = it },
                    oneTimeCodeAutofill = false,
                )
                LemonadeUi.Text(
                    text = "No OTC suggestion above the keyboard",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }

        item(key = "Submitting") {
            PinCodeSection("Submitting") {
                LemonadeUi.PinCode(
                    value = SAMPLE_PIN,
                    onValueChange = { /* no-op */ },
                    submitting = true,
                )
            }
        }
    }
}

@Composable
private fun PinCodeSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.padding(bottom = LemonadeTheme.spaces.spacing300),
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
