# Pool cron prompt

This file is the job for every pool Linux host. The scheduled task reads it from `origin/coordination` at the start of each tick, so edits take effect on the next tick. Do not work from any other copy.

## General rules

- Claim work before you start it. The claim is the record that you are on it.
- Give every ticket the same attention you would give a pull request. A verdict needs the evidence the rubric asks for: the code paths read with file and line, the git history, and a reproduction attempt wherever the ticket describes a testable behavior.
- Read the code and the history, not only the ticket. When a reproduction is inconclusive, find the gap and try again with a different variant.
- A thin verdict is worse than an open ticket. If the evidence does not meet the rubric, use `keep-open` and say what is missing.

## Setup, every tick

0. Read your host id: `POOL_HOST=$(cat ~/.pool-host)`. It is a file on this VM's local disk, not in the repo. If the file is missing or empty, exit and record nothing. Every `$POOL_HOST` below is that id.
1. Your worktree: `/workspace/worktrees/pool-$POOL_HOST`. If it does not exist, create it with `git -C /workspace/solr worktree add --detach /workspace/worktrees/pool-$POOL_HOST origin/coordination`. Every tick, `git fetch origin coordination` and `git rebase origin/coordination` inside it. Never switch the shared checkout at `/workspace/solr`.
2. Sync scripts and skills: copy `pool/skills/*` from your worktree into `/workspace/.claude/skills/`, replacing older copies. Do this only if the files differ.
3. If `flock -n /workspace/tools/.gradle-$POOL_HOST.lock true` fails, a build is running on this host. Exit.

## Pick work

4. Find your live claim: a file in `claims/` that names your host and has neither a `DONE:` line nor a `STALE:` line. If there is one, do its next step. Heartbeat only if the last heartbeat is more than an hour old.
5. If there is no live claim, take the first `assignments/*.md` in filename order that has `Status: OPEN`, names any pool Linux host as its intended host, and lists tags that appear in `hosts/pool-linux.md`, and that has no live claim. Items marked for vm1 only or `Status: HANDED` go only to vm1; other hosts skip them.
6. Write `claims/<assignment slug>.md` from `pool/templates/claim.md`. In the same commit, set the assignment to `Status: CLAIMED`. Commit as Nick Shanin with no trailers and push fast-forward. If the push is rejected, fetch, rebase, re-read the branch, and stand down.

## Work the claim

Builds and test runs go through `pool/bin/sweep-run.sh <worktree> <log dir> <run name> <gradle args...>` from your worktree. It takes this host's lock and writes the log, the headroom record, and the test XML. Exit code 2 means the lock is held; record it and stop.

7. Work the assignment item by item. Each Decisions row is binding. Where a row says escalate, record it in the claim and stop that item.
8. After every build or test run, write a Result line in the claim: the command, the head sha, pass or fail, the seed, the log path, and whether the result is conclusive, with the reason.
9. If a result is not conclusive, gather more information: read the code, the git history (`git log -S`, `git blame`, the test's own history), and the ticket. Pick a different build or test variant that separates the explanations, run it, and record that attempt as well.
10. A failed gate job is not relaunched on sight. Record the failure and escalate. Reproduction sweeps may retry as their Decisions rows allow.

## Skip-queue tickets

11. For a skip ticket, follow `material/skip-audit/verdict-rubric.md` and the templates in `material/skip-audit/templates/`.
12. Gather evidence with `python3 -I /workspace/.claude/skills/skip-ticket-evidence/evidence.py KEY --repo <your worktree> --ref origin/main`, then write the record and the JIRA comment draft.
13. Put the record and the draft comment on a branch named `solr-NNNN-draft-STATUS`, where STATUS is `close`, `pr`, or `open` per the rubric. Create the branch from the current `origin/coordination` tip, add `drafts/KEY/record.md` and `drafts/KEY/jira-comment.md`, and push the branch.
14. Record the verdict with `python3 -I material/skip-audit/queue_tool.py --repo <your worktree> set KEY VERDICT BRANCH PINNED_HEAD`, then commit and push `material/skip-audit/queue-current.json` to `coordination`.
15. Post nothing to JIRA or GitHub. Do not open pull requests from draft branches. The comment is a draft until a person posts it.

## Finish

16. When the work lands: write the deliverable, mark the assignment and the claim `DONE:` with the deliverable paths, and push once.
17. If you cannot finish, write the reason in the claim, set the assignment to `Status: HANDED`, and push.

## Limits

- Never create assignments. Never post to JIRA or GitHub. Never open PRs. Never force-push. Never use bare `git stash`. Never print secrets or tokens.
- Do not use the Windows host.
- Stop and record in the claim if free space on `/` is under 2 GB, or free inodes on `/` are under 10%.
- The heartbeat is once an hour while a claim is active.
