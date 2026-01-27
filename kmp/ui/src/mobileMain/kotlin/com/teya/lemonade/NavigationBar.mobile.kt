package com.teya.lemonade

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

public enum class NavigationBarVariant {
    Default,
    Subtle,
}

private val NavigationBarVariant.backgroundColor: Color
    @Composable get() {
        return when (this) {
            NavigationBarVariant.Default -> LocalColors.current.background.bgDefault
            NavigationBarVariant.Subtle -> LocalColors.current.background.bgSubtle
        }
    }

@Composable
public fun LemonadeUi.NavigationBar(
    label: String,
    contentScrollState: ScrollState?,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
    leadingSlot: @Composable (BoxScope.() -> Unit)? = null,
    trailingSlot: @Composable (RowScope.() -> Unit)? = null,
    bottomSlot: @Composable (BoxScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    CoreNavigationBar(
        leadingSlot = leadingSlot,
        trailingSlot = trailingSlot,
        bottomSlot = bottomSlot,
        content = content,
        label = label,
        variant = variant,
        modifier = modifier,
    )
}

@Composable
internal fun CoreNavigationBar(
    leadingSlot: @Composable (BoxScope.() -> Unit)?,
    trailingSlot: @Composable (RowScope.() -> Unit)?,
    bottomSlot: @Composable (BoxScope.() -> Unit)?,
    content: LazyListScope.() -> Unit,
    label: String,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        stickyHeader { _ ->
            CoreNavigationBarContent(
                leadingSlot = leadingSlot,
                trailingSlot = trailingSlot,
                label = label,
                variant = variant,
                modifier = Modifier
                    .background(color = variant.backgroundColor)
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpaces.current.spacing300)
                    .padding(bottom = LocalSpaces.current.spacing200),
            )
        }

        item {
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.headingLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .background(color = variant.backgroundColor)
                    .fillMaxWidth()
                    .padding(horizontal = LocalSpaces.current.spacing300)
                    .padding(bottom = LocalSpaces.current.spacing200),
            )
        }

        bottomSlot?.let { bottomContent ->
            stickyHeader { _ ->
                Box(
                    content = bottomContent,
                    modifier = Modifier
                        .background(color = variant.backgroundColor)
                        .fillMaxWidth(),
                )
            }
        }

        content()
    }
}

@Composable
internal fun CoreNavigationBarContent(
    leadingSlot: @Composable (BoxScope.() -> Unit)?,
    trailingSlot: @Composable (RowScope.() -> Unit)?,
    label: String,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f),
            content = {
                leadingSlot?.invoke(this)
            },
        )

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.headingXXSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        Row(
            modifier = Modifier.weight(weight = 1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = LocalSpaces.current.spacing200,
                alignment = Alignment.End,
            ),
        ) {
            trailingSlot?.invoke(this)
        }
    }
}