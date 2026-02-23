package com.teya.lemonade.app

import androidx.compose.runtime.Composable
import com.teya.lemonade.ActionListItemDisplay
import com.teya.lemonade.BadgeDisplay
import com.teya.lemonade.BorderWidthDisplay
import com.teya.lemonade.BrandLogoDisplay
import com.teya.lemonade.ButtonDisplay
import com.teya.lemonade.CardDisplay
import com.teya.lemonade.CheckboxDisplay
import com.teya.lemonade.ChipDisplay
import com.teya.lemonade.ColorsDisplay
import com.teya.lemonade.CountryFlagDisplay
import com.teya.lemonade.Displays
import com.teya.lemonade.DividerDisplay
import com.teya.lemonade.HomeDisplay
import com.teya.lemonade.IconButtonDisplay
import com.teya.lemonade.IconsDisplay
import com.teya.lemonade.LinkDisplay
import com.teya.lemonade.OpacityDisplay
import com.teya.lemonade.PopoverDisplay
import com.teya.lemonade.RadioButtonDisplay
import com.teya.lemonade.RadiusDisplay
import com.teya.lemonade.ResourceListItemDisplay
import com.teya.lemonade.SearchFieldDisplay
import com.teya.lemonade.SegmentedControlDisplay
import com.teya.lemonade.SelectionListItemDisplay
import com.teya.lemonade.ShadowDisplay
import com.teya.lemonade.SizesDisplay
import com.teya.lemonade.SpacingDisplay
import com.teya.lemonade.SpinnerDisplay
import com.teya.lemonade.SwitchDisplay
import com.teya.lemonade.SymbolContainerDisplay
import com.teya.lemonade.TagDisplay
import com.teya.lemonade.TextDisplay
import com.teya.lemonade.TextFieldDisplay
import com.teya.lemonade.TileDisplay

@Composable
internal expect fun App()

internal expect val platformScreens: Map<Displays, @Composable (onNavigate: (Displays) -> Unit) -> Unit>

internal val screens: Map<Displays, @Composable (onNavigate: (Displays) -> Unit) -> Unit> = platformScreens + mapOf(
    Displays.Home to { onNavigate -> HomeDisplay(onNavigate = onNavigate) },
    Displays.Colors to { _ -> ColorsDisplay() },
    Displays.Icons to { _ -> IconsDisplay() },
    Displays.CountryFlag to { _ -> CountryFlagDisplay() },
    Displays.BrandLogo to { _ -> BrandLogoDisplay() },
    Displays.Badge to { _ -> BadgeDisplay() },
    Displays.Switch to { _ -> SwitchDisplay() },
    Displays.Checkbox to { _ -> CheckboxDisplay() },
    Displays.RadioButton to { _ -> RadioButtonDisplay() },
    Displays.SelectionListItem to { _ -> SelectionListItemDisplay() },
    Displays.ActionListItem to { _ -> ActionListItemDisplay() },
    Displays.ResourceListItem to { _ -> ResourceListItemDisplay() },
    Displays.Chip to { _ -> ChipDisplay() },
    Displays.SegmentedControl to { _ -> SegmentedControlDisplay() },
    Displays.Text to { _ -> TextDisplay() },
    Displays.SymbolContainer to { _ -> SymbolContainerDisplay() },
    Displays.Tag to { _ -> TagDisplay() },
    Displays.TextField to { _ -> TextFieldDisplay() },
    Displays.SearchField to { _ -> SearchFieldDisplay() },
    Displays.Card to { _ -> CardDisplay() },
    Displays.Button to { _ -> ButtonDisplay() },
    Displays.IconButton to { _ -> IconButtonDisplay() },
    Displays.Link to { _ -> LinkDisplay() },
    Displays.Shadows to { _ -> ShadowDisplay() },
    Displays.Tile to { _ -> TileDisplay() },
    Displays.Spacing to { _ -> SpacingDisplay() },
    Displays.Radius to { _ -> RadiusDisplay() },
    Displays.Sizes to { _ -> SizesDisplay() },
    Displays.Opacity to { _ -> OpacityDisplay() },
    Displays.BorderWidth to { _ -> BorderWidthDisplay() },
    Displays.Spinner to { _ -> SpinnerDisplay() },
    Displays.Divider to { _ -> DividerDisplay() },
    Displays.Popover to { _ -> PopoverDisplay() },
)
