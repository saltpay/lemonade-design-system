package com.teya.lemonade

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private data class BorderWidthItem(
    val name: String,
    val value: Dp
)

private val borderWidthItems = listOf(
    BorderWidthItem("border0", 0.dp),
    BorderWidthItem("border25", 1.dp),
    BorderWidthItem("border50", 2.dp),
    BorderWidthItem("border75", 3.dp),
    BorderWidthItem("border100", 4.dp),
)

@Composable
internal fun BorderWidthDisplay() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing300),
    ) {
        item {
            LemonadeUi.Text(
                text = "Border Width Tokens",
                textStyle = LemonadeTheme.typography.headingMedium,
                modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing200)
            )
        }

        items(borderWidthItems) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                LemonadeUi.Text(
                    text = item.name,
                    textStyle = LemonadeTheme.typography.bodySmallMedium,
                    modifier = Modifier.width(120.dp)
                )

                LemonadeUi.Text(
                    text = "${item.value.value.toInt()}dp",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentSecondary,
                    modifier = Modifier.width(50.dp)
                )

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .then(
                            if (item.value > 0.dp) {
                                Modifier.border(
                                    width = item.value,
                                    color = LemonadePrimitiveColors.Solid.Blue.blue500,
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }
    }
}
