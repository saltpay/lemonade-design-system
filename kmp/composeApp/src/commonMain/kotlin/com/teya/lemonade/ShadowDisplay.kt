package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teya.lemonade.core.LemonadeShadow

@Composable
internal fun ShadowDisplay() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = "Lemonade Component - Shadow",
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 20.sp,
            textStyle = LemonadeTheme.typography.bodyMediumRegular
        )

        LemonadeShadow.entries.forEach { shadow ->
            LemonadeUi.Text(
                text = "Shadow - $shadow",
                textStyle = LemonadeTheme.typography.displayMedium,
            )

            Box(
                modifier = Modifier
                    .lemonadeShadow(
                        shadow = shadow,
                        shape = LemonadeTheme.shapes.radius500,
                    )
                    .background(
                        color = LemonadeTheme.colors.background.bgDefault,
                        shape = LemonadeTheme.shapes.radius500,
                    )
                    .size(
                        height = 100.dp,
                        width = 300.dp,
                    )
            )
        }

        var shownShadow by remember {
            mutableStateOf(LemonadeShadow.None)
        }

        LemonadeUi.Text(
            text = "Animated Shadow",
            textStyle = LemonadeTheme.typography.displayMedium,
        )

        Box(
            modifier = Modifier
                .animateLemonadeShadow(
                    shape = LemonadeTheme.shapes.radius500,
                    shadow = shownShadow,
                )
                .background(
                    color = LemonadeTheme.colors.background.bgDefault,
                    shape = LemonadeTheme.shapes.radius500,
                )
                .size(300.dp)
        )

        LemonadeUi.Switch(
            label = "Show Shadow",
            checked = shownShadow == LemonadeShadow.Large,
            onCheckedChange = { setTo ->
                shownShadow = if (setTo) {
                    LemonadeShadow.Large
                } else {
                    LemonadeShadow.None
                }
            }
        )
    }
}