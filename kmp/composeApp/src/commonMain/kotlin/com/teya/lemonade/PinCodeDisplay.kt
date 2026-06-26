@file:OptIn(ExperimentalLemonadeComponent::class)

package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadePinCodeVariant

private const val SamplePin = "159999"

@Composable
internal fun PinCodeDisplay() {
    SampleScreenDisplayColumn("PinCode") {
        PinCodeSection("Numeric (masked) with biometry") {
            var pin by remember { mutableStateOf("") }
            var error by remember { mutableStateOf(false) }

            LemonadeUi.PinCode(
                value = pin,
                onValueChange = {
                    pin = it
                    error = false
                },
                error = error,
                onBiometryClick = { pin = SamplePin },
                onComplete = { code -> error = code != SamplePin },
            )
            LemonadeUi.Text(
                text = "Entered: ${pin.length} digit(s)",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
            )
        }

        PinCodeSection("Numeric (unmasked)") {
            var pin by remember { mutableStateOf("") }
            LemonadeUi.PinCode(
                value = pin,
                onValueChange = { pin = it },
                masked = false,
            )
        }

        PinCodeSection("Alphanumeric (system keyboard, masked)") {
            var pin by remember { mutableStateOf("") }
            LemonadeUi.PinCode(
                value = pin,
                onValueChange = { pin = it },
                variant = LemonadePinCodeVariant.Alphanumeric,
            )
        }

        PinCodeSection("Alphanumeric (system keyboard, unmasked)") {
            var pin by remember { mutableStateOf("") }
            LemonadeUi.PinCode(
                value = pin,
                onValueChange = { pin = it },
                variant = LemonadePinCodeVariant.Alphanumeric,
                masked = false,
            )
        }

        PinCodeSection("Submitting") {
            LemonadeUi.PinCode(
                value = SamplePin,
                onValueChange = { /* no-op */ },
                submitting = true,
            )
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
        modifier = modifier,
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
