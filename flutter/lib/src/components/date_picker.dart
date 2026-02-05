import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Callback type for formatting month headers in [LemonadeDatePicker].
///
/// Receives the [year] and [month] (1-12) and returns a formatted string.
typedef MonthHeaderFormatter = String Function(int year, int month);

/// Callback for date range changes.
///
/// Called when both start and end dates have been selected in range mode.
typedef DateRangeChangedCallback =
    void Function(
      DateTime startDate,
      DateTime endDate,
    );

/// {@template LemonadeDatePicker}
/// A date picker widget from the Lemonade Design System.
///
/// A [LemonadeDatePicker] provides a scrollable month view that allows users
/// to select a date. It features smooth page animations between months and
/// supports restricting selection to a specific date range using [minDate]
/// and [maxDate].
///
/// The picker supports two modes:
/// - **Single date mode** (default): Select a single date
/// - **Date range mode**: Select a start and end date
///
/// ## Example - Single Date Mode
/// ```dart
/// DateTime? selectedDate;
///
/// LemonadeDatePicker(
///   monthHeaderFormatter: (year, month) {
///     const months = ['January', 'February', 'March', 'April', 'May', 'June',
///       'July', 'August', 'September', 'October', 'November', 'December'];
///     return '${months[month - 1]} $year';
///   },
///   weekdayAbbreviations: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
///   initialDate: DateTime.now(),
///   onDateChanged: (DateTime newDate) {
///     setState(() {
///       selectedDate = newDate;
///     });
///   },
/// )
/// ```
///
/// ## Example - Future Dates Only
/// ```dart
/// LemonadeDatePicker(
///   monthHeaderFormatter: (year, month) => '...',
///   weekdayAbbreviations: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
///   minDate: DateTime.now(),
///   onDateChanged: (date) => print(date),
/// )
/// ```
///
/// ## Example - Date Range Mode
/// ```dart
/// LemonadeDatePicker(
///   monthHeaderFormatter: (year, month) => '...',
///   weekdayAbbreviations: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
///   isDateRange: true,
///   initialStartDate: DateTime.now().subtract(Duration(days: 7)),
///   initialEndDate: DateTime.now(),
///   onDateRangeChanged: (start, end) => print('$start - $end'),
/// )
/// ```
///
/// See also:
/// - [LemonadeTheme], which provides the design tokens
/// - [LemonadeThemeData], for theme configuration
/// {@endtemplate}
class LemonadeDatePicker extends StatefulWidget {
  /// {@macro LemonadeDatePicker}
  const LemonadeDatePicker({
    required this.monthHeaderFormatter,
    required this.weekdayAbbreviations,
    super.key,
    this.initialDate,
    this.onDateChanged,
    this.minDate,
    this.maxDate,
    this.semanticIdentifier,
    this.semanticLabel,
    this.isDateRange = false,
    this.onDateRangeChanged,
    this.initialStartDate,
    this.initialEndDate,
  });

  /// {@template LemonadeDatePicker.initialDate}
  /// The initially selected date.
  ///
  /// If null, no date is pre-selected. If [minDate] is set and [initialDate]
  /// is before it, the picker will start at [minDate]'s month.
  ///
  /// This is used in single date mode. For date range mode, use
  /// [initialStartDate] and [initialEndDate].
  /// {@endtemplate}
  final DateTime? initialDate;

  /// {@template LemonadeDatePicker.onDateChanged}
  /// Called when the user selects a date.
  ///
  /// The date picker passes the selected [DateTime] to the callback.
  /// This is used in single date mode.
  /// {@endtemplate}
  final ValueChanged<DateTime>? onDateChanged;

  /// {@template LemonadeDatePicker.minDate}
  /// The minimum selectable date.
  ///
  /// Dates before this date are displayed with reduced opacity and cannot
  /// be tapped. If null, there is no minimum date restriction.
  /// {@endtemplate}
  final DateTime? minDate;

  /// {@template LemonadeDatePicker.maxDate}
  /// The maximum selectable date.
  ///
  /// Dates after this date are displayed with reduced opacity and cannot
  /// be tapped. If null, there is no maximum date restriction.
  /// {@endtemplate}
  final DateTime? maxDate;

  /// {@template LemonadeDatePicker.monthHeaderFormatter}
  /// Formatter for the month header.
  ///
  /// Receives the year and month (1-12) and should return a formatted string.
  /// Example: `(year, month) => '${monthNames[month - 1]} $year'`
  /// {@endtemplate}
  final MonthHeaderFormatter monthHeaderFormatter;

  /// {@template LemonadeDatePicker.weekdayAbbreviations}
  /// Weekday abbreviations starting from Sunday.
  ///
  /// Must contain exactly 7 items representing Sunday through Saturday.
  /// Example: `['S', 'M', 'T', 'W', 'T', 'F', 'S']`
  /// {@endtemplate}
  final List<String> weekdayAbbreviations;

  /// {@template LemonadeDatePicker.semanticIdentifier}
  /// An identifier for the date picker used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeDatePicker.semanticLabel}
  /// A label for the date picker used for accessibility purposes.
  /// {@endtemplate}
  final String? semanticLabel;

  /// {@template LemonadeDatePicker.isDateRange}
  /// Whether the picker is in date range selection mode.
  ///
  /// When true, the user can select a start and end date. The selection
  /// follows this pattern:
  /// 1. First tap sets the start date
  /// 2. Second tap sets the end date (auto-swaps if before start)
  /// 3. Third tap resets and starts a new range selection
  ///
  /// Defaults to false (single date mode).
  /// {@endtemplate}
  final bool isDateRange;

  /// {@template LemonadeDatePicker.onDateRangeChanged}
  /// Called when the user completes a date range selection.
  ///
  /// This callback is only invoked after both start and end dates have been
  /// selected. The start date is always before or equal to the end date.
  ///
  /// Only used when [isDateRange] is true.
  /// {@endtemplate}
  final DateRangeChangedCallback? onDateRangeChanged;

  /// {@template LemonadeDatePicker.initialStartDate}
  /// The initial start date for date range mode.
  ///
  /// Only used when [isDateRange] is true.
  /// {@endtemplate}
  final DateTime? initialStartDate;

  /// {@template LemonadeDatePicker.initialEndDate}
  /// The initial end date for date range mode.
  ///
  /// Only used when [isDateRange] is true.
  /// {@endtemplate}
  final DateTime? initialEndDate;

  @override
  State<LemonadeDatePicker> createState() => _LemonadeDatePickerState();
}

class _LemonadeDatePickerState extends State<LemonadeDatePicker> {
  static const int _monthsTotal = 120; // finite range (~10 years)
  late final int _centerPageIndex;
  late final PageController _pageController;

  late final DateTime _baseMonth;
  late DateTime? _selectedDate;
  late int _currentPageIndex;

  late final DateTime _today; // today at midnight
  late final DateTime? _effectiveMinDate;
  late final DateTime? _effectiveMaxDate;

  // Date range state
  DateTime? _rangeStartDate;
  DateTime? _rangeEndDate;
  bool _isSelectingEndDate = false;

  @override
  void initState() {
    super.initState();

    final now = DateTime.now();
    _today = DateTime(now.year, now.month, now.day);

    _effectiveMinDate = widget.minDate;
    _effectiveMaxDate = widget.maxDate;

    DateTime effectiveInitial;

    if (widget.isDateRange) {
      // In range mode, use start date or today
      effectiveInitial = widget.initialStartDate ?? _today;
      _rangeStartDate = widget.initialStartDate;
      _rangeEndDate = widget.initialEndDate;
      // If both dates are provided, we're not in selecting mode
      _isSelectingEndDate =
          widget.initialStartDate != null && widget.initialEndDate == null;
      _selectedDate = null;
    } else {
      effectiveInitial = widget.initialDate ?? _today;
      _selectedDate = widget.initialDate;
    }

    // Clamp initial date to min/max bounds
    final minDate = _effectiveMinDate;
    final maxDate = _effectiveMaxDate;
    if (minDate != null && effectiveInitial.isBefore(minDate)) {
      effectiveInitial = minDate;
    }
    if (maxDate != null && effectiveInitial.isAfter(maxDate)) {
      effectiveInitial = maxDate;
    }

    _baseMonth = DateTime(effectiveInitial.year, effectiveInitial.month);
    _centerPageIndex = _monthsTotal ~/ 2;
    _currentPageIndex = _centerPageIndex;
    _pageController = PageController(initialPage: _centerPageIndex);
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  DateTime _monthFromPage(int index) {
    final monthOffset = index - _centerPageIndex;
    return DateTime(_baseMonth.year, _baseMonth.month + monthOffset);
  }

  String _formatMonthHeader(DateTime date) =>
      widget.monthHeaderFormatter(date.year, date.month);

  bool _canGoToPreviousMonth() {
    if (_currentPageIndex <= 0) return false;
    final minDate = _effectiveMinDate;
    if (minDate == null) return true;

    // We only allow going to a month that has at least one day >= minDate
    final prevMonth = _monthFromPage(_currentPageIndex - 1);
    final lastDayPrevMonth = DateTime(
      prevMonth.year,
      prevMonth.month + 1,
      0,
    );

    return !lastDayPrevMonth.isBefore(minDate);
  }

  void _goToPreviousMonth() {
    if (!_canGoToPreviousMonth()) return;
    _pageController.previousPage(
      duration: const Duration(milliseconds: 200),
      curve: Curves.easeOut,
    );
  }

  bool _canGoToNextMonth() {
    if (_currentPageIndex >= _monthsTotal - 1) return false;
    final maxDate = _effectiveMaxDate;
    if (maxDate == null) return true;

    // We only allow going to a month that has at least one day <= maxDate
    final nextMonth = _monthFromPage(_currentPageIndex + 1);
    final firstDayNextMonth = DateTime(nextMonth.year, nextMonth.month);

    return !firstDayNextMonth.isAfter(maxDate);
  }

  void _goToNextMonth() {
    if (!_canGoToNextMonth()) return;
    _pageController.nextPage(
      duration: const Duration(milliseconds: 200),
      curve: Curves.easeOut,
    );
  }

  void _handleDateSelected(DateTime date) {
    if (widget.isDateRange) {
      _handleRangeDateSelected(date);
    } else {
      setState(() => _selectedDate = date);
      widget.onDateChanged?.call(date);
    }
  }

  void _handleRangeDateSelected(DateTime date) {
    setState(() {
      if (!_isSelectingEndDate) {
        // First tap: set start date, clear end date
        _rangeStartDate = date;
        _rangeEndDate = null;
        _isSelectingEndDate = true;
      } else {
        // Second tap: set end date
        if (date.isBefore(_rangeStartDate!)) {
          // Auto-swap if end is before start
          _rangeEndDate = _rangeStartDate;
          _rangeStartDate = date;
        } else {
          _rangeEndDate = date;
        }
        _isSelectingEndDate = false;

        // Fire callback with completed range
        widget.onDateRangeChanged?.call(_rangeStartDate!, _rangeEndDate!);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final spaces = theme.spaces;

    final baseTextStyle = theme.typography.bodySmallMedium.copyWith(
      color: theme.colors.content.contentPrimary,
    );

    final visibleMonth = _monthFromPage(_currentPageIndex);
    final headerLabel = _formatMonthHeader(visibleMonth);

    final canGoPrev = _canGoToPreviousMonth();
    final canGoNext = _canGoToNextMonth();

    return Semantics(
      identifier: widget.semanticIdentifier,
      label: widget.semanticLabel,
      child: SizedBox(
        height: 364, // overall component height: can stay as a fixed value
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            // Header with chevrons
            Padding(
              padding: EdgeInsets.only(
                top: spaces.spacing200,
                left: spaces.spacing400,
                right: spaces.spacing400,
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Semantics(
                    button: true,
                    identifier: 'previous_month',
                    child: GestureDetector(
                      onTap: canGoPrev ? _goToPreviousMonth : null,
                      behavior: HitTestBehavior.opaque,
                      child: Padding(
                        padding: EdgeInsets.all(spaces.spacing200),
                        child: LemonadeIcon(
                          icon: LemonadeIcons.chevronLeft,
                          color: canGoPrev
                              ? theme.colors.content.contentPrimary
                              : theme.colors.content.contentTertiary,
                        ),
                      ),
                    ),
                  ),
                  Text(
                    headerLabel,
                    style: theme.typography.bodySmallSemibold.copyWith(
                      color: theme.colors.content.contentPrimary,
                    ),
                  ),
                  Semantics(
                    button: true,
                    identifier: 'next_month',
                    child: GestureDetector(
                      onTap: canGoNext ? _goToNextMonth : null,
                      behavior: HitTestBehavior.opaque,
                      child: Padding(
                        padding: EdgeInsets.all(spaces.spacing200),
                        child: LemonadeIcon(
                          icon: LemonadeIcons.chevronRight,
                          color: canGoNext
                              ? theme.colors.content.contentPrimary
                              : theme.colors.content.contentTertiary,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),

            SizedBox(height: spaces.spacing200),

            // Weekday initials
            Padding(
              padding: EdgeInsets.symmetric(horizontal: spaces.spacing400),
              child: Row(
                children: widget.weekdayAbbreviations.map((String day) {
                  return Expanded(
                    child: Center(
                      child: Text(
                        day,
                        style: theme.typography.bodyXSmallOverline.copyWith(
                          color: theme.colors.content.contentSecondary,
                        ),
                      ),
                    ),
                  );
                }).toList(),
              ),
            ),

            SizedBox(height: spaces.spacing100),

            // Month pages (only grid scrolls)
            Expanded(
              child: PageView.builder(
                controller: _pageController,
                itemCount: _monthsTotal,
                physics: const PageScrollPhysics(),
                onPageChanged: (int index) {
                  setState(() {
                    _currentPageIndex = index;
                  });
                },
                itemBuilder: (BuildContext context, int index) {
                  final month = _monthFromPage(index);
                  return _MonthGrid(
                    visibleMonth: month,
                    selectedDate: _selectedDate,
                    theme: theme,
                    baseTextStyle: baseTextStyle,
                    minDate: _effectiveMinDate,
                    maxDate: _effectiveMaxDate,
                    onDateSelected: _handleDateSelected,
                    isDateRange: widget.isDateRange,
                    rangeStartDate: _rangeStartDate,
                    rangeEndDate: _rangeEndDate,
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _MonthGrid extends StatelessWidget {
  const _MonthGrid({
    required this.visibleMonth,
    required this.selectedDate,
    required this.theme,
    required this.baseTextStyle,
    required this.onDateSelected,
    required this.minDate,
    required this.maxDate,
    required this.isDateRange,
    required this.rangeStartDate,
    required this.rangeEndDate,
  });

  final DateTime visibleMonth;
  final DateTime? selectedDate;
  final LemonadeThemeData theme;
  final TextStyle baseTextStyle;
  final ValueChanged<DateTime> onDateSelected;
  final DateTime? minDate;
  final DateTime? maxDate;
  final bool isDateRange;
  final DateTime? rangeStartDate;
  final DateTime? rangeEndDate;

  static bool _sameDay(DateTime a, DateTime b) =>
      a.year == b.year && a.month == b.month && a.day == b.day;

  @override
  Widget build(BuildContext context) {
    final spaces = theme.spaces;

    final daysInMonth = DateTime(
      visibleMonth.year,
      visibleMonth.month + 1,
      0,
    ).day;
    final firstWeekday = DateTime(
      visibleMonth.year,
      visibleMonth.month,
    ).weekday;
    final offset = firstWeekday % 7;

    // Calculate range boundaries for empty cell backgrounds
    final firstDayOfMonth = DateTime(visibleMonth.year, visibleMonth.month);
    final lastDayOfMonth = DateTime(
      visibleMonth.year,
      visibleMonth.month,
      daysInMonth,
    );
    final isRangeComplete =
        isDateRange && rangeStartDate != null && rangeEndDate != null;
    // Check if range extends before/after this month
    final rangeExtendsBefore =
        isRangeComplete && rangeStartDate!.isBefore(firstDayOfMonth);
    final rangeExtendsAfter =
        isRangeComplete && rangeEndDate!.isAfter(lastDayOfMonth);

    final rows = <Widget>[];
    var day = 1;

    while (day <= daysInMonth) {
      final weekRow = <Widget>[];

      for (var i = 0; i < 7; i++) {
        final isLeadingEmpty = rows.isEmpty && i < offset;
        final isTrailingEmpty = day > daysInMonth;

        if (isLeadingEmpty || isTrailingEmpty) {
          // Check if empty cell should show range background
          final showEmptyRangeBg =
              (isLeadingEmpty &&
                  rangeExtendsBefore &&
                  rangeEndDate!.isAfter(
                    firstDayOfMonth.subtract(const Duration(days: 1)),
                  )) ||
              (isTrailingEmpty &&
                  rangeExtendsAfter &&
                  rangeStartDate!.isBefore(
                    lastDayOfMonth.add(const Duration(days: 1)),
                  ));

          weekRow.add(
            Expanded(
              child: _EmptyDayCell(
                theme: theme,
                showRangeBackground: showEmptyRangeBg,
                isFirstInRow: i == 0,
                isLastInRow: i == 6,
              ),
            ),
          );
        } else {
          final current = DateTime(
            visibleMonth.year,
            visibleMonth.month,
            day,
          );

          // Determine selection state
          final isSelected =
              !isDateRange &&
              selectedDate != null &&
              _sameDay(current, selectedDate!);
          final isBeforeMin = minDate != null && current.isBefore(minDate!);
          final isAfterMax = maxDate != null && current.isAfter(maxDate!);
          final isDisabled = isBeforeMin || isAfterMax;

          // Range states - only show range visuals when both dates selected
          final isRangeComplete =
              isDateRange && rangeStartDate != null && rangeEndDate != null;
          final isRangeStart =
              isRangeComplete && _sameDay(current, rangeStartDate!);
          final isRangeEnd =
              isRangeComplete && _sameDay(current, rangeEndDate!);
          final isInRange =
              isRangeComplete &&
              current.isAfter(rangeStartDate!) &&
              current.isBefore(rangeEndDate!);
          // Show circle for start date even before range is complete
          final showStartCircle =
              isDateRange &&
              rangeStartDate != null &&
              _sameDay(current, rangeStartDate!);

          // Determine position in week for range styling
          final isFirstInRow = i == 0;
          final isLastInRow = i == 6;

          weekRow.add(
            Expanded(
              child: _DayCell(
                date: current,
                isSelected: isSelected,
                isDisabled: isDisabled,
                baseTextStyle: baseTextStyle,
                theme: theme,
                onTap: isDisabled ? null : () => onDateSelected(current),
                isRangeStart: isRangeStart,
                isRangeEnd: isRangeEnd,
                isInRange: isInRange,
                isFirstInRow: isFirstInRow,
                isLastInRow: isLastInRow,
                showStartCircle: showStartCircle,
              ),
            ),
          );
          day++;
        }
      }

      rows.add(
        Padding(
          padding: EdgeInsets.symmetric(vertical: spaces.spacing50),
          child: Row(children: weekRow),
        ),
      );
    }

    return Padding(
      padding: EdgeInsets.symmetric(horizontal: spaces.spacing400),
      child: Column(children: rows),
    );
  }
}

class _DayCell extends StatelessWidget {
  const _DayCell({
    required this.date,
    required this.isSelected,
    required this.isDisabled,
    required this.baseTextStyle,
    required this.theme,
    required this.onTap,
    this.isRangeStart = false,
    this.isRangeEnd = false,
    this.isInRange = false,
    this.isFirstInRow = false,
    this.isLastInRow = false,
    this.showStartCircle = false,
  });

  final DateTime date;
  final bool isSelected;
  final bool isDisabled;
  final TextStyle baseTextStyle;
  final LemonadeThemeData theme;
  final VoidCallback? onTap;
  final bool isRangeStart;
  final bool isRangeEnd;
  final bool isInRange;
  final bool isFirstInRow;
  final bool isLastInRow;
  final bool showStartCircle;

  @override
  Widget build(BuildContext context) {
    final spaces = theme.spaces;
    final now = DateTime.now();
    final isToday =
        date.year == now.year && date.month == now.month && date.day == now.day;

    // Determine if this is a range endpoint (start or end) with complete range
    final isRangeEndpoint = isRangeStart || isRangeEnd;
    // Show circle for: single selection, range endpoints, or pending start
    final showCircle = isSelected || isRangeEndpoint || showStartCircle;

    final Color textColor;
    if (isDisabled) {
      textColor = theme.colors.content.contentTertiary;
    } else if (showCircle || isToday) {
      textColor = theme.colors.content.contentOnBrandHigh;
    } else {
      textColor = theme.colors.content.contentPrimary;
    }

    // Calculate background decorations for range mode - only when range complete
    final Widget child = Stack(
      alignment: Alignment.center,
      children: <Widget>[
        // In-range background - only shown when both dates are selected
        if (isInRange || isRangeStart || isRangeEnd)
          _buildRangeBackground(spaces),
        // Selected circle for endpoints
        if (showCircle)
          Positioned.fill(
            child: Center(
              child: DecoratedBox(
                decoration: BoxDecoration(
                  color: theme.colors.background.bgBrand,
                  borderRadius: BorderRadius.circular(spaces.spacing300),
                ),
                child: SizedBox(
                  height: spaces.spacing1000,
                  width: spaces.spacing1000,
                ),
              ),
            ),
          ),
        Text(
          '${date.day}',
          style: baseTextStyle.copyWith(color: textColor),
        ),
        if (isToday)
          Positioned(
            bottom: spaces.spacing50, // 2
            child: DecoratedBox(
              decoration: BoxDecoration(
                color: theme.colors.content.contentOnBrandHigh,
                shape: BoxShape.circle,
              ),
              child: SizedBox(
                width: spaces.spacing100, // 4
                height: spaces.spacing100, // 4
              ),
            ),
          ),
      ],
    );

    return GestureDetector(
      onTap: onTap,
      behavior: HitTestBehavior.opaque,
      child: SizedBox(
        height: spaces.spacing1000, // 40
        child: child,
      ),
    );
  }

  Widget _buildRangeBackground(LemonadeSpaces spaces) {
    final bgColor = theme.colors.background.bgBrandSubtle;

    // Same-day selection: no background needed, just the circle
    if (isRangeStart && isRangeEnd) {
      return const SizedBox.shrink();
    }

    // For in-range cells: full background with rounded corners at row edges
    // For start: background on right half only (unless last in row)
    // For end: background on left half only (unless first in row)

    if (isInRange) {
      // Full background for in-between dates, with rounded corners at row edges
      return Positioned.fill(
        child: Container(
          decoration: BoxDecoration(
            color: bgColor,
            borderRadius: BorderRadius.horizontal(
              left: isFirstInRow
                  ? Radius.circular(spaces.spacing300)
                  : Radius.zero,
              right: isLastInRow
                  ? Radius.circular(spaces.spacing300)
                  : Radius.zero,
            ),
          ),
        ),
      );
    }

    // For start/end, show half background
    // Don't show trailing background if at row edge (range continues on next/prev row)
    final showLeftBg = isRangeEnd && !isFirstInRow;
    final showRightBg = isRangeStart && !isLastInRow;

    return Positioned.fill(
      child: Row(
        children: [
          // Left half
          Expanded(
            child: Container(
              color: showLeftBg ? bgColor : null,
            ),
          ),
          // Right half
          Expanded(
            child: Container(
              color: showRightBg ? bgColor : null,
            ),
          ),
        ],
      ),
    );
  }
}

class _EmptyDayCell extends StatelessWidget {
  const _EmptyDayCell({
    required this.theme,
    required this.showRangeBackground,
    required this.isFirstInRow,
    required this.isLastInRow,
  });

  final LemonadeThemeData theme;
  final bool showRangeBackground;
  final bool isFirstInRow;
  final bool isLastInRow;

  @override
  Widget build(BuildContext context) {
    final spaces = theme.spaces;

    if (!showRangeBackground) {
      return SizedBox(height: spaces.spacing1000);
    }

    return SizedBox(
      height: spaces.spacing1000,
      child: Container(
        decoration: BoxDecoration(
          color: theme.colors.background.bgBrandSubtle,
          borderRadius: BorderRadius.horizontal(
            left: isFirstInRow
                ? Radius.circular(spaces.spacing300)
                : Radius.zero,
            right: isLastInRow
                ? Radius.circular(spaces.spacing300)
                : Radius.zero,
          ),
        ),
      ),
    );
  }
}
