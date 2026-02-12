package com.teya.lemonade

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.material3.LoadingIndicator as M3LoadingIndicator

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
public fun LemonadeUi.LoadingIndicator() {
    M3LoadingIndicator()
}