# Tabs Component Update Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Update Tabs component on Android (KMP/Compose) and iOS (SwiftUI) to match Figma spec — new layout with icon support, animated indicator, fade scroll hint, disabled state, and Liquid Glass on iOS 26+.

**Architecture:** Both platforms get a new `TabItem` model replacing plain strings. The tab item layout adds a "Content Wrapper" inner layer for hover/focus backgrounds. The active indicator animates position/width on selection change. A trailing fade gradient overlays the scroll area in hug mode.

**Tech Stack:** Kotlin Multiplatform (Compose), SwiftUI, existing Lemonade DS tokens.

---

## Task 1: Android — Add TabItem model and TabsItemSize enum

**Files:**
- Modify: `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/Tabs.kt`

**Step 1: Add the data class and enum at the top of Tabs.kt (after imports, before the Tabs composable)**

```kotlin
/**
 * Represents a single tab with its label, optional icon, and disabled state.
 *
 * @param label The text label for the tab.
 * @param icon Optional [LemonadeIcons] displayed before the label.
 * @param isDisabled Whether the tab is disabled and non-interactive.
 */
public data class TabItem(
    val label: String,
    val icon: LemonadeIcons? = null,
    val isDisabled: Boolean = false,
)

/**
 * Defines how tab items are sized within the Tabs component.
 */
public enum class TabsItemSize {
    /** Each tab hugs its content. Scrollable when items overflow. */
    Hug,
    /** Tabs stretch to fill available width equally. */
    Stretch,
}
```

**Step 2: Update the public Tabs API signature**

Change:
```kotlin
public fun LemonadeUi.Tabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
)
```

To:
```kotlin
public fun LemonadeUi.Tabs(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    itemsSize: TabsItemSize = TabsItemSize.Hug,
    modifier: Modifier = Modifier,
)
```

Pass `itemsSize` through to `CoreTabs`.

**Step 3: Update CoreTabs signature to accept `List<TabItem>` and `TabsItemSize`**

**Step 4: Verify it compiles**

Run: `cd kmp && ./gradlew :ui:compileKotlinDesktop` (or appropriate target)

**Step 5: Commit**

```
feat(tabs): add TabItem model and TabsItemSize enum for Android
```

---

## Task 2: Android — Rewrite tab item layout with Content Wrapper

**Files:**
- Modify: `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/Tabs.kt`

**Step 1: Add required imports**

```kotlin
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teya.lemonade.core.LemonadeAssetSize
```

**Step 2: Rewrite the internal TabItem composable**

Replace the existing `TabItem` composable with this new structure matching Figma:

```kotlin
@Composable
private fun TabItemView(
    tab: TabItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val contentColor = if (isSelected) {
        LocalColors.current.content.contentBrand
    } else {
        LocalColors.current.content.contentSecondary
    }
    val iconColor = if (isSelected) {
        LocalColors.current.content.contentBrand
    } else {
        LocalColors.current.content.contentPrimary
    }
    val textStyle = if (isSelected) {
        LocalTypographies.current.bodySmallSemiBold
    } else {
        LocalTypographies.current.bodySmallMedium
    }

    val wrapperBg by animateColorAsState(
        targetValue = if ((isHovered || isPressed) && !tab.isDisabled) {
            LocalColors.current.interaction.bgSubtleInteractive
        } else {
            Color.Transparent
        },
    )

    val pressedAlpha = if (isPressed && !tab.isDisabled) {
        LocalOpacities.current.state.opacityPressed
    } else {
        1f
    }

    val disabledAlpha = if (tab.isDisabled) {
        LocalOpacities.current.state.opacityDisabled
    } else {
        1f
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .heightIn(min = LocalSizes.current.size1100)
            .alpha(alpha = disabledAlpha)
            .clickable(
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
                enabled = !tab.isDisabled,
            )
            .padding(horizontal = LocalSpaces.current.spacing200),
    ) {
        // Content Wrapper
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = LocalSpaces.current.spacing100,
            ),
            modifier = Modifier
                .alpha(alpha = pressedAlpha)
                .background(
                    color = wrapperBg,
                    shape = LocalShapes.current.radius150,
                )
                .clip(shape = LocalShapes.current.radius150)
                .padding(
                    horizontal = LocalSpaces.current.spacing200,
                    vertical = LocalSpaces.current.spacing100,
                ),
        ) {
            if (tab.icon != null) {
                LemonadeUi.Icon(
                    icon = tab.icon,
                    contentDescription = null,
                    size = LemonadeAssetSize.Small,
                    tint = iconColor,
                )
            }
            LemonadeUi.Text(
                text = tab.label,
                textStyle = textStyle,
                color = contentColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        // Active Indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = LocalBorderWidths.current.base.border75)
                    .background(
                        color = LocalColors.current.background.bgBrandHigh,
                        shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp),
                    ),
            )
        }
    }
}
```

**Step 3: Update CoreTabs to use the new TabItemView and support stretch mode**

```kotlin
@Composable
internal fun CoreTabs(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    itemsSize: TabsItemSize,
    modifier: Modifier = Modifier,
) {
    require(
        value = tabs.isNotEmpty(),
        lazyMessage = { "Tabs list should not be empty." },
    )
    require(
        value = selectedIndex in tabs.indices,
        lazyMessage = {
            "Selected index ($selectedIndex) is out of bounds for tabs list of size ${tabs.size}."
        },
    )

    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        when (itemsSize) {
            TabsItemSize.Hug -> {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(state = scrollState),
                ) {
                    tabs.forEachIndexed { index, tab ->
                        TabItemView(
                            tab = tab,
                            isSelected = index == selectedIndex,
                            onClick = { onTabSelected(index) },
                        )
                    }
                }
            }
            TabsItemSize.Stretch -> {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Box(modifier = Modifier.weight(1f)) {
                            TabItemView(
                                tab = tab,
                                isSelected = index == selectedIndex,
                                onClick = { onTabSelected(index) },
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = LocalBorderWidths.current.base.border25)
                .background(color = LocalColors.current.border.borderNeutralLow),
        )
    }
}
```

**Step 4: Verify it compiles**

**Step 5: Commit**

```
feat(tabs): rewrite tab item layout with content wrapper, icon, and interaction states
```

---

## Task 3: Android — Animated indicator + scroll-to-selected

**Files:**
- Modify: `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/Tabs.kt`

**Step 1: Add animation imports**

```kotlin
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
```

**Step 2: Refactor indicator out of TabItemView and into CoreTabs as an animated overlay**

The indicator should be positioned absolutely within the Row, animating its offset and width when selection changes. Replace the indicator inside `TabItemView` with a shared animated indicator in `CoreTabs`.

In `TabItemView`, remove the "Active Indicator" section (the `if (isSelected)` Box). Instead, track each tab's position and width via `onGloballyPositioned`.

In `CoreTabs`, maintain state for tab positions:

```kotlin
val density = LocalDensity.current
val coroutineScope = rememberCoroutineScope()
val tabWidths = remember { mutableStateMapOf<Int, Dp>() }
val tabOffsets = remember { mutableStateMapOf<Int, Dp>() }

val indicatorWidth by animateDpAsState(
    targetValue = tabWidths[selectedIndex] ?: 0.dp,
    animationSpec = tween(durationMillis = 250),
)
val indicatorOffset by animateDpAsState(
    targetValue = tabOffsets[selectedIndex] ?: 0.dp,
    animationSpec = tween(durationMillis = 250),
)
```

Add `onGloballyPositioned` to each tab item to track position:

```kotlin
TabItemView(
    tab = tab,
    isSelected = index == selectedIndex,
    onClick = { onTabSelected(index) },
    modifier = Modifier.onGloballyPositioned { coordinates ->
        with(density) {
            tabWidths[index] = coordinates.size.width.toDp()
            tabOffsets[index] = coordinates.positionInParent().x.toDp()
        }
    },
)
```

Render the animated indicator as a sibling after the Row, positioned with offset:

```kotlin
if (tabWidths.isNotEmpty() && tabOffsets.containsKey(selectedIndex)) {
    Box(
        modifier = Modifier
            .offset { IntOffset(x = with(density) { indicatorOffset.roundToPx() }, y = 0) }
            .width(width = indicatorWidth)
            .height(height = LocalBorderWidths.current.base.border75)
            .background(
                color = LocalColors.current.background.bgBrandHigh,
                shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp),
            ),
    )
}
```

**Step 3: Add scroll-to-selected behavior**

In CoreTabs, add a `LaunchedEffect` that scrolls to keep the selected tab visible:

```kotlin
LaunchedEffect(selectedIndex) {
    val offset = tabOffsets[selectedIndex] ?: return@LaunchedEffect
    val width = tabWidths[selectedIndex] ?: return@LaunchedEffect
    with(density) {
        val tabStart = offset.roundToPx()
        val tabEnd = tabStart + width.roundToPx()
        val viewportStart = scrollState.value
        val viewportEnd = viewportStart + scrollState.viewportSize

        if (tabStart < viewportStart) {
            scrollState.animateScrollTo(tabStart)
        } else if (tabEnd > viewportEnd) {
            scrollState.animateScrollTo(tabEnd - scrollState.viewportSize)
        }
    }
}
```

**Step 4: Verify animations work in preview**

**Step 5: Commit**

```
feat(tabs): add animated indicator sliding and scroll-to-selected
```

---

## Task 4: Android — Fade scroll overlay

**Files:**
- Modify: `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/Tabs.kt`

**Step 1: Add gradient imports**

```kotlin
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Brush
```

**Step 2: Wrap the Hug-mode row in a Box and add fade overlays**

Replace the bare `Row` in Hug mode with:

```kotlin
TabsItemSize.Hug -> {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val fadeWidth = maxWidth * 0.15f
        val canScrollForward = scrollState.canScrollForward
        val canScrollBackward = scrollState.canScrollBackward

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(state = scrollState),
        ) {
            // tab items...
        }

        // Animated indicator here...

        // Trailing fade
        if (canScrollForward) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(width = fadeWidth)
                    .matchParentSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                LocalColors.current.background.bgDefault,
                            ),
                        ),
                    ),
            )
        }

        // Leading fade
        if (canScrollBackward) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(width = fadeWidth)
                    .matchParentSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                LocalColors.current.background.bgDefault,
                                Color.Transparent,
                            ),
                        ),
                    ),
            )
        }
    }
}
```

**Step 3: Verify in preview with many tabs**

**Step 4: Commit**

```
feat(tabs): add fade scroll overlay for hug mode
```

---

## Task 5: Android — Update previews and TabsDisplay

**Files:**
- Modify: `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/Tabs.kt`
- Modify: `kmp/composeApp/src/commonMain/kotlin/com/teya/lemonade/TabsDisplay.kt`

**Step 1: Update previews in Tabs.kt**

```kotlin
@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun TabsPreview() {
    LemonadeUi.Tabs(
        tabs = listOf(
            TabItem(label = "Overview"),
            TabItem(label = "Details"),
            TabItem(label = "Reviews"),
        ),
        selectedIndex = 1,
        onTabSelected = {},
    )
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun TabsWithIconsPreview() {
    LemonadeUi.Tabs(
        tabs = listOf(
            TabItem(label = "Overview", icon = LemonadeIcons.Home),
            TabItem(label = "Details", icon = LemonadeIcons.Info),
            TabItem(label = "Reviews", icon = LemonadeIcons.Star),
        ),
        selectedIndex = 0,
        onTabSelected = {},
    )
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun TabsStretchPreview() {
    LemonadeUi.Tabs(
        tabs = listOf(
            TabItem(label = "Tab A"),
            TabItem(label = "Tab B"),
            TabItem(label = "Tab C"),
        ),
        selectedIndex = 0,
        onTabSelected = {},
        itemsSize = TabsItemSize.Stretch,
    )
}

@Suppress("UnusedPrivateMember")
@LemonadePreview
@Composable
private fun TabsManyItemsPreview() {
    LemonadeUi.Tabs(
        tabs = listOf(
            TabItem(label = "Dashboard"),
            TabItem(label = "Analytics"),
            TabItem(label = "Reports"),
            TabItem(label = "Settings"),
            TabItem(label = "Users"),
            TabItem(label = "Activity"),
        ),
        selectedIndex = 2,
        onTabSelected = {},
    )
}
```

**Step 2: Update TabsDisplay.kt to use new API**

Update all `listOf("Label", ...)` calls to `listOf(TabItem(label = "Label"), ...)` and add new demo sections for icons, stretch, and disabled states.

**Step 3: Verify it compiles**

**Step 4: Commit**

```
feat(tabs): update previews and demo for new Tabs API
```

---

## Task 6: iOS — Add LemonadeTabItem model and update API

**Files:**
- Modify: `swiftui/Sources/Lemonade/Components/LemonadeTabs.swift`

**Step 1: Replace TabItemsSize comment block and add LemonadeTabItem**

```swift
// MARK: - Tab Item Model

public struct LemonadeTabItem {
    public let label: String
    public let icon: LemonadeIcon?
    public let isDisabled: Bool

    public init(
        label: String,
        icon: LemonadeIcon? = nil,
        isDisabled: Bool = false
    ) {
        self.label = label
        self.icon = icon
        self.isDisabled = isDisabled
    }
}
```

**Step 2: Update the public Tabs API**

Change `tabs: [String]` to `tabs: [LemonadeTabItem]` in the public factory method and internal view.

**Step 3: Update internal `LemonadeTabsView` and `TabItemView`**

Update all references from `String` to `LemonadeTabItem`.

**Step 4: Verify it compiles via Xcode or `swift build`**

**Step 5: Commit**

```
feat(tabs): add LemonadeTabItem model for iOS
```

---

## Task 7: iOS — Rewrite tab item layout with Content Wrapper + icon

**Files:**
- Modify: `swiftui/Sources/Lemonade/Components/LemonadeTabs.swift`

**Step 1: Rewrite TabItemView**

```swift
private struct TabItemView: View {
    let tab: LemonadeTabItem
    let isSelected: Bool
    let stretch: Bool
    let onClick: () -> Void

    @State private var isPressed = false
    @State private var isHovering = false

    private var contentColor: Color {
        isSelected
            ? LemonadeTheme.colors.content.contentBrand
            : LemonadeTheme.colors.content.contentSecondary
    }

    private var iconColor: Color {
        isSelected
            ? LemonadeTheme.colors.content.contentBrand
            : LemonadeTheme.colors.content.contentPrimary
    }

    private var textStyle: LemonadeTextStyle {
        isSelected
            ? LemonadeTypography.shared.bodySmallSemiBold
            : LemonadeTypography.shared.bodySmallMedium
    }

    private var wrapperBackground: Color {
        (isHovering || isPressed) && !tab.isDisabled
            ? LemonadeTheme.colors.interaction.bgSubtleInteractive
            : .clear
    }

    var body: some View {
        SwiftUI.Button(action: onClick) {
            VStack(spacing: 0) {
                // Content Wrapper
                HStack(spacing: LemonadeTheme.spaces.spacing100) {
                    if let icon = tab.icon {
                        LemonadeUi.Icon(
                            icon: icon,
                            contentDescription: nil,
                            size: .small,
                            tint: iconColor
                        )
                    }
                    LemonadeUi.Text(
                        tab.label,
                        textStyle: textStyle,
                        color: contentColor,
                        overflow: .tail,
                        maxLines: 1
                    )
                }
                .padding(.horizontal, LemonadeTheme.spaces.spacing200)
                .padding(.vertical, LemonadeTheme.spaces.spacing100)
                .background(
                    RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius150)
                        .fill(wrapperBackground)
                )
            }
            .padding(.horizontal, LemonadeTheme.spaces.spacing200)
            .frame(minHeight: LemonadeTheme.sizes.size1100)
            .frame(maxWidth: stretch ? .infinity : nil)
        }
        .buttonStyle(.plain)
        .disabled(tab.isDisabled)
        .opacity(tab.isDisabled ? LemonadeTheme.opacity.state.opacityDisabled : 1.0)
        .onHover { hovering in
            isHovering = hovering
        }
        .simultaneousGesture(
            DragGesture(minimumDistance: 0)
                .onChanged { _ in isPressed = true }
                .onEnded { _ in isPressed = false }
        )
        .animation(.easeInOut(duration: 0.15), value: isPressed)
        .animation(.easeInOut(duration: 0.15), value: isHovering)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}
```

**Step 2: Verify it compiles**

**Step 3: Commit**

```
feat(tabs): rewrite iOS tab item with content wrapper, icon, and interaction states
```

---

## Task 8: iOS — Animated indicator with matchedGeometryEffect

**Files:**
- Modify: `swiftui/Sources/Lemonade/Components/LemonadeTabs.swift`

**Step 1: Add namespace and animated indicator to LemonadeTabsView**

```swift
private struct LemonadeTabsView: View {
    let tabs: [LemonadeTabItem]
    let selectedIndex: Int
    let onTabSelected: (Int) -> Void
    let itemsSize: TabItemsSize

    @Namespace private var tabNamespace

    var body: some View {
        VStack(spacing: 0) {
            switch itemsSize {
            case .hug:
                ScrollViewReader { proxy in
                    ScrollView(.horizontal, showsIndicators: false) {
                        tabRow
                    }
                    .onChange(of: selectedIndex) { newIndex in
                        withAnimation(.easeInOut(duration: 0.25)) {
                            proxy.scrollTo(newIndex, anchor: .center)
                        }
                    }
                }
            case .stretch:
                tabRow
            }

            Rectangle()
                .fill(LemonadeTheme.colors.border.borderNeutralLow)
                .frame(height: LemonadeTheme.borderWidth.base.border25)
        }
    }

    private var tabRow: some View {
        HStack(spacing: 0) {
            ForEach(Array(tabs.enumerated()), id: \.offset) { index, tab in
                VStack(spacing: 0) {
                    TabItemView(
                        tab: tab,
                        isSelected: index == selectedIndex,
                        stretch: itemsSize == .stretch,
                        onClick: { onTabSelected(index) }
                    )

                    // Active indicator with animation
                    if index == selectedIndex {
                        UnevenRoundedRectangle(
                            topLeadingRadius: 2,
                            bottomLeadingRadius: 0,
                            bottomTrailingRadius: 0,
                            topTrailingRadius: 2
                        )
                        .fill(LemonadeTheme.colors.background.bgBrandHigh)
                        .frame(height: LemonadeTheme.borderWidth.base.border75)
                        .matchedGeometryEffect(id: "indicator", in: tabNamespace)
                    } else {
                        Color.clear
                            .frame(height: LemonadeTheme.borderWidth.base.border75)
                    }
                }
                .id(index)
            }
        }
    }
}
```

The `matchedGeometryEffect` will automatically animate the indicator sliding from one tab to another. The `ScrollViewReader` with `onChange` handles auto-scroll to selected.

**Step 2: Wrap tab selection in animation**

In the public API or wherever `onTabSelected` fires, ensure the selection change triggers animation:

```swift
onClick: {
    withAnimation(.easeInOut(duration: 0.25)) {
        onTabSelected(index)
    }
}
```

**Step 3: Verify animations in preview**

**Step 4: Commit**

```
feat(tabs): add animated indicator and scroll-to-selected for iOS
```

---

## Task 9: iOS — Fade scroll overlay

**Files:**
- Modify: `swiftui/Sources/Lemonade/Components/LemonadeTabs.swift`

**Step 1: Add scroll offset tracking and fade overlays for hug mode**

Wrap the ScrollView in a `GeometryReader` + `ZStack` to overlay fades:

```swift
case .hug:
    ScrollViewReader { proxy in
        GeometryReader { geometry in
            let fadeWidth = geometry.size.width * 0.15

            ZStack(alignment: .leading) {
                ScrollView(.horizontal, showsIndicators: false) {
                    tabRow
                        .background(
                            GeometryReader { contentGeo in
                                Color.clear.preference(
                                    key: ScrollOffsetPreferenceKey.self,
                                    value: contentGeo.frame(in: .named("scroll")).origin.x
                                )
                            }
                        )
                }
                .coordinateSpace(name: "scroll")
                .onPreferenceChange(ScrollOffsetPreferenceKey.self) { value in
                    scrollOffset = value
                    // Calculate if we can scroll further
                }

                // Trailing fade
                if showTrailingFade {
                    LinearGradient(
                        colors: [.clear, LemonadeTheme.colors.background.bgDefault],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                    .frame(width: fadeWidth)
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    .allowsHitTesting(false)
                }

                // Leading fade
                if showLeadingFade {
                    LinearGradient(
                        colors: [LemonadeTheme.colors.background.bgDefault, .clear],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                    .frame(width: fadeWidth)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .allowsHitTesting(false)
                }
            }
        }
        .onChange(of: selectedIndex) { newIndex in
            withAnimation(.easeInOut(duration: 0.25)) {
                proxy.scrollTo(newIndex, anchor: .center)
            }
        }
    }
```

Add the preference key and state properties:

```swift
private struct ScrollOffsetPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}
```

And `@State` properties in `LemonadeTabsView`:

```swift
@State private var scrollOffset: CGFloat = 0
@State private var contentWidth: CGFloat = 0
@State private var containerWidth: CGFloat = 0

private var showTrailingFade: Bool {
    contentWidth > containerWidth && scrollOffset + containerWidth < contentWidth
}

private var showLeadingFade: Bool {
    scrollOffset > 0
}
```

**Step 2: Verify with many tabs in preview**

**Step 3: Commit**

```
feat(tabs): add fade scroll overlay for iOS hug mode
```

---

## Task 10: iOS — Liquid Glass for iOS 26+

**Files:**
- Modify: `swiftui/Sources/Lemonade/Components/LemonadeTabs.swift`

**Step 1: Add conditional Liquid Glass modifier to the active indicator**

Create a view modifier that conditionally applies glass effect:

```swift
extension View {
    @ViewBuilder
    func tabIndicatorStyle() -> some View {
        if #available(iOS 26, *) {
            self.glassEffect(.regular.tint(.green).interactive())
        } else {
            self
        }
    }
}
```

Apply it to the active indicator rectangle:

```swift
if index == selectedIndex {
    UnevenRoundedRectangle(
        topLeadingRadius: 2,
        bottomLeadingRadius: 0,
        bottomTrailingRadius: 0,
        topTrailingRadius: 2
    )
    .fill(LemonadeTheme.colors.background.bgBrandHigh)
    .frame(height: LemonadeTheme.borderWidth.base.border75)
    .matchedGeometryEffect(id: "indicator", in: tabNamespace)
    .tabIndicatorStyle()
}
```

**Step 2: Verify it compiles (glass effect requires Xcode 26 beta SDK)**

Note: If building with Xcode < 26, the `#available` check ensures the glass code is only compiled when the SDK supports it. The component should compile cleanly on both old and new SDKs.

**Step 3: Commit**

```
feat(tabs): add Liquid Glass indicator for iOS 26+
```

---

## Task 11: iOS — Update previews and TabsDisplayView

**Files:**
- Modify: `swiftui/Sources/Lemonade/Components/LemonadeTabs.swift`
- Modify: `swiftui/SampleApp/TabsDisplayView.swift`

**Step 1: Update previews in LemonadeTabs.swift**

```swift
#if DEBUG
struct LemonadeTabs_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LemonadeTheme.spaces.spacing800) {
            LemonadeUi.Tabs(
                tabs: [
                    LemonadeTabItem(label: "Overview"),
                    LemonadeTabItem(label: "Details"),
                    LemonadeTabItem(label: "Reviews"),
                ],
                selectedIndex: 1,
                onTabSelected: { _ in }
            )

            LemonadeUi.Tabs(
                tabs: [
                    LemonadeTabItem(label: "Home", icon: .home),
                    LemonadeTabItem(label: "Search", icon: .search),
                    LemonadeTabItem(label: "Profile", icon: .user),
                ],
                selectedIndex: 0,
                onTabSelected: { _ in }
            )

            LemonadeUi.Tabs(
                tabs: [
                    LemonadeTabItem(label: "Dashboard"),
                    LemonadeTabItem(label: "Analytics"),
                    LemonadeTabItem(label: "Reports"),
                    LemonadeTabItem(label: "Settings"),
                    LemonadeTabItem(label: "Users"),
                    LemonadeTabItem(label: "Activity"),
                ],
                selectedIndex: 2,
                onTabSelected: { _ in }
            )

            LemonadeUi.Tabs(
                tabs: [
                    LemonadeTabItem(label: "Tab A"),
                    LemonadeTabItem(label: "Tab B"),
                    LemonadeTabItem(label: "Tab C"),
                ],
                selectedIndex: 0,
                onTabSelected: { _ in },
                itemsSize: .stretch
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
#endif
```

**Step 2: Update TabsDisplayView.swift**

Update all string arrays to `LemonadeTabItem` arrays and add new demo sections for icons, disabled, and stretch modes.

**Step 3: Verify it compiles**

**Step 4: Commit**

```
feat(tabs): update iOS previews and demo for new Tabs API
```

---

## Task 12: Final verification and cleanup

**Files:**
- All modified files

**Step 1: Run Android build**

```bash
cd kmp && ./gradlew :ui:compileKotlinDesktop
```

**Step 2: Run iOS build**

```bash
cd swiftui && xcodebuild -scheme Lemonade -destination 'platform=iOS Simulator,name=iPhone 16' build
```

**Step 3: Check for lint/detekt issues**

```bash
cd kmp && ./gradlew detekt ktlintCheck
```

**Step 4: Review all changes**

Ensure consistent naming, no leftover old API references, and both platforms match the Figma spec.

**Step 5: Final commit if any cleanup needed**

```
chore(tabs): final cleanup and lint fixes
```
