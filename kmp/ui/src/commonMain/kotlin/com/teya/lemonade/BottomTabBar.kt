package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow

/**
 * Represents a single item in a [LemonadeUi.BottomTabBar].
 *
 * @param label The text label displayed below the icon.
 * @param icon The [LemonadeIcons] displayed above the label.
 */
public data class BottomTabBarItem(
    val label: String,
    val icon: LemonadeIcons,
)

/**
 * A floating, pill-shaped bottom navigation bar for top-level destinations.
 *
 * Items are distributed equally across the bar. The selected item is highlighted with an
 * elevated background pill. The bar applies a soft scroll-edge gradient behind itself so it
 * floats over scrollable content, and reserves space for the system navigation bar.
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
private val ScrollEdgeHeight: Dp = 132.dp

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

    val edgeColor = LocalColors.current.background.bgSubtle

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = ScrollEdgeHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            edgeColor.copy(alpha = 0f),
                            edgeColor,
                        ),
                    ),
                ),
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = LocalSpaces.current.spacing400,
                    end = LocalSpaces.current.spacing400,
                    top = LocalSpaces.current.spacing400,
                    bottom = LocalSpaces.current.spacing800,
                ),
        ) {
            BottomTabBarRow(
                items = items,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected,
            )
        }
    }
}

@Composable
private fun BottomTabBarRow(
    items: List<BottomTabBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .widthIn(max = MaxBarWidth)
            .lemonadeShadow(
                shadow = LemonadeShadow.Xlarge,
                shape = LocalShapes.current.radiusFull,
            ).clip(shape = LocalShapes.current.radiusFull)
            .background(color = LocalColors.current.background.bgDefault)
            .padding(all = LocalSpaces.current.spacing100),
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

@Composable
private fun BottomTabBarItemContent(
    item: BottomTabBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) {
        LocalColors.current.background.bgElevated
    } else {
        Color.Transparent
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = LocalSpaces.current.spacing50,
            alignment = Alignment.CenterVertically,
        ),
        modifier = modifier
            .height(height = ItemHeight)
            .clip(shape = LocalShapes.current.radiusFull)
            .background(color = backgroundColor)
            .clickable(
                onClick = onClick,
                role = Role.Tab,
            ).padding(vertical = LocalSpaces.current.spacing200),
    ) {
        LemonadeUi.Icon(
            icon = item.icon,
            contentDescription = null,
            size = LemonadeAssetSize.Medium,
            tint = LocalColors.current.content.contentPrimary,
        )

        LemonadeUi.Text(
            text = item.label,
            textStyle = LocalTypographies.current.bodyXSmallMedium,
            color = LocalColors.current.content.contentPrimary,
        )
    }
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun BottomTabBarPreview() {
    LemonadeUi.BottomTabBar(
        items = listOf(
            BottomTabBarItem(label = "Home", icon = LemonadeIcons.BrandTeyaSymbol),
            BottomTabBarItem(label = "Sales", icon = LemonadeIcons.ChartStats),
            BottomTabBarItem(label = "Money", icon = LemonadeIcons.Wallet),
            BottomTabBarItem(label = "Teya AI", icon = LemonadeIcons.SparklesSoft),
        ),
        selectedIndex = 0,
        onItemSelected = { /* preview only */ },
    )
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun BottomTabBarThreeItemsPreview() {
    LemonadeUi.BottomTabBar(
        items = listOf(
            BottomTabBarItem(label = "Home", icon = LemonadeIcons.BrandTeyaSymbol),
            BottomTabBarItem(label = "Sales", icon = LemonadeIcons.ChartStats),
            BottomTabBarItem(label = "Money", icon = LemonadeIcons.Wallet),
        ),
        selectedIndex = 1,
        onItemSelected = { /* preview only */ },
    )
}
