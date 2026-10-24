# Claim: pr-5204-picocli-review-vm4-round-1-phase-r

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Job or assignment: `assignments/pr-5204-picocli-review-implementation-and-gate-vm4-round-1.md`, the initial slice: Phase R (review reproduction), the BATS capability setup, and Phase I (fork implementation) only if the finding is confirmed.
- Not claimed here: the executable proof phase (Phase G, tags `gate, bats`). That needs its own claim after the BATS prerequisites are recorded in `hosts/vm4.md`.
- Ticket: SOLR-18566, PR https://github.com/apache/solr/pull/5204, reviewed head `7eb288a6f5777770d4472fc2c550b66e0d1cec12`, merge base `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Capability tags used: review, implementation, records, draft.
- Started: 2026-10-24T20:40:39Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5204-picocli-review-vm4-round-1-phase-r@2026-10-24T20:40:39Z.
- Constraints: no public PR or JIRA comment, no PR creation, no push to the foreign branch `jaykay12/solr:SOLR-18566-picocli-bats-fixed`, no upstream write, no force push. The #5214 packet is not touched. Commit as Nick Shanin with no trailers.
- Planned fork branch if Phase I is needed: `solr-18566-pr5204-cli-review`, created only after confirming it is absent (it was absent at 2026-10-24T20:40:39Z).
- Decision ladder: rung 1 (source of `bats_helper.bash`, `bin/solr`, `build.gradle`) and rung 2 (BATS probe under `SOLR_PICOCLI=false`) for the selector finding.
- Deliverables: `reports/pr-5204-cli-review-implementation-vm4-round-1.md` (only if Phase I runs), and the Phase R findings included in that report or in `reports/pr-5204-phase-r-vm4-round-1.md`.

Heartbeat: 2026-10-24T20:40:39Z
