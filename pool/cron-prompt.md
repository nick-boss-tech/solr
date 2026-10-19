# Pool cron prompt

This file is the job for every pool Linux host. Each tick reads it from `origin/coordination` at its start, so edits take effect on the next tick. Do not work from any other copy.

## Mode

OVERNIGHT: off
SKIP QUEUE: hold

- `OVERNIGHT` is the chain switch. `on` means you keep the chain going by scheduling your own wake at the end of each tick (step 18). `off` means you finish any live claim, schedule nothing, and stop. The human sets this line; you do not change it.
- `SKIP QUEUE` gates the skip-queue work in steps 6a and 11 to 15. `hold` means take none of it. The human sets it to `open` when the queue list is confirmed.
- Hosts read these two lines exactly as written. Anything other than `on`, `off`, `open` or `hold` counts as `off` and `hold`.

## General rules

- There is no main agent. Wherever an assignment, WORKFLOW.md or START-HERE.md says "main agent", read it as: the claimant decides, and the owner (Nick) answers only questions that are genuinely ambiguous.
- A finding is not the end of a claim. Each finding ends in one of: a commit on the PR branch that fixes it, with the check rerun on the new head and the PR updated; a PR body or draft update that corrects the claim; or a recorded finding that the check did not reproduce, with the reason.

- Claim work before you start it. The claim is the record that you are on it.
- Give every ticket the same attention you would give a pull request. A verdict needs the evidence the rubric asks for: the code paths read with file and line, the git history, and a reproduction attempt wherever the ticket describes a testable behavior.
- Read the code and the history, not only the ticket. When a reproduction is inconclusive, find the gap and try again with a different variant.
- A thin verdict is worse than an open ticket. If the evidence does not meet the rubric, use `keep-open` and say what is missing.
- Shared files that collect one section per host or per item (reports, censuses, tallies) are appended to, never rewritten from a stale base. Fetch and rebase to the current tip first, then add or update only your own section. Rewriting a shared file from an older copy has already deleted other hosts' sections once; do not repeat it.

## Setup, every tick

0. Read your host id from the line that starts `- Host id:` in `/workspace/AGENTS.md`. Every `$POOL_HOST` below is that id. If the line is missing, stop: write nothing, schedule nothing, and say in your reply that the host id is missing. Do not guess the id from the hostname or the directory name.
1. Your worktree: `/workspace/worktrees/pool-$POOL_HOST`. If it does not exist, create it with `git -C /workspace/solr worktree add --detach /workspace/worktrees/pool-$POOL_HOST origin/coordination`. Every tick, `git fetch origin coordination` and `git rebase origin/coordination` inside it. Never switch the shared checkout at `/workspace/solr`.
2. Sync scripts and skills: copy `pool/skills/*` from your worktree into `/workspace/.claude/skills/`, replacing older copies. Do this only if the files differ.
3. Builds queue on this host's lock, so a build running does not stop the tick. Start each build in the background (`run_in_background`) and read its log on a later tick. Do not block a tick waiting on a build.

## Pick work

4. Find your live claim: a file in `claims/` that names your host and has neither a `DONE:` line nor a `STALE:` line. If there is one, do its next step and do not start a second claim while it is live. Heartbeat only if the last heartbeat is more than an hour old.
5. If there is no live claim, take the first `assignments/*.md` in filename order that has `Status: OPEN`, names any pool Linux host as its intended host, and lists tags that appear in `hosts/pool-linux.md`, and that has no live claim. If no assignment qualifies, take the first `gates/*.md` in filename order that has `Status: OPEN` and no live claim; a gate job's own file names its intended host and capability tags, and the same tag test applies. Items marked for vm1 only go only to vm1; that is for work that needs npm or BATS, and nothing else. A `Status: HANDED` item is not a host restriction: any pool Linux host with the item's tags may claim it once the decision it waits for is recorded in the item. Until then, skip it. Skip-queue slices from `material/skip-audit/overnight-queue-2026-10-18.md` are not assignments until the `SKIP QUEUE` line is `open`.
5a. For a gate job, before any claim is written: read the branch tip with `git ls-remote` on the fork, and run `bash pool/bin/gate-preflight.sh https://github.com/nick-boss-tech/solr.git <tip sha> /workspace/gates/logs/$POOL_HOST/preflight/<gate id>`. Exit 0 means tidy is clean and you continue to step 6. Exit 3 means tidy would change files, so the gate would be GATE INVALID: set the job's Status to `GATE INVALID (preflight)` with the first line of `preflight.log`, commit and push that alone, and do not claim. Exit 2 means the check could not run: record the reason in the job and do not claim. A preflight run at posting time does not replace this one, because the tip can move.
6. Write `claims/<assignment slug>.md` from `pool/templates/claim.md` (for a gate job, use the gate ID in the claim filename and name the job file in it). In the same commit, set the assignment or gate job to `Status: CLAIMED`. Commit as Nick Shanin with no trailers and push fast-forward. If the push is rejected, fetch, rebase, re-read the branch, and stand down.
6a. For a skip-queue ticket, claim the ticket before reading it deeply. Run `git ls-tree -r --name-only origin/coordination claims/` and read any file that names the ticket key. If one has neither a `DONE:` nor a `STALE:` line, skip the ticket. Otherwise write `claims/skip-<KEY>-$POOL_HOST.md` from `pool/templates/claim.md` with `Status: CLAIMED`, commit as Nick Shanin with no trailers, and push fast-forward. The push is the lock: if it is rejected, fetch, re-check the ticket, and pick the next one. Two claim files for one ticket is a defect; name both in your claim and stop on that ticket.

## Work the claim

Builds and test runs go through `pool/bin/sweep-run.sh <worktree> <log dir> <run name> <gradle args...>` from your worktree, started in the background. It takes this host's lock and waits up to `SWEEP_WAIT` seconds (default 14400) for it. It writes the log, the headroom record, and the test XML. The host environment (JAVA_HOME, GRADLE_USER_HOME) comes from `/workspace/AGENTS.md`, with defaults in the script. Exit code 2 means the wait timed out; record it and stop. Any other non-zero exit means the build failed; read the log before you retry.

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

11. For a skip ticket, follow `material/skip-audit/verdict-rubric.md` and the templates in `material/skip-audit/templates/`.
12. Gather evidence with `python3 -I /workspace/.claude/skills/skip-ticket-evidence/evidence.py KEY --repo <your worktree> --ref origin/main`, then write the record and the JIRA comment draft.
13. Put the record and the draft comment on a branch named `solr-NNNN-draft-STATUS`, where STATUS is `close`, `pr`, or `open` per the rubric. Create the branch from the current `origin/coordination` tip, add `drafts/KEY/record.md` and `drafts/KEY/jira-comment.md`, and push the branch.
14. Record the verdict with `python3 -I material/skip-audit/queue_tool.py --repo <your worktree> set KEY VERDICT BRANCH PINNED_HEAD`, then commit and push `material/skip-audit/queue-current.json` to `coordination`.
15. Post nothing to JIRA or GitHub. JIRA does not accept comments from agents right now, so the draft waits for a bulk post by the human. Do not open pull requests from draft branches.

## Finish

16. When the work lands: write the deliverable, mark the assignment and the claim `DONE:` with the deliverable paths, and push once. For a gate job, also set the gate file to `Status: DONE` with the gated head, and write or refresh `receipts/<TICKET>.md` in the same push, as WORKFLOW.md requires of the gating host.
17. If you cannot finish, write the reason in the claim, set the assignment to `Status: HANDED`, and push.
18. Wake, if `OVERNIGHT` is `on`. End every tick by calling `mcp__user-input__schedule_resume` to continue this same session. Use `in 2 minutes` if this tick pushed a claim step, a record or a result, and `in 15 minutes` if it found nothing to do. The note names the mode, your live claim (or none), and the next step. A session holds one pending wake, so schedule once per tick, at the end. If `OVERNIGHT` is `off`, schedule nothing. Nothing enforces this step: it is part of finishing the tick. Do not use `schedule_task` for ticks: it starts a fresh session with no memory of this one.

## Limits

- Pool hosts may create assignments and gate jobs. Never post to JIRA or GitHub. JIRA takes no agent comments right now, so drafts only. Never open PRs. Never force-push. Never use bare `git stash`. Never print secrets or tokens.
- Do not use the Windows host.
- Stop and record in the claim if free space on `/` is under 2 GB, or free inodes on `/` are under 10%.
- The heartbeat is once an hour while a claim is active.
