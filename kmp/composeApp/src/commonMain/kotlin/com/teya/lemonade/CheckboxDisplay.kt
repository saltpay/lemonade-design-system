package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.CheckboxStatus

/**
 * Cycles a checkbox through every status so the tri-state values stay reachable by tapping.
 */
private fun CheckboxStatus.next(): CheckboxStatus =
    when (this) {
        CheckboxStatus.Unchecked -> CheckboxStatus.Checked
        CheckboxStatus.Checked -> CheckboxStatus.Indeterminate
        CheckboxStatus.Indeterminate -> CheckboxStatus.Unchecked
    }

private fun CheckboxStatus.toggle(): CheckboxStatus =
    if (this == CheckboxStatus.Checked) {
        CheckboxStatus.Unchecked
    } else {
        CheckboxStatus.Checked
    }

@Suppress("LongMethod")
@Composable
internal fun CheckboxDisplay() {
    var uncheckedSwatch by rememberSaveable { mutableStateOf(CheckboxStatus.Unchecked) }
    var checkedSwatch by rememberSaveable { mutableStateOf(CheckboxStatus.Checked) }
    var indeterminateSwatch by rememberSaveable { mutableStateOf(CheckboxStatus.Indeterminate) }
    var isChecked1 by rememberSaveable { mutableStateOf(false) }
    var isChecked2 by rememberSaveable { mutableStateOf(true) }
    var labeledChecked by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(CheckboxStatus.Checked) }
    var selectAll by rememberSaveable { mutableStateOf(CheckboxStatus.Indeterminate) }

    SampleScreenDisplayLazyColumn(title = "Checkbox") {
        item(key = "states") {
            CheckboxSection(title = "States") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600),
                    verticalAlignment = Alignment.Top,
                ) {
                    CheckboxStateSwatch(
                        status = uncheckedSwatch,
                        onCheckboxClicked = { uncheckedSwatch = uncheckedSwatch.next() },
                    )

                    CheckboxStateSwatch(
                        status = checkedSwatch,
                        onCheckboxClicked = { checkedSwatch = checkedSwatch.next() },
                    )

                    CheckboxStateSwatch(
                        status = indeterminateSwatch,
                        onCheckboxClicked = { indeterminateSwatch = indeterminateSwatch.next() },
                    )
                }
            }
        }

        item(key = "interactive") {
            CheckboxSection(title = "Interactive") {
                CheckboxRow(
                    label = "Tap to toggle",
                    status = if (isChecked1) CheckboxStatus.Checked else CheckboxStatus.Unchecked,
                    onCheckboxClicked = { isChecked1 = !isChecked1 },
                )

                CheckboxRow(
                    label = "Initially checked",
                    status = if (isChecked2) CheckboxStatus.Checked else CheckboxStatus.Unchecked,
                    onCheckboxClicked = { isChecked2 = !isChecked2 },
                )
            }
        }

        item(key = "with-label") {
            CheckboxSection(title = "With Label") {
                LemonadeUi.Checkbox(
                    status = if (labeledChecked) CheckboxStatus.Checked else CheckboxStatus.Unchecked,
                    onCheckboxClicked = { labeledChecked = !labeledChecked },
                    label = "Accept terms and conditions",
                )

                LemonadeUi.Checkbox(
                    status = rememberMe,
                    onCheckboxClicked = { rememberMe = rememberMe.toggle() },
                    label = "Remember me",
                )

                LemonadeUi.Checkbox(
                    status = selectAll,
                    onCheckboxClicked = { selectAll = selectAll.next() },
                    label = "Select all items",
                )
            }
        }

        item(key = "disabled") {
            CheckboxSection(title = "Disabled") {
                CheckboxRow(
                    label = "Disabled unchecked",
                    status = CheckboxStatus.Unchecked,
                    onCheckboxClicked = {},
                    enabled = false,
                )

                CheckboxRow(
                    label = "Disabled checked",
                    status = CheckboxStatus.Checked,
                    onCheckboxClicked = {},
                    enabled = false,
                )

                LemonadeUi.Checkbox(
                    status = CheckboxStatus.Checked,
                    onCheckboxClicked = {},
                    label = "Disabled with label",
                    enabled = false,
                )
            }
        }
    }
}

@Composable
private fun CheckboxStateSwatch(
    status: CheckboxStatus,
    onCheckboxClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200),
    ) {
        LemonadeUi.Checkbox(
            status = status,
            onCheckboxClicked = onCheckboxClicked,
        )
        LemonadeUi.Text(
            text = status.name,
            textStyle = LemonadeTheme.typography.bodySmallRegular,
        )
    }
}

@Composable
private fun CheckboxRow(
    label: String,
    status: CheckboxStatus,
    onCheckboxClicked: () -> Unit,
    enabled: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LemonadeUi.Checkbox(
            status = status,
            onCheckboxClicked = onCheckboxClicked,
            enabled = enabled,
        )
        LemonadeUi.Text(
            text = label,
            textStyle = LemonadeTheme.typography.bodyMediumRegular,
            color = if (enabled) {
                LemonadeTheme.colors.content.contentPrimary
            } else {
                LemonadeTheme.colors.content.contentSecondary
            },
        )
    }
}

@Composable
private fun CheckboxSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
        modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing600),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
