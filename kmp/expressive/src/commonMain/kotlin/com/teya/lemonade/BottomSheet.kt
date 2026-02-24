package com.teya.lemonade

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp

/**
 * A bottom sheet overlay following the Lemonade Design System, wrapping Material 3's [ModalBottomSheet].
 *
 * This composable provides a modal bottom sheet that slides up from the bottom of the screen,
 * styled with Lemonade design tokens for shape, color, and elevation. The sheet visibility is
 * controlled by the [expanded] flag, following the same pattern used by [LemonadeUi.Dialog] and
 * [LemonadeUi.Dropdown].
 *
 * The component handles exit animations automatically: when [expanded] changes from `true` to
 * `false` (e.g., from a button click inside the sheet), the sheet animates out before being
 * removed from composition. Drag-to-dismiss and scrim taps also animate correctly.
 *
 * @param expanded Whether the bottom sheet is currently visible. When `false`, the sheet
 *   animates out and is removed from composition after the animation completes.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the bottom sheet
 *   (e.g., by swiping down, tapping the scrim, or pressing back).
 * @param sheetState The [SheetState] that controls the bottom sheet's internal state (e.g., expanded,
 *   partially expanded). Defaults to [rememberModalBottomSheetState].
 * @param showDragHandle Whether to display the drag handle at the top of the sheet. Defaults to `true`.
 * @param content A composable lambda with [ColumnScope] receiver that defines the sheet's content.
 *
 * ## Usage Example
 *
 * ```kotlin
 * var showSheet by remember { mutableStateOf(false) }
 *
 * LemonadeUi.Button(
 *     label = "Open Bottom Sheet",
 *     onClick = { showSheet = true },
 * )
 *
 * LemonadeUi.BottomSheet(
 *     expanded = showSheet,
 *     onDismissRequest = { showSheet = false },
 * ) {
 *     Column(modifier = Modifier.padding(LemonadeTheme.spaces.spacing400)) {
 *         LemonadeUi.Text(text = "Bottom Sheet Title", textStyle = LemonadeTheme.typography.headingSmall)
 *         LemonadeUi.Text(text = "Sheet body content goes here.")
 *     }
 * }
 * ```
 *
 * ## Design Notes
 *
 * - The sheet uses [LemonadeTheme.radius.radius500] for the top corners.
 * - Background color is [LemonadeTheme.colors.background.bgDefault].
 * - Tonal elevation is set to 0.dp; the sheet relies on Lemonade color tokens for visual hierarchy.
 * - The drag handle uses Material 3's default [BottomSheetDefaults.DragHandle] styling.
 * - For overlay components with a unified visibility API, see also [LemonadeUi.Dialog] and
 *   [LemonadeUi.Dropdown], which share the same `expanded` flag pattern.
 *
 * @see LemonadeUi.Dialog For a dialog overlay with the same visibility pattern.
 * @see LemonadeUi.Dropdown For a dropdown menu overlay with the same visibility pattern.
 * @see ModalBottomSheet The underlying Material 3 component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LemonadeUi.BottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    showDragHandle: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(expanded) {
        if (!expanded && sheetState.isVisible) {
            sheetState.hide()
        }
    }

    if (expanded || sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(
                topStart = LemonadeTheme.radius.radius500,
                topEnd = LemonadeTheme.radius.radius500,
            ),
            containerColor = LemonadeTheme.colors.background.bgDefault,
            tonalElevation = 0.dp,
            dragHandle = if (showDragHandle) {
                { BottomSheetDefaults.DragHandle() }
            } else {
                null
            },
            content = content,
        )
    }
}
