import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeChip}
/// A chip widget from the Lemonade Design System.
///
/// A [LemonadeChip] is a compact element used to display information,
/// trigger actions, or represent selections. Commonly used for tags,
/// filters, or interactive choices in dense interfaces.
///
/// ## Example
/// ```dart
/// LemonadeChip(
///   label: 'Filter',
///   selected: true,
///   onTap: () {
///     print('Chip tapped!');
///   },
/// )
/// ```
///
/// With icons and counter:
/// ```dart
/// LemonadeChip(
///   label: 'Category',
///   selected: false,
///   leadingIcon: LemonadeIcons.tag,
///   trailingIcon: LemonadeIcons.close,
///   counter: 5,
///   onTap: () {},
///   onTrailingIconTap: () {},
/// )
/// ```
///
/// See also:
/// - [LemonadeChipTheme], for theme configuration
/// {@endtemplate}
class LemonadeChip extends StatefulWidget {
  /// {@macro LemonadeChip}
  const LemonadeChip({
    required this.label,
    this.selected = false,
    this.leadingIcon,
    this.trailingIcon,
    this.counter,
    this.enabled = true,
    this.onTap,
    this.onTrailingIconTap,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// The label text displayed on the chip.
  final String label;

  /// Whether the chip is in a selected state.
  final bool selected;

  /// Optional icon displayed before the label.
  final LemonadeIcons? leadingIcon;

  /// Optional icon displayed after the label.
  final LemonadeIcons? trailingIcon;

  /// Optional counter displayed on the chip.
  final int? counter;

  /// Whether the chip is enabled. Defaults to true.
  final bool enabled;

  /// Callback invoked when the chip is tapped.
  final VoidCallback? onTap;

  /// Callback invoked when the trailing icon is tapped.
  final VoidCallback? onTrailingIconTap;

  /// An identifier for the chip for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the chip for accessibility purposes.
  final String? semanticLabel;

  @override
  State<LemonadeChip> createState() => _LemonadeChipState();
}

class _LemonadeChipState extends State<LemonadeChip> {
  bool _isPressed = false;

  void _handleTapDown(TapDownDetails details) {
    if (widget.enabled && widget.onTap != null) {
      setState(() => _isPressed = true);
    }
  }

  void _handleTapUp(TapUpDetails details) {
    setState(() => _isPressed = false);
  }

  void _handleTapCancel() {
    setState(() => _isPressed = false);
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final chipTheme = theme.components.chipTheme;
    final colors = theme.colors;
    final spaces = theme.spaces;
    final sizes = theme.sizes;

    final opacity = widget.enabled
        ? theme.opacity.base.opacity100
        : theme.opacity.state.opacityDisabled;

    final chipColors = _getChipColors(colors);

    final backgroundColor = _isPressed
        ? chipColors.pressedBackgroundColor
        : chipColors.backgroundColor;

    return Semantics(
      label: widget.semanticLabel ?? widget.label,
      identifier: widget.semanticIdentifier,
      button: widget.onTap != null,
      child: GestureDetector(
        onTapDown: _handleTapDown,
        onTapUp: _handleTapUp,
        onTapCancel: _handleTapCancel,
        onTap: widget.enabled ? widget.onTap : null,
        child: Opacity(
          opacity: opacity,
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 150),
            constraints: BoxConstraints(
              minWidth: chipTheme.minWidth,
              minHeight: chipTheme.minHeight,
            ),
            decoration: BoxDecoration(
              color: backgroundColor,
              borderRadius: BorderRadius.circular(theme.radius.radiusFull),
              border: Border.all(
                color: chipColors.borderColor,
              ),
            ),
            padding: EdgeInsets.symmetric(
              horizontal: spaces.spacing200,
              vertical: spaces.spacing100,
            ),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                if (widget.leadingIcon != null) ...[
                  SizedBox(
                    width: chipTheme.iconSize,
                    height: chipTheme.iconSize,
                    child: Center(
                      child: LemonadeIcon(
                        icon: widget.leadingIcon!,
                        size: LemonadeIconSize.small,
                        color: chipColors.contentColor,
                      ),
                    ),
                  ),
                ],
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: spaces.spacing100),
                  child: Text(
                    widget.label,
                    style: theme.typography.bodySmallMedium.apply(
                      color: chipColors.contentColor,
                    ),
                  ),
                ),
                if (widget.counter != null) ...[
                  Container(
                    constraints: BoxConstraints(
                      minWidth: sizes.size450,
                      minHeight: sizes.size400,
                    ),
                    margin: EdgeInsets.symmetric(
                      horizontal: spaces.spacing100,
                    ),
                    padding: EdgeInsets.symmetric(
                      horizontal: spaces.spacing100,
                    ),
                    decoration: BoxDecoration(
                      color: colors.background.bgBrand,
                      borderRadius: BorderRadius.circular(
                        theme.radius.radiusFull,
                      ),
                    ),
                    child: Center(
                      child: Text(
                        widget.counter.toString(),
                        maxLines: 1,
                        textAlign: TextAlign.center,
                        style: theme.typography.bodyXSmallSemibold.apply(
                          color: colors.content.contentOnBrandHigh,
                        ),
                      ),
                    ),
                  ),
                ],
                if (widget.trailingIcon != null) ...[
                  Padding(
                    padding: EdgeInsets.only(left: spaces.spacing50),
                    child: GestureDetector(
                      onTap: widget.enabled ? widget.onTrailingIconTap : null,
                      child: SizedBox(
                        width: chipTheme.iconSize,
                        height: chipTheme.iconSize,
                        child: Center(
                          child: LemonadeIcon(
                            icon: widget.trailingIcon!,
                            size: LemonadeIconSize.small,
                            color: chipColors.contentColor,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ],
            ),
          ),
        ),
      ),
    );
  }

  _ChipColors _getChipColors(LemonadeSemanticColors colors) {
    if (widget.selected) {
      return _ChipColors(
        backgroundColor: colors.background.bgBrandHigh,
        pressedBackgroundColor: colors.interaction.bgBrandHighInteractive,
        contentColor: colors.content.contentBrandInverse,
        borderColor: colors.border.borderNeutralMedium,
      );
    } else {
      return _ChipColors(
        backgroundColor: colors.background.bgDefault,
        pressedBackgroundColor: colors.interaction.bgSubtleInteractive,
        contentColor: colors.content.contentPrimary,
        borderColor: colors.border.borderNeutralMedium,
      );
    }
  }
}

class _ChipColors {
  const _ChipColors({
    required this.backgroundColor,
    required this.pressedBackgroundColor,
    required this.contentColor,
    required this.borderColor,
  });

  final Color backgroundColor;
  final Color pressedBackgroundColor;
  final Color contentColor;
  final Color borderColor;
}
