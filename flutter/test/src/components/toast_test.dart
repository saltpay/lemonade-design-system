import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeToast', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Test message',
        ),
      );

      expect(find.byType(LemonadeToast), findsOneWidget);
      expect(find.text('Test message'), findsOneWidget);
      expect(find.byType(SvgPicture), findsOneWidget);

      final toastWidget = tester.widget<LemonadeToast>(
        find.byType(LemonadeToast),
      );
      expect(toastWidget.voice, equals(LemonadeToastVoice.neutral));
      expect(toastWidget.label, equals('Test message'));
    });

    testWidgets('renders all voice variants', (tester) async {
      for (final voice in LemonadeToastVoice.values) {
        await tester.pumpLemonadeWidget(
          LemonadeToast(
            label: 'Test message',
            voice: voice,
          ),
        );

        final toastWidget = tester.widget<LemonadeToast>(
          find.byType(LemonadeToast),
        );
        expect(toastWidget.voice, equals(voice));
      }
    });

    testWidgets('renders success voice with correct icon', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Success message',
          voice: LemonadeToastVoice.success,
        ),
      );

      expect(find.byType(LemonadeToast), findsOneWidget);
      expect(find.text('Success message'), findsOneWidget);

      final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
      final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
      expect(
        pictureProvider.assetName,
        equals(LemonadeIcons.circleCheck.assetPath),
      );
    });

    testWidgets('renders error voice with correct icon', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Error message',
          voice: LemonadeToastVoice.error,
        ),
      );

      expect(find.byType(LemonadeToast), findsOneWidget);
      expect(find.text('Error message'), findsOneWidget);

      final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
      final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
      expect(
        pictureProvider.assetName,
        equals(LemonadeIcons.circleX.assetPath),
      );
    });

    testWidgets('renders neutral voice with correct icon', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Neutral message',
        ),
      );

      expect(find.byType(LemonadeToast), findsOneWidget);
      expect(find.text('Neutral message'), findsOneWidget);

      final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
      final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
      expect(
        pictureProvider.assetName,
        equals(LemonadeIcons.circleAlert.assetPath),
      );
    });

    testWidgets('renders neutral voice with custom icon', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Custom icon message',
          icon: LemonadeIcons.circleCheck,
        ),
      );

      expect(find.byType(LemonadeToast), findsOneWidget);
      expect(find.text('Custom icon message'), findsOneWidget);

      final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
      final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
      expect(
        pictureProvider.assetName,
        equals(LemonadeIcons.circleCheck.assetPath),
      );
    });

    group('factory constructors', () {
      testWidgets('success factory creates success voice', (tester) async {
        await tester.pumpLemonadeWidget(
          const LemonadeToast.success(label: 'Success'),
        );

        final toastWidget = tester.widget<LemonadeToast>(
          find.byType(LemonadeToast),
        );
        expect(toastWidget.voice, equals(LemonadeToastVoice.success));
        expect(toastWidget.icon, isNull);

        final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
        final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
        expect(
          pictureProvider.assetName,
          equals(LemonadeIcons.circleCheck.assetPath),
        );
      });

      testWidgets('error factory creates error voice', (tester) async {
        await tester.pumpLemonadeWidget(
          const LemonadeToast.error(label: 'Error'),
        );

        final toastWidget = tester.widget<LemonadeToast>(
          find.byType(LemonadeToast),
        );
        expect(toastWidget.voice, equals(LemonadeToastVoice.error));
        expect(toastWidget.icon, isNull);

        final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
        final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
        expect(
          pictureProvider.assetName,
          equals(LemonadeIcons.circleX.assetPath),
        );
      });

      testWidgets('neutral factory creates neutral voice', (tester) async {
        await tester.pumpLemonadeWidget(
          const LemonadeToast.neutral(label: 'Neutral'),
        );

        final toastWidget = tester.widget<LemonadeToast>(
          find.byType(LemonadeToast),
        );
        expect(toastWidget.voice, equals(LemonadeToastVoice.neutral));

        final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
        final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
        expect(
          pictureProvider.assetName,
          equals(LemonadeIcons.circleAlert.assetPath),
        );
      });

      testWidgets('neutral factory with custom icon', (tester) async {
        await tester.pumpLemonadeWidget(
          const LemonadeToast.neutral(
            label: 'Custom',
            icon: LemonadeIcons.circleCheck,
          ),
        );

        final toastWidget = tester.widget<LemonadeToast>(
          find.byType(LemonadeToast),
        );
        expect(toastWidget.voice, equals(LemonadeToastVoice.neutral));
        expect(toastWidget.icon, equals(LemonadeIcons.circleCheck));

        final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
        final pictureProvider = svgPicture.bytesLoader as SvgAssetLoader;
        expect(
          pictureProvider.assetName,
          equals(LemonadeIcons.circleCheck.assetPath),
        );
      });
    });

    testWidgets('renders with semantic properties', (tester) async {
      const semanticLabel = 'Toast notification';
      const semanticsIdentifier = 'toast_notification';

      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Test message',
          semanticLabel: semanticLabel,
          semanticIdentifier: semanticsIdentifier,
        ),
      );

      final toastWidget = tester.widget<LemonadeToast>(
        find.byType(LemonadeToast),
      );
      expect(toastWidget.semanticLabel, equals(semanticLabel));
      expect(toastWidget.semanticIdentifier, equals(semanticsIdentifier));
      expect(find.byType(Semantics), findsWidgets);
    });

    testWidgets('applies theme styling correctly', (tester) async {
      final theme = LemonadeThemeData();
      final toastTheme = theme.components.toastTheme;

      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Themed toast',
        ),
        lightTheme: theme,
      );

      final container = tester.widget<Container>(
        find.descendant(
          of: find.byType(LemonadeToast),
          matching: find.byType(Container),
        ),
      );

      final decoration = container.decoration! as BoxDecoration;
      expect(decoration.color, equals(toastTheme.backgroundColor));
      expect(
        decoration.borderRadius,
        equals(BorderRadius.circular(toastTheme.borderRadius)),
      );
      expect(decoration.boxShadow, equals(toastTheme.shadow));
    });

    testWidgets('applies correct icon size from theme', (tester) async {
      final theme = LemonadeThemeData();
      final toastTheme = theme.components.toastTheme;

      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Test message',
        ),
        lightTheme: theme,
      );

      final svgPicture = tester.widget<SvgPicture>(find.byType(SvgPicture));
      expect(svgPicture.width, equals(toastTheme.iconSize));
      expect(svgPicture.height, equals(toastTheme.iconSize));
    });

    testWidgets('uses label as semantic label when none provided', (
      tester,
    ) async {
      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Accessible message',
        ),
      );

      final semantics = tester.widget<Semantics>(
        find
            .ancestor(
              of: find.byType(Container),
              matching: find.byType(Semantics),
            )
            .first,
      );
      expect(semantics.properties.label, equals('Accessible message'));
    });

    testWidgets('respects min height constraint', (tester) async {
      final theme = LemonadeThemeData();
      final toastTheme = theme.components.toastTheme;

      await tester.pumpLemonadeWidget(
        const LemonadeToast(
          label: 'Short',
        ),
        lightTheme: theme,
      );

      final container = tester.widget<Container>(
        find.descendant(
          of: find.byType(LemonadeToast),
          matching: find.byType(Container),
        ),
      );

      expect(
        container.constraints?.minHeight,
        equals(toastTheme.minHeight),
      );
    });
  });

  group('LemonadeToastTheme', () {
    test('creates from LemonadeThemeData correctly', () {
      final themeData = LemonadeThemeData();
      final toastTheme = themeData.components.toastTheme;

      // Verify colors use semantic tokens
      expect(
        toastTheme.backgroundColor,
        equals(themeData.colors.background.bgAlwaysDark),
      );
      expect(
        toastTheme.successIconColor,
        equals(themeData.colors.content.contentPositiveOnColor),
      );
      expect(
        toastTheme.errorIconColor,
        equals(themeData.colors.content.contentCritical),
      );
      expect(
        toastTheme.neutralIconColor,
        equals(themeData.colors.content.contentNeutralOnColor),
      );
      // Verify sizing tokens
      expect(toastTheme.iconSize, equals(themeData.sizes.size500));
      expect(toastTheme.iconLabelGap, equals(themeData.spaces.spacing300));
      expect(toastTheme.borderRadius, equals(themeData.radius.radiusFull));
      expect(toastTheme.minHeight, equals(themeData.sizes.size1100));
      expect(toastTheme.shadow, equals(themeData.shadows.large));
    });

    test('lerp interpolates between two themes', () {
      final themeData = LemonadeThemeData();
      final themeA = themeData.components.toastTheme;
      final themeB = themeA.copyWith(
        backgroundColor: const Color(0xFFFFFFFF),
        iconSize: 40,
      );

      final interpolated = LemonadeToastTheme.lerp(themeA, themeB, 0.5);

      expect(
        interpolated.backgroundColor,
        isNot(equals(themeA.backgroundColor)),
      );
      expect(
        interpolated.backgroundColor,
        isNot(equals(themeB.backgroundColor)),
      );
      expect(interpolated.iconSize, equals(30.0));
    });

    test('lerp returns original when identical', () {
      final themeData = LemonadeThemeData();
      final theme = themeData.components.toastTheme;

      final result = LemonadeToastTheme.lerp(theme, theme, 0.5);

      expect(identical(result, theme), isTrue);
    });

    test('copyWith creates copy with replaced values', () {
      final themeData = LemonadeThemeData();
      final theme = themeData.components.toastTheme;
      const newBackgroundColor = Color(0xFFFF0000);

      final copied = theme.copyWith(backgroundColor: newBackgroundColor);

      expect(copied.backgroundColor, equals(newBackgroundColor));
      expect(copied.successIconColor, equals(theme.successIconColor));
      expect(copied.errorIconColor, equals(theme.errorIconColor));
    });

    test('mergeWith returns this when other is null', () {
      final themeData = LemonadeThemeData();
      final theme = themeData.components.toastTheme;

      final merged = theme.mergeWith(null);

      expect(identical(merged, theme), isTrue);
    });

    test('mergeWith merges values from other theme', () {
      final themeData = LemonadeThemeData();
      final themeA = themeData.components.toastTheme;
      final themeB = themeA.copyWith(
        backgroundColor: const Color(0xFFFF0000),
      );

      final merged = themeA.mergeWith(themeB);

      expect(merged.backgroundColor, equals(const Color(0xFFFF0000)));
    });

    test('equality and hashCode work correctly', () {
      final themeDataA = LemonadeThemeData();
      final themeDataB = LemonadeThemeData();
      final themeA = themeDataA.components.toastTheme;
      final themeB = themeDataB.components.toastTheme;
      final themeC = themeA.copyWith(iconSize: 100);

      expect(themeA == themeB, isTrue);
      expect(themeA.hashCode == themeB.hashCode, isTrue);
      expect(themeA == themeC, isFalse);
    });
  });
}
