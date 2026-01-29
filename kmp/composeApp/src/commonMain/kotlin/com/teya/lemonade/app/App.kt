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
import com.teya.lemonade.Display
import com.teya.lemonade.HomeDisplay
import com.teya.lemonade.IconButtonDisplay
import com.teya.lemonade.IconsDisplay
import com.teya.lemonade.OpacityDisplay
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

internal val screens: Map<Display, @Composable (onNavigate: (Display) -> Unit) -> Unit> = mapOf(
    Display.Home to { onNavigate -> HomeDisplay(onNavigate = onNavigate) },
    Display.Colors to { _ -> ColorsDisplay() },
    Display.Icons to { _ -> IconsDisplay() },
    Display.CountryFlag to { _ -> CountryFlagDisplay() },
    Display.BrandLogo to { _ -> BrandLogoDisplay() },
    Display.Badge to { _ -> BadgeDisplay() },
    Display.Switch to { _ -> SwitchDisplay() },
    Display.Checkbox to { _ -> CheckboxDisplay() },
    Display.RadioButton to { _ -> RadioButtonDisplay() },
    Display.SelectionListItem to { _ -> SelectionListItemDisplay() },
    Display.ActionListItem to { _ -> ActionListItemDisplay() },
    Display.ResourceListItem to { _ -> ResourceListItemDisplay() },
    Display.Chip to { _ -> ChipDisplay() },
    Display.SegmentedControl to { _ -> SegmentedControlDisplay() },
    Display.Text to { _ -> TextDisplay() },
    Display.SymbolContainer to { _ -> SymbolContainerDisplay() },
    Display.Tag to { _ -> TagDisplay() },
    Display.TextField to { _ -> TextFieldDisplay() },
    Display.SearchField to { _ -> SearchFieldDisplay() },
    Display.Card to { _ -> CardDisplay() },
    Display.Button to { _ -> ButtonDisplay() },
    Display.IconButton to { _ -> IconButtonDisplay() },
    Display.Shadows to { _ -> ShadowDisplay() },
    Display.Tile to { _ -> TileDisplay() },
    Display.Spacing to { _ -> SpacingDisplay() },
    Display.Radius to { _ -> RadiusDisplay() },
    Display.Sizes to { _ -> SizesDisplay() },
    Display.Opacity to { _ -> OpacityDisplay() },
    Display.BorderWidth to { _ -> BorderWidthDisplay() },
    Display.Spinner to { _ -> SpinnerDisplay() },
)