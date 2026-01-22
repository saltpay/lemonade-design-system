import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Background variants available for [LemonadeCard].
enum LemonadeCardBackground {
  /// Default background color
  defaultBg,

  /// Subtle background color
  subtle,
}

/// Content padding variants available for [LemonadeCard].
enum LemonadeCardPadding {
  /// No padding
  none,

  /// Extra small padding
  xSmall,

  /// Small padding
  small,

  /// Medium padding
  medium,
}

/// {@template LemonadeCardHeader}
/// Configuration for [LemonadeCard] header.
/// {@endtemplate}
class LemonadeCardHeader {
  /// {@macro LemonadeCardHeader}
  const LemonadeCardHeader({
    required this.title,
    this.trailing,
  });

  /// The title displayed in the header.
  final String title;

  /// Optional trailing widget displayed after the title.
  final Widget? trailing;
}

/// {@template LemonadeCard}
/// A card widget from the Lemonade Design System.
///
/// A [LemonadeCard] is a container component used to group related content
/// with optional header and consistent styling.
///
/// ## Example
/// ```dart
/// LemonadeCard(
///   header: LemonadeCardHeader(title: 'Card Title'),
///   padding: LemonadeCardPadding.medium,
///   child: Text('Card content'),
/// )
/// ```
///
/// See also:
/// - [LemonadeCardTheme], for theme configuration
/// {@endtemplate}
class LemonadeCard extends StatelessWidget {
  /// {@macro LemonadeCard}
  const LemonadeCard({
    required this.child,
    this.header,
    this.padding = LemonadeCardPadding.none,
    this.background = LemonadeCardBackground.defaultBg,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// The content of the card.
  final Widget child;

  /// Optional header configuration.
  final LemonadeCardHeader? header;

  /// The padding around the content. Defaults to [LemonadeCardPadding.none].
  final LemonadeCardPadding padding;

  /// The background variant. Defaults to [LemonadeCardBackground.defaultBg].
  final LemonadeCardBackground background;

  /// An identifier for the card for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the card for accessibility purposes.
  final String? semanticLabel;

  Color _getBackgroundColor(LemonadeSemanticColors colors) {
    return switch (background) {
      LemonadeCardBackground.defaultBg => colors.background.bgDefault,
      LemonadeCardBackground.subtle => colors.background.bgSubtle,
    };
  }

  double _getPadding(LemonadeSpaces spaces) {
    return switch (padding) {
      LemonadeCardPadding.none => spaces.spacing0,
      LemonadeCardPadding.xSmall => spaces.spacing100,
      LemonadeCardPadding.small => spaces.spacing200,
      LemonadeCardPadding.medium => spaces.spacing400,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final colors = theme.colors;
    final spaces = theme.spaces;
    final radius = theme.radius;
    final typography = theme.typography;

    final backgroundColor = _getBackgroundColor(colors);
    final contentPadding = _getPadding(spaces);

    return Semantics(
      label: semanticLabel ?? header?.title,
      identifier: semanticIdentifier,
      container: true,
      child: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          color: backgroundColor,
          borderRadius: BorderRadius.circular(radius.radius400),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            if (header != null) ...[
              Padding(
                padding: EdgeInsets.only(
                  left: spaces.spacing400,
                  top: spaces.spacing400,
                  right: spaces.spacing400,
                  bottom: spaces.spacing0,
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Text(
                        header!.title,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: typography.heading2XSmall.copyWith(
                          color: colors.content.contentPrimary,
                        ),
                      ),
                    ),
                    if (header!.trailing != null) ...[
                      SizedBox(width: spaces.spacing200),
                      header!.trailing!,
                    ],
                  ],
                ),
              ),
            ],
            Padding(
              padding: EdgeInsets.all(contentPadding),
              child: child,
            ),
          ],
        ),
      ),
    );
  }
}
