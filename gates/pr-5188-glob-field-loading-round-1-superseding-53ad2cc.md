# Gate: PR 5188 glob RTG field loading and lazy prefetch, superseding round 2 (pinned `53ad2cc`)

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): aborted before the post-fix legs and superseded by the `7ac2bc8` job, per its own gate file.
Tags: gate, implementation, review
Intended host: vm6 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5188-glob-field-loading-round-1-vm6.md`.
Supersedes: `gates/pr-5188-glob-field-loading-round-1-superseding-b428596.md` for head `b428596`, which failed (`reports/pr-5188-glob-field-loading-gate-failure-round-1.md`). Repair: `reports/pr-5188-glob-field-loading-repair-round-2.md`.

## Pinned inputs

- Final head F2: `53ad2ccacbf205333f9029ed9f6ebbc93da034f2` (tree `aec5272c76`). Test blobs `BasicFunctionalityTest.java` `aebc9f6e52`, `TestPseudoReturnFields.java` `5e8c15fc4f`.
- Pre-fix tree P: original production `e07455a0f953e702da27f677c51ea894b7bafc3a` plus F2's two test blobs above. The three production files are identical to `e07455a` (checked).
- Seed `5188C0DE`. Module `:solr:core`.

## Legs (same as the superseding round 1)

1. tidy at F2 in a clean checkout, tree must stay clean.
2. Pre-fix RTG on P: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading -Ptests.seed=5188C0DE`. Expected to fail on `assertUnloadedLazyField(doc, "unrelated_s")`.
3. Pre-fix prefetch on P: `--tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch`. Expected to fail with `source_* did not prefetch source_s` or an equivalent prefetch assertion.
4. Post-fix focused on F2: the three methods (`testGlobRTGStoredFieldLoading`, `testGlobHighlightPrefetch`, `TestPseudoReturnFields.testGlobRTGUncommittedFields`). All pass, zero skips.
5. Post-fix suites on F2: `BasicFunctionalityTest`, `TestPseudoReturnFields`, `TestRetrieveFieldsOptimizer`, `ReturnFieldsTest`. Record XML counts by class.
6. On F2: tidy, `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`, `:solr:core:check -x test`.

Green requires legs 2 and 3 to fail on the loading assertions and legs 4 to 6 to pass on F2. Logs: `/workspace/gates/logs/vm6/pr5188-round-1-superseding-2/`.
