import 'package:flutter_svg/flutter_svg.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// The voice/variant of a toast notification.
///
/// Determines the icon and color scheme used for the toast.
enum LemonadeToastVoice {
  /// Success toast with a checkmark icon.
  success,

  /// Error toast with an error icon.
  error,

  /// Neutral toast with a customizable icon.
  neutral,
}

/// {@template LemonadeToast}
/// A toast notification widget from the Lemonade Design System.
///
/// Displays a brief message with an icon in a pill-shaped container.
/// Used for non-intrusive feedback to user actions.
///
/// ## Example
///
/// Use factory constructors for specific voices:
/// ```dart
/// LemonadeToast.success(label: 'Changes saved successfully')
/// LemonadeToast.error(label: 'Something went wrong')
/// LemonadeToast.neutral(label: 'Your session will expire')
/// ```
///
/// For neutral toasts, you can customize the icon:
/// ```dart
/// LemonadeToast.neutral(
///   label: 'Your link is ready',
///   icon: LemonadeIcons.circleCheck,
/// )
/// ```
///
/// See also:
/// - [LemonadeToastVoice], for available toast variants
/// - [LemonadeToastTheme], for customizing toast appearance
/// {@endtemplate}
class LemonadeToast extends StatelessWidget {
  /// {@macro LemonadeToast}
  const LemonadeToast({
    required this.label,
    this.voice = LemonadeToastVoice.neutral,
    this.icon,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  });

  /// Creates a success toast with a checkmark icon.
  ///
  /// The icon is fixed and cannot be customized.
  const LemonadeToast.success({
    required this.label,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : voice = LemonadeToastVoice.success,
       icon = null;

  /// Creates an error toast with an error icon.
  ///
  /// The icon is fixed and cannot be customized.
  const LemonadeToast.error({
    required this.label,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : voice = LemonadeToastVoice.error,
       icon = null;

  /// Creates a neutral toast with a customizable icon.
  ///
  /// If [icon] is not provided, defaults to [LemonadeIcons.circleAlert].
  const LemonadeToast.neutral({
    required this.label,
    this.icon,
    this.semanticIdentifier,
    this.semanticLabel,
    super.key,
  }) : voice = LemonadeToastVoice.neutral;

  /// The message to display in the toast.
  final String label;

  /// The voice/variant of the toast.
  ///
  /// Determines the icon and color used.
  /// Defaults to [LemonadeToastVoice.neutral].
  final LemonadeToastVoice voice;

  /// Custom icon for the toast.
  ///
  /// Only used when [voice] is [LemonadeToastVoice.neutral].
  /// If not provided, defaults to [LemonadeIcons.circleAlert].
  final LemonadeIcons? icon;

  /// An identifier for the toast used for accessibility and testing purposes.
  final String? semanticIdentifier;

  /// A label for the toast used for accessibility purposes.
  final String? semanticLabel;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    final toastTheme = theme.components.toastTheme;

    final iconColor = _getIconColor(toastTheme);
    final iconAsset = _getIconAsset();

    return Semantics(
      label: semanticLabel ?? label,
      identifier: semanticIdentifier,
      child: Container(
        constraints: BoxConstraints(
          minHeight: toastTheme.minHeight,
        ),
        padding: toastTheme.padding,
        decoration: BoxDecoration(
          color: toastTheme.backgroundColor,
          borderRadius: BorderRadius.circular(toastTheme.borderRadius),
          boxShadow: toastTheme.shadow,
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            SvgPicture.asset(
              iconAsset,
              width: toastTheme.iconSize,
              height: toastTheme.iconSize,
              colorFilter: ColorFilter.mode(
                iconColor,
                BlendMode.srcIn,
              ),
            ),
            SizedBox(width: toastTheme.iconLabelGap),
            Flexible(
              child: Text(
                label,
                style: toastTheme.labelStyle,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Color _getIconColor(LemonadeToastTheme toastTheme) {
    return switch (voice) {
      LemonadeToastVoice.success => toastTheme.successIconColor,
      LemonadeToastVoice.error => toastTheme.errorIconColor,
      LemonadeToastVoice.neutral => toastTheme.neutralIconColor,
    };
  }

  String _getIconAsset() {
    return switch (voice) {
      LemonadeToastVoice.success => LemonadeIcons.circleCheck.assetPath,
      LemonadeToastVoice.error => LemonadeIcons.circleX.assetPath,
      LemonadeToastVoice.neutral =>
        icon?.assetPath ?? LemonadeIcons.circleAlert.assetPath,
    };
  }
}
