package com.teya.lemonade

import androidx.compose.runtime.Composable
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import androidx.compose.material3.AlertDialog as M3AlertDialog
import androidx.compose.material3.AlertDialogDefaults as M3AlertDialogDefaults
import androidx.compose.material3.Text as M3Text
import androidx.compose.material3.TextButton as M3TextButton

@Composable
public fun LemonadeUi.AlertDialog(
    icon: LemonadeIcons,
    title: String,
    text: String,
    onConfirmation: () -> Unit,
    confirmationLabel: String,
    onDismissRequest: () -> Unit,
    dismissLabel: String,
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
    )
}
