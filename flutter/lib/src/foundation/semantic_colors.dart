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
  /// Interaction state colors for UI elements
  final LemonadeInteractionColors interaction;

  /// Border state colors for UI elements
  final LemonadeBorderColors border;

  /// Content state colors for UI elements
  final LemonadeContentColors content;

  /// Background state colors for UI elements
  final LemonadeBackgroundColors background;

  const LemonadeSemanticColors({
    required this.interaction,
    required this.border,
    required this.content,
    required this.background,
  });

  /// Linearly interpolates between two [LemonadeSemanticColors] objects.
  factory LemonadeSemanticColors.lerp(
    LemonadeSemanticColors a,
    LemonadeSemanticColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeSemanticColors(
      interaction: LemonadeInteractionColors.lerp(a.interaction, b.interaction, t),
      border: LemonadeBorderColors.lerp(a.border, b.border, t),
      content: LemonadeContentColors.lerp(a.content, b.content, t),
      background: LemonadeBackgroundColors.lerp(a.background, b.background, t),
    );
  }

  /// Obtains the instance of [LemonadeSemanticColors] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeSemanticColors of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.colors;
  }
}

/// Interaction state colors for UI elements
interface class LemonadeInteractionColors {
  final Color bgNeutralSubtleInteractive;
  final Color bgPositiveSubtleInteractive;
  final Color bgInfoSubtleInteractive;
  final Color bgNeutralInteractive;
  final Color bgBrandHighInteractive;
  final Color bgDefaultInteractive;
  final Color bgCautionInteractive;
  final Color bgPositiveInteractive;
  final Color bgSubtleInteractive;
  final Color bgInfoInteractive;
  final Color bgCautionSubtleInteractive;
  final Color bgCriticalInteractive;
  final Color bgBrandInteractive;
  final Color bgElevatedInteractive;
  final Color bgCriticalSubtleInteractive;
  final Color bgDefaultPressed;
  final Color bgElevatedPressed;
  final Color bgBrandPressed;
  final Color bgBrandHighPressed;
  final Color bgCriticalPressed;
  final Color bgCautionPressed;
  final Color bgInfoPressed;
  final Color bgPositivePressed;
  final Color bgNeutralPressed;
  final Color bgCautionSubtlePressed;
  final Color bgInfoSubtlePressed;
  final Color bgPositiveSubtlePressed;
  final Color bgNeutralSubtlePressed;
  final Color bgElevatedHighInteractive;
  final Color bgBrandElevatedInteractive;
  final Color bgBrandElevatedPressed;

  const LemonadeInteractionColors({
    required this.bgNeutralSubtleInteractive,
    required this.bgPositiveSubtleInteractive,
    required this.bgInfoSubtleInteractive,
    required this.bgNeutralInteractive,
    required this.bgBrandHighInteractive,
    required this.bgDefaultInteractive,
    required this.bgCautionInteractive,
    required this.bgPositiveInteractive,
    required this.bgSubtleInteractive,
    required this.bgInfoInteractive,
    required this.bgCautionSubtleInteractive,
    required this.bgCriticalInteractive,
    required this.bgBrandInteractive,
    required this.bgElevatedInteractive,
    required this.bgCriticalSubtleInteractive,
    required this.bgDefaultPressed,
    required this.bgElevatedPressed,
    required this.bgBrandPressed,
    required this.bgBrandHighPressed,
    required this.bgCriticalPressed,
    required this.bgCautionPressed,
    required this.bgInfoPressed,
    required this.bgPositivePressed,
    required this.bgNeutralPressed,
    required this.bgCautionSubtlePressed,
    required this.bgInfoSubtlePressed,
    required this.bgPositiveSubtlePressed,
    required this.bgNeutralSubtlePressed,
    required this.bgElevatedHighInteractive,
    required this.bgBrandElevatedInteractive,
    required this.bgBrandElevatedPressed,
  });

  /// Linearly interpolates between two [LemonadeInteractionColors] objects.
  factory LemonadeInteractionColors.lerp(
    LemonadeInteractionColors a,
    LemonadeInteractionColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeInteractionColors(
      bgNeutralSubtleInteractive: Color.lerp(a.bgNeutralSubtleInteractive, b.bgNeutralSubtleInteractive, t)!,
      bgPositiveSubtleInteractive: Color.lerp(a.bgPositiveSubtleInteractive, b.bgPositiveSubtleInteractive, t)!,
      bgInfoSubtleInteractive: Color.lerp(a.bgInfoSubtleInteractive, b.bgInfoSubtleInteractive, t)!,
      bgNeutralInteractive: Color.lerp(a.bgNeutralInteractive, b.bgNeutralInteractive, t)!,
      bgBrandHighInteractive: Color.lerp(a.bgBrandHighInteractive, b.bgBrandHighInteractive, t)!,
      bgDefaultInteractive: Color.lerp(a.bgDefaultInteractive, b.bgDefaultInteractive, t)!,
      bgCautionInteractive: Color.lerp(a.bgCautionInteractive, b.bgCautionInteractive, t)!,
      bgPositiveInteractive: Color.lerp(a.bgPositiveInteractive, b.bgPositiveInteractive, t)!,
      bgSubtleInteractive: Color.lerp(a.bgSubtleInteractive, b.bgSubtleInteractive, t)!,
      bgInfoInteractive: Color.lerp(a.bgInfoInteractive, b.bgInfoInteractive, t)!,
      bgCautionSubtleInteractive: Color.lerp(a.bgCautionSubtleInteractive, b.bgCautionSubtleInteractive, t)!,
      bgCriticalInteractive: Color.lerp(a.bgCriticalInteractive, b.bgCriticalInteractive, t)!,
      bgBrandInteractive: Color.lerp(a.bgBrandInteractive, b.bgBrandInteractive, t)!,
      bgElevatedInteractive: Color.lerp(a.bgElevatedInteractive, b.bgElevatedInteractive, t)!,
      bgCriticalSubtleInteractive: Color.lerp(a.bgCriticalSubtleInteractive, b.bgCriticalSubtleInteractive, t)!,
      bgDefaultPressed: Color.lerp(a.bgDefaultPressed, b.bgDefaultPressed, t)!,
      bgElevatedPressed: Color.lerp(a.bgElevatedPressed, b.bgElevatedPressed, t)!,
      bgBrandPressed: Color.lerp(a.bgBrandPressed, b.bgBrandPressed, t)!,
      bgBrandHighPressed: Color.lerp(a.bgBrandHighPressed, b.bgBrandHighPressed, t)!,
      bgCriticalPressed: Color.lerp(a.bgCriticalPressed, b.bgCriticalPressed, t)!,
      bgCautionPressed: Color.lerp(a.bgCautionPressed, b.bgCautionPressed, t)!,
      bgInfoPressed: Color.lerp(a.bgInfoPressed, b.bgInfoPressed, t)!,
      bgPositivePressed: Color.lerp(a.bgPositivePressed, b.bgPositivePressed, t)!,
      bgNeutralPressed: Color.lerp(a.bgNeutralPressed, b.bgNeutralPressed, t)!,
      bgCautionSubtlePressed: Color.lerp(a.bgCautionSubtlePressed, b.bgCautionSubtlePressed, t)!,
      bgInfoSubtlePressed: Color.lerp(a.bgInfoSubtlePressed, b.bgInfoSubtlePressed, t)!,
      bgPositiveSubtlePressed: Color.lerp(a.bgPositiveSubtlePressed, b.bgPositiveSubtlePressed, t)!,
      bgNeutralSubtlePressed: Color.lerp(a.bgNeutralSubtlePressed, b.bgNeutralSubtlePressed, t)!,
      bgElevatedHighInteractive: Color.lerp(a.bgElevatedHighInteractive, b.bgElevatedHighInteractive, t)!,
      bgBrandElevatedInteractive: Color.lerp(a.bgBrandElevatedInteractive, b.bgBrandElevatedInteractive, t)!,
      bgBrandElevatedPressed: Color.lerp(a.bgBrandElevatedPressed, b.bgBrandElevatedPressed, t)!,
    );
  }
}
/// Border state colors for UI elements
interface class LemonadeBorderColors {
  final Color borderBrandInverse;
  final Color borderSelectedInverse;
  final Color borderNeutralMediumInverse;
  final Color borderNeutralLowInverse;
  final Color borderAlwaysDark;
  final Color borderPositiveSubtle;
  final Color borderInfoSubtle;
  final Color borderCautionSubtle;
  final Color borderCriticalSubtle;
  final Color borderPositive;
  final Color borderInfo;
  final Color borderCaution;
  final Color borderOnBrandMedium;
  final Color borderBrand;
  final Color borderSelected;
  final Color borderOnBrandLow;
  final Color borderOnBrandHigh;
  final Color borderCritical;
  final Color borderNeutralLow;
  final Color borderNeutralMedium;
  final Color borderAlwaysLight;
  final Color borderNeutralHigh;
  final Color borderNeutralHighInverse;

  const LemonadeBorderColors({
    required this.borderBrandInverse,
    required this.borderSelectedInverse,
    required this.borderNeutralMediumInverse,
    required this.borderNeutralLowInverse,
    required this.borderAlwaysDark,
    required this.borderPositiveSubtle,
    required this.borderInfoSubtle,
    required this.borderCautionSubtle,
    required this.borderCriticalSubtle,
    required this.borderPositive,
    required this.borderInfo,
    required this.borderCaution,
    required this.borderOnBrandMedium,
    required this.borderBrand,
    required this.borderSelected,
    required this.borderOnBrandLow,
    required this.borderOnBrandHigh,
    required this.borderCritical,
    required this.borderNeutralLow,
    required this.borderNeutralMedium,
    required this.borderAlwaysLight,
    required this.borderNeutralHigh,
    required this.borderNeutralHighInverse,
  });

  /// Linearly interpolates between two [LemonadeBorderColors] objects.
  factory LemonadeBorderColors.lerp(
    LemonadeBorderColors a,
    LemonadeBorderColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeBorderColors(
      borderBrandInverse: Color.lerp(a.borderBrandInverse, b.borderBrandInverse, t)!,
      borderSelectedInverse: Color.lerp(a.borderSelectedInverse, b.borderSelectedInverse, t)!,
      borderNeutralMediumInverse: Color.lerp(a.borderNeutralMediumInverse, b.borderNeutralMediumInverse, t)!,
      borderNeutralLowInverse: Color.lerp(a.borderNeutralLowInverse, b.borderNeutralLowInverse, t)!,
      borderAlwaysDark: Color.lerp(a.borderAlwaysDark, b.borderAlwaysDark, t)!,
      borderPositiveSubtle: Color.lerp(a.borderPositiveSubtle, b.borderPositiveSubtle, t)!,
      borderInfoSubtle: Color.lerp(a.borderInfoSubtle, b.borderInfoSubtle, t)!,
      borderCautionSubtle: Color.lerp(a.borderCautionSubtle, b.borderCautionSubtle, t)!,
      borderCriticalSubtle: Color.lerp(a.borderCriticalSubtle, b.borderCriticalSubtle, t)!,
      borderPositive: Color.lerp(a.borderPositive, b.borderPositive, t)!,
      borderInfo: Color.lerp(a.borderInfo, b.borderInfo, t)!,
      borderCaution: Color.lerp(a.borderCaution, b.borderCaution, t)!,
      borderOnBrandMedium: Color.lerp(a.borderOnBrandMedium, b.borderOnBrandMedium, t)!,
      borderBrand: Color.lerp(a.borderBrand, b.borderBrand, t)!,
      borderSelected: Color.lerp(a.borderSelected, b.borderSelected, t)!,
      borderOnBrandLow: Color.lerp(a.borderOnBrandLow, b.borderOnBrandLow, t)!,
      borderOnBrandHigh: Color.lerp(a.borderOnBrandHigh, b.borderOnBrandHigh, t)!,
      borderCritical: Color.lerp(a.borderCritical, b.borderCritical, t)!,
      borderNeutralLow: Color.lerp(a.borderNeutralLow, b.borderNeutralLow, t)!,
      borderNeutralMedium: Color.lerp(a.borderNeutralMedium, b.borderNeutralMedium, t)!,
      borderAlwaysLight: Color.lerp(a.borderAlwaysLight, b.borderAlwaysLight, t)!,
      borderNeutralHigh: Color.lerp(a.borderNeutralHigh, b.borderNeutralHigh, t)!,
      borderNeutralHighInverse: Color.lerp(a.borderNeutralHighInverse, b.borderNeutralHighInverse, t)!,
    );
  }
}
/// Content state colors for UI elements
interface class LemonadeContentColors {
  final Color contentOnBrandLow;
  final Color contentBrand;
  final Color contentCaution;
  final Color contentInfo;
  final Color contentTertiary;
  final Color contentAlwaysLight;
  final Color contentSecondary;
  final Color contentPrimaryInverse;
  final Color contentPositive;
  final Color contentTertiaryInverse;
  final Color contentAlwaysDark;
  final Color contentNeutral;
  final Color contentBrandInverse;
  final Color contentSecondaryInverse;
  final Color contentCritical;
  final Color contentOnBrandHigh;
  final Color contentPrimary;
  final Color contentCautionOnColor;
  final Color contentInfoOnColor;
  final Color contentPositiveOnColor;
  final Color contentNeutralOnColor;
  final Color contentBrandHigh;

  const LemonadeContentColors({
    required this.contentOnBrandLow,
    required this.contentBrand,
    required this.contentCaution,
    required this.contentInfo,
    required this.contentTertiary,
    required this.contentAlwaysLight,
    required this.contentSecondary,
    required this.contentPrimaryInverse,
    required this.contentPositive,
    required this.contentTertiaryInverse,
    required this.contentAlwaysDark,
    required this.contentNeutral,
    required this.contentBrandInverse,
    required this.contentSecondaryInverse,
    required this.contentCritical,
    required this.contentOnBrandHigh,
    required this.contentPrimary,
    required this.contentCautionOnColor,
    required this.contentInfoOnColor,
    required this.contentPositiveOnColor,
    required this.contentNeutralOnColor,
    required this.contentBrandHigh,
  });

  /// Linearly interpolates between two [LemonadeContentColors] objects.
  factory LemonadeContentColors.lerp(
    LemonadeContentColors a,
    LemonadeContentColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeContentColors(
      contentOnBrandLow: Color.lerp(a.contentOnBrandLow, b.contentOnBrandLow, t)!,
      contentBrand: Color.lerp(a.contentBrand, b.contentBrand, t)!,
      contentCaution: Color.lerp(a.contentCaution, b.contentCaution, t)!,
      contentInfo: Color.lerp(a.contentInfo, b.contentInfo, t)!,
      contentTertiary: Color.lerp(a.contentTertiary, b.contentTertiary, t)!,
      contentAlwaysLight: Color.lerp(a.contentAlwaysLight, b.contentAlwaysLight, t)!,
      contentSecondary: Color.lerp(a.contentSecondary, b.contentSecondary, t)!,
      contentPrimaryInverse: Color.lerp(a.contentPrimaryInverse, b.contentPrimaryInverse, t)!,
      contentPositive: Color.lerp(a.contentPositive, b.contentPositive, t)!,
      contentTertiaryInverse: Color.lerp(a.contentTertiaryInverse, b.contentTertiaryInverse, t)!,
      contentAlwaysDark: Color.lerp(a.contentAlwaysDark, b.contentAlwaysDark, t)!,
      contentNeutral: Color.lerp(a.contentNeutral, b.contentNeutral, t)!,
      contentBrandInverse: Color.lerp(a.contentBrandInverse, b.contentBrandInverse, t)!,
      contentSecondaryInverse: Color.lerp(a.contentSecondaryInverse, b.contentSecondaryInverse, t)!,
      contentCritical: Color.lerp(a.contentCritical, b.contentCritical, t)!,
      contentOnBrandHigh: Color.lerp(a.contentOnBrandHigh, b.contentOnBrandHigh, t)!,
      contentPrimary: Color.lerp(a.contentPrimary, b.contentPrimary, t)!,
      contentCautionOnColor: Color.lerp(a.contentCautionOnColor, b.contentCautionOnColor, t)!,
      contentInfoOnColor: Color.lerp(a.contentInfoOnColor, b.contentInfoOnColor, t)!,
      contentPositiveOnColor: Color.lerp(a.contentPositiveOnColor, b.contentPositiveOnColor, t)!,
      contentNeutralOnColor: Color.lerp(a.contentNeutralOnColor, b.contentNeutralOnColor, t)!,
      contentBrandHigh: Color.lerp(a.contentBrandHigh, b.contentBrandHigh, t)!,
    );
  }
}
/// Background state colors for UI elements
interface class LemonadeBackgroundColors {
  final Color bgAlwaysLight;
  final Color bgAlwaysDark;
  final Color bgDefaultInverse;
  final Color bgNeutralSubtle;
  final Color bgCautionSubtle;
  final Color bgCriticalSubtle;
  final Color bgNeutral;
  final Color bgBrandSubtle;
  final Color bgSubtleInverse;
  final Color bgBrandElevated;
  final Color bgPositiveSubtle;
  final Color bgDefault;
  final Color bgCritical;
  final Color bgBrandHigh;
  final Color bgCaution;
  final Color bgBrand;
  final Color bgElevated;
  final Color bgElevatedInverse;
  final Color bgSubtle;
  final Color bgInfo;
  final Color bgPositive;
  final Color bgInfoSubtle;
  final Color bgElevatedHigh;

  const LemonadeBackgroundColors({
    required this.bgAlwaysLight,
    required this.bgAlwaysDark,
    required this.bgDefaultInverse,
    required this.bgNeutralSubtle,
    required this.bgCautionSubtle,
    required this.bgCriticalSubtle,
    required this.bgNeutral,
    required this.bgBrandSubtle,
    required this.bgSubtleInverse,
    required this.bgBrandElevated,
    required this.bgPositiveSubtle,
    required this.bgDefault,
    required this.bgCritical,
    required this.bgBrandHigh,
    required this.bgCaution,
    required this.bgBrand,
    required this.bgElevated,
    required this.bgElevatedInverse,
    required this.bgSubtle,
    required this.bgInfo,
    required this.bgPositive,
    required this.bgInfoSubtle,
    required this.bgElevatedHigh,
  });

  /// Linearly interpolates between two [LemonadeBackgroundColors] objects.
  factory LemonadeBackgroundColors.lerp(
    LemonadeBackgroundColors a,
    LemonadeBackgroundColors b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeBackgroundColors(
      bgAlwaysLight: Color.lerp(a.bgAlwaysLight, b.bgAlwaysLight, t)!,
      bgAlwaysDark: Color.lerp(a.bgAlwaysDark, b.bgAlwaysDark, t)!,
      bgDefaultInverse: Color.lerp(a.bgDefaultInverse, b.bgDefaultInverse, t)!,
      bgNeutralSubtle: Color.lerp(a.bgNeutralSubtle, b.bgNeutralSubtle, t)!,
      bgCautionSubtle: Color.lerp(a.bgCautionSubtle, b.bgCautionSubtle, t)!,
      bgCriticalSubtle: Color.lerp(a.bgCriticalSubtle, b.bgCriticalSubtle, t)!,
      bgNeutral: Color.lerp(a.bgNeutral, b.bgNeutral, t)!,
      bgBrandSubtle: Color.lerp(a.bgBrandSubtle, b.bgBrandSubtle, t)!,
      bgSubtleInverse: Color.lerp(a.bgSubtleInverse, b.bgSubtleInverse, t)!,
      bgBrandElevated: Color.lerp(a.bgBrandElevated, b.bgBrandElevated, t)!,
      bgPositiveSubtle: Color.lerp(a.bgPositiveSubtle, b.bgPositiveSubtle, t)!,
      bgDefault: Color.lerp(a.bgDefault, b.bgDefault, t)!,
      bgCritical: Color.lerp(a.bgCritical, b.bgCritical, t)!,
      bgBrandHigh: Color.lerp(a.bgBrandHigh, b.bgBrandHigh, t)!,
      bgCaution: Color.lerp(a.bgCaution, b.bgCaution, t)!,
      bgBrand: Color.lerp(a.bgBrand, b.bgBrand, t)!,
      bgElevated: Color.lerp(a.bgElevated, b.bgElevated, t)!,
      bgElevatedInverse: Color.lerp(a.bgElevatedInverse, b.bgElevatedInverse, t)!,
      bgSubtle: Color.lerp(a.bgSubtle, b.bgSubtle, t)!,
      bgInfo: Color.lerp(a.bgInfo, b.bgInfo, t)!,
      bgPositive: Color.lerp(a.bgPositive, b.bgPositive, t)!,
      bgInfoSubtle: Color.lerp(a.bgInfoSubtle, b.bgInfoSubtle, t)!,
      bgElevatedHigh: Color.lerp(a.bgElevatedHigh, b.bgElevatedHigh, t)!,
    );
  }
}
