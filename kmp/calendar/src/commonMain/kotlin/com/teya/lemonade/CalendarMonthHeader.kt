package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonType
import com.teya.lemonade.core.LemonadeIcons

/**
 * Shared month header composable used by both [LemonadeUi.DatePicker] and
 * [LemonadeUi.InlineCalendar].
 *
 * Displays the current month/year label with left/right navigation arrows.
 *
 * @param headerLabel Formatted month-year string to display in the center.
 * @param canGoPrev Whether the "previous month" arrow is enabled.
 * @param canGoNext Whether the "next month" arrow is enabled.
 * @param onPrev Called when the user taps the previous-month arrow.
 * @param onNext Called when the user taps the next-month arrow.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param prevMonthContentDescription Accessibility label for the previous-month button.
 *   Defaults to the English string "Previous month". Callers should supply a localized
 *   string for proper i18n support.
 * @param nextMonthContentDescription Accessibility label for the next-month button.
 *   Defaults to the English string "Next month". Callers should supply a localized
 *   string for proper i18n support.
 */
@Composable
internal fun CalendarMonthHeader(
    headerLabel: String,
    canGoPrev: Boolean,
    canGoNext: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    prevMonthContentDescription: String = "Previous month",
    nextMonthContentDescription: String = "Next month",
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LemonadeUi.IconButton(
            icon = LemonadeIcons.ChevronLeft,
            contentDescription = prevMonthContentDescription,
            type = LemonadeButtonType.Ghost,
            enabled = canGoPrev,
            onClick = onPrev,
        )

        LemonadeUi.Text(
            text = headerLabel,
            textStyle = LocalTypographies.current.bodySmallSemiBold,
            color = LocalColors.current.content.contentPrimary,
        )

        LemonadeUi.IconButton(
            icon = LemonadeIcons.ChevronRight,
            contentDescription = nextMonthContentDescription,
            type = LemonadeButtonType.Ghost,
            enabled = canGoNext,
            onClick = onNext,
        )
    }
}
