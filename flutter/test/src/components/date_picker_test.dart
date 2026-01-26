import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  const weekdayAbbreviations = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
  const monthNames = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
  ];

  String monthHeaderFormatter(int year, int month) =>
      '${monthNames[month - 1]} $year';

  group('LemonadeDatePicker', () {
    testWidgets('renders with required parameters', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      expect(find.byType(LemonadeDatePicker), findsOneWidget);
    });

    testWidgets('displays weekday abbreviations', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      expect(find.text('S'), findsNWidgets(2)); // Sunday and Saturday
      expect(find.text('M'), findsOneWidget);
      expect(find.text('T'), findsNWidgets(2)); // Tuesday and Thursday
      expect(find.text('W'), findsOneWidget);
      expect(find.text('F'), findsOneWidget);
    });

    testWidgets('displays month header', (tester) async {
      final today = DateTime.now();
      final expectedHeader = monthHeaderFormatter(today.year, today.month);

      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      expect(find.text(expectedHeader), findsOneWidget);
    });

    testWidgets('displays initial date month when provided', (tester) async {
      final initialDate = DateTime(2025, 6, 15);
      const expectedHeader = 'June 2025';

      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
          initialDate: initialDate,
        ),
      );

      expect(find.text(expectedHeader), findsOneWidget);
    });

    testWidgets('calls onDateChanged when date is selected', (tester) async {
      DateTime? selectedDate;
      final today = DateTime.now();

      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
          onDateChanged: (date) {
            selectedDate = date;
          },
        ),
      );

      // Tap on today's date
      await tester.tap(find.text('${today.day}').first);
      await tester.pump();

      expect(selectedDate, isNotNull);
      expect(selectedDate!.day, equals(today.day));
    });

    testWidgets('navigates to next month when right chevron is tapped', (
      tester,
    ) async {
      final today = DateTime.now();
      final nextMonth = DateTime(today.year, today.month + 1);
      final expectedHeader = monthHeaderFormatter(
        nextMonth.year,
        nextMonth.month,
      );

      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      // Find and tap right chevron
      final rightChevron = find.byWidgetPredicate(
        (widget) =>
            widget is LemonadeIcon && widget.icon == LemonadeIcons.chevronRight,
      );
      await tester.tap(rightChevron);
      await tester.pumpAndSettle();

      expect(find.text(expectedHeader), findsOneWidget);
    });

    testWidgets('navigates to previous month when left chevron is tapped', (
      tester,
    ) async {
      // Start with a future month to ensure we can go back
      final startDate = DateTime(2025, 12, 15);
      final previousMonth = DateTime(2025, 11);
      final expectedHeader = monthHeaderFormatter(
        previousMonth.year,
        previousMonth.month,
      );

      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
          initialDate: startDate,
        ),
      );

      // Find and tap left chevron
      final leftChevron = find.byWidgetPredicate(
        (widget) =>
            widget is LemonadeIcon && widget.icon == LemonadeIcons.chevronLeft,
      );
      await tester.tap(leftChevron);
      await tester.pumpAndSettle();

      expect(find.text(expectedHeader), findsOneWidget);
    });

    group('allowBeforeToday', () {
      testWidgets('allows selecting past dates when true', (tester) async {
        DateTime? selectedDate;
        final pastDate = DateTime(2020, 1, 15);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: pastDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Tap on day 1
        await tester.tap(find.text('1').first);
        await tester.pump();

        expect(selectedDate, isNotNull);
      });

      testWidgets('prevents selecting past dates when false', (tester) async {
        DateTime? selectedDate;
        // Use a date far in the future to have selectable days
        final futureDate = DateTime(2030, 6, 15);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: futureDate,
            allowBeforeToday: false,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Day 1 should be in the past relative to today
        // Since we're viewing June 2030, all days should be selectable
        await tester.tap(find.text('1').first);
        await tester.pump();

        expect(selectedDate, isNotNull);
      });
    });

    group('allowAfterToday', () {
      testWidgets('allows selecting future dates when true', (tester) async {
        DateTime? selectedDate;
        final futureDate = DateTime(2030, 6, 15);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: futureDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Tap on day 20
        await tester.tap(find.text('20').first);
        await tester.pump();

        expect(selectedDate, isNotNull);
      });

      testWidgets(
        'allows selecting past dates when allowAfterToday is false',
        (tester) async {
          DateTime? selectedDate;
          // Use a date in the past
          final pastDate = DateTime(2020, 1, 15);

          await tester.pumpLemonadeWidget(
            LemonadeDatePicker(
              monthHeaderFormatter: monthHeaderFormatter,
              weekdayAbbreviations: weekdayAbbreviations,
              initialDate: pastDate,
              allowAfterToday: false,
              onDateChanged: (date) {
                selectedDate = date;
              },
            ),
          );

          // Since we're viewing January 2020, all days should be in the past
          // and selectable when allowAfterToday is false
          await tester.tap(find.text('10').first);
          await tester.pump();

          expect(selectedDate, isNotNull);
        },
      );

      testWidgets(
        'does not show future month when allowAfterToday is false',
        (tester) async {
          final futureDate = DateTime(2030, 6, 15);

          await tester.pumpLemonadeWidget(
            LemonadeDatePicker(
              monthHeaderFormatter: monthHeaderFormatter,
              weekdayAbbreviations: weekdayAbbreviations,
              initialDate: futureDate,
              allowAfterToday: false,
            ),
          );

          // Should not show June 2030 (the provided initialDate)
          expect(find.text('June 2030'), findsNothing);
        },
      );

      testWidgets(
        'disables next month navigation when allowAfterToday is false',
        (tester) async {
          await tester.pumpLemonadeWidget(
            LemonadeDatePicker(
              monthHeaderFormatter: monthHeaderFormatter,
              weekdayAbbreviations: weekdayAbbreviations,
              allowAfterToday: false,
            ),
          );

          // Capture current header before tapping
          final headerFinder = find.byWidgetPredicate(
            (widget) =>
                widget is Text &&
                widget.data != null &&
                widget.data!.contains('202'),
          );
          final headerBefore =
              tester.widget<Text>(headerFinder.first).data;

          // Try to navigate to next month
          final rightChevron = find.byWidgetPredicate(
            (widget) =>
                widget is LemonadeIcon &&
                widget.icon == LemonadeIcons.chevronRight,
          );
          await tester.tap(rightChevron);
          await tester.pumpAndSettle();

          // Header should remain unchanged (navigation disabled)
          final headerAfter =
              tester.widget<Text>(headerFinder.first).data;
          expect(headerAfter, equals(headerBefore));
        },
      );
    });

    testWidgets('displays chevron navigation icons', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      expect(
        find.byWidgetPredicate(
          (widget) =>
              widget is LemonadeIcon &&
              widget.icon == LemonadeIcons.chevronLeft,
        ),
        findsOneWidget,
      );
      expect(
        find.byWidgetPredicate(
          (widget) =>
              widget is LemonadeIcon &&
              widget.icon == LemonadeIcons.chevronRight,
        ),
        findsOneWidget,
      );
    });

    testWidgets('uses PageView for month navigation', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      expect(find.byType(PageView), findsOneWidget);
    });

    group('date range mode', () {
      testWidgets('renders in date range mode', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
          ),
        );

        expect(find.byType(LemonadeDatePicker), findsOneWidget);
      });

      testWidgets('first tap selects start date', (tester) async {
        DateTime? startDate;
        DateTime? endDate;

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialDate: DateTime(2025, 6, 15),
            onDateRangeChanged: (start, end) {
              startDate = start;
              endDate = end;
            },
          ),
        );

        // First tap - should not trigger callback yet
        await tester.tap(find.text('10').first);
        await tester.pump();

        // Callback should not be called after first tap
        expect(startDate, isNull);
        expect(endDate, isNull);
      });

      testWidgets('second tap completes range and calls callback', (
        tester,
      ) async {
        DateTime? startDate;
        DateTime? endDate;

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialDate: DateTime(2025, 6, 15),
            onDateRangeChanged: (start, end) {
              startDate = start;
              endDate = end;
            },
          ),
        );

        // First tap - select start
        await tester.tap(find.text('10').first);
        await tester.pump();

        // Second tap - select end
        await tester.tap(find.text('20').first);
        await tester.pump();

        // Callback should be called with correct dates
        expect(startDate, isNotNull);
        expect(endDate, isNotNull);
        expect(startDate!.day, equals(10));
        expect(endDate!.day, equals(20));
      });

      testWidgets('auto-swaps when end date is before start date', (
        tester,
      ) async {
        DateTime? startDate;
        DateTime? endDate;

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialDate: DateTime(2025, 6, 15),
            onDateRangeChanged: (start, end) {
              startDate = start;
              endDate = end;
            },
          ),
        );

        // First tap - select day 20 as "start"
        await tester.tap(find.text('20').first);
        await tester.pump();

        // Second tap - select day 10 (before start)
        await tester.tap(find.text('10').first);
        await tester.pump();

        // Should auto-swap: start=10, end=20
        expect(startDate, isNotNull);
        expect(endDate, isNotNull);
        expect(startDate!.day, equals(10));
        expect(endDate!.day, equals(20));
      });

      testWidgets('third tap resets and starts new range', (tester) async {
        var callCount = 0;
        DateTime? lastStartDate;
        DateTime? lastEndDate;

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialDate: DateTime(2025, 6, 15),
            onDateRangeChanged: (start, end) {
              callCount++;
              lastStartDate = start;
              lastEndDate = end;
            },
          ),
        );

        // First range: tap 5, then 10
        await tester.tap(find.text('5').first);
        await tester.pump();
        await tester.tap(find.text('10').first);
        await tester.pump();

        expect(callCount, equals(1));
        expect(lastStartDate!.day, equals(5));
        expect(lastEndDate!.day, equals(10));

        // Third tap starts new range (day 15)
        await tester.tap(find.text('15').first);
        await tester.pump();

        // Callback not called yet (only start selected)
        expect(callCount, equals(1));

        // Fourth tap completes new range (day 25)
        await tester.tap(find.text('25').first);
        await tester.pump();

        expect(callCount, equals(2));
        expect(lastStartDate!.day, equals(15));
        expect(lastEndDate!.day, equals(25));
      });

      testWidgets('same date for start and end works', (tester) async {
        DateTime? startDate;
        DateTime? endDate;

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialDate: DateTime(2025, 6, 15),
            onDateRangeChanged: (start, end) {
              startDate = start;
              endDate = end;
            },
          ),
        );

        // Tap same date twice
        await tester.tap(find.text('15').first);
        await tester.pump();
        await tester.tap(find.text('15').first);
        await tester.pump();

        expect(startDate, isNotNull);
        expect(endDate, isNotNull);
        expect(startDate!.day, equals(15));
        expect(endDate!.day, equals(15));
      });

      testWidgets('respects initial start and end dates', (tester) async {
        final initialStart = DateTime(2025, 6, 5);
        final initialEnd = DateTime(2025, 6, 15);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialStartDate: initialStart,
            initialEndDate: initialEnd,
          ),
        );

        // Should display the correct month
        expect(find.text('June 2025'), findsOneWidget);
      });

      testWidgets('does not call onDateChanged in range mode', (tester) async {
        DateTime? singleDate;

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            isDateRange: true,
            initialDate: DateTime(2025, 6, 15),
            onDateChanged: (date) {
              singleDate = date;
            },
          ),
        );

        await tester.tap(find.text('10').first);
        await tester.pump();
        await tester.tap(find.text('20').first);
        await tester.pump();

        // onDateChanged should not be called in range mode
        expect(singleDate, isNull);
      });
    });
  });
}
