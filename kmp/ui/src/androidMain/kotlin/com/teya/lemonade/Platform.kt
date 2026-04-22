package com.teya.lemonade

import android.os.Build

internal actual fun supportsImeInsets(): Boolean = Build.VERSION.SDK_INT >= 29
