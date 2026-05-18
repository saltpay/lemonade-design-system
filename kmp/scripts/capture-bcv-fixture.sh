#!/usr/bin/env bash
# Captures a single BCV diff fixture from a real apiDump run.
#
# Usage:
#   ./capture-bcv-fixture.sh <fixture-name> <stage1-source> <stage2-source>
#
# Writes the unified diff (with -U999999 full context, same as CI will use)
# to build-logic/src/test/resources/fixtures/<dir>/<fixture-name>.diff
# The <dir> is decided by the caller via the FIXTURE_BUCKET env var:
#   additions-only | breaking | no-changes

set -euo pipefail

FIXTURE_NAME="$1"
STAGE1_FILE="$2"
STAGE2_FILE="$3"
BUCKET="${FIXTURE_BUCKET:?must set FIXTURE_BUCKET}"

KMP_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$KMP_ROOT"

OUT="build-logic/src/test/resources/fixtures/$BUCKET/$FIXTURE_NAME.diff"
TEST_KT="core/src/commonMain/kotlin/com/teya/lemonade/core/_bcvFixture.kt"

cleanup() {
  rm -f "$TEST_KT"
  git checkout -- core/api/ >/dev/null 2>&1 || true
}
trap cleanup EXIT

git checkout -- core/api/ >/dev/null 2>&1 || true

cp "$STAGE1_FILE" "$TEST_KT"
./gradlew :core:apiDump --no-daemon -q >/dev/null

mkdir -p /tmp/bcv-fixture-stage1
cp core/api/android/core.api /tmp/bcv-fixture-stage1/android.api
cp core/api/desktop/core.api /tmp/bcv-fixture-stage1/desktop.api
cp core/api/core.klib.api    /tmp/bcv-fixture-stage1/klib.api

cp "$STAGE2_FILE" "$TEST_KT"
./gradlew :core:apiDump --no-daemon -q >/dev/null

{
  diff -u -U999999 \
    --label "a/core/api/android/core.api" \
    --label "b/core/api/android/core.api" \
    /tmp/bcv-fixture-stage1/android.api core/api/android/core.api 2>/dev/null || true
  diff -u -U999999 \
    --label "a/core/api/desktop/core.api" \
    --label "b/core/api/desktop/core.api" \
    /tmp/bcv-fixture-stage1/desktop.api core/api/desktop/core.api 2>/dev/null || true
  diff -u -U999999 \
    --label "a/core/api/core.klib.api" \
    --label "b/core/api/core.klib.api" \
    /tmp/bcv-fixture-stage1/klib.api core/api/core.klib.api 2>/dev/null || true
} > "$OUT"

rm -rf /tmp/bcv-fixture-stage1

echo "Captured: $OUT ($(wc -l <"$OUT" | tr -d ' ') lines)"
