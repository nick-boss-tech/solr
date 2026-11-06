#!/usr/bin/env python3
"""Catalog legacy assignment, claim and gate files without deciding their status."""
import argparse
from collections import Counter, defaultdict
import json
from pathlib import Path
import re
from urllib.parse import quote


def catalog(manifest):
    entries = manifest["entries"]
    selected = {p: e for p, e in entries.items()
                if p.split("/", 1)[0] in ("assignments", "claims", "gates")}
    incoming = defaultdict(list)
    for source, entry in entries.items():
        for target in entry.get("mentioned_paths", []):
            if target in selected:
                incoming[target].append(source)
    rows = []
    for path, entry in sorted(selected.items()):
        rows.append({"path": path, "git_oid": entry["git_oid"], "bytes": entry["bytes"],
                     "title": entry.get("title"), "reported_status": entry.get("reported_status"),
                     "mentions_existing": [p for p in entry.get("mentioned_paths", []) if p in entries],
                     "mentions_not_found": [p for p in entry.get("mentioned_paths", []) if p not in entries],
                     "mentioned_by": sorted(set(incoming[path]))})
    return {"snapshot": manifest["snapshot"],
            "caveat": "Reported statuses and literal path mentions are leads only; they do not establish ownership, activity, closure, or proof.",
            "counts": dict(sorted(Counter(r["path"].split("/", 1)[0] for r in rows).items())),
            "records": rows}


def md(value):
    return str(value or "—").replace("\\", "\\\\").replace("|", "\\|").replace("\n", " ").replace("`", "'")


def markdown(data, repo):
    base = f"https://github.com/{repo}/blob/{data['snapshot']}/"
    result = ["# Legacy work catalog", "", f"Snapshot: `{data['snapshot']}`.", "",
              "This is a reference catalog, not a new queue. Statuses are copied from source text; missing or stale statuses, "
              "literal path mentions and absent links do not determine who owns work or whether it is finished. "
              "Before carrying an item over, read its current source records and verify any active owner, gate and exact head.", "",
              "The complete [machine-readable catalog](legacy-work-catalog.json) includes Git object IDs, extracted "
              "path mentions, incoming mentions and paths not found in this snapshot. Mentions are not guaranteed to be links.", ""]
    for kind in ("assignments", "claims", "gates"):
        result += [f"## {kind.capitalize()} ({data['counts'].get(kind, 0)} files)", "",
                   "| Source record | Reported status |", "| --- | --- |"]
        for row in data["records"]:
            if row["path"].startswith(kind + "/"):
                name = md(row["path"][len(kind) + 1:])
                result.append(f"| [{name}]({base}{quote(row['path'], safe='/')}) | {md(row['reported_status'])} |")
        result.append("")
    return "\n".join(result)


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--inventory", required=True)
    p.add_argument("--json-out", required=True)
    p.add_argument("--markdown-out", required=True)
    p.add_argument("--repo", required=True, help="GitHub owner/repository for source links")
    args = p.parse_args()
    if not re.fullmatch(r"[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+", args.repo):
        p.error("repo must be owner/repository")
    data = catalog(json.loads(Path(args.inventory).read_text(encoding="utf-8")))
    Path(args.json_out).write_text(json.dumps(data, indent=2, ensure_ascii=False, sort_keys=True) + "\n", encoding="utf-8")
    Path(args.markdown_out).write_text(markdown(data, args.repo), encoding="utf-8")
    print(json.dumps({"snapshot": data["snapshot"], "counts": data["counts"]}))


if __name__ == "__main__":
    main()
