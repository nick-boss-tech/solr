# Claim: solr-17988-docs-refguide-build-round-2

- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: LIVE.
- Job or assignment: `assignments/solr-17988-docs-refguide-build-round-1.md` (owner-authorized retry of the SOLR-17988 Ref Guide build and link checks).
- Slice: At the pinned docs head `16d3005293d3c6a047277bc586b30e05b6b6ef11`, run `:solr:solr-ref-guide:buildLocalSite`, `:solr:solr-ref-guide:checkSiteLinks`, and `:solr:solr-ref-guide:checkCliDocsUpToDate` in order, using the detached host runner and lock. Do not edit docs or source files, retarget the head, or post PR, Jira, or GitHub text.
- Capability tags used: `npm`, `sweep`. Verified Node.js 24.19.0 and npm 11.17.0. Host Java and Gradle settings are in `/workspace/AGENTS.md` and `hosts/phone-agent-2.md`.
- Started: 2026-10-24T17:07:21Z (UTC).
- Lease: live while work proceeds; heartbeat in the host file if needed. Retry-claim attempts: 1. Idempotency key: `solr-17988-docs-refguide-build-round-2@phone-agent-2`.
- Links: assignment `assignments/solr-17988-docs-refguide-build-round-1.md`; prior claim `claims/solr-17988-docs-refguide-build-round-1.md`; prior result `reports/solr-17988-docs-refguide-build-round-1.md`; retry result `reports/solr-17988-docs-refguide-build-round-2.md`; docs branch `solr-17988-docs` at the pinned SHA; host lock `/workspace/tools/.gradle-phone-agent-2.lock`.
- Decision ladder: rung 3. The owner-authorized retry instruction and the assignment specify the exact task names and pinned head; run those tasks without choosing a substitute or retargeting.
- Supersedes: `claims/solr-17988-docs-refguide-build-round-1.md`, which was HANDED after the ambiguous `buildLocal` invocation returned 1 and the three specified tasks were not run. The previous attempt and report remain unchanged.
- Coordination rule: assignment records are immutable under `WORKFLOW.md`; this retry and its result are recorded in new claim and report files rather than by editing the original assignment.
