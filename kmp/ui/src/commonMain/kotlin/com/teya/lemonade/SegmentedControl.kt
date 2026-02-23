package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.TabButtonProperties

/**
 * A horizontal control used to select a single option from a set of two or more segments.
 *  Ideal for toggling between views or filtering content.
 *
 *  ## Usage
 *  ```kotlin
 *  LemonadeUi.SegmentedControl(
 *      onTabSelected = { tabIndex -> /* ... */ },
 *      selectedTab = selectedTabIndex,
 *      properties = listOf(
 *          TabButtonProperties(label = "Tab 1", icon = LemonadeIcons.Heart),
 *          TabButtonProperties(label = "Tab 2", icon = LemonadeIcons.Laptop),
 *          TabButtonProperties(label = "Tab 3"),
 *      ),
 *  )
 *  ```
 *
 * @param properties - A list of [TabButtonProperties] that represent the tab buttons' information.
 * @param selectedTab - [Int] that indicates what is the index of the selected tab.
 * @param onTabSelected - A callback invoked when a tab is selected.
 * @param modifier - The [Modifier] to be applied to the root container of the component.
 */
@Composable
public fun LemonadeUi.SegmentedControl(
    properties: List<TabButtonProperties>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CoreSegmentedControl(
        selectedTab = { tabIndex -> tabIndex == selectedTab },
        onTabSelected = onTabSelected,
        modifier = modifier,
        tabCount = { properties.size },
        content = { index ->
            val property = properties[index]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
            ) {
                property.icon?.let { icon ->
                    LemonadeUi.Icon(
                        icon = icon,
                        contentDescription = property.label,
                        size = LemonadeAssetSize.Small,
                    )
                }

                LemonadeUi.Text(
                    text = property.label,
                    textStyle = LocalTypographies.current.bodySmallMedium,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
    )
}

@Suppress("LongMethod")
@Composable
internal fun CoreSegmentedControl(
    content: @Composable BoxScope.(Int) -> Unit,
    tabCount: () -> Int,
    selectedTab: (Int) -> Boolean,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(
        value = tabCount() > 0,
        lazyMessage = { "Tab count should be greater than zero." },
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing0),
        modifier = modifier
            .background(
                color = LocalColors.current.background.bgElevated,
                shape = LocalShapes.current.radius200,
            ).padding(all = LocalSpaces.current.spacing50),
    ) {
        repeat(times = tabCount()) { tabIndex ->
            val tabInteractionSource = remember { MutableInteractionSource() }
            val isHovering by tabInteractionSource.collectIsHoveredAsState()
            val isPressed by tabInteractionSource.collectIsPressedAsState()
            val animatedBackgroundColor by animateColorAsState(
                targetValue = when {
                    selectedTab(tabIndex) -> LocalColors.current.background.bgDefault
                    isPressed -> LocalColors.current.interaction.bgDefaultPressed
                    isHovering -> LocalColors.current.interaction.bgDefaultInteractive
                    else -> LocalColors.current.background.bgDefault.copy(
                        alpha = LocalOpacities.current.base.opacity0,
                    )
                },
            )

            Box(
                content = { content(tabIndex) },
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .animateLemonadeShadow(
                        shape = LocalShapes.current.radius200,
                        shadow = if (selectedTab(tabIndex)) {
                            LemonadeShadow.Xsmall
                        } else {
                            LemonadeShadow.None
                        },
                    ).weight(weight = 1f)
                    .clip(shape = LocalShapes.current.radius200)
                    .clickable(
                        onClick = { onTabSelected(tabIndex) },
                        role = Role.Tab,
                        interactionSource = tabInteractionSource,
                        indication = null,
                    ).background(color = animatedBackgroundColor)
                    .defaultMinSize(
                        minHeight = LocalSizes.current.size800,
                        minWidth = LocalSizes.current.size1600,
                    ).padding(
                        vertical = LocalSpaces.current.spacing100,
                        horizontal = LocalSpaces.current.spacing200,
                    ),
            )
        }
    }
}

@LemonadePreview
@Composable
private fun SegmentedControlPreview() {
    LemonadeUi.SegmentedControl(
        onTabSelected = { /* preview only */ },
        selectedTab = 1,
        properties = listOf(
            TabButtonProperties(label = "Tab 1", icon = LemonadeIcons.Heart),
            TabButtonProperties(label = "Tab 2", icon = LemonadeIcons.Laptop),
            TabButtonProperties(label = "Tab 3"),
        ),
    )
}
