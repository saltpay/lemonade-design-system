package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeCardPadding

@Composable
internal fun AlertDialogDisplay() {
    var showBasicDialog by remember { mutableStateOf(false) }
    var showDestructiveDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    SampleScreenDisplayColumn(
        title = "AlertDialog",
        itemsSpacing = LemonadeTheme.spaces.spacing600,
    ) {
        DialogSection(
            title = "Basic Alert Dialog",
        ) {
            LemonadeUi.Card(
                contentPadding = LemonadeCardPadding.Medium,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = LemonadeTheme.spaces.spacing300,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LemonadeUi.Text(
                        text = "A basic alert dialog with confirm and dismiss actions.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                        color = LemonadeTheme.colors.content.contentSecondary,
                    )

                    @OptIn(ExperimentalLemonadeComponent::class)
                    LemonadeUi.Button(
                        label = "Show Basic Dialog",
                        onClick = { showBasicDialog = true },
                        variant = LemonadeButtonVariant.Primary,
                        size = LemonadeButtonSize.Medium,
                    )
                }
            }
        }

        DialogSection(
            title = "Destructive Alert Dialog",
        ) {
            LemonadeUi.Card(
                contentPadding = LemonadeCardPadding.Medium,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = LemonadeTheme.spaces.spacing300,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LemonadeUi.Text(
                        text = "A destructive alert dialog with critical styling for dangerous actions.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                        color = LemonadeTheme.colors.content.contentSecondary,
                    )

                    @OptIn(ExperimentalLemonadeComponent::class)
                    LemonadeUi.Button(
                        label = "Show Destructive Dialog",
                        onClick = { showDestructiveDialog = true },
                        variant = LemonadeButtonVariant.CriticalSolid,
                        size = LemonadeButtonSize.Medium,
                    )
                }
            }
        }

        DialogSection(
            title = "Info Alert Dialog",
        ) {
            LemonadeUi.Card(
                contentPadding = LemonadeCardPadding.Medium,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = LemonadeTheme.spaces.spacing300,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LemonadeUi.Text(
                        text = "An info-only alert dialog with no dismiss button.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                        color = LemonadeTheme.colors.content.contentSecondary,
                    )

                    @OptIn(ExperimentalLemonadeComponent::class)
                    LemonadeUi.Button(
                        label = "Show Info Dialog",
                        onClick = { showInfoDialog = true },
                        variant = LemonadeButtonVariant.Primary,
                        size = LemonadeButtonSize.Medium,
                    )
                }
            }
        }
    }

    if (showBasicDialog) {
        LemonadeUi.AlertDialog(
            title = "Save changes?",
            message = "You have unsaved changes. Would you like to save them before leaving?",
            confirmLabel = "Save",
            dismissLabel = "Don't save",
            onConfirm = { showBasicDialog = false },
            onDismiss = { showBasicDialog = false },
            isDestructive = false,
        )
    }

    if (showDestructiveDialog) {
        LemonadeUi.AlertDialog(
            title = "Delete item?",
            message = "This action cannot be undone. The item will be permanently deleted from your account.",
            confirmLabel = "Delete",
            dismissLabel = "Cancel",
            onConfirm = { showDestructiveDialog = false },
            onDismiss = { showDestructiveDialog = false },
            isDestructive = true,
        )
    }

    if (showInfoDialog) {
        LemonadeUi.AlertDialog(
            title = "Update available",
            message = "A new version of the app is available. Update now to get the latest features and improvements.",
            confirmLabel = "Update now",
            onConfirm = { showInfoDialog = false },
            onDismiss = { showInfoDialog = false },
            isDestructive = false,
        )
    }
}

@Composable
private fun DialogSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = LemonadeTheme.spaces.spacing300,
        ),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
