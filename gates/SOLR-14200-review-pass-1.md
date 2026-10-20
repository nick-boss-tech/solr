# Gate job: SOLR-14200 review pass 1 (manual shards and empty shard lists)

Status: HANDED BACK (implementation: silent empty result in SignificantTermsStream on a missing manual shard entry; ArithmeticException with no clear message in DeepRandomStream)

- Branch: solr-14200-submit. Head: `10991af9bdac7ec464ac615de98e39d17b384d69` (ls-remote 2026-10-20T15:04Z).
- Prior gated head: `10991af9bd` (receipt). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:solrj-streaming`. Focused class: `org.apache.solr.client.solrj.io.stream.StreamingTest`. Seed: default.
- Diff scope against `14c7aac0d1`: TupleStream, ParallelStream, SqlStream, StreamingTest, one changelog.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope read
- [x] compile `:solr:solrj-streaming:compileJava` rc=0
- [x] focused run: StreamingTest tests=37, skipped=1, failures=0, errors=0 (fresh XML)
- [x] finding: SignificantTermsStream empty result (MAJOR)
- [x] finding: DeepRandomStream division by zero (MINOR)
- [ ] changelog re-parse, tidy, Error Prone, module check: not run this pass

## Deliverable

- Pass 1 result and findings in `reports/newdev-critblock-review-round-1/solr-14200-submit.md`.
- Receipt section in `receipts/SOLR-14200.md`, top section, HANDED BACK.
