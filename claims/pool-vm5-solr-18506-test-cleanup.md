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
