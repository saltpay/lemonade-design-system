package com.teya.lemonade

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import androidx.compose.material3.Text as M3Text

/**
 * A dropdown menu overlay following the Lemonade Design System.
 *
 * This composable provides a popup menu anchored to its parent layout, styled with Lemonade
 * design tokens for shape, color, and spacing. The menu visibility is controlled by the [expanded]
 * flag, following the same pattern used by [LemonadeUi.Dialog] and [LemonadeUi.BottomSheet].
 *
 * @param expanded Whether the dropdown menu is currently visible.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the dropdown
 *   (e.g., by tapping outside or pressing back, depending on [dismissOnBackPress]
 *   and [dismissOnClickOutside]).
 * @param dismissOnBackPress Whether pressing the back button dismisses the dropdown. Defaults to `true`.
 * @param dismissOnClickOutside Whether tapping outside the dropdown dismisses it. Defaults to `true`.
 * @param content A composable lambda with [ColumnScope] receiver that defines the dropdown's menu items.
 *   Typically composed of [LemonadeUi.DropdownItem] calls.
 *
 * ## Usage Example
 *
 * ```kotlin
 * var expanded by remember { mutableStateOf(false) }
 *
 * Box {
 *     LemonadeUi.Button(
 *         label = "Open Menu",
 *         onClick = { expanded = true },
 *     )
 *
 *     LemonadeUi.Dropdown(
 *         expanded = expanded,
 *         onDismissRequest = { expanded = false },
 *     ) {
 *         LemonadeUi.DropdownItem(
 *             text = "Option 1",
 *             onClick = { expanded = false },
 *         )
 *     }
 * }
 * ```
 *
 * ## Design Notes
 *
 * - The dropdown enforces a minimum width of 248.dp.
 * - Uses [LemonadeTheme.shapes.radius500] for rounded corners.
 * - Background color is [LemonadeTheme.colors.background.bgDefault].
 * - Vertical offset is [LemonadeTheme.spaces.spacing200] from the anchor.
 *
 * @see LemonadeUi.DropdownItem For individual menu items within this dropdown.
 * @see LemonadeUi.Dialog For a dialog overlay with the same visibility pattern.
 * @see LemonadeUi.BottomSheet For a bottom sheet overlay with the same visibility pattern.
 * @see DropdownMenu The underlying component.
 */
@Composable
public fun LemonadeUi.Dropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val dropdownMinWidth = 248.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .defaultMinSize(minWidth = dropdownMinWidth),
        offset = DpOffset(
            y = LemonadeTheme.spaces.spacing200,
            x = LemonadeTheme.spaces.spacing0,
        ),
        containerColor = LemonadeTheme.colors.background.bgDefault,
        shape = LemonadeTheme.shapes.radius500,
        properties = PopupProperties(
            focusable = true,
            clippingEnabled = true,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
        ),
        content = content,
    )
}

/**
 * A single item within a [LemonadeUi.Dropdown] menu, following the Lemonade Design System.
 *
 * This composable wraps [DropdownMenuItem] with Lemonade styling, and restricts
 * icon parameters to [LemonadeIcons] values. Icons are rendered internally using
 * [LemonadeUi.Icon] at appropriate sizes: [LemonadeAssetSize.Medium] for leading icons and
 * [LemonadeAssetSize.Small] for trailing icons. For arbitrary composable content in the trailing
 * position, use the `DropdownItem` overload with a `trailingSlot` parameter.
 *
 * @param text The label text displayed for this menu item.
 * @param onClick Callback invoked when the user clicks this menu item.
 * @param leadingIcon An optional [LemonadeIcons] displayed before the text.
 *   Rendered at [LemonadeAssetSize.Medium]. Defaults to `null` (no leading icon).
 * @param trailingIcon An optional [LemonadeIcons] displayed after the text.
 *   Rendered at [LemonadeAssetSize.Small]. Defaults to `null` (no trailing icon).
 * @param enabled Whether this menu item is interactive. Defaults to `true`.
 *   When `false`, the item is visually dimmed and does not respond to clicks.
 *
 * ## Usage Example
 *
 * ```kotlin
 * LemonadeUi.DropdownItem(
 *     text = "Settings",
 *     onClick = { /* handle click */ },
 *     leadingIcon = LemonadeIcons.Gear,
 * )
 *
 * LemonadeUi.DropdownItem(
 *     text = "Delete",
 *     onClick = { /* handle click */ },
 *     trailingIcon = LemonadeIcons.Trash,
 *     enabled = false,
 * )
 * ```
 *
 * @see LemonadeUi.Dropdown The parent dropdown menu component.
 * @see LemonadeIcons For available icon options.
 */
@Composable
public fun LemonadeUi.DropdownItem(
    text: String,
    onClick: () -> Unit,
    leadingIcon: LemonadeIcons? = null,
    trailingIcon: LemonadeIcons? = null,
    enabled: Boolean = true,
) {
    LemonadeUi.DropdownItem(
        text = text,
        onClick = onClick,
        leadingIcon = leadingIcon,
        trailingSlot = if (trailingIcon != null) {
            {
                LemonadeUi.Icon(
                    icon = trailingIcon,
                    contentDescription = null,
                    size = LemonadeAssetSize.Small,
                )
            }
        } else {
            null
        },
        enabled = enabled,
    )
}

/**
 * A single item within a [LemonadeUi.Dropdown] menu with slot-based trailing content.
 *
 * This composable wraps [DropdownMenuItem] with Lemonade styling. The leading position is
 * restricted to [LemonadeIcons] values, rendered internally using [LemonadeUi.Icon] at
 * [LemonadeAssetSize.Medium], while the trailing position accepts an arbitrary composable slot.
 * Slot content is rendered as provided, so callers are responsible for sizing it appropriately
 * for a menu item.
 *
 * @param text The label text displayed for this menu item.
 * @param onClick Callback invoked when the user clicks this menu item.
 * @param modifier [Modifier] applied to this menu item.
 * @param leadingIcon An optional [LemonadeIcons] displayed before the text.
 *   Rendered at [LemonadeAssetSize.Medium]. Defaults to `null` (no leading icon).
 * @param trailingSlot An optional composable displayed after the text.
 *   Defaults to `null` (no trailing content).
 * @param enabled Whether this menu item is interactive. Defaults to `true`.
 *   When `false`, the item is visually dimmed and does not respond to clicks.
 *
 * ## Usage Example
 *
 * ```kotlin
 * LemonadeUi.DropdownItem(
 *     text = "Notifications",
 *     onClick = { /* handle click */ },
 *     leadingIcon = LemonadeIcons.Bell,
 *     trailingSlot = {
 *         LemonadeUi.Badge(text = "12")
 *     },
 * )
 * ```
 *
 * @see LemonadeUi.Dropdown The parent dropdown menu component.
 * @see LemonadeIcons For available icon options.
 */
@Composable
public fun LemonadeUi.DropdownItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: LemonadeIcons? = null,
    trailingSlot: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
) {
    CoreDropdownItem(
        text = text,
        onClick = onClick,
        leadingSlot = if (leadingIcon != null) {
            {
                LemonadeUi.Icon(
                    icon = leadingIcon,
                    contentDescription = null,
                    size = LemonadeAssetSize.Medium,
                )
            }
        } else {
            null
        },
        trailingSlot = trailingSlot,
        enabled = enabled,
        modifier = modifier,
    )
}

@Composable
private fun CoreDropdownItem(
    text: String,
    onClick: () -> Unit,
    leadingSlot: (@Composable () -> Unit)?,
    trailingSlot: (@Composable () -> Unit)?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = modifier
            .padding(LemonadeTheme.spaces.spacing100)
            .clip(LemonadeTheme.shapes.radius400),
        leadingIcon = leadingSlot,
        trailingIcon = trailingSlot,
        enabled = enabled,
        contentPadding = PaddingValues(LemonadeTheme.spaces.spacing300),
        colors = MenuDefaults.itemColors(),
        text = {
            M3Text(text = text)
        },
    )
}
