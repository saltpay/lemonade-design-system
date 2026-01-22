import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeBrandLogoTheme}
/// Theme configuration for [LemonadeBrandLogo] component.
///
/// This class provides styling options for customizing the appearance
/// of brand logo components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeBrandLogoTheme {
  /// {@macro LemonadeBrandLogoTheme}
  const LemonadeBrandLogoTheme({
    required this.smallSize,
    required this.mediumSize,
    required this.largeSize,
    required this.xLargeSize,
    required this.xxLargeSize,
  });

  /// Creates a [LemonadeBrandLogoTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeBrandLogoTheme.from(LemonadeTokens tokens) {
    return LemonadeBrandLogoTheme(
      smallSize: tokens.sizes.size400,
      mediumSize: tokens.sizes.size500,
      largeSize: tokens.sizes.size600,
      xLargeSize: tokens.sizes.size800,
      xxLargeSize: tokens.sizes.size1000,
    );
  }

  /// Linearly interpolates between two [LemonadeBrandLogoTheme] objects.
  factory LemonadeBrandLogoTheme.lerp(
    LemonadeBrandLogoTheme a,
    LemonadeBrandLogoTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeBrandLogoTheme(
      smallSize: lerpDouble(a.smallSize, b.smallSize, t)!,
      mediumSize: lerpDouble(a.mediumSize, b.mediumSize, t)!,
      largeSize: lerpDouble(a.largeSize, b.largeSize, t)!,
      xLargeSize: lerpDouble(a.xLargeSize, b.xLargeSize, t)!,
      xxLargeSize: lerpDouble(a.xxLargeSize, b.xxLargeSize, t)!,
    );
  }

  /// Size for [LemonadeBrandLogoSize.small].
  final double smallSize;

  /// Size for [LemonadeBrandLogoSize.medium].
  final double mediumSize;

  /// Size for [LemonadeBrandLogoSize.large].
  final double largeSize;

  /// Size for [LemonadeBrandLogoSize.xLarge].
  final double xLargeSize;

  /// Size for [LemonadeBrandLogoSize.xxLarge].
  final double xxLargeSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeBrandLogoTheme copyWith({
    double? smallSize,
    double? mediumSize,
    double? largeSize,
    double? xLargeSize,
    double? xxLargeSize,
    double? xxxLargeSize,
  }) {
    return LemonadeBrandLogoTheme(
      smallSize: smallSize ?? this.smallSize,
      mediumSize: mediumSize ?? this.mediumSize,
      largeSize: largeSize ?? this.largeSize,
      xLargeSize: xLargeSize ?? this.xLargeSize,
      xxLargeSize: xxLargeSize ?? this.xxLargeSize,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeBrandLogoTheme mergeWith(LemonadeBrandLogoTheme? other) {
    if (other == null) return this;
    return copyWith(
      smallSize: other.smallSize,
      mediumSize: other.mediumSize,
      largeSize: other.largeSize,
      xLargeSize: other.xLargeSize,
      xxLargeSize: other.xxLargeSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LemonadeBrandLogoTheme &&
        other.smallSize == smallSize &&
        other.mediumSize == mediumSize &&
        other.largeSize == largeSize &&
        other.xLargeSize == xLargeSize &&
        other.xxLargeSize == xxLargeSize;
  }

  @override
  int get hashCode {
    return Object.hash(
      smallSize,
      mediumSize,
      largeSize,
      xLargeSize,
      xxLargeSize,
    );
  }
}
