#!/usr/bin/env python3
"""Publish a checked, atomic batch of legacy board files through a Git push."""
import argparse
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import tempfile

from board import BoardError, fetch, git

SAFE_PATH = re.compile(r"^(?:assignments|claims|gates|receipts|reports|hosts|jira|drafts|material)/[A-Za-z0-9_.\-/]+$")
OID = re.compile(r"^[0-9a-f]{40}(?:[0-9a-f]{24})?$")


def checked_entries(manifest):
    if not isinstance(manifest, list) or not manifest:
        raise BoardError("manifest must be a nonempty array")
    seen = set()
    out = []
    for item in manifest:
        if not isinstance(item, dict) or set(item) != {"path", "file", "expected_blob"}:
            raise BoardError("each entry requires only path, file and expected_blob")
        path = item["path"]
        if (not isinstance(path, str) or not SAFE_PATH.fullmatch(path)
                or any(x in ("", ".", "..") for x in path.split("/"))):
            raise BoardError(f"unsafe or unsupported board path: {path}")
        if path in seen:
            raise BoardError(f"duplicate path: {path}")
        seen.add(path)
        expected = item["expected_blob"]
        if expected is not None and (not isinstance(expected, str) or not OID.fullmatch(expected)):
            raise BoardError(f"expected_blob must be null or a full blob id for {path}")
        out.append({"path": path, "content": Path(item["file"]).read_bytes(), "expected": expected})
    return out


def tree_blob(repo, commit, path):
    result = git(repo, ["--literal-pathspecs", "ls-tree", "-z", commit, "--", path]).stdout
    if not result:
        return None
    lines = result.rstrip("\0").split("\0")
    if len(lines) != 1:
        raise BoardError(f"ambiguous path: {path}")
    meta, actual = lines[0].split("\t", 1)
    mode, kind, oid = meta.split()
    if actual != path or kind != "blob" or mode != "100644":
        raise BoardError(f"unsupported existing path or mode: {path}")
    return oid


def hash_blob(repo, content):
    p = subprocess.run(["git", "-C", str(repo), "hash-object", "-w", "--stdin"],
                       input=content, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if p.returncode:
        raise BoardError("git hash-object failed")
    return p.stdout.decode("ascii").strip()


def publish(repo, remote, branch, manifest, message):
    entries = checked_entries(manifest)
    if not message.strip() or "\0" in message:
        raise BoardError("a commit message is required")
    # Read and hash each payload once, so retries cannot silently change content.
    for entry in entries:
        entry["new"] = hash_blob(repo, entry["content"])
    for _ in range(3):
        parent = fetch(repo, remote, branch)
        for entry in entries:
            if tree_blob(repo, parent, entry["path"]) != entry["expected"]:
                raise BoardError(f"base changed at {entry['path']}; re-read and reconcile the batch")
        with tempfile.TemporaryDirectory(prefix="legacy-board-index-") as temp:
            env = os.environ.copy()
            env["GIT_INDEX_FILE"] = str(Path(temp) / "index")
            git(repo, ["read-tree", parent], env=env)
            for entry in entries:
                git(repo, ["update-index", "--add", "--cacheinfo", "100644", entry["new"], entry["path"]], env=env)
            tree = git(repo, ["write-tree"], env=env).stdout.strip()
            commit = git(repo, ["commit-tree", tree, "-p", parent], input=message + "\n").stdout.strip()
        pushed = git(repo, ["push", remote, f"{commit}:refs/heads/{branch}"], check=False)
        live = fetch(repo, remote, branch)
        ancestor = git(repo, ["merge-base", "--is-ancestor", commit, live], check=False).returncode == 0
        matches = all(tree_blob(repo, live, e["path"]) == e["new"] for e in entries)
        if ancestor and matches:
            return {"commit": commit, "live_head": live, "paths": [e["path"] for e in entries]}
        if pushed.returncode == 0:
            raise BoardError("push reported success but committed batch is not current and reachable")
        if live == parent:
            raise BoardError("push failed without branch movement; investigate before retrying")
        # The next iteration checks the same expected blob IDs against the new tip.
    raise BoardError("board kept moving; reconcile and retry")


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--repo", default=".")
    p.add_argument("--remote", required=True)
    p.add_argument("--branch", required=True)
    p.add_argument("--manifest", required=True)
    p.add_argument("--message", required=True)
    args = p.parse_args()
    try:
        manifest = json.loads(Path(args.manifest).read_text(encoding="utf-8"))
        print(json.dumps(publish(args.repo, args.remote, args.branch, manifest, args.message), indent=2))
    except (OSError, ValueError, BoardError) as e:
        print(f"legacy-publish: {e}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
