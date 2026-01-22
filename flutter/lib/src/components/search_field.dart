import 'package:flutter/services.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeSearchField}
/// A search field widget from the Lemonade Design System.
///
/// A [LemonadeSearchField] is an input field designated for search
/// and querying.
/// It features a search icon, optional placeholder text, and a clear button
/// that appears when there is input content.
///
/// ## Example
/// ```dart
/// LemonadeSearchField(
///   controller: _searchController,
///   placeholder: 'Search products...',
///   onChanged: (value) {
///     // Handle search
///   },
/// )
/// ```
///
/// See also:
/// - [LemonadeTextField], for general text input
/// - [LemonadeSearchFieldTheme], for theme configuration
/// {@endtemplate}
class LemonadeSearchField extends StatefulWidget {
  /// {@macro LemonadeSearchField}
  const LemonadeSearchField({
    this.controller,
    this.onChanged,
    this.onClear,
    this.placeholder,
    this.enabled = true,
    this.focusNode,
    this.onSubmitted,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// Controller for the search field.
  final TextEditingController? controller;

  /// Called when the search field value changes.
  final ValueChanged<String>? onChanged;

  /// Called when the clear button is tapped.
  /// If not provided, the field will be cleared automatically.
  final VoidCallback? onClear;

  /// Placeholder text displayed when the field is empty.
  final String? placeholder;

  /// Whether the search field is enabled.
  final bool enabled;

  /// Focus node for the search field.
  final FocusNode? focusNode;

  /// Called when the user submits the search field.
  final ValueChanged<String>? onSubmitted;

  /// An identifier for the search field for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the search field for accessibility purposes.
  final String? semanticLabel;

  @override
  State<LemonadeSearchField> createState() => _LemonadeSearchFieldState();
}

class _LemonadeSearchFieldState extends State<LemonadeSearchField> {
  late final TextEditingController _controller;
  late final FocusNode _focusNode;
  bool _isFocused = false;
  bool _hasContent = false;

  @override
  void initState() {
    super.initState();
    _controller = widget.controller ?? TextEditingController();
    _focusNode = widget.focusNode ?? FocusNode();
    _focusNode.addListener(_onFocusChange);
    _controller.addListener(_onTextChange);
    _hasContent = _controller.text.isNotEmpty;
  }

  @override
  void dispose() {
    _focusNode.removeListener(_onFocusChange);
    _controller.removeListener(_onTextChange);
    if (widget.controller == null) {
      _controller.dispose();
    }
    if (widget.focusNode == null) {
      _focusNode.dispose();
    }
    super.dispose();
  }

  void _onFocusChange() {
    setState(() {
      _isFocused = _focusNode.hasFocus;
    });
  }

  void _onTextChange() {
    final hasContent = _controller.text.isNotEmpty;
    if (hasContent != _hasContent) {
      setState(() {
        _hasContent = hasContent;
      });
    }
    widget.onChanged?.call(_controller.text);
  }

  void _handleClear() {
    if (widget.onClear != null) {
      widget.onClear!();
    } else {
      _controller.clear();
      widget.onChanged?.call('');
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final searchFieldTheme = theme.components.searchFieldTheme;
    final colors = theme.colors;
    final typography = theme.typography;
    final spaces = theme.spaces;

    final opacity = widget.enabled
        ? theme.opacity.base.opacity100
        : theme.opacity.state.opacityDisabled;

    final backgroundColor = _isFocused
        ? colors.background.bgDefault
        : colors.background.bgElevated;

    final borderColor = _isFocused
        ? colors.border.borderSelected
        : const Color(0x00000000);

    final textStyle = typography.bodyMediumRegular.copyWith(
      color: colors.content.contentPrimary,
    );

    return Semantics(
      label: widget.semanticLabel ?? 'Search',
      identifier: widget.semanticIdentifier,
      child: Opacity(
        opacity: opacity,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 200),
          height: searchFieldTheme.height,
          decoration: BoxDecoration(
            color: backgroundColor,
            borderRadius: BorderRadius.circular(searchFieldTheme.height / 2),
            border: Border.all(
              color: borderColor,
              width: searchFieldTheme.borderWidth,
            ),
            boxShadow: _isFocused
                ? [
                    BoxShadow(
                      color: colors.background.bgElevatedHigh,
                      spreadRadius: searchFieldTheme.focusBorderWidth,
                    ),
                  ]
                : null,
          ),
          child: Row(
            children: [
              SizedBox(width: spaces.spacing300),
              LemonadeIcon(
                icon: LemonadeIcons.search,
                color: colors.content.contentPrimary,
              ),
              SizedBox(width: spaces.spacing200),
              Expanded(
                child: Stack(
                  alignment: Alignment.centerLeft,
                  children: [
                    if (_controller.text.isEmpty && widget.placeholder != null)
                      Text(
                        widget.placeholder!,
                        style: typography.bodyMediumRegular.copyWith(
                          color: colors.content.contentTertiary,
                        ),
                      ),
                    EditableText(
                      controller: _controller,
                      focusNode: _focusNode,
                      style: textStyle,
                      cursorColor: colors.content.contentPrimary,
                      backgroundCursorColor: colors.background.bgSubtle,
                      selectionColor: colors.interaction.bgBrandPressed,
                      keyboardType: TextInputType.text,
                      textInputAction: TextInputAction.search,
                      onSubmitted: widget.onSubmitted,
                      readOnly: !widget.enabled,
                    ),
                  ],
                ),
              ),
              AnimatedOpacity(
                duration: const Duration(milliseconds: 150),
                opacity: _hasContent && widget.enabled ? 1.0 : 0.0,
                child: _hasContent && widget.enabled
                    ? GestureDetector(
                        onTap: _handleClear,
                        child: Padding(
                          padding: EdgeInsets.symmetric(
                            horizontal: spaces.spacing300,
                          ),
                          child: LemonadeIcon(
                            icon: LemonadeIcons.circleXSolid,
                            color: colors.content.contentSecondary,
                          ),
                        ),
                      )
                    : SizedBox(width: spaces.spacing300),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
