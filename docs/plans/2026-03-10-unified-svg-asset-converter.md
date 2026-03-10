# Unified SVG Asset Converter Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Unify KMP and SwiftUI icon/flag/brandlogo generation into a single script that reads SVGs and converts them to both platforms, eliminating the need for pre-made PDFs.

**Architecture:** A single `svg-asset-converter.main.kts` script reads SVGs from `svg/{icons,flags,brandLogos}/`, generates KMP vector drawables + Kotlin enums, and converts SVGs to PDFs via `rsvg-convert` for SwiftUI asset catalogs + Swift enums. A `generate-resources.main.kts` orchestrator replaces the old KMP-only one.

**Tech Stack:** Kotlin Script (.kts), rsvg-convert (librsvg), Xcode asset catalog format, Android vector drawable XML

---

### Task 1: Create `svg-asset-converter.main.kts`

**Files:**
- Create: `scripts/svg-asset-converter.main.kts`

This script replaces:
- `scripts/kmp-svg-converter.main.kts`
- `scripts/swiftui-svg-converter.main.kts`
- `scripts/swiftui-brandlogos-converter.main.kts`
- `scripts/swiftui-flags-converter.main.kts`

**Step 1: Write the unified script**

The script must:
1. Check `rsvg-convert` is available (exit with clear error if not)
2. Define 3 pack configs (icons, flags, brandLogos) with per-pack metadata
3. For each pack, read SVGs from `svg/{packDir}/` and:
   - **KMP**: Convert SVG → vector drawable XML in `kmp/ui/src/commonMain/composeResources/drawable/gen_*.xml`, generate enum in `kmp/core/src/commonMain/kotlin/com/teya/lemonade/core/{EnumName}.kt`, generate extension in `kmp/ui/src/commonMain/kotlin/com/teya/lemonade/{EnumName}Extension.kt`
   - **SwiftUI**: Convert SVG → PDF via `rsvg-convert`, create `.imageset` dirs in `swiftui/Sources/Lemonade/Resources/Assets.xcassets/`, generate Swift enum in `swiftui/Sources/Lemonade/{FileName}.swift`

Key differences per pack type:

| | Icons | Flags | BrandLogos |
|---|---|---|---|
| KMP enum name | `LemonadeIcons` | `LemonadeCountryFlags` | `LemonadeBrandLogos` |
| Swift enum name | `LemonadeIcon` | `LemonadeCountryFlag` | `LemonadeBrandLogo` |
| Swift file name | `LemonadeIcons.swift` | `LemonadeCountryFlags.swift` | `LemonadeBrandLogos.swift` |
| Asset catalog template rendering | yes (tintable) | no | no |
| Swift enum extras | `image` property, `LemonadeIconSize`, View extension | `countryCode`, `countryName` properties | none |
| Swift imports | `SwiftUI`, `UIKit` | `Foundation` | `Foundation` |

Conversion logic to port:
- `convertSvgToVectorDrawable()` from `kmp-svg-converter.main.kts` (lines 5-62)
- `generateIconsCore()` from `kmp-svg-converter.main.kts` (lines 64-99) — generalized for any pack name
- `generateIconsExtension()` from `kmp-svg-converter.main.kts` (lines 102-138) — generalized
- Asset catalog creation from `swiftui-svg-converter.main.kts` (lines 77-126) — with PDF from rsvg-convert instead of copying
- Swift enum builders from all 3 SwiftUI scripts — each pack type has its own builder

SVG → PDF conversion:
```bash
rsvg-convert -f pdf -o output.pdf input.svg
```

**Step 2: Verify the script runs**

```bash
cd /path/to/lemonade-design-system
kotlin scripts/svg-asset-converter.main.kts
```

Expected: Script processes all 3 packs, generates KMP + SwiftUI files, prints success messages.

**Step 3: Verify generated outputs match previous outputs**

- Check `kmp/core/src/commonMain/kotlin/com/teya/lemonade/core/LemonadeIcons.kt` has same enum entries
- Check `kmp/ui/src/commonMain/composeResources/drawable/` has same XML files
- Check `swiftui/Sources/Lemonade/LemonadeIcons.swift` has same enum entries
- Check `swiftui/Sources/Lemonade/Resources/Assets.xcassets/` has imagesets with PDFs

---

### Task 2: Create `generate-resources.main.kts`

**Files:**
- Create: `scripts/generate-resources.main.kts`

Replaces: `scripts/kmp-generate-resources.main.kts`

**Step 1: Write the orchestrator**

Calls all resource generation scripts for both platforms:
- All KMP token converters (same as before)
- All SwiftUI token converters + color assets generator
- `svg-asset-converter.main.kts` (replaces 3x kmp-svg-converter calls)
- `kmp-country-flags-alpha2-generator.main.kts` (after svg-asset-converter, since it modifies the generated flags enum)

**Step 2: Verify it runs end-to-end**

```bash
kotlin scripts/generate-resources.main.kts
```

---

### Task 3: Clean up old files

**Files:**
- Delete: `scripts/kmp-svg-converter.main.kts`
- Delete: `scripts/kmp-generate-resources.main.kts`
- Delete: `scripts/swiftui-svg-converter.main.kts`
- Delete: `scripts/swiftui-brandlogos-converter.main.kts`
- Delete: `scripts/swiftui-flags-converter.main.kts`
- Delete: `svg/pdf/` directory (entire directory tree)

**Step 1: Remove old scripts and pdf directory**

```bash
rm scripts/kmp-svg-converter.main.kts
rm scripts/kmp-generate-resources.main.kts
rm scripts/swiftui-svg-converter.main.kts
rm scripts/swiftui-brandlogos-converter.main.kts
rm scripts/swiftui-flags-converter.main.kts
rm -rf svg/pdf/
```

**Step 2: Verify no broken references**

Search for references to old script names in remaining files.

---

### Task 4: Final verification

**Step 1: Run full generation**

```bash
kotlin scripts/generate-resources.main.kts
```

**Step 2: Verify all outputs are correct**

Check that KMP and SwiftUI generated files are present and correct.

**Step 3: Commit**

```bash
git add -A
git commit -m "feat: unify KMP + SwiftUI asset generation, convert SVG to PDF via rsvg-convert"
```
