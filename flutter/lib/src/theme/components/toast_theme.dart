import 'dart:ui';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeToastTheme}
/// Theme configuration for [LemonadeToast] component.
///
/// This class provides styling options for customizing the appearance
/// of toast components throughout the application.
/// {@endtemplate}
@immutable
class LemonadeToastTheme {
  /// {@macro LemonadeToastTheme}
  const LemonadeToastTheme({
    required this.backgroundColor,
    required this.labelStyle,
    required this.successIconColor,
    required this.errorIconColor,
    required this.neutralIconColor,
    required this.iconSize,
    required this.iconLabelGap,
    required this.padding,
    required this.borderRadius,
    required this.minHeight,
    required this.shadow,
  });

  /// Creates a [LemonadeToastTheme] with default configuration based on the
  /// provided [tokens].
  factory LemonadeToastTheme.from(LemonadeTokens tokens) {
    return LemonadeToastTheme(
      backgroundColor: tokens.colors.background.bgAlwaysDark,
      labelStyle: tokens.typography.bodySmallMedium.copyWith(
        color: tokens.colors.content.contentAlwaysLight,
      ),
      successIconColor: tokens.colors.content.contentPositiveOnColor,
      errorIconColor: tokens.colors.content.contentCritical,
      neutralIconColor: tokens.colors.content.contentNeutralOnColor,
      iconSize: tokens.sizes.size500,
      iconLabelGap: tokens.spaces.spacing300,
      padding: EdgeInsets.only(
        top: tokens.spaces.spacing300,
        bottom: tokens.spaces.spacing300,
        left: tokens.spaces.spacing400,
        right: tokens.spaces.spacing500,
      ),
      borderRadius: tokens.radius.radiusFull,
      minHeight: tokens.sizes.size1100,
      shadow: tokens.shadows.large,
    );
  }

  /// Linearly interpolates between two [LemonadeToastTheme] objects.
  factory LemonadeToastTheme.lerp(
    LemonadeToastTheme a,
    LemonadeToastTheme b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeToastTheme(
      backgroundColor: Color.lerp(a.backgroundColor, b.backgroundColor, t)!,
      labelStyle: TextStyle.lerp(a.labelStyle, b.labelStyle, t)!,
      successIconColor: Color.lerp(a.successIconColor, b.successIconColor, t)!,
      errorIconColor: Color.lerp(a.errorIconColor, b.errorIconColor, t)!,
      neutralIconColor: Color.lerp(a.neutralIconColor, b.neutralIconColor, t)!,
      iconSize: lerpDouble(a.iconSize, b.iconSize, t)!,
      iconLabelGap: lerpDouble(a.iconLabelGap, b.iconLabelGap, t)!,
      padding: EdgeInsets.lerp(a.padding, b.padding, t)!,
      borderRadius: lerpDouble(a.borderRadius, b.borderRadius, t)!,
      minHeight: lerpDouble(a.minHeight, b.minHeight, t)!,
      shadow: BoxShadow.lerpList(a.shadow, b.shadow, t)!,
    );
  }

  /// Background color of the toast.
  final Color backgroundColor;

  /// Text style for the toast label.
  final TextStyle labelStyle;

  /// Icon color for success voice.
  final Color successIconColor;

  /// Icon color for error voice.
  final Color errorIconColor;

  /// Icon color for neutral voice.
  final Color neutralIconColor;

  /// Size of the icon.
  final double iconSize;

  /// Gap between icon and label.
  final double iconLabelGap;

  /// Padding inside the toast.
  final EdgeInsets padding;

  /// Border radius for the pill shape.
  final double borderRadius;

  /// Minimum height of the toast.
  final double minHeight;

  /// Shadow applied to the toast.
  final List<BoxShadow> shadow;

  /// Creates a copy of this theme with the given fields replaced.
  LemonadeToastTheme copyWith({
    Color? backgroundColor,
    TextStyle? labelStyle,
    Color? successIconColor,
    Color? errorIconColor,
    Color? neutralIconColor,
    double? iconSize,
    double? iconLabelGap,
    EdgeInsets? padding,
    double? borderRadius,
    double? minHeight,
    List<BoxShadow>? shadow,
  }) {
    return LemonadeToastTheme(
      backgroundColor: backgroundColor ?? this.backgroundColor,
      labelStyle: labelStyle ?? this.labelStyle,
      successIconColor: successIconColor ?? this.successIconColor,
      errorIconColor: errorIconColor ?? this.errorIconColor,
      neutralIconColor: neutralIconColor ?? this.neutralIconColor,
      iconSize: iconSize ?? this.iconSize,
      iconLabelGap: iconLabelGap ?? this.iconLabelGap,
      padding: padding ?? this.padding,
      borderRadius: borderRadius ?? this.borderRadius,
      minHeight: minHeight ?? this.minHeight,
      shadow: shadow ?? this.shadow,
    );
  }

  /// Merges this theme with another theme.
  ///
  /// Non-null values from [other] will override values in this theme.
  LemonadeToastTheme mergeWith(LemonadeToastTheme? other) {
    if (other == null) return this;
    return copyWith(
      backgroundColor: other.backgroundColor,
      labelStyle: other.labelStyle,
      successIconColor: other.successIconColor,
      errorIconColor: other.errorIconColor,
      neutralIconColor: other.neutralIconColor,
      iconSize: other.iconSize,
      iconLabelGap: other.iconLabelGap,
      padding: other.padding,
      borderRadius: other.borderRadius,
      minHeight: other.minHeight,
      shadow: other.shadow,
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LemonadeToastTheme &&
        other.backgroundColor == backgroundColor &&
        other.labelStyle == labelStyle &&
        other.successIconColor == successIconColor &&
        other.errorIconColor == errorIconColor &&
        other.neutralIconColor == neutralIconColor &&
        other.iconSize == iconSize &&
        other.iconLabelGap == iconLabelGap &&
        other.padding == padding &&
        other.borderRadius == borderRadius &&
        other.minHeight == minHeight &&
        other.shadow == shadow;
  }

  @override
  int get hashCode {
    return Object.hash(
      backgroundColor,
      labelStyle,
      successIconColor,
      errorIconColor,
      neutralIconColor,
      iconSize,
      iconLabelGap,
      padding,
      borderRadius,
      minHeight,
      shadow,
    );
  }
}
