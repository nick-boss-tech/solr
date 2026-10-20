# Gate job: SOLR-15326 review pass 3 (export sort unique-key tie-breaker)

Status: DONE (GATE GREEN at dab6a46631, vm5, 2026-10-20T15:35Z; minor notes recorded)

- Branch: solr-15326-submit. Head: `dab6a466316940ea643c22dd9e56c9a63c0cb7fb` (ls-remote 2026-10-20T15:33Z, unchanged across passes 1 to 3).
- Prior gated head: `dab6a46631` (passes 1 and 2 green). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:solrj-streaming`. Focused class: `org.apache.solr.client.solrj.io.stream.StreamingTest`. Seed: random (not pinned).

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] diff scope unchanged
- [x] focused run: StreamingTest tests=35, skipped=1, failures=0, errors=0 (`g15326-test-p3.log`)
- [x] both `useAlias` branches covered across passes 1 to 3 (skip counts 1, 2, 1)
- [ ] tidy, Error Prone, module check: not run this round (receipt covers them)

## Deliverable

- Pass 3 result in `reports/newdev-critblock-review-round-1/solr-15326-submit.md`.
- Receipt section in `receipts/SOLR-15326.md`, pass 3.
