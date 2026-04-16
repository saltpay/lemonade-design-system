/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Font size values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Font size configuration for the Lemonade Design System.
///
/// Defines font sizes used for text styling.
@immutable
class LemonadeFontSizes {
  /// The base font family for the design system.
  static const String fontFamily = 'Figtree';

  /// Creates a [LemonadeFontSizes] configuration.
  const LemonadeFontSizes({
    this.fontSize250 = 10.0,
    this.fontSize300 = 12.0,
    this.fontSize350 = 14.0,
    this.fontSize400 = 16.0,
    this.fontSize450 = 18.0,
    this.fontSize500 = 20.0,
    this.fontSize600 = 24.0,
    this.fontSize700 = 28.0,
    this.fontSize800 = 32.0,
    this.fontSize900 = 36.0,
    this.fontSize1000 = 40.0,
    this.fontSize1200 = 48.0,
    this.fontSize1400 = 56.0,
    this.fontSize1600 = 64.0,
    this.fontSize1800 = 72.0,
  });

  /// Linearly interpolates between two [LemonadeFontSizes] objects.
  /// If they are identical, returns [a].
  factory LemonadeFontSizes.lerp(
    LemonadeFontSizes a,
    LemonadeFontSizes b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeFontSizes(
      fontSize250: lerpDouble(a.fontSize250, b.fontSize250, t)!,
      fontSize300: lerpDouble(a.fontSize300, b.fontSize300, t)!,
      fontSize350: lerpDouble(a.fontSize350, b.fontSize350, t)!,
      fontSize400: lerpDouble(a.fontSize400, b.fontSize400, t)!,
      fontSize450: lerpDouble(a.fontSize450, b.fontSize450, t)!,
      fontSize500: lerpDouble(a.fontSize500, b.fontSize500, t)!,
      fontSize600: lerpDouble(a.fontSize600, b.fontSize600, t)!,
      fontSize700: lerpDouble(a.fontSize700, b.fontSize700, t)!,
      fontSize800: lerpDouble(a.fontSize800, b.fontSize800, t)!,
      fontSize900: lerpDouble(a.fontSize900, b.fontSize900, t)!,
      fontSize1000: lerpDouble(a.fontSize1000, b.fontSize1000, t)!,
      fontSize1200: lerpDouble(a.fontSize1200, b.fontSize1200, t)!,
      fontSize1400: lerpDouble(a.fontSize1400, b.fontSize1400, t)!,
      fontSize1600: lerpDouble(a.fontSize1600, b.fontSize1600, t)!,
      fontSize1800: lerpDouble(a.fontSize1800, b.fontSize1800, t)!,
    );
  }

  /// Font size of 10px from token `fontSize250`
  final double fontSize250;

  /// Font size of 12px from token `fontSize300`
  final double fontSize300;

  /// Font size of 14px from token `fontSize350`
  final double fontSize350;

  /// Font size of 16px from token `fontSize400`
  final double fontSize400;

  /// Font size of 18px from token `fontSize450`
  final double fontSize450;

  /// Font size of 20px from token `fontSize500`
  final double fontSize500;

  /// Font size of 24px from token `fontSize600`
  final double fontSize600;

  /// Font size of 28px from token `fontSize700`
  final double fontSize700;

  /// Font size of 32px from token `fontSize800`
  final double fontSize800;

  /// Font size of 36px from token `fontSize900`
  final double fontSize900;

  /// Font size of 40px from token `fontSize1000`
  final double fontSize1000;

  /// Font size of 48px from token `fontSize1200`
  final double fontSize1200;

  /// Font size of 56px from token `fontSize1400`
  final double fontSize1400;

  /// Font size of 64px from token `fontSize1600`
  final double fontSize1600;

  /// Font size of 72px from token `fontSize1800`
  final double fontSize1800;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeFontSizes &&
          runtimeType == other.runtimeType &&
          fontSize250 == other.fontSize250 &&
          fontSize300 == other.fontSize300 &&
          fontSize350 == other.fontSize350 &&
          fontSize400 == other.fontSize400 &&
          fontSize450 == other.fontSize450 &&
          fontSize500 == other.fontSize500 &&
          fontSize600 == other.fontSize600 &&
          fontSize700 == other.fontSize700 &&
          fontSize800 == other.fontSize800 &&
          fontSize900 == other.fontSize900 &&
          fontSize1000 == other.fontSize1000 &&
          fontSize1200 == other.fontSize1200 &&
          fontSize1400 == other.fontSize1400 &&
          fontSize1600 == other.fontSize1600 &&
          fontSize1800 == other.fontSize1800;

  @override
  int get hashCode => Object.hashAll([
    fontSize250,
    fontSize300,
    fontSize350,
    fontSize400,
    fontSize450,
    fontSize500,
    fontSize600,
    fontSize700,
    fontSize800,
    fontSize900,
    fontSize1000,
    fontSize1200,
    fontSize1400,
    fontSize1600,
    fontSize1800,
  ]);

  /// Helper method to access [LemonadeFontSizes] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeFontSizes of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.fontSizes;
  }
}
