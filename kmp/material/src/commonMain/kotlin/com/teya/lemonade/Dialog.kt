package com.teya.lemonade

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.teya.lemonade.core.LemonadeCardPadding
import androidx.compose.ui.window.Dialog as ComposeDialog

/**
 * A custom dialog wrapper that combines Compose's [Dialog] with Lemonade's [Card] component.
 *
 * This composable provides a flexible, content-driven dialog that automatically wraps its content
 * in a [LemonadeUi.Card] with customizable padding and styling. The dialog follows the Material 3
 * motion guidelines with platform-level enter/exit animations on Skiko targets (iOS/Desktop).
 *
 * @param onDismissRequest Callback invoked when the dialog is dismissed (via back press, outside tap,
 *                        or explicit dismissal, depending on [properties]).
 * @param properties Configuration for the dialog's behavior (e.g., dismissable on outside tap, back press).
 *                  Defaults to [DialogProperties()] with standard dismissal behavior.
 * @param contentPadding The internal padding of the card content. Defaults to [LemonadeCardPadding.None].
 *                      Accepts None, XSmall, Small, or Medium padding sizes.
 * @param content A composable lambda that defines the dialog's content. This is rendered inside
 *               the Lemonade Card with the specified [contentPadding].
 *
 * ## Usage Example
 *
 * ```kotlin
 * var showDialog by remember { mutableStateOf(false) }
 *
 * if (showDialog) {
 *     LemonadeUi.Dialog(
 *         onDismissRequest = { showDialog = false },
 *         contentPadding = LemonadeCardPadding.Medium,
 *     ) {
 *         LemonadeUi.Text(text = "Custom dialog content")
 *     }
 * }
 * ```
 *
 * ## Design Notes
 *
 * - The dialog enforces a minimum height of 200.dp and fills the available width.
 * - Content is automatically wrapped in a [LemonadeUi.Card] using the Lemonade design system tokens.
 * - For a more structured dialog with title, text, and buttons, use [LemonadeUi.AlertDialog] instead.
 *
 * @see LemonadeUi.AlertDialog For a predefined alert dialog with title, text, and action buttons.
 * @see LemonadeUi.Card The card component that wraps this dialog's content.
 */
@Composable
public fun LemonadeUi.Dialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    contentPadding: LemonadeCardPadding = LemonadeCardPadding.None,
    content: @Composable () -> Unit,
) {
    ComposeDialog(
        onDismissRequest = onDismissRequest
    ) {
        LemonadeUi.Card(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 200.dp),
            contentPadding = contentPadding
        ) {
            content()
        }
    }
}
