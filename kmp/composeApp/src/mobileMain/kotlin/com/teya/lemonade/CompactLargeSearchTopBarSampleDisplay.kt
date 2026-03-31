package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun CompactLargeSearchTopBarSampleDisplay() {
    val topBarState = rememberTopBarState()
    var searchInput by remember { mutableStateOf("") }

    val items = remember {
        listOf(
            "Apple", "Banana", "Cherry", "Date", "Elderberry",
            "Fig", "Grape", "Honeydew", "Kiwi", "Lemon",
            "Mango", "Nectarine", "Orange", "Papaya", "Quince",
        )
    }

    val filteredItems = remember(searchInput) {
        if (searchInput.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.contains(
                    other = searchInput,
                    ignoreCase = true,
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LemonadeTheme.colors.background.bgSubtle)
            .navigationBarsPadding(),
    ) {
        LemonadeUi.TopBar(
            label = "Discover",
            subheading = "Find your favorite fruit",
            searchInput = searchInput,
            onSearchChanged = { value ->
                searchInput = value
            },
            state = topBarState,
            trailingSlot = {
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.EllipsisVertical,
                    contentDescription = "More",
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Ghost,
                )
            },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
                .nestedScroll(connection = topBarState.nestedScrollConnection)
                .background(color = LemonadeTheme.colors.background.bgSubtle),
        ) {
            items(items = filteredItems) { item ->
                LemonadeUi.Text(
                    text = item,
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LemonadeTheme.spaces.spacing300,
                            vertical = LemonadeTheme.spaces.spacing200,
                        ),
                )
            }
        }
    }
}
