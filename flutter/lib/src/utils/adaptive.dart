import 'package:flutter/foundation.dart';

/// If set, forces the adaptive function to use the given platform.
/// It is primarily intended for testing purposes.
@internal
TargetPlatform? forceAdaptivePlatform;

/// Returns a platform-specific value for mobile or desktop platforms.
///
/// Automatically selects [mobile] value for Android/iOS, and [desktop] value
/// for all other platforms.
///
/// ## Example
/// ```dart
/// final fontSize = adaptive(
///   mobile: 14.0,
///   desktop: 12.0,
/// );
/// ```
T adaptive<T>({
  required T mobile,
  required T desktop,
}) {
  final platform = forceAdaptivePlatform ?? defaultTargetPlatform;
  if (platform == TargetPlatform.android || platform == TargetPlatform.iOS) {
    return mobile;
  }
  return desktop;
}
