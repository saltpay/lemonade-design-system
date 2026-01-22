import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeIcon', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeIcon(
          icon: LemonadeIcons.search,
        ),
      );

      expect(find.byType(LemonadeIcon), findsOneWidget);
      expect(find.byType(SvgPicture), findsOneWidget);

      final iconWidget = tester.widget<LemonadeIcon>(
        find.byType(LemonadeIcon),
      );
      expect(iconWidget.size, equals(LemonadeIconSize.medium));
      expect(iconWidget.icon, equals(LemonadeIcons.search));
    });

    testWidgets('accepts all icon sizes', (tester) async {
      for (final size in LemonadeIconSize.values) {
        await tester.pumpLemonadeWidget(
          LemonadeIcon(
            icon: LemonadeIcons.search,
            size: size,
          ),
        );

        final iconWidget = tester.widget<LemonadeIcon>(
          find.byType(LemonadeIcon),
        );
        expect(iconWidget.size, equals(size));
      }
    });

    testWidgets('renders with custom properties', (tester) async {
      const customColor = Color(0xFFFF0000);
      const semanticLabel = 'Search icon';
      const semanticsIdentifier = 'search_icon';

      await tester.pumpLemonadeWidget(
        const LemonadeIcon(
          icon: LemonadeIcons.home,
          color: customColor,
          semanticLabel: semanticLabel,
          semanticIdentifier: semanticsIdentifier,
        ),
      );

      final iconWidget = tester.widget<LemonadeIcon>(
        find.byType(LemonadeIcon),
      );
      expect(iconWidget.icon, equals(LemonadeIcons.home));
      expect(iconWidget.color, equals(customColor));
      expect(iconWidget.semanticLabel, equals(semanticLabel));
      expect(iconWidget.semanticIdentifier, equals(semanticsIdentifier));
      expect(find.byType(Semantics), findsWidgets);
    });

    testWidgets('applies correct icon sizes from theme', (tester) async {
      final theme = LemonadeThemeData();
      final sizeMappings = {
        LemonadeIconSize.xSmall: theme.components.iconTheme.xSmallSize,
        LemonadeIconSize.small: theme.components.iconTheme.smallSize,
        LemonadeIconSize.medium: theme.components.iconTheme.mediumSize,
        LemonadeIconSize.large: theme.components.iconTheme.largeSize,
        LemonadeIconSize.xLarge: theme.components.iconTheme.xLargeSize,
      };

      for (final entry in sizeMappings.entries) {
        await tester.pumpLemonadeWidget(
          LemonadeIcon(
            icon: LemonadeIcons.search,
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

    testWidgets(
      'applies color filter from theme when no custom color provided',
      (tester) async {
        await tester.pumpLemonadeWidget(
          const LemonadeIcon(
            icon: LemonadeIcons.search,
          ),
        );

        final svgPicture = tester.widget<SvgPicture>(
          find.byType(SvgPicture),
        );
        expect(svgPicture.colorFilter, isNotNull);
      },
    );
  });
}
