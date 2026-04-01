# Tabs Component Update Design

**Date:** 2026-04-01
**Branch:** feat/swiftui-tabs
**Figma:** https://www.figma.com/design/91S16rhVrl5wivqV66fNjm/?node-id=8122-11819&m=dev

## Summary

Update the Tabs component on both Android (KMP/Compose) and iOS (SwiftUI) to match the Figma design spec. Key changes: new tab item structure with icon support, redesigned active indicator, animated tab switching, fade scroll hint, disabled state, stretch/hug layout modes, and Liquid Glass on iOS 26+.

## API

### Android (Kotlin/Compose)

```kotlin
public data class TabItem(
    val label: String,
    val icon: LemonadeIcons? = null,
    val isDisabled: Boolean = false,
)

public enum class TabsItemSize { Hug, Stretch }

@Composable
public fun LemonadeUi.Tabs(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    itemsSize: TabsItemSize = TabsItemSize.Hug,
    modifier: Modifier = Modifier,
)
```

### iOS (SwiftUI)

```swift
public struct LemonadeTabItem {
    public let label: String
    public let icon: LemonadeIcon?
    public let isDisabled: Bool
    
    public init(label: String, icon: LemonadeIcon? = nil, isDisabled: Bool = false)
}

@ViewBuilder
static func Tabs(
    tabs: [LemonadeTabItem],
    selectedIndex: Int,
    onTabSelected: @escaping (Int) -> Void,
    itemsSize: TabItemsSize = .hug
) -> some View
```

## Layout Structure

```
Tabs Container (full width)
+-- ZStack
|   +-- ScrollView (.horizontal, hug mode) / HStack (stretch mode)
|   |   +-- Tab Button (min-height: 44, h-padding: spacing200)
|   |   |   +-- Content Wrapper (h-padding: spacing200, v-padding: spacing100, radius: radius150, gap: spacing100)
|   |   |   |   +-- Icon (16x16, optional, Small size)
|   |   |   |   +-- Label text
|   |   |   +-- Active Indicator (3px height, bgBrandHigh, top-corners 2px, animated position)
|   |   +-- Tab Button ...
|   +-- Fade overlay (trailing edge, 15% width gradient, when scrollable & not at end)
+-- Bottom separator (border25, borderNeutralLow)
```

## Visual Tokens

| Aspect | Value |
|--------|-------|
| Selected text style | `bodySmallSemiBold` (weight 600) |
| Unselected text style | `bodySmallMedium` (weight 500) |
| Selected text/icon color | `contentBrand` |
| Unselected text color | `contentSecondary` |
| Unselected icon color | `contentPrimary` |
| Indicator height | 3px |
| Indicator color | `bgBrandHigh` |
| Indicator corners | 2px top-left, 2px top-right |
| Content wrapper radius | `radius150` (6px) |
| Content wrapper padding | h: `spacing200`, v: `spacing100` |
| Content wrapper gap | `spacing100` (icon to label) |
| Tab button padding | h: `spacing200` |
| Tab button min-height | 44px (size1100) |
| Bottom separator height | `border25` |
| Bottom separator color | `borderNeutralLow` |
| Fade width | 15% of tabs container width |

## Interaction States

### Unselected Tab
- **Rest**: transparent bg, `contentSecondary` text, `contentPrimary` icon
- **Hover**: `bgSubtleInteractive` bg on content wrapper, `contentPrimary` text + icon
- **Pressed**: opacity `opacityPressed` on entire tab button
- **Focus**: 2px `borderSelected` ring on content wrapper
- **Disabled**: reduced opacity, no interaction

### Selected Tab
- **Rest**: transparent bg, `contentBrand` text + icon, active indicator bar
- **Hover**: `bgSubtleInteractive` bg on content wrapper
- **Pressed**: opacity `opacityPressed` on entire tab button
- **Focus**: 2px `borderSelected` ring on content wrapper + active indicator

## Animations

| Animation | Android | iOS |
|-----------|---------|-----|
| Indicator slide | Animated offset + width when selection changes | `matchedGeometryEffect` on indicator |
| Scroll to selected | `animateScrollTo()` on coroutine | `ScrollViewReader.scrollTo` with `.animation` |
| Fade gradient | Reactive to scroll offset | Reactive to scroll offset |
| Press state | Opacity via `Indication` or `animateFloatAsState` | `.opacity` modifier |

## Liquid Glass (iOS 26+)

- Apply `.glassEffect()` to the active indicator bar for translucent brand-tinted appearance
- Gate behind `@available(iOS 26, *)` with solid color fallback
- Content wrapper on hover/focus can use glass material background

## Text Overflow

When text doesn't fit, align left and truncate with ellipsis (single line).

## Scroll Behavior

- Hug mode: tabs scroll horizontally
- Trailing fade gradient (15% width) hints at more content
- Fade disappears when scrolled to the end
- On tab selection, auto-scroll to keep selected tab visible

## Stretch Mode

- All tabs divide available width equally
- No scroll, no fade
- Already exists on iOS, needs to be added to Android
