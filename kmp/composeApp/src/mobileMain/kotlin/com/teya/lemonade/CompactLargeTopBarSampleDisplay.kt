package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun CompactLargeTopBarSampleDisplay() {
    val topBarState = rememberTopBarState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LemonadeTheme.colors.background.bgSubtle)
            .navigationBarsPadding(),
    ) {
        LemonadeUi.TopBar(
            label = "Compact Large",
            subheading = "With sticky bottom slot",
            state = topBarState,
            trailingSlot = {
                LemonadeUi.IconButton(
                    icon = LemonadeIcons.Bell,
                    contentDescription = "Notifications",
                    onClick = {},
                    variant = LemonadeIconButtonVariant.Ghost,
                )
            },
            bottomSlot = {
                LemonadeUi.Text(
                    text = "Sticky content",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    modifier = Modifier.padding(
                        horizontal = LemonadeTheme.spaces.spacing400,
                        vertical = LemonadeTheme.spaces.spacing200,
                    ),
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
            items(count = 30) { index ->
                LemonadeUi.Text(
                    text = "Item ${index + 1}",
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
