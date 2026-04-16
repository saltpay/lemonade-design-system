/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Line height values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Line height configuration for the Lemonade Design System.
///
/// Defines line heights used for text styling.
@immutable
class LemonadeLineHeights {
  /// Creates a [LemonadeLineHeights] configuration.
  const LemonadeLineHeights({
    this.lineHeight250 = 10.0,
    this.lineHeight300 = 12.0,
    this.lineHeight350 = 14.0,
    this.lineHeight400 = 16.0,
    this.lineHeight450 = 18.0,
    this.lineHeight500 = 20.0,
    this.lineHeight600 = 24.0,
    this.lineHeight650 = 26.0,
    this.lineHeight700 = 28.0,
    this.lineHeight800 = 32.0,
    this.lineHeight900 = 36.0,
    this.lineHeight1000 = 40.0,
    this.lineHeight1100 = 44.0,
    this.lineHeight1200 = 48.0,
    this.lineHeight1400 = 56.0,
    this.lineHeight1600 = 64.0,
    this.lineHeight1800 = 72.0,
    this.lineHeight2000 = 80.0,
  });

  /// Linearly interpolates between two [LemonadeLineHeights] objects.
  /// If they are identical, returns [a].
  factory LemonadeLineHeights.lerp(
    LemonadeLineHeights a,
    LemonadeLineHeights b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeLineHeights(
      lineHeight250: lerpDouble(a.lineHeight250, b.lineHeight250, t)!,
      lineHeight300: lerpDouble(a.lineHeight300, b.lineHeight300, t)!,
      lineHeight350: lerpDouble(a.lineHeight350, b.lineHeight350, t)!,
      lineHeight400: lerpDouble(a.lineHeight400, b.lineHeight400, t)!,
      lineHeight450: lerpDouble(a.lineHeight450, b.lineHeight450, t)!,
      lineHeight500: lerpDouble(a.lineHeight500, b.lineHeight500, t)!,
      lineHeight600: lerpDouble(a.lineHeight600, b.lineHeight600, t)!,
      lineHeight650: lerpDouble(a.lineHeight650, b.lineHeight650, t)!,
      lineHeight700: lerpDouble(a.lineHeight700, b.lineHeight700, t)!,
      lineHeight800: lerpDouble(a.lineHeight800, b.lineHeight800, t)!,
      lineHeight900: lerpDouble(a.lineHeight900, b.lineHeight900, t)!,
      lineHeight1000: lerpDouble(a.lineHeight1000, b.lineHeight1000, t)!,
      lineHeight1100: lerpDouble(a.lineHeight1100, b.lineHeight1100, t)!,
      lineHeight1200: lerpDouble(a.lineHeight1200, b.lineHeight1200, t)!,
      lineHeight1400: lerpDouble(a.lineHeight1400, b.lineHeight1400, t)!,
      lineHeight1600: lerpDouble(a.lineHeight1600, b.lineHeight1600, t)!,
      lineHeight1800: lerpDouble(a.lineHeight1800, b.lineHeight1800, t)!,
      lineHeight2000: lerpDouble(a.lineHeight2000, b.lineHeight2000, t)!,
    );
  }

  /// Line height of 10px from token `lineHeight250`
  final double lineHeight250;

  /// Line height of 12px from token `lineHeight300`
  final double lineHeight300;

  /// Line height of 14px from token `lineHeight350`
  final double lineHeight350;

  /// Line height of 16px from token `lineHeight400`
  final double lineHeight400;

  /// Line height of 18px from token `lineHeight450`
  final double lineHeight450;

  /// Line height of 20px from token `lineHeight500`
  final double lineHeight500;

  /// Line height of 24px from token `lineHeight600`
  final double lineHeight600;

  /// Line height of 26px from token `lineHeight650`
  final double lineHeight650;

  /// Line height of 28px from token `lineHeight700`
  final double lineHeight700;

  /// Line height of 32px from token `lineHeight800`
  final double lineHeight800;

  /// Line height of 36px from token `lineHeight900`
  final double lineHeight900;

  /// Line height of 40px from token `lineHeight1000`
  final double lineHeight1000;

  /// Line height of 44px from token `lineHeight1100`
  final double lineHeight1100;

  /// Line height of 48px from token `lineHeight1200`
  final double lineHeight1200;

  /// Line height of 56px from token `lineHeight1400`
  final double lineHeight1400;

  /// Line height of 64px from token `lineHeight1600`
  final double lineHeight1600;

  /// Line height of 72px from token `lineHeight1800`
  final double lineHeight1800;

  /// Line height of 80px from token `lineHeight2000`
  final double lineHeight2000;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeLineHeights &&
          runtimeType == other.runtimeType &&
          lineHeight250 == other.lineHeight250 &&
          lineHeight300 == other.lineHeight300 &&
          lineHeight350 == other.lineHeight350 &&
          lineHeight400 == other.lineHeight400 &&
          lineHeight450 == other.lineHeight450 &&
          lineHeight500 == other.lineHeight500 &&
          lineHeight600 == other.lineHeight600 &&
          lineHeight650 == other.lineHeight650 &&
          lineHeight700 == other.lineHeight700 &&
          lineHeight800 == other.lineHeight800 &&
          lineHeight900 == other.lineHeight900 &&
          lineHeight1000 == other.lineHeight1000 &&
          lineHeight1100 == other.lineHeight1100 &&
          lineHeight1200 == other.lineHeight1200 &&
          lineHeight1400 == other.lineHeight1400 &&
          lineHeight1600 == other.lineHeight1600 &&
          lineHeight1800 == other.lineHeight1800 &&
          lineHeight2000 == other.lineHeight2000;

  @override
  int get hashCode => Object.hashAll([
    lineHeight250,
    lineHeight300,
    lineHeight350,
    lineHeight400,
    lineHeight450,
    lineHeight500,
    lineHeight600,
    lineHeight650,
    lineHeight700,
    lineHeight800,
    lineHeight900,
    lineHeight1000,
    lineHeight1100,
    lineHeight1200,
    lineHeight1400,
    lineHeight1600,
    lineHeight1800,
    lineHeight2000,
  ]);

  /// Helper method to access [LemonadeLineHeights] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeLineHeights of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.lineHeights;
  }
}
