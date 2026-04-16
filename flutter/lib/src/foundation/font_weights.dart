/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Font weight values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Font weight configuration for the Lemonade Design System.
///
/// Defines font weights used for text styling.
@immutable
class LemonadeFontWeights {
  /// Creates a [LemonadeFontWeights] configuration.
  const LemonadeFontWeights({
    this.bold = FontWeight.bold,
    this.semibold = FontWeight.w600,
    this.medium = FontWeight.w500,
    this.regular = FontWeight.normal,
  });

  /// Linearly interpolates between two [LemonadeFontWeights] objects.
  /// If they are identical, returns [a].
  factory LemonadeFontWeights.lerp(
    LemonadeFontWeights a,
    LemonadeFontWeights b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeFontWeights(
      bold: FontWeight.lerp(a.bold, b.bold, t) ?? a.bold,
      semibold: FontWeight.lerp(a.semibold, b.semibold, t) ?? a.semibold,
      medium: FontWeight.lerp(a.medium, b.medium, t) ?? a.medium,
      regular: FontWeight.lerp(a.regular, b.regular, t) ?? a.regular,
    );
  }

  /// Font weight Bold from token `bold`
  final FontWeight bold;

  /// Font weight SemiBold from token `semibold`
  final FontWeight semibold;

  /// Font weight Medium from token `medium`
  final FontWeight medium;

  /// Font weight Regular from token `regular`
  final FontWeight regular;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeFontWeights &&
          runtimeType == other.runtimeType &&
          bold == other.bold &&
          semibold == other.semibold &&
          medium == other.medium &&
          regular == other.regular;

  @override
  int get hashCode => Object.hashAll([
    bold,
    semibold,
    medium,
    regular,
  ]);

  /// Helper method to access [LemonadeFontWeights] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeFontWeights of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.fontWeights;
  }
}
