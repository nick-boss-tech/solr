# Claim: SOLR-18506 TestThinCache cleanup after assertion failure

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: ACTIVE. Mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/pool-solr-18506-cache-cleanup-vm5.md`.
- Slice: the whole assignment.
- Capability tags used: implementation, gate.
- Started: 2026-10-23T16:28:19Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: SOLR-18506-test-cleanup@77c019e1ff0e922e3c2350379dece841651b1d44; a takeover resumes from this claim's recorded steps.
- Links: Ticket or parent: SOLR-18506, PR #5029 branch `solr-18506-submit`. Category: test quality, with search and query components as the production category. Siblings: `reports/teardown-objecttracker-leak.md`. Supersedes: none.

Heartbeat: 2026-10-23T16:28:19Z

Result: (pending)

DONE: (pending)

Heartbeat: 2026-10-23T16:35:01Z

Result: 2026-10-23T16:35:01Z: BLOCKED before any Gradle run. The host runner (`pool/bin/sweep-run.sh`, inode guard) refused to start: `/workspace` is an NFS mount that reports 0 total inodes, so `inode-guard.py` reads the counters as invalid (exit 4, no files deleted). Candidate: local branch `solr-18506-test-cleanup` at a local commit on top of 77c019e1ff0e (try-with-resources in `TestThinCache.testSimple`), not pushed because the gate has not run. Tidy, Error Prone, the proof leg, the focused run and the module check are not done.

ENDED: 2026-10-23T16:35:01Z: the host cannot finish until the inode guard has a valid counter source for `/workspace` or the owner gives explicit direction. Handed back per WORKFLOW.md; the assignment is set to HANDED.

Heartbeat: 2026-10-23T16:40:47Z

Resumed: 2026-10-23T16:40:47Z. Owner direction (Nick, 2026-10-23): push the candidate branch, check whether the inode counters can be fixed, and otherwise run a one-off. The candidate branch `solr-18506-test-cleanup` is pushed at 5262d15678d0. No client-side fix exists: `/workspace` is the NFS export, and the Gradle home is on it, so the counters stay invalid. Root `/` reports 9% of inodes used. One-off: Gradle runs directly under `/workspace/tools/.gradle-vm5.lock` with `flock`, bypassing `inode-guard.py` for this claim only. Each run is logged under `gates/logs/vm5/SOLR-18506-test-cleanup/`.

Rung: 3 of the decision ladder (the assignment and the owner's direction settle it). The ObjectTracker proof uses a temporary edit that is reverted before the final runs.

Result: 2026-10-23T17:08:00Z: GATE GREEN for candidate `solr-18506-test-cleanup` at 5262d15678d0f21ec1b0a9cfcbf968252a0de5aa. Receipt: `receipts/SOLR-18506.md`. Gate file: `gates/SOLR-18506-test-cleanup-vm5.md`.

DONE: 2026-10-23T17:08:00Z. Deliverables: `reports/solr-18506-test-cleanup-vm5.md`, `gates/SOLR-18506-test-cleanup-vm5.md`, `receipts/SOLR-18506.md` (new top section), candidate branch `solr-18506-test-cleanup`.
