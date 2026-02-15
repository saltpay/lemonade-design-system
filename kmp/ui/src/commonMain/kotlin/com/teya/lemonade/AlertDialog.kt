package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Lemonade alert dialog component. A modal dialog that displays a title, message, and action buttons.
 * ## Usage
 * ```kotlin
 * LemonadeUi.AlertDialog(
 *   title = "Delete item?",
 *   message = "This action cannot be undone.",
 *   confirmLabel = "Delete",
 *   onConfirm = { /* handle confirm */ },
 *   onDismiss = { /* handle dismiss */ },
 *   dismissLabel = "Cancel",
 *   isDestructive = true,
 * )
 * ```
 * @param title - [String] title text displayed at the top of the dialog.
 * @param message - [String] message text displayed below the title.
 * @param confirmLabel - [String] label for the confirm button.
 * @param onConfirm - Callback invoked when the confirm button is clicked.
 * @param onDismiss - Callback invoked when the dialog is dismissed (scrim tap or dismiss button).
 * @param modifier - [Modifier] to be applied to the dialog container.
 * @param dismissLabel - [String] optional label for the dismiss button. If null, no dismiss button is shown.
 * @param isDestructive - [Boolean] flag to style the confirm button as destructive (critical).
 */
@Composable
public fun LemonadeUi.AlertDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissLabel: String? = null,
    isDestructive: Boolean = false,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = LocalSpaces.current.spacing400,
            ),
            modifier = modifier
                .widthIn(
                    min = 280.dp,
                    max = 400.dp,
                )
                .clip(
                    shape = LocalShapes.current.radius400,
                )
                .background(
                    color = LocalColors.current.background.bgElevated,
                )
                .border(
                    width = LocalBorderWidths.current.base.border25,
                    color = LocalColors.current.border.borderNeutralLow,
                    shape = LocalShapes.current.radius400,
                )
                .padding(
                    all = LocalSpaces.current.spacing500,
                ),
        ) {
            LemonadeUi.Text(
                text = title,
                textStyle = LocalTypographies.current.headingSmall,
                color = LocalColors.current.content.contentPrimary,
            )

            LemonadeUi.Text(
                text = message,
                textStyle = LocalTypographies.current.bodyMediumRegular,
                color = LocalColors.current.content.contentSecondary,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = LocalSpaces.current.spacing300,
                    alignment = androidx.compose.ui.Alignment.End,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (dismissLabel != null) {
                    @OptIn(ExperimentalLemonadeComponent::class)
                    LemonadeUi.Button(
                        label = dismissLabel,
                        onClick = onDismiss,
                        variant = LemonadeButtonVariant.Neutral,
                        size = LemonadeButtonSize.Medium,
                    )
                }

                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = confirmLabel,
                    onClick = onConfirm,
                    variant = if (isDestructive) {
                        LemonadeButtonVariant.CriticalSolid
                    } else {
                        LemonadeButtonVariant.Primary
                    },
                    size = LemonadeButtonSize.Medium,
                )
            }
        }
    }
}

private data class AlertDialogPreviewData(
    val title: String,
    val message: String,
    val confirmLabel: String,
    val dismissLabel: String?,
    val isDestructive: Boolean,
)

private class AlertDialogPreviewProvider : PreviewParameterProvider<AlertDialogPreviewData> {
    override val values: Sequence<AlertDialogPreviewData> = sequenceOf(
        AlertDialogPreviewData(
            title = "Delete item?",
            message = "This action cannot be undone. The item will be permanently deleted.",
            confirmLabel = "Delete",
            dismissLabel = "Cancel",
            isDestructive = true,
        ),
        AlertDialogPreviewData(
            title = "Save changes?",
            message = "You have unsaved changes. Would you like to save them before leaving?",
            confirmLabel = "Save",
            dismissLabel = "Don't save",
            isDestructive = false,
        ),
        AlertDialogPreviewData(
            title = "Update available",
            message = "A new version of the app is available. Update now to get the latest features.",
            confirmLabel = "Update",
            dismissLabel = null,
            isDestructive = false,
        ),
    )
}

@LemonadePreview
@Composable
private fun AlertDialogPreview(
    @PreviewParameter(AlertDialogPreviewProvider::class)
    previewData: AlertDialogPreviewData,
) {
    LemonadeUi.AlertDialog(
        title = previewData.title,
        message = previewData.message,
        confirmLabel = previewData.confirmLabel,
        onConfirm = { /* Nothing */ },
        onDismiss = { /* Nothing */ },
        dismissLabel = previewData.dismissLabel,
        isDestructive = previewData.isDestructive,
    )
}
