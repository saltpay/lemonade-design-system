package com.teya.lemonade

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private data class BorderWidthItem(
    val name: String,
    val value: Dp,
)

private val borderWidthItems: List<BorderWidthItem>
    @Composable get() = listOf(
        BorderWidthItem("border0", LemonadeTheme.borderWidths.base.border0),
        BorderWidthItem("border25", LemonadeTheme.borderWidths.base.border25),
        BorderWidthItem("border40", LemonadeTheme.borderWidths.base.border40),
        BorderWidthItem("border50", LemonadeTheme.borderWidths.base.border50),
        BorderWidthItem("border75", LemonadeTheme.borderWidths.base.border75),
        BorderWidthItem("border100", LemonadeTheme.borderWidths.base.border100),
    )

@Composable
internal fun BorderWidthDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = "Border Width Tokens",
            textStyle = LemonadeTheme.typography.headingMedium,
            modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing200),
        )

        borderWidthItems.map { border ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                LemonadeUi.Text(
                    text = border.name,
                    textStyle = LemonadeTheme.typography.bodySmallMedium,
                    modifier = Modifier.width(120.dp),
                )

                LemonadeUi.Text(
                    text = "${border.value.value}dp",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentSecondary,
                    modifier = Modifier.width(50.dp),
                )

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(60.dp)
                        .then(
                            if (border.value > 0.dp) {
                                Modifier.border(
                                    width = border.value,
                                    color = LemonadeTheme.colors.background.bgInfo,
                                    shape = RoundedCornerShape(8.dp),
                                )
                            } else {
                                Modifier
                            },
                        ),
                )
            }
        }
    }
}
