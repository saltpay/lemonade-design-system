@file:OptIn(ExperimentalTime::class)

package com.teya.lemonade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * State holder for [LemonadeUi.InlineCalendar].
 *
 * Manages the currently selected date and navigation bounds.
 * The displayed month is derived from the scroll position by the composable
 * and is not used as a data driver.
 *
 * Create via [rememberInlineCalendarState].
 *
 * @param initialDate The initially selected date.
 * @param initialDisplayedMonth The month shown initially; defaults to the
 *   month of [initialDate] or [today].
 * @param today The date treated as "today" — used to highlight the current day in the
 *   strip and as the fallback displayed month. Defaults to the system clock in the
 *   system timezone via [rememberInlineCalendarState]. Override for tests, previews,
 *   or a non-system timezone. To change "today" at runtime, recreate the state.
 * @param minDate Minimum selectable date (inclusive).
 * @param maxDate Maximum selectable date (inclusive).
 * @param firstDayOfWeek Reserved for future use (e.g. week-start snapping). Not currently
 *   used by the inline calendar rendering - the inline calendar is a continuous day strip
 *   with no grid columns, so week-start ordering does not affect the displayed layout.
 *   Stored and persisted so the API can be extended without a breaking change.
 */
@Stable
public class InlineCalendarState internal constructor(
    initialDate: LocalDate?,
    initialDisplayedMonth: YearMonth?,
    public val today: LocalDate,
    public val minDate: LocalDate?,
    public val maxDate: LocalDate?,
    public val firstDayOfWeek: DayOfWeek,
) {
    /** The currently selected date. */
    public var selectedDate: LocalDate? by mutableStateOf(initialDate)
        internal set

    /**
     * The month currently shown in the header.
     *
     * This is a read-only observable updated by the composable based on
     * scroll position. It is NOT used as a data driver for list generation.
     */
    public var displayedMonth: YearMonth by mutableStateOf(
        initialDisplayedMonth
            ?: initialDate?.let { date ->
                YearMonth(
                    year = date.year,
                    month = date.month.number,
                )
            }
            ?: YearMonth(
                year = today.year,
                month = today.month.number,
            ),
    )
        internal set

    /**
     * Programmatic navigation target. Written only by [navigateToMonth] and
     * consumed (then cleared) by the composable. Keeping this separate from
     * [displayedMonth] prevents the scroll-driven feedback loop where an
     * internal header update triggers an unwanted scroll to the 1st.
     */
    internal var navigationTarget: YearMonth? by mutableStateOf(null)

    /**
     * Select a date. The composable handles scrolling to make it visible.
     *
     * Does not update [displayedMonth] - the composable derives the
     * displayed month from the scroll position automatically.
     */
    public fun selectDate(date: LocalDate) {
        if (minDate != null && date < minDate) return
        if (maxDate != null && date > maxDate) return
        selectedDate = date
    }

    /**
     * Navigate to a specific month without changing the selection.
     *
     * Sets [navigationTarget] which the composable observes to trigger
     * a scroll to the first day of the given month.
     */
    public fun navigateToMonth(yearMonth: YearMonth) {
        if (minDate != null && yearMonth.lastDay < minDate) return
        if (maxDate != null && yearMonth.firstDay > maxDate) return
        navigationTarget = yearMonth
    }

    internal companion object {
        /**
         * [Saver] implementation that persists the state across configuration
         * changes using simple primitives.
         */
        fun saver(
            minDate: LocalDate?,
            maxDate: LocalDate?,
            firstDayOfWeek: DayOfWeek,
        ): Saver<InlineCalendarState, *> {
            return listSaver(
                save = { state ->
                    listOf(
                        state.selectedDate?.toString().orEmpty(),
                        state.displayedMonth.year,
                        state.displayedMonth.month.number,
                        state.today.toString(),
                    )
                },
                restore = { list ->
                    val selectedStr = list[0] as String
                    val year = list[1] as Int
                    val month = list[2] as Int
                    val savedToday = LocalDate.parse(list[3] as String)
                    InlineCalendarState(
                        initialDate = selectedStr
                            .takeIf { value -> value.isNotEmpty() }
                            ?.let { value -> LocalDate.parse(value) },
                        initialDisplayedMonth = YearMonth(
                            year = year,
                            month = month,
                        ),
                        today = savedToday,
                        minDate = minDate,
                        maxDate = maxDate,
                        firstDayOfWeek = firstDayOfWeek,
                    )
                },
            )
        }
    }
}

/**
 * Creates and remembers an [InlineCalendarState] that survives configuration
 * changes via [rememberSaveable].
 *
 * @param initialDate The initially selected date.
 * @param initialDisplayedMonth The month to display initially.
 * @param today The date treated as "today". Defaults to the system clock in the
 *   system timezone. Override for tests, previews, or non-system timezones. The
 *   value is persisted across configuration changes; supplying a different value
 *   on recomposition does not retroactively change the restored state.
 * @param minDate Minimum selectable date (inclusive).
 * @param maxDate Maximum selectable date (inclusive).
 * @param firstDayOfWeek Reserved for future use (e.g. week-start snapping). Not currently
 *   used by the inline calendar rendering - the inline calendar is a continuous day strip
 *   with no grid columns, so week-start ordering does not affect the displayed layout.
 *   Stored and persisted so the API can be extended without a breaking change.
 */
@Composable
public fun rememberInlineCalendarState(
    initialDate: LocalDate? = null,
    initialDisplayedMonth: YearMonth? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
): InlineCalendarState {
    return rememberSaveable(
        saver = InlineCalendarState.saver(
            minDate = minDate,
            maxDate = maxDate,
            firstDayOfWeek = firstDayOfWeek,
        ),
    ) {
        InlineCalendarState(
            initialDate = initialDate,
            initialDisplayedMonth = initialDisplayedMonth,
            today = today,
            minDate = minDate,
            maxDate = maxDate,
            firstDayOfWeek = firstDayOfWeek,
        )
    }
}
