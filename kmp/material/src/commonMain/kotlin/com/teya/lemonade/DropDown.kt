package com.teya.lemonade

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.PopupProperties

@Composable
public fun LemonadeUi.DropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = DpOffset.Zero,
        scrollState = scrollState,
        containerColor = LemonadeTheme.colors.background.bgDefault,
        shape = LemonadeTheme.shapes.radius500,
        properties = PopupProperties(
            focusable = true,
            clippingEnabled = true,
            dismissOnBackPress = dismissable,
            dismissOnClickOutside = dismissable,
        ),
        border = border,
        content = content,
    )
}

@Composable
public fun LemonadeUi.DropDownItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    /**
     * fixme - these doesn't seem usable tbh
     *  maybe implement the item from scratch?
     */
    DropdownMenuItem(
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        contentPadding = PaddingValues.Zero,
        interactionSource = interactionSource,
        text = {
            LemonadeUi.Text(
                text = text,
                textStyle = LemonadeTheme.typography.bodyMediumRegular,
            )
        },
    )
}