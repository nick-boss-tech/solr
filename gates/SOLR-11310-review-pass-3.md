# Gate job: SOLR-11310 review pass 3 (elevated docs survive the LTR rerank window)

Status: DONE (GATE GREEN at e38ddec527, vm5, 2026-10-20T14:43Z; proof leg red at base, as expected)

- Branch: solr-11310-submit. Head: `e38ddec5279c465f65a2fa2de22ee9c92e0f5e31` (ls-remote 2026-10-20T14:42Z, unchanged across passes 1 to 3).
- Prior gated head: `e38ddec527` (passes 1 and 2 green). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:modules:ltr`. Focused class: `org.apache.solr.ltr.TestLTRReRankingPipeline`. Seed: default.
- Diff scope: `solr/core/src/java/org/apache/solr/search/ReRankCollector.java` (-2), the LTR test (+53), changelog (+7).

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] diff scope unchanged
- [x] changelog `type: fixed`, author, link present
- [x] step 2 compile (pass 1): rc=0
- [x] step 3 proof leg, rerun: at base `ReRankCollector`, `testBoostedDocsSurviveLTRRescore` FAILED `expected:<[1]> but was:<[0]>`; log `proof-base.log`; head file restored
- [x] step 4 focused run on head: tests=4, failures=0, errors=0, skipped=0 (`test-p3.log`)
- [ ] tidy, Error Prone, ltr module check: not rerun this round (receipt's hardening gate covers the head)

## Deliverable

- Pass 3 result in `reports/newdev-critblock-review-round-1/solr-11310-submit.md`.
- Receipt section in `receipts/SOLR-11310.md`, pass 3.
