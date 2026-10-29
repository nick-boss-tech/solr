# Claim: SOLR-16396-config-jaxrs gate, round 1 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE; mark DONE with the receipt path when the gate lands.
- Job or assignment: `gates/SOLR-16396-config-jaxrs.md` (head `6f1577221646dec1dd4ada8e409faa552d4f2913`).
- Slice: the whole gate job, steps 1 to 6.
- Capability tags used: gate.
- Started: 2026-10-29T01:11:37Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16396-config-jaxrs-gate@6f1577221646.
- Links: Ticket or parent: SOLR-16396. Category: see `pool/work-map.md`. Siblings: `gates/SOLR-16398-command-jaxrs.md`, `gates/SOLR-16398-core-commands.md` (open, not claimed here). Supersedes: none.
- Preflight: `/workspace/gates/logs/vm6/preflight/SOLR-16396-config-jaxrs/` exit 3 (tidy would change 3 files: `ModifyConfigApi.java`, `SolrConfigHandler.java`, `V2ConfigAPIMappingTest.java`). Tidy patch: `tidy.patch` in that directory. Repair under WORKFLOW.md "Gates": the gate host applies it as its own commit and records it.
- Decision ladder: rung 3, the job's step 2 says a tidy-only diff gets the standard tidy commit and retarget. Recorded before the run. The tidy commit stays local; the job does not say to push the branch, so the fork branch is not pushed. The receipt will name the local head.
- Constraints: no JIRA or GitHub post, no PR, no push to the contributor branch in this claim. Commit identity Nick Shanin, no trailers.

Heartbeat: 2026-10-29T01:11:37Z

