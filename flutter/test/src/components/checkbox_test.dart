import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeCheckbox', () {
    testWidgets('renders with initial status unchecked', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.unchecked,
          onChanged: () {},
        ),
      );

      expect(find.byType(LemonadeCheckbox), findsOneWidget);
    });

    testWidgets('renders with initial status checked', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.checked,
          onChanged: () {},
        ),
      );

      expect(find.byType(LemonadeCheckbox), findsOneWidget);
    });

    testWidgets('calls onChanged when tapped', (tester) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.unchecked,
          onChanged: () {
            wasCalled = true;
          },
        ),
      );

      await tester.tap(find.byType(LemonadeCheckbox));
      expect(wasCalled, isTrue);
    });

    testWidgets('does not call onChanged when disabled', (tester) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.unchecked,
          enabled: false,
          onChanged: () {
            wasCalled = true;
          },
        ),
      );

      await tester.tap(find.byType(LemonadeCheckbox));
      expect(wasCalled, isFalse);
    });

    testWidgets('is enabled by default', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.unchecked,
          onChanged: () {},
        ),
      );

      final checkboxWidget = tester.widget<LemonadeCheckbox>(
        find.byType(LemonadeCheckbox),
      );
      expect(checkboxWidget.enabled, isTrue);
    });

    testWidgets('is disabled when enabled is false', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.unchecked,
          enabled: false,
          onChanged: () {},
        ),
      );

      final checkboxWidget = tester.widget<LemonadeCheckbox>(
        find.byType(LemonadeCheckbox),
      );
      expect(checkboxWidget.enabled, isFalse);
    });

    testWidgets('updates when status changes externally', (tester) async {
      var status = CheckboxStatus.unchecked;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeCheckbox(
              label: 'Checkbox label',
              status: status,
              onChanged: () {
                setState(() {
                  status = status == CheckboxStatus.checked
                      ? CheckboxStatus.unchecked
                      : CheckboxStatus.checked;
                });
              },
            );
          },
        ),
      );

      // Change status externally
      setState(() {
        status = CheckboxStatus.checked;
      });
      await tester.pump();

      final checkboxWidget = tester.widget<LemonadeCheckbox>(
        find.byType(LemonadeCheckbox),
      );
      expect(checkboxWidget.status, CheckboxStatus.checked);
    });

    testWidgets('displays checkmark icon when checked', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.checked,
          onChanged: () {},
        ),
      );

      expect(find.byType(LemonadeIcon), findsOneWidget);
    });

    testWidgets('displays minus icon when indeterminate', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.indeterminate,
          onChanged: () {},
        ),
      );

      expect(find.byType(LemonadeIcon), findsOneWidget);
    });

    testWidgets('animates icon changes', (tester) async {
      var status = CheckboxStatus.unchecked;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeCheckbox(
              label: 'Checkbox label',
              status: status,
              onChanged: () {
                setState(() {
                  status = status == CheckboxStatus.checked
                      ? CheckboxStatus.unchecked
                      : CheckboxStatus.checked;
                });
              },
            );
          },
        ),
      );

      // Tap to check
      await tester.tap(find.byType(LemonadeCheckbox));
      await tester.pump();

      // Animation should be in progress
      expect(find.byType(AnimatedSwitcher), findsOneWidget);

      await tester.pumpAndSettle();
      expect(status, CheckboxStatus.checked);
    });

    testWidgets('supports indeterminate state', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeCheckbox(
          label: 'Checkbox label',
          status: CheckboxStatus.indeterminate,
          onChanged: () {},
        ),
      );

      final checkboxWidget = tester.widget<LemonadeCheckbox>(
        find.byType(LemonadeCheckbox),
      );
      expect(checkboxWidget.status, CheckboxStatus.indeterminate);
      expect(find.byType(LemonadeIcon), findsOneWidget);
    });
  });
}
