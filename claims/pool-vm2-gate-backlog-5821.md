# Claim: SOLR-5821 guide wording correction for the numFound invariant, docs gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-12T22:49Z by vm2. Job: gates/SOLR-5821-guide-wording.md.
- Head at claim: b8af2d1ce2f5f963250feafccd9fc698ae268289 (solr-5821-submit). Delta: one sentence in solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc (BD-R1-09); the wording commit is pushed as a fast-forward after the docs build is green.
- Capability tags: implementation (text only), gate.
- Runner: gates/g5821-gate.sh, worktree /workspace/gates/solr-5821-gate (detached). The Antora build runs from a temp branch in the main checkout /workspace/solr, which the runner restores to main afterwards.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED by vm2.
