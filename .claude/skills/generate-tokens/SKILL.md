---
name: generate-tokens
description: >
  Regenerate the Lemonade platform token code (KMP + SwiftUI) from the Figma token
  exports in `tokens/*.tokens.json`. Use when a `tokens/*.tokens.json` file changes
  (theme-colors, radius, spacing, size, opacity, border-width, shadow, typography,
  primitive-colors) and the generated Kotlin / Swift needs to be rebuilt, or when
  the user asks to "generate tokens", "run the token converters", or "sync tokens
  to code".
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
# 1. Ingest a fresh Figma native export (routes files by content, not by name)
python3 .claude/skills/generate-tokens/scripts/ingest-tokens.py ~/Downloads/<export-dir>

# 2. Regenerate only what changed vs HEAD (recommended)
.claude/skills/generate-tokens/scripts/run-converters.sh --changed
```

Tokens are exported from Figma with **File → Export variables** (the native
export, not a plugin). It emits one `*.tokens.json` per collection, and one per
*mode* for multi-mode collections. Run it twice: once in the design-system file,
once in the **Colors** library file that holds the primitives.

`ingest-tokens.py` also accepts `--allow-shrink`: by default it refuses to
ingest a collection whose token count decreased from what's committed (public
API disappearing should be a deliberate call), so pass the flag the first time
a token is legitimately removed upstream.

That's the routine flow for the common case — a token *value* changed and the
generated code needs rebuilding. `verify-generated.sh` is a separate, narrower
tool; see below for when to reach for it.

## One hard requirement (it bites silently)

**Kotlin 2.3.20 — NOT Homebrew's 2.4.0.** The `.main.kts` converters fail to
compile on Kotlin 2.4.0 with:
`Expected FirResolvedTypeRef with ConeKotlinType but was FirUserTypeRefImpl`.
The runner installs 2.3.20 into `~/.local/kotlin-2.3.20` on first use and
always calls it by absolute path, ignoring whatever `kotlin` is on `PATH`. If
you run a converter by hand, invoke
`~/.local/kotlin-2.3.20/kotlinc/bin/kotlin scripts/<name>.main.kts` from the
repo root — never bare `kotlin`.

## How the converters work

- Each converter is a standalone `kotlin` script run **from the repo root** — it
  reads `tokens/<x>.tokens.json` via a relative path and writes generated source
  into the platform module. Running from any other directory silently
  reads/writes nothing useful.
- Converters overwrite their output files wholesale (each carries a
  "DO NOT MODIFY THIS FILE MANUALLY" banner). Never hand-edit generated files.
- `primitive-colors.tokens.json` did **not** change? Skip its converters — they're
  only needed when the raw color ramp changes, not for semantic (`theme-colors`)
  edits.

## Token file → converter map

| `tokens/` file                                                   | KMP                               | SwiftUI                                             |
|-------------------------------------------------------------------|----------------------------------|-----------------------------------------------------|
| `primitive-colors.tokens.json`                                     | `kmp-color`                      | `swiftui-color`                                     |
| `theme-colors.light.tokens.json` / `theme-colors.dark.tokens.json` | `kmp-theme`                      | `swiftui-theme` + `swiftui-color-assets-generator`  |
| `radius.tokens.json`                                               | `kmp-radius`                     | `swiftui-radius`                                    |
| `spacing.tokens.json`                                              | `kmp-spacing`                    | `swiftui-spacing`                                   |
| `size.tokens.json`                                                 | `kmp-dimension`                  | `swiftui-size`                                      |
| `opacity.tokens.json`                                              | `kmp-opacity`                    | `swiftui-opacity`                                   |
| `border-width.tokens.json`                                         | `kmp-border-width`               | `swiftui-border`                                    |
| `shadow.tokens.json`                                               | `kmp-shadow`                     | `swiftui-shadow`                                    |
| `typography.tokens.json`                                           | `kmp-typography`                 | `swiftui-typography`                                |

(Converter names above omit the `-token-converter.main.kts` suffix, except
`swiftui-color-assets-generator.main.kts`. `flutter-*` converters exist in
`scripts/` but are deliberately not run — see the note at the top.)

## When to run verify-generated.sh

`verify-generated.sh` is **not** a routine step after a normal token edit — an
intentional token *value* change is expected to fail it, every time, because
the new value is (correctly) not byte-identical to what's on `origin/main`.
Running it there and seeing `FAIL … (must be byte-identical)` is not a
problem to chase down; it is the harness confirming the value actually
changed.

It exists for the opposite case: a change that is **not supposed to alter any
generated output** — a converter refactor, a loader edit, a format migration,
reordering cleanup. For those, run it against the appropriate ref (default
`origin/main`, or a specific commit/tag when comparing against a known-good
snapshot) and expect `PASS: only permitted reordering found`. Anything else
means the "no-op" change actually moved something consumer-visible.

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
- Commit the cleaned `tokens/*.tokens.json` alongside the regenerated source in
  the same change.
