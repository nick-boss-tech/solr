# Claim: pool-vm2-merged-pr-followup-promises-round-1-t

- Claimant host: vm2, matching a file in `hosts/`.
- Status: ENDED (stale since 2026-10-18; superseded by pool-vm2-merged-pr-followup-promises-round-1-u.md at 2026-10-20T11:18:26Z).
- Job or assignment: `assignments/pool-merged-pr-followup-promises-round-1.md` (recurring; this pass re-derives the merged set and updates the ledger in place).
- Slice: the whole assignment, one recurrence pass.
- Capability tags used: review.
- Started: 2026-10-18T14:02:21Z (UTC).

Heartbeat: 2026-10-18T14:02:21Z

Result: 2026-10-18T14:09Z: merged set re-derived from the unauthenticated search API (`repo:apache/solr is:pr is:merged author:nick-boss-tech`, 10 results: #4939, #4953, #4959, #4965, #4995, #5001, #5012, #5027, #5062, #5070), same set as pass s, no new merge since #5062 and #5027 (both closed 2026-10-17). The token in the environment returned "Bad credentials" on the authenticated call, so the read used no token. `gh` is not installed on this host. Ledger unchanged.

DONE: 2026-10-18T14:09Z. Deliverables: `assignments/pool-merged-pr-followup-promises-round-1.md` (status line only).

ENDED: 2026-10-20T11:18:26Z. Stale from 2026-10-18 with no heartbeat since; superseded by the recurring check in claims/pool-vm2-merged-pr-followup-promises-round-1-u.md.
