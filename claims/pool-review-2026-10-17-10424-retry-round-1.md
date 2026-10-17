# Claim: SOLR-10424 V2 id-less update check, retry (vm2)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-review-2026-10-17-10424-retry-round-1.md (posted d74dcb7ec7).
- Slice taken: the whole retry (corrected scratch test, both legs, check file). Test change is scratch only, not committed to the fork.
- Capability tags used: implementation, gate.
- Branch solr-10424-submit at 5b087b1a389c892850401f79154824607cb338ac (ls-remote at claim time). Upstream main 4b5b922383.
- Scratch test: /workspace/gates/10424-retry/TechproductsV2IdlessUpdateTest.java.

Heartbeat: 2026-10-17T05:20Z (claim).
- 2026-10-17T05:35Z result: INCONCLUSIVE, escalated. Head V2 request fails with missing uniqueKey id; main V2 request accepted but count 0; legacy requests fail with no live SolrServers on both legs. Report: reports/2026-10-16-pr-batch/SOLR-10424-check.md.
- Status: DONE (INCONCLUSIVE, escalated, 2026-10-17T05:35Z).
- 2026-10-17T06:37Z result: CONFIRMED on the second retry (the first retry's INCONCLUSIVE was caused by two test bugs, fixed). Head V2 HTTP 400 missing uniqueKey id; main V2 adds a UUID id; legacy adds a UUID id on both. Report: reports/2026-10-16-pr-batch/SOLR-10424-check.md. Escalated for the fix shape.
