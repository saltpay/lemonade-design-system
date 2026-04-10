import Foundation

// MARK: - Day Label Format

/// Controls how weekday labels are displayed in calendar components.
///
/// Used by ``LemonadeUi/InlineCalendar`` to determine whether weekday headers
/// show single-letter abbreviations or short abbreviations.
public enum DayLabelFormat {
    /// Single-letter weekday labels (e.g. "S", "M", "T").
    ///
    /// Maps to `Calendar.veryShortWeekdaySymbols`.
    case narrow

    /// Short weekday labels (e.g. "Sun", "Mon", "Tue").
    ///
    /// Maps to `Calendar.shortWeekdaySymbols`.
    case short
}
