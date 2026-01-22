/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Shadow values from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Shadow configuration for the Lemonade Design System.
///
/// Defines shadow styles used for elevation and depth.
@immutable
class LemonadeShadows {
  /// Creates a [LemonadeShadows] configuration.
  const LemonadeShadows({
    this.xsmall = const <BoxShadow>[
      BoxShadow(
        color: Color(0x0C313931),
        blurRadius: 1,
        offset: Offset(0, 1),
      ),
      BoxShadow(
        color: Color(0x2D0A280A),
        blurRadius: 3,
        offset: Offset(0, 3),
        spreadRadius: -3,
      ),
    ],
    this.small = const <BoxShadow>[
      BoxShadow(
        color: Color(0x0C313931),
        blurRadius: 1,
        offset: Offset(0, 1),
      ),
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 6,
        offset: Offset(0, 4),
        spreadRadius: -3,
      ),
    ],
    this.medium = const <BoxShadow>[
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 1,
        offset: Offset(0, 1),
      ),
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 32,
        offset: Offset(0, 16),
        spreadRadius: -6,
      ),
    ],
    this.large = const <BoxShadow>[
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 1,
        offset: Offset(0, 1),
      ),
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 8,
        offset: Offset(0, 8),
        spreadRadius: -8,
      ),
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 28,
        offset: Offset(0, 24),
        spreadRadius: -12,
      ),
      BoxShadow(
        color: Color(0x190D300D),
        blurRadius: 48,
        offset: Offset(0, 32),
        spreadRadius: -8,
      ),
    ],
  });

  /// Linearly interpolates between two [LemonadeShadows] objects.
  /// If they are identical, returns [a].
  factory LemonadeShadows.lerp(
    LemonadeShadows a,
    LemonadeShadows b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeShadows(
      xsmall: BoxShadow.lerpList(a.xsmall, b.xsmall, t)!,
      small: BoxShadow.lerpList(a.small, b.small, t)!,
      medium: BoxShadow.lerpList(a.medium, b.medium, t)!,
      large: BoxShadow.lerpList(a.large, b.large, t)!,
    );
  }

  /// Extra small shadow style
  final List<BoxShadow> xsmall;

  /// Small shadow style
  final List<BoxShadow> small;

  /// Medium shadow style
  final List<BoxShadow> medium;

  /// Large shadow style
  final List<BoxShadow> large;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeShadows &&
          runtimeType == other.runtimeType &&
          xsmall == other.xsmall &&
          small == other.small &&
          medium == other.medium &&
          large == other.large;

  @override
  int get hashCode => Object.hash(xsmall, small, medium, large);

  /// Helper method to access [LemonadeShadows] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeShadows of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.shadows;
  }
}
