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
/// LemonadeActionListItem(
///   label: 'Account Settings',
///   description: 'Manage your account',
///   withNavigation: true,
///   leadingSlot: (context) => LemonadeIcons.heart,
/// )
/// ```
///
/// See also:
/// - [LemonadeCoreListItem], the base list item component
/// - [LemonadeSelectionListItem], which adds selection controls
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeActionListItem extends StatefulWidget {
  /// {@macro LemonadeActionListItem}
  const LemonadeActionListItem({
    required this.label,
    required this.onPressed,
    super.key,
    this.description,
    this.enabled = true,
    this.leadingSlot,
    this.trailingSlot,
    this.withNavigation = false,
    this.semanticIdentifier,
    this.semanticLabel,
  });

  /// {@template LemonadeActionListItem.label}
  /// Main label text displayed on the row.
  /// {@endtemplate}
  final String label;

  /// {@template LemonadeActionListItem.description}
  /// Optional description text shown below [label].
  /// {@endtemplate}
  final String? description;

  /// {@template LemonadeActionListItem.enabled}
  /// Whether this item is interactive.
  ///
  /// When false, the item is displayed with reduced opacity. Defaults to true.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeActionListItem.leadingSlot}
  /// Optional widget placed at the start of the row (e.g. an icon).
  /// {@endtemplate}
  final WidgetBuilder? leadingSlot;

  /// {@template LemonadeActionListItem.trailingSlot}
  /// Optional widget placed before the navigation chevron (e.g. a badge).
  ///
  /// When [withNavigation] is true, this content is displayed before the
  /// chevron icon.
  /// {@endtemplate}
  final WidgetBuilder? trailingSlot;

  /// {@template LemonadeActionListItem.withNavigation}
  /// Whether to show a chevron icon indicating navigation.
  ///
  /// When true, a right-pointing chevron is displayed in the trailing slot.
  /// Defaults to false.
  /// {@endtemplate}
  final bool withNavigation;

  /// {@template LemonadeActionListItem.onPressed}
  /// Called when the item is tapped.
  ///
  /// If null, the item will be non-interactive. Only invoked if [enabled] is
  /// true.
  /// {@endtemplate}
  final VoidCallback? onPressed;

  /// {@template LemonadeActionListItem.semanticIdentifier}
  /// An identifier for the item used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeActionListItem.semanticLabel}
  /// A label for the item used for accessibility purposes.
  /// {@endtemplate}
  final String? semanticLabel;

  @override
  State<LemonadeActionListItem> createState() => _LemonadeActionListItemState();
}

class _LemonadeActionListItemState extends State<LemonadeActionListItem> {
  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    WidgetBuilder? finalTrailingSlot;

    if (widget.withNavigation) {
      finalTrailingSlot = (context) {
        final children = <Widget>[];

        if (widget.trailingSlot != null) {
          children
            ..add(widget.trailingSlot!(context))
            ..add(SizedBox(width: theme.spaces.spacing100));
        }

        children.add(
          LemonadeIcon(
            icon: LemonadeIcons.chevronRight,
            color: theme.colors.content.contentTertiary,
          ),
        );

        return Row(
          mainAxisSize: MainAxisSize.min,
          children: children,
        );
      };
    } else {
      finalTrailingSlot = widget.trailingSlot;
    }

    return LemonadeCoreListItem(
      label: widget.label,
      description: widget.description,
      enabled: widget.enabled,
      leadingSlot: widget.leadingSlot,
      trailingSlot: finalTrailingSlot,
      onPressed: widget.onPressed,
      semanticIdentifier: widget.semanticIdentifier,
      semanticLabel: widget.semanticLabel,
    );
  }
}
