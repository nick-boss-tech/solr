# Claim: SOLR-7351 MoreLikeThis with date fields, premise run then implementation, round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: NEEDS-OWNER. Premise differs from the assignment; work stopped, no branch.
- Job or assignment: `assignments/critical-impl-7351-round-1.md` (OPEN, not edited). Source triage: `reports/critical-implementation-triage-round-1.md`, rank 8.
- Slice: the whole assignment (premise, narrow fix on `solr-7351-submit`, gate request at the stop point).
- Capability tags used: implementation, premise-run.
- Started: 2026-10-27T12:05:09Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: critical-impl-7351@151a8c2b13.
- Links: Ticket: SOLR-7351. Category: implementation (`pool/work-map.md`). Siblings: SOLR-16139, SOLR-12069 (both vm4, closed). Supersedes: none.

Heartbeat: 2026-10-27T12:05:09Z

Plan: standalone shallow clone of apache/solr main, `:solr:packaging:assemble` through `pool/bin/sweep-run.sh` with `env -u NODE_ENV`. Then on the packaged Solr, run an MLT request (`/mlt` or `mlt=true`) with a TrieDateField (or DatePointField) in the fields and check whether the interesting-terms list is empty. If the premise fails, write the premise report and stop with no branch. If it reproduces, implement narrowly on `solr-7351-submit`, add tests on SolrTestCase or SolrCloudTestCase, push, and request the gate with `pool/bin/request-gate.py`. No PR, JIRA, or GitHub comment.

Result: pending.

Run (premise setup): `s7351-assemble-ee2f1bf-20261027T121031Z`, `:solr:packaging:assemble` on apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f` (standalone clone `/workspace/worktrees/s7351-clone`), started 2026-10-27T12:10:31Z with `env -u NODE_ENV`. Logs `/workspace/gates/logs/vm4/critical-impl-7351/`.
Heartbeat: 2026-10-27T12:10:48Z

Premise result (run `s7351-assemble-ee2f1bf-20261027T121031Z`, main `ee2f1bf2e0`): does NOT reproduce as written. The expected empty interesting-terms list does not appear. MLT on a DatePointField (`last_modified`) and on a TrieDateField (`tdate_mlt`) throws `Invalid Date String:'1577836800000'`; the text control returns alpha, beta, gamma, delta. Report: `reports/critical-impl-7351-premise-round-1.md`. Solr 8987 killed and clone removed; logs kept.

Question for Nick: (a) narrow fix to the MLT date-term path for the observed exception, or (b) close as a premise mismatch until the ticket's exact symptom is confirmed. No branch pushed.

Heartbeat: 2026-10-27T12:33:31Z

