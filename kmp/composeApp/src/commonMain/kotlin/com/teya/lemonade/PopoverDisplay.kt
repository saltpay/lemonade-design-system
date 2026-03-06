package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
internal fun PopoverDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        BasicPopoverSection()
        WithActionsPopoverSection()
        InteractivePopoverSection()
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun BasicPopoverSection() {
    PopoverSection(title = "Basic") {
        Column(
            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
        ) {
            LemonadeUi.Popover(
                title = "Feature highlight",
                description = "This feature helps you manage your account settings easily.",
            )
            LemonadeUi.Popover(
                title = "Quick tip",
                description = "Swipe left to reveal more options.",
            )
        }
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun WithActionsPopoverSection() {
    PopoverSection(title = "With Actions") {
        Column(
            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
        ) {
            LemonadeUi.Popover(
                title = "New update available",
                description = "Version 2.0 includes performance improvements and bug fixes.",
                primaryActionLabel = "Update now",
                onPrimaryAction = {},
                secondaryActionLabel = "Later",
                onSecondaryAction = {},
            )
            LemonadeUi.Popover(
                title = "Enable notifications",
                description = "Stay updated with the latest changes to your account.",
                primaryActionLabel = "Enable",
                onPrimaryAction = {},
            )
        }
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun InteractivePopoverSection() {
    PopoverSection(title = "Interactive (Tap to Toggle)") {
        Column(
            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
        ) {
            var showPopover by remember { mutableStateOf(value = false) }
            LemonadeUi.PopoverBox(
                title = "Account info",
                description = "Tap here to view your account details and manage your subscription.",
                isVisible = showPopover,
                primaryActionLabel = "View details",
                onPrimaryAction = { showPopover = false },
                secondaryActionLabel = "Dismiss",
                onSecondaryAction = { showPopover = false },
            ) {
                LemonadeUi.Button(
                    label = "Show popover",
                    onClick = { showPopover = !showPopover },
                    variant = LemonadeButtonVariant.Primary,
                    size = LemonadeButtonSize.Medium,
                )
            }
        }
    }
}

@Composable
private fun PopoverSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
