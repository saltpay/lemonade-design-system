package com.teya.lemonade

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

/**
 * Invisible focus target for dismissing text fields on Android 8.1 and below, where the framework
 * undoes `clearFocus` within the same frame and focuses the first field when a window opens.
 * Dismissal moves focus here instead of clearing it, and [claimFocusOnEntry] absorbs the
 * window-open grant.
 */
@Composable
internal fun SearchFocusDecoy(
    focusRequester: FocusRequester,
    claimFocusOnEntry: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .focusRequester(focusRequester = focusRequester)
            .focusable(),
    )

    if (claimFocusOnEntry) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
