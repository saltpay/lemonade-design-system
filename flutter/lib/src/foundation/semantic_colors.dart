/// GENERATED CODE - DO NOT MODIFY BY HAND
/// *****************************************************
/// Semantic colors from Lemonade Design System Foundations
/// *****************************************************

// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: public_member_api_docs, prefer_int_literals,
// lines_longer_than_80_chars, dangling_library_doc_comments
// dart format off

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Semantic color tokens from Lemonade Design System Foundations
/// Organized by usage categories: Background, Content, Border, and Interaction
/// These tokens map to primitive colors and provide semantic meaning for UI elements
interface class LemonadeSemanticColors {
  /// Background state colors for UI elements
  final LemonadeBackgroundColors background;

  /// Border state colors for UI elements
  final LemonadeBorderColors border;

  /// Content state colors for UI elements
  final LemonadeContentColors content;

  /// Interaction state colors for UI elements
  final LemonadeInteractionColors interaction;

  /// Scoped state colors for UI elements
  final LemonadeScopedColors scoped;

  /// Shadow state colors for UI elements
  final LemonadeShadowColors shadow;

  const LemonadeSemanticColors({
    required this.background,
    required this.border,
    required this.content,
    required this.interaction,
    required this.scoped,
    required this.shadow,
  });

  /// Linearly interpolates between two [LemonadeSemanticColors] objects.
  factory LemonadeSemanticColors.lerp(
    LemonadeSemanticColors a,
    LemonadeSemanticColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeSemanticColors(
      background: LemonadeBackgroundColors.lerp(a.background, b.background, t),
      border: LemonadeBorderColors.lerp(a.border, b.border, t),
      content: LemonadeContentColors.lerp(a.content, b.content, t),
      interaction: LemonadeInteractionColors.lerp(a.interaction, b.interaction, t),
      scoped: LemonadeScopedColors.lerp(a.scoped, b.scoped, t),
      shadow: LemonadeShadowColors.lerp(a.shadow, b.shadow, t),
    );
  }

  /// Obtains the instance of [LemonadeSemanticColors] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeSemanticColors of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.colors;
  }
}

/// Background state colors for UI elements
interface class LemonadeBackgroundColors {
  final Color bgBrand;
  final Color bgBrandElevated;
  final Color bgBrandHigh;
  final Color bgBrandSubtle;
  final Color bgAlwaysDark;
  final Color bgAlwaysDarkHigh;
  final Color bgAlwaysDarkLow;
  final Color bgAlwaysDarkMedium;
  final Color bgAlwaysLight;
  final Color bgAlwaysLightHigh;
  final Color bgAlwaysLightLow;
  final Color bgAlwaysLightMedium;
  final Color bgDefaultInverse;
  final Color bgElevatedInverse;
  final Color bgSubtleInverse;
  final Color bgCaution;
  final Color bgCautionSubtle;
  final Color bgCritical;
  final Color bgCriticalSubtle;
  final Color bgFeatured;
  final Color bgFeaturedSubtle;
  final Color bgInfo;
  final Color bgInfoSubtle;
  final Color bgNeutral;
  final Color bgNeutralSubtle;
  final Color bgPositive;
  final Color bgPositiveSubtle;
  final Color bgDefault;
  final Color bgElevated;
  final Color bgElevatedHigh;
  final Color bgSubtle;

  const LemonadeBackgroundColors({
    required this.bgBrand,
    required this.bgBrandElevated,
    required this.bgBrandHigh,
    required this.bgBrandSubtle,
    required this.bgAlwaysDark,
    required this.bgAlwaysDarkHigh,
    required this.bgAlwaysDarkLow,
    required this.bgAlwaysDarkMedium,
    required this.bgAlwaysLight,
    required this.bgAlwaysLightHigh,
    required this.bgAlwaysLightLow,
    required this.bgAlwaysLightMedium,
    required this.bgDefaultInverse,
    required this.bgElevatedInverse,
    required this.bgSubtleInverse,
    required this.bgCaution,
    required this.bgCautionSubtle,
    required this.bgCritical,
    required this.bgCriticalSubtle,
    required this.bgFeatured,
    required this.bgFeaturedSubtle,
    required this.bgInfo,
    required this.bgInfoSubtle,
    required this.bgNeutral,
    required this.bgNeutralSubtle,
    required this.bgPositive,
    required this.bgPositiveSubtle,
    required this.bgDefault,
    required this.bgElevated,
    required this.bgElevatedHigh,
    required this.bgSubtle,
  });

  /// Linearly interpolates between two [LemonadeBackgroundColors] objects.
  factory LemonadeBackgroundColors.lerp(
    LemonadeBackgroundColors a,
    LemonadeBackgroundColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeBackgroundColors(
      bgBrand: Color.lerp(a.bgBrand, b.bgBrand, t)!,
      bgBrandElevated: Color.lerp(a.bgBrandElevated, b.bgBrandElevated, t)!,
      bgBrandHigh: Color.lerp(a.bgBrandHigh, b.bgBrandHigh, t)!,
      bgBrandSubtle: Color.lerp(a.bgBrandSubtle, b.bgBrandSubtle, t)!,
      bgAlwaysDark: Color.lerp(a.bgAlwaysDark, b.bgAlwaysDark, t)!,
      bgAlwaysDarkHigh: Color.lerp(a.bgAlwaysDarkHigh, b.bgAlwaysDarkHigh, t)!,
      bgAlwaysDarkLow: Color.lerp(a.bgAlwaysDarkLow, b.bgAlwaysDarkLow, t)!,
      bgAlwaysDarkMedium: Color.lerp(a.bgAlwaysDarkMedium, b.bgAlwaysDarkMedium, t)!,
      bgAlwaysLight: Color.lerp(a.bgAlwaysLight, b.bgAlwaysLight, t)!,
      bgAlwaysLightHigh: Color.lerp(a.bgAlwaysLightHigh, b.bgAlwaysLightHigh, t)!,
      bgAlwaysLightLow: Color.lerp(a.bgAlwaysLightLow, b.bgAlwaysLightLow, t)!,
      bgAlwaysLightMedium: Color.lerp(a.bgAlwaysLightMedium, b.bgAlwaysLightMedium, t)!,
      bgDefaultInverse: Color.lerp(a.bgDefaultInverse, b.bgDefaultInverse, t)!,
      bgElevatedInverse: Color.lerp(a.bgElevatedInverse, b.bgElevatedInverse, t)!,
      bgSubtleInverse: Color.lerp(a.bgSubtleInverse, b.bgSubtleInverse, t)!,
      bgCaution: Color.lerp(a.bgCaution, b.bgCaution, t)!,
      bgCautionSubtle: Color.lerp(a.bgCautionSubtle, b.bgCautionSubtle, t)!,
      bgCritical: Color.lerp(a.bgCritical, b.bgCritical, t)!,
      bgCriticalSubtle: Color.lerp(a.bgCriticalSubtle, b.bgCriticalSubtle, t)!,
      bgFeatured: Color.lerp(a.bgFeatured, b.bgFeatured, t)!,
      bgFeaturedSubtle: Color.lerp(a.bgFeaturedSubtle, b.bgFeaturedSubtle, t)!,
      bgInfo: Color.lerp(a.bgInfo, b.bgInfo, t)!,
      bgInfoSubtle: Color.lerp(a.bgInfoSubtle, b.bgInfoSubtle, t)!,
      bgNeutral: Color.lerp(a.bgNeutral, b.bgNeutral, t)!,
      bgNeutralSubtle: Color.lerp(a.bgNeutralSubtle, b.bgNeutralSubtle, t)!,
      bgPositive: Color.lerp(a.bgPositive, b.bgPositive, t)!,
      bgPositiveSubtle: Color.lerp(a.bgPositiveSubtle, b.bgPositiveSubtle, t)!,
      bgDefault: Color.lerp(a.bgDefault, b.bgDefault, t)!,
      bgElevated: Color.lerp(a.bgElevated, b.bgElevated, t)!,
      bgElevatedHigh: Color.lerp(a.bgElevatedHigh, b.bgElevatedHigh, t)!,
      bgSubtle: Color.lerp(a.bgSubtle, b.bgSubtle, t)!,
    );
  }
}
/// Border state colors for UI elements
interface class LemonadeBorderColors {
  final Color borderBrand;
  final Color borderOnBrandHigh;
  final Color borderOnBrandLow;
  final Color borderOnBrandMedium;
  final Color borderAlwaysDark;
  final Color borderAlwaysDarkHigh;
  final Color borderAlwaysDarkLow;
  final Color borderAlwaysDarkMedium;
  final Color borderAlwaysLight;
  final Color borderAlwaysLightHigh;
  final Color borderAlwaysLightLow;
  final Color borderAlwaysLightMedium;
  final Color borderBrandInverse;
  final Color borderNeutralHighInverse;
  final Color borderNeutralLowInverse;
  final Color borderNeutralMediumInverse;
  final Color borderSelectedInverse;
  final Color borderCaution;
  final Color borderCautionSubtle;
  final Color borderCritical;
  final Color borderCriticalSubtle;
  final Color borderFeatured;
  final Color borderFeaturedSubtle;
  final Color borderInfo;
  final Color borderInfoSubtle;
  final Color borderPositive;
  final Color borderPositiveSubtle;
  final Color borderNeutralHigh;
  final Color borderNeutralLow;
  final Color borderNeutralMedium;
  final Color borderSelected;

  const LemonadeBorderColors({
    required this.borderBrand,
    required this.borderOnBrandHigh,
    required this.borderOnBrandLow,
    required this.borderOnBrandMedium,
    required this.borderAlwaysDark,
    required this.borderAlwaysDarkHigh,
    required this.borderAlwaysDarkLow,
    required this.borderAlwaysDarkMedium,
    required this.borderAlwaysLight,
    required this.borderAlwaysLightHigh,
    required this.borderAlwaysLightLow,
    required this.borderAlwaysLightMedium,
    required this.borderBrandInverse,
    required this.borderNeutralHighInverse,
    required this.borderNeutralLowInverse,
    required this.borderNeutralMediumInverse,
    required this.borderSelectedInverse,
    required this.borderCaution,
    required this.borderCautionSubtle,
    required this.borderCritical,
    required this.borderCriticalSubtle,
    required this.borderFeatured,
    required this.borderFeaturedSubtle,
    required this.borderInfo,
    required this.borderInfoSubtle,
    required this.borderPositive,
    required this.borderPositiveSubtle,
    required this.borderNeutralHigh,
    required this.borderNeutralLow,
    required this.borderNeutralMedium,
    required this.borderSelected,
  });

  /// Linearly interpolates between two [LemonadeBorderColors] objects.
  factory LemonadeBorderColors.lerp(
    LemonadeBorderColors a,
    LemonadeBorderColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeBorderColors(
      borderBrand: Color.lerp(a.borderBrand, b.borderBrand, t)!,
      borderOnBrandHigh: Color.lerp(a.borderOnBrandHigh, b.borderOnBrandHigh, t)!,
      borderOnBrandLow: Color.lerp(a.borderOnBrandLow, b.borderOnBrandLow, t)!,
      borderOnBrandMedium: Color.lerp(a.borderOnBrandMedium, b.borderOnBrandMedium, t)!,
      borderAlwaysDark: Color.lerp(a.borderAlwaysDark, b.borderAlwaysDark, t)!,
      borderAlwaysDarkHigh: Color.lerp(a.borderAlwaysDarkHigh, b.borderAlwaysDarkHigh, t)!,
      borderAlwaysDarkLow: Color.lerp(a.borderAlwaysDarkLow, b.borderAlwaysDarkLow, t)!,
      borderAlwaysDarkMedium: Color.lerp(a.borderAlwaysDarkMedium, b.borderAlwaysDarkMedium, t)!,
      borderAlwaysLight: Color.lerp(a.borderAlwaysLight, b.borderAlwaysLight, t)!,
      borderAlwaysLightHigh: Color.lerp(a.borderAlwaysLightHigh, b.borderAlwaysLightHigh, t)!,
      borderAlwaysLightLow: Color.lerp(a.borderAlwaysLightLow, b.borderAlwaysLightLow, t)!,
      borderAlwaysLightMedium: Color.lerp(a.borderAlwaysLightMedium, b.borderAlwaysLightMedium, t)!,
      borderBrandInverse: Color.lerp(a.borderBrandInverse, b.borderBrandInverse, t)!,
      borderNeutralHighInverse: Color.lerp(a.borderNeutralHighInverse, b.borderNeutralHighInverse, t)!,
      borderNeutralLowInverse: Color.lerp(a.borderNeutralLowInverse, b.borderNeutralLowInverse, t)!,
      borderNeutralMediumInverse: Color.lerp(a.borderNeutralMediumInverse, b.borderNeutralMediumInverse, t)!,
      borderSelectedInverse: Color.lerp(a.borderSelectedInverse, b.borderSelectedInverse, t)!,
      borderCaution: Color.lerp(a.borderCaution, b.borderCaution, t)!,
      borderCautionSubtle: Color.lerp(a.borderCautionSubtle, b.borderCautionSubtle, t)!,
      borderCritical: Color.lerp(a.borderCritical, b.borderCritical, t)!,
      borderCriticalSubtle: Color.lerp(a.borderCriticalSubtle, b.borderCriticalSubtle, t)!,
      borderFeatured: Color.lerp(a.borderFeatured, b.borderFeatured, t)!,
      borderFeaturedSubtle: Color.lerp(a.borderFeaturedSubtle, b.borderFeaturedSubtle, t)!,
      borderInfo: Color.lerp(a.borderInfo, b.borderInfo, t)!,
      borderInfoSubtle: Color.lerp(a.borderInfoSubtle, b.borderInfoSubtle, t)!,
      borderPositive: Color.lerp(a.borderPositive, b.borderPositive, t)!,
      borderPositiveSubtle: Color.lerp(a.borderPositiveSubtle, b.borderPositiveSubtle, t)!,
      borderNeutralHigh: Color.lerp(a.borderNeutralHigh, b.borderNeutralHigh, t)!,
      borderNeutralLow: Color.lerp(a.borderNeutralLow, b.borderNeutralLow, t)!,
      borderNeutralMedium: Color.lerp(a.borderNeutralMedium, b.borderNeutralMedium, t)!,
      borderSelected: Color.lerp(a.borderSelected, b.borderSelected, t)!,
    );
  }
}
/// Content state colors for UI elements
interface class LemonadeContentColors {
  final Color contentBrand;
  final Color contentBrandHigh;
  final Color contentOnBrandHigh;
  final Color contentOnBrandLow;
  final Color contentAlwaysDark;
  final Color contentAlwaysLight;
  final Color contentCautionAlwaysOnColor;
  final Color contentCriticalAlwaysOnColor;
  final Color contentInfoAlwaysOnColor;
  final Color contentNeutralAlwaysOnColor;
  final Color contentPositiveAlwaysOnColor;
  final Color contentBrandInverse;
  final Color contentPrimaryInverse;
  final Color contentSecondaryInverse;
  final Color contentTertiaryInverse;
  final Color contentCautionOnColor;
  final Color contentCriticalOnColor;
  final Color contentFeaturedOnColor;
  final Color contentInfoOnColor;
  final Color contentNeutralOnColor;
  final Color contentPositiveOnColor;
  final Color contentCaution;
  final Color contentCritical;
  final Color contentFeatured;
  final Color contentInfo;
  final Color contentNeutral;
  final Color contentPositive;
  final Color contentPrimary;
  final Color contentSecondary;
  final Color contentTertiary;

  const LemonadeContentColors({
    required this.contentBrand,
    required this.contentBrandHigh,
    required this.contentOnBrandHigh,
    required this.contentOnBrandLow,
    required this.contentAlwaysDark,
    required this.contentAlwaysLight,
    required this.contentCautionAlwaysOnColor,
    required this.contentCriticalAlwaysOnColor,
    required this.contentInfoAlwaysOnColor,
    required this.contentNeutralAlwaysOnColor,
    required this.contentPositiveAlwaysOnColor,
    required this.contentBrandInverse,
    required this.contentPrimaryInverse,
    required this.contentSecondaryInverse,
    required this.contentTertiaryInverse,
    required this.contentCautionOnColor,
    required this.contentCriticalOnColor,
    required this.contentFeaturedOnColor,
    required this.contentInfoOnColor,
    required this.contentNeutralOnColor,
    required this.contentPositiveOnColor,
    required this.contentCaution,
    required this.contentCritical,
    required this.contentFeatured,
    required this.contentInfo,
    required this.contentNeutral,
    required this.contentPositive,
    required this.contentPrimary,
    required this.contentSecondary,
    required this.contentTertiary,
  });

  /// Linearly interpolates between two [LemonadeContentColors] objects.
  factory LemonadeContentColors.lerp(
    LemonadeContentColors a,
    LemonadeContentColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeContentColors(
      contentBrand: Color.lerp(a.contentBrand, b.contentBrand, t)!,
      contentBrandHigh: Color.lerp(a.contentBrandHigh, b.contentBrandHigh, t)!,
      contentOnBrandHigh: Color.lerp(a.contentOnBrandHigh, b.contentOnBrandHigh, t)!,
      contentOnBrandLow: Color.lerp(a.contentOnBrandLow, b.contentOnBrandLow, t)!,
      contentAlwaysDark: Color.lerp(a.contentAlwaysDark, b.contentAlwaysDark, t)!,
      contentAlwaysLight: Color.lerp(a.contentAlwaysLight, b.contentAlwaysLight, t)!,
      contentCautionAlwaysOnColor: Color.lerp(a.contentCautionAlwaysOnColor, b.contentCautionAlwaysOnColor, t)!,
      contentCriticalAlwaysOnColor: Color.lerp(a.contentCriticalAlwaysOnColor, b.contentCriticalAlwaysOnColor, t)!,
      contentInfoAlwaysOnColor: Color.lerp(a.contentInfoAlwaysOnColor, b.contentInfoAlwaysOnColor, t)!,
      contentNeutralAlwaysOnColor: Color.lerp(a.contentNeutralAlwaysOnColor, b.contentNeutralAlwaysOnColor, t)!,
      contentPositiveAlwaysOnColor: Color.lerp(a.contentPositiveAlwaysOnColor, b.contentPositiveAlwaysOnColor, t)!,
      contentBrandInverse: Color.lerp(a.contentBrandInverse, b.contentBrandInverse, t)!,
      contentPrimaryInverse: Color.lerp(a.contentPrimaryInverse, b.contentPrimaryInverse, t)!,
      contentSecondaryInverse: Color.lerp(a.contentSecondaryInverse, b.contentSecondaryInverse, t)!,
      contentTertiaryInverse: Color.lerp(a.contentTertiaryInverse, b.contentTertiaryInverse, t)!,
      contentCautionOnColor: Color.lerp(a.contentCautionOnColor, b.contentCautionOnColor, t)!,
      contentCriticalOnColor: Color.lerp(a.contentCriticalOnColor, b.contentCriticalOnColor, t)!,
      contentFeaturedOnColor: Color.lerp(a.contentFeaturedOnColor, b.contentFeaturedOnColor, t)!,
      contentInfoOnColor: Color.lerp(a.contentInfoOnColor, b.contentInfoOnColor, t)!,
      contentNeutralOnColor: Color.lerp(a.contentNeutralOnColor, b.contentNeutralOnColor, t)!,
      contentPositiveOnColor: Color.lerp(a.contentPositiveOnColor, b.contentPositiveOnColor, t)!,
      contentCaution: Color.lerp(a.contentCaution, b.contentCaution, t)!,
      contentCritical: Color.lerp(a.contentCritical, b.contentCritical, t)!,
      contentFeatured: Color.lerp(a.contentFeatured, b.contentFeatured, t)!,
      contentInfo: Color.lerp(a.contentInfo, b.contentInfo, t)!,
      contentNeutral: Color.lerp(a.contentNeutral, b.contentNeutral, t)!,
      contentPositive: Color.lerp(a.contentPositive, b.contentPositive, t)!,
      contentPrimary: Color.lerp(a.contentPrimary, b.contentPrimary, t)!,
      contentSecondary: Color.lerp(a.contentSecondary, b.contentSecondary, t)!,
      contentTertiary: Color.lerp(a.contentTertiary, b.contentTertiary, t)!,
    );
  }
}
/// Interaction state colors for UI elements
interface class LemonadeInteractionColors {
  final Color bgAlwaysDarkHighInteractive;
  final Color bgAlwaysDarkLowInteractive;
  final Color bgAlwaysDarkMediumInteractive;
  final Color bgAlwaysLightHighInteractive;
  final Color bgAlwaysLightLowInteractive;
  final Color bgAlwaysLightMediumInteractive;
  final Color bgBrandElevatedInteractive;
  final Color bgBrandHighInteractive;
  final Color bgBrandInteractive;
  final Color bgCautionInteractive;
  final Color bgCautionSubtleInteractive;
  final Color bgCriticalInteractive;
  final Color bgCriticalSubtleInteractive;
  final Color bgDefaultInteractive;
  final Color bgElevatedHighInteractive;
  final Color bgElevatedInteractive;
  final Color bgFeaturedInteractive;
  final Color bgFeaturedSubtleInteractive;
  final Color bgInfoInteractive;
  final Color bgInfoSubtleInteractive;
  final Color bgNeutralInteractive;
  final Color bgNeutralSubtleInteractive;
  final Color bgPositiveInteractive;
  final Color bgPositiveSubtleInteractive;
  final Color bgSubtleInteractive;
  final Color bgBrandElevatedPressed;
  final Color bgBrandHighPressed;
  final Color bgBrandPressed;
  final Color bgCautionPressed;
  final Color bgCautionSubtlePressed;
  final Color bgCriticalPressed;
  final Color bgCriticalSubtlePressed;
  final Color bgDefaultPressed;
  final Color bgElevatedPressed;
  final Color bgFeaturedPressed;
  final Color bgFeaturedSubtlePressed;
  final Color bgInfoPressed;
  final Color bgInfoSubtlePressed;
  final Color bgNeutralPressed;
  final Color bgNeutralSubtlePressed;
  final Color bgPositivePressed;
  final Color bgPositiveSubtlePressed;
  final Color bgSubtlePressed;

  const LemonadeInteractionColors({
    required this.bgAlwaysDarkHighInteractive,
    required this.bgAlwaysDarkLowInteractive,
    required this.bgAlwaysDarkMediumInteractive,
    required this.bgAlwaysLightHighInteractive,
    required this.bgAlwaysLightLowInteractive,
    required this.bgAlwaysLightMediumInteractive,
    required this.bgBrandElevatedInteractive,
    required this.bgBrandHighInteractive,
    required this.bgBrandInteractive,
    required this.bgCautionInteractive,
    required this.bgCautionSubtleInteractive,
    required this.bgCriticalInteractive,
    required this.bgCriticalSubtleInteractive,
    required this.bgDefaultInteractive,
    required this.bgElevatedHighInteractive,
    required this.bgElevatedInteractive,
    required this.bgFeaturedInteractive,
    required this.bgFeaturedSubtleInteractive,
    required this.bgInfoInteractive,
    required this.bgInfoSubtleInteractive,
    required this.bgNeutralInteractive,
    required this.bgNeutralSubtleInteractive,
    required this.bgPositiveInteractive,
    required this.bgPositiveSubtleInteractive,
    required this.bgSubtleInteractive,
    required this.bgBrandElevatedPressed,
    required this.bgBrandHighPressed,
    required this.bgBrandPressed,
    required this.bgCautionPressed,
    required this.bgCautionSubtlePressed,
    required this.bgCriticalPressed,
    required this.bgCriticalSubtlePressed,
    required this.bgDefaultPressed,
    required this.bgElevatedPressed,
    required this.bgFeaturedPressed,
    required this.bgFeaturedSubtlePressed,
    required this.bgInfoPressed,
    required this.bgInfoSubtlePressed,
    required this.bgNeutralPressed,
    required this.bgNeutralSubtlePressed,
    required this.bgPositivePressed,
    required this.bgPositiveSubtlePressed,
    required this.bgSubtlePressed,
  });

  /// Linearly interpolates between two [LemonadeInteractionColors] objects.
  factory LemonadeInteractionColors.lerp(
    LemonadeInteractionColors a,
    LemonadeInteractionColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeInteractionColors(
      bgAlwaysDarkHighInteractive: Color.lerp(a.bgAlwaysDarkHighInteractive, b.bgAlwaysDarkHighInteractive, t)!,
      bgAlwaysDarkLowInteractive: Color.lerp(a.bgAlwaysDarkLowInteractive, b.bgAlwaysDarkLowInteractive, t)!,
      bgAlwaysDarkMediumInteractive: Color.lerp(a.bgAlwaysDarkMediumInteractive, b.bgAlwaysDarkMediumInteractive, t)!,
      bgAlwaysLightHighInteractive: Color.lerp(a.bgAlwaysLightHighInteractive, b.bgAlwaysLightHighInteractive, t)!,
      bgAlwaysLightLowInteractive: Color.lerp(a.bgAlwaysLightLowInteractive, b.bgAlwaysLightLowInteractive, t)!,
      bgAlwaysLightMediumInteractive: Color.lerp(a.bgAlwaysLightMediumInteractive, b.bgAlwaysLightMediumInteractive, t)!,
      bgBrandElevatedInteractive: Color.lerp(a.bgBrandElevatedInteractive, b.bgBrandElevatedInteractive, t)!,
      bgBrandHighInteractive: Color.lerp(a.bgBrandHighInteractive, b.bgBrandHighInteractive, t)!,
      bgBrandInteractive: Color.lerp(a.bgBrandInteractive, b.bgBrandInteractive, t)!,
      bgCautionInteractive: Color.lerp(a.bgCautionInteractive, b.bgCautionInteractive, t)!,
      bgCautionSubtleInteractive: Color.lerp(a.bgCautionSubtleInteractive, b.bgCautionSubtleInteractive, t)!,
      bgCriticalInteractive: Color.lerp(a.bgCriticalInteractive, b.bgCriticalInteractive, t)!,
      bgCriticalSubtleInteractive: Color.lerp(a.bgCriticalSubtleInteractive, b.bgCriticalSubtleInteractive, t)!,
      bgDefaultInteractive: Color.lerp(a.bgDefaultInteractive, b.bgDefaultInteractive, t)!,
      bgElevatedHighInteractive: Color.lerp(a.bgElevatedHighInteractive, b.bgElevatedHighInteractive, t)!,
      bgElevatedInteractive: Color.lerp(a.bgElevatedInteractive, b.bgElevatedInteractive, t)!,
      bgFeaturedInteractive: Color.lerp(a.bgFeaturedInteractive, b.bgFeaturedInteractive, t)!,
      bgFeaturedSubtleInteractive: Color.lerp(a.bgFeaturedSubtleInteractive, b.bgFeaturedSubtleInteractive, t)!,
      bgInfoInteractive: Color.lerp(a.bgInfoInteractive, b.bgInfoInteractive, t)!,
      bgInfoSubtleInteractive: Color.lerp(a.bgInfoSubtleInteractive, b.bgInfoSubtleInteractive, t)!,
      bgNeutralInteractive: Color.lerp(a.bgNeutralInteractive, b.bgNeutralInteractive, t)!,
      bgNeutralSubtleInteractive: Color.lerp(a.bgNeutralSubtleInteractive, b.bgNeutralSubtleInteractive, t)!,
      bgPositiveInteractive: Color.lerp(a.bgPositiveInteractive, b.bgPositiveInteractive, t)!,
      bgPositiveSubtleInteractive: Color.lerp(a.bgPositiveSubtleInteractive, b.bgPositiveSubtleInteractive, t)!,
      bgSubtleInteractive: Color.lerp(a.bgSubtleInteractive, b.bgSubtleInteractive, t)!,
      bgBrandElevatedPressed: Color.lerp(a.bgBrandElevatedPressed, b.bgBrandElevatedPressed, t)!,
      bgBrandHighPressed: Color.lerp(a.bgBrandHighPressed, b.bgBrandHighPressed, t)!,
      bgBrandPressed: Color.lerp(a.bgBrandPressed, b.bgBrandPressed, t)!,
      bgCautionPressed: Color.lerp(a.bgCautionPressed, b.bgCautionPressed, t)!,
      bgCautionSubtlePressed: Color.lerp(a.bgCautionSubtlePressed, b.bgCautionSubtlePressed, t)!,
      bgCriticalPressed: Color.lerp(a.bgCriticalPressed, b.bgCriticalPressed, t)!,
      bgCriticalSubtlePressed: Color.lerp(a.bgCriticalSubtlePressed, b.bgCriticalSubtlePressed, t)!,
      bgDefaultPressed: Color.lerp(a.bgDefaultPressed, b.bgDefaultPressed, t)!,
      bgElevatedPressed: Color.lerp(a.bgElevatedPressed, b.bgElevatedPressed, t)!,
      bgFeaturedPressed: Color.lerp(a.bgFeaturedPressed, b.bgFeaturedPressed, t)!,
      bgFeaturedSubtlePressed: Color.lerp(a.bgFeaturedSubtlePressed, b.bgFeaturedSubtlePressed, t)!,
      bgInfoPressed: Color.lerp(a.bgInfoPressed, b.bgInfoPressed, t)!,
      bgInfoSubtlePressed: Color.lerp(a.bgInfoSubtlePressed, b.bgInfoSubtlePressed, t)!,
      bgNeutralPressed: Color.lerp(a.bgNeutralPressed, b.bgNeutralPressed, t)!,
      bgNeutralSubtlePressed: Color.lerp(a.bgNeutralSubtlePressed, b.bgNeutralSubtlePressed, t)!,
      bgPositivePressed: Color.lerp(a.bgPositivePressed, b.bgPositivePressed, t)!,
      bgPositiveSubtlePressed: Color.lerp(a.bgPositiveSubtlePressed, b.bgPositiveSubtlePressed, t)!,
      bgSubtlePressed: Color.lerp(a.bgSubtlePressed, b.bgSubtlePressed, t)!,
    );
  }
}
/// Scoped state colors for UI elements
interface class LemonadeScopedColors {
  final Color bgSettlementBusinessDays;
  final Color bgSettlementEveryday;
  final Color bgSettlementInstant;
  final Color bgSettlementScheduled;
  final Color contentOnSettlementBusinessDays;
  final Color contentOnSettlementEveryday;
  final Color contentOnSettlementInstant;
  final Color contentOnSettlementScheduled;

  const LemonadeScopedColors({
    required this.bgSettlementBusinessDays,
    required this.bgSettlementEveryday,
    required this.bgSettlementInstant,
    required this.bgSettlementScheduled,
    required this.contentOnSettlementBusinessDays,
    required this.contentOnSettlementEveryday,
    required this.contentOnSettlementInstant,
    required this.contentOnSettlementScheduled,
  });

  /// Linearly interpolates between two [LemonadeScopedColors] objects.
  factory LemonadeScopedColors.lerp(
    LemonadeScopedColors a,
    LemonadeScopedColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeScopedColors(
      bgSettlementBusinessDays: Color.lerp(a.bgSettlementBusinessDays, b.bgSettlementBusinessDays, t)!,
      bgSettlementEveryday: Color.lerp(a.bgSettlementEveryday, b.bgSettlementEveryday, t)!,
      bgSettlementInstant: Color.lerp(a.bgSettlementInstant, b.bgSettlementInstant, t)!,
      bgSettlementScheduled: Color.lerp(a.bgSettlementScheduled, b.bgSettlementScheduled, t)!,
      contentOnSettlementBusinessDays: Color.lerp(a.contentOnSettlementBusinessDays, b.contentOnSettlementBusinessDays, t)!,
      contentOnSettlementEveryday: Color.lerp(a.contentOnSettlementEveryday, b.contentOnSettlementEveryday, t)!,
      contentOnSettlementInstant: Color.lerp(a.contentOnSettlementInstant, b.contentOnSettlementInstant, t)!,
      contentOnSettlementScheduled: Color.lerp(a.contentOnSettlementScheduled, b.contentOnSettlementScheduled, t)!,
    );
  }
}
/// Shadow state colors for UI elements
interface class LemonadeShadowColors {
  final Color shadowDefault;

  const LemonadeShadowColors({
    required this.shadowDefault,
  });

  /// Linearly interpolates between two [LemonadeShadowColors] objects.
  factory LemonadeShadowColors.lerp(
    LemonadeShadowColors a,
    LemonadeShadowColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeShadowColors(
      shadowDefault: Color.lerp(a.shadowDefault, b.shadowDefault, t)!,
    );
  }
}
