import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Sizes available for [LemonadeBadge].
enum LemonadeBadgeSize {
  /// Extra small size
  xSmall,

  /// Small size
  small,
}

/// {@template LemonadeTile}
/// A badge widget from the Lemonade Design System.
///
/// A [LemonadeBadge] is a small promotion badge that displays a label.
/// It includes different sizes.
///
/// ## Parameters
/// - [label]: The text displayed as the label of the badge.
/// - [size]: The size, defined by [LemonadeBadgeSize].
/// - [semanticIdentifier]: A semantic identifier for accessibility.
/// - [semanticLabel]: A semantic label for accessibility.
///
/// ## Example
/// ```dart
/// LemonadeBadge(
///   label: 'Tile Label',
/// )
/// ```
///
/// See also:
/// - [LemonadeTileVariants]
/// {@endtemplate}
class LemonadeBadge extends StatelessWidget {
  /// {@macro LemonadeBadge}
  const LemonadeBadge({
    required this.label,
    this.icon,
    this.size = LemonadeBadgeSize.small,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// The label to display in the badge.
  final String label;

  /// The optional icon to display in the badge.
  final LemonadeIcons? icon;

  /// The size of the badge. Defaults to [LemonadeBadgeSize.small].
  final LemonadeBadgeSize size;

  /// A semantic identifier for accessibility.
  final String? semanticIdentifier;

  /// A semantic label for accessibility.
  final String? semanticLabel;

  double _getVerticalPadding(LemonadeBadgeTheme logoTheme) {
    return switch (size) {
      LemonadeBadgeSize.xSmall => logoTheme.xSmallVerticalPadding,
      LemonadeBadgeSize.small => logoTheme.smallVerticalPadding,
    };
  }

  double _getHorizontalPadding(LemonadeBadgeTheme badgeTheme) {
    return switch (size) {
      LemonadeBadgeSize.xSmall => badgeTheme.xSmallHorizontalPadding,
      LemonadeBadgeSize.small => badgeTheme.smallHorizontalPadding,
    };
  }

  double _getFontSize(LemonadeBadgeTheme badgeTheme) {
    return switch (size) {
      LemonadeBadgeSize.xSmall => badgeTheme.xSmallFontSize,
      LemonadeBadgeSize.small => badgeTheme.smallFontSize,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final badgeTheme = theme.components.badgeTheme;
    final verticalPadding = _getVerticalPadding(badgeTheme);
    final horizontalPadding = _getHorizontalPadding(badgeTheme);
    final fontSize = _getFontSize(badgeTheme);

    return Semantics(
      label: semanticLabel ?? label,
      identifier: semanticIdentifier,
      child: Container(
        padding: EdgeInsets.symmetric(
          horizontal: horizontalPadding,
          vertical: verticalPadding,
        ),
        decoration: BoxDecoration(
          color: theme.colors.background.bgBrand,
          borderRadius: BorderRadius.circular(theme.radius.radiusFull),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (icon != null) ...[
              SizedBox(width: theme.spaces.spacing50),
              LemonadeIcon(
                icon: icon!,
                size: LemonadeIconSize.small,
                color: theme.colors.content.contentOnBrandHigh,
              ),
            ],
            Padding(
              padding: EdgeInsets.symmetric(horizontal: theme.spaces.spacing50),
              child: Text(
                label,
                maxLines: 1,
                style: theme.typography.bodyXSmallSemibold.copyWith(
                  color: theme.colors.content.contentOnBrandHigh,
                  fontSize: fontSize,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
