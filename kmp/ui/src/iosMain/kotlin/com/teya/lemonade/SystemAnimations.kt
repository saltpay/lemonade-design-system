package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIAccessibilityIsReduceMotionEnabled

@Composable
internal actual fun rememberSystemAnimationsEnabled(): Boolean = remember { !UIAccessibilityIsReduceMotionEnabled() }
