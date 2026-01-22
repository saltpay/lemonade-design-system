import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_design_system/src/raw_components/decorator.dart';
import 'package:lemonade_design_system/src/raw_components/disabled.dart';
import 'package:lemonade_design_system/src/raw_components/focusable.dart';

/// Variants available for [LemonadeTile].
enum LemonadeTileVariants {
  /// Neutral style variant
  neutral,

  /// Muted style variant
  muted,

  /// On brand style variant
  onBrand,
}

/// {@template LemonadeTile}
/// A tile widget from the Lemonade Design System.
///
/// A [LemonadeTile] is a card container that displays an icon and a label.
/// It includes different variants for various looks.
///
/// ## Parameters
/// - [label]: The text displayed as the label of the tile.
/// - [leadingIcon]: The icon from [LemonadeIcons] shown at the top of the tile.
/// - [enabled]: Whether the tile is interactive. Defaults to `true`.
/// - [onTap]: A callback invoked when the tile is clicked.
/// - [addOnSlot]: An optional badge displayed at the top right of the tile.
/// - [variant]: The visual style of the tile defined by [LemonadeTileVariants].
///
/// ## Example
/// ```dart
/// LemonadeTile(
///   leadingIcon: LemonadeIcons.heart,
///   label: 'Tile Label',
/// )
/// ```
///
/// See also:
/// - [LemonadeTileVariants]
/// {@endtemplate}
class LemonadeTile extends StatefulWidget {
  /// {@macro LemonadeTile}
  const LemonadeTile({
    required this.label,
    required this.leadingIcon,
    this.enabled = true,
    this.onTap,
    this.addOnSlot,
    this.variant = LemonadeTileVariants.neutral,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// Whether the tile is interactive, can apply a disabled visual style.
  final bool enabled;

  /// The label to display in the tile.
  final String label;

  /// The visual style variant of the tile.
  final LemonadeTileVariants variant;

  /// The icon to display at the top of the tile.
  final LemonadeIcons leadingIcon;

  /// A callback that is invoked when the user clicks the tile.
  final VoidCallback? onTap;

  /// An optional badge to display at the top right of the tile.
  final Widget? addOnSlot;

  /// A semantic identifier for accessibility.
  final String? semanticIdentifier;

  /// A semantic label for accessibility.
  final String? semanticLabel;

  @override
  State<LemonadeTile> createState() => _LemonadeTileState();
}

class _LemonadeTileState extends State<LemonadeTile> {
  bool _isHovered = false;
  bool _isPressed = false;

  final FocusNode _focusNode = FocusNode();

  Color _getBorderColor(LemonadeThemeData theme) {
    final tileTheme = theme.components.tileTheme;

    return switch (widget.variant) {
      LemonadeTileVariants.neutral => tileTheme.borderColorNeutral,
      LemonadeTileVariants.muted => tileTheme.borderColorMuted,
      LemonadeTileVariants.onBrand => tileTheme.borderColorOnBrand,
    };
  }

  Color _getBackgroundColor(LemonadeThemeData theme) {
    final tileTheme = theme.components.tileTheme;

    if (widget.enabled && _isHovered) {
      return switch (widget.variant) {
        LemonadeTileVariants.neutral => tileTheme.backgroundColorNeutralHovered,
        LemonadeTileVariants.muted => tileTheme.backgroundColorMutedHovered,
        LemonadeTileVariants.onBrand => tileTheme.backgroundColorOnColorHovered,
      };
    }

    if (widget.enabled && _isPressed) {
      return switch (widget.variant) {
        LemonadeTileVariants.neutral => tileTheme.backgroundColorNeutralPressed,
        LemonadeTileVariants.muted => tileTheme.backgroundColorMutedPressed,
        LemonadeTileVariants.onBrand => tileTheme.backgroundColorOnColorPressed,
      };
    }

    return switch (widget.variant) {
      LemonadeTileVariants.neutral => tileTheme.backgroundColorNeutralDefault,
      LemonadeTileVariants.muted => tileTheme.backgroundColorMutedDefault,
      LemonadeTileVariants.onBrand => tileTheme.backgroundColorOnColorDefault,
    };
  }

  BoxBorder? _getBoxBorder(LemonadeThemeData theme) {
    if (widget.variant == LemonadeTileVariants.neutral && _isPressed) {
      return null;
    }

    final borderColor = _getBorderColor(theme);
    final borderWidth = theme.border.base.border25;

    return switch (widget.variant) {
      LemonadeTileVariants.neutral => Border(
        bottom: BorderSide(
          color: borderColor,
          width: borderWidth,
        ),
      ),
      _ => Border.all(
        color: borderColor,
        width: borderWidth,
      ),
    };
  }

  List<BoxShadow>? _getBoxShadow(LemonadeThemeData theme) {
    if (widget.variant == LemonadeTileVariants.muted && !_isPressed) {
      return theme.shadows.xsmall;
    }

    return null;
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    final opacity = widget.enabled
        ? theme.opacity.base.opacity100
        : theme.opacity.state.opacityDisabled;

    return Semantics(
      label: widget.semanticLabel ?? widget.label,
      identifier: widget.semanticIdentifier,
      child: LemonadeDisabled(
        showForbiddenCursor: true,
        disabled: !widget.enabled,
        child: Opacity(
          opacity: opacity,
          child: Stack(
            clipBehavior: Clip.none,
            children: [
              _focusableTile(theme),

              if (widget.addOnSlot != null)
                Positioned(
                  top: -theme.spaces.spacing200,
                  right: -theme.spaces.spacing400,
                  child: widget.addOnSlot!,
                ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _focusableTile(LemonadeThemeData theme) {
    return LemonadeFocusable(
      focusNode: _focusNode,
      canRequestFocus: widget.enabled,
      builder: (context, isFocused, focusChild) {
        return LemonadeDecorator(
          focused: isFocused,
          decoration: LemonadeDecoration(
            borderRadius: BorderRadius.circular(theme.radius.radius400),
            focusRingWidth: theme.border.state.focusRing,
            focusRingColor: theme.colors.border.borderSelected,
          ),
          child: focusChild!,
        );
      },
      child: MouseRegion(
        cursor: widget.enabled
            ? SystemMouseCursors.click
            : SystemMouseCursors.basic,
        onEnter: (_) => setState(() => _isHovered = true),
        onExit: (_) => setState(() => _isHovered = false),
        child: GestureDetector(
          onTapDown: (_) => setState(() => _isPressed = true),
          onTapUp: (_) => setState(() => _isPressed = false),
          onTapCancel: () => setState(() => _isPressed = false),
          onTap: widget.enabled ? widget.onTap : null,
          child: _roundedIconLabel(theme),
        ),
      ),
    );
  }

  Widget _roundedIconLabel(LemonadeThemeData theme) {
    final tileTheme = theme.components.tileTheme;

    final backgroundColor = _getBackgroundColor(theme);

    return Container(
      width: tileTheme.width,
      padding: EdgeInsets.symmetric(
        horizontal: tileTheme.horizontalPadding,
        vertical: theme.spaces.spacing400,
      ),
      decoration: BoxDecoration(
        color: backgroundColor,
        borderRadius: BorderRadius.circular(theme.radius.radius400),
        border: _getBoxBorder(theme),
        boxShadow: _getBoxShadow(theme),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: adaptive(
          mobile: CrossAxisAlignment.center,
          desktop: CrossAxisAlignment.start,
        ),
        children: [
          SizedBox(height: theme.spaces.spacing50),

          LemonadeIcon(icon: widget.leadingIcon),

          SizedBox(height: theme.spaces.spacing200),

          Text(
            widget.label,
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
            style: theme.typography.bodySmallMedium.apply(
              color: theme.colors.content.contentPrimary,
            ),
          ),
        ],
      ),
    );
  }
}
