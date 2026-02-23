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

@Suppress("LongMethod")
@Composable
internal fun TextDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Display Styles
        TextSection(title = "Display") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Text(
                    text = "Display Large",
                    textStyle = LemonadeTheme.typography.displayLarge,
                )
                LemonadeUi.Text(
                    text = "Display Medium",
                    textStyle = LemonadeTheme.typography.displayMedium,
                )
                LemonadeUi.Text(
                    text = "Display Small",
                    textStyle = LemonadeTheme.typography.displaySmall,
                )
            }
        }

        // Heading Styles
        TextSection(title = "Heading") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                LemonadeUi.Text(
                    text = "Heading XLarge",
                    textStyle = LemonadeTheme.typography.headingXLarge,
                )
                LemonadeUi.Text(
                    text = "Heading Large",
                    textStyle = LemonadeTheme.typography.headingLarge,
                )
                LemonadeUi.Text(
                    text = "Heading Medium",
                    textStyle = LemonadeTheme.typography.headingMedium,
                )
                LemonadeUi.Text(
                    text = "Heading Small",
                    textStyle = LemonadeTheme.typography.headingSmall,
                )
                LemonadeUi.Text(
                    text = "Heading XSmall",
                    textStyle = LemonadeTheme.typography.headingXSmall,
                )
                LemonadeUi.Text(
                    text = "Heading XXSmall",
                    textStyle = LemonadeTheme.typography.headingXXSmall,
                )
            }
        }

        // Body Styles
        TextSection(title = "Body") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
            ) {
                // XLarge
                LemonadeUi.Text(
                    text = "Body XLarge Regular",
                    textStyle = LemonadeTheme.typography.bodyXLargeRegular,
                )
                LemonadeUi.Text(
                    text = "Body XLarge Medium",
                    textStyle = LemonadeTheme.typography.bodyXLargeMedium,
                )
                LemonadeUi.Text(
                    text = "Body XLarge SemiBold",
                    textStyle = LemonadeTheme.typography.bodyXLargeSemiBold,
                )

                LemonadeUi.HorizontalDivider()

                // Large
                LemonadeUi.Text(
                    text = "Body Large Regular",
                    textStyle = LemonadeTheme.typography.bodyLargeRegular,
                )
                LemonadeUi.Text(
                    text = "Body Large Medium",
                    textStyle = LemonadeTheme.typography.bodyLargeMedium,
                )
                LemonadeUi.Text(
                    text = "Body Large SemiBold",
                    textStyle = LemonadeTheme.typography.bodyLargeSemiBold,
                )

                LemonadeUi.HorizontalDivider()

                // Medium
                LemonadeUi.Text(
                    text = "Body Medium Regular",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular,
                )
                LemonadeUi.Text(
                    text = "Body Medium Medium",
                    textStyle = LemonadeTheme.typography.bodyMediumMedium,
                )
                LemonadeUi.Text(
                    text = "Body Medium SemiBold",
                    textStyle = LemonadeTheme.typography.bodyMediumSemiBold,
                )

                LemonadeUi.HorizontalDivider()

                // Small
                LemonadeUi.Text(
                    text = "Body Small Regular",
                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                )
                LemonadeUi.Text(
                    text = "Body Small Medium",
                    textStyle = LemonadeTheme.typography.bodySmallMedium,
                )
                LemonadeUi.Text(
                    text = "Body Small SemiBold",
                    textStyle = LemonadeTheme.typography.bodySmallSemiBold,
                )

                LemonadeUi.HorizontalDivider()

                // XSmall
                LemonadeUi.Text(
                    text = "Body XSmall Regular",
                    textStyle = LemonadeTheme.typography.bodyXSmallRegular,
                )
                LemonadeUi.Text(
                    text = "Body XSmall Medium",
                    textStyle = LemonadeTheme.typography.bodyXSmallMedium,
                )
                LemonadeUi.Text(
                    text = "Body XSmall SemiBold",
                    textStyle = LemonadeTheme.typography.bodyXSmallSemiBold,
                )
            }
        }

        // Text Colors
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

        // Overflow
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
