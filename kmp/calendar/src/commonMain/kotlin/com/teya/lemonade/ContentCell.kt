package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics

/**
 * Reusable calendar date cell used by both the full-month [LemonadeUi.DatePicker]
 * grid and the inline calendar strip.
 *
 * The cell renders a day number with the appropriate color/style based on its
 * selection, "today", disabled, and outside-month states. A small dot indicator
 * appears below the text when [isCurrent] is true.
 *
 * @param text The display text (typically the day-of-month number).
 * @param isCurrent Whether this cell represents today's date.
 * @param isSelected Whether this cell is the selected date.
 * @param isEnabled Whether the cell is interactive.
 * @param isOutsideVisibleRange Whether the date falls outside the displayed month.
 * @param isInsideSelectedRange Whether the cell is within a selected date range.
 * @param onClick Invoked when the cell is tapped.
 * @param modifier Optional [Modifier] applied to the root container.
 * @param contentDescription Optional accessibility label. When provided, screen readers
 *   announce this description instead of just the [text] value.
 * @param showSelectionBackground Whether to render the selection background color within
 *   this cell. When `false`, the parent is expected to provide its own selection styling
 *   (e.g. [CalendarDayCell] wraps the whole cell in a dark background). Defaults to `true`
 *   so that the [LemonadeUi.DatePicker] grid continues to show the brand-colored background.
 * @param selectionContentColor When non-null and the cell [isSelected], overrides the default
 *   text color used on selected cells.
 * @param interactionSource Interaction source for ripple/focus handling.
 */
@Composable
internal fun ContentCell(
    text: String,
    isCurrent: Boolean,
    isSelected: Boolean,
    isEnabled: Boolean,
    isOutsideVisibleRange: Boolean,
    isInsideSelectedRange: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    showSelectionBackground: Boolean = true,
    showTodayIndicator: Boolean = showSelectionBackground,
    selectionContentColor: Color? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    val textColor = contentCellTextColor(
        isEnabled = isEnabled,
        isSelected = isSelected,
        isCurrent = isCurrent,
        isInsideSelectedRange = isInsideSelectedRange,
        isOutsideVisibleRange = isOutsideVisibleRange,
        showSelectionBackground = showSelectionBackground,
        selectionContentColor = selectionContentColor,
    )

    val textStyle = if (isCurrent) {
        LocalTypographies.current.bodyMediumSemiBold
    } else {
        LocalTypographies.current.bodyMediumMedium
    }

    val backgroundColor = if (isSelected && showSelectionBackground) {
        LocalColors.current.interaction.bgBrandInteractive
    } else {
        Color.Transparent
    }

    val cellShape = LocalShapes.current.radius200

    Box(
        modifier = modifier
            .then(
                other = if (isFocused) {
                    Modifier
                        .border(
                            width = LocalBorderWidths.current.base.border50,
                            color = LocalColors.current.border.borderSelected,
                            shape = cellShape,
                        )
                } else {
                    Modifier
                },
            ).clip(
                shape = cellShape,
            ).clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                role = Role.Button,
                enabled = isEnabled,
                indication = LocalEffects.current.interactionIndication,
            ).semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
                selected = isSelected
                if (!isEnabled) {
                    disabled()
                }
            }.background(
                color = backgroundColor,
            ),
        contentAlignment = Alignment.Center,
    ) {
        LemonadeUi.Text(
            modifier = Modifier.padding(vertical = LocalSpaces.current.spacing200),
            text = text,
            textStyle = textStyle,
            color = textColor,
        )

        if (isCurrent && showTodayIndicator) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = LocalSpaces.current.spacing100)
                    .size(LocalSpaces.current.spacing100)
                    .background(
                        color = textColor,
                        shape = LocalShapes.current.radiusFull,
                    ),
            )
        }
    }
}

/**
 * Resolves the text color for a [ContentCell] based on its combined state flags.
 * Extracted from the composable body to keep cyclomatic complexity of the cell
 * itself inside detekt's threshold.
 *
 * When [selectionContentColor] is non-null and the cell [isSelected], that color
 * takes precedence over the default brand on-color.
 */
@Composable
private fun contentCellTextColor(
    isEnabled: Boolean,
    isSelected: Boolean,
    isCurrent: Boolean,
    isInsideSelectedRange: Boolean,
    isOutsideVisibleRange: Boolean,
    showSelectionBackground: Boolean,
    selectionContentColor: Color?,
): Color =
    when {
        !isEnabled -> LocalColors.current.content.contentTertiary
        isSelected && selectionContentColor != null -> selectionContentColor
        isSelected && !showSelectionBackground -> LocalColors.current.content.contentOnBrandHigh
        isSelected || isInsideSelectedRange -> LocalColors.current.content.contentOnBrandHigh
        isCurrent -> LocalColors.current.content.contentBrand
        isOutsideVisibleRange -> LocalColors.current.content.contentSecondary
        else -> LocalColors.current.content.contentPrimary
    }
