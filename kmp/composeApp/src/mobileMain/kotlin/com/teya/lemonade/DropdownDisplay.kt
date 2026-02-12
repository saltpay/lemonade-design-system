package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun DropDownSampleDisplay() {
    var basicExpanded by remember { mutableStateOf(false) }
    var leadingIconsExpanded by remember { mutableStateOf(false) }
    var trailingIconsExpanded by remember { mutableStateOf(false) }
    var disabledItemsExpanded by remember { mutableStateOf(false) }
    var nonDismissableExpanded by remember { mutableStateOf(false) }
    var interactiveExpanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("Select an option") }

    val openAlertDialog = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {

        // Basic
        DropdownSection(title = "Dialog") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Dialog",
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
                        )
                    }
                }
            }
        }

        // Basic
        DropdownSection(title = "Basic") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Basic Menu",
                    onClick = { basicExpanded = true },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                LemonadeUi.Dropdown(
                    expanded = basicExpanded,
                    onDismissRequest = { basicExpanded = false },
                ) {
                    LemonadeUi.DropdownItem(
                        text = "Option 1",
                        onClick = { basicExpanded = false },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Option 2",
                        onClick = { basicExpanded = false },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Option 3",
                        onClick = { basicExpanded = false },
                    )
                }
            }
        }

        // With Leading Icons
        DropdownSection(title = "With Leading Icons") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Menu with Leading Icons",
                    onClick = { leadingIconsExpanded = true },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                LemonadeUi.Dropdown(
                    expanded = leadingIconsExpanded,
                    onDismissRequest = { leadingIconsExpanded = false },
                ) {
                    LemonadeUi.DropdownItem(
                        text = "Settings",
                        onClick = { leadingIconsExpanded = false },
                        leadingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Gear,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Profile",
                        onClick = { leadingIconsExpanded = false },
                        leadingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.User,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Notifications",
                        onClick = { leadingIconsExpanded = false },
                        leadingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Bell,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                        },
                    )
                }
            }
        }

        // With Trailing Icons
        DropdownSection(title = "With Trailing Icons") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Menu with Trailing Icons",
                    onClick = { trailingIconsExpanded = true },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                LemonadeUi.Dropdown(
                    expanded = trailingIconsExpanded,
                    onDismissRequest = { trailingIconsExpanded = false },
                ) {
                    LemonadeUi.DropdownItem(
                        text = "Edit",
                        onClick = { trailingIconsExpanded = false },
                        trailingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.PencilLine,
                                contentDescription = null,
                                size = LemonadeAssetSize.Small,
                            )
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Share",
                        onClick = { trailingIconsExpanded = false },
                        trailingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Share,
                                contentDescription = null,
                                size = LemonadeAssetSize.Small,
                            )
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Delete",
                        onClick = { trailingIconsExpanded = false },
                        trailingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Trash,
                                contentDescription = null,
                                size = LemonadeAssetSize.Small,
                            )
                        },
                    )
                }
            }
        }

        // With Disabled Items
        DropdownSection(title = "With Disabled Items") {
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Menu with Disabled Items",
                    onClick = { disabledItemsExpanded = true },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                LemonadeUi.Dropdown(
                    expanded = disabledItemsExpanded,
                    onDismissRequest = { disabledItemsExpanded = false },
                ) {
                    LemonadeUi.DropdownItem(
                        text = "Available Option",
                        onClick = { disabledItemsExpanded = false },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Disabled Option",
                        onClick = {},
                        enabled = false,
                    )
                    LemonadeUi.DropdownItem(
                        text = "Another Available",
                        onClick = { disabledItemsExpanded = false },
                    )
                }
            }
        }

        // Non-Dismissable
        DropdownSection(title = "Non-Dismissable") {
            LemonadeUi.Text(
                text = "This dropdown won't close when tapping outside",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Open Non-Dismissable Menu",
                    onClick = { nonDismissableExpanded = true },
                    variant = LemonadeButtonVariant.Secondary,
                    size = LemonadeButtonSize.Medium,
                )

                LemonadeUi.Dropdown(
                    expanded = nonDismissableExpanded,
                    onDismissRequest = { nonDismissableExpanded = false },
                    dismissable = false,
                ) {
                    LemonadeUi.DropdownItem(
                        text = "Close Menu",
                        onClick = { nonDismissableExpanded = false },
                        leadingIcon = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Times,
                                contentDescription = null,
                                size = LemonadeAssetSize.Small,
                            )
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Another Option",
                        onClick = { nonDismissableExpanded = false },
                    )
                }
            }
        }

        // Interactive
        DropdownSection(title = "Interactive Selection") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LemonadeUi.Text(
                    text = "Selected:",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                )
                LemonadeUi.Text(
                    text = selectedItem,
                    textStyle = LemonadeTheme.typography.bodyMediumMedium,
                    color = LemonadeTheme.colors.content.contentPrimary,
                )
            }
            Box {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.Button(
                    label = "Choose Option",
                    onClick = { interactiveExpanded = true },
                    variant = LemonadeButtonVariant.Primary,
                    size = LemonadeButtonSize.Medium,
                    trailingIcon = LemonadeIcons.ChevronDown,
                )

                LemonadeUi.Dropdown(
                    expanded = interactiveExpanded,
                    onDismissRequest = { interactiveExpanded = false },
                ) {
                    LemonadeUi.DropdownItem(
                        text = "Small",
                        onClick = {
                            selectedItem = "Small"
                            interactiveExpanded = false
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Medium",
                        onClick = {
                            selectedItem = "Medium"
                            interactiveExpanded = false
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Large",
                        onClick = {
                            selectedItem = "Large"
                            interactiveExpanded = false
                        },
                    )
                    LemonadeUi.DropdownItem(
                        text = "Extra Large",
                        onClick = {
                            selectedItem = "Extra Large"
                            interactiveExpanded = false
                        },
                    )
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
