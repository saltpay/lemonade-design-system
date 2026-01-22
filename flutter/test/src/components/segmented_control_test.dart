import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

enum _TestValue { first, second, third }

void main() {
  group('LemonadeSegmentedControl', () {
    final testItems = [
      const LemonadeSegmentItem(value: _TestValue.first, label: 'First'),
      const LemonadeSegmentItem(value: _TestValue.second, label: 'Second'),
      const LemonadeSegmentItem(value: _TestValue.third, label: 'Third'),
    ];

    testWidgets('renders all segment labels', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          onChanged: (_) {},
        ),
      );

      expect(find.text('First'), findsOneWidget);
      expect(find.text('Second'), findsOneWidget);
      expect(find.text('Third'), findsOneWidget);
    });

    testWidgets('calls onChanged when unselected segment is tapped', (
      tester,
    ) async {
      _TestValue? changedValue;

      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          onChanged: (value) {
            changedValue = value;
          },
        ),
      );

      await tester.tap(find.text('Second'));
      expect(changedValue, equals(_TestValue.second));
    });

    testWidgets('does not call onChanged when selected segment is tapped', (
      tester,
    ) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          onChanged: (_) {
            wasCalled = true;
          },
        ),
      );

      await tester.tap(find.text('First'));
      expect(wasCalled, isFalse);
    });

    testWidgets('does not call onChanged when disabled', (tester) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          isEnabled: false,
          onChanged: (_) {
            wasCalled = true;
          },
        ),
      );

      await tester.tap(find.text('Second'));
      expect(wasCalled, isFalse);
    });

    testWidgets('applies reduced opacity when disabled', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          isEnabled: false,
          onChanged: (_) {},
        ),
      );

      final opacity = tester.widget<Opacity>(find.byType(Opacity));
      expect(opacity.opacity, equals(0.6));
    });

    testWidgets('is enabled by default', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          onChanged: (_) {},
        ),
      );

      final segmentedControl = tester
          .widget<LemonadeSegmentedControl<_TestValue>>(
            find.byType(LemonadeSegmentedControl<_TestValue>),
          );
      expect(segmentedControl.isEnabled, isTrue);
    });

    testWidgets('updates selection when selectedValue changes', (tester) async {
      var selectedValue = _TestValue.first;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeSegmentedControl<_TestValue>(
              items: testItems,
              selectedValue: selectedValue,
              onChanged: (value) {
                setState(() {
                  selectedValue = value;
                });
              },
            );
          },
        ),
      );

      // Change selection externally
      setState(() {
        selectedValue = _TestValue.third;
      });
      await tester.pump();

      final segmentedControl = tester
          .widget<LemonadeSegmentedControl<_TestValue>>(
            find.byType(LemonadeSegmentedControl<_TestValue>),
          );
      expect(segmentedControl.selectedValue, equals(_TestValue.third));
    });

    testWidgets('animates thumb position on selection change', (tester) async {
      var selectedValue = _TestValue.first;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeSegmentedControl<_TestValue>(
              items: testItems,
              selectedValue: selectedValue,
              onChanged: (value) {
                setState(() {
                  selectedValue = value;
                });
              },
            );
          },
        ),
      );

      // Tap second segment
      await tester.tap(find.text('Second'));
      await tester.pump();

      // Animation should be in progress
      expect(find.byType(AnimatedPositioned), findsOneWidget);

      await tester.pumpAndSettle();
      expect(selectedValue, equals(_TestValue.second));
    });

    testWidgets('renders with custom height', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: testItems,
          selectedValue: _TestValue.first,
          height: 60,
          onChanged: (_) {},
        ),
      );

      final segmentedControl = tester
          .widget<LemonadeSegmentedControl<_TestValue>>(
            find.byType(LemonadeSegmentedControl<_TestValue>),
          );
      expect(segmentedControl.height, equals(60));
    });

    testWidgets('renders segment with icon', (tester) async {
      final itemsWithIcon = [
        const LemonadeSegmentItem(
          value: _TestValue.first,
          label: 'First',
          icon: LemonadeIcon(icon: LemonadeIcons.card),
        ),
        const LemonadeSegmentItem(value: _TestValue.second, label: 'Second'),
      ];

      await tester.pumpLemonadeWidget(
        LemonadeSegmentedControl<_TestValue>(
          items: itemsWithIcon,
          selectedValue: _TestValue.first,
          onChanged: (_) {},
        ),
      );

      expect(find.byType(LemonadeIcon), findsOneWidget);
    });
  });
}
