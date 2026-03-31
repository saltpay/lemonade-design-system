package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeSegmentedControlSize
import com.teya.lemonade.core.TabButtonProperties

@Suppress("LongMethod")
@Composable
internal fun SegmentedControlDisplay() {
    var selectedLarge by remember { mutableIntStateOf(0) }
    var selectedMedium by remember { mutableIntStateOf(0) }
    var selectedSmall by remember { mutableIntStateOf(0) }
    var selectedIcons by remember { mutableIntStateOf(1) }
    var selectedIconOnly by remember { mutableIntStateOf(0) }

    SampleScreenDisplayColumn("SegmentedControl") {
        SegmentedControlSection("Large (Default)") {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = LemonadeTheme.spaces.spacing200,
                ),
            ) {
                LemonadeUi.SegmentedControl(
                    onTabSelected = { index ->
                        selectedLarge = index
                    },
                    selectedTab = selectedLarge,
                    properties = listOf(
                        TabButtonProperties(label = "Tab 1"),
                        TabButtonProperties(label = "Tab 2"),
                        TabButtonProperties(label = "Tab 3"),
                    ),
                )
                LemonadeUi.Text(
                    text = "Selected: Tab ${selectedLarge + 1}",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }

        SegmentedControlSection("Medium") {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = LemonadeTheme.spaces.spacing200,
                ),
            ) {
                LemonadeUi.SegmentedControl(
                    onTabSelected = { index ->
                        selectedMedium = index
                    },
                    selectedTab = selectedMedium,
                    size = LemonadeSegmentedControlSize.Medium,
                    properties = listOf(
                        TabButtonProperties(label = "Tab 1"),
                        TabButtonProperties(label = "Tab 2"),
                        TabButtonProperties(label = "Tab 3"),
                    ),
                )
                LemonadeUi.Text(
                    text = "Selected: Tab ${selectedMedium + 1}",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }

        SegmentedControlSection("Small") {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = LemonadeTheme.spaces.spacing200,
                ),
            ) {
                LemonadeUi.SegmentedControl(
                    onTabSelected = { index ->
                        selectedSmall = index
                    },
                    selectedTab = selectedSmall,
                    size = LemonadeSegmentedControlSize.Small,
                    properties = listOf(
                        TabButtonProperties(label = "Tab 1"),
                        TabButtonProperties(label = "Tab 2"),
                        TabButtonProperties(label = "Tab 3"),
                    ),
                )
                LemonadeUi.Text(
                    text = "Selected: Tab ${selectedSmall + 1}",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }

        SegmentedControlSection("With Icons") {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = LemonadeTheme.spaces.spacing200,
                ),
            ) {
                LemonadeUi.SegmentedControl(
                    onTabSelected = { index ->
                        selectedIcons = index
                    },
                    selectedTab = selectedIcons,
                    properties = listOf(
                        TabButtonProperties(label = "Home", icon = LemonadeIcons.Home),
                        TabButtonProperties(label = "Profile", icon = LemonadeIcons.User),
                        TabButtonProperties(label = "Settings", icon = LemonadeIcons.Gear),
                    ),
                )
                LemonadeUi.Text(
                    text = "Selected: ${listOf("Home", "Profile", "Settings")[selectedIcons]}",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
            }
        }

        SegmentedControlSection("Icon Only (Small)") {
            LemonadeUi.SegmentedControl(
                onTabSelected = { index ->
                    selectedIconOnly = index
                },
                selectedTab = selectedIconOnly,
                size = LemonadeSegmentedControlSize.Small,
                properties = listOf(
                    TabButtonProperties(icon = LemonadeIcons.Heart),
                    TabButtonProperties(icon = LemonadeIcons.Star),
                    TabButtonProperties(icon = LemonadeIcons.Gear),
                ),
            )
        }
    }
}

@Composable
private fun SegmentedControlSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = LemonadeTheme.spaces.spacing300,
        ),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
