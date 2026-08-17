@file:OptIn(ExperimentalTime::class)

package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.DayLabelFormat
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val EVENT_DAY_INTERVAL = 3

/**
 * The predicate the "trailing dots" samples use to decide which days carry an event.
 * Shared by `enabledDates` and `trailingContent` so the dot and the enabled state can
 * never disagree.
 */
private fun hasEvent(date: LocalDate): Boolean = date.day % EVENT_DAY_INTERVAL == 0

/**
 * First day on or after [date] that satisfies [hasEvent].
 *
 * The dot samples disable every day without an event, so seeding their state with
 * today would leave the calendar opened on a selection the user cannot re-select and
 * that renders as disabled text on the selection background. Deriving the initial
 * date from the same predicate keeps the sample valid whatever today happens to be.
 */
private fun firstEventDateOnOrAfter(date: LocalDate): LocalDate =
    generateSequence(date) { candidate -> candidate.plus(1, DateTimeUnit.DAY) }
        .first { candidate -> hasEvent(candidate) }

@Composable
internal fun InlineCalendarDisplay() {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    SampleScreenDisplayLazyColumn(title = "InlineCalendar") {
        item(key = "Default") {
            DefaultSection(today = today)
        }
        item(key = "Trailing Dots") {
            TrailingDotsSection(today = today)
        }
        item(key = "Short Labels") {
            ShortLabelsSection(today = today)
        }
        item(key = "Constrained Range") {
            ConstrainedRangeSection(today = today)
        }
        item(key = "Compact Selection") {
            CompactSelectionSection(today = today)
        }
        item(key = "Compact Dots") {
            CompactDotsSection(today = today)
        }
        item(key = "Custom Colors") {
            CustomColorsSection(today = today)
        }
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun DefaultSection(today: LocalDate) {
    InlineCalendarSection(title = "Default (today selected)") {
        val state = rememberInlineCalendarState(initialDate = today)
        LemonadeUi.InlineCalendar(
            state = state,
            onDateSelected = { /* observe state.selectedDate */ },
        )
        SelectedDateLabel(state = state)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun TrailingDotsSection(today: LocalDate) {
    InlineCalendarSection(title = "With trailing content (dot on every 3rd day)") {
        val initialDate = remember(today) { firstEventDateOnOrAfter(today) }
        val state = rememberInlineCalendarState(initialDate = initialDate)
        LemonadeUi.InlineCalendar(
            state = state,
            onDateSelected = { /* observe state.selectedDate */ },
            enabledDates = { date -> hasEvent(date) },
            trailingContent = { date, isSelected ->
                if (hasEvent(date)) {
                    EventDot(isSelected = isSelected)
                }
            },
        )
        SelectedDateLabel(state = state)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun ShortLabelsSection(today: LocalDate) {
    InlineCalendarSection(title = "Short day labels (Mon, Tue, Wed...)") {
        val state = rememberInlineCalendarState(initialDate = today)
        LemonadeUi.InlineCalendar(
            state = state,
            dayLabelFormat = DayLabelFormat.Short,
            onDateSelected = { /* observe state.selectedDate */ },
        )
        SelectedDateLabel(state = state)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun ConstrainedRangeSection(today: LocalDate) {
    InlineCalendarSection(title = "Constrained range (7 days before to 30 days after)") {
        val minDate = remember { today.plus(-7, DateTimeUnit.DAY) }
        val maxDate = remember { today.plus(30, DateTimeUnit.DAY) }
        val state = rememberInlineCalendarState(
            initialDate = today,
            minDate = minDate,
            maxDate = maxDate,
        )
        LemonadeUi.InlineCalendar(
            state = state,
            onDateSelected = { /* observe state.selectedDate */ },
        )
        LemonadeUi.Text(
            text = "Range: ${formatInlineDate(minDate)} - ${formatInlineDate(maxDate)}",
            textStyle = LemonadeTheme.typography.bodySmallRegular,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        SelectedDateLabel(state = state)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun CompactSelectionSection(today: LocalDate) {
    InlineCalendarSection(title = "Compact selection (day number only)") {
        val state = rememberInlineCalendarState(initialDate = today)
        LemonadeUi.InlineCalendar(
            state = state,
            expandSelectionToLabel = false,
            onDateSelected = { /* observe state.selectedDate */ },
        )
        SelectedDateLabel(state = state)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun CompactDotsSection(today: LocalDate) {
    InlineCalendarSection(title = "Compact selection with trailing dots") {
        val initialDate = remember(today) { firstEventDateOnOrAfter(today) }
        val state = rememberInlineCalendarState(initialDate = initialDate)
        LemonadeUi.InlineCalendar(
            state = state,
            expandSelectionToLabel = false,
            onDateSelected = { /* observe state.selectedDate */ },
            enabledDates = { date -> hasEvent(date) },
            trailingContent = { date, isSelected ->
                if (hasEvent(date)) {
                    EventDot(isSelected = isSelected)
                }
            },
        )
        SelectedDateLabel(state = state)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun CustomColorsSection(today: LocalDate) {
    InlineCalendarSection(title = "Custom selection colors") {
        val state = rememberInlineCalendarState(initialDate = today)
        LemonadeUi.InlineCalendar(
            state = state,
            selectionBackgroundColor = LemonadeTheme.colors.interaction.bgInfoInteractive,
            selectionContentColor = LemonadeTheme.colors.content.contentAlwaysLight,
            onDateSelected = { /* observe state.selectedDate */ },
        )
        SelectedDateLabel(state = state)
    }
}

@Composable
private fun EventDot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(size = 6.dp)
            .background(
                color = if (isSelected) {
                    LemonadeTheme.colors.content.contentOnBrandHigh
                } else {
                    LemonadeTheme.colors.content.contentBrand
                },
                shape = CircleShape,
            ),
    )
}

@Composable
private fun SelectedDateLabel(state: InlineCalendarState) {
    LemonadeUi.Text(
        text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
        textStyle = LemonadeTheme.typography.bodySmallRegular,
        color = LemonadeTheme.colors.content.contentSecondary,
    )
}

@Composable
private fun InlineCalendarSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing300),
        modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing600),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}

private fun formatInlineDate(date: LocalDate): String = "${date.day}/${date.month.number}/${date.year}"
