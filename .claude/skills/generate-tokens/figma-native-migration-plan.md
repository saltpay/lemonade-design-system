# Figma Native Token Export Migration — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the third-party "Export/import variables" Figma plugin with Figma's native DTCG variable export, without changing anything SDK consumers compile against.

**Architecture:** `tokens/` comes to hold the raw native DTCG export. Format translation lives in one place per platform — the three shared `*-resource-file-loading.main.kts` loaders — which synthesize the `{resolvedValue, alias, aliasName}` object the downstream converters already consume. The old and new token files coexist while each platform migrates, so the tree stays green at every commit; the old files are deleted last.

**Tech Stack:** Kotlin 2.3.20 `.main.kts` scripts, `org.json:json:20240303`, Python 3, Bash, GitHub Actions.

**Spec:** `.claude/skills/generate-tokens/figma-native-migration-design.md`

## Global Constraints

- **Kotlin 2.3.20 only.** Homebrew's 2.4.0 crashes the converters with `Expected FirResolvedTypeRef with ConeKotlinType but was FirUserTypeRefImpl`. Always invoke `~/.local/kotlin-2.3.20/kotlinc/bin/kotlin` by absolute path. Never bare `kotlin`.
- **Run every converter and test from the repo root.** They resolve `tokens/...` relatively.
- **Baseline is `origin/main` at `b32247e` or later.** Rebase this branch onto it before starting.
- **`org.json` `JSONObject.keys()` returns `HashMap` order, not file order.** Any traversal of DTCG objects MUST impose an explicit sort. This is verified, not assumed.
- **Never hand-edit generated files.** They carry a "DO NOT MODIFY THIS FILE MANUALLY" banner.
- **`docs/` is gitignored.** Plans and specs live in `.claude/skills/generate-tokens/`.
- The native export lives at `/Users/ext-felipe.marcon/Desktop/lemonade-tokens-export` and contains: `border-width`, `dark`, `light`, `opacity`, `primitive-colors`, `radius`, `shadow`, `sizing`, `spacing`, `typography` — each `*.tokens.json`.
- **Two known drifted values** must be trimmed for verification: `shadow/xsmall/level-1/sd-xs-lv1-blur` (export 1, baseline 2) and `sd-xs-lv1-offset-y` (export 0.5, baseline 1).

---

## File Structure

**Created:**
- `.claude/skills/generate-tokens/scripts/verify-generated.sh` — golden-file comparison harness. The integration test for every task.
- `.claude/skills/generate-tokens/scripts/ingest-tokens.py` — content-routed ingest.
- `scripts/kmp-loader-dtcg-test.main.kts` — unit test for the KMP loader's DTCG path.
- `scripts/testdata/sample.tokens.json` — DTCG fixture.
- `tokens/*.tokens.json` — the native export (10 files).
- `.github/workflows/token_drift.yml` — CI drift job.

**Modified:**
- `scripts/kmp-resource-file-loading.main.kts` — DTCG parsing, canonical sort, mode-by-name.
- `scripts/swiftui-resource-file-loading.main.kts` — same.
- `scripts/flutter-resource-file-loading.main.kts` — same.
- `scripts/kmp-theme-token-converter.main.kts`, `scripts/swiftui-theme-token-converter.main.kts`, `scripts/swiftui-color-assets-generator.main.kts`, `scripts/flutter-theme-token-converter.main.kts` — mode enumeration from files.
- `scripts/kmp-typography-token-converter.main.kts`, `scripts/swiftui-typography-token-converter.main.kts` — explicit font-weight sort.
- `scripts/kmp-shadow-token-converter.main.kts`, `scripts/swiftui-shadow-token-converter.main.kts`, `scripts/flutter-shadow-token-converter.main.kts` — explicit level sort.
- All remaining converters — token filename only.
- `.claude/skills/generate-tokens/scripts/run-converters.sh`, `.claude/skills/generate-tokens/SKILL.md`.

**Deleted:**
- `tokens/*.json` (the nine plugin-format files).
- `.claude/skills/generate-tokens/scripts/strip-stray-modes.py`.

---

### Task 1: Verification harness

The golden-file comparison every later task runs. Per the spec: byte-identical everywhere except the colour source files, which may only be reordered.

**Files:**
- Create: `.claude/skills/generate-tokens/scripts/verify-generated.sh`

**Interfaces:**
- Produces: `verify-generated.sh [<git-ref>]` — compares the working tree's generated files against `<git-ref>` (default `origin/main`). Exits 0 on pass, 1 on failure. Used by every subsequent task and by CI.

- [ ] **Step 1: Rebase onto the baseline**

```bash
git fetch origin main
git rebase origin/main
git log --oneline -1 origin/main   # must be b32247e or later
```

- [ ] **Step 2: Write the harness**

Create `.claude/skills/generate-tokens/scripts/verify-generated.sh`:

```bash
#!/usr/bin/env bash
# Compare generated platform code in the working tree against a git ref.
#
# Usage (from the repo root):
#   .claude/skills/generate-tokens/scripts/verify-generated.sh [<git-ref>]
#
# Colour source files are allowed to differ ONLY by line order — the token
# loader imposes a canonical sort that differs from the old plugin export's
# arbitrary array order. Everything else, including every .colorset, must be
# byte-identical: that is where the public enums live, and enum entry order
# is consumer-visible.
set -euo pipefail

REF="${1:-origin/main}"
cd "$(git rev-parse --show-toplevel)"

# Files permitted to differ by ordering alone.
reorderable() {
  case "$1" in
    */LemonadePrimitiveColors.kt|*/LemonadePrimitiveColors.swift) return 0 ;;
    */LemonadeLightTheme.kt|*/LemonadeDarkTheme.kt) return 0 ;;
    */LemonadeSemanticColors.kt|*/LemonadeSemanticColors.swift) return 0 ;;
    */LemonadeAdaptiveTheme.swift|*/Color+Lemonade.swift) return 0 ;;
    flutter/lib/src/foundation/primitive_colors.dart) return 0 ;;
    flutter/lib/src/foundation/semantic_colors.dart) return 0 ;;
    flutter/lib/src/theme/colors.dart) return 0 ;;
    *) return 1 ;;
  esac
}

if ! changed="$(git diff --name-only "$REF" -- kmp/ swiftui/ flutter/)"; then
  echo "FAIL: could not diff against $REF (bad or unreachable ref?)"
  exit 1
fi

# New generated files that were never staged/committed don't show up in
# `git diff` at all — catch them separately so a converter emitting a
# brand-new file can't silently slip past the harness. Check the git exit
# code before the grep, so a git-status failure (lock contention, corrupt
# or inaccessible .git, permission/disk errors) FAILs loudly instead of
# looking identical to "zero untracked files" once it hits grep.
if ! status_out="$(git status --porcelain -- kmp/ swiftui/ flutter/)"; then
  echo "FAIL: could not read git status"
  exit 1
fi
untracked=""
if [ -n "$status_out" ]; then
  untracked="$(printf '%s\n' "$status_out" | grep '^??' | cut -c4- || true)"
fi

if [ -z "$changed" ] && [ -z "$untracked" ]; then
  echo "PASS: generated output is byte-identical to $REF"
  exit 0
fi

fail=0

while IFS= read -r f; do
  [ -z "$f" ] && continue
  echo "FAIL  $f (untracked new file, not compared against $REF)"
  fail=1
done <<< "$untracked"

while IFS= read -r f; do
  [ -z "$f" ] && continue
  if [ ! -f "$f" ]; then
    echo "FAIL  $f (deleted or missing in working tree)"
    fail=1
    continue
  fi
  if reorderable "$f"; then
    if diff -q <(git show "$REF:$f" | sort) <(sort "$f") >/dev/null 2>&1; then
      echo "ok    $f (reordered only)"
    else
      echo "FAIL  $f (content changed, not just order)"
      diff <(git show "$REF:$f" | sort) <(sort "$f") | head -20
      fail=1
    fi
  else
    echo "FAIL  $f (must be byte-identical)"
    git diff --stat "$REF" -- "$f"
    fail=1
  fi
done <<< "$changed"

if [ "$fail" -eq 0 ]; then
  echo "PASS: only permitted reordering found"
else
  echo "FAIL: generated output diverged from $REF"
fi
exit "$fail"
```

- [ ] **Step 3: Make it executable and run it on the untouched tree**

```bash
chmod +x .claude/skills/generate-tokens/scripts/verify-generated.sh
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: `PASS: generated output is byte-identical to origin/main` — nothing has changed yet.

- [ ] **Step 4: Prove the baseline is actually reproducible**

This is the step that catches a pre-existing mismatch between committed tokens and committed code. Regenerate everything with the *current* pipeline and confirm nothing moves:

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: PASS. If this fails, STOP — the repo's generated code does not match its tokens, and that must be resolved (or explicitly recorded) before any migration work, because every later verification depends on this baseline.

- [ ] **Step 5: Reset any incidental changes and commit**

```bash
git checkout -- kmp/ swiftui/ flutter/ tokens/
git add .claude/skills/generate-tokens/scripts/verify-generated.sh
git commit -m "test(tokens): add generated-output verification harness"
```

---

### Task 2: DTCG fixture and failing loader test

TDD for the loader shim. The test comes first and must fail.

**Files:**
- Create: `scripts/testdata/sample.tokens.json`
- Create: `scripts/kmp-loader-dtcg-test.main.kts`

**Interfaces:**
- Consumes: `ResourceData<T>`, `readFileResourceFile` from `scripts/kmp-resource-file-loading.main.kts` (Task 1 state — plugin-only).
- Produces: a runnable test asserting the DTCG contract that Task 3 implements.

- [ ] **Step 1: Write the fixture**

Create `scripts/testdata/sample.tokens.json`. It deliberately exercises every DTCG behaviour: nested groups, a number, a local reference, a remote alias with `aliasData`, a hidden token, and a file-level `modeName`.

```json
{
  "base": {
    "border-0": {
      "$type": "number",
      "$value": 0,
      "$extensions": { "com.figma.variableId": "VariableID:1:1" }
    },
    "border-50": {
      "$type": "number",
      "$value": 2,
      "$extensions": { "com.figma.variableId": "VariableID:1:2" }
    },
    "border-100": {
      "$type": "number",
      "$value": 4,
      "$extensions": { "com.figma.variableId": "VariableID:1:3" }
    }
  },
  "state": {
    "border-selected": {
      "$type": "number",
      "$value": "{base.border-50}",
      "$extensions": { "com.figma.variableId": "VariableID:1:4" }
    },
    "hidden-one": {
      "$type": "number",
      "$value": 9,
      "$extensions": {
        "com.figma.variableId": "VariableID:1:5",
        "com.figma.hiddenFromPublishing": true
      }
    }
  },
  "tint": {
    "brand": {
      "$type": "color",
      "$value": {
        "colorSpace": "srgb",
        "components": [0.5, 0.25, 0.125],
        "alpha": 0.8,
        "hex": "#804020"
      },
      "$extensions": {
        "com.figma.variableId": "VariableID:1:6",
        "com.figma.aliasData": {
          "targetVariableId": "VariableID:9:9",
          "targetVariableName": "purple/500"
        }
      }
    }
  },
  "$extensions": { "com.figma.modeName": "Default" }
}
```

- [ ] **Step 2: Write the failing test**

Create `scripts/kmp-loader-dtcg-test.main.kts`:

```kotlin
#!/usr/bin/env kotlin

@file:Import("kmp-resource-file-loading.main.kts")

import org.json.JSONObject
import java.io.File

fun check(condition: Boolean, message: String) {
    if (!condition) error("FAIL: $message")
    println("  ok  $message")
}

fun main() {
    val fixture = File("scripts/testdata/sample.tokens.json")
    check(fixture.isFile, "fixture exists at ${fixture.path}")

    val resources = readFileResourceFile(
        file = fixture,
        resourceMap = { jsonObject -> jsonObject },
    )

    check(resources.size == 5, "hidden tokens are excluded (expected 5, got ${resources.size})")

    val names = resources.map { resource ->
        (resource.groups + resource.name).joinToString("/")
    }
    check(
        names == listOf(
            "Base/border0",
            "Base/border50",
            "Base/border100",
            "State/borderSelected",
            "Tint/brand",
        ),
        "canonical order is group-then-natural-numeric, got $names",
    )

    val selected = resources.first { it.name == "borderSelected" }
    check(selected.value.getInt("resolvedValue") == 2, "local DTCG reference resolves to its target value")
    check(
        selected.value.getString("aliasName") == "base/border-50",
        "local reference exposes aliasName as a slash path",
    )
    check(
        selected.value.getString("alias") == "VariableID:1:2",
        "local reference exposes the target's variable id as alias",
    )

    val brand = resources.first { it.name == "brand" }
    val colour = brand.value.getJSONObject("resolvedValue")
    check(colour.getDouble("r") == 0.5, "colour red component comes from components[0]")
    check(colour.getDouble("g") == 0.25, "colour green component comes from components[1]")
    check(colour.getDouble("b") == 0.125, "colour blue component comes from components[2]")
    check(colour.getDouble("a") == 0.8, "colour alpha comes from \$value.alpha")
    check(brand.value.getString("aliasName") == "purple/500", "remote alias name comes from aliasData")
    check(
        brand.value.getString("alias") == "VariableID:9:9",
        "remote alias id comes from aliasData",
    )

    val modes = availableModeNames(listOf(fixture))
    check(modes == listOf("Default"), "mode name is read from the file-level extensions, got $modes")

    println("ALL PASSED")
}

main()
```

- [ ] **Step 3: Run the test to verify it fails**

```bash
~/.local/kotlin-2.3.20/kotlinc/bin/kotlin scripts/kmp-loader-dtcg-test.main.kts
```

Expected: FAIL — compilation error, `Unresolved reference: availableModeNames`. The loader has no DTCG support yet.

- [ ] **Step 4: Commit the failing test**

```bash
git add scripts/testdata/sample.tokens.json scripts/kmp-loader-dtcg-test.main.kts
git commit -m "test(tokens): add failing DTCG loader test and fixture"
```

---

### Task 3: DTCG support in the KMP loader

**Files:**
- Modify: `scripts/kmp-resource-file-loading.main.kts`
- Test: `scripts/kmp-loader-dtcg-test.main.kts`

**Interfaces:**
- Consumes: `ResourceData<T>` (unchanged), `sanitizedGroups()`, `sanitizedValueName()`, `sanitizedClassName()` (unchanged).
- Produces, for Tasks 4–9:
  - `fun isDtcgDocument(json: JSONObject): Boolean`
  - `fun availableModeNames(files: List<File>): List<String>`
  - `fun <T> readFileResourceFile(file: File, resourceMap: (JSONObject) -> T): List<ResourceData<T>>` — unchanged signature, now handling both formats.
  - `fun <T> readFileResourceFileByMode(files: List<File>, modeName: String, resourceMap: (JSONObject) -> T): List<ResourceData<T>>` — **signature changed**: takes a file list and a mode *name*, not a single file and a mode *key*.
  - `fun tokenFile(vararg candidates: String): File`
  - `fun tokenFiles(prefix: String): List<File>`

- [ ] **Step 1: Add the DTCG block to the loader**

Append to `scripts/kmp-resource-file-loading.main.kts`, after the existing `isValueNumberOnly()`:

```kotlin
// ---------------------------------------------------------------------------
// Figma native (DTCG) support
//
// org.json's JSONObject is backed by a HashMap, so keys() does NOT return file
// order. Every traversal below therefore sorts explicitly — without it the
// generated output would be non-deterministic.
// ---------------------------------------------------------------------------

private const val EXTENSIONS = "\$extensions"
private const val TYPE = "\$type"
private const val VALUE = "\$value"

/** A plugin export has a top-level `variables` array; a DTCG document does not. */
fun isDtcgDocument(json: JSONObject): Boolean = !json.has("variables")

/** Compares digit runs numerically, so `border-50` sorts before `border-100`. */
fun naturalCompare(left: String, right: String): Int {
    var i = 0
    var j = 0
    while (i < left.length && j < right.length) {
        val leftChar = left[i]
        val rightChar = right[j]
        if (leftChar.isDigit() && rightChar.isDigit()) {
            var leftEnd = i
            while (leftEnd < left.length && left[leftEnd].isDigit()) leftEnd++
            var rightEnd = j
            while (rightEnd < right.length && right[rightEnd].isDigit()) rightEnd++
            val leftNumber = left.substring(i, leftEnd).trimStart('0').ifEmpty { "0" }
            val rightNumber = right.substring(j, rightEnd).trimStart('0').ifEmpty { "0" }
            if (leftNumber.length != rightNumber.length) return leftNumber.length - rightNumber.length
            val comparison = leftNumber.compareTo(rightNumber)
            if (comparison != 0) return comparison
            i = leftEnd
            j = rightEnd
        } else {
            if (leftChar != rightChar) return leftChar.compareTo(rightChar)
            i++
            j++
        }
    }
    return (left.length - i) - (right.length - j)
}

/** Canonical token ordering: segment by segment, numerically aware. */
fun canonicalTokenOrder(left: String, right: String): Int {
    val leftSegments = left.split("/")
    val rightSegments = right.split("/")
    for (index in 0 until minOf(leftSegments.size, rightSegments.size)) {
        val comparison = naturalCompare(leftSegments[index], rightSegments[index])
        if (comparison != 0) return comparison
    }
    return leftSegments.size - rightSegments.size
}

/** Every leaf token in a DTCG document, keyed by its slash-joined group path. */
fun dtcgTokens(root: JSONObject): Map<String, JSONObject> {
    val tokens = linkedMapOf<String, JSONObject>()
    fun walk(node: JSONObject, path: List<String>) {
        node.keys().asSequence().toList().sortedWith(::naturalCompare).forEach { key ->
            if (key.startsWith("\$")) return@forEach
            val child = node.optJSONObject(key) ?: return@forEach
            if (child.has(TYPE)) {
                tokens[(path + key).joinToString("/")] = child
            } else {
                walk(child, path + key)
            }
        }
    }
    walk(root, emptyList())
    return tokens
}

/** `{a.b.c}` -> `a/b/c`, or null when the value is not a DTCG reference. */
private fun dtcgReferenceTarget(node: JSONObject): String? {
    val value = node.opt(VALUE)
    if (value !is String) return null
    if (!value.startsWith("{") || !value.endsWith("}")) return null
    return value.substring(1, value.length - 1).replace('.', '/')
}

private fun dtcgResolvedValue(tokens: Map<String, JSONObject>, name: String, seen: MutableSet<String>): Any {
    val node = tokens[name] ?: error("DTCG reference '$name' does not exist in this document")
    val target = dtcgReferenceTarget(node)
    if (target != null) {
        if (!seen.add(name)) error("DTCG reference cycle involving '$name'")
        return dtcgResolvedValue(tokens, target, seen)
    }
    return node.get(VALUE)
}

/**
 * Builds the object the converters already expect:
 * `{ "resolvedValue": <number|string|{r,g,b,a}>, "alias": .., "aliasName": .. }`.
 */
fun dtcgResolvedValueObject(tokens: Map<String, JSONObject>, name: String): JSONObject {
    val node = tokens.getValue(name)
    val result = JSONObject()

    val declaredType = node.optString(TYPE)
    require(declaredType in setOf("color", "number", "string")) {
        "Unsupported DTCG \$type '$declaredType' on token '$name'"
    }

    when (val resolved = dtcgResolvedValue(tokens, name, mutableSetOf())) {
        is JSONObject -> {
            val components = resolved.getJSONArray("components")
            result.put(
                "resolvedValue",
                JSONObject()
                    .put("r", components.getDouble(0))
                    .put("g", components.getDouble(1))
                    .put("b", components.getDouble(2))
                    .put("a", resolved.optDouble("alpha", 1.0)),
            )
        }
        else -> result.put("resolvedValue", resolved)
    }

    val aliasData = node.optJSONObject(EXTENSIONS)?.optJSONObject("com.figma.aliasData")
    if (aliasData != null) {
        result.put("alias", aliasData.optString("targetVariableId"))
        result.put("aliasName", aliasData.optString("targetVariableName"))
    } else {
        val target = dtcgReferenceTarget(node)
        if (target != null) {
            result.put("aliasName", target)
            val targetId = tokens[target]?.optJSONObject(EXTENSIONS)?.optString("com.figma.variableId")
            if (!targetId.isNullOrBlank()) result.put("alias", targetId)
        }
    }
    return result
}

private fun isHidden(node: JSONObject): Boolean =
    node.optJSONObject(EXTENSIONS)?.optBoolean("com.figma.hiddenFromPublishing") ?: false

fun dtcgModeName(json: JSONObject): String =
    json.optJSONObject(EXTENSIONS)?.optString("com.figma.modeName").orEmpty()

/** The distinct mode names across [files], in file order. Works for both formats. */
fun availableModeNames(files: List<File>): List<String> {
    val names = mutableListOf<String>()
    files.forEach { file ->
        val json = JSONObject(file.readText())
        if (isDtcgDocument(json)) {
            val mode = dtcgModeName(json)
            if (mode.isNotBlank() && mode !in names) names.add(mode)
        } else {
            val modes = json.getJSONObject("modes")
            modes.keys().asSequence().toList().sorted().forEach { key ->
                val mode = modes.getString(key)
                if (mode !in names) names.add(mode)
            }
        }
    }
    return names
}

/** First existing file among [candidates], resolved under `tokens/`. */
fun tokenFile(vararg candidates: String): File =
    candidates.map { File("tokens/$it") }.firstOrNull { it.isFile }
        ?: error("None of ${candidates.joinToString()} exist under tokens/")

/** All `tokens/` files whose name starts with [prefix], sorted by name. */
fun tokenFiles(prefix: String): List<File> =
    (File("tokens").listFiles() ?: emptyArray())
        .filter { it.isFile && it.name.startsWith(prefix) && it.name.endsWith(".json") }
        .sortedBy { it.name }

private fun <T> dtcgResources(
    json: JSONObject,
    resourceMap: (JSONObject) -> T,
): List<ResourceData<T>> {
    val tokens = dtcgTokens(json)
    return tokens.keys
        .sortedWith(::canonicalTokenOrder)
        .filterNot { isHidden(tokens.getValue(it)) }
        .map { name ->
            ResourceData(
                groups = name.sanitizedGroups(),
                groupFullName = name.sanitizedClassName(),
                name = name.sanitizedValueName(),
                value = resourceMap(dtcgResolvedValueObject(tokens, name)),
            )
        }
}
```

- [ ] **Step 2: Route `readFileResourceFile` through the new code**

Replace the body of the existing `readFileResourceFile` in the same file:

```kotlin
fun <T> readFileResourceFile(
    file: File,
    resourceMap: (JSONObject) -> T,
): List<ResourceData<T>> {
    val json = JSONObject(file.readText())
    if (isDtcgDocument(json)) {
        val resources = dtcgResources(json, resourceMap)
        println("Found ${resources.size} variables")
        return resources
    }
    val variablesJsonArray = json.getJSONArray("variables")
    val resources = mutableListOf<ResourceData<T>>()
    println("Found ${variablesJsonArray.length()} variables")
    repeat(times = variablesJsonArray.length()) { index ->
        val variableJsonObject = variablesJsonArray.getJSONObject(index)
        if (!variableJsonObject.optBoolean("hiddenFromPublishing")) {
            val name = variableJsonObject.getString("name")
            val resolvedValues = variableJsonObject.getJSONObject("resolvedValuesByMode")
            resolvedValues.keys().asSequence().firstOrNull()?.let { resolvedValueKey ->
                val resolvedValueKeyObject = resolvedValues.getJSONObject(resolvedValueKey)
                resources.add(
                    ResourceData(
                        groups = name.sanitizedGroups(),
                        groupFullName = name.sanitizedClassName(),
                        name = name.sanitizedValueName(),
                        value = resourceMap(resolvedValueKeyObject),
                    )
                )
            }
        }
    }
    return resources
}
```

- [ ] **Step 3: Replace `readFileResourceFileByMode` with the mode-name form**

Delete the existing `readFileResourceFileByMode` and put this in its place:

```kotlin
/**
 * Resources for one mode. Accepts both formats: a DTCG export splits modes
 * across files (matched on `com.figma.modeName`), a plugin export keeps them
 * in one file's `modes` map (matched on the mode's display name).
 */
fun <T> readFileResourceFileByMode(
    files: List<File>,
    modeName: String,
    resourceMap: (JSONObject) -> T,
): List<ResourceData<T>> {
    files.forEach { file ->
        val json = JSONObject(file.readText())
        if (isDtcgDocument(json)) {
            if (!dtcgModeName(json).equals(modeName, ignoreCase = true)) return@forEach
            val resources = dtcgResources(json, resourceMap)
            println("Found ${resources.size} variables for mode $modeName")
            return resources
        }

        val modes = json.getJSONObject("modes")
        val modeKey = modes.keys().asSequence().firstOrNull { key ->
            modes.getString(key).equals(modeName, ignoreCase = true)
        } ?: return@forEach

        val variablesJsonArray = json.getJSONArray("variables")
        val resources = mutableListOf<ResourceData<T>>()
        println("Found ${variablesJsonArray.length()} variables")
        repeat(times = variablesJsonArray.length()) { index ->
            val variableJsonObject = variablesJsonArray.getJSONObject(index)
            if (!variableJsonObject.optBoolean("hiddenFromPublishing")) {
                val name = variableJsonObject.getString("name")
                val resolvedValues = variableJsonObject.getJSONObject("resolvedValuesByMode")
                if (resolvedValues.has(modeKey)) {
                    resources.add(
                        ResourceData(
                            groups = name.sanitizedGroups(),
                            groupFullName = name.sanitizedClassName(),
                            name = name.sanitizedValueName(),
                            value = resourceMap(resolvedValues.getJSONObject(modeKey)),
                        )
                    )
                }
            }
        }
        return resources
    }
    error("No token file provides mode '$modeName'")
}
```

- [ ] **Step 4: Run the unit test to verify it passes**

```bash
~/.local/kotlin-2.3.20/kotlinc/bin/kotlin scripts/kmp-loader-dtcg-test.main.kts
```

Expected: every `ok` line prints, ending with `ALL PASSED`.

- [ ] **Step 5: Update the one caller of the changed signature**

`scripts/kmp-theme-token-converter.main.kts` calls `readFileResourceFileByMode(file = ..., modeKey = ...)` twice and reads `modes` directly. Replace the whole `modeKeys`/`modesObject` preamble and both call sites. In `main()`, replace lines from `val fileContent = colorTokensFile.readText()` through the end of the `modeKeys.forEach { ... }` block, and the interface-generation block below it, with:

```kotlin
        val themeFiles = tokenFiles("theme-colors")
        val modeNames = availableModeNames(themeFiles)

        modeNames.forEach { modeName ->
            val themeName = when {
                modeName.equals("Light", ignoreCase = true) -> "LemonadeLightTheme"
                modeName.equals("Dark", ignoreCase = true) -> "LemonadeDarkTheme"
                else -> "Lemonade${modeName}Theme"
            }

            val themeResources = readFileResourceFileByMode(
                files = themeFiles,
                modeName = modeName,
                resourceMap = { jsonObject ->
                    val aliasName = jsonObject.optString("aliasName")
                    val groups = aliasName?.sanitizedGroups().orEmpty()
                    if (!aliasName.isNullOrBlank() && groups.isNotEmpty()) {
                        ThemeResourceData(
                            valueName = aliasName.sanitizedValueName(),
                            valueGroup = if (groups.contains("Alpha")) {
                                "Alpha.${groups.first()}"
                            } else {
                                "Solid.${groups.first()}"
                            },
                        )
                    } else {
                        null
                    }
                },
            ).filterNull()

            println("✓ Loaded $modeName theme resource")

            val classCode = buildThemeCode(
                fileName = themeName,
                scriptFilePath = "scripts/kmp-theme-token-converter.main.kts",
                resources = themeResources,
                themeName = modeName,
            )
            println("✓ $modeName implementation generated")

            File(themesOutputDir, "$themeName.kt").writeText(classCode)
            println("✓ $themeName.kt created")
        }

        val themeResources = readFileResourceFileByMode(
            files = themeFiles,
            modeName = modeNames.first { it.equals("Light", ignoreCase = true) },
            resourceMap = { jsonObject ->
                val aliasName = jsonObject.optString("aliasName")
                val groups = aliasName?.sanitizedGroups().orEmpty()
                if (!aliasName.isNullOrBlank() && groups.isNotEmpty()) {
                    ThemeResourceData(
                        valueName = aliasName.sanitizedValueName(),
                        valueGroup = if (groups.contains("Alpha")) {
                            "Alpha.${groups.first()}"
                        } else {
                            "Solid.${groups.first()}"
                        },
                    )
                } else {
                    null
                }
            },
        ).filterNull()
```

Also change the file handle at the top of `main()`:

```kotlin
    val colorTokensFile = tokenFile("theme-colors.light.tokens.json", "theme-colors.json")
```

and delete the now-unused `import org.json.JSONObject` only if no other reference remains.

- [ ] **Step 6: Verify the KMP pipeline still reproduces the baseline**

Nothing has switched to DTCG yet — `tokens/` is still plugin format — so this must be byte-identical.

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: `PASS: generated output is byte-identical to origin/main`.

- [ ] **Step 7: Commit**

```bash
git add scripts/kmp-resource-file-loading.main.kts scripts/kmp-theme-token-converter.main.kts
git commit -m "feat(tokens): teach the KMP loader to read Figma native DTCG exports"
```

---

### Task 4: Ingest script

**Files:**
- Create: `.claude/skills/generate-tokens/scripts/ingest-tokens.py`

**Interfaces:**
- Produces: `ingest-tokens.py <export-dir>` — routes each `*.tokens.json` to its canonical `tokens/` filename by Figma variable-id overlap. Exits non-zero and writes nothing on any ambiguity.

- [ ] **Step 1: Write the script**

Create `.claude/skills/generate-tokens/scripts/ingest-tokens.py`:

```python
#!/usr/bin/env python3
"""Copy a Figma native variable export into tokens/, routing by content.

Figma's filenames are not a contract: the `Size` collection exports as
`sizing.tokens.json`, `.Shadow` as `shadow.tokens.json`, and theme modes as
`light`/`dark`. So each incoming file is matched to its destination by the
overlap of its Figma variable ids with the file already committed there.

Usage (from the repo root):
    python3 .claude/skills/generate-tokens/scripts/ingest-tokens.py <export-dir>

Writes nothing unless every file routes unambiguously.
"""
import json
import pathlib
import shutil
import sys

TOKENS = pathlib.Path("tokens")

# Logical collection -> (new DTCG filename(s), legacy plugin filename)
TARGETS = {
    "border-width": ("border-width.tokens.json", "border-width.json"),
    "opacity": ("opacity.tokens.json", "opacity.json"),
    "primitive-colors": ("primitive-colors.tokens.json", "primitive-colors.json"),
    "radius": ("radius.tokens.json", "radius.json"),
    "shadow": ("shadow.tokens.json", "shadow.json"),
    "size": ("size.tokens.json", "size.json"),
    "spacing": ("spacing.tokens.json", "spacing.json"),
    "theme-colors": ("theme-colors.{mode}.tokens.json", "theme-colors.json"),
    "typography": ("typography.tokens.json", "typography.json"),
}

MIN_OVERLAP = 0.5
MIN_MARGIN = 0.2
MAX_SHRINK = 0.10  # a collection may not lose more than 10% of its tokens


def leaves(node, path=()):
    for key, value in node.items():
        if key.startswith("$") or not isinstance(value, dict):
            continue
        if "$type" in value:
            yield "/".join(path + (key,)), value
        else:
            yield from leaves(value, path + (key,))


def dtcg_ids(doc):
    out = set()
    for _, token in leaves(doc):
        vid = token.get("$extensions", {}).get("com.figma.variableId")
        if vid:
            out.add(vid)
    return out


def dtcg_count(doc):
    return sum(1 for _ in leaves(doc))


def reference_ids(collection):
    """Variable ids currently committed for `collection`, in either format."""
    new_name, legacy_name = TARGETS[collection]
    if "{mode}" in new_name:
        ids, count = set(), 0
        for path in sorted(TOKENS.glob("theme-colors.*.tokens.json")):
            doc = json.loads(path.read_text())
            ids |= dtcg_ids(doc)
            count = max(count, dtcg_count(doc))
        if ids:
            return ids, count
    else:
        path = TOKENS / new_name
        if path.is_file():
            doc = json.loads(path.read_text())
            return dtcg_ids(doc), dtcg_count(doc)

    legacy = TOKENS / legacy_name
    if legacy.is_file():
        doc = json.loads(legacy.read_text())
        variables = doc.get("variables", [])
        return {v["id"] for v in variables}, len(variables)
    return set(), 0


def main():
    if len(sys.argv) != 2:
        sys.exit(__doc__)
    export_dir = pathlib.Path(sys.argv[1]).expanduser()
    if not export_dir.is_dir():
        sys.exit(f"error: {export_dir} is not a directory")
    if not TOKENS.is_dir():
        sys.exit("error: run this from the repo root (no tokens/ directory here)")

    references = {name: reference_ids(name) for name in TARGETS}
    incoming = sorted(export_dir.glob("*.tokens.json"))
    if not incoming:
        sys.exit(f"error: no *.tokens.json files in {export_dir}")

    planned = []       # (source_path, destination_name)
    claimed = {}       # destination_name -> source_path
    problems = []

    for source in incoming:
        try:
            doc = json.loads(source.read_text())
        except json.JSONDecodeError as exc:
            problems.append(f"{source.name}: not valid JSON ({exc})")
            continue

        ids = dtcg_ids(doc)
        if not ids:
            problems.append(f"{source.name}: no Figma variable ids found")
            continue

        scored = sorted(
            (
                (len(ids & ref_ids) / len(ids | ref_ids) if ref_ids else 0.0, name, ref_count)
                for name, (ref_ids, ref_count) in references.items()
            ),
            reverse=True,
        )
        best_score, best_name, ref_count = scored[0]
        runner_up = scored[1][0] if len(scored) > 1 else 0.0

        if best_score < MIN_OVERLAP:
            problems.append(
                f"{source.name}: no destination matched (best {best_name} at {best_score:.3f})"
            )
            continue
        if best_score - runner_up < MIN_MARGIN:
            problems.append(
                f"{source.name}: ambiguous — {best_name} {best_score:.3f} vs runner-up {runner_up:.3f}"
            )
            continue

        count = dtcg_count(doc)
        if ref_count and count < ref_count * (1 - MAX_SHRINK):
            problems.append(
                f"{source.name}: only {count} tokens vs {ref_count} committed "
                f"— looks like a partial export"
            )
            continue

        new_name, _ = TARGETS[best_name]
        if "{mode}" in new_name:
            mode = doc.get("$extensions", {}).get("com.figma.modeName", "")
            if not mode:
                problems.append(f"{source.name}: multi-mode collection with no com.figma.modeName")
                continue
            destination = new_name.format(mode=mode.lower())
        else:
            destination = new_name

        if destination in claimed:
            problems.append(
                f"{destination}: claimed by both {claimed[destination].name} and {source.name}"
            )
            continue
        claimed[destination] = source
        planned.append((source, destination))
        print(f"  {source.name:32} -> tokens/{destination}   (overlap {best_score:.3f})")

    expected = {new_name for new_name, _ in TARGETS.values() if "{mode}" not in new_name}
    missing = expected - set(claimed)
    if missing:
        problems.append(f"no incoming file for: {', '.join(sorted(missing))}")

    theme_files = [d for d in claimed if d.startswith("theme-colors.")]
    if len(theme_files) < 2:
        problems.append(
            f"expected a light and a dark theme file, got {len(theme_files)}: {theme_files}"
        )

    if problems:
        print("\nRefusing to write. Problems:", file=sys.stderr)
        for problem in problems:
            print(f"  - {problem}", file=sys.stderr)
        sys.exit(1)

    for source, destination in planned:
        target = TOKENS / destination
        shutil.copyfile(source, target)
        text = target.read_text()
        if not text.endswith("\n"):
            target.write_text(text + "\n")

    print(f"\nWrote {len(planned)} token file(s) into tokens/.")


if __name__ == "__main__":
    main()
```

Note the script copies bytes verbatim rather than re-serializing — re-encoding would risk altering float representations in 396 primitive colours.

- [ ] **Step 2: Run it against the real export**

```bash
chmod +x .claude/skills/generate-tokens/scripts/ingest-tokens.py
python3 .claude/skills/generate-tokens/scripts/ingest-tokens.py ~/Desktop/lemonade-tokens-export
```

Expected: ten routing lines, every overlap `1.000`, then `Wrote 10 token file(s) into tokens/.`

- [ ] **Step 3: Verify it refuses a partial export**

```bash
mkdir -p /tmp/partial-export
cp ~/Desktop/lemonade-tokens-export/radius.tokens.json /tmp/partial-export/
python3 .claude/skills/generate-tokens/scripts/ingest-tokens.py /tmp/partial-export; echo "exit=$?"
```

Expected: `exit=1` and a "no incoming file for: ..." problem list. Nothing written.

- [ ] **Step 4: Trim the two drifted shadow values**

Verification requires the export to match the committed baseline:

```bash
python3 - <<'PY'
import json, pathlib
p = pathlib.Path("tokens/shadow.tokens.json")
d = json.loads(p.read_text())
lv1 = d["shadow"]["xsmall"]["level-1"]
lv1["sd-xs-lv1-blur"]["$value"] = 2
lv1["sd-xs-lv1-offset-y"]["$value"] = 1
p.write_text(json.dumps(d, indent=2) + "\n")
print("trimmed sd-xs-lv1-blur -> 2, sd-xs-lv1-offset-y -> 1")
PY
```

- [ ] **Step 5: Confirm the old files are untouched and generation still passes**

```bash
git status --short tokens/
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: ten untracked `*.tokens.json` files, the nine legacy `*.json` unmodified, and PASS — no converter reads the new files yet.

- [ ] **Step 6: Commit the script and the ingested tokens**

```bash
git add .claude/skills/generate-tokens/scripts/ingest-tokens.py tokens/
git commit -m "feat(tokens): add content-routed ingest and the native DTCG export"
```

---

### Task 5: Point KMP at the DTCG files

The first task where output actually changes. Colour files reshuffle; everything else must hold byte-for-byte.

**Files:**
- Modify: `scripts/kmp-color-token-converter.main.kts`, `scripts/kmp-theme-token-converter.main.kts`, `scripts/kmp-radius-token-converter.main.kts`, `scripts/kmp-spacing-token-converter.main.kts`, `scripts/kmp-dimension-token-converter.main.kts`, `scripts/kmp-opacity-token-converter.main.kts`, `scripts/kmp-border-width-token-converter.main.kts`, `scripts/kmp-shadow-token-converter.main.kts`, `scripts/kmp-typography-token-converter.main.kts`

**Interfaces:**
- Consumes: `tokenFile()`, `tokenFiles()` from Task 3.
- Produces: nothing new.

- [ ] **Step 1: Switch every KMP converter's token file**

Replace each `File("tokens/<x>.json")` with a `tokenFile(...)` call that prefers the DTCG name:

| Script | New expression |
|---|---|
| `kmp-color-token-converter` | `tokenFile("primitive-colors.tokens.json", "primitive-colors.json")` |
| `kmp-radius-token-converter` | `tokenFile("radius.tokens.json", "radius.json")` |
| `kmp-spacing-token-converter` | `tokenFile("spacing.tokens.json", "spacing.json")` |
| `kmp-dimension-token-converter` | `tokenFile("size.tokens.json", "size.json")` |
| `kmp-opacity-token-converter` | `tokenFile("opacity.tokens.json", "opacity.json")` |
| `kmp-border-width-token-converter` | `tokenFile("border-width.tokens.json", "border-width.json")` |
| `kmp-shadow-token-converter` | `tokenFile("shadow.tokens.json", "shadow.json")` |
| `kmp-typography-token-converter` | `tokenFile("typography.tokens.json", "typography.json")` |

`kmp-theme-token-converter` already uses `tokenFiles("theme-colors")` from Task 3 Step 5.

- [ ] **Step 2: Add the explicit font-weight sort**

In `scripts/kmp-typography-token-converter.main.kts`, the font-weight enum's entry order is consumer-visible (it sets every ordinal). Extract the string→weight mapping so the sort and the emitted value cannot diverge.

Add near the top of the file, outside `main()`:

```kotlin
/**
 * Numeric weight for a Figma font-weight token. The token itself carries only
 * the style name, so this mapping is the single source of truth for both the
 * emitted value and the enum's entry order.
 */
fun fontWeightValue(styleName: String?): Int = when (styleName) {
    "Bold" -> 700
    "SemiBold" -> 600
    "Medium" -> 500
    "Regular" -> 400
    else -> 400
}
```

Change the resource selection to sort by it, descending:

```kotlin
        val fontWeightResources = allResources
            .filter { it.groups.firstOrNull() == "FontWeight" }
            .sortedByDescending { fontWeightValue(it.value.stringValue) }
```

And replace the inline `when` inside `buildFontWeightsDefinitionCode` (the `val weightInt = when (resource.value.stringValue) { ... }` block) with:

```kotlin
        val weightInt = fontWeightValue(resource.value.stringValue)
```

- [ ] **Step 3: Add the explicit shadow level sort**

In `scripts/kmp-shadow-token-converter.main.kts`, `toShadowResource()` derives layer order from encounter order, and layer order inside `sequenceOf` is consumer-visible rendering behaviour. Make it explicit — change:

```kotlin
            val groupLevels = resources.groupBy { groupResource -> groupResource.groups[2] }
            val levels = groupLevels.map { (_, levelResources) ->
```

to:

```kotlin
            val groupLevels = resources.groupBy { groupResource -> groupResource.groups[2] }
            val levels = groupLevels.entries.sortedBy { (levelName, _) -> levelName }.map { (_, levelResources) ->
```

- [ ] **Step 4: Regenerate and verify**

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: `ok ... (reordered only)` for `LemonadePrimitiveColors.kt`, `LemonadeLightTheme.kt`, `LemonadeDarkTheme.kt` and `LemonadeSemanticColors.kt`; every other file absent from the diff entirely; final line `PASS: only permitted reordering found`.

If any non-colour KMP file appears as `FAIL (must be byte-identical)`, the diff names the defect — most likely a missing sort or a mis-synthesized value. Fix before committing.

- [ ] **Step 5: Confirm the public API is unchanged**

```bash
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
.claude/skills/binary-compatibility/scripts/bcv-check.sh --ci
```

Expected: `NO_CHANGES`.

- [ ] **Step 6: Commit**

```bash
git add scripts/kmp-*.main.kts kmp/
git commit -m "feat(tokens): generate KMP tokens from the native DTCG export"
```

---

### Task 6: SwiftUI loader and converters

**Files:**
- Modify: `scripts/swiftui-resource-file-loading.main.kts`, `scripts/swiftui-theme-token-converter.main.kts`, `scripts/swiftui-color-assets-generator.main.kts`, and the seven other `swiftui-*-token-converter.main.kts`

**Interfaces:**
- Produces: the same helper set as Task 3, but named for Swift — `sanitizedSwiftValueName()` / `sanitizedSwiftClassName()` are used in place of the KMP names.

- [ ] **Step 1: Port the DTCG block**

Copy the entire "Figma native (DTCG) support" block from `scripts/kmp-resource-file-loading.main.kts` into `scripts/swiftui-resource-file-loading.main.kts`, then change the two name-sanitizing calls inside `dtcgResources`:

```kotlin
            ResourceData(
                groups = name.sanitizedGroups(),
                groupFullName = name.sanitizedSwiftClassName(),
                name = name.sanitizedSwiftValueName(),
                value = resourceMap(dtcgResolvedValueObject(tokens, name)),
            )
```

Apply the same `readFileResourceFile` routing and `readFileResourceFileByMode` replacement as Task 3 Steps 2–3, again substituting `sanitizedSwiftClassName()` / `sanitizedSwiftValueName()` in the plugin-format branches.

- [ ] **Step 2: Rework the SwiftUI theme converter**

`scripts/swiftui-theme-token-converter.main.kts` reads `modes` and walks `variables` to build asset names. Replace the preamble in `main()` — from `val fileContent = colorTokensFile.readText()` down to the end of the `resourcesWithAssets` loop — with:

```kotlin
        val themeFiles = tokenFiles("theme-colors")
        val modeNames = availableModeNames(themeFiles)
        val lightMode = modeNames.first { it.equals("Light", ignoreCase = true) }

        val lightFile = themeFiles.first { file ->
            val json = JSONObject(file.readText())
            if (isDtcgDocument(json)) dtcgModeName(json).equals(lightMode, ignoreCase = true) else true
        }
        val lightJson = JSONObject(lightFile.readText())

        val tokenNames = if (isDtcgDocument(lightJson)) {
            val tokens = dtcgTokens(lightJson)
            tokens.keys
                .sortedWith(::canonicalTokenOrder)
                .filterNot { name ->
                    tokens.getValue(name).optJSONObject("\$extensions")
                        ?.optBoolean("com.figma.hiddenFromPublishing") ?: false
                }
        } else {
            val variablesJson = lightJson.getJSONArray("variables")
            (0 until variablesJson.length())
                .map { variablesJson.getJSONObject(it) }
                .filterNot { it.optBoolean("hiddenFromPublishing") }
                .map { it.getString("name") }
        }

        val themeResources = readFileResourceFileByMode(
            files = themeFiles,
            modeName = lightMode,
            resourceMap = { _ -> Unit },
        )

        val resourcesWithAssets = mutableListOf<Pair<ResourceData<Unit>, String>>()
        tokenNames.forEach { name ->
            val assetName = "lemonade-${name.split("/").joinToString("-") { it.lowercase().replace("_", "-") }}"
            val resource = themeResources.find { it.name == name.sanitizedSwiftValueName() }
            if (resource != null) {
                resourcesWithAssets.add(resource to assetName)
            }
        }
```

Change the file handle at the top of `main()` to:

```kotlin
    val colorTokensFile = tokenFile("theme-colors.light.tokens.json", "theme-colors.json")
```

- [ ] **Step 3: Rework the colour assets generator**

**First**, `scripts/swiftui-color-assets-generator.main.kts` does not currently import the shared loader — it parses the token JSON itself. Add the import directly beneath the existing `@file:DependsOn` line, so the helpers below are in scope:

```kotlin
@file:Import("swiftui-resource-file-loading.main.kts")
```

There is no symbol collision: the generator's `ColorValue`, `sanitizeGroup()` and `sanitizeSwiftName()` do not exist in the loader, and the loader declares no `main()`.

Then replace `parseThemeColors(file, modeKey)` with a form that reads a whole file, and drive it from the mode list. Replace the `modes`-reading preamble in `main()`:

```kotlin
        val themeFiles = tokenFiles("theme-colors")
        val modeNames = availableModeNames(themeFiles)
        val lightColors = parseThemeColors(themeFiles, modeNames.first { it.equals("Light", ignoreCase = true) })
        println("✓ Loaded ${lightColors.size} colors from light theme")
        val darkColors = parseThemeColors(themeFiles, modeNames.first { it.equals("Dark", ignoreCase = true) })
        println("✓ Loaded ${darkColors.size} colors from dark theme")
```

and replace the whole `parseThemeColors` function with:

```kotlin
fun parseThemeColors(files: List<File>, modeName: String): Map<String, ColorValue> {
    val colors = linkedMapOf<String, ColorValue>()
    readFileResourceFileByModeRaw(files, modeName) { name, resolved ->
        colors[name] = ColorValue(
            r = resolved.getDouble("r"),
            g = resolved.getDouble("g"),
            b = resolved.getDouble("b"),
            a = resolved.optDouble("a", 1.0),
        )
    }
    return colors
}
```

`parseThemeColors` needs the *raw* token name rather than the sanitized one, so add this helper to `scripts/swiftui-resource-file-loading.main.kts` alongside the DTCG block:

```kotlin
/**
 * Visits each non-hidden token for [modeName], handing the callback the raw
 * token name (e.g. `Content/Brand/content-accent`) and its resolved value
 * object. Used where the caller needs the unsanitized name.
 */
fun readFileResourceFileByModeRaw(
    files: List<File>,
    modeName: String,
    visit: (String, JSONObject) -> Unit,
) {
    files.forEach { file ->
        val json = JSONObject(file.readText())
        if (isDtcgDocument(json)) {
            if (!dtcgModeName(json).equals(modeName, ignoreCase = true)) return@forEach
            val tokens = dtcgTokens(json)
            tokens.keys
                .sortedWith(::canonicalTokenOrder)
                .filterNot { name ->
                    tokens.getValue(name).optJSONObject("\$extensions")
                        ?.optBoolean("com.figma.hiddenFromPublishing") ?: false
                }
                .forEach { name ->
                    val resolved = dtcgResolvedValueObject(tokens, name).optJSONObject("resolvedValue")
                    if (resolved != null) visit(name, resolved)
                }
            return
        }

        val modes = json.getJSONObject("modes")
        val modeKey = modes.keys().asSequence().firstOrNull { key ->
            modes.getString(key).equals(modeName, ignoreCase = true)
        } ?: return@forEach
        val variables = json.getJSONArray("variables")
        repeat(variables.length()) { index ->
            val variable = variables.getJSONObject(index)
            if (!variable.optBoolean("hiddenFromPublishing")) {
                val resolved = variable.getJSONObject("resolvedValuesByMode")
                    .optJSONObject(modeKey)?.optJSONObject("resolvedValue")
                if (resolved != null) visit(variable.getString("name"), resolved)
            }
        }
        return
    }
    error("No token file provides mode '$modeName'")
}
```

- [ ] **Step 4: Switch the remaining SwiftUI converters' token files**

| Script | New expression |
|---|---|
| `swiftui-color-token-converter` | `tokenFile("primitive-colors.tokens.json", "primitive-colors.json")` |
| `swiftui-radius-token-converter` | `tokenFile("radius.tokens.json", "radius.json")` |
| `swiftui-spacing-token-converter` | `tokenFile("spacing.tokens.json", "spacing.json")` |
| `swiftui-size-token-converter` | `tokenFile("size.tokens.json", "size.json")` |
| `swiftui-opacity-token-converter` | `tokenFile("opacity.tokens.json", "opacity.json")` |
| `swiftui-border-token-converter` | `tokenFile("border-width.tokens.json", "border-width.json")` |
| `swiftui-shadow-token-converter` | `tokenFile("shadow.tokens.json", "shadow.json")` |
| `swiftui-typography-token-converter` | `tokenFile("typography.tokens.json", "typography.json")` |

- [ ] **Step 5: Apply the same two ordering fixes**

In `scripts/swiftui-typography-token-converter.main.kts`, add the identical `fontWeightValue` helper from Task 5 Step 2, sort `fontWeightResources` with `.sortedByDescending { fontWeightValue(it.value.stringValue) }`, and replace the inline `when` with a call to it.

In `scripts/swiftui-shadow-token-converter.main.kts`, apply the same `.entries.sortedBy { (levelName, _) -> levelName }` change as Task 5 Step 3.

- [ ] **Step 6: Regenerate and verify**

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: reordering permitted only in `LemonadePrimitiveColors.swift`, `LemonadeSemanticColors.swift`, `LemonadeAdaptiveTheme.swift` and `Color+Lemonade.swift`. Critically, **no `.colorset` file may appear in the diff at all** — those are per-token files and must be byte-identical.

- [ ] **Step 7: Confirm the asset catalogue is intact**

```bash
ls swiftui/Sources/Lemonade/Resources/Assets.xcassets/Colors/ | wc -l
git status --short swiftui/Sources/Lemonade/Resources/
```

Expected: `138` (137 colorsets + `Contents.json`), and no modifications reported.

- [ ] **Step 8: Commit**

```bash
git add scripts/swiftui-*.main.kts swiftui/
git commit -m "feat(tokens): generate SwiftUI tokens from the native DTCG export"
```

---

### Task 7: Flutter loader and converters

**Files:**
- Modify: `scripts/flutter-resource-file-loading.main.kts`, `scripts/flutter-theme-token-converter.main.kts`, `scripts/flutter-shadow-token-converter.main.kts`, and the six other `flutter-*-token-converter.main.kts`

**Interfaces:**
- Consumes: nothing from other tasks — Flutter has its own loader.
- Produces: nothing consumed elsewhere.

- [ ] **Step 1: Port the DTCG block**

Copy the "Figma native (DTCG) support" block from `scripts/kmp-resource-file-loading.main.kts` into `scripts/flutter-resource-file-loading.main.kts` verbatim — its sanitizer names (`sanitizedClassName`, `sanitizedValueName`) already match. Apply the Task 3 Step 2 routing change to `readFileResourceFile`.

Flutter has no `readFileResourceFileByMode`; do not add one.

- [ ] **Step 2: Switch every Flutter converter's token file**

| Script | New expression |
|---|---|
| `flutter-color-token-converter` | `tokenFile("primitive-colors.tokens.json", "primitive-colors.json")` |
| `flutter-theme-token-converter` | `tokenFile("theme-colors.light.tokens.json", "theme-colors.json")` |
| `flutter-border-token-converter` | `tokenFile("border-width.tokens.json", "border-width.json")` |
| `flutter-opacity-token-converter` | `tokenFile("opacity.tokens.json", "opacity.json")` |
| `flutter-radius-token-converter` | `tokenFile("radius.tokens.json", "radius.json")` |
| `flutter-size-token-converter` | `tokenFile("size.tokens.json", "size.json")` |
| `flutter-spacing-token-converter` | `tokenFile("spacing.tokens.json", "spacing.json")` |
| `flutter-shadow-token-converter` | `tokenFile("shadow.tokens.json", "shadow.json")` |

`flutter-theme-token-converter` generates only the light theme and uses `readFileResourceFile`, which under DTCG reads exactly the file it is handed — so pointing it at the light file is the complete change.

- [ ] **Step 3: Apply the shadow level sort**

Apply the same `.entries.sortedBy { (levelName, _) -> levelName }` change as Task 5 Step 3 to `scripts/flutter-shadow-token-converter.main.kts`.

- [ ] **Step 4: Regenerate Flutter and verify**

The skill's runner intentionally excludes Flutter, so invoke the converters directly:

```bash
for c in color theme border opacity radius size spacing shadow; do
  echo "==> flutter-$c"
  ~/.local/kotlin-2.3.20/kotlinc/bin/kotlin "scripts/flutter-$c-token-converter.main.kts"
done
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: only Flutter colour/theme Dart files appear, as `ok ... (reordered only)`.

- [ ] **Step 5: Commit**

```bash
git add scripts/flutter-*.main.kts flutter/
git commit -m "feat(tokens): generate Flutter tokens from the native DTCG export"
```

---

### Task 8: Retire the plugin format

**Files:**
- Delete: `tokens/*.json` (the nine plugin files), `.claude/skills/generate-tokens/scripts/strip-stray-modes.py`
- Modify: every `scripts/*-token-converter.main.kts` and the three loaders, `.claude/skills/generate-tokens/scripts/run-converters.sh`

**Interfaces:**
- Produces: `tokenFile(name: String): File` — single-candidate form replacing the varargs one.

- [ ] **Step 1: Delete the plugin token files and the stray-mode workaround**

```bash
git rm tokens/border-width.json tokens/opacity.json tokens/primitive-colors.json \
       tokens/radius.json tokens/shadow.json tokens/size.json tokens/spacing.json \
       tokens/theme-colors.json tokens/typography.json
git rm .claude/skills/generate-tokens/scripts/strip-stray-modes.py
```

- [ ] **Step 2: Simplify `tokenFile` in all three loaders**

Replace the varargs helper with:

```kotlin
/** Resolves a token file under `tokens/`, failing loudly when it is absent. */
fun tokenFile(name: String): File =
    File("tokens/$name").takeIf { it.isFile }
        ?: error("tokens/$name does not exist — run ingest-tokens.py first")
```

Then update every call site to pass only the `.tokens.json` name — e.g. `tokenFile("radius.tokens.json")`.

- [ ] **Step 3: Delete the plugin-format branches**

In all three loaders, remove the legacy branch from `readFileResourceFile` so only the DTCG path remains:

```kotlin
fun <T> readFileResourceFile(
    file: File,
    resourceMap: (JSONObject) -> T,
): List<ResourceData<T>> {
    val json = JSONObject(file.readText())
    require(isDtcgDocument(json)) { "${file.path} is not a Figma native DTCG export" }
    val resources = dtcgResources(json, resourceMap)
    println("Found ${resources.size} variables")
    return resources
}
```

Apply the equivalent simplification to `readFileResourceFileByMode`, `readFileResourceFileByModeRaw` (SwiftUI only) and `availableModeNames`, deleting each plugin-format branch. `isDtcgDocument` stays — it now backs the `require`.

- [ ] **Step 4: Update the runner**

In `.claude/skills/generate-tokens/scripts/run-converters.sh`:

Replace the `converters_for()` cases with the new filenames, adding both theme files:

```bash
converters_for() {
  case "$1" in
    primitive-colors.tokens.json) echo "kmp-color-token-converter swiftui-color-token-converter" ;;
    theme-colors.light.tokens.json|theme-colors.dark.tokens.json)
                                  echo "kmp-theme-token-converter swiftui-theme-token-converter swiftui-color-assets-generator" ;;
    radius.tokens.json)           echo "kmp-radius-token-converter swiftui-radius-token-converter" ;;
    spacing.tokens.json)          echo "kmp-spacing-token-converter swiftui-spacing-token-converter" ;;
    size.tokens.json)             echo "kmp-dimension-token-converter swiftui-size-token-converter" ;;
    opacity.tokens.json)          echo "kmp-opacity-token-converter swiftui-opacity-token-converter" ;;
    border-width.tokens.json)     echo "kmp-border-width-token-converter swiftui-border-token-converter" ;;
    shadow.tokens.json)           echo "kmp-shadow-token-converter swiftui-shadow-token-converter" ;;
    typography.tokens.json)       echo "kmp-typography-token-converter swiftui-typography-token-converter" ;;
    *) echo "" ;;
  esac
}

ALL_FILES="primitive-colors.tokens.json theme-colors.light.tokens.json radius.tokens.json spacing.tokens.json size.tokens.json opacity.tokens.json border-width.tokens.json shadow.tokens.json typography.tokens.json"
```

Change the `--changed` glob from `'tokens/*.json'` to `'tokens/*.tokens.json'` in both `git diff` invocations.

Delete the entire stray-modes block:

```bash
# theme-colors.json must be stripped of stray Figma modes before conversion.
for f in "${files[@]}"; do
  if [ "$f" = "theme-colors.json" ]; then
    echo "==> Stripping stray modes from tokens/theme-colors.json"
    python3 "$SKILL_DIR/strip-stray-modes.py" tokens/theme-colors.json
  fi
done
```

Because `--all` now lists only the light theme file, running it still triggers all three theme converters exactly once, which is the desired behaviour.

- [ ] **Step 5: Regenerate everything and verify**

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
for c in color theme border opacity radius size spacing shadow; do
  ~/.local/kotlin-2.3.20/kotlinc/bin/kotlin "scripts/flutter-$c-token-converter.main.kts"
done
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Expected: identical result to Task 7 — colour files reordered, everything else untouched, `PASS`.

- [ ] **Step 6: Confirm the unit test still passes and the API is stable**

```bash
~/.local/kotlin-2.3.20/kotlinc/bin/kotlin scripts/kmp-loader-dtcg-test.main.kts
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
.claude/skills/binary-compatibility/scripts/bcv-check.sh --ci
```

Expected: `ALL PASSED`, then `NO_CHANGES`.

- [ ] **Step 7: Commit**

```bash
git add -A scripts/ tokens/ .claude/skills/generate-tokens/ kmp/ swiftui/ flutter/
git commit -m "refactor(tokens): retire the plugin export format"
```

---

### Task 9: Token drift CI job

**Files:**
- Create: `.github/workflows/token_drift.yml`

**Interfaces:**
- Consumes: `run-converters.sh`, `verify-generated.sh`.

- [ ] **Step 1: Write the workflow**

Create `.github/workflows/token_drift.yml`:

```yaml
name: Token Drift

on:
  pull_request:
    paths:
      - 'tokens/**'
      - 'scripts/*token*'
      - 'scripts/*resource-file-loading*'
      - '.claude/skills/generate-tokens/**'

jobs:
  token-drift:
    name: Generated code matches tokens
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'

      - name: Regenerate tokens
        run: .claude/skills/generate-tokens/scripts/run-converters.sh --all

      - name: Fail if generated code drifted
        run: |
          if [ -n "$(git status --porcelain -- kmp/ swiftui/)" ]; then
            echo "::error::Generated token code does not match tokens/. Run:"
            echo "  .claude/skills/generate-tokens/scripts/run-converters.sh --all"
            git --no-pager diff --stat -- kmp/ swiftui/
            git --no-pager diff -- kmp/ swiftui/ | head -100
            exit 1
          fi
          echo "Generated token code is in sync with tokens/."
```

Flutter is excluded to match the skill's existing policy of not running its converters by default.

- [ ] **Step 2: Verify the workflow parses**

```bash
python3 -c "import yaml,sys; yaml.safe_load(open('.github/workflows/token_drift.yml')); print('valid YAML')"
```

Expected: `valid YAML`.

- [ ] **Step 3: Verify the job's core assertion holds locally**

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
git status --porcelain -- kmp/ swiftui/
```

Expected: empty output — the tree is already in sync, which is exactly what the job asserts.

- [ ] **Step 4: Commit**

```bash
git add .github/workflows/token_drift.yml
git commit -m "ci(tokens): fail when generated code drifts from tokens/"
```

---

### Task 10: Documentation and the honest export

Restores the two trimmed shadow values so the committed export is a faithful copy of Figma, and rewrites the skill.

**Files:**
- Modify: `tokens/shadow.tokens.json`, `.claude/skills/generate-tokens/SKILL.md`

- [ ] **Step 1: Rewrite the skill's "two hard requirements" section**

In `.claude/skills/generate-tokens/SKILL.md`, delete requirement 2 (strip stray modes) entirely and replace the section heading with `## One hard requirement (it bites silently)`, keeping only the Kotlin 2.3.20 text.

- [ ] **Step 2: Document the new ingest step**

Replace the TL;DR block with:

````markdown
## TL;DR

From the repo root:

```bash
# 1. Ingest a fresh Figma native export (routes files by content, not by name)
python3 .claude/skills/generate-tokens/scripts/ingest-tokens.py ~/Downloads/<export-dir>

# 2. Regenerate only what changed vs HEAD (recommended)
.claude/skills/generate-tokens/scripts/run-converters.sh --changed

# 3. Confirm nothing consumer-visible moved unexpectedly
.claude/skills/generate-tokens/scripts/verify-generated.sh
```

Tokens are exported from Figma with **File → Export variables** (the native
export, not a plugin). It emits one `*.tokens.json` per collection, and one per
*mode* for multi-mode collections. Run it twice: once in the design-system file,
once in the **Colors** library file that holds the primitives.
````

- [ ] **Step 3: Update the token file → converter table**

Replace the table's left column with the new filenames (`primitive-colors.tokens.json`, `theme-colors.light.tokens.json` / `theme-colors.dark.tokens.json`, `radius.tokens.json`, `spacing.tokens.json`, `size.tokens.json`, `opacity.tokens.json`, `border-width.tokens.json`, `shadow.tokens.json`, `typography.tokens.json`), leaving the converter columns unchanged.

- [ ] **Step 4: Restore the two drifted shadow values**

The migration is proven; the repo should now hold the real export.

```bash
python3 - <<'PY'
import json, pathlib
p = pathlib.Path("tokens/shadow.tokens.json")
d = json.loads(p.read_text())
lv1 = d["shadow"]["xsmall"]["level-1"]
lv1["sd-xs-lv1-blur"]["$value"] = 1
lv1["sd-xs-lv1-offset-y"]["$value"] = 0.5
p.write_text(json.dumps(d, indent=2) + "\n")
print("restored sd-xs-lv1-blur -> 1, sd-xs-lv1-offset-y -> 0.5")
PY
.claude/skills/generate-tokens/scripts/run-converters.sh shadow.tokens.json
```

- [ ] **Step 5: Confirm the shadow change is the only new difference**

```bash
git --no-pager diff --stat -- kmp/ swiftui/
grep -n "blur\|offsetY" kmp/ui/src/commonMain/kotlin/com/teya/lemonade/LemonadeShadowsSequence.kt | head -6
```

Expected: only `LemonadeShadowsSequence.kt` and `LemonadeShadow.swift` changed, and the `Xsmall` layer now reads `blur = 1.0f` / `offsetY = 0.5f`. This is the intended design change, not a migration artifact.

- [ ] **Step 6: Commit**

```bash
git add tokens/shadow.tokens.json .claude/skills/generate-tokens/SKILL.md kmp/ swiftui/
git commit -m "feat(tokens): adopt the corrected xsmall shadow values from Figma"
```

- [ ] **Step 7: Final full verification**

```bash
.claude/skills/generate-tokens/scripts/run-converters.sh --all
git status --porcelain -- kmp/ swiftui/     # must be empty
~/.local/kotlin-2.3.20/kotlinc/bin/kotlin scripts/kmp-loader-dtcg-test.main.kts
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
.claude/skills/binary-compatibility/scripts/bcv-check.sh --ci
```

Expected: empty status, `ALL PASSED`, `NO_CHANGES`.

---

## PR notes

The **API Dump** section is mandatory whenever the baseline files change. Expected verdict here is `NO_CHANGES` — the migration changes only how tokens are read, and the two shadow values are floats inside method bodies, not signatures. If the baseline does change, that is a signal something reordered a public enum; investigate before writing it up.

Call out explicitly in the description:

1. **Colour source files are reordered, not changed.** `verify-generated.sh` proves this by comparing sorted lines. The old order came from the plugin's arbitrary array order; the new one is a canonical natural sort, which is why `LemonadePrimitiveColors` now reads `yellow50, yellow100, …` instead of `yellowLime900, yellowLime600, …`.
2. **`tokens/` carries a one-time metadata refresh** — iOS `codeSyntax` modernised from `context.lemonade.*` to `LemonadeTheme.*` on border-width/spacing/size, and spacing descriptions filled in. No converter reads those fields.
3. **Two shadow values changed** (`sd-xs-lv1-blur` 2→1, `sd-xs-lv1-offset-y` 1→0.5), adopted from Figma. Landed in its own commit so it can be reviewed separately from the migration.
4. **`LemonadeFontWeights` ordinals are now guaranteed by construction.** Previously they depended on the plugin's emission order; a designer reordering rows in Figma could have silently changed them.

## Follow-ups (not in this plan)

- `swiftui-color-assets-generator` never deletes `.colorset` directories, so a removed or renamed semantic token leaves an orphan in the SDK.
- `radius-350`'s Figma code syntax contains a typo: `LemonadeTheme.r adius.radius350`.
- Flutter's last release (`v0.8.3`, 2026-03-31) is five months behind SwiftUI's.
