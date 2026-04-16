@file:Suppress("MatchingDeclarationName")

package com.teya.lemonade.core

public sealed class TopBarAction {
    public data object Back : TopBarAction()
    public data object Close : TopBarAction()
    public data class Custom(val icon: LemonadeIcons) : TopBarAction()
}
