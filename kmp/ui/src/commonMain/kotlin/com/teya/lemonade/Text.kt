package com.teya.lemonade

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.teya.lemonade.core.LemonadeTextStyle

/**
 * Displays styled text using the Lemonade Design System typography tokens.
 *
 * @param text The text to display.
 * @param fontSize Optional font size override. When [TextUnit.Unspecified], the size from [textStyle] is used.
 * @param modifier Modifier to apply to the text layout.
 * @param textStyle The Lemonade typography token to apply. Defaults to the current local text style.
 * @param textAlign The alignment of the text within its container.
 * @param color The text color. When [Color.Unspecified], the color from [textStyle] is used.
 * @param overflow How text overflow is handled.
 * @param maxLines Maximum number of lines to display.
 * @param minLines Minimum number of lines to display.
 * @param autoSize Optional auto-sizing configuration for the text.
 */
@Composable
public fun LemonadeUi.Text(
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    modifier: Modifier = Modifier,
    textStyle: LemonadeTextStyle = LocalTextStyles.current,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = LocalColors.current.content.contentPrimary,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    autoSize: TextAutoSize? = null,
) {
    val finalText = if (textStyle == LocalTypographies.current.bodyXSmallOverline) {
        text.uppercase()
    } else {
        text
    }

    val style = textStyle.textStyle
    val finalFontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize
    val finalColor = if (color != Color.Unspecified) color else style.color

    BasicText(
        text = finalText,
        modifier = modifier,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        autoSize = autoSize,
        style = textStyle.textStyle.copy(
            fontSize = finalFontSize,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
            color = finalColor,
            textAlign = textAlign,
        ),
    )
}

/**
 * Displays styled text using a raw Compose [TextStyle].
 *
 * Use this overload when you need full control over the text style rather than
 * using Lemonade typography tokens.
 *
 * @param text The text to display.
 * @param modifier Modifier to apply to the text layout.
 * @param textStyle The Compose [TextStyle] to apply.
 * @param overflow How text overflow is handled.
 * @param maxLines Maximum number of lines to display.
 * @param minLines Minimum number of lines to display.
 * @param autoSize Optional auto-sizing configuration for the text.
 */
@Composable
public fun LemonadeUi.Text(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    autoSize: TextAutoSize? = null,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        autoSize = autoSize,
    )
}

/**
 * Defines the supported inline markdown markers for Lemonade text formatting.
 *
 * Each marker wraps text with its [key] to apply a specific style:
 * - `**text**` for [SemiBold]
 * - `***text***` for [Bold]
 * - `__text__` for [Underline]
 * - `___text___` for [StrikeThrough]
 * - `~~text~~` for [Italic]
 *
 * Use [String.toLemonadeMarkdown] to parse a string containing these markers
 * into an [AnnotatedString] with the corresponding styles applied.
 */
public sealed class LemonadeMarkdown(
    public val key: String,
) {
    public data object SemiBold : LemonadeMarkdown(key = "**")

    public data object Bold : LemonadeMarkdown(key = "***")

    public data object Underline : LemonadeMarkdown(key = "__")

    public data object StrikeThrough : LemonadeMarkdown(key = "___")

    public data object Italic : LemonadeMarkdown(key = "~~")

    internal companion object {
        internal val values = listOf(
            Bold,
            StrikeThrough,
            SemiBold,
            Underline,
            Italic,
        )
    }
}

/**
 * Parses this string for [LemonadeMarkdown] markers and returns an [AnnotatedString]
 * with the markers removed and the corresponding [SpanStyle] applied to the enclosed text.
 *
 * Markers are matched in pairs — an unpaired marker is left as plain text.
 * Longer markers are matched first to avoid partial matches (e.g. `***` before `**`).
 *
 * Example:
 * ```
 * "Hello **world**".toLemonadeMarkdown()
 * // Returns "Hello world" with semi-bold applied to "world"
 * ```
 */
public fun String.toLemonadeMarkdown(): AnnotatedString {
    val source = this
    val markerPositions = mutableSetOf<Int>()
    val spanStarts = mutableListOf<Int>()
    val spanEnds = mutableListOf<Int>()
    val spanStyles = mutableListOf<SpanStyle>()

    val sortedMarkdowns = LemonadeMarkdown.values
        .sortedByDescending { markdown ->
            markdown.key.length
        }

    for (markdown in sortedMarkdowns) {
        val key = markdown.key
        var searchFrom = 0

        while (searchFrom < source.length) {
            val openIdx = source.indexOf(
                string = key,
                startIndex = searchFrom,
            )
            if (openIdx == -1) break

            val openRange = openIdx until (openIdx + key.length)
            if (openRange.any { index -> index in markerPositions }) {
                searchFrom = openIdx + 1
                continue
            }

            val closeIdx = source.indexOf(
                string = key,
                startIndex = openIdx + key.length,
            )
            if (closeIdx == -1) break

            val closeRange = closeIdx until (closeIdx + key.length)
            if (closeRange.any { index -> index in markerPositions }) {
                searchFrom = openIdx + 1
                continue
            }

            openRange.forEach { index -> markerPositions.add(index) }
            closeRange.forEach { index -> markerPositions.add(index) }

            spanStarts.add(openIdx + key.length)
            spanEnds.add(closeIdx)
            spanStyles.add(markdown.toSpanStyle())

            searchFrom = closeIdx + key.length
        }
    }

    return buildAnnotatedString {
        val indexMapping = IntArray(size = source.length)
        var newIndex = 0

        for (i in source.indices) {
            indexMapping[i] = newIndex
            if (i !in markerPositions) {
                append(source[i])
                newIndex++
            }
        }

        for (i in spanStarts.indices) {
            addStyle(
                style = spanStyles[i],
                start = indexMapping[spanStarts[i]],
                end = indexMapping[spanEnds[i]],
            )
        }
    }
}

private fun LemonadeMarkdown.toSpanStyle(): SpanStyle {
    val markdown = this
    return when (markdown) {
        is LemonadeMarkdown.SemiBold -> SpanStyle(
            fontWeight = FontWeight.SemiBold,
        )
        is LemonadeMarkdown.Bold -> SpanStyle(
            fontWeight = FontWeight.Bold,
        )
        is LemonadeMarkdown.Underline -> SpanStyle(
            textDecoration = TextDecoration.Underline,
        )
        is LemonadeMarkdown.Italic -> SpanStyle(
            fontStyle = FontStyle.Italic,
        )
        is LemonadeMarkdown.StrikeThrough -> SpanStyle(
            textDecoration = TextDecoration.LineThrough,
        )
    }
}
