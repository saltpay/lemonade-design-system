package com.teya.lemonade

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun BottomTabBarDisplay() {
    SampleScreenDisplayLazyColumn(
        title = "BottomTabBar",
    ) {
        defaultBottomTabBarSection()
        interactiveBottomTabBarSection()
        threeItemsBottomTabBarSection()
        twoItemsBottomTabBarSection()
        item {
            Spacer(modifier = Modifier.height(height = LemonadeTheme.spaces.spacing500))
        }
    }
}

private fun LazyListScope.defaultBottomTabBarSection() {
    item {
        LemonadeUi.Text(
            text = "Default (4 items)",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing400,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        LemonadeUi.BottomTabBar(
            items = listOf(
                BottomTabBarItem(
                    label = "Home",
                    icon = LemonadeIcons.BrandTeyaSymbol,
                ),
                BottomTabBarItem(
                    label = "Sales",
                    icon = LemonadeIcons.ChartStats,
                    selectedIcon = LemonadeIcons.ChartStatsSolid,
                ),
                BottomTabBarItem(
                    label = "Money",
                    icon = LemonadeIcons.Wallet,
                    selectedIcon = LemonadeIcons.WalletSolid,
                ),
                BottomTabBarItem(
                    label = "Teya AI",
                    icon = LemonadeIcons.SparklesSoft,
                    selectedIcon = LemonadeIcons.SparklesSoftSolid,
                ),
            ),
            selectedIndex = 0,
            onItemSelected = { /* display only */ },
        )
    }
}

private fun LazyListScope.interactiveBottomTabBarSection() {
    item {
        LemonadeUi.Text(
            text = "Interactive",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedIndex by remember { mutableStateOf(value = 1) }

        LemonadeUi.BottomTabBar(
            items = listOf(
                BottomTabBarItem(
                    label = "Home",
                    icon = LemonadeIcons.BrandTeyaSymbol,
                ),
                BottomTabBarItem(
                    label = "Sales",
                    icon = LemonadeIcons.ChartStats,
                    selectedIcon = LemonadeIcons.ChartStatsSolid,
                ),
                BottomTabBarItem(
                    label = "Money",
                    icon = LemonadeIcons.Wallet,
                    selectedIcon = LemonadeIcons.WalletSolid,
                ),
                BottomTabBarItem(
                    label = "Teya AI",
                    icon = LemonadeIcons.SparklesSoft,
                    selectedIcon = LemonadeIcons.SparklesSoftSolid,
                ),
            ),
            selectedIndex = selectedIndex,
            onItemSelected = { index -> selectedIndex = index },
        )
    }
}

private fun LazyListScope.threeItemsBottomTabBarSection() {
    item {
        LemonadeUi.Text(
            text = "Three items",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedIndex by remember { mutableStateOf(value = 0) }

        LemonadeUi.BottomTabBar(
            items = listOf(
                BottomTabBarItem(
                    label = "Home",
                    icon = LemonadeIcons.BrandTeyaSymbol,
                ),
                BottomTabBarItem(
                    label = "Sales",
                    icon = LemonadeIcons.ChartStats,
                    selectedIcon = LemonadeIcons.ChartStatsSolid,
                ),
                BottomTabBarItem(
                    label = "Money",
                    icon = LemonadeIcons.Wallet,
                    selectedIcon = LemonadeIcons.WalletSolid,
                ),
            ),
            selectedIndex = selectedIndex,
            onItemSelected = { index -> selectedIndex = index },
        )
    }
}

private fun LazyListScope.twoItemsBottomTabBarSection() {
    item {
        LemonadeUi.Text(
            text = "Two items",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedIndex by remember { mutableStateOf(value = 0) }

        LemonadeUi.BottomTabBar(
            items = listOf(
                BottomTabBarItem(
                    label = "Home",
                    icon = LemonadeIcons.BrandTeyaSymbol,
                ),
                BottomTabBarItem(
                    label = "Money",
                    icon = LemonadeIcons.Wallet,
                    selectedIcon = LemonadeIcons.WalletSolid,
                ),
            ),
            selectedIndex = selectedIndex,
            onItemSelected = { index -> selectedIndex = index },
        )
    }
}
