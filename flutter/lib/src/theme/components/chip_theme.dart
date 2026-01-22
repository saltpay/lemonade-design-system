import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeChipTheme}
/// Theme configuration for [LemonadeChip] component.
///
/// This class provides styling options for customizing the appearance
/// of chip components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeChipTheme {
  /// {@macro LemonadeChipTheme}
  const LemonadeChipTheme({
    required this.minWidth,
    required this.minHeight,
    required this.iconSize,
  });

  /// Creates a [LemonadeChipTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeChipTheme.from(LemonadeTokens tokens) {
    return LemonadeChipTheme(
      minWidth: tokens.sizes.size1600,
      minHeight: tokens.sizes.size800,
      iconSize: tokens.sizes.size400,
    );
  }

  /// Linearly interpolates between two [LemonadeChipTheme] objects.
  factory LemonadeChipTheme.lerp(
    LemonadeChipTheme a,
    LemonadeChipTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeChipTheme(
      minWidth: lerpDouble(a.minWidth, b.minWidth, t)!,
      minHeight: lerpDouble(a.minHeight, b.minHeight, t)!,
      iconSize: lerpDouble(a.iconSize, b.iconSize, t)!,
    );
  }

  /// The minimum width of a chip.
  final double minWidth;

  /// The minimum height of a chip.
  final double minHeight;

  /// The size of icons in the chip.
  final double iconSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeChipTheme copyWith({
    double? minWidth,
    double? minHeight,
    double? iconSize,
  }) {
    return LemonadeChipTheme(
      minWidth: minWidth ?? this.minWidth,
      minHeight: minHeight ?? this.minHeight,
      iconSize: iconSize ?? this.iconSize,
    );
  }

  /// Merges this theme with another theme.
  LemonadeChipTheme mergeWith(LemonadeChipTheme? other) {
    if (other == null) return this;
    return copyWith(
      minWidth: other.minWidth,
      minHeight: other.minHeight,
      iconSize: other.iconSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeChipTheme &&
        other.minWidth == minWidth &&
        other.minHeight == minHeight &&
        other.iconSize == iconSize;
  }

  @override
  int get hashCode => Object.hash(minWidth, minHeight, iconSize);
}
