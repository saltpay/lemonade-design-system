package com.teya.lemonade

import androidx.compose.runtime.Composable

/**
 * Resolves [LemonadeAnimationMode.System] against the platform preference. The preference is
 * read once when the calling composition first composes.
 */
@Composable
internal expect fun rememberSystemAnimationsEnabled(): Boolean
