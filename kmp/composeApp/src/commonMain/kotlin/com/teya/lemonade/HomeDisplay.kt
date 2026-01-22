package com.teya.lemonade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons

@Composable
internal fun HomeDisplay(
    onNavigate: (Display) -> Unit
) {
    SampleScreenDisplayLazyColumn(
        title = "Lemonade Design System",
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing400)
            ) {
                DisplayRegistry.homeItems.forEach { display ->
                    Column(
                        modifier = Modifier
                            .background(
                                LemonadeTheme.colors.background.bgDefault,
                                shape = RoundedCornerShape(LemonadeTheme.radius.radius400)
                            )
                    ) {
                        LemonadeUi.Text(
                            text = display.title,
                            modifier = Modifier.padding(
                                top = LemonadeTheme.spaces.spacing400,
                                start = LemonadeTheme.spaces.spacing400,
                                end = LemonadeTheme.spaces.spacing400,
                                bottom = LemonadeTheme.spaces.spacing100
                            ),
                            textStyle = LemonadeTheme.typography.headingXSmall
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(LemonadeTheme.spaces.spacing100),
                            modifier = Modifier
                                .padding(all = LemonadeTheme.spaces.spacing100)
                        ) {

                            display.items.forEach { item ->
                                LemonadeUi.ActionListItem(
                                    label = item.label,
                                    onItemClicked = { onNavigate(item) },
                                    trailingSlot = {
                                        LemonadeUi.Icon(
                                            icon = LemonadeIcons.ChevronRight,
                                            size = LemonadeAssetSize.Medium,
                                            contentDescription = "Navigation indicator",
                                            tint = LemonadeTheme.colors.content.contentTertiary
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

internal object DisplayRegistry {
    val homeItems: List<DisplayData> = listOf(
        DisplayData(
            title = "Foundations",
            items = listOf(
                Display.Colors,
                Display.Spacing,
                Display.Radius,
                Display.Shadows,
                Display.Sizes,
                Display.Opacity,
                Display.BorderWidth,
            )
        ),
        DisplayData(
            title = "Assets",
            items = listOf(
                Display.Icons,
                Display.CountryFlag,
                Display.BrandLogo,
            )
        ),
        DisplayData(
            title = "Typography",
            items = listOf(
                Display.Text,
            )
        ),
        DisplayData(
            title = "Form Controls",
            items = listOf(
                Display.Button,
                Display.IconButton,
                Display.Checkbox,
                Display.RadioButton,
                Display.Switch,
            )
        ),
        DisplayData(
            title = "Input Fields",
            items = listOf(
                Display.TextField,
                Display.SearchField,
            )
        ),
        DisplayData(
            title = "Display Components",
            items = listOf(
                Display.Tag,
                Display.Badge,
                Display.SymbolContainer,
                Display.Card,
                Display.Spinner
            )
        ),
        DisplayData(
            title = "Selection & Lists",
            items = listOf(
                Display.Chip,
                Display.SelectionListItem,
                Display.ResourceListItem,
                Display.SegmentedControl,
            )
        ),
        DisplayData(
            title = "Navigation",
            items = listOf(
                Display.Tile,
            )
        ),
    )

    data class DisplayData(
        val title: String,
        val items: List<Display>
    )
}
