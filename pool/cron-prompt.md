# Pool cron prompt

This file is the job for every pool Linux host. Each tick reads it from `origin/coordination` at its start, so edits take effect on the next tick. Do not work from any other copy.

## Mode

OVERNIGHT: on
SKIP QUEUE: open

- `OVERNIGHT` is the chain switch. `on` means you keep the chain going by scheduling your own wake at the end of each tick (step 18). `off` means you finish any live claim, schedule nothing, and stop. The human sets this line; you do not change it.
- `SKIP QUEUE` gates the skip-queue work in steps 6a and 11 to 15. `hold` means take none of it. The human sets it to `open` when the queue list is confirmed.
- Hosts read these two lines exactly as written. Anything other than `on`, `off`, `open` or `hold` counts as `off` and `hold`.

## General rules

- Before claiming work on any tick, read `pool/owner-complaints-2026-10-19.md`. It is the owner's current list of what is going wrong on this branch and what he expects instead, and it outranks habit: if your next claim would repeat something complained about there, take different work. A closeout that answers a complaint says so and names its number. `pool/START-HERE.md` carries the same pointer for posting and claiming.
- There is no main agent. Wherever an assignment, WORKFLOW.md or START-HERE.md says "main agent", read it as: the claimant decides, and the owner (Nick) answers only questions that are genuinely ambiguous.
- A finding is not the end of a claim. Each finding ends in one of: a commit on the PR branch that fixes it, with the check rerun on the new head and the PR updated; a PR body or draft update that corrects the claim; or a recorded finding that the check did not reproduce, with the reason.

- WON'T DO, do not redo. Before starting any claimed item, check it against `pool/advisory-wont-do.md`. If the item is already done, already claimed and live, structurally unable to finish, or only stale paperwork, refuse it: stamp your claim WON'T DO with the evidence, take the clearing action the advisory names, in the same push, and take other work. A refusal that clears a stale record is finished work.
- Work comes from the three stores in `pool/work-stores.md`, pulled in that order: follow-ups and reviews of existing work, the skip list, then the CSV backlog. If your store has nothing claimable, pull from the next store and name the store in your heartbeat. Do not report no work while any store holds unclaimed items; when the posted CSV slices are all taken, cut the next two-row slice yourself under the rules in `pool/work-stores.md`.
- CSV overlap check is required before classifying either row: look up the exact issue key in `material/skip-audit/` and prior `material/jira-csv-triage/` slices, the completed critical/blocker reports, and ticket-specific assignments, claims, reports, drafts, gates and receipts. If earlier ticket work exists, keep the CSV row in its slice, mark it `overlap`, and cite the record path and its current status. Compare dates and heads when records disagree: the queue can lag a newer claim, report or receipt, and a green receipt proves only its recorded head. A queue entry by itself is not proof of completion; an open remediation, live claim or Store 1 promise is still open. The full lookup and precedence rules are in `assignments/pool-csv-backlog-round-1.md`.
- Claims carry lease fields (WORKFLOW.md, Claims): a claim is live for 2 hours past its last heartbeat, a takeover increments Attempts and resumes from recorded steps under the claim's idempotency key, and every end state in WORKFLOW.md (Terminal states) is terminal: never edit a terminal claim; supersede it with a new claim that names it. An item you touched never stays OPEN: it ends DONE, NEARLY, WON'T DO, OWNER DECISION, RETRY or HANDED BACK. Never poll or sleep inside a tick; run work detached and end the tick with a wake.
- Decide from `pool/decision-map.md` before asking the owner anything. It classifies every item on its axes and carries the standing answers; a question reaches the owner only if it passes the escalation test on that page (no rule settles it, the ladder does not decide it, the fact cannot be read, and the choice is genuinely ambiguous). Asking a settled question is a defect in the asking.
- Claim work before you start it. The claim is the record that you are on it.
- Take the claim to the end (WORKFLOW.md rule 9). Do not stop a claimed task to ask permission: decide inside the claim with `material/decision-ladder.md`, record the rung, and continue to DONE or to an ENDED line with the reason. The owner is asked only at rung 5; opening a PR stays owner-approved.
- Give every ticket the same attention you would give a pull request. A verdict needs the evidence the rubric asks for: the code paths read with file and line, the git history, and a reproduction attempt wherever the ticket describes a testable behavior.
- Read the code and the history, not only the ticket. When a reproduction is inconclusive, find the gap and try again with a different variant.
- A thin verdict is worse than an open ticket. If the evidence does not meet the rubric, use `keep-open` and say what is missing.
- A recorded skip verdict is provisional until a pool honesty audit passes it (the pool-aab cross-audit batches and the remediation re-audits under the pool-aaa batches). `material/skip-audit/remediation-current.json` lists the verdicts the audit has marked suspect; a listed verdict is not done, whatever `queue-current.json` says. Remediation work outranks fresh skip closeouts: the `assignments/pool-aaa-*` batches carry it, and a ticket listed there is worked under its batch, never as a fresh take.
- The pool maintains the remediation queue itself; no outside audit does it. When your cross-audit work marks a verdict suspect, append it to `material/skip-audit/remediation-current.json` as status open in the same push. When you re-audit another host's remediation redo and it passes the full rubric, set that item's status to cleared with the date in the same file; when it fails, leave it open and append the gaps to `reports/skip-verdict-audit.md`. When the open items that no `assignments/pool-aaa-*.md` file lists reach 25, post the next batch yourself: copy the structure of the highest-numbered existing batch, list up to 25 unbatched open items in remediation-current.json order, regenerate `OPEN-WORK.md` with `pool/bin/open-work.py`, and push both in one commit. Never list an item in two batches.
- Shared files that collect one section per host or per item (reports, censuses, tallies) are appended to, never rewritten from a stale base. Fetch and rebase to the current tip first, then add or update only your own section. Rewriting a shared file from an older copy has already deleted other hosts' sections once; do not repeat it.

## Setup, every tick

0. Read your host id from the line that starts `- Host id:` in `/workspace/AGENTS.md`. Every `$POOL_HOST` below is that id. If the line is missing, stop: write nothing, schedule nothing, and say in your reply that the host id is missing. Do not guess the id from the hostname or the directory name.
1. Your worktree: `/workspace/worktrees/pool-$POOL_HOST`. If it does not exist, create it with `git -C /workspace/solr worktree add --detach /workspace/worktrees/pool-$POOL_HOST origin/coordination`. Every tick, `git fetch origin coordination` and `git rebase origin/coordination` inside it. Never switch the shared checkout at `/workspace/solr`.
2. Sync scripts and skills: copy `pool/skills/*` from your worktree into `/workspace/.claude/skills/`, replacing older copies. Do this only if the files differ.
3a. Check `df -h /` and `df -i /`. If free space on `/` is under 2 GB or free inodes are under 10%, stop work, record it in your live claim, and end the tick with the wake in step 18 set to `in 15 minutes`.
3. Builds queue on this host's lock, so a build running does not stop the tick. No build, test, gate or preflight runs in the foreground, and no tick waits for one. Do not `sleep`, loop, or re-run a status check inside a tick. Start it detached (see "Work the claim") and do other claim work or end the tick.

## Pick work

4. Find your live claim: a file in `claims/` that names your host and has neither a `DONE:` line nor a `STALE:` line. If there is one, do its next step. A host holds at most one live claim: a skip-ticket claim stays live until its `DONE:` line is pushed. Heartbeat only if the last heartbeat is more than an hour old.
5. If there is no live claim, take work in this order: first an `assignments/pool-aaa-*.md` remediation batch with `Status: OPEN` and no live claim (these outrank every other assignment and gate job); then an `assignments/pool-aab-*.md` cross-audit batch; then the first `assignments/*.md` in filename order that has `Status: OPEN`, names any pool Linux host as its intended host, and lists tags that appear in `hosts/pool-linux.md`, and that has no live claim. If no assignment qualifies, take the first `gates/*.md` in filename order that has `Status: OPEN` and no live claim; a gate job's own file names its intended host and capability tags, and the same tag test applies. Items whose tags include bats or npm go only to a host whose file in hosts/ lists those tags, and nothing else. A `Status: HANDED` item is not a host restriction: any pool Linux host with the item's tags may claim it once the decision it waits for is recorded in the item. Until then, skip it. A recurring assignment (its Status line says recurring) is taken only if its newest DONE date is more than 12 hours old; its claim's DONE line starts the next window, so no tick repeats it. The overnight slices in `material/skip-audit/overnight-queue-2026-10-18.md` are already DONE or HANDED; do not claim them again.
5a. For a gate job, before any claim is written: read the branch tip with `git ls-remote` on the fork, and start the preflight detached: `setsid nohup bash -c 'bash pool/bin/gate-preflight.sh https://github.com/nick-boss-tech/solr.git <tip sha> /workspace/gates/logs/$POOL_HOST/preflight/<gate id>; echo rc=$? > /workspace/gates/logs/$POOL_HOST/preflight/<gate id>/preflight.rc' >/dev/null 2>&1 </dev/null &`. End the tick. On a later tick, if `preflight.rc` does not exist yet, the preflight is still running: end the tick again. When it exists, exit 0 means tidy is clean and you continue to step 6. Exit 3 means tidy would change files. That is a gate repair, not a stop: claim as usual, and the gate host applies the tidy output under WORKFLOW.md "Gate repairs" and records the patch. Exit 2 means the check could not run: record the reason in the job and do not claim. A preflight run at posting time does not replace this one, because the tip can move.
6. Write `claims/<assignment slug>.md` from `pool/templates/claim.md` (for a gate job, use the gate ID in the claim filename and name the job file in it). In the same commit, set the assignment or gate job to `Status: CLAIMED`. Commit as Nick Shanin with no trailers and push fast-forward. If the push is rejected, fetch, rebase, re-read the branch, and stand down.
6a. When steps 4 and 5 leave nothing to do and `SKIP QUEUE` is `open`, take the next skip ticket. In the rebased tree, read `material/skip-audit/queue-current.json` in file order and take the first record with `status` `skip`, no top-level `verdict`, and `audit.verdict` of `confirmed-skip`, `deferred` or `branched`. Skip any key that a file in `claims/` names without a `DONE:` or `STALE:` line. Then write `claims/skip-<KEY>-$POOL_HOST.md` from `pool/templates/claim.md` with `Status: CLAIMED`, commit as Nick Shanin with no trailers, and push fast-forward. The push is the lock: if it is rejected, fetch, rebase, re-check the ticket, and pick the next one. Two claim files for one ticket is a defect; name both in your claim and stop on that ticket. A key listed with status `open` in `material/skip-audit/remediation-current.json` is never a fresh take under this step; it is remediation, worked under its `pool-aaa-*` batch assignment.

## Work the claim

Builds and test runs go through `pool/bin/sweep-run.sh <worktree> <log dir> <run name> <gradle args...>` from your worktree. It takes this host's lock and waits up to `SWEEP_WAIT` seconds (default 14400) for it. It writes the log, the headroom record, and the test XML. The host environment (JAVA_HOME, GRADLE_USER_HOME) comes from `/workspace/AGENTS.md`, with defaults in the script.

Start every run detached, so the tool call returns at once and the run is not tied to the tick's timeout:

`setsid nohup bash pool/bin/sweep-run.sh <worktree> <log dir> <run name> <gradle args...> >/dev/null 2>&1 </dev/null &`

Use a run name unique to this attempt (for example include the head sha and a time). Then, in the same tick: write a claim line with the run name, log dir, head sha and start time, push it, and end the tick (step 18). Do not wait for the run inside the tick.

On a later tick, check it once with `bash pool/bin/run-status.sh <log dir> <run name>`. It prints one line and never waits:
- `queued`: waiting for the host lock. Leave it alone.
- `running`: in Gradle. Leave it alone, or do other claim work.
- `done rc=N`: finished. Read the log and the test XML, then write the Result line (step 8). `rc=0` is a pass only for the checks the assignment names.
- `died`: the process is gone without an `rc=` line. Record it and treat it as a failed run.
- `not-started (lock wait timed out)`: exit code 2 from the wait. Record it and stop.

A tick never runs a status check in a loop. If a claim has nothing else to do until the run finishes, end the tick with the wake from step 18 set to `in 15 minutes`.

A gate job or a test list with several steps (tidy, Error Prone, focused tests, module checks) starts as one detached job so each step gets the lock in turn:

`setsid nohup bash pool/bin/bg-steps.sh <job> <worktree> /workspace/gates/logs/$POOL_HOST <steps file> >/dev/null 2>&1 </dev/null &`

The steps file has one line per step, `<label>|<gradle args>`, and a `|continue` suffix on a step that may fail without stopping the job. The job stops at the first failing step without `continue`. Check it with `bash pool/bin/job-status.sh /workspace/gates/logs/$POOL_HOST <job>`, which prints the state, each finished step with its rc, and the current step. Its state is `running`, `done`, `stopped at <label>`, or `died`. Use a job name unique to the attempt.

7. Work the assignment item by item. Each Decisions row is binding. A decision you have to make inside a claim follows the ladder in `material/decision-ladder.md`: read the code and its history first, then probes, then stated intent, then a recorded choice; only an open maintainer question or an owner-level decision goes to NEEDS-OWNER. Where a row says escalate, make the call under the PR's stated intent, record the reason in the claim, and carry it through to the PR branch or draft. Stop only when the intent itself is ambiguous: then set the item to `Status: NEEDS-OWNER` and write the one question for Nick in the claim.
8. After every build or test run, write a Result line in the claim: the command, the head sha, pass or fail, the seed, the log path, and whether the result is conclusive, with the reason.
9. If a result is not conclusive, gather more information: read the code, the git history (`git log -S`, `git blame`, the test's own history), and the ticket. Pick a different build or test variant that separates the explanations, run it, and record that attempt as well.
10. A failed run is never repeated on its own. Follow the failure sequence in the section below, and record each step in the claim.

## Failed runs

A failed build, gate, test or sweep moves through these steps in order. The claim records each one.

1. Assignment: state the question the run answers and the result you expected.
2. Information gathering: read the failing output, the code path it names, and the git history of that code. Write what the failure shows.
3. Test, gate or build: run again only if something changed since the failure. Something changed means one of these, written in the claim before the run: the head sha, a patch, the seed or the input, or the environment (for example JAVA_HOME). A rerun with the same head, seed, input and environment is not allowed.
4. Patch and retry, or attach a status: if the gathering shows a fix, commit the patch on a branch, then run once against the new head. If it does not, attach a status (NEEDS-OWNER, keep-open, or refuted) with the reason, and stop.

A repeated run that fails the same way is not new evidence. A reproduction sweep may use new seeds only if its Decisions rows name that rule.

## Skip-queue tickets

Steps 11 to 15 apply only when `SKIP QUEUE` is `open` and you hold a ticket claim from step 6a.

The skip audit is already done for these tickets: `audit.verdict` in `queue-current.json` holds the audit result. Your work is the closeout, a re-check at a pinned head and one closeout verdict.

11. Read the ticket's `audit` record in `queue-current.json` and its evidence. The audit verdict is the starting point, not the answer.
12. Re-check at the pinned head: `python3 -I /workspace/.claude/skills/skip-ticket-evidence/evidence.py KEY --repo <your worktree> --ref origin/main`. Choose one closeout verdict per `material/skip-audit/verdict-rubric.md`, meeting its minimum evidence. Write `drafts/KEY/record.md` and `drafts/KEY/jira-comment.md` from the templates in `material/skip-audit/templates/`.
12a. Before recording any verdict, check your own work the way the honesty audit will: the claim file for this ticket exists and will carry its DONE line in the same push sequence; the record names the Jira status read in this pass; the code path is cited with file and line, or the reproduction attempt and its result are stated; the pinned head in the record equals the head step 14 will record. If any item is missing, the verdict is not ready: finish the missing piece, or record `keep-open` naming the gap.
13. Put them on a branch named `solr-NNNN-draft-STATUS`, where STATUS is `close`, `pr`, or `open` per the rubric. Create the branch from the current `origin/coordination` tip, add the two files, and push the branch.
14. In a tree rebased to the current `origin/coordination`, record the verdict with `python3 -I material/skip-audit/queue_tool.py --repo <your worktree> set KEY VERDICT BRANCH PINNED_HEAD`. Commit `material/skip-audit/queue-current.json` and your claim's `DONE:` line together, and push fast-forward. If the push is rejected, fetch, rebase, run `set` again on the rebased file, and push. Never resolve a `queue-current.json` conflict by hand. For a remediation ticket, the claim's DONE line names its `pool-aaa-*` batch assignment, and the batch report line in `reports/skip-verdict-remediation-<batch>.md` is written in the same push sequence.
15. Post nothing to JIRA or GitHub. JIRA takes no agent comments, so the draft waits for a bulk post by the human. Do not open pull requests from draft branches.

## Finish

16. When the work lands: write the deliverable, mark the assignment and the claim `DONE:` with the deliverable paths, and push once. For a gate job, also set the gate file to `Status: DONE` with the gated head, and write or refresh `receipts/<TICKET>.md` in the same push, as WORKFLOW.md requires of the gating host.
17. If you cannot finish, write the reason in the claim, set the assignment to `Status: HANDED`, and push.
18. Every tick ends here, whether or not a run is in progress. Wake, if `OVERNIGHT` is `on`. On hosts with the wake hooks (`pool/hooks/`, installed in `/workspace/.claude/hooks`), the Stop hook refuses to end a turn without a scheduled wake while `OVERNIGHT` is `on`; the hook is a backstop, not a reason to skip the wake. End every tick by calling `mcp__user-input__schedule_resume` to continue this same session. Use `in 2 minutes` by default. Use `in 15 minutes` only when this tick's only live work is a running build or preflight with no other claim work, or when the disk check in step 3a stopped you. The note names the mode, your live claim (or none), and the next step. A session holds one pending wake, so schedule once per tick, at the end. If `OVERNIGHT` is `off`, schedule nothing. Nothing enforces this step: it is part of finishing the tick. Do not use `schedule_task` for ticks: it starts a fresh session with no memory of this one.

## Limits

- Pool hosts may create assignments and gate jobs. Never post to JIRA or GitHub. JIRA takes no agent comments right now, so drafts only. Never open PRs. Never force-push. Never use bare `git stash`. Never print secrets or tokens.
- Do not use the Windows host.
- Stop and record in the claim if free space on `/` is under 2 GB, or free inodes on `/` are under 10%.
- The heartbeat is once an hour while a claim is active.
