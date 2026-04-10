@file:OptIn(ExperimentalTime::class)

package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
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
import kotlinx.datetime.plus
import kotlinx.datetime.plusMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val PAGES_TOTAL = 1200
private const val CENTER_PAGE = PAGES_TOTAL / 2

/**
 * State holder for [LemonadeUi.DatePicker].
 *
 * Holds the currently selected date as observable state, plus the selectable date range.
 * Observe [selectedDate] to react to user selections.
 *
 * @param initialDate The initially selected date — seeds [selectedDate].
 * @param minDate Minimum selectable date.
 * @param maxDate Maximum selectable date.
 * @see rememberDatePickerState
 */
@Stable
public class DatePickerState internal constructor(
    initialDate: LocalDate? = null,
    public val minDate: LocalDate? = null,
    public val maxDate: LocalDate? = null,
) {
    public var selectedDate: LocalDate? by mutableStateOf(initialDate)
        internal set
}

/**
 * Creates and remembers a [DatePickerState].
 *
 * @param initialDate The initially selected date.
 * @param minDate Minimum selectable date.
 * @param maxDate Maximum selectable date.
 */
@Composable
public fun rememberDatePickerState(
    initialDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
): DatePickerState =
    remember(initialDate, minDate, maxDate) {
        DatePickerState(
            initialDate = initialDate,
            minDate = minDate,
            maxDate = maxDate,
        )
    }

/**
 * State holder for [LemonadeUi.DateRangePicker].
 *
 * Holds the currently selected start/end dates as observable state, plus the selectable
 * date range and maximum range length. Observe [selectedStartDate] and [selectedEndDate]
 * to react to user selections.
 *
 * @param initialStartDate Initial start date — seeds [selectedStartDate].
 * @param initialEndDate Initial end date — seeds [selectedEndDate].
 * @param minDate Minimum selectable date.
 * @param maxDate Maximum selectable date.
 * @param maxRangeDays Maximum number of days allowed in a date range selection.
 * @see rememberDateRangePickerState
 */
@Stable
public class DateRangePickerState internal constructor(
    initialStartDate: LocalDate? = null,
    initialEndDate: LocalDate? = null,
    public val minDate: LocalDate? = null,
    public val maxDate: LocalDate? = null,
    public val maxRangeDays: Int? = null,
) {
    public var selectedStartDate: LocalDate? by mutableStateOf(initialStartDate)
        internal set
    public var selectedEndDate: LocalDate? by mutableStateOf(initialEndDate)
        internal set
}

/**
 * Creates and remembers a [DateRangePickerState].
 *
 * @param initialStartDate Initial start date for the range.
 * @param initialEndDate Initial end date for the range.
 * @param minDate Minimum selectable date.
 * @param maxDate Maximum selectable date.
 * @param maxRangeDays Maximum number of days allowed in a date range selection.
 */
@Composable
public fun rememberDateRangePickerState(
    initialStartDate: LocalDate? = null,
    initialEndDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    maxRangeDays: Int? = null,
): DateRangePickerState =
    remember(initialStartDate, initialEndDate, minDate, maxDate, maxRangeDays) {
        DateRangePickerState(
            initialStartDate = initialStartDate,
            initialEndDate = initialEndDate,
            minDate = minDate,
            maxDate = maxDate,
            maxRangeDays = maxRangeDays,
        )
    }

/**
 * A single-date picker widget from the Lemonade Design System.
 *
 * Provides a scrollable month view that allows users to select a single date.
 * It features smooth page animations between months and supports restricting
 * selection to a specific date range using [DatePickerState.minDate] and [DatePickerState.maxDate].
 *
 * ## Usage
 * ```kotlin
 * val state = rememberDatePickerState(initialDate = today)
 * LemonadeUi.DatePicker(
 *     state = state,
 *     monthFormatter = { monthNumber -> monthNames[monthNumber - 1] },
 *     weekdayAbbreviations = listOf("S", "M", "T", "W", "T", "F", "S"),
 * )
 * // Observe: state.selectedDate
 * ```
 *
 * @param monthFormatter Formatter that returns the month name for a given month number (1-12).
 * Needs to be localized by the caller.
 * @param weekdayAbbreviations List of exactly 7 items representing Sunday through Saturday.
 * Needs to be localized by the caller.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param state Configuration state created via [rememberDatePickerState].
 * Observe [DatePickerState.selectedDate] to react to user selections.
 * @param firstDayOfWeek The first day of the week shown in the grid. Defaults to
 * [DayOfWeek.SUNDAY]. Callers should supply the correct value for their locale
 * (e.g. [DayOfWeek.MONDAY] for ISO / European locales).
 * @param onMonthDisplayed Optional callback invoked when the displayed month changes.
 */
@Composable
public fun LemonadeUi.DatePicker(
    monthFormatter: (month: Int) -> String,
    weekdayAbbreviations: List<String>,
    modifier: Modifier = Modifier,
    state: DatePickerState = rememberDatePickerState(),
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    onMonthDisplayed: ((YearMonth) -> Unit)? = null,
) {
    CoreDatePicker(
        monthFormatter = monthFormatter,
        weekdayAbbreviations = weekdayAbbreviations,
        modifier = modifier,
        selectedDates = setOfNotNull(state.selectedDate),
        onDateSelected = { date -> state.selectedDate = date },
        minDate = state.minDate,
        maxDate = state.maxDate,
        firstDayOfWeek = firstDayOfWeek,
        onMonthDisplayed = onMonthDisplayed,
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
 * val state = rememberDateRangePickerState(maxRangeDays = 7)
 * LemonadeUi.DateRangePicker(
 *     state = state,
 *     monthFormatter = { monthNumber -> monthNames[monthNumber - 1] },
 *     weekdayAbbreviations = listOf("S", "M", "T", "W", "T", "F", "S"),
 * )
 * // Observe: state.selectedStartDate, state.selectedEndDate
 * ```
 *
 * @param monthFormatter Formatter that returns the month name for a given month number (1-12).
 * Needs to be localized by the caller.
 * @param weekdayAbbreviations List of exactly 7 items representing Sunday through Saturday.
 * Needs to be localized by the caller.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param state Configuration state created via [rememberDateRangePickerState].
 * Observe [DateRangePickerState.selectedStartDate] and [DateRangePickerState.selectedEndDate]
 * to react to user selections.
 * @param firstDayOfWeek The first day of the week shown in the grid. Defaults to
 * [DayOfWeek.SUNDAY]. Callers should supply the correct value for their locale
 * (e.g. [DayOfWeek.MONDAY] for ISO / European locales).
 * @param onMonthDisplayed Optional callback invoked when the displayed month changes.
 */
@Composable
public fun LemonadeUi.DateRangePicker(
    monthFormatter: (month: Int) -> String,
    weekdayAbbreviations: List<String>,
    modifier: Modifier = Modifier,
    state: DateRangePickerState = rememberDateRangePickerState(),
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    onMonthDisplayed: ((YearMonth) -> Unit)? = null,
) {
    val isSelectingEndDate = state.selectedStartDate != null && state.selectedEndDate == null

    val effectiveMin = remember(
        state.minDate,
        state.selectedStartDate,
        isSelectingEndDate,
        state.maxRangeDays,
    ) {
        var min = state.minDate
        if (isSelectingEndDate && state.maxRangeDays != null) {
            state.selectedStartDate?.let { start ->
                val rangeMin = start.minus(state.maxRangeDays, DateTimeUnit.DAY)
                if (min == null || rangeMin > min) {
                    min = rangeMin
                }
            }
        }
        min
    }
    val effectiveMax = remember(
        state.maxDate,
        state.selectedStartDate,
        isSelectingEndDate,
        state.maxRangeDays,
    ) {
        var max = state.maxDate
        if (isSelectingEndDate && state.maxRangeDays != null) {
            state.selectedStartDate?.let { start ->
                val rangeMax = start.plus(state.maxRangeDays, DateTimeUnit.DAY)

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
        selectedDates = setOfNotNull(state.selectedStartDate, state.selectedEndDate),
        onDateSelected = { date ->
            val start = state.selectedStartDate

            if (!isSelectingEndDate || start == null) {
                state.selectedStartDate = date
                state.selectedEndDate = null
                return@CoreDatePicker
            }

            state.selectedStartDate = minOf(start, date)
            state.selectedEndDate = maxOf(start, date)
        },
        minDate = effectiveMin,
        maxDate = effectiveMax,
        firstDayOfWeek = firstDayOfWeek,
        onMonthDisplayed = onMonthDisplayed,
    )
}

@Composable
private fun CoreDatePicker(
    modifier: Modifier,
    monthFormatter: (Int) -> String,
    weekdayAbbreviations: List<String>,
    selectedDates: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    firstDayOfWeek: DayOfWeek,
    onMonthDisplayed: ((YearMonth) -> Unit)?,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    val startMonth = remember { YearMonth(today.year, today.month.number) }

    val pagerState = rememberPagerState(initialPage = CENTER_PAGE) { PAGES_TOTAL }
    val coroutineScope = rememberCoroutineScope()

    val centerYearMonth = startMonth.plus(pagerState.currentPage.toLong() - CENTER_PAGE, DateTimeUnit.MONTH)

    val hasEmittedInitialMonth = remember { BoolRef(false) }

    if (onMonthDisplayed != null) {
        LaunchedEffect(centerYearMonth) {
            if (hasEmittedInitialMonth.value) {
                onMonthDisplayed(centerYearMonth)
            } else {
                hasEmittedInitialMonth.value = true
            }
        }
    }

    val headerLabel = "${monthFormatter(centerYearMonth.month.number)} ${centerYearMonth.year}"

    val canGoPrev = pagerState.currentPage > 0 &&
        minDate?.let {
            it <= centerYearMonth.minus(1, DateTimeUnit.MONTH).lastDay
        } ?: true

    val canGoNext = pagerState.currentPage < PAGES_TOTAL - 1 &&
        maxDate?.let {
            it >= centerYearMonth.plus(1, DateTimeUnit.MONTH).firstDay
        } ?: true

    val horizontalPadding = LocalSpaces.current.spacing400

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CalendarMonthHeader(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
            headerLabel = headerLabel,
            canGoPrev = canGoPrev,
            canGoNext = canGoNext,
            onPrev = {
                val newYearMonth = centerYearMonth.minusMonth()
                val diff = centerYearMonth.monthsUntil(newYearMonth)
                val targetPage = (pagerState.currentPage + diff).coerceIn(0, PAGES_TOTAL - 1)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(targetPage)
                }
            },
            onNext = {
                val newYearMonth = centerYearMonth.plusMonth()
                val diff = centerYearMonth.monthsUntil(newYearMonth)
                val targetPage = (pagerState.currentPage + diff).coerceIn(0, PAGES_TOTAL - 1)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(targetPage)
                }
            },
        )

        Spacer(modifier = Modifier.height(LocalSpaces.current.spacing200))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
        ) {
            weekdayAbbreviations
                .take(DayOfWeek.entries.size)
                .forEach { day ->
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
                firstDayOfWeek = firstDayOfWeek,
                onDateSelected = onDateSelected,
            )
        }
    }
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDates: Set<LocalDate>,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    firstDayOfWeek: DayOfWeek,
    onDateSelected: (LocalDate) -> Unit,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    val days = remember(yearMonth, firstDayOfWeek) { daysForMonth(yearMonth, firstDayOfWeek = firstDayOfWeek) }

    val isRangeComplete = selectedDates.size >= 2
    val rangeStartDate = if (isRangeComplete) selectedDates.min() else null
    val rangeEndDate = if (isRangeComplete) selectedDates.max() else null

    val cellHorizontalPadding = LocalSpaces.current.spacing200

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LocalSpaces.current.spacing100),
    ) {
        days.chunked(DayOfWeek.entries.size).forEach { week ->
            Row(
                modifier = Modifier.drawRangeHighlight(
                    week = week,
                    rangeStartDate = rangeStartDate,
                    rangeEndDate = rangeEndDate,
                    cellHorizontalPadding = cellHorizontalPadding,
                ),
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
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = cellHorizontalPadding),
                        text = "${current.day}",
                        isCurrent = current == today,
                        isSelected = current in selectedDates,
                        isEnabled = !isBeforeMin && !isAfterMax,
                        isOutsideVisibleRange = current.yearMonth != yearMonth,
                        isInsideSelectedRange = isInRange,
                        onClick = { onDateSelected(current) },
                    )
                }
            }
        }
    }
}

@Composable
private fun Modifier.drawRangeHighlight(
    week: List<LocalDate>,
    rangeStartDate: LocalDate?,
    rangeEndDate: LocalDate?,
    cellHorizontalPadding: Dp,
): Modifier {
    if (rangeStartDate == null || rangeEndDate == null) return this

    val cellRadius = LocalRadius.current.radius200
    val highlightColor = LocalColors.current.background.bgBrandSubtle

    return drawBehind {
        val startIndex = week.indexOfFirst { it >= rangeStartDate }
        val endIndex = week.indexOfLast { it <= rangeEndDate }

        if (startIndex != -1 && endIndex != -1) {
            val cellWidth = size.width / DayOfWeek.entries.size
            val paddingPx = cellHorizontalPadding.toPx()
            val left = cellWidth * startIndex + paddingPx
            val right = cellWidth * (endIndex + 1) - paddingPx

            drawRoundRect(
                color = highlightColor,
                topLeft = Offset(left, 0f),
                size = Size(right - left, size.height),
                cornerRadius = CornerRadius(cellRadius.toPx()),
            )
        }
    }
}
