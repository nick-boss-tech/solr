# Gate job: SOLR-11310 review pass 1 (elevated docs survive the LTR rerank window)

Status: DONE (GATE GREEN at e38ddec527, vm5, 2026-10-20T14:34Z)

- Branch: solr-11310-submit. Head: `e38ddec5279c465f65a2fa2de22ee9c92e0f5e31` (ls-remote 2026-10-20T14:21Z, unchanged).
- Prior gated head: `e38ddec527` (the receipt's gated head, DONE 2026-10-03). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:core` (compile), `:solr:modules:ltr` (compile, test). Focused class: `org.apache.solr.ltr.TestLTRReRankingPipeline`. Seed: default.
- Diff scope against `14c7aac0d1`: `solr/core/src/java/org/apache/solr/search/ReRankCollector.java` (-2), `solr/modules/ltr/src/test/org/apache/solr/ltr/TestLTRReRankingPipeline.java` (+53), changelog (+7).

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] diff scope confirmed
- [x] step 0 changelog: `type: fixed`, author and link present (read at the head)
- [ ] step 1 tidy: not rerun this pass; the head's tidy commit (e38ddec527) was gated in the receipt
- [x] step 2 compile: `:solr:core:compileJava :solr:modules:ltr:compileTestJava`, rc=0
- [ ] step 2 Error Prone: not rerun this pass
- [x] step 3 proof leg: the round 35 A/B (receipt) shows the new test failing at base; cited, not rerun
- [x] step 4 focused run: `TestLTRReRankingPipeline` tests=4 failures=0 errors=0 skipped=0 (fresh XML)
- [ ] step 5 module check: not rerun this pass

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-11310-submit.md`.
- Receipt section in `receipts/SOLR-11310.md`, top section (pass 1).
