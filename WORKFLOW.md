# Workflow: claims, gates, posting

This branch is an open pool, not addressed mail. Any agent that polls it may take any assignment it is capable of running. There is no coordinating agent: hosts coordinate through claims, gate files, and reports on this branch, and the owner (Nick Shanin) answers only questions no rule settles. The order of operations is in `START-HERE.md`.

## Invariants

- Authorship on every commit is Nick Shanin, with no attribution trailers. Host identity lives in claim files and commit messages. Factory commits land in apache/solr PRs; a host name as author would be a defect there.
- A handoff is a file on this branch. Chat messages and local notes are not handoffs.
- Push only as a fast-forward: fetch, rebase, push. Never force. Never reset a worktree that holds unpushed commits.
- Work branches go to the fork (nick-boss-tech/solr) when they first have a commit, under a name carrying the ticket key. Creating a branch is not a PR and needs no approval.
- Edits to our own posted PR bodies and comments are allowed and should be made at once when they are wrong; opening a PR, posting a new comment, or filing or commenting on Jira needs the owner's approval first.

## Folders

- `assignments/`: the pool. Every assignment names its capability tags, deliverables, and rules.
- `claims/`: one claim file per assignment or slice.
- `gates/`: one file per gate job: ticket, branch, head, module, focused classes, seed, proof shape, claiming host, runner and log paths, step checklist.
- `receipts/`: one file per ticket with the verified gate state at the live tip, written by the host that ran the gate as its finishing step.
- `reports/`, `jira/`, `material/`: deliverables, comment drafts and the scoped list, shared state.
- `hosts/`: one file per host, its capabilities and resource record.

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
3. Heartbeat at least hourly while a claim is live.
4. A claim with no heartbeat for 2 hours is stale. Takeover appends a line (old holder, time, reason) and resumes from the recorded steps; it never restarts finished steps without a stated reason. For a gate claim, the recorded gate log must also have stopped growing; a gate never runs on two hosts at once.
5. The holder marks the claim DONE with the deliverable paths in the same push sequence as the deliverable. A claim that ends in failure says so, with the reason.
6. Terminal claims (DONE, WON'T DO) are never edited. Supersede with a new claim naming the old one.
7. Take the claim to the end without stopping to ask permission. Decide inside the claim with `material/decision-ladder.md`, record the rung, and continue. The owner is asked only at the ladder's top rung, and opening a PR stays owner-approved.
8. Before starting a claimed item, check it against the WON'T DO shapes: already done, already claimed and live, structurally unable to finish, only stale paperwork. On a match, refuse it, record the refusal with the evidence, clear the stale record in the same push, and take other work.

## Gates

- One gate per host at a time, serialized behind the host's local build lock. Two hosts may gate different branches in parallel.
- Run the detached preflight (tidy check at the exact tip) before claiming a gate. A tidy-dirty tip is a gate repair, not a stop: the gate host applies the tidy change as its own commit and records it.
- On green, the gating host pushes the branch if the job says to, writes the receipt, and marks the gate file and the claim DONE. The receipt's final section is the PR description draft.
- A gate that executed tests and failed is repaired and retried only as a repair; a run that never executed tests (environment refusal, VM replacement) may be relaunched once.
- Upstream CI is not a gate. The local gate on the head is the proof; the PR body states it.

## Posting a file to this branch

Single-file records go through `pool/bin/post-file.sh`, which commits through the GitHub contents API on the branch's current tip. For an edit, pass `--base-sha` with the blob SHA the edit is based on; a refusal means the file moved: re-read, re-apply, post again. Multi-file atomic changes go through a normal git push with fetch and rebase first. Shared files that collect sections are appended to, never rewritten from a stale base.

## Evidence

A verdict needs the code paths read with file and line, the git history, and a reproduction attempt wherever the ticket describes testable behavior. A finding ends in a fix commit with the check rerun, a correction to the claim it falsifies, or a recorded non-reproduction with the reason. Gate claims stand on the run's log file and fresh test XML, never on a summary.
