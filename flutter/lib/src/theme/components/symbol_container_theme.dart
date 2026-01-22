import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeSymbolContainerTheme}
/// Theme configuration for [LemonadeSymbolContainer] component.
///
/// This class provides styling options for customizing the appearance
/// of symbol container components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeSymbolContainerTheme {
  /// {@macro LemonadeSymbolContainerTheme}
  const LemonadeSymbolContainerTheme({
    required this.xSmallContainerSize,
    required this.smallContainerSize,
    required this.mediumContainerSize,
    required this.largeContainerSize,
    required this.xLargeContainerSize,
  });

  /// Creates a [LemonadeSymbolContainerTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeSymbolContainerTheme.from(LemonadeTokens tokens) {
    return LemonadeSymbolContainerTheme(
      xSmallContainerSize: tokens.sizes.size600,
      smallContainerSize: tokens.sizes.size800,
      mediumContainerSize: tokens.sizes.size1000,
      largeContainerSize: tokens.sizes.size1200,
      xLargeContainerSize: tokens.sizes.size1600,
    );
  }

  /// Linearly interpolates between two [LemonadeSymbolContainerTheme] objects.
  factory LemonadeSymbolContainerTheme.lerp(
    LemonadeSymbolContainerTheme a,
    LemonadeSymbolContainerTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeSymbolContainerTheme(
      xSmallContainerSize: lerpDouble(
        a.xSmallContainerSize,
        b.xSmallContainerSize,
        t,
      )!,
      smallContainerSize: lerpDouble(
        a.smallContainerSize,
        b.smallContainerSize,
        t,
      )!,
      mediumContainerSize: lerpDouble(
        a.mediumContainerSize,
        b.mediumContainerSize,
        t,
      )!,
      largeContainerSize: lerpDouble(
        a.largeContainerSize,
        b.largeContainerSize,
        t,
      )!,
      xLargeContainerSize: lerpDouble(
        a.xLargeContainerSize,
        b.xLargeContainerSize,
        t,
      )!,
    );
  }

  /// The size of an extra small container.
  final double xSmallContainerSize;

  /// The size of a small container.
  final double smallContainerSize;

  /// The size of a medium container.
  final double mediumContainerSize;

  /// The size of a large container.
  final double largeContainerSize;

  /// The size of an extra large container.
  final double xLargeContainerSize;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeSymbolContainerTheme copyWith({
    double? xSmallContainerSize,
    double? smallContainerSize,
    double? mediumContainerSize,
    double? largeContainerSize,
    double? xLargeContainerSize,
  }) {
    return LemonadeSymbolContainerTheme(
      xSmallContainerSize: xSmallContainerSize ?? this.xSmallContainerSize,
      smallContainerSize: smallContainerSize ?? this.smallContainerSize,
      mediumContainerSize: mediumContainerSize ?? this.mediumContainerSize,
      largeContainerSize: largeContainerSize ?? this.largeContainerSize,
      xLargeContainerSize: xLargeContainerSize ?? this.xLargeContainerSize,
    );
  }

  /// Merges this theme with another theme.
  LemonadeSymbolContainerTheme mergeWith(LemonadeSymbolContainerTheme? other) {
    if (other == null) return this;
    return copyWith(
      xSmallContainerSize: other.xSmallContainerSize,
      smallContainerSize: other.smallContainerSize,
      mediumContainerSize: other.mediumContainerSize,
      largeContainerSize: other.largeContainerSize,
      xLargeContainerSize: other.xLargeContainerSize,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeSymbolContainerTheme &&
        other.xSmallContainerSize == xSmallContainerSize &&
        other.smallContainerSize == smallContainerSize &&
        other.mediumContainerSize == mediumContainerSize &&
        other.largeContainerSize == largeContainerSize &&
        other.xLargeContainerSize == xLargeContainerSize;
  }

  @override
  int get hashCode => Object.hash(
    xSmallContainerSize,
    smallContainerSize,
    mediumContainerSize,
    largeContainerSize,
    xLargeContainerSize,
  );
}
