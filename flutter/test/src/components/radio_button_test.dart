import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeRadioButton', () {
    testWidgets('renders with initial checked f alse', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      expect(find.byType(LemonadeRadioButton), findsOneWidget);
    });

    testWidgets('renders with initial checked true', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: true,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      expect(find.byType(LemonadeRadioButton), findsOneWidget);
    });

    testWidgets('displays label text', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Test Label',
        ),
      );

      expect(find.text('Test Label'), findsOneWidget);
    });

    testWidgets('displays support text when provided', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
          supportText: 'Additional information',
        ),
      );

      expect(find.text('Additional information'), findsOneWidget);
    });

    testWidgets('does not display support text when not provided', (
      tester,
    ) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      // Only the label should be present
      expect(find.byType(Text), findsOneWidget);
    });

    testWidgets('calls onChanged when tapped while unchecked', (
      tester,
    ) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {
            wasCalled = true;
          },
          label: 'Option 1',
        ),
      );

      await tester.tap(find.byType(LemonadeRadioButton));
      expect(wasCalled, isTrue);
    });

    testWidgets('does not call onChanged when already checked', (
      tester,
    ) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: true,
          onChanged: () {
            wasCalled = true;
          },
          label: 'Option 1',
        ),
      );

      await tester.tap(find.byType(LemonadeRadioButton));
      expect(wasCalled, isFalse);
    });

    testWidgets('does not call onChanged when disabled', (
      tester,
    ) async {
      var wasCalled = false;

      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          enabled: false,
          onChanged: () {
            wasCalled = true;
          },
          label: 'Option 1',
        ),
      );

      await tester.tap(find.byType(LemonadeRadioButton));
      expect(wasCalled, isFalse);
    });

    testWidgets('is enabled by default', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      final radioButtonWidget = tester.widget<LemonadeRadioButton>(
        find.byType(LemonadeRadioButton),
      );
      expect(radioButtonWidget.enabled, isTrue);
    });

    testWidgets('is disabled when enabled is false', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          enabled: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      final radioButtonWidget = tester.widget<LemonadeRadioButton>(
        find.byType(LemonadeRadioButton),
      );
      expect(radioButtonWidget.enabled, isFalse);
    });

    testWidgets('updates when checked changes externally', (tester) async {
      var checked = false;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeRadioButton(
              checked: checked,
              onChanged: () {
                setState(() {
                  checked = true;
                });
              },
              label: 'Option 1',
            );
          },
        ),
      );

      // Change checked externally
      setState(() {
        checked = true;
      });
      await tester.pump();

      final radioButtonWidget = tester.widget<LemonadeRadioButton>(
        find.byType(LemonadeRadioButton),
      );
      expect(radioButtonWidget.checked, isTrue);
    });

    testWidgets('animates inner circle on state change', (tester) async {
      var checked = false;
      late StateSetter setState;

      await tester.pumpLemonadeWidget(
        StatefulBuilder(
          builder: (context, setStateFunc) {
            setState = setStateFunc;
            return LemonadeRadioButton(
              checked: checked,
              onChanged: () {
                setState(() {
                  checked = true;
                });
              },
              label: 'Option 1',
            );
          },
        ),
      );

      // Tap to check
      await tester.tap(find.byType(LemonadeRadioButton));
      await tester.pump();

      // Animation should be in progress
      expect(find.byType(AnimatedContainer), findsOneWidget);

      await tester.pumpAndSettle();
      expect(checked, isTrue);
    });

    testWidgets('renders with both label and support text', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Primary Label',
          supportText: 'Support Text',
        ),
      );

      expect(find.text('Primary Label'), findsOneWidget);
      expect(find.text('Support Text'), findsOneWidget);
    });

    testWidgets('has correct semantics', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: true,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      // Verify semantics widget exists
      expect(find.byType(Semantics), findsWidgets);
    });

    testWidgets('uses custom semantic label when provided', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
          semanticLabel: 'Custom accessibility label',
        ),
      );

      final semantics = tester.getSemantics(find.byType(Semantics).first);
      expect(semantics.label, contains('Custom accessibility label'));
    });

    testWidgets(
      'uses label as semantic label when semanticLabel is not provided',
      (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeRadioButton(
            checked: false,
            onChanged: () {},
            label: 'Option 1',
          ),
        );

        final semantics = tester.getSemantics(find.byType(Semantics).first);
        expect(semantics.label, contains('Option 1'));
      },
    );

    testWidgets('applies semantic identifier when provided', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
          semanticIdentifier: 'radio_option_1',
        ),
      );

      final semantics = tester.getSemantics(find.byType(Semantics).first);
      expect(semantics.identifier, 'radio_option_1');
    });

    testWidgets('renders disabled state with reduced opacity', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          enabled: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      final opacityWidget = tester.widget<Opacity>(find.byType(Opacity));
      expect(opacityWidget.opacity, lessThan(1.0));
    });

    testWidgets('renders enabled state with full opacity', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      final opacityWidget = tester.widget<Opacity>(find.byType(Opacity));
      expect(opacityWidget.opacity, equals(1.0));
    });

    testWidgets('renders checked and disabled state correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        LemonadeRadioButton(
          checked: true,
          enabled: false,
          onChanged: () {},
          label: 'Option 1',
        ),
      );

      expect(find.byType(LemonadeRadioButton), findsOneWidget);

      final radioButtonWidget = tester.widget<LemonadeRadioButton>(
        find.byType(LemonadeRadioButton),
      );
      expect(radioButtonWidget.checked, isTrue);
      expect(radioButtonWidget.enabled, isFalse);
    });
  });
}
