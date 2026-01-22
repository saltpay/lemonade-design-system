package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.CheckboxStatus

@Composable
internal fun CheckboxDisplay() {
    var isChecked1 by remember { mutableStateOf(false) }
    var isChecked2 by remember { mutableStateOf(true) }
    var labeledChecked by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // States
        CheckboxSection(title = "States") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.Checkbox(
                        status = CheckboxStatus.Unchecked,
                        onCheckboxClicked = {}
                    )
                    LemonadeUi.Text(
                        text = "Unchecked",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.Checkbox(
                        status = CheckboxStatus.Checked,
                        onCheckboxClicked = {}
                    )
                    LemonadeUi.Text(
                        text = "Checked",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.Checkbox(
                        status = CheckboxStatus.Indeterminate,
                        onCheckboxClicked = {}
                    )
                    LemonadeUi.Text(
                        text = "Indeterminate",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }
            }
        }

        // Interactive
        CheckboxSection(title = "Interactive") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Checkbox(
                    status = if (isChecked1) CheckboxStatus.Checked else CheckboxStatus.Unchecked,
                    onCheckboxClicked = { isChecked1 = !isChecked1 }
                )
                LemonadeUi.Text(
                    text = "Tap to toggle",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Checkbox(
                    status = if (isChecked2) CheckboxStatus.Checked else CheckboxStatus.Unchecked,
                    onCheckboxClicked = { isChecked2 = !isChecked2 }
                )
                LemonadeUi.Text(
                    text = "Initially checked",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                )
            }
        }

        // With Label
        CheckboxSection(title = "With Label") {
            LemonadeUi.Checkbox(
                status = if (labeledChecked) CheckboxStatus.Checked else CheckboxStatus.Unchecked,
                onCheckboxClicked = { labeledChecked = !labeledChecked },
                label = "Accept terms and conditions"
            )

            LemonadeUi.Checkbox(
                status = CheckboxStatus.Checked,
                onCheckboxClicked = {},
                label = "Remember me"
            )

            LemonadeUi.Checkbox(
                status = CheckboxStatus.Indeterminate,
                onCheckboxClicked = {},
                label = "Select all items"
            )
        }

        // Disabled
        CheckboxSection(title = "Disabled") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Checkbox(
                    status = CheckboxStatus.Unchecked,
                    onCheckboxClicked = {},
                    enabled = false
                )
                LemonadeUi.Text(
                    text = "Disabled unchecked",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentSecondary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LemonadeUi.Checkbox(
                    status = CheckboxStatus.Checked,
                    onCheckboxClicked = {},
                    enabled = false
                )
                LemonadeUi.Text(
                    text = "Disabled checked",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentSecondary
                )
            }

            LemonadeUi.Checkbox(
                status = CheckboxStatus.Checked,
                onCheckboxClicked = {},
                label = "Disabled with label",
                enabled = false
            )
        }
    }
}

@Composable
private fun CheckboxSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary
        )
        content()
    }
}
