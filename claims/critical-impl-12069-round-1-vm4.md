# Claim: SOLR-12069 q.op on the standard query parser, implementation round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: ACTIVE. Step 1 premise run first; implementation only if the premise reproduces.
- Job or assignment: `assignments/critical-impl-12069-round-1.md` (OPEN, not edited). Source triage: `reports/critical-implementation-triage-round-1.md`, rank 2.
- Slice: the whole assignment (premise, narrow fix on `solr-12069-submit`, gate request at the stop point).
- Capability tags used: implementation, premise-run.
- Started: 2026-10-27T09:04:16Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: critical-impl-12069@4b6f91da3b.
- Links: Ticket: SOLR-12069. Category: implementation (`pool/work-map.md`). Siblings: none known. Supersedes: none.

Heartbeat: 2026-10-27T09:04:16Z

Plan: reproduce on the techproducts example with `q=a b` under `q.op=AND` and the default, at `-Ptests.workDir` on local disk, through `pool/bin/sweep-run.sh`. If the premise fails, write the premise report and stop with no branch. If it reproduces, implement narrowly on `solr-12069-submit`, add tests on SolrTestCase or SolrCloudTestCase, push the branch, and request the gate with `pool/bin/request-gate.py`. No PR, JIRA, or GitHub comment.

Result: pending.
