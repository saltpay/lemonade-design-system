package com.teya.lemonade

import android.os.Build

// androidx.core emulates the IME inset type from API 23 in windows that resize for the keyboard,
// and it reports correctly on the API 27 PAX terminals this was verified on — in the activity
// window and inside a ModalBottomSheet window alike. Where a window genuinely never reports IME
// insets the value stays 0 and clearFocusOnKeyboardDismiss degrades to a no-op, so the floor is
// the compat minimum rather than 29.
internal actual fun supportsImeInsets(): Boolean = Build.VERSION.SDK_INT >= 23
