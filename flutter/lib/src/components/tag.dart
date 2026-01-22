import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Voice variants available for [LemonadeTag].
enum LemonadeTagVoice {
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
}

/// {@template LemonadeTag}
/// A tag widget from the Lemonade Design System.
///
/// A [LemonadeTag] is a compact label used to categorize, organize,
/// or annotate content. Typically static and non-interactive.
///
/// ## Example
/// ```dart
/// LemonadeTag(
///   label: 'NEW',
///   voice: LemonadeTagVoice.positive,
/// )
/// ```
///
/// With icon:
/// ```dart
/// LemonadeTag(
///   label: 'WARNING',
///   icon: LemonadeIcons.warning,
///   voice: LemonadeTagVoice.warning,
/// )
/// ```
///
/// See also:
/// - [LemonadeTagTheme], for theme configuration
/// {@endtemplate}
class LemonadeTag extends StatelessWidget {
  /// {@macro LemonadeTag}
  const LemonadeTag({
    required this.label,
    this.icon,
    this.voice = LemonadeTagVoice.neutral,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// The label text displayed on the tag.
  final String label;

  /// Optional icon displayed before the label.
  final LemonadeIcons? icon;

  /// The voice/tone of the tag. Defaults to [LemonadeTagVoice.neutral].
  final LemonadeTagVoice voice;

  /// An identifier for the tag for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the tag for accessibility purposes.
  final String? semanticLabel;

  Color _getTintColor(LemonadeSemanticColors colors) {
    return switch (voice) {
      LemonadeTagVoice.neutral => colors.content.contentPrimary,
      LemonadeTagVoice.critical => colors.content.contentCritical,
      LemonadeTagVoice.warning => colors.content.contentCaution,
      LemonadeTagVoice.info => colors.content.contentInfo,
      LemonadeTagVoice.positive => colors.content.contentPositive,
    };
  }

  Color _getContainerColor(LemonadeSemanticColors colors) {
    return switch (voice) {
      LemonadeTagVoice.neutral => colors.background.bgNeutralSubtle,
      LemonadeTagVoice.critical => colors.background.bgCriticalSubtle,
      LemonadeTagVoice.warning => colors.background.bgCautionSubtle,
      LemonadeTagVoice.info => colors.background.bgInfoSubtle,
      LemonadeTagVoice.positive => colors.background.bgPositiveSubtle,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final colors = theme.colors;
    final typography = theme.typography;
    final spaces = theme.spaces;
    final radius = theme.radius;

    final tintColor = _getTintColor(colors);
    final containerColor = _getContainerColor(colors);

    return Semantics(
      label: semanticLabel ?? label,
      identifier: semanticIdentifier,
      child: Container(
        decoration: BoxDecoration(
          color: containerColor,
          borderRadius: BorderRadius.circular(radius.radius100),
        ),
        padding: EdgeInsets.symmetric(
          horizontal: spaces.spacing100,
          vertical: spaces.spacing50,
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (icon != null) ...[
              LemonadeIcon(
                icon: icon!,
                size: LemonadeIconSize.small,
                color: tintColor,
              ),
              SizedBox(width: spaces.spacing50),
            ],
            Flexible(
              child: Padding(
                padding: EdgeInsets.symmetric(horizontal: spaces.spacing50),
                child: Text(
                  label,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: typography.bodyXSmallSemibold.copyWith(
                    color: tintColor,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
