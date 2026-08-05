package com.teya.lemonade

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
