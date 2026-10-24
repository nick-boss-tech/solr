# Pool cron prompt (coordination-ui board)

This file is the job for every pool Linux host on this board. Each tick reads it from `origin/coordination-ui` at its start, so edits take effect on the next tick. Do not work from any other copy.

## Mode

OVERNIGHT: off
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
