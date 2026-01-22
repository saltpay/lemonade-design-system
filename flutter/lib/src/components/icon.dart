import 'package:flutter_svg/flutter_svg.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Sizes available for [LemonadeIcon].
enum LemonadeIconSize {
  /// Extra small size
  xSmall,

  /// Small size
  small,

  /// Medium size
  medium,

  /// Large size
  large,

  /// Extra large size
  xLarge,
}

/// {@template LemonadeIcon}
/// An icon widget from the Lemonade Design System.
///
/// Displays SVG icons from the design system with configurable size and color.
///
/// ## Example
/// ```dart
/// LemonadeIcon(
///   icon: LemonadeIcons.search,
///   size: LemonadeIconSize.medium,
///   color: theme.colors.content.contentDefault,
/// )
/// ```
///
/// See also:
/// - [LemonadeIcons], which contains all available icons
/// - [LemonadeIconSize], for size options
/// {@endtemplate}
class LemonadeIcon extends StatelessWidget {
  /// {@macro LemonadeIcon}
  const LemonadeIcon({
    required this.icon,
    this.size = LemonadeIconSize.medium,
    this.semanticIdentifier,
    this.semanticLabel,
    this.color,
    super.key,
  });

  /// {@template LemonadeIcon.icon}
  /// The icon to display from [LemonadeIcons].
  ///
  /// This determines which icon from the design system will be rendered.
  /// {@endtemplate}
  final LemonadeIcons icon;

  /// {@template LemonadeIcon.size}
  /// The size of the icon.
  ///
  /// Determines the dimensions of the icon. Defaults to
  /// [LemonadeIconSize.medium].
  /// {@endtemplate}
  final LemonadeIconSize size;

  /// {@template LemonadeIcon.semanticsIdentifier}
  /// An identifier for the icon used for accessibility and testing purposes.
  ///
  /// This identifier helps to uniquely identify the icon in the UI.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeIcon.semanticLabel}
  /// A label for the icon used for accessibility purposes.
  ///
  /// This label provides a description of the icon's purpose.
  /// {@endtemplate}
  final String? semanticLabel;

  /// {@template LemonadeIcon.color}
  /// Optional color override for the icon.
  ///
  /// If not provided, uses the default icon color from theme
  /// ([LemonadeContentColors.contentPrimary]).
  /// {@endtemplate}
  final Color? color;

  double _getIconSize(LemonadeIconTheme iconTheme) {
    return switch (size) {
      LemonadeIconSize.xSmall => iconTheme.xSmallSize,
      LemonadeIconSize.small => iconTheme.smallSize,
      LemonadeIconSize.medium => iconTheme.mediumSize,
      LemonadeIconSize.large => iconTheme.largeSize,
      LemonadeIconSize.xLarge => iconTheme.xLargeSize,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final iconSize = _getIconSize(theme.components.iconTheme);
    final iconColor = color ?? theme.components.iconTheme.color;

    return Semantics(
      label: semanticLabel,
      identifier: semanticIdentifier,
      child: Align(
        widthFactor: 1,
        heightFactor: 1,
        child: SvgPicture.asset(
          icon.assetPath,
          width: iconSize,
          height: iconSize,
          colorFilter: ColorFilter.mode(
            iconColor,
            BlendMode.srcIn,
          ),
        ),
      ),
    );
  }
}
