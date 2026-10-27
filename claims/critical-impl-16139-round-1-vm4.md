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

Run (premise setup): `s16139-assemble-ee2f1bf-20261027T100633Z`, `:solr:packaging:assemble` on apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f` (standalone clone `/workspace/worktrees/s16139-clone`), started 2026-10-27T10:06:33Z with `env -u NODE_ENV`. Logs `/workspace/gates/logs/vm4/critical-impl-16139/`.
Heartbeat: 2026-10-27T10:06:49Z

Premise result (2026-10-27T10:28:46Z): REPRODUCES on main ee2f1bf2e0. Nested terms facet on SortableTextField gives avg/sum 0.0 and min null; the same nested stats on a string field are correct (15, 40, 7). Report `reports/critical-impl-16139-premise-round-1.md`. Solr process 72898 is a zombie (PID 1 does not reap orphans); port 8986 closed, nothing running. Step 2 (implementation on solr-16139-submit) next.
Heartbeat: 2026-10-27T10:28:46Z
