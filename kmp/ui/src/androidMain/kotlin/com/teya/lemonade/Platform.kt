package com.teya.lemonade

import android.os.Build

internal actual fun supportsImeInsets(): Boolean {
    return Build.VERSION.SDK_INT >= 30
}
