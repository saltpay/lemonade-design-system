package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.TagVoice

@Composable
internal fun ResourceListItemDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .background(LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400)
    ) {
        // ResourceListItem
        ResourceListItemSection(title = "ResourceListItem") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
            ) {
                LemonadeUi.ResourceListItem(
                    label = "Account Balance",
                    value = "$1,234.56",
                    supportText = "Updated today",
                    leadingSlot = {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Money,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Info,
                            size = SymbolContainerSize.Large
                        )
                    }
                )

                LemonadeUi.ResourceListItem(
                    label = "Savings",
                    value = "$5,000.00",
                    leadingSlot = {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Coins,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Positive,
                            size = SymbolContainerSize.Large
                        )
                    }
                )
            }
        }

        // ResourceListItem with Addon
        ResourceListItemSection(title = "ResourceListItem with Addon") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
            ) {
                LemonadeUi.ResourceListItem(
                    label = "Last Transaction",
                    value = "-$50.00",
                    supportText = "Yesterday",
                    addonSlot = {
                        LemonadeUi.Tag(label = "Pending", voice = TagVoice.Warning)
                    },
                    leadingSlot = {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.ArrowUpRight,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Critical,
                            size = SymbolContainerSize.Large
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ResourceListItemSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
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
