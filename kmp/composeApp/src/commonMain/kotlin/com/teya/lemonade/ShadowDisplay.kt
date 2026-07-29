package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeShadow

@Composable
internal fun ShadowDisplay() {
    // Hoisted above the lazy list so the toggle survives its item being scrolled out and disposed.
    var shownShadow by remember { mutableStateOf(value = LemonadeShadow.None) }

    SampleScreenDisplayLazyColumn(title = "Shadows") {
        items(
            items = LemonadeShadow.entries,
            key = { shadow -> shadow.name },
        ) { shadow ->
            ShadowSection(title = "Shadow - $shadow") {
                Box(
                    modifier = Modifier
                        .lemonadeShadow(
                            shadow = shadow,
                            shape = LemonadeTheme.shapes.radius500,
                        ).background(
                            color = LemonadeTheme.colors.background.bgDefault,
                            shape = LemonadeTheme.shapes.radius500,
                        ).size(
                            height = 100.dp,
                            width = 300.dp,
                        ),
                )
            }
        }

        item(key = "animated") {
            ShadowSection(title = "Animated Shadow") {
                Box(
                    modifier = Modifier
                        .animateLemonadeShadow(
                            shape = LemonadeTheme.shapes.radius500,
                            shadow = shownShadow,
                        ).background(
                            color = LemonadeTheme.colors.background.bgDefault,
                            shape = LemonadeTheme.shapes.radius500,
                        ).size(300.dp),
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
                    },
                )
            }
        }
    }
}

@Composable
private fun ShadowSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = LemonadeTheme.spaces.spacing400),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.displayMedium,
        )
        content()
    }
}
