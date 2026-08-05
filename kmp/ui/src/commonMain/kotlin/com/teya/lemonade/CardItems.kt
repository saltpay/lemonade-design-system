package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.layout.LazyScopeMarker
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.teya.lemonade.core.LemonadeCardBackground
import com.teya.lemonade.core.LemonadeCardPadding

internal enum class LemonadeCardItemPosition {
    Single,
    First,
    Middle,
    Last,
}

internal fun resolveCardSlotPosition(
    visualIndex: Int,
    totalCount: Int,
): LemonadeCardItemPosition {
    val isFirst = visualIndex == 0
    val isLast = visualIndex == totalCount - 1
    return when {
        isFirst && isLast -> LemonadeCardItemPosition.Single
        isFirst -> LemonadeCardItemPosition.First
        isLast -> LemonadeCardItemPosition.Last
        else -> LemonadeCardItemPosition.Middle
    }
}

internal fun cardTotalSlotCount(
    rowCount: Int,
    hasHeader: Boolean,
    hasFooter: Boolean,
): Int {
    val headerCount = if (hasHeader) 1 else 0
    val footerCount = if (hasFooter) 1 else 0
    return headerCount + rowCount + footerCount
}

internal fun cardRowVisualIndex(
    rowIndex: Int,
    hasHeader: Boolean,
): Int = if (hasHeader) rowIndex + 1 else rowIndex

/**
 * Renders a Lemonade card directly into a [LazyListScope], keeping every row a real lazy
 * item of the host list. Rows are composed on demand as they scroll into view, unlike
 * [LemonadeUi.Card] whose whole content composes at once.
 *
 * The card look is drawn per item: the first visual slot (the header when present,
 * otherwise the first row) carries the top corners, and the last visual slot (the footer
 * when present, otherwise the last row) carries the bottom corners.
 *
 * The host list must not use `verticalArrangement = Arrangement.spacedBy(...)`: the gaps
 * would appear between the card's own rows and visually split the card. Give rows their
 * own internal padding, and separate consecutive cards with a spacer item instead.
 */
public fun LazyListScope.lemonadeCardItems(
    contentPadding: LemonadeCardPadding = LemonadeCardPadding.None,
    background: LemonadeCardBackground = LemonadeCardBackground.Default,
    header: CardHeaderConfig? = null,
    footerAction: CardFooterActionConfig? = null,
    content: LemonadeCardItemsScope.() -> Unit,
) {
    val recorder = CardItemsRecorder()
    recorder.content()

    val rowCount = recorder.rowCount
    val totalCount = cardTotalSlotCount(
        rowCount = rowCount,
        hasHeader = header != null,
        hasFooter = footerAction != null,
    )
    if (totalCount == 0) {
        return
    }

    if (header != null) {
        item(contentType = CardSlotContentType.Header) {
            CardSlotContainer(
                position = resolveCardSlotPosition(visualIndex = 0, totalCount = totalCount),
                background = background,
            ) {
                CardHeader(config = header)
            }
        }
    }

    var emittedRows = 0
    recorder.intervals.forEach { interval ->
        val intervalStart = emittedRows
        items(
            count = interval.count,
            key = interval.key,
            contentType = { localIndex: Int ->
                CardRowContentType(userType = interval.contentType(localIndex))
            },
        ) { localIndex: Int ->
            val itemScope = this
            val rowIndex = intervalStart + localIndex
            CardRowContainer(
                position = resolveCardSlotPosition(
                    visualIndex = cardRowVisualIndex(
                        rowIndex = rowIndex,
                        hasHeader = header != null,
                    ),
                    totalCount = totalCount,
                ),
                background = background,
                contentPadding = contentPadding,
                isFirstRow = rowIndex == 0,
                isLastRow = rowIndex == rowCount - 1,
            ) {
                interval.itemContent(itemScope, localIndex)
            }
        }
        emittedRows += interval.count
    }

    if (footerAction != null) {
        item(contentType = CardSlotContentType.Footer) {
            CardSlotContainer(
                position = resolveCardSlotPosition(
                    visualIndex = totalCount - 1,
                    totalCount = totalCount,
                ),
                background = background,
            ) {
                CardFooterAction(config = footerAction)
            }
        }
    }
}

/**
 * Receiver for [lemonadeCardItems] content. Mirrors the [LazyListScope] item DSL; every
 * entry becomes a real lazy item of the host list.
 */
@LazyScopeMarker
public sealed interface LemonadeCardItemsScope {

    public fun item(
        key: Any? = null,
        contentType: Any? = null,
        content: @Composable LazyItemScope.() -> Unit,
    )

    public fun items(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
    )
}

private class CardItemsInterval(
    val count: Int,
    val key: ((index: Int) -> Any)?,
    val contentType: (index: Int) -> Any?,
    val itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
)

private class CardItemsRecorder : LemonadeCardItemsScope {
    val intervals: MutableList<CardItemsInterval> = mutableListOf()

    val rowCount: Int
        get() = intervals.sumOf { interval -> interval.count }

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable LazyItemScope.() -> Unit,
    ) {
        intervals.add(
            CardItemsInterval(
                count = 1,
                key = if (key != null) {
                    { _: Int -> key }
                } else {
                    null
                },
                contentType = { _: Int -> contentType },
                itemContent = { _: Int -> content() },
            ),
        )
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
    ) {
        intervals.add(
            CardItemsInterval(
                count = count,
                key = key,
                contentType = contentType,
                itemContent = itemContent,
            ),
        )
    }
}

private data class CardRowContentType(
    val userType: Any?,
)

private enum class CardSlotContentType {
    Header,
    Footer,
}

@Composable
private fun CardSlotContainer(
    position: LemonadeCardItemPosition,
    background: LemonadeCardBackground,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = position.shape)
            .background(color = background.background),
    ) {
        content()
    }
}

@Composable
private fun CardRowContainer(
    position: LemonadeCardItemPosition,
    background: LemonadeCardBackground,
    contentPadding: LemonadeCardPadding,
    isFirstRow: Boolean,
    isLastRow: Boolean,
    content: @Composable () -> Unit,
) {
    val padding = contentPadding.spacing
    val zero = LocalSpaces.current.spacing0
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = position.shape)
            .background(color = background.background)
            .padding(
                start = padding,
                top = if (isFirstRow) padding else zero,
                end = padding,
                bottom = if (isLastRow) padding else zero,
            ),
    ) {
        content()
    }
}

private val LemonadeCardItemPosition.shape: Shape
    @Composable get() {
        val radius = LocalRadius.current.semantic.radiusContainerDefault
        val zero = LocalRadius.current.radius0
        return when (this) {
            LemonadeCardItemPosition.Single -> LocalShapes.current.semantic.radiusContainerDefault
            LemonadeCardItemPosition.First -> RoundedCornerShape(
                topStart = radius,
                topEnd = radius,
                bottomEnd = zero,
                bottomStart = zero,
            )
            LemonadeCardItemPosition.Middle -> RectangleShape
            LemonadeCardItemPosition.Last -> RoundedCornerShape(
                topStart = zero,
                topEnd = zero,
                bottomEnd = radius,
                bottomStart = radius,
            )
        }
    }
