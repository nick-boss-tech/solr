# Pool cron prompt (coordination-ui board)

This file is the job for every pool Linux host on this board. Each tick reads it from `origin/coordination-ui` at its start, so edits take effect on the next tick. Do not work from any other copy.

## Mode

OVERNIGHT: on
INTAKE: open

- `OVERNIGHT` is the chain switch. `on` means keep the chain going by scheduling your own wake at the end of each tick. `off` means finish any live claim, schedule nothing, and stop. The owner sets this line; you do not change it.
- `INTAKE` gates new claims. `hold` means take no new work; finish live claims only. `open` means pull from the stores in step 5. The owner sets it.
- Anything other than `on`, `off`, `open`, or `hold` counts as `off` and `hold`.

## General rules

- This board replaced `coordination` on 2026-10-23. The old board is the archive (`archive/coordination-2026-10-23`); nothing on it is live. Claims from the old board do not carry: if you held one, re-claim here or leave the work.
- Before claiming work on any tick, read `pool/owner-complaints-2026-10-19.md`. It outranks habit.
- There is no main agent. The claimant decides; the owner answers only questions that pass the escalation test in `pool/decision-map.md`.
- Claim before work, heartbeat hourly, take the claim to the end (WORKFLOW.md). Never poll or sleep inside a tick; run builds detached and end the tick.
- Claim small: one claim covers one unit of work, about the size of a ticket. When an assignment lists numbered items, claim one item and name it in the claim file; the remaining items stay for other hosts. If a unit turns out to hold several tickets of work, split it in the claim and take one.
- Decide from `pool/decision-map.md` and `material/decision-ladder.md` before asking the owner anything. Record the rung used in the claim.
- A finding ends in a fix with the check rerun, a correction to the claim it falsifies, or a recorded non-reproduction with the reason. A thin verdict is worse than an open item: keep it open and say what is missing.
- Check every claimed item against the WON'T DO shapes in WORKFLOW.md before starting; refuse and clear in the same push.
- Shared files are appended to, never rewritten from a stale base.

## Setup, every tick

0. Read your host id from the line that starts `- Host id:` in `/workspace/AGENTS.md`. If it is missing, stop and say so; do not guess.
1. Your worktree: `/workspace/worktrees/pool-$POOL_HOST-ui`. Create it with `git -C /workspace/solr worktree add --detach /workspace/worktrees/pool-$POOL_HOST-ui origin/coordination-ui` if it does not exist. Every tick: `git fetch origin coordination-ui` and `git rebase origin/coordination-ui` inside it. Never switch the shared checkout at `/workspace/solr`.
2. Check `df -h /` and `df -i /`. Under 2 GB free or at most 15% free inodes: stop work, record it in your live claim, end the tick.
3. No build, test, or gate runs in the foreground of a tick. Start runs detached through `pool/bin/sweep-run.sh`, which takes this host's build lock.

## Pick work

4. Find your live claim (a file in `claims/` naming your host with no DONE line). If there is one, do its next step. A host holds at most one live claim.
5. If there is no live claim and `INTAKE` is `open`, take work in this order: the first `assignments/*.md` in filename order with `Status: OPEN`, no live claim, and tags your host holds (per `hosts/`); then the first `gates/*.md` with `Status: OPEN` and no live claim, same tag test. For a gate, run the detached preflight first per WORKFLOW.md and claim on a later tick when it has finished.
6. Write the claim from `pool/templates/claim.md` (for a gate, use the gate ID as the claim filename), set the assignment or gate job to `Status: CLAIMED` in the same commit, commit as Nick Shanin with no trailers, and push fast-forward. A rejected push means fetch, rebase, re-read, and stand down if the slice was taken.

## Work the claim

Builds and test runs go through `pool/bin/sweep-run.sh <worktree> <log dir> <run name> <gradle args...>` from your worktree. It takes this host's lock and waits up to `SWEEP_WAIT` seconds (default 14400) for it. It writes the log, the headroom record, and the test XML. The host environment (JAVA_HOME, GRADLE_USER_HOME) comes from `/workspace/AGENTS.md`, with defaults in the script. After acquiring the lock, the inode guard checks `/`, the worktree and Gradle home: 15% or less free, or unavailable inode counters, refuses Gradle with exit 4. No automatic deletion is performed; record the reason and wait for explicit cleanup direction.

Start every run detached, so the tool call returns at once and the run is not tied to the tick's timeout:

`setsid nohup bash pool/bin/sweep-run.sh <worktree> <log dir> <run name> <gradle args...> >/dev/null 2>&1 </dev/null &`

Use a run name unique to this attempt (for example include the head sha and a time). Then, in the same tick: write a claim line with the run name, log dir, head sha and start time, push it, and end the tick (step 13). Do not wait for the run inside the tick.

On a later tick, check it once with `bash pool/bin/run-status.sh <log dir> <run name>`. It prints one line and never waits:
- `queued`: waiting for the host lock. Leave it alone.
- `running`: in Gradle. Leave it alone, or do other claim work.
- `done rc=4`: inode safety refusal; Gradle was not started. Record the reason and stop without automatic deletion.
- `done rc=N`: finished. Read the log and the test XML, then write the Result line (step 8). `rc=0` is a pass only for the checks the assignment names.
- `died`: the process is gone without an `rc=` line. Record it and treat it as a failed run.
- `not-started (lock wait timed out)`: exit code 2 from the wait. Record it and stop.

A tick never runs a status check in a loop. If a claim has nothing else to do until the run finishes, end the tick with the wake from step 13 set to `in 15 minutes`.

A gate job or a test list with several steps (tidy, Error Prone, focused tests, module checks) starts as one detached job so each step gets the lock in turn:

`setsid nohup bash pool/bin/bg-steps.sh <job> <worktree> /workspace/gates/logs/$POOL_HOST <steps file> >/dev/null 2>&1 </dev/null &`

The steps file has one line per step, `<label>|<gradle args>`, and a `|continue` suffix on a step that may fail without stopping the job. The job stops at the first failing step without `continue`; inode safety refusal (exit 4) always stops the job, even with `continue`. Check it with `bash pool/bin/job-status.sh /workspace/gates/logs/$POOL_HOST <job>`, which prints the state, each finished step with its rc, and the current step. Its state is `running`, `done`, `stopped at <label>`, or `died`. Use a job name unique to the attempt.

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

## Finish

11. When the work lands: write the deliverable, mark the assignment and the claim `DONE:` with the deliverable paths, and push once. For a gate job, also set the gate file to `Status: DONE` with the gated head, and write or refresh `receipts/<TICKET>.md` in the same push, as WORKFLOW.md requires of the gating host.
12. If you cannot finish, write the reason in the claim, set the assignment to `Status: HANDED`, and push.
13. Every tick ends here, whether or not a run is in progress. Wake, if `OVERNIGHT` is `on`. End every tick by calling `mcp__user-input__schedule_resume` to continue this same session. Use `in 2 minutes` by default. Use `in 15 minutes` only when this tick's only live work is a running build or preflight with no other claim work, or when the disk check in step 2 stopped you. The note names the mode, your live claim (or none), and the next step. A session holds one pending wake, so schedule once per tick, at the end. If `OVERNIGHT` is `off`, schedule nothing. Do not use `schedule_task` for ticks: it starts a fresh session with no memory of this one.

## Limits

- Pool hosts may create assignments and gate jobs. Never post to JIRA or GitHub. JIRA takes no agent comments right now, so drafts only. Never open PRs. Never force-push. Never use bare `git stash`. Never print secrets or tokens.
- Do not use the Windows host.
- Stop and record in the claim if free space on `/` is under 2 GB, or free inodes on `/` are 15% or less.
- The heartbeat is once an hour while a claim is active.
