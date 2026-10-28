# Gate: PR 5188 glob RTG field loading and lazy prefetch, superseding round 1 (pinned `b428596`)

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): executed and FAILED at head `b428596` (recorded in `reports/pr-5188-glob-field-loading-gate-failure-round-1.md`), then superseded by the `53ad2cc` job.
Tags: gate, implementation, review
Intended host: vm6 (Linux, JDK 21.0.12.1+1), claim `claims/pr-5188-glob-field-loading-round-1-vm6.md`.
Supersedes: `gates/pr-5188-glob-field-loading-round-1.md` for the post-fix head only, under its repair rule. The pre-fix legs now use the overlay below.

## Pinned inputs

- Fork: `nick-boss-tech/solr:pr-5188-glob-field-loading-followup`.
- Final post-fix head F: `b428596720d0ee1119167d04323681252e86136d` (repair commit on candidate `b71a1687bf98`).
- Pre-fix tree P: original production `e07455a0f953e702da27f677c51ea894b7bafc3a` plus the final test blobs `BasicFunctionalityTest.java` `ef9903c5d7` and `TestPseudoReturnFields.java` `b7569c8f70`. Built as an archive, not a checkout of F, so the test files in P are exactly F's.
- Module `:solr:core`; seed `5188C0DE` for every leg; record the printed test seed.

## Legs (same commands as the original gate)

1. Preflight at F: `./gradlew --no-daemon --max-workers=1 tidy` must leave the tree clean (guard).
2. Pre-fix RTG on P: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading -Ptests.seed=5188C0DE`. Expected to fail on the lazy-loading assertion for `unrelated_s`, not on response shape.
3. Pre-fix prefetch on P: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch -Ptests.seed=5188C0DE`. Expected to fail with `source_* did not prefetch source_s` on the glob case, with the finite highlighter control passing.
4. Post-fix focused on F: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=5188C0DE`. All three pass, zero skips.
5. Post-fix suites on F: `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest -Ptests.seed=5188C0DE`. Record XML counts by class.
6. On F: `tidy`, `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`, `:solr:core:check -x test`. Record each exit.

Each leg: archived log, UTC start and end, exit code, fresh XML (cleanTest first). Skips, compile errors and setup failures are not expected-regression proof. Green requires legs 2 and 3 to fail on the relevant loading assertions and legs 4 to 6 to pass on F. The receipt is written only then.

Runner: detached, under `/workspace/tools/.gradle-vm6.lock`, absolute `-Ptests.workDir` under `/workspace/tmp`. Logs under `/workspace/gates/logs/vm6/pr5188-round-1-superseding/`.
