import 'package:flutter/widgets.dart';

/// A builder function that creates a widget based on focus state.
///
/// The [focused] parameter indicates whether the widget currently has focus.
/// The [child] parameter is an optional child widget passed to the builder.
typedef FocusWidgetBuilder =
    Widget Function(
      BuildContext context,
      bool focused,
      Widget? child,
    );

/// {@template LemonadeFocusable}
/// A focusable widget from the Lemonade Design System.
///
/// [LemonadeFocusable] wraps a [Focus] widget and provides a builder pattern
/// to build widgets that respond to focus changes. It manages focus state
/// internally and notifies listeners when focus changes.
///
/// ## Example
/// ```dart
/// LemonadeFocusable(
///   builder: (context, focused, child) {
///     return Container(
///       decoration: BoxDecoration(
///         border: focused
///           ? Border.all(color: Colors.blue, width: 2)
///           : null,
///       ),
///       child: child,
///     );
///   },
///   child: Text('Focusable Widget'),
/// )
/// ```
///
/// See also:
/// - [Focus], the underlying Flutter widget
/// - [FocusNode], which manages focus state
/// {@endtemplate}
class LemonadeFocusable extends StatefulWidget {
  /// {@macro LemonadeFocusable}
  const LemonadeFocusable({
    required this.builder,
    this.focusNode,
    this.canRequestFocus = true,
    this.autofocus = false,
    this.child,
    this.onFocusChange,
    this.onKeyEvent,
    this.skipTraversal,
    this.descendantsAreFocusable,
    this.descendantsAreTraversable,
    this.includeSemantics = true,
    this.debugLabel,
    super.key,
  });

  /// Whether this widget can request focus.
  ///
  /// When false, the widget cannot receive keyboard focus.
  final bool canRequestFocus;

  /// Whether to automatically focus this widget when it is first added to
  /// the widget tree.
  final bool autofocus;

  /// An optional focus node to use for managing focus.
  ///
  /// If not provided, an internal [FocusNode] will be created and managed
  /// by this widget.
  final FocusNode? focusNode;

  /// A builder function that creates the widget based on focus state.
  ///
  /// This function is called whenever the focus state changes.
  final FocusWidgetBuilder builder;

  /// An optional child widget to pass to the builder.
  final Widget? child;

  /// Called when the focus state changes.
  ///
  /// The callback receives a boolean indicating the new focus state.
  final ValueChanged<bool>? onFocusChange;

  /// Called when a key event occurs while this widget has focus.
  final FocusOnKeyEventCallback? onKeyEvent;

  /// Whether this node should be skipped during focus traversal.
  final bool? skipTraversal;

  /// Whether descendants of this node can be focused.
  final bool? descendantsAreFocusable;

  /// Whether descendants of this node can be traversed.
  final bool? descendantsAreTraversable;

  /// Whether to include this widget in the semantics tree.
  final bool includeSemantics;

  /// A debug label for this focus node.
  final String? debugLabel;

  @override
  State<LemonadeFocusable> createState() => _LemonadeFocusableState();
}

class _LemonadeFocusableState extends State<LemonadeFocusable> {
  FocusNode? _internal;

  final isFocused = ValueNotifier(false);

  FocusNode get focusNode => widget.focusNode ?? _internal!;

  @override
  void initState() {
    super.initState();
    if (widget.focusNode == null) _internal = FocusNode();
    isFocused
      ..value = focusNode.hasFocus
      ..addListener(onFocusChange);
  }

  @override
  void dispose() {
    isFocused.removeListener(onFocusChange);
    _internal?.dispose();
    isFocused.dispose();
    super.dispose();
  }

  void onFocusChange() {
    widget.onFocusChange?.call(isFocused.value);
  }

  @override
  Widget build(BuildContext context) {
    return Focus(
      autofocus: widget.autofocus,
      canRequestFocus: widget.canRequestFocus,
      onFocusChange: (value) => isFocused.value = value,
      focusNode: focusNode,
      onKeyEvent: widget.onKeyEvent,
      skipTraversal: widget.skipTraversal,
      descendantsAreFocusable: widget.descendantsAreFocusable,
      descendantsAreTraversable: widget.descendantsAreTraversable,
      includeSemantics: widget.includeSemantics,
      debugLabel: widget.debugLabel,
      child: ValueListenableBuilder(
        valueListenable: isFocused,
        builder: (context, value, child) =>
            widget.builder(context, value, child),
        child: widget.child,
      ),
    );
  }
}
