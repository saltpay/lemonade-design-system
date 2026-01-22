package com.teya.lemonade

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
                trim = LineHeightStyle.Trim.None
            ),
            color = finalColor,
            textAlign = textAlign,
        )
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