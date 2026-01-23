import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Orientation variants available for [LemonadeContentItem].
enum LemonadeContentItemOrientation {
  /// Horizontal orientation with label on left and value on right
  horizontal,

  /// Vertical orientation with label above value
  vertical,
}

/// Size variants available for [LemonadeContentItem].
enum LemonadeContentItemSize {
  /// Small size variant
  small,

  /// Large size variant
  large,
}

/// {@template LemonadeContentItem}
/// A content item widget from the Lemonade Design System.
///
/// A [LemonadeContentItem] displays a label-value pair with optional leading
/// and trailing slots. It supports the following valid combinations:
/// - horizontal orientation with small size
/// - vertical orientation with small or large size.
///
/// ## Example
/// ```dart
/// Horizontal small content item
/// LemonadeContentItem(
///   label: 'Email',
///   value: 'user@example.com',
/// )
///
/// Vertical large with leading icon
/// LemonadeContentItem(
///   label: 'Balance',
///   value: '€1,234.56',
///   orientation: LemonadeContentItemOrientation.vertical,
///   size: LemonadeContentItemSize.large,
///   leadingSlot: LemonadeSymbolContainer(
///     icon: LemonadeIcons.wallet,
///     size: LemonadeSymbolContainerSize.medium,
///   ),
///   onPressed: () => {}
/// )
/// ```
/// {@endtemplate}
class LemonadeContentItem extends StatelessWidget {
  /// {@macro LemonadeContentItem}
  const LemonadeContentItem({
    required this.label,
    required this.value,
    this.orientation = LemonadeContentItemOrientation.horizontal,
    this.size = LemonadeContentItemSize.small,
    this.leadingSlot,
    this.trailingSlot,
    this.addonSlot,
    this.valueStyle,
    this.semanticIdentifier,
    this.onPressed,
    super.key,
  }) : assert(
         !(orientation == LemonadeContentItemOrientation.horizontal &&
             size == LemonadeContentItemSize.large),
         'Horizontal orientation does not support large size variant.',
       );

  /// The label text displayed.
  final String label;

  /// The value text displayed.
  final String value;

  /// The orientation direction of the content item.
  final LemonadeContentItemOrientation orientation;

  /// The size variant of the content item.
  final LemonadeContentItemSize size;

  /// Optional widget displayed before the label.
  final Widget? leadingSlot;

  /// Optional widget displayed after the value.
  final Widget? trailingSlot;

  /// Optional widget displayed below the value.
  final Widget? addonSlot;

  /// Optional custom text style for the value.
  /// This style will be merged with the default style, allowing you to
  /// override specific properties like color, fontWeight, fontSize, etc.
  final TextStyle? valueStyle;

  /// A semantic identifier for accessibility.
  final String? semanticIdentifier;

  /// Callback invoked when the component is pressed.
  final VoidCallback? onPressed;

  StatelessWidget _getContentItemVariant(
    LemonadeContentItemSize size,
    LemonadeContentItemOrientation orientation,
  ) {
    if (orientation == LemonadeContentItemOrientation.vertical &&
        size == LemonadeContentItemSize.small) {
      return _VerticalContentItem(
        label: label,
        value: value,
        leadingSlot: leadingSlot,
        trailingSlot: trailingSlot,
        addonSlot: addonSlot,
        valueStyle: valueStyle,
      );
    }

    if (orientation == LemonadeContentItemOrientation.vertical &&
        size == LemonadeContentItemSize.large) {
      return _VerticalContentItem(
        label: label,
        value: value,
        size: LemonadeContentItemSize.large,
        leadingSlot: leadingSlot,
        trailingSlot: trailingSlot,
        addonSlot: addonSlot,
        valueStyle: valueStyle,
      );
    }

    return _HorizontalContentItem(
      label: label,
      value: value,
      leadingSlot: leadingSlot,
      trailingSlot: trailingSlot,
      addonSlot: addonSlot,
      valueStyle: valueStyle,
    );
  }

  @override
  Widget build(BuildContext context) {
    final isInteractve = onPressed != null;

    return Semantics(
      identifier: semanticIdentifier,
      child: isInteractve
          ? GestureDetector(
              onTap: onPressed,
              child: _getContentItemVariant(size, orientation),
            )
          : _getContentItemVariant(size, orientation),
    );
  }
}

class _HorizontalContentItem extends StatelessWidget {
  const _HorizontalContentItem({
    required this.label,
    required this.value,
    this.leadingSlot,
    this.trailingSlot,
    this.addonSlot,
    this.valueStyle,
  });

  final String label;
  final String value;
  final Widget? leadingSlot;
  final Widget? trailingSlot;
  final Widget? addonSlot;
  final TextStyle? valueStyle;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (leadingSlot != null) ...[
          ConstrainedBox(
            constraints: BoxConstraints(
              minHeight: theme.sizes.size600,
            ),
            child: leadingSlot,
          ),

          SizedBox(width: theme.spaces.spacing300),
        ],
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                label,
                style: theme.typography.bodyMediumRegular.copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
              ),
              if (addonSlot != null) addonSlot!,
            ],
          ),
        ),
        SizedBox(width: theme.spaces.spacing300),
        Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              value,
              textAlign: TextAlign.right,
              style: theme.typography.bodyMediumMedium
                  .copyWith(
                    color: theme.colors.content.contentPrimary,
                  )
                  .merge(valueStyle),
            ),
            if (trailingSlot != null) ...[
              SizedBox(width: theme.spaces.spacing300),
              trailingSlot!,
            ],
          ],
        ),
      ],
    );
  }
}

class _VerticalContentItem extends StatelessWidget {
  const _VerticalContentItem({
    required this.label,
    required this.value,
    this.size = LemonadeContentItemSize.small,
    this.leadingSlot,
    this.trailingSlot,
    this.addonSlot,
    this.valueStyle,
  });

  final String label;
  final String value;
  final LemonadeContentItemSize size;
  final Widget? leadingSlot;
  final Widget? trailingSlot;
  final Widget? addonSlot;
  final TextStyle? valueStyle;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final textStyle = size == LemonadeContentItemSize.large
        ? theme.typography.bodyXLargeSemibold
        : theme.typography.bodyMediumSemibold;

    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (leadingSlot != null) ...[
          leadingSlot!,
          SizedBox(width: theme.spaces.spacing300),
        ],
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                label,
                style: theme.typography.bodyMediumRegular.copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
              ),
              Row(
                children: [
                  Expanded(
                    child: Text(
                      value,
                      style: textStyle
                          .copyWith(
                            color: theme.colors.content.contentPrimary,
                          )
                          .merge(valueStyle),
                    ),
                  ),
                  if (trailingSlot != null) ...[
                    SizedBox(width: theme.spaces.spacing100),
                    trailingSlot!,
                  ],
                ],
              ),
              if (addonSlot != null) addonSlot!,
            ],
          ),
        ),
      ],
    );
  }
}
