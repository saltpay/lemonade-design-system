import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeBadge', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeBadge(
          label: 'Test Badge',
        ),
      );

      expect(find.byType(LemonadeBadge), findsOneWidget);

      final badgeWidget = tester.widget<LemonadeBadge>(
        find.byType(LemonadeBadge),
      );
      expect(badgeWidget.size, equals(LemonadeBadgeSize.small));
      expect(find.text('Test Badge'), findsOneWidget);
    });

    testWidgets('renders correctly all badge sizes', (tester) async {
      for (final size in LemonadeBadgeSize.values) {
        await tester.pumpLemonadeWidget(
          LemonadeBadge(
            label: 'Size Test',
            size: size,
          ),
        );

        final badgeWidget = tester.widget<LemonadeBadge>(
          find.byType(LemonadeBadge),
        );
        expect(badgeWidget.size, equals(size));
      }
    });

    testWidgets('renders with custom properties', (tester) async {
      const semanticLabel = 'Promotional Badge';
      const semanticsIdentifier = 'promo_badge';

      await tester.pumpLemonadeWidget(
        const LemonadeBadge(
          label: 'Special Offer',
          semanticLabel: semanticLabel,
          semanticIdentifier: semanticsIdentifier,
        ),
      );

      final badgeWidget = tester.widget<LemonadeBadge>(
        find.byType(LemonadeBadge),
      );
      expect(find.text('Special Offer'), findsOneWidget);
      expect(badgeWidget.semanticLabel, equals(semanticLabel));
      expect(badgeWidget.semanticIdentifier, equals(semanticsIdentifier));
      expect(find.byType(Semantics), findsWidgets);
    });
  });
}
