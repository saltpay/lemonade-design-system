package com.teya.lemonade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * A free-content dialog following the Lemonade Design System.
 *
 * This composable provides a flexible dialog that displays custom content inside a styled [Surface]
 * with Lemonade design tokens for shape, color, and elevation. The dialog visibility is controlled
 * by the [expanded] flag, following the same pattern used by [LemonadeUi.Dropdown] and
 * [LemonadeUi.BottomSheet].
 *
 * The dialog enters with an expressive spring animation that expands vertically from the center,
 * giving a bouncy, lively feel.
 *
 * @param expanded Whether the dialog is currently visible. When `false`, the dialog is not composed.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the dialog
 *   (e.g., by tapping outside or pressing back, depending on [dismissOnClickOutside]
 *   and [dismissOnBackPress]).
 * @param dismissOnClickOutside Whether tapping outside the dialog dismisses it. Defaults to `true`.
 * @param dismissOnBackPress Whether pressing the back button dismisses the dialog. Defaults to `true`.
 * @param content A composable lambda that defines the dialog's content.
 *
 * ## Usage Example
 *
 * ```kotlin
 * var showDialog by remember { mutableStateOf(false) }
 *
 * LemonadeUi.Button(
 *     label = "Open Dialog",
 *     onClick = { showDialog = true },
 * )
 *
 * LemonadeUi.Dialog(
 *     expanded = showDialog,
 *     onDismissRequest = { showDialog = false },
 * ) {
 *     Column(modifier = Modifier.padding(LemonadeTheme.spaces.spacing400)) {
 *         LemonadeUi.Text(text = "Dialog Title", textStyle = LemonadeTheme.typography.headingSmall)
 *         LemonadeUi.Text(text = "Dialog body content goes here.")
 *     }
 * }
 * ```
 *
 * ## Design Notes
 *
 * - The dialog surface uses [LemonadeTheme.radius.radius400] for rounded corners.
 * - Background color is [LemonadeTheme.colors.background.bgDefault].
 * - Tonal elevation is set to 0.dp; the dialog relies on Lemonade color tokens for visual hierarchy.
 * - Enter animation uses a loose spring with vertical expansion from the center.
 * - For overlay components with a unified visibility API, see also [LemonadeUi.Dropdown] and
 *   [LemonadeUi.BottomSheet], which share the same `expanded` flag pattern.
 *
 * @see LemonadeUi.BottomSheet For a bottom sheet overlay with the same visibility pattern.
 * @see LemonadeUi.Dropdown For a dropdown menu overlay with the same visibility pattern.
 * @see BasicAlertDialog The underlying component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LemonadeUi.Dialog(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (expanded) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = dismissOnClickOutside,
                dismissOnBackPress = dismissOnBackPress,
            ),
        ) {
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                    expandFrom = Alignment.CenterVertically,
                ) + fadeIn(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                    ),
                ),
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(size = LemonadeTheme.radius.radius400),
                    color = LemonadeTheme.colors.background.bgDefault,
                    tonalElevation = 0.dp,
                    content = content,
                )
            }
        }
    }
}
