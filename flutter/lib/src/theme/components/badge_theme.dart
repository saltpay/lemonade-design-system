import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeBadgeTheme}
/// Theme configuration for [LemonadeBadge] component.
///
/// This class provides styling options for customizing the appearance
/// of badge components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeBadgeTheme {
  /// {@macro LemonadeBadgeTheme}
  const LemonadeBadgeTheme({
    required this.xSmallVerticalPadding,
    required this.xSmallHorizontalPadding,
    required this.xSmallFontSize,
    required this.smallVerticalPadding,
    required this.smallHorizontalPadding,
    required this.smallFontSize,
  });

  /// Creates a [LemonadeBadgeTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeBadgeTheme.from(LemonadeTokens tokens) {
    return LemonadeBadgeTheme(
      xSmallVerticalPadding: tokens.spaces.spacing50,
      xSmallHorizontalPadding: tokens.spaces.spacing50,
      xSmallFontSize: tokens.sizes.size250,
      smallVerticalPadding: tokens.spaces.spacing50,
      smallHorizontalPadding: tokens.spaces.spacing100,
      smallFontSize: tokens.sizes.size300,
    );
  }

  /// Linearly interpolates between two [LemonadeBadgeTheme] objects.
  factory LemonadeBadgeTheme.lerp(
    LemonadeBadgeTheme a,
    LemonadeBadgeTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeBadgeTheme(
      xSmallVerticalPadding: lerpDouble(
        a.xSmallVerticalPadding,
        b.xSmallVerticalPadding,
        t,
      )!,
      xSmallHorizontalPadding: lerpDouble(
        a.xSmallHorizontalPadding,
        b.xSmallHorizontalPadding,
        t,
      )!,
      xSmallFontSize: lerpDouble(a.xSmallFontSize, b.xSmallFontSize, t)!,
      smallVerticalPadding: lerpDouble(
        a.smallVerticalPadding,
        b.smallVerticalPadding,
        t,
      )!,
      smallHorizontalPadding: lerpDouble(
        a.smallHorizontalPadding,
        b.smallHorizontalPadding,
        t,
      )!,
      smallFontSize: lerpDouble(a.smallFontSize, b.smallFontSize, t)!,
    );
  }

  /// Vertical padding for [LemonadeBadgeSize.xSmall].
  final double xSmallVerticalPadding;

  /// Horizontal padding for [LemonadeBadgeSize.xSmall].
  final double xSmallHorizontalPadding;

  /// Font size for [LemonadeBadgeSize.xSmall].
  final double xSmallFontSize;

  /// Vertical padding for [LemonadeBadgeSize.small].
  final double smallVerticalPadding;

  /// Horizontal padding for [LemonadeBadgeSize.small].
  final double smallHorizontalPadding;

  /// Font size for [LemonadeBadgeSize.small].
  final double smallFontSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeBadgeTheme copyWith({
    double? xSmallVerticalPadding,
    double? xSmallHorizontalPadding,
    double? xSmallFontSize,
    double? smallVerticalPadding,
    double? smallHorizontalPadding,
    double? smallFontSize,
  }) {
    return LemonadeBadgeTheme(
      xSmallVerticalPadding:
          xSmallVerticalPadding ?? this.xSmallVerticalPadding,
      xSmallHorizontalPadding:
          xSmallHorizontalPadding ?? this.xSmallHorizontalPadding,
      xSmallFontSize: xSmallFontSize ?? this.xSmallFontSize,
      smallVerticalPadding: smallVerticalPadding ?? this.smallVerticalPadding,
      smallHorizontalPadding:
          smallHorizontalPadding ?? this.smallHorizontalPadding,
      smallFontSize: smallFontSize ?? this.smallFontSize,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeBadgeTheme mergeWith(LemonadeBadgeTheme? other) {
    if (other == null) return this;
    return copyWith(
      xSmallVerticalPadding: other.xSmallVerticalPadding,
      xSmallHorizontalPadding: other.xSmallHorizontalPadding,
      xSmallFontSize: other.xSmallFontSize,
      smallVerticalPadding: other.smallVerticalPadding,
      smallHorizontalPadding: other.smallHorizontalPadding,
      smallFontSize: other.smallFontSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeBadgeTheme &&
        other.xSmallVerticalPadding == xSmallVerticalPadding &&
        other.xSmallHorizontalPadding == xSmallHorizontalPadding &&
        other.xSmallFontSize == xSmallFontSize &&
        other.smallVerticalPadding == smallVerticalPadding &&
        other.smallHorizontalPadding == smallHorizontalPadding &&
        other.smallFontSize == smallFontSize;
  }

  @override
  int get hashCode {
    return Object.hash(
      xSmallVerticalPadding,
      xSmallHorizontalPadding,
      xSmallFontSize,
      smallVerticalPadding,
      smallHorizontalPadding,
      smallFontSize,
    );
  }
}
