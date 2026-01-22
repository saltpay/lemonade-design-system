import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeTile', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeTile(
          leadingIcon: LemonadeIcons.heartSolid,
          label: 'Label',
        ),
      );

      expect(find.byType(LemonadeTile), findsOneWidget);
      expect(find.byType(LemonadeBadge), findsNothing);

      final tileWidget = tester.widget<LemonadeTile>(
        find.byType(LemonadeTile),
      );
      expect(find.text('Label'), findsOneWidget);
      expect(tileWidget.leadingIcon, LemonadeIcons.heartSolid);
      expect(tileWidget.variant, LemonadeTileVariants.neutral);
    });

    testWidgets('renders with badge when provided', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeTile(
          label: 'Label',
          leadingIcon: LemonadeIcons.airplane,
          addOnSlot: LemonadeBadge(label: 'Badge'),
        ),
      );

      expect(find.byType(LemonadeBadge), findsOneWidget);

      final tileWidget = tester.widget<LemonadeTile>(
        find.byType(LemonadeTile),
      );

      expect(find.text('Badge'), findsOneWidget);
      expect(tileWidget.addOnSlot, isA<LemonadeBadge>());
    });

    testWidgets('renders with custom properties', (tester) async {
      const semanticLabel = 'Semantics';
      const semanticsIdentifier = 'identifier';

      await tester.pumpLemonadeWidget(
        const LemonadeTile(
          label: 'Label',
          leadingIcon: LemonadeIcons.money,
          semanticLabel: semanticLabel,
          semanticIdentifier: semanticsIdentifier,
          variant: LemonadeTileVariants.onBrand,
        ),
      );

      final tileWidget = tester.widget<LemonadeTile>(
        find.byType(LemonadeTile),
      );

      expect(find.byType(Semantics), findsWidgets);
      expect(tileWidget.variant, equals(LemonadeTileVariants.onBrand));
      expect(find.text('Label'), findsOneWidget);
      expect(tileWidget.leadingIcon, LemonadeIcons.money);
    });

    testWidgets('onTap callback is invoked when tile is tapped', (
      tester,
    ) async {
      var tapCount = 0;

      await tester.pumpLemonadeWidget(
        LemonadeTile(
          leadingIcon: LemonadeIcons.heartSolid,
          label: 'Tappable Tile',
          onTap: () => tapCount++,
        ),
      );

      await tester.tap(find.text('Tappable Tile'));
      await tester.pump();

      expect(tapCount, equals(1));
    });

    testWidgets('onTap callback is not invoked when tile is disabled', (
      tester,
    ) async {
      var tapCount = 0;

      await tester.pumpLemonadeWidget(
        LemonadeTile(
          leadingIcon: LemonadeIcons.heartSolid,
          label: 'Disabled Tile',
          enabled: false,
          onTap: () => tapCount++,
        ),
      );

      await tester.tap(find.text('Disabled Tile'), warnIfMissed: false);
      await tester.pump();

      expect(tapCount, equals(0));
    });
  });
}
