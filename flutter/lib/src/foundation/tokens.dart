import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeTokens}
/// Core design tokens for the Lemonade Design System.
///
/// This class aggregates all the fundamental design tokens (colors, typography,
/// spacing, etc.) that define the visual language of the design system.
/// These tokens are used throughout the system to build component themes,
/// create consistent layouts, and maintain design consistency.
///
/// Design tokens provide a single source of truth for design decisions,
/// making it easy to maintain and update the visual style across the entire
/// application.
/// {@endtemplate}
@immutable
class LemonadeTokens {
  /// {@macro LemonadeTokens}
  const LemonadeTokens({
    required this.border,
    required this.colors,
    required this.opacity,
    required this.radius,
    required this.shapes,
    required this.spaces,
    required this.shadows,
    required this.sizes,
    required this.typography,
  });

  /// Border tokens defining border widths and styles.
  final LemonadeBorder border;

  /// Semantic color tokens defining the color palette.
  final LemonadeSemanticColors colors;

  /// Opacity tokens defining transparency levels.
  final LemonadeOpacity opacity;

  /// Radius tokens defining corner radius values.
  final LemonadeRadius radius;

  /// Shape tokens defining component shapes.
  final LemonadeShapes shapes;

  /// Spacing tokens defining layout spacing and padding values.
  final LemonadeSpaces spaces;

  /// Shadow tokens defining elevation and shadow effects.
  final LemonadeShadows shadows;

  /// Size tokens defining layout sizes.
  final LemonadeSizes sizes;

  /// Typography tokens defining text styles and type scales.
  final LemonadeTypography typography;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeTokens &&
          runtimeType == other.runtimeType &&
          border == other.border &&
          colors == other.colors &&
          opacity == other.opacity &&
          radius == other.radius &&
          shapes == other.shapes &&
          spaces == other.spaces &&
          shadows == other.shadows &&
          sizes == other.sizes &&
          typography == other.typography;

  @override
  int get hashCode => Object.hash(
    border,
    colors,
    opacity,
    radius,
    shapes,
    spaces,
    shadows,
    sizes,
    typography,
  );
}
