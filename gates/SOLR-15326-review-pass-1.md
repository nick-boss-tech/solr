# Gate job: SOLR-15326 review pass 1 (export sort unique-key tie-breaker)

Status: DONE (GATE GREEN at dab6a46631, vm5, 2026-10-20T15:25Z; minor notes recorded)

- Branch: solr-15326-submit. Head: `dab6a466316940ea643c22dd9e56c9a63c0cb7fb` (ls-remote 2026-10-20T15:17Z).
- Prior gated head: `dab6a46631` (receipt, 2026-10-10). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:solrj-streaming`. Focused class: `org.apache.solr.client.solrj.io.stream.StreamingTest`. Seed: default.
- Diff scope against `14c7aac0d1`: CloudSolrStream (+69), StreamingTest (+22), changelog (+7).

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope read
- [x] idempotence of the sort append and comparator append (read)
- [x] compile `:solr:solrj-streaming:compileJava` rc=0
- [x] focused run: StreamingTest tests=35, skipped=1, failures=0, errors=0 (fresh XML)
- [x] changelog fields read at the head
- [ ] tidy, Error Prone, module check: not run this pass (receipt covers them on the head)
- Notes (minor, recorded): extra SchemaRequest round trip per construction; new IOException on schema lookup failure; no docValues check on the unique key (untested).

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-15326-submit.md`.
- Receipt section in `receipts/SOLR-15326.md`, top section.
