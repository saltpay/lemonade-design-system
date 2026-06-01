---
applyTo: "kmp/{core,tokens,ui,expressive,calendar}/src/**/*.kt"
---

# Binary compatibility review guidelines

The published modules (`core`, `tokens`, `ui`, `expressive`, `calendar`) ship to real apps. Their public API is an ABI contract: an app that was already compiled links against the exact symbols recorded in `api/*.api` and `*.klib.api`. Delete or rename one of those symbols and the app crashes at runtime with `NoSuchMethodError`, even though the source still compiles and CI was green. Flag the cases below.

## The value-class mangling trap (this is the one that bit us)

Adding a default parameter to a public function reads as source-compatible. Existing call sites still compile. But Kotlin rewrites the JVM method signature, and if any parameter is a value class (`TextUnit`, `Color`, `Dp`, `TextAlign`, `TextOverflow`, `Offset`, and so on) the function name carries a mangled hash suffix like `Text-Zb-9Jy0`. Adding a value-class parameter changes that hash, so the old symbol stops existing. An app built against the old artifact calls a method that is no longer there.

That is exactly how `LemonadeUi.Text` broke in #200. A `lineSpacing: TextUnit` default was added, `Text-Zb-9Jy0` became `Text-hCnJeNg`, and every consumer that called the old symbol crashed.

So: flag any new default parameter or new overload on a public function in a published module unless the previous signature stays behind as a `@Deprecated(level = DeprecationLevel.HIDDEN)` overload that forwards to the new one. For a mangled function the shim has to carry the **exact old parameter list** (without the new parameter) so the compiler regenerates the original `-<hash>` symbol. Reference implementations live in `kmp/ui/.../CountryFlag.kt` and the two `Text` shims.

## Regenerating the dump is not a fix

A red `apiCheck` means the committed baseline no longer matches the source. Running `./gradlew apiDump` and committing the result turns it green, but that only moves the baseline to match the break. `apiDump` keeps the baseline honest; it never makes a change safe. If a PR regenerates `api/*.api` and the diff drops a `-` line, that is a removed symbol, not housekeeping. Ask where the shim is.

## What to flag in an `api` dump diff

- Any `-` line in `api/*.api` or `*.klib.api` other than the `---` file header. That covers a removal, a rename, a changed signature, or narrowed visibility. The single exception is a `-`/`+` pair that differs only by the `synthetic` modifier. That pair is a `@Deprecated(HIDDEN)` symbol keeping its descriptor, which is safe and classifies as additive.
- A new `abstract` member added to a type that already existed. It looks additive in the diff but throws `AbstractMethodError` on any existing implementer. Give it a default body, or escalate.

## Other public-API changes

| Change | Safe move |
|--------|-----------|
| Add a default param, or rename a function | `@Deprecated(HIDDEN)` overload forwarding to the new one (exact old params for mangled functions) |
| Add a property to a public `data class` | Append it with a default, then restore the old `<init>` and `copy(...)` as two `@Deprecated(HIDDEN)` shims |
| Add an enum entry (config enum) | Fine. Handle the new entry in every internal `when` |
| Add an interface member | Give it a default implementation, or accept a `BREAKING` verdict and get a maintainer to approve |
| Rename or remove a property, interface member, or enum entry | Stop. Real break, no shim. Escalate to a maintainer. |

Full decision table and the per-case patterns are in `.claude/skills/binary-compatibility/SKILL.md`. To get the same verdict CI will produce (`NO_CHANGES`, `ADDITIONS_ONLY`, or `BREAKING`) before you push, run:

```bash
.claude/skills/binary-compatibility/scripts/bcv-check.sh
```
