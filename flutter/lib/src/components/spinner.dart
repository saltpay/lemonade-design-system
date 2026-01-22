import 'dart:math' as math;

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Sizes available for [LemonadeSpinner].
enum LemonadeSpinnerSize {
  /// Extra small size (12px)
  xSmall,

  /// Small size (16px)
  small,

  /// Medium size (20px)
  medium,

  /// Large size (24px)
  large,

  /// Extra large size (32px)
  xLarge,

  /// Extra extra large size (40px)
  xxLarge,

  /// Extra extra extra large size (48px)
  xxxLarge,
}

/// {@template LemonadeSpinner}
/// A spinner widget from the Lemonade Design System.
///
/// A [LemonadeSpinner] is an animated indicator used to show that a process
/// is ongoing. It communicates loading or waiting states without blocking
/// the interface.
///
/// ## Example
/// ```dart
/// LemonadeSpinner(
///   size: LemonadeSpinnerSize.medium,
/// )
/// ```
///
/// See also:
/// - [LemonadeSpinnerSize], for size options
/// - [LemonadeSpinnerTheme], for theme configuration
/// {@endtemplate}
class LemonadeSpinner extends StatefulWidget {
  /// {@macro LemonadeSpinner}
  const LemonadeSpinner({
    this.size = LemonadeSpinnerSize.medium,
    this.color,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// {@template LemonadeSpinner.size}
  /// The size of the spinner.
  ///
  /// Determines the dimensions of the spinner. Defaults to
  /// [LemonadeSpinnerSize.medium].
  /// {@endtemplate}
  final LemonadeSpinnerSize size;

  /// {@template LemonadeSpinner.color}
  /// Optional color override for the spinner.
  ///
  /// If not provided, uses the default spinner color from theme
  /// ([LemonadeContentColors.contentSecondary]).
  /// {@endtemplate}
  final Color? color;

  /// {@template LemonadeSpinner.semanticsIdentifier}
  /// An identifier for the spinner used for accessibility and testing purposes.
  ///
  /// This identifier helps to uniquely identify the spinner in the UI.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeSpinner.semanticLabel}
  /// A label for the spinner used for accessibility purposes.
  ///
  /// This label provides a description of the spinner's purpose.
  /// {@endtemplate}
  final String? semanticLabel;

  @override
  State<LemonadeSpinner> createState() => _LemonadeSpinnerState();
}

class _LemonadeSpinnerState extends State<LemonadeSpinner>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1000),
    )..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  double _getSpinnerSize(LemonadeSpinnerTheme spinnerTheme) {
    return switch (widget.size) {
      LemonadeSpinnerSize.xSmall => spinnerTheme.xSmallSize,
      LemonadeSpinnerSize.small => spinnerTheme.smallSize,
      LemonadeSpinnerSize.medium => spinnerTheme.mediumSize,
      LemonadeSpinnerSize.large => spinnerTheme.largeSize,
      LemonadeSpinnerSize.xLarge => spinnerTheme.xLargeSize,
      LemonadeSpinnerSize.xxLarge => spinnerTheme.xxLargeSize,
      LemonadeSpinnerSize.xxxLarge => spinnerTheme.xxxLargeSize,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final spinnerTheme = theme.components.spinnerTheme;
    final spinnerSize = _getSpinnerSize(spinnerTheme);
    final spinnerColor = widget.color ?? spinnerTheme.color;
    final strokeWidth =
        spinnerSize * (spinnerTheme.strokeWidth / spinnerTheme.mediumSize);

    return Semantics(
      label: widget.semanticLabel ?? 'Loading',
      identifier: widget.semanticIdentifier,
      child: AnimatedBuilder(
        animation: _controller,
        builder: (context, child) {
          return Transform.rotate(
            angle: _controller.value * 2 * math.pi,
            child: CustomPaint(
              size: Size(spinnerSize, spinnerSize),
              painter: _SpinnerPainter(
                color: spinnerColor,
                strokeWidth: strokeWidth,
              ),
            ),
          );
        },
      ),
    );
  }
}

class _SpinnerPainter extends CustomPainter {
  _SpinnerPainter({
    required this.color,
    required this.strokeWidth,
  });

  final Color color;
  final double strokeWidth;

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = color
      ..style = PaintingStyle.stroke
      ..strokeWidth = strokeWidth
      ..strokeCap = StrokeCap.square;

    final rect = Rect.fromLTWH(0, 0, size.width, size.height);

    // Draw arc covering 285 degrees (same as KMP)
    canvas.drawArc(
      rect,
      0,
      285 * math.pi / 180,
      false,
      paint,
    );
  }

  @override
  bool shouldRepaint(_SpinnerPainter oldDelegate) {
    return oldDelegate.color != color || oldDelegate.strokeWidth != strokeWidth;
  }
}
