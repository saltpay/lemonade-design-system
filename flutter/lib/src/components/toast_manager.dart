import 'dart:async';

import 'package:lemonade_design_system/lemonade_design_system.dart';

/// Predefined duration values for toast notifications.
///
/// These constants provide common durations for displaying toasts.
/// You can also use custom [Duration] values when needed.
///
/// ## Example
/// ```dart
/// Using predefined durations
/// LemonadeToastManager.show(
///   context,
///   voice: LemonadeToastVoice.success,
///   message: 'Quick message',
/// );
///
/// Using custom duration
/// LemonadeToastManager.show(
///   context,
///   voice: LemonadeToastVoice.error,
///   message: 'Important message',
///   duration: LemonadeToastDuration.long,
/// );
/// ```
class LemonadeToastDuration {
  const LemonadeToastDuration._();

  /// Short duration: 3 seconds
  static const short = Duration(seconds: 3);

  /// Medium duration: 6 seconds
  static const medium = Duration(seconds: 6);

  /// Long duration: 9 seconds
  static const long = Duration(seconds: 9);
}

/// Internal class to represent a toast request in the queue.
class _ToastRequest {
  const _ToastRequest({
    required this.context,
    required this.toast,
    required this.duration,
    this.dismissible = true,
  });

  final BuildContext context;
  final Widget toast;
  final Duration duration;
  final bool dismissible;
}

/// {@template LemonadeToastManager}
/// A manager for displaying toast notifications as overlays.
///
/// This class provides static methods to show toast notifications
/// that appear at the bottom of the screen and auto-dismiss after a duration.
///
/// ## Example
/// ```dart
/// Generic method with voice parameter
/// LemonadeToastManager.show(
///   context,
///   voice: LemonadeToastVoice.success,
///   message: 'Changes saved successfully',
/// );
///
/// LemonadeToastManager.show(
///   context,
///   voice: LemonadeToastVoice.neutral,
///   message: 'Added to favorites',
///   icon: LemonadeIcons.heart,
/// );
/// ```
/// {@endtemplate}
class LemonadeToastManager {
  LemonadeToastManager._();

  static OverlayEntry? _currentOverlay;
  static _ToastOverlayState? _currentState;
  static Timer? _dismissTimer;
  static _ToastRequest? _pendingRequest;
  static Timer? _pendingTimer;
  static bool _isProcessing = false;

  static const Duration _pendingDelay = Duration(milliseconds: 300);

  /// Shows a toast notification with the specified voice/variant.
  ///
  /// The toast will appear at the bottom of the screen and automatically
  /// dismiss after [duration] (defaults to 3 seconds).
  ///
  /// Set [dismissible] to false to prevent dismissing the toast by swiping down.
  /// By default, toasts can be dismissed with a downward swipe gesture.
  ///
  /// ## Example
  /// ```dart
  /// Using predefined duration
  /// LemonadeToastManager.show(
  ///   context,
  ///   voice: LemonadeToastVoice.success,
  ///   message: 'Changes saved successfully',
  /// );
  ///
  /// Using custom duration
  /// LemonadeToastManager.show(
  ///   context,
  ///   voice: LemonadeToastVoice.neutral,
  ///   message: 'Added to favorites',
  ///   icon: LemonadeIcons.heart,
  /// duration: LemonadeToastDuration.long,
  /// );
  ///
  /// Non-dismissible toast
  /// LemonadeToastManager.show(
  ///   context,
  ///   voice: LemonadeToastVoice.error,
  ///   message: 'Critical error',
  ///   dismissible: false,
  /// );
  /// ```
  static void show(
    BuildContext context, {
    required LemonadeToastVoice voice,
    required String message,
    LemonadeIcons? icon,
    Duration duration = LemonadeToastDuration.short,
    String? semanticLabel,
    bool dismissible = true,
  }) {
    _showToast(
      context,
      toast: LemonadeToast(
        voice: voice,
        label: message,
        icon: icon,
        semanticLabel: semanticLabel,
      ),
      duration: duration,
      dismissible: dismissible,
    );
  }

  /// Shows a custom toast widget.
  ///
  /// Use this method if you need complete control over the toast appearance.
  /// The toast will appear at the bottom of the screen and automatically
  /// dismiss after [duration] (defaults to 3 seconds).
  ///
  /// Set [dismissible] to false to prevent dismissing the toast by swiping down.
  static void showWidget(
    BuildContext context, {
    required Widget toast,
    Duration duration = const Duration(seconds: 3),
    bool dismissible = true,
  }) {
    _showToast(
      context,
      toast: toast,
      duration: duration,
      dismissible: dismissible,
    );
  }

  /// Dismisses the currently showing toast with animation.
  static void dismiss() {
    _dismissTimer?.cancel();
    _dismissTimer = null;
    _pendingTimer?.cancel();
    _pendingTimer = null;

    final overlay = _currentOverlay;
    final state = _currentState;
    final pending = _pendingRequest;

    // Clear pending immediately so new requests during animation don't get lost
    _pendingRequest = null;

    if (overlay != null) {
      if (state != null) {
        state._animateOut(hasPending: pending != null).then((_) {
          overlay.remove();
          if (_currentOverlay == overlay) {
            _currentOverlay = null;
            _currentState = null;
          }
          _isProcessing = false;

          // Check for new pending toast that might have been set during animation
          final newPending = _pendingRequest;
          if (newPending != null && newPending.context.mounted) {
            _pendingRequest = null;
            _isProcessing = true;
            _displayToast(
              newPending.context,
              toast: newPending.toast,
              duration: newPending.duration,
              dismissible: newPending.dismissible,
            );
          } else if (pending != null && pending.context.mounted) {
            // Show original pending toast if no new one was set
            _isProcessing = true;
            _displayToast(
              pending.context,
              toast: pending.toast,
              duration: pending.duration,
              dismissible: pending.dismissible,
            );
          }
        });
      } else {
        // Fallback: remove immediately if state not found
        overlay.remove();
        _currentOverlay = null;
        _currentState = null;
        _isProcessing = false;

        // Check for new pending toast that might have been set
        final newPending = _pendingRequest;
        if (newPending != null && newPending.context.mounted) {
          _pendingRequest = null;
          _isProcessing = true;
          _displayToast(
            newPending.context,
            toast: newPending.toast,
            duration: newPending.duration,
            dismissible: newPending.dismissible,
          );
        } else if (pending != null && pending.context.mounted) {
          _isProcessing = true;
          _displayToast(
            pending.context,
            toast: pending.toast,
            duration: pending.duration,
            dismissible: pending.dismissible,
          );
        }
      }
    }
  }

  static void _showToast(
    BuildContext context, {
    required Widget toast,
    required Duration duration,
    bool dismissible = true,
  }) {
    // If already processing a toast, store the new request and dismiss current after 300ms
    if (_isProcessing) {
      _pendingTimer?.cancel();
      _pendingRequest = _ToastRequest(
        context: context,
        toast: toast,
        duration: duration,
        dismissible: dismissible,
      );

      // Wait 300ms before dismissing current toast
      _pendingTimer = Timer(
        _pendingDelay,
        dismiss,
      );
      return;
    }

    _isProcessing = true;
    _displayToast(
      context,
      toast: toast,
      duration: duration,
      dismissible: dismissible,
    );
  }

  /// Displays a toast immediately.
  static void _displayToast(
    BuildContext context, {
    required Widget toast,
    required Duration duration,
    bool dismissible = true,
  }) {
    final overlay = Overlay.of(context);
    final theme = LemonadeTheme.of(context);

    late OverlayEntry overlayEntry;
    late _ToastOverlayState? overlayState;

    overlayEntry = OverlayEntry(
      builder: (context) => _ToastOverlay(
        toast: toast,
        bottomPadding: theme.spaces.spacing600,
        dismissible: dismissible,
        onStateCreated: (state) {
          overlayState = state;
          _currentState = state;
        },
        onDismissed: dismiss,
      ),
    );

    _currentOverlay = overlayEntry;
    overlay.insert(overlayEntry);

    // Auto-dismiss after duration
    _dismissTimer = Timer(duration, () {
      final hasPending = _pendingRequest != null;
      overlayState?._animateOut(hasPending: hasPending).then((_) {
        overlayEntry.remove();
        if (_currentOverlay == overlayEntry) {
          _currentOverlay = null;
          _currentState = null;
        }
        _dismissTimer = null;
        _isProcessing = false;

        // Show pending toast if exists and context is still valid
        final pending = _pendingRequest;
        if (pending != null && pending.context.mounted) {
          _pendingRequest = null;
          _isProcessing = true;
          _displayToast(
            pending.context,
            toast: pending.toast,
            duration: pending.duration,
            dismissible: pending.dismissible,
          );
        } else if (pending != null) {
          _pendingRequest = null;
        }
      });
    });
  }
}

/// Internal widget that handles the toast overlay positioning and animation.
class _ToastOverlay extends StatefulWidget {
  const _ToastOverlay({
    required this.toast,
    required this.bottomPadding,
    this.dismissible = true,
    this.onStateCreated,
    this.onDismissed,
  });

  final Widget toast;
  final double bottomPadding;
  final bool dismissible;
  final void Function(_ToastOverlayState)? onStateCreated;
  final VoidCallback? onDismissed;

  @override
  State<_ToastOverlay> createState() => _ToastOverlayState();
}

class _ToastOverlayState extends State<_ToastOverlay>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<Offset> _slideAnimation;
  late Animation<double> _fadeAnimation;

  static const _animationDuration = Duration(milliseconds: 300);

  @override
  void initState() {
    super.initState();

    _controller = AnimationController(
      duration: _animationDuration,
      vsync: this,
    );

    _slideAnimation =
        Tween<Offset>(
          begin: const Offset(0, 2),
          end: Offset.zero,
        ).animate(
          CurvedAnimation(
            parent: _controller,
            curve: Curves.easeInOut,
          ),
        );

    _fadeAnimation =
        Tween<double>(
          begin: 0,
          end: 1,
        ).animate(
          CurvedAnimation(
            parent: _controller,
            curve: Curves.easeInOut,
          ),
        );

    _controller.forward();

    // Notify parent that state is ready
    widget.onStateCreated?.call(this);
  }

  /// Animates the toast out and returns a future that completes when done.
  ///
  /// If [hasPending] is true, the toast will slide up and fade out.
  /// If [hasPending] is false, the toast will slide down without fading.
  Future<void> _animateOut({bool hasPending = false}) async {
    if (hasPending) {
      // Animate sliding up and fading out
      final slideUp =
          Tween<Offset>(
            begin: Offset.zero,
            end: const Offset(0, -0.5),
          ).animate(
            CurvedAnimation(
              parent: _controller,
              curve: Curves.easeInOut,
            ),
          );

      final fadeOut =
          Tween<double>(
            begin: 1,
            end: 0,
          ).animate(
            CurvedAnimation(
              parent: _controller,
              curve: Curves.easeInOut,
            ),
          );

      // Update the animations
      setState(() {
        _slideAnimation = slideUp;
        _fadeAnimation = fadeOut;
      });

      // Reset and run the exit animation
      _controller.reset();
      await _controller.forward();
    } else {
      // Just slide down without fading (reverse the entrance)
      await _controller.reverse();
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    Widget toastContent = FadeTransition(
      opacity: _fadeAnimation,
      child: SlideTransition(
        position: _slideAnimation,
        child: Padding(
          padding: EdgeInsets.symmetric(
            vertical: theme.spaces.spacing400,
            horizontal: theme.spaces.spacing200,
          ),
          child: Center(child: widget.toast),
        ),
      ),
    );

    // Wrap in Dismissible if dismissible is enabled
    if (widget.dismissible) {
      toastContent = Dismissible(
        key: const Key('toast_dismissible'),
        direction: DismissDirection.down,
        onDismissed: (_) {
          widget.onDismissed?.call();
        },
        child: toastContent,
      );
    }

    return Positioned(
      left: 0,
      right: 0,
      bottom: widget.bottomPadding,
      child: toastContent,
    );
  }
}
