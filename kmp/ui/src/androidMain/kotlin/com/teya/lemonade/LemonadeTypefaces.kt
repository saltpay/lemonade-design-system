package com.teya.lemonade

import android.content.Context
import android.graphics.Typeface
import com.teya.lemonade.core.LemonadeTextStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.getFontResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import java.io.File
import java.util.concurrent.ConcurrentHashMap

private val typefaceCache = ConcurrentHashMap<FontResource, Typeface>()

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
public fun LemonadeTextStyle.typeface(context: Context): Typeface {
    val fontResource = fontWeight.toFontResource()
    return typefaceCache.getOrPut(fontResource) {
        loadTypeface(context, fontResource)
    }
}

private fun Int.toFontResource(): FontResource = when (this) {
    500 -> LemonadeRes.font.Figtree_Medium
    600, 700 -> LemonadeRes.font.Figtree_SemiBold
    else -> LemonadeRes.font.Figtree_Regular
}

private fun loadTypeface(context: Context, resource: FontResource): Typeface {
    val environment = getSystemResourceEnvironment()
    val bytes = runBlocking(Dispatchers.IO) { getFontResourceBytes(environment, resource) }
    val tempFile = File.createTempFile("lemonade_font", ".ttf", context.cacheDir)
    try {
        tempFile.writeBytes(bytes)
        return Typeface.createFromFile(tempFile)
    } finally {
        tempFile.delete()
    }
}
