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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.ExperimentalTime

private val monthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December",
)

private val weekdayAbbreviations = listOf("S", "M", "T", "W", "T", "F", "S")

private fun formatMonth(month: Int): String = monthNames[month - 1]

@Suppress("LongMethod")
@Composable
internal fun DatePickerDisplay() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedDateFuture by remember { mutableStateOf<LocalDate?>(null) }
    var selectedDatePast by remember { mutableStateOf<LocalDate?>(null) }
    var selectedDateCustom by remember { mutableStateOf<LocalDate?>(null) }
    var rangeStart by remember { mutableStateOf<LocalDate?>(null) }
    var rangeEnd by remember { mutableStateOf<LocalDate?>(null) }
    var maxRangeStart by remember { mutableStateOf<LocalDate?>(null) }
    var maxRangeEnd by remember { mutableStateOf<LocalDate?>(null) }

    @OptIn(ExperimentalTime::class)
    val today = remember { kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault()) }

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
            LemonadeUi.DatePicker(
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                initialDate = today,
                onDateChanged = { date -> selectedDate = date },
            )
            LemonadeUi.Text(
                text = "Selected: ${selectedDate?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Future dates only (minDate: today)") {
            LemonadeUi.DatePicker(
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                minDate = today,
                onDateChanged = { date -> selectedDateFuture = date },
            )
            LemonadeUi.Text(
                text = "Selected: ${selectedDateFuture?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Past dates only (maxDate: today)") {
            LemonadeUi.DatePicker(
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                maxDate = today,
                onDateChanged = { date -> selectedDatePast = date },
            )
            LemonadeUi.Text(
                text = "Selected: ${selectedDatePast?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Custom range (minDate & maxDate)") {
            val monthNumber = today.month.number
            val customMinDate = LocalDate(today.year, monthNumber, 1)
            val customMaxDate = LocalDate(today.year, monthNumber, daysInMonth(today.year, monthNumber))
            LemonadeUi.DatePicker(
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                minDate = customMinDate,
                maxDate = customMaxDate,
                onDateChanged = { date -> selectedDateCustom = date },
            )
            LemonadeUi.Text(
                text = "Selected: ${selectedDateCustom?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Date Range Mode") {
            LemonadeUi.DateRangePicker(
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                onDateRangeChanged = { start, end ->
                    rangeStart = start
                    rangeEnd = end
                },
            )
            LemonadeUi.Text(
                text = "Range: ${rangeStart?.let { formatDate(it) }} - ${rangeEnd?.let { formatDate(it) }}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        DatePickerSection(title = "Date Range with max 7 days") {
            LemonadeUi.DateRangePicker(
                monthFormatter = ::formatMonth,
                weekdayAbbreviations = weekdayAbbreviations,
                maxRangeDays = 7,
                onDateRangeChanged = { start, end ->
                    maxRangeStart = start
                    maxRangeEnd = end
                },
            )
            LemonadeUi.Text(
                text = "Range: ${maxRangeStart?.let { formatDate(it) }} - ${maxRangeEnd?.let { formatDate(it) }}",
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

private fun daysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> 30
    }
}

private fun formatDate(date: LocalDate): String {
    return "${date.day}/${date.month.number}/${date.year}"
}
