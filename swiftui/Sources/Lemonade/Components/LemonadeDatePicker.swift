import SwiftUI

// MARK: - Public API

public extension LemonadeUi {
    /// A single-date picker component from the Lemonade Design System.
    ///
    /// Provides a swipeable month view that allows users to select a single date.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.DatePicker(
    ///     monthFormatter: { month in DateFormatter().monthSymbols[month - 1] },
    ///     weekdayAbbreviations: ["S", "M", "T", "W", "T", "F", "S"],
    ///     onDateChanged: { date in print(date) }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - monthFormatter: Formatter that returns the month name for a given month number (1-12).
    ///   - weekdayAbbreviations: Exactly 7 items representing Sunday through Saturday.
    ///   - initialDate: The initially selected date.
    ///   - onDateChanged: Called when a date is selected.
    ///   - minDate: Minimum selectable date.
    ///   - maxDate: Maximum selectable date.
    @ViewBuilder
    static func DatePicker(
        monthFormatter: @escaping (Int) -> String,
        weekdayAbbreviations: [String],
        initialDate: Date? = nil,
        onDateChanged: ((Date) -> Void)? = nil,
        minDate: Date? = nil,
        maxDate: Date? = nil
    ) -> some View {
        LemonadeDatePickerView(
            monthFormatter: monthFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: initialDate,
            onDateChanged: onDateChanged,
            minDate: minDate,
            maxDate: maxDate
        )
    }

    /// A date range picker component from the Lemonade Design System.
    ///
    /// Provides a swipeable month view that allows users to select a start and end date.
    /// The user first taps to select a start date, then taps again to select an end date.
    /// If the second tap is before the start date, the dates are swapped automatically.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.DateRangePicker(
    ///     monthFormatter: { month in DateFormatter().monthSymbols[month - 1] },
    ///     weekdayAbbreviations: ["S", "M", "T", "W", "T", "F", "S"],
    ///     onDateRangeChanged: { start, end in print("\(start) - \(end)") }
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - monthFormatter: Formatter that returns the month name for a given month number (1-12).
    ///   - weekdayAbbreviations: Exactly 7 items representing Sunday through Saturday.
    ///   - onDateRangeChanged: Called when a complete date range is selected (start and end).
    ///   - initialStartDate: Initial start date for the range.
    ///   - initialEndDate: Initial end date for the range.
    ///   - minDate: Minimum selectable date.
    ///   - maxDate: Maximum selectable date.
    ///   - maxRangeDays: Maximum number of days allowed in a date range selection.
    @ViewBuilder
    static func DateRangePicker(
        monthFormatter: @escaping (Int) -> String,
        weekdayAbbreviations: [String],
        onDateRangeChanged: ((Date, Date) -> Void)? = nil,
        initialStartDate: Date? = nil,
        initialEndDate: Date? = nil,
        minDate: Date? = nil,
        maxDate: Date? = nil,
        maxRangeDays: Int? = nil
    ) -> some View {
        LemonadeDateRangePickerView(
            monthFormatter: monthFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            onDateRangeChanged: onDateRangeChanged,
            initialStartDate: initialStartDate,
            initialEndDate: initialEndDate,
            minDate: minDate,
            maxDate: maxDate,
            maxRangeDays: maxRangeDays
        )
    }
}

// MARK: - Single Date Picker View

private struct LemonadeDatePickerView: View {
    let monthFormatter: (Int) -> String
    let weekdayAbbreviations: [String]
    let initialDate: Date?
    let onDateChanged: ((Date) -> Void)?
    let minDate: Date?
    let maxDate: Date?

    @State private var selectedDate: Date?

    init(
        monthFormatter: @escaping (Int) -> String,
        weekdayAbbreviations: [String],
        initialDate: Date?,
        onDateChanged: ((Date) -> Void)?,
        minDate: Date?,
        maxDate: Date?
    ) {
        self.monthFormatter = monthFormatter
        self.weekdayAbbreviations = weekdayAbbreviations
        self.initialDate = initialDate
        self.onDateChanged = onDateChanged
        self.minDate = minDate
        self.maxDate = maxDate
        self._selectedDate = State(initialValue: initialDate)
    }

    var body: some View {
        CoreDatePickerView(
            monthFormatter: monthFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            selectedDates: selectedDate.map { Set([$0]) } ?? [],
            onDateSelected: { date in
                selectedDate = date
                onDateChanged?(date)
            },
            minDate: minDate,
            maxDate: maxDate
        )
    }
}

// MARK: - Date Range Picker View

private struct LemonadeDateRangePickerView: View {
    let monthFormatter: (Int) -> String
    let weekdayAbbreviations: [String]
    let onDateRangeChanged: ((Date, Date) -> Void)?
    let minDate: Date?
    let maxDate: Date?
    let maxRangeDays: Int?

    @State private var rangeStartDate: Date?
    @State private var rangeEndDate: Date?

    init(
        monthFormatter: @escaping (Int) -> String,
        weekdayAbbreviations: [String],
        onDateRangeChanged: ((Date, Date) -> Void)?,
        initialStartDate: Date?,
        initialEndDate: Date?,
        minDate: Date?,
        maxDate: Date?,
        maxRangeDays: Int?
    ) {
        self.monthFormatter = monthFormatter
        self.weekdayAbbreviations = weekdayAbbreviations
        self.onDateRangeChanged = onDateRangeChanged
        self.minDate = minDate
        self.maxDate = maxDate
        self.maxRangeDays = maxRangeDays
        self._rangeStartDate = State(initialValue: initialStartDate)
        self._rangeEndDate = State(initialValue: initialEndDate)
    }

    private var isSelectingEndDate: Bool {
        rangeStartDate != nil && rangeEndDate == nil
    }

    private var effectiveMin: Date? {
        var min = minDate
        if isSelectingEndDate, let maxDays = maxRangeDays, let start = rangeStartDate {
            let rangeMin = Calendar.current.date(byAdding: .day, value: -maxDays, to: start)!
            if min == nil || rangeMin > min! {
                min = rangeMin
            }
        }
        return min
    }

    private var effectiveMax: Date? {
        var max = maxDate
        if isSelectingEndDate, let maxDays = maxRangeDays, let start = rangeStartDate {
            let rangeMax = Calendar.current.date(byAdding: .day, value: maxDays, to: start)!
            if max == nil || rangeMax < max! {
                max = rangeMax
            }
        }
        return max
    }

    var body: some View {
        CoreDatePickerView(
            monthFormatter: monthFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            selectedDates: Set([rangeStartDate, rangeEndDate].compactMap { $0 }),
            onDateSelected: { date in
                guard isSelectingEndDate, let start = rangeStartDate else {
                    rangeStartDate = date
                    rangeEndDate = nil
                    return
                }

                let newStart = min(date, start)
                let newEnd = max(date, start)
                rangeStartDate = newStart
                rangeEndDate = newEnd
                onDateRangeChanged?(newStart, newEnd)
            },
            minDate: effectiveMin,
            maxDate: effectiveMax
        )
    }
}

// MARK: - Core Date Picker View

private struct CoreDatePickerView: View {
    let monthFormatter: (Int) -> String
    let weekdayAbbreviations: [String]
    let selectedDates: Set<Date>
    let onDateSelected: (Date) -> Void
    let minDate: Date?
    let maxDate: Date?

    private let totalPages = 1200
    private var centerPage: Int { totalPages / 2 }

    @State private var displayedMonthOffset: Int = 0

    private var calendar: Calendar { Calendar.current }
    private var today: Date { DatePickerUtils.startOfDay(Date()) }

    private var currentYearMonth: DateComponents {
        let target = calendar.date(byAdding: .month, value: displayedMonthOffset, to: today)!
        return calendar.dateComponents([.year, .month], from: target)
    }

    private var headerLabel: String {
        let ym = currentYearMonth
        return "\(monthFormatter(ym.month!)) \(ym.year!)"
    }

    private var canGoPrev: Bool {
        guard let min = minDate else { return true }
        let prevMonth = calendar.date(byAdding: .month, value: displayedMonthOffset - 1, to: today)!
        let prevYM = calendar.dateComponents([.year, .month], from: prevMonth)
        let lastDayOfPrev = DatePickerUtils.lastDayOfMonth(year: prevYM.year!, month: prevYM.month!, calendar: calendar)
        return min <= lastDayOfPrev
    }

    private var canGoNext: Bool {
        guard let max = maxDate else { return true }
        let nextMonth = calendar.date(byAdding: .month, value: displayedMonthOffset + 1, to: today)!
        let nextYM = calendar.dateComponents([.year, .month], from: nextMonth)
        let firstDayOfNext = calendar.date(from: DateComponents(year: nextYM.year!, month: nextYM.month!, day: 1))!
        return max >= firstDayOfNext
    }

    var body: some View {
        VStack(spacing: 0) {
            MonthHeaderView(
                headerLabel: headerLabel,
                canGoPrev: canGoPrev,
                canGoNext: canGoNext,
                onPrev: { displayedMonthOffset -= 1 },
                onNext: { displayedMonthOffset += 1 }
            )
            .padding(.horizontal, LemonadeTheme.spaces.spacing400)

            Spacer().frame(height: LemonadeTheme.spaces.spacing200)

            HStack(spacing: 0) {
                ForEach(Array(weekdayAbbreviations.enumerated()), id: \.offset) { _, day in
                    LemonadeUi.Text(
                        day,
                        textStyle: LemonadeTypography.shared.bodyXSmallOverline,
                        color: LemonadeTheme.colors.content.contentPrimary
                    )
                    .frame(maxWidth: .infinity)
                }
            }
            .padding(.horizontal, LemonadeTheme.spaces.spacing400)

            Spacer().frame(height: LemonadeTheme.spaces.spacing100)

            TabView(selection: $displayedMonthOffset) {
                ForEach(-centerPage..<centerPage, id: \.self) { offset in
                    MonthGridView(
                        monthOffset: offset,
                        baseDate: today,
                        selectedDates: selectedDates,
                        minDate: minDate,
                        maxDate: maxDate,
                        onDateSelected: onDateSelected
                    )
                    .padding(.horizontal, LemonadeTheme.spaces.spacing400)
                    .tag(offset)
                }
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
            .frame(height: 300)
        }
    }
}

// MARK: - Month Header View

private struct MonthHeaderView: View {
    let headerLabel: String
    let canGoPrev: Bool
    let canGoNext: Bool
    let onPrev: () -> Void
    let onNext: () -> Void

    var body: some View {
        HStack {
            LemonadeUi.IconButton(
                icon: .chevronLeft,
                contentDescription: "Previous month",
                onClick: onPrev,
                enabled: canGoPrev,
                variant: .ghost
            )

            Spacer()

            LemonadeUi.Text(
                headerLabel,
                textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                color: LemonadeTheme.colors.content.contentPrimary
            )

            Spacer()

            LemonadeUi.IconButton(
                icon: .chevronRight,
                contentDescription: "Next month",
                onClick: onNext,
                enabled: canGoNext,
                variant: .ghost
            )
        }
    }
}

// MARK: - Month Grid View

private struct MonthGridView: View {
    let monthOffset: Int
    let baseDate: Date
    let selectedDates: Set<Date>
    let minDate: Date?
    let maxDate: Date?
    let onDateSelected: (Date) -> Void

    private var calendar: Calendar { Calendar.current }
    private var today: Date { DatePickerUtils.startOfDay(Date()) }

    private var yearMonth: DateComponents {
        let target = calendar.date(byAdding: .month, value: monthOffset, to: baseDate)!
        return calendar.dateComponents([.year, .month], from: target)
    }

    private var days: [Date] {
        DatePickerUtils.generateMonthDays(year: yearMonth.year!, month: yearMonth.month!, calendar: calendar)
    }

    private var normalizedSelectedDates: Set<Date> {
        Set(selectedDates.map { DatePickerUtils.startOfDay($0) })
    }

    var body: some View {
        let allDays = days
        let isRangeComplete = normalizedSelectedDates.count >= 2
        let rangeStart = isRangeComplete ? normalizedSelectedDates.min() : nil
        let rangeEnd = isRangeComplete ? normalizedSelectedDates.max() : nil

        VStack(spacing: LemonadeTheme.spaces.spacing100) {
            ForEach(0..<6, id: \.self) { weekIndex in
                let weekStart = weekIndex * 7
                let weekDays = Array(allDays[weekStart..<min(weekStart + 7, allDays.count)])

                HStack(spacing: 0) {
                    ForEach(Array(weekDays.enumerated()), id: \.offset) { _, current in
                        let normalized = DatePickerUtils.startOfDay(current)
                        let isInRange = isRangeComplete &&
                            normalized >= rangeStart! &&
                            normalized <= rangeEnd!

                        let currentMonth = calendar.component(.month, from: current)
                        let isOutsideMonth = currentMonth != yearMonth.month!

                        let isBeforeMin = minDate != nil && normalized < DatePickerUtils.startOfDay(minDate!)
                        let isAfterMax = maxDate != nil && normalized > DatePickerUtils.startOfDay(maxDate!)

                        ContentCellView(
                            text: "\(calendar.component(.day, from: current))",
                            isCurrent: normalized == today,
                            isSelected: normalizedSelectedDates.contains(normalized),
                            isEnabled: !isBeforeMin && !isAfterMax,
                            isOutsideVisibleRange: isOutsideMonth,
                            isInsideSelectedRange: isInRange,
                            onClick: { onDateSelected(normalized) }
                        )
                        .padding(.horizontal, LemonadeTheme.spaces.spacing200)
                    }
                }
                .background(
                    GeometryReader { geo in
                        if let rangeStart = rangeStart, let rangeEnd = rangeEnd {
                            let cellWidth = geo.size.width / 7
                            let padding = LemonadeTheme.spaces.spacing200
                            let radius = LemonadeTheme.radius.radius200

                            let startIdx = weekDays.firstIndex { DatePickerUtils.startOfDay($0) >= rangeStart }
                            let endIdx = weekDays.lastIndex { DatePickerUtils.startOfDay($0) <= rangeEnd }

                            if let startIdx = startIdx, let endIdx = endIdx {
                                let left = cellWidth * CGFloat(startIdx) + padding
                                let right = cellWidth * CGFloat(endIdx + 1) - padding

                                RoundedRectangle(cornerRadius: radius)
                                    .fill(LemonadeTheme.colors.background.bgBrandSubtle)
                                    .frame(width: right - left, height: geo.size.height)
                                    .offset(x: left)
                            }
                        }
                    }
                )
            }
        }
    }
}

// MARK: - Content Cell View

private struct ContentCellView: View {
    let text: String
    let isCurrent: Bool
    let isSelected: Bool
    let isEnabled: Bool
    let isOutsideVisibleRange: Bool
    let isInsideSelectedRange: Bool
    let onClick: () -> Void

    private var textColor: Color {
        if !isEnabled {
            return LemonadeTheme.colors.content.contentTertiary
        } else if isSelected || isInsideSelectedRange {
            return LemonadeTheme.colors.content.contentOnBrandHigh
        } else if isCurrent {
            return LemonadeTheme.colors.content.contentBrand
        } else if isOutsideVisibleRange {
            return LemonadeTheme.colors.content.contentSecondary
        } else {
            return LemonadeTheme.colors.content.contentPrimary
        }
    }

    private var textStyle: LemonadeTextStyle {
        isCurrent
            ? LemonadeTypography.shared.bodyMediumSemiBold
            : LemonadeTypography.shared.bodyMediumMedium
    }

    private var backgroundColor: Color {
        isSelected
            ? LemonadeTheme.colors.interaction.bgBrandInteractive
            : Color.clear
    }

    var body: some View {
        SwiftUI.Button(action: {
            if isEnabled { onClick() }
        }) {
            ZStack(alignment: .bottom) {
                LemonadeUi.Text(
                    text,
                    textStyle: textStyle,
                    color: textColor
                )
                .padding(.vertical, LemonadeTheme.spaces.spacing200)

                if isCurrent {
                    Circle()
                        .fill(textColor)
                        .frame(
                            width: LemonadeTheme.spaces.spacing100,
                            height: LemonadeTheme.spaces.spacing100
                        )
                        .padding(.bottom, LemonadeTheme.spaces.spacing100)
                }
            }
            .frame(maxWidth: .infinity)
            .background(
                RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius200)
                    .fill(backgroundColor)
            )
        }
        .buttonStyle(.plain)
        .disabled(!isEnabled)
    }
}

// MARK: - Utilities

private enum DatePickerUtils {
    static func startOfDay(_ date: Date) -> Date {
        Calendar.current.startOfDay(for: date)
    }

    static func lastDayOfMonth(year: Int, month: Int, calendar: Calendar) -> Date {
        var components = DateComponents(year: year, month: month)
        let range = calendar.range(of: .day, in: .month, for: calendar.date(from: components)!)!
        components.day = range.upperBound - 1
        return calendar.date(from: components)!
    }

    /// Generate 42 dates (6 weeks) for a given month, with leading/trailing days.
    static func generateMonthDays(year: Int, month: Int, calendar: Calendar) -> [Date] {
        let firstOfMonth = calendar.date(from: DateComponents(year: year, month: month, day: 1))!
        let weekday = calendar.component(.weekday, from: firstOfMonth) // 1=Sun, 7=Sat
        let firstDayOffset = weekday - 1 // days from Sunday

        var days: [Date] = []

        // Previous month trailing days
        for i in (0..<firstDayOffset).reversed() {
            let date = calendar.date(byAdding: .day, value: -(i + 1), to: firstOfMonth)!
            days.append(date)
        }

        // Current month days
        let range = calendar.range(of: .day, in: .month, for: firstOfMonth)!
        for day in range {
            let date = calendar.date(from: DateComponents(year: year, month: month, day: day))!
            days.append(date)
        }

        // Next month leading days
        var nextDay = 1
        let nextMonthStart = calendar.date(byAdding: .month, value: 1, to: firstOfMonth)!
        while days.count < 42 {
            let date = calendar.date(byAdding: .day, value: nextDay - 1, to: nextMonthStart)!
            days.append(date)
            nextDay += 1
        }

        return days
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeDatePicker_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(spacing: 32) {
                LemonadeUi.DatePicker(
                    monthFormatter: { month in
                        DateFormatter().monthSymbols[month - 1]
                    },
                    weekdayAbbreviations: ["S", "M", "T", "W", "T", "F", "S"]
                )

                LemonadeUi.DateRangePicker(
                    monthFormatter: { month in
                        DateFormatter().monthSymbols[month - 1]
                    },
                    weekdayAbbreviations: ["S", "M", "T", "W", "T", "F", "S"]
                )
            }
            .padding()
        }
        .previewLayout(.sizeThatFits)
    }
}
#endif
