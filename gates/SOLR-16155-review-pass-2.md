# Gate job: SOLR-16155 review pass 2 (document values out of update errors)

Status: DONE (GATE GREEN at 0881de1ed6, vm5, 2026-10-20T15:52Z; minor scope note carried from pass 1)

- Branch: solr-16155-submit. Head: `0881de1ed68b847c781abd7ccb56196cdbf15ecd` (ls-remote 2026-10-20T15:52Z, unchanged since pass 1).
- Prior gated head: `0881de1ed6` (pass 1 green). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `JavabinLoaderTest`, `DocumentBuilderTest`, `TolerantUpdateProcessorTest`. Seed: default.
- Diff scope: unchanged from pass 1.

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] test expectations read against the message shape (`ERROR: [doc=1] Error adding field 'weight'`)
- [x] focused reruns: JavabinLoaderTest 3/3, DocumentBuilderTest 17/17, TolerantUpdateProcessorTest 11/11 (fresh XML, `g16155-test-p2.log`)
- [ ] tidy, Error Prone, module check: not run this round

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-16155-submit.md`.
- Receipt section in `receipts/SOLR-16155.md`, pass 2.
