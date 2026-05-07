@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)
@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow

/**
 * Represents a single item in a [LemonadeUi.BottomTabBar].
 *
 * @param label The text label displayed below the icon.
 * @param icon The [LemonadeIcons] displayed above the label when the item is not selected.
 * @param selectedIcon Optional [LemonadeIcons] rendered while the item is selected. A common
 *   pattern is to pair an outline `icon` with its solid variant here (e.g. `Wallet` /
 *   `WalletSolid`). When `null`, [icon] is used for both states.
 */
public data class BottomTabBarItem(
    val label: String,
    val icon: LemonadeIcons,
    val selectedIcon: LemonadeIcons? = null,
)

/**
 * A floating, pill-shaped bottom navigation bar for top-level destinations.
 *
 * Built on Material3's [HorizontalFloatingToolbar] so the bar inherits its elevation, motion and
 * interaction styling. Items are distributed equally across the bar; the selected item is
 * highlighted with an elevated background pill. The component paints a soft scroll-edge gradient
 * behind itself so it floats nicely over scrollable content.
 *
 * The component already applies `Modifier.navigationBarsPadding`, so callers do not need to pad
 * around the system navigation bar themselves.
 *
 * ## Usage
 * ```kotlin
 * var selectedIndex by remember { mutableStateOf(value = 0) }
 *
 * LemonadeUi.BottomTabBar(
 *     items = listOf(
 *         BottomTabBarItem(label = "Home", icon = LemonadeIcons.Home),
 *         BottomTabBarItem(label = "Sales", icon = LemonadeIcons.ChartStats),
 *         BottomTabBarItem(label = "Money", icon = LemonadeIcons.Wallet),
 *     ),
 *     selectedIndex = selectedIndex,
 *     onItemSelected = { index -> selectedIndex = index },
 * )
 * ```
 *
 * @param items A non-empty list of [BottomTabBarItem] to display.
 * @param selectedIndex The index of the currently selected item. Must be within [items] indices.
 * @param onItemSelected A callback invoked with the index of the item the user selected.
 * @param modifier The [Modifier] to be applied to the root container of the component.
 */
@Composable
public fun LemonadeUi.BottomTabBar(
    items: List<BottomTabBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CoreBottomTabBar(
        items = items,
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected,
        modifier = modifier,
    )
}

private val MaxBarWidth: Dp = 500.dp
private val ItemHeight: Dp = 54.dp

@Composable
internal fun CoreBottomTabBar(
    items: List<BottomTabBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(
        value = items.isNotEmpty(),
        lazyMessage = { "BottomTabBar items list should not be empty." },
    )
    require(
        value = selectedIndex in items.indices,
        lazyMessage = {
            "Selected index ($selectedIndex) is out of bounds for items list of size ${items.size}."
        },
    )

    val edgeColor = LemonadeTheme.colors.background.bgSubtle

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        edgeColor.copy(alpha = 0f),
                        edgeColor,
                    ),
                ),
            )
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400)
            .height(intrinsicSize = IntrinsicSize.Min),
    ) {
        HorizontalFloatingToolbar(
            modifier = Modifier
                .weight(weight = 1f, fill = false)
                .widthIn(max = MaxBarWidth)
                .lemonadeShadow(
                    shadow = LemonadeShadow.Xlarge,
                    shape = LemonadeTheme.shapes.radiusFull,
                ),
            expanded = true,
            colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                toolbarContainerColor = LemonadeTheme.colors.background.bgDefault,
            ),
            contentPadding = PaddingValues(all = LemonadeTheme.spaces.spacing100),
        ) {
            items.forEachIndexed { index, item ->
                BottomTabBarItemContent(
                    item = item,
                    isSelected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    modifier = Modifier.weight(weight = 1f),
                )
            }
        }
    }
}

@Composable
private fun BottomTabBarItemContent(
    item: BottomTabBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) {
        LemonadeTheme.colors.background.bgElevated
    } else {
        LemonadeTheme.colors.background.bgDefault
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .height(height = ItemHeight)
            .clip(shape = LemonadeTheme.shapes.radiusFull)
            .background(color = backgroundColor)
            .clickable(
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = LocalEffects.current.interactionIndication,
            ).semantics { selected = isSelected }
            .padding(vertical = LemonadeTheme.spaces.spacing200),
    ) {
        val displayedIcon = if (isSelected) {
            item.selectedIcon
                ?: item.icon
        } else {
            item.icon
        }

        LemonadeUi.Icon(
            icon = displayedIcon,
            contentDescription = null,
            size = LemonadeAssetSize.Medium,
            tint = LemonadeTheme.colors.content.contentPrimary,
        )

        Spacer(modifier = Modifier.height(height = LemonadeTheme.spaces.spacing50))

        LemonadeUi.Text(
            text = item.label,
            textStyle = LemonadeTheme.typography.bodyXSmallMedium,
            color = LemonadeTheme.colors.content.contentPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
