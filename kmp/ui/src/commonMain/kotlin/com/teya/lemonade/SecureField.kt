package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Marks this region as secure: while it is composed (and [enabled]), the hosting
 * window is excluded from OS screenshots and screen recordings. Intended for
 * password and other sensitive fields.
 *
 * This protects against capture only. To also mask the characters on screen,
 * pass `visualTransformation = PasswordVisualTransformation()` to the text field
 * — Compose draws its own text, so masking is a separate, existing option.
 *
 * Platform behaviour:
 * - **Android** — applies `FLAG_SECURE` to the Activity window while any secure
 *   region is present. This is window-level: Compose renders to a single
 *   surface, so the flag cannot be scoped to one composable.
 * - **iOS / Desktop** — currently a no-op. (iOS capture-protection for
 *   Compose-rendered content needs a native secure-layer overlay; planned as a
 *   follow-up.)
 *
 * @param enabled when `false` the modifier has no effect, so a show/hide toggle
 *   can flip it from state.
 */
@Composable
public fun Modifier.secureField(enabled: Boolean = true): Modifier = secureFieldModifier(enabled)

@Composable
internal expect fun Modifier.secureFieldModifier(enabled: Boolean): Modifier
