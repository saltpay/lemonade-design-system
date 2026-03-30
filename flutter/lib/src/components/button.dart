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
  /// Extra small size
  xSmall,

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
/// Builder function that receives [LemonadeButtonColors] for variant-aware
/// custom content inside button slots.
typedef LemonadeButtonSlotBuilder =
    Widget Function(LemonadeButtonColors colors);

/// {@macro LemonadeButton}
class LemonadeButton extends StatefulWidget {
  /// {@macro LemonadeButton}
  const LemonadeButton({
    required this.label,
    required this.onPressed,
    this.leadingIcon,
    this.trailingIcon,
    this.variant = LemonadeButtonVariant.primary,
    this.size = LemonadeButtonSize.large,
    this.spacedContents = false,
    this.enabled = true,
    this.loading = false,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : leadingSlot = null,
       trailingSlot = null;

  /// Creates a [LemonadeButton] with custom slot builders for leading and
  /// trailing content.
  ///
  /// The slot builders receive [LemonadeButtonColors] so custom content can
  /// use variant-aware colors.
  ///
  /// ## Example
  /// ```dart
  /// LemonadeButton.slots(
  ///   label: 'Custom',
  ///   onPressed: () {},
  ///   leadingSlot: (colors) => Icon(Icons.star, color: colors.contentColor),
  /// )
  /// ```
  const LemonadeButton.slots({
    required this.label,
    required this.onPressed,
    this.leadingSlot,
    this.trailingSlot,
    this.variant = LemonadeButtonVariant.primary,
    this.size = LemonadeButtonSize.large,
    this.spacedContents = false,
    this.enabled = true,
    this.loading = false,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : leadingIcon = null,
       trailingIcon = null;

  /// The label text displayed on the button.
  final String label;

  /// Callback invoked when the button is pressed.
  final VoidCallback? onPressed;

  /// Optional icon displayed before the label.
  final LemonadeIcons? leadingIcon;

  /// Optional icon displayed after the label.
  final LemonadeIcons? trailingIcon;

  /// Optional custom widget builder for leading content.
  final LemonadeButtonSlotBuilder? leadingSlot;

  /// Optional custom widget builder for trailing content.
  final LemonadeButtonSlotBuilder? trailingSlot;

  /// The button variant. Defaults to [LemonadeButtonVariant.primary].
  final LemonadeButtonVariant variant;

  /// The button size. Defaults to [LemonadeButtonSize.large].
  final LemonadeButtonSize size;

  /// When true, arranges content with space between (leading / label / trailing
  /// spread across the full width). Defaults to false (centered).
  final bool spacedContents;

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
              mainAxisAlignment: widget.spacedContents
                  ? MainAxisAlignment.spaceBetween
                  : MainAxisAlignment.center,
              children: [
                if (widget.loading) ...[
                  LemonadeSpinner(color: variantColors.contentColor),
                ] else ...[
                  if (widget.leadingSlot != null)
                    widget.leadingSlot!(variantColors)
                  else if (widget.leadingIcon != null) ...[
                    LemonadeIcon(
                      icon: widget.leadingIcon!,
                      color: variantColors.contentColor,
                    ),
                  ],
                  if (widget.spacedContents)
                    Expanded(
                      child: Padding(
                        padding: EdgeInsets.symmetric(
                          horizontal: spaces.spacing200,
                        ),
                        child: Text(
                          widget.label,
                          style: sizeData.textStyle.copyWith(
                            color: variantColors.contentColor,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    )
                  else
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
                  if (widget.trailingSlot != null)
                    widget.trailingSlot!(variantColors)
                  else if (widget.trailingIcon != null) ...[
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

  LemonadeButtonColors _getVariantColors(LemonadeSemanticColors colors) {
    return switch (widget.variant) {
      LemonadeButtonVariant.primary => LemonadeButtonColors(
        contentColor: colors.content.contentOnBrandHigh,
        backgroundColor: colors.background.bgBrand,
        pressedBackgroundColor: colors.interaction.bgBrandInteractive,
      ),
      LemonadeButtonVariant.secondary => LemonadeButtonColors(
        contentColor: colors.content.contentPrimaryInverse,
        backgroundColor: colors.background.bgSubtleInverse,
        pressedBackgroundColor: colors.interaction.bgNeutralPressed,
      ),
      LemonadeButtonVariant.neutralSubtle => LemonadeButtonColors(
        contentColor: colors.content.contentPrimary,
        backgroundColor: colors.background.bgElevated,
        pressedBackgroundColor: colors.interaction.bgElevatedPressed,
      ),
      LemonadeButtonVariant.neutralGhost => LemonadeButtonColors(
        contentColor: colors.content.contentPrimary,
        backgroundColor: const Color(0x00000000),
        pressedBackgroundColor: colors.interaction.bgSubtleInteractive,
      ),
      LemonadeButtonVariant.criticalSubtle => LemonadeButtonColors(
        contentColor: colors.content.contentCritical,
        backgroundColor: colors.background.bgCriticalSubtle,
        pressedBackgroundColor: colors.interaction.bgCriticalSubtleInteractive,
      ),
      LemonadeButtonVariant.criticalSolid => LemonadeButtonColors(
        contentColor: colors.content.contentAlwaysLight,
        backgroundColor: colors.background.bgCritical,
        pressedBackgroundColor: colors.interaction.bgCriticalInteractive,
      ),
      LemonadeButtonVariant.special => LemonadeButtonColors(
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
      LemonadeButtonSize.xSmall => _ButtonSizeData(
        verticalPadding: theme.spaces.spacing100,
        horizontalPadding: theme.spaces.spacing200,
        minHeight: buttonTheme.xSmallHeight,
        minWidth: buttonTheme.xSmallMinWidth,
        borderRadius: theme.radius.radius250,
        textStyle: theme.typography.bodySmallSemibold,
      ),
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

/// Colors resolved for a specific [LemonadeButtonVariant].
///
/// Exposed so that custom slot content can use variant-aware colors.
class LemonadeButtonColors {
  /// Creates button variant colors.
  const LemonadeButtonColors({
    required this.contentColor,
    required this.backgroundColor,
    required this.pressedBackgroundColor,
  });

  /// The color used for text and icon content.
  final Color contentColor;

  /// The default background color.
  final Color backgroundColor;

  /// The background color when the button is pressed.
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
