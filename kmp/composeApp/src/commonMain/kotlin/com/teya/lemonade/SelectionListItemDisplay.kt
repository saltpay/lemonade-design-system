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
        SelectionListItemSection(title = "SelectListItem - Single") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
            ) {
                (0..2).forEach { index ->
                    LemonadeUi.SelectListItem(
                        label = "Option ${index + 1}",
                        type = SelectListItemType.Single,
                        checked = singleSelection == index,
                        onItemClicked = { singleSelection = index },
                        supportText = if (index == 0) "With support text" else null
                    )
                }
            }
        }

        // SelectListItem - Multiple
        SelectionListItemSection(title = "SelectListItem - Multiple") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
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
        }

        // SelectListItem with Leading Slot
        SelectionListItemSection(title = "SelectListItem with Leading") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
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
        }

        // Disabled States
        SelectionListItemSection(title = "Disabled States") {
            Column(
                modifier = Modifier
                    .background(
                        LemonadeTheme.colors.background.bgDefault,
                        shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                    )
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
}

@Composable
private fun SelectionListItemSection(
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
