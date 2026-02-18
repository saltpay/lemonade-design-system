package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.teya.lemonade.core.LemonadeSkeletonSize

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
                LemonadeUi.LineSkeleton(
                    modifier = Modifier.fillMaxWidth()
                )
                LemonadeUi.LineSkeleton(
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f)
                )
                LemonadeUi.LineSkeleton(
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f)
                )
            }
        }

        // Avatar
        SkeletonSection(title = "Avatar") {
            LemonadeUi.CircleSkeleton(
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
                LemonadeUi.CircleSkeleton(
                    size = LemonadeSkeletonSize.XXLarge,
                )
                Column(
                    modifier = Modifier.weight(weight = 1f),
                ) {
                    LemonadeUi.LineSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.6f)
                    )
                    LemonadeUi.LineSkeleton(
                        size = LemonadeSkeletonSize.Small,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.4f)
                    )
                }
            }
        }

        // Block Placeholder
        SkeletonSection(title = "Block Placeholder") {
            LemonadeUi.BlockSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
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
