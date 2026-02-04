package com.teya.lemonade

import androidx.compose.runtime.Composable

internal actual val chipPlatformDimensions: ChipPlatformDimensions
    @Composable get() {
        return defaultChipDimensions()
    }