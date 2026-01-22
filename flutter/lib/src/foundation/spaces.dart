/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Spacing values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Spacing configuration for the Lemonade Design System.
///
/// Defines spacing values used for padding, margins, and gaps.
@immutable
class LemonadeSpaces {
  /// Creates a [LemonadeSpaces] configuration.
  const LemonadeSpaces({
    this.spacing0 = 0.0,
    this.spacing50 = 2.0,
    this.spacing100 = 4.0,
    this.spacing200 = 8.0,
    this.spacing300 = 12.0,
    this.spacing400 = 16.0,
    this.spacing500 = 20.0,
    this.spacing600 = 24.0,
    this.spacing800 = 32.0,
    this.spacing1000 = 40.0,
    this.spacing1200 = 48.0,
    this.spacing1400 = 56.0,
    this.spacing1600 = 64.0,
    this.spacing1800 = 72.0,
    this.spacing2000 = 80.0,
  });

  /// Linearly interpolates between two [LemonadeSpaces] objects.
  /// If they are identical, returns [a].
  factory LemonadeSpaces.lerp(
    LemonadeSpaces a,
    LemonadeSpaces b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeSpaces(
      spacing0: lerpDouble(a.spacing0, b.spacing0, t)!,
      spacing50: lerpDouble(a.spacing50, b.spacing50, t)!,
      spacing100: lerpDouble(a.spacing100, b.spacing100, t)!,
      spacing200: lerpDouble(a.spacing200, b.spacing200, t)!,
      spacing300: lerpDouble(a.spacing300, b.spacing300, t)!,
      spacing400: lerpDouble(a.spacing400, b.spacing400, t)!,
      spacing500: lerpDouble(a.spacing500, b.spacing500, t)!,
      spacing600: lerpDouble(a.spacing600, b.spacing600, t)!,
      spacing800: lerpDouble(a.spacing800, b.spacing800, t)!,
      spacing1000: lerpDouble(a.spacing1000, b.spacing1000, t)!,
      spacing1200: lerpDouble(a.spacing1200, b.spacing1200, t)!,
      spacing1400: lerpDouble(a.spacing1400, b.spacing1400, t)!,
      spacing1600: lerpDouble(a.spacing1600, b.spacing1600, t)!,
      spacing1800: lerpDouble(a.spacing1800, b.spacing1800, t)!,
      spacing2000: lerpDouble(a.spacing2000, b.spacing2000, t)!,
    );
  }

  /// Spacing value of 0px from token `spacing0`
  final double spacing0;

  /// Spacing value of 2px from token `spacing50`
  final double spacing50;

  /// Spacing value of 4px from token `spacing100`
  final double spacing100;

  /// Spacing value of 8px from token `spacing200`
  final double spacing200;

  /// Spacing value of 12px from token `spacing300`
  final double spacing300;

  /// Spacing value of 16px from token `spacing400`
  final double spacing400;

  /// Spacing value of 20px from token `spacing500`
  final double spacing500;

  /// Spacing value of 24px from token `spacing600`
  final double spacing600;

  /// Spacing value of 32px from token `spacing800`
  final double spacing800;

  /// Spacing value of 40px from token `spacing1000`
  final double spacing1000;

  /// Spacing value of 48px from token `spacing1200`
  final double spacing1200;

  /// Spacing value of 56px from token `spacing1400`
  final double spacing1400;

  /// Spacing value of 64px from token `spacing1600`
  final double spacing1600;

  /// Spacing value of 72px from token `spacing1800`
  final double spacing1800;

  /// Spacing value of 80px from token `spacing2000`
  final double spacing2000;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeSpaces &&
          runtimeType == other.runtimeType &&
          spacing0 == other.spacing0 &&
          spacing50 == other.spacing50 &&
          spacing100 == other.spacing100 &&
          spacing200 == other.spacing200 &&
          spacing300 == other.spacing300 &&
          spacing400 == other.spacing400 &&
          spacing500 == other.spacing500 &&
          spacing600 == other.spacing600 &&
          spacing800 == other.spacing800 &&
          spacing1000 == other.spacing1000 &&
          spacing1200 == other.spacing1200 &&
          spacing1400 == other.spacing1400 &&
          spacing1600 == other.spacing1600 &&
          spacing1800 == other.spacing1800 &&
          spacing2000 == other.spacing2000;

  @override
  int get hashCode => Object.hash(
    spacing0,
    spacing50,
    spacing100,
    spacing200,
    spacing300,
    spacing400,
    spacing500,
    spacing600,
    spacing800,
    spacing1000,
    spacing1200,
    spacing1400,
    spacing1600,
    spacing1800,
    spacing2000,
  );

  /// Helper method to access [LemonadeSpaces] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeSpaces of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.spaces;
  }
}
