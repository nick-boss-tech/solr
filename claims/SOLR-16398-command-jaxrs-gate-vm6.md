# Claim: SOLR-16398-command-jaxrs gate, round 1 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (see the DONE line below).
- Job or assignment: `gates/SOLR-16398-command-jaxrs.md` (head `5cc3fc4c0019a84a2875c15920664dbe93a9ee4d`).
- Slice: the whole gate job, steps 1 to 6.
- Capability tags used: gate.
- Started: 2026-10-29T05:59:19Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16398-command-jaxrs-gate@5cc3fc4c0019.
- Links: Ticket or parent: SOLR-16398. Category: see `pool/work-map.md`. Siblings: `gates/SOLR-16398-core-commands.md` (open, not claimed here), `gates/SOLR-16396-config-jaxrs.md` (DONE; same registration defect found there). Supersedes: none.
- Preflight: `/workspace/gates/logs/vm6/preflight/SOLR-16398-command-jaxrs/` exit 3. Tidy would change one file, `solr/core/src/java/org/apache/solr/handler/admin/api/SplitShard.java` (`tidy.patch` in that directory). Repair under WORKFLOW.md "Gates": applied as its own commit in the standalone clone and recorded in the receipt.
- Pre-gate finding (code, not yet run): `CollectionsHandler.getJerseyResources()` does not list `SplitShard`, `MoveReplica`, `RebalanceLeaders` or `MigrateDocs`. The same registration gap broke the config APIs on SOLR-16396. The gate's focused test covers only `ModifyCollection`, so the listed steps may not catch it.
- Constraints: no JIRA or GitHub post, no PR. Fixes return to the owner side through the job file. Commit identity Nick Shanin, no trailers.

Heartbeat: 2026-10-29T05:59:19Z

Result: 2026-10-29T06:24:01Z: gate steps 1 to 6 PASS at 5cc3fc4c with repair commit 7fa61aa. Four command resources are not registered; see the receipt.

DONE: 2026-10-29T06:24:01Z. Deliverables: `receipts/SOLR-16398.md`; `gates/SOLR-16398-command-jaxrs.md` set to DONE.
