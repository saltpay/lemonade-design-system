import SwiftUI

// MARK: - Inline Calendar State

/// State holder for ``LemonadeUi/InlineCalendar(state:dayLabelFormat:onMonthDisplayed:trailingContent:)``.
///
/// Manages the currently selected date, displayed month, and navigation bounds.
/// Observe ``selectedDate`` via SwiftUI's `onChange(of:)` to react to user selections.
///
/// ## Usage
/// ```swift
/// @StateObject var state = LemonadeInlineCalendarState()
/// // React to selections:
/// .onChange(of: state.selectedDate) { newDate in
///     print("Selected: \(String(describing: newDate))")
/// }
/// ```
@MainActor
public final class LemonadeInlineCalendarState: ObservableObject {

    // MARK: - Published Properties

    /// The currently selected date, or `nil` if no date is selected.
    @Published public internal(set) var selectedDate: Date?

    /// The year and month currently displayed in the calendar header.
    ///
    /// Contains `.year` and `.month` components. Updated by the view based on
    /// scroll position - do not set this directly to navigate; use ``navigate(toMonth:)``
    /// or the arrow buttons instead.
    @Published public internal(set) var displayedMonth: DateComponents

    // MARK: - Bounds

    /// The earliest selectable date, or `nil` for no lower bound.
    public let minDate: Date?

    /// The latest selectable date, or `nil` for no upper bound.
    public let maxDate: Date?

    // MARK: - Initialization

    /// Creates a new inline calendar state.
    ///
    /// All date parameters are normalized to start-of-day to ensure consistent
    /// comparisons throughout the calendar lifecycle.
    ///
    /// - Parameters:
    ///   - initialDate: The initially selected date. Defaults to `nil`.
    ///   - initialMonth: The month to display initially. Defaults to the current month.
    ///   - minDate: The earliest selectable date. Defaults to `nil` (no lower bound).
    ///   - maxDate: The latest selectable date. Defaults to `nil` (no upper bound).
    public init(
        initialDate: Date? = nil,
        initialMonth: DateComponents? = nil,
        minDate: Date? = nil,
        maxDate: Date? = nil
    ) {
        let cal = Calendar.current
        self.selectedDate = initialDate.map { CalendarUtils.startOfDay($0, calendar: cal) }
        self.minDate = minDate.map { CalendarUtils.startOfDay($0, calendar: cal) }
        self.maxDate = maxDate.map { CalendarUtils.startOfDay($0, calendar: cal) }

        if let month = initialMonth {
            self.displayedMonth = month
        } else {
            let referenceDate = initialDate ?? Date()
            self.displayedMonth = cal.dateComponents([.year, .month], from: referenceDate)
        }
    }

    // MARK: - Selection

    /// Selects the given date.
    ///
    /// The date is normalized to start-of-day before storing.
    /// If the date falls outside `minDate`/`maxDate` bounds, the selection is ignored.
    /// The view is responsible for updating `displayedMonth` based on scroll position.
    ///
    /// - Parameters:
    ///   - date: The date to select.
    ///   - calendar: The calendar to use for normalization. Defaults to `.current`.
    public func select(date: Date, calendar: Calendar = .current) {
        let normalized = CalendarUtils.startOfDay(date, calendar: calendar)

        if let min = minDate, normalized < min {
            return
        }
        if let max = maxDate, normalized > max {
            return
        }

        selectedDate = normalized
    }

    // MARK: - Navigation

    /// Navigates to the specified month, clamped to `minDate`/`maxDate`. The
    /// equality guard prevents `objectWillChange` storms from the view's
    /// scroll-driven feedback loop.
    ///
    /// - Parameters:
    ///   - month: Date components with `.year` and `.month` set.
    ///   - calendar: The calendar to use for date arithmetic. Defaults to `.current`.
    public func navigate(toMonth month: DateComponents, calendar: Calendar = .current) {
        guard let targetDate = calendar.date(from: month) else { return }
        let targetYM = calendar.dateComponents([.year, .month], from: targetDate)

        var resolvedYM = targetYM

        if let min = minDate,
           let targetMonthDate = calendar.date(from: targetYM) {
            let minYM = calendar.dateComponents([.year, .month], from: min)
            if let minMonthDate = calendar.date(from: minYM),
               targetMonthDate < minMonthDate {
                resolvedYM = minYM
            }
        }

        if let max = maxDate,
           let resolvedDate = calendar.date(from: resolvedYM) {
            let maxYM = calendar.dateComponents([.year, .month], from: max)
            if let maxMonthDate = calendar.date(from: maxYM),
               resolvedDate > maxMonthDate {
                resolvedYM = maxYM
            }
        }

        guard resolvedYM.year != displayedMonth.year
                || resolvedYM.month != displayedMonth.month else {
            return
        }
        displayedMonth = resolvedYM
    }

    /// Navigates to the previous month.
    ///
    /// Does nothing if `canGoPrev(calendar:)` returns `false`.
    ///
    /// - Parameter calendar: The calendar to use for date arithmetic. Defaults to `.current`.
    public func navigatePreviousMonth(calendar: Calendar = .current) {
        guard canGoPrev(calendar: calendar) else { return }
        guard let current = calendar.date(from: displayedMonth),
              let prev = calendar.date(byAdding: .month, value: -1, to: current) else { return }
        displayedMonth = calendar.dateComponents([.year, .month], from: prev)
    }

    /// Navigates to the next month.
    ///
    /// Does nothing if `canGoNext(calendar:)` returns `false`.
    ///
    /// - Parameter calendar: The calendar to use for date arithmetic. Defaults to `.current`.
    public func navigateNextMonth(calendar: Calendar = .current) {
        guard canGoNext(calendar: calendar) else { return }
        guard let current = calendar.date(from: displayedMonth),
              let next = calendar.date(byAdding: .month, value: 1, to: current) else { return }
        displayedMonth = calendar.dateComponents([.year, .month], from: next)
    }

    // MARK: - Navigation Availability

    /// Whether the calendar can navigate to the previous month.
    ///
    /// Returns `false` if `minDate` is set and the previous month would be
    /// entirely before it.
    ///
    /// - Parameter calendar: The calendar to use for date arithmetic. Defaults to `.current`.
    public func canGoPrev(calendar: Calendar = .current) -> Bool {
        guard let min = minDate else { return true }
        guard let current = calendar.date(from: displayedMonth),
              let prev = calendar.date(byAdding: .month, value: -1, to: current) else { return false }
        let prevYM = calendar.dateComponents([.year, .month], from: prev)
        guard let prevYear = prevYM.year, let prevMonth = prevYM.month else { return false }
        guard let lastDay = CalendarUtils.lastDayOfMonth(year: prevYear, month: prevMonth, calendar: calendar) else {
            return false
        }
        return min <= lastDay
    }

    /// Whether the calendar can navigate to the next month.
    ///
    /// Returns `false` if `maxDate` is set and the next month would be
    /// entirely after it.
    ///
    /// - Parameter calendar: The calendar to use for date arithmetic. Defaults to `.current`.
    public func canGoNext(calendar: Calendar = .current) -> Bool {
        guard let max = maxDate else { return true }
        guard let current = calendar.date(from: displayedMonth),
              let next = calendar.date(byAdding: .month, value: 1, to: current) else { return false }
        let nextYM = calendar.dateComponents([.year, .month], from: next)
        guard let nextYear = nextYM.year, let nextMonth = nextYM.month,
              let firstDay = calendar.date(from: DateComponents(year: nextYear, month: nextMonth, day: 1)) else {
            return false
        }
        return max >= firstDay
    }
}
