package com.teya.lemonade

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

@Composable
public fun LemonadeUi.NavigationBar(
    leadingSlot: @Composable (RowScope.() -> Unit)? = null,
    trailingSlot: @Composable (RowScope.() -> Unit)? = null,
    bottomSlot: @Composable (ColumnScope.() -> Unit)? = null,
    title: String,
    contentScrollState: ScrollState?,
    modifier: Modifier = Modifier,
) {
    CoreNavigationBar(
        leadingSlot = leadingSlot,
        trailingSlot = trailingSlot,
        bottomSlot = bottomSlot,
        title = title,
        contentScrollState = contentScrollState,
        modifier = modifier,
    )
}

@Composable
internal fun CoreNavigationBar(
    leadingSlot: @Composable (RowScope.() -> Unit)?,
    trailingSlot: @Composable (RowScope.() -> Unit)?,
    bottomSlot: @Composable (ColumnScope.() -> Unit)?,
    title: String,
    contentScrollState: ScrollState?,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var titleSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    val collapsedPercentage by remember {
        derivedStateOf {
            if (contentScrollState == null) {
                0f
            } else {
                (contentScrollState.value.toFloat() / titleSize.height).coerceIn(
                    minimumValue = 0f,
                    maximumValue = 1f,
                )
            }
        }
    }

    val isTitleCollapsed by remember {
        derivedStateOf {
            collapsedPercentage == 1f
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingSlot?.invoke(this)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(weight = 1f),
            ) {
                AnimatedContent(
                    targetState = isTitleCollapsed,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    content = { isCollapsed ->
                        if (isCollapsed) {
                            LemonadeUi.Text(
                                text = title,
                            )
                        }
                    }
                )
            }

            trailingSlot?.invoke(this)
        }

        LemonadeUi.Text(
            text = title,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .requiredHeightIn(
                    max = if (titleSize == IntSize.Zero || contentScrollState == null) {
                        Dp.Unspecified
                    } else {
                        with(density) {
                            (titleSize.height - contentScrollState.value).toDp()
                        }
                    },
                )
                .onGloballyPositioned { layoutCoordinates ->
                    titleSize = layoutCoordinates.size
                }
        )

        bottomSlot?.invoke(this)

        AnimatedContent(
            targetState = isTitleCollapsed,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            content = { isCollapsed ->
                if (isCollapsed) {
                    // fixme - replace by LemonadeUi.Divider once it is created.
                    Spacer(
                        modifier = Modifier
                            .background(color = LocalColors.current.border.borderNeutralMedium)
                            .height(height = LocalBorderWidths.current.base.border25)
                            .fillMaxWidth()
                    )
                }
            }
        )
    }
}