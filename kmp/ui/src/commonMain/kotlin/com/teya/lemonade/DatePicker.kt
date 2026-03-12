@file:OptIn(ExperimentalTime::class)

package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIcons
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.minusMonth
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.number
import kotlinx.datetime.onDay
import kotlinx.datetime.plus
import kotlinx.datetime.plusMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val PAGES_TOTAL = 1200
private const val CENTER_PAGE = PAGES_TOTAL / 2

/**
 * A single-date picker widget from the Lemonade Design System.
 *
 * Provides a scrollable month view that allows users to select a single date.
 * It features smooth page animations between months and supports restricting
 * selection to a specific date range using [minDate] and [maxDate].
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.DatePicker(
 *     monthFormatter = { month -> "Month $month" },
 *     weekdayAbbreviations = listOf("S", "M", "T", "W", "T", "F", "S"),
 *     onDateChanged = { date -> println(date) },
 * )
 * ```
 *
 * @param monthFormatter Formatter that returns the month name for a given month number (1-12).
 * @param weekdayAbbreviations Exactly 7 items representing Sunday through Saturday.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param initialDate The initially selected date.
 * @param onDateChanged Called when a date is selected.
 * @param minDate Minimum selectable date.
 * @param maxDate Maximum selectable date.
 */
@Composable
public fun LemonadeUi.DatePicker(
    monthFormatter: (month: Int) -> String,
    weekdayAbbreviations: List<String>,
    modifier: Modifier = Modifier,
    initialDate: LocalDate? = null,
    onDateChanged: ((LocalDate) -> Unit)? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
) {
    var selectedDate by remember { mutableStateOf(initialDate) }

    CoreDatePicker(
        monthFormatter = monthFormatter,
        weekdayAbbreviations = weekdayAbbreviations,
        modifier = modifier,
        selectedDates = setOfNotNull(selectedDate),
        onDateSelected = { date ->
            selectedDate = date
            onDateChanged?.invoke(date)
        },
        minDate = minDate,
        maxDate = maxDate,
    )
}

/**
 * A date range picker widget from the Lemonade Design System.
 *
 * Provides a scrollable month view that allows users to select a start and end date.
 * The user first taps to select a start date, then taps again to select an end date.
 * If the second tap is before the start date, the dates are swapped automatically.
 *
 * ## Usage
 * ```kotlin
 * LemonadeUi.DateRangePicker(
 *     monthFormatter = { month -> "Month $month" },
 *     weekdayAbbreviations = listOf("S", "M", "T", "W", "T", "F", "S"),
 *     onDateRangeChanged = { start, end -> println("$start - $end") },
 * )
 * ```
 *
 * @param monthFormatter Formatter that returns the month name for a given month number (1-12).
 * @param weekdayAbbreviations Exactly 7 items representing Sunday through Saturday.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param onDateRangeChanged Called when a complete date range is selected (start and end).
 * @param initialStartDate Initial start date for the range.
 * @param initialEndDate Initial end date for the range.
 * @param minDate Minimum selectable date.
 * @param maxDate Maximum selectable date.
 * @param maxRangeDays Maximum number of days allowed in a date range selection.
 */
@Composable
public fun LemonadeUi.DateRangePicker(
    monthFormatter: (month: Int) -> String,
    weekdayAbbreviations: List<String>,
    modifier: Modifier = Modifier,
    onDateRangeChanged: ((start: LocalDate, end: LocalDate) -> Unit)? = null,
    initialStartDate: LocalDate? = null,
    initialEndDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    maxRangeDays: Int? = null,
) {
    var rangeStartDate by remember { mutableStateOf(initialStartDate) }
    var rangeEndDate by remember { mutableStateOf(initialEndDate) }
    val isSelectingEndDate = rangeStartDate != null && rangeEndDate == null

    val effectiveMin = remember(minDate, rangeStartDate, isSelectingEndDate, maxRangeDays) {
        var min = minDate
        if (isSelectingEndDate && maxRangeDays != null) {
            rangeStartDate?.let { start ->
                val rangeMin = start.minus(maxRangeDays, DateTimeUnit.DAY)
                if (min == null || rangeMin > min) {
                    min = rangeMin
                }
            }
        }
        min
    }
    val effectiveMax = remember(maxDate, rangeStartDate, isSelectingEndDate, maxRangeDays) {
        var max = maxDate
        if (isSelectingEndDate && maxRangeDays != null) {
            rangeStartDate?.let { start ->
                val rangeMax = start.plus(maxRangeDays, DateTimeUnit.DAY)

                if (max == null || rangeMax < max) {
                    max = rangeMax
                }
            }
        }
        max
    }

    CoreDatePicker(
        monthFormatter = monthFormatter,
        weekdayAbbreviations = weekdayAbbreviations,
        modifier = modifier,
        selectedDates = setOfNotNull(rangeStartDate, rangeEndDate),
        onDateSelected = { date ->
            val start = rangeStartDate

            if (!isSelectingEndDate || start == null) {
                rangeStartDate = date
                rangeEndDate = null
                return@CoreDatePicker
            }

            val newStart = minOf(start, date)
            val newEnd = maxOf(start, date)

            rangeStartDate = newStart
            rangeEndDate = newEnd

            onDateRangeChanged?.invoke(newStart, newEnd)
        },
        minDate = effectiveMin,
        maxDate = effectiveMax,
    )
}

@Composable
private fun CoreDatePicker(
    monthFormatter: (Int) -> String,
    weekdayAbbreviations: List<String>,
    modifier: Modifier,
    selectedDates: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    minDate: LocalDate?,
    maxDate: LocalDate?,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    val startMonth = remember { YearMonth(today.year, today.month.number) }

    val pagerState = rememberPagerState(initialPage = CENTER_PAGE) { PAGES_TOTAL }
    val coroutineScope = rememberCoroutineScope()

    val centerYearMonth = startMonth.plus(pagerState.currentPage.toLong() - CENTER_PAGE, DateTimeUnit.MONTH)

    val headerLabel = "${monthFormatter(centerYearMonth.month.number)} ${centerYearMonth.year}"

    val canGoPrev = minDate?.let {
        it <= centerYearMonth.minus(1, DateTimeUnit.MONTH).lastDay
    } ?: true

    val canGoNext = maxDate?.let {
        it >= centerYearMonth.plus(1, DateTimeUnit.MONTH).firstDay
    } ?: true

    val horizontalPadding = LocalSpaces.current.spacing400

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MonthHeader(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
            yearMonth = centerYearMonth,
            onYearMonthChange = { newYearMonth ->
                val diff = centerYearMonth.monthsUntil(newYearMonth)

                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + diff)
                }
            },
            headerLabel = headerLabel,
            canGoPrev = canGoPrev,
            canGoNext = canGoNext,
        )

        Spacer(modifier = Modifier.height(LocalSpaces.current.spacing200))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
        ) {
            weekdayAbbreviations.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    LemonadeUi.Text(
                        text = day,
                        textStyle = LocalTypographies.current.bodyXSmallOverline,
                        color = LocalColors.current.content.contentPrimary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(LocalSpaces.current.spacing100))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = horizontalPadding,
        ) { pageIndex ->
            MonthGrid(
                yearMonth = startMonth.plus(pageIndex.toLong() - CENTER_PAGE, DateTimeUnit.MONTH),
                selectedDates = selectedDates,
                minDate = minDate,
                maxDate = maxDate,
                onDateSelected = onDateSelected,
            )
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    onYearMonthChange: (YearMonth) -> Unit,
    headerLabel: String,
    canGoPrev: Boolean,
    canGoNext: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LemonadeUi.IconButton(
            icon = LemonadeIcons.ChevronLeft,
            contentDescription = "Previous month",
            variant = LemonadeIconButtonVariant.Ghost,
            enabled = canGoPrev,
            onClick = { onYearMonthChange(yearMonth.minusMonth()) },
        )

        LemonadeUi.Text(
            text = headerLabel,
            textStyle = LocalTypographies.current.bodySmallSemiBold,
            color = LocalColors.current.content.contentPrimary,
        )

        LemonadeUi.IconButton(
            icon = LemonadeIcons.ChevronRight,
            contentDescription = "Next month",
            variant = LemonadeIconButtonVariant.Ghost,
            enabled = canGoNext,
            onClick = { onYearMonthChange(yearMonth.plusMonth()) },
        )
    }
}

private fun generateMonthDays(month: YearMonth): List<LocalDate> {
    val firstDayOfMonth = month.firstDay.dayOfWeek

    val startingWeekDay = DayOfWeek.SUNDAY
    val firstDayOffset = (firstDayOfMonth.ordinal - startingWeekDay.ordinal + DayOfWeek.entries.size) % DayOfWeek.entries.size
    val days = mutableListOf<LocalDate>()

    // Previous month
    val previousMonth = month.minusMonth()
    val prevMonthLength = previousMonth.numberOfDays
    for (i in firstDayOffset downTo 1) {
        days.add(previousMonth.onDay(prevMonthLength - i + 1))
    }

    // Current month
    for (day in 1..month.numberOfDays) {
        days.add(month.onDay(day))
    }

    // Next month
    val nextMonth = month.plusMonth()
    var nextDay = 1
    while (days.size % DayOfWeek.entries.size != 0 || days.size < 42) {
        days.add(nextMonth.onDay(nextDay++))
    }

    return days
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDates: Set<LocalDate>,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    val days = remember(yearMonth) { generateMonthDays(yearMonth) }

    val isRangeComplete = selectedDates.size >= 2
    val rangeStartDate = if (isRangeComplete) selectedDates.min() else null
    val rangeEndDate = if (isRangeComplete) selectedDates.max() else null

    val cellHorizontalPadding = LocalSpaces.current.spacing200
    val cellRadius = LocalRadius.current.radius200
    val colors = LocalColors.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing100),
    ) {
        days.chunked(DayOfWeek.entries.size).forEach { week ->
            Row(
                modifier = Modifier
                    .drawBehind {
                        if (rangeStartDate != null && rangeEndDate != null) {
                            val cellWidth = size.width / DayOfWeek.entries.size
                            val paddingPx = cellHorizontalPadding.toPx()

                            val startIndex = week.indexOfFirst { it >= rangeStartDate }
                            val endIndex = week.indexOfLast { it <= rangeEndDate }

                            if (startIndex != -1 && endIndex != -1) {
                                val left = cellWidth * startIndex + paddingPx
                                val right = cellWidth * (endIndex + 1) - paddingPx

                                drawRoundRect(
                                    color = colors.background.bgBrandSubtle,
                                    topLeft = Offset(left, 0f),
                                    size = Size(right - left, size.height),
                                    cornerRadius = CornerRadius(cellRadius.toPx())
                                )
                            }
                        }
                    }
            ) {
                week.forEach { current ->
                    val isInRange = rangeStartDate?.let { start ->
                        rangeEndDate?.let { end ->
                            current in start..end
                        }
                    } ?: false

                    val isBeforeMin = minDate != null && current < minDate
                    val isAfterMax = maxDate != null && current > maxDate

                    ContentCell(
                        modifier = Modifier.padding(horizontal = cellHorizontalPadding),
                        text = "${current.day}",
                        isCurrent = current == today,
                        isSelected = current in selectedDates,
                        isEnabled = !isBeforeMin && !isAfterMax,
                        isOutsideVisibleRange = current.yearMonth != yearMonth,
                        isInsideSelectedRange = isInRange,
                        onClick = { onDateSelected(current) },
                        interactionSource = remember { MutableInteractionSource() },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.ContentCell(
    modifier: Modifier = Modifier,
    text: String,
    isCurrent: Boolean,
    isSelected: Boolean,
    isEnabled: Boolean,
    isOutsideVisibleRange: Boolean,
    isInsideSelectedRange: Boolean,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource,
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    val textColor = when {
        !isEnabled -> LocalColors.current.content.contentTertiary
        isSelected || isInsideSelectedRange -> LocalColors.current.content.contentOnBrandHigh
        isCurrent -> LocalColors.current.content.contentBrand
        isOutsideVisibleRange -> LocalColors.current.content.contentSecondary
        else -> LocalColors.current.content.contentPrimary
    }

    val textStyle = when {
        isCurrent -> LocalTypographies.current.bodyMediumSemiBold
        else -> LocalTypographies.current.bodyMediumMedium
    }

    val backgroundColor = when {
        isSelected -> LocalColors.current.interaction.bgBrandInteractive
        else -> Color.Transparent
    }

    val cellShape = LocalShapes.current.radius200

    Box(
        modifier = modifier
            .weight(1f)
            .then(
                other = if (isFocused) {
                    Modifier
                        .border(
                            width = LocalBorderWidths.current.base.border50,
                            color = LocalColors.current.border.borderSelected,
                            shape = cellShape,
                        )
                } else {
                    Modifier
                }
            ).clip(
                shape = cellShape
            ).clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                role = Role.Button,
                enabled = isEnabled,
                indication = null,
            ).background(
                color = backgroundColor,
            ),
        contentAlignment = Alignment.Center
    ) {
        LemonadeUi.Text(
            modifier = Modifier.padding(vertical = LocalSpaces.current.spacing200),
            text = text,
            textStyle = textStyle,
            color = textColor,
        )

        if (isCurrent) {
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = LocalSpaces.current.spacing100)
                .size(LocalSpaces.current.spacing100)
                .background(
                    color = textColor,
                    shape = LocalShapes.current.radiusFull,
                    ),
                )
        }
    }
}
