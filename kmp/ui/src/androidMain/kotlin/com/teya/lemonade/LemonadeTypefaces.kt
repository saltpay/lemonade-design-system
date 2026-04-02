@file:OptIn(InternalResourceApi::class)
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.teya.lemonade

import android.content.Context
import android.graphics.Typeface
import com.teya.lemonade.core.LemonadeTextStyle
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.getResourceItemByEnvironment
import org.jetbrains.compose.resources.getSystemResourceEnvironment

/**
 * Returns the Android [Typeface] matching this text style's font weight.
 * Typefaces are cached after first load.
 *
 * This is the Android equivalent of the iOS `LemonadeTextStyle.uiFont` property,
 * intended for native SDK integrations that require [android.graphics.Typeface]
 * outside of Compose context.
 *
 * For Compose usage, prefer [lemonadeFontFamily] and [textStyle] instead.
 */
public fun LemonadeTextStyle.androidTypeface(context: Context): Typeface {
    val fontResource = fontWeight.toFontResource()
    val environment = getSystemResourceEnvironment()
    val resourceItem = fontResource.getResourceItemByEnvironment(environment)
    return Typeface.createFromAsset(context.assets, resourceItem.path)
}

private fun Int.toFontResource(): FontResource =
    when (this) {
        500 -> LemonadeRes.font.Figtree_Medium
        600, 700 -> LemonadeRes.font.Figtree_SemiBold
        else -> LemonadeRes.font.Figtree_Regular
    }
