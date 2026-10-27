# Claim: PR 5112 target header follow-up, Linux gate, round 1

- Claimant host: vm5, matching the Host id line in `hosts/vm5.md`.
- Status: ACTIVE; a receipt at the exact head is the completion record.
- Job or assignment: `gates/pr-5112-target-header-followup-round-1.md`.
- Slice: the whole gate job (preflight tidy, focused suites, neighbor suites, `check -x test`, and the fail-before negative proof on a separate checkout of `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`).
- Capability tags used: gate, premise-run.
- Started: 2026-10-27T16:35:29Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: pr-5112-target-header-followup-round-1@3d762be30f6b8c2805c3edcfc48446488265a806.
- Links: Ticket or parent: fork branch `nick-boss-tech/solr:solr-18537-submit`, implementation head `3d762be30f6b8c2805c3edcfc48446488265a806`, original PR head `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85` (apache/solr PR 5112). Implementation claim: `claims/pr-5112-followup-windows-round-1.md`. Category: review. Siblings: none known. Supersedes: none.

Decision rung: rung 1 for scope. The gate job names any capable Linux gate host; vm5 holds gate and premise-run. The host lock `/workspace/tools/.gradle-vm5.lock` serializes the gate behind any other build on this host. The earlier vm5 claim `claims/gradle-help-tarball-review-round-1-vm5.md` has no heartbeat since 2026-10-27T07:44:44Z and no running build; it is paused and its review resumes after this gate.

Method note: gate checkouts are full detached trees under `/workspace/gates/pr-5112/`, not the pool worktree, because sweep-run.sh refuses check tasks in a git worktree. Logs and JUnit XML go to `/workspace/gates/logs/vm5/pr5112-linux/`.

Heartbeat: 2026-10-27T16:35:29Z

Result: none yet.
