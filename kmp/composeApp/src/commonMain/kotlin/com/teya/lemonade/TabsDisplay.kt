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
internal fun TabsDisplay() {
    SampleScreenDisplayLazyColumn(
        title = "Tabs",
    ) {
        basicTabsSection()
        tabsWithIconsSection()
        stretchTabsSection()
        disabledTabsSection()
        manyTabsSection()
        interactiveTabsSection()
        twoTabsSection()
        item {
            Spacer(modifier = Modifier.height(height = LemonadeTheme.spaces.spacing500))
        }
    }
}

private fun LazyListScope.basicTabsSection() {
    item {
        LemonadeUi.Text(
            text = "Basic Tabs",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing400,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        LemonadeUi.Tabs(
            tabs = listOf(
                TabItem(label = "Overview"),
                TabItem(label = "Details"),
                TabItem(label = "Reviews"),
            ),
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
        )
    }
}

private fun LazyListScope.tabsWithIconsSection() {
    item {
        LemonadeUi.Text(
            text = "Tabs with Icons",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        LemonadeUi.Tabs(
            tabs = listOf(
                TabItem(label = "Home", icon = LemonadeIcons.Home),
                TabItem(label = "Analytics", icon = LemonadeIcons.Chart),
                TabItem(label = "Settings", icon = LemonadeIcons.Gear),
            ),
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
        )
    }
}

private fun LazyListScope.stretchTabsSection() {
    item {
        LemonadeUi.Text(
            text = "Stretch Mode",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        LemonadeUi.Tabs(
            tabs = listOf(
                TabItem(label = "Tab A"),
                TabItem(label = "Tab B"),
                TabItem(label = "Tab C"),
            ),
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
            itemsSize = TabsItemSize.Stretch,
        )
    }
}

private fun LazyListScope.disabledTabsSection() {
    item {
        LemonadeUi.Text(
            text = "Disabled Tab",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        LemonadeUi.Tabs(
            tabs = listOf(
                TabItem(label = "Active"),
                TabItem(label = "Also Active"),
                TabItem(label = "Disabled", isDisabled = true),
            ),
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
        )
    }
}

private fun LazyListScope.manyTabsSection() {
    item {
        LemonadeUi.Text(
            text = "Many Tabs (Scrollable)",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        LemonadeUi.Tabs(
            tabs = listOf(
                TabItem(label = "Dashboard"),
                TabItem(label = "Analytics"),
                TabItem(label = "Reports"),
                TabItem(label = "Settings"),
                TabItem(label = "Users"),
                TabItem(label = "Activity"),
                TabItem(label = "Notifications"),
            ),
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
        )
    }
}

private fun LazyListScope.interactiveTabsSection() {
    item {
        LemonadeUi.Text(
            text = "Interactive with Content",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        val tabs = listOf(
            TabItem(label = "Account"),
            TabItem(label = "Privacy"),
            TabItem(label = "Notifications"),
        )
        val content = listOf(
            "Manage your account settings and preferences.",
            "Control your privacy settings and data.",
            "Configure notification preferences and alerts.",
        )

        LemonadeUi.Tabs(
            tabs = tabs,
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
        )

        Spacer(modifier = Modifier.height(height = LemonadeTheme.spaces.spacing400))

        LemonadeUi.Card {
            LemonadeUi.Text(
                text = content[selectedTab],
                textStyle = LemonadeTheme.typography.bodyMediumRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
                modifier = Modifier.padding(all = LemonadeTheme.spaces.spacing400),
            )
        }
    }
}

private fun LazyListScope.twoTabsSection() {
    item {
        LemonadeUi.Text(
            text = "Two Tabs",
            textStyle = LemonadeTheme.typography.headingXSmall,
            modifier = Modifier.padding(
                top = LemonadeTheme.spaces.spacing500,
                bottom = LemonadeTheme.spaces.spacing200,
            ),
        )
    }
    item {
        var selectedTab by remember { mutableStateOf(value = 0) }

        LemonadeUi.Tabs(
            tabs = listOf(
                TabItem(label = "Login"),
                TabItem(label = "Sign Up"),
            ),
            selectedIndex = selectedTab,
            onTabSelected = { index -> selectedTab = index },
        )
    }
}
