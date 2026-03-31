package com.teya.lemonade

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.LemonadeSegmentedControlSize
import com.teya.lemonade.core.LemonadeTextStyle
import com.teya.lemonade.core.TabButtonProperties

/**
 * A horizontal control used to select a single option from a set of two or more segments.
 * Ideal for toggling between views or filtering content.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.SegmentedControl(
 *     onTabSelected = { tabIndex -> /* ... */ },
 *     selectedTab = selectedTabIndex,
 *     properties = listOf(
 *         TabButtonProperties(label = "Tab 1", icon = LemonadeIcons.Heart),
 *         TabButtonProperties(label = "Tab 2", icon = LemonadeIcons.Laptop),
 *         TabButtonProperties(label = "Tab 3"),
 *     ),
 * )
 * ```
 *
 * @param properties A list of [TabButtonProperties] that represent the tab buttons' information.
 * @param selectedTab [Int] that indicates what is the index of the selected tab.
 * @param onTabSelected A callback invoked when a tab is selected.
 * @param size The size of the segmented control. Defaults to [LemonadeSegmentedControlSize.Large].
 * @param modifier The [Modifier] to be applied to the root container of the component.
 */
@Composable
public fun LemonadeUi.SegmentedControl(
    properties: List<TabButtonProperties>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    size: LemonadeSegmentedControlSize = LemonadeSegmentedControlSize.Large,
    modifier: Modifier = Modifier,
) {
    CoreSegmentedControl(
        selectedTab = { tabIndex ->
            tabIndex == selectedTab
        },
        onTabSelected = onTabSelected,
        modifier = modifier,
        tabCount = { properties.size },
        size = size,
        content = { index ->
            val property = properties[index]
            val contentColor = if (selectedTab == index) {
                LocalColors.current.content.contentPrimary
            } else {
                LocalColors.current.content.contentSecondary
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = size.buttonContentGap(),
                ),
            ) {
                property.icon?.let { icon ->
                    LemonadeUi.Icon(
                        icon = icon,
                        contentDescription = property.label,
                        size = LemonadeAssetSize.Small,
                        tint = contentColor,
                    )
                }

                property.label?.let { label ->
                    LemonadeUi.Text(
                        text = label,
                        textStyle = size.textStyle(),
                        overflow = TextOverflow.Ellipsis,
                        color = contentColor,
                    )
                }
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
    size: LemonadeSegmentedControlSize = LemonadeSegmentedControlSize.Large,
    modifier: Modifier = Modifier,
) {
    require(
        value = tabCount() > 0,
        lazyMessage = { "Tab count should be greater than zero." },
    )

    val density = LocalDensity.current
    val pillShape = RoundedCornerShape(percent = 50)
    val tabWidths = remember { mutableStateMapOf<Int, Dp>() }
    val tabOffsets = remember { mutableStateMapOf<Int, Dp>() }

    val selectedIndex = (0 until tabCount()).firstOrNull { index ->
        selectedTab(index)
    }
        ?: 0

    val hasMeasurements = tabWidths.containsKey(selectedIndex) && tabOffsets.containsKey(selectedIndex)
    val indicatorWidth by animateDpAsState(
        targetValue = tabWidths[selectedIndex]
            ?: 0.dp,
        animationSpec = tween(durationMillis = 250),
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabOffsets[selectedIndex]
            ?: 0.dp,
        animationSpec = tween(durationMillis = 250),
    )

    Box(
        modifier = modifier
            .height(height = size.containerHeight())
            .background(
                color = LocalColors.current.background.bgElevated,
                shape = pillShape,
            )
            .clip(shape = pillShape)
            .padding(all = size.containerPadding()),
    ) {
        // Sliding indicator
        if (hasMeasurements) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = with(density) { indicatorOffset.roundToPx() },
                            y = 0,
                        )
                    }
                    .width(width = indicatorWidth)
                    .fillMaxHeight()
                    .animateLemonadeShadow(
                        shape = pillShape,
                        shadow = LemonadeShadow.Xsmall,
                    )
                    .background(
                        color = LocalColors.current.background.bgDefault,
                        shape = pillShape,
                    ),
            )
        }

        // Tab buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = LocalSpaces.current.spacing0,
            ),
        ) {
            repeat(times = tabCount()) { tabIndex ->
                val tabInteractionSource = remember { MutableInteractionSource() }
                val isHovering by tabInteractionSource.collectIsHoveredAsState()
                val isPressed by tabInteractionSource.collectIsPressedAsState()
                val animatedBackgroundColor by animateColorAsState(
                    targetValue = when {
                        selectedTab(tabIndex) -> LocalColors.current.background.bgDefault.copy(
                            alpha = LocalOpacities.current.base.opacity0,
                        )
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
                        .weight(weight = 1f)
                        .fillMaxHeight()
                        .onGloballyPositioned { coordinates ->
                            with(density) {
                                val newWidth = coordinates.size.width.toDp()
                                val newOffset = coordinates.positionInParent().x.toDp()
                                if (tabWidths[tabIndex] != newWidth) {
                                    tabWidths[tabIndex] = newWidth
                                }
                                if (tabOffsets[tabIndex] != newOffset) {
                                    tabOffsets[tabIndex] = newOffset
                                }
                            }
                        }
                        .clip(shape = pillShape)
                        .clickable(
                            onClick = { onTabSelected(tabIndex) },
                            role = Role.Tab,
                            interactionSource = tabInteractionSource,
                            indication = LocalEffects.current.interactionIndication,
                        )
                        .background(color = animatedBackgroundColor)
                        .padding(
                            horizontal = size.buttonHorizontalPadding(),
                        ),
                )
            }
        }
    }
}

@Composable
private fun LemonadeSegmentedControlSize.containerHeight(): Dp {
    val sizes = LocalSizes.current
    return when (this) {
        LemonadeSegmentedControlSize.Small -> sizes.size800
        LemonadeSegmentedControlSize.Medium -> sizes.size1000
        LemonadeSegmentedControlSize.Large -> sizes.size1200
    }
}

@Composable
private fun LemonadeSegmentedControlSize.containerPadding(): Dp {
    val spaces = LocalSpaces.current
    return when (this) {
        LemonadeSegmentedControlSize.Small -> spaces.spacing50
        LemonadeSegmentedControlSize.Medium,
        LemonadeSegmentedControlSize.Large,
        -> spaces.spacing100
    }
}

@Composable
private fun LemonadeSegmentedControlSize.buttonContentGap(): Dp {
    val spaces = LocalSpaces.current
    return when (this) {
        LemonadeSegmentedControlSize.Small -> spaces.spacing50
        LemonadeSegmentedControlSize.Medium,
        LemonadeSegmentedControlSize.Large,
        -> spaces.spacing100
    }
}

@Composable
private fun LemonadeSegmentedControlSize.buttonHorizontalPadding(): Dp {
    val spaces = LocalSpaces.current
    return when (this) {
        LemonadeSegmentedControlSize.Small -> spaces.spacing200
        LemonadeSegmentedControlSize.Medium,
        LemonadeSegmentedControlSize.Large,
        -> spaces.spacing300
    }
}

@Composable
private fun LemonadeSegmentedControlSize.textStyle(): LemonadeTextStyle {
    val typography = LocalTypographies.current
    return when (this) {
        LemonadeSegmentedControlSize.Small -> typography.bodyXSmallMedium
        LemonadeSegmentedControlSize.Medium,
        LemonadeSegmentedControlSize.Large,
        -> typography.bodySmallMedium
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
