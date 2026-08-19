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

changed="$(git diff --name-only "$REF" -- kmp/ swiftui/ flutter/ || true)"

if [ -z "$changed" ]; then
  echo "PASS: generated output is byte-identical to $REF"
  exit 0
fi

fail=0
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
