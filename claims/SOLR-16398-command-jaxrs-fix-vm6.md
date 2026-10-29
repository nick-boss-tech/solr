# Claim: SOLR-16398-command-jaxrs fix and regate, round 2 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (see the DONE line below).
- Job or assignment: `gates/SOLR-16398-command-jaxrs.md` (owner direction 2026-10-29: "we need to try to complete 16398 work even if it takes some implementation").
- Slice: the owner-side fix (register the four command resources in `CollectionsHandler.getJerseyResources()`, run rebalance-leaders in-process, add end-to-end V2 tests) and a regate on the new fork commit.
- Capability tags used: gate.
- Started: 2026-10-29T07:06:39Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16398-command-jaxrs-fix@7fa61aa.
- Links: Ticket or parent: SOLR-16398. Category: see `pool/work-map.md`. Siblings: `gates/SOLR-16396-config-jaxrs.md` (DONE; same registration defect, fixed in `a645bff`). Supersedes: the round 1 gate result for `5cc3fc4c` (see `receipts/SOLR-16398.md`).
- Constraints: fork push fast-forward only; no JIRA or GitHub post, no PR. Commit identity Nick Shanin, no trailers.
- Decisions (decision ladder): rebalance-leaders runs the v1 logic in-process, because the Overseer command rejects the top-level message (rung 1: `RebalanceLeadersCmd` requires per-replica params; the v1 action is `REBALANCELEADERS_OP`, which calls `RebalanceLeaders.execute()` locally). Rebalance summary goes into the existing `success` field (rung 4: the other option was a new response field, which changes the API model; left for the owner, see the receipt). Stale split-shard and migrate-docs cases removed from `TestCollectionAPIs` (rung 3: the migration commit states those mapping tests were removed).

Heartbeat: 2026-10-29T07:45:20Z

Result: 2026-10-29T07:45:20Z: gate round 2 PASS at 1a8ef0a (fork fast-forward from 5cc3fc4). Red run confirmed the four routes unserved (4 of 4 new tests fail, "Cannot find API for the path"). Receipt: `receipts/SOLR-16398.md`.

DONE: 2026-10-29T07:45:20Z. Deliverables: `receipts/SOLR-16398.md`; `gates/SOLR-16398-command-jaxrs.md` set to DONE; fork commit `1a8ef0a44b779e0573914c3368ef7fb1f66a9299` on `solr-16398-command-jaxrs`.
