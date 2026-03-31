package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.teya.lemonade.core.LemonadeContentListItemLayout

/**
 * A display-only list item for showing label-value pairs.
 *
 * Supports horizontal (label left, value right) and vertical (label top, value bottom) layouts.
 * In vertical layout, providing a [contentSlot] switches the value to a larger typography.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.ContentListItem(
 *     label = "Account holder",
 *     value = "John Doe",
 * )
 *
 * LemonadeUi.ContentListItem(
 *     label = "Balance",
 *     value = "$1,234.56",
 *     layout = LemonadeContentListItemLayout.Vertical,
 *     contentSlot = { LemonadeUi.Tag(label = "Available", voice = TagVoice.Positive) },
 * )
 * ```
 *
 * @param label - Label [String] describing the data field.
 * @param value - Value [String] to display.
 * @param layout - [LemonadeContentListItemLayout] horizontal or vertical arrangement.
 * @param modifier - [Modifier] to be applied to the root container.
 * @param leadingSlot - Optional slot for a leading element (e.g. SymbolContainer).
 * @param trailingSlot - Optional slot for a trailing element (e.g. icon action).
 * @param contentSlot - Optional slot for additional content. In vertical layout, this also
 *   switches the value typography to [bodyXLargeSemiBold].
 */
@Composable
public fun LemonadeUi.ContentListItem(
    label: String,
    value: String,
    layout: LemonadeContentListItemLayout = LemonadeContentListItemLayout.Horizontal,
    modifier: Modifier = Modifier,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    when (layout) {
        LemonadeContentListItemLayout.Horizontal -> HorizontalContentListItem(
            label = label,
            value = value,
            modifier = modifier,
            leadingSlot = leadingSlot,
            trailingSlot = trailingSlot,
            contentSlot = contentSlot,
        )

        LemonadeContentListItemLayout.Vertical -> VerticalContentListItem(
            label = label,
            value = value,
            modifier = modifier,
            leadingSlot = leadingSlot,
            trailingSlot = trailingSlot,
            contentSlot = contentSlot,
        )
    }
}

@Composable
private fun HorizontalContentListItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        modifier = modifier
            .padding(
                horizontal = LocalSpaces.current.spacing400,
                vertical = LocalSpaces.current.spacing200,
            ),
    ) {
        if (leadingSlot != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leadingSlot()
            }
        }

        Column(
            modifier = Modifier.weight(weight = 1f),
        ) {
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodyMediumRegular,
                color = LocalColors.current.content.contentSecondary,
            )

            if (contentSlot != null) {
                contentSlot()
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        ) {
            LemonadeUi.Text(
                text = value,
                textStyle = LocalTypographies.current.bodyMediumMedium,
                color = LocalColors.current.content.contentPrimary,
                textAlign = TextAlign.Right,
            )

            if (trailingSlot != null) {
                trailingSlot()
            }
        }
    }
}

@Composable
private fun VerticalContentListItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    contentSlot: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val isLarge = contentSlot != null

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing300),
        modifier = modifier
            .padding(
                horizontal = LocalSpaces.current.spacing400,
                vertical = LocalSpaces.current.spacing200,
            ),
    ) {
        if (leadingSlot != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leadingSlot()
            }
        }

        Column(
            modifier = Modifier.weight(weight = 1f),
        ) {
            LemonadeUi.Text(
                text = label,
                textStyle = LocalTypographies.current.bodySmallRegular,
                color = LocalColors.current.content.contentSecondary,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = LocalSpaces.current.spacing100),
            ) {
                LemonadeUi.Text(
                    text = value,
                    textStyle = if (isLarge) {
                        LocalTypographies.current.bodyXLargeSemiBold
                    } else {
                        LocalTypographies.current.bodyMediumMedium
                    },
                    color = LocalColors.current.content.contentPrimary,
                    modifier = Modifier.weight(weight = 1f),
                )

                if (trailingSlot != null) {
                    trailingSlot()
                }
            }

            if (contentSlot != null) {
                contentSlot()
            }
        }
    }
}
