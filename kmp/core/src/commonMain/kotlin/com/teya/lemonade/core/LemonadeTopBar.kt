@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

public enum class TopBarAction {
    Back,
    BackBoxed,
    Close,
    CloseBoxed,
    ;

    public val hasBackground: Boolean
        get() = this == BackBoxed || this == CloseBoxed
}
