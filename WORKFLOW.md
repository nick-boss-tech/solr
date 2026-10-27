# Workflow: claims, gates, posting

This branch is an open pool, not addressed mail. Any agent that polls it may take any assignment it is capable of running. There is no coordinating agent: hosts coordinate through claims, gate files, and reports on this branch, and the owner (Nick Shanin) answers only questions no rule settles. The order of operations is in `START-HERE.md`.

## Invariants

- Authorship on every commit is Nick Shanin, with no attribution trailers. Host identity lives in claim files and commit messages. Factory commits land in apache/solr PRs; a host name as author would be a defect there.
- A handoff is a file on this branch. Chat messages and local notes are not handoffs.
- Workers add files; they do not edit shared files. A claim, a report, a draft, or a gate job is a new file. Shared indexes are generated, not hand-edited.
- Push only as a fast-forward: fetch, rebase, push. Never force. Never reset a worktree that holds unpushed commits.
- Work branches go to the fork (nick-boss-tech/solr) when they first have a commit, under a name carrying the ticket key. Creating a branch is not a PR and needs no approval.
- Edits to our own posted PR bodies and comments are allowed and should be made at once when they are wrong; opening a PR, posting a new comment, or filing or commenting on Jira needs the owner's approval first.

## Folders

- `assignments/`: the pool. Every assignment names its capability tags, deliverables, and rules. Posted once, never edited by workers.
- `claims/`: one claim file per assignment or slice. Added once, never edited; status changes are new files (see Claims).
- `gates/`: one file per gate job: ticket, branch, head, module, focused classes, seed, proof shape, claiming host, runner and log paths, step checklist. Posted once, never edited; completion is derived from the receipt.
- `receipts/`: one file per ticket with the verified gate state at the live tip, written by the host that ran the gate as its finishing step. The gating host owns its receipt and is its only writer.
- `reports/`: findings, assessments, audits, reconciliations. Add-only deliverables, one per round.
- `jira/`: comment drafts and the scoped list. Drafts are added, never edited; the scoped list is maintained by the phone agent, not by workers.
- `drafts/`: outbound drafts that are not Jira comments, such as vote-thread notes. Add-only.
- `hosts/`: one file per host, its capabilities and resource record. Each host owns its own file and is its only writer; the hourly heartbeat goes here, not in the claim.
- `OPEN-WORK.md`: a generated index, rebuilt by `pool/bin/open-work.py` from the records. Never hand-edited.

## Capability tags

- `review`, `draft`: any host.
- `records`: hosts whose file lists it.
- `implementation`: hosts whose file lists it; build and test proof stays with a capable Linux host.
- `gate`, `premise-run`, `settling-run`, `sweep`, `bats`: Linux hosts with the toolchain.
- `selenium`, `windows-check`: the Windows host.
- `ui`: work in the `ui/` build. Linux hosts; the build facts are in `UI-PLAYBOOK.md`.

## Claims

1. Claim before work. The claim file records the claimant host, the slice, the tags used, and a started timestamp in UTC.
2. First push wins. A later claimant for the same slice stands down.
3. Heartbeat at least hourly while a claim is live, as a line in the claimant's own host file. Never heartbeat by editing the claim.
4. A claim with no host activity for 2 hours is stale: no push touching the claim's deliverables and no heartbeat line in the claimant's host file. Takeover is a new claim file naming the old one (old holder, time, reason); it resumes from the recorded steps and never restarts finished steps without a stated reason. For a gate claim, the recorded gate log must also have stopped growing; a gate never runs on two hosts at once.
5. The holder never edits the claim. DONE is derived: the deliverables named in the claim exist on the branch. The finishing push contains the deliverables (and, for gates, the receipt). A claim that ends in failure gets a failure verdict as a new file naming the claim, with the reason.
6. A claim file is written once and never edited, terminal or not. Supersede with a new claim naming the old one.
7. Take the claim to the end without stopping to ask permission. Decide inside the claim with `material/decision-ladder.md`, record the rung, and continue. The owner is asked only at the ladder's top rung, and opening a PR stays owner-approved.
8. Before starting a claimed item, check it against the WON'T DO shapes: already done, already claimed and live, structurally unable to finish, only stale paperwork. On a match, refuse it, record the refusal as a new WON'T DO claim naming the stale record, with the evidence, and take other work. Never edit or delete the stale record.

## Gates

- One gate per host at a time, serialized behind the host's local build lock. Two hosts may gate different branches in parallel.
- Run the detached preflight (tidy check at the exact tip) before claiming a gate. A tidy-dirty tip is a gate repair, not a stop: the gate host applies the tidy change as its own commit and records it.
- On green, the gating host pushes the branch if the job says to and writes the receipt; the receipt's final section is the PR description draft. The gate job file and the claim are never edited: completion is derived from the receipt's existence.
- A gate that executed tests and failed is repaired and retried only as a repair; a run that never executed tests (environment refusal, VM replacement) may be relaunched once.
- Upstream CI is not a gate. The local gate on the head is the proof; the PR body states it.

## Posting a file to this branch

Single-file records go through `pool/bin/post-file.sh`, which commits through the GitHub contents API on the branch's current tip. Workers ADD files. An edit to a shared file by a worker is a workflow defect, not a routine step: if the work needs a shared file changed, the worker posts a new file and lets the single owner regenerate the index, or asks the owner.

The only edits allowed on this branch are single-writer rewrites: a host rewriting its own host file, the gating host rewriting its own receipt, and the phone agent maintaining the scoped list and the generated indexes. For an edit, pass `--base-sha` with the blob SHA the edit is based on; a refusal means the file moved: re-read, re-apply, post again. Multi-file records go through `pool/bin/board-push.sh`, which builds one commit on the fetched tip with a temporary index and pushes it fast-forward: build on the fetched tip, no rebase. A conflict on a new path means another host claimed the same item first, and the later claimant stands down.

Resolve a ticket from its receipt first (`receipts/SOLR-XXXXX.md`), then the live PR head. Never grep-first for a branch by ticket number: branch names do not reliably carry ticket numbers, fork branches are not visible locally, and worktree directories are ad hoc.

Report outcomes, not procedures. A message to the owner states what landed, what failed, or what decision is needed. Never narrate reconciliation, re-reading, or re-applying an edit; do it silently or not at all. A fetch returning a newer tip is routine, not a status update, and is never reported to the owner.

## Evidence

A verdict needs the code paths read with file and line, the git history, and a reproduction attempt wherever the ticket describes testable behavior. A finding ends in a fix commit with the check rerun, a correction to the claim it falsifies, or a recorded non-reproduction with the reason. Gate claims stand on the run's log file and fresh test XML, never on a summary.
