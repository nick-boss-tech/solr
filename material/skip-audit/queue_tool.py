import argparse, json, re, subprocess, sys

QUEUE = "material/skip-audit/queue-current.json"
VERDICTS = {
    "close-fixed": "close",
    "close-obsolete": "close",
    "close-cannot-reproduce": "close",
    "close-answered": "close",
    "pr-needed": "pr",
    "keep-open": "open",
}
BRANCH_RE = re.compile(r"solr-(\d+)-draft-(close|pr|open)$")


def load(repo):
    with open(f"{repo}/{QUEUE}") as f:
        return json.load(f)


def save(repo, data):
    with open(f"{repo}/{QUEUE}", "w") as f:
        f.write(json.dumps(data, indent=2, ensure_ascii=False) + "\n")


def draft_branches(repo):
    out = subprocess.run(
        ["git", "-C", repo, "ls-remote", "--heads", "origin", "solr-*-draft-*"],
        capture_output=True, text=True, check=True,
    ).stdout
    found = {}
    for line in out.splitlines():
        ref = line.split("\t", 1)[1].removeprefix("refs/heads/")
        m = BRANCH_RE.match(ref)
        if m:
            found[f"SOLR-{m.group(1)}"] = ref
    return found


def cmd_coverage(a):
    q = load(a.repo)
    by_status = {}
    by_queue_verdict = {}
    by_audit_verdict = {}
    for r in q:
        by_status[r["status"]] = by_status.get(r["status"], 0) + 1
        if r.get("verdict"):
            by_queue_verdict[r["verdict"]] = by_queue_verdict.get(r["verdict"], 0) + 1
        audit = r.get("audit") or {}
        if audit.get("verdict"):
            verdict = audit["verdict"]
            by_audit_verdict[verdict] = by_audit_verdict.get(verdict, 0) + 1
    branches = draft_branches(a.repo)
    skips = [r["key"] for r in q if r["status"] == "skip"]
    with_queue_verdict = [r["key"] for r in q if r.get("verdict")]
    with_audit_verdict = [r["key"] for r in q if (r.get("audit") or {}).get("verdict")]
    print(f"records: {len(q)}")
    print("by status:", dict(sorted(by_status.items())))
    print(
        "skip tickets with an audit verdict:",
        sum(1 for k in skips if k in with_audit_verdict),
        "of",
        len(skips),
    )
    print(
        "skip tickets with a closeout/PR verdict:",
        sum(1 for k in skips if k in with_queue_verdict),
        "of",
        len(skips),
    )
    print("skip-audit verdicts (audit.verdict):", dict(sorted(by_audit_verdict.items())))
    print("closeout/PR verdicts (top-level verdict):", dict(sorted(by_queue_verdict.items())))
    print("draft branches on origin:", len(branches))
    stale = [k for k in with_queue_verdict if k not in branches]
    if stale:
        print("closeout/PR verdict without draft branch:", stale)


def cmd_set(a):
    if a.verdict not in VERDICTS:
        sys.exit(f"unknown verdict {a.verdict}; allowed: {', '.join(VERDICTS)}")
    expected = f"solr-{a.key.split('-')[1]}-draft-{VERDICTS[a.verdict]}"
    if a.branch != expected:
        sys.exit(f"branch must be {expected} for verdict {a.verdict}")
    q = load(a.repo)
    hit = [r for r in q if r["key"] == a.key]
    if not hit:
        sys.exit(f"{a.key} is not in the queue")
    r = hit[0]
    r["verdict"] = a.verdict
    r["draft_branch"] = a.branch
    r["evidence_head"] = a.head
    save(a.repo, q)
    print(f"{a.key}: {a.verdict} on {a.branch} at {a.head}")


def cmd_list(a):
    for r in load(a.repo):
        if r.get("verdict") == a.verdict or (a.verdict is None and r.get("verdict")):
            print(r["key"], r.get("verdict"), r.get("draft_branch"), r.get("evidence_head"))


def main():
    p = argparse.ArgumentParser(description="Skip queue verdicts and coverage.")
    p.add_argument("--repo", default=".")
    sub = p.add_subparsers(dest="cmd", required=True)
    sub.add_parser("coverage").set_defaults(fn=cmd_coverage)
    s = sub.add_parser("set")
    s.add_argument("key")
    s.add_argument("verdict")
    s.add_argument("branch")
    s.add_argument("head")
    s.set_defaults(fn=cmd_set)
    l = sub.add_parser("list")
    l.add_argument("--verdict", default=None)
    l.set_defaults(fn=cmd_list)
    a = p.parse_args()
    a.fn(a)


if __name__ == "__main__":
    main()
