package com.teya.lemonade

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.teya.lemonade.core.LemonadeBottomSheetVariant

/**
 * Android-specific [BottomSheet][LemonadeUi.BottomSheet] variant that forces the system
 * navigation bar (back / home / recent buttons) hidden inside the sheet's dialog window.
 *
 * Every [LemonadeUi.BottomSheet] already keeps whichever system bars the host window hides, so an
 * app running fully immersive needs nothing from this overload. Reach for it only to hide the
 * navigation bar in the sheet while the host window still shows it.
 *
 * When [hideNavigationBar] is `false`, this behaves identically to the common
 * [LemonadeUi.BottomSheet]: the sheet inherits the host window's system bars.
 *
 * @param expanded Whether the bottom sheet is currently visible.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the bottom sheet.
 * @param hideNavigationBar Whether to hide the system navigation bar in the dialog window even
 *   when the host window shows it. Inheriting the host window's bars does not need this flag.
 * @param showDragHandle Whether to display the drag handle at the top of the sheet.
 * @param skipPartiallyExpanded Whether the partially expanded state should be skipped.
 * @param gesturesEnabled Whether the sheet responds to swipe/drag gestures. When `false`, the
 *   drag handle is hidden (overriding [showDragHandle]) and the sheet cannot be dragged. Defaults
 *   to `true`.
 * @param background The background variant of the bottom sheet. Defaults to
 *   [LemonadeBottomSheetVariant.Default].
 * @param properties Dismissal behaviour for the bottom sheet (back press / scrim tap). Defaults
 *   to [LemonadeBottomSheetProperties] with both flags enabled.
 * @param content A composable lambda with [ColumnScope] receiver that defines the sheet's content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LemonadeUi.BottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    hideNavigationBar: Boolean,
    showDragHandle: Boolean = true,
    skipPartiallyExpanded: Boolean = false,
    gesturesEnabled: Boolean = true,
    background: LemonadeBottomSheetVariant = LemonadeBottomSheetVariant.Default,
    properties: LemonadeBottomSheetProperties = LemonadeBottomSheetProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    CoreBottomSheet(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        showDragHandle = showDragHandle,
        skipPartiallyExpanded = skipPartiallyExpanded,
        gesturesEnabled = gesturesEnabled,
        background = background,
        properties = properties,
        forcedHiddenSystemBars = HiddenSystemBars(navigationBar = hideNavigationBar),
        content = content,
    )
}

@Deprecated(
    message = "Use the overload with a gesturesEnabled parameter.",
    replaceWith = ReplaceWith(
        "BottomSheet(expanded, onDismissRequest, hideNavigationBar, showDragHandle, " +
            "skipPartiallyExpanded, true, background, properties, content)",
    ),
    level = DeprecationLevel.HIDDEN,
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LemonadeUi.BottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    hideNavigationBar: Boolean,
    showDragHandle: Boolean = true,
    skipPartiallyExpanded: Boolean = false,
    background: LemonadeBottomSheetVariant = LemonadeBottomSheetVariant.Default,
    properties: LemonadeBottomSheetProperties = LemonadeBottomSheetProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    BottomSheet(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        hideNavigationBar = hideNavigationBar,
        showDragHandle = showDragHandle,
        skipPartiallyExpanded = skipPartiallyExpanded,
        gesturesEnabled = true,
        background = background,
        properties = properties,
        content = content,
    )
}

@Deprecated(
    message = "Use the overload with a properties parameter.",
    replaceWith = ReplaceWith(
        "BottomSheet(expanded, onDismissRequest, hideNavigationBar, showDragHandle, " +
            "skipPartiallyExpanded, true, background, LemonadeBottomSheetProperties(), content)",
    ),
    level = DeprecationLevel.HIDDEN,
)
@Composable
public fun LemonadeUi.BottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    hideNavigationBar: Boolean,
    showDragHandle: Boolean = true,
    skipPartiallyExpanded: Boolean = false,
    background: LemonadeBottomSheetVariant = LemonadeBottomSheetVariant.Default,
    content: @Composable ColumnScope.() -> Unit,
) {
    BottomSheet(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        hideNavigationBar = hideNavigationBar,
        showDragHandle = showDragHandle,
        skipPartiallyExpanded = skipPartiallyExpanded,
        background = background,
        properties = LemonadeBottomSheetProperties(),
        content = content,
    )
}
