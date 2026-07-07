package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val monthNames = listOf(
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

private val weekdayAbbreviations = listOf("S", "M", "T", "W", "T", "F", "S")

private fun formatMonth(month: Int): String = monthNames[month - 1]

@Suppress("LongMethod")
@Composable
internal fun DatePickerDisplay() {
    @OptIn(ExperimentalTime::class)
    val today = remember {
        Clock.System
            .todayIn(TimeZone.currentSystemDefault())
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        DatePickerSection(title = "Default (all dates selectable)") {
            val state = rememberDatePickerState(initialDate = today)
            LemonadeUi.DatePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Future dates only (minDate: today)") {
            val state = rememberDatePickerState(minDate = today)
            LemonadeUi.DatePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Past dates only (maxDate: today)") {
            val state = rememberDatePickerState(maxDate = today)
            LemonadeUi.DatePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Custom range (minDate & maxDate)") {
            val monthNumber = today.month.number
            val customMinDate = LocalDate(today.year, monthNumber, 1)
            val customMaxDate = LocalDate(today.year, monthNumber, daysInMonth(today.year, monthNumber))
            val state = rememberDatePickerState(
                minDate = customMinDate,
                maxDate = customMaxDate,
            )
            LemonadeUi.DatePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Dynamic disabled dates (fetched per visible month)") {
            val state = rememberDatePickerState(initialDate = today)
            var currentMonth by remember { mutableStateOf(YearMonth(today.year, today.month.number)) }

            // Simulated per-month "sparse" API: every 3rd, 8th, 14th, 21st and 27th of any month
            // comes back as disabled. Swap for a repository call in real usage.
            LaunchedEffect(currentMonth) {
                delay(FAKE_FETCH_DELAY_MS)
                state.disabledDates = disabledDatesFor(currentMonth)
            }

            LemonadeUi.DatePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                onMonthDisplayed = { yearMonth -> currentMonth = yearMonth },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatDate(it) }} — " +
                    "disabled this month: ${state.disabledDates.size}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Date Range Mode") {
            val state = rememberDateRangePickerState()
            LemonadeUi.DateRangePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
            )
            LemonadeUi.Text(
                text = "Range: ${state.selectedStartDate?.let { formatDate(it) }} - " +
                    "${state.selectedEndDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Date Range with max 7 days") {
            val state = rememberDateRangePickerState(maxRangeDays = 7)
            LemonadeUi.DateRangePicker(
                state = state,
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
            )
            LemonadeUi.Text(
                text = "Range: ${state.selectedStartDate?.let { formatDate(it) }} - " +
                    "${state.selectedEndDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }
    }
}

@Composable
private fun DatePickerSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}

private fun daysInMonth(
    year: Int,
    month: Int,
): Int =
    when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> 30
    }

private fun formatDate(date: LocalDate): String = "${date.day}/${date.month.number}/${date.year}"

private const val FAKE_FETCH_DELAY_MS = 200L
private val FAKE_DISABLED_DAY_OFFSETS = intArrayOf(2, 7, 13, 20, 26)

private fun disabledDatesFor(yearMonth: YearMonth): Set<LocalDate> {
    val firstOfMonth = LocalDate(yearMonth.year, yearMonth.month.number, 1)
    return FAKE_DISABLED_DAY_OFFSETS
        .map { offset -> firstOfMonth.plus(offset, DateTimeUnit.DAY) }
        .filter { it.month == yearMonth.month }
        .toSet()
}
