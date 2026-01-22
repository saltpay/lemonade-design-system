import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeThemeData}
/// Configuration data for the Lemonade Design System theme.
///
/// A [LemonadeThemeData] object can be used to configure the appearance
/// of widgets throughout the app using the [LemonadeTheme] widget.
/// {@endtemplate}
@immutable
class LemonadeThemeData {
  /// {@macro LemonadeThemeData}
  ///
  /// If any parameter is null, a default value will be used.
  factory LemonadeThemeData({
    LemonadeSemanticColors? colors,
    LemonadeTypography? typography,
    LemonadeOpacity? opacity,
    LemonadeRadius? radius,
    LemonadeShapes? shapes,
    LemonadeSpaces? spaces,
    LemonadeSizes? sizes,
    LemonadeShadows? shadows,
    LemonadeBorder? border,
    LemonadeComponents? components,
    Brightness brightness = Brightness.light,
  }) {
    final tokens = LemonadeTokens(
      border: border ?? const LemonadeBorder(),
      colors: colors ?? const LemonadeLightColors(),
      opacity: opacity ?? const LemonadeOpacity(),
      radius: radius ?? const LemonadeRadius(),
      shapes: shapes ?? const LemonadeShapes(),
      shadows: shadows ?? const LemonadeShadows(),
      spaces: spaces ?? const LemonadeSpaces(),
      sizes: sizes ?? const LemonadeSizes(),
      typography: typography ?? const LemonadeTypography(),
    );

    return LemonadeThemeData._(
      brightness: brightness,
      border: tokens.border,
      colors: tokens.colors,
      components: LemonadeComponents.from(tokens).mergeWith(components),
      opacity: tokens.opacity,
      radius: tokens.radius,
      shapes: tokens.shapes,
      shadows: tokens.shadows,
      spaces: tokens.spaces,
      sizes: tokens.sizes,
      typography: tokens.typography,
    );
  }

  /// Linearly interpolates between two [LemonadeThemeData] objects.
  ///
  /// The interpolation will typically be performed on colors, and the
  /// [brightness], [typography], [spaces], and [shadows] will be
  /// taken from [b].
  factory LemonadeThemeData.lerp(
    LemonadeThemeData a,
    LemonadeThemeData b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeThemeData._(
      brightness: b.brightness,
      colors: LemonadeSemanticColors.lerp(a.colors, b.colors, t),
      typography: LemonadeTypography.lerp(a.typography, b.typography, t),
      opacity: LemonadeOpacity.lerp(a.opacity, b.opacity, t),
      spaces: LemonadeSpaces.lerp(a.spaces, b.spaces, t),
      sizes: LemonadeSizes.lerp(a.sizes, b.sizes, t),
      radius: LemonadeRadius.lerp(a.radius, b.radius, t),
      shapes: LemonadeShapes.lerp(a.shapes, b.shapes, t),
      shadows: LemonadeShadows.lerp(a.shadows, b.shadows, t),
      border: LemonadeBorder.lerp(a.border, b.border, t),
      components: LemonadeComponents.lerp(a.components, b.components, t),
    );
  }

  /// Creates a [LemonadeThemeData] with the specified configuration.
  const LemonadeThemeData._({
    required this.colors,
    required this.brightness,
    required this.typography,
    required this.opacity,
    required this.radius,
    required this.shapes,
    required this.spaces,
    required this.sizes,
    required this.shadows,
    required this.border,
    required this.components,
  });

  /// The semantic colors to use throughout the app.
  final LemonadeSemanticColors colors;

  /// The overall brightness of this theme.
  final Brightness brightness;

  /// The typography configuration for text styles.
  final LemonadeTypography typography;

  /// The opacity configuration for transparency levels.
  final LemonadeOpacity opacity;

  /// The radius configuration for shapes.
  final LemonadeRadius radius;

  /// The shape configuration for the Lemonade Design System.
  final LemonadeShapes shapes;

  /// The spacing configuration for layout.
  final LemonadeSpaces spaces;

  /// The size configuration for layout.
  final LemonadeSizes sizes;

  /// The shadow configuration for elevation.
  final LemonadeShadows shadows;

  /// The border configuration for borders and outlines.
  final LemonadeBorder border;

  /// Component-specific theme configurations.
  final LemonadeComponents components;

  /// Creates a copy of this theme data with the given fields replaced with
  /// the new values.
  LemonadeThemeData copyWith({
    LemonadeSemanticColors? colors,
    Brightness? brightness,
    LemonadeTypography? typography,
    LemonadeOpacity? opacity,
    LemonadeRadius? radius,
    LemonadeShapes? shapes,
    LemonadeSpaces? spaces,
    LemonadeSizes? sizes,
    LemonadeShadows? shadows,
    LemonadeBorder? border,
    LemonadeComponents? components,
  }) {
    return LemonadeThemeData._(
      colors: colors ?? this.colors,
      brightness: brightness ?? this.brightness,
      typography: typography ?? this.typography,
      opacity: opacity ?? this.opacity,
      radius: radius ?? this.radius,
      shapes: shapes ?? this.shapes,
      spaces: spaces ?? this.spaces,
      sizes: sizes ?? this.sizes,
      shadows: shadows ?? this.shadows,
      border: border ?? this.border,
      components: components ?? this.components,
    );
  }

  /// Merges this theme data with another theme data.
  ///
  /// If [other] is null, returns this theme data unchanged.
  /// Otherwise, returns a new theme data where any non-null fields from
  /// [other] override the corresponding fields in this theme data.
  LemonadeThemeData merge(LemonadeThemeData? other) {
    if (other == null) return this;
    return copyWith(
      colors: other.colors,
      brightness: other.brightness,
      typography: other.typography,
      opacity: other.opacity,
      radius: other.radius,
      shapes: other.shapes,
      spaces: other.spaces,
      sizes: other.sizes,
      shadows: other.shadows,
      border: other.border,
      components: other.components,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    return other is LemonadeThemeData &&
        other.colors == colors &&
        other.brightness == brightness &&
        other.typography == typography &&
        other.opacity == opacity &&
        other.radius == radius &&
        other.shapes == shapes &&
        other.spaces == spaces &&
        other.sizes == sizes &&
        other.shadows == shadows &&
        other.border == border &&
        other.components == components;
  }

  @override
  int get hashCode => Object.hash(
    colors,
    brightness,
    typography,
    opacity,
    radius,
    shapes,
    spaces,
    sizes,
    shadows,
    border,
    components,
  );
}
