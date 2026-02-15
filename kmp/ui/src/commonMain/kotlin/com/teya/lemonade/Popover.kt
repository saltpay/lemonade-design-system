package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

/**
 * Popover component that displays rich content in an overlay popup.
 *
 * A Popover shows contextual information with a title, description, and optional
 * action buttons. It features a dark background with inverse text colors and rounded corners.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Popover(
 *     title = "Feature info",
 *     description = "This feature helps you manage your settings",
 * )
 * ```
 *
 * @param title - [String] the title text displayed in the popover.
 * @param description - [String] the description text displayed below the title.
 * @param modifier - [Modifier] for additional styling.
 * @param primaryActionLabel - [String] optional label for primary action button.
 * @param onPrimaryAction - callback for primary action button click.
 * @param secondaryActionLabel - [String] optional label for secondary action button.
 * @param onSecondaryAction - callback for secondary action button click.
 */
@Composable
public fun LemonadeUi.Popover(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
) {
    CorePopover(
        title = title,
        description = description,
        modifier = modifier,
        primaryActionLabel = primaryActionLabel,
        onPrimaryAction = onPrimaryAction,
        secondaryActionLabel = secondaryActionLabel,
        onSecondaryAction = onSecondaryAction,
    )
}

/**
 * PopoverBox component that wraps content and shows a popover when visible.
 *
 * This component provides an interactive way to display popovers. The popover
 * appears above the content when [isVisible] is true and can be controlled
 * externally by the caller.
 *
 * ## Usage
 * ```kotlin
 * var showPopover by remember { mutableStateOf(false) }
 * LemonadeUi.PopoverBox(
 *     title = "Feature info",
 *     description = "Detailed description here",
 *     isVisible = showPopover,
 * ) {
 *     LemonadeUi.Button(
 *         label = "Show info",
 *         onClick = { showPopover = !showPopover },
 *     )
 * }
 * ```
 *
 * @param title - [String] the title text displayed in the popover.
 * @param description - [String] the description text displayed below the title.
 * @param isVisible - [Boolean] controls whether the popover is shown.
 * @param modifier - [Modifier] for additional styling.
 * @param primaryActionLabel - [String] optional label for primary action button.
 * @param onPrimaryAction - callback for primary action button click.
 * @param secondaryActionLabel - [String] optional label for secondary action button.
 * @param onSecondaryAction - callback for secondary action button click.
 * @param content - composable content that the popover is anchored to.
 */
@Composable
public fun LemonadeUi.PopoverBox(
    title: String,
    description: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isVisible) {
            LemonadeUi.Popover(
                title = title,
                description = description,
                modifier = Modifier.padding(bottom = LocalSpaces.current.spacing200),
                primaryActionLabel = primaryActionLabel,
                onPrimaryAction = onPrimaryAction,
                secondaryActionLabel = secondaryActionLabel,
                onSecondaryAction = onSecondaryAction,
            )
        }

        content()
    }
}

@Composable
private fun CorePopover(
    title: String,
    description: String,
    modifier: Modifier,
    primaryActionLabel: String?,
    onPrimaryAction: (() -> Unit)?,
    secondaryActionLabel: String?,
    onSecondaryAction: (() -> Unit)?,
) {
    Box(
        modifier = modifier
            .clip(LocalShapes.current.radius300)
            .background(LocalColors.current.background.bgDefaultInverse)
            .padding(LocalSpaces.current.spacing400),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing200),
        ) {
            LemonadeUi.Text(
                text = title,
                textStyle = LocalTypographies.current.bodySmallSemiBold,
                color = LocalColors.current.content.contentPrimaryInverse,
            )

            LemonadeUi.Text(
                text = description,
                textStyle = LocalTypographies.current.bodySmallRegular,
                color = LocalColors.current.content.contentPrimaryInverse,
            )

            val hasPrimary = primaryActionLabel != null && onPrimaryAction != null
            val hasSecondary = secondaryActionLabel != null && onSecondaryAction != null

            if (hasPrimary || hasSecondary) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing200),
                    modifier = Modifier.padding(top = LocalSpaces.current.spacing200),
                ) {
                    if (hasSecondary) {
                        LemonadeUi.Text(
                            text = secondaryActionLabel!!,
                            textStyle = LocalTypographies.current.bodySmallSemiBold,
                            color = LocalColors.current.content.contentSecondaryInverse,
                            modifier = Modifier
                                .clickable { onSecondaryAction?.invoke() }
                                .clip(LocalShapes.current.radius200)
                                .background(LocalColors.current.background.bgDefaultInverse)
                                .padding(
                                    horizontal = LocalSpaces.current.spacing300,
                                    vertical = LocalSpaces.current.spacing100,
                                ),
                        )
                    }

                    if (hasPrimary) {
                        LemonadeUi.Text(
                            text = primaryActionLabel!!,
                            textStyle = LocalTypographies.current.bodySmallSemiBold,
                            color = LocalColors.current.content.contentPrimaryInverse,
                            modifier = Modifier
                                .clickable { onPrimaryAction?.invoke() }
                                .clip(LocalShapes.current.radius200)
                                .background(LocalColors.current.content.contentSecondaryInverse)
                                .padding(
                                    horizontal = LocalSpaces.current.spacing300,
                                    vertical = LocalSpaces.current.spacing100,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@LemonadePreview
@Composable
internal fun PreviewPopover() {
    Column(
        modifier = Modifier.padding(LocalSpaces.current.spacing400),
        verticalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing400),
    ) {
        LemonadeUi.Popover(
            title = "Feature title",
            description = "This is a description of the feature",
        )

        LemonadeUi.Popover(
            title = "With actions",
            description = "This popover has action buttons",
            primaryActionLabel = "Got it",
            onPrimaryAction = {},
            secondaryActionLabel = "Dismiss",
            onSecondaryAction = {},
        )
    }
}
