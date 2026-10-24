#!/usr/bin/env python3
"""Build material/work-graph.json from the coordination branch's records.

usage: build-work-graph.py [checkout] [remote]

Reads receipts/, claims/, assignments/ and gates/ in the checkout, extracts the
structured fields the records already carry (Branch and Head lines in receipts,
Status lines, claim hosts and heartbeats, any explicit Links fields), checks the
recorded heads against the fork's live tips with one ls-remote, and writes the
graph as JSON. Nothing in the graph is inferred beyond those extractions: a
field the record does not carry is reported in the record's "missing" list, so
the graph doubles as the backfill list for the linkage pass.
"""
import datetime, json, os, re, subprocess, sys

root = sys.argv[1] if len(sys.argv) > 1 else "."
remote = sys.argv[2] if len(sys.argv) > 2 else "origin"
now = datetime.datetime.now(datetime.timezone.utc)

HEX40 = re.compile(r"\b[0-9a-f]{40}\b")
LINK_KEYS = ("ticket or parent", "category", "siblings", "supersedes")


def read(path):
    try:
        with open(path, encoding="utf-8", errors="replace") as f:
            return f.read()
    except OSError:
        return ""


def links_of(text):
    out = {}
    for line in text.splitlines():
        m = re.match(r"\s*[-*]?\s*(Ticket or parent|Category|Siblings|Supersedes)\s*:\s*(.+)$", line, re.I)
        if m:
            out[m.group(1).lower()] = m.group(2).strip().strip("`")
    return out


def branch_heads(text):
    pairs, current = [], None
    for line in text.splitlines():
        if "Branch:" in line:
            m = re.search(r"Branch:\s*`?([A-Za-z0-9._/\-]+)`?", line)
            current = m.group(1) if m else None
            h = HEX40.search(line)
            if current and h:
                pairs.append((current, h.group(0)))
                current = None
        elif current:
            h = HEX40.search(line)
            if h:
                pairs.append((current, h.group(0)))
                current = None
    seen, out = set(), []
    for b, h in pairs:
        if (b, h) not in seen:
            seen.add((b, h))
            out.append({"branch": b, "recorded_head": h})
    return out


def ls_remote_tips():
    try:
        out = subprocess.run(["git", "ls-remote", remote], capture_output=True,
                             text=True, timeout=120, cwd=root)
        tips = {}
        for line in out.stdout.splitlines():
            parts = line.split("refs/heads/")
            if len(parts) == 2:
                tips[parts[1].strip()] = parts[0].strip()
        return tips
    except Exception:
        return {}


tips = ls_remote_tips()
nodes, edges = [], []

# Tickets, from receipts.
receipt_dir = os.path.join(root, "receipts")
stale = []
for name in sorted(os.listdir(receipt_dir)) if os.path.isdir(receipt_dir) else []:
    if not name.endswith(".md") or name == "README.md":
        continue
    path = f"receipts/{name}"
    text = read(os.path.join(root, path))
    ticket = name[:-3]
    branches = branch_heads(text)
    for b in branches:
        b["tip_now"] = tips.get(b["branch"])
        b["at_tip"] = (b["tip_now"] == b["recorded_head"]) if b["tip_now"] else None
        if b["at_tip"] is False:
            stale.append({"ticket": ticket, "branch": b["branch"],
                          "recorded_head": b["recorded_head"], "tip_now": b["tip_now"]})
        edges.append({"from": ticket, "to": b["branch"], "type": "branch"})
    prs = sorted({int(n) for n in re.findall(r"#(\d{4,5})\b", text)})
    for pr in prs:
        edges.append({"from": ticket, "to": f"PR-{pr}", "type": "pr"})
    links = links_of(text)
    missing = [k for k in LINK_KEYS if k not in links]
    if not branches:
        missing.append("branch/head")
    nodes.append({"id": ticket, "kind": "ticket", "receipt": path,
                  "no_gate": "NO GATE" in text, "branches": branches,
                  "prs": prs, "links": links, "missing": missing})

# Assignments and gate jobs, from Status lines.
for sub in ("assignments", "gates"):
    d = os.path.join(root, sub)
    for name in sorted(os.listdir(d)) if os.path.isdir(d) else []:
        if not name.endswith(".md"):
            continue
        path = f"{sub}/{name}"
        text = read(os.path.join(root, path))
        m = re.search(r"^Status:\s*(.+)$", text, re.M)
        links = links_of(text)
        nodes.append({"id": path, "kind": "assignment" if sub == "assignments" else "gate",
                      "status": m.group(1).strip() if m else "UNCONFIRMED",
                      "links": links})

# Claims, with lease state from heartbeats.
claim_dir = os.path.join(root, "claims")
for name in sorted(os.listdir(claim_dir)) if os.path.isdir(claim_dir) else []:
    if not name.endswith(".md"):
        continue
    path = f"claims/{name}"
    text = read(os.path.join(root, path))
    terminal = None
    if re.search(r"^DONE:", text, re.M) or "Status: DONE" in text:
        terminal = "DONE"
    if "WON'T DO" in text:
        terminal = "WON'T DO"
    stamps = re.findall(r"\b(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z)\b", text)
    last = max(stamps) if stamps else None
    stale_claim = False
    if last and not terminal:
        last_dt = datetime.datetime.strptime(last, "%Y-%m-%dT%H:%M:%SZ").replace(
            tzinfo=datetime.timezone.utc)
        stale_claim = (now - last_dt) > datetime.timedelta(hours=2)
    m = re.search(r"(?:Job or assignment|Assignment|Gate job):\s*`?([^`\n]+)`?", text)
    job = m.group(1).strip() if m else None
    if job:
        edges.append({"from": path, "to": job, "type": "claims"})
    nodes.append({"id": path, "kind": "claim", "terminal": terminal,
                  "last_timestamp": last, "stale": stale_claim, "job": job})

graph = {
    "generated_at": now.strftime("%Y-%m-%dT%H:%M:%SZ"),
    "remote": remote,
    "tips_checked": len(tips),
    "summary": {
        "tickets_with_receipts": sum(1 for n in nodes if n["kind"] == "ticket"),
        "tickets_with_branch_head": sum(1 for n in nodes if n["kind"] == "ticket" and n["branches"]),
        "tickets_missing_links": sum(1 for n in nodes if n["kind"] == "ticket" and n["missing"]),
        "stale_branch_records": len(stale),
        "claims_total": sum(1 for n in nodes if n["kind"] == "claim"),
        "claims_stale_open": sum(1 for n in nodes if n["kind"] == "claim" and n["stale"]),
        "edges": len(edges),
    },
    "stale": stale,
    "nodes": nodes,
    "edges": edges,
}
out_path = os.path.join(root, "material", "work-graph.json")
os.makedirs(os.path.dirname(out_path), exist_ok=True)
with open(out_path, "w", encoding="utf-8") as f:
    json.dump(graph, f, indent=1, sort_keys=True)
print(json.dumps(graph["summary"], indent=1))
print("stale records:", len(stale))
for s in stale[:10]:
    print(" ", s["ticket"], s["branch"], s["recorded_head"][:12], "->", (s["tip_now"] or "?")[:12])
