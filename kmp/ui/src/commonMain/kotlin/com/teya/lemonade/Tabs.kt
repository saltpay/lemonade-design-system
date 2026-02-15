package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A horizontal scrollable row of tab items where one is selected at a time.
 * The selected tab has a brand-colored bottom indicator line.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.Tabs(
 *     tabs = listOf("Overview", "Details", "Reviews"),
 *     selectedIndex = 0,
 *     onTabSelected = { index -> /* ... */ },
 * )
 * ```
 *
 * @param tabs - A list of tab labels to display.
 * @param selectedIndex - The index of the currently selected tab.
 * @param onTabSelected - A callback invoked when a tab is selected with the tab index.
 * @param modifier - The [Modifier] to be applied to the root container of the component.
 */
@Composable
public fun LemonadeUi.Tabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CoreTabs(
        tabs = tabs,
        selectedIndex = selectedIndex,
        onTabSelected = onTabSelected,
        modifier = modifier,
    )
}

@Composable
internal fun CoreTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(
        value = tabs.isNotEmpty(),
        lazyMessage = { "Tabs list should not be empty." },
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(state = scrollState),
        ) {
            tabs.forEachIndexed { index, label ->
                TabItem(
                    label = label,
                    isSelected = index == selectedIndex,
                    onClick = { onTabSelected(index) },
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = LocalBorderWidths.current.base.border25)
                .background(color = LocalColors.current.border.borderNeutralLow),
        )
    }
}

@Composable
private fun TabItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor = if (isSelected) {
        LocalColors.current.content.contentBrand
    } else {
        LocalColors.current.content.contentSecondary
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .padding(
                horizontal = LocalSpaces.current.spacing300,
                vertical = LocalSpaces.current.spacing200,
            ),
    ) {
        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.bodySmallMedium,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 2.dp)
                    .background(color = LocalColors.current.content.contentBrand),
            )
        }
    }
}

@LemonadePreview
@Composable
private fun TabsPreview() {
    LemonadeUi.Tabs(
        tabs = listOf("Overview", "Details", "Reviews"),
        selectedIndex = 1,
        onTabSelected = { /* preview only */ },
    )
}

@LemonadePreview
@Composable
private fun TabsManyItemsPreview() {
    LemonadeUi.Tabs(
        tabs = listOf(
            "Dashboard",
            "Analytics",
            "Reports",
            "Settings",
            "Users",
            "Activity",
        ),
        selectedIndex = 2,
        onTabSelected = { /* preview only */ },
    )
}
