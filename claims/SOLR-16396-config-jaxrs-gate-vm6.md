# Claim: SOLR-16396-config-jaxrs gate, round 1 (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (see the DONE line below).
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

Heartbeat: 2026-10-29T01:36:15Z (resume after VM restart)

Steps recorded before the restart, job `solr16396-r1-f8ba76d` (log dir `/workspace/gates/logs/vm6/solr16396-r1-f8ba76d/`), head `f8ba76d` (tidy commit on `6f15772`):
- tidy: rc 0, 2026-10-29T01:14:35Z to 01:17:52Z.
- errorprone-compile: rc 0, 01:17:52Z to 01:24:44Z.
- solrj-generation: rc 0, 01:24:44Z to 01:26:09Z.
- focused-tests: interrupted by a VM restart (no rc, no state file). Not counted.

Resume: job `solr16396-r1-f8ba76d-resume` runs focused-tests and module-check at `f8ba76d` after the restart, with the same seed-free command as the job steps. Reason: environment change (VM restart), which the failed-run rule allows; the earlier steps are not rerun.

Result: 2026-10-29T01:43:24Z: gate PASS at `f8ba76d` (tidy rc 0 with commit f8ba76d, Error Prone compile rc 0, SolrJ generation rc 0, focused tests 3 of 3 in job solr16396-r1-f8ba76d-resume, module check rc 0). Receipt `receipts/SOLR-16396.md`.

DONE: 2026-10-29T01:43:24Z. Deliverables: `receipts/SOLR-16396.md`; `gates/SOLR-16396-config-jaxrs.md` set to DONE.
