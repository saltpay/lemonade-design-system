import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeSwitchTheme}
/// Theme configuration for [LemonadeSwitch] component.
///
/// This class provides styling options for customizing the appearance
/// and behavior of switch components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeSwitchTheme {
  /// {@macro LemonadeSwitchTheme}
  const LemonadeSwitchTheme({
    required this.trackWidth,
    required this.trackHeight,
    required this.thumbDiameter,
    required this.thumbHoverExtension,
    required this.thumbPressExtension,
    required this.thumbColor,
    required this.trackColorChecked,
    required this.trackColorUnchecked,
    required this.trackColorCheckedHover,
    required this.trackColorUncheckedHover,
    required this.trackColorCheckedPressed,
    required this.trackColorUncheckedPressed,
    required this.trackColorDisabled,
    required this.thumbColorDisabled,
    required this.duration,
    required this.shadows,
  });

  /// Creates a [LemonadeSwitchTheme] with default sizes based on the provided
  factory LemonadeSwitchTheme.from(LemonadeTokens tokens) {
    return LemonadeSwitchTheme(
      trackWidth: adaptive(
        mobile: tokens.sizes.size1200,
        desktop: tokens.sizes.size800,
      ),
      trackHeight: adaptive(
        mobile: tokens.sizes.size700,
        desktop: tokens.sizes.size450,
      ),
      thumbDiameter: adaptive(
        mobile: tokens.sizes.size550,
        desktop: tokens.sizes.size350,
      ),
      thumbHoverExtension: adaptive(
        mobile: tokens.sizes.size0,
        desktop: tokens.sizes.size50,
      ),
      thumbPressExtension: adaptive(
        mobile: tokens.sizes.size100,
        desktop: tokens.sizes.size150,
      ),
      thumbColor: tokens.colors.background.bgDefault,
      trackColorChecked: tokens.colors.background.bgBrandHigh,
      trackColorUnchecked: tokens.colors.background.bgElevatedHigh,
      trackColorCheckedHover: tokens.colors.interaction.bgBrandHighInteractive,
      trackColorUncheckedHover:
          tokens.colors.interaction.bgElevatedHighInteractive,
      trackColorCheckedPressed: tokens.colors.interaction.bgBrandHighPressed,
      trackColorUncheckedPressed: tokens.colors.interaction.bgElevatedPressed,
      trackColorDisabled: tokens.colors.background.bgElevatedHigh,
      thumbColorDisabled: tokens.colors.background.bgElevatedHigh,
      duration: kAnimationDuration,
      shadows: tokens.shadows.small,
    );
  }

  /// Linearly interpolates between two [LemonadeSwitchTheme] objects.
  factory LemonadeSwitchTheme.lerp(
    LemonadeSwitchTheme a,
    LemonadeSwitchTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeSwitchTheme(
      trackWidth: lerpDouble(a.trackWidth, b.trackWidth, t)!,
      trackHeight: lerpDouble(a.trackHeight, b.trackHeight, t)!,
      thumbDiameter: lerpDouble(a.thumbDiameter, b.thumbDiameter, t)!,
      thumbHoverExtension: lerpDouble(
        a.thumbHoverExtension,
        b.thumbHoverExtension,
        t,
      )!,
      thumbPressExtension: lerpDouble(
        a.thumbPressExtension,
        b.thumbPressExtension,
        t,
      )!,
      thumbColor: Color.lerp(a.thumbColor, b.thumbColor, t)!,
      trackColorChecked: Color.lerp(
        a.trackColorChecked,
        b.trackColorChecked,
        t,
      )!,
      trackColorUnchecked: Color.lerp(
        a.trackColorUnchecked,
        b.trackColorUnchecked,
        t,
      )!,
      trackColorCheckedHover: Color.lerp(
        a.trackColorCheckedHover,
        b.trackColorCheckedHover,
        t,
      )!,
      trackColorUncheckedHover: Color.lerp(
        a.trackColorUncheckedHover,
        b.trackColorUncheckedHover,
        t,
      )!,
      trackColorCheckedPressed: Color.lerp(
        a.trackColorCheckedPressed,
        b.trackColorCheckedPressed,
        t,
      )!,
      trackColorUncheckedPressed: Color.lerp(
        a.trackColorUncheckedPressed,
        b.trackColorUncheckedPressed,
        t,
      )!,
      trackColorDisabled: Color.lerp(
        a.trackColorDisabled,
        b.trackColorDisabled,
        t,
      )!,
      thumbColorDisabled: Color.lerp(
        a.thumbColorDisabled,
        b.thumbColorDisabled,
        t,
      )!,
      duration: b.duration,
      shadows: BoxShadow.lerpList(a.shadows, b.shadows, t)!,
    );
  }

  /// Width of the switch track.
  final double trackWidth;

  /// Height of the switch track.
  final double trackHeight;

  /// Diameter of the switch thumb.
  final double thumbDiameter;

  /// Extension of the thumb when hovered (desktop only).
  final double thumbHoverExtension;

  /// Extension of the thumb when pressed.
  final double thumbPressExtension;

  /// Color of the thumb when enabled.
  final Color thumbColor;

  /// Color of the track when checked.
  final Color trackColorChecked;

  /// Color of the track when unchecked.
  final Color trackColorUnchecked;

  /// Color of the track when checked and hovered.
  final Color trackColorCheckedHover;

  /// Color of the track when unchecked and hovered.
  final Color trackColorUncheckedHover;

  /// Color of the track when checked and pressed.
  final Color trackColorCheckedPressed;

  /// Color of the track when unchecked and pressed.
  final Color trackColorUncheckedPressed;

  /// Color of the track when disabled.
  final Color trackColorDisabled;

  /// Color of the thumb when disabled.
  final Color thumbColorDisabled;

  /// Duration for animations.
  final Duration duration;

  /// Shadows to apply to the thumb.
  final List<BoxShadow> shadows;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeSwitchTheme copyWith({
    double? trackWidth,
    double? trackHeight,
    double? thumbDiameter,
    double? thumbHoverExtension,
    double? thumbPressExtension,
    Color? thumbColor,
    Color? trackColorChecked,
    Color? trackColorUnchecked,
    Color? trackColorCheckedHover,
    Color? trackColorUncheckedHover,
    Color? trackColorCheckedPressed,
    Color? trackColorUncheckedPressed,
    Color? trackColorDisabled,
    Color? thumbColorDisabled,
    Duration? duration,
    List<BoxShadow>? shadows,
  }) {
    return LemonadeSwitchTheme(
      trackWidth: trackWidth ?? this.trackWidth,
      trackHeight: trackHeight ?? this.trackHeight,
      thumbDiameter: thumbDiameter ?? this.thumbDiameter,
      thumbHoverExtension: thumbHoverExtension ?? this.thumbHoverExtension,
      thumbPressExtension: thumbPressExtension ?? this.thumbPressExtension,
      thumbColor: thumbColor ?? this.thumbColor,
      trackColorChecked: trackColorChecked ?? this.trackColorChecked,
      trackColorUnchecked: trackColorUnchecked ?? this.trackColorUnchecked,
      trackColorCheckedHover:
          trackColorCheckedHover ?? this.trackColorCheckedHover,
      trackColorUncheckedHover:
          trackColorUncheckedHover ?? this.trackColorUncheckedHover,
      trackColorCheckedPressed:
          trackColorCheckedPressed ?? this.trackColorCheckedPressed,
      trackColorUncheckedPressed:
          trackColorUncheckedPressed ?? this.trackColorUncheckedPressed,
      trackColorDisabled: trackColorDisabled ?? this.trackColorDisabled,
      thumbColorDisabled: thumbColorDisabled ?? this.thumbColorDisabled,
      duration: duration ?? this.duration,
      shadows: shadows ?? this.shadows,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeSwitchTheme mergeWith(LemonadeSwitchTheme? other) {
    if (other == null) return this;
    return copyWith(
      trackWidth: other.trackWidth,
      trackHeight: other.trackHeight,
      thumbDiameter: other.thumbDiameter,
      thumbHoverExtension: other.thumbHoverExtension,
      thumbPressExtension: other.thumbPressExtension,
      thumbColor: other.thumbColor,
      trackColorChecked: other.trackColorChecked,
      trackColorUnchecked: other.trackColorUnchecked,
      trackColorCheckedHover: other.trackColorCheckedHover,
      trackColorUncheckedHover: other.trackColorUncheckedHover,
      trackColorCheckedPressed: other.trackColorCheckedPressed,
      trackColorUncheckedPressed: other.trackColorUncheckedPressed,
      trackColorDisabled: other.trackColorDisabled,
      thumbColorDisabled: other.thumbColorDisabled,
      duration: other.duration,
      shadows: other.shadows,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LemonadeSwitchTheme &&
        other.trackWidth == trackWidth &&
        other.trackHeight == trackHeight &&
        other.thumbDiameter == thumbDiameter &&
        other.thumbHoverExtension == thumbHoverExtension &&
        other.thumbPressExtension == thumbPressExtension &&
        other.thumbColor == thumbColor &&
        other.trackColorChecked == trackColorChecked &&
        other.trackColorUnchecked == trackColorUnchecked &&
        other.trackColorCheckedHover == trackColorCheckedHover &&
        other.trackColorUncheckedHover == trackColorUncheckedHover &&
        other.trackColorCheckedPressed == trackColorCheckedPressed &&
        other.trackColorUncheckedPressed == trackColorUncheckedPressed &&
        other.trackColorDisabled == trackColorDisabled &&
        other.thumbColorDisabled == thumbColorDisabled &&
        other.duration == duration &&
        listEquals(other.shadows, shadows);
  }

  @override
  int get hashCode {
    return Object.hash(
      trackWidth,
      trackHeight,
      thumbDiameter,
      thumbHoverExtension,
      thumbPressExtension,
      thumbColor,
      trackColorChecked,
      trackColorUnchecked,
      trackColorCheckedHover,
      trackColorUncheckedHover,
      trackColorCheckedPressed,
      trackColorUncheckedPressed,
      trackColorDisabled,
      thumbColorDisabled,
      duration,
      shadows,
    );
  }
}
