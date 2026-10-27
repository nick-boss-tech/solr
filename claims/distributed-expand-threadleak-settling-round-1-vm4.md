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
