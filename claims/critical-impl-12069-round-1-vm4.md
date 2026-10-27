# Claim: SOLR-12069 q.op on the standard query parser, implementation round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: DONE. Premise failed; no implementation.
- Job or assignment: `assignments/critical-impl-12069-round-1.md` (OPEN, not edited). Source triage: `reports/critical-implementation-triage-round-1.md`, rank 2.
- Slice: the whole assignment (premise, narrow fix on `solr-12069-submit`, gate request at the stop point).
- Capability tags used: implementation, premise-run.
- Started: 2026-10-27T09:04:16Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: critical-impl-12069@4b6f91da3b.
- Links: Ticket: SOLR-12069. Category: implementation (`pool/work-map.md`). Siblings: none known. Supersedes: none.

Heartbeat: 2026-10-27T09:04:16Z

Plan: reproduce on the techproducts example with `q=a b` under `q.op=AND` and the default, at `-Ptests.workDir` on local disk, through `pool/bin/sweep-run.sh`. If the premise fails, write the premise report and stop with no branch. If it reproduces, implement narrowly on `solr-12069-submit`, add tests on SolrTestCase or SolrCloudTestCase, push the branch, and request the gate with `pool/bin/request-gate.py`. No PR, JIRA, or GitHub comment.

Result: pending.

Run (premise setup): `q12069-assemble-ee2f1bf-20261027T090948Z`, `:solr:packaging:assemble` on apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f` (standalone clone `/workspace/worktrees/q12069-clone`), started 2026-10-27T09:09:48Z, logs `/workspace/gates/logs/vm4/critical-impl-12069/`. Purpose: a packaged Solr to run techproducts for the q=a b premise check.
Heartbeat: 2026-10-27T09:10:05Z

Attempt 1 (q12069-assemble-ee2f1bf-20261027T090948Z): BUILD FAILED rc 1 at `:solr:webapp:js-client:jsClientDownloadDeps` (npm run build exit 1). Cause: the launching shell had NODE_ENV=production, which skips devDependencies; no tests ran. Logs kept in `logs/vm4/critical-impl-12069/attempt-1-npm-node-env/`. Relaunch once with `env -u NODE_ENV`: run `q12069-assemble-retry-ee2f1bf-20261027T092631Z`, started 2026-10-27T09:26:31Z.
Heartbeat: 2026-10-27T09:27:27Z

Result: 2026-10-27T09:49:42Z: premise does NOT reproduce on main ee2f1bf2e0. q.op is applied on the standard parser (AND gives +(+text:a +text:b), 0 hits; OR and default give 1 hit; "ipod black" AND 1 hit vs OR 4). Report `reports/critical-impl-12069-premise-round-1.md`. Solr stopped. No branch, no gate request.

DONE: 2026-10-27T09:49:42Z. Deliverables: reports/critical-impl-12069-premise-round-1.md (premise failed; implementation stopped by assignment rule).
