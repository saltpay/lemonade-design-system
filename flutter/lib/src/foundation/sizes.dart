/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Size values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Size configuration for the Lemonade Design System.
///
/// Defines size values used for width and height.
@immutable
class LemonadeSizes {
  /// Creates a [LemonadeSizes] configuration.
  const LemonadeSizes({
    this.size0 = 0.0,
    this.size50 = 2.0,
    this.size100 = 4.0,
    this.size150 = 6.0,
    this.size200 = 8.0,
    this.size250 = 10.0,
    this.size300 = 12.0,
    this.size350 = 14.0,
    this.size400 = 16.0,
    this.size450 = 18.0,
    this.size500 = 20.0,
    this.size550 = 22.0,
    this.size600 = 24.0,
    this.size700 = 28.0,
    this.size750 = 30.0,
    this.size800 = 32.0,
    this.size900 = 36.0,
    this.size1000 = 40.0,
    this.size1100 = 44.0,
    this.size1200 = 48.0,
    this.size1400 = 56.0,
    this.size1600 = 64.0,
  });

  /// Linearly interpolates between two [LemonadeSizes] objects.
  /// If they are identical, returns [a].
  factory LemonadeSizes.lerp(
    LemonadeSizes a,
    LemonadeSizes b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeSizes(
      size0: lerpDouble(a.size0, b.size0, t)!,
      size50: lerpDouble(a.size50, b.size50, t)!,
      size100: lerpDouble(a.size100, b.size100, t)!,
      size150: lerpDouble(a.size150, b.size150, t)!,
      size200: lerpDouble(a.size200, b.size200, t)!,
      size250: lerpDouble(a.size250, b.size250, t)!,
      size300: lerpDouble(a.size300, b.size300, t)!,
      size350: lerpDouble(a.size350, b.size350, t)!,
      size400: lerpDouble(a.size400, b.size400, t)!,
      size450: lerpDouble(a.size450, b.size450, t)!,
      size500: lerpDouble(a.size500, b.size500, t)!,
      size550: lerpDouble(a.size550, b.size550, t)!,
      size600: lerpDouble(a.size600, b.size600, t)!,
      size700: lerpDouble(a.size700, b.size700, t)!,
      size750: lerpDouble(a.size750, b.size750, t)!,
      size800: lerpDouble(a.size800, b.size800, t)!,
      size900: lerpDouble(a.size900, b.size900, t)!,
      size1000: lerpDouble(a.size1000, b.size1000, t)!,
      size1100: lerpDouble(a.size1100, b.size1100, t)!,
      size1200: lerpDouble(a.size1200, b.size1200, t)!,
      size1400: lerpDouble(a.size1400, b.size1400, t)!,
      size1600: lerpDouble(a.size1600, b.size1600, t)!,
    );
  }

  /// Size value of 0px from token `size0`
  final double size0;

  /// Size value of 2px from token `size50`
  final double size50;

  /// Size value of 4px from token `size100`
  final double size100;

  /// Size value of 6px from token `size150`
  final double size150;

  /// Size value of 8px from token `size200`
  final double size200;

  /// Size value of 10px from token `size250`
  final double size250;

  /// Size value of 12px from token `size300`
  final double size300;

  /// Size value of 14px from token `size350`
  final double size350;

  /// Size value of 16px from token `size400`
  final double size400;

  /// Size value of 18px from token `size450`
  final double size450;

  /// Size value of 20px from token `size500`
  final double size500;

  /// Size value of 22px from token `size550`
  final double size550;

  /// Size value of 24px from token `size600`
  final double size600;

  /// Size value of 28px from token `size700`
  final double size700;

  /// Size value of 30px from token `size750`
  final double size750;

  /// Size value of 32px from token `size800`
  final double size800;

  /// Size value of 36px from token `size900`
  final double size900;

  /// Size value of 40px from token `size1000`
  final double size1000;

  /// Size value of 44px from token `size1100`
  final double size1100;

  /// Size value of 48px from token `size1200`
  final double size1200;

  /// Size value of 56px from token `size1400`
  final double size1400;

  /// Size value of 64px from token `size1600`
  final double size1600;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeSizes &&
          runtimeType == other.runtimeType &&
          size0 == other.size0 &&
          size50 == other.size50 &&
          size100 == other.size100 &&
          size150 == other.size150 &&
          size200 == other.size200 &&
          size250 == other.size250 &&
          size300 == other.size300 &&
          size350 == other.size350 &&
          size400 == other.size400 &&
          size450 == other.size450 &&
          size500 == other.size500 &&
          size550 == other.size550 &&
          size600 == other.size600 &&
          size700 == other.size700 &&
          size750 == other.size750 &&
          size800 == other.size800 &&
          size900 == other.size900 &&
          size1000 == other.size1000 &&
          size1100 == other.size1100 &&
          size1200 == other.size1200 &&
          size1400 == other.size1400 &&
          size1600 == other.size1600;

  @override
  int get hashCode => Object.hashAll([
    size0,
    size50,
    size100,
    size150,
    size200,
    size250,
    size300,
    size350,
    size400,
    size450,
    size500,
    size550,
    size600,
    size700,
    size750,
    size800,
    size900,
    size1000,
    size1100,
    size1200,
    size1400,
    size1600,
  ]);

  /// Helper method to access [LemonadeSizes] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeSizes of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.sizes;
  }
}
