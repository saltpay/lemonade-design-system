/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Opacity values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Opacity configuration for the Lemonade Design System.
///
/// Provides a consistent and scalable way to manage opacity across interfaces.
/// These values can be used for disabled components and general transparency handling.
@immutable
class LemonadeOpacity {
  /// Creates a [LemonadeOpacity] configuration.
  const LemonadeOpacity({
    this.base = const LemonadeBaseOpacity(),
    this.state = const LemonadeStateOpacity(),
  });

  /// Linearly interpolates between two [LemonadeOpacity] objects.
  /// If they are identical, returns [a].
  factory LemonadeOpacity.lerp(
    LemonadeOpacity a,
    LemonadeOpacity b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeOpacity(
      base: LemonadeBaseOpacity.lerp(a.base, b.base, t),
      state: LemonadeStateOpacity.lerp(a.state, b.state, t),
    );
  }

  /// Base opacity values
  final LemonadeBaseOpacity base;

  /// State opacity values
  final LemonadeStateOpacity state;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeOpacity &&
          runtimeType == other.runtimeType &&
          base == other.base &&
          state == other.state;

  @override
  int get hashCode => Object.hash(
    base,
    state,
  );

  /// Helper method to access [LemonadeOpacity] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeOpacity of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.opacity;
  }
}

/// Base opacity values for consistent transparency handling
@immutable
class LemonadeBaseOpacity {
  /// Creates a [LemonadeBaseOpacity] configuration.
  const LemonadeBaseOpacity({
    this.opacity0 = 0.0,
    this.opacity5 = 0.05,
    this.opacity10 = 0.1,
    this.opacity20 = 0.2,
    this.opacity30 = 0.3,
    this.opacity40 = 0.4,
    this.opacity50 = 0.5,
    this.opacity60 = 0.6,
    this.opacity70 = 0.7,
    this.opacity80 = 0.8,
    this.opacity90 = 0.9,
    this.opacity100 = 1.0,
  });

  /// Linearly interpolates between two [LemonadeBaseOpacity] objects.
  /// If they are identical, returns [a].
  factory LemonadeBaseOpacity.lerp(
    LemonadeBaseOpacity a,
    LemonadeBaseOpacity b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeBaseOpacity(
      opacity0: lerpDouble(a.opacity0, b.opacity0, t)!,
      opacity5: lerpDouble(a.opacity5, b.opacity5, t)!,
      opacity10: lerpDouble(a.opacity10, b.opacity10, t)!,
      opacity20: lerpDouble(a.opacity20, b.opacity20, t)!,
      opacity30: lerpDouble(a.opacity30, b.opacity30, t)!,
      opacity40: lerpDouble(a.opacity40, b.opacity40, t)!,
      opacity50: lerpDouble(a.opacity50, b.opacity50, t)!,
      opacity60: lerpDouble(a.opacity60, b.opacity60, t)!,
      opacity70: lerpDouble(a.opacity70, b.opacity70, t)!,
      opacity80: lerpDouble(a.opacity80, b.opacity80, t)!,
      opacity90: lerpDouble(a.opacity90, b.opacity90, t)!,
      opacity100: lerpDouble(a.opacity100, b.opacity100, t)!,
    );
  }

  /// Opacity value of 0% from token `opacity0`
  final double opacity0;

  /// Opacity value of 5% from token `opacity5`
  final double opacity5;

  /// Opacity value of 10% from token `opacity10`
  final double opacity10;

  /// Opacity value of 20% from token `opacity20`
  final double opacity20;

  /// Opacity value of 30% from token `opacity30`
  final double opacity30;

  /// Opacity value of 40% from token `opacity40`
  final double opacity40;

  /// Opacity value of 50% from token `opacity50`
  final double opacity50;

  /// Opacity value of 60% from token `opacity60`
  final double opacity60;

  /// Opacity value of 70% from token `opacity70`
  final double opacity70;

  /// Opacity value of 80% from token `opacity80`
  final double opacity80;

  /// Opacity value of 90% from token `opacity90`
  final double opacity90;

  /// Opacity value of 100% from token `opacity100`
  final double opacity100;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeBaseOpacity &&
          runtimeType == other.runtimeType &&
          opacity0 == other.opacity0 &&
          opacity5 == other.opacity5 &&
          opacity10 == other.opacity10 &&
          opacity20 == other.opacity20 &&
          opacity30 == other.opacity30 &&
          opacity40 == other.opacity40 &&
          opacity50 == other.opacity50 &&
          opacity60 == other.opacity60 &&
          opacity70 == other.opacity70 &&
          opacity80 == other.opacity80 &&
          opacity90 == other.opacity90 &&
          opacity100 == other.opacity100;

  @override
  int get hashCode => Object.hash(
    opacity0,
    opacity5,
    opacity10,
    opacity20,
    opacity30,
    opacity40,
    opacity50,
    opacity60,
    opacity70,
    opacity80,
    opacity90,
    opacity100,
  );
}

/// State opacity values for consistent transparency handling
@immutable
class LemonadeStateOpacity {
  /// Creates a [LemonadeStateOpacity] configuration.
  const LemonadeStateOpacity({
    this.opacityPressed = 0.2,
    this.opacityDisabled = 0.4,
  });

  /// Linearly interpolates between two [LemonadeStateOpacity] objects.
  /// If they are identical, returns [a].
  factory LemonadeStateOpacity.lerp(
    LemonadeStateOpacity a,
    LemonadeStateOpacity b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeStateOpacity(
      opacityPressed: lerpDouble(a.opacityPressed, b.opacityPressed, t)!,
      opacityDisabled: lerpDouble(a.opacityDisabled, b.opacityDisabled, t)!,
    );
  }

  /// Opacity value of 20% from token `opacityPressed`
  final double opacityPressed;

  /// Opacity value of 40% from token `opacityDisabled`
  final double opacityDisabled;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeStateOpacity &&
          runtimeType == other.runtimeType &&
          opacityPressed == other.opacityPressed &&
          opacityDisabled == other.opacityDisabled;

  @override
  int get hashCode => Object.hash(
    opacityPressed,
    opacityDisabled,
  );
}
