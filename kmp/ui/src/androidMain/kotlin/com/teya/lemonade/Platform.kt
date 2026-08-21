package com.teya.lemonade

import android.os.Build

// The compat minimum: androidx.core emulates the IME inset type from API 23. Where a window never
// reports it the value stays 0 and clearFocusOnKeyboardDismiss degrades to a no-op.
internal actual fun supportsImeInsets(): Boolean = Build.VERSION.SDK_INT >= 23
