# Claim: SOLR-16139 JSON stat facet over SortableTextField, premise run then implementation, round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: ACTIVE. Step 1 premise run first; implementation only if the premise reproduces.
- Job or assignment: `assignments/critical-impl-16139-round-1.md` (OPEN, not edited). Source triage: `reports/critical-implementation-triage-round-1.md`, rank 3.
- Slice: the whole assignment (premise, narrow fix on `solr-16139-submit`, gate request at the stop point).
- Capability tags used: implementation, premise-run.
- Started: 2026-10-27T09:59:26Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: critical-impl-16139@012f513a29.
- Links: Ticket: SOLR-16139. Category: implementation (`pool/work-map.md`). Siblings: SOLR-12069 premise (closed, vm4). Supersedes: none.

Heartbeat: 2026-10-27T09:59:26Z

Plan: standalone shallow clone of apache/solr main, `:solr:packaging:assemble` through `pool/bin/sweep-run.sh` with `env -u NODE_ENV`, then index a SortableTextField and run a nested avg stat facet over JSON facets on the packaged Solr. If the premise fails, write the premise report and stop with no branch. If it reproduces, implement narrowly on `solr-16139-submit`, add tests on SolrTestCase or SolrCloudTestCase, push, and request the gate with `pool/bin/request-gate.py`. No PR, JIRA, or GitHub comment.

Result: pending.
