import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeTheme}
/// A theme provider that manages the current theme and provides theme data
/// to the widget tree via [InheritedWidget].
/// {@endtemplate}
class LemonadeTheme extends InheritedWidget {
  /// {@macro LemonadeTheme}
  const LemonadeTheme({
    required this.data,
    required super.child,
    super.key,
  });

  /// The theme data configuration.
  final LemonadeThemeData data;

  /// Returns the [LemonadeThemeData] from the closest [LemonadeTheme]
  /// ancestor.
  ///
  /// If no [LemonadeTheme] ancestor is found, this will return
  /// a default light theme.
  static LemonadeThemeData of(BuildContext context) {
    final theme = context.dependOnInheritedWidgetOfExactType<LemonadeTheme>();
    return theme?.data ?? LemonadeThemeData();
  }

  /// Creates a [LemonadeTheme] that merges the given [data] with the
  /// [LemonadeThemeData] from the closest [LemonadeTheme] ancestor.
  static Widget merge({
    required Widget child,
    Key? key,
    LemonadeThemeData? data,
  }) {
    return Builder(
      builder: (BuildContext context) {
        final parent = LemonadeTheme.of(context);
        return LemonadeTheme(
          data: parent.merge(data),
          child: child,
        );
      },
    );
  }

  @override
  bool updateShouldNotify(LemonadeTheme oldWidget) {
    return data != oldWidget.data;
  }
}

/// {@template LemonadeThemeDataTween}
/// An interpolation between two [LemonadeThemeData]s.
///
/// This class specializes the interpolation of [Tween<LemonadeThemeData>] to
/// call the [LemonadeThemeData.lerp] method.
///
/// See [Tween] for a discussion on how to use interpolation objects.
/// {@endtemplate}
class LemonadeThemeDataTween extends Tween<LemonadeThemeData> {
  /// {@macro LemonadeThemeDataTween}
  ///
  /// The [begin] and [end] properties must be non-null before the tween is
  /// first used, but the arguments can be null if the values are going to be
  /// filled in later.
  LemonadeThemeDataTween({super.begin, super.end});

  @override
  LemonadeThemeData lerp(double t) => LemonadeThemeData.lerp(begin!, end!, t);
}

/// {@template LemonadeAnimatedTheme}
/// Animated version of [LemonadeTheme] which automatically transitions the
/// theme properties over a given duration whenever the given theme changes.
/// {@endtemplate}
class LemonadeAnimatedTheme extends ImplicitlyAnimatedWidget {
  /// {@macro LemonadeAnimatedTheme}
  ///
  /// By default, the theme transition uses a linear curve and lasts 200ms.
  const LemonadeAnimatedTheme({
    required this.data,
    required this.child,
    super.duration = const Duration(milliseconds: 200),
    super.curve,
    super.onEnd,
    super.key,
  });

  /// Specifies the theme data values for descendant widgets.
  final LemonadeThemeData data;

  /// The widget below this widget in the tree.
  final Widget child;

  @override
  AnimatedWidgetBaseState<LemonadeAnimatedTheme> createState() =>
      _LemonadeAnimatedThemeState();
}

class _LemonadeAnimatedThemeState
    extends AnimatedWidgetBaseState<LemonadeAnimatedTheme> {
  LemonadeThemeDataTween? _data;

  @override
  void forEachTween(TweenVisitor<dynamic> visitor) {
    _data =
        visitor(
              _data,
              widget.data,
              (dynamic value) => LemonadeThemeDataTween(
                begin: value as LemonadeThemeData,
              ),
            )!
            as LemonadeThemeDataTween;
  }

  @override
  Widget build(BuildContext context) {
    return LemonadeTheme(
      data: _data!.evaluate(animation),
      child: widget.child,
    );
  }
}
