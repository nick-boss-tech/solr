# Gate: PR 5188 glob RTG field loading and lazy prefetch, superseding round 3 (pinned `7ac2bc8`)

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): superseded by the `51e92b6` job, which executed; see `receipts/PR-5188-glob-field-loading-round-1.md`.
Tags: gate, implementation, review
Intended host: vm6 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5188-glob-field-loading-round-1-vm6.md`.
Supersedes: `gates/pr-5188-glob-field-loading-round-1-superseding-53ad2cc.md` (head `53ad2cc`, aborted before the post-fix legs). Repair: `reports/pr-5188-glob-field-loading-repair-round-3.md`.

## Pinned inputs

- Final head F3: `7ac2bc86fed51ed420f3b1a9559e167631e5a3c8`.
- Pre-fix tree P: original production `e07455a0f953e702da27f677c51ea894b7bafc3a` plus F3's two test blobs. Production files identical to `e07455a`.
- Seed `5188C0DE`. Module `:solr:core`.

## Legs

1. tidy at F3 in a clean checkout, tree must stay clean.
2. Pre-fix RTG on P: `testGlobRTGStoredFieldLoading`. Expected to fail on `unrelated_s should remain lazy`.
3. Pre-fix prefetch on P: `testGlobHighlightPrefetch`. Expected to fail with a `did not prefetch` assertion.
4. Post-fix focused on F3: `testGlobRTGStoredFieldLoading`, `testGlobHighlightPrefetch`, `TestPseudoReturnFields.testGlobRTGUncommittedFields`. All pass, zero skips.
5. Post-fix suites on F3: `BasicFunctionalityTest`, `TestPseudoReturnFields`, `TestRetrieveFieldsOptimizer`, `ReturnFieldsTest`. Record XML counts by class.
6. On F3: tidy, compileJava and compileTestJava with `-Pvalidation.errorprone=true`, `:solr:core:check -x test`.

Green requires legs 2 and 3 to fail on the loading assertions and legs 4 to 6 to pass on F3. Logs: `/workspace/gates/logs/vm6/pr5188-round-1-superseding-3/`.
