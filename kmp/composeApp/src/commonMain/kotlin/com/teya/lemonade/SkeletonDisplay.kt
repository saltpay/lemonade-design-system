package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeSkeletonSize
import com.teya.lemonade.core.LemonadeSkeletonVariant

@Composable
internal fun SkeletonDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        // Text Lines
        SkeletonSection(title = "Text Lines") {
            Column {
                LemonadeUi.Skeleton(
                    modifier = Modifier.fillMaxWidth()
                )
                LemonadeUi.Skeleton(
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f)
                )
                LemonadeUi.Skeleton(
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f)
                )
            }
        }

        // Avatar
        SkeletonSection(title = "Avatar") {
            LemonadeUi.Skeleton(
                variant = LemonadeSkeletonVariant.Circle,
                modifier = Modifier.size(size = LemonadeTheme.sizes.size1200),
            )
        }

        // Card Skeleton
        SkeletonSection(title = "Card Skeleton") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(size = LemonadeTheme.radius.radius500))
                    .background(color = LemonadeTheme.colors.background.bgElevated)
                    .padding(all = LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Skeleton(
                    variant = LemonadeSkeletonVariant.Circle,
                    modifier = Modifier.size(size = LemonadeTheme.sizes.size1200),
                )
                Column(
                    modifier = Modifier.weight(weight = 1f),
                ) {
                    LemonadeUi.Skeleton(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.6f)
                    )
                    LemonadeUi.Skeleton(
                        size = LemonadeSkeletonSize.Small,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.4f)
                    )
                }
            }
        }

        // Image Placeholder
        SkeletonSection(title = "Image Placeholder") {
            LemonadeUi.Skeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 200.dp),
            )
        }
    }
}

@Composable
private fun SkeletonSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
