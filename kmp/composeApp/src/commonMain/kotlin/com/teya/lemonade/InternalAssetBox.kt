package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
internal fun LemonadeAssetBox(
    asset: @Composable BoxScope.() -> Unit,
    label: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = LemonadeTheme.borderWidths.base.border25,
                color = LemonadeTheme.colors.border.borderNeutralLow,
                shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
            )
            .background(
                color = LemonadeTheme.colors.background.bgDefault,
                shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
            )
            .padding(LemonadeTheme.spaces.spacing400)
            .heightIn(min = LemonadeTheme.sizes.size1600),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = LemonadeTheme.spaces.spacing300,
            alignment = Alignment.CenterVertically
        )
    ) {
        Box(content = asset)
        LemonadeUi.Text(
            text = label,
            textStyle = LemonadeTheme.typography.bodyXSmallRegular,
            color = LemonadeTheme.colors.content.contentSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}