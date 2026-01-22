import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_design_system/src/raw_components/focusable.dart';

/// {@template LemonadeDecorator}
/// A decorator widget that applies visual styling based on focus state.
///
/// [LemonadeDecorator] wraps a child widget and applies a decoration that
/// can change based on whether the widget is focused. It's commonly used
/// to add focus rings or other visual indicators to interactive components.
///
/// ## Example
/// ```dart
/// LemonadeDecorator(
///   focused: isFocused,
///   decoration: LemonadeDecoration(
///     borderRadius: BorderRadius.circular(8),
///     focusRingColor: Colors.blue,
///   ),
///   child: Container(
///     width: 100,
///     height: 100,
///     color: Colors.white,
///   ),
/// )
/// ```
///
/// See also:
/// - [LemonadeFocusable], which provides focus state detection
/// - [LemonadeDecoration], which defines the decoration properties
/// {@endtemplate}
class LemonadeDecorator extends StatelessWidget {
  /// {@macro LemonadeDecorator}
  const LemonadeDecorator({
    required this.child,
    this.decoration,
    this.focused = false,
    super.key,
  });

  /// The child widget to decorate.
  final Widget child;

  /// The decoration to apply to the child.
  ///
  /// If null, default decoration from theme will be used.
  final LemonadeDecoration? decoration;

  /// Whether the child is currently focused.
  ///
  /// When true, the focus ring will be displayed if configured.
  final bool focused;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final effectiveDecoration = decoration ?? const LemonadeDecoration();

    // If not focused or no focus ring configured, just wrap the child
    if (!focused || effectiveDecoration.focusRingWidth == null) {
      return Container(
        decoration: BoxDecoration(
          color: effectiveDecoration.color,
          borderRadius: effectiveDecoration.borderRadius,
          boxShadow: effectiveDecoration.shadows,
        ),
        child: child,
      );
    }

    // Apply focus ring decoration
    final focusRingWidth = effectiveDecoration.focusRingWidth!;
    final focusRingColor =
        effectiveDecoration.focusRingColor ??
        theme.colors.border.borderSelected;
    final focusRingOffset =
        effectiveDecoration.focusRingOffset ?? kFocusRingOffset;

    return Container(
      decoration: BoxDecoration(
        color: effectiveDecoration.color,
        borderRadius: effectiveDecoration.borderRadius,
        boxShadow: effectiveDecoration.shadows,
      ),
      child: CustomPaint(
        painter: _FocusRingPainter(
          color: focusRingColor,
          width: focusRingWidth,
          offset: focusRingOffset,
          borderRadius: effectiveDecoration.borderRadius,
        ),
        child: child,
      ),
    );
  }
}

/// {@template LemonadeDecoration}
/// Configuration for [LemonadeDecorator] appearance.
///
/// Defines properties like background color, border radius, shadows,
/// and focus ring styling.
/// {@endtemplate}
@immutable
class LemonadeDecoration {
  /// {@macro LemonadeDecoration}
  const LemonadeDecoration({
    this.color,
    this.borderRadius,
    this.shadows,
    this.focusRingWidth,
    this.focusRingColor,
    this.focusRingOffset,
  });

  /// Background color of the decorated widget.
  final Color? color;

  /// Border radius for rounded corners.
  final BorderRadius? borderRadius;

  /// Shadows to apply to the widget.
  final List<BoxShadow>? shadows;

  /// Width of the focus ring border.
  ///
  /// If null, no focus ring will be displayed even when focused.
  final double? focusRingWidth;

  /// Color of the focus ring.
  ///
  /// If null, defaults to the theme's selected border color.
  final Color? focusRingColor;

  /// Offset of the focus ring from the widget edge.
  ///
  /// Positive values move the ring outward, negative values inward.
  /// Defaults to [kFocusRingOffset].
  final double? focusRingOffset;

  /// Creates a copy of this decoration with the given fields replaced.
  LemonadeDecoration copyWith({
    Color? color,
    BorderRadius? borderRadius,
    List<BoxShadow>? shadows,
    double? focusRingWidth,
    Color? focusRingColor,
    double? focusRingOffset,
  }) {
    return LemonadeDecoration(
      color: color ?? this.color,
      borderRadius: borderRadius ?? this.borderRadius,
      shadows: shadows ?? this.shadows,
      focusRingWidth: focusRingWidth ?? this.focusRingWidth,
      focusRingColor: focusRingColor ?? this.focusRingColor,
      focusRingOffset: focusRingOffset ?? this.focusRingOffset,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LemonadeDecoration &&
          runtimeType == other.runtimeType &&
          color == other.color &&
          borderRadius == other.borderRadius &&
          shadows == other.shadows &&
          focusRingWidth == other.focusRingWidth &&
          focusRingColor == other.focusRingColor &&
          focusRingOffset == other.focusRingOffset;

  @override
  int get hashCode => Object.hash(
    color,
    borderRadius,
    shadows,
    focusRingWidth,
    focusRingColor,
    focusRingOffset,
  );
}

/// Custom painter that draws a focus ring around the widget.
class _FocusRingPainter extends CustomPainter {
  const _FocusRingPainter({
    required this.color,
    required this.width,
    required this.offset,
    this.borderRadius,
  });

  /// Color of the focus ring.
  final Color color;

  /// Width of the focus ring stroke.
  final double width;

  /// Offset from the widget edge (positive = outward, negative = inward).
  final double offset;

  /// Border radius for rounded corners.
  final BorderRadius? borderRadius;

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = color
      ..style = PaintingStyle.stroke
      ..strokeWidth = width;

    final rect = Offset.zero & size;
    final inflatedRect = rect.inflate(offset);

    if (borderRadius != null) {
      final rrect = borderRadius!.toRRect(inflatedRect);
      canvas.drawRRect(rrect, paint);
    } else {
      canvas.drawRect(inflatedRect, paint);
    }
  }

  @override
  bool shouldRepaint(_FocusRingPainter oldDelegate) {
    return color != oldDelegate.color ||
        width != oldDelegate.width ||
        offset != oldDelegate.offset ||
        borderRadius != oldDelegate.borderRadius;
  }
}
