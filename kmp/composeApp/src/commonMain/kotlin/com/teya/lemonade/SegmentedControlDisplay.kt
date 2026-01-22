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
import com.teya.lemonade.core.TabButtonProperties

@Composable
internal fun SegmentedControlDisplay() {
    var selectedTab1 by remember { mutableIntStateOf(0) }
    var selectedTab2 by remember { mutableIntStateOf(1) }

    SampleScreenDisplayColumn("SegmentedControl") {
        SegmentedControlSection("Basic") {
            LemonadeUi.SegmentedControl(
                onTabSelected = { },
                selectedTab = 0,
                properties = listOf(
                    TabButtonProperties(label = "Tab 1"),
                    TabButtonProperties(label = "Tab 2"),
                )
            )
        }

        SegmentedControlSection("With Icons") {
            LemonadeUi.SegmentedControl(
                onTabSelected = { },
                selectedTab = 1,
                properties = listOf(
                    TabButtonProperties(label = "Tab 1", icon = LemonadeIcons.Heart),
                    TabButtonProperties(label = "Tab 2", icon = LemonadeIcons.Laptop),
                    TabButtonProperties(label = "Tab 3", icon = LemonadeIcons.User),
                )
            )
        }

        SegmentedControlSection("Interactive") {
            Column(verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)) {
                LemonadeUi.SegmentedControl(
                    onTabSelected = { selectedTab1 = it },
                    selectedTab = selectedTab1,
                    properties = listOf(
                        TabButtonProperties(label = "First"),
                        TabButtonProperties(label = "Second"),
                        TabButtonProperties(label = "Third"),
                    )
                )
                LemonadeUi.Text(
                    text = "Selected tab index: $selectedTab1",
                    textStyle = LemonadeTheme.typography.bodySmallRegular
                )
            }
        }

        SegmentedControlSection("With Icons Interactive") {
            Column(verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)) {
                LemonadeUi.SegmentedControl(
                    onTabSelected = { selectedTab2 = it },
                    selectedTab = selectedTab2,
                    properties = listOf(
                        TabButtonProperties(label = "Home", icon = LemonadeIcons.Home),
                        TabButtonProperties(label = "Profile", icon = LemonadeIcons.User),
                        TabButtonProperties(label = "Settings", icon = LemonadeIcons.Gear),
                    )
                )
                LemonadeUi.Text(
                    text = "Selected tab index: $selectedTab2",
                    textStyle = LemonadeTheme.typography.bodySmallRegular
                )
            }
        }
    }
}

@Composable
private fun SegmentedControlSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary
        )
        content()
    }
}
