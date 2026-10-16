# Claim: pool-solr-11475-proof-fix-round-1

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-solr-11475-proof-fix-round-1.md`.
- Slice: the whole assignment.
- Capability tags used: implementation, gate.
- Started: 2026-10-16T01:52:00Z (UTC).

Heartbeat: 2026-10-16T01:52:00Z

Scope note: diagnose the OutOfMemoryError fail-before proof on solr-11475-submit at 229947201fd7, make the smallest test-only change that gives an assertion-shaped fail-before, then the full gate at the resulting head. The sign-mismatch readiness blocker recorded in the audit is not decided by this job.
