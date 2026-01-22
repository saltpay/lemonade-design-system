import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeDisabled}
/// A widget that disables its child by preventing interaction and applying
/// visual cues.
///
/// The [LemonadeDisabled] widget wraps a child widget to make it
/// non-interactive
/// when [disabled] is true, optionally showing a forbidden cursor to
/// indicate the disabled state.
/// {@endtemplate}
class LemonadeDisabled extends StatelessWidget {
  /// {@macro LemonadeDisabled}
  const LemonadeDisabled({
    required this.disabled,
    required this.child,
    this.showForbiddenCursor = false,
    super.key,
  });

  /// Whether the child widget is disabled.
  ///
  /// When true, the child becomes non-interactive.
  final bool disabled;

  /// The widget to be conditionally disabled.
  ///
  /// Receives the disabled behavior applied by this widget.
  final Widget child;

  /// Whether to display a forbidden cursor when the widget is disabled and
  /// hovered.
  ///
  /// Defaults to false; when true, uses [SystemMouseCursors.forbidden].
  final bool showForbiddenCursor;

  @override
  Widget build(BuildContext context) {
    Widget view = AbsorbPointer(
      absorbing: disabled,
      child: child,
    );

    if (showForbiddenCursor && disabled) {
      view = MouseRegion(cursor: SystemMouseCursors.forbidden, child: view);
    }

    return view;
  }
}
