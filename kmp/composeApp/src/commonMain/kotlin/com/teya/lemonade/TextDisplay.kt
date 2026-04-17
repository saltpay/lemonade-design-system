package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.teya.lemonade.core.LemonadeTypography

@Composable
internal fun TextDisplay() {
    val categorizedStyles = remember {
        LemonadeTypography.entries
            .groupBy { it.category() }
            .mapValues { (_, styles) -> styles.sortedByDescending { it.style.fontSize } }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        categorizedStyles.forEach { (category, styles) ->
            TextSection(title = category) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
                ) {
                    styles.forEachIndexed { index, typography ->
                        if (index > 0 && typography.subCategory() != styles[index - 1].subCategory()) {
                            LemonadeUi.HorizontalDivider()
                        }
                        LemonadeUi.Text(
                            text = typography.toDisplayLabel(),
                            textStyle = typography.style,
                        )
                    }
                }
            }
        }

        // Text Colors — not part of the typography enum, kept manual
        TextSection(title = "Colors") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                LemonadeUi.Text(
                    text = "Primary",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentPrimary,
                )
                LemonadeUi.Text(
                    text = "Secondary",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentSecondary,
                )
                LemonadeUi.Text(
                    text = "Tertiary",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentTertiary,
                )
                LemonadeUi.Text(
                    text = "Critical",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentCritical,
                )
                LemonadeUi.Text(
                    text = "Positive",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentPositive,
                )
                LemonadeUi.Text(
                    text = "Info",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    color = LemonadeTheme.colors.content.contentInfo,
                )
            }
        }

        // Overflow — behavioural examples, kept manual
        TextSection(title = "Overflow") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                LemonadeUi.Text(
                    text = "This is a very long text that will be truncated at the end " +
                        "with ellipsis because it exceeds the available width",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                LemonadeUi.Text(
                    text = "This text allows multiple lines but is limited to 2 lines " +
                        "maximum. Lorem ipsum dolor sit amet, consectetur adipiscing " +
                        "elit. Sed do eiusmod tempor incididunt ut labore.",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// Splits the enum name into words by inserting spaces before uppercase letters/digits
// that follow a lowercase letter (e.g. "BodyXLargeRegular" → ["Body", "XLarge", "Regular"]).
private fun LemonadeTypography.labelParts(): List<String> =
    name.replace(Regex("([a-z])([A-Z0-9])"), "$1 $2").split(" ")

private fun LemonadeTypography.toDisplayLabel(): String = labelParts().joinToString(" ")

private fun LemonadeTypography.category(): String = labelParts().first()

// Returns non-null only for styles with weight variants (e.g. Body),
// so callers can insert dividers between size groups.
private fun LemonadeTypography.subCategory(): String? = labelParts().let { if (it.size > 2) it[1] else null }

@Composable
private fun TextSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary,
        )
        content()
    }
}
