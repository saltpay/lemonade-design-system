@file:OptIn(ExperimentalTime::class)

package com.teya.lemonade

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.teya.lemonade.core.DayLabelFormat
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.onDay
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TOTAL_DAYS = 3651
private const val CENTER_INDEX = TOTAL_DAYS / 2

private const val DEFAULT_VISIBLE_CELLS = 7
private const val LARGE_FONT_SCALE_VISIBLE_CELLS = 5
private const val LARGE_FONT_SCALE_THRESHOLD = 1.3f

/**
 * Default English weekday labels ordered Monday through Sunday.
 *
 * These are provided as a fallback; callers should supply localized labels
 * via the [weekdayLabels] parameter for proper i18n support.
 */
private val DEFAULT_WEEKDAY_LABELS_NARROW: List<String> =
    listOf("M", "T", "W", "T", "F", "S", "S")

/**
 * Default English full weekday names ordered Monday through Sunday.
 *
 * Used as the accessibility fallback when [LemonadeUi.InlineCalendar] is called
 * without [weekdayAccessibilityLabels]. Callers should supply a localized list for
 * proper i18n support.
 */
private val DEFAULT_WEEKDAY_FULL_NAMES: List<String> = listOf(
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
)

private val DEFAULT_WEEKDAY_LABELS_SHORT: List<String> =
    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/**
 * Default English month names indexed 0-11 (January through December).
 *
 * Provided as a fallback; callers should supply a localized [monthFormatter]
 * for proper i18n support.
 */
private val DEFAULT_MONTH_NAMES: List<String> = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
)

/**
 * An inline calendar strip from the Lemonade Design System.
 *
 * Renders a horizontally scrollable row of day cells using index-based virtual
 * scrolling over a fixed range of [TOTAL_DAYS] days (approximately +/-5 years
 * from today). The list never regenerates - the displayed month is derived
 * purely from the current scroll position, eliminating snap-back glitches.
 *
 * ## Localization
 * By default the component uses English labels. For localized UIs, provide
 * [weekdayLabels] and [monthFormatter] matching the user's locale, similar to
 * the pattern used by [DatePicker].
 *
 * ## Usage
 * ```kotlin
 * val state = rememberInlineCalendarState(initialDate = today)
 * LemonadeUi.InlineCalendar(
 *     state = state,
 *     weekdayLabels = listOf("L", "M", "M", "J", "V", "S", "D"), // French
 *     monthFormatter = { month -> frenchMonthNames[month - 1] },
 *     onDateSelected = { date -> /* handle selection */ },
 * )
 * // Observe: state.selectedDate
 * ```
 *
 * @param state State holder created via [rememberInlineCalendarState].
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param dayLabelFormat Controls whether weekday labels are [DayLabelFormat.Narrow] (single char)
 *   or [DayLabelFormat.Short] (3 chars). Only used when [weekdayLabels] is not provided.
 * @param weekdayLabels Optional list of exactly 7 localized weekday abbreviations ordered
 *   Monday through Sunday. When provided, [dayLabelFormat] is ignored. Callers should
 *   provide this for proper localization.
 * @param monthFormatter Optional formatter returning a localized month name for a given
 *   month number (1-12). Used in the header label. Defaults to English month names.
 * @param onDateSelected Optional callback invoked when the user taps a date.
 * @param onMonthDisplayed Optional callback invoked when the displayed month changes.
 * @param enabledDates Optional predicate that controls which dates are interactive.
 *   When provided, a date is enabled only when this predicate returns `true` **and**
 *   the date falls within the [InlineCalendarState.minDate]..[InlineCalendarState.maxDate]
 *   range (if set). This is useful, for example, to disable dates that have no associated
 *   content.
 * @param prevMonthContentDescription Accessibility label for the previous-month navigation
 *   button. Defaults to the English string "Previous month". Callers should supply a
 *   localized string for proper i18n support.
 * @param nextMonthContentDescription Accessibility label for the next-month navigation
 *   button. Defaults to the English string "Next month". Callers should supply a localized
 *   string for proper i18n support.
 * @param weekdayAccessibilityLabels Optional list of exactly 7 full weekday names ordered
 *   Monday through Sunday, used only by screen readers (not as visual labels). When `null`,
 *   defaults to English full names from [DEFAULT_WEEKDAY_FULL_NAMES]. Callers should
 *   supply a localized list for proper i18n support.
 * @param expandSelectionToLabel When `true` (default), the selection background covers the
 *   weekday label, day number, and trailing content. When `false`, only the day number
 *   circle carries the brand background (DatePicker style).
 * @param selectionBackgroundColor When non-null, overrides the default brand background color
 *   for selected cells.
 * @param selectionContentColor When non-null, overrides the default text color on selected cells.
 * @param trailingContent Optional composable rendered below each day cell (e.g. event dots).
 *   Receives the [LocalDate] for the cell and a [Boolean] indicating whether that date is
 *   currently selected.
 */
@ExperimentalLemonadeComponent
@Composable
public fun LemonadeUi.InlineCalendar(
    state: InlineCalendarState,
    modifier: Modifier = Modifier,
    dayLabelFormat: DayLabelFormat = DayLabelFormat.Narrow,
    weekdayLabels: List<String>? = null,
    monthFormatter: ((month: Int) -> String)? = null,
    onDateSelected: ((LocalDate) -> Unit)? = null,
    onMonthDisplayed: ((YearMonth) -> Unit)? = null,
    enabledDates: ((LocalDate) -> Boolean)? = null,
    prevMonthContentDescription: String = "Previous month",
    nextMonthContentDescription: String = "Next month",
    weekdayAccessibilityLabels: List<String>? = null,
    expandSelectionToLabel: Boolean = true,
    selectionBackgroundColor: Color? = null,
    selectionContentColor: Color? = null,
    trailingContent: @Composable ((LocalDate, Boolean) -> Unit)? = null,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    val anchorDate = remember { today }

    val density = LocalDensity.current
    val visibleCells = remember(density.fontScale) {
        if (density.fontScale > LARGE_FONT_SCALE_THRESHOLD) {
            LARGE_FONT_SCALE_VISIBLE_CELLS
        } else {
            DEFAULT_VISIBLE_CELLS
        }
    }

    val resolvedWeekdayLabels = remember(weekdayLabels, dayLabelFormat) {
        resolveWeekdayLabels(weekdayLabels, dayLabelFormat)
    }

    val resolvedMonthFormatter: (Int) -> String = remember(monthFormatter) {
        monthFormatter ?: { month -> DEFAULT_MONTH_NAMES[month - 1] }
    }

    val resolvedAccessibilityLabels = remember(weekdayAccessibilityLabels) {
        weekdayAccessibilityLabels ?: DEFAULT_WEEKDAY_FULL_NAMES
    }

    // Clamp the scrollable range to minDate/maxDate when set, with visibleCells
    // of padding so the user can see a bit of context beyond the boundary.
    // selectedDate is intentionally excluded from the key: changing it must not
    // recalculate firstIndex, which would invalidate all index-to-date mappings
    // and desync listState from the virtual index space.
    val indexRange = remember(state.minDate, state.maxDate, anchorDate, visibleCells) {
        calculateIndexRange(
            minDate = state.minDate,
            maxDate = state.maxDate,
            anchorDate = anchorDate,
            rangePadding = visibleCells,
        )
    }
    val firstIndex = indexRange.firstIndex
    val itemCount = indexRange.itemCount

    // initialIndex positions the list so today (or center) is roughly visible
    // before the LaunchedEffect(Unit) performs the exact centered scroll.
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = indexRange.initialIndex,
    )

    // Guards the selected-date and navigateToMonth effects from competing with
    // the initial non-animated centering scroll in LaunchedEffect(Unit).
    val initialScrollDone = remember { mutableStateOf(false) }

    // Header month: derived from the item at the center of the visible viewport.
    // Uses layoutInfo to find the actual center pixel, not an approximation.
    val headerMonth by remember {
        derivedStateOf {
            deriveHeaderMonth(
                listState = listState,
                firstIndex = firstIndex,
                visibleCells = visibleCells,
                anchorDate = anchorDate,
            )
        }
    }

    val haptic = LocalHapticFeedback.current

    // True when the current headerMonth change was triggered by a day tap rather
    // than a scroll gesture. The onClick handler sets this flag before calling
    // selectDate() so the LaunchedEffect below can skip its haptic and avoid
    // firing a second feedback for cross-month taps.
    val selectionTriggeredMonthChange = remember { BoolRef(false) }

    // LaunchedEffect(headerMonth) fires once on initial composition with the
    // starting month value. We need to swallow that first emission so mounting
    // the component does not trigger a haptic (noticeable when several
    // calendars appear on the same screen).
    val hasEmittedInitialMonth = remember { BoolRef(false) }

    // Sync displayedMonth on the state so external observers can read it.
    // Haptic tick on month boundary crossing gives tactile scroll feedback,
    // but only when the change was driven by a scroll - not a day tap (which
    // already fired its own haptic in onClick) and not the initial composition.
    LaunchedEffect(headerMonth) {
        state.displayedMonth = headerMonth
        onMonthDisplayed?.invoke(headerMonth)
        when {
            !hasEmittedInitialMonth.value -> {
                hasEmittedInitialMonth.value = true
            }
            selectionTriggeredMonthChange.value -> {
                selectionTriggeredMonthChange.value = false
            }
            else -> {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        }
    }

    // Scroll to a specific date, centering it in the viewport.
    val coroutineScope = rememberCoroutineScope()

    val scrollToDate: (LocalDate) -> Unit = { date ->
        val targetIndex = (dateToGlobalIndex(date, anchorDate) - firstIndex)
            .coerceIn(0, itemCount - 1)
        coroutineScope.launch {
            listState.scrollToCenteredIndex(targetIndex, visibleCells, animate = true)
        }
        Unit
    }

    // Center on initial position without animation. Sets initialScrollDone when
    // complete so subsequent effects know the viewport is ready.
    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo.viewportSize.width }
            .filter { width -> width > 0 }
            .first()
        val targetDate = state.selectedDate ?: today
        val targetIndex = (dateToGlobalIndex(targetDate, anchorDate) - firstIndex)
            .coerceIn(0, itemCount - 1)
        listState.scrollToCenteredIndex(targetIndex = targetIndex, visibleCells = visibleCells, animate = false)
        initialScrollDone.value = true
    }

    // Scroll to newly selected date with animation. Guarded by initialScrollDone
    // to prevent a competing animated scroll during initial composition.
    LaunchedEffect(state.selectedDate) {
        if (!initialScrollDone.value) return@LaunchedEffect
        val targetDate = state.selectedDate ?: return@LaunchedEffect
        val targetIndex = (dateToGlobalIndex(targetDate, anchorDate) - firstIndex)
            .coerceIn(0, itemCount - 1)
        snapshotFlow { listState.layoutInfo.viewportSize.width }
            .filter { width -> width > 0 }
            .first()
        listState.scrollToCenteredIndex(targetIndex = targetIndex, visibleCells = visibleCells, animate = true)
    }

    // Responds to external navigateToMonth() calls by observing the dedicated
    // navigationTarget on the state. Clears the target after scrolling so
    // the same month can be navigated to again.
    LaunchedEffect(state.navigationTarget) {
        val target = state.navigationTarget
            ?: return@LaunchedEffect
        scrollToDate(target.onDay(1))
        state.navigationTarget = null
    }

    Column(modifier = modifier) {
        CalendarMonthHeader(
            modifier = Modifier.fillMaxWidth(),
            headerLabel = buildHeaderLabel(headerMonth, resolvedMonthFormatter),
            canGoPrev = canNavigatePrev(headerMonth, state.minDate),
            canGoNext = canNavigateNext(headerMonth, state.maxDate),
            prevMonthContentDescription = prevMonthContentDescription,
            nextMonthContentDescription = nextMonthContentDescription,
            onPrev = {
                val firstOfPrev = headerMonth.minus(1, DateTimeUnit.MONTH).onDay(1)
                scrollToDate(firstOfPrev)
            },
            onNext = {
                val firstOfNext = headerMonth.plus(1, DateTimeUnit.MONTH).onDay(1)
                scrollToDate(firstOfNext)
            },
        )

        Spacer(modifier = Modifier.height(LocalSpaces.current.spacing200))

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val cellWidth: Dp = maxWidth / visibleCells

            LazyRow(
                state = listState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        collectionInfo = CollectionInfo(rowCount = 1, columnCount = itemCount)
                    },
            ) {
                items(
                    count = itemCount,
                    key = { it + firstIndex },
                ) { localIndex ->
                    val globalIndex = localIndex + firstIndex
                    val date = remember(globalIndex) {
                        globalIndexToDate(globalIndex, anchorDate)
                    }
                    InlineCalendarDayItem(
                        date = date,
                        today = today,
                        cellWidth = cellWidth,
                        state = state,
                        headerMonth = headerMonth,
                        enabledDates = enabledDates,
                        resolvedWeekdayLabels = resolvedWeekdayLabels,
                        resolvedAccessibilityLabels = resolvedAccessibilityLabels,
                        resolvedMonthFormatter = resolvedMonthFormatter,
                        expandSelectionToLabel = expandSelectionToLabel,
                        selectionBackgroundColor = selectionBackgroundColor,
                        selectionContentColor = selectionContentColor,
                        trailingContent = trailingContent,
                        onDateTap = { tappedDate ->
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (tappedDate.yearMonth != headerMonth) {
                                selectionTriggeredMonthChange.value = true
                            }
                            state.selectDate(tappedDate)
                            onDateSelected?.invoke(tappedDate)
                        },
                    )
                }
            }
        }
    }
}

/**
 * Resolves a weekday label from the caller-provided [weekdayLabels] list.
 *
 * The list is ordered Monday (index 0) through Sunday (index 6), matching
 * [DayOfWeek] ordinal values.
 */
private fun resolveWeekdayLabel(
    dayOfWeek: DayOfWeek,
    weekdayLabels: List<String>,
): String = weekdayLabels[dayOfWeek.ordinal]

/**
 * Builds a "Month Year" header string from a [YearMonth] using the provided
 * [monthFormatter] for localized month names.
 */
private fun buildHeaderLabel(
    yearMonth: YearMonth,
    monthFormatter: (Int) -> String,
): String {
    val monthName = monthFormatter(yearMonth.month.number)
    return "$monthName ${yearMonth.year}"
}

/**
 * Builds an accessibility content description for a calendar day cell.
 *
 * Format: "Weekday, Month Day, Year" (e.g. "Monday, April 14, 2025").
 * Uses the caller-provided labels so accessibility reads match the visual labels.
 */
private fun buildCellContentDescription(
    date: LocalDate,
    weekdayLabels: List<String>,
    monthFormatter: (Int) -> String,
): String {
    val weekday = weekdayLabels[date.dayOfWeek.ordinal]
    val month = monthFormatter(date.month.number)
    return "$weekday, $month ${date.day}, ${date.year}"
}

/**
 * Pure predicate that combines the state's `minDate`/`maxDate` bounds with the
 * caller-provided [enabledDates] closure. Lives outside the main composable so
 * its branching does not count toward the composable's cyclomatic complexity.
 */
private fun isDateEnabled(
    date: LocalDate,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    enabledDates: ((LocalDate) -> Boolean)?,
): Boolean {
    val inRange = (minDate == null || date >= minDate) &&
        (maxDate == null || date <= maxDate)
    return inRange && enabledDates?.invoke(date) != false
}

/**
 * Converts a global virtual index (0..[TOTAL_DAYS]) to a [LocalDate] using the
 * caller-provided [anchorDate] pinned at [CENTER_INDEX].
 */
private fun globalIndexToDate(
    globalIndex: Int,
    anchorDate: LocalDate,
): LocalDate {
    val daysFromAnchor = (globalIndex - CENTER_INDEX).toLong()
    return anchorDate.plus(daysFromAnchor, DateTimeUnit.DAY)
}

/**
 * Inverse of [globalIndexToDate]: returns the global virtual index for [date].
 */
private fun dateToGlobalIndex(
    date: LocalDate,
    anchorDate: LocalDate,
): Int = CENTER_INDEX + anchorDate.daysUntil(date)

/**
 * Resolves the effective weekday labels from the caller-provided list or a
 * format-based English fallback.
 */
private fun resolveWeekdayLabels(
    weekdayLabels: List<String>?,
    dayLabelFormat: DayLabelFormat,
): List<String> =
    weekdayLabels ?: when (dayLabelFormat) {
        DayLabelFormat.Narrow -> DEFAULT_WEEKDAY_LABELS_NARROW
        DayLabelFormat.Short -> DEFAULT_WEEKDAY_LABELS_SHORT
    }

/**
 * Bounded index range and initial scroll offset derived from the calendar
 * state's `minDate`/`maxDate`/`selectedDate`.
 */
private data class CalendarIndexRange(
    val firstIndex: Int,
    val lastIndex: Int,
    val itemCount: Int,
    val initialIndex: Int,
)

/**
 * Computes the clamped virtual index window. The initial scroll position is
 * intentionally fixed to [CENTER_INDEX] so that [rememberLazyListState] always
 * receives a stable value - the exact centering is handled by the
 * `LaunchedEffect(Unit)` in the composable after the viewport is measured.
 *
 * [selectedDate] is NOT a parameter here by design: including it would cause
 * [firstIndex] to shift whenever the user taps a date, which would desync
 * all index-to-date mappings while [LazyListState] retains its old position.
 */
private fun calculateIndexRange(
    minDate: LocalDate?,
    maxDate: LocalDate?,
    anchorDate: LocalDate,
    rangePadding: Int,
): CalendarIndexRange {
    val firstIndex = minDate
        ?.let { (dateToGlobalIndex(it, anchorDate) - rangePadding).coerceAtLeast(0) }
        ?: 0
    val lastIndex = maxDate
        ?.let { (dateToGlobalIndex(it, anchorDate) + rangePadding).coerceAtMost(TOTAL_DAYS - 1) }
        ?: TOTAL_DAYS - 1
    val itemCount = lastIndex - firstIndex + 1
    val initialIndex = (CENTER_INDEX - firstIndex).coerceIn(0, itemCount - 1)
    return CalendarIndexRange(
        firstIndex = firstIndex,
        lastIndex = lastIndex,
        itemCount = itemCount,
        initialIndex = initialIndex,
    )
}

/**
 * Whether the chevron can navigate backward from [headerMonth] given [minDate].
 */
private fun canNavigatePrev(
    headerMonth: YearMonth,
    minDate: LocalDate?,
): Boolean = minDate?.yearMonth?.let { headerMonth > it } ?: true

/**
 * Whether the chevron can navigate forward from [headerMonth] given [maxDate].
 */
private fun canNavigateNext(
    headerMonth: YearMonth,
    maxDate: LocalDate?,
): Boolean = maxDate?.yearMonth?.let { headerMonth < it } ?: true

/**
 * Returns the [YearMonth] that should currently appear in the header label.
 *
 * The "current" month is the one owning the cell whose center is closest to
 * the viewport center, matching the Compose reference. Falls back to
 * `firstVisibleItemIndex + visibleCells / 2` when `layoutInfo` has not emitted
 * visible items yet (e.g. during the initial frame).
 */
private fun deriveHeaderMonth(
    listState: LazyListState,
    firstIndex: Int,
    visibleCells: Int,
    anchorDate: LocalDate,
): YearMonth {
    val layoutInfo = listState.layoutInfo
    val viewportCenter = layoutInfo.viewportStartOffset +
        (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
    val centerItem = layoutInfo.visibleItemsInfo.minByOrNull {
        kotlin.math.abs(it.offset + it.size / 2 - viewportCenter)
    }
    val localIndex = centerItem?.index ?: listState.firstVisibleItemIndex + visibleCells / 2
    val globalIndex = (localIndex + firstIndex).coerceIn(0, TOTAL_DAYS - 1)
    val date = globalIndexToDate(globalIndex, anchorDate)
    return YearMonth(date.year, date.month.number)
}

/**
 * Scrolls a [LazyListState] so the cell at [targetIndex] sits in the horizontal
 * center of the viewport. Shared between the initial-position effect, the
 * selection-scroll effect, and the chevron handler so none of them has to
 * recompute the viewport-relative offset inline.
 */
private suspend fun LazyListState.scrollToCenteredIndex(
    targetIndex: Int,
    visibleCells: Int,
    animate: Boolean,
) {
    val viewportWidth = layoutInfo.viewportSize.width
    if (viewportWidth <= 0) return
    val cellWidthPx = viewportWidth / visibleCells
    val scrollOffset = -(viewportWidth - cellWidthPx) / 2
    if (animate) {
        animateScrollToItem(targetIndex, scrollOffset)
    } else {
        scrollToItem(targetIndex, scrollOffset)
    }
}

/**
 * A single day cell inside the inline calendar strip.
 *
 * Extracted from the main composable body so its own branching (enabled
 * predicate, selection state, cross-month detection) does not inflate the
 * parent's cyclomatic complexity. Callers pass pre-resolved
 * [resolvedWeekdayLabels], [resolvedAccessibilityLabels], and [resolvedMonthFormatter] so
 * the item does not re-resolve them on every recomposition.
 */
@Composable
@Suppress("LongParameterList")
private fun InlineCalendarDayItem(
    date: LocalDate,
    today: LocalDate,
    cellWidth: Dp,
    state: InlineCalendarState,
    headerMonth: YearMonth,
    enabledDates: ((LocalDate) -> Boolean)?,
    resolvedWeekdayLabels: List<String>,
    resolvedAccessibilityLabels: List<String>,
    resolvedMonthFormatter: (Int) -> String,
    expandSelectionToLabel: Boolean,
    selectionBackgroundColor: Color?,
    selectionContentColor: Color?,
    trailingContent: @Composable ((LocalDate, Boolean) -> Unit)?,
    onDateTap: (LocalDate) -> Unit,
) {
    val isInHeaderMonth = date.yearMonth == headerMonth

    val isEnabled = remember(date, state.minDate, state.maxDate, enabledDates) {
        isDateEnabled(
            date = date,
            minDate = state.minDate,
            maxDate = state.maxDate,
            enabledDates = enabledDates,
        )
    }

    val weekdayLabel = remember(date, resolvedWeekdayLabels) {
        resolveWeekdayLabel(date.dayOfWeek, resolvedWeekdayLabels)
    }

    val accessibilityDescription = remember(
        date,
        resolvedAccessibilityLabels,
        resolvedMonthFormatter,
    ) {
        buildCellContentDescription(
            date = date,
            weekdayLabels = resolvedAccessibilityLabels,
            monthFormatter = resolvedMonthFormatter,
        )
    }

    val isSelected = date == state.selectedDate
    CalendarDayCell(
        modifier = Modifier.width(cellWidth),
        text = "${date.day}",
        contentDescription = accessibilityDescription,
        isCurrent = date == today,
        isSelected = isSelected,
        isEnabled = isEnabled,
        isOutsideVisibleRange = !isInHeaderMonth,
        isInsideSelectedRange = false,
        showWeekdayLabel = true,
        weekdayLabel = weekdayLabel,
        expandSelectionToLabel = expandSelectionToLabel,
        selectionBackgroundColor = selectionBackgroundColor,
        selectionContentColor = selectionContentColor,
        onClick = { onDateTap(date) },
        trailingContent = trailingContent?.let { content ->
            {
                content(date, isSelected)
            }
        },
    )
}
