import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeSpinnerTheme}
/// Theme configuration for [LemonadeSpinner] component.
///
/// This class provides styling options for customizing the appearance
/// of spinner components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeSpinnerTheme {
  /// {@macro LemonadeSpinnerTheme}
  const LemonadeSpinnerTheme({
    required this.xSmallSize,
    required this.smallSize,
    required this.mediumSize,
    required this.largeSize,
    required this.xLargeSize,
    required this.xxLargeSize,
    required this.xxxLargeSize,
    required this.strokeWidth,
    required this.color,
  });

  /// Creates a [LemonadeSpinnerTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeSpinnerTheme.from(LemonadeTokens tokens) {
    return LemonadeSpinnerTheme(
      xSmallSize: tokens.sizes.size300,
      smallSize: tokens.sizes.size400,
      mediumSize: tokens.sizes.size500,
      largeSize: tokens.sizes.size600,
      xLargeSize: tokens.sizes.size800,
      xxLargeSize: tokens.sizes.size1000,
      xxxLargeSize: tokens.sizes.size1200,
      strokeWidth: tokens.border.base.border50,
      color: tokens.colors.content.contentSecondary,
    );
  }

  /// Linearly interpolates between two [LemonadeSpinnerTheme] objects.
  factory LemonadeSpinnerTheme.lerp(
    LemonadeSpinnerTheme a,
    LemonadeSpinnerTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeSpinnerTheme(
      xSmallSize: lerpDouble(a.xSmallSize, b.xSmallSize, t)!,
      smallSize: lerpDouble(a.smallSize, b.smallSize, t)!,
      mediumSize: lerpDouble(a.mediumSize, b.mediumSize, t)!,
      largeSize: lerpDouble(a.largeSize, b.largeSize, t)!,
      xLargeSize: lerpDouble(a.xLargeSize, b.xLargeSize, t)!,
      xxLargeSize: lerpDouble(a.xxLargeSize, b.xxLargeSize, t)!,
      xxxLargeSize: lerpDouble(a.xxxLargeSize, b.xxxLargeSize, t)!,
      strokeWidth: lerpDouble(a.strokeWidth, b.strokeWidth, t)!,
      color: Color.lerp(a.color, b.color, t)!,
    );
  }

  /// Size for [LemonadeSpinnerSize.xSmall].
  final double xSmallSize;

  /// Size for [LemonadeSpinnerSize.small].
  final double smallSize;

  /// Size for [LemonadeSpinnerSize.medium].
  final double mediumSize;

  /// Size for [LemonadeSpinnerSize.large].
  final double largeSize;

  /// Size for [LemonadeSpinnerSize.xLarge].
  final double xLargeSize;

  /// Size for [LemonadeSpinnerSize.xxLarge].
  final double xxLargeSize;

  /// Size for [LemonadeSpinnerSize.xxxLarge].
  final double xxxLargeSize;

  /// The stroke width of the spinner arc.
  final double strokeWidth;

  /// The default color of the spinner.
  final Color color;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeSpinnerTheme copyWith({
    double? xSmallSize,
    double? smallSize,
    double? mediumSize,
    double? largeSize,
    double? xLargeSize,
    double? xxLargeSize,
    double? xxxLargeSize,
    double? strokeWidth,
    Color? color,
  }) {
    return LemonadeSpinnerTheme(
      xSmallSize: xSmallSize ?? this.xSmallSize,
      smallSize: smallSize ?? this.smallSize,
      mediumSize: mediumSize ?? this.mediumSize,
      largeSize: largeSize ?? this.largeSize,
      xLargeSize: xLargeSize ?? this.xLargeSize,
      xxLargeSize: xxLargeSize ?? this.xxLargeSize,
      xxxLargeSize: xxxLargeSize ?? this.xxxLargeSize,
      strokeWidth: strokeWidth ?? this.strokeWidth,
      color: color ?? this.color,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeSpinnerTheme mergeWith(LemonadeSpinnerTheme? other) {
    if (other == null) return this;
    return copyWith(
      xSmallSize: other.xSmallSize,
      smallSize: other.smallSize,
      mediumSize: other.mediumSize,
      largeSize: other.largeSize,
      xLargeSize: other.xLargeSize,
      xxLargeSize: other.xxLargeSize,
      xxxLargeSize: other.xxxLargeSize,
      strokeWidth: other.strokeWidth,
      color: other.color,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeSpinnerTheme &&
        other.xSmallSize == xSmallSize &&
        other.smallSize == smallSize &&
        other.mediumSize == mediumSize &&
        other.largeSize == largeSize &&
        other.xLargeSize == xLargeSize &&
        other.xxLargeSize == xxLargeSize &&
        other.xxxLargeSize == xxxLargeSize &&
        other.strokeWidth == strokeWidth &&
        other.color == color;
  }

  @override
  int get hashCode {
    return Object.hash(
      xSmallSize,
      smallSize,
      mediumSize,
      largeSize,
      xLargeSize,
      xxLargeSize,
      xxxLargeSize,
      strokeWidth,
      color,
    );
  }
}
