# Gate job: SOLR-15326 review pass 2 (export sort unique-key tie-breaker)

Status: DONE (GATE GREEN at dab6a46631, vm5, 2026-10-20T15:30Z; minor notes recorded)

- Branch: solr-15326-submit. Head: `dab6a466316940ea643c22dd9e56c9a63c0cb7fb` (ls-remote 2026-10-20T15:28Z, unchanged since pass 1).
- Prior gated head: `dab6a46631` (pass 1 green). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:solrj-streaming`. Focused class: `org.apache.solr.client.solrj.io.stream.StreamingTest`. Seed: random (not pinned); this run took the `useAlias` branch.
- Diff scope: unchanged from pass 1.

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] test assertion read (substring `id asc`, weak; minor finding)
- [x] export handler sort: the test opens and reads the stream, so the appended clause is accepted
- [x] focused run: StreamingTest tests=35, skipped=2, failures=0, errors=0 (`g15326-test-p2.log`)
- [x] skip count explained: testParallelExceptionStream is @Ignore; testDaemonTopicStream is Assume(!useAlias), useAlias from random()
- [ ] tidy, Error Prone, module check: not run this round

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-15326-submit.md`.
- Receipt section in `receipts/SOLR-15326.md`, pass 2.
