package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
internal fun MarkdownDisplay() {
    var input by remember {
        mutableStateOf(
            "Hello **semi-bold** and ***bold*** with __underline__ and ___strikethrough___ or ~~italic~~ plus {critical}critical{/critical} and {positive}positive{/positive}",
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        LemonadeUi.Text(
            text = "Markdown",
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )

        LemonadeUi.TextField(
            input = input,
            onInputChanged = { value -> input = value },
            label = "Markdown input",
            placeholderText = "Type markdown here...",
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
        ) {
            LemonadeUi.Text(
                text = "Preview",
                textStyle = LemonadeTheme.typography.headingXSmall,
                color = LemonadeTheme.colors.content.contentSecondary,
            )

            LemonadeUi.Text(
                text = input.toLemonadeMarkdown(),
                textStyle = LemonadeTheme.typography.bodyMediumRegular,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
        ) {
            LemonadeUi.Text(
                text = "Syntax Reference",
                textStyle = LemonadeTheme.typography.headingXSmall,
                color = LemonadeTheme.colors.content.contentSecondary,
            )
            LemonadeUi.Text(
                text = "**text** = Semi-Bold",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentTertiary,
            )
            LemonadeUi.Text(
                text = "***text*** = Bold",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentTertiary,
            )
            LemonadeUi.Text(
                text = "__text__ = Underline",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentTertiary,
            )
            LemonadeUi.Text(
                text = "___text___ = Strikethrough",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentTertiary,
            )
            LemonadeUi.Text(
                text = "~~text~~ = Italic",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentTertiary,
            )
            LemonadeUi.Text(
                text = "{color}text{/color} = Color (e.g. critical, positive, info, caution, brand)",
                textStyle = LemonadeTheme.typography.bodySmallRegular,
                color = LemonadeTheme.colors.content.contentTertiary,
            )
        }
    }
}
