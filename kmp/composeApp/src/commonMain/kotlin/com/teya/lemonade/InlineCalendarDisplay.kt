package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.DayLabelFormat
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.datetime.plus

// Pinned so screenshot diffs of this screen aren't flaky as the system date advances.
private val DEMO_TODAY = LocalDate(year = 2025, month = 1, day = 15)

@Composable
internal fun InlineCalendarDisplay() {
    val today = DEMO_TODAY

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        DefaultSection(today = today)
        TrailingDotsSection(today = today)
        ShortLabelsSection(today = today)
        ConstrainedRangeSection(today = today)
        CompactSelectionSection(today = today)
        CompactDotsSection(today = today)
        CustomColorsSection(today = today)
    }
}

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
private fun DefaultSection(today: LocalDate) {
    InlineCalendarSection(title = "Default (today selected)") {
        val state = rememberInlineCalendarState(
            today = today,
            initialDate = today,
        )
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
        val state = rememberInlineCalendarState(
            today = today,
            initialDate = today,
        )
        LemonadeUi.InlineCalendar(
            state = state,
            onDateSelected = { /* observe state.selectedDate */ },
            enabledDates = { date -> date.day % 3 == 0 },
            trailingContent = { date, isSelected ->
                if (date.day % 3 == 0) {
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
        val state = rememberInlineCalendarState(
            today = today,
            initialDate = today,
        )
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
            today = today,
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
        val state = rememberInlineCalendarState(
            today = today,
            initialDate = today,
        )
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
        val state = rememberInlineCalendarState(
            today = today,
            initialDate = today,
        )
        LemonadeUi.InlineCalendar(
            state = state,
            expandSelectionToLabel = false,
            onDateSelected = { /* observe state.selectedDate */ },
            enabledDates = { date -> date.day % 3 == 0 },
            trailingContent = { date, isSelected ->
                if (date.day % 3 == 0) {
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
        val state = rememberInlineCalendarState(
            today = today,
            initialDate = today,
        )
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

private fun formatInlineDate(date: LocalDate): String = "${date.day}/${date.month.number}/${date.year}"
