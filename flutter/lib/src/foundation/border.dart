/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Border width values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Border width configuration for the Lemonade Design System.
///
/// Provides a consistent and scalable way to manage border widths across interfaces.
/// These values can be used for borders, outlines, and focus indicators.
@immutable
class LemonadeBorder {
  /// Creates a [LemonadeBorder] configuration.
  const LemonadeBorder({
    this.base = const LemonadeBaseBorder(),
    this.state = const LemonadeStateBorder(),
  });

  /// Linearly interpolates between two [LemonadeBorder] objects.
  /// If they are identical, returns [a].
  factory LemonadeBorder.lerp(
    LemonadeBorder a,
    LemonadeBorder b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeBorder(
      base: LemonadeBaseBorder.lerp(a.base, b.base, t),
      state: LemonadeStateBorder.lerp(a.state, b.state, t),
    );
  }

  /// Base border width values
  final LemonadeBaseBorder base;

  /// State border width values
  final LemonadeStateBorder state;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeBorder &&
          runtimeType == other.runtimeType &&
          base == other.base &&
          state == other.state;

  @override
  int get hashCode => Object.hash(
    base,
    state,
  );

  /// Helper method to access [LemonadeBorder] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeBorder of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.border;
  }
}

/// Base border width values for consistent border styling
@immutable
class LemonadeBaseBorder {
  /// Creates a [LemonadeBaseBorder] configuration.
  const LemonadeBaseBorder({
    this.border0 = 0.0,
    this.border25 = 1.0,
    this.border50 = 2.0,
    this.border75 = 3.0,
    this.border100 = 4.0,
  });

  /// Linearly interpolates between two [LemonadeBaseBorder] objects.
  /// If they are identical, returns [a].
  factory LemonadeBaseBorder.lerp(
    LemonadeBaseBorder a,
    LemonadeBaseBorder b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeBaseBorder(
      border0: lerpDouble(a.border0, b.border0, t)!,
      border25: lerpDouble(a.border25, b.border25, t)!,
      border50: lerpDouble(a.border50, b.border50, t)!,
      border75: lerpDouble(a.border75, b.border75, t)!,
      border100: lerpDouble(a.border100, b.border100, t)!,
    );
  }

  /// Border width value of 0px from token `border0`
  final double border0;

  /// Border width value of 1px from token `border25`
  final double border25;

  /// Border width value of 2px from token `border50`
  final double border50;

  /// Border width value of 3px from token `border75`
  final double border75;

  /// Border width value of 4px from token `border100`
  final double border100;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeBaseBorder &&
          runtimeType == other.runtimeType &&
          border0 == other.border0 &&
          border25 == other.border25 &&
          border50 == other.border50 &&
          border75 == other.border75 &&
          border100 == other.border100;

  @override
  int get hashCode => Object.hash(
    border0,
    border25,
    border50,
    border75,
    border100,
  );
}

/// State border width values for consistent border styling
@immutable
class LemonadeStateBorder {
  /// Creates a [LemonadeStateBorder] configuration.
  const LemonadeStateBorder({
    this.base = const LemonadeBaseBorder(),
  });

  /// Base border configuration for referencing values
  final LemonadeBaseBorder base;

  /// Linearly interpolates between two [LemonadeStateBorder] objects.
  /// If they are identical, returns [a].
  factory LemonadeStateBorder.lerp(
    LemonadeStateBorder a,
    LemonadeStateBorder b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeStateBorder(
      base: LemonadeBaseBorder.lerp(a.base, b.base, t),
    );
  }

  /// Border width value of 2px from token `focusRing` (references base/border-50)
  double get focusRing => base.border50;

  /// Border width value of 2px from token `borderSelected` (references base/border-50)
  double get borderSelected => base.border50;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeStateBorder &&
          runtimeType == other.runtimeType &&
          base == other.base;

  @override
  int get hashCode => base.hashCode;
}
