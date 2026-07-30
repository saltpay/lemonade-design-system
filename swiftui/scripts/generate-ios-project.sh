#!/bin/bash
#
# Generates Lemonade.xcodeproj from project.yml.
#
# Run this after changing project.yml, and after adding or deleting a file under
# SampleApp/ or Sources/ — the generated project is gitignored and a stale one
# fails the build with "Build input file cannot be found".
#
# Also ensures Local.xcconfig exists. project.yml references it as the base
# configuration, and XcodeGen refuses to generate if it is missing.

set -euo pipefail

SWIFTUI_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$SWIFTUI_DIR"

LOCAL_CONFIG="Local.xcconfig"
TEMPLATE="Local.xcconfig.template"

# Reads the Team ID from the keychain, but only when the answer is unambiguous.
# A signing certificate's subject carries it in the OU field:
#   subject= /UID=.../CN=Apple Development: Name (XXXX)/OU=<TEAM ID>/O=Org/C=US
detect_team_id() {
    local teams
    teams=$(security find-certificate -a -c "Apple Development" -p 2>/dev/null \
        | openssl x509 -noout -subject 2>/dev/null \
        | sed -n 's/.*OU *= *\([A-Z0-9]*\).*/\1/p' \
        | sort -u) || return 0

    # Ambiguous (several teams) or absent: leave it blank rather than guess wrong.
    if [ "$(printf '%s' "$teams" | grep -c . || true)" -eq 1 ]; then
        printf '%s' "$teams"
    fi

    # Must succeed even when nothing was found: under `set -e` a non-zero return
    # here would abort the caller's `TEAM_ID=$(detect_team_id)` assignment.
    return 0
}

if [ ! -f "$LOCAL_CONFIG" ]; then
    cp "$TEMPLATE" "$LOCAL_CONFIG"

    TEAM_ID=$(detect_team_id)
    if [ -n "$TEAM_ID" ]; then
        # BSD sed (macOS) requires an argument to -i.
        sed -i '' "s/^DEVELOPMENT_TEAM =.*/DEVELOPMENT_TEAM = $TEAM_ID/" "$LOCAL_CONFIG"
        echo "📝 Created $LOCAL_CONFIG with DEVELOPMENT_TEAM = $TEAM_ID"
    else
        echo "📝 Created $LOCAL_CONFIG"
        echo "   No unambiguous signing certificate found, so DEVELOPMENT_TEAM is empty."
        echo "   Simulator builds work as-is. For device builds, set your Team ID:"
        echo "     security find-certificate -c \"Apple Development\" -p | openssl x509 -noout -subject"
        echo "   and put the OU value in $SWIFTUI_DIR/$LOCAL_CONFIG"
    fi
fi

echo "⚙️  Generating Lemonade.xcodeproj..."
xcodegen generate

echo "✅ Done."
