import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Extension on [WidgetTester] to simplify widget testing with Lemonade UI.
extension LemonadeWidgetTester on WidgetTester {
  /// Pumps a widget wrapped in [LemonadeUi] and [WidgetsApp].
  ///
  /// This is a convenience method that automatically wraps the provided widget
  /// in the required [LemonadeUi] and [WidgetsApp] structure, reducing
  /// boilerplate in tests.
  ///
  /// Optional parameters [lightTheme] and [darkTheme] allow specifying custom
  /// themes for the Lemonade UI. If not provided, default themes will be used.
  Future<void> pumpLemonadeWidget(
    Widget child, {
    LemonadeThemeData? lightTheme,
    LemonadeThemeData? darkTheme,
  }) async {
    await pumpWidget(
      LemonadeUi(
        lightTheme: lightTheme,
        darkTheme: darkTheme,
        builder: (context) => WidgetsApp(
          color: LemonadeTheme.of(context).colors.content.contentBrand,
          builder: (_, _) => child,
        ),
      ),
    );
  }
}
