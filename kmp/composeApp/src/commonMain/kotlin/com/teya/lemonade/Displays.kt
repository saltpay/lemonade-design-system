package com.teya.lemonade

import kotlinx.serialization.Serializable

internal expect val platformSpecificEntries: List<Displays>

internal interface Displays {
    val label: String

    companion object {
        val entries: List<Displays> = listOf(
            Home,
            Colors,
            Icons,
            CountryFlag,
            BrandLogo,
            Badge,
            Switch,
            Checkbox,
            RadioButton,
            SelectionListItem,
            ActionListItem,
            ResourceListItem,
            Chip,
            SegmentedControl,
            SymbolContainer,
            Text,
            Tag,
            TextField,
            SearchField,
            Card,
            Button,
            IconButton,
            Link,
            Shadows,
            Tile,
            Spacing,
            Radius,
            Sizes,
            Opacity,
            BorderWidth,
            Spinner,
            Divider,
            Popover,
        )
    }

    @Serializable
    data object Home : Displays {
        override val label: String = "Home"
    }

    @Serializable
    data object Colors : Displays {
        override val label: String = "Colors"
    }

    @Serializable
    data object Icons : Displays {
        override val label: String = "Icons"
    }

    @Serializable
    data object CountryFlag : Displays {
        override val label: String = "CountryFlag"
    }

    @Serializable
    data object BrandLogo : Displays {
        override val label: String = "BrandLogo"
    }

    @Serializable
    data object Badge : Displays {
        override val label: String = "Badge"
    }

    @Serializable
    data object Switch : Displays {
        override val label: String = "Switch"
    }

    @Serializable
    data object Checkbox : Displays {
        override val label: String = "Checkbox"
    }

    @Serializable
    data object RadioButton : Displays {
        override val label: String = "RadioButton"
    }

    @Serializable
    data object SelectionListItem : Displays {
        override val label: String = "SelectionListItem"
    }

    @Serializable
    data object ActionListItem : Displays {
        override val label: String = "ActionListItem"
    }

    @Serializable
    data object ResourceListItem : Displays {
        override val label: String = "ResourceListItem"
    }

    @Serializable
    data object Chip : Displays {
        override val label: String = "Chip"
    }

    @Serializable
    data object SegmentedControl : Displays {
        override val label: String = "SegmentedControl"
    }

    @Serializable
    data object SymbolContainer : Displays {
        override val label: String = "SymbolContainer"
    }

    @Serializable
    data object Text : Displays {
        override val label: String = "Text"
    }

    @Serializable
    data object Tag : Displays {
        override val label: String = "Tag"
    }

    @Serializable
    data object TextField : Displays {
        override val label: String = "TextField"
    }

    @Serializable
    data object SearchField : Displays {
        override val label: String = "SearchField"
    }

    @Serializable
    data object Card : Displays {
        override val label: String = "Card"
    }

    @Serializable
    data object Button : Displays {
        override val label: String = "Button"
    }

    @Serializable
    data object IconButton : Displays {
        override val label: String = "IconButton"
    }

    @Serializable
    data object Link : Displays {
        override val label: String = "Link"
    }

    @Serializable
    data object Shadows : Displays {
        override val label: String = "Shadows"
    }

    @Serializable
    data object Tile : Displays {
        override val label: String = "Tile"
    }

    @Serializable
    data object Spacing : Displays {
        override val label: String = "Spacing"
    }

    @Serializable
    data object Radius : Displays {
        override val label: String = "Radius"
    }

    @Serializable
    data object Sizes : Displays {
        override val label: String = "Sizes"
    }

    @Serializable
    data object Opacity : Displays {
        override val label: String = "Opacity"
    }

    @Serializable
    data object BorderWidth : Displays {
        override val label: String = "Border Width"
    }

    @Serializable
    data object Spinner : Displays {
        override val label: String = "Spinner"
    }

    @Serializable
    data object Divider : Displays {
        override val label: String = "Divider"
    }

    @Serializable
    data object Popover : Displays {
        override val label: String = "Popover"
    }
}
