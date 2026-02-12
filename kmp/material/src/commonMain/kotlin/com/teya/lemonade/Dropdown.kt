package com.teya.lemonade

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.material3.Text as M3Text

@Composable
public fun LemonadeUi.Dropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val dropdownMinWidth = 248.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .defaultMinSize(minWidth = dropdownMinWidth),
        offset = DpOffset(
            y = LemonadeTheme.spaces.spacing200,
            x = LemonadeTheme.spaces.spacing0
        ),
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
public fun LemonadeUi.DropdownItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: MenuItemColors = MenuDefaults.itemColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {

    /**
     * fixme - these doesn't seem usable tbh
     *  maybe implement the item from scratch?
     */
    DropdownMenuItem(
        onClick = onClick,
        modifier = modifier
            .padding(LemonadeTheme.spaces.spacing100)
            .clip(LemonadeTheme.shapes.radius400),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        contentPadding = PaddingValues(LemonadeTheme.spaces.spacing300),
        colors = colors,
        interactionSource = interactionSource,
        text = {
            M3Text(text = text)
        },
    )
}
