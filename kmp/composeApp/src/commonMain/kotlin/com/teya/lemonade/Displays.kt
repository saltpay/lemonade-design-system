package com.teya.lemonade

import kotlinx.serialization.Serializable

@Serializable
internal enum class Display(
    val label: String,
){
    Home(label = "Home"),
    Colors(label = "Colors"),
    Icons(label = "Icons"),
    CountryFlag(label = "CountryFlag"),
    BrandLogo(label = "BrandLogo"),
    Badge(label = "Badge"),
    Switch(label = "Switch"),
    Checkbox(label = "Checkbox"),
    RadioButton(label = "RadioButton"),
    SelectionListItem(label = "SelectionListItem"),
    ResourceListItem(label = "ResourceListItem"),
    Chip(label = "Chip"),
    SegmentedControl(label = "SegmentedControl"),
    SymbolContainer(label = "SymbolContainer"),
    Text(label = "Text"),
    Tag(label = "Tag"),
    TextField(label = "TextField"),
    SearchField(label = "SearchField"),
    Card(label = "Card"),
    Button(label = "Button"),
    IconButton(label = "IconButton"),
    Shadows(label = "Shadows"),
    Tile(label = "Tile"),
    Spacing(label = "Spacing"),
    Radius(label = "Radius"),
    Sizes(label = "Sizes"),
    Opacity(label = "Opacity"),
    BorderWidth(label = "Border Width"),
    Spinner(label = "Spinner"),
}
