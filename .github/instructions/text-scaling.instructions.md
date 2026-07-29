---
applyTo: "kmp/{ui,expressive,calendar}/src/**/*.kt,swiftui/Sources/Lemonade/**/*.swift"
---

# Text scaling across the design system

Status: proposal, not agreed yet. If we agree on it, it becomes the rule for any component
that puts text inside a fixed dimension.

Reference implementation: [#298](https://github.com/saltpay/lemonade-design-system/pull/298)
(`BottomTabBar`, MOP-342).

## Why this exists

While fixing `BottomTabBar` for MOP-342 I went looking for whether the same mistake lived
anywhere else. It does. `Button` clips its label on a stock Android phone at 200% text, and
`Button` is the most used component we ship.

This proposes a contract, the fix for each platform, and an order to work through them.

## The pattern

Someone sets a height in `dp` or `pt` around content that contains text. The text grows with
the user's text-size setting. The height does not. The budget is usually drawn for the default
scale with nothing to spare, and then Compose removes the overflow with a `clip`. SwiftUI
does not clip, so the text just draws outside its own background.

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

It is the same bug every time. The SwiftUI package is a port of the KMP one, so the same
components carry the same fixed heights, sometimes at the same line numbers.

### Why nobody caught it

The budget tends to close exactly at the default scale. `BottomTabBar` was 16dp padding +
20dp icon + 2dp spacer + 16sp line height, against a fixed `ItemHeight` of 54.dp. Adds up to
54 on the nose. Nothing looks wrong until a user touches their text size, and then it breaks
on the first step up.

## What I found

Checked on a Pixel 10a and a Pixel 7 emulator for Android, and an iPhone 17 simulator running
iOS 26.4 for SwiftUI. Screenshots are in the PR.

| component | platform | status | what happens |
|---|---|---|---|
| `BottomTabBar` | KMP | confirmed | Instrumented with `onTextLayout`. Every label reports `didOverflowHeight = true` at `fontScale 1.15`. At `2.0` the label gets `42px` of the `84px` it needs, so you see exactly half the glyph. Fixed in #298. |
| `Button` | KMP | confirmed | Labels clipped at `fontScale 2.0` on all four sizes and both variants. `Button.kt:428` does `.requiredHeight()` then `.clip()`. |
| `SegmentedControl` | KMP | confirmed | At `2.0` the `Large` size fills its container exactly. Not clipped, but nothing left over. `SegmentedControl.kt:210`. |
| `SegmentedControl` | SwiftUI | confirmed | At `AX5` the labels sit outside the pill. The descender on "Day" hangs below it. `LemonadeSegmentedControl.swift:167,182`. |
| `Badge` | SwiftUI | confirmed | At `AX5` the text runs above and below the capsule. `LemonadeBadge.swift:110`. |
| `Tabs` | SwiftUI | fine today | Uses `.frame(minHeight:)` instead of `.frame(height:)`, and grows properly at `AX5`. |
| `Button` | SwiftUI | partly checked | The default size survives `AX5`. I did not check the others. |
| `InlineCalendar` | KMP | different problem | Branches on `density.fontScale > 1.3`. `InlineCalendar.kt:57,194`. |

### Button break points, by arithmetic

Budget is `requiredHeight` minus twice the vertical padding, against `lineHeight × scale`.

| size | `requiredHeight` | vertical padding | available | line height | clips above |
|---|---|---|---|---|---|
| `XSmall` | `size800` 32dp | `spacing100` 4dp | 24dp | 20sp | 1.20 |
| `Small` | `size1000` 40dp | `spacing200` 8dp | 24dp | 20sp | 1.20 |
| `Medium` | `size1200` 48dp | `spacing300` 12dp | 24dp | 24sp | 1.00 |
| `Large` | `size1400` 56dp | `spacing300` 12dp | 32dp | 24sp | 1.33 |

That is arithmetic off the tokens, not measurement. The same arithmetic said `BottomTabBar`
would have zero headroom at 1.0 and the instrumented run agreed, so I trust the method. I
would still check each component rather than take the table on faith.

`Medium` having no headroom at all at the default scale is the number I would look at first.

## What the platforms say

Android 14 took the ceiling from 130% to 200% and made anything above 100% non-linear. Small
text can double; text that is already big barely moves. That is why `scaledDensity` is
deprecated, and why the docs now say `fontScale` is *"for informational purposes only, because
fonts are no longer scaled with a single scalar value."*

Apple has twelve Dynamic Type sizes. Seven standard ones, `xSmall` through `xxxLarge` with
`Large` as the default, plus five accessibility sizes `AX1` to `AX5` that only appear once the
user turns on Larger Accessibility Sizes. Body text lands around 310% at `AX5`. In code the
line is `DynamicTypeSize.isAccessibilitySize`, true from `AX1` up. Apple picked that threshold
so we do not have to.

On bars specifically, WWDC24 "Get started with Dynamic Type" says bar heights should not scale:
*"If the tab bar height were to increase when large text is enabled, it would occupy almost a
quarter of the screen."* The same talk says not to drop content either: *"ensure that
functionality and essential content are not lost."* Apple gets to have both because of the
Large Content Viewer, where pressing and holding a bar control brings up a large icon and label.

Android has no equivalent, so for bars we have to give one of them up. Material is comfortable
with that. `labelVisibilityMode` has an `UNLABELED` option, and its `AUTO` mode already hides
labels on unselected items once you pass four.

## The contract

1. Support 200% on Android and `AX5` on iOS. That covers both platform ceilings, and roughly
   what WCAG 1.4.4 asks for.
2. Nothing clips text inside that range. A fixed dimension around text becomes a floor.
3. Do not branch on `fontScale`. Measure. With non-linear scaling the same `fontScale` gives
   different rendered sizes depending on the base size, so any threshold is calibrated against
   a number Android tells us not to read.
4. Bars can opt out of rule 2. They may stay compact and drop labels, as long as the label
   survives as a content description.
5. Icons stay fixed. Both platforms agree there, so `LemonadeAssetSize` stays in `dp` and `pt`.

## Fix recipes

### Compose: make the cap a floor

```kotlin
// Before: fixed height, and the clip removes whatever spills
Modifier
    .requiredHeight(height = size.requiredHeight)
    .clip(shape = size.shape)

// After: a floor, so the component grows rather than cutting
Modifier
    .heightIn(min = size.requiredHeight)
    .clip(shape = size.shape)
```

Where a parent needs a real height to position a sibling against, a selection indicator say,
take the height from the content and let the sibling fill it:

```kotlin
Box(
    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min),
) {
    Box(modifier = Modifier.fillMaxHeight())   // indicator tracks the row
    Row { /* items, each .heightIn(min = ItemHeight) */ }
}
```

One caveat. `IntrinsicSize.Min` there is a height intrinsic. `HorizontalFloatingToolbar` asks
its content for width intrinsics, and `SubcomposeLayout` throws when you do that, which is why
`BoxWithConstraints` is off the table inside it.

### Compose: measure instead of guessing

If a component really has to change shape once the text stops fitting, measure the text against
the room it has:

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

`rememberTextMeasurer()` measures without laying anything out, so it works in the places
`SubcomposeLayout` cannot.

### SwiftUI: minHeight, or @ScaledMetric

```swift
// Before
.frame(height: size.containerHeight)

// After, which is what Tabs already does
.frame(minHeight: size.containerHeight)
```

For dimensions that have to track the text rather than sit still, an inset or a thumb, scale
them instead of pinning them:

```swift
@ScaledMetric(relativeTo: .body) private var containerHeight: CGFloat = 48
```

Right now nothing in `swiftui/Sources/Lemonade` uses `@ScaledMetric`.

### SwiftUI: switching layout at accessibility sizes

```swift
@Environment(\.dynamicTypeSize) private var dynamicTypeSize

var layout: AnyLayout {
    dynamicTypeSize.isAccessibilitySize
        ? AnyLayout(VStackLayout())
        : AnyLayout(HStackLayout())
}
```

This is the one threshold worth keeping, because Apple defines it and we do not.

## What will move

On Android, components get taller as text grows. That is the point, but it shifts layouts. A
`Button` at `Medium` sits at exactly 48dp today and will pass that above the default scale, so
any consumer pinning a row height around one of our components needs a look. Going from
`requiredHeight` to `heightIn` also means the component starts respecting incoming constraints
instead of overriding them. Worth flagging per component, because a few lean on that override
to get out of a cramped parent.

On iOS the movement is less obvious, since `.frame(height:)` never clipped anything. The text
was already drawing outside its background. Moving to `minHeight` makes the background grow to
hold it, so things that looked wrong start looking right and take up more room. Expect shifts
in the sample app and in consumer screens at accessibility sizes, and close to nothing at
default Dynamic Type.

No public API changes either way. These are internal layout modifiers. The `BottomTabBar` fix
came out as `ADDITIONS_ONLY`, and the only baseline movement was an internal Compose singleton
re-hashing.

## Order of work

Most used first. Each one is its own PR, checked on device at the default scale, at 200% on
Android and at `AX5` on iOS.

1. `Button`. Confirmed broken on Android and used everywhere. KMP and SwiftUI in one go.
2. `SegmentedControl`. Confirmed broken on iOS, no headroom on Android.
3. `Badge`, `Tag`, `Chip`. Small text containers with the same problem, cheap to do together.
4. `SearchField`, `TextField`, `PinCode`, `SelectField`. Input heights. These need more care,
   because the cursor and the platform text field bring their own metrics.
5. `InlineCalendar`. Swap the `fontScale > 1.3` branch for something measured.
6. The rest, from the audit below.

### Audit commands

Today these come back with 26 fixed dimensions in the published KMP modules, 2 `fontScale`
references (both the `InlineCalendar` branch), and 21 fixed frame heights in SwiftUI.

```bash
# KMP, fixed dimensions in published modules
grep -rn "\.height(height =\|\.requiredHeight(\|\.requiredSize(" \
  --include='*.kt' kmp/ui/src/commonMain kmp/expressive/src/commonMain kmp/calendar/src/commonMain \
  | grep -v "IntrinsicSize\|heightIn"

# KMP, fontScale branching
grep -rn "fontScale" --include='*.kt' kmp/*/src

# SwiftUI, fixed frame heights
grep -rn "\.frame(height:" swiftui/Sources/Lemonade/Components/ | grep -v "minHeight\|maxHeight"
```

Plenty of those hits are fine. `Icon`, `BrandLogo`, `CountryFlag`, `Checkbox`, `RadioButton`,
`SymbolContainer` and the `Tabs` indicator are fixed dimensions around things that are not
text, and they should stay fixed. The rule only covers dimensions that wrap text.

### Keeping it fixed

Hand checking on a device does not scale to a sweep this size. Part of agreeing this proposal
is deciding whether we add screenshot tests at default, 200% and `AX5`, so the contract holds
itself up instead of needing another afternoon of somebody poking at emulators.
[#199](https://github.com/saltpay/lemonade-design-system/pull/199) already adds Roborazzi
screenshot testing for `:ui` and may be the right place to hang this.

## Open questions

1. How far does the bar exception go? `BottomTabBar` drops labels once they stop fitting.
   Apple says do not lose functionality and pays for it with the Large Content Viewer, which
   Android does not have. Do we drop labels on both platforms so they match, or use the Large
   Content Viewer on iOS and only drop on Android?
2. Long labels at the default scale. The measured approach in #298 can go icon-only at
   `fontScale 1.0` if someone passes a long label on a narrow device, where before it
   ellipsised. That is deliberate, but it is still a behaviour change. Should it have a floor?
3. Screenshot tests. Part of this work, or its own track alongside #199?

## References

- [Get started with Dynamic Type, WWDC24](https://developer.apple.com/videos/play/wwdc2024/10074/)
- [`DynamicTypeSize.isAccessibilitySize`, Apple Developer](https://developer.apple.com/documentation/swiftui/dynamictypesize/isaccessibilitysize)
- [Android 14 features and APIs, non-linear font scaling](https://developer.android.com/about/versions/14/features)
- [Android 14 non-linear text scaling migration, Flutter docs](https://docs.flutter.dev/release/breaking-changes/android-14-nonlinear-text-scaling-migration)
- [Bottom navigation `labelVisibilityMode`, Material Components Android](https://github.com/material-components/material-components-android/blob/master/docs/components/BottomNavigation.md)
- [Navigation bar accessibility, Material Design 3](https://m3.material.io/components/navigation-bar/accessibility)
- [Supporting Dynamic Type, Create with Swift](https://www.createwithswift.com/supporting-dynamic-type-and-larger-text-in-your-app-to-enhance-accessibility/)
- WCAG 2.2 [1.4.4 Resize Text](https://www.w3.org/WAI/WCAG22/Understanding/resize-text.html)
