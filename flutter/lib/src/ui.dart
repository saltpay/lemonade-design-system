import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeUi}
/// A widget that sets up the Lemonade UI for the application.
///
/// This widget provides a [LemonadeTheme] for the entire application and
/// handles theme switching between light and dark modes.
/// {@endtemplate}
class LemonadeUi extends StatelessWidget {
  /// {@macro LemonadeUi}
  const LemonadeUi({
    required this.builder,
    this.lightTheme,
    this.darkTheme,
    super.key,
  });

  /// The theme to use when the app is in light mode.
  final LemonadeThemeData? lightTheme;

  /// The theme to use when the app is in dark mode.
  final LemonadeThemeData? darkTheme;

  /// A custom app widget builder.
  final WidgetBuilder builder;

  @override
  Widget build(BuildContext context) {
    final effectiveTheme = _getEffectiveTheme(context);

    return LemonadeAnimatedTheme(
      data: effectiveTheme,
      child: Builder(
        builder: builder,
      ),
    );
  }

  LemonadeThemeData _getEffectiveTheme(BuildContext context) {
    final platformBrightness = MediaQuery.platformBrightnessOf(context);

    return switch (platformBrightness) {
      Brightness.dark => lightTheme ?? LemonadeThemeData(),
      Brightness.light => darkTheme ?? LemonadeThemeData(),
    };
  }
}
