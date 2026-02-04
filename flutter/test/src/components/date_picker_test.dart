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
      await tester.tap(find.bySemanticsIdentifier('next_month'));
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

      await tester.tap(find.bySemanticsIdentifier('previous_month'));
      await tester.pumpAndSettle();

      expect(find.text(expectedHeader), findsOneWidget);
    });

    group('minDate', () {
      testWidgets('allows selecting dates on or after minDate', (tester) async {
        DateTime? selectedDate;
        final minDate = DateTime(2025, 6, 10);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2025, 6, 15),
            minDate: minDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Day 15 should be selectable (after minDate)
        await tester.tap(find.text('15').first);
        await tester.pump();

        expect(selectedDate, isNotNull);
        expect(selectedDate!.day, equals(15));
      });

      testWidgets('prevents selecting dates before minDate', (tester) async {
        DateTime? selectedDate;
        final minDate = DateTime(2025, 6, 10);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2025, 6, 15),
            minDate: minDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Day 5 should not be selectable (before minDate)
        await tester.tap(find.text('5').first);
        await tester.pump();

        expect(selectedDate, isNull);
      });

      testWidgets('clamps initialDate to minDate if before', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2025, 2),
            minDate: DateTime(2025, 6, 15),
          ),
        );

        // Should show June 2025 (minDate's month) not February 2025
        expect(find.text('June 2025'), findsOneWidget);
      });
    });

    group('maxDate', () {
      testWidgets('allows selecting dates on or before maxDate', (
        tester,
      ) async {
        DateTime? selectedDate;
        final maxDate = DateTime(2025, 6, 20);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2025, 6, 15),
            maxDate: maxDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Day 15 should be selectable (before maxDate)
        await tester.tap(find.text('15').first);
        await tester.pump();

        expect(selectedDate, isNotNull);
        expect(selectedDate!.day, equals(15));
      });

      testWidgets('prevents selecting dates after maxDate', (tester) async {
        DateTime? selectedDate;
        final maxDate = DateTime(2025, 6, 10);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2025, 6, 5),
            maxDate: maxDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Day 15 should not be selectable (after maxDate)
        await tester.tap(find.text('15').first);
        await tester.pump();

        expect(selectedDate, isNull);
      });

      testWidgets('clamps initialDate to maxDate if after', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2030, 12),
            maxDate: DateTime(2025, 6, 15),
          ),
        );

        // Should show June 2025 (maxDate's month) not December 2030
        expect(find.text('June 2025'), findsOneWidget);
      });

      testWidgets(
        'disables next month navigation when at maxDate month',
        (tester) async {
          final maxDate = DateTime(2025, 6, 15);

          await tester.pumpLemonadeWidget(
            LemonadeDatePicker(
              monthHeaderFormatter: monthHeaderFormatter,
              weekdayAbbreviations: weekdayAbbreviations,
              initialDate: DateTime(2025, 6, 10),
              maxDate: maxDate,
            ),
          );

          // Capture current header before tapping
          const headerBefore = 'June 2025';
          expect(find.text(headerBefore), findsOneWidget);

          // Try to navigate to next month
          await tester.tap(find.bySemanticsIdentifier('next_month'));
          await tester.pumpAndSettle();

          // Header should remain unchanged (navigation disabled)
          expect(find.text(headerBefore), findsOneWidget);
        },
      );
    });

    group('minDate and maxDate combined', () {
      testWidgets('restricts selection to date range', (tester) async {
        DateTime? selectedDate;
        final minDate = DateTime(2025, 6, 10);
        final maxDate = DateTime(2025, 6, 20);

        await tester.pumpLemonadeWidget(
          LemonadeDatePicker(
            monthHeaderFormatter: monthHeaderFormatter,
            weekdayAbbreviations: weekdayAbbreviations,
            initialDate: DateTime(2025, 6, 15),
            minDate: minDate,
            maxDate: maxDate,
            onDateChanged: (date) {
              selectedDate = date;
            },
          ),
        );

        // Day 15 should be selectable (within range)
        await tester.tap(find.text('15').first);
        await tester.pump();
        expect(selectedDate, isNotNull);
        expect(selectedDate!.day, equals(15));

        // Reset
        selectedDate = null;

        // Day 5 should not be selectable (before minDate)
        await tester.tap(find.text('5').first);
        await tester.pump();
        expect(selectedDate, isNull);

        // Day 25 should not be selectable (after maxDate)
        await tester.tap(find.text('25').first);
        await tester.pump();
        expect(selectedDate, isNull);
      });
    });

    testWidgets('displays chevron navigation icons', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeDatePicker(
          monthHeaderFormatter: monthHeaderFormatter,
          weekdayAbbreviations: weekdayAbbreviations,
        ),
      );

      expect(
        find.bySemanticsIdentifier('previous_month'),
        findsOneWidget,
      );
      expect(
        find.bySemanticsIdentifier('next_month'),
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
