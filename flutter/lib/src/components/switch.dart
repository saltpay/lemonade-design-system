import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_design_system/src/raw_components/decorator.dart';
import 'package:lemonade_design_system/src/raw_components/disabled.dart';
import 'package:lemonade_design_system/src/raw_components/focusable.dart';

/// {@template LemonadeSwitch}
/// A switch widget from the Lemonade Design System.
///
/// A [LemonadeSwitch] provides a toggle control that allows users to switch
/// between two states (on/off). It features smooth animations for state
/// changes, hover, and press interactions.
///
/// ## Example
/// ```dart
/// bool switchValue = false;
///
/// LemonadeSwitch(
///   checked: switchValue,
///   onCheckedChange: (bool newValue) {
///     setState(() {
///       switchValue = newValue;
///     });
///   },
/// )
/// ```
///
/// See also:
/// - [LemonadeTheme], which provides the design tokens
/// - [LemonadeThemeData], for theme configuration
/// {@endtemplate}
class LemonadeSwitch extends StatefulWidget {
  /// {@macro LemonadeSwitch}
  ///
  /// The [onCheckedChange] callback must not be null when [enabled] is true.
  const LemonadeSwitch({
    required this.checked,
    this.enabled = true,
    this.semanticIdentifier,
    this.semanticLabel,
    this.onCheckedChange,
    this.focusNode,
    super.key,
  });

  /// {@template LemonadeSwitch.checked}
  /// Whether this switch is on or off.
  ///
  /// When true, the switch is in the "on" state. When false, it is "off".
  /// {@endtemplate}
  final bool checked;

  /// {@template LemonadeSwitch.enabled}
  /// Whether this switch is interactive.
  ///
  /// When false, the switch is disabled and cannot be interacted with.
  /// {@endtemplate}
  final bool enabled;

  /// {@template LemonadeSwitch.semanticsIdentifier}
  /// An identifier for the switch used for accessibility and testing purposes.
  ///
  /// This identifier helps to uniquely identify the switch in the UI.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeSwitch.semanticsLabel}
  /// A label for the switch used for accessibility purposes.
  ///
  /// This label provides a description of the switch's purpose.
  /// {@endtemplate}
  final String? semanticLabel;

  /// {@template LemonadeSwitch.onCheckedChange}
  /// Called when the user toggles the switch on or off.
  ///
  /// The switch passes the new value to the callback. If null, the switch
  /// will be disabled.
  /// {@endtemplate}
  final ValueChanged<bool>? onCheckedChange;

  /// {@template LemonadeSwitch.focusNode}
  /// Focus node to control the focus state of the switch.
  ///
  /// If not provided, an internal [FocusNode] is created.
  /// {@endtemplate}
  final FocusNode? focusNode;

  @override
  State<LemonadeSwitch> createState() => _LemonadeSwitchState();
}

class _LemonadeSwitchState extends State<LemonadeSwitch> {
  bool _isHovered = false;
  bool _isPressed = false;

  FocusNode? _internalFocusNode;

  @override
  void initState() {
    super.initState();

    if (widget.focusNode == null) {
      _internalFocusNode = FocusNode();
    }
  }

  @override
  void didUpdateWidget(covariant LemonadeSwitch oldWidget) {
    super.didUpdateWidget(oldWidget);

    if (oldWidget.focusNode != widget.focusNode &&
        widget.focusNode == null &&
        _internalFocusNode == null) {
      _internalFocusNode = FocusNode();
    }
  }

  @override
  void dispose() {
    _internalFocusNode?.dispose();
    super.dispose();
  }

  void _handleTap() {
    if (!widget.enabled || widget.onCheckedChange == null) return;

    widget.onCheckedChange!(!widget.checked);

    final focusNode = _resolveFocusNode();
    if (!focusNode.hasFocus) {
      FocusScope.of(context).unfocus();
    }
  }

  FocusNode _resolveFocusNode() {
    return widget.focusNode ?? _internalFocusNode!;
  }

  Color _resolveTrackColor(LemonadeThemeData theme) {
    final switchTheme = theme.components.switchTheme;
    final isEnabled = widget.enabled;
    final isChecked = widget.checked;

    if (!isEnabled) {
      return switchTheme.trackColorDisabled;
    }

    if (_isPressed) {
      return isChecked
          ? switchTheme.trackColorCheckedPressed
          : switchTheme.trackColorUncheckedPressed;
    }

    if (_isHovered) {
      return isChecked
          ? switchTheme.trackColorCheckedHover
          : switchTheme.trackColorUncheckedHover;
    }

    return isChecked
        ? switchTheme.trackColorChecked
        : switchTheme.trackColorUnchecked;
  }

  Color _resolveThumbColor(LemonadeThemeData theme) {
    final switchTheme = theme.components.switchTheme;
    if (widget.enabled) {
      return switchTheme.thumbColor;
    }
    return switchTheme.thumbColorDisabled;
  }

  double _resolveThumbExtension(LemonadeSwitchTheme switchTheme) {
    if (_isPressed) return switchTheme.thumbPressExtension;
    if (_isHovered) return switchTheme.thumbHoverExtension;
    return 0;
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final focusNode = _resolveFocusNode();
    final switchTheme = theme.components.switchTheme;

    // Geometry
    final trackWidth = switchTheme.trackWidth;
    final trackHeight = switchTheme.trackHeight;
    final thumbDiameter = switchTheme.thumbDiameter;
    final thumbExtension = _resolveThumbExtension(switchTheme);
    final thumbPadding = (trackHeight - thumbDiameter) / 2;
    final thumbWidth = thumbDiameter + thumbExtension;

    // Colors
    final thumbColor = _resolveThumbColor(theme);
    final trackColor = _resolveTrackColor(theme);

    // Shadows
    final thumbShadows = widget.enabled ? switchTheme.shadows : null;

    return Semantics(
      toggled: widget.checked,
      label: widget.semanticLabel,
      identifier: widget.semanticIdentifier,
      child: LemonadeDisabled(
        showForbiddenCursor: true,
        disabled: !widget.enabled,
        child: LemonadeFocusable(
          focusNode: focusNode,
          canRequestFocus: widget.enabled,
          builder: (context, isFocused, focusChild) {
            return adaptive(
              mobile: focusChild!,
              desktop: LemonadeDecorator(
                focused: isFocused,
                decoration: LemonadeDecoration(
                  borderRadius: BorderRadius.circular(theme.radius.radiusFull),
                  focusRingWidth: theme.border.state.focusRing,
                  focusRingColor: theme.colors.border.borderSelected,
                ),
                child: focusChild,
              ),
            );
          },
          child: MouseRegion(
            cursor: widget.enabled
                ? SystemMouseCursors.click
                : SystemMouseCursors.basic,
            onEnter: (_) => setState(() => _isHovered = true),
            onExit: (_) => setState(() => _isHovered = false),
            child: GestureDetector(
              onTap: _handleTap,
              onTapDown: (_) => setState(() => _isPressed = true),
              onTapUp: (_) => setState(() => _isPressed = false),
              onTapCancel: () => setState(() => _isPressed = false),
              child: AnimatedContainer(
                duration: switchTheme.duration,
                width: trackWidth,
                height: trackHeight,
                curve: kAnimationCurve,
                padding: EdgeInsets.all(thumbPadding),
                decoration: ShapeDecoration(
                  shape: theme.shapes.radiusFull,
                  color: trackColor,
                ),
                child: AnimatedAlign(
                  duration: switchTheme.duration,
                  alignment: widget.checked
                      ? Alignment.centerRight
                      : Alignment.centerLeft,
                  curve: kAnimationCurve,
                  child: AnimatedContainer(
                    duration: switchTheme.duration,
                    width: thumbWidth,
                    height: thumbDiameter,
                    curve: kAnimationCurve,
                    decoration: ShapeDecoration(
                      shape: theme.shapes.radiusFull,
                      color: thumbColor,
                      shadows: thumbShadows,
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
