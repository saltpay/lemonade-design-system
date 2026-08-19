#!/usr/bin/env python3
"""Copy a Figma native variable export into tokens/, routing by content.

Figma's filenames are not a contract: the `Size` collection exports as
`sizing.tokens.json`, `.Shadow` as `shadow.tokens.json`, and theme modes as
`light`/`dark`. So each incoming file is matched to its destination by the
overlap of its Figma variable ids with the file already committed there.

Usage (from the repo root):
    python3 .claude/skills/generate-tokens/scripts/ingest-tokens.py <export-dir> [--allow-shrink]

(--allow-shrink may appear before or after <export-dir>.)

Writes nothing unless every file routes unambiguously. A collection whose
token count decreases from what's already committed refuses by default —
in a published design system, a token disappearing means public API surface
disappearing, and that should require a human to say so out loud rather than
be absorbed silently by a percentage threshold. Pass --allow-shrink to permit
a decrease (e.g. a token was intentionally removed upstream); each shrink is
then announced on stdout.
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
    args = sys.argv[1:]
    allow_shrink = "--allow-shrink" in args
    positional = [a for a in args if a != "--allow-shrink"]
    if len(positional) != 1:
        sys.exit(__doc__)
    export_dir = pathlib.Path(positional[0]).expanduser()
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
        if ref_count and count < ref_count:
            if not allow_shrink:
                problems.append(
                    f"{source.name}: {best_name} shrank from {ref_count} to {count} tokens "
                    f"(pass --allow-shrink to allow)"
                )
                continue
            print(f"note: {best_name} shrank from {ref_count} to {count} tokens (--allow-shrink)")

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
