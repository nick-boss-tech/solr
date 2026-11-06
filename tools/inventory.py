#!/usr/bin/env python3
"""Inventory a pinned legacy Git board without inferring ownership or completion."""
import argparse
from collections import Counter
import json
from pathlib import Path
import re
import subprocess
import sys

REFS = re.compile(r"(?<![A-Za-z0-9._/-])(?:assignments|claims|gates|receipts|reports|hosts|material|jira|drafts)/[A-Za-z0-9_.\-/]+")
TRACKED = {"assignments", "claims", "gates", "receipts", "reports", "hosts", "material", "jira", "drafts"}


def git(repo, *args, input=None):
    p = subprocess.run(["git", "-C", str(repo), *args], input=input,
                       stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if p.returncode:
        raise ValueError(f"git {args[0]} failed: {p.stderr.decode('utf-8', 'replace').strip()}")
    return p.stdout


def snapshot(repo, revision):
    commit = git(repo, "rev-parse", "--verify", f"{revision}^{{commit}}").decode().strip()
    raw = git(repo, "ls-tree", "-r", "-z", "-l", "--full-tree", commit)
    entries = {}
    for item in raw.split(b"\0"):
        if not item:
            continue
        meta, path_bytes = item.split(b"\t", 1)
        mode, kind, oid, size = meta.decode("ascii").split()
        path = path_bytes.decode("utf-8")
        entries[path] = {"mode": mode, "kind": kind, "git_oid": oid,
                         "bytes": int(size) if size != "-" else None}
    # Read only bounded Markdown records. Every tree entry remains in the manifest,
    # including binary evidence and files not parsed for linkage.
    selected = [(p, e) for p, e in entries.items() if e["kind"] == "blob"
                and p.endswith(".md") and (e["bytes"] or 0) <= 1_000_000
                and p.split("/", 1)[0] in TRACKED]
    query = "".join(e["git_oid"] + "\n" for _, e in selected).encode("ascii")
    out = git(repo, "cat-file", "--batch", input=query)
    offset = 0
    for path, entry in selected:
        end = out.index(b"\n", offset)
        header = out[offset:end].decode("ascii").split()
        if len(header) != 3 or header[0] != entry["git_oid"] or header[1] != "blob":
            raise ValueError(f"unexpected blob response for {path}")
        length = int(header[2])
        start = end + 1
        raw_text = out[start:start + length]
        if len(raw_text) != length or out[start + length:start + length + 1] != b"\n":
            raise ValueError(f"truncated blob response for {path}")
        offset = start + length + 1
        text = raw_text.decode("utf-8", "replace")
        title = re.search(r"(?m)^#\s+(.+)$", text)
        status = re.search(r"(?mi)^\s*(?:[-*]\s*)?(?:\*\*)?Status(?:\*\*)?:\s*(.+)$", text)
        entry["title"] = title.group(1).strip() if title else None
        entry["reported_status"] = status.group(1).strip()[:500] if status else None
        entry["mentioned_paths"] = sorted(set(m.group(0).rstrip(".,;:") for m in REFS.finditer(text)))
    if offset != len(out):
        raise ValueError("extra data in cat-file response")
    groups = Counter(p.split("/", 1)[0] for p in entries)
    return {"schema": 1, "snapshot": commit,
            "scope": "all tree entries; bounded Markdown mentions and status lines are observations, not derived ownership or proof",
            "summary": {"files": len(entries), "bytes": sum(e["bytes"] or 0 for e in entries.values()),
                        "files_by_root": dict(sorted(groups.items())), "markdown_parsed": len(selected)},
            "entries": dict(sorted(entries.items()))}


def compare(old, new):
    a, b = old["entries"], new["entries"]
    added = sorted(b.keys() - a.keys())
    removed = sorted(a.keys() - b.keys())
    modified = sorted(p for p in a.keys() & b.keys()
                      if (a[p]["git_oid"], a[p]["mode"]) != (b[p]["git_oid"], b[p]["mode"]))
    return {"from": old["snapshot"], "to": new["snapshot"], "added": added,
            "removed": removed, "modified": modified,
            "warning": "reconcile each changed record and host state; a tree diff does not prove workers stopped"}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    sub = parser.add_subparsers(dest="command", required=True)
    build = sub.add_parser("snapshot")
    build.add_argument("--repo", default=".")
    build.add_argument("--revision", required=True)
    build.add_argument("--out", required=True)
    diff = sub.add_parser("compare")
    diff.add_argument("--old", required=True)
    diff.add_argument("--new", required=True)
    diff.add_argument("--out", required=True)
    args = parser.parse_args()
    try:
        if args.command == "snapshot":
            result = snapshot(args.repo, args.revision)
        else:
            result = compare(json.loads(Path(args.old).read_text(encoding="utf-8")),
                             json.loads(Path(args.new).read_text(encoding="utf-8")))
        Path(args.out).write_text(json.dumps(result, indent=2, ensure_ascii=False, sort_keys=True) + "\n", encoding="utf-8")
        print(json.dumps({"out": args.out, "snapshot": result.get("snapshot"),
                          "summary": result.get("summary"),
                          "changes": {k: len(result[k]) for k in ("added", "removed", "modified") if k in result}}))
    except (OSError, UnicodeError, ValueError, KeyError, IndexError) as e:
        print(f"inventory: {e}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
