/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Shape values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Shape configuration for the Lemonade Design System.
///
/// Provides predefined shape values based on radius tokens for consistent
/// rounded corners across the product.
@immutable
class LemonadeShapes {
  /// Creates a [LemonadeShapes] configuration.
  const LemonadeShapes({
    this.radius0 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(0.0)),
    ),
    this.radius50 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(2.0)),
    ),
    this.radius100 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(4.0)),
    ),
    this.radius150 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(6.0)),
    ),
    this.radius200 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(8.0)),
    ),
    this.radius300 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(12.0)),
    ),
    this.radius400 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(16.0)),
    ),
    this.radius500 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(20.0)),
    ),
    this.radius600 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(24.0)),
    ),
    this.radius800 = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(32.0)),
    ),
    this.radiusFull = const RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(999.0)),
    ),
  });

  /// Linearly interpolates between two [LemonadeShapes] objects.
  /// If they are identical, returns [a].
  factory LemonadeShapes.lerp(
    LemonadeShapes a,
    LemonadeShapes b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeShapes(
      radius0: ShapeBorder.lerp(a.radius0, b.radius0, t)!,
      radius50: ShapeBorder.lerp(a.radius50, b.radius50, t)!,
      radius100: ShapeBorder.lerp(a.radius100, b.radius100, t)!,
      radius150: ShapeBorder.lerp(a.radius150, b.radius150, t)!,
      radius200: ShapeBorder.lerp(a.radius200, b.radius200, t)!,
      radius300: ShapeBorder.lerp(a.radius300, b.radius300, t)!,
      radius400: ShapeBorder.lerp(a.radius400, b.radius400, t)!,
      radius500: ShapeBorder.lerp(a.radius500, b.radius500, t)!,
      radius600: ShapeBorder.lerp(a.radius600, b.radius600, t)!,
      radius800: ShapeBorder.lerp(a.radius800, b.radius800, t)!,
      radiusFull: ShapeBorder.lerp(a.radiusFull, b.radiusFull, t)!,
    );
  }

  /// Radius value of 0px from token `radius0`
  final ShapeBorder radius0;

  /// Radius value of 2px from token `radius50`
  final ShapeBorder radius50;

  /// Radius value of 4px from token `radius100`
  final ShapeBorder radius100;

  /// Radius value of 6px from token `radius150`
  final ShapeBorder radius150;

  /// Radius value of 8px from token `radius200`
  final ShapeBorder radius200;

  /// Radius value of 12px from token `radius300`
  final ShapeBorder radius300;

  /// Radius value of 16px from token `radius400`
  final ShapeBorder radius400;

  /// Radius value of 20px from token `radius500`
  final ShapeBorder radius500;

  /// Radius value of 24px from token `radius600`
  final ShapeBorder radius600;

  /// Radius value of 32px from token `radius800`
  final ShapeBorder radius800;

  /// Radius value of 999px from token `radiusFull`
  final ShapeBorder radiusFull;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeShapes &&
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

  /// Helper method to access [LemonadeShapes] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeShapes of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.shapes;
  }
}
