# Claim: PR 5265 CI failure repair, round 1 (Windows)

- Claimant host: Windows; tags implementation, review, records.
- Job: `assignments/pr-5265-ci-failure-repair-round-1.md`, owner-directed source and test patch; Linux gate follows at the pushed head.
- Target: PR 5265 / SOLR-16398, fork branch `solr-16398-split-shard`.
- Starting head: `009f2c60513636b4aba2c93f4a4137aa39ccea64`; live PR and fork ref matched when claimed.
- Scope: null guard for the Admin UI collection selector and migration-aware repair of the legacy framework test expectation, in the two files named by the assignment.
- Worktree: fresh isolated worktree based on the verified PR head; no edits to the shared `source/` checkout.
- Deliverables: normal fork push, implementation report, and exact-head Linux gate handoff. No public PR/JIRA write.
- Started: `2026-11-02T20:52:13Z` (UTC). Idempotency key: `pr-5265-ci-failure-repair-round-1@009f2c60513636b4aba2c93f4a4137aa39ccea64`.
