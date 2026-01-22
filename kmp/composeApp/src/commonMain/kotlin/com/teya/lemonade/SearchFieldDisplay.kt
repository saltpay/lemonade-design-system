package com.teya.lemonade

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun SearchFieldDisplay() {
    val focusManager = LocalFocusManager.current
    var searchText1 by remember { mutableStateOf("") }
    var searchText2 by remember { mutableStateOf("Sample search") }
    var searchText3 by remember { mutableStateOf("") }

    val productList = listOf("iPhone 15", "MacBook Pro", "iPad Air", "Apple Watch")

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        // Basic
        SearchFieldSection(title = "Basic") {
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.SearchField(
                input = searchText1,
                onInputChanged = { searchText1 = it },
                placeholder = "Search..."
            )
        }

        // With Content
        SearchFieldSection(title = "With Content") {
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.SearchField(
                input = searchText2,
                onInputChanged = { searchText2 = it },
                placeholder = "Search..."
            )
        }

        // With Callbacks
        SearchFieldSection(title = "With Callbacks") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
            ) {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.SearchField(
                    input = searchText3,
                    onInputChanged = { newValue ->
                        searchText3 = newValue
                        println("Search changed: $newValue")
                    },
                    placeholder = "Type to search...",
                    onInputClear = {
                        println("Search cleared")
                    }
                )

                if (searchText3.isNotEmpty()) {
                    LemonadeUi.Text(
                        text = "Searching for: $searchText3",
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                        color = LemonadeTheme.colors.content.contentSecondary
                    )
                }
            }
        }

        // Disabled
        SearchFieldSection(title = "Disabled") {
            @OptIn(ExperimentalLemonadeComponent::class)
            LemonadeUi.SearchField(
                input = "",
                onInputChanged = {},
                placeholder = "Search disabled...",
                enabled = false
            )
        }

        // Usage Example
        SearchFieldSection(title = "Usage Example") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                @OptIn(ExperimentalLemonadeComponent::class)
                LemonadeUi.SearchField(
                    input = searchText1,
                    onInputChanged = { searchText1 = it },
                    placeholder = "Search products..."
                )

                val displayList = if (searchText1.isEmpty()) {
                    productList
                } else {
                    productList.filter { it.contains(searchText1, ignoreCase = true) }
                }

                if (displayList.isEmpty()) {
                    LemonadeUi.Text(
                        text = "No results found",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                        color = LemonadeTheme.colors.content.contentSecondary,
                        modifier = Modifier.padding(LemonadeTheme.spaces.spacing400)
                    )
                } else {
                    Column {
                        displayList.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = LemonadeTheme.spaces.spacing200),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LemonadeUi.Text(
                                    text = item,
                                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                                )
                                LemonadeUi.Icon(
                                    icon = LemonadeIcons.ChevronRight,
                                    contentDescription = null,
                                    size = LemonadeAssetSize.Small,
                                    tint = LemonadeTheme.colors.content.contentTertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchFieldSection(
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
