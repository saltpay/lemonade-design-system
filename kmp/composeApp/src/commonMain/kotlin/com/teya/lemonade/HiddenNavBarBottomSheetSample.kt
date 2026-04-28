package com.teya.lemonade

import androidx.compose.runtime.Composable

/**
 * Platform-specific sample demonstrating the [LemonadeUi.BottomSheet] variant with
 * `hideNavigationBar`. Only renders content on Android; no-op on other platforms.
 */
@Composable
internal expect fun HiddenNavBarBottomSheetSample()
