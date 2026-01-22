/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Radius values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Radius configuration for the Lemonade Design System.
///
/// Sets a small, clear set of predefined radius values for UI elements to ensure
/// consistent, scalable rounding across the product.
@immutable
class LemonadeRadius {
  /// Creates a [LemonadeRadius] configuration.
  const LemonadeRadius({
    this.radius0 = 0.0,
    this.radius50 = 2.0,
    this.radius100 = 4.0,
    this.radius150 = 6.0,
    this.radius200 = 8.0,
    this.radius300 = 12.0,
    this.radius400 = 16.0,
    this.radius500 = 20.0,
    this.radius600 = 24.0,
    this.radius800 = 32.0,
    this.radiusFull = 999.0,
  });

  /// Linearly interpolates between two [LemonadeRadius] objects.
  /// If they are identical, returns [a].
  factory LemonadeRadius.lerp(
    LemonadeRadius a,
    LemonadeRadius b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeRadius(
      radius0: lerpDouble(a.radius0, b.radius0, t)!,
      radius50: lerpDouble(a.radius50, b.radius50, t)!,
      radius100: lerpDouble(a.radius100, b.radius100, t)!,
      radius150: lerpDouble(a.radius150, b.radius150, t)!,
      radius200: lerpDouble(a.radius200, b.radius200, t)!,
      radius300: lerpDouble(a.radius300, b.radius300, t)!,
      radius400: lerpDouble(a.radius400, b.radius400, t)!,
      radius500: lerpDouble(a.radius500, b.radius500, t)!,
      radius600: lerpDouble(a.radius600, b.radius600, t)!,
      radius800: lerpDouble(a.radius800, b.radius800, t)!,
      radiusFull: lerpDouble(a.radiusFull, b.radiusFull, t)!,
    );
  }

  /// Radius value of 0px from token `radius0`
  final double radius0;

  /// Radius value of 2px from token `radius50`
  final double radius50;

  /// Radius value of 4px from token `radius100`
  final double radius100;

  /// Radius value of 6px from token `radius150`
  final double radius150;

  /// Radius value of 8px from token `radius200`
  final double radius200;

  /// Radius value of 12px from token `radius300`
  final double radius300;

  /// Radius value of 16px from token `radius400`
  final double radius400;

  /// Radius value of 20px from token `radius500`
  final double radius500;

  /// Radius value of 24px from token `radius600`
  final double radius600;

  /// Radius value of 32px from token `radius800`
  final double radius800;

  /// Radius value of 999px from token `radiusFull`
  final double radiusFull;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeRadius &&
          runtimeType == other.runtimeType &&
          radius0 == other.radius0 &&
          radius50 == other.radius50 &&
          radius100 == other.radius100 &&
          radius150 == other.radius150 &&
          radius200 == other.radius200 &&
          radius300 == other.radius300 &&
          radius400 == other.radius400 &&
          radius500 == other.radius500 &&
          radius600 == other.radius600 &&
          radius800 == other.radius800 &&
          radiusFull == other.radiusFull;

  @override
  int get hashCode => Object.hash(
    radius0,
    radius50,
    radius100,
    radius150,
    radius200,
    radius300,
    radius400,
    radius500,
    radius600,
    radius800,
    radiusFull,
  );

  /// Helper method to access [LemonadeRadius] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeRadius of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.radius;
  }
}
