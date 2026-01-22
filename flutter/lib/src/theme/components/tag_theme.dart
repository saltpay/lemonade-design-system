import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeTagTheme}
/// Theme configuration for [LemonadeTag] component.
///
/// This class provides styling options for customizing the appearance
/// of tag components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeTagTheme {
  /// {@macro LemonadeTagTheme}
  const LemonadeTagTheme({
    required this.iconSize,
  });

  /// Creates a [LemonadeTagTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeTagTheme.from(LemonadeTokens tokens) {
    return LemonadeTagTheme(
      iconSize: tokens.sizes.size400,
    );
  }

  /// Linearly interpolates between two [LemonadeTagTheme] objects.
  factory LemonadeTagTheme.lerp(
    LemonadeTagTheme a,
    LemonadeTagTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeTagTheme(
      iconSize: lerpDouble(a.iconSize, b.iconSize, t)!,
    );
  }

  /// The size of icons in the tag.
  final double iconSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeTagTheme copyWith({
    double? iconSize,
  }) {
    return LemonadeTagTheme(
      iconSize: iconSize ?? this.iconSize,
    );
  }

  /// Merges this theme with another theme.
  LemonadeTagTheme mergeWith(LemonadeTagTheme? other) {
    if (other == null) return this;
    return copyWith(
      iconSize: other.iconSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeTagTheme && other.iconSize == iconSize;
  }

  @override
  int get hashCode => iconSize.hashCode;
}
