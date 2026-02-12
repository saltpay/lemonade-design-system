package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun DialogSampleDisplay() {
    val openAlertDialog = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }


    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {// Dialog
        DropdownSection(title = "Dialog") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Dialog",
                    onClick = { openDialog.value = !openDialog.value },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                when {
                    openDialog.value -> {
                        LemonadeUi.Dialog(
                            onDismissRequest = { openDialog.value = false },
                            contentPadding = LemonadeCardPadding.Medium,
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                LemonadeUi.Text(
                                    "Dialog content",
                                    color = LemonadeTheme.colors.content.contentSecondary,
                                )
                            }
                        }
                    }
                }
            }
        }

        // AlertDialog
        DropdownSection(title = "AlertDialog") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open AlertDialog",
                    onClick = { openAlertDialog.value = !openAlertDialog.value },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                when {
                    openAlertDialog.value -> {
                        LemonadeUi.AlertDialog(
                            icon = LemonadeIcons.CircleAlert,
                            title = "Alert dialog example",
                            text = "This is an example of an alert dialog with buttons",
                            dismissLabel = "Dismiss",
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                openAlertDialog.value = false
                                println("Confirmation registered") // Add logic here to handle confirmation.
                            },
                            confirmationLabel = "Confirmation",
                            properties = DialogProperties(
                                dismissOnClickOutside = true
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )

        content()
    }
}
