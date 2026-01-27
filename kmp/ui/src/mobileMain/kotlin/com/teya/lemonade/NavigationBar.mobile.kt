package com.teya.lemonade

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

public enum class NavigationBarVariant {
    Default,
    Subtle,
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
) {
    CoreNavigationBar(
        leadingSlot = leadingSlot,
        trailingSlot = trailingSlot,
        bottomSlot = bottomSlot,
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
    label: String,
    variant: NavigationBarVariant,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        CoreNavigationBarContent(
            leadingSlot = leadingSlot,
            trailingSlot = trailingSlot,
            label = label,
            variant = variant,
            modifier = Modifier.padding(horizontal = LocalSpaces.current.spacing300),
        )

        LemonadeUi.Text(
            text = label,
            textStyle = LocalTypographies.current.headingXLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            modifier = Modifier
                .padding(
                vertical = LocalSpaces.current.spacing200,
                horizontal = LocalSpaces.current.spacing300,
            )
        )

        bottomSlot?.let { bottomContent ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                content = bottomContent,
            )
        }
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
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing200),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            trailingSlot?.invoke(this)
        }
    }
}

@LemonadePreview
@Composable
private fun NavigationBarPreview() {

}