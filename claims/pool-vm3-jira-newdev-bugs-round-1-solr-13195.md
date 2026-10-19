# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-13195 (item 6 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE. No branch (not reproduced over HTTP). Slice: item 6 only, SOLR-13195. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 6.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T18:13:00Z (UTC).

Heartbeat: 2026-10-19T18:13:00Z

Heartbeat: 2026-10-19T18:52:00Z

Heartbeat: 2026-10-19T19:45:00Z

## Result (vm3, 2026-10-19)

- Base: `c8ab71430f`. No branch pushed.
- Over HTTP, the ticket's request (`distrib=true&facet=on&facet.field=...`, no `shards`) does not produce the NPE on the base. The standalone node returns 400: the implicit `shards` value (the node's own URL) is rejected by the shards allowlist.
- In-process callers with no HTTP request (`TestHarness`, `EmbeddedSolrServer`) hit an NPE in `HttpShardHandler.prepDistributed` at the `req.getHttpSolrCall().getThisNodeUrl()` call. Nick should decide whether that path needs a fix. It is not the HTTP symptom in the ticket, so it was not fixed here.
- Test runs: a harness-only test (SimpleFacetsTest) reproduced the in-process NPE. A Jetty HTTP test (DistribFacetWithoutShardsTest) got the 400 at the base. Setup iterations are recorded in the test history, not a gate.
- Verdict: does not reproduce over HTTP. No branch. Open question for Nick on the in-process NPE.
- Receipt: `receipts/SOLR-13195.md` on coordination.

Heartbeat: 2026-10-19T20:40:00Z

DONE: 2026-10-19. Deliverables: this claim file (result section), `receipts/SOLR-13195.md`. No branch.

