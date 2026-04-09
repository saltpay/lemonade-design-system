@file:OptIn(ExperimentalTime::class)

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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalLemonadeComponent::class)
@Composable
internal fun InlineCalendarDisplay() {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        InlineCalendarSection(title = "Default (today selected)") {
            val state = rememberInlineCalendarState(initialDate = today)
            LemonadeUi.InlineCalendar(
                state = state,
                onDateSelected = { /* observe state.selectedDate */ },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        InlineCalendarSection(title = "With trailing content (dot on every 3rd day)") {
            val state = rememberInlineCalendarState(initialDate = today)
            LemonadeUi.InlineCalendar(
                state = state,
                onDateSelected = { /* observe state.selectedDate */ },
                enabledDates = { date -> date.day % 3 == 0 },
                trailingContent = { date, isSelected ->
                    if (date.day % 3 == 0) {
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
                },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        InlineCalendarSection(title = "Short day labels (Mon, Tue, Wed...)") {
            val state = rememberInlineCalendarState(initialDate = today)
            LemonadeUi.InlineCalendar(
                state = state,
                dayLabelFormat = DayLabelFormat.Short,
                onDateSelected = { /* observe state.selectedDate */ },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

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
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        InlineCalendarSection(title = "Compact selection (day number only)") {
            val state = rememberInlineCalendarState(initialDate = today)
            LemonadeUi.InlineCalendar(
                state = state,
                expandSelectionToLabel = false,
                onDateSelected = { /* observe state.selectedDate */ },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        InlineCalendarSection(title = "Compact selection with trailing dots") {
            val state = rememberInlineCalendarState(initialDate = today)
            LemonadeUi.InlineCalendar(
                state = state,
                expandSelectionToLabel = false,
                onDateSelected = { /* observe state.selectedDate */ },
                enabledDates = { date -> date.day % 3 == 0 },
                trailingContent = { date, isSelected ->
                    if (date.day % 3 == 0) {
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
                },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }

        InlineCalendarSection(title = "Custom selection colors") {
            val state = rememberInlineCalendarState(initialDate = today)
            LemonadeUi.InlineCalendar(
                state = state,
                selectionBackgroundColor = LemonadeTheme.colors.interaction.bgInfoInteractive,
                selectionContentColor = LemonadeTheme.colors.content.contentAlwaysLight,
                onDateSelected = { /* observe state.selectedDate */ },
            )
            LemonadeUi.Text(
                text = "Selected: ${state.selectedDate?.let { formatInlineDate(it) } ?: "none"}",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
        }
    }
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
