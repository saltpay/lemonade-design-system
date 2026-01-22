package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice

@Composable
internal fun SymbolContainerDisplay() {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LemonadeTheme.spaces.spacing400),
    ) {
        // Sizes (Icon)
        SymbolContainerSection(title = "Sizes (Icon)") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        size = SymbolContainerSize.XSmall
                    )
                    LemonadeUi.Text(
                        text = "XSmall",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        size = SymbolContainerSize.Small
                    )
                    LemonadeUi.Text(
                        text = "Small",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        size = SymbolContainerSize.Medium
                    )
                    LemonadeUi.Text(
                        text = "Medium",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        size = SymbolContainerSize.Large
                    )
                    LemonadeUi.Text(
                        text = "Large",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                ) {
                    LemonadeUi.SymbolContainer(
                        icon = LemonadeIcons.Heart,
                        contentDescription = null,
                        size = SymbolContainerSize.XLarge
                    )
                    LemonadeUi.Text(
                        text = "XLarge",
                        textStyle = LemonadeTheme.typography.bodySmallRegular
                    )
                }
            }
        }

        // Voices
        SymbolContainerSection(title = "Voices") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Heart,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Neutral,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Neutral",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.CircleX,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Critical,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Critical",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.TriangleAlert,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Warning,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Warning",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.CircleInfo,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Info,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Info",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.CircleCheck,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Positive,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Positive",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Star,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Brand,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Brand",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Star,
                            contentDescription = null,
                            voice = SymbolContainerVoice.BrandSubtle,
                            size = SymbolContainerSize.Medium
                        )
                        LemonadeUi.Text(
                            text = "Brand Subtle",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }
                }
            }
        }

        // Text Variant
        SymbolContainerSection(title = "Text Variant") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                LemonadeUi.SymbolContainer(text = "A", voice = SymbolContainerVoice.Neutral, size = SymbolContainerSize.Small)
                LemonadeUi.SymbolContainer(text = "B", voice = SymbolContainerVoice.Info, size = SymbolContainerSize.Medium)
                LemonadeUi.SymbolContainer(text = "C", voice = SymbolContainerVoice.Positive, size = SymbolContainerSize.Large)
                LemonadeUi.SymbolContainer(text = "1", voice = SymbolContainerVoice.Critical, size = SymbolContainerSize.Medium)
                LemonadeUi.SymbolContainer(text = "99", voice = SymbolContainerVoice.Warning, size = SymbolContainerSize.Large)
            }
        }

        // Use Cases
        SymbolContainerSection(title = "Use Cases") {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                // User avatar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(LemonadeTheme.radius.radius300))
                        .background(LemonadeTheme.colors.background.bgElevated)
                        .padding(LemonadeTheme.spaces.spacing400),
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing300),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LemonadeUi.SymbolContainer(text = "JD", voice = SymbolContainerVoice.Brand, size = SymbolContainerSize.Large)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing100)
                    ) {
                        LemonadeUi.Text(
                            text = "John Doe",
                            textStyle = LemonadeTheme.typography.headingXSmall
                        )
                        LemonadeUi.Text(
                            text = "john@example.com",
                            textStyle = LemonadeTheme.typography.bodySmallRegular,
                            color = LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Status indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing600)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.CircleCheck,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Positive,
                            size = SymbolContainerSize.Large
                        )
                        LemonadeUi.Text(
                            text = "Completed",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Clock,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Warning,
                            size = SymbolContainerSize.Large
                        )
                        LemonadeUi.Text(
                            text = "Pending",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing200)
                    ) {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.CircleX,
                            contentDescription = null,
                            voice = SymbolContainerVoice.Critical,
                            size = SymbolContainerSize.Large
                        )
                        LemonadeUi.Text(
                            text = "Failed",
                            textStyle = LemonadeTheme.typography.bodySmallRegular
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SymbolContainerSection(
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
