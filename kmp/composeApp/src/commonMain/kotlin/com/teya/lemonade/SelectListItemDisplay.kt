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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.SelectListItemType
import com.teya.lemonade.core.SelectListItemVariant
import com.teya.lemonade.core.SymbolContainerShape
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.TagVoice

private data class OutlinedOption(
    val label: String,
    val icon: LemonadeIcons,
)

private data class TrailingPreset(
    val label: String,
    val voice: TagVoice,
)

private val outlinedOptions: List<OutlinedOption> = listOf(
    OutlinedOption(label = "Option A", icon = LemonadeIcons.Heart),
    OutlinedOption(label = "Option B", icon = LemonadeIcons.Star),
    OutlinedOption(label = "Option C", icon = LemonadeIcons.Sparkles),
    OutlinedOption(label = "Option D", icon = LemonadeIcons.Gift),
)

private val outlinedOptionsFirstThree: List<OutlinedOption> = outlinedOptions.take(n = 3)

private val trailingPresets: List<TrailingPreset> = listOf(
    TrailingPreset(label = "New", voice = TagVoice.Info),
    TrailingPreset(label = "Recommended", voice = TagVoice.Positive),
    TrailingPreset(label = "Popular", voice = TagVoice.Neutral),
)

private fun Set<Int>.toggle(index: Int): Set<Int> {
    return if (contains(element = index)) {
        this - index
    } else {
        this + index
    }
}

@Suppress("LongMethod")
@Composable
internal fun SelectListItemDisplay() {
    var plainSingle by remember { mutableIntStateOf(value = 0) }
    var plainMultiple by remember { mutableStateOf(value = setOf(0)) }
    var plainToggles by remember { mutableStateOf(value = setOf(0)) }
    var outlinedWithLeading by remember { mutableIntStateOf(value = 0) }
    var outlinedWithTrailing by remember { mutableIntStateOf(value = 1) }
    var outlinedLabelOnly by remember { mutableIntStateOf(value = 0) }
    var outlinedWithSupport by remember { mutableIntStateOf(value = 0) }
    var outlinedMultiple by remember { mutableStateOf(value = setOf(0)) }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing600),
        modifier = Modifier
            .background(color = LemonadeTheme.colors.background.bgSubtle)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Plain — Single"),
        ) {
            for (index in 0..2) {
                LemonadeUi.SelectListItem(
                    label = "Option ${index + 1}",
                    type = SelectListItemType.Single,
                    checked = plainSingle == index,
                    onItemClicked = { plainSingle = index },
                    supportText = if (index == 0) "With support text" else null,
                    showDivider = true,
                )
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Plain — Multiple"),
        ) {
            for (index in 0..2) {
                LemonadeUi.SelectListItem(
                    label = "Item ${index + 1}",
                    type = SelectListItemType.Multiple,
                    checked = plainMultiple.contains(index),
                    onItemClicked = { plainMultiple = plainMultiple.toggle(index = index) },
                )
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Plain — Toggle"),
        ) {
            for (index in 0..2) {
                LemonadeUi.SelectListItem(
                    label = "Setting ${index + 1}",
                    type = SelectListItemType.Toggle,
                    checked = plainToggles.contains(index),
                    onItemClicked = { plainToggles = plainToggles.toggle(index = index) },
                )
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Plain — With leading icon"),
        ) {
            LemonadeUi.SelectListItem(
                label = "With icon",
                type = SelectListItemType.Single,
                checked = true,
                onItemClicked = { /* Nothing */ },
                supportText = "Leading icon example",
                leadingSlot = {
                    LemonadeUi.Icon(
                        icon = LemonadeIcons.Star,
                        contentDescription = null,
                        size = LemonadeAssetSize.Medium,
                    )
                },
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Plain — Disabled"),
        ) {
            LemonadeUi.SelectListItem(
                label = "Disabled option",
                type = SelectListItemType.Single,
                checked = false,
                onItemClicked = { /* Nothing */ },
                enabled = false,
            )

            LemonadeUi.SelectListItem(
                label = "Disabled toggle",
                type = SelectListItemType.Toggle,
                checked = true,
                onItemClicked = { /* Nothing */ },
                enabled = false,
            )
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Outlined — Leading icon only"),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                outlinedOptions.forEachIndexed { index, option ->
                    val isChecked = outlinedWithLeading == index
                    LemonadeUi.SelectListItem(
                        label = option.label,
                        type = SelectListItemType.Single,
                        variant = SelectListItemVariant.Outlined,
                        checked = isChecked,
                        onItemClicked = { outlinedWithLeading = index },
                        leadingSlot = {
                            LemonadeUi.SymbolContainer(
                                icon = option.icon,
                                contentDescription = null,
                                size = SymbolContainerSize.Large,
                                shape = SymbolContainerShape.Rounded,
                                voice = if (isChecked) {
                                    SymbolContainerVoice.Positive
                                } else {
                                    SymbolContainerVoice.Neutral
                                },
                            )
                        },
                    )
                }
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Outlined — Leading + trailing tag"),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                outlinedOptionsFirstThree.forEachIndexed { index, option ->
                    val isChecked = outlinedWithTrailing == index
                    val preset = trailingPresets[index]
                    LemonadeUi.SelectListItem(
                        label = option.label,
                        type = SelectListItemType.Single,
                        variant = SelectListItemVariant.Outlined,
                        checked = isChecked,
                        onItemClicked = { outlinedWithTrailing = index },
                        leadingSlot = {
                            LemonadeUi.SymbolContainer(
                                icon = option.icon,
                                contentDescription = null,
                                size = SymbolContainerSize.Large,
                                shape = SymbolContainerShape.Rounded,
                                voice = if (isChecked) {
                                    SymbolContainerVoice.Positive
                                } else {
                                    SymbolContainerVoice.Neutral
                                },
                            )
                        },
                        trailingSlot = {
                            LemonadeUi.Tag(
                                label = preset.label,
                                voice = preset.voice,
                            )
                        },
                    )
                }
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Outlined — Label only (no leading, no trailing)"),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                for (index in 0..2) {
                    LemonadeUi.SelectListItem(
                        label = "Option ${index + 1}",
                        type = SelectListItemType.Single,
                        variant = SelectListItemVariant.Outlined,
                        checked = outlinedLabelOnly == index,
                        onItemClicked = { outlinedLabelOnly = index },
                    )
                }
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Outlined — With support text"),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                outlinedOptionsFirstThree.forEachIndexed { index, option ->
                    LemonadeUi.SelectListItem(
                        label = option.label,
                        type = SelectListItemType.Single,
                        variant = SelectListItemVariant.Outlined,
                        checked = outlinedWithSupport == index,
                        onItemClicked = { outlinedWithSupport = index },
                        supportText = "Short description for ${option.label.lowercase()}",
                        leadingSlot = {
                            LemonadeUi.SymbolContainer(
                                icon = option.icon,
                                contentDescription = null,
                                size = SymbolContainerSize.Large,
                                shape = SymbolContainerShape.Rounded,
                                voice = SymbolContainerVoice.Neutral,
                            )
                        },
                    )
                }
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Outlined — Multiple"),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                outlinedOptions.forEachIndexed { index, option ->
                    val isChecked = outlinedMultiple.contains(index)
                    LemonadeUi.SelectListItem(
                        label = option.label,
                        type = SelectListItemType.Multiple,
                        variant = SelectListItemVariant.Outlined,
                        checked = isChecked,
                        onItemClicked = { outlinedMultiple = outlinedMultiple.toggle(index = index) },
                        supportText = "Tap to toggle".takeIf { index == 0 },
                        leadingSlot = {
                            LemonadeUi.SymbolContainer(
                                icon = option.icon,
                                contentDescription = null,
                                size = SymbolContainerSize.Large,
                                shape = SymbolContainerShape.Rounded,
                                voice = if (isChecked) {
                                    SymbolContainerVoice.Positive
                                } else {
                                    SymbolContainerVoice.Neutral
                                },
                            )
                        },
                    )
                }
            }
        }

        LemonadeUi.Card(
            header = CardHeaderConfig(title = "Outlined — Disabled states"),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            ) {
                LemonadeUi.SelectListItem(
                    label = "Disabled, no leading",
                    type = SelectListItemType.Single,
                    variant = SelectListItemVariant.Outlined,
                    checked = false,
                    enabled = false,
                    onItemClicked = { /* Nothing */ },
                )

                LemonadeUi.SelectListItem(
                    label = "Disabled, with leading",
                    type = SelectListItemType.Single,
                    variant = SelectListItemVariant.Outlined,
                    checked = false,
                    enabled = false,
                    onItemClicked = { /* Nothing */ },
                    leadingSlot = {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Padlock,
                            contentDescription = null,
                            size = SymbolContainerSize.Large,
                            shape = SymbolContainerShape.Rounded,
                            voice = SymbolContainerVoice.Neutral,
                        )
                    },
                )

                LemonadeUi.SelectListItem(
                    label = "Disabled, with trailing tag",
                    type = SelectListItemType.Single,
                    variant = SelectListItemVariant.Outlined,
                    checked = false,
                    enabled = false,
                    onItemClicked = { /* Nothing */ },
                    leadingSlot = {
                        LemonadeUi.SymbolContainer(
                            icon = LemonadeIcons.Bell,
                            contentDescription = null,
                            size = SymbolContainerSize.Large,
                            shape = SymbolContainerShape.Rounded,
                            voice = SymbolContainerVoice.Neutral,
                        )
                    },
                    trailingSlot = {
                        LemonadeUi.Tag(
                            label = "Coming Soon",
                            voice = TagVoice.Neutral,
                        )
                    },
                )
            }
        }
    }
}
