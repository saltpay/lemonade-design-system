import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_design_system/src/raw_components/core_list_item.dart';

/// Selection behaviour mode for [LemonadeSelectionListItem].
///
/// * [single] – behaves like a radio button (fires when going from
///   unchecked → checked).
/// * [multiple] – behaves like a checkbox (fires on every tap).
/// * [toggle] – behaves like a switch/toggle (fires on every tap).
enum LemonadeSelectionListItemType {
  /// Single selection (radio button style).
  single,

  /// Multiple selection (checkbox style).
  multiple,

  /// Toggle selection (switch style).
  toggle,
}

/// {@template LemonadeSelectionListItem}
/// A selection list item from the Lemonade Design System.
///
/// A [LemonadeSelectionListItem] displays a row with a label, optional support
/// text, and a selection control (radio, checkbox, or switch) on the right.
///
/// ## Example
/// ```dart
/// LemonadeSelectionListItem(
///   label: 'Option 1',
///   type: LemonadeSelectionListItemType.single,
///   checked: isSelected,
///   onPressed: () {
///     setState(() {
///       isSelected = true;
///     });
///   },
/// )
/// ```
///
/// ## Selection Types
/// - [LemonadeSelectionListItemType.single]: Radio button style, only fires
///   callback when transitioning from unchecked to checked
/// - [LemonadeSelectionListItemType.multiple]: Checkbox style, fires callback
///   on every tap
/// - [LemonadeSelectionListItemType.toggle]: Switch style, fires callback
///   on every tap
///
/// See also:
/// - [LemonadeCheckbox], the standalone checkbox control
/// - [LemonadeSwitch], the standalone switch control
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeSelectionListItem extends StatefulWidget {
  /// {@macro LemonadeSelectionListItem}
  const LemonadeSelectionListItem({
    required this.label,
    required this.type,
    required this.checked,
    required this.onPressed,
    super.key,
    this.supportText,
    this.enabled = true,
    this.leadingSlot,
    this.trailingSlot,
    this.semanticIdentifier,
    this.semanticLabel,
  });

  /// {@template LemonadeSelectionListItem.label}
  /// Main label text displayed on the row.
  /// {@endtemplate}
  final String label;

  /// {@template LemonadeSelectionListItem.supportText}
  /// Optional support text shown below [label].
  /// {@endtemplate}
  final String? supportText;

  /// {@template LemonadeSelectionListItem.type}
  /// Selection behaviour and visual type.
  /// {@endtemplate}
  final LemonadeSelectionListItemType type;

  /// {@template LemonadeSelectionListItem.checked}
  /// Whether this item is currently selected.
  /// {@endtemplate}
  final bool checked;

  /// {@template LemonadeSelectionListItem.enabled}
  /// Whether this item is interactive.
  ///
  /// When false, the item is displayed with reduced opacity and does not
  /// respond to taps. Defaults to true.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeSelectionListItem.onPressed}
  /// Called when the row is activated.
  ///
  /// For [LemonadeSelectionListItemType.single], this is only fired when
  /// transitioning from unchecked → checked.
  /// For [LemonadeSelectionListItemType.multiple] and
  /// [LemonadeSelectionListItemType.toggle], it is fired on every tap.
  /// {@endtemplate}
  final VoidCallback onPressed;

  /// {@template LemonadeSelectionListItem.leadingSlot}
  /// Optional widget placed at the start of the row (e.g. an icon).
  /// {@endtemplate}
  final WidgetBuilder? leadingSlot;

  /// {@template LemonadeSelectionListItem.trailingSlot}
  /// Optional widget placed before the selection control (e.g. a tag).
  /// {@endtemplate}
  final WidgetBuilder? trailingSlot;

  /// {@template LemonadeSelectionListItem.semanticIdentifier}
  /// An identifier for the item used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeSelectionListItem.semanticLabel}
  /// A label for the item used for accessibility purposes.
  /// {@endtemplate}
  final String? semanticLabel;

  @override
  State<LemonadeSelectionListItem> createState() =>
      _LemonadeSelectionListItemState();
}

class _LemonadeSelectionListItemState extends State<LemonadeSelectionListItem> {
  void _handleTap() {
    if (!widget.enabled) return;

    switch (widget.type) {
      case LemonadeSelectionListItemType.single:
        if (!widget.checked) {
          widget.onPressed();
        }
      case LemonadeSelectionListItemType.multiple:
      case LemonadeSelectionListItemType.toggle:
        widget.onPressed();
    }
  }

  @override
  Widget build(BuildContext context) {
    WidgetBuilder? combinedTrailingSlot;

    if (widget.trailingSlot != null) {
      combinedTrailingSlot = (context) {
        final spaces = LemonadeTheme.of(context).spaces;
        return Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            widget.trailingSlot!(context),
            SizedBox(width: spaces.spacing200),
            _SelectionControl(
              type: widget.type,
              checked: widget.checked,
              enabled: widget.enabled,
              onPressed: _handleTap,
            ),
          ],
        );
      };
    } else {
      combinedTrailingSlot = (context) => _SelectionControl(
        type: widget.type,
        checked: widget.checked,
        enabled: widget.enabled,
        onPressed: _handleTap,
      );
    }

    return Semantics(
      identifier: widget.semanticIdentifier,
      label: widget.semanticLabel,
      checked: widget.checked,
      enabled: widget.enabled,
      child: LemonadeCoreListItem(
        label: widget.label,
        description: widget.supportText,
        enabled: widget.enabled,
        leadingSlot: widget.leadingSlot,
        trailingSlot: combinedTrailingSlot,
        onPressed: _handleTap,
      ),
    );
  }
}

class _SelectionControl extends StatelessWidget {
  const _SelectionControl({
    required this.type,
    required this.checked,
    required this.enabled,
    required this.onPressed,
  });

  final LemonadeSelectionListItemType type;
  final bool checked;
  final bool enabled;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    switch (type) {
      case LemonadeSelectionListItemType.single:
        return LemonadeRadioButton(
          checked: checked,
          enabled: enabled,
          onChanged: onPressed,
        );
      case LemonadeSelectionListItemType.multiple:
        return LemonadeCheckbox(
          status: checked ? CheckboxStatus.checked : CheckboxStatus.unchecked,
          enabled: enabled,
          onChanged: onPressed,
        );
      case LemonadeSelectionListItemType.toggle:
        return LemonadeSwitch(
          checked: checked,
          enabled: enabled,
          onCheckedChange: (_) => onPressed(),
        );
    }
  }
}
