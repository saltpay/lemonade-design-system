import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeTileTheme}
/// Theme configuration for [LemonadeTile] component.
///
/// This class provides styling options for customizing the appearance
/// and behavior of tile components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeTileTheme {
  /// {@macro LemonadeTileTheme}
  const LemonadeTileTheme({
    required this.width,
    required this.horizontalPadding,
    required this.backgroundColorNeutralDefault,
    required this.backgroundColorNeutralHovered,
    required this.backgroundColorNeutralPressed,

    required this.backgroundColorMutedDefault,
    required this.backgroundColorMutedHovered,
    required this.backgroundColorMutedPressed,

    required this.backgroundColorOnColorDefault,
    required this.backgroundColorOnColorHovered,
    required this.backgroundColorOnColorPressed,

    required this.borderColorNeutral,
    required this.borderColorMuted,
    required this.borderColorOnBrand,
  });

  /// Creates a [LemonadeTileTheme] with default sizes
  /// based on the provided tokens
  factory LemonadeTileTheme.from(LemonadeTokens tokens) {
    return LemonadeTileTheme(
      width: adaptive(mobile: 112, desktop: 216),
      horizontalPadding: adaptive(
        mobile: tokens.spaces.spacing200,
        desktop: tokens.spaces.spacing400,
      ),
      backgroundColorNeutralDefault: tokens.colors.background.bgElevated,
      backgroundColorNeutralHovered:
          tokens.colors.interaction.bgElevatedInteractive,
      backgroundColorNeutralPressed:
          tokens.colors.interaction.bgElevatedPressed,

      backgroundColorMutedDefault: tokens.colors.background.bgDefault,
      backgroundColorMutedHovered:
          tokens.colors.interaction.bgDefaultInteractive,
      backgroundColorMutedPressed: tokens.colors.interaction.bgDefaultPressed,

      backgroundColorOnColorDefault: tokens.colors.background.bgBrandElevated,
      backgroundColorOnColorHovered:
          tokens.colors.interaction.bgBrandElevatedInteractive,
      backgroundColorOnColorPressed:
          tokens.colors.interaction.bgBrandElevatedPressed,

      borderColorNeutral: tokens.colors.border.borderNeutralMedium,
      borderColorMuted: tokens.colors.border.borderNeutralMedium,
      borderColorOnBrand: tokens.colors.border.borderNeutralMediumInverse,
    );
  }

  /// Linearly interpolates between two [LemonadeTileTheme] objects.
  factory LemonadeTileTheme.lerp(
    LemonadeTileTheme a,
    LemonadeTileTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeTileTheme(
      width: lerpDouble(a.width, b.width, t)!,
      horizontalPadding: lerpDouble(
        a.horizontalPadding,
        b.horizontalPadding,
        t,
      )!,
      backgroundColorNeutralDefault: Color.lerp(
        a.backgroundColorNeutralDefault,
        b.backgroundColorNeutralDefault,
        t,
      )!,
      backgroundColorNeutralHovered: Color.lerp(
        a.backgroundColorNeutralHovered,
        b.backgroundColorNeutralHovered,
        t,
      )!,
      backgroundColorNeutralPressed: Color.lerp(
        a.backgroundColorNeutralPressed,
        b.backgroundColorNeutralPressed,
        t,
      )!,

      backgroundColorMutedDefault: Color.lerp(
        a.backgroundColorMutedDefault,
        b.backgroundColorMutedDefault,
        t,
      )!,
      backgroundColorMutedHovered: Color.lerp(
        a.backgroundColorMutedHovered,
        b.backgroundColorMutedHovered,
        t,
      )!,
      backgroundColorMutedPressed: Color.lerp(
        a.backgroundColorMutedPressed,
        b.backgroundColorMutedPressed,
        t,
      )!,

      backgroundColorOnColorDefault: Color.lerp(
        a.backgroundColorOnColorDefault,
        b.backgroundColorOnColorDefault,
        t,
      )!,
      backgroundColorOnColorHovered: Color.lerp(
        a.backgroundColorOnColorHovered,
        b.backgroundColorOnColorHovered,
        t,
      )!,
      backgroundColorOnColorPressed: Color.lerp(
        a.backgroundColorOnColorPressed,
        b.backgroundColorOnColorPressed,
        t,
      )!,

      borderColorNeutral: Color.lerp(
        a.borderColorNeutral,
        b.borderColorNeutral,
        t,
      )!,
      borderColorMuted: Color.lerp(a.borderColorMuted, b.borderColorMuted, t)!,
      borderColorOnBrand: Color.lerp(
        a.borderColorOnBrand,
        b.borderColorOnBrand,
        t,
      )!,
    );
  }

  /// Width of tile.
  final double width;

  /// Inner horizontal padding of the tile's card.
  final double horizontalPadding;

  /// Background color of the tile in Neutral variant, rest state.
  final Color backgroundColorNeutralDefault;

  /// Background color of the tile in Neutral variant, hovered state.
  final Color backgroundColorNeutralHovered;

  /// Background color of the tile in Neutral variant, pressed state.
  final Color backgroundColorNeutralPressed;

  /// Background color of the tile in Muted variant, rest state.
  final Color backgroundColorMutedDefault;

  /// Background color of the tile in Muted variant, hovered state.
  final Color backgroundColorMutedHovered;

  /// Background color of the tile in Muted variant, pressed state.
  final Color backgroundColorMutedPressed;

  /// Background color of the tile in On Color variant, rest state.
  final Color backgroundColorOnColorDefault;

  /// Background color of the tile in On Color variant, hovered state.
  final Color backgroundColorOnColorHovered;

  /// Background color of the tile in On Color variant, pressed state.
  final Color backgroundColorOnColorPressed;

  /// Border color of the tile in Neutral variant.
  final Color borderColorNeutral;

  /// Border color of the tile in Muted variant.
  final Color borderColorMuted;

  /// Border color of the tile in On Brand variant.
  final Color borderColorOnBrand;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeTileTheme copyWith({
    double? width,
    double? horizontalPadding,
    Color? backgroundColorNeutralDefault,
    Color? backgroundColorNeutralHovered,
    Color? backgroundColorNeutralPressed,

    Color? backgroundColorMutedDefault,
    Color? backgroundColorMutedHovered,
    Color? backgroundColorMutedPressed,

    Color? backgroundColorOnColorDefault,
    Color? backgroundColorOnColorHovered,
    Color? backgroundColorOnColorPressed,

    Color? borderColorNeutral,
    Color? borderColorMuted,
    Color? borderColorOnBrand,

    TextStyle? labelStyle,
  }) {
    return LemonadeTileTheme(
      width: width ?? this.width,
      horizontalPadding: horizontalPadding ?? this.horizontalPadding,
      backgroundColorNeutralDefault:
          backgroundColorNeutralDefault ?? this.backgroundColorNeutralDefault,
      backgroundColorNeutralHovered:
          backgroundColorNeutralHovered ?? this.backgroundColorNeutralHovered,
      backgroundColorNeutralPressed:
          backgroundColorNeutralPressed ?? this.backgroundColorNeutralPressed,
      backgroundColorMutedDefault:
          backgroundColorMutedDefault ?? this.backgroundColorMutedDefault,
      backgroundColorMutedHovered:
          backgroundColorMutedHovered ?? this.backgroundColorMutedHovered,
      backgroundColorMutedPressed:
          backgroundColorMutedPressed ?? this.backgroundColorMutedPressed,
      backgroundColorOnColorDefault:
          backgroundColorOnColorDefault ?? this.backgroundColorOnColorDefault,
      backgroundColorOnColorHovered:
          backgroundColorOnColorHovered ?? this.backgroundColorOnColorHovered,
      backgroundColorOnColorPressed:
          backgroundColorOnColorPressed ?? this.backgroundColorOnColorPressed,
      borderColorNeutral: borderColorNeutral ?? this.borderColorNeutral,
      borderColorMuted: borderColorMuted ?? this.borderColorMuted,
      borderColorOnBrand: borderColorOnBrand ?? this.borderColorOnBrand,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeTileTheme mergeWith(LemonadeTileTheme? other) {
    if (other == null) return this;
    return copyWith(
      width: other.width,
      horizontalPadding: other.horizontalPadding,
      backgroundColorNeutralDefault: other.backgroundColorNeutralDefault,
      backgroundColorNeutralHovered: other.backgroundColorNeutralHovered,
      backgroundColorNeutralPressed: other.backgroundColorNeutralPressed,
      backgroundColorMutedDefault: other.backgroundColorMutedDefault,
      backgroundColorMutedHovered: other.backgroundColorMutedHovered,
      backgroundColorMutedPressed: other.backgroundColorMutedPressed,
      backgroundColorOnColorDefault: other.backgroundColorOnColorDefault,
      backgroundColorOnColorHovered: other.backgroundColorOnColorHovered,
      backgroundColorOnColorPressed: other.backgroundColorOnColorPressed,
      borderColorNeutral: other.borderColorNeutral,
      borderColorMuted: other.borderColorMuted,
      borderColorOnBrand: other.borderColorOnBrand,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LemonadeTileTheme &&
        other.width == width &&
        other.horizontalPadding == horizontalPadding &&
        other.backgroundColorNeutralDefault == backgroundColorNeutralDefault &&
        other.backgroundColorNeutralHovered == backgroundColorNeutralHovered &&
        other.backgroundColorNeutralPressed == backgroundColorNeutralPressed &&
        other.backgroundColorMutedDefault == backgroundColorMutedDefault &&
        other.backgroundColorMutedHovered == backgroundColorMutedHovered &&
        other.backgroundColorMutedPressed == backgroundColorMutedPressed &&
        other.backgroundColorOnColorDefault == backgroundColorOnColorDefault &&
        other.backgroundColorOnColorHovered == backgroundColorOnColorHovered &&
        other.backgroundColorOnColorPressed == backgroundColorOnColorPressed &&
        other.borderColorNeutral == borderColorNeutral &&
        other.borderColorMuted == borderColorMuted &&
        other.borderColorOnBrand == borderColorOnBrand;
  }

  @override
  int get hashCode {
    return Object.hash(
      width,
      horizontalPadding,
      backgroundColorNeutralDefault,
      backgroundColorNeutralHovered,
      backgroundColorNeutralPressed,
      backgroundColorMutedDefault,
      backgroundColorMutedHovered,
      backgroundColorMutedPressed,
      backgroundColorOnColorDefault,
      backgroundColorOnColorHovered,
      backgroundColorOnColorPressed,
      borderColorNeutral,
      borderColorMuted,
      borderColorOnBrand,
    );
  }
}
