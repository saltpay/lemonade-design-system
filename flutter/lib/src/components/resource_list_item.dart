import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_design_system/src/raw_components/core_list_item.dart';

/// {@template LemonadeActionListItem}
/// An action list item from the Lemonade Design System.
///
/// A [LemonadeActionListItem] is a specialized [LemonadeCoreListItem] that
/// supports navigation indicators and additional trailing content.
///
/// ## Example
/// ```dart
/// LemonadeResourceListItem(
///   label: 'Credit ···· 9074',
///   description: '18:25 • Camden Corner',
///   leadingSlot: (context) => LemonadeSymbolContainer.custom(),
///   addonSlot: (context) => LemonadeTag()
///   onPressed: () => {}
/// )
/// ```
///
/// See also:
/// - [LemonadeCoreListItem], the base list item component
/// - [LemonadeActionListItem], which perform actions, such as navigating menus
/// - [LemonadeSelectionListItem], which adds selection controls
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeResourceListItem extends StatefulWidget {
  /// {@macro LemonadeResourceListItem}
  const LemonadeResourceListItem({
    required this.label,
    required this.onPressed,
    required this.value,
    super.key,
    this.description,
    this.enabled = true,
    this.leadingSlot,
    this.addonSlot,
    this.showDivider,
    this.semanticIdentifier,
    this.semanticLabel,
  });

  /// {@template LemonadeResourceListItem.label}
  /// Main label text displayed on the row.
  /// {@endtemplate}
  final String label;

  /// {@template LemonadeResourceListItem.description}
  /// Optional description text shown below [label].
  /// {@endtemplate}
  final String? description;

  /// {@template LemonadeResourceListItem.enabled}
  /// Whether this item is interactive.
  ///
  /// When false, the item is displayed with reduced opacity. Defaults to true.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeResourceListItem.leadingSlot}
  /// Optional widget placed at the start of the row (e.g. an icon).
  /// {@endtemplate}
  final WidgetBuilder? leadingSlot;

  /// {@template LemonadeResourceListItem.trailingSlot}
  /// Optional widget placed before the navigation chevron (e.g. a badge).
  ///
  /// When [value] is not null, this content is displayed below the
  /// value.
  /// {@endtemplate}
  final WidgetBuilder? addonSlot;

  /// {@template LemonadeResourceListItem.withNavigation}
  /// Whether to show a chevron icon indicating navigation.
  ///
  /// When true, a right-pointing chevron is displayed in the trailing slot.
  /// Defaults to false.
  /// {@endtemplate}
  final String value;

  /// {@template LemonadeCoreListItem.showDivider}
  /// Whether to show a divider line below the item.
  /// {@endtemplate}
  final bool? showDivider;

  /// {@template LemonadeResourceListItem.onPressed}
  /// Called when the item is tapped.
  ///
  /// If null, the item will be non-interactive. Only invoked if [enabled] is
  /// true.
  /// {@endtemplate}
  final VoidCallback? onPressed;

  /// {@template LemonadeResourceListItem.semanticIdentifier}
  /// An identifier for the item used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeResourceListItem.semanticLabel}
  /// A label for the item used for accessibility purposes.
  /// {@endtemplate}
  final String? semanticLabel;

  @override
  State<LemonadeResourceListItem> createState() =>
      _LemonadeResourceListItemState();
}

class _LemonadeResourceListItemState extends State<LemonadeResourceListItem> {
  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    WidgetBuilder? finalTrailingSlot;

    finalTrailingSlot = (context) {
      final children = <Widget>[
        Text(
          widget.value,
          style: theme.typography.bodyMediumMedium.apply(
            color: theme.colors.content.contentPrimary,
          ),
          textAlign: TextAlign.right,
        ),
      ];

      if (widget.addonSlot != null) {
        children
          ..add(widget.addonSlot!(context))
          ..add(SizedBox(width: theme.spaces.spacing100));
      }

      return Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: children,
      );
    };

    return LemonadeCoreListItem(
      label: widget.label,
      description: widget.description,
      enabled: widget.enabled,
      leadingSlot: widget.leadingSlot,
      trailingSlot: finalTrailingSlot,
      showDivider: widget.showDivider,
      onPressed: widget.onPressed,
      semanticIdentifier: widget.semanticIdentifier,
      semanticLabel: widget.semanticLabel,
    );
  }
}
