package com.teya.lemonade

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun HomeDisplay(onNavigate: (Displays) -> Unit) {
    SampleScreenDisplayLazyColumn(
        title = "Lemonade Design System",
    ) {
        item {
            DisplayRegistry.homeItems.forEach { display ->
                LemonadeUi.Card(
                    header = CardHeaderConfig(display.title),
                    modifier = Modifier.padding(top = LemonadeTheme.spaces.spacing400),
                ) {
                    display.items.forEachIndexed { index, item ->
                        LemonadeUi.ActionListItem(
                            label = item.label,
                            onItemClicked = { onNavigate(item) },
                            showDivider = display.items.lastIndex != index,
                            trailingSlot = {
                                LemonadeUi.Icon(
                                    icon = LemonadeIcons.ChevronRight,
                                    size = LemonadeAssetSize.Medium,
                                    contentDescription = "Navigation indicator",
                                    tint = LemonadeTheme.colors.content.contentTertiary,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

internal object DisplayRegistry {
    val homeItems: List<DisplayData> = listOfNotNull(
        DisplayData(
            title = "Foundations",
            items = listOf(
                Displays.Colors,
                Displays.Spacing,
                Displays.Radius,
                Displays.Shadows,
                Displays.Sizes,
                Displays.Opacity,
                Displays.BorderWidth,
            ),
        ),
        DisplayData(
            title = "Assets",
            items = listOf(
                Displays.Icons,
                Displays.CountryFlag,
                Displays.BrandLogo,
            ),
        ),
        DisplayData(
            title = "Typography",
            items = listOf(
                Displays.Text,
            ),
        ),
        DisplayData(
            title = "Form Controls",
            items = listOf(
                Displays.Button,
                Displays.IconButton,
                Displays.Checkbox,
                Displays.RadioButton,
                Displays.Switch,
            ),
        ),
        DisplayData(
            title = "Input Fields",
            items = listOf(
                Displays.TextField,
                Displays.SearchField,
            ),
        ),
        DisplayData(
            title = "Display Components",
            items = listOf(
                Displays.Tag,
                Displays.Badge,
                Displays.SymbolContainer,
                Displays.Card,
                Displays.Spinner,
                Displays.Snackbar,
                Displays.Divider,
            ),
        ),
        DisplayData(
            title = "Selection & Lists",
            items = listOf(
                Displays.Chip,
                Displays.SelectionListItem,
                Displays.ResourceListItem,
                Displays.SegmentedControl,
                Displays.ActionListItem,
            ),
        ),
        DisplayData(
            title = "Navigation",
            items = listOf(
                Displays.Link,
                Displays.Tile,
            ),
        ),
        DisplayData(
            title = "Platform Specific",
            items = platformSpecificEntries,
        ).takeIf { platformSpecificEntries.isNotEmpty() },
    )

    data class DisplayData(
        val title: String,
        val items: List<Displays>,
    )
}
