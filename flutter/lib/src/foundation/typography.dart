// ignore_for_file: lines_longer_than_80_chars
// dart format width=120

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Default font family used in Lemonade Design System
const String kDefaultFontFamily = 'packages/lemonade_design_system/Figtree';

/// Defines the typography styles used throughout the Lemonade Design System.
@immutable
class LemonadeTypography {
  /// Creates a [LemonadeTypography] with the specified text styles.
  const LemonadeTypography({
    this.bodyXSmallRegular = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 12,
      height: 1.33,
      fontWeight: FontWeight.w400,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXSmallMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 12,
      height: 1.33,
      fontWeight: FontWeight.w500,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXSmallSemibold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 12,
      height: 1.33,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXSmallBold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 12,
      height: 1.33,
      fontWeight: FontWeight.w700,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXSmallOverline = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 12,
      height: 1.33,
      fontWeight: FontWeight.w600,
      letterSpacing: 1.5,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodySmallRegular = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 14,
      height: 1.43,
      fontWeight: FontWeight.w400,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodySmallMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 14,
      height: 1.43,
      fontWeight: FontWeight.w500,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodySmallSemibold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 14,
      height: 1.43,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodySmallBold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 14,
      height: 1.43,
      fontWeight: FontWeight.w700,
      letterSpacing: 0.25,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyMediumRegular = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 16,
      height: 1.5,
      fontWeight: FontWeight.w400,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyMediumMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 16,
      height: 1.5,
      fontWeight: FontWeight.w500,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyMediumSemibold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 16,
      height: 1.5,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyMediumBold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 16,
      height: 1.5,
      fontWeight: FontWeight.w700,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyLargeRegular = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 18,
      height: 1.56,
      fontWeight: FontWeight.w400,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyLargeMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 18,
      height: 1.56,
      fontWeight: FontWeight.w500,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyLargeSemibold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 18,
      height: 1.56,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyLargeBold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 18,
      height: 1.56,
      fontWeight: FontWeight.w700,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXLargeRegular = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 20,
      height: 1.4,
      fontWeight: FontWeight.w400,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXLargeMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 20,
      height: 1.4,
      fontWeight: FontWeight.w500,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXLargeSemibold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 20,
      height: 1.4,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.bodyXLargeBold = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 20,
      height: 1.4,
      fontWeight: FontWeight.w700,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.heading2XSmall = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 16,
      height: 1.5,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.headingXSmall = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 18,
      height: 1.44,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.headingSmall = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 24,
      height: 1.33,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.headingMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 28,
      height: 1.29,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.headingLarge = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 32,
      height: 1.25,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.headingXLarge = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 40,
      height: 1.2,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.displayMedium = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 36,
      height: 1.22,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
    this.displayLarge = const TextStyle(
      fontFamily: kDefaultFontFamily,
      fontSize: 48,
      height: 1.17,
      fontWeight: FontWeight.w600,
      leadingDistribution: TextLeadingDistribution.even,
    ),
  });

  /// Linearly interpolates between two [LemonadeTypography] objects.
  /// If they are identical, returns [a].
  factory LemonadeTypography.lerp(
    LemonadeTypography a,
    LemonadeTypography b,
    double t,
  ) {
    if (identical(a, b)) return a;

    return LemonadeTypography(
      bodyXSmallRegular: TextStyle.lerp(a.bodyXSmallRegular, b.bodyXSmallRegular, t)!,
      bodyXSmallMedium: TextStyle.lerp(a.bodyXSmallMedium, b.bodyXSmallMedium, t)!,
      bodyXSmallSemibold: TextStyle.lerp(a.bodyXSmallSemibold, b.bodyXSmallSemibold, t)!,
      bodyXSmallBold: TextStyle.lerp(a.bodyXSmallBold, b.bodyXSmallBold, t)!,
      bodyXSmallOverline: TextStyle.lerp(a.bodyXSmallOverline, b.bodyXSmallOverline, t)!,
      bodySmallRegular: TextStyle.lerp(a.bodySmallRegular, b.bodySmallRegular, t)!,
      bodySmallMedium: TextStyle.lerp(a.bodySmallMedium, b.bodySmallMedium, t)!,
      bodySmallSemibold: TextStyle.lerp(a.bodySmallSemibold, b.bodySmallSemibold, t)!,
      bodySmallBold: TextStyle.lerp(a.bodySmallBold, b.bodySmallBold, t)!,
      bodyMediumRegular: TextStyle.lerp(a.bodyMediumRegular, b.bodyMediumRegular, t)!,
      bodyMediumMedium: TextStyle.lerp(a.bodyMediumMedium, b.bodyMediumMedium, t)!,
      bodyMediumSemibold: TextStyle.lerp(a.bodyMediumSemibold, b.bodyMediumSemibold, t)!,
      bodyMediumBold: TextStyle.lerp(a.bodyMediumBold, b.bodyMediumBold, t)!,
      bodyLargeRegular: TextStyle.lerp(a.bodyLargeRegular, b.bodyLargeRegular, t)!,
      bodyLargeMedium: TextStyle.lerp(a.bodyLargeMedium, b.bodyLargeMedium, t)!,
      bodyLargeSemibold: TextStyle.lerp(a.bodyLargeSemibold, b.bodyLargeSemibold, t)!,
      bodyLargeBold: TextStyle.lerp(a.bodyLargeBold, b.bodyLargeBold, t)!,
      bodyXLargeRegular: TextStyle.lerp(a.bodyXLargeRegular, b.bodyXLargeRegular, t)!,
      bodyXLargeMedium: TextStyle.lerp(a.bodyXLargeMedium, b.bodyXLargeMedium, t)!,
      bodyXLargeSemibold: TextStyle.lerp(a.bodyXLargeSemibold, b.bodyXLargeSemibold, t)!,
      bodyXLargeBold: TextStyle.lerp(a.bodyXLargeBold, b.bodyXLargeBold, t)!,
      heading2XSmall: TextStyle.lerp(a.heading2XSmall, b.heading2XSmall, t)!,
      headingXSmall: TextStyle.lerp(a.headingXSmall, b.headingXSmall, t)!,
      headingSmall: TextStyle.lerp(a.headingSmall, b.headingSmall, t)!,
      headingMedium: TextStyle.lerp(a.headingMedium, b.headingMedium, t)!,
      headingLarge: TextStyle.lerp(a.headingLarge, b.headingLarge, t)!,
      headingXLarge: TextStyle.lerp(a.headingXLarge, b.headingXLarge, t)!,
      displayMedium: TextStyle.lerp(a.displayMedium, b.displayMedium, t)!,
      displayLarge: TextStyle.lerp(a.displayLarge, b.displayLarge, t)!,
    );
  }

  /// Extra small body text styles (12px)
  final TextStyle bodyXSmallRegular;

  /// Extra small body text styles (12px)
  final TextStyle bodyXSmallMedium;

  /// Extra small body text styles (12px)
  final TextStyle bodyXSmallSemibold;

  /// Extra small body text styles (12px)
  final TextStyle bodyXSmallBold;

  /// Extra small body text styles (12px)
  final TextStyle bodyXSmallOverline;

  /// Small body text styles (14px)
  final TextStyle bodySmallRegular;

  /// Small body text styles (14px)
  final TextStyle bodySmallMedium;

  /// Small body text styles (14px)
  final TextStyle bodySmallSemibold;

  /// Small body text styles (14px)
  final TextStyle bodySmallBold;

  /// Medium body text styles (16px)
  final TextStyle bodyMediumRegular;

  /// Medium body text styles (16px)
  final TextStyle bodyMediumMedium;

  /// Medium body text styles (16px)
  final TextStyle bodyMediumSemibold;

  /// Medium body text styles (16px)
  final TextStyle bodyMediumBold;

  /// Large body text styles (18px)
  final TextStyle bodyLargeRegular;

  /// Large body text styles (18px)
  final TextStyle bodyLargeMedium;

  /// Large body text styles (18px)
  final TextStyle bodyLargeSemibold;

  /// Large body text styles (18px)
  final TextStyle bodyLargeBold;

  /// Extra large body text styles (20px)
  final TextStyle bodyXLargeRegular;

  /// Extra large body text styles (20px)
  final TextStyle bodyXLargeMedium;

  /// Extra large body text styles (20px)
  final TextStyle bodyXLargeSemibold;

  /// Extra large body text styles (20px)
  final TextStyle bodyXLargeBold;

  /// Heading text styles (16px)
  final TextStyle heading2XSmall;

  /// Heading text styles (18px)
  final TextStyle headingXSmall;

  /// Heading text styles (24px)
  final TextStyle headingSmall;

  /// Heading text styles (28px)
  final TextStyle headingMedium;

  /// Heading text styles (32px)
  final TextStyle headingLarge;

  /// Heading text styles (40px)
  final TextStyle headingXLarge;

  /// Display text styles (36px)
  final TextStyle displayMedium;

  /// Display text styles (48px)
  final TextStyle displayLarge;

  /// Creates a copy of this text style replacing or altering the specified
  /// properties.
  LemonadeTypography apply({
    Color? color,
    String? fontFamily,
  }) {
    return LemonadeTypography(
      bodyXSmallRegular: bodyXSmallRegular.apply(color: color, fontFamily: fontFamily),
      bodyXSmallMedium: bodyXSmallMedium.apply(color: color, fontFamily: fontFamily),
      bodyXSmallSemibold: bodyXSmallSemibold.apply(color: color, fontFamily: fontFamily),
      bodyXSmallBold: bodyXSmallBold.apply(color: color, fontFamily: fontFamily),
      bodyXSmallOverline: bodyXSmallOverline.apply(color: color, fontFamily: fontFamily),
      bodySmallRegular: bodySmallRegular.apply(color: color, fontFamily: fontFamily),
      bodySmallMedium: bodySmallMedium.apply(color: color, fontFamily: fontFamily),
      bodySmallSemibold: bodySmallSemibold.apply(color: color, fontFamily: fontFamily),
      bodySmallBold: bodySmallBold.apply(color: color, fontFamily: fontFamily),
      bodyMediumRegular: bodyMediumRegular.apply(color: color, fontFamily: fontFamily),
      bodyMediumMedium: bodyMediumMedium.apply(color: color, fontFamily: fontFamily),
      bodyMediumSemibold: bodyMediumSemibold.apply(color: color, fontFamily: fontFamily),
      bodyMediumBold: bodyMediumBold.apply(color: color, fontFamily: fontFamily),
      bodyLargeRegular: bodyLargeRegular.apply(color: color, fontFamily: fontFamily),
      bodyLargeMedium: bodyLargeMedium.apply(color: color, fontFamily: fontFamily),
      bodyLargeSemibold: bodyLargeSemibold.apply(color: color, fontFamily: fontFamily),
      bodyLargeBold: bodyLargeBold.apply(color: color, fontFamily: fontFamily),
      bodyXLargeRegular: bodyXLargeRegular.apply(color: color, fontFamily: fontFamily),
      bodyXLargeMedium: bodyXLargeMedium.apply(color: color, fontFamily: fontFamily),
      bodyXLargeSemibold: bodyXLargeSemibold.apply(color: color, fontFamily: fontFamily),
      bodyXLargeBold: bodyXLargeBold.apply(color: color, fontFamily: fontFamily),
      heading2XSmall: heading2XSmall.apply(color: color, fontFamily: fontFamily),
      headingXSmall: headingXSmall.apply(color: color, fontFamily: fontFamily),
      headingSmall: headingSmall.apply(color: color, fontFamily: fontFamily),
      headingMedium: headingMedium.apply(color: color, fontFamily: fontFamily),
      headingLarge: headingLarge.apply(color: color, fontFamily: fontFamily),
      headingXLarge: headingXLarge.apply(color: color, fontFamily: fontFamily),
      displayMedium: displayMedium.apply(color: color, fontFamily: fontFamily),
      displayLarge: displayLarge.apply(color: color, fontFamily: fontFamily),
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeTypography &&
          runtimeType == other.runtimeType &&
          bodyXSmallRegular == other.bodyXSmallRegular &&
          bodyXSmallMedium == other.bodyXSmallMedium &&
          bodyXSmallSemibold == other.bodyXSmallSemibold &&
          bodyXSmallBold == other.bodyXSmallBold &&
          bodyXSmallOverline == other.bodyXSmallOverline &&
          bodySmallRegular == other.bodySmallRegular &&
          bodySmallMedium == other.bodySmallMedium &&
          bodySmallSemibold == other.bodySmallSemibold &&
          bodySmallBold == other.bodySmallBold &&
          bodyMediumRegular == other.bodyMediumRegular &&
          bodyMediumMedium == other.bodyMediumMedium &&
          bodyMediumSemibold == other.bodyMediumSemibold &&
          bodyMediumBold == other.bodyMediumBold &&
          bodyLargeRegular == other.bodyLargeRegular &&
          bodyLargeMedium == other.bodyLargeMedium &&
          bodyLargeSemibold == other.bodyLargeSemibold &&
          bodyLargeBold == other.bodyLargeBold &&
          bodyXLargeRegular == other.bodyXLargeRegular &&
          bodyXLargeMedium == other.bodyXLargeMedium &&
          bodyXLargeSemibold == other.bodyXLargeSemibold &&
          bodyXLargeBold == other.bodyXLargeBold &&
          heading2XSmall == other.heading2XSmall &&
          headingXSmall == other.headingXSmall &&
          headingSmall == other.headingSmall &&
          headingMedium == other.headingMedium &&
          headingLarge == other.headingLarge &&
          headingXLarge == other.headingXLarge &&
          displayMedium == other.displayMedium &&
          displayLarge == other.displayLarge;

  @override
  int get hashCode => Object.hashAll([
    bodyXSmallRegular,
    bodyXSmallMedium,
    bodyXSmallSemibold,
    bodyXSmallBold,
    bodyXSmallOverline,
    bodySmallRegular,
    bodySmallMedium,
    bodySmallSemibold,
    bodySmallBold,
    bodyMediumRegular,
    bodyMediumMedium,
    bodyMediumSemibold,
    bodyMediumBold,
    bodyLargeRegular,
    bodyLargeMedium,
    bodyLargeSemibold,
    bodyLargeBold,
    bodyXLargeRegular,
    bodyXLargeMedium,
    bodyXLargeSemibold,
    bodyXLargeBold,
    heading2XSmall,
    headingXSmall,
    headingSmall,
    headingMedium,
    headingLarge,
    headingXLarge,
    displayMedium,
    displayLarge,
  ]);

  /// Helper method to access [LemonadeTypography] from the closest
  /// [LemonadeTheme] ancestor.
  static LemonadeTypography of(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return theme.typography;
  }
}
