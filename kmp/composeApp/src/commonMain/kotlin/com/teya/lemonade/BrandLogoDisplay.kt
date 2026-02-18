package com.teya.lemonade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeBrandLogos

@Composable
internal fun BrandLogoDisplay() {
    SampleScreenDisplayLazyGrid(
        title = "Brand Logos",
        columnsGap = LemonadeTheme.spaces.spacing200,
    ) {
        items(
            items = LemonadeBrandLogos.entries,
            key = { it.ordinal },
        ) { icon ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 70.dp)
                    .clickable { /* Nothing */ },
            ) {
                LemonadeUi.BrandLogo(
                    logo = icon,
                    size = LemonadeAssetSize.XXLarge,
                )
            }
        }
    }
}
