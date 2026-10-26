# Claim: SOLR-10928-pr5229-fielded-query-round-2 (vm6)

- Claimant host: vm6, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `gates/SOLR-10928-pr5229-fielded-query-round-2.md`.
- Slice: the whole gate at head `5275653c66edc2610ab90046927d1c30fbe6f0f4`, fixed base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, seeds `10928ABC0001` and `10928ABC0002`.
- Capability tags used: Linux, JDK 21, Python 3, Git, flock.
- Started: 2026-10-26T01:20:00Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: SOLR-10928-pr5229-fielded-query-round-2@5275653c66edc2610ab90046927d1c30fbe6f0f4.
- Links: Ticket or parent: SOLR-10928 / PR 5229 (draft), ticket branch `nick-boss-tech/solr:solr-10928-submit`. Category: gate proof. Siblings: the Windows implementation claim `claims/pr-5229-fielded-query-windows-round-1.md`; prior vm6 receipt `receipts/SOLR-10928.md` proves only `f2001e640a464e8e861ee3c202821b6dcc4bd497`. Supersedes: none.

Pre-claim: tidy preflight at the head, `/workspace/gates/logs/vm6/preflight/SOLR-10928-pr5229-fielded-query-round-2`, exit 0 (tidy clean at `5275653c66`), run under `/workspace/tools/.gradle-vm6.lock`.
Host state at claim: `/` has 2.6G free, inodes 26% free. Gradle home is 2.6G. The run builds two Solr worktrees on the same volume; if `/` drops under 2 GB, the run stops and this claim records it.
Build lock: `/workspace/tools/.gradle-vm6.lock` through `SOLR_GATE_LOCK`.
Runner: `/workspace/gates/logs/vm6/SOLR-10928-pr5229-fielded-query-round-2/run-fielded-query-proof.sh`, run detached against the clone `/workspace/solr`. Log: `/workspace/gates/logs/vm6/SOLR-10928-pr5229-fielded-query-round-2/runner.log`. Evidence directory: under `/tmp/solr-10928-fielded-proof.*`.

Heartbeat: 2026-10-26T01:20:00Z

Result: pending; the gate is running.
