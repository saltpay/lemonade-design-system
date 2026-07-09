---
name: generate-tokens
description: >
  Regenerate the Lemonade platform token code (KMP + SwiftUI) from the Figma token
  exports in `tokens/*.json`. Use when a `tokens/*.json` file changes (theme-colors,
  radius, spacing, size, opacity, border-width, shadow, typography, primitive-colors)
  and the generated Kotlin / Swift needs to be rebuilt, or when the user asks to
  "generate tokens", "run the token converters", or "sync tokens to code".
---

# Generate Lemonade tokens

Design tokens are authored in Figma and exported as JSON into `tokens/`. A set of
Kotlin script converters in `scripts/*-token-converter.main.kts` read those JSON
files and (over)write the generated source for each platform. This skill runs the
right converters for whatever token files changed.

**Flutter is intentionally excluded.** The repo ships `flutter-*` converters too,
but this skill targets **KMP + SwiftUI only** — do not run the Flutter converters
or commit changes under `flutter/`. If Flutter is ever brought back into scope,
re-add its converters to `converters_for()` in `run-converters.sh` and the table
below.

## TL;DR

From the repo root:

```bash
# Regenerate only what changed vs HEAD (recommended)
.claude/skills/generate-tokens/scripts/run-converters.sh --changed

# Or target specific token files
.claude/skills/generate-tokens/scripts/run-converters.sh theme-colors.json radius.json

# Or regenerate everything
.claude/skills/generate-tokens/scripts/run-converters.sh --all
```

The runner pins Kotlin 2.3.20, strips stray modes from `theme-colors.json`, runs
the mapped converters, and prints the generated files that changed. Then review
the diff and, for color/theme changes, glance at the KMP API baseline (see
[Binary compatibility](#binary-compatibility)).

## Two hard requirements (both bite silently)

1. **Kotlin 2.3.20 — NOT Homebrew's 2.4.0.** The `.main.kts` converters fail to
   compile on Kotlin 2.4.0 with:
   `Expected FirResolvedTypeRef with ConeKotlinType but was FirUserTypeRefImpl`.
   The runner installs 2.3.20 into `~/.local/kotlin-2.3.20` on first use and
   always calls it by absolute path, ignoring whatever `kotlin` is on `PATH`. If
   you run a converter by hand, invoke
   `~/.local/kotlin-2.3.20/kotlinc/bin/kotlin scripts/<name>.main.kts` from the
   repo root — never bare `kotlin`.

2. **Strip stray modes from `theme-colors.json` first.** Figma re-exports carry an
   extra mode id in every variable's `valuesByMode` / `resolvedValuesByMode`
   (historically `3932:0`) that is not one of the two real modes declared in the
   top-level `modes` map (`3037:0` = Light, `4431:0` = Dark). The committed file
   must never contain it. Run:
   ```bash
   python3 .claude/skills/generate-tokens/scripts/strip-stray-modes.py tokens/theme-colors.json
   # or check without editing:
   python3 .claude/skills/generate-tokens/scripts/strip-stray-modes.py --check tokens/theme-colors.json
   ```
   The stripper removes any mode key absent from `modes` (so it also handles a
   future stray id) and rewrites with 2-space indent + trailing newline, matching
   Figma's native format so the diff stays clean. `run-converters.sh` does this
   automatically whenever `theme-colors.json` is in scope.

## How the converters work

- Each converter is a standalone `kotlin` script run **from the repo root** — it
  reads `tokens/<x>.json` via a relative path and writes generated source into the
  platform module. Running from any other directory silently reads/writes nothing
  useful.
- Converters overwrite their output files wholesale (each carries a
  "DO NOT MODIFY THIS FILE MANUALLY" banner). Never hand-edit generated files.
- `primitive-colors.json` did **not** change? Skip its converters — they're only
  needed when the raw color ramp changes, not for semantic (`theme-colors`) edits.

## Token file → converter map

| `tokens/` file          | KMP                              | SwiftUI                                             |
|-------------------------|----------------------------------|-----------------------------------------------------|
| `primitive-colors.json` | `kmp-color`                      | `swiftui-color`                                     |
| `theme-colors.json`     | `kmp-theme`                      | `swiftui-theme` + `swiftui-color-assets-generator`  |
| `radius.json`           | `kmp-radius`                     | `swiftui-radius`                                    |
| `spacing.json`          | `kmp-spacing`                    | `swiftui-spacing`                                   |
| `size.json`             | `kmp-dimension`                  | `swiftui-size`                                      |
| `opacity.json`          | `kmp-opacity`                    | `swiftui-opacity`                                   |
| `border-width.json`     | `kmp-border-width`               | `swiftui-border`                                    |
| `shadow.json`           | `kmp-shadow`                     | `swiftui-shadow`                                    |
| `typography.json`       | `kmp-typography`                 | `swiftui-typography`                                |

(Converter names above omit the `-token-converter.main.kts` suffix, except
`swiftui-color-assets-generator.main.kts`. `flutter-*` converters exist in
`scripts/` but are deliberately not run — see the note at the top.)

## After generating

- `git status` / `git diff` the generated sources. A semantic color change
  typically touches `LemonadeSemanticColors.{kt,swift}`, the theme classes
  (`LemonadeLightTheme.kt` / `LemonadeDarkTheme.kt`,
  `LemonadeAdaptiveTheme.swift`), and the SwiftUI `Assets.xcassets/Colors` +
  `Color+Lemonade.swift`. Nothing under `flutter/` should change — if it does,
  a Flutter converter was run by mistake; revert it.
- <a id="binary-compatibility"></a>**Binary compatibility:** adding new public
  token symbols is additive (safe), but renaming/removing one is an ABI break.
  If public API may have shifted, run the classifier from `kmp/` and follow the
  **binary-compatibility** skill:
  `.claude/skills/binary-compatibility/scripts/bcv-check.sh --ci`.
- Commit the cleaned `tokens/*.json` alongside the regenerated source in the same
  change.
