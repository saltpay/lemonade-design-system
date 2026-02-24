package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import androidx.compose.material3.AlertDialog as M3AlertDialog
import androidx.compose.material3.AlertDialogDefaults as M3AlertDialogDefaults
import androidx.compose.material3.Text as M3Text
import androidx.compose.material3.TextButton as M3TextButton

/**
 * A Material 3-based alert dialog following the Lemonade Design System.
 *
 * This composable wraps Material 3's [AlertDialog] and applies Lemonade theming with proper typography,
 * colors, and spacing. The dialog presents a user with a decision point, displaying an icon, title, body text,
 * and two action buttons (confirm and dismiss).
 *
 * The dialog uses Lemonade's design tokens for colors, typography, and icon sizing, while leveraging Material 3's
 * structure and Material Design 3 motion guidelines. Typography is automatically mapped to M3 tokens:
 * - Title uses [androidx.compose.material3.Typography.headlineSmall]
 * - Text uses [androidx.compose.material3.Typography.bodyMedium]
 * - Button text uses [androidx.compose.material3.Typography.labelLarge]
 *
 * @param icon The [LemonadeIcons] to display in the dialog header. Rendered at [LemonadeAssetSize.XXXLarge].
 * @param title The dialog title string. Displayed with M3 HeadlineSmall typography.
 * @param text The dialog body text string. Displayed with M3 BodyMedium typography.
 * @param onConfirmation Callback invoked when the user clicks the confirmation button.
 * @param confirmationLabel The label text for the confirmation button (e.g., "Ok", "Confirm", "Accept").
 * @param onDismissRequest Callback invoked when the dialog is dismissed (via confirmation button, dismiss button,
 *                        or external dismissal, depending on [properties]).
 * @param dismissLabel The label text for the dismiss button (e.g., "Cancel", "Dismiss", "Decline").
 * @param properties Configuration for dialog behavior. Defaults to non-dismissable outside taps and back presses
 *                  ([DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)]).
 *
 * ## Usage Example
 *
 * ```kotlin
 * var showDialog by remember { mutableStateOf(false) }
 *
 * if (showDialog) {
 *     LemonadeUi.AlertDialog(
 *         icon = LemonadeIcons.AlertCircle,
 *         title = "Confirm Action",
 *         text = "Are you sure you want to proceed?",
 *         onConfirmation = {
 *             // Handle confirmation
 *             showDialog = false
 *         },
 *         confirmationLabel = "Proceed",
 *         onDismissRequest = { showDialog = false },
 *         dismissLabel = "Cancel",
 *     )
 * }
 * ```
 *
 * ## Design Notes
 *
 * - By default, the dialog cannot be dismissed by tapping outside or pressing back. Override via [properties].
 * - Icon is tinted using [M3AlertDialogDefaults.iconContentColor] (typically the primary brand color).
 * - Text colors and typography follow Material 3 token mappings from the Lemonade Material theme.
 * - Both buttons are Material 3 [TextButton]s with default styling from [M3AlertDialogDefaults].
 * - The dialog includes platform-level enter/exit animations on Skiko (iOS/Desktop), following Material 3 motion patterns.
 *
 * @see LemonadeUi.Dialog For a more flexible, content-driven dialog without predefined structure.
 * @see androidx.compose.material3.AlertDialog The underlying Material 3 component.
 * @see LemonadeIcons For available icon options.
 */
@Composable
public fun LemonadeUi.AlertDialog(
    icon: LemonadeIcons,
    title: String,
    text: String,
    onConfirmation: () -> Unit,
    confirmationLabel: String,
    onDismissRequest: () -> Unit,
    dismissLabel: String,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false
    ),
) {
    M3AlertDialog(
        icon = {
            LemonadeUi.Icon(
                icon = icon,
                contentDescription = null,
                size = LemonadeAssetSize.XXXLarge,
                tint = M3AlertDialogDefaults.iconContentColor
            )
        },
        title = {
            M3Text(text = title)
        },
        text = {
            M3Text(text = text)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            M3TextButton(
                onClick = {
                    onConfirmation()
                },
            ) {
                M3Text(text = confirmationLabel)
            }
        },
        dismissButton = {
            M3TextButton(
                onClick = {
                    onDismissRequest()
                },
            ) {
                M3Text(text = dismissLabel)
            }
        },
        properties = properties
    )
}
