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
    local pems
    pems=$(security find-certificate -a -c "Apple Development" -p 2>/dev/null) || return 0

    # openssl x509 reads only the first PEM block, so several certificates
    # cannot be told apart after the pipe — refuse to guess unless there is
    # exactly one. Two certificates of the same team (a renewal, say) also land
    # here; blank is the safe default and the caller prints what to do.
    if [ "$(printf '%s\n' "$pems" | grep -c 'BEGIN CERTIFICATE')" -ne 1 ]; then
        return 0
    fi

    # `|| return 0`: under `set -e` a non-zero pipeline here would abort the
    # caller's `TEAM_ID=$(detect_team_id)` assignment.
    printf '%s\n' "$pems" \
        | openssl x509 -noout -subject 2>/dev/null \
        | sed -n 's/.*OU *= *\([A-Z0-9]*\).*/\1/p' \
        || return 0
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
        echo "     security find-certificate -a -c \"Apple Development\" -p | openssl x509 -noout -subject"
        echo "   and put the OU value in $SWIFTUI_DIR/$LOCAL_CONFIG"
    fi
fi

echo "⚙️  Generating Lemonade.xcodeproj..."
xcodegen generate

echo "✅ Done."
