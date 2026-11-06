#!/usr/bin/env python3
"""Portable Git board prototype. Never runs code or a build from board records."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import tempfile
import uuid

KINDS = {"assignment", "claim", "gate", "receipt", "report", "evidence"}
ID = re.compile(r"^[a-z0-9][a-z0-9._-]{0,99}$")
SHA = re.compile(r"^[0-9a-f]{40,64}$")


class BoardError(Exception):
    pass


def git(repo, args, *, env=None, input=None, check=True):
    p = subprocess.run(["git", "-C", str(repo), *args], input=input, text=True,
                       stdout=subprocess.PIPE, stderr=subprocess.PIPE, env=env)
    if check and p.returncode:
        raise BoardError(f"git {args[0]} failed: {p.stderr.strip()}")
    return p


def ident(value, label):
    if not isinstance(value, str) or not ID.fullmatch(value):
        raise BoardError(f"invalid {label}: use lowercase letters, digits, dot, underscore or hyphen")
    return value


def validate(record):
    if not isinstance(record, dict):
        raise BoardError("record must be an object")
    kind = record.get("type")
    if kind not in KINDS:
        raise BoardError("unknown record type")
    ident(record.get("id"), "id")
    ident(record.get("job"), "job")
    if kind == "assignment" and record["id"] != record["job"]:
        raise BoardError("assignment id must equal job")
    if kind == "claim" and record["id"] != record["job"]:
        raise BoardError("claim id must equal job: all claimants must contend for one path")
    if kind in ("gate", "receipt") and not SHA.fullmatch(str(record.get("head", ""))):
        raise BoardError("gate and receipt require a full lowercase source head")
    if kind == "receipt":
        ident(record.get("gate"), "gate")
        if record.get("verdict") not in ("passed", "failed", "incomplete", "non-reproduced"):
            raise BoardError("receipt verdict must be passed, failed, incomplete or non-reproduced")
    if kind == "evidence":
        if not re.fullmatch(r"[0-9a-f]{64}", str(record.get("sha256", ""))):
            raise BoardError("evidence requires sha256")
        if not str(record.get("location", "")).startswith(("https://", "s3://", "gs://")):
            raise BoardError("evidence requires an https://, s3:// or gs:// location")
        if not SHA.fullmatch(str(record.get("head", ""))):
            raise BoardError("evidence requires a full source head")
    return kind


def record_path(record):
    return f"records/{record['type']}/{record['id']}.json"


def fetch(repo, remote, branch):
    git(repo, ["check-ref-format", "--branch", branch])
    ref = f"refs/board-tmp/{uuid.uuid4().hex}"
    try:
        git(repo, ["fetch", "--no-tags", "--no-write-fetch-head", remote, f"refs/heads/{branch}:{ref}"])
        return git(repo, ["rev-parse", "--verify", ref]).stdout.strip()
    finally:
        git(repo, ["update-ref", "-d", ref], check=False)


def read_record(repo, commit, path):
    p = git(repo, ["show", f"{commit}:{path}"], check=False)
    if p.returncode:
        return None
    try:
        return json.loads(p.stdout)
    except json.JSONDecodeError as e:
        raise BoardError(f"invalid JSON in {path}: {e}") from e


def publish(repo, remote, branch, record):
    kind = validate(record)
    path = record_path(record)
    payload = json.dumps(record, sort_keys=True, ensure_ascii=False, indent=2) + "\n"
    for attempt in range(3):
        parent = fetch(repo, remote, branch)
        existing = read_record(repo, parent, path)
        if existing is not None:
            raise BoardError(f"record already exists: {path}; reconcile ownership or use a new record id")
        if kind != "assignment" and read_record(repo, parent, f"records/assignment/{record['job']}.json") is None:
            raise BoardError("assignment is absent from the fetched snapshot")
        if kind == "receipt":
            gate = read_record(repo, parent, f"records/gate/{record['gate']}.json")
            if gate is None or gate.get("job") != record["job"]:
                raise BoardError("referenced gate is absent or belongs to another job")
        with tempfile.TemporaryDirectory(prefix="board-index-") as temp:
            env = os.environ.copy()
            env["GIT_INDEX_FILE"] = str(Path(temp) / "index")
            git(repo, ["read-tree", parent], env=env)
            blob = git(repo, ["hash-object", "-w", "--stdin"], input=payload).stdout.strip()
            git(repo, ["update-index", "--add", "--cacheinfo", "100644", blob, path], env=env)
            tree = git(repo, ["write-tree"], env=env).stdout.strip()
            # Values in trailers are validated above; optional fields stay in the JSON body.
            message = f"Add board {kind} {record['id']}\n\nRecord-Type: {kind}\nJob: {record['job']}\n"
            if kind in ("gate", "receipt", "evidence"):
                message += f"Head: {record['head']}\n"
            if kind == "receipt":
                message += f"Verdict: {record['verdict']}\n"
            commit = git(repo, ["commit-tree", tree, "-p", parent], input=message).stdout.strip()
        pushed = git(repo, ["push", remote, f"{commit}:refs/heads/{branch}"], check=False)
        # Always re-fetch: a lost response is not proof of a failed or successful push.
        live = fetch(repo, remote, branch)
        ancestor = git(repo, ["merge-base", "--is-ancestor", commit, live], check=False).returncode == 0
        stored = read_record(repo, live, path)
        if ancestor and stored == record:
            return {"commit": commit, "live_head": live, "path": path}
        if stored is not None:
            raise BoardError(f"competing record appeared at {path}; do not retry as a new claim")
        if pushed.returncode == 0:
            raise BoardError("push reported success but commit or record is not reachable; investigate")
        if live == parent:
            raise BoardError(f"push failed without branch movement: {pushed.stderr.strip()}")
    raise BoardError("board kept moving; reconcile and retry")


def index(repo, snapshot):
    head = git(repo, ["rev-parse", "--verify", f"{snapshot}^{{commit}}" ]).stdout.strip()
    listing = git(repo, ["ls-tree", "-r", "--name-only", head, "--", "records/"]).stdout.splitlines()
    records = []
    warnings = []
    for path in listing:
        if not re.fullmatch(r"records/(assignment|claim|gate|receipt|report|evidence)/[a-z0-9][a-z0-9._-]{0,99}\.json", path):
            warnings.append(f"unrecognized path: {path}")
            continue
        try:
            rec = read_record(repo, head, path)
            validate(rec)
            if record_path(rec) != path:
                raise BoardError("record path does not match type/id")
            records.append((path, rec))
        except BoardError as e:
            warnings.append(f"{path}: {e}")
    jobs = {}
    for path, rec in records:
        job = jobs.setdefault(rec["job"], {"records": [], "status": "unconfirmed"})
        job["records"].append({"path": path, "type": rec["type"], "id": rec["id"]})
    for job_id, job in jobs.items():
        job_records = [r for _, r in records if r["job"] == job_id]
        assignments = [r for r in job_records if r["type"] == "assignment" and r["id"] == job_id]
        claims = [r for r in job_records if r["type"] == "claim"]
        receipts = [r for r in job_records if r["type"] == "receipt"]
        gates = {r["id"]: r for r in job_records if r["type"] == "gate"}
        if not assignments:
            job["status"] = "unconfirmed: missing canonical assignment"
        elif receipts:
            job["status"] = "receipt recorded: inspect linked gate and proof"
        elif claims:
            job["status"] = "claim recorded: verify owner activity"
        else:
            job["status"] = "open: no canonical claim recorded"
        job["proof"] = [{"receipt": r["id"], "gate": r["gate"], "head": r["head"],
                         "verdict": r["verdict"],
                         "gate_matches": r["gate"] in gates and gates[r["gate"]]["head"] == r["head"]}
                        for r in receipts]
    return {"snapshot": head, "scope": "records/ structured prototype only; legacy records not indexed",
            "jobs": jobs, "warnings": warnings}


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--repo", default=".", help="existing Git repository")
    sub = p.add_subparsers(dest="command", required=True)
    put = sub.add_parser("publish", help="publish one create-only JSON record")
    put.add_argument("--remote", required=True)
    put.add_argument("--branch", required=True)
    put.add_argument("--file", required=True)
    idx = sub.add_parser("index", help="print an index for one local commit")
    idx.add_argument("--snapshot", required=True)
    ev = sub.add_parser("evidence", help="make an evidence pointer; does not upload the file")
    ev.add_argument("--file", required=True)
    ev.add_argument("--id", required=True)
    ev.add_argument("--job", required=True)
    ev.add_argument("--head", required=True)
    ev.add_argument("--location", required=True)
    args = p.parse_args()
    try:
        if args.command == "publish":
            result = publish(args.repo, args.remote, args.branch, json.loads(Path(args.file).read_text(encoding="utf-8")))
        elif args.command == "index":
            result = index(args.repo, args.snapshot)
        else:
            h = hashlib.sha256()
            with open(args.file, "rb") as f:
                for chunk in iter(lambda: f.read(1024 * 1024), b""):
                    h.update(chunk)
            result = {"type": "evidence", "id": args.id, "job": args.job, "head": args.head,
                      "location": args.location, "sha256": h.hexdigest()}
            validate(result)
        print(json.dumps(result, indent=2, sort_keys=True))
    except (BoardError, OSError, ValueError) as e:
        print(f"board: {e}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
