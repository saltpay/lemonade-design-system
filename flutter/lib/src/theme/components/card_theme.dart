import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeCardTheme}
/// Theme configuration for [LemonadeCard] component.
///
/// This class provides styling options for customizing the appearance
/// of card components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeCardTheme {
  /// {@macro LemonadeCardTheme}
  const LemonadeCardTheme({
    required this.borderRadius,
  });

  /// Creates a [LemonadeCardTheme] with default values
  /// based on the provided [tokens].
  factory LemonadeCardTheme.from(LemonadeTokens tokens) {
    return LemonadeCardTheme(
      borderRadius: tokens.radius.radius400,
    );
  }

  /// Linearly interpolates between two [LemonadeCardTheme] objects.
  factory LemonadeCardTheme.lerp(
    LemonadeCardTheme a,
    LemonadeCardTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeCardTheme(
      borderRadius: lerpDouble(a.borderRadius, b.borderRadius, t)!,
    );
  }

  /// The border radius of the card.
  final double borderRadius;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeCardTheme copyWith({
    double? borderRadius,
  }) {
    return LemonadeCardTheme(
      borderRadius: borderRadius ?? this.borderRadius,
    );
  }

  /// Merges this theme with another theme.
  LemonadeCardTheme mergeWith(LemonadeCardTheme? other) {
    if (other == null) return this;
    return copyWith(
      borderRadius: other.borderRadius,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeCardTheme && other.borderRadius == borderRadius;
  }

  @override
  int get hashCode => borderRadius.hashCode;
}
