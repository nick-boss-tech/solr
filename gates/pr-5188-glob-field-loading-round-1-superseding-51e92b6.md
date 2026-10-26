# Gate: PR 5188 glob RTG field loading and lazy prefetch, superseding round 4 (pinned `51e92b6`)

Status: OPEN
Tags: gate, implementation, review
Intended host: vm6 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5188-glob-field-loading-round-1-vm6.md`.
Supersedes: `gates/pr-5188-glob-field-loading-round-1-superseding-7ac2bc8.md` (head `7ac2bc8`). Repair and recorded decisions: `reports/pr-5188-glob-field-loading-repair-round-4.md`.

## Pinned inputs

- Final head F4: `51e92b63fddb97db83a3962f758bde3c79f1c4dd`.
- Pre-fix tree P: `e07455a0f953e702da27f677c51ea894b7bafc3a` production plus F4's two test blobs.
- Seed `5188C0DE`. Module `:solr:core`.

## Legs

1. tidy at F4 (clean tree after).
2. Pre-fix RTG on P: `testGlobRTGStoredFieldLoading`, expected `unrelated_s should remain lazy`.
3. Pre-fix prefetch on P: `testGlobHighlightPrefetch`, expected `did not prefetch`.
4. Post-fix focused on F4: the three methods, zero skips.
5. Post-fix suites on F4: `BasicFunctionalityTest`, `TestPseudoReturnFields`, `TestRetrieveFieldsOptimizer`, `ReturnFieldsTest`.
6. On F4: tidy, compileJava and compileTestJava with `-Pvalidation.errorprone=true`, `:solr:core:check -x test`.

Green requires legs 2 and 3 to fail on the loading assertions, and legs 4 to 6 to pass on F4. The receipt must state the unified-snippet limitation from the repair report.
