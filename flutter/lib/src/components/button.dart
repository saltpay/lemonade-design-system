import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Variants available for [LemonadeButton].
enum LemonadeButtonVariant {
  /// Primary variant - brand colored background
  primary,

  /// Secondary variant - inverse/dark background
  secondary,

  /// Neutral variant - elevated background
  neutralSubtle,

  /// Neutral variant - elevated background
  neutralGhost,

  /// Critical subtle variant - critical/danger colored background
  criticalSubtle,

  /// Critical solid variant - critical/danger colored background
  criticalSolid,

  /// Special variant - brand with gradient overlay
  special,
}

/// Sizes available for [LemonadeButton].
enum LemonadeButtonSize {
  /// Small size
  small,

  /// Medium size
  medium,

  /// Large size
  large,
}

/// {@template LemonadeButton}
/// A button widget from the Lemonade Design System.
///
/// A [LemonadeButton] is used for triggering actions with support for
/// multiple variants, sizes, and optional leading/trailing icons.
///
/// ## Example
/// ```dart
/// LemonadeButton(
///   label: 'Click me!',
///   onPressed: () {
///     print('Button pressed!');
///   },
/// )
/// ```
///
/// With variant and icon:
/// ```dart
/// LemonadeButton(
///   label: 'Delete',
///   variant: LemonadeButtonVariant.critical,
///   leadingIcon: LemonadeIcons.trash,
///   onPressed: () {},
/// )
/// ```
///
/// See also:
/// - [LemonadeButtonTheme], for theme configuration
/// {@endtemplate}
class LemonadeButton extends StatefulWidget {
  /// {@macro LemonadeButton}
  const LemonadeButton({
    required this.label,
    required this.onPressed,
    this.leadingIcon,
    this.trailingIcon,
    this.variant = LemonadeButtonVariant.primary,
    this.size = LemonadeButtonSize.large,
    this.enabled = true,
    this.loading = false,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// The label text displayed on the button.
  final String label;

  /// Callback invoked when the button is pressed.
  final VoidCallback? onPressed;

  /// Optional icon displayed before the label.
  final LemonadeIcons? leadingIcon;

  /// Optional icon displayed after the label.
  final LemonadeIcons? trailingIcon;

  /// The button variant. Defaults to [LemonadeButtonVariant.primary].
  final LemonadeButtonVariant variant;

  /// The button size. Defaults to [LemonadeButtonSize.large].
  final LemonadeButtonSize size;

  /// Whether the button is enabled. Defaults to true.
  final bool enabled;

  /// Whether the button is loading. Defaults to false.
  final bool loading;

  /// An identifier for the button for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the button for accessibility purposes.
  final String? semanticLabel;

  @override
  State<LemonadeButton> createState() => _LemonadeButtonState();
}

class _LemonadeButtonState extends State<LemonadeButton> {
  bool _isPressed = false;

  void _handleTapDown(TapDownDetails details) {
    if (widget.enabled && !widget.loading) {
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
    final buttonTheme = theme.components.buttonTheme;
    final colors = theme.colors;
    final spaces = theme.spaces;

    final opacity = widget.enabled
        ? theme.opacity.base.opacity100
        : theme.opacity.state.opacityDisabled;

    // Get variant colors
    final variantColors = _getVariantColors(colors);
    final sizeData = _getSizeData(buttonTheme, theme);

    final backgroundColor = _isPressed
        ? variantColors.pressedBackgroundColor
        : variantColors.backgroundColor;

    return Semantics(
      label: widget.semanticLabel ?? widget.label,
      identifier: widget.semanticIdentifier,
      button: true,
      child: GestureDetector(
        onTapDown: _handleTapDown,
        onTapUp: _handleTapUp,
        onTapCancel: _handleTapCancel,
        onTap: widget.enabled || widget.loading ? widget.onPressed : null,
        child: Opacity(
          opacity: opacity,
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 150),
            constraints: BoxConstraints(
              minWidth: sizeData.minWidth,
              minHeight: sizeData.minHeight,
            ),
            decoration: BoxDecoration(
              color: backgroundColor,
              borderRadius: BorderRadius.circular(sizeData.borderRadius),
            ),
            padding: EdgeInsets.symmetric(
              horizontal: sizeData.horizontalPadding,
              vertical: sizeData.verticalPadding,
            ),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                if (widget.loading) ...[
                  LemonadeSpinner(
                    color: variantColors.contentColor,
                  ),
                ] else ...[
                  if (widget.leadingIcon != null) ...[
                    LemonadeIcon(
                      icon: widget.leadingIcon!,
                      color: variantColors.contentColor,
                    ),
                  ],
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: spaces.spacing200,
                    ),
                    child: Text(
                      widget.label,
                      style: sizeData.textStyle.copyWith(
                        color: variantColors.contentColor,
                      ),
                    ),
                  ),
                  if (widget.trailingIcon != null) ...[
                    LemonadeIcon(
                      icon: widget.trailingIcon!,
                      size: LemonadeIconSize.small,
                      color: variantColors.contentColor,
                    ),
                  ],
                ],
              ],
            ),
          ),
        ),
      ),
    );
  }

  _ButtonVariantColors _getVariantColors(LemonadeSemanticColors colors) {
    return switch (widget.variant) {
      LemonadeButtonVariant.primary => _ButtonVariantColors(
        contentColor: colors.content.contentOnBrandHigh,
        backgroundColor: colors.background.bgBrand,
        pressedBackgroundColor: colors.interaction.bgBrandInteractive,
      ),
      LemonadeButtonVariant.secondary => _ButtonVariantColors(
        contentColor: colors.content.contentPrimaryInverse,
        backgroundColor: colors.background.bgSubtleInverse,
        pressedBackgroundColor: colors.interaction.bgNeutralPressed,
      ),
      LemonadeButtonVariant.neutralSubtle => _ButtonVariantColors(
        contentColor: colors.content.contentPrimary,
        backgroundColor: colors.background.bgElevated,
        pressedBackgroundColor: colors.interaction.bgElevatedPressed,
      ),
      LemonadeButtonVariant.neutralGhost => _ButtonVariantColors(
        contentColor: colors.content.contentPrimary,
        backgroundColor: const Color(0x00000000),
        pressedBackgroundColor: colors.interaction.bgSubtleInteractive,
      ),
      LemonadeButtonVariant.criticalSubtle => _ButtonVariantColors(
        contentColor: colors.content.contentCritical,
        backgroundColor: colors.background.bgCriticalSubtle,
        pressedBackgroundColor: colors.interaction.bgCriticalSubtleInteractive,
      ),
      LemonadeButtonVariant.criticalSolid => _ButtonVariantColors(
        contentColor: colors.content.contentAlwaysLight,
        backgroundColor: colors.background.bgCritical,
        pressedBackgroundColor: colors.interaction.bgCriticalInteractive,
      ),
      LemonadeButtonVariant.special => _ButtonVariantColors(
        contentColor: colors.content.contentOnBrandHigh,
        backgroundColor: colors.background.bgBrand,
        pressedBackgroundColor: colors.interaction.bgBrandPressed,
      ),
    };
  }

  _ButtonSizeData _getSizeData(
    LemonadeButtonTheme buttonTheme,
    LemonadeThemeData theme,
  ) {
    return switch (widget.size) {
      LemonadeButtonSize.small => _ButtonSizeData(
        verticalPadding: theme.spaces.spacing200,
        horizontalPadding: theme.spaces.spacing300,
        minHeight: buttonTheme.smallHeight,
        minWidth: buttonTheme.smallMinWidth,
        borderRadius: theme.radius.radius300,
        textStyle: theme.typography.bodySmallSemibold,
      ),
      LemonadeButtonSize.medium => _ButtonSizeData(
        verticalPadding: theme.spaces.spacing300,
        horizontalPadding: theme.spaces.spacing400,
        minHeight: buttonTheme.mediumHeight,
        minWidth: buttonTheme.mediumMinWidth,
        borderRadius: theme.radius.radius300,
        textStyle: theme.typography.bodyMediumSemibold,
      ),
      LemonadeButtonSize.large => _ButtonSizeData(
        verticalPadding: theme.spaces.spacing300,
        horizontalPadding: theme.spaces.spacing400,
        minHeight: buttonTheme.largeHeight,
        minWidth: buttonTheme.largeMinWidth,
        borderRadius: theme.radius.radius400,
        textStyle: theme.typography.bodyMediumSemibold,
      ),
    };
  }
}

class _ButtonVariantColors {
  const _ButtonVariantColors({
    required this.contentColor,
    required this.backgroundColor,
    required this.pressedBackgroundColor,
  });

  final Color contentColor;
  final Color backgroundColor;
  final Color pressedBackgroundColor;
}

class _ButtonSizeData {
  const _ButtonSizeData({
    required this.verticalPadding,
    required this.horizontalPadding,
    required this.minHeight,
    required this.minWidth,
    required this.borderRadius,
    required this.textStyle,
  });

  final double verticalPadding;
  final double horizontalPadding;
  final double minHeight;
  final double minWidth;
  final double borderRadius;
  final TextStyle textStyle;
}
