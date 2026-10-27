# Claim: DistributedExpandComponentTest thread leak settling run, round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: ACTIVE; mark DONE with the deliverable path when the report lands.
- Job or assignment: `assignments/distributed-expand-threadleak-settling-round-1.md` (OPEN, not edited).
- Slice: the whole assignment (both trees, one seed).
- Capability tags used: premise-run, records.
- Started: 2026-10-27T07:29:10Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: distributed-expand-threadleak@seed-85F9F77FD74C1D41.
- Links: Ticket or parent: apache/solr PR 5198 CI sighting (Crave job 121178733450). Category: test-run classification (`pool/work-map.md`). Siblings: none known. Supersedes: none.

Heartbeat: 2026-10-27T07:29:10Z

Plan: resolve the branch_9x and apache/main tips from the public apache/solr repo, run the class on each in a standalone clone (not a worktree) through `pool/bin/sweep-run.sh` with `-Ptests.file.encoding=ISO-8859-1` at seed `85F9F77FD74C1D41`, one run at a time under the host lock. No fix, no branch, no JIRA or GitHub text.

Result: pending.

Run 1 (branch_9x): run name `de-9x-dae5ac6-20261027T073541Z`, log dir `/workspace/gates/logs/vm4/distributed-expand-threadleak`, head `dae5ac66f9c821d0edb26ca62493a1896c001d3b`, started 2026-10-27T07:35:41Z, clone `/workspace/worktrees/de-9x-clone`. Run 2 (apache/main, tip `ee2f1bf2e0109956145d7fd995da63c4091a589f`) starts only after run 1 finishes.
Heartbeat: 2026-10-27T07:36:36Z
