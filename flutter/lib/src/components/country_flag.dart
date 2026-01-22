import 'package:flutter_svg/flutter_svg.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Sizes available for [LemonadeCountryFlag].
enum LemonadeFlagSize {
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

  /// Three times extra large size
  xxxLarge,
}

/// {@template LemonadeCountryFlag}
/// A country flag widget from the Lemonade Design System.
///
/// Displays country flags in a standardized circular format with a border.
///
/// ## Example
/// ```dart
/// LemonadeCountryFlag(
///   flag: LemonadeFlags.usUnitedStates,
///   size: LemonadeFlagSize.medium,
/// )
/// ```
///
/// See also:
/// - [LemonadeFlags], which contains all available country flags
/// - [LemonadeFlagSize], for size options
/// {@endtemplate}
class LemonadeCountryFlag extends StatelessWidget {
  /// {@macro LemonadeCountryFlag}
  const LemonadeCountryFlag({
    required this.flag,
    this.size = LemonadeFlagSize.medium,
    this.semanticIndentifier,
    this.semanticLabel,
    super.key,
  });

  /// The country flag to display from [LemonadeFlags].
  final LemonadeFlags flag;

  /// The size of the flag. Defaults to [LemonadeFlagSize.medium].
  final LemonadeFlagSize size;

  /// A semantic identifier for accessibility.
  final String? semanticIndentifier;

  /// A semantic label for accessibility.
  final String? semanticLabel;

  double _getFlagSize(LemonadeCountryFlagTheme flagTheme) {
    return switch (size) {
      LemonadeFlagSize.small => flagTheme.smallSize,
      LemonadeFlagSize.medium => flagTheme.mediumSize,
      LemonadeFlagSize.large => flagTheme.largeSize,
      LemonadeFlagSize.xLarge => flagTheme.xLargeSize,
      LemonadeFlagSize.xxLarge => flagTheme.xxLargeSize,
      LemonadeFlagSize.xxxLarge => flagTheme.xxxLargeSize,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final flagTheme = theme.components.countryFlagTheme;
    final flagSize = _getFlagSize(flagTheme);

    return Semantics(
      label: semanticLabel,
      identifier: semanticIndentifier,
      child: Align(
        widthFactor: 1,
        heightFactor: 1,
        child: Container(
          width: flagSize,
          height: flagSize,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            border: Border.all(
              color: flagTheme.borderColor,
              width: flagTheme.borderWidth,
            ),
          ),
          child: ClipOval(
            child: SvgPicture.asset(
              flag.assetPath,
              width: flagSize,
              height: flagSize,
              fit: BoxFit.cover,
            ),
          ),
        ),
      ),
    );
  }
}
