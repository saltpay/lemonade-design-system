package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeRadius

private data class RadiusItem(
    val name: String,
    val value: Dp,
)

private val radiusItems = listOf(
    RadiusItem("radius0", LemonadeRadius.Radius0.dp),
    RadiusItem("radius50", LemonadeRadius.Radius50.dp),
    RadiusItem("radius100", LemonadeRadius.Radius100.dp),
    RadiusItem("radius150", LemonadeRadius.Radius150.dp),
    RadiusItem("radius200", LemonadeRadius.Radius200.dp),
    RadiusItem("radius300", LemonadeRadius.Radius300.dp),
    RadiusItem("radius400", LemonadeRadius.Radius400.dp),
    RadiusItem("radius500", LemonadeRadius.Radius500.dp),
    RadiusItem("radius600", LemonadeRadius.Radius600.dp),
    RadiusItem("radius800", LemonadeRadius.Radius800.dp),
    RadiusItem("radiusFull", LemonadeRadius.RadiusFull.dp),
)

@Composable
internal fun RadiusDisplay() {
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
                text = "Radius Tokens",
                textStyle = LemonadeTheme.typography.headingMedium,
                modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing200),
            )
        }

        items(radiusItems) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                LemonadeUi.Text(
                    text = item.name,
                    textStyle = LemonadeTheme.typography.bodySmallMedium,
                    modifier = Modifier.width(100.dp),
                )

                LemonadeUi.Text(
                    text = "${item.value.value.toInt()}dp",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                    color = LemonadeTheme.colors.content.contentSecondary,
                    modifier = Modifier.width(50.dp),
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(item.value))
                        .background(LemonadePrimitiveColors.Solid.Blue.blue500),
                )
            }
        }
    }
}
