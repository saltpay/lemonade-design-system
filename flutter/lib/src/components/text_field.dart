import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeTextField}
/// A text field widget from the Lemonade Design System.
///
/// A [LemonadeTextField] allows users to enter or edit text with support for
/// labels, placeholders, error states, and helper text. It adapts styling
/// based on focus and validation states.
///
/// ## Example
/// ```dart
/// LemonadeTextField(
///   controller: _controller,
///   label: 'Email',
///   placeholder: 'Enter your email',
///   supportText: 'We will never share your email',
/// )
/// ```
///
/// With error state:
/// ```dart
/// LemonadeTextField(
///   controller: _controller,
///   label: 'Email',
///   hasError: true,
///   errorMessage: 'Invalid email format',
/// )
/// ```
///
/// See also:
/// - [LemonadeSearchField], for search-specific input
/// - [LemonadeTextFieldTheme], for theme configuration
/// {@endtemplate}
class LemonadeTextField extends StatefulWidget {
  /// {@macro LemonadeTextField}
  const LemonadeTextField({
    this.controller,
    this.onChanged,
    this.label,
    this.placeholder,
    this.supportText,
    this.errorMessage,
    this.optionalIndicator,
    this.hasError = false,
    this.enabled = true,
    this.obscureText = false,
    this.keyboardType,
    this.textInputAction,
    this.inputFormatters,
    this.onSubmitted,
    this.focusNode,
    this.leadingIcon,
    this.trailingIcon,
    this.onTrailingIconTap,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// Controller for the text field.
  final TextEditingController? controller;

  /// Called when the text field value changes.
  final ValueChanged<String>? onChanged;

  /// Label displayed above the text field.
  final String? label;

  /// Placeholder text displayed when the field is empty.
  final String? placeholder;

  /// Support text displayed below the text field.
  final String? supportText;

  /// Error message displayed when [hasError] is true.
  final String? errorMessage;

  /// Optional indicator text displayed on the right side of the label.
  final String? optionalIndicator;

  /// Whether the text field has an error state.
  final bool hasError;

  /// Whether the text field is enabled.
  final bool enabled;

  /// Whether the text should be obscured (for passwords).
  final bool obscureText;

  /// The type of keyboard to use for editing the text.
  final TextInputType? keyboardType;

  /// The action button to use for the keyboard.
  final TextInputAction? textInputAction;

  /// Input formatters to apply to the text field.
  final List<TextInputFormatter>? inputFormatters;

  /// Called when the user submits the text field.
  final ValueChanged<String>? onSubmitted;

  /// Focus node for the text field.
  final FocusNode? focusNode;

  /// Optional icon displayed at the start of the text field.
  final LemonadeIcons? leadingIcon;

  /// Optional icon displayed at the end of the text field.
  final LemonadeIcons? trailingIcon;

  /// Called when the trailing icon is tapped.
  final VoidCallback? onTrailingIconTap;

  /// An identifier for the text field for accessibility and testing.
  final String? semanticIdentifier;

  /// A label for the text field for accessibility purposes.
  final String? semanticLabel;

  @override
  State<LemonadeTextField> createState() => _LemonadeTextFieldState();
}

class _LemonadeTextFieldState extends State<LemonadeTextField> {
  late final TextEditingController _controller;
  late final FocusNode _focusNode;
  bool _isFocused = false;

  @override
  void initState() {
    super.initState();
    _controller = widget.controller ?? TextEditingController();
    _focusNode = widget.focusNode ?? FocusNode();
    _focusNode.addListener(_onFocusChange);
    _controller.addListener(_onTextChanged);
  }

  @override
  void dispose() {
    _focusNode.removeListener(_onFocusChange);
    _controller.removeListener(_onTextChanged);
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

  void _onTextChanged() {
    widget.onChanged?.call(_controller.text);
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final textFieldTheme = theme.components.textFieldTheme;
    final colors = theme.colors;
    final typography = theme.typography;
    final radius = theme.radius;
    final spaces = theme.spaces;

    final showError = widget.hasError && !_isFocused;
    final opacity = widget.enabled
        ? theme.opacity.base.opacity100
        : theme.opacity.state.opacityDisabled;

    final backgroundColor = !widget.enabled
        ? colors.background.bgElevated
        : showError
        ? colors.background.bgCriticalSubtle
        : colors.background.bgDefault;

    final borderColor = !widget.enabled
        ? const Color(0x00000000)
        : _isFocused
        ? colors.border.borderSelected
        : showError
        ? colors.border.borderCritical
        : colors.border.borderNeutralMedium;

    return Semantics(
      label: widget.semanticLabel ?? widget.label,
      identifier: widget.semanticIdentifier,
      child: Opacity(
        opacity: opacity,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            // Label row
            if (widget.label != null || widget.optionalIndicator != null)
              Padding(
                padding: EdgeInsets.only(
                  left: spaces.spacing50,
                  right: spaces.spacing50,
                  bottom: spaces.spacing50,
                ),
                child: Row(
                  children: [
                    if (widget.label != null)
                      Expanded(
                        child: Text(
                          widget.label!,
                          style: typography.bodySmallMedium.copyWith(
                            color: widget.enabled
                                ? colors.content.contentPrimary
                                : colors.content.contentSecondary,
                          ),
                        ),
                      )
                    else
                      const Spacer(),
                    if (widget.optionalIndicator != null)
                      Text(
                        widget.optionalIndicator!,
                        style: typography.bodySmallRegular.copyWith(
                          color: colors.content.contentSecondary,
                        ),
                      ),
                  ],
                ),
              ),

            // Text field container
            GestureDetector(
              onTap: widget.enabled ? () => _focusNode.requestFocus() : null,
              child: AnimatedContainer(
                duration: const Duration(milliseconds: 200),
                decoration: BoxDecoration(
                  color: backgroundColor,
                  borderRadius: BorderRadius.circular(radius.radius300),
                  border: Border.all(
                    color: borderColor,
                    width: _isFocused
                        ? textFieldTheme.focusBorderWidth
                        : textFieldTheme.borderWidth,
                  ),
                  boxShadow: _isFocused
                      ? [
                          BoxShadow(
                            color: colors.background.bgElevated,
                            spreadRadius: textFieldTheme.focusBorderWidth,
                          ),
                        ]
                      : null,
                ),
                child: SizedBox(
                  height: textFieldTheme.height,
                  child: Row(
                    children: [
                      if (widget.leadingIcon != null) ...[
                        SizedBox(width: spaces.spacing300),
                        LemonadeIcon(
                          icon: widget.leadingIcon!,
                          color: colors.content.contentSecondary,
                        ),
                      ],
                      SizedBox(width: spaces.spacing300),
                      Expanded(
                        child: Stack(
                          alignment: Alignment.centerLeft,
                          children: [
                            ListenableBuilder(
                              listenable: _controller,
                              builder: (context, child) =>
                                  _controller.text.isEmpty &&
                                      widget.placeholder != null
                                  ? Text(
                                      widget.placeholder!,
                                      style: typography.bodyMediumRegular.apply(
                                        color: colors.content.contentSecondary,
                                      ),
                                    )
                                  : const SizedBox.shrink(),
                            ),
                            _EditableText(
                              controller: _controller,
                              focusNode: _focusNode,
                              keyboardType: widget.keyboardType,
                              textInputAction: widget.textInputAction,
                              inputFormatters: widget.inputFormatters,
                              onSubmitted: widget.onSubmitted,
                              readOnly: !widget.enabled,
                              obscureText: widget.obscureText,
                            ),
                          ],
                        ),
                      ),
                      if (widget.trailingIcon != null) ...[
                        GestureDetector(
                          onTap: widget.onTrailingIconTap,
                          child: Padding(
                            padding: EdgeInsets.only(right: spaces.spacing300),
                            child: LemonadeIcon(
                              icon: widget.trailingIcon!,
                              color: colors.content.contentSecondary,
                            ),
                          ),
                        ),
                      ] else
                        SizedBox(width: spaces.spacing300),
                    ],
                  ),
                ),
              ),
            ),

            // Error or support text
            if (widget.enabled && showError && widget.errorMessage != null)
              Padding(
                padding: EdgeInsets.only(
                  left: spaces.spacing50,
                  top: spaces.spacing50,
                ),
                child: Text(
                  widget.errorMessage!,
                  style: typography.bodyXSmallRegular.copyWith(
                    color: colors.content.contentCritical,
                  ),
                ),
              )
            else if (widget.supportText != null)
              Padding(
                padding: EdgeInsets.only(
                  left: spaces.spacing50,
                  top: spaces.spacing50,
                ),
                child: Text(
                  widget.supportText!,
                  style: typography.bodyXSmallRegular.copyWith(
                    color: colors.content.contentSecondary,
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }
}

/// Internal editable text widget used within [LemonadeTextField].
///
/// Note: This widget uses Material components, which would normally be
/// considered a mistake in Lemonade Design System. However, in this case
/// it was necessary because there are many text editing boilerplates
/// already implemented and well-consolidated in Material library.
class _EditableText extends StatelessWidget {
  const _EditableText({
    required this.controller,
    required this.focusNode,
    this.readOnly = false,
    this.obscureText = false,
    this.keyboardType,
    this.textInputAction,
    this.inputFormatters,
    this.onSubmitted,
  });

  /// Controller for the text field.
  final TextEditingController controller;

  /// Focus node for the text field.
  final FocusNode focusNode;

  /// Whether the text field is read-only.
  final bool readOnly;

  /// Whether the text should be obscured (for passwords).
  final bool obscureText;

  /// The type of keyboard to use for editing the text.
  final TextInputType? keyboardType;

  /// The action button to use for the keyboard.
  final TextInputAction? textInputAction;

  /// Input formatters to apply to the text field.
  final List<TextInputFormatter>? inputFormatters;

  /// Called when the user submits the text field.
  final ValueChanged<String>? onSubmitted;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final colors = theme.colors;
    final typography = theme.typography;

    return Material(
      type: MaterialType.transparency,
      child: Localizations.override(
        context: context,
        delegates: List.of([
          DefaultMaterialLocalizations.delegate,
        ]),
        child: TextField(
          controller: controller,
          focusNode: focusNode,
          style: typography.bodyMediumRegular.copyWith(
            color: colors.content.contentPrimary,
          ),
          decoration: null,
          cursorColor: colors.content.contentPrimary,
          keyboardType: keyboardType,
          textInputAction: textInputAction,
          inputFormatters: inputFormatters,
          onSubmitted: onSubmitted,
          readOnly: readOnly,
          obscureText: obscureText,
          enableInteractiveSelection: !readOnly,
        ),
      ),
    );
  }
}
