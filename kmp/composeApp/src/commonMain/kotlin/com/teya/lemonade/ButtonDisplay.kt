package com.teya.lemonade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun ButtonDisplay() {
    SampleScreenDisplayColumn("Button", itemsSpacing = LemonadeTheme.spaces.spacing600) {

        LemonadeButtonVariant.entries.forEach { variant ->
            ButtonSection(title = variant.toString()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = LemonadeTheme.spaces.spacing300,
                    ),
                ) {
                    ButtonCard() {
                        LemonadeButtonSize.entries.forEach { size ->
                            @OptIn(ExperimentalLemonadeComponent::class)
                            LemonadeUi.Button(
                                label = size.toString(),
                                onClick = {},
                                variant = variant,
                                size = size,
                            )
                        }
                    }


                    ButtonCard() {
                        listOf("leading", "trailing").forEach { position ->
                            val leadingIcon = getButtonLeadingIcon(variant)

                            @OptIn(ExperimentalLemonadeComponent::class)
                            LemonadeUi.Button(
                                label = position.replaceFirstChar { it.uppercase() },
                                onClick = {},
                                variant = variant,
                                size = LemonadeButtonSize.Medium,
                                leadingIcon = if (position == "leading") leadingIcon else null,
                                trailingIcon = if (position == "trailing") LemonadeIcons.ChevronRight else null,
                            )
                        }
                    }

                    ButtonCard() {
                        @OptIn(ExperimentalLemonadeComponent::class)
                        LemonadeUi.Button(
                            label = "Loading",
                            onClick = {},
                            variant = variant,
                            size = LemonadeButtonSize.Medium,
                            loading = true
                        )

                        @OptIn(ExperimentalLemonadeComponent::class)
                        LemonadeUi.Button(
                            label = "Disabled",
                            onClick = {},
                            variant = variant,
                            size = LemonadeButtonSize.Medium,
                            enabled = false,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ButtonCard(
    content: @Composable () -> Unit,
) {
    LemonadeUi.Card(
        contentPadding = LemonadeCardPadding.Medium,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = LemonadeTheme.spaces.spacing300,
                alignment = Alignment.CenterHorizontally,
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            content()
        }
    }
}

@Composable
private fun ButtonSection(
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

private fun getButtonLeadingIcon(
    variant: LemonadeButtonVariant = LemonadeButtonVariant.Primary,
): LemonadeIcons {
    return when (variant) {
        LemonadeButtonVariant.Primary,
        LemonadeButtonVariant.Secondary,
        LemonadeButtonVariant.Neutral,
        LemonadeButtonVariant.Special -> LemonadeIcons.Heart

        LemonadeButtonVariant.CriticalSubtle,
        LemonadeButtonVariant.CriticalSolid -> LemonadeIcons.Trash
    }
}
