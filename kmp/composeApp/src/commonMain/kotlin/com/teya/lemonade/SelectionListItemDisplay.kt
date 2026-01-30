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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SelectListItemType

@Composable
internal fun SelectionListItemDisplay() {
    var singleSelection by remember { mutableIntStateOf(0) }
    var multipleSelections by remember { mutableStateOf(setOf(0)) }

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
        // SelectListItem - Single

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "SelectListItem - Single")
        ) {
            (0..2).forEach { index ->
                LemonadeUi.SelectListItem(
                    label = "Option ${index + 1}",
                    type = SelectListItemType.Single,
                    checked = singleSelection == index,
                    onItemClicked = { singleSelection = index },
                    supportText = if (index == 0) "With support text" else null,
                    divider = true
                )
            }
        }


        // SelectListItem - Multiple
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "SelectListItem - Multiple")
        ) {

            (0..2).forEach { index ->
                LemonadeUi.SelectListItem(
                    label = "Item ${index + 1}",
                    type = SelectListItemType.Multiple,
                    checked = multipleSelections.contains(index),
                    onItemClicked = {
                        multipleSelections = if (multipleSelections.contains(index)) {
                            multipleSelections - index
                        } else {
                            multipleSelections + index
                        }
                    }
                )
            }
        }
// SelectListItem - Multiple
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "SelectListItem with Leading")
        ) {
            LemonadeUi.SelectListItem(
                label = "With Icon",
                type = SelectListItemType.Single,
                checked = true,
                onItemClicked = {},
                supportText = "Leading icon example",
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Star,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                }
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Disabled States")
        ) {

            LemonadeUi.SelectListItem(
                label = "Disabled Option",
                type = SelectListItemType.Single,
                checked = false,
                onItemClicked = {},
                enabled = false
            )
        }
    }
}
