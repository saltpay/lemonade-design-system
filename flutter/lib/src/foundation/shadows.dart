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
        color: Color(0x19000000),
        blurRadius: 2,
        offset: Offset(0, 1),
      ),
    ],
    this.small = const <BoxShadow>[
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 3,
        offset: Offset(0, 1),
      ),
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 2,
        offset: Offset(0, 1),
        spreadRadius: -1,
      ),
    ],
    this.medium = const <BoxShadow>[
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 3,
        offset: Offset(0, 2),
        spreadRadius: -2,
      ),
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 6,
        offset: Offset(0, 4),
        spreadRadius: -2,
      ),
    ],
    this.large = const <BoxShadow>[
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 6,
        offset: Offset(0, 4),
        spreadRadius: -4,
      ),
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 15,
        offset: Offset(0, 10),
        spreadRadius: -3,
      ),
    ],
    this.xlarge = const <BoxShadow>[
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 10,
        offset: Offset(0, 8),
        spreadRadius: -6,
      ),
      BoxShadow(
        color: Color(0x19000000),
        blurRadius: 25,
        offset: Offset(0, 20),
        spreadRadius: -5,
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
      xlarge: BoxShadow.lerpList(a.xlarge, b.xlarge, t)!,
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

  /// Shadow style for xlarge
  final List<BoxShadow> xlarge;

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeShadows &&
          runtimeType == other.runtimeType &&
          xsmall == other.xsmall &&
          small == other.small &&
          medium == other.medium &&
          large == other.large &&
          xlarge == other.xlarge;

  @override
  int get hashCode => Object.hash(xsmall, small, medium, large, xlarge);

  /// Helper method to access [LemonadeShadows] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeShadows of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.shadows;
  }
}
