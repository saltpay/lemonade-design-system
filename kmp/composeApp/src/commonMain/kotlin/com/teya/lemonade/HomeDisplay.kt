package com.teya.lemonade

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.TabButtonProperties

@Composable
internal fun HomeDisplay(onNavigate: (Displays) -> Unit) {
    val styleHandler = LocalLemonadeStyleHandler.current
    val styles = LemonadeStyle.entries
    val selectedIndex = styles.indexOf(styleHandler.currentStyle)
    val variants = LemonadeThemeVariant.entries
    val selectedVariantIndex = variants.indexOf(styleHandler.currentVariant)

    SampleScreenDisplayLazyColumn(
        title = "Lemonade Design System",
    ) {
        item {
            LemonadeUi.SegmentedControl(
                selectedTab = selectedIndex,
                onTabSelected = { index -> styleHandler.currentStyle = styles[index] },
                properties = styles.map { style -> TabButtonProperties.label(label = style.label) },
                modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing200),
            )
            LemonadeUi.SegmentedControl(
                selectedTab = selectedVariantIndex,
                onTabSelected = { index -> styleHandler.currentVariant = variants[index] },
                properties = variants.map { variant -> TabButtonProperties.label(label = variant.label) },
                modifier = Modifier.padding(bottom = LemonadeTheme.spaces.spacing400),
            )
        }
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
                Displays.Markdown,
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
                Displays.DatePicker,
                Displays.InlineCalendar,
            ),
        ),
        DisplayData(
            title = "Input Fields",
            items = listOf(
                Displays.TextField,
                Displays.SearchField,
                Displays.SelectField,
            ),
        ),
        DisplayData(
            title = "Display Components",
            items = listOf(
                Displays.Tag,
                Displays.NoticeRow,
                Displays.Badge,
                Displays.SymbolContainer,
                Displays.Card,
                Displays.Spinner,
                Displays.Skeleton,
                Displays.Divider,
                Displays.Notice,
                Displays.Toast,
                Displays.HistoryTimeline,
            ),
        ),
        DisplayData(
            title = "Selection & Lists",
            items = listOf(
                Displays.Chip,
                Displays.SelectListItem,
                Displays.ResourceListItem,
                Displays.ContentListItem,
                Displays.SegmentedControl,
                Displays.ActionListItem,
            ),
        ),
        DisplayData(
            title = "Navigation",
            items = listOf(
                Displays.Link,
                Displays.Tabs,
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
