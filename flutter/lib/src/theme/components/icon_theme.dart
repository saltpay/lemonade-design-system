import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeIconTheme}
/// Theme configuration for [LemonadeIcon] component.
///
/// This class provides styling options for customizing the appearance
/// of icon components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeIconTheme {
  /// {@macro LemonadeIconTheme}
  const LemonadeIconTheme({
    required this.color,
    required this.xSmallSize,
    required this.smallSize,
    required this.mediumSize,
    required this.largeSize,
    required this.xLargeSize,
  });

  /// Creates a [LemonadeIconTheme] with default sizes based on the provided
  /// [tokens].
  factory LemonadeIconTheme.from(LemonadeTokens tokens) {
    return LemonadeIconTheme(
      color: tokens.colors.content.contentPrimary,
      xSmallSize: tokens.sizes.size300,
      smallSize: tokens.sizes.size400,
      mediumSize: tokens.sizes.size500,
      largeSize: tokens.sizes.size600,
      xLargeSize: tokens.sizes.size800,
    );
  }

  /// Linearly interpolates between two [LemonadeIconTheme] objects.
  factory LemonadeIconTheme.lerp(
    LemonadeIconTheme a,
    LemonadeIconTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeIconTheme(
      color: Color.lerp(a.color, b.color, t)!,
      xSmallSize: lerpDouble(a.xSmallSize, b.xSmallSize, t)!,
      smallSize: lerpDouble(a.smallSize, b.smallSize, t)!,
      mediumSize: lerpDouble(a.mediumSize, b.mediumSize, t)!,
      largeSize: lerpDouble(a.largeSize, b.largeSize, t)!,
      xLargeSize: lerpDouble(a.xLargeSize, b.xLargeSize, t)!,
    );
  }

  /// Default color for icons.
  /// If not specified, defaults to [LemonadeContentColors.contentPrimary].
  final Color color;

  /// Size for [LemonadeIconSize.xSmall].
  final double xSmallSize;

  /// Size for [LemonadeIconSize.small].
  final double smallSize;

  /// Size for [LemonadeIconSize.medium].
  final double mediumSize;

  /// Size for [LemonadeIconSize.large].
  final double largeSize;

  /// Size for [LemonadeIconSize.xLarge].
  final double xLargeSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeIconTheme copyWith({
    Color? color,
    double? xSmallSize,
    double? smallSize,
    double? mediumSize,
    double? largeSize,
    double? xLargeSize,
  }) {
    return LemonadeIconTheme(
      color: color ?? this.color,
      xSmallSize: xSmallSize ?? this.xSmallSize,
      smallSize: smallSize ?? this.smallSize,
      mediumSize: mediumSize ?? this.mediumSize,
      largeSize: largeSize ?? this.largeSize,
      xLargeSize: xLargeSize ?? this.xLargeSize,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeIconTheme mergeWith(LemonadeIconTheme? other) {
    if (other == null) return this;
    return copyWith(
      color: other.color,
      xSmallSize: other.xSmallSize,
      smallSize: other.smallSize,
      mediumSize: other.mediumSize,
      largeSize: other.largeSize,
      xLargeSize: other.xLargeSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LemonadeIconTheme &&
        other.color == color &&
        other.xSmallSize == xSmallSize &&
        other.smallSize == smallSize &&
        other.mediumSize == mediumSize &&
        other.largeSize == largeSize &&
        other.xLargeSize == xLargeSize;
  }

  @override
  int get hashCode {
    return Object.hash(
      color,
      xSmallSize,
      smallSize,
      mediumSize,
      largeSize,
      xLargeSize,
    );
  }
}
