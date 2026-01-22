import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeCountryFlag', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeCountryFlag(
          flag: LemonadeFlags.gbUnitedKingdom,
        ),
      );

      expect(find.byType(LemonadeCountryFlag), findsOneWidget);
      expect(find.byType(Container), findsOneWidget);
      expect(find.byType(ClipOval), findsOneWidget);
      expect(find.byType(SvgPicture), findsOneWidget);

      final flagWidget = tester.widget<LemonadeCountryFlag>(
        find.byType(LemonadeCountryFlag),
      );
      expect(flagWidget.size, equals(LemonadeFlagSize.medium));
      expect(flagWidget.flag, equals(LemonadeFlags.gbUnitedKingdom));
    });

    testWidgets('accepts all flag sizes', (tester) async {
      for (final size in LemonadeFlagSize.values) {
        await tester.pumpLemonadeWidget(
          LemonadeCountryFlag(
            flag: LemonadeFlags.adAndorra,
            size: size,
          ),
        );

        final flagWidget = tester.widget<LemonadeCountryFlag>(
          find.byType(LemonadeCountryFlag),
        );
        expect(flagWidget.size, equals(size));
      }
    });

    testWidgets('renders with custom properties', (tester) async {
      const semanticLabel = 'Andorra flag';
      const semanticIdentifier = 'andorra_flag';

      await tester.pumpLemonadeWidget(
        const LemonadeCountryFlag(
          flag: LemonadeFlags.adAndorra,
          semanticLabel: semanticLabel,
          semanticIndentifier: semanticIdentifier,
        ),
      );

      final flagWidget = tester.widget<LemonadeCountryFlag>(
        find.byType(LemonadeCountryFlag),
      );
      expect(flagWidget.flag, equals(LemonadeFlags.adAndorra));
      expect(flagWidget.semanticLabel, equals(semanticLabel));
      expect(flagWidget.semanticIndentifier, equals(semanticIdentifier));
      expect(find.byType(Semantics), findsWidgets);
    });

    testWidgets('applies correct flag sizes from theme', (tester) async {
      final theme = LemonadeThemeData();
      final sizeMappings = {
        LemonadeFlagSize.small: theme.components.countryFlagTheme.smallSize,
        LemonadeFlagSize.medium: theme.components.countryFlagTheme.mediumSize,
        LemonadeFlagSize.large: theme.components.countryFlagTheme.largeSize,
        LemonadeFlagSize.xLarge: theme.components.countryFlagTheme.xLargeSize,
        LemonadeFlagSize.xxLarge: theme.components.countryFlagTheme.xxLargeSize,
        LemonadeFlagSize.xxxLarge:
            theme.components.countryFlagTheme.xxxLargeSize,
      };

      for (final entry in sizeMappings.entries) {
        await tester.pumpLemonadeWidget(
          LemonadeCountryFlag(
            flag: LemonadeFlags.brBrazil,
            size: entry.key,
          ),
          lightTheme: theme,
        );

        final container = tester.widget<Container>(
          find.byType(Container),
        );
        expect(container.constraints!.maxWidth, equals(entry.value));
        expect(container.constraints!.maxHeight, equals(entry.value));
      }
    });

    testWidgets('applies border styling from theme', (tester) async {
      final theme = LemonadeThemeData();

      await tester.pumpLemonadeWidget(
        const LemonadeCountryFlag(
          flag: LemonadeFlags.adAndorra,
        ),
        lightTheme: theme,
      );

      final container = tester.widget<Container>(
        find.byType(Container),
      );

      final decoration = container.decoration! as BoxDecoration;
      expect(decoration.shape, equals(BoxShape.circle));
      expect(
        decoration.border!.top.color,
        equals(theme.components.countryFlagTheme.borderColor),
      );
      expect(
        decoration.border!.top.width,
        equals(theme.components.countryFlagTheme.borderWidth),
      );
    });
  });
}
