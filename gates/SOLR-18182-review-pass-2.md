# Gate job: SOLR-18182 review pass 2 (Deprecate ByteBuffersDirectoryFactory)

Status: DONE (GATE GREEN at 0abcca2f4b, vm4, 2026-10-22T18:56:25Z)

- Branch: solr-18182-deprecate-bytebuffers. Head: `0abcca2f4b99b92136e5a5650fc7262bbe24ab17` (unchanged since pass 1). Prior gated head: `0abcca2f4b` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.core.ByteBuffersDirectoryFactoryTest`. Seed: `18182C0FFEE18182`.
- Why: pass 2 of the review round for unit 36, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (18:46:50)
- [x] step 3 proof leg (base `ByteBuffersDirectoryFactory.java`): rc=1. Expected failure `testDeprecated`, same as pass 1.
- [x] step 4 focused head run, from fresh XML: ByteBuffersDirectoryFactoryTest tests=5, skipped=0, failures=0, errors=0 (ts 2026-10-22T18:54:17Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (18:56:25).

Logs: `/workspace/gates/logs/vm4/18182/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-18182-deprecate-bytebuffers.md`.
- Receipt section in `receipts/SOLR-18182.md`, top section.
