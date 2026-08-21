package com.teya.lemonade

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

/**
 * On Android 8.1 and below the framework re-assigns focus whenever a window's focus is cleared —
 * even in touch mode, aiming at the previously focused rect — so `clearFocus` on a search or text
 * field is undone within the same frame and the field cannot be dismissed. Dismissal therefore
 * MOVES focus onto this zero-sized target instead of clearing it: the window never loses focus and
 * the legacy re-assign never runs. It draws nothing and taps are unaffected.
 *
 * The same versions also grant focus to the first focusable when a window opens, which lands on
 * the field and pops the keyboard unasked — being zero-sized, the decoy is invisible to that focus
 * search, so with [claimFocusOnEntry] it claims the grant back explicitly as soon as it enters
 * composition. Hosts whose surrounding screen legitimately assigns its own initial focus pass
 * `false` and keep the decoy as a dismissal target only.
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
