# Gate job: SOLR-18182 review pass 1 (Deprecate ByteBuffersDirectoryFactory)

Status: DONE (GATE GREEN at 0abcca2f4b, vm4, 2026-10-22T18:38:26Z)

- Branch: solr-18182-deprecate-bytebuffers. Head: `0abcca2f4b99b92136e5a5650fc7262bbe24ab17` (ls-remote 2026-10-22T18:04:48Z, from the claim). Prior gated head: `0abcca2f4b` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.core.ByteBuffersDirectoryFactoryTest`. Seed: `18182C0FFEE18182`.
- Why: pass 1 of the review round for unit 36.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`changelog/unreleased/SOLR-18182-deprecate-bytebuffers-directory-factory.yml`)
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (18:23:39)
- [x] step 3 proof leg (base `ByteBuffersDirectoryFactory.java` in worktree `proof-18182`, test at head): rc=1, expected failure `testDeprecated` (the annotation is absent on base). Tests 5, failed 1.
- [x] step 4 focused head run, from fresh XML: ByteBuffersDirectoryFactoryTest tests=5, skipped=0, failures=0, errors=0 (ts 2026-10-22T18:34:46Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (18:38:26).

Logs: `/workspace/gates/logs/vm4/18182/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18182-deprecate-bytebuffers.md`.
- Receipt section in `receipts/SOLR-18182.md`, top section.
