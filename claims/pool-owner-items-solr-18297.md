# Claim: pool-owner-items-scrub-implementation-round-1, item 8 (SOLR-18297)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Assignment: `assignments/pool-owner-items-scrub-implementation-round-1.md`, item 8.
- Branch: `solr-18297-jetty-12-1-14`, head `7c07821070` (decided by `reports/owner-items-scrub-2026-10-22.md`).
- Capability tags: implementation, gate.
- Status: DONE. Work: removed `pr-draft/SOLR-18297.md` from the tree; fixed the Error Prone `DirectInvocationOnMock` failure at `CoreReplicationAPITest.java:213` and `:217` that vm3 reproduced on a clean compile (`gates/SOLR-18297-review-pass-1-vm3.md`); regenerated module lockfiles for Jetty 12.1.14 (14 modules were pinned to 12.1.12). Gate records: `gates/SOLR-18297-owner-items.md`. Heads: `c44fc60601e`, `73f269e99a0`.
- Started: 2026-10-22T14:59:19Z (UTC).

Heartbeat: 2026-10-22T14:59:19Z

Result: 2026-10-22T16:40:00Z: DONE on the gate records above.
