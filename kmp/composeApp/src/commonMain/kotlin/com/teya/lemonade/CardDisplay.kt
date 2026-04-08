package com.teya.lemonade

import androidx.compose.foundation.background
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
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeCardBackground
import com.teya.lemonade.core.LemonadeCardHeadingStyle
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.TagVoice

@Suppress("LongMethod")
@Composable
internal fun CardDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .background(LemonadeTheme.colors.background.bgSubtle)
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Background Variants
        CardSection(title = "Backgrounds") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    background = LemonadeCardBackground.Default,
                ) {
                    LemonadeUi.Text(
                        text = "Default",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    background = LemonadeCardBackground.Subtle,
                ) {
                    LemonadeUi.Text(
                        text = "Subtle",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    background = LemonadeCardBackground.Elevated,
                ) {
                    LemonadeUi.Text(
                        text = "Elevated",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }
            }
        }

        // Spacing Variants
        CardSection(title = "Spacing") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeCardPadding.entries.forEach { padding ->
                    LemonadeUi.Card(contentPadding = padding) {
                        LemonadeUi.Text(
                            text = padding.name,
                            textStyle = LemonadeTheme.typography.bodyMediumRegular,
                        )
                    }
                }
            }
        }

        // Heading Styles
        CardSection(title = "Heading Styles") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "Default Heading",
                        trailingSlot = {
                            LemonadeUi.Tag(label = "Tag", voice = TagVoice.Neutral)
                        },
                    ),
                ) {
                    LemonadeUi.Text(
                        text = "Card with default heading style.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "Overline Heading",
                        headingStyle = LemonadeCardHeadingStyle.Overline,
                        trailingSlot = {
                            LemonadeUi.Tag(label = "Tag", voice = TagVoice.Neutral)
                        },
                    ),
                ) {
                    LemonadeUi.Text(
                        text = "Card with overline heading style.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }
            }
        }

        // Header Slots
        CardSection(title = "Header Slots") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400),
            ) {
                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "Leading Icon",
                        leadingSlot = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Store,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                        },
                    ),
                ) {
                    LemonadeUi.Text(
                        text = "Header with leading slot.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "Navigation",
                        showNavigationIndicator = true,
                    ),
                ) {
                    LemonadeUi.Text(
                        text = "Header with navigation indicator.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "All Slots",
                        leadingSlot = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Store,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                        },
                        trailingSlot = {
                            LemonadeUi.Tag(label = "Active", voice = TagVoice.Positive)
                        },
                        showNavigationIndicator = true,
                    ),
                ) {
                    LemonadeUi.Text(
                        text = "Leading, trailing, and navigation combined.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                    )
                }
            }
        }
    }
}

@Composable
private fun CardSection(
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
