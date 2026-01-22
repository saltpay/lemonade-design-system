import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeButtonTheme}
/// Theme configuration for [LemonadeButton] component.
///
/// This class provides styling options for customizing the appearance
/// of button components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeButtonTheme {
  /// {@macro LemonadeButtonTheme}
  const LemonadeButtonTheme({
    required this.smallHeight,
    required this.smallMinWidth,
    required this.mediumHeight,
    required this.mediumMinWidth,
    required this.largeHeight,
    required this.largeMinWidth,
  });

  /// Creates a [LemonadeButtonTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeButtonTheme.from(LemonadeTokens tokens) {
    return LemonadeButtonTheme(
      smallHeight: tokens.sizes.size1000,
      smallMinWidth: tokens.sizes.size1600,
      mediumHeight: tokens.sizes.size1200,
      mediumMinWidth: tokens.sizes.size1600,
      largeHeight: tokens.sizes.size1400,
      largeMinWidth: tokens.sizes.size1600,
    );
  }

  /// Linearly interpolates between two [LemonadeButtonTheme] objects.
  factory LemonadeButtonTheme.lerp(
    LemonadeButtonTheme a,
    LemonadeButtonTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeButtonTheme(
      smallHeight: lerpDouble(a.smallHeight, b.smallHeight, t)!,
      smallMinWidth: lerpDouble(a.smallMinWidth, b.smallMinWidth, t)!,
      mediumHeight: lerpDouble(a.mediumHeight, b.mediumHeight, t)!,
      mediumMinWidth: lerpDouble(a.mediumMinWidth, b.mediumMinWidth, t)!,
      largeHeight: lerpDouble(a.largeHeight, b.largeHeight, t)!,
      largeMinWidth: lerpDouble(a.largeMinWidth, b.largeMinWidth, t)!,
    );
  }

  /// The height of a small button.
  final double smallHeight;

  /// The minimum width of a small button.
  final double smallMinWidth;

  /// The height of a medium button.
  final double mediumHeight;

  /// The minimum width of a medium button.
  final double mediumMinWidth;

  /// The height of a large button.
  final double largeHeight;

  /// The minimum width of a large button.
  final double largeMinWidth;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeButtonTheme copyWith({
    double? smallHeight,
    double? smallMinWidth,
    double? mediumHeight,
    double? mediumMinWidth,
    double? largeHeight,
    double? largeMinWidth,
  }) {
    return LemonadeButtonTheme(
      smallHeight: smallHeight ?? this.smallHeight,
      smallMinWidth: smallMinWidth ?? this.smallMinWidth,
      mediumHeight: mediumHeight ?? this.mediumHeight,
      mediumMinWidth: mediumMinWidth ?? this.mediumMinWidth,
      largeHeight: largeHeight ?? this.largeHeight,
      largeMinWidth: largeMinWidth ?? this.largeMinWidth,
    );
  }

  /// Merges this theme with another theme.
  LemonadeButtonTheme mergeWith(LemonadeButtonTheme? other) {
    if (other == null) return this;
    return copyWith(
      smallHeight: other.smallHeight,
      smallMinWidth: other.smallMinWidth,
      mediumHeight: other.mediumHeight,
      mediumMinWidth: other.mediumMinWidth,
      largeHeight: other.largeHeight,
      largeMinWidth: other.largeMinWidth,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeButtonTheme &&
        other.smallHeight == smallHeight &&
        other.smallMinWidth == smallMinWidth &&
        other.mediumHeight == mediumHeight &&
        other.mediumMinWidth == mediumMinWidth &&
        other.largeHeight == largeHeight &&
        other.largeMinWidth == largeMinWidth;
  }

  @override
  int get hashCode => Object.hash(
    smallHeight,
    smallMinWidth,
    mediumHeight,
    mediumMinWidth,
    largeHeight,
    largeMinWidth,
  );
}
