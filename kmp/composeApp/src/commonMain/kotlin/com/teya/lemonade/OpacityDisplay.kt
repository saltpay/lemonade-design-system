package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

private data class OpacityItem(
    val name: String,
    val value: Float
)

private val baseOpacityItems = listOf(
    OpacityItem("opacity0", 0.0f),
    OpacityItem("opacity5", 0.05f),
    OpacityItem("opacity10", 0.1f),
    OpacityItem("opacity20", 0.2f),
    OpacityItem("opacity30", 0.3f),
    OpacityItem("opacity40", 0.4f),
    OpacityItem("opacity50", 0.5f),
    OpacityItem("opacity60", 0.6f),
    OpacityItem("opacity70", 0.7f),
    OpacityItem("opacity80", 0.8f),
    OpacityItem("opacity90", 0.9f),
    OpacityItem("opacity100", 1.0f),
)

private val stateOpacityItems = listOf(
    OpacityItem("opacityPressed", 0.2f),
    OpacityItem("opacityDisabled", 0.4f),
)

@Composable
internal fun OpacityDisplay() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing300),
    ) {
        item {
            LemonadeUi.Text(
                text = "Opacity Tokens",
                textStyle = LemonadeTheme.typography.headingMedium,
                modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing200)
            )
        }

        item {
            LemonadeUi.Text(
                text = "Base Opacities",
                textStyle = LemonadeTheme.typography.headingXSmall,
                modifier = Modifier.padding(vertical = LemonadeTheme.spaces.spacing200)
            )
        }

        items(baseOpacityItems) { item ->
            OpacityRow(item)
        }

        item {
            LemonadeUi.Text(
                text = "State Opacities",
                textStyle = LemonadeTheme.typography.headingXSmall,
                modifier = Modifier.padding(vertical = LemonadeTheme.spaces.spacing200)
            )
        }

        items(stateOpacityItems) { item ->
            OpacityRow(item)
        }
    }
}

@Composable
private fun OpacityRow(item: OpacityItem) {
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
            text = "${(item.value * 100).toInt()}%",
            textStyle = LemonadeTheme.typography.bodySmallRegular,
            color = LemonadeTheme.colors.content.contentSecondary,
            modifier = Modifier.width(50.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LemonadePrimitiveColors.Solid.Green.green500.copy(alpha = item.value))
        )
    }
}
