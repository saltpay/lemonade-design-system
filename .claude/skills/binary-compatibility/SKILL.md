---
name: binary-compatibility
description: >
  Diagnose and fix Kotlin Binary Compatibility Validator (BCV) failures in the
  Lemonade KMP modules. Use when `apiCheck` fails, when the "API Stability
  Review" CI job flags a PR as BREAKING, when an `api/*.api` or `*.klib.api`
  file shows up in a diff, or when adding a parameter/overload/enum entry to a
  published component and you need to keep ABI stable.
---

# Lemonade binary compatibility

The published KMP modules (`core`, `tokens`, `ui`, `expressive`, `calendar`) ship
to real consumers. The Binary Compatibility Validator dumps every public symbol
into `kmp/<module>/api/*.api` (JVM/Android), `kmp/<module>/api/*/*.api`, and
`*.klib.api` (Kotlin/Native). CI then does two separate things with those files,
and it's worth knowing which is which before you touch anything:

- **`apiCheck`** recomputes the API from your source and fails if it no longer
  matches the committed baseline. The fix is always the same: run `apiDump` and
  commit the regenerated files. This just keeps the baseline honest; it doesn't
  judge whether the change is safe.
- **API Stability Review** takes the diff of those baseline files between your
  branch and the base, and classifies it. The classifier lives at
  `kmp/build-logic/src/main/kotlin/ApiStabilityClassifier.kt` and the verdict is
  one of `NO_CHANGES`, `ADDITIONS_ONLY`, or `BREAKING`. Additions auto-pass.
  A `BREAKING` verdict blocks merge until one of @caiqueslp, @williankl, or
  @DevSrSouza approves the exact head commit.

Two rules decide `BREAKING`, and they are blunt on purpose:

1. **Any `-` line** in the diff (other than the `---` file header). A removal, a
   rename, a return-type change, a narrowed visibility — anything that deletes or
   alters an existing line counts. One carve-out: a `-`/`+` pair in the same file
   that differs *only* by the `synthetic` modifier is treated as additive, because
   that's a `@Deprecated(HIDDEN)` symbol keeping its exact descriptor (more below).
2. **A `+` abstract member added inside a type that already existed.** That looks
   additive in the diff but throws `AbstractMethodError` on consumers who already
   implement the type. A brand-new type with abstract members is fine — nobody
   implements it yet.

That's the whole model. The classifier reads text, not intent, so it stays
deliberately strict — the `synthetic` carve-out is the only place it reasons about
what a change actually means for the binary.

## Step 1 — see what actually changed

Run the diagnosis script from anywhere in the repo:

```bash
.claude/skills/binary-compatibility/scripts/bcv-check.sh
```

It regenerates the baseline from your current source, diffs it against `HEAD`,
prints the `+`/`-` lines that moved, and runs the project's own classifier so you
get the same verdict CI will. Add `--ci` to mirror CI exactly (committed baseline
vs `origin/main`, no regeneration).

Read the verdict and the moved lines before deciding anything. A `BREAKING`
verdict with reasons like `removed/changed declaration(s): …` tells you which
symbol the classifier objected to.

## Step 2 — decide, per kind of change

### Adding a default parameter to a public function

Adding a default parameter looks source-compatible — existing call sites still
compile. But Kotlin changes the JVM method signature: `foo(a)` becomes `foo(a, b)`
plus a synthetic `foo$default(...)`. The old `foo(a)` line disappears from the
dump, so the classifier sees a `-` line and calls it `BREAKING`.

Keep the old binary symbol by leaving the previous signature in place as a
`@Deprecated(HIDDEN)` overload that delegates to the new one. The real example in
this repo is `LemonadeUi.CountryFlag` — read
`kmp/ui/src/commonMain/kotlin/com/teya/lemonade/CountryFlag.kt`:

```kotlin
@Composable
public fun LemonadeUi.CountryFlag(
    flag: LemonadeCountryFlags,
    contentDescription: String = flag.name,
    size: LemonadeAssetSize = LemonadeAssetSize.Medium,
    modifier: Modifier = Modifier,
    shape: CountryFlagShape = CountryFlagShape.Circular, // the new parameter
) { /* … */ }

@Deprecated(
    message = "Use the overload with a shape parameter.",
    replaceWith = ReplaceWith("CountryFlag(flag, contentDescription, size, modifier, CountryFlagShape.Circular)"),
    level = DeprecationLevel.HIDDEN,
)
@Composable
public fun LemonadeUi.CountryFlag(
    flag: LemonadeCountryFlags,
    contentDescription: String = flag.name,
    size: LemonadeAssetSize = LemonadeAssetSize.Medium,
    modifier: Modifier = Modifier,
) {
    CountryFlag(flag, contentDescription, size, modifier, CountryFlagShape.Circular)
}
```

`HIDDEN` keeps the old method in the bytecode (so already-compiled consumers keep
linking) while removing it from the Kotlin source API (so nobody writes new code
against it). That is the right move and you should make it.

`HIDDEN` adds the `synthetic` flag to that retained method, so its dump line flips
from `public static final fun …` to `public static final synthetic fun …` — a
`-`/`+` pair. The classifier recognises this case: a pair that differs only by
`synthetic` keeps an identical descriptor, so no consumer can break, and it's
counted as additive rather than a removal. Verdict `ADDITIONS_ONLY`, CI auto-passes,
no maintainer gate. Still mention the overload in the PR's API Dump section so a
reviewer can see what happened.

If you'd rather not hide the old overload at all, keep it **visible** (drop the
`@Deprecated`, or use `DeprecationLevel.WARNING`) and add the new behaviour as a
separate explicit overload. Both overloads then stay in the Kotlin API. Either way
the verdict is `ADDITIONS_ONLY` — the only difference is whether the old signature
is still visible to source. Pick per case and say which in the PR.

### Renaming a public function

Same shape as above. Keep the old name as a `@Deprecated(HIDDEN)` function that
forwards to the new one. The old binary symbol survives, source users move to the
new name, and the `synthetic`-only flip classifies as `ADDITIONS_ONLY`.

### Renaming or removing a public property — STOP

There is no clean overload trick for a property. Renaming or removing one is a
real ABI **and** API break: existing binaries fail to link and existing source
fails to compile. Do not do it silently to make a build pass.

If the rename is genuinely required, the change has to be deliberate: surface it
to the user, keep the old property where you can (e.g. a `@Deprecated(HIDDEN)`
backing accessor), and get sign-off from the required reviewers. This is exactly
the case the maintainer-approval gate exists for. Never reshape a property to
quiet `apiCheck`.

### Adding to an `interface`

Adding a property or function to an interface can be an API and ABI change in the
general case. In Lemonade it's almost always fine: the interfaces here are meant
to be implemented *inside* the design system, not by consumers. If something
downstream implements one, that's already a misuse and we don't design around it.

So: **additions to an interface are acceptable**, but they're not free. The
classifier flags a new *abstract* member on an existing type as `BREAKING` (rule
2), because it would `AbstractMethodError` any external implementer. Two ways
through:

- Give the new member a default implementation (`fun foo(): T = …` on the
  interface). It dumps as `open`, not `abstract`, so the verdict stays additive.
- Or keep it abstract if a default makes no sense, accept the `BREAKING` verdict,
  and have a maintainer approve it after you state in the PR that the interface is
  local-only.

Renaming an interface member is a different story — that's a hard break with no
workaround. Treat it like a property rename: STOP and escalate.

### Adding an `enum` entry

Most enums here configure a component, and the component is the only thing that
does `when (value)` over them. Adding an entry is an ABI/API change in theory
(an exhaustive `when` in consumer code could now miss a branch), but for a
config enum the component owns the `when`, so in practice it's safe. The
classifier treats a pure entry addition as `ADDITIONS_ONLY` — no `-` line — so it
auto-passes.

Two caveats: make sure every `when` *inside the design system* handles the new
entry, and call the addition out in the PR so a reviewer can confirm the enum is
really config-only and not something a consumer branches on. Renaming or removing
an entry is a `-` line and a real break — STOP and escalate.

## Step 3 — regenerate, re-check, then write the PR section

After any of the above:

```bash
cd kmp && ./gradlew apiDump      # rewrite the baseline from your new source
.claude/skills/binary-compatibility/scripts/bcv-check.sh   # confirm the verdict
```

Commit the regenerated `api/` files alongside the source. Then fill in the
**API Dump** section of the PR description (the template has it). The section
exists so a reviewer can read the API delta without re-deriving it. State:

- the verdict (`ADDITIONS_ONLY` or `BREAKING`);
- any `@Deprecated(HIDDEN)` overload you added — note that the `-`/`+` pair is just
  the `synthetic` flag with an unchanged descriptor, which the classifier counts as
  additive;
- any interface or enum addition, with a line confirming the type is local-only /
  config-only;
- if the verdict is `BREAKING` for a real reason, who needs to approve and why the
  break is acceptable.

A reviewer should be able to approve from that section alone. If you can't write a
clean justification for a `-` line, that's the signal the change needs a real
conversation, not a workaround.
