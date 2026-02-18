---
name: lemonade-kmp
description: Lemonade Design System for Kotlin Multiplatform (Compose Multiplatform). Use when writing, editing, or reviewing KMP/Compose UI code that uses the Lemonade library. This skill provides complete component APIs, design tokens, theming, and usage patterns.
---

# Lemonade Design System - KMP (Compose Multiplatform)

A production-ready, multi-platform UI component library for Android, iOS, and JVM Desktop using Compose Multiplatform.

- **Package**: `com.teya.lemonade` (UI), `com.teya.lemonade.core` (core definitions)
- **Maven coordinates**: `com.teya.foundation:lemonade-ui`, `com.teya.foundation:lemonade-core`
- **Platforms**: Android (minSdk 23), iOS (arm64 + simulatorArm64), JVM Desktop

---

## Installation

### libs.versions.toml

```toml
[versions]
lemonade = "latest-version"

[libraries]
lemonade-ui = { module = "com.teya.foundation:lemonade-ui", version.ref = "lemonade" }
lemonade-core = { module = "com.teya.foundation:lemonade-core", version.ref = "lemonade" }
```

### build.gradle.kts (commonMain)

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.lemonade.ui)
            }
        }
    }
}
```

> `lemonade-core` is included transitively via `lemonade-ui` (exposed as `api`). Only add `lemonade-core` directly if you need core definitions without UI (e.g., server-driven UI).

---

## Theme Setup

Wrap your root composable in `LemonadeTheme`. This is optional - default values are used if omitted.

```kotlin
import com.teya.lemonade.LemonadeTheme

@Composable
fun App() {
    LemonadeTheme(
        colors = LemonadeTheme.colors,
        typography = LemonadeTheme.typography,
        radius = LemonadeTheme.radius,
        shapes = LemonadeTheme.shapes,
        opacities = LemonadeTheme.opacities,
        spaces = LemonadeTheme.spaces,
        borderWidths = LemonadeTheme.borderWidths,
        sizes = LemonadeTheme.sizes,
    ) {
        MyScreenContent()
    }
}
```

### Accessing Theme Values in Composables

```kotlin
// Via LemonadeTheme object (recommended)
LemonadeTheme.colors.background.bgDefault
LemonadeTheme.typography.bodyMediumRegular
LemonadeTheme.spaces.spacing400
LemonadeTheme.radius.radius300
LemonadeTheme.sizes.size1200

// Via CompositionLocals (for internal/advanced use)
LocalColors.current.content.contentPrimary
LocalTypographies.current.headingSmall
LocalSpaces.current.spacing200
LocalRadius.current.radius200
LocalShapes.current.radius300
LocalOpacities.current.state.opacityDisabled
LocalBorderWidths.current.base.border25
LocalSizes.current.size1000
```

---

## Component Usage Pattern

All components are extension functions on the `LemonadeUi` data object:

```kotlin
import com.teya.lemonade.LemonadeUi

@Composable
fun MyScreen() {
    LemonadeUi.Text(text = "Hello")
    LemonadeUi.Button(label = "Click", onClick = { })
    LemonadeUi.Switch(checked = true, onCheckedChange = { })
}
```

This pattern enables auto-complete discovery and clear provenance of components.

---

## Complete Component API Reference

### Button
**File**: `Button.kt` | **Annotation**: `@ExperimentalLemonadeComponent`

```kotlin
@Composable
@ExperimentalLemonadeComponent
public fun LemonadeUi.Button(
    label: String,
    onClick: () -> Unit,
    leadingIcon: LemonadeIcons? = null,
    trailingIcon: LemonadeIcons? = null,
    variant: LemonadeButtonVariant = LemonadeButtonVariant.Primary,
    size: LemonadeButtonSize = LemonadeButtonSize.Large,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
)
```

**Example**:
```kotlin
@OptIn(ExperimentalLemonadeComponent::class)
LemonadeUi.Button(
    label = "Submit",
    onClick = { /* handle click */ },
    leadingIcon = LemonadeIcons.Check,
    variant = LemonadeButtonVariant.Primary,
    size = LemonadeButtonSize.Large,
)
```

### IconButton
**File**: `IconButton.kt`

```kotlin
@Composable
public fun LemonadeUi.IconButton(
    icon: LemonadeIcons,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    variant: LemonadeIconButtonVariant = LemonadeIconButtonVariant.Subtle,
    size: LemonadeIconButtonSize = LemonadeIconButtonSize.Medium,
)
```

**Example**:
```kotlin
LemonadeUi.IconButton(
    icon = LemonadeIcons.Heart,
    contentDescription = "Favorite",
    onClick = { /* handle click */ },
)
```

### Link
**File**: `Link.kt`

```kotlin
@Composable
public fun LemonadeUi.Link(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: LemonadeIcons? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
)
```

**Example**:
```kotlin
LemonadeUi.Link(
    text = "Learn more",
    onClick = { /* navigate */ },
    icon = LemonadeIcons.ExternalLink,
)
```

### Text
**File**: `Text.kt`

```kotlin
// Overload 1: With LemonadeTextStyle
@Composable
public fun LemonadeUi.Text(
    text: String,
    fontSize: TextUnit? = null,
    modifier: Modifier = Modifier,
    textStyle: LemonadeTextStyle = LocalTypographies.current.bodyMediumRegular,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = LocalContentColors.current,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    autoSize: TextAutoSize? = null,
)

// Overload 2: With Compose TextStyle
@Composable
public fun LemonadeUi.Text(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    autoSize: TextAutoSize? = null,
)
```

**Example**:
```kotlin
LemonadeUi.Text(
    text = "Welcome to Lemonade",
    textStyle = LemonadeTheme.typography.headingSmall,
    color = LemonadeTheme.colors.content.contentPrimary,
)
```

### TextField
**File**: `TextField.kt`

```kotlin
@Composable
public fun LemonadeUi.TextField(
    input: String,
    onInputChanged: (String) -> Unit,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    placeholderText: String? = null,
    errorMessage: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: Boolean = false,
    enabled: Boolean = true,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
)
```

**Example**:
```kotlin
var text by remember { mutableStateOf("") }

LemonadeUi.TextField(
    input = text,
    onInputChanged = { text = it },
    label = "Email",
    placeholderText = "Enter your email",
    leadingContent = {
        LemonadeUi.Icon(
            icon = LemonadeIcons.Envelope,
            contentDescription = null,
        )
    },
)
```

### TextFieldWithSelector
**File**: `TextField.kt`

```kotlin
@Composable
public fun LemonadeUi.TextFieldWithSelector(
    input: String,
    onInputChanged: (String) -> Unit,
    leadingAction: () -> Unit,
    leadingContent: @Composable BoxScope.() -> Unit,
    label: String? = null,
    optionalIndicator: String? = null,
    supportText: String? = null,
    placeholderText: String? = null,
    errorMessage: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: Boolean = false,
    enabled: Boolean = true,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
)
```

**Example** (phone number input with country selector):
```kotlin
LemonadeUi.TextFieldWithSelector(
    input = phoneNumber,
    onInputChanged = { phoneNumber = it },
    label = "Phone Number",
    leadingAction = { showCountryPicker = true },
    leadingContent = {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = LemonadeTheme.spaces.spacing200),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = LemonadeTheme.spaces.spacing400),
        ) {
            LemonadeUi.CountryFlag(flag = LemonadeCountryFlags.BRBrazil)
            LemonadeUi.Text(text = "+55")
            LemonadeUi.Icon(
                icon = LemonadeIcons.ChevronDown,
                contentDescription = null,
            )
        }
    },
)
```

### SearchField
**File**: `SearchField.kt` | **Annotation**: `@ExperimentalLemonadeComponent`

```kotlin
@Composable
@ExperimentalLemonadeComponent
public fun LemonadeUi.SearchField(
    input: String,
    onInputChanged: (String) -> Unit,
    placeholder: String? = null,
    onInputClear: () -> Unit = { onInputChanged("") },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
)
```

### Switch
**File**: `Switch.kt`

```kotlin
// Icon-only variant
@Composable
public fun LemonadeUi.Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
)

// Labeled variant
@Composable
public fun LemonadeUi.Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    supportText: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
)
```

**Example**:
```kotlin
var enabled by remember { mutableStateOf(false) }

LemonadeUi.Switch(
    checked = enabled,
    onCheckedChange = { enabled = it },
    label = "Enable notifications",
    supportText = "Receive push notifications",
)
```

### Checkbox
**File**: `Checkbox.kt`

```kotlin
// Labeled variant
@Composable
public fun LemonadeUi.Checkbox(
    status: CheckboxStatus,
    onCheckboxClicked: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    supportText: String? = null,
    enabled: Boolean = true,
)

// Icon-only variant
@Composable
public fun LemonadeUi.Checkbox(
    status: CheckboxStatus,
    onCheckboxClicked: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
)
```

**Example**:
```kotlin
var status by remember { mutableStateOf(CheckboxStatus.Unchecked) }

LemonadeUi.Checkbox(
    status = status,
    onCheckboxClicked = {
        status = if (status == CheckboxStatus.Checked) {
            CheckboxStatus.Unchecked
        } else {
            CheckboxStatus.Checked
        }
    },
    label = "I agree to the terms",
)
```

### RadioButton
**File**: `RadioButton.kt`

```kotlin
// Labeled variant
@Composable
public fun LemonadeUi.RadioButton(
    checked: Boolean,
    onRadioButtonClicked: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    supportText: String? = null,
    enabled: Boolean = true,
)

// Icon-only variant
@Composable
public fun LemonadeUi.RadioButton(
    checked: Boolean,
    onRadioButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
)
```

### Chip
**File**: `Chip.kt`

```kotlin
@Composable
public fun LemonadeUi.Chip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    leadingIcon: LemonadeIcons? = null,
    trailingIcon: LemonadeIcons? = null,
    counter: Int? = null,
    enabled: Boolean = true,
    onChipClicked: (() -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
)
```

**Example**:
```kotlin
LemonadeUi.Chip(
    label = "Filter",
    selected = isSelected,
    leadingIcon = LemonadeIcons.Filter,
    onChipClicked = { isSelected = !isSelected },
)
```

### Card
**File**: `Card.kt`

```kotlin
@Composable
public fun LemonadeUi.Card(
    modifier: Modifier = Modifier,
    contentPadding: LemonadeCardPadding = LemonadeCardPadding.None,
    background: LemonadeCardBackground = LemonadeCardBackground.Default,
    header: CardHeaderConfig? = null,
    content: (@Composable ColumnScope.() -> Unit),
)
```

**`CardHeaderConfig`**:
```kotlin
public data class CardHeaderConfig(
    val title: String,
    val trailingSlot: (@Composable RowScope.() -> Unit)? = null,
)
```

**Example**:
```kotlin
LemonadeUi.Card(
    contentPadding = LemonadeCardPadding.Medium,
    background = LemonadeCardBackground.Default,
    header = CardHeaderConfig(
        title = "Account Details",
        trailingSlot = {
            LemonadeUi.Tag(
                label = "Active",
                voice = TagVoice.Positive,
            )
        },
    ),
) {
    LemonadeUi.Text(text = "Card body content")
}
```

### Tag
**File**: `Tag.kt`

```kotlin
@Composable
public fun LemonadeUi.Tag(
    label: String,
    modifier: Modifier = Modifier,
    icon: LemonadeIcons? = null,
    voice: TagVoice = TagVoice.Neutral,
)
```

**Example**:
```kotlin
LemonadeUi.Tag(
    label = "Approved",
    icon = LemonadeIcons.Check,
    voice = TagVoice.Positive,
)
```

### Badge
**File**: `Badge.kt`

```kotlin
@Composable
public fun LemonadeUi.Badge(
    text: String,
    modifier: Modifier = Modifier,
    size: LemonadeBadgeSize = LemonadeBadgeSize.Small,
)
```

### SymbolContainer
**File**: `SymbolContainer.kt`

```kotlin
// With icon
@Composable
public fun LemonadeUi.SymbolContainer(
    icon: LemonadeIcons,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
)

// With text
@Composable
public fun LemonadeUi.SymbolContainer(
    text: String,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
)

// With custom content slot
@Composable
public fun LemonadeUi.SymbolContainer(
    contentSlot: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
)
```

**Example**:
```kotlin
LemonadeUi.SymbolContainer(
    icon = LemonadeIcons.Heart,
    contentDescription = "Favorites",
    voice = SymbolContainerVoice.Info,
    size = SymbolContainerSize.Large,
)
```

### Icon
**File**: `Icon.kt`

```kotlin
@Composable
public fun LemonadeUi.Icon(
    icon: LemonadeIcons,
    contentDescription: String?,
    size: LemonadeAssetSize = LemonadeAssetSize.Medium,
    tint: Color = LocalContentColors.current,
    modifier: Modifier = Modifier,
)
```

### Spinner
**File**: `Spinner.kt`

```kotlin
@Composable
public fun LemonadeUi.Spinner(
    size: LemonadeAssetSize = LemonadeAssetSize.Medium,
    tint: Color = LocalContentColors.current,
    modifier: Modifier = Modifier,
)
```

### CountryFlag
**File**: `CountryFlag.kt`

```kotlin
@Composable
public fun LemonadeUi.CountryFlag(
    flag: LemonadeCountryFlags,
    modifier: Modifier = Modifier,
)
```

### BrandLogo
**File**: `BrandLogo.kt`

```kotlin
@Composable
public fun LemonadeUi.BrandLogo(
    logo: LemonadeBrandLogos,
    modifier: Modifier = Modifier,
)
```

### Divider
**File**: `Divider.kt`

```kotlin
@Composable
public fun LemonadeUi.HorizontalDivider(
    modifier: Modifier = Modifier,
    label: String? = null,
    variant: DividerVariant = DividerVariant.Solid,
)

@Composable
public fun LemonadeUi.VerticalDivider(
    modifier: Modifier = Modifier,
    variant: DividerVariant = DividerVariant.Solid,
)
```

### SegmentedControl
**File**: `SegmentedControl.kt`

```kotlin
@Composable
public fun LemonadeUi.SegmentedControl(
    properties: List<TabButtonProperties>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
)
```

**`TabButtonProperties`** (from `com.teya.lemonade.core`):
```kotlin
public data class TabButtonProperties(
    val label: String,
    val icon: LemonadeIcons? = null,
)
```

**Example**:
```kotlin
var selectedTab by remember { mutableIntStateOf(0) }

LemonadeUi.SegmentedControl(
    selectedTab = selectedTab,
    onTabSelected = { selectedTab = it },
    properties = listOf(
        TabButtonProperties(label = "Overview", icon = LemonadeIcons.Heart),
        TabButtonProperties(label = "Details"),
        TabButtonProperties(label = "History"),
    ),
)
```

### SelectListItem
**File**: `ListItem.kt`

```kotlin
@Composable
public fun LemonadeUi.SelectListItem(
    label: String,
    type: SelectListItemType,
    checked: Boolean,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showDivider: Boolean = false,
    supportText: String? = null,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
)
```

**Example**:
```kotlin
LemonadeUi.SelectListItem(
    label = "Option A",
    type = SelectListItemType.Single,
    checked = selectedOption == "A",
    onItemClicked = { selectedOption = "A" },
    showDivider = true,
    leadingSlot = {
        LemonadeUi.Icon(
            icon = LemonadeIcons.Star,
            contentDescription = null,
        )
    },
)
```

### ResourceListItem
**File**: `ListItem.kt`

```kotlin
@Composable
public fun LemonadeUi.ResourceListItem(
    leadingSlot: @Composable BoxScope.() -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    addonSlot: (@Composable ColumnScope.() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onItemClicked: (() -> Unit)? = null,
    enabled: Boolean = true,
    supportText: String? = null,
    showDivider: Boolean = false,
)
```

**Example**:
```kotlin
LemonadeUi.ResourceListItem(
    label = "Transaction",
    value = "$42.00",
    supportText = "Coffee Shop",
    showDivider = true,
    leadingSlot = {
        LemonadeUi.SymbolContainer(
            icon = LemonadeIcons.ShoppingBag,
            voice = SymbolContainerVoice.Neutral,
            size = SymbolContainerSize.Large,
            contentDescription = null,
        )
    },
    addonSlot = {
        LemonadeUi.Tag(
            label = "Completed",
            voice = TagVoice.Positive,
        )
    },
)
```

### ActionListItem
**File**: `ListItem.kt`

```kotlin
@Composable
public fun LemonadeUi.ActionListItem(
    label: String,
    modifier: Modifier = Modifier,
    supportText: String? = null,
    leadingSlot: (@Composable RowScope.() -> Unit)? = null,
    trailingSlot: (@Composable RowScope.() -> Unit)? = null,
    voice: LemonadeListItemVoice = LemonadeListItemVoice.Neutral,
    enabled: Boolean = true,
    onItemClicked: (() -> Unit)? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    showNavigationIndicator: Boolean = false,
    showDivider: Boolean = false,
)
```

**Example**:
```kotlin
LemonadeUi.ActionListItem(
    label = "Settings",
    supportText = "Manage your preferences",
    showNavigationIndicator = true,
    showDivider = true,
    onItemClicked = { navigateToSettings() },
    leadingSlot = {
        LemonadeUi.Icon(
            icon = LemonadeIcons.Gear,
            size = LemonadeAssetSize.Medium,
            contentDescription = null,
        )
    },
)
```

---

## Enums & Data Classes

All enums are in `com.teya.lemonade.core`.

### Button

| Enum | Entries |
|------|---------|
| `LemonadeButtonVariant` | `Primary`, `Secondary`, `Neutral`, `CriticalSubtle`, `CriticalSolid`, `Special` |
| `LemonadeButtonSize` | `Small`, `Medium`, `Large` |

### IconButton

| Enum | Entries |
|------|---------|
| `LemonadeIconButtonVariant` | `Subtle`, `Neutral`, `Brand` |
| `LemonadeIconButtonSize` | `Small`, `Medium`, `Large` |

### Card

| Enum | Entries |
|------|---------|
| `LemonadeCardPadding` | `None`, `XSmall`, `Small`, `Medium` |
| `LemonadeCardBackground` | `Default`, `Subtle` |

### Checkbox

| Enum | Entries |
|------|---------|
| `CheckboxStatus` | `Checked`, `Unchecked`, `Indeterminate` |

### List Items

| Enum | Entries |
|------|---------|
| `SelectListItemType` | `Single`, `Multiple`, `Toggle` |
| `LemonadeListItemVoice` | `Neutral`, `Critical` |

### Tag & SymbolContainer Voice

| Enum | Entries |
|------|---------|
| `TagVoice` | `Neutral`, `Critical`, `Warning`, `Info`, `Positive` |
| `SymbolContainerVoice` | `Neutral`, `Critical`, `Warning`, `Info`, `Positive`, `Brand`, `BrandSubtle` |
| `SymbolContainerSize` | `XSmall`, `Small`, `Medium`, `Large`, `XLarge` |

### Divider

| Enum | Entries |
|------|---------|
| `DividerVariant` | `Solid`, `Dashed` |

### Asset Sizes

| Enum | Entries |
|------|---------|
| `LemonadeAssetSize` | `XSmall`, `Small`, `Medium`, `Large`, `XLarge`, `XXLarge`, `XXXLarge` |

### TextField

| Enum | Entries |
|------|---------|
| `TextFieldSize` | `Small`, `Medium`, `Large` |

---

## Design Tokens

### Semantic Colors

Access via `LemonadeTheme.colors` or `LocalColors.current`. Colors are organized in nested groups:

**Background** (`colors.background.*`):
- `bgAlwaysLight`, `bgAlwaysDark`
- `bgDefault`, `bgDefaultInverse`, `bgElevated`, `bgElevatedInverse`, `bgElevatedHigh`
- `bgSubtle`, `bgSubtleInverse`
- `bgBrand`, `bgBrandSubtle`, `bgBrandHigh`, `bgBrandElevated`
- `bgNeutral`, `bgNeutralSubtle`
- `bgCritical`, `bgCriticalSubtle`
- `bgCaution`, `bgCautionSubtle`
- `bgInfo`, `bgInfoSubtle`
- `bgPositive`, `bgPositiveSubtle`

**Content** (`colors.content.*`):
- `contentAlwaysLight`, `contentAlwaysDark`, `contentNeutral`
- `contentPrimary`, `contentPrimaryInverse`
- `contentSecondary`, `contentSecondaryInverse`
- `contentTertiary`, `contentTertiaryInverse`
- `contentBrand`, `contentBrandInverse`, `contentBrandHigh`, `contentBrandLow`
- `contentOnBrandHigh`, `contentOnBrandLow`
- `contentCritical`, `contentCriticalOnColor`
- `contentCaution`, `contentCautionOnColor`
- `contentInfo`, `contentInfoOnColor`
- `contentPositive`, `contentPositiveOnColor`
- `contentNeutralOnColor`

**Border** (`colors.border.*`):
- `borderAlwaysDark`, `borderAlwaysLight`
- `borderNeutralLow`, `borderNeutralMedium`, `borderNeutralHigh`
- `borderNeutralLowInverse`, `borderNeutralMediumInverse`, `borderNeutralHighInverse`
- `borderSelected`, `borderSelectedInverse`
- `borderBrand`, `borderBrandInverse`
- `borderOnBrandLow`, `borderOnBrandMedium`, `borderOnBrandHigh`
- `borderCritical`, `borderCriticalSubtle`
- `borderCaution`, `borderCautionSubtle`
- `borderInfo`, `borderInfoSubtle`
- `borderPositive`, `borderPositiveSubtle`

**Interaction** (`colors.interaction.*`):
- `bgSubtleInteractive`, `bgDefaultInteractive`, `bgElevatedInteractive`, `bgElevatedHighInteractive`
- `bgNeutralInteractive`, `bgNeutralSubtleInteractive`
- `bgBrandInteractive`, `bgBrandHighInteractive`, `bgBrandElevatedInteractive`
- `bgCriticalInteractive`, `bgCriticalSubtleInteractive`
- `bgDefaultPressed`, `bgElevatedPressed`, `bgNeutralPressed`
- `bgBrandPressed`, `bgBrandHighPressed`, `bgBrandElevatedPressed`
- `bgCriticalPressed`, `bgCriticalSubtlePressed`

### Typography

Access via `LemonadeTheme.typography` or `LocalTypographies.current`. Font family: **Figtree**.

| Style | Size | Line Height | Weight |
|-------|------|-------------|--------|
| `displayXSmall` | 24sp | 32sp | 600 |
| `displaySmall` | 28sp | 36sp | 600 |
| `displayMedium` | 36sp | 44sp | 600 |
| `displayLarge` | 48sp | 56sp | 600 |
| `headingXLarge` | 40sp | 48sp | 600 |
| `headingLarge` | 32sp | 40sp | 600 |
| `headingMedium` | 28sp | 36sp | 600 |
| `headingSmall` | 24sp | 32sp | 600 |
| `headingXSmall` | 18sp | 26sp | 600 |
| `headingXXSmall` | 16sp | 24sp | 600 |
| `bodyXLargeRegular` | 20sp | 28sp | 400 |
| `bodyXLargeMedium` | 20sp | 28sp | 500 |
| `bodyXLargeSemiBold` | 20sp | 28sp | 600 |
| `bodyLargeRegular` | 18sp | 28sp | 400 |
| `bodyLargeMedium` | 18sp | 28sp | 500 |
| `bodyLargeSemiBold` | 18sp | 28sp | 600 |
| `bodyMediumRegular` | 16sp | 24sp | 400 |
| `bodyMediumMedium` | 16sp | 24sp | 500 |
| `bodyMediumSemiBold` | 16sp | 24sp | 600 |
| `bodyMediumBold` | 16sp | 24sp | 700 |
| `bodySmallRegular` | 14sp | 20sp | 400 |
| `bodySmallMedium` | 14sp | 20sp | 500 |
| `bodySmallSemiBold` | 14sp | 20sp | 600 |
| `bodyXSmallRegular` | 12sp | 16sp | 400 |
| `bodyXSmallMedium` | 12sp | 16sp | 500 |
| `bodyXSmallSemiBold` | 12sp | 16sp | 600 |
| `bodyXSmallOverline` | 12sp | 16sp | 600 (1.5sp letter-spacing, auto-uppercase) |

### Spacing

Access via `LemonadeTheme.spaces` or `LocalSpaces.current`:

| Token | Property |
|-------|----------|
| `spacing0` | 0dp |
| `spacing50` | 2dp |
| `spacing100` | 4dp |
| `spacing200` | 8dp |
| `spacing300` | 12dp |
| `spacing400` | 16dp |
| `spacing500` | 20dp |
| `spacing600` | 24dp |
| `spacing800` | 32dp |
| `spacing1000` | 40dp |
| `spacing1200` | 48dp |
| `spacing1400` | 56dp |
| `spacing1600` | 64dp |
| `spacing1800` | 72dp |
| `spacing2000` | 80dp |

**Usage**: `Modifier.padding(all = LemonadeTheme.spaces.spacing400)`

### Radius

Access via `LemonadeTheme.radius` or `LocalRadius.current`:

`radius0`, `radius50`, `radius100`, `radius150`, `radius200`, `radius300`, `radius400`, `radius500`, `radius600`, `radius800`, `radiusFull`

### Shapes

Access via `LemonadeTheme.shapes` or `LocalShapes.current`:

Shapes are pre-built `RoundedCornerShape` instances using radius tokens: `radius0` through `radiusFull`.

**Usage**: `Modifier.clip(shape = LemonadeTheme.shapes.radius300)`

### Shadows

Defined in `LemonadeShadow` enum: `Large`, `Medium`, `Small`, `Xsmall`, `None`

**Usage via modifier**: `.lemonadeShadow(shadow = LemonadeShadow.Small, shape = LemonadeTheme.shapes.radius300)`

### Opacity

Access via `LemonadeTheme.opacities` or `LocalOpacities.current`:

- **Base**: `opacity0`, `opacity100` (and intermediate values)
- **State**: `opacityDisabled` (for disabled component states)

### Sizes

Access via `LemonadeTheme.sizes` or `LocalSizes.current`:

`size0`, `size50`, `size100`, `size150`, `size200`, `size250`, `size300`, `size350`, `size400`, `size450`, `size500`, `size550`, `size600`, `size700`, `size750`, `size800`, `size900`, `size1000`, `size1100`, `size1200`, `size1400`, `size1600`

### Border Widths

Access via `LemonadeTheme.borderWidths` or `LocalBorderWidths.current`:

- **Base**: `border0`, `border25`, `border50`, `border100`
- **Focus**: `borderFocus`

---

## Icons

The `LemonadeIcons` enum contains **228+ icons** covering categories:

- **Navigation**: `ChevronDown`, `ChevronUp`, `ChevronLeft`, `ChevronRight`, `ExternalLink`, `ArrowDown`, `ArrowUp`, `ArrowLeft`, `ArrowRight`
- **Actions**: `Check`, `Plus`, `Minus`, `Close`, `Copy`, `Delete`, `Download`, `Edit`, `Send`, `Search`
- **Status**: `AlertTriangle`, `CheckCircle`, `InfoCircle`, `QuestionCircle`, `XCircle`
- **Finance**: `Bank`, `Card`, `Cash`, `Coins`, `Money`
- **Communication**: `Envelope`, `Phone`, `Bubbles`, `SupportChat`
- **Objects**: `Heart`, `Star`, `Gear`, `Lock`, `Key`, `Shield`, `User`, `Users`
- **Documents**: `File`, `FileText`, `FileChart`
- **Commerce**: `ShoppingBag`, `Store`, `Briefcase`

Use with: `LemonadeIcons.Heart`, `LemonadeIcons.ChevronRight`, etc.

---

## Country Flags & Brand Logos

### Country Flags

`LemonadeCountryFlags` enum with 200+ entries using Alpha-2 code naming:

```kotlin
LemonadeCountryFlags.BRBrazil
LemonadeCountryFlags.USUnitedStates
LemonadeCountryFlags.GBUnitedKingdom
LemonadeCountryFlags.PTPortugal
// ... etc
```

### Brand Logos

`LemonadeBrandLogos` enum for brand logo assets.

---

## Annotations

### @ExperimentalLemonadeComponent

Components marked with this annotation have an API that may change. Opt-in is required:

```kotlin
@OptIn(ExperimentalLemonadeComponent::class)
LemonadeUi.Button(
    label = "Click me",
    onClick = { },
)
```

Currently experimental: `Button`, `SearchField`

### @InternalLemonadeApi

Marks APIs as internal to the Lemonade library. These should not be used by consumers.

---

## Platform-Specific Behavior

The library uses Kotlin `expect`/`actual` for platform-specific sizing:

- **Source sets**: `commonMain` (shared), `mobileMain` (Android + iOS), `desktopMain` (JVM)
- **Desktop components** are slightly smaller (e.g., Switch thumb: 14dp desktop vs 22dp mobile)
- Platform-specific files use the naming convention: `Component.mobile.kt`, `Component.desktop.kt`

---

## Code Conventions

When writing code that uses the Lemonade library, follow these conventions (from the project's kotlin-conventions skill):

1. **Explicit API mode** - All public declarations need explicit visibility modifiers and return types
2. **Named parameters** - Always use named parameters in function calls
3. **One parameter per line** - Each parameter on its own line
4. **No single-expression functions** - Use block body with explicit `return`
5. **Chain calls on separate lines** - Break on each `.method()`
6. **Elvis on new line** - `?:` goes on a new line
7. **Named lambda parameters** - No implicit `it`, use `{ item -> ... }`
8. **Separate when subject** - Declare variable before `when`

**Example following all conventions**:
```kotlin
@Composable
public fun MyScreen() {
    var checked by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = LemonadeTheme.spaces.spacing400),
    ) {
        LemonadeUi.Text(
            text = "Welcome to Lemonade",
            textStyle = LemonadeTheme.typography.headingSmall,
        )

        LemonadeUi.Switch(
            checked = checked,
            onCheckedChange = { newValue ->
                checked = newValue
            },
        )

        @OptIn(ExperimentalLemonadeComponent::class)
        LemonadeUi.Button(
            label = "Submit",
            onClick = { /* handle action */ },
            variant = LemonadeButtonVariant.Primary,
            size = LemonadeButtonSize.Large,
        )
    }
}
```

---

## Key Imports

```kotlin
// Core UI entry point
import com.teya.lemonade.LemonadeUi
import com.teya.lemonade.LemonadeTheme

// Theme function
import com.teya.lemonade.LemonadeTheme

// Component enums and data classes (from core module)
import com.teya.lemonade.core.LemonadeButtonVariant
import com.teya.lemonade.core.LemonadeButtonSize
import com.teya.lemonade.core.LemonadeIconButtonVariant
import com.teya.lemonade.core.LemonadeIconButtonSize
import com.teya.lemonade.core.LemonadeIcons
import com.teya.lemonade.core.LemonadeAssetSize
import com.teya.lemonade.core.LemonadeCardPadding
import com.teya.lemonade.core.LemonadeCardBackground
import com.teya.lemonade.core.CheckboxStatus
import com.teya.lemonade.core.SelectListItemType
import com.teya.lemonade.core.LemonadeListItemVoice
import com.teya.lemonade.core.TagVoice
import com.teya.lemonade.core.SymbolContainerVoice
import com.teya.lemonade.core.SymbolContainerSize
import com.teya.lemonade.core.TabButtonProperties
import com.teya.lemonade.core.LemonadeCountryFlags
import com.teya.lemonade.core.LemonadeBrandLogos
import com.teya.lemonade.core.LemonadeShadow
import com.teya.lemonade.core.DividerVariant

// Card header config (from UI module)
import com.teya.lemonade.CardHeaderConfig

// Experimental annotation
import com.teya.lemonade.ExperimentalLemonadeComponent
```
