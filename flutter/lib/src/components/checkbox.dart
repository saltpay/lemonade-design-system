import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Represents the state of a checkbox.
enum CheckboxStatus {
  /// The checkbox is checked.
  checked,

  /// The checkbox is unchecked.
  unchecked,

  /// The checkbox is in an indeterminate state.
  indeterminate,
}

/// {@template LemonadeCheckbox}
/// A form control that lets users select one or more options from a set.
///
/// A [LemonadeCheckbox] displays a square checkbox with an optional label
/// and support text. Supports checked, unchecked, and indeterminate states
/// for flexible selection logic.
///
/// ## Example
/// ```dart
/// LemonadeCheckbox(
///   status: CheckboxStatus.checked,
///   onChanged: () {
///     setState(() {
///       status = status == CheckboxStatus.checked
///           ? CheckboxStatus.unchecked
///           : CheckboxStatus.checked;
///     });
///   },
///   label: 'Option 1',
///   supportText: 'Additional information about this option',
/// )
/// ```
///
/// See also:
/// - [LemonadeRadioButton], for single selection scenarios
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeCheckbox extends StatelessWidget {
  /// {@macro LemonadeCheckbox}
  const LemonadeCheckbox({
    required this.status,
    required this.onChanged,
    this.label,
    super.key,
    this.supportText,
    this.enabled = true,
    this.semanticIdentifier,
    this.semanticLabel,
  });

  /// {@template LemonadeCheckbox.status}
  /// The current status of the checkbox.
  ///
  /// Can be [CheckboxStatus.checked], [CheckboxStatus.unchecked],
  /// or [CheckboxStatus.indeterminate].
  /// {@endtemplate}
  final CheckboxStatus status;

  /// {@template LemonadeCheckbox.onChanged}
  /// A callback that is invoked when the user clicks the checkbox.
  /// {@endtemplate}
  final VoidCallback onChanged;

  /// {@template LemonadeCheckbox.label}
  /// The primary text label displayed next to the checkbox.
  /// {@endtemplate}
  final String? label;

  /// {@template LemonadeCheckbox.supportText}
  /// Optional secondary text displayed below the label.
  ///
  /// Use this to provide additional context or information about the option.
  /// If null, no support text is shown.
  /// {@endtemplate}
  final String? supportText;

  /// {@template LemonadeCheckbox.enabled}
  /// Whether the checkbox is interactive.
  ///
  /// When false, the control is displayed with reduced opacity and does not
  /// respond to taps. Defaults to true.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeCheckbox.semanticIdentifier}
  /// An identifier for the checkbox used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeCheckbox.semanticLabel}
  /// A label for the checkbox used for accessibility purposes.
  ///
  /// If not provided, the [label] text is used for accessibility.
  /// {@endtemplate}
  final String? semanticLabel;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final spaces = theme.spaces;
    final colors = theme.colors;
    final opacity = theme.opacity;

    return Semantics(
      identifier: semanticIdentifier,
      label: semanticLabel ?? label,
      checked: status == CheckboxStatus.checked,
      enabled: enabled,
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: enabled ? onChanged : null,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            Container(
              width: theme.sizes.size550,
              height: theme.sizes.size600,
              alignment: Alignment.center,
              child: _CheckboxControl(
                status: status,
                enabled: enabled,
              ),
            ),
            if (label != null) ...[
              SizedBox(width: spaces.spacing200),
              Flexible(
                child: Opacity(
                  opacity: enabled ? 1.0 : opacity.state.opacityDisabled,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      Text(
                        label!,
                        style: theme.typography.bodyMediumMedium.apply(
                          color: colors.content.contentPrimary,
                        ),
                      ),
                      if (supportText != null) ...[
                        Text(
                          supportText!,
                          style: theme.typography.bodySmallRegular.apply(
                            color: colors.content.contentSecondary,
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

/// Internal checkbox control widget that renders the square selection indicator
class _CheckboxControl extends StatelessWidget {
  const _CheckboxControl({
    required this.status,
    required this.enabled,
  });

  final CheckboxStatus status;
  final bool enabled;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final colors = theme.colors;

    // Resolve colors based on state
    final Color backgroundColor;
    final Color borderColor;
    final Color iconColor;

    final size = theme.sizes.size550;
    final borderWidth = theme.border.base.border50;
    final borderRadius = theme.radius.radius150;

    if (!enabled) {
      backgroundColor = status == CheckboxStatus.unchecked
          ? colors.background.bgElevatedHigh
          : colors.background.bgElevatedHigh;
      borderColor = status == CheckboxStatus.unchecked
          ? colors.border.borderNeutralMedium
          : const Color(0x00000000);
      iconColor = colors.content.contentTertiary;
    } else if (status == CheckboxStatus.unchecked) {
      backgroundColor = colors.background.bgDefault;
      borderColor = colors.border.borderNeutralHigh;
      iconColor = colors.content.contentPrimaryInverse;
    } else {
      backgroundColor = colors.background.bgBrandHigh;
      borderColor = const Color(0x00000000);
      iconColor = colors.content.contentPrimaryInverse;
    }

    final showBorder = status == CheckboxStatus.unchecked || !enabled;

    return SizedBox(
      width: size,
      height: size,
      child: DecoratedBox(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(borderRadius),
          color: backgroundColor,
          border: showBorder
              ? Border.all(
                  color: borderColor,
                  width: borderWidth,
                )
              : null,
        ),
        child: AnimatedSwitcher(
          duration: const Duration(milliseconds: 150),
          switchInCurve: Curves.easeInOut,
          switchOutCurve: Curves.easeInOut,
          transitionBuilder: (child, animation) {
            return FadeTransition(
              opacity: animation,
              child: ScaleTransition(
                scale: animation,
                child: child,
              ),
            );
          },
          child: status != CheckboxStatus.unchecked
              ? Center(
                  key: ValueKey(status),
                  child: LemonadeIcon(
                    icon: status == CheckboxStatus.checked
                        ? LemonadeIcons.checkSmall
                        : LemonadeIcons.minus,
                    color: iconColor,
                  ),
                )
              : const SizedBox.shrink(
                  key: ValueKey('empty'),
                ),
        ),
      ),
    );
  }
}
