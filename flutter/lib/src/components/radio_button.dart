import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeRadioButton}
/// A form control that allows users to select a single option from a group.
///
/// A [LemonadeRadioButton] displays a circular radio button with an optional
/// label and support text. Selecting one option in a group automatically
/// deselects any previously selected option.
///
/// ## Example
/// ```dart
/// LemonadeRadioButton(
///   checked: isSelected,
///   onChanged: () {
///     setState(() {
///       isSelected = true;
///     });
///   },
///   label: 'Option 1',
///   supportText: 'Additional information about this option',
/// )
/// ```
///
/// See also:
/// - [LemonadeCheckbox], for multiple selection scenarios
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeRadioButton extends StatelessWidget {
  /// {@macro LemonadeRadioButton}
  const LemonadeRadioButton({
    required this.checked,
    required this.onChanged,
    this.label,
    super.key,
    this.supportText,
    this.enabled = true,
    this.semanticIdentifier,
    this.semanticLabel,
  });

  /// {@template LemonadeRadioButton.checked}
  /// The selected state of the radio button.
  ///
  /// When true, this option is selected and displays a filled circle.
  /// When false, the radio button is unselected.
  /// {@endtemplate}
  final bool checked;

  /// {@template LemonadeRadioButton.onChanged}
  /// A callback that is invoked when the user clicks the radio button.
  ///
  /// This callback is only invoked when transitioning from unchecked to
  /// checked state, following standard radio button behavior.
  /// {@endtemplate}
  final VoidCallback onChanged;

  /// {@template LemonadeRadioButton.label}
  /// The primary text label displayed next to the radio button.
  /// {@endtemplate}
  final String? label;

  /// {@template LemonadeRadioButton.supportText}
  /// Optional secondary text displayed below the label.
  ///
  /// Use this to provide additional context or information about the option.
  /// If null, no support text is shown.
  /// {@endtemplate}
  final String? supportText;

  /// {@template LemonadeRadioButton.enabled}
  /// Whether the radio button is interactive.
  ///
  /// When false, the control is displayed with reduced opacity and does not
  /// respond to taps. Defaults to true.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeRadioButton.semanticIdentifier}
  /// An identifier for the radio button used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeRadioButton.semanticLabel}
  /// A label for the radio button used for accessibility purposes.
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
      checked: checked,
      enabled: enabled,
      inMutuallyExclusiveGroup: true,
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: enabled
            ? () {
                if (!checked) {
                  onChanged();
                }
              }
            : null,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            Container(
              width: theme.sizes.size550,
              height: theme.sizes.size600,
              alignment: Alignment.center,
              child: _RadioControl(
                checked: checked,
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

/// Internal radio control widget that renders the circular selection indicator.
class _RadioControl extends StatelessWidget {
  const _RadioControl({
    required this.checked,
    required this.enabled,
  });

  final bool checked;
  final bool enabled;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final colors = theme.colors;
    final opacity = theme.opacity;

    // Resolve colors based on state
    final Color backgroundColor;
    final Color borderColor;
    final Color innerCircleColor;

    final outerSize = theme.sizes.size500;
    final innerSize = theme.sizes.size250;
    final borderWidth = theme.border.base.border50;

    if (!enabled) {
      backgroundColor = colors.background.bgElevatedHigh;
      borderColor = colors.border.borderNeutralMedium;
      innerCircleColor = checked
          ? colors.background.bgDefaultInverse.withValues(
              alpha: opacity.state.opacityDisabled,
            )
          : const Color(0x00000000);
    } else if (checked) {
      backgroundColor = colors.background.bgBrandHigh;
      borderColor = const Color(0x00000000);
      innerCircleColor = colors.background.bgDefault;
    } else {
      backgroundColor = colors.background.bgDefault;
      borderColor = colors.border.borderNeutralHigh;
      innerCircleColor = const Color(0x00000000);
    }

    final showBorder = !checked || !enabled;

    return SizedBox(
      width: outerSize,
      height: outerSize,
      child: DecoratedBox(
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: backgroundColor,
          border: showBorder
              ? Border.all(
                  color: borderColor,
                  width: borderWidth,
                )
              : null,
        ),
        child: Center(
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 150),
            curve: Curves.easeInOut,
            width: checked ? innerSize : 0,
            height: checked ? innerSize : 0,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: innerCircleColor,
            ),
          ),
        ),
      ),
    );
  }
}
