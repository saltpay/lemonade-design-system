import 'package:flutter/material.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Variants available for [LemonadeDivider].
enum LemonadeDividerVariant {
  /// Solid line divider
  solid,

  /// Dashed line divider
  dashed,
}

/// Orientations available for [LemonadeDivider].
enum LemonadeDividerOrientation {
  /// Horizontal divider
  horizontal,

  /// Vertical divider
  vertical,
}

/// {@template LemonadeDivider}
/// A divider widget from the Lemonade Design System.
///
/// A [LemonadeDivider] displays a horizontal or vertical line to visually
/// separate content. It supports solid and dashed variants and optional labels.
///
/// ## Example
/// ```dart
/// Simple horizontal divider
/// LemonadeDivider()
///
/// Divider with label
/// LemonadeDivider(
///   label: 'Section Title',
///   variant: LemonadeDividerVariant.dashed,
/// )
///
/// Vertical divider
/// LemonadeDivider(
///   orientation: LemonadeDividerOrientation.vertical,
/// )
/// ```
/// {@endtemplate}
class LemonadeDivider extends StatelessWidget {
  /// {@macro LemonadeDivider}
  const LemonadeDivider({
    this.label,
    this.variant = LemonadeDividerVariant.solid,
    this.orientation,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : assert(
         orientation != LemonadeDividerOrientation.vertical || label == null,
         'Label cannot be used with vertical orientation',
       );

  /// The optional label to display in the center of the divider.
  /// Cannot be used with vertical orientation.
  final String? label;

  /// The variant of the divider.
  final LemonadeDividerVariant variant;

  /// The orientation of the divider.
  final LemonadeDividerOrientation? orientation;

  /// A semantic identifier for accessibility.
  final String? semanticIdentifier;

  /// A semantic label for accessibility.
  final String? semanticLabel;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    final thickness = theme.border.base.border25;
    Color dividerColor;

    switch (variant) {
      case LemonadeDividerVariant.solid:
        dividerColor = theme.colors.border.borderNeutralLow;
      case LemonadeDividerVariant.dashed:
        dividerColor = theme.colors.border.borderNeutralMedium;
    }

    return Semantics(
      label: semanticLabel ?? label,
      identifier: semanticIdentifier,
      child: label != null
          ? _DividerWithLabel(
              label: label!,
              color: dividerColor,
              variant: variant,
              thickness: thickness,
            )
          : _CoreDivider(
              orientation: orientation,
              color: dividerColor,
              variant: variant,
              thickness: thickness,
            ),
    );
  }
}

class _DividerWithLabel extends StatelessWidget {
  const _DividerWithLabel({
    required this.label,
    required this.color,
    required this.thickness,
    required this.variant,
  });

  final String label;
  final Color color;
  final double thickness;
  final LemonadeDividerVariant variant;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return Row(
      children: [
        Expanded(
          child: _CoreDivider(
            color: color,
            variant: variant,
            thickness: thickness,
          ),
        ),
        Padding(
          padding: EdgeInsets.symmetric(horizontal: theme.spaces.spacing300),
          child: Text(
            label,
            style: LemonadeTheme.of(context).typography.bodySmallRegular
                .copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
          ),
        ),
        Expanded(
          child: _CoreDivider(
            color: color,
            variant: variant,
            thickness: thickness,
          ),
        ),
      ],
    );
  }
}

class _CoreDivider extends StatelessWidget {
  const _CoreDivider({
    required this.color,
    required this.variant,
    required this.thickness,
    this.orientation,
  });

  final LemonadeDividerVariant variant;
  final double thickness;
  final Color color;
  final LemonadeDividerOrientation? orientation;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final dividerOrientation =
        orientation ?? LemonadeDividerOrientation.horizontal;

    switch (variant) {
      case LemonadeDividerVariant.solid:
        if (dividerOrientation == LemonadeDividerOrientation.vertical) {
          return VerticalDivider(
            width: thickness,
            thickness: thickness,
            color: color,
          );
        }
        return Divider(
          height: thickness,
          thickness: thickness,
          color: color,
        );
      case LemonadeDividerVariant.dashed:
        if (dividerOrientation == LemonadeDividerOrientation.vertical) {
          return SizedBox(
            width: thickness,
            child: CustomPaint(
              painter: _DashedDividerPainter(
                color: color,
                thickness: thickness,
                dashWidth: theme.sizes.size100,
                dashGap: theme.spaces.spacing100,
                orientation: dividerOrientation,
              ),
            ),
          );
        }
        return SizedBox(
          height: thickness,
          child: CustomPaint(
            painter: _DashedDividerPainter(
              color: color,
              thickness: thickness,
              dashWidth: theme.sizes.size100,
              dashGap: theme.spaces.spacing100,
              orientation: dividerOrientation,
            ),
          ),
        );
    }
  }
}

class _DashedDividerPainter extends CustomPainter {
  _DashedDividerPainter({
    required this.color,
    required this.thickness,
    required this.dashWidth,
    required this.dashGap,
    required this.orientation,
  });

  final Color color;
  final double thickness;
  final double dashWidth;
  final double dashGap;
  final LemonadeDividerOrientation orientation;

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = color
      ..strokeWidth = thickness
      ..style = PaintingStyle.stroke
      ..strokeCap = StrokeCap.butt;

    if (orientation == LemonadeDividerOrientation.horizontal) {
      double offsetX = 0;
      final offsetY = size.height / 2;

      while (offsetX < size.width) {
        canvas.drawLine(
          Offset(offsetX, offsetY),
          Offset(offsetX + dashWidth, offsetY),
          paint,
        );
        offsetX += dashWidth + dashGap;
      }
    } else {
      double offsetY = 0;
      final offsetX = size.width / 2;

      while (offsetY < size.height) {
        canvas.drawLine(
          Offset(offsetX, offsetY),
          Offset(offsetX, offsetY + dashWidth),
          paint,
        );
        offsetY += dashWidth + dashGap;
      }
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    if (oldDelegate is! _DashedDividerPainter) {
      return true;
    }
    return oldDelegate.color != color ||
        oldDelegate.thickness != thickness ||
        oldDelegate.dashWidth != dashWidth ||
        oldDelegate.dashGap != dashGap ||
        oldDelegate.orientation != orientation;
  }
}
