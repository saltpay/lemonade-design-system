package com.teya.lemonade

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.teya.lemonade.core.LemonadeTextStyle

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
 * Rich text component that parses inline tags and applies the corresponding [SpanStyle]
 * from the [tags] map. Untagged text inherits the base [textStyle] and [color].
 *
 * Tags use the format `<name>content</name>`. This is localization-friendly because
 * translators can reorder the tags freely within the translated string.
 *
 * **Note:** Overline auto-uppercasing is not applied in this overload. If using the overline
 * text style, the caller must uppercase the string content before passing it.
 *
 * ## Usage
 * ```kotlin
 * // The string can come from string resources — tag order is language-dependent
 * LemonadeUi.Text(
 *     text = "It should arrive by <bold>15 June</bold>",
 *     textStyle = LemonadeTheme.typography.bodyMediumRegular,
 *     tags = mapOf("bold" to LemonadeTheme.typography.bodyMediumSemiBold.spanStyle),
 * )
 * ```
 */
@Composable
public fun LemonadeUi.Text(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: LemonadeTextStyle = LocalTextStyles.current,
    tags: Map<String, SpanStyle>,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = LocalColors.current.content.contentPrimary,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    val baseStyle = textStyle.textStyle
    val finalColor = if (color != Color.Unspecified) color else baseStyle.color

    val annotatedString = buildAnnotatedString {
        val segments = parseTaggedText(text)
        for ((content, tag) in segments) {
            if (tag != null) {
                val spanStyle = tags[tag]
                if (spanStyle != null) {
                    withStyle(spanStyle) { append(content) }
                } else {
                    append(content)
                }
            } else {
                append(content)
            }
        }
    }

    BasicText(
        text = annotatedString,
        modifier = modifier,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = baseStyle.copy(
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
            color = finalColor,
            textAlign = textAlign,
        ),
    )
}

private val tagPattern = Regex("<(\\w+)>(.*?)</\\1>")

private fun parseTaggedText(text: String): List<Pair<String, String?>> {
    val segments = mutableListOf<Pair<String, String?>>()
    var lastIndex = 0
    for (match in tagPattern.findAll(text)) {
        if (match.range.first > lastIndex) {
            segments.add(Pair(text.substring(lastIndex, match.range.first), null))
        }
        segments.add(Pair(match.groupValues[2], match.groupValues[1]))
        lastIndex = match.range.last + 1
    }
    if (lastIndex < text.length) {
        segments.add(Pair(text.substring(lastIndex), null))
    }
    return segments
}
