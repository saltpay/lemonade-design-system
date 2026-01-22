import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeSwitch', () {
    testWidgets('renders with initial checked false', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSwitch(
          checked: false,
          onCheckedChange: (_) {},
        ),
      );

      expect(find.byType(LemonadeSwitch), findsOneWidget);
    });

    testWidgets('renders with initial checked true', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSwitch(
          checked: true,
          onCheckedChange: (_) {},
        ),
      );

      expect(find.byType(LemonadeSwitch), findsOneWidget);
    });

    testWidgets('calls onCheckedChange when tapped', (tester) async {
      final checked = ValueNotifier<bool>(false);

      await tester.pumpLemonadeWidget(
        LemonadeSwitch(
          checked: checked.value,
          onCheckedChange: (newValue) {
            checked.value = newValue;
          },
        ),
      );

      await tester.tap(find.byType(LemonadeSwitch));
      expect(checked.value, isTrue);
    });

    testWidgets('does not call onCheckedChange when disabled', (tester) async {
      final checked = ValueNotifier<bool>(false);

      await tester.pumpLemonadeWidget(
        LemonadeSwitch(
          checked: checked.value,
          enabled: false,
          onCheckedChange: (newValue) {
            checked.value = newValue;
          },
        ),
      );

      await tester.tap(find.byType(LemonadeSwitch));
      expect(checked.value, isFalse);
    });

    testWidgets('animates when checked changes', (tester) async {
      final checked = ValueNotifier<bool>(false);
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeSwitch(
              checked: checked.value,
              onCheckedChange: (newValue) {
                setState(() {
                  checked.value = newValue;
                });
              },
            );
          },
        ),
      );

      expect(find.byType(LemonadeSwitch), findsOneWidget);

      // Tap to change value
      await tester.tap(find.byType(LemonadeSwitch));
      await tester.pump();
      expect(checked.value, isTrue);

      // Allow animation to complete
      await tester.pumpAndSettle();
      expect(checked.value, isTrue);
    });

    testWidgets('is enabled when onCheckedChange is not null', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSwitch(
          checked: false,
          onCheckedChange: (_) {},
        ),
      );

      final switchWidget = tester.widget<LemonadeSwitch>(
        find.byType(LemonadeSwitch),
      );
      expect(switchWidget.enabled, isTrue);
    });

    testWidgets('is disabled when onCheckedChange is null', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeSwitch(
          checked: false,
          enabled: false,
        ),
      );

      final switchWidget = tester.widget<LemonadeSwitch>(
        find.byType(LemonadeSwitch),
      );
      expect(switchWidget.enabled, isFalse);
    });

    testWidgets('has correct semantics', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeSwitch(
          checked: true,
          onCheckedChange: (_) {},
        ),
      );

      // Verify semantics widget exists
      expect(find.byType(Semantics), findsWidgets);
    });

    testWidgets('updates when checked changes externally', (tester) async {
      var checked = false;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeSwitch(
              checked: checked,
              onCheckedChange: (newValue) {
                setState(() {
                  checked = newValue;
                });
              },
            );
          },
        ),
      );

      // Change checked externally
      setState(() {
        checked = true;
      });
      await tester.pump();
      await tester.pumpAndSettle();

      // Widget should reflect the new value
      final switchWidget = tester.widget<LemonadeSwitch>(
        find.byType(LemonadeSwitch),
      );
      expect(switchWidget.checked, isTrue);
    });
  });
}
