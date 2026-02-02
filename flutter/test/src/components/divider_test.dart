import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeDivider', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(),
      );

      expect(find.byType(LemonadeDivider), findsOneWidget);

      final dividerWidget = tester.widget<LemonadeDivider>(
        find.byType(LemonadeDivider),
      );
      expect(dividerWidget.variant, equals(LemonadeDividerVariant.solid));
      expect(dividerWidget.orientation, isNull);
      expect(dividerWidget.label, isNull);
    });

    testWidgets('renders solid variant', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(),
      );

      final dividerWidget = tester.widget<LemonadeDivider>(
        find.byType(LemonadeDivider),
      );
      expect(dividerWidget.variant, equals(LemonadeDividerVariant.solid));
      expect(find.byType(Divider), findsOneWidget);
    });

    testWidgets('renders dashed variant', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          variant: LemonadeDividerVariant.dashed,
        ),
      );

      final dividerWidget = tester.widget<LemonadeDivider>(
        find.byType(LemonadeDivider),
      );
      expect(dividerWidget.variant, equals(LemonadeDividerVariant.dashed));
      expect(find.byType(CustomPaint), findsWidgets);
    });

    testWidgets('renders horizontal orientation by default', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(),
      );

      expect(find.byType(Divider), findsOneWidget);
      expect(find.byType(VerticalDivider), findsNothing);
    });

    testWidgets('renders vertical orientation', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          orientation: LemonadeDividerOrientation.vertical,
        ),
      );

      expect(find.byType(VerticalDivider), findsOneWidget);
      expect(find.byType(Divider), findsNothing);
    });

    testWidgets('renders with label', (tester) async {
      const label = 'Section Title';

      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          label: label,
        ),
      );

      final dividerWidget = tester.widget<LemonadeDivider>(
        find.byType(LemonadeDivider),
      );
      expect(dividerWidget.label, equals(label));
      expect(find.text(label), findsOneWidget);
      expect(find.byType(Row), findsOneWidget);
    });

    testWidgets('renders with label and dashed variant', (tester) async {
      const label = 'Dashed Section';

      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          label: label,
          variant: LemonadeDividerVariant.dashed,
        ),
      );

      expect(find.text(label), findsOneWidget);
      expect(
        find.byType(CustomPaint),
        findsAtLeastNWidgets(2),
      ); // Two dashed lines
    });

    testWidgets(
      'throws assertion error when label is used with vertical orientation',
      (tester) async {
        expect(
          () => LemonadeDivider(
            label: 'Invalid',
            orientation: LemonadeDividerOrientation.vertical,
          ),
          throwsAssertionError,
        );
      },
    );

    testWidgets('renders with custom semantic properties', (tester) async {
      const semanticLabel = 'Content Separator';
      const semanticsIdentifier = 'divider_main';

      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          semanticLabel: semanticLabel,
          semanticIdentifier: semanticsIdentifier,
        ),
      );

      final dividerWidget = tester.widget<LemonadeDivider>(
        find.byType(LemonadeDivider),
      );
      expect(dividerWidget.semanticLabel, equals(semanticLabel));
      expect(dividerWidget.semanticIdentifier, equals(semanticsIdentifier));

      final semantics = tester.getSemantics(find.byType(Semantics).first);
      expect(semantics.label, contains(semanticLabel));
    });

    testWidgets(
      'uses label as semantic label when semantic label is not provided',
      (tester) async {
        const label = 'Section';

        await tester.pumpLemonadeWidget(
          const LemonadeDivider(
            label: label,
          ),
        );

        final semantics = tester.getSemantics(find.byType(Semantics).first);
        expect(semantics.label, contains(label));
      },
    );

    testWidgets('renders all variant types', (tester) async {
      for (final variant in LemonadeDividerVariant.values) {
        await tester.pumpLemonadeWidget(
          LemonadeDivider(
            variant: variant,
          ),
        );

        final dividerWidget = tester.widget<LemonadeDivider>(
          find.byType(LemonadeDivider),
        );
        expect(dividerWidget.variant, equals(variant));
      }
    });

    testWidgets('renders all orientation types', (tester) async {
      for (final orientation in LemonadeDividerOrientation.values) {
        await tester.pumpLemonadeWidget(
          LemonadeDivider(
            orientation: orientation,
          ),
        );

        final dividerWidget = tester.widget<LemonadeDivider>(
          find.byType(LemonadeDivider),
        );
        expect(dividerWidget.orientation, equals(orientation));
      }
    });

    testWidgets('vertical solid divider renders correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          orientation: LemonadeDividerOrientation.vertical,
        ),
      );

      expect(find.byType(VerticalDivider), findsOneWidget);
    });

    testWidgets('vertical dashed divider renders correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          orientation: LemonadeDividerOrientation.vertical,
          variant: LemonadeDividerVariant.dashed,
        ),
      );

      expect(find.byType(CustomPaint), findsWidgets);
    });

    testWidgets('horizontal dashed divider renders correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          orientation: LemonadeDividerOrientation.horizontal,
          variant: LemonadeDividerVariant.dashed,
        ),
      );

      expect(find.byType(CustomPaint), findsWidgets);
    });

    testWidgets('divider with label has correct structure', (tester) async {
      const label = 'Test Label';

      await tester.pumpLemonadeWidget(
        const LemonadeDivider(
          label: label,
        ),
      );

      // Should have a Row with expanded dividers on both sides
      expect(find.byType(Row), findsOneWidget);
      expect(find.byType(Expanded), findsNWidgets(2));
      expect(find.text(label), findsOneWidget);
      expect(find.byType(Padding), findsWidgets);
    });
  });
}
