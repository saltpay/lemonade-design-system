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
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeContentListItemLayout
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.TagVoice

@Suppress("LongMethod")
@Composable
internal fun ContentListItemDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .background(LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        // Horizontal simple (stacked with dividers)
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Horizontal — Simple"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Account holder",
                value = "John Doe",
                layout = LemonadeContentListItemLayout.Horizontal,
                showDivider = true,
            )
            LemonadeUi.ContentListItem(
                label = "Account number",
                value = "123-456-789",
                layout = LemonadeContentListItemLayout.Horizontal,
            )
        }

        // Horizontal with leading SymbolContainer + trailing icon
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Horizontal — Leading & Trailing"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Favourite",
                value = "Enabled",
                layout = LemonadeContentListItemLayout.Horizontal,
                leadingSlot = {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        voice = SymbolContainerVoice.Neutral,
                        size = SymbolContainerSize.Medium,
                        contentDescription = null,
                    )
                },
                trailingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.PencilLine,
                        tint = LemonadeTheme.colors.content.contentBrand,
                        size = LemonadeAssetSize.Medium,
                        contentDescription = "Edit",
                    )
                },
            )
        }

        // Horizontal with content slot
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Horizontal — Content Slot"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Status",
                value = "Active",
                layout = LemonadeContentListItemLayout.Horizontal,
                contentSlot = {
                    LemonadeUi.Tag(
                        label = "Available",
                        voice = TagVoice.Positive,
                    )
                },
            )
        }

        // Vertical small (no content slot)
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Vertical Small — Simple"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Balance",
                value = "$1,234.56",
                layout = LemonadeContentListItemLayout.Vertical,
            )
        }

        // Vertical small with leading + trailing
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Vertical Small — Leading & Trailing"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Savings",
                value = "$5,678.90",
                layout = LemonadeContentListItemLayout.Vertical,
                leadingSlot = {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        voice = SymbolContainerVoice.Neutral,
                        size = SymbolContainerSize.Medium,
                        contentDescription = null,
                    )
                },
                trailingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.PencilLine,
                        tint = LemonadeTheme.colors.content.contentBrand,
                        size = LemonadeAssetSize.Medium,
                        contentDescription = "Edit",
                    )
                },
            )
        }

        // Vertical large (with content slot)
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Vertical Large — Content Slot"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Total balance",
                value = "$12,345.67",
                layout = LemonadeContentListItemLayout.Vertical,
                contentSlot = {
                    LemonadeUi.Tag(
                        label = "Available",
                        voice = TagVoice.Positive,
                    )
                },
            )
        }

        // Vertical large with leading + trailing + content slot
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Vertical Large — Full"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Investment portfolio",
                value = "$98,765.43",
                layout = LemonadeContentListItemLayout.Vertical,
                leadingSlot = {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        voice = SymbolContainerVoice.Neutral,
                        size = SymbolContainerSize.Medium,
                        contentDescription = null,
                    )
                },
                trailingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.PencilLine,
                        tint = LemonadeTheme.colors.content.contentBrand,
                        size = LemonadeAssetSize.Medium,
                        contentDescription = "Edit",
                    )
                },
                contentSlot = {
                    LemonadeUi.Tag(
                        label = "Available",
                        voice = TagVoice.Positive,
                    )
                },
            )
        }

        // Mixed list with dividers
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Mixed List with Dividers"),
        ) {
            LemonadeUi.ContentListItem(
                label = "Label",
                value = "Value",
                layout = LemonadeContentListItemLayout.Horizontal,
                showDivider = true,
            )
            LemonadeUi.ContentListItem(
                label = "Label",
                value = "Value",
                layout = LemonadeContentListItemLayout.Vertical,
                showDivider = true,
            )
            LemonadeUi.ContentListItem(
                label = "Label",
                value = "Value",
                layout = LemonadeContentListItemLayout.Vertical,
                contentSlot = {
                    LemonadeUi.Tag(
                        label = "Available",
                        voice = TagVoice.Positive,
                    )
                },
            )
        }
    }
}
