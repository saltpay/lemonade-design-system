import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeBrandLogo', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeBrandLogo(
          logo: LemonadeBrandLogos.visa,
        ),
      );

      expect(find.byType(LemonadeBrandLogo), findsOneWidget);
      expect(find.byType(SvgPicture), findsOneWidget);

      final logoWidget = tester.widget<LemonadeBrandLogo>(
        find.byType(LemonadeBrandLogo),
      );
      expect(logoWidget.size, equals(LemonadeBrandLogoSize.medium));
      expect(logoWidget.logo, equals(LemonadeBrandLogos.visa));
    });

    testWidgets('accepts all brand logo sizes', (tester) async {
      for (final size in LemonadeBrandLogoSize.values) {
        await tester.pumpLemonadeWidget(
          LemonadeBrandLogo(
            logo: LemonadeBrandLogos.visa,
            size: size,
          ),
        );

        final logoWidget = tester.widget<LemonadeBrandLogo>(
          find.byType(LemonadeBrandLogo),
        );
        expect(logoWidget.size, equals(size));
      }
    });

    testWidgets('renders with custom properties', (tester) async {
      const semanticLabel = 'Visa logo';
      const semanticsIdentifier = 'visa_logo';

      await tester.pumpLemonadeWidget(
        const LemonadeBrandLogo(
          logo: LemonadeBrandLogos.mastercard,
          semanticLabel: semanticLabel,
          semanticIdentifier: semanticsIdentifier,
        ),
      );

      final logoWidget = tester.widget<LemonadeBrandLogo>(
        find.byType(LemonadeBrandLogo),
      );
      expect(logoWidget.logo, equals(LemonadeBrandLogos.mastercard));
      expect(logoWidget.semanticLabel, equals(semanticLabel));
      expect(logoWidget.semanticIdentifier, equals(semanticsIdentifier));
      expect(find.byType(Semantics), findsWidgets);
    });

    testWidgets('applies correct brand logo sizes from theme', (tester) async {
      final theme = LemonadeThemeData();
      final sizeMappings = {
        LemonadeBrandLogoSize.small: theme.components.brandLogoTheme.smallSize,
        LemonadeBrandLogoSize.medium:
            theme.components.brandLogoTheme.mediumSize,
        LemonadeBrandLogoSize.large: theme.components.brandLogoTheme.largeSize,
        LemonadeBrandLogoSize.xLarge:
            theme.components.brandLogoTheme.xLargeSize,
        LemonadeBrandLogoSize.xxLarge:
            theme.components.brandLogoTheme.xxLargeSize,
      };

      for (final entry in sizeMappings.entries) {
        await tester.pumpLemonadeWidget(
          LemonadeBrandLogo(
            logo: LemonadeBrandLogos.visa,
            size: entry.key,
          ),
          lightTheme: theme,
        );

        final svgPicture = tester.widget<SvgPicture>(
          find.byType(SvgPicture),
        );
        expect(svgPicture.width, equals(entry.value));
        expect(svgPicture.height, equals(entry.value));
      }
    });
  });
}
