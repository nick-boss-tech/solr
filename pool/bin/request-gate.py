#!/usr/bin/env python3
"""Post a gate request to coordination as gates/<GATE_ID>.md.

usage: request-gate.py [--dry-run] <request file>

The request file has one "Field: value" per line (pool/templates/gate-request.md).
The script checks it, reads the branch tip from the fork, refuses a gate ID that already
has a gate job or a live claim, writes the gate job, and pushes it to coordination as a
fast-forward commit. --dry-run prints the job and changes nothing.
"""
import base64, datetime, os, re, subprocess, sys, tempfile

REQUIRED = ["Ticket", "Branch", "Base", "Module", "Focused class", "Seed",
            "Changelog", "Diff scope", "Proof leg", "Why"]
DASHES = ("\u2014", "\u2013")
os.environ.update({"GIT_AUTHOR_NAME": "Nick Shanin", "GIT_AUTHOR_EMAIL": "nick.boss.us@gmail.com",
                   "GIT_COMMITTER_NAME": "Nick Shanin", "GIT_COMMITTER_EMAIL": "nick.boss.us@gmail.com"})
TOKEN = None


def die(msg):
    sys.exit("refused: " + msg)


def load_token():
    t = os.environ.get("GITHUB_TOKEN")
    if not t and os.path.exists("/workspace/.env"):
        for line in open("/workspace/.env", encoding="utf-8"):
            if line.startswith("GITHUB_TOKEN="):
                t = line.split("=", 1)[1].strip().strip('"\r')
    if not t:
        die("no GITHUB_TOKEN in the environment or /workspace/.env")
    return t


def git(*args, cwd=None, auth=False, check=True):
    cmd = ["git"]
    if auth:
        header = "AUTHORIZATION: basic " + base64.b64encode(f"x-access-token:{TOKEN}".encode()).decode()
        cmd += ["-c", "http.extraheader=" + header]
    cmd += list(args)
    r = subprocess.run(cmd, cwd=cwd, capture_output=True, text=True)
    if check and r.returncode != 0:
        die(f"git {args[0]} failed: {r.stderr.strip()[:300]}")
    return r


def parse(path):
    vals = {}
    for line in open(path, encoding="utf-8"):
        key, sep, value = line.rstrip("\n").partition(":")
        if sep:
            vals[key.strip()] = value.strip()
    return vals


def validate(v):
    for k in REQUIRED:
        val = v.get(k, "")
        if not val:
            die(f"'{k}' is empty")
        if val.startswith("FILL"):
            die(f"'{k}' still says FILL")
    for k, val in v.items():
        if any(d in val for d in DASHES):
            die(f"'{k}' has an em dash or en dash")
    if not re.fullmatch(r"[A-Z][A-Z0-9]*-\d+", v["Ticket"]):
        die("Ticket must look like SOLR-1234")
    gate = v.get("Gate ID", "").strip()
    if gate and gate != v["Ticket"] and not re.fullmatch(
        re.escape(v["Ticket"]) + r"-phase-[a-z0-9]+(?:-[a-z0-9]+)*", gate, re.I
    ):
        die("Gate ID must be the ticket key or the ticket key plus -phase-<slug>")
    if not re.fullmatch(r"[A-Za-z0-9._/-]+", v["Branch"]):
        die("Branch must be a plain branch name")
    if not re.fullmatch(r"[0-9a-f]{7,40}", v["Base"]):
        die("Base must be a lowercase sha")
    if v.get("Head") and not re.fullmatch(r"[0-9a-f]{40}", v["Head"]):
        die("Head must be a 40-character lowercase sha")
    if not v["Module"].startswith(":"):
        die("Module must be a Gradle path such as :solr:core")
    if not re.fullmatch(r"[A-Za-z_][\w.]*", v["Focused class"]):
        die("Focused class must be a fully qualified class name")
    if not re.fullmatch(r"[0-9A-Fa-f]+", v["Seed"]):
        die("Seed must be hex")
    if v["Changelog"] != "none" and not v["Changelog"].startswith("changelog/"):
        die("Changelog must be a path under changelog/, or none")


def gate_id(v):
    return v.get("Gate ID", "").strip() or v["Ticket"]


def check_free(ticket, gate, cwd):
    job_path = f"gates/{gate}.md"
    if git("cat-file", "-e", f"origin/coordination:{job_path}", cwd=cwd, check=False).returncode == 0:
        die(f"{job_path} already exists on coordination")
    names = git("ls-tree", "--name-only", "origin/coordination", "claims/", cwd=cwd).stdout.split()
    pattern = re.escape(ticket.lower()) + r"(?!\d)"
    legacy_gate_pattern = "gate-backlog-" + ticket.rsplit("-", 1)[1].lower()
    phase_pattern = re.compile(re.escape(ticket.lower()) + r"-phase-[a-z0-9]+(?:-[a-z0-9]+)*(?=\.md$)")
    for name in names:
        lower_name = name.lower()
        phase_match = phase_pattern.search(lower_name)
        if gate.lower() != ticket.lower() and phase_match and phase_match.group() != gate.lower():
            continue
        if not re.search(pattern, lower_name) and legacy_gate_pattern not in lower_name:
            continue
        text = git("show", f"origin/coordination:{name}", cwd=cwd).stdout
        if not re.search(r"(?m)^(DONE|STALE)\b|^Status: (DONE|STALE|CLOSED)", text):
            die(f"{name} is a live claim for {gate}")


def build(v, tip):
    ticket, module, cls, seed = v["Ticket"], v["Module"], v["Focused class"], v["Seed"]
    gate = gate_id(v)
    heading = f" ({v['Title']})" if v.get("Title") else ""
    host = v.get("Intended host") or "any capable Linux host"
    today = datetime.datetime.now(datetime.timezone.utc).date().isoformat()
    if v["Changelog"] == "none":
        step0 = "Step 0: changelog check. The branch must add no changelog fragment; if it adds one, record that here and stop."
    else:
        step0 = f"Step 0: changelog YAML parse of `{v['Changelog']}`."
    if v["Proof leg"].lower() == "none":
        step3 = "Step 3, proof leg: none requested. Record that in the receipt."
    else:
        step3 = f"Step 3, proof leg: {v['Proof leg']} Any other failure shape stops the job and follows the failure-route row."
    rows = [
        ("Tidy changes any file", "decided: repair",
         "Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs.", "Nick Shanin (standing pool rule)"),
        ("Failure in repair scope (compile, changelog, lint, or up to two failing tests)", "decided: repair, then one retry",
         "Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree.", "Nick Shanin (standing pool rule)"),
        ("Failure outside repair scope, or any failure after the retry", "decided: hand back",
         "Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch.", "Nick Shanin (standing pool rule)"),
        ("Cluster fails to start before any assertion", "default: run the same command once more",
         "If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result.", "Nick Shanin (standing pool rule)"),
        ("Proof leg result differs from the stated expectation", "decided: record and stop",
         "Record the test name and both failure texts here. Do not edit the tests.", "Nick Shanin (standing pool rule)"),
        ("Tip moved at claim time", "decided: stop", "Record the new tip here and hand back.", "Nick Shanin (standing pool rule)"),
    ]
    table = "\n".join(f"| {a} | {b} | {c} | {d} |" for a, b, c, d in rows)
    return f"""# Gate job: {ticket}{heading}

Status: OPEN

- Branch: `{v['Branch']}`. Head: `{tip}` (fork tip, confirmed by ls-remote on {today}). If the tip has moved at claim time, stop and record the new tip here.
- Base: `{v['Base']}`. Prior gated head: none recorded.
- Gate ID: `{gate}`. Claim file: `claims/pool-<host>-gate-{gate}.md`.
- Intended host: {host}. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `{module}`. Focused class: `{cls}`. Seed: `{seed}`.
- Diff scope: {v['Diff scope']}
- Why: {v['Why']}

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- {step0}
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `{module}:compileJava {module}:compileTestJava -Pvalidation.errorprone=true`.
- {step3}
- Step 4, focused head run: `{module}:cleanTest` first, then `{module}:test --tests {cls} -Ptests.seed={seed}`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `{module}:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
{table}

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/{ticket}.md` with a new top section for gate ID `{gate}`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-{gate}.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
"""


def post(ticket, gate, job, tip, cwd):
    job_path = f"gates/{gate}.md"
    path = tempfile.mkdtemp(prefix="request-gate-")
    git("worktree", "add", "--detach", path, "origin/coordination", cwd=cwd)
    try:
        os.makedirs(os.path.join(path, "gates"), exist_ok=True)
        with open(os.path.join(path, job_path), "w", encoding="utf-8") as f:
            f.write(job)
        git("add", job_path, cwd=path)
        git("commit", "-q", "-m", f"{ticket}: post {gate} gate at {tip[:10]}", cwd=path)
        for _ in range(3):
            if git("push", "origin", "HEAD:refs/heads/coordination", cwd=path, auth=True, check=False).returncode == 0:
                break
            git("fetch", "-q", "origin", "coordination", cwd=path, auth=True)
            if git("rebase", "origin/coordination", cwd=path, check=False).returncode != 0:
                die(f"rebase onto coordination failed; resolve in {path}")
            check_free(ticket, gate, path)
        else:
            die("push was rejected three times")
        git("fetch", "-q", "origin", "coordination", cwd=path, auth=True)
        back = git("show", f"origin/coordination:{job_path}", cwd=path).stdout
        if f"# Gate job: {ticket}" not in back:
            die("read-back did not find the gate job")
        sha = git("rev-parse", "--short", "HEAD", cwd=path).stdout.strip()
        print(f"posted {job_path} on coordination at {sha}; read back OK")
    finally:
        git("worktree", "remove", "--force", path, cwd=cwd, check=False)


def main():
    global TOKEN
    args = sys.argv[1:]
    dry = "--dry-run" in args
    args = [a for a in args if a != "--dry-run"]
    if len(args) != 1:
        sys.exit(__doc__.strip())
    v = parse(args[0])
    validate(v)
    TOKEN = load_token()
    ls = git("ls-remote", "origin", f"refs/heads/{v['Branch']}", auth=True).stdout.split()
    if not ls:
        die(f"branch {v['Branch']} is not on origin")
    tip = ls[0]
    if v.get("Head") and v["Head"] != tip:
        die(f"fork tip of {v['Branch']} is {tip}, not {v['Head']}")
    cwd = os.getcwd()
    git("fetch", "-q", "origin", "coordination", cwd=cwd, auth=True)
    gate = gate_id(v)
    check_free(v["Ticket"], gate, cwd)
    job = build(v, tip)
    if dry:
        sys.stdout.write(job)
        return
    post(v["Ticket"], gate, job, tip, cwd)


if __name__ == "__main__":
    main()
