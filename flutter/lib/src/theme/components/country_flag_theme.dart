import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeCountryFlagTheme}
/// Theme configuration for [LemonadeCountryFlag] component.
///
/// This class provides styling options for customizing the appearance
/// of country flag components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeCountryFlagTheme {
  /// {@macro LemonadeCountryFlagTheme}
  const LemonadeCountryFlagTheme({
    required this.borderColor,
    required this.borderWidth,
    required this.smallSize,
    required this.mediumSize,
    required this.largeSize,
    required this.xLargeSize,
    required this.xxLargeSize,
    required this.xxxLargeSize,
  });

  /// Creates a [LemonadeCountryFlagTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeCountryFlagTheme.from(LemonadeTokens tokens) {
    return LemonadeCountryFlagTheme(
      borderColor: tokens.colors.border.borderNeutralMedium,
      borderWidth: tokens.border.base.border25,
      smallSize: tokens.sizes.size400,
      mediumSize: tokens.sizes.size500,
      largeSize: tokens.sizes.size600,
      xLargeSize: tokens.sizes.size800,
      xxLargeSize: tokens.sizes.size1000,
      xxxLargeSize: tokens.sizes.size1200,
    );
  }

  /// Linearly interpolates between two [LemonadeCountryFlagTheme] objects.
  factory LemonadeCountryFlagTheme.lerp(
    LemonadeCountryFlagTheme a,
    LemonadeCountryFlagTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeCountryFlagTheme(
      borderColor: Color.lerp(a.borderColor, b.borderColor, t)!,
      borderWidth: lerpDouble(a.borderWidth, b.borderWidth, t)!,
      smallSize: lerpDouble(a.smallSize, b.smallSize, t)!,
      mediumSize: lerpDouble(a.mediumSize, b.mediumSize, t)!,
      largeSize: lerpDouble(a.largeSize, b.largeSize, t)!,
      xLargeSize: lerpDouble(a.xLargeSize, b.xLargeSize, t)!,
      xxLargeSize: lerpDouble(a.xxLargeSize, b.xxLargeSize, t)!,
      xxxLargeSize: lerpDouble(a.xxxLargeSize, b.xxxLargeSize, t)!,
    );
  }

  /// Default border color for country flags.
  final Color borderColor;

  /// Default border width for country flags.
  final double borderWidth;

  /// Size for [LemonadeFlagSize.small].
  final double smallSize;

  /// Size for [LemonadeFlagSize.medium].
  final double mediumSize;

  /// Size for [LemonadeFlagSize.large].
  final double largeSize;

  /// Size for [LemonadeFlagSize.xLarge].
  final double xLargeSize;

  /// Size for [LemonadeFlagSize.xxLarge].
  final double xxLargeSize;

  /// Size for [LemonadeFlagSize.xxxLarge].
  final double xxxLargeSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeCountryFlagTheme copyWith({
    Color? borderColor,
    double? borderWidth,
    double? smallSize,
    double? mediumSize,
    double? largeSize,
    double? xLargeSize,
    double? xxLargeSize,
    double? xxxLargeSize,
  }) {
    return LemonadeCountryFlagTheme(
      borderColor: borderColor ?? this.borderColor,
      borderWidth: borderWidth ?? this.borderWidth,
      smallSize: smallSize ?? this.smallSize,
      mediumSize: mediumSize ?? this.mediumSize,
      largeSize: largeSize ?? this.largeSize,
      xLargeSize: xLargeSize ?? this.xLargeSize,
      xxLargeSize: xxLargeSize ?? this.xxLargeSize,
      xxxLargeSize: xxxLargeSize ?? this.xxxLargeSize,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeCountryFlagTheme mergeWith(LemonadeCountryFlagTheme? other) {
    if (other == null) return this;
    return copyWith(
      borderColor: other.borderColor,
      borderWidth: other.borderWidth,
      smallSize: other.smallSize,
      mediumSize: other.mediumSize,
      largeSize: other.largeSize,
      xLargeSize: other.xLargeSize,
      xxLargeSize: other.xxLargeSize,
      xxxLargeSize: other.xxxLargeSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LemonadeCountryFlagTheme &&
        other.borderColor == borderColor &&
        other.borderWidth == borderWidth &&
        other.smallSize == smallSize &&
        other.mediumSize == mediumSize &&
        other.largeSize == largeSize &&
        other.xLargeSize == xLargeSize &&
        other.xxLargeSize == xxLargeSize &&
        other.xxxLargeSize == xxxLargeSize;
  }

  @override
  int get hashCode {
    return Object.hash(
      borderColor,
      borderWidth,
      smallSize,
      mediumSize,
      largeSize,
      xLargeSize,
      xxLargeSize,
      xxxLargeSize,
    );
  }
}
