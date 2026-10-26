# PR 5188 glob field loading, superseding gate: failure report (vm6)

Status: FAILED. No green receipt. Gate job: `gates/pr-5188-glob-field-loading-round-1-superseding-b428596.md`. Claim: `claims/pr-5188-glob-field-loading-round-1-vm6.md`. Repair: `reports/pr-5188-glob-field-loading-repair-round-1.md`.

## Pins

- Final head F `b428596720d0ee1119167d04323681252e86136d` (fork, pushed fast-forward from candidate `b71a1687bf98`). Tree `1f5ca309fb`.
- Pre-fix tree P: original production `e07455a0f953e702da27f677c51ea894b7bafc3a` plus F's final test blobs `BasicFunctionalityTest.java` `ef9903c5d7` and `TestPseudoReturnFields.java` `b7569c8f70`.
- JDK 21.0.12.1+1, Linux aarch64, host vm6, build lock `/workspace/tools/.gradle-vm6.lock`. Seed `5188C0DE`, printed as `[5188C0DE:9B63294A879A3AA9]` on the probe run.

## Results

| Leg | rc | What happened |
| --- | --- | --- |
| tidy at F | 0 | Tree clean after tidy (`tidy-tree: 0 changed`) |
| pre-fix RTG on P | 1 | `testGlobRTGStoredFieldLoading` fails at `//doc/str[@name='title'][.='keyword']`. The response has `<arr name="title"><str>keyword</str></arr>` for `fl=tit*,...`. It fails before the lazy-field assertion for `unrelated_s`. |
| pre-fix prefetch on P | 1 | `testGlobHighlightPrefetch` fails with `expected null, but was:<//result/doc/str[@name='source_s'][.='source']>` at the response-XML check (test lines 961 to 965). It does not reach the `did not prefetch` assertion. |
| post-fix focused on F | 1 | 3 tests: RTG fails (same xpath as pre-fix), prefetch fails (same message as pre-fix), `testGlobRTGUncommittedFields` passes. |
| post-fix suites on F | 1 | `BasicFunctionalityTest` 25 tests, 2 failures (the two above). `TestRetrieveFieldsOptimizer` 1/1 pass. `ReturnFieldsTest` 16/16 pass. `TestPseudoReturnFields` 35 tests, 8 failures, all `xpath=//result[@numFound='5']`; the response shows `numFound="6"`. |
| compile on F, Error Prone enabled | 0 | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` |
| check on F | 0 | `:solr:core:check -x test` |

Log directory: `/workspace/gates/logs/vm6/pr5188-round-1-superseding/`, with `status.txt`, each leg's log and `*-xml/` copies.

## What the probe showed

Probe copy of F with diagnostic prints, not pushed. Log directory: `/workspace/gates/logs/vm6/pr5188-round-1-superseding-probe/`.

1. The response-shape failures are not the predicted loading failures. On F, the explicit `fl=source_s` with `hl=true` (original highlighter) returns `<arr name="source_s"><str>source</str></arr>`, not `<str>`. The glob RTG `fl=tit*` returns `<arr name="title">`. The pre-fix tree fails the same xpaths, so this shape does not come from the candidate. It is base behavior, and the gate's prediction (the first failure on `unrelated_s` laziness) did not happen.
2. Source reading: `RealTimeGetComponent.toSolrDoc` (line 929 onward) writes a value as an array when a field name occurs more than once in the Lucene document. The candidate does not change this path. The cause of the duplicate is not yet identified.
3. The 8 `TestPseudoReturnFields` failures: `testGlobRTGStoredFieldLoading` has no `finally` cleanup. When it fails, doc `7781` stays committed. When `TestPseudoReturnFields` runs in the same JVM, `numFound` is 6, not 5. Run alone, `testGlobs` sees 5 docs (ids 42 to 46). This is a strong suspicion, not yet a proof: the suite-level leak has not been reproduced in isolation.

## Open questions for the implementer and owner

- Whether single-valued fields returned as `<arr>` on the glob RTG path and the highlighted `hl=true` path are a defect to fix in production, or whether the test's `str` expectation is wrong. The gate forbids changing expected assertions to match output without evidence.
- Whether `testGlobRTGStoredFieldLoading` should clean up its committed doc `7781` in a `finally` block, as `testGlobHighlightPrefetch` does.

## Status

Not green. The candidate's own regression tests fail at F, and the pre-fix legs fail on the same assertions, so they do not show the fix. No receipt is written. The fork branch has the formatting-only repair commit `b428596`. Nothing is posted to GitHub or JIRA.
