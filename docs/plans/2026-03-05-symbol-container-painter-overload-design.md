# SymbolContainer Painter Overload

## Summary

Add a 4th public `SymbolContainer` overload that accepts a `Painter` with a `fill` boolean parameter controlling whether the painter fills the entire container or is constrained to `contentSize` like other overloads.

## API Signature

```kotlin
@Composable
public fun LemonadeUi.SymbolContainer(
    painter: Painter,
    contentDescription: String?,
    fill: Boolean,
    modifier: Modifier = Modifier,
    voice: SymbolContainerVoice = SymbolContainerVoice.Neutral,
    size: SymbolContainerSize = SymbolContainerSize.Medium,
    shape: SymbolContainerShape = SymbolContainerShape.Circle,
    badgeSlot: (@Composable BoxScope.() -> Unit)? = null,
)
```

### Parameter decisions

- `painter`, `contentDescription`, `fill` — required, no defaults. Forces callers to be explicit about fill intent.
- No tint — painter renders with original colors. Voice only affects background.
- Background always renders underneath (visible if painter has transparency).
- All other params match existing overloads exactly.

## Rendering behavior

### `fill = false`

Painter sized to `contentSize` (e.g. 20dp for Medium), centered in container. Consistent with icon/text overloads.

```kotlin
Image(
    painter = painter,
    contentDescription = contentDescription,
    modifier = Modifier.requiredSize(
        size = LocalSymbolContainerPlatformDimensions.current.contentSize
    ),
    contentScale = ContentScale.Fit,
)
```

### `fill = true`

Painter fills the entire container, clipped to shape (circle/rounded). Like a profile photo avatar.

```kotlin
Image(
    painter = painter,
    contentDescription = contentDescription,
    modifier = Modifier.matchParentSize(),
    contentScale = ContentScale.Crop,
)
```

The existing `.clip(resolvedShape)` on the parent Box in `CoreSymbolContainer` handles shape clipping.

## Approach

Self-contained overload (Approach A). No changes to `CoreSymbolContainer` or any other existing code. The overload builds the appropriate `contentSlot` lambda based on the `fill` parameter.

## Files changed

- `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/SymbolContainer.kt` — add Painter overload
- `kmp/composeApp/src/commonMain/kotlin/com/teya/lemonade/SymbolContainerDisplay.kt` — add "Painter Variant" section with netflix logo examples (fill=true and fill=false)

## Display examples

New "Painter Variant" section showing:
- `fill = false` — netflix logo constrained to contentSize across sizes
- `fill = true` — netflix logo filling container, clipped to shape (circle + rounded)
