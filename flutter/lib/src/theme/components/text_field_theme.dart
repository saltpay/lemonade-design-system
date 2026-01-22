import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeTextFieldTheme}
/// Theme configuration for [LemonadeTextField] component.
///
/// This class provides styling options for customizing the appearance
/// of text field components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeTextFieldTheme {
  /// {@macro LemonadeTextFieldTheme}
  const LemonadeTextFieldTheme({
    required this.height,
    required this.borderWidth,
    required this.focusBorderWidth,
  });

  /// Creates a [LemonadeTextFieldTheme] with default sizes
  /// based on the provided [tokens].
  factory LemonadeTextFieldTheme.from(LemonadeTokens tokens) {
    return LemonadeTextFieldTheme(
      height: tokens.sizes.size1400,
      borderWidth: tokens.border.base.border25,
      focusBorderWidth: tokens.border.base.border50,
    );
  }

  /// Linearly interpolates between two [LemonadeTextFieldTheme] objects.
  factory LemonadeTextFieldTheme.lerp(
    LemonadeTextFieldTheme a,
    LemonadeTextFieldTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeTextFieldTheme(
      height: lerpDouble(a.height, b.height, t)!,
      borderWidth: lerpDouble(a.borderWidth, b.borderWidth, t)!,
      focusBorderWidth: lerpDouble(a.focusBorderWidth, b.focusBorderWidth, t)!,
    );
  }

  /// The height of the text field.
  final double height;

  /// The border width of the text field.
  final double borderWidth;

  /// The border width when focused.
  final double focusBorderWidth;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeTextFieldTheme copyWith({
    double? height,
    double? borderWidth,
    double? focusBorderWidth,
  }) {
    return LemonadeTextFieldTheme(
      height: height ?? this.height,
      borderWidth: borderWidth ?? this.borderWidth,
      focusBorderWidth: focusBorderWidth ?? this.focusBorderWidth,
    );
  }

  /// Merges this theme with another theme.
  LemonadeTextFieldTheme mergeWith(LemonadeTextFieldTheme? other) {
    if (other == null) return this;
    return copyWith(
      height: other.height,
      borderWidth: other.borderWidth,
      focusBorderWidth: other.focusBorderWidth,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeTextFieldTheme &&
        other.height == height &&
        other.borderWidth == borderWidth &&
        other.focusBorderWidth == focusBorderWidth;
  }

  @override
  int get hashCode => Object.hash(height, borderWidth, focusBorderWidth);
}
