package com.teya.lemonade

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager

internal fun Modifier.modifyIf(
    predicate: Boolean,
    modify: Modifier.() -> Modifier,
): Modifier =
    then(
        other = if (predicate) {
            modify()
        } else {
            this
        },
    )

internal fun Modifier.clearFocusOnKeyboardDismiss(): Modifier =
    composed {
        var isFocused by remember { mutableStateOf(false) }
        var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

        if (isFocused) {
            val imeIsVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
            val focusManager = LocalFocusManager.current

            LaunchedEffect(imeIsVisible) {
                when {
                    keyboardAppearedSinceLastFocused -> focusManager.clearFocus()
                    imeIsVisible -> keyboardAppearedSinceLastFocused = true
                }
            }
        }

        onFocusEvent { focusEvent ->
            isFocused = focusEvent.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
