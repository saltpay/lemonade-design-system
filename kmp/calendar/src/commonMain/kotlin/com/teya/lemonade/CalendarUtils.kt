package com.teya.lemonade

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minusMonth
import kotlinx.datetime.onDay
import kotlinx.datetime.plusMonth

/**
 * Computes the number of leading cells from the previous month needed before
 * [month]'s first day, given that the calendar grid starts on [firstDayOfWeek].
 */
private fun leadingOffset(
    month: YearMonth,
    firstDayOfWeek: DayOfWeek,
): Int {
    val firstDayOrdinal = month.onDay(1).dayOfWeek.ordinal
    val startOrdinal = firstDayOfWeek.ordinal
    return (firstDayOrdinal - startOrdinal + DayOfWeek.entries.size) % DayOfWeek.entries.size
}

/**
 * Generates the full grid of [LocalDate] values for a calendar month page,
 * including leading days from the previous month and trailing days from the
 * next month so that the grid starts on [firstDayOfWeek] and fills 6 complete
 * weeks (42 cells).
 *
 * @param month The target [YearMonth].
 * @param firstDayOfWeek The day that should appear in the first column
 *   (e.g. [DayOfWeek.MONDAY] for ISO locales).
 * @return A list of exactly 42 [LocalDate] values.
 */
internal fun daysForMonth(
    month: YearMonth,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
): List<LocalDate> {
    val offset = leadingOffset(month = month, firstDayOfWeek = firstDayOfWeek)

    return buildList {
        // Previous month trailing days
        val previousMonth = month.minusMonth()
        val prevMonthLength = previousMonth.numberOfDays
        for (day in prevMonthLength - offset + 1..prevMonthLength) {
            add(previousMonth.onDay(day))
        }

        // Current month
        for (day in 1..month.numberOfDays) {
            add(month.onDay(day))
        }

        // Next month leading days
        val nextMonth = month.plusMonth()
        val remaining = CALENDAR_GRID_CELLS - size
        for (day in 1..remaining) {
            add(nextMonth.onDay(day))
        }
    }
}

/**
 * Returns the days from the previous month that appear before [month]'s first
 * day when the calendar starts on [firstDayOfWeek].
 */
internal fun peekDaysBefore(
    month: YearMonth,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
): List<LocalDate> {
    val offset = leadingOffset(month = month, firstDayOfWeek = firstDayOfWeek)

    val previousMonth = month.minusMonth()
    val prevMonthLength = previousMonth.numberOfDays

    return buildList {
        for (day in prevMonthLength - offset + 1..prevMonthLength) {
            add(previousMonth.onDay(day))
        }
    }
}

/**
 * Returns the days from the next month that appear after [month]'s last
 * day to fill the remaining grid cells.
 */
internal fun peekDaysAfter(
    month: YearMonth,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
): List<LocalDate> {
    val totalBeforeAndCurrent =
        leadingOffset(month = month, firstDayOfWeek = firstDayOfWeek) + month.numberOfDays
    val remaining = CALENDAR_GRID_CELLS - totalBeforeAndCurrent

    val nextMonth = month.plusMonth()
    return buildList {
        for (day in 1..remaining) {
            add(nextMonth.onDay(day))
        }
    }
}

/**
 * Produces an ordered list of [DayOfWeek] entries starting from [firstDayOfWeek].
 *
 * For example, if [firstDayOfWeek] is [DayOfWeek.MONDAY], the result is
 * `[MONDAY, TUESDAY, ..., SUNDAY]`.
 */
internal fun weekdayOrder(firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY): List<DayOfWeek> {
    val all = DayOfWeek.entries
    val startIndex = all.indexOf(firstDayOfWeek)
    return all.subList(startIndex, all.size) + all.subList(0, startIndex)
}

/** Number of cells in a 6-week calendar grid. */
private const val CALENDAR_GRID_CELLS = 42
