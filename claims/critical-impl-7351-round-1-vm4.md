# Claim: SOLR-7351 MoreLikeThis with date fields, premise run then implementation, round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: ACTIVE. Step 1 premise run first; implementation only if the premise reproduces.
- Job or assignment: `assignments/critical-impl-7351-round-1.md` (OPEN, not edited). Source triage: `reports/critical-implementation-triage-round-1.md`, rank 8.
- Slice: the whole assignment (premise, narrow fix on `solr-7351-submit`, gate request at the stop point).
- Capability tags used: implementation, premise-run.
- Started: 2026-10-27T12:05:09Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: critical-impl-7351@151a8c2b13.
- Links: Ticket: SOLR-7351. Category: implementation (`pool/work-map.md`). Siblings: SOLR-16139, SOLR-12069 (both vm4, closed). Supersedes: none.

Heartbeat: 2026-10-27T12:05:09Z

Plan: standalone shallow clone of apache/solr main, `:solr:packaging:assemble` through `pool/bin/sweep-run.sh` with `env -u NODE_ENV`. Then on the packaged Solr, run an MLT request (`/mlt` or `mlt=true`) with a TrieDateField (or DatePointField) in the fields and check whether the interesting-terms list is empty. If the premise fails, write the premise report and stop with no branch. If it reproduces, implement narrowly on `solr-7351-submit`, add tests on SolrTestCase or SolrCloudTestCase, push, and request the gate with `pool/bin/request-gate.py`. No PR, JIRA, or GitHub comment.

Result: pending.
