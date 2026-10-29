# Claim: SOLR-16398-command-jaxrs fix and regate, round 2 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `gates/SOLR-16398-command-jaxrs.md` (owner direction 2026-10-29: "we need to try to complete 16398 work even if it takes some implementation").
- Slice: the owner-side fix (register the four command resources in `CollectionsHandler.getJerseyResources()`, add end-to-end V2 tests) and a regate on the new fork commit.
- Capability tags used: gate.
- Started: 2026-10-29T07:06:39Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16398-command-jaxrs-fix@7fa61aa.
- Links: Ticket or parent: SOLR-16398. Category: see `pool/work-map.md`. Siblings: `gates/SOLR-16396-config-jaxrs.md` (DONE; same registration defect, fixed in `a645bff`). Supersedes: the round 1 gate result for `5cc3fc4c` (see `receipts/SOLR-16398.md`).
- Constraints: fork push fast-forward only (owner approved pushes for these fixes); no JIRA or GitHub post, no PR. Commit identity Nick Shanin, no trailers.

Heartbeat: 2026-10-29T07:06:39Z

Result: pending.
