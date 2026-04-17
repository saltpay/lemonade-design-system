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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.teya.lemonade.core.LemonadeTypography

@Composable
internal fun TextDisplay() {
    val categorizedStyles = LemonadeTypography.entries
        .groupBy { it.category() }
        .mapValues { (_, styles) -> styles.sortedByDescending { it.style.fontSize } }

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
                    var lastSubCategory: String? = null
                    styles.forEach { typography ->
                        val subCategory = typography.subCategory()
                        if (lastSubCategory != null && subCategory != lastSubCategory) {
                            LemonadeUi.HorizontalDivider()
                        }
                        lastSubCategory = subCategory
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

/**
 * Derives the top-level category from the enum entry name.
 * e.g. "BodyXLargeRegular" → "Body", "DisplayXSmall" → "Display"
 */
private fun LemonadeTypography.category(): String =
    toDisplayLabel().split(" ").first()

/**
 * Derives the size sub-category used to insert dividers within a category.
 * Returns a value only when there are more than two label words (i.e. Body styles),
 * so that dividers are inserted between BodyXLarge/BodyLarge/etc. groups.
 * e.g. "BodyXLargeRegular" → "XLarge", "DisplayXSmall" → null
 */
private fun LemonadeTypography.subCategory(): String? {
    val parts = toDisplayLabel().split(" ")
    return if (parts.size > 2) parts[1] else null
}

/**
 * Converts a camelCase enum name to a human-readable label by inserting a space
 * before any uppercase letter or digit that follows a lowercase letter.
 * e.g. "Display3XLarge" → "Display 3XLarge", "BodyXLargeRegular" → "Body XLarge Regular"
 */
private fun LemonadeTypography.toDisplayLabel(): String =
    name.replace(Regex("([a-z])([A-Z0-9])"), "$1 $2")

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
