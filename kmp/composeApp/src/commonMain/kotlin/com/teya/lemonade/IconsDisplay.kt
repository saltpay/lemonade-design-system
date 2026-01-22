package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons


@Composable
internal fun IconsDisplay() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(minSize = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            contentPadding = PaddingValues(LemonadeTheme.spaces.spacing400)
        ) {
            items(
                items = LemonadeIcons.entries,
                key = { it.ordinal }
            ) { icon ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(LemonadeTheme.radius.radius200))
                        .background(LemonadeTheme.colors.background.bgSubtle)
                        .padding(LemonadeTheme.spaces.spacing200),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LemonadeUi.Icon(
                        icon = icon,
                        contentDescription = "Icon: ${icon.name}",
                        size = LemonadeAssetSize.Large,
                    )

                    LemonadeUi.Text(
                        text = icon.name,
                        textStyle = LemonadeTheme.typography.bodyXSmallRegular,
                        color = LemonadeTheme.colors.content.contentSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = LemonadeTheme.spaces.spacing100)
                    )
                }
            }
        }
    }
}
