import 'package:flutter_svg/flutter_svg.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Sizes available for [LemonadeBrandLogo].
enum LemonadeBrandLogoSize {
  /// Small size
  small,

  /// Medium size
  medium,

  /// Large size
  large,

  /// Extra large size
  xLarge,

  /// Two times extra large size
  xxLarge,
}

/// {@template LemonadeBrandLogo}
/// A brand logo widget from the Lemonade Design System.
///
/// Displays brand logos (payment methods, partners, etc.)
/// with configurable size.
///
/// ## Example
/// ```dart
/// LemonadeBrandLogo(
///   logo: LemonadeBrandLogos.visa,
///   size: LemonadeBrandLogoSize.medium,
/// )
/// ```
///
/// See also:
/// - [LemonadeBrandLogos], which contains all available brand logos
/// - [LemonadeBrandLogoSize], for size options
/// {@endtemplate}
class LemonadeBrandLogo extends StatelessWidget {
  /// {@macro LemonadeBrandLogo}
  const LemonadeBrandLogo({
    required this.logo,
    this.size = LemonadeBrandLogoSize.medium,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// The brand logo to display from [LemonadeBrandLogos].
  final LemonadeBrandLogos logo;

  /// The size of the brand logo. Defaults to [LemonadeBrandLogoSize.medium].
  final LemonadeBrandLogoSize size;

  /// A semantic identifier for accessibility.
  final String? semanticIdentifier;

  /// A semantic label for accessibility.
  final String? semanticLabel;

  double _getLogoSize(LemonadeBrandLogoTheme logoTheme) {
    return switch (size) {
      LemonadeBrandLogoSize.small => logoTheme.smallSize,
      LemonadeBrandLogoSize.medium => logoTheme.mediumSize,
      LemonadeBrandLogoSize.large => logoTheme.largeSize,
      LemonadeBrandLogoSize.xLarge => logoTheme.xLargeSize,
      LemonadeBrandLogoSize.xxLarge => logoTheme.xxLargeSize,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final logoTheme = theme.components.brandLogoTheme;
    final logoSize = _getLogoSize(logoTheme);

    return Semantics(
      label: semanticLabel,
      identifier: semanticIdentifier,
      child: Align(
        widthFactor: 1,
        heightFactor: 1,
        child: SvgPicture.asset(
          logo.assetPath,
          width: logoSize,
          height: logoSize,
        ),
      ),
    );
  }
}
