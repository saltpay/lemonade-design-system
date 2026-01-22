import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

void main() {
  group('LemonadeTheme', () {
    testWidgets('should provide theme data to descendants', (tester) async {
      final themeData = LemonadeThemeData();
      LemonadeThemeData? capturedTheme;

      await tester.pumpWidget(
        LemonadeTheme(
          data: themeData,
          child: Builder(
            builder: (context) {
              capturedTheme = LemonadeTheme.of(context);
              return const SizedBox();
            },
          ),
        ),
      );

      expect(capturedTheme, equals(themeData));
    });

    testWidgets('should return default theme when no ancestor found', (
      tester,
    ) async {
      LemonadeThemeData? capturedTheme;

      await tester.pumpWidget(
        Builder(
          builder: (context) {
            capturedTheme = LemonadeTheme.of(context);
            return const SizedBox();
          },
        ),
      );

      expect(capturedTheme, isA<LemonadeThemeData>());
    });

    testWidgets('should merge themes correctly', (tester) async {
      final parentTheme = LemonadeThemeData();
      final childData = LemonadeThemeData();
      LemonadeThemeData? capturedTheme;

      await tester.pumpWidget(
        LemonadeTheme(
          data: parentTheme,
          child: LemonadeTheme.merge(
            data: childData,
            child: Builder(
              builder: (context) {
                capturedTheme = LemonadeTheme.of(context);
                return const SizedBox();
              },
            ),
          ),
        ),
      );

      expect(capturedTheme, isNotNull);
      expect(capturedTheme, isNot(same(parentTheme)));
      expect(capturedTheme, isNot(same(childData)));
    });

    testWidgets('should notify when theme data changes', (tester) async {
      final theme1 = LemonadeThemeData();
      final theme2 = LemonadeThemeData(brightness: Brightness.dark);

      final widget1 = LemonadeTheme(data: theme1, child: const SizedBox());
      final widget2 = LemonadeTheme(data: theme2, child: const SizedBox());

      expect(widget1.updateShouldNotify(widget2), isTrue);
      expect(widget1.updateShouldNotify(widget1), isFalse);
    });
  });

  group('LemonadeThemeDataTween', () {
    test('should interpolate between theme data', () {
      final begin = LemonadeThemeData();
      final end = LemonadeThemeData(brightness: Brightness.dark);
      final tween = LemonadeThemeDataTween(begin: begin, end: end);

      final result = tween.lerp(0.5);

      expect(result, isA<LemonadeThemeData>());
      expect(result, isNot(same(begin)));
    });
  });

  group('LemonadeAnimatedTheme', () {
    testWidgets('should provide animated theme data to descendants', (
      tester,
    ) async {
      final themeData = LemonadeThemeData();
      LemonadeThemeData? capturedTheme;

      await tester.pumpWidget(
        LemonadeAnimatedTheme(
          data: themeData,
          child: Builder(
            builder: (context) {
              capturedTheme = LemonadeTheme.of(context);
              return const SizedBox();
            },
          ),
        ),
      );

      expect(capturedTheme, isNotNull);
    });
  });
}
