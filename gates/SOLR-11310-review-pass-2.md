# Gate job: SOLR-11310 review pass 2 (elevated docs survive the LTR rerank window)

Status: DONE (GATE GREEN at e38ddec527, vm5, 2026-10-20T14:38Z)

- Branch: solr-11310-submit. Head: `e38ddec5279c465f65a2fa2de22ee9c92e0f5e31` (ls-remote 2026-10-20T14:38Z, unchanged since pass 1).
- Prior gated head: `e38ddec527` (pass 1 green). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:modules:ltr`. Focused class: `org.apache.solr.ltr.TestLTRReRankingPipeline`. Seed: default.
- Diff scope: unchanged from pass 1.

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] diff scope unchanged
- [x] focused run: tests=4, failures=0, errors=0, skipped=0 (fresh XML, `/workspace/gates/logs/vm5/g11310/test-p2.log`)
- [x] test assertions read against the window (doc 1 first, doc 0 second)
- [x] `forceElevation` path located (QueryElevationComponent, default from config); test does not set it (coverage note)
- [ ] tidy, Error Prone, module check: not rerun this pass (pass 1 notes)

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-11310-submit.md`.
- Receipt section in `receipts/SOLR-11310.md`, pass 2.
