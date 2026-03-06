import 'package:flutter/material.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template _LemonadeCoreListItem}
/// A core list item from the Lemonade Design System.
///
/// A [LemonadeCoreListItem] displays a row with a label, optional description,
/// and optional leading and trailing slots.
///
/// This is an internal component used to build other list item components.
///
/// ## Example
/// ```dart
/// _LemonadeCoreListItem(
///   label: 'Item Label',
///   description: 'Optional description text',
///   leadingSlot: (context) => Icon(Icons.star),
///   trailingSlot: (context) => Icon(Icons.chevron_right),
/// )
/// ```
///
/// See also:
/// - [LemonadeActionListItem], a public component built with this
/// - [LemonadeSelectionListItem], which adds selection controls
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeCoreListItem extends StatefulWidget {
  /// {@macro LemonadeCoreListItem}
  const LemonadeCoreListItem({
    required this.label,
    required this.onPressed,
    this.description,
    this.enabled = true,
    this.leadingSlot,
    this.trailingSlot,
    this.showDivider,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// {@template LemonadeCoreListItem.label}
  /// Main label text displayed on the row.
  /// {@endtemplate}
  final String label;

  /// {@template LemonadeCoreListItem.description}
  /// Optional description text shown below [label].
  /// {@endtemplate}
  final String? description;

  /// {@template LemonadeCoreListItem.enabled}
  /// Whether this item is interactive.
  ///
  /// When false, the item is displayed with reduced opacity. Defaults to true.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeCoreListItem.leadingSlot}
  /// Optional widget placed at the start of the row (e.g. an icon).
  /// {@endtemplate}
  final WidgetBuilder? leadingSlot;

  /// {@template LemonadeCoreListItem.trailingSlot}
  /// Optional widget placed at the end of the row (e.g. a tag or chevron).
  /// {@endtemplate}
  final WidgetBuilder? trailingSlot;

  /// {@template LemonadeCoreListItem.showDivider}
  /// Whether to show a divider line below the item.
  /// {@endtemplate}
  final bool? showDivider;

  /// {@template LemonadeCoreListItem.onPressed}
  /// Called when the item is tapped.
  ///
  /// If null, the item will be non-interactive. Only invoked if [enabled] is
  /// true.
  /// {@endtemplate}
  final VoidCallback? onPressed;

  /// {@template LemonadeCoreListItem.semanticIdentifier}
  /// An identifier for the item used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeCoreListItem.semanticLabel}
  /// A label for the item used for accessibility purposes.
  /// {@endtemplate}
  final String? semanticLabel;

  @override
  State<LemonadeCoreListItem> createState() => _LemonadeCoreListItemState();
}

class _LemonadeCoreListItemState extends State<LemonadeCoreListItem> {
  bool _isPressed = false;

  void _handlePressChange({required bool isPressed}) {
    if (!widget.enabled) return;
    setState(() => _isPressed = isPressed);
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final spaces = theme.spaces;

    final pressedBackground = theme.colors.interaction.bgSubtleInteractive;
    final background = _isPressed ? pressedBackground : const Color(0x00000000);

    final disabledOpacity = theme.opacity.state.opacityDisabled;

    return Semantics(
      identifier: widget.semanticIdentifier,
      label: widget.semanticLabel,
      enabled: widget.enabled,
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: widget.enabled ? widget.onPressed : null,
        onTapDown: (_) => _handlePressChange(isPressed: true),
        onTapUp: (_) => _handlePressChange(isPressed: false),
        onTapCancel: () => _handlePressChange(isPressed: false),
        child: Column(
          children: <Widget>[
            Padding(
              padding: EdgeInsets.all(spaces.spacing100),
              child: AnimatedContainer(
                duration: const Duration(milliseconds: 120),
                curve: Curves.easeOut,
                decoration: BoxDecoration(
                  color: background,
                  borderRadius: BorderRadius.circular(spaces.spacing300),
                ),
                constraints: BoxConstraints(
                  minHeight: theme.sizes.size1200,
                ),
                padding: EdgeInsets.only(
                  left: spaces.spacing300,
                  top: spaces.spacing300,
                  right: spaces.spacing200,
                  bottom: spaces.spacing300,
                ),
                child: Opacity(
                  opacity: widget.enabled ? 1.0 : disabledOpacity,
                  child: Row(
                    children: <Widget>[
                      if (widget.leadingSlot != null) ...<Widget>[
                        widget.leadingSlot!(context),
                        SizedBox(width: spaces.spacing300),
                      ],

                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            Text(
                              widget.label,
                              style: theme.typography.bodyMediumMedium.apply(
                                color: theme.colors.content.contentPrimary,
                              ),
                            ),

                            if (widget.description != null) ...<Widget>[
                              Text(
                                widget.description!,
                                style: theme.typography.bodySmallRegular.apply(
                                  color: theme.colors.content.contentSecondary,
                                ),
                              ),
                            ],
                          ],
                        ),
                      ),

                      if (widget.trailingSlot != null) ...<Widget>[
                        SizedBox(width: spaces.spacing200),
                        widget.trailingSlot!(context),
                      ],
                    ],
                  ),
                ),
              ),
            ),
            if (widget.showDivider ?? false) ...<Widget>[
              Padding(
                padding: EdgeInsetsGeometry.symmetric(
                  horizontal: spaces.spacing400,
                ),
                // TODO(felipeemarcon): Replace with LemonadeDivider when available
                child: Divider(
                  height: theme.border.base.border25,
                  thickness: theme.border.base.border25,
                  color: theme.colors.border.borderNeutralLow,
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
