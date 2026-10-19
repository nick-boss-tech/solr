import argparse, json, re, subprocess, sys, urllib.request, urllib.error

JIRA = "https://issues.apache.org/jira/rest/api/2/issue/{}?fields=summary,status,resolution,fixVersions,versions,created,updated,issuelinks,comment,description"
CLASS_RE = re.compile(r"\b([A-Z][A-Za-z0-9]*(?:Test|Tests|IT))\b")
SYM_RE = re.compile(r"`([A-Za-z_][A-Za-z0-9_.]*)`")


def git(repo, *args):
    r = subprocess.run(["git", "-C", repo, *args], capture_output=True, text=True)
    return r.stdout if r.returncode == 0 else ""


def fetch_jira(key):
    try:
        with urllib.request.urlopen(JIRA.format(key), timeout=30) as f:
            return json.load(f)
    except urllib.error.URLError as e:
        return {"error": str(e)}


def clean(s, n):
    s = re.sub(r"\s+", " ", s or "").strip()
    return s if len(s) <= n else s[: n - 3] + "..."


def main():
    p = argparse.ArgumentParser(description="Evidence for one skip-queue ticket, as markdown.")
    p.add_argument("key")
    p.add_argument("--repo", default="/workspace/solr")
    p.add_argument("--ref", default="origin/main")
    p.add_argument("--commit", help="optional fix commit to check for ancestry on --ref")
    a = p.parse_args()
    out = []
    j = fetch_jira(a.key)
    out.append(f"## JIRA {a.key}")
    if "error" in j:
        out.append(f"JIRA fetch failed: {j['error']}")
        j = {"fields": {}}
    f = j.get("fields", {})
    out.append(f"- Summary: {clean(f.get('summary'), 200)}")
    out.append(f"- Status: {(f.get('status') or {}).get('name')}; resolution: {(f.get('resolution') or {}).get('name')}")
    out.append(f"- Created: {(f.get('created') or '')[:10]}; updated: {(f.get('updated') or '')[:10]}")
    out.append(f"- Fix versions: {[v['name'] for v in f.get('fixVersions', [])]}")
    out.append(f"- Links: " + "; ".join(
        f"{l['type']['name']} {(l.get('outwardIssue') or l.get('inwardIssue'))['key']} ({(l.get('outwardIssue') or l.get('inwardIssue'))['fields']['status']['name']})"
        for l in f.get("issuelinks", [])) or "- Links: none")
    out.append("")
    out.append("### Description (first 4000 chars)")
    out.append(clean(f.get("description"), 4000) or "(empty)")
    out.append("")
    comments = (f.get("comment") or {}).get("comments", [])
    out.append(f"### Comments ({len(comments)})")
    for c in comments:
        out.append(f"- {c['created'][:10]} {(c.get('author') or {}).get('name')}: {clean(c.get('body'), 2000)}")
    out.append("")
    out.append(f"### Commits on {a.ref} naming {a.key}")
    log = git(a.repo, "log", a.ref, "--grep", a.key, "-i", "--format=%h %ad %s", "--date=short", "-30")
    out.append("```text")
    out.append(log.rstrip() or "(none)")
    out.append("```")
    if a.commit:
        ok = subprocess.run(["git", "-C", a.repo, "merge-base", "--is-ancestor", a.commit, a.ref]).returncode == 0
        out.append(f"- Commit {a.commit} is an ancestor of {a.ref}: {ok}")
    out.append("")
    text = (f.get("summary") or "") + "\n" + (f.get("description") or "")
    classes = sorted(set(CLASS_RE.findall(text)))
    out.append("### Test classes named in the ticket")
    if not classes:
        out.append("(none)")
    tree = git(a.repo, "ls-tree", "-r", "--name-only", a.ref)
    for c in classes:
        paths = [x for x in tree.splitlines() if x.endswith("/" + c + ".java")]
        if not paths:
            out.append(f"- {c}: not on {a.ref}")
        for path in paths:
            last = git(a.repo, "log", "-2", a.ref, "--format=%h %ad %s", "--date=short", "--", path).rstrip()
            out.append(f"- {c}: {path}")
            out.append("  " + last.replace("\n", "\n  "))
    out.append("")
    syms = []
    for s in SYM_RE.findall(text):
        if s not in syms and len(s) > 3:
            syms.append(s)
    out.append("### Identifiers in backticks, hits on the ref (solr/ tree)")
    if not syms:
        out.append("(none)")
    for s in syms[:12]:
        hits = git(a.repo, "grep", "-c", "-F", "-e", s, a.ref, "--", "solr").strip().splitlines()
        n = sum(int(h.rsplit(":", 1)[1]) for h in hits if h.rsplit(":", 1)[-1].isdigit())
        files = len(hits)
        out.append(f"- `{s}`: {n} matching lines in {files} files")
    print("\n".join(out))


if __name__ == "__main__":
    sys.exit(main())
