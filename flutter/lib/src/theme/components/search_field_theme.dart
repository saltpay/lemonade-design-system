import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeSearchFieldTheme}
/// Theme configuration for [LemonadeSearchField] component.
///
/// This class provides styling options for customizing the appearance
/// of search field components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeSearchFieldTheme {
  /// {@macro LemonadeSearchFieldTheme}
  const LemonadeSearchFieldTheme({
    required this.height,
    required this.borderWidth,
    required this.focusBorderWidth,
  });

  /// Creates a [LemonadeSearchFieldTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeSearchFieldTheme.from(LemonadeTokens tokens) {
    return LemonadeSearchFieldTheme(
      height: tokens.sizes.size1100,
      borderWidth: tokens.border.state.borderSelected,
      focusBorderWidth: tokens.border.base.border50,
    );
  }

  /// Linearly interpolates between two [LemonadeSearchFieldTheme] objects.
  factory LemonadeSearchFieldTheme.lerp(
    LemonadeSearchFieldTheme a,
    LemonadeSearchFieldTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeSearchFieldTheme(
      height: lerpDouble(a.height, b.height, t)!,
      borderWidth: lerpDouble(a.borderWidth, b.borderWidth, t)!,
      focusBorderWidth: lerpDouble(a.focusBorderWidth, b.focusBorderWidth, t)!,
    );
  }

  /// The height of the search field.
  final double height;

  /// The border width of the search field.
  final double borderWidth;

  /// The border width when focused.
  final double focusBorderWidth;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeSearchFieldTheme copyWith({
    double? height,
    double? borderWidth,
    double? focusBorderWidth,
  }) {
    return LemonadeSearchFieldTheme(
      height: height ?? this.height,
      borderWidth: borderWidth ?? this.borderWidth,
      focusBorderWidth: focusBorderWidth ?? this.focusBorderWidth,
    );
  }

  /// Merges this theme with another theme.
  LemonadeSearchFieldTheme mergeWith(LemonadeSearchFieldTheme? other) {
    if (other == null) return this;
    return copyWith(
      height: other.height,
      borderWidth: other.borderWidth,
      focusBorderWidth: other.focusBorderWidth,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeSearchFieldTheme &&
        other.height == height &&
        other.borderWidth == borderWidth &&
        other.focusBorderWidth == focusBorderWidth;
  }

  @override
  int get hashCode => Object.hash(height, borderWidth, focusBorderWidth);
}
