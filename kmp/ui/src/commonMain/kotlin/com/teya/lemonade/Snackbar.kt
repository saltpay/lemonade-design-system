package com.teya.lemonade

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * A brief notification that appears at the bottom of the screen.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Snackbar(
 *     message = "Item added to cart",
 *     actionLabel = "Undo",
 *     onActionClick = { /* handle action */ },
 * )
 * ```
 *
 * @param message - [String] message text to display in the snackbar.
 * @param modifier - [Modifier] to be applied to the root container of the snackbar.
 * @param actionLabel - optional [String] text for the action button.
 * @param onActionClick - optional callback invoked when the action is clicked.
 */
@Composable
public fun LemonadeUi.Snackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    CoreSnackbar(
        message = message,
        modifier = modifier,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
    )
}

@Composable
private fun CoreSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = LocalColors.current.background.bgDefaultInverse,
                shape = LocalShapes.current.radius300,
            )
            .border(
                border = BorderStroke(
                    width = LocalBorderWidths.current.base.border25,
                    color = LocalColors.current.border.borderNeutralLow,
                ),
                shape = LocalShapes.current.radius300,
            )
            .padding(
                horizontal = LocalSpaces.current.spacing500,
                vertical = LocalSpaces.current.spacing300,
            ),
    ) {
        LemonadeUi.Text(
            text = message,
            textStyle = LocalTypographies.current.bodyMediumMedium,
            color = LocalColors.current.content.contentPrimaryInverse,
            modifier = Modifier.weight(weight = 1f),
        )

        if (actionLabel != null && onActionClick != null) {
            LemonadeUi.Text(
                text = actionLabel,
                textStyle = LocalTypographies.current.bodyMediumMedium,
                color = LocalColors.current.content.contentBrand,
                modifier = Modifier.clickable(onClick = onActionClick),
            )
        }
    }
}

private data class SnackbarPreviewData(
    val message: String,
    val actionLabel: String?,
)

private class SnackbarPreviewProvider :
    PreviewParameterProvider<SnackbarPreviewData> {
    override val values: Sequence<SnackbarPreviewData> = buildAllVariants()
    private fun buildAllVariants(): Sequence<SnackbarPreviewData> {
        return listOf(
            SnackbarPreviewData(
                message = "Item added to cart",
                actionLabel = null,
            ),
            SnackbarPreviewData(
                message = "Item added to cart",
                actionLabel = "Undo",
            ),
        ).asSequence()
    }
}

@Composable
@LemonadePreview
private fun SnackbarPreview(
    @PreviewParameter(SnackbarPreviewProvider::class)
    previewData: SnackbarPreviewData,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        LemonadeUi.Snackbar(
            message = previewData.message,
            actionLabel = previewData.actionLabel,
            onActionClick = previewData.actionLabel?.let { { } },
        )
    }
}
