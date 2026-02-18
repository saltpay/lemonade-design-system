package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.TagVoice

@Suppress("LongMethod")
@Composable
internal fun ResourceListItemDisplay() {
    Column(
        modifier = Modifier
            .background(LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .padding(horizontal = LemonadeTheme.spaces.spacing400)
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(
            LemonadeTheme.spaces.spacing400,
        ),
    ) {
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ResourceListItem"),
        ) {
            LemonadeUi.ResourceListItem(
                label = "Account Balance",
                value = "$1,234.56",
                supportText = "Updated today",
                showDivider = true,
                onItemClicked = {},
                leadingSlot = {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Money,
                        contentDescription = null,
                        voice = SymbolContainerVoice.Info,
                        size = SymbolContainerSize.Large,
                    )
                },
            )

            LemonadeUi.ResourceListItem(
                label = "Savings",
                value = "$5,000.00",
                onItemClicked = {},
                showDivider = true,
                leadingSlot = {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Coins,
                        contentDescription = null,
                        voice = SymbolContainerVoice.Positive,
                        size = SymbolContainerSize.Large,
                    )
                },
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "ResourceListItem with Addon"),
        ) {
            LemonadeUi.ResourceListItem(
                label = "Last Transaction",
                value = "-$50.00",
                onItemClicked = {},
                showDivider = false,
                supportText = "Yesterday",
                addonSlot = {
                    LemonadeUi.Tag(label = "Pending", voice = TagVoice.Warning)
                },
                leadingSlot = {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.ArrowUpRight,
                        contentDescription = null,
                        voice = SymbolContainerVoice.Critical,
                        size = SymbolContainerSize.Large,
                    )
                },
            )
        }
    }
}
