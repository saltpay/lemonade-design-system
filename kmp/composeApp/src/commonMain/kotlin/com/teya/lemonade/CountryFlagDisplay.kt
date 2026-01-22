package com.teya.lemonade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeCountryFlags
import com.teya.lemonade.core.LemonadeIcons


@Composable
internal fun CountryFlagDisplay() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.FixedSize(70.dp),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                items = LemonadeCountryFlags.entries,
                key = { it.ordinal }
            ) { icon ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clickable { },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LemonadeUi.CountryFlag(
                        flag = icon,
                        size = LemonadeAssetSize.XXLarge,
                    )
                }
            }
        }
    }
}
