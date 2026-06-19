package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// TODO: iOS capture-protection for Compose-rendered content requires overlaying
// a native secure layer (UITextField.isSecureTextEntry). Tracked as a follow-up;
// no-op for now so `secureField()` is safe to call cross-platform.
@Composable
internal actual fun Modifier.secureFieldModifier(enabled: Boolean): Modifier = this
