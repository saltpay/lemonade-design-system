import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Voice variants available for [LemonadeSymbolContainer].
enum LemonadeSymbolContainerVoice {
  /// Neutral voice - default styling
  neutral,

  /// Critical voice - error/danger styling
  critical,

  /// Warning voice - caution styling
  warning,

  /// Info voice - informational styling
  info,

  /// Positive voice - success styling
  positive,

  /// Brand voice - brand color styling
  brand,

  /// Brand subtle voice - subtle brand styling
  brandSubtle,
}

/// Size variants available for [LemonadeSymbolContainer].
enum LemonadeSymbolContainerSize {
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

/// {@template LemonadeSymbolContainer}
/// A symbol container widget from the Lemonade Design System.
///
/// A [LemonadeSymbolContainer] is a versatile container used to display
/// an icon, text, or custom content with consistent sizing and styling.
///
/// ## Example with icon
/// ```dart
/// LemonadeSymbolContainer.icon(
///   icon: LemonadeIcons.heart,
///   voice: LemonadeSymbolContainerVoice.info,
///   size: LemonadeSymbolContainerSize.medium,
/// )
/// ```
///
/// ## Example with text
/// ```dart
/// LemonadeSymbolContainer.text(
///   text: 'A',
///   voice: LemonadeSymbolContainerVoice.positive,
/// )
/// ```
///
/// See also:
/// - [LemonadeSymbolContainerTheme], for theme configuration
/// {@endtemplate}
class LemonadeSymbolContainer extends StatelessWidget {
  /// Creates a symbol container with an icon.
  const LemonadeSymbolContainer.icon({
    required this.icon,
    this.voice = LemonadeSymbolContainerVoice.neutral,
    this.size = LemonadeSymbolContainerSize.medium,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : text = null,
       child = null;

  /// Creates a symbol container with text.
  const LemonadeSymbolContainer.text({
    required String this.text,
    this.voice = LemonadeSymbolContainerVoice.neutral,
    this.size = LemonadeSymbolContainerSize.medium,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : icon = null,
       child = null;

  /// Creates a symbol container with custom content.
  const LemonadeSymbolContainer.custom({
    required Widget this.child,
    this.voice = LemonadeSymbolContainerVoice.neutral,
    this.size = LemonadeSymbolContainerSize.medium,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : icon = null,
       text = null;

  /// The icon to display (if using icon variant).
  final LemonadeIcons? icon;

  /// The text to display (if using text variant).
  final String? text;

  /// Custom content (if using custom variant).
  final Widget? child;

  /// The voice/tone of the container. Defaults to [LemonadeSymbolContainerVoice.neutral].
  final LemonadeSymbolContainerVoice voice;

  /// The size of the container.
  /// Defaults to [LemonadeSymbolContainerSize.medium].
  final LemonadeSymbolContainerSize size;

  /// An identifier for the container for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the container for accessibility purposes.
  final String? semanticLabel;

  Color _getTintColor(LemonadeSemanticColors colors) {
    return switch (voice) {
      LemonadeSymbolContainerVoice.neutral => colors.content.contentPrimary,
      LemonadeSymbolContainerVoice.critical => colors.content.contentCritical,
      LemonadeSymbolContainerVoice.warning => colors.content.contentCaution,
      LemonadeSymbolContainerVoice.info => colors.content.contentInfo,
      LemonadeSymbolContainerVoice.positive => colors.content.contentPositive,
      LemonadeSymbolContainerVoice.brand => colors.content.contentOnBrandHigh,
      LemonadeSymbolContainerVoice.brandSubtle =>
        colors.content.contentOnBrandHigh,
    };
  }

  Color _getContainerColor(LemonadeSemanticColors colors) {
    return switch (voice) {
      LemonadeSymbolContainerVoice.neutral => colors.background.bgNeutralSubtle,
      LemonadeSymbolContainerVoice.critical =>
        colors.background.bgCriticalSubtle,
      LemonadeSymbolContainerVoice.warning => colors.background.bgCautionSubtle,
      LemonadeSymbolContainerVoice.info => colors.background.bgInfoSubtle,
      LemonadeSymbolContainerVoice.positive =>
        colors.background.bgPositiveSubtle,
      LemonadeSymbolContainerVoice.brand => colors.background.bgBrand,
      LemonadeSymbolContainerVoice.brandSubtle =>
        colors.background.bgBrandSubtle,
    };
  }

  _SymbolContainerDimensions _getDimensions(
    LemonadeSymbolContainerTheme theme,
  ) {
    return switch (size) {
      LemonadeSymbolContainerSize.xSmall => _SymbolContainerDimensions(
        containerSize: theme.xSmallContainerSize,
        iconSize: LemonadeIconSize.xSmall,
      ),
      LemonadeSymbolContainerSize.small => _SymbolContainerDimensions(
        containerSize: theme.smallContainerSize,
        iconSize: LemonadeIconSize.small,
      ),
      LemonadeSymbolContainerSize.medium => _SymbolContainerDimensions(
        containerSize: theme.mediumContainerSize,
        iconSize: LemonadeIconSize.medium,
      ),
      LemonadeSymbolContainerSize.large => _SymbolContainerDimensions(
        containerSize: theme.largeContainerSize,
        iconSize: LemonadeIconSize.large,
      ),
      LemonadeSymbolContainerSize.xLarge => _SymbolContainerDimensions(
        containerSize: theme.xLargeContainerSize,
        iconSize: LemonadeIconSize.xLarge,
      ),
    };
  }

  TextStyle _getTextStyle(
    LemonadeTypography typography,
    LemonadeSymbolContainerSize size,
  ) {
    return switch (size) {
      LemonadeSymbolContainerSize.xSmall => typography.bodyXSmallSemibold,
      LemonadeSymbolContainerSize.small => typography.bodySmallSemibold,
      LemonadeSymbolContainerSize.medium => typography.bodySmallSemibold,
      LemonadeSymbolContainerSize.large => typography.bodyLargeSemibold,
      LemonadeSymbolContainerSize.xLarge => typography.bodyXLargeSemibold,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final symbolContainerTheme = theme.components.symbolContainerTheme;
    final colors = theme.colors;
    final typography = theme.typography;

    final tintColor = _getTintColor(colors);
    final containerColor = _getContainerColor(colors);
    final dimensions = _getDimensions(symbolContainerTheme);

    Widget content;
    if (icon != null) {
      content = LemonadeIcon(
        icon: icon!,
        size: dimensions.iconSize,
        color: tintColor,
      );
    } else if (text != null) {
      content = Text(
        text!,
        style: _getTextStyle(typography, size).copyWith(color: tintColor),
      );
    } else {
      content = child!;
    }

    return Semantics(
      label: semanticLabel ?? text,
      identifier: semanticIdentifier,
      child: Container(
        width: dimensions.containerSize,
        height: dimensions.containerSize,
        decoration: BoxDecoration(
          color: containerColor,
          shape: BoxShape.circle,
        ),
        child: Center(child: content),
      ),
    );
  }
}

class _SymbolContainerDimensions {
  const _SymbolContainerDimensions({
    required this.containerSize,
    required this.iconSize,
  });

  final double containerSize;
  final LemonadeIconSize iconSize;
}
