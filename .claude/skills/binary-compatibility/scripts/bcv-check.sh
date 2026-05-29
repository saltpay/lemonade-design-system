#!/usr/bin/env bash
# Diagnose the Binary Compatibility Validator (BCV) state of the current work.
#
# Two modes:
#   (default)  Regenerate the API baseline from your current source, diff it
#              against HEAD, and classify the delta. Use this mid-change to see
#              what your edits do to the public API.
#   --ci       Diff the *committed* baseline against a base branch and classify,
#              exactly the way CI's "API Stability Review" job does. No apiDump.
#
# Verdict is one of NO_CHANGES / ADDITIONS_ONLY / BREAKING, printed with the
# raw +/- lines so you can see precisely what moved.
#
# Usage:
#   bcv-check.sh                 # regenerate + diff working tree vs HEAD
#   bcv-check.sh --ci            # diff committed baseline vs origin/main
#   bcv-check.sh --ci --base release/x   # against a different base branch
set -euo pipefail

MODE="local"
BASE="main"
while [[ $# -gt 0 ]]; do
  case "$1" in
    --ci) MODE="ci"; shift ;;
    --base) BASE="${2:?--base needs a branch name}"; shift 2 ;;
    -h|--help) sed -n '2,20p' "$0"; exit 0 ;;
    *) echo "Unknown arg: $1" >&2; exit 2 ;;
  esac
done

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(git -C "$SCRIPT_DIR" rev-parse --show-toplevel)"
KMP_DIR="$REPO_ROOT/kmp"
DIFF="$(mktemp -t lemonade-api.XXXXXX.diff)"
VERDICT="$(mktemp -t lemonade-api.XXXXXX.verdict)"
GLOBS=( 'kmp/*/api/*.klib.api' 'kmp/*/api/*/*.api' )

if [[ "$MODE" == "local" ]]; then
  echo ">> Regenerating API baseline from current source (./gradlew apiDump)…"
  ( cd "$KMP_DIR" && ./gradlew apiDump --no-daemon -q )
  git -C "$REPO_ROOT" diff -U999999 HEAD -- "${GLOBS[@]}" > "$DIFF"
else
  echo ">> Fetching origin/$BASE and diffing committed baseline (CI mirror)…"
  git -C "$REPO_ROOT" fetch --no-tags origin "$BASE" >/dev/null 2>&1 || true
  git -C "$REPO_ROOT" diff -U999999 "origin/$BASE...HEAD" -- "${GLOBS[@]}" > "$DIFF"
fi

if [[ ! -s "$DIFF" ]]; then
  echo
  echo "Verdict: NO_CHANGES — public API is identical. Nothing to review."
  rm -f "$DIFF" "$VERDICT"
  exit 0
fi

( cd "$KMP_DIR" && ./gradlew classifyApiDiff -PapiDiffFile="$DIFF" -PapiVerdictFile="$VERDICT" --no-daemon -q )

echo
echo "── API delta (the lines that moved) ─────────────────────────────"
grep -E '^[+-]' "$DIFF" | grep -vE '^(\+\+\+|---)' || true
echo "── Verdict ──────────────────────────────────────────────────────"
cat "$VERDICT"
echo "─────────────────────────────────────────────────────────────────"
echo "Diff saved at: $DIFF"

case "$(head -n1 "$VERDICT")" in
  ADDITIONS_ONLY)
    echo "Additions only. CI auto-passes. Still call the additions out in the PR's API Dump section."
    ;;
  BREAKING)
    echo "BREAKING. Either rework it to be additive (see the skill) or get a maintainer to approve,"
    echo "and document every '-' line in the PR's API Dump section."
    ;;
esac