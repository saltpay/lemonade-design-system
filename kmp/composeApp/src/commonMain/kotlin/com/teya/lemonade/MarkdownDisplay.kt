package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

private val syntaxReference: List<String> = listOf(
    "**text** = Semi-Bold",
    "***text*** = Bold",
    "__text__ = Underline",
    "___text___ = Strikethrough",
    "~~text~~ = Italic",
    "{color}text{/color} = Color (e.g. critical, positive, info, caution, brand)",
)

@Composable
internal fun MarkdownDisplay() {
    var input by remember {
        mutableStateOf(
            "Hello **semi-bold** and ***bold*** with __underline__" +
                " and ___strikethrough___ or ~~italic~~" +
                " plus {critical}critical{/critical}" +
                " and {positive}positive{/positive}",
        )
    }

    SampleScreenDisplayLazyColumn(title = "Markdown") {
        item(key = "input") {
            LemonadeUi.TextField(
                input = input,
                onInputChanged = { value -> input = value },
                label = "Markdown input",
                placeholderText = "Type markdown here...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LemonadeTheme.spaces.spacing600),
            )
        }

        item(key = "preview") {
            // toLemonadeMarkdown() is itself @Composable (it resolves theme colors), so it cannot be
            // wrapped in remember. Keeping the preview in its own lazy item is what bounds the
            // re-parse: it now only runs when `input` changes, not when anything else on the screen does.
            MarkdownSection(title = "Preview") {
                LemonadeUi.Text(
                    text = input.toLemonadeMarkdown(),
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                )
            }
        }

        item(key = "syntax-reference") {
            MarkdownSection(title = "Syntax Reference") {
                syntaxReference.forEach { line ->
                    LemonadeUi.Text(
                        text = line,
                        textStyle = LemonadeTheme.typography.bodySmallRegular,
                        color = LemonadeTheme.colors.content.contentTertiary,
                    )
                }
            }
        }
    }
}

@Composable
private fun MarkdownSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
        modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing600),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
