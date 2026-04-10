import SwiftUI

// MARK: - Accessibility Formatter

/// Shared date formatter for generating VoiceOver-friendly date labels.
///
/// Produces full date strings such as "Thursday, April 2, 2026". Defined at
/// file scope so it is allocated exactly once for the process - `DateFormatter`
/// initialization is expensive due to ObjC bridging and locale loading. Cannot
/// live as a static stored property on `CalendarDayCell` because that struct
/// is generic over `TrailingContent`, and Swift forbids static stored properties
/// on generic types.
private let calendarDayCellAccessibilityFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateStyle = .full
    return formatter
}()

// MARK: - Calendar Day Cell

/// A generic day cell that wraps ``ContentCellView`` with an optional weekday label
/// on top and an optional trailing content slot below.
///
/// Used by both ``LemonadeUi/DatePicker`` and ``LemonadeUi/InlineCalendar``
/// to render individual day cells with consistent styling.
///
/// - Parameters:
///   - TrailingContent: The type of view displayed below the day number.
struct CalendarDayCell<TrailingContent: View>: View {

    // MARK: - Properties

    let date: Date?
    let text: String
    let isCurrent: Bool
    let isSelected: Bool
    let isEnabled: Bool
    let isOutsideVisibleRange: Bool
    let isInsideSelectedRange: Bool
    let showWeekdayLabel: Bool
    let weekdayLabel: String?
    /// Controls the extent of the selection background when `showWeekdayLabel` is `true`.
    ///
    /// When `true` (default), the brand background covers the entire cell - weekday
    /// label, day number, and trailing content. When `false`, only the day number
    /// circle draws the brand background (DatePicker style), and the weekday label
    /// retains its default color.
    let expandSelectionToLabel: Bool
    let selectionBackgroundColor: Color?
    let selectionContentColor: Color?
    let onClick: () -> Void
    let trailingContent: () -> TrailingContent

    init(
        date: Date? = nil,
        text: String,
        isCurrent: Bool,
        isSelected: Bool,
        isEnabled: Bool,
        isOutsideVisibleRange: Bool,
        isInsideSelectedRange: Bool = false,
        showWeekdayLabel: Bool = true,
        weekdayLabel: String? = nil,
        expandSelectionToLabel: Bool = true,
        selectionBackgroundColor: Color? = nil,
        selectionContentColor: Color? = nil,
        onClick: @escaping () -> Void,
        @ViewBuilder trailingContent: @escaping () -> TrailingContent = { EmptyView() }
    ) {
        self.date = date
        self.text = text
        self.isCurrent = isCurrent
        self.isSelected = isSelected
        self.isEnabled = isEnabled
        self.isOutsideVisibleRange = isOutsideVisibleRange
        self.isInsideSelectedRange = isInsideSelectedRange
        self.showWeekdayLabel = showWeekdayLabel
        self.weekdayLabel = weekdayLabel
        self.expandSelectionToLabel = expandSelectionToLabel
        self.selectionBackgroundColor = selectionBackgroundColor
        self.selectionContentColor = selectionContentColor
        self.onClick = onClick
        self.trailingContent = trailingContent
    }

    /// Returns a VoiceOver-friendly label for the cell.
    ///
    /// If a `date` is provided, returns the full date string (e.g. "Thursday, April 2, 2026").
    /// Otherwise falls back to the display text (day number).
    private var cellAccessibilityLabel: String {
        if let date = date {
            return calendarDayCellAccessibilityFormatter.string(from: date)
        }
        return text
    }

    /// The text color for the weekday label, adapting to selection state.
    ///
    /// When the cell is selected and `expandSelectionToLabel` is `true`, the label
    /// switches to an inverse color so it remains legible against the dark selection
    /// background. When `expandSelectionToLabel` is `false`, the brand background
    /// only covers the day number, so the weekday label keeps its default color.
    private var weekdayLabelColor: Color {
        if isSelected && expandSelectionToLabel {
            return selectionContentColor ?? LemonadeTheme.colors.content.contentOnBrandHigh
        }
        return LemonadeTheme.colors.content.contentPrimary
    }

    var body: some View {
        VStack(spacing: 0) {
            if showWeekdayLabel, let label = weekdayLabel {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.bodyXSmallOverline,
                    color: weekdayLabelColor
                )
                .frame(maxWidth: .infinity)
            }

            // When expandSelectionToLabel is false, wrap the day number +
            // trailing content in their own selection background so the weekday
            // label stays outside the highlight.
            VStack(spacing: 0) {
                ContentCellView(
                    text: text,
                    accessibilityLabel: cellAccessibilityLabel,
                    isCurrent: isCurrent,
                    isSelected: isSelected,
                    isEnabled: isEnabled,
                    isOutsideVisibleRange: isOutsideVisibleRange,
                    isInsideSelectedRange: isInsideSelectedRange,
                    showSelectionBackground: false,
                    showTodayIndicator: false,
                    selectionContentColor: selectionContentColor,
                    onClick: onClick
                )

                trailingContent()
            }
            .padding(.vertical, !expandSelectionToLabel ? LemonadeTheme.spaces.spacing100 : 0)
            .background(
                Group {
                    if !expandSelectionToLabel && isSelected {
                        // Negative horizontal padding extends the background into the
                        // outer VStack's padding area so the compact selection matches
                        // the expanded selection width.
                        RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400)
                            .fill(selectionBackgroundColor ?? LemonadeTheme.colors.interaction.bgBrandInteractive)
                            .padding(.horizontal, -LemonadeTheme.spaces.spacing100)
                            .padding(.vertical, -LemonadeTheme.spaces.spacing100)
                    }
                }
            )
        }
        .padding(.horizontal, LemonadeTheme.spaces.spacing100)
        .padding(.vertical, LemonadeTheme.spaces.spacing100)
        .background(
            Group {
                if showWeekdayLabel && expandSelectionToLabel && isSelected {
                    RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400)
                        .fill(selectionBackgroundColor ?? LemonadeTheme.colors.interaction.bgBrandInteractive)
                }
            }
        )
    }
}
