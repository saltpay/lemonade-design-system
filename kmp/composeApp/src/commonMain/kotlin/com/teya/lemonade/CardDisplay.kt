package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeCardBackground
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.TagVoice

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
        // Basic Card
        CardSection(title = "Basic Card") {
            LemonadeUi.Card(contentPadding = LemonadeCardPadding.Medium) {
                LemonadeUi.Text(
                    text = "This is a basic card with medium padding.",
                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                )
            }
        }

        // Padding Variants
        CardSection(title = "Padding Variants") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeUi.Card(contentPadding = LemonadeCardPadding.None) {
                    LemonadeUi.Text(
                        text = "No padding",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular,
                        modifier = Modifier.padding(LemonadeTheme.spaces.spacing400)
                    )
                }

                LemonadeUi.Card(contentPadding = LemonadeCardPadding.XSmall) {
                    LemonadeUi.Text(
                        text = "XSmall padding",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }

                LemonadeUi.Card(contentPadding = LemonadeCardPadding.Small) {
                    LemonadeUi.Text(
                        text = "Small padding",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }

                LemonadeUi.Card(contentPadding = LemonadeCardPadding.Medium) {
                    LemonadeUi.Text(
                        text = "Medium padding",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }
            }
        }

        // Background Variants
        CardSection(title = "Background Variants") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    background = LemonadeCardBackground.Default
                ) {
                    LemonadeUi.Text(
                        text = "Default background",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    background = LemonadeCardBackground.Subtle
                ) {
                    LemonadeUi.Text(
                        text = "Subtle background",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }
            }
        }

        // With Header
        CardSection(title = "With Header") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(title = "Card Title")
                ) {
                    LemonadeUi.Text(
                        text = "Card content goes here. This is an example of a card with a header.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "With Trailing Slot",
                        trailingSlot = {
                            LemonadeUi.Tag(label = "New", voice = TagVoice.Positive)
                        }
                    )
                ) {
                    LemonadeUi.Text(
                        text = "This card has a header with a trailing tag.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }

                LemonadeUi.Card(
                    contentPadding = LemonadeCardPadding.Medium,
                    header = CardHeaderConfig(
                        title = "Actions",
                        trailingSlot = {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.EllipsisVertical,
                                contentDescription = "More options",
                                size = LemonadeAssetSize.Medium,
                            )
                        }
                    )
                ) {
                    LemonadeUi.Text(
                        text = "Card with action icon in header.",
                        textStyle = LemonadeTheme.typography.bodyMediumRegular
                    )
                }
            }
        }

        // Complex Content
        CardSection(title = "Complex Content") {
            LemonadeUi.Card(
                contentPadding = LemonadeCardPadding.Medium,
                header = CardHeaderConfig(
                    title = "Order Summary",
                    trailingSlot = {
                        LemonadeUi.Tag(label = "Confirmed", voice = TagVoice.Positive)
                    }
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LemonadeUi.Text(
                            text = "Subtotal",
                            textStyle = LemonadeTheme.typography.bodyMediumRegular
                        )
                        LemonadeUi.Text(
                            text = "$99.00",
                            textStyle = LemonadeTheme.typography.bodyMediumRegular
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LemonadeUi.Text(
                            text = "Shipping",
                            textStyle = LemonadeTheme.typography.bodyMediumRegular
                        )
                        LemonadeUi.Text(
                            text = "$5.00",
                            textStyle = LemonadeTheme.typography.bodyMediumRegular
                        )
                    }

                    SimpleDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LemonadeUi.Text(
                            text = "Total",
                            textStyle = LemonadeTheme.typography.bodyMediumSemiBold
                        )
                        LemonadeUi.Text(
                            text = "$104.00",
                            textStyle = LemonadeTheme.typography.bodyMediumSemiBold
                        )
                    }
                }
            }
        }

        // Nested Cards
        CardSection(title = "Nested Cards") {
            LemonadeUi.Card(
                contentPadding = LemonadeCardPadding.Medium,
                header = CardHeaderConfig(title = "Payment Methods")
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300)
                ) {
                    LemonadeUi.Card(
                        contentPadding = LemonadeCardPadding.Small,
                        background = LemonadeCardBackground.Subtle
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Card,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                LemonadeUi.Text(
                                    text = "Visa ending in 4242",
                                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                                )
                                LemonadeUi.Text(
                                    text = "Expires 12/25",
                                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                                    color = LemonadeTheme.colors.content.contentSecondary
                                )
                            }
                            LemonadeUi.Tag(label = "Default", voice = TagVoice.Info)
                        }
                    }

                    LemonadeUi.Card(
                        contentPadding = LemonadeCardPadding.Small,
                        background = LemonadeCardBackground.Subtle
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LemonadeUi.Icon(
                                icon = LemonadeIcons.Card,
                                contentDescription = null,
                                size = LemonadeAssetSize.Medium,
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                LemonadeUi.Text(
                                    text = "Mastercard ending in 1234",
                                    textStyle = LemonadeTheme.typography.bodyMediumRegular
                                )
                                LemonadeUi.Text(
                                    text = "Expires 06/24",
                                    textStyle = LemonadeTheme.typography.bodySmallRegular,
                                    color = LemonadeTheme.colors.content.contentSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
    ) {
        LemonadeUi.Text(
            text = title,
            textStyle = LemonadeTheme.typography.headingXSmall,
            color = LemonadeTheme.colors.content.contentSecondary
        )
        content()
    }
}

@Composable
private fun SimpleDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LemonadeTheme.colors.border.borderNeutralLow)
    )
}
