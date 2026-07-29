---
applyTo: "kmp/{ui,expressive,calendar}/src/**/*.kt,swiftui/Sources/Lemonade/**/*.swift"
---

# Proposal: text scaling across the design system

**Status:** proposal — not yet agreed. Once agreed, this becomes the standing rule for
any component that wraps text in a fixed dimension.
**Reference implementation:** [#298](https://github.com/saltpay/lemonade-design-system/pull/298) (`BottomTabBar`, MOP-342)

## Why

Fixing the `BottomTabBar` for MOP-342 turned up a bug that is not specific to that
component. The same pattern is spread across both platforms, and in the most-used
component in the system it clips text on a stock Android phone.

This document proposes a contract, a fix recipe per platform, and a rollout order.

## The pattern

A component sets a **fixed height in `dp`/`pt`** around content that includes text,
whose height scales with the user's text-size setting. The budget is sized for the
default text scale, so it has little or no headroom. On Compose the excess is then
removed by a `clip`; on SwiftUI the text spills outside its own background.

```kotlin
// The shape of the bug, in Compose
Row(
    modifier = Modifier
        .requiredHeight(height = 48.dp)   // fixed
        .clip(shape = pillShape)          // and clipped
) {
    LemonadeUi.Text(text = label)         // scales with fontScale
}
```

It is one root cause, not N unrelated bugs. The SwiftUI package is a port of the KMP
one, so the same components carry the same fixed heights at mirrored line numbers.

### Why it is hard to notice

The budget usually closes *exactly* at the default text scale. `BottomTabBar` was
`16dp padding + 20dp icon + 2dp spacer + 16sp line height = 54dp`, against a fixed
`ItemHeight = 54.dp`. Nothing looks wrong until someone changes their text size, and
then it breaks at the very first step above default.

## Evidence

Measured on a Pixel 10a and a Pixel 7 emulator (Android), and an iPhone 17 simulator
running iOS 26.4 (SwiftUI). Screenshots are attached to the PR.

| component | platform | finding |
|---|---|---|
| `BottomTabBar` | KMP | **Confirmed.** Instrumented with `onTextLayout`: every label reports `didOverflowHeight = true` at `fontScale 1.15`. At `2.0` the label is given `42px` of the `84px` it needs — exactly half the glyph. Fixed in #298. |
| `Button` | KMP | **Confirmed.** Labels visibly clipped at `fontScale 2.0` across all four sizes and both variants. `Button.kt:428` — `.requiredHeight(...)` followed by `.clip(...)`. |
| `SegmentedControl` | KMP | **Confirmed zero headroom.** At `fontScale 2.0` the `Large` size renders with the text exactly filling the container — not clipped, but no padding left. Smaller sizes are computed to clip earlier. `SegmentedControl.kt:210`. |
| `SegmentedControl` | SwiftUI | **Confirmed.** At Dynamic Type `AX5` the labels overflow the pill vertically — the descender of "Day" sits outside the container. `LemonadeSegmentedControl.swift:167,182`. |
| `Badge` | SwiftUI | **Confirmed.** At `AX5` the text extends well above and below the capsule. `LemonadeBadge.swift:110`. |
| `Tabs` | SwiftUI | **Correct today.** Uses `.frame(minHeight:)` rather than `.frame(height:)`, and grows cleanly at `AX5`. This is the counterexample that proves the rule. |
| `Button` | SwiftUI | Survives `AX5` at the default size. Other sizes not yet checked. |
| `InlineCalendar` | KMP | **Different failure.** Branches layout on `density.fontScale > 1.3` (`InlineCalendar.kt:57,194`). See "Do not branch on `fontScale`" below. |

### Computed break points for `Button` (KMP)

Budget is `requiredHeight − 2 × verticalPadding`, against `lineHeight × scale`:

| size | `requiredHeight` | vertical padding | available | line height | clips above |
|---|---|---|---|---|---|
| `XSmall` | `size800` 32dp | `spacing100` 4dp | 24dp | 20sp | **1.20** |
| `Small` | `size1000` 40dp | `spacing200` 8dp | 24dp | 20sp | **1.20** |
| `Medium` | `size1200` 48dp | `spacing300` 12dp | 24dp | 24sp | **1.00** |
| `Large` | `size1400` 56dp | `spacing300` 12dp | 32dp | 24sp | **1.33** |

These are arithmetic from the tokens, not measurements. The same arithmetic predicted
`BottomTabBar` would have zero headroom at `1.0`, which the instrumented run confirmed,
so the method is sound — but each component should be verified rather than trusted.

`Medium` having zero headroom at the default scale is worth reading twice.

## What the platforms actually say

**Android.** Android 14 raised the maximum from 130% to **200%**, and made scaling above
100% **non-linear** — small text grows up to 2×, already-large text grows barely at all.
As a direct consequence `scaledDensity` is deprecated and the docs state `fontScale`
should be used *"for informational purposes only, because fonts are no longer scaled with
a single scalar value."*

**Apple.** Dynamic Type has 12 sizes: 7 standard (`xSmall` … `xxxLarge`, with `Large` the
default) plus 5 accessibility sizes `AX1`–`AX5`, the latter only when "Larger Accessibility
Sizes" is on. Body text reaches roughly **310%** at `AX5`. The canonical threshold in code
is `DynamicTypeSize.isAccessibilitySize`, true from `AX1` up — Apple draws that line for us.

**Apple, on bars specifically.** WWDC24 "Get started with Dynamic Type" is explicit that
bar heights should *not* scale: *"If the tab bar height were to increase when large text is
enabled, it would occupy almost a quarter of the screen."* It is equally explicit that you
should not simply drop content: *"ensure that functionality and essential content are not
lost."* Apple resolves the tension with the **Large Content Viewer** — press and hold a bar
control and the system shows a large icon and label.

We have no Large Content Viewer on Android, so for bars we cannot have both. Material
sanctions the other side of the trade: `labelVisibilityMode` includes `UNLABELED`, and its
`AUTO` mode already shows the label only on the selected item once there are 4+ items.

## Proposed contract

1. **Target: 200% on Android, `AX5` on iOS.** The union of both platform maxima, and what
   WCAG 1.4.4 (Resize Text) asks for.
2. **No component may clip text within the target.** Any fixed dimension that wraps text
   becomes a floor, not a cap.
3. **Never branch on `fontScale`.** Measure instead. Non-linear scaling on Android 14+ means
   the same `fontScale` produces different rendered sizes depending on the base text size,
   so a threshold is calibrated against a number the platform tells us not to trust.
4. **Bars are the exception to rule 2** — they may stay compact and drop labels, provided the
   label remains available to assistive technology as a content description.
5. **Icons do not scale with text.** Both platforms agree; keep `LemonadeAssetSize` in `dp`/`pt`.

## Fix recipes

### KMP / Compose — turn the cap into a floor

```kotlin
// Before — fixed height plus clip removes the overflow
Modifier
    .requiredHeight(height = size.requiredHeight)
    .clip(shape = size.shape)

// After — floor, so the component grows instead of cutting
Modifier
    .heightIn(min = size.requiredHeight)
    .clip(shape = size.shape)
```

Where a parent needs a concrete height to lay siblings out against — an overlay, a
selection indicator — drive it from the content and let the sibling fill it:

```kotlin
Box(
    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min),
) {
    Box(modifier = Modifier.fillMaxHeight())   // indicator tracks the row
    Row { /* items, each .heightIn(min = ItemHeight) */ }
}
```

`IntrinsicSize.Min` here is a **height** intrinsic. Compose's `HorizontalFloatingToolbar`
queries **width** intrinsics and a `SubcomposeLayout` throws on those — which is why
`BoxWithConstraints` is not an option inside it.

### KMP / Compose — measure, do not threshold

When a component genuinely has to change shape because the text no longer fits, measure the
text against the space it has:

```kotlin
@Composable
private fun rememberLabelsFit(items: List<Item>, rowWidthPx: Int): Boolean {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = LemonadeTheme.typography.bodyXSmallMedium.textStyle
    val gutterPx = with(LocalDensity.current) {
        (LemonadeTheme.spaces.spacing100 * 2).roundToPx()
    }

    return remember(rowWidthPx, items, labelStyle, textMeasurer, gutterPx) {
        if (rowWidthPx == 0) return@remember true
        val availableWidthPx = rowWidthPx / items.size - gutterPx
        items.all { item ->
            textMeasurer.measure(
                text = item.label,
                style = labelStyle,
                maxLines = 1,
            ).size.width <= availableWidthPx
        }
    }
}
```

`rememberTextMeasurer()` measures without laying out, so it works where
`SubcomposeLayout` cannot.

### SwiftUI — `minHeight`, or `@ScaledMetric`

```swift
// Before
.frame(height: size.containerHeight)

// After — the Tabs component already does this and behaves correctly
.frame(minHeight: size.containerHeight)
```

Where a dimension has to stay fixed relative to the text — an inset, a corner radius, a
thumb — scale it with the text instead of pinning it:

```swift
@ScaledMetric(relativeTo: .body) private var containerHeight: CGFloat = 48
```

There are currently **zero** uses of `@ScaledMetric` in `swiftui/Sources/Lemonade`.

### SwiftUI — the accessibility threshold, where a layout switch is needed

```swift
@Environment(\.dynamicTypeSize) private var dynamicTypeSize

var layout: AnyLayout {
    dynamicTypeSize.isAccessibilitySize
        ? AnyLayout(VStackLayout())
        : AnyLayout(HStackLayout())
}
```

This is the one place a threshold is right, because Apple defines it rather than us
picking a number.

## Effects to expect

**Android.** Components get taller as the user's text size grows. That is the intended
outcome, but it moves layouts: a `Button` at `Medium` currently occupies exactly 48dp and
would grow past it above the default scale. Any consumer that pins a row height around a
Lemonade component will need to be checked. `requiredHeight` → `heightIn` also means the
component now respects incoming constraints where before it overrode them — worth calling
out per component, since a few rely on that override to escape a cramped parent.

**iOS.** Less visible movement, because SwiftUI's `.frame(height:)` never clipped — the text
was already drawing outside its background. Switching to `minHeight` makes the background
grow to contain the text, so components that *looked* broken will start looking correct and
occupying more space. Expect layout shifts in the sample app and in consumer screens at
accessibility sizes, and essentially none at default Dynamic Type.

**Both.** No public API changes are expected — these are internal layout modifiers. The
`BottomTabBar` fix landed as `ADDITIONS_ONLY`, with the only baseline movement being a
name-mangled internal Compose singleton re-hashing.

## Proposed rollout

Ordered by blast radius, most-used first. Each step is its own PR, verified on device at
the default scale, 200% on Android and `AX5` on iOS.

1. **`Button`** — confirmed broken on Android, highest usage. KMP + SwiftUI together.
2. **`SegmentedControl`** — confirmed broken on iOS, zero headroom on Android.
3. **`Badge`, `Tag`, `Chip`** — small text containers, same pattern, cheap to fix as a group.
4. **`SearchField`, `TextField`, `PinCode`, `SelectField`** — input heights; needs care because
   the cursor and the platform text field have their own metrics.
5. **`InlineCalendar`** — replace the `fontScale > 1.3` branch with a measured decision.
6. **Everything else** — sweep the remaining fixed dimensions listed by the audit command below.

### Audit commands

As of this proposal these return **26** fixed dimensions in the published KMP modules,
**2** `fontScale` references (both the `InlineCalendar` branch) and **21** fixed frame
heights in SwiftUI.

```bash
# KMP — fixed dimensions in published modules
grep -rn "\.height(height =\|\.requiredHeight(\|\.requiredSize(" \
  --include='*.kt' kmp/ui/src/commonMain kmp/expressive/src/commonMain kmp/calendar/src/commonMain \
  | grep -v "IntrinsicSize\|heightIn"

# KMP — fontScale branching
grep -rn "fontScale" --include='*.kt' kmp/*/src

# SwiftUI — fixed frame heights
grep -rn "\.frame(height:" swiftui/Sources/Lemonade/Components/ | grep -v "minHeight\|maxHeight"
```

Not every hit is a bug — `Icon`, `BrandLogo`, `CountryFlag`, `Checkbox`, `RadioButton`,
`SymbolContainer` and the `Tabs` indicator are fixed dimensions around *non-text* content and
should stay fixed. The rule is only about dimensions that wrap text.

### Regression cover

Manual device checks do not scale to a sweep this size. Worth deciding as part of this
proposal whether to add screenshot tests at default / 200% / `AX5`, so the contract is
enforced rather than re-verified by hand each time.

## Open questions

1. **Bars: how far do we take the exception?** `BottomTabBar` drops labels when they no longer
   fit. Apple says do not lose functionality, and compensates with the Large Content Viewer,
   which Android does not have. Do we accept dropping labels on both platforms for symmetry,
   or use the Large Content Viewer on iOS and only drop on Android?
2. **Long labels at the default scale.** The measured approach in #298 can drop labels to
   icon-only at `fontScale 1.0` if a consumer passes a long label on a compact device, where
   before it ellipsised. Intended, but it is a behaviour change — should it have a floor?
3. **Screenshot tests** — in scope for this work, or a separate track?

## References

- [Get started with Dynamic Type — WWDC24](https://developer.apple.com/videos/play/wwdc2024/10074/)
- [`DynamicTypeSize.isAccessibilitySize` — Apple Developer](https://developer.apple.com/documentation/swiftui/dynamictypesize/isaccessibilitysize)
- [Android 14 features and APIs — non-linear font scaling](https://developer.android.com/about/versions/14/features)
- [Android 14 non-linear text scaling migration — Flutter docs](https://docs.flutter.dev/release/breaking-changes/android-14-nonlinear-text-scaling-migration)
- [Bottom navigation `labelVisibilityMode` — Material Components Android](https://github.com/material-components/material-components-android/blob/master/docs/components/BottomNavigation.md)
- [Navigation bar accessibility — Material Design 3](https://m3.material.io/components/navigation-bar/accessibility)
- [Supporting Dynamic Type — Create with Swift](https://www.createwithswift.com/supporting-dynamic-type-and-larger-text-in-your-app-to-enhance-accessibility/)
- WCAG 2.2 [1.4.4 Resize Text](https://www.w3.org/WAI/WCAG22/Understanding/resize-text.html)
