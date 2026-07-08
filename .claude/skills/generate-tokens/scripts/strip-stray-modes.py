#!/usr/bin/env python3
"""Strip stray mode keys from a Figma theme-token export.

Figma re-exports of `tokens/theme-colors.json` sometimes carry an extra mode id
(historically `3932:0`) inside every variable's `valuesByMode` /
`resolvedValuesByMode`, even though it is NOT declared in the top-level `modes`
map (which should only be Light + Dark). The committed file must never contain
these — they pollute the diff and are not real theme modes.

This script removes any mode key that is not declared in the top-level `modes`
object, so it fixes `3932:0` and any future stray id without hard-coding it.

Usage:
    python3 strip-stray-modes.py tokens/theme-colors.json
    python3 strip-stray-modes.py --check tokens/theme-colors.json   # report only, exit 1 if strays

Rewrites the file in place (2-space indent + trailing newline, matching Figma's
native export format so the diff stays clean) unless --check.
"""
import json
import sys


def find_stray_keys(obj, valid_modes, hits):
    if isinstance(obj, dict):
        for container in ("valuesByMode", "resolvedValuesByMode"):
            sub = obj.get(container)
            if isinstance(sub, dict):
                for k in list(sub.keys()):
                    if k not in valid_modes:
                        hits.append((container, k))
        for v in obj.values():
            find_stray_keys(v, valid_modes, hits)
    elif isinstance(obj, list):
        for v in obj:
            find_stray_keys(v, valid_modes, hits)


def strip(obj, valid_modes):
    removed = 0
    if isinstance(obj, dict):
        for container in ("valuesByMode", "resolvedValuesByMode"):
            sub = obj.get(container)
            if isinstance(sub, dict):
                for k in list(sub.keys()):
                    if k not in valid_modes:
                        del sub[k]
                        removed += 1
        for v in obj.values():
            removed += strip(v, valid_modes)
    elif isinstance(obj, list):
        for v in obj:
            removed += strip(v, valid_modes)
    return removed


def main():
    args = [a for a in sys.argv[1:] if a != "--check"]
    check_only = "--check" in sys.argv
    if len(args) != 1:
        print(__doc__)
        sys.exit(2)
    path = args[0]
    with open(path) as f:
        data = json.load(f)

    valid_modes = set(data.get("modes", {}).keys())
    if not valid_modes:
        print(f"error: no top-level `modes` map found in {path}", file=sys.stderr)
        sys.exit(2)

    if check_only:
        hits = []
        find_stray_keys(data, valid_modes, hits)
        stray_ids = sorted({k for _, k in hits})
        if stray_ids:
            print(f"stray mode ids present ({len(hits)} entries): {', '.join(stray_ids)}")
            sys.exit(1)
        print("clean: no stray modes")
        return

    removed = strip(data, valid_modes)
    if removed:
        with open(path, "w") as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
            f.write("\n")
    print(f"valid modes: {', '.join(sorted(valid_modes))}")
    print(f"removed {removed} stray mode entries from {path}")


if __name__ == "__main__":
    main()
